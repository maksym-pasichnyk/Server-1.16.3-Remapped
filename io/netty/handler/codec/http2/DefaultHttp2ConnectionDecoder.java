/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.http.HttpStatusClass;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2ConnectionDecoder
/*     */   implements Http2ConnectionDecoder
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2ConnectionDecoder.class);
/*  53 */   private Http2FrameListener internalFrameListener = new PrefaceFrameListener();
/*     */   
/*     */   private final Http2Connection connection;
/*     */   
/*     */   private Http2LifecycleManager lifecycleManager;
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private final Http2FrameReader frameReader;
/*     */   private Http2FrameListener listener;
/*     */   private final Http2PromisedRequestVerifier requestVerifier;
/*     */   
/*     */   public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader) {
/*  64 */     this(connection, encoder, frameReader, Http2PromisedRequestVerifier.ALWAYS_VERIFY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier) {
/*  71 */     this.connection = (Http2Connection)ObjectUtil.checkNotNull(connection, "connection");
/*  72 */     this.frameReader = (Http2FrameReader)ObjectUtil.checkNotNull(frameReader, "frameReader");
/*  73 */     this.encoder = (Http2ConnectionEncoder)ObjectUtil.checkNotNull(encoder, "encoder");
/*  74 */     this.requestVerifier = (Http2PromisedRequestVerifier)ObjectUtil.checkNotNull(requestVerifier, "requestVerifier");
/*  75 */     if (connection.local().flowController() == null) {
/*  76 */       connection.local().flowController(new DefaultHttp2LocalFlowController(connection));
/*     */     }
/*  78 */     ((Http2LocalFlowController)connection.local().flowController()).frameWriter(encoder.frameWriter());
/*     */   }
/*     */ 
/*     */   
/*     */   public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
/*  83 */     this.lifecycleManager = (Http2LifecycleManager)ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Connection connection() {
/*  88 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Http2LocalFlowController flowController() {
/*  93 */     return this.connection.local().flowController();
/*     */   }
/*     */ 
/*     */   
/*     */   public void frameListener(Http2FrameListener listener) {
/*  98 */     this.listener = (Http2FrameListener)ObjectUtil.checkNotNull(listener, "listener");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameListener frameListener() {
/* 103 */     return this.listener;
/*     */   }
/*     */ 
/*     */   
/*     */   Http2FrameListener internalFrameListener() {
/* 108 */     return this.internalFrameListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean prefaceReceived() {
/* 113 */     return (FrameReadListener.class == this.internalFrameListener.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception {
/* 118 */     this.frameReader.readFrame(ctx, in, this.internalFrameListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Settings localSettings() {
/* 123 */     Http2Settings settings = new Http2Settings();
/* 124 */     Http2FrameReader.Configuration config = this.frameReader.configuration();
/* 125 */     Http2HeadersDecoder.Configuration headersConfig = config.headersConfiguration();
/* 126 */     Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 127 */     settings.initialWindowSize(flowController().initialWindowSize());
/* 128 */     settings.maxConcurrentStreams(this.connection.remote().maxActiveStreams());
/* 129 */     settings.headerTableSize(headersConfig.maxHeaderTableSize());
/* 130 */     settings.maxFrameSize(frameSizePolicy.maxFrameSize());
/* 131 */     settings.maxHeaderListSize(headersConfig.maxHeaderListSize());
/* 132 */     if (!this.connection.isServer())
/*     */     {
/* 134 */       settings.pushEnabled(this.connection.local().allowPushTo());
/*     */     }
/* 136 */     return settings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 141 */     this.frameReader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long calculateMaxHeaderListSizeGoAway(long maxHeaderListSize) {
/* 152 */     return Http2CodecUtil.calculateMaxHeaderListSizeGoAway(maxHeaderListSize);
/*     */   }
/*     */   
/*     */   private int unconsumedBytes(Http2Stream stream) {
/* 156 */     return flowController().unconsumedBytes(stream);
/*     */   }
/*     */ 
/*     */   
/*     */   void onGoAwayRead0(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
/* 161 */     if (this.connection.goAwayReceived() && this.connection.local().lastStreamKnownByPeer() < lastStreamId)
/* 162 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "lastStreamId MUST NOT increase. Current value: %d new value: %d", new Object[] {
/* 163 */             Integer.valueOf(this.connection.local().lastStreamKnownByPeer()), Integer.valueOf(lastStreamId)
/*     */           }); 
/* 165 */     this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/* 166 */     this.connection.goAwayReceived(lastStreamId, errorCode, debugData);
/*     */   }
/*     */ 
/*     */   
/*     */   void onUnknownFrame0(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
/* 171 */     this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */   }
/*     */   
/*     */   private final class FrameReadListener
/*     */     implements Http2FrameListener
/*     */   {
/*     */     private FrameReadListener() {}
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/*     */       boolean shouldIgnore;
/* 181 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 182 */       Http2LocalFlowController flowController = DefaultHttp2ConnectionDecoder.this.flowController();
/* 183 */       int bytesToReturn = data.readableBytes() + padding;
/*     */ 
/*     */       
/*     */       try {
/* 187 */         shouldIgnore = shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, "DATA");
/* 188 */       } catch (Http2Exception e) {
/*     */ 
/*     */         
/* 191 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/* 192 */         flowController.consumeBytes(stream, bytesToReturn);
/* 193 */         throw e;
/* 194 */       } catch (Throwable t) {
/* 195 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Unhandled error on data stream id %d", new Object[] { Integer.valueOf(streamId) });
/*     */       } 
/*     */       
/* 198 */       if (shouldIgnore) {
/*     */ 
/*     */         
/* 201 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/* 202 */         flowController.consumeBytes(stream, bytesToReturn);
/*     */ 
/*     */         
/* 205 */         verifyStreamMayHaveExisted(streamId);
/*     */ 
/*     */         
/* 208 */         return bytesToReturn;
/*     */       } 
/*     */       
/* 211 */       Http2Exception error = null;
/* 212 */       switch (stream.state()) {
/*     */         case OPEN:
/*     */         case HALF_CLOSED_LOCAL:
/*     */           break;
/*     */         case HALF_CLOSED_REMOTE:
/*     */         case CLOSED:
/* 218 */           error = Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 219 */                 Integer.valueOf(stream.id()), stream.state() });
/*     */           break;
/*     */         default:
/* 222 */           error = Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] {
/* 223 */                 Integer.valueOf(stream.id()), stream.state()
/*     */               });
/*     */           break;
/*     */       } 
/* 227 */       int unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/*     */       try {
/* 229 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/*     */         
/* 231 */         unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/*     */ 
/*     */         
/* 234 */         if (error != null) {
/* 235 */           throw error;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 240 */         bytesToReturn = DefaultHttp2ConnectionDecoder.this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/* 241 */         return bytesToReturn;
/* 242 */       } catch (Http2Exception e) {
/*     */ 
/*     */ 
/*     */         
/* 246 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 247 */         bytesToReturn -= delta;
/* 248 */         throw e;
/* 249 */       } catch (RuntimeException e) {
/*     */ 
/*     */ 
/*     */         
/* 253 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 254 */         bytesToReturn -= delta;
/* 255 */         throw e;
/*     */       } finally {
/*     */         
/* 258 */         flowController.consumeBytes(stream, bytesToReturn);
/*     */         
/* 260 */         if (endOfStream) {
/* 261 */           DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream, ctx.newSucceededFuture());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
/* 269 */       onHeadersRead(ctx, streamId, headers, 0, (short)16, false, padding, endOfStream);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
/* 275 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 276 */       boolean allowHalfClosedRemote = false;
/* 277 */       if (stream == null && !DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId)) {
/* 278 */         stream = DefaultHttp2ConnectionDecoder.this.connection.remote().createStream(streamId, endOfStream);
/*     */         
/* 280 */         allowHalfClosedRemote = (stream.state() == Http2Stream.State.HALF_CLOSED_REMOTE);
/*     */       } 
/*     */       
/* 283 */       if (shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, "HEADERS")) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 288 */       boolean isInformational = (!DefaultHttp2ConnectionDecoder.this.connection.isServer() && HttpStatusClass.valueOf(headers.status()) == HttpStatusClass.INFORMATIONAL);
/* 289 */       if (((isInformational || !endOfStream) && stream.isHeadersReceived()) || stream.isTrailersReceived()) {
/* 290 */         throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Stream %d received too many headers EOS: %s state: %s", new Object[] {
/*     */               
/* 292 */               Integer.valueOf(streamId), Boolean.valueOf(endOfStream), stream.state()
/*     */             });
/*     */       }
/* 295 */       switch (stream.state()) {
/*     */         case RESERVED_REMOTE:
/* 297 */           stream.open(endOfStream);
/*     */           break;
/*     */         
/*     */         case OPEN:
/*     */         case HALF_CLOSED_LOCAL:
/*     */           break;
/*     */         case HALF_CLOSED_REMOTE:
/* 304 */           if (!allowHalfClosedRemote)
/* 305 */             throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 306 */                   Integer.valueOf(stream.id()), stream.state()
/*     */                 }); 
/*     */           break;
/*     */         case CLOSED:
/* 310 */           throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 311 */                 Integer.valueOf(stream.id()), stream.state()
/*     */               });
/*     */         default:
/* 314 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream
/* 315 */                 .state() });
/*     */       } 
/*     */       
/* 318 */       stream.headersReceived(isInformational);
/* 319 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
/*     */       
/* 321 */       DefaultHttp2ConnectionDecoder.this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
/*     */ 
/*     */       
/* 324 */       if (endOfStream) {
/* 325 */         DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
/* 332 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
/*     */       
/* 334 */       DefaultHttp2ConnectionDecoder.this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
/* 339 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 340 */       if (stream == null) {
/* 341 */         verifyStreamMayHaveExisted(streamId);
/*     */         
/*     */         return;
/*     */       } 
/* 345 */       switch (stream.state()) {
/*     */         case IDLE:
/* 347 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "RST_STREAM received for IDLE stream %d", new Object[] { Integer.valueOf(streamId) });
/*     */ 
/*     */         
/*     */         case CLOSED:
/*     */           return;
/*     */       } 
/*     */       
/* 354 */       DefaultHttp2ConnectionDecoder.this.listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */       
/* 356 */       DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStream(stream, ctx.newSucceededFuture());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
/* 362 */       Http2Settings settings = DefaultHttp2ConnectionDecoder.this.encoder.pollSentSettings();
/*     */       
/* 364 */       if (settings != null) {
/* 365 */         applyLocalSettings(settings);
/*     */       }
/*     */       
/* 368 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsAckRead(ctx);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void applyLocalSettings(Http2Settings settings) throws Http2Exception {
/* 377 */       Boolean pushEnabled = settings.pushEnabled();
/* 378 */       Http2FrameReader.Configuration config = DefaultHttp2ConnectionDecoder.this.frameReader.configuration();
/* 379 */       Http2HeadersDecoder.Configuration headerConfig = config.headersConfiguration();
/* 380 */       Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 381 */       if (pushEnabled != null) {
/* 382 */         if (DefaultHttp2ConnectionDecoder.this.connection.isServer()) {
/* 383 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */         }
/* 385 */         DefaultHttp2ConnectionDecoder.this.connection.local().allowPushTo(pushEnabled.booleanValue());
/*     */       } 
/*     */       
/* 388 */       Long maxConcurrentStreams = settings.maxConcurrentStreams();
/* 389 */       if (maxConcurrentStreams != null) {
/* 390 */         DefaultHttp2ConnectionDecoder.this.connection.remote().maxActiveStreams((int)Math.min(maxConcurrentStreams.longValue(), 2147483647L));
/*     */       }
/*     */       
/* 393 */       Long headerTableSize = settings.headerTableSize();
/* 394 */       if (headerTableSize != null) {
/* 395 */         headerConfig.maxHeaderTableSize(headerTableSize.longValue());
/*     */       }
/*     */       
/* 398 */       Long maxHeaderListSize = settings.maxHeaderListSize();
/* 399 */       if (maxHeaderListSize != null) {
/* 400 */         headerConfig.maxHeaderListSize(maxHeaderListSize.longValue(), DefaultHttp2ConnectionDecoder.this.calculateMaxHeaderListSizeGoAway(maxHeaderListSize.longValue()));
/*     */       }
/*     */       
/* 403 */       Integer maxFrameSize = settings.maxFrameSize();
/* 404 */       if (maxFrameSize != null) {
/* 405 */         frameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */       }
/*     */       
/* 408 */       Integer initialWindowSize = settings.initialWindowSize();
/* 409 */       if (initialWindowSize != null) {
/* 410 */         DefaultHttp2ConnectionDecoder.this.flowController().initialWindowSize(initialWindowSize.intValue());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
/* 419 */       DefaultHttp2ConnectionDecoder.this.encoder.writeSettingsAck(ctx, ctx.newPromise());
/*     */       
/* 421 */       DefaultHttp2ConnectionDecoder.this.encoder.remoteSettings(settings);
/*     */       
/* 423 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsRead(ctx, settings);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/* 430 */       DefaultHttp2ConnectionDecoder.this.encoder.writePing(ctx, true, data, ctx.newPromise());
/*     */       
/* 432 */       DefaultHttp2ConnectionDecoder.this.listener.onPingRead(ctx, data);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/* 437 */       DefaultHttp2ConnectionDecoder.this.listener.onPingAckRead(ctx, data);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
/* 444 */       if (DefaultHttp2ConnectionDecoder.this.connection().isServer()) {
/* 445 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A client cannot push.", new Object[0]);
/*     */       }
/*     */       
/* 448 */       Http2Stream parentStream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/*     */       
/* 450 */       if (shouldIgnoreHeadersOrDataFrame(ctx, streamId, parentStream, "PUSH_PROMISE")) {
/*     */         return;
/*     */       }
/*     */       
/* 454 */       if (parentStream == null) {
/* 455 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d does not exist", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */       
/* 458 */       switch (parentStream.state()) {
/*     */         case OPEN:
/*     */         case HALF_CLOSED_LOCAL:
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 465 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state for receiving push promise: %s", new Object[] {
/*     */                 
/* 467 */                 Integer.valueOf(parentStream.id()), parentStream.state()
/*     */               });
/*     */       } 
/* 470 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isAuthoritative(ctx, headers))
/* 471 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not authoritative", new Object[] {
/*     */               
/* 473 */               Integer.valueOf(streamId), Integer.valueOf(promisedStreamId)
/*     */             }); 
/* 475 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isCacheable(headers))
/* 476 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be cacheable", new Object[] {
/*     */               
/* 478 */               Integer.valueOf(streamId), Integer.valueOf(promisedStreamId)
/*     */             }); 
/* 480 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isSafe(headers)) {
/* 481 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be safe", new Object[] {
/*     */               
/* 483 */               Integer.valueOf(streamId), Integer.valueOf(promisedStreamId)
/*     */             });
/*     */       }
/*     */       
/* 487 */       DefaultHttp2ConnectionDecoder.this.connection.remote().reservePushStream(promisedStreamId, parentStream);
/*     */       
/* 489 */       DefaultHttp2ConnectionDecoder.this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
/* 495 */       DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
/* 501 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 502 */       if (stream == null || stream.state() == Http2Stream.State.CLOSED || streamCreatedAfterGoAwaySent(streamId)) {
/*     */         
/* 504 */         verifyStreamMayHaveExisted(streamId);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 509 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().incrementWindowSize(stream, windowSizeIncrement);
/*     */       
/* 511 */       DefaultHttp2ConnectionDecoder.this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
/* 517 */       DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean shouldIgnoreHeadersOrDataFrame(ChannelHandlerContext ctx, int streamId, Http2Stream stream, String frameName) throws Http2Exception {
/* 526 */       if (stream == null) {
/* 527 */         if (streamCreatedAfterGoAwaySent(streamId)) {
/* 528 */           DefaultHttp2ConnectionDecoder.logger.info("{} ignoring {} frame for stream {}. Stream sent after GOAWAY sent", new Object[] { ctx
/* 529 */                 .channel(), frameName, Integer.valueOf(streamId) });
/* 530 */           return true;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 535 */         throw Http2Exception.streamError(streamId, Http2Error.STREAM_CLOSED, "Received %s frame for an unknown stream %d", new Object[] { frameName, 
/* 536 */               Integer.valueOf(streamId) });
/* 537 */       }  if (stream.isResetSent() || streamCreatedAfterGoAwaySent(streamId)) {
/* 538 */         if (DefaultHttp2ConnectionDecoder.logger.isInfoEnabled()) {
/* 539 */           DefaultHttp2ConnectionDecoder.logger.info("{} ignoring {} frame for stream {} {}", new Object[] { ctx.channel(), frameName, 
/* 540 */                 stream.isResetSent() ? "RST_STREAM sent." : ("Stream created after GOAWAY sent. Last known stream by peer " + 
/*     */                 
/* 542 */                 DefaultHttp2ConnectionDecoder.access$100(this.this$0).remote().lastStreamKnownByPeer()) });
/*     */         }
/* 544 */         return true;
/*     */       } 
/* 546 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean streamCreatedAfterGoAwaySent(int streamId) {
/* 562 */       Http2Connection.Endpoint<?> remote = DefaultHttp2ConnectionDecoder.this.connection.remote();
/* 563 */       return (DefaultHttp2ConnectionDecoder.this.connection.goAwaySent() && remote.isValidStreamId(streamId) && streamId > remote
/* 564 */         .lastStreamKnownByPeer());
/*     */     }
/*     */     
/*     */     private void verifyStreamMayHaveExisted(int streamId) throws Http2Exception {
/* 568 */       if (!DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId)) {
/* 569 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d does not exist", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class PrefaceFrameListener
/*     */     implements Http2FrameListener
/*     */   {
/*     */     private PrefaceFrameListener() {}
/*     */ 
/*     */     
/*     */     private void verifyPrefaceReceived() throws Http2Exception {
/* 582 */       if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
/* 583 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received non-SETTINGS as first frame.", new Object[0]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/* 590 */       verifyPrefaceReceived();
/* 591 */       return DefaultHttp2ConnectionDecoder.this.internalFrameListener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
/* 597 */       verifyPrefaceReceived();
/* 598 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, padding, endOfStream);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
/* 604 */       verifyPrefaceReceived();
/* 605 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
/* 612 */       verifyPrefaceReceived();
/* 613 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
/* 618 */       verifyPrefaceReceived();
/* 619 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onRstStreamRead(ctx, streamId, errorCode);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
/* 624 */       verifyPrefaceReceived();
/* 625 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsAckRead(ctx);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
/* 632 */       if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
/* 633 */         DefaultHttp2ConnectionDecoder.this.internalFrameListener = new DefaultHttp2ConnectionDecoder.FrameReadListener();
/*     */       }
/* 635 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsRead(ctx, settings);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/* 640 */       verifyPrefaceReceived();
/* 641 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingRead(ctx, data);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/* 646 */       verifyPrefaceReceived();
/* 647 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingAckRead(ctx, data);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
/* 653 */       verifyPrefaceReceived();
/* 654 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
/* 660 */       DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
/* 666 */       verifyPrefaceReceived();
/* 667 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
/* 673 */       DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */