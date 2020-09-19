/*     */ package io.netty.handler.flush;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import java.util.concurrent.Future;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlushConsolidationHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*     */   private final int explicitFlushAfterFlushes;
/*     */   private final boolean consolidateWhenNoReadInProgress;
/*     */   private final Runnable flushTask;
/*     */   private int flushPendingCount;
/*     */   private boolean readInProgress;
/*     */   private ChannelHandlerContext ctx;
/*     */   private Future<?> nextScheduledFlush;
/*     */   
/*     */   public FlushConsolidationHandler() {
/*  71 */     this(256, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlushConsolidationHandler(int explicitFlushAfterFlushes) {
/*  80 */     this(explicitFlushAfterFlushes, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlushConsolidationHandler(int explicitFlushAfterFlushes, boolean consolidateWhenNoReadInProgress) {
/*  91 */     if (explicitFlushAfterFlushes <= 0) {
/*  92 */       throw new IllegalArgumentException("explicitFlushAfterFlushes: " + explicitFlushAfterFlushes + " (expected: > 0)");
/*     */     }
/*     */     
/*  95 */     this.explicitFlushAfterFlushes = explicitFlushAfterFlushes;
/*  96 */     this.consolidateWhenNoReadInProgress = consolidateWhenNoReadInProgress;
/*  97 */     this.flushTask = consolidateWhenNoReadInProgress ? new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 101 */           if (FlushConsolidationHandler.this.flushPendingCount > 0 && !FlushConsolidationHandler.this.readInProgress) {
/* 102 */             FlushConsolidationHandler.this.flushPendingCount = 0;
/* 103 */             FlushConsolidationHandler.this.ctx.flush();
/* 104 */             FlushConsolidationHandler.this.nextScheduledFlush = null;
/*     */           } 
/*     */         }
/*     */       } : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 113 */     this.ctx = ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 118 */     if (this.readInProgress) {
/*     */ 
/*     */       
/* 121 */       if (++this.flushPendingCount == this.explicitFlushAfterFlushes) {
/* 122 */         flushNow(ctx);
/*     */       }
/* 124 */     } else if (this.consolidateWhenNoReadInProgress) {
/*     */       
/* 126 */       if (++this.flushPendingCount == this.explicitFlushAfterFlushes) {
/* 127 */         flushNow(ctx);
/*     */       } else {
/* 129 */         scheduleFlush(ctx);
/*     */       } 
/*     */     } else {
/*     */       
/* 133 */       flushNow(ctx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 140 */     resetReadAndFlushIfNeeded(ctx);
/* 141 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 146 */     this.readInProgress = true;
/* 147 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 153 */     resetReadAndFlushIfNeeded(ctx);
/* 154 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 160 */     resetReadAndFlushIfNeeded(ctx);
/* 161 */     ctx.disconnect(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 167 */     resetReadAndFlushIfNeeded(ctx);
/* 168 */     ctx.close(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 173 */     if (!ctx.channel().isWritable())
/*     */     {
/* 175 */       flushIfNeeded(ctx);
/*     */     }
/* 177 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 182 */     flushIfNeeded(ctx);
/*     */   }
/*     */   
/*     */   private void resetReadAndFlushIfNeeded(ChannelHandlerContext ctx) {
/* 186 */     this.readInProgress = false;
/* 187 */     flushIfNeeded(ctx);
/*     */   }
/*     */   
/*     */   private void flushIfNeeded(ChannelHandlerContext ctx) {
/* 191 */     if (this.flushPendingCount > 0) {
/* 192 */       flushNow(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */   private void flushNow(ChannelHandlerContext ctx) {
/* 197 */     cancelScheduledFlush();
/* 198 */     this.flushPendingCount = 0;
/* 199 */     ctx.flush();
/*     */   }
/*     */   
/*     */   private void scheduleFlush(ChannelHandlerContext ctx) {
/* 203 */     if (this.nextScheduledFlush == null)
/*     */     {
/* 205 */       this.nextScheduledFlush = (Future<?>)ctx.channel().eventLoop().submit(this.flushTask);
/*     */     }
/*     */   }
/*     */   
/*     */   private void cancelScheduledFlush() {
/* 210 */     if (this.nextScheduledFlush != null) {
/* 211 */       this.nextScheduledFlush.cancel(false);
/* 212 */       this.nextScheduledFlush = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\flush\FlushConsolidationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */