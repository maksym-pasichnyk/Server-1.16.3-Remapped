/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public abstract class AbstractTrafficShapingHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long DEFAULT_CHECK_INTERVAL = 1000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long DEFAULT_MAX_TIME = 15000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final long DEFAULT_MAX_SIZE = 4194304L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final long MINIMAL_WAIT = 10L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TrafficCounter trafficCounter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long writeLimit;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long readLimit;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected volatile long maxTime = 15000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected volatile long checkInterval = 1000L;
/*     */ 
/*     */   
/* 100 */   static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
/* 101 */   static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class
/* 102 */       .getName() + ".REOPEN_TASK");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   volatile long maxWriteDelay = 4000L;
/*     */ 
/*     */ 
/*     */   
/* 111 */   volatile long maxWriteSize = 4194304L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int userDefinedWritabilityIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int CHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int GLOBAL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int GLOBALCHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setTrafficCounter(TrafficCounter newTrafficCounter) {
/* 139 */     this.trafficCounter = newTrafficCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int userDefinedWritabilityIndex() {
/* 150 */     return 1;
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval, long maxTime) {
/* 166 */     if (maxTime <= 0L) {
/* 167 */       throw new IllegalArgumentException("maxTime must be positive");
/*     */     }
/*     */     
/* 170 */     this.userDefinedWritabilityIndex = userDefinedWritabilityIndex();
/* 171 */     this.writeLimit = writeLimit;
/* 172 */     this.readLimit = readLimit;
/* 173 */     this.checkInterval = checkInterval;
/* 174 */     this.maxTime = maxTime;
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
/* 188 */     this(writeLimit, readLimit, checkInterval, 15000L);
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit) {
/* 201 */     this(writeLimit, readLimit, 1000L, 15000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractTrafficShapingHandler() {
/* 209 */     this(0L, 0L, 1000L, 15000L);
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
/*     */   protected AbstractTrafficShapingHandler(long checkInterval) {
/* 221 */     this(0L, 0L, checkInterval, 15000L);
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
/*     */   public void configure(long newWriteLimit, long newReadLimit, long newCheckInterval) {
/* 238 */     configure(newWriteLimit, newReadLimit);
/* 239 */     configure(newCheckInterval);
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
/*     */   public void configure(long newWriteLimit, long newReadLimit) {
/* 254 */     this.writeLimit = newWriteLimit;
/* 255 */     this.readLimit = newReadLimit;
/* 256 */     if (this.trafficCounter != null) {
/* 257 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(long newCheckInterval) {
/* 267 */     this.checkInterval = newCheckInterval;
/* 268 */     if (this.trafficCounter != null) {
/* 269 */       this.trafficCounter.configure(this.checkInterval);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWriteLimit() {
/* 277 */     return this.writeLimit;
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
/*     */   public void setWriteLimit(long writeLimit) {
/* 290 */     this.writeLimit = writeLimit;
/* 291 */     if (this.trafficCounter != null) {
/* 292 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getReadLimit() {
/* 300 */     return this.readLimit;
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
/*     */   public void setReadLimit(long readLimit) {
/* 313 */     this.readLimit = readLimit;
/* 314 */     if (this.trafficCounter != null) {
/* 315 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCheckInterval() {
/* 323 */     return this.checkInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckInterval(long checkInterval) {
/* 330 */     this.checkInterval = checkInterval;
/* 331 */     if (this.trafficCounter != null) {
/* 332 */       this.trafficCounter.configure(checkInterval);
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
/*     */   
/*     */   public void setMaxTimeWait(long maxTime) {
/* 348 */     if (maxTime <= 0L) {
/* 349 */       throw new IllegalArgumentException("maxTime must be positive");
/*     */     }
/* 351 */     this.maxTime = maxTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxTimeWait() {
/* 358 */     return this.maxTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxWriteDelay() {
/* 365 */     return this.maxWriteDelay;
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
/*     */   public void setMaxWriteDelay(long maxWriteDelay) {
/* 379 */     if (maxWriteDelay <= 0L) {
/* 380 */       throw new IllegalArgumentException("maxWriteDelay must be positive");
/*     */     }
/* 382 */     this.maxWriteDelay = maxWriteDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxWriteSize() {
/* 389 */     return this.maxWriteSize;
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
/*     */   public void setMaxWriteSize(long maxWriteSize) {
/* 405 */     this.maxWriteSize = maxWriteSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doAccounting(TrafficCounter counter) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ReopenReadTimerTask
/*     */     implements Runnable
/*     */   {
/*     */     final ChannelHandlerContext ctx;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ReopenReadTimerTask(ChannelHandlerContext ctx) {
/* 425 */       this.ctx = ctx;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 430 */       Channel channel = this.ctx.channel();
/* 431 */       ChannelConfig config = channel.config();
/* 432 */       if (!config.isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
/*     */ 
/*     */         
/* 435 */         if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 436 */           AbstractTrafficShapingHandler.logger.debug("Not unsuspend: " + config.isAutoRead() + ':' + 
/* 437 */               AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */         }
/* 439 */         channel.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
/*     */       } else {
/*     */         
/* 442 */         if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 443 */           if (config.isAutoRead() && !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
/* 444 */             AbstractTrafficShapingHandler.logger.debug("Unsuspend: " + config.isAutoRead() + ':' + 
/* 445 */                 AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */           } else {
/* 447 */             AbstractTrafficShapingHandler.logger.debug("Normal unsuspend: " + config.isAutoRead() + ':' + 
/* 448 */                 AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */           } 
/*     */         }
/* 451 */         channel.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
/* 452 */         config.setAutoRead(true);
/* 453 */         channel.read();
/*     */       } 
/* 455 */       if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 456 */         AbstractTrafficShapingHandler.logger.debug("Unsuspend final status => " + config.isAutoRead() + ':' + 
/* 457 */             AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void releaseReadSuspended(ChannelHandlerContext ctx) {
/* 466 */     Channel channel = ctx.channel();
/* 467 */     channel.attr(READ_SUSPENDED).set(Boolean.valueOf(false));
/* 468 */     channel.config().setAutoRead(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 473 */     long size = calculateSize(msg);
/* 474 */     long now = TrafficCounter.milliSecondFromNano();
/* 475 */     if (size > 0L) {
/*     */       
/* 477 */       long wait = this.trafficCounter.readTimeToWait(size, this.readLimit, this.maxTime, now);
/* 478 */       wait = checkWaitReadTime(ctx, wait, now);
/* 479 */       if (wait >= 10L) {
/*     */ 
/*     */         
/* 482 */         Channel channel = ctx.channel();
/* 483 */         ChannelConfig config = channel.config();
/* 484 */         if (logger.isDebugEnabled()) {
/* 485 */           logger.debug("Read suspend: " + wait + ':' + config.isAutoRead() + ':' + 
/* 486 */               isHandlerActive(ctx));
/*     */         }
/* 488 */         if (config.isAutoRead() && isHandlerActive(ctx)) {
/* 489 */           config.setAutoRead(false);
/* 490 */           channel.attr(READ_SUSPENDED).set(Boolean.valueOf(true));
/*     */ 
/*     */           
/* 493 */           Attribute<Runnable> attr = channel.attr(REOPEN_TASK);
/* 494 */           Runnable reopenTask = (Runnable)attr.get();
/* 495 */           if (reopenTask == null) {
/* 496 */             reopenTask = new ReopenReadTimerTask(ctx);
/* 497 */             attr.set(reopenTask);
/*     */           } 
/* 499 */           ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
/* 500 */           if (logger.isDebugEnabled()) {
/* 501 */             logger.debug("Suspend final status => " + config.isAutoRead() + ':' + 
/* 502 */                 isHandlerActive(ctx) + " will reopened at: " + wait);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 507 */     informReadOperation(ctx, now);
/* 508 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now) {
/* 519 */     return wait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void informReadOperation(ChannelHandlerContext ctx, long now) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isHandlerActive(ChannelHandlerContext ctx) {
/* 531 */     Boolean suspended = (Boolean)ctx.channel().attr(READ_SUSPENDED).get();
/* 532 */     return (suspended == null || Boolean.FALSE.equals(suspended));
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) {
/* 537 */     if (isHandlerActive(ctx))
/*     */     {
/* 539 */       ctx.read();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 546 */     long size = calculateSize(msg);
/* 547 */     long now = TrafficCounter.milliSecondFromNano();
/* 548 */     if (size > 0L) {
/*     */       
/* 550 */       long wait = this.trafficCounter.writeTimeToWait(size, this.writeLimit, this.maxTime, now);
/* 551 */       if (wait >= 10L) {
/* 552 */         if (logger.isDebugEnabled()) {
/* 553 */           logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + 
/* 554 */               isHandlerActive(ctx));
/*     */         }
/* 556 */         submitWrite(ctx, msg, size, wait, now, promise);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 561 */     submitWrite(ctx, msg, size, 0L, now, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void submitWrite(ChannelHandlerContext ctx, Object msg, long delay, ChannelPromise promise) {
/* 567 */     submitWrite(ctx, msg, calculateSize(msg), delay, 
/* 568 */         TrafficCounter.milliSecondFromNano(), promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
/* 576 */     setUserDefinedWritability(ctx, true);
/* 577 */     super.channelRegistered(ctx);
/*     */   }
/*     */   
/*     */   void setUserDefinedWritability(ChannelHandlerContext ctx, boolean writable) {
/* 581 */     ChannelOutboundBuffer cob = ctx.channel().unsafe().outboundBuffer();
/* 582 */     if (cob != null) {
/* 583 */       cob.setUserDefinedWritability(this.userDefinedWritabilityIndex, writable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void checkWriteSuspend(ChannelHandlerContext ctx, long delay, long queueSize) {
/* 594 */     if (queueSize > this.maxWriteSize || delay > this.maxWriteDelay) {
/* 595 */       setUserDefinedWritability(ctx, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void releaseWriteSuspended(ChannelHandlerContext ctx) {
/* 602 */     setUserDefinedWritability(ctx, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrafficCounter trafficCounter() {
/* 610 */     return this.trafficCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 621 */     StringBuilder builder = (new StringBuilder(290)).append("TrafficShaping with Write Limit: ").append(this.writeLimit).append(" Read Limit: ").append(this.readLimit).append(" CheckInterval: ").append(this.checkInterval).append(" maxDelay: ").append(this.maxWriteDelay).append(" maxSize: ").append(this.maxWriteSize).append(" and Counter: ");
/* 622 */     if (this.trafficCounter != null) {
/* 623 */       builder.append(this.trafficCounter);
/*     */     } else {
/* 625 */       builder.append("none");
/*     */     } 
/* 627 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long calculateSize(Object msg) {
/* 638 */     if (msg instanceof ByteBuf) {
/* 639 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 641 */     if (msg instanceof ByteBufHolder) {
/* 642 */       return ((ByteBufHolder)msg).content().readableBytes();
/*     */     }
/* 644 */     return -1L;
/*     */   }
/*     */   
/*     */   abstract void submitWrite(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, long paramLong1, long paramLong2, long paramLong3, ChannelPromise paramChannelPromise);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\traffic\AbstractTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */