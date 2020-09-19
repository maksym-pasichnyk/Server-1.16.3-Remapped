/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.HttpServerUpgradeHandler;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Http2FrameCodec
/*     */   extends Http2ConnectionHandler
/*     */ {
/* 144 */   private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Http2FrameCodec.class);
/*     */   
/*     */   private final Http2Connection.PropertyKey streamKey;
/*     */   
/*     */   private final Http2Connection.PropertyKey upgradeKey;
/*     */   
/*     */   private final Integer initialFlowControlWindowSize;
/*     */   
/*     */   private ChannelHandlerContext ctx;
/*     */   
/*     */   private int numBufferedStreams;
/*     */   private DefaultHttp2FrameStream frameStreamToInitialize;
/*     */   
/*     */   Http2FrameCodec(Http2ConnectionEncoder encoder, Http2ConnectionDecoder decoder, Http2Settings initialSettings) {
/* 158 */     super(decoder, encoder, initialSettings);
/*     */     
/* 160 */     decoder.frameListener(new FrameListener());
/* 161 */     connection().addListener(new ConnectionListener());
/* 162 */     ((Http2RemoteFlowController)connection().remote().flowController()).listener(new Http2RemoteFlowControllerListener());
/* 163 */     this.streamKey = connection().newKey();
/* 164 */     this.upgradeKey = connection().newKey();
/* 165 */     this.initialFlowControlWindowSize = initialSettings.initialWindowSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DefaultHttp2FrameStream newStream() {
/* 172 */     return new DefaultHttp2FrameStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void forEachActiveStream(final Http2FrameStreamVisitor streamVisitor) throws Http2Exception {
/* 181 */     assert this.ctx.executor().inEventLoop();
/*     */     
/* 183 */     connection().forEachActiveStream(new Http2StreamVisitor()
/*     */         {
/*     */           public boolean visit(Http2Stream stream) {
/*     */             try {
/* 187 */               return streamVisitor.visit(stream.<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey));
/* 188 */             } catch (Throwable cause) {
/* 189 */               Http2FrameCodec.this.onError(Http2FrameCodec.this.ctx, false, cause);
/* 190 */               return false;
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 198 */     this.ctx = ctx;
/* 199 */     super.handlerAdded(ctx);
/* 200 */     handlerAdded0(ctx);
/*     */ 
/*     */     
/* 203 */     Http2Connection connection = connection();
/* 204 */     if (connection.isServer()) {
/* 205 */       tryExpandConnectionFlowControlWindow(connection);
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryExpandConnectionFlowControlWindow(Http2Connection connection) throws Http2Exception {
/* 210 */     if (this.initialFlowControlWindowSize != null) {
/*     */ 
/*     */       
/* 213 */       Http2Stream connectionStream = connection.connectionStream();
/* 214 */       Http2LocalFlowController localFlowController = connection.local().flowController();
/* 215 */       int delta = this.initialFlowControlWindowSize.intValue() - localFlowController.initialWindowSize(connectionStream);
/*     */       
/* 217 */       if (delta > 0) {
/*     */         
/* 219 */         localFlowController.incrementWindowSize(connectionStream, Math.max(delta << 1, delta));
/* 220 */         flush(this.ctx);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void handlerAdded0(ChannelHandlerContext ctx) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 235 */     if (evt == Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE) {
/*     */       
/* 237 */       tryExpandConnectionFlowControlWindow(connection());
/* 238 */     } else if (evt instanceof HttpServerUpgradeHandler.UpgradeEvent) {
/* 239 */       HttpServerUpgradeHandler.UpgradeEvent upgrade = (HttpServerUpgradeHandler.UpgradeEvent)evt;
/*     */       try {
/* 241 */         onUpgradeEvent(ctx, upgrade.retain());
/* 242 */         Http2Stream stream = connection().stream(1);
/* 243 */         if (stream.getProperty(this.streamKey) == null)
/*     */         {
/*     */ 
/*     */           
/* 247 */           onStreamActive0(stream);
/*     */         }
/* 249 */         upgrade.upgradeRequest().headers().setInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_ID
/* 250 */             .text(), 1);
/* 251 */         stream.setProperty(this.upgradeKey, Boolean.valueOf(true));
/* 252 */         InboundHttpToHttp2Adapter.handle(ctx, 
/* 253 */             connection(), decoder().frameListener(), (FullHttpMessage)upgrade.upgradeRequest().retain());
/*     */       } finally {
/* 255 */         upgrade.release();
/*     */       } 
/*     */       return;
/*     */     } 
/* 259 */     super.userEventTriggered(ctx, evt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/* 268 */     if (msg instanceof Http2DataFrame) {
/* 269 */       Http2DataFrame dataFrame = (Http2DataFrame)msg;
/* 270 */       encoder().writeData(ctx, dataFrame.stream().id(), dataFrame.content(), dataFrame
/* 271 */           .padding(), dataFrame.isEndStream(), promise);
/* 272 */     } else if (msg instanceof Http2HeadersFrame) {
/* 273 */       writeHeadersFrame(ctx, (Http2HeadersFrame)msg, promise);
/* 274 */     } else if (msg instanceof Http2WindowUpdateFrame) {
/* 275 */       Http2WindowUpdateFrame frame = (Http2WindowUpdateFrame)msg;
/* 276 */       Http2FrameStream frameStream = frame.stream();
/*     */ 
/*     */       
/*     */       try {
/* 280 */         if (frameStream == null) {
/* 281 */           increaseInitialConnectionWindow(frame.windowSizeIncrement());
/*     */         } else {
/* 283 */           consumeBytes(frameStream.id(), frame.windowSizeIncrement());
/*     */         } 
/* 285 */         promise.setSuccess();
/* 286 */       } catch (Throwable t) {
/* 287 */         promise.setFailure(t);
/*     */       } 
/* 289 */     } else if (msg instanceof Http2ResetFrame) {
/* 290 */       Http2ResetFrame rstFrame = (Http2ResetFrame)msg;
/* 291 */       encoder().writeRstStream(ctx, rstFrame.stream().id(), rstFrame.errorCode(), promise);
/* 292 */     } else if (msg instanceof Http2PingFrame) {
/* 293 */       Http2PingFrame frame = (Http2PingFrame)msg;
/* 294 */       encoder().writePing(ctx, frame.ack(), frame.content(), promise);
/* 295 */     } else if (msg instanceof Http2SettingsFrame) {
/* 296 */       encoder().writeSettings(ctx, ((Http2SettingsFrame)msg).settings(), promise);
/* 297 */     } else if (msg instanceof Http2GoAwayFrame) {
/* 298 */       writeGoAwayFrame(ctx, (Http2GoAwayFrame)msg, promise);
/* 299 */     } else if (msg instanceof Http2UnknownFrame) {
/* 300 */       Http2UnknownFrame unknownFrame = (Http2UnknownFrame)msg;
/* 301 */       encoder().writeFrame(ctx, unknownFrame.frameType(), unknownFrame.stream().id(), unknownFrame
/* 302 */           .flags(), unknownFrame.content(), promise);
/* 303 */     } else if (!(msg instanceof Http2Frame)) {
/* 304 */       ctx.write(msg, promise);
/*     */     } else {
/* 306 */       ReferenceCountUtil.release(msg);
/* 307 */       throw new UnsupportedMessageTypeException(msg, new Class[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void increaseInitialConnectionWindow(int deltaBytes) throws Http2Exception {
/* 313 */     ((Http2LocalFlowController)connection().local().flowController()).incrementWindowSize(connection().connectionStream(), deltaBytes);
/*     */   }
/*     */   
/*     */   final boolean consumeBytes(int streamId, int bytes) throws Http2Exception {
/* 317 */     Http2Stream stream = connection().stream(streamId);
/*     */ 
/*     */     
/* 320 */     if (stream != null && streamId == 1) {
/* 321 */       Boolean upgraded = stream.<Boolean>getProperty(this.upgradeKey);
/* 322 */       if (Boolean.TRUE.equals(upgraded)) {
/* 323 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 327 */     return ((Http2LocalFlowController)connection().local().flowController()).consumeBytes(stream, bytes);
/*     */   }
/*     */   
/*     */   private void writeGoAwayFrame(ChannelHandlerContext ctx, Http2GoAwayFrame frame, ChannelPromise promise) {
/* 331 */     if (frame.lastStreamId() > -1) {
/* 332 */       frame.release();
/* 333 */       throw new IllegalArgumentException("Last stream id must not be set on GOAWAY frame");
/*     */     } 
/*     */     
/* 336 */     int lastStreamCreated = connection().remote().lastStreamCreated();
/* 337 */     long lastStreamId = lastStreamCreated + frame.extraStreamIds() * 2L;
/*     */     
/* 339 */     if (lastStreamId > 2147483647L) {
/* 340 */       lastStreamId = 2147483647L;
/*     */     }
/* 342 */     goAway(ctx, (int)lastStreamId, frame.errorCode(), frame.content(), promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHeadersFrame(ChannelHandlerContext ctx, Http2HeadersFrame headersFrame, final ChannelPromise promise) {
/* 348 */     if (Http2CodecUtil.isStreamIdValid(headersFrame.stream().id())) {
/* 349 */       encoder().writeHeaders(ctx, headersFrame.stream().id(), headersFrame.headers(), headersFrame.padding(), headersFrame
/* 350 */           .isEndStream(), promise);
/*     */     } else {
/* 352 */       DefaultHttp2FrameStream stream = (DefaultHttp2FrameStream)headersFrame.stream();
/* 353 */       Http2Connection connection = connection();
/* 354 */       int streamId = connection.local().incrementAndGetNextStreamId();
/* 355 */       if (streamId < 0) {
/* 356 */         promise.setFailure(new Http2NoMoreStreamIdsException());
/*     */         return;
/*     */       } 
/* 359 */       stream.id = streamId;
/*     */ 
/*     */ 
/*     */       
/* 363 */       assert this.frameStreamToInitialize == null;
/* 364 */       this.frameStreamToInitialize = stream;
/*     */ 
/*     */       
/* 367 */       ChannelPromise writePromise = ctx.newPromise();
/*     */       
/* 369 */       encoder().writeHeaders(ctx, streamId, headersFrame.headers(), headersFrame.padding(), headersFrame
/* 370 */           .isEndStream(), writePromise);
/* 371 */       if (writePromise.isDone()) {
/* 372 */         notifyHeaderWritePromise((ChannelFuture)writePromise, promise);
/*     */       } else {
/* 374 */         this.numBufferedStreams++;
/*     */         
/* 376 */         writePromise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 379 */                 Http2FrameCodec.this.numBufferedStreams--;
/*     */                 
/* 381 */                 Http2FrameCodec.notifyHeaderWritePromise(future, promise);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void notifyHeaderWritePromise(ChannelFuture future, ChannelPromise promise) {
/* 389 */     Throwable cause = future.cause();
/* 390 */     if (cause == null) {
/* 391 */       promise.setSuccess();
/*     */     } else {
/* 393 */       promise.setFailure(cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onStreamActive0(Http2Stream stream) {
/* 398 */     if (connection().local().isValidStreamId(stream.id())) {
/*     */       return;
/*     */     }
/*     */     
/* 402 */     DefaultHttp2FrameStream stream2 = newStream().setStreamAndProperty(this.streamKey, stream);
/* 403 */     onHttp2StreamStateChanged(this.ctx, stream2);
/*     */   }
/*     */   
/*     */   private final class ConnectionListener extends Http2ConnectionAdapter {
/*     */     private ConnectionListener() {}
/*     */     
/*     */     public void onStreamAdded(Http2Stream stream) {
/* 410 */       if (Http2FrameCodec.this.frameStreamToInitialize != null && stream.id() == Http2FrameCodec.this.frameStreamToInitialize.id()) {
/* 411 */         Http2FrameCodec.this.frameStreamToInitialize.setStreamAndProperty(Http2FrameCodec.this.streamKey, stream);
/* 412 */         Http2FrameCodec.this.frameStreamToInitialize = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onStreamActive(Http2Stream stream) {
/* 418 */       Http2FrameCodec.this.onStreamActive0(stream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onStreamClosed(Http2Stream stream) {
/* 423 */       Http2FrameCodec.DefaultHttp2FrameStream stream2 = stream.<Http2FrameCodec.DefaultHttp2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
/* 424 */       if (stream2 != null) {
/* 425 */         Http2FrameCodec.this.onHttp2StreamStateChanged(Http2FrameCodec.this.ctx, stream2);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onStreamHalfClosed(Http2Stream stream) {
/* 431 */       Http2FrameCodec.DefaultHttp2FrameStream stream2 = stream.<Http2FrameCodec.DefaultHttp2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
/* 432 */       if (stream2 != null) {
/* 433 */         Http2FrameCodec.this.onHttp2StreamStateChanged(Http2FrameCodec.this.ctx, stream2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onConnectionError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception http2Ex) {
/* 441 */     if (!outbound)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 446 */       ctx.fireExceptionCaught(cause);
/*     */     }
/* 448 */     super.onConnectionError(ctx, outbound, cause, http2Ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onStreamError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception.StreamException streamException) {
/* 458 */     int streamId = streamException.streamId();
/* 459 */     Http2Stream connectionStream = connection().stream(streamId);
/* 460 */     if (connectionStream == null) {
/* 461 */       onHttp2UnknownStreamError(ctx, cause, streamException);
/*     */       
/* 463 */       super.onStreamError(ctx, outbound, cause, streamException);
/*     */       
/*     */       return;
/*     */     } 
/* 467 */     Http2FrameStream stream = connectionStream.<Http2FrameStream>getProperty(this.streamKey);
/* 468 */     if (stream == null) {
/* 469 */       LOG.warn("Stream exception thrown without stream object attached.", cause);
/*     */       
/* 471 */       super.onStreamError(ctx, outbound, cause, streamException);
/*     */       
/*     */       return;
/*     */     } 
/* 475 */     if (!outbound)
/*     */     {
/* 477 */       onHttp2FrameStreamException(ctx, new Http2FrameStreamException(stream, streamException.error(), cause));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void onHttp2UnknownStreamError(ChannelHandlerContext ctx, Throwable cause, Http2Exception.StreamException streamException) {
/* 484 */     LOG.warn("Stream exception thrown for unkown stream {}.", Integer.valueOf(streamException.streamId()), cause);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean isGracefulShutdownComplete() {
/* 489 */     return (super.isGracefulShutdownComplete() && this.numBufferedStreams == 0);
/*     */   }
/*     */   
/*     */   private final class FrameListener
/*     */     implements Http2FrameListener {
/*     */     private FrameListener() {}
/*     */     
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) {
/* 497 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2UnknownFrame(frameType, flags, payload))
/* 498 */           .stream(requireStream(streamId)).retain());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) {
/* 503 */       Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2SettingsFrame(settings));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPingRead(ChannelHandlerContext ctx, long data) {
/* 508 */       Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, false));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, long data) {
/* 513 */       Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) {
/* 518 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2ResetFrame(errorCode)).stream(requireStream(streamId)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) {
/* 523 */       if (streamId == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 527 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2WindowUpdateFrame(windowSizeIncrement)).stream(requireStream(streamId)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) {
/* 534 */       onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) {
/* 540 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2HeadersFrame(headers, endOfStream, padding))
/* 541 */           .stream(requireStream(streamId)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) {
/* 547 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2DataFrame(data, endOfStream, padding))
/* 548 */           .stream(requireStream(streamId)).retain());
/*     */       
/* 550 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) {
/* 555 */       Http2FrameCodec.this.onHttp2Frame(ctx, (new DefaultHttp2GoAwayFrame(lastStreamId, errorCode, debugData)).retain());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) {}
/*     */ 
/*     */ 
/*     */     
/*     */     private Http2FrameStream requireStream(int streamId) {
/* 576 */       Http2FrameStream stream = Http2FrameCodec.this.connection().stream(streamId).<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
/* 577 */       if (stream == null) {
/* 578 */         throw new IllegalStateException("Stream object required for identifier: " + streamId);
/*     */       }
/* 580 */       return stream;
/*     */     }
/*     */   }
/*     */   
/*     */   void onUpgradeEvent(ChannelHandlerContext ctx, HttpServerUpgradeHandler.UpgradeEvent evt) {
/* 585 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */ 
/*     */   
/*     */   void onHttp2StreamWritabilityChanged(ChannelHandlerContext ctx, Http2FrameStream stream, boolean writable) {
/* 590 */     ctx.fireUserEventTriggered(Http2FrameStreamEvent.writabilityChanged(stream));
/*     */   }
/*     */   
/*     */   void onHttp2StreamStateChanged(ChannelHandlerContext ctx, Http2FrameStream stream) {
/* 594 */     ctx.fireUserEventTriggered(Http2FrameStreamEvent.stateChanged(stream));
/*     */   }
/*     */   
/*     */   void onHttp2Frame(ChannelHandlerContext ctx, Http2Frame frame) {
/* 598 */     ctx.fireChannelRead(frame);
/*     */   }
/*     */   
/*     */   void onHttp2FrameStreamException(ChannelHandlerContext ctx, Http2FrameStreamException cause) {
/* 602 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */   
/*     */   final boolean isWritable(DefaultHttp2FrameStream stream) {
/* 606 */     Http2Stream s = stream.stream;
/* 607 */     return (s != null && ((Http2RemoteFlowController)connection().remote().flowController()).isWritable(s));
/*     */   }
/*     */   
/*     */   private final class Http2RemoteFlowControllerListener
/*     */     implements Http2RemoteFlowController.Listener {
/*     */     public void writabilityChanged(Http2Stream stream) {
/* 613 */       Http2FrameStream frameStream = stream.<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
/* 614 */       if (frameStream == null) {
/*     */         return;
/*     */       }
/* 617 */       Http2FrameCodec.this.onHttp2StreamWritabilityChanged(Http2FrameCodec.this
/* 618 */           .ctx, frameStream, ((Http2RemoteFlowController)Http2FrameCodec.this.connection().remote().flowController()).isWritable(stream));
/*     */     }
/*     */ 
/*     */     
/*     */     private Http2RemoteFlowControllerListener() {}
/*     */   }
/*     */   
/*     */   static class DefaultHttp2FrameStream
/*     */     implements Http2FrameStream
/*     */   {
/* 628 */     private volatile int id = -1;
/*     */     volatile Http2Stream stream;
/*     */     
/*     */     DefaultHttp2FrameStream setStreamAndProperty(Http2Connection.PropertyKey streamKey, Http2Stream stream) {
/* 632 */       assert this.id == -1 || stream.id() == this.id;
/* 633 */       this.stream = stream;
/* 634 */       stream.setProperty(streamKey, this);
/* 635 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int id() {
/* 640 */       Http2Stream stream = this.stream;
/* 641 */       return (stream == null) ? this.id : stream.id();
/*     */     }
/*     */ 
/*     */     
/*     */     public Http2Stream.State state() {
/* 646 */       Http2Stream stream = this.stream;
/* 647 */       return (stream == null) ? Http2Stream.State.IDLE : stream.state();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 652 */       return String.valueOf(id());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */