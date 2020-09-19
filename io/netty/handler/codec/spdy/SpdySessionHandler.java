/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class SpdySessionHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  35 */   private static final SpdyProtocolException PROTOCOL_EXCEPTION = (SpdyProtocolException)ThrowableUtil.unknownStackTrace(new SpdyProtocolException(), SpdySessionHandler.class, "handleOutboundMessage(...)");
/*     */   
/*  37 */   private static final SpdyProtocolException STREAM_CLOSED = (SpdyProtocolException)ThrowableUtil.unknownStackTrace(new SpdyProtocolException("Stream closed"), SpdySessionHandler.class, "removeStream(...)");
/*     */   
/*     */   private static final int DEFAULT_WINDOW_SIZE = 65536;
/*     */   
/*  41 */   private int initialSendWindowSize = 65536;
/*  42 */   private int initialReceiveWindowSize = 65536;
/*  43 */   private volatile int initialSessionReceiveWindowSize = 65536;
/*     */   
/*  45 */   private final SpdySession spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
/*     */   
/*     */   private int lastGoodStreamId;
/*     */   private static final int DEFAULT_MAX_CONCURRENT_STREAMS = 2147483647;
/*  49 */   private int remoteConcurrentStreams = Integer.MAX_VALUE;
/*  50 */   private int localConcurrentStreams = Integer.MAX_VALUE;
/*     */   
/*  52 */   private final AtomicInteger pings = new AtomicInteger();
/*     */ 
/*     */   
/*     */   private boolean sentGoAwayFrame;
/*     */ 
/*     */   
/*     */   private boolean receivedGoAwayFrame;
/*     */ 
/*     */   
/*     */   private ChannelFutureListener closeSessionFutureListener;
/*     */ 
/*     */   
/*     */   private final boolean server;
/*     */ 
/*     */   
/*     */   private final int minorVersion;
/*     */ 
/*     */ 
/*     */   
/*     */   public SpdySessionHandler(SpdyVersion version, boolean server) {
/*  72 */     if (version == null) {
/*  73 */       throw new NullPointerException("version");
/*     */     }
/*  75 */     this.server = server;
/*  76 */     this.minorVersion = version.getMinorVersion();
/*     */   }
/*     */   
/*     */   public void setSessionReceiveWindowSize(int sessionReceiveWindowSize) {
/*  80 */     if (sessionReceiveWindowSize < 0) {
/*  81 */       throw new IllegalArgumentException("sessionReceiveWindowSize");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.initialSessionReceiveWindowSize = sessionReceiveWindowSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  94 */     if (msg instanceof SpdyDataFrame) {
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
/* 118 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 119 */       int streamId = spdyDataFrame.streamId();
/*     */       
/* 121 */       int deltaWindowSize = -1 * spdyDataFrame.content().readableBytes();
/*     */       
/* 123 */       int newSessionWindowSize = this.spdySession.updateReceiveWindowSize(0, deltaWindowSize);
/*     */ 
/*     */       
/* 126 */       if (newSessionWindowSize < 0) {
/* 127 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 132 */       if (newSessionWindowSize <= this.initialSessionReceiveWindowSize / 2) {
/* 133 */         int sessionDeltaWindowSize = this.initialSessionReceiveWindowSize - newSessionWindowSize;
/* 134 */         this.spdySession.updateReceiveWindowSize(0, sessionDeltaWindowSize);
/* 135 */         SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(0, sessionDeltaWindowSize);
/*     */         
/* 137 */         ctx.writeAndFlush(spdyWindowUpdateFrame);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (!this.spdySession.isActiveStream(streamId)) {
/* 143 */         spdyDataFrame.release();
/* 144 */         if (streamId <= this.lastGoodStreamId) {
/* 145 */           issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/* 146 */         } else if (!this.sentGoAwayFrame) {
/* 147 */           issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 154 */       if (this.spdySession.isRemoteSideClosed(streamId)) {
/* 155 */         spdyDataFrame.release();
/* 156 */         issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 161 */       if (!isRemoteInitiatedId(streamId) && !this.spdySession.hasReceivedReply(streamId)) {
/* 162 */         spdyDataFrame.release();
/* 163 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       int newWindowSize = this.spdySession.updateReceiveWindowSize(streamId, deltaWindowSize);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       if (newWindowSize < this.spdySession.getReceiveWindowSizeLowerBound(streamId)) {
/* 182 */         spdyDataFrame.release();
/* 183 */         issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 189 */       if (newWindowSize < 0) {
/* 190 */         while (spdyDataFrame.content().readableBytes() > this.initialReceiveWindowSize) {
/*     */           
/* 192 */           SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readRetainedSlice(this.initialReceiveWindowSize));
/* 193 */           ctx.writeAndFlush(partialDataFrame);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 198 */       if (newWindowSize <= this.initialReceiveWindowSize / 2 && !spdyDataFrame.isLast()) {
/* 199 */         int streamDeltaWindowSize = this.initialReceiveWindowSize - newWindowSize;
/* 200 */         this.spdySession.updateReceiveWindowSize(streamId, streamDeltaWindowSize);
/* 201 */         SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(streamId, streamDeltaWindowSize);
/*     */         
/* 203 */         ctx.writeAndFlush(spdyWindowUpdateFrame);
/*     */       } 
/*     */ 
/*     */       
/* 207 */       if (spdyDataFrame.isLast()) {
/* 208 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 211 */     else if (msg instanceof SpdySynStreamFrame) {
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
/* 227 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 228 */       int streamId = spdySynStreamFrame.streamId();
/*     */ 
/*     */       
/* 231 */       if (spdySynStreamFrame.isInvalid() || 
/* 232 */         !isRemoteInitiatedId(streamId) || this.spdySession
/* 233 */         .isActiveStream(streamId)) {
/* 234 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 239 */       if (streamId <= this.lastGoodStreamId) {
/* 240 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 245 */       byte priority = spdySynStreamFrame.priority();
/* 246 */       boolean remoteSideClosed = spdySynStreamFrame.isLast();
/* 247 */       boolean localSideClosed = spdySynStreamFrame.isUnidirectional();
/* 248 */       if (!acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
/* 249 */         issueStreamError(ctx, streamId, SpdyStreamStatus.REFUSED_STREAM);
/*     */         
/*     */         return;
/*     */       } 
/* 253 */     } else if (msg instanceof SpdySynReplyFrame) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 262 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 263 */       int streamId = spdySynReplyFrame.streamId();
/*     */ 
/*     */       
/* 266 */       if (spdySynReplyFrame.isInvalid() || 
/* 267 */         isRemoteInitiatedId(streamId) || this.spdySession
/* 268 */         .isRemoteSideClosed(streamId)) {
/* 269 */         issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 274 */       if (this.spdySession.hasReceivedReply(streamId)) {
/* 275 */         issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_IN_USE);
/*     */         
/*     */         return;
/*     */       } 
/* 279 */       this.spdySession.receivedReply(streamId);
/*     */ 
/*     */       
/* 282 */       if (spdySynReplyFrame.isLast()) {
/* 283 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 286 */     else if (msg instanceof SpdyRstStreamFrame) {
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
/* 297 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 298 */       removeStream(spdyRstStreamFrame.streamId(), ctx.newSucceededFuture());
/*     */     }
/* 300 */     else if (msg instanceof SpdySettingsFrame) {
/*     */       
/* 302 */       SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
/*     */       
/* 304 */       int settingsMinorVersion = spdySettingsFrame.getValue(0);
/* 305 */       if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
/*     */         
/* 307 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 312 */       int newConcurrentStreams = spdySettingsFrame.getValue(4);
/* 313 */       if (newConcurrentStreams >= 0) {
/* 314 */         this.remoteConcurrentStreams = newConcurrentStreams;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 320 */       if (spdySettingsFrame.isPersisted(7)) {
/* 321 */         spdySettingsFrame.removeValue(7);
/*     */       }
/* 323 */       spdySettingsFrame.setPersistValue(7, false);
/*     */ 
/*     */       
/* 326 */       int newInitialWindowSize = spdySettingsFrame.getValue(7);
/* 327 */       if (newInitialWindowSize >= 0) {
/* 328 */         updateInitialSendWindowSize(newInitialWindowSize);
/*     */       }
/*     */     }
/* 331 */     else if (msg instanceof SpdyPingFrame) {
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
/* 342 */       SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
/*     */       
/* 344 */       if (isRemoteInitiatedId(spdyPingFrame.id())) {
/* 345 */         ctx.writeAndFlush(spdyPingFrame);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 350 */       if (this.pings.get() == 0) {
/*     */         return;
/*     */       }
/* 353 */       this.pings.getAndDecrement();
/*     */     }
/* 355 */     else if (msg instanceof SpdyGoAwayFrame) {
/*     */       
/* 357 */       this.receivedGoAwayFrame = true;
/*     */     }
/* 359 */     else if (msg instanceof SpdyHeadersFrame) {
/*     */       
/* 361 */       SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 362 */       int streamId = spdyHeadersFrame.streamId();
/*     */ 
/*     */       
/* 365 */       if (spdyHeadersFrame.isInvalid()) {
/* 366 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */         
/*     */         return;
/*     */       } 
/* 370 */       if (this.spdySession.isRemoteSideClosed(streamId)) {
/* 371 */         issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 376 */       if (spdyHeadersFrame.isLast()) {
/* 377 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 380 */     else if (msg instanceof SpdyWindowUpdateFrame) {
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
/* 392 */       SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)msg;
/* 393 */       int streamId = spdyWindowUpdateFrame.streamId();
/* 394 */       int deltaWindowSize = spdyWindowUpdateFrame.deltaWindowSize();
/*     */ 
/*     */       
/* 397 */       if (streamId != 0 && this.spdySession.isLocalSideClosed(streamId)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 402 */       if (this.spdySession.getSendWindowSize(streamId) > Integer.MAX_VALUE - deltaWindowSize) {
/* 403 */         if (streamId == 0) {
/* 404 */           issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */         } else {
/* 406 */           issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 411 */       updateSendWindowSize(ctx, streamId, deltaWindowSize);
/*     */     } 
/*     */     
/* 414 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 419 */     for (Integer streamId : this.spdySession.activeStreams().keySet()) {
/* 420 */       removeStream(streamId.intValue(), ctx.newSucceededFuture());
/*     */     }
/* 422 */     ctx.fireChannelInactive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 427 */     if (cause instanceof SpdyProtocolException) {
/* 428 */       issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */     }
/*     */     
/* 431 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 436 */     sendGoAwayFrame(ctx, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 441 */     if (msg instanceof SpdyDataFrame || msg instanceof SpdySynStreamFrame || msg instanceof SpdySynReplyFrame || msg instanceof SpdyRstStreamFrame || msg instanceof SpdySettingsFrame || msg instanceof SpdyPingFrame || msg instanceof SpdyGoAwayFrame || msg instanceof SpdyHeadersFrame || msg instanceof SpdyWindowUpdateFrame) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       handleOutboundMessage(ctx, msg, promise);
/*     */     } else {
/* 453 */       ctx.write(msg, promise);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleOutboundMessage(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 458 */     if (msg instanceof SpdyDataFrame) {
/*     */       
/* 460 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 461 */       int streamId = spdyDataFrame.streamId();
/*     */ 
/*     */       
/* 464 */       if (this.spdySession.isLocalSideClosed(streamId)) {
/* 465 */         spdyDataFrame.release();
/* 466 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 483 */       int dataLength = spdyDataFrame.content().readableBytes();
/* 484 */       int sendWindowSize = this.spdySession.getSendWindowSize(streamId);
/* 485 */       int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
/* 486 */       sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
/*     */       
/* 488 */       if (sendWindowSize <= 0) {
/*     */         
/* 490 */         this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise)); return;
/*     */       } 
/* 492 */       if (sendWindowSize < dataLength) {
/*     */         
/* 494 */         this.spdySession.updateSendWindowSize(streamId, -1 * sendWindowSize);
/* 495 */         this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
/*     */ 
/*     */ 
/*     */         
/* 499 */         SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readRetainedSlice(sendWindowSize));
/*     */ 
/*     */         
/* 502 */         this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
/*     */ 
/*     */ 
/*     */         
/* 506 */         final ChannelHandlerContext context = ctx;
/* 507 */         ctx.write(partialDataFrame).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 510 */                 if (!future.isSuccess()) {
/* 511 */                   SpdySessionHandler.this.issueSessionError(context, SpdySessionStatus.INTERNAL_ERROR);
/*     */                 }
/*     */               }
/*     */             });
/*     */         
/*     */         return;
/*     */       } 
/* 518 */       this.spdySession.updateSendWindowSize(streamId, -1 * dataLength);
/* 519 */       this.spdySession.updateSendWindowSize(0, -1 * dataLength);
/*     */ 
/*     */ 
/*     */       
/* 523 */       final ChannelHandlerContext context = ctx;
/* 524 */       promise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 527 */               if (!future.isSuccess()) {
/* 528 */                 SpdySessionHandler.this.issueSessionError(context, SpdySessionStatus.INTERNAL_ERROR);
/*     */               }
/*     */             }
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 535 */       if (spdyDataFrame.isLast()) {
/* 536 */         halfCloseStream(streamId, false, (ChannelFuture)promise);
/*     */       }
/*     */     }
/* 539 */     else if (msg instanceof SpdySynStreamFrame) {
/*     */       
/* 541 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 542 */       int streamId = spdySynStreamFrame.streamId();
/*     */       
/* 544 */       if (isRemoteInitiatedId(streamId)) {
/* 545 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/* 549 */       byte priority = spdySynStreamFrame.priority();
/* 550 */       boolean remoteSideClosed = spdySynStreamFrame.isUnidirectional();
/* 551 */       boolean localSideClosed = spdySynStreamFrame.isLast();
/* 552 */       if (!acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
/* 553 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/* 557 */     } else if (msg instanceof SpdySynReplyFrame) {
/*     */       
/* 559 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 560 */       int streamId = spdySynReplyFrame.streamId();
/*     */ 
/*     */       
/* 563 */       if (!isRemoteInitiatedId(streamId) || this.spdySession.isLocalSideClosed(streamId)) {
/* 564 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 569 */       if (spdySynReplyFrame.isLast()) {
/* 570 */         halfCloseStream(streamId, false, (ChannelFuture)promise);
/*     */       }
/*     */     }
/* 573 */     else if (msg instanceof SpdyRstStreamFrame) {
/*     */       
/* 575 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 576 */       removeStream(spdyRstStreamFrame.streamId(), (ChannelFuture)promise);
/*     */     }
/* 578 */     else if (msg instanceof SpdySettingsFrame) {
/*     */       
/* 580 */       SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
/*     */       
/* 582 */       int settingsMinorVersion = spdySettingsFrame.getValue(0);
/* 583 */       if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
/*     */         
/* 585 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 590 */       int newConcurrentStreams = spdySettingsFrame.getValue(4);
/* 591 */       if (newConcurrentStreams >= 0) {
/* 592 */         this.localConcurrentStreams = newConcurrentStreams;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 598 */       if (spdySettingsFrame.isPersisted(7)) {
/* 599 */         spdySettingsFrame.removeValue(7);
/*     */       }
/* 601 */       spdySettingsFrame.setPersistValue(7, false);
/*     */ 
/*     */       
/* 604 */       int newInitialWindowSize = spdySettingsFrame.getValue(7);
/* 605 */       if (newInitialWindowSize >= 0) {
/* 606 */         updateInitialReceiveWindowSize(newInitialWindowSize);
/*     */       }
/*     */     }
/* 609 */     else if (msg instanceof SpdyPingFrame) {
/*     */       
/* 611 */       SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
/* 612 */       if (isRemoteInitiatedId(spdyPingFrame.id())) {
/* 613 */         ctx.fireExceptionCaught(new IllegalArgumentException("invalid PING ID: " + spdyPingFrame
/* 614 */               .id()));
/*     */         return;
/*     */       } 
/* 617 */       this.pings.getAndIncrement();
/*     */     } else {
/* 619 */       if (msg instanceof SpdyGoAwayFrame) {
/*     */ 
/*     */ 
/*     */         
/* 623 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         return;
/*     */       } 
/* 626 */       if (msg instanceof SpdyHeadersFrame) {
/*     */         
/* 628 */         SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 629 */         int streamId = spdyHeadersFrame.streamId();
/*     */ 
/*     */         
/* 632 */         if (this.spdySession.isLocalSideClosed(streamId)) {
/* 633 */           promise.setFailure(PROTOCOL_EXCEPTION);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 638 */         if (spdyHeadersFrame.isLast()) {
/* 639 */           halfCloseStream(streamId, false, (ChannelFuture)promise);
/*     */         }
/*     */       }
/* 642 */       else if (msg instanceof SpdyWindowUpdateFrame) {
/*     */ 
/*     */         
/* 645 */         promise.setFailure(PROTOCOL_EXCEPTION);
/*     */         return;
/*     */       } 
/*     */     } 
/* 649 */     ctx.write(msg, promise);
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
/*     */   private void issueSessionError(ChannelHandlerContext ctx, SpdySessionStatus status) {
/* 664 */     sendGoAwayFrame(ctx, status).addListener((GenericFutureListener)new ClosingChannelFutureListener(ctx, ctx.newPromise()));
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
/*     */   private void issueStreamError(ChannelHandlerContext ctx, int streamId, SpdyStreamStatus status) {
/* 679 */     boolean fireChannelRead = !this.spdySession.isRemoteSideClosed(streamId);
/* 680 */     ChannelPromise promise = ctx.newPromise();
/* 681 */     removeStream(streamId, (ChannelFuture)promise);
/*     */     
/* 683 */     SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, status);
/* 684 */     ctx.writeAndFlush(spdyRstStreamFrame, promise);
/* 685 */     if (fireChannelRead) {
/* 686 */       ctx.fireChannelRead(spdyRstStreamFrame);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRemoteInitiatedId(int id) {
/* 695 */     boolean serverId = SpdyCodecUtil.isServerId(id);
/* 696 */     return ((this.server && !serverId) || (!this.server && serverId));
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateInitialSendWindowSize(int newInitialWindowSize) {
/* 701 */     int deltaWindowSize = newInitialWindowSize - this.initialSendWindowSize;
/* 702 */     this.initialSendWindowSize = newInitialWindowSize;
/* 703 */     this.spdySession.updateAllSendWindowSizes(deltaWindowSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateInitialReceiveWindowSize(int newInitialWindowSize) {
/* 708 */     int deltaWindowSize = newInitialWindowSize - this.initialReceiveWindowSize;
/* 709 */     this.initialReceiveWindowSize = newInitialWindowSize;
/* 710 */     this.spdySession.updateAllReceiveWindowSizes(deltaWindowSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acceptStream(int streamId, byte priority, boolean remoteSideClosed, boolean localSideClosed) {
/* 717 */     if (this.receivedGoAwayFrame || this.sentGoAwayFrame) {
/* 718 */       return false;
/*     */     }
/*     */     
/* 721 */     boolean remote = isRemoteInitiatedId(streamId);
/* 722 */     int maxConcurrentStreams = remote ? this.localConcurrentStreams : this.remoteConcurrentStreams;
/* 723 */     if (this.spdySession.numActiveStreams(remote) >= maxConcurrentStreams) {
/* 724 */       return false;
/*     */     }
/* 726 */     this.spdySession.acceptStream(streamId, priority, remoteSideClosed, localSideClosed, this.initialSendWindowSize, this.initialReceiveWindowSize, remote);
/*     */ 
/*     */     
/* 729 */     if (remote) {
/* 730 */       this.lastGoodStreamId = streamId;
/*     */     }
/* 732 */     return true;
/*     */   }
/*     */   
/*     */   private void halfCloseStream(int streamId, boolean remote, ChannelFuture future) {
/* 736 */     if (remote) {
/* 737 */       this.spdySession.closeRemoteSide(streamId, isRemoteInitiatedId(streamId));
/*     */     } else {
/* 739 */       this.spdySession.closeLocalSide(streamId, isRemoteInitiatedId(streamId));
/*     */     } 
/* 741 */     if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
/* 742 */       future.addListener((GenericFutureListener)this.closeSessionFutureListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeStream(int streamId, ChannelFuture future) {
/* 747 */     this.spdySession.removeStream(streamId, STREAM_CLOSED, isRemoteInitiatedId(streamId));
/*     */     
/* 749 */     if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
/* 750 */       future.addListener((GenericFutureListener)this.closeSessionFutureListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateSendWindowSize(final ChannelHandlerContext ctx, int streamId, int deltaWindowSize) {
/* 755 */     this.spdySession.updateSendWindowSize(streamId, deltaWindowSize);
/*     */ 
/*     */     
/*     */     while (true) {
/* 759 */       SpdySession.PendingWrite pendingWrite = this.spdySession.getPendingWrite(streamId);
/* 760 */       if (pendingWrite == null) {
/*     */         return;
/*     */       }
/*     */       
/* 764 */       SpdyDataFrame spdyDataFrame = pendingWrite.spdyDataFrame;
/* 765 */       int dataFrameSize = spdyDataFrame.content().readableBytes();
/* 766 */       int writeStreamId = spdyDataFrame.streamId();
/* 767 */       int sendWindowSize = this.spdySession.getSendWindowSize(writeStreamId);
/* 768 */       int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
/* 769 */       sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
/*     */       
/* 771 */       if (sendWindowSize <= 0)
/*     */         return; 
/* 773 */       if (sendWindowSize < dataFrameSize) {
/*     */         
/* 775 */         this.spdySession.updateSendWindowSize(writeStreamId, -1 * sendWindowSize);
/* 776 */         this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
/*     */ 
/*     */ 
/*     */         
/* 780 */         SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(writeStreamId, spdyDataFrame.content().readRetainedSlice(sendWindowSize));
/*     */ 
/*     */ 
/*     */         
/* 784 */         ctx.writeAndFlush(partialDataFrame).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 787 */                 if (!future.isSuccess()) {
/* 788 */                   SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
/*     */                 }
/*     */               }
/*     */             });
/*     */         continue;
/*     */       } 
/* 794 */       this.spdySession.removePendingWrite(writeStreamId);
/* 795 */       this.spdySession.updateSendWindowSize(writeStreamId, -1 * dataFrameSize);
/* 796 */       this.spdySession.updateSendWindowSize(0, -1 * dataFrameSize);
/*     */ 
/*     */       
/* 799 */       if (spdyDataFrame.isLast()) {
/* 800 */         halfCloseStream(writeStreamId, false, (ChannelFuture)pendingWrite.promise);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 805 */       ctx.writeAndFlush(spdyDataFrame, pendingWrite.promise).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 808 */               if (!future.isSuccess()) {
/* 809 */                 SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
/*     */               }
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendGoAwayFrame(ChannelHandlerContext ctx, ChannelPromise future) {
/* 819 */     if (!ctx.channel().isActive()) {
/* 820 */       ctx.close(future);
/*     */       
/*     */       return;
/*     */     } 
/* 824 */     ChannelFuture f = sendGoAwayFrame(ctx, SpdySessionStatus.OK);
/* 825 */     if (this.spdySession.noActiveStreams()) {
/* 826 */       f.addListener((GenericFutureListener)new ClosingChannelFutureListener(ctx, future));
/*     */     } else {
/* 828 */       this.closeSessionFutureListener = new ClosingChannelFutureListener(ctx, future);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture sendGoAwayFrame(ChannelHandlerContext ctx, SpdySessionStatus status) {
/* 835 */     if (!this.sentGoAwayFrame) {
/* 836 */       this.sentGoAwayFrame = true;
/* 837 */       SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, status);
/* 838 */       return ctx.writeAndFlush(spdyGoAwayFrame);
/*     */     } 
/* 840 */     return ctx.newSucceededFuture();
/*     */   }
/*     */   
/*     */   private static final class ClosingChannelFutureListener
/*     */     implements ChannelFutureListener {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelPromise promise;
/*     */     
/*     */     ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 849 */       this.ctx = ctx;
/* 850 */       this.promise = promise;
/*     */     }
/*     */ 
/*     */     
/*     */     public void operationComplete(ChannelFuture sentGoAwayFuture) throws Exception {
/* 855 */       this.ctx.close(this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdySessionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */