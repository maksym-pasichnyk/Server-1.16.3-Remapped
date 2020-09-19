/*     */ package io.netty.handler.timeout;
/*     */ 
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class WriteTimeoutHandler
/*     */   extends ChannelOutboundHandlerAdapter
/*     */ {
/*  66 */   private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
/*     */ 
/*     */ 
/*     */   
/*     */   private final long timeoutNanos;
/*     */ 
/*     */ 
/*     */   
/*     */   private WriteTimeoutTask lastTask;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteTimeoutHandler(int timeoutSeconds) {
/*  84 */     this(timeoutSeconds, TimeUnit.SECONDS);
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
/*     */   public WriteTimeoutHandler(long timeout, TimeUnit unit) {
/*  96 */     if (unit == null) {
/*  97 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 100 */     if (timeout <= 0L) {
/* 101 */       this.timeoutNanos = 0L;
/*     */     } else {
/* 103 */       this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 109 */     if (this.timeoutNanos > 0L) {
/* 110 */       promise = promise.unvoid();
/* 111 */       scheduleTimeout(ctx, promise);
/*     */     } 
/* 113 */     ctx.write(msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 118 */     WriteTimeoutTask task = this.lastTask;
/* 119 */     this.lastTask = null;
/* 120 */     while (task != null) {
/* 121 */       task.scheduledFuture.cancel(false);
/* 122 */       WriteTimeoutTask prev = task.prev;
/* 123 */       task.prev = null;
/* 124 */       task.next = null;
/* 125 */       task = prev;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void scheduleTimeout(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 131 */     WriteTimeoutTask task = new WriteTimeoutTask(ctx, promise);
/* 132 */     task.scheduledFuture = (ScheduledFuture<?>)ctx.executor().schedule(task, this.timeoutNanos, TimeUnit.NANOSECONDS);
/*     */     
/* 134 */     if (!task.scheduledFuture.isDone()) {
/* 135 */       addWriteTimeoutTask(task);
/*     */ 
/*     */       
/* 138 */       promise.addListener((GenericFutureListener)task);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addWriteTimeoutTask(WriteTimeoutTask task) {
/* 143 */     if (this.lastTask != null) {
/* 144 */       this.lastTask.next = task;
/* 145 */       task.prev = this.lastTask;
/*     */     } 
/* 147 */     this.lastTask = task;
/*     */   }
/*     */   
/*     */   private void removeWriteTimeoutTask(WriteTimeoutTask task) {
/* 151 */     if (task == this.lastTask) {
/*     */       
/* 153 */       assert task.next == null;
/* 154 */       this.lastTask = this.lastTask.prev;
/* 155 */       if (this.lastTask != null)
/* 156 */         this.lastTask.next = null; 
/*     */     } else {
/* 158 */       if (task.prev == null && task.next == null) {
/*     */         return;
/*     */       }
/* 161 */       if (task.prev == null) {
/*     */         
/* 163 */         task.next.prev = null;
/*     */       } else {
/* 165 */         task.prev.next = task.next;
/* 166 */         task.next.prev = task.prev;
/*     */       } 
/* 168 */     }  task.prev = null;
/* 169 */     task.next = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
/* 176 */     if (!this.closed) {
/* 177 */       ctx.fireExceptionCaught((Throwable)WriteTimeoutException.INSTANCE);
/* 178 */       ctx.close();
/* 179 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final class WriteTimeoutTask
/*     */     implements Runnable, ChannelFutureListener
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     private final ChannelPromise promise;
/*     */     WriteTimeoutTask prev;
/*     */     WriteTimeoutTask next;
/*     */     ScheduledFuture<?> scheduledFuture;
/*     */     
/*     */     WriteTimeoutTask(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 195 */       this.ctx = ctx;
/* 196 */       this.promise = promise;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 204 */       if (!this.promise.isDone()) {
/*     */         try {
/* 206 */           WriteTimeoutHandler.this.writeTimedOut(this.ctx);
/* 207 */         } catch (Throwable t) {
/* 208 */           this.ctx.fireExceptionCaught(t);
/*     */         } 
/*     */       }
/* 211 */       WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void operationComplete(ChannelFuture future) throws Exception {
/* 217 */       this.scheduledFuture.cancel(false);
/* 218 */       WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\timeout\WriteTimeoutHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */