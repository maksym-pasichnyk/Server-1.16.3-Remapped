/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.PromiseCombiner;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PendingWriteQueue
/*     */ {
/*  32 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final int PENDING_WRITE_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.pendingWriteSizeOverhead", 64);
/*     */   
/*     */   private final ChannelHandlerContext ctx;
/*     */   
/*     */   private final PendingBytesTracker tracker;
/*     */   
/*     */   private PendingWrite head;
/*     */   private PendingWrite tail;
/*     */   private int size;
/*     */   private long bytes;
/*     */   
/*     */   public PendingWriteQueue(ChannelHandlerContext ctx) {
/*  50 */     this.tracker = PendingBytesTracker.newTracker(ctx.channel());
/*  51 */     this.ctx = ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  58 */     assert this.ctx.executor().inEventLoop();
/*  59 */     return (this.head == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  66 */     assert this.ctx.executor().inEventLoop();
/*  67 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long bytes() {
/*  75 */     assert this.ctx.executor().inEventLoop();
/*  76 */     return this.bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int size(Object msg) {
/*  82 */     int messageSize = this.tracker.size(msg);
/*  83 */     if (messageSize < 0)
/*     */     {
/*  85 */       messageSize = 0;
/*     */     }
/*  87 */     return messageSize + PENDING_WRITE_OVERHEAD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Object msg, ChannelPromise promise) {
/*  94 */     assert this.ctx.executor().inEventLoop();
/*  95 */     if (msg == null) {
/*  96 */       throw new NullPointerException("msg");
/*     */     }
/*  98 */     if (promise == null) {
/*  99 */       throw new NullPointerException("promise");
/*     */     }
/*     */ 
/*     */     
/* 103 */     int messageSize = size(msg);
/*     */     
/* 105 */     PendingWrite write = PendingWrite.newInstance(msg, messageSize, promise);
/* 106 */     PendingWrite currentTail = this.tail;
/* 107 */     if (currentTail == null) {
/* 108 */       this.tail = this.head = write;
/*     */     } else {
/* 110 */       currentTail.next = write;
/* 111 */       this.tail = write;
/*     */     } 
/* 113 */     this.size++;
/* 114 */     this.bytes += messageSize;
/* 115 */     this.tracker.incrementPendingOutboundBytes(write.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture removeAndWriteAll() {
/* 126 */     assert this.ctx.executor().inEventLoop();
/*     */     
/* 128 */     if (isEmpty()) {
/* 129 */       return null;
/*     */     }
/*     */     
/* 132 */     ChannelPromise p = this.ctx.newPromise();
/* 133 */     PromiseCombiner combiner = new PromiseCombiner();
/*     */ 
/*     */     
/*     */     try {
/* 137 */       for (PendingWrite write = this.head; write != null; write = this.head) {
/* 138 */         this.head = this.tail = null;
/* 139 */         this.size = 0;
/* 140 */         this.bytes = 0L;
/*     */         
/* 142 */         while (write != null) {
/* 143 */           PendingWrite next = write.next;
/* 144 */           Object msg = write.msg;
/* 145 */           ChannelPromise promise = write.promise;
/* 146 */           recycle(write, false);
/* 147 */           if (!(promise instanceof VoidChannelPromise)) {
/* 148 */             combiner.add(promise);
/*     */           }
/* 150 */           this.ctx.write(msg, promise);
/* 151 */           write = next;
/*     */         } 
/*     */       } 
/* 154 */       combiner.finish(p);
/* 155 */     } catch (Throwable cause) {
/* 156 */       p.setFailure(cause);
/*     */     } 
/* 158 */     assertEmpty();
/* 159 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAndFailAll(Throwable cause) {
/* 167 */     assert this.ctx.executor().inEventLoop();
/* 168 */     if (cause == null) {
/* 169 */       throw new NullPointerException("cause");
/*     */     }
/*     */ 
/*     */     
/* 173 */     for (PendingWrite write = this.head; write != null; write = this.head) {
/* 174 */       this.head = this.tail = null;
/* 175 */       this.size = 0;
/* 176 */       this.bytes = 0L;
/* 177 */       while (write != null) {
/* 178 */         PendingWrite next = write.next;
/* 179 */         ReferenceCountUtil.safeRelease(write.msg);
/* 180 */         ChannelPromise promise = write.promise;
/* 181 */         recycle(write, false);
/* 182 */         safeFail(promise, cause);
/* 183 */         write = next;
/*     */       } 
/*     */     } 
/* 186 */     assertEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAndFail(Throwable cause) {
/* 194 */     assert this.ctx.executor().inEventLoop();
/* 195 */     if (cause == null) {
/* 196 */       throw new NullPointerException("cause");
/*     */     }
/* 198 */     PendingWrite write = this.head;
/*     */     
/* 200 */     if (write == null) {
/*     */       return;
/*     */     }
/* 203 */     ReferenceCountUtil.safeRelease(write.msg);
/* 204 */     ChannelPromise promise = write.promise;
/* 205 */     safeFail(promise, cause);
/* 206 */     recycle(write, true);
/*     */   }
/*     */   
/*     */   private void assertEmpty() {
/* 210 */     assert this.tail == null && this.head == null && this.size == 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture removeAndWrite() {
/* 221 */     assert this.ctx.executor().inEventLoop();
/* 222 */     PendingWrite write = this.head;
/* 223 */     if (write == null) {
/* 224 */       return null;
/*     */     }
/* 226 */     Object msg = write.msg;
/* 227 */     ChannelPromise promise = write.promise;
/* 228 */     recycle(write, true);
/* 229 */     return this.ctx.write(msg, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelPromise remove() {
/* 239 */     assert this.ctx.executor().inEventLoop();
/* 240 */     PendingWrite write = this.head;
/* 241 */     if (write == null) {
/* 242 */       return null;
/*     */     }
/* 244 */     ChannelPromise promise = write.promise;
/* 245 */     ReferenceCountUtil.safeRelease(write.msg);
/* 246 */     recycle(write, true);
/* 247 */     return promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object current() {
/* 254 */     assert this.ctx.executor().inEventLoop();
/* 255 */     PendingWrite write = this.head;
/* 256 */     if (write == null) {
/* 257 */       return null;
/*     */     }
/* 259 */     return write.msg;
/*     */   }
/*     */   
/*     */   private void recycle(PendingWrite write, boolean update) {
/* 263 */     PendingWrite next = write.next;
/* 264 */     long writeSize = write.size;
/*     */     
/* 266 */     if (update) {
/* 267 */       if (next == null) {
/*     */ 
/*     */         
/* 270 */         this.head = this.tail = null;
/* 271 */         this.size = 0;
/* 272 */         this.bytes = 0L;
/*     */       } else {
/* 274 */         this.head = next;
/* 275 */         this.size--;
/* 276 */         this.bytes -= writeSize;
/* 277 */         assert this.size > 0 && this.bytes >= 0L;
/*     */       } 
/*     */     }
/*     */     
/* 281 */     write.recycle();
/* 282 */     this.tracker.decrementPendingOutboundBytes(writeSize);
/*     */   }
/*     */   
/*     */   private static void safeFail(ChannelPromise promise, Throwable cause) {
/* 286 */     if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
/* 287 */       logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PendingWrite
/*     */   {
/* 295 */     private static final Recycler<PendingWrite> RECYCLER = new Recycler<PendingWrite>()
/*     */       {
/*     */         protected PendingWriteQueue.PendingWrite newObject(Recycler.Handle<PendingWriteQueue.PendingWrite> handle) {
/* 298 */           return new PendingWriteQueue.PendingWrite(handle);
/*     */         }
/*     */       };
/*     */     
/*     */     private final Recycler.Handle<PendingWrite> handle;
/*     */     private PendingWrite next;
/*     */     private long size;
/*     */     private ChannelPromise promise;
/*     */     private Object msg;
/*     */     
/*     */     private PendingWrite(Recycler.Handle<PendingWrite> handle) {
/* 309 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     static PendingWrite newInstance(Object msg, int size, ChannelPromise promise) {
/* 313 */       PendingWrite write = (PendingWrite)RECYCLER.get();
/* 314 */       write.size = size;
/* 315 */       write.msg = msg;
/* 316 */       write.promise = promise;
/* 317 */       return write;
/*     */     }
/*     */     
/*     */     private void recycle() {
/* 321 */       this.size = 0L;
/* 322 */       this.next = null;
/* 323 */       this.msg = null;
/* 324 */       this.promise = null;
/* 325 */       this.handle.recycle(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\PendingWriteQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */