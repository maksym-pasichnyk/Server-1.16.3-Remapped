/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PromiseNotificationUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*     */ public final class ChannelOutboundBuffer
/*     */ {
/*  61 */   static final int CHANNEL_OUTBOUND_BUFFER_ENTRY_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.outboundBufferEntrySizeOverhead", 96);
/*     */   
/*  63 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
/*     */   
/*  65 */   private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS = new FastThreadLocal<ByteBuffer[]>()
/*     */     {
/*     */       protected ByteBuffer[] initialValue() throws Exception {
/*  68 */         return new ByteBuffer[1024];
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Channel channel;
/*     */ 
/*     */   
/*     */   private Entry flushedEntry;
/*     */   
/*     */   private Entry unflushedEntry;
/*     */   
/*     */   private Entry tailEntry;
/*     */   
/*     */   private int flushed;
/*     */   
/*     */   private int nioBufferCount;
/*     */   
/*     */   private long nioBufferSize;
/*     */   
/*     */   private boolean inFail;
/*     */   
/*  91 */   private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
/*     */ 
/*     */   
/*     */   private volatile long totalPendingSize;
/*     */ 
/*     */   
/*  97 */   private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> UNWRITABLE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "unwritable");
/*     */   
/*     */   private volatile int unwritable;
/*     */   
/*     */   private volatile Runnable fireChannelWritabilityChangedTask;
/*     */ 
/*     */   
/*     */   ChannelOutboundBuffer(AbstractChannel channel) {
/* 105 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessage(Object msg, int size, ChannelPromise promise) {
/* 113 */     Entry entry = Entry.newInstance(msg, size, total(msg), promise);
/* 114 */     if (this.tailEntry == null) {
/* 115 */       this.flushedEntry = null;
/*     */     } else {
/* 117 */       Entry tail = this.tailEntry;
/* 118 */       tail.next = entry;
/*     */     } 
/* 120 */     this.tailEntry = entry;
/* 121 */     if (this.unflushedEntry == null) {
/* 122 */       this.unflushedEntry = entry;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 127 */     incrementPendingOutboundBytes(entry.pendingSize, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFlush() {
/* 139 */     Entry entry = this.unflushedEntry;
/* 140 */     if (entry != null) {
/* 141 */       if (this.flushedEntry == null)
/*     */       {
/* 143 */         this.flushedEntry = entry;
/*     */       }
/*     */       do {
/* 146 */         this.flushed++;
/* 147 */         if (!entry.promise.setUncancellable()) {
/*     */           
/* 149 */           int pending = entry.cancel();
/* 150 */           decrementPendingOutboundBytes(pending, false, true);
/*     */         } 
/* 152 */         entry = entry.next;
/* 153 */       } while (entry != null);
/*     */ 
/*     */       
/* 156 */       this.unflushedEntry = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void incrementPendingOutboundBytes(long size) {
/* 165 */     incrementPendingOutboundBytes(size, true);
/*     */   }
/*     */   
/*     */   private void incrementPendingOutboundBytes(long size, boolean invokeLater) {
/* 169 */     if (size == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 173 */     long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
/* 174 */     if (newWriteBufferSize > this.channel.config().getWriteBufferHighWaterMark()) {
/* 175 */       setUnwritable(invokeLater);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void decrementPendingOutboundBytes(long size) {
/* 184 */     decrementPendingOutboundBytes(size, true, true);
/*     */   }
/*     */   
/*     */   private void decrementPendingOutboundBytes(long size, boolean invokeLater, boolean notifyWritability) {
/* 188 */     if (size == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 192 */     long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
/* 193 */     if (notifyWritability && newWriteBufferSize < this.channel.config().getWriteBufferLowWaterMark()) {
/* 194 */       setWritable(invokeLater);
/*     */     }
/*     */   }
/*     */   
/*     */   private static long total(Object msg) {
/* 199 */     if (msg instanceof ByteBuf) {
/* 200 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 202 */     if (msg instanceof FileRegion) {
/* 203 */       return ((FileRegion)msg).count();
/*     */     }
/* 205 */     if (msg instanceof ByteBufHolder) {
/* 206 */       return ((ByteBufHolder)msg).content().readableBytes();
/*     */     }
/* 208 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object current() {
/* 215 */     Entry entry = this.flushedEntry;
/* 216 */     if (entry == null) {
/* 217 */       return null;
/*     */     }
/*     */     
/* 220 */     return entry.msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void progress(long amount) {
/* 227 */     Entry e = this.flushedEntry;
/* 228 */     assert e != null;
/* 229 */     ChannelPromise p = e.promise;
/* 230 */     if (p instanceof ChannelProgressivePromise) {
/* 231 */       long progress = e.progress + amount;
/* 232 */       e.progress = progress;
/* 233 */       ((ChannelProgressivePromise)p).tryProgress(progress, e.total);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove() {
/* 243 */     Entry e = this.flushedEntry;
/* 244 */     if (e == null) {
/* 245 */       clearNioBuffers();
/* 246 */       return false;
/*     */     } 
/* 248 */     Object msg = e.msg;
/*     */     
/* 250 */     ChannelPromise promise = e.promise;
/* 251 */     int size = e.pendingSize;
/*     */     
/* 253 */     removeEntry(e);
/*     */     
/* 255 */     if (!e.cancelled) {
/*     */       
/* 257 */       ReferenceCountUtil.safeRelease(msg);
/* 258 */       safeSuccess(promise);
/* 259 */       decrementPendingOutboundBytes(size, false, true);
/*     */     } 
/*     */ 
/*     */     
/* 263 */     e.recycle();
/*     */     
/* 265 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Throwable cause) {
/* 274 */     return remove0(cause, true);
/*     */   }
/*     */   
/*     */   private boolean remove0(Throwable cause, boolean notifyWritability) {
/* 278 */     Entry e = this.flushedEntry;
/* 279 */     if (e == null) {
/* 280 */       clearNioBuffers();
/* 281 */       return false;
/*     */     } 
/* 283 */     Object msg = e.msg;
/*     */     
/* 285 */     ChannelPromise promise = e.promise;
/* 286 */     int size = e.pendingSize;
/*     */     
/* 288 */     removeEntry(e);
/*     */     
/* 290 */     if (!e.cancelled) {
/*     */       
/* 292 */       ReferenceCountUtil.safeRelease(msg);
/*     */       
/* 294 */       safeFail(promise, cause);
/* 295 */       decrementPendingOutboundBytes(size, false, notifyWritability);
/*     */     } 
/*     */ 
/*     */     
/* 299 */     e.recycle();
/*     */     
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   private void removeEntry(Entry e) {
/* 305 */     if (--this.flushed == 0) {
/*     */       
/* 307 */       this.flushedEntry = null;
/* 308 */       if (e == this.tailEntry) {
/* 309 */         this.tailEntry = null;
/* 310 */         this.unflushedEntry = null;
/*     */       } 
/*     */     } else {
/* 313 */       this.flushedEntry = e.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBytes(long writtenBytes) {
/*     */     while (true) {
/* 323 */       Object msg = current();
/* 324 */       if (!(msg instanceof ByteBuf)) {
/* 325 */         assert writtenBytes == 0L;
/*     */         
/*     */         break;
/*     */       } 
/* 329 */       ByteBuf buf = (ByteBuf)msg;
/* 330 */       int readerIndex = buf.readerIndex();
/* 331 */       int readableBytes = buf.writerIndex() - readerIndex;
/*     */       
/* 333 */       if (readableBytes <= writtenBytes) {
/* 334 */         if (writtenBytes != 0L) {
/* 335 */           progress(readableBytes);
/* 336 */           writtenBytes -= readableBytes;
/*     */         } 
/* 338 */         remove(); continue;
/*     */       } 
/* 340 */       if (writtenBytes != 0L) {
/* 341 */         buf.readerIndex(readerIndex + (int)writtenBytes);
/* 342 */         progress(writtenBytes);
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 347 */     clearNioBuffers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearNioBuffers() {
/* 353 */     int count = this.nioBufferCount;
/* 354 */     if (count > 0) {
/* 355 */       this.nioBufferCount = 0;
/* 356 */       Arrays.fill((Object[])NIO_BUFFERS.get(), 0, count, (Object)null);
/*     */     } 
/*     */   }
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
/*     */   public ByteBuffer[] nioBuffers() {
/* 371 */     return nioBuffers(2147483647, 2147483647L);
/*     */   }
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
/*     */   public ByteBuffer[] nioBuffers(int maxCount, long maxBytes) {
/* 389 */     assert maxCount > 0;
/* 390 */     assert maxBytes > 0L;
/* 391 */     long nioBufferSize = 0L;
/* 392 */     int nioBufferCount = 0;
/* 393 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 394 */     ByteBuffer[] nioBuffers = (ByteBuffer[])NIO_BUFFERS.get(threadLocalMap);
/* 395 */     Entry entry = this.flushedEntry;
/* 396 */     while (isFlushedEntry(entry) && entry.msg instanceof ByteBuf) {
/* 397 */       if (!entry.cancelled) {
/* 398 */         ByteBuf buf = (ByteBuf)entry.msg;
/* 399 */         int readerIndex = buf.readerIndex();
/* 400 */         int readableBytes = buf.writerIndex() - readerIndex;
/*     */         
/* 402 */         if (readableBytes > 0) {
/* 403 */           if (maxBytes - readableBytes < nioBufferSize && nioBufferCount != 0) {
/*     */             break;
/*     */           }
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
/* 417 */           nioBufferSize += readableBytes;
/* 418 */           int count = entry.count;
/* 419 */           if (count == -1)
/*     */           {
/* 421 */             entry.count = count = buf.nioBufferCount();
/*     */           }
/* 423 */           int neededSpace = Math.min(maxCount, nioBufferCount + count);
/* 424 */           if (neededSpace > nioBuffers.length) {
/* 425 */             nioBuffers = expandNioBufferArray(nioBuffers, neededSpace, nioBufferCount);
/* 426 */             NIO_BUFFERS.set(threadLocalMap, nioBuffers);
/*     */           } 
/* 428 */           if (count == 1) {
/* 429 */             ByteBuffer nioBuf = entry.buf;
/* 430 */             if (nioBuf == null)
/*     */             {
/*     */               
/* 433 */               entry.buf = nioBuf = buf.internalNioBuffer(readerIndex, readableBytes);
/*     */             }
/* 435 */             nioBuffers[nioBufferCount++] = nioBuf;
/*     */           } else {
/* 437 */             ByteBuffer[] nioBufs = entry.bufs;
/* 438 */             if (nioBufs == null)
/*     */             {
/*     */               
/* 441 */               entry.bufs = nioBufs = buf.nioBuffers();
/*     */             }
/* 443 */             for (int i = 0; i < nioBufs.length && nioBufferCount < maxCount; i++) {
/* 444 */               ByteBuffer nioBuf = nioBufs[i];
/* 445 */               if (nioBuf == null)
/*     */                 break; 
/* 447 */               if (nioBuf.hasRemaining())
/*     */               {
/*     */                 
/* 450 */                 nioBuffers[nioBufferCount++] = nioBuf; } 
/*     */             } 
/*     */           } 
/* 453 */           if (nioBufferCount == maxCount) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/* 458 */       entry = entry.next;
/*     */     } 
/* 460 */     this.nioBufferCount = nioBufferCount;
/* 461 */     this.nioBufferSize = nioBufferSize;
/*     */     
/* 463 */     return nioBuffers;
/*     */   }
/*     */   
/*     */   private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] array, int neededSpace, int size) {
/* 467 */     int newCapacity = array.length;
/*     */ 
/*     */     
/*     */     do {
/* 471 */       newCapacity <<= 1;
/*     */       
/* 473 */       if (newCapacity < 0) {
/* 474 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/* 477 */     while (neededSpace > newCapacity);
/*     */     
/* 479 */     ByteBuffer[] newArray = new ByteBuffer[newCapacity];
/* 480 */     System.arraycopy(array, 0, newArray, 0, size);
/*     */     
/* 482 */     return newArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 491 */     return this.nioBufferCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long nioBufferSize() {
/* 500 */     return this.nioBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 510 */     return (this.unwritable == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUserDefinedWritability(int index) {
/* 518 */     return ((this.unwritable & writabilityMask(index)) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserDefinedWritability(int index, boolean writable) {
/* 525 */     if (writable) {
/* 526 */       setUserDefinedWritability(index);
/*     */     } else {
/* 528 */       clearUserDefinedWritability(index);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setUserDefinedWritability(int index) {
/* 533 */     int mask = writabilityMask(index) ^ 0xFFFFFFFF;
/*     */     while (true) {
/* 535 */       int oldValue = this.unwritable;
/* 536 */       int newValue = oldValue & mask;
/* 537 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 538 */         if (oldValue != 0 && newValue == 0) {
/* 539 */           fireChannelWritabilityChanged(true);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clearUserDefinedWritability(int index) {
/* 547 */     int mask = writabilityMask(index);
/*     */     while (true) {
/* 549 */       int oldValue = this.unwritable;
/* 550 */       int newValue = oldValue | mask;
/* 551 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 552 */         if (oldValue == 0 && newValue != 0) {
/* 553 */           fireChannelWritabilityChanged(true);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int writabilityMask(int index) {
/* 561 */     if (index < 1 || index > 31) {
/* 562 */       throw new IllegalArgumentException("index: " + index + " (expected: 1~31)");
/*     */     }
/* 564 */     return 1 << index;
/*     */   }
/*     */   
/*     */   private void setWritable(boolean invokeLater) {
/*     */     while (true) {
/* 569 */       int oldValue = this.unwritable;
/* 570 */       int newValue = oldValue & 0xFFFFFFFE;
/* 571 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 572 */         if (oldValue != 0 && newValue == 0) {
/* 573 */           fireChannelWritabilityChanged(invokeLater);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setUnwritable(boolean invokeLater) {
/*     */     while (true) {
/* 582 */       int oldValue = this.unwritable;
/* 583 */       int newValue = oldValue | 0x1;
/* 584 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 585 */         if (oldValue == 0 && newValue != 0) {
/* 586 */           fireChannelWritabilityChanged(invokeLater);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fireChannelWritabilityChanged(boolean invokeLater) {
/* 594 */     final ChannelPipeline pipeline = this.channel.pipeline();
/* 595 */     if (invokeLater) {
/* 596 */       Runnable task = this.fireChannelWritabilityChangedTask;
/* 597 */       if (task == null) {
/* 598 */         this.fireChannelWritabilityChangedTask = task = new Runnable()
/*     */           {
/*     */             public void run() {
/* 601 */               pipeline.fireChannelWritabilityChanged();
/*     */             }
/*     */           };
/*     */       }
/* 605 */       this.channel.eventLoop().execute(task);
/*     */     } else {
/* 607 */       pipeline.fireChannelWritabilityChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 615 */     return this.flushed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 623 */     return (this.flushed == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void failFlushed(Throwable cause, boolean notify) {
/* 632 */     if (this.inFail) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 637 */       this.inFail = true; do {
/*     */       
/* 639 */       } while (remove0(cause, notify));
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 644 */       this.inFail = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   void close(final Throwable cause, final boolean allowChannelOpen) {
/* 649 */     if (this.inFail) {
/* 650 */       this.channel.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 653 */               ChannelOutboundBuffer.this.close(cause, allowChannelOpen);
/*     */             }
/*     */           });
/*     */       
/*     */       return;
/*     */     } 
/* 659 */     this.inFail = true;
/*     */     
/* 661 */     if (!allowChannelOpen && this.channel.isOpen()) {
/* 662 */       throw new IllegalStateException("close() must be invoked after the channel is closed.");
/*     */     }
/*     */     
/* 665 */     if (!isEmpty()) {
/* 666 */       throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 671 */       Entry e = this.unflushedEntry;
/* 672 */       while (e != null) {
/*     */         
/* 674 */         int size = e.pendingSize;
/* 675 */         TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
/*     */         
/* 677 */         if (!e.cancelled) {
/* 678 */           ReferenceCountUtil.safeRelease(e.msg);
/* 679 */           safeFail(e.promise, cause);
/*     */         } 
/* 681 */         e = e.recycleAndGetNext();
/*     */       } 
/*     */     } finally {
/* 684 */       this.inFail = false;
/*     */     } 
/* 686 */     clearNioBuffers();
/*     */   }
/*     */   
/*     */   void close(ClosedChannelException cause) {
/* 690 */     close(cause, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void safeSuccess(ChannelPromise promise) {
/* 696 */     PromiseNotificationUtil.trySuccess(promise, null, (promise instanceof VoidChannelPromise) ? null : logger);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void safeFail(ChannelPromise promise, Throwable cause) {
/* 702 */     PromiseNotificationUtil.tryFailure(promise, cause, (promise instanceof VoidChannelPromise) ? null : logger);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void recycle() {}
/*     */ 
/*     */   
/*     */   public long totalPendingWriteBytes() {
/* 711 */     return this.totalPendingSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long bytesBeforeUnwritable() {
/* 719 */     long bytes = this.channel.config().getWriteBufferHighWaterMark() - this.totalPendingSize;
/*     */ 
/*     */ 
/*     */     
/* 723 */     if (bytes > 0L) {
/* 724 */       return isWritable() ? bytes : 0L;
/*     */     }
/* 726 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long bytesBeforeWritable() {
/* 734 */     long bytes = this.totalPendingSize - this.channel.config().getWriteBufferLowWaterMark();
/*     */ 
/*     */ 
/*     */     
/* 738 */     if (bytes > 0L) {
/* 739 */       return isWritable() ? 0L : bytes;
/*     */     }
/* 741 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEachFlushedMessage(MessageProcessor processor) throws Exception {
/* 750 */     if (processor == null) {
/* 751 */       throw new NullPointerException("processor");
/*     */     }
/*     */     
/* 754 */     Entry entry = this.flushedEntry;
/* 755 */     if (entry == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     do {
/* 760 */       if (!entry.cancelled && 
/* 761 */         !processor.processMessage(entry.msg)) {
/*     */         return;
/*     */       }
/*     */       
/* 765 */       entry = entry.next;
/* 766 */     } while (isFlushedEntry(entry));
/*     */   }
/*     */   
/*     */   private boolean isFlushedEntry(Entry e) {
/* 770 */     return (e != null && e != this.unflushedEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface MessageProcessor
/*     */   {
/*     */     boolean processMessage(Object param1Object) throws Exception;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Entry
/*     */   {
/* 782 */     private static final Recycler<Entry> RECYCLER = new Recycler<Entry>()
/*     */       {
/*     */         protected ChannelOutboundBuffer.Entry newObject(Recycler.Handle<ChannelOutboundBuffer.Entry> handle) {
/* 785 */           return new ChannelOutboundBuffer.Entry(handle);
/*     */         }
/*     */       };
/*     */     
/*     */     private final Recycler.Handle<Entry> handle;
/*     */     Entry next;
/*     */     Object msg;
/*     */     ByteBuffer[] bufs;
/*     */     ByteBuffer buf;
/*     */     ChannelPromise promise;
/*     */     long progress;
/*     */     long total;
/*     */     int pendingSize;
/* 798 */     int count = -1;
/*     */     boolean cancelled;
/*     */     
/*     */     private Entry(Recycler.Handle<Entry> handle) {
/* 802 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     static Entry newInstance(Object msg, int size, long total, ChannelPromise promise) {
/* 806 */       Entry entry = (Entry)RECYCLER.get();
/* 807 */       entry.msg = msg;
/* 808 */       entry.pendingSize = size + ChannelOutboundBuffer.CHANNEL_OUTBOUND_BUFFER_ENTRY_OVERHEAD;
/* 809 */       entry.total = total;
/* 810 */       entry.promise = promise;
/* 811 */       return entry;
/*     */     }
/*     */     
/*     */     int cancel() {
/* 815 */       if (!this.cancelled) {
/* 816 */         this.cancelled = true;
/* 817 */         int pSize = this.pendingSize;
/*     */ 
/*     */         
/* 820 */         ReferenceCountUtil.safeRelease(this.msg);
/* 821 */         this.msg = Unpooled.EMPTY_BUFFER;
/*     */         
/* 823 */         this.pendingSize = 0;
/* 824 */         this.total = 0L;
/* 825 */         this.progress = 0L;
/* 826 */         this.bufs = null;
/* 827 */         this.buf = null;
/* 828 */         return pSize;
/*     */       } 
/* 830 */       return 0;
/*     */     }
/*     */     
/*     */     void recycle() {
/* 834 */       this.next = null;
/* 835 */       this.bufs = null;
/* 836 */       this.buf = null;
/* 837 */       this.msg = null;
/* 838 */       this.promise = null;
/* 839 */       this.progress = 0L;
/* 840 */       this.total = 0L;
/* 841 */       this.pendingSize = 0;
/* 842 */       this.count = -1;
/* 843 */       this.cancelled = false;
/* 844 */       this.handle.recycle(this);
/*     */     }
/*     */     
/*     */     Entry recycleAndGetNext() {
/* 848 */       Entry next = this.next;
/* 849 */       recycle();
/* 850 */       return next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelOutboundBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */