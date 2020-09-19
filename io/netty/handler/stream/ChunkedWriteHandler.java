/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelProgressivePromise;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedWriteHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  71 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
/*     */   
/*  73 */   private final Queue<PendingWrite> queue = new ArrayDeque<PendingWrite>();
/*     */ 
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */ 
/*     */   
/*     */   private PendingWrite currentWrite;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ChunkedWriteHandler(int maxPendingWrites) {
/*  85 */     if (maxPendingWrites <= 0) {
/*  86 */       throw new IllegalArgumentException("maxPendingWrites: " + maxPendingWrites + " (expected: > 0)");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/*  93 */     this.ctx = ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resumeTransfer() {
/* 100 */     final ChannelHandlerContext ctx = this.ctx;
/* 101 */     if (ctx == null) {
/*     */       return;
/*     */     }
/* 104 */     if (ctx.executor().inEventLoop()) {
/* 105 */       resumeTransfer0(ctx);
/*     */     } else {
/*     */       
/* 108 */       ctx.executor().execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 112 */               ChunkedWriteHandler.this.resumeTransfer0(ctx);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resumeTransfer0(ChannelHandlerContext ctx) {
/*     */     try {
/* 120 */       doFlush(ctx);
/* 121 */     } catch (Exception e) {
/* 122 */       if (logger.isWarnEnabled()) {
/* 123 */         logger.warn("Unexpected exception while sending chunks.", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 130 */     this.queue.add(new PendingWrite(msg, promise));
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 135 */     doFlush(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 140 */     doFlush(ctx);
/* 141 */     ctx.fireChannelInactive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 146 */     if (ctx.channel().isWritable())
/*     */     {
/* 148 */       doFlush(ctx);
/*     */     }
/* 150 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */   
/*     */   private void discard(Throwable cause) {
/*     */     while (true) {
/* 155 */       PendingWrite currentWrite = this.currentWrite;
/*     */       
/* 157 */       if (this.currentWrite == null) {
/* 158 */         currentWrite = this.queue.poll();
/*     */       } else {
/* 160 */         this.currentWrite = null;
/*     */       } 
/*     */       
/* 163 */       if (currentWrite == null) {
/*     */         break;
/*     */       }
/* 166 */       Object message = currentWrite.msg;
/* 167 */       if (message instanceof ChunkedInput) {
/* 168 */         ChunkedInput<?> in = (ChunkedInput)message;
/*     */         try {
/* 170 */           if (!in.isEndOfInput()) {
/* 171 */             if (cause == null) {
/* 172 */               cause = new ClosedChannelException();
/*     */             }
/* 174 */             currentWrite.fail(cause);
/*     */           } else {
/* 176 */             currentWrite.success(in.length());
/*     */           } 
/* 178 */           closeInput(in);
/* 179 */         } catch (Exception e) {
/* 180 */           currentWrite.fail(e);
/* 181 */           logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", e);
/* 182 */           closeInput(in);
/*     */         }  continue;
/*     */       } 
/* 185 */       if (cause == null) {
/* 186 */         cause = new ClosedChannelException();
/*     */       }
/* 188 */       currentWrite.fail(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doFlush(ChannelHandlerContext ctx) {
/* 194 */     final Channel channel = ctx.channel();
/* 195 */     if (!channel.isActive()) {
/* 196 */       discard(null);
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     boolean requiresFlush = true;
/* 201 */     ByteBufAllocator allocator = ctx.alloc();
/* 202 */     while (channel.isWritable()) {
/* 203 */       if (this.currentWrite == null) {
/* 204 */         this.currentWrite = this.queue.poll();
/*     */       }
/*     */       
/* 207 */       if (this.currentWrite == null) {
/*     */         break;
/*     */       }
/* 210 */       final PendingWrite currentWrite = this.currentWrite;
/* 211 */       final Object pendingMessage = currentWrite.msg;
/*     */       
/* 213 */       if (pendingMessage instanceof ChunkedInput) {
/* 214 */         boolean endOfInput, suspend; final ChunkedInput<?> chunks = (ChunkedInput)pendingMessage;
/*     */ 
/*     */         
/* 217 */         Object message = null;
/*     */         try {
/* 219 */           message = chunks.readChunk(allocator);
/* 220 */           endOfInput = chunks.isEndOfInput();
/*     */           
/* 222 */           if (message == null) {
/*     */             
/* 224 */             suspend = !endOfInput;
/*     */           } else {
/* 226 */             suspend = false;
/*     */           } 
/* 228 */         } catch (Throwable t) {
/* 229 */           this.currentWrite = null;
/*     */           
/* 231 */           if (message != null) {
/* 232 */             ReferenceCountUtil.release(message);
/*     */           }
/*     */           
/* 235 */           currentWrite.fail(t);
/* 236 */           closeInput(chunks);
/*     */           
/*     */           break;
/*     */         } 
/* 240 */         if (suspend) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 247 */         if (message == null)
/*     */         {
/*     */           
/* 250 */           message = Unpooled.EMPTY_BUFFER;
/*     */         }
/*     */         
/* 253 */         ChannelFuture f = ctx.write(message);
/* 254 */         if (endOfInput) {
/* 255 */           this.currentWrite = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 262 */           f.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 265 */                   currentWrite.progress(chunks.progress(), chunks.length());
/* 266 */                   currentWrite.success(chunks.length());
/* 267 */                   ChunkedWriteHandler.closeInput(chunks);
/*     */                 }
/*     */               });
/* 270 */         } else if (channel.isWritable()) {
/* 271 */           f.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 274 */                   if (!future.isSuccess()) {
/* 275 */                     ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 276 */                     currentWrite.fail(future.cause());
/*     */                   } else {
/* 278 */                     currentWrite.progress(chunks.progress(), chunks.length());
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } else {
/* 283 */           f.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 286 */                   if (!future.isSuccess()) {
/* 287 */                     ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 288 */                     currentWrite.fail(future.cause());
/*     */                   } else {
/* 290 */                     currentWrite.progress(chunks.progress(), chunks.length());
/* 291 */                     if (channel.isWritable()) {
/* 292 */                       ChunkedWriteHandler.this.resumeTransfer();
/*     */                     }
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/*     */         
/* 299 */         ctx.flush();
/* 300 */         requiresFlush = false;
/*     */       } else {
/* 302 */         this.currentWrite = null;
/* 303 */         ctx.write(pendingMessage, currentWrite.promise);
/* 304 */         requiresFlush = true;
/*     */       } 
/*     */       
/* 307 */       if (!channel.isActive()) {
/* 308 */         discard(new ClosedChannelException());
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 313 */     if (requiresFlush) {
/* 314 */       ctx.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void closeInput(ChunkedInput<?> chunks) {
/*     */     try {
/* 320 */       chunks.close();
/* 321 */     } catch (Throwable t) {
/* 322 */       if (logger.isWarnEnabled())
/* 323 */         logger.warn("Failed to close a chunked input.", t); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ChunkedWriteHandler() {}
/*     */   
/*     */   private static final class PendingWrite { final Object msg;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     PendingWrite(Object msg, ChannelPromise promise) {
/* 333 */       this.msg = msg;
/* 334 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     void fail(Throwable cause) {
/* 338 */       ReferenceCountUtil.release(this.msg);
/* 339 */       this.promise.tryFailure(cause);
/*     */     }
/*     */     
/*     */     void success(long total) {
/* 343 */       if (this.promise.isDone()) {
/*     */         return;
/*     */       }
/*     */       
/* 347 */       progress(total, total);
/* 348 */       this.promise.trySuccess();
/*     */     }
/*     */     
/*     */     void progress(long progress, long total) {
/* 352 */       if (this.promise instanceof ChannelProgressivePromise)
/* 353 */         ((ChannelProgressivePromise)this.promise).tryProgress(progress, total); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\stream\ChunkedWriteHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */