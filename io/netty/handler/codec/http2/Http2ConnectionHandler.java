/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
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
/*     */ public class Http2ConnectionHandler
/*     */   extends ByteToMessageDecoder
/*     */   implements Http2LifecycleManager, ChannelOutboundHandler
/*     */ {
/*  69 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Http2ConnectionHandler.class);
/*     */   
/*  71 */   private static final Http2Headers HEADERS_TOO_LARGE_HEADERS = ReadOnlyHttp2Headers.serverHeaders(false, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE
/*  72 */       .codeAsText(), new io.netty.util.AsciiString[0]);
/*  73 */   private static final ByteBuf HTTP_1_X_BUF = Unpooled.unreleasableBuffer(
/*  74 */       Unpooled.wrappedBuffer(new byte[] { 72, 84, 84, 80, 47, 49, 46 })).asReadOnly();
/*     */   
/*     */   private final Http2ConnectionDecoder decoder;
/*     */   
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private final Http2Settings initialSettings;
/*     */   private ChannelFutureListener closeListener;
/*     */   private BaseDecoder byteDecoder;
/*     */   private long gracefulShutdownTimeoutMillis;
/*     */   
/*     */   protected Http2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
/*  85 */     this.initialSettings = (Http2Settings)ObjectUtil.checkNotNull(initialSettings, "initialSettings");
/*  86 */     this.decoder = (Http2ConnectionDecoder)ObjectUtil.checkNotNull(decoder, "decoder");
/*  87 */     this.encoder = (Http2ConnectionEncoder)ObjectUtil.checkNotNull(encoder, "encoder");
/*  88 */     if (encoder.connection() != decoder.connection()) {
/*  89 */       throw new IllegalArgumentException("Encoder and Decoder do not share the same connection object");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Http2ConnectionHandler(boolean server, Http2FrameWriter frameWriter, Http2FrameLogger frameLogger, Http2Settings initialSettings) {
/*  95 */     this.initialSettings = (Http2Settings)ObjectUtil.checkNotNull(initialSettings, "initialSettings");
/*     */     
/*  97 */     Http2Connection connection = new DefaultHttp2Connection(server);
/*     */     
/*  99 */     Long maxHeaderListSize = initialSettings.maxHeaderListSize();
/*     */ 
/*     */     
/* 102 */     Http2FrameReader frameReader = new DefaultHttp2FrameReader((maxHeaderListSize == null) ? new DefaultHttp2HeadersDecoder(true) : new DefaultHttp2HeadersDecoder(true, maxHeaderListSize.longValue()));
/*     */     
/* 104 */     if (frameLogger != null) {
/* 105 */       frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger);
/* 106 */       frameReader = new Http2InboundFrameLogger(frameReader, frameLogger);
/*     */     } 
/* 108 */     this.encoder = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
/* 109 */     this.decoder = new DefaultHttp2ConnectionDecoder(connection, this.encoder, frameReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long gracefulShutdownTimeoutMillis() {
/* 118 */     return this.gracefulShutdownTimeoutMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/* 128 */     if (gracefulShutdownTimeoutMillis < -1L) {
/* 129 */       throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + gracefulShutdownTimeoutMillis + " (expected: -1 for indefinite or >= 0)");
/*     */     }
/*     */     
/* 132 */     this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
/*     */   }
/*     */   
/*     */   public Http2Connection connection() {
/* 136 */     return this.encoder.connection();
/*     */   }
/*     */   
/*     */   public Http2ConnectionDecoder decoder() {
/* 140 */     return this.decoder;
/*     */   }
/*     */   
/*     */   public Http2ConnectionEncoder encoder() {
/* 144 */     return this.encoder;
/*     */   }
/*     */   
/*     */   private boolean prefaceSent() {
/* 148 */     return (this.byteDecoder != null && this.byteDecoder.prefaceSent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHttpClientUpgrade() throws Http2Exception {
/* 156 */     if (connection().isServer()) {
/* 157 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client-side HTTP upgrade requested for a server", new Object[0]);
/*     */     }
/* 159 */     if (!prefaceSent())
/*     */     {
/*     */       
/* 162 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
/*     */     }
/* 164 */     if (this.decoder.prefaceReceived()) {
/* 165 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 169 */     connection().local().createStream(1, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHttpServerUpgrade(Http2Settings settings) throws Http2Exception {
/* 177 */     if (!connection().isServer()) {
/* 178 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server-side HTTP upgrade requested for a client", new Object[0]);
/*     */     }
/* 180 */     if (!prefaceSent())
/*     */     {
/*     */       
/* 183 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
/*     */     }
/* 185 */     if (this.decoder.prefaceReceived()) {
/* 186 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 190 */     this.encoder.remoteSettings(settings);
/*     */ 
/*     */     
/* 193 */     connection().remote().createStream(1, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) {
/*     */     try {
/* 200 */       this.encoder.flowController().writePendingBytes();
/* 201 */       ctx.flush();
/* 202 */     } catch (Http2Exception e) {
/* 203 */       onError(ctx, true, e);
/* 204 */     } catch (Throwable cause) {
/* 205 */       onError(ctx, true, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause, "Error flushing", new Object[0]));
/*     */     } 
/*     */   }
/*     */   
/*     */   private abstract class BaseDecoder { private BaseDecoder() {}
/*     */     
/*     */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}
/*     */     
/*     */     public void channelActive(ChannelHandlerContext ctx) throws Exception {}
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 216 */       Http2ConnectionHandler.this.encoder().close();
/* 217 */       Http2ConnectionHandler.this.decoder().close();
/*     */ 
/*     */ 
/*     */       
/* 221 */       Http2ConnectionHandler.this.connection().close((Promise<Void>)ctx.voidPromise());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean prefaceSent() {
/* 228 */       return true;
/*     */     }
/*     */     
/*     */     public abstract void decode(ChannelHandlerContext param1ChannelHandlerContext, ByteBuf param1ByteBuf, List<Object> param1List) throws Exception; }
/*     */   
/*     */   private final class PrefaceDecoder extends BaseDecoder {
/*     */     private ByteBuf clientPrefaceString;
/*     */     
/*     */     public PrefaceDecoder(ChannelHandlerContext ctx) throws Exception {
/* 237 */       this.clientPrefaceString = Http2ConnectionHandler.clientPrefaceString(Http2ConnectionHandler.this.encoder.connection());
/*     */ 
/*     */       
/* 240 */       sendPreface(ctx);
/*     */     }
/*     */     private boolean prefaceSent;
/*     */     
/*     */     public boolean prefaceSent() {
/* 245 */       return this.prefaceSent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */       try {
/* 251 */         if (ctx.channel().isActive() && readClientPrefaceString(in) && verifyFirstFrameIsSettings(in)) {
/*     */           
/* 253 */           Http2ConnectionHandler.this.byteDecoder = new Http2ConnectionHandler.FrameDecoder();
/* 254 */           Http2ConnectionHandler.this.byteDecoder.decode(ctx, in, out);
/*     */         } 
/* 256 */       } catch (Throwable e) {
/* 257 */         Http2ConnectionHandler.this.onError(ctx, false, e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 264 */       sendPreface(ctx);
/*     */     }
/*     */ 
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 269 */       cleanup();
/* 270 */       super.channelInactive(ctx);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 278 */       cleanup();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void cleanup() {
/* 285 */       if (this.clientPrefaceString != null) {
/* 286 */         this.clientPrefaceString.release();
/* 287 */         this.clientPrefaceString = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean readClientPrefaceString(ByteBuf in) throws Http2Exception {
/* 298 */       if (this.clientPrefaceString == null) {
/* 299 */         return true;
/*     */       }
/*     */       
/* 302 */       int prefaceRemaining = this.clientPrefaceString.readableBytes();
/* 303 */       int bytesRead = Math.min(in.readableBytes(), prefaceRemaining);
/*     */ 
/*     */       
/* 306 */       if (bytesRead == 0 || !ByteBufUtil.equals(in, in.readerIndex(), this.clientPrefaceString, this.clientPrefaceString
/* 307 */           .readerIndex(), bytesRead)) {
/*     */         
/* 309 */         int maxSearch = 1024;
/*     */         
/* 311 */         int http1Index = ByteBufUtil.indexOf(Http2ConnectionHandler.HTTP_1_X_BUF, in.slice(in.readerIndex(), Math.min(in.readableBytes(), maxSearch)));
/* 312 */         if (http1Index != -1) {
/* 313 */           String chunk = in.toString(in.readerIndex(), http1Index - in.readerIndex(), CharsetUtil.US_ASCII);
/* 314 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Unexpected HTTP/1.x request: %s", new Object[] { chunk });
/*     */         } 
/* 316 */         String receivedBytes = ByteBufUtil.hexDump(in, in.readerIndex(), 
/* 317 */             Math.min(in.readableBytes(), this.clientPrefaceString.readableBytes()));
/* 318 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP/2 client preface string missing or corrupt. Hex dump for received bytes: %s", new Object[] { receivedBytes });
/*     */       } 
/*     */       
/* 321 */       in.skipBytes(bytesRead);
/* 322 */       this.clientPrefaceString.skipBytes(bytesRead);
/*     */       
/* 324 */       if (!this.clientPrefaceString.isReadable()) {
/*     */         
/* 326 */         this.clientPrefaceString.release();
/* 327 */         this.clientPrefaceString = null;
/* 328 */         return true;
/*     */       } 
/* 330 */       return false;
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
/*     */     private boolean verifyFirstFrameIsSettings(ByteBuf in) throws Http2Exception {
/* 342 */       if (in.readableBytes() < 5)
/*     */       {
/* 344 */         return false;
/*     */       }
/*     */       
/* 347 */       short frameType = in.getUnsignedByte(in.readerIndex() + 3);
/* 348 */       short flags = in.getUnsignedByte(in.readerIndex() + 4);
/* 349 */       if (frameType != 4 || (flags & 0x1) != 0)
/* 350 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "First received frame was not SETTINGS. Hex dump for first 5 bytes: %s", new Object[] {
/*     */               
/* 352 */               ByteBufUtil.hexDump(in, in.readerIndex(), 5)
/*     */             }); 
/* 354 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void sendPreface(ChannelHandlerContext ctx) throws Exception {
/* 361 */       if (this.prefaceSent || !ctx.channel().isActive()) {
/*     */         return;
/*     */       }
/*     */       
/* 365 */       this.prefaceSent = true;
/*     */       
/* 367 */       boolean isClient = !Http2ConnectionHandler.this.connection().isServer();
/* 368 */       if (isClient)
/*     */       {
/* 370 */         ctx.write(Http2CodecUtil.connectionPrefaceBuf()).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */       }
/*     */ 
/*     */       
/* 374 */       Http2ConnectionHandler.this.encoder.writeSettings(ctx, Http2ConnectionHandler.this.initialSettings, ctx.newPromise()).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */ 
/*     */       
/* 377 */       if (isClient)
/*     */       {
/*     */ 
/*     */         
/* 381 */         Http2ConnectionHandler.this.userEventTriggered(ctx, Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE); } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final class FrameDecoder extends BaseDecoder {
/*     */     private FrameDecoder() {}
/*     */     
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */       try {
/* 390 */         Http2ConnectionHandler.this.decoder.decodeFrame(ctx, in, out);
/* 391 */       } catch (Throwable e) {
/* 392 */         Http2ConnectionHandler.this.onError(ctx, false, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 400 */     this.encoder.lifecycleManager(this);
/* 401 */     this.decoder.lifecycleManager(this);
/* 402 */     this.encoder.flowController().channelHandlerContext(ctx);
/* 403 */     this.decoder.flowController().channelHandlerContext(ctx);
/* 404 */     this.byteDecoder = new PrefaceDecoder(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
/* 409 */     if (this.byteDecoder != null) {
/* 410 */       this.byteDecoder.handlerRemoved(ctx);
/* 411 */       this.byteDecoder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 417 */     if (this.byteDecoder == null) {
/* 418 */       this.byteDecoder = new PrefaceDecoder(ctx);
/*     */     }
/* 420 */     this.byteDecoder.channelActive(ctx);
/* 421 */     super.channelActive(ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 427 */     super.channelInactive(ctx);
/* 428 */     if (this.byteDecoder != null) {
/* 429 */       this.byteDecoder.channelInactive(ctx);
/* 430 */       this.byteDecoder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/*     */     try {
/* 439 */       if (ctx.channel().isWritable()) {
/* 440 */         flush(ctx);
/*     */       }
/* 442 */       this.encoder.flowController().channelWritabilityChanged();
/*     */     } finally {
/* 444 */       super.channelWritabilityChanged(ctx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 450 */     this.byteDecoder.decode(ctx, in, out);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 455 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 461 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 466 */     ctx.disconnect(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 471 */     promise = promise.unvoid();
/*     */     
/* 473 */     if (!ctx.channel().isActive()) {
/* 474 */       ctx.close(promise);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 483 */     ChannelFuture future = connection().goAwaySent() ? ctx.write(Unpooled.EMPTY_BUFFER) : goAway(ctx, (Http2Exception)null);
/* 484 */     ctx.flush();
/* 485 */     doGracefulShutdown(ctx, future, promise);
/*     */   }
/*     */   
/*     */   private void doGracefulShutdown(ChannelHandlerContext ctx, ChannelFuture future, ChannelPromise promise) {
/* 489 */     if (isGracefulShutdownComplete()) {
/*     */       
/* 491 */       future.addListener((GenericFutureListener)new ClosingChannelFutureListener(ctx, promise));
/*     */     
/*     */     }
/* 494 */     else if (this.gracefulShutdownTimeoutMillis < 0L) {
/* 495 */       this.closeListener = new ClosingChannelFutureListener(ctx, promise);
/*     */     } else {
/* 497 */       this.closeListener = new ClosingChannelFutureListener(ctx, promise, this.gracefulShutdownTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 505 */     ctx.deregister(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception {
/* 510 */     ctx.read();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 515 */     ctx.write(msg, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/*     */     try {
/* 524 */       channelReadComplete0(ctx);
/*     */     } finally {
/* 526 */       flush(ctx);
/*     */     } 
/*     */   }
/*     */   
/*     */   void channelReadComplete0(ChannelHandlerContext ctx) throws Exception {
/* 531 */     super.channelReadComplete(ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 539 */     if (Http2CodecUtil.getEmbeddedHttp2Exception(cause) != null) {
/*     */       
/* 541 */       onError(ctx, false, cause);
/*     */     } else {
/* 543 */       super.exceptionCaught(ctx, cause);
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
/*     */   public void closeStreamLocal(Http2Stream stream, ChannelFuture future) {
/* 556 */     switch (stream.state()) {
/*     */       case GRACEFUL_SHUTDOWN:
/*     */       case null:
/* 559 */         stream.closeLocalSide();
/*     */         return;
/*     */     } 
/* 562 */     closeStream(stream, future);
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
/*     */   public void closeStreamRemote(Http2Stream stream, ChannelFuture future) {
/* 576 */     switch (stream.state()) {
/*     */       case null:
/*     */       case null:
/* 579 */         stream.closeRemoteSide();
/*     */         return;
/*     */     } 
/* 582 */     closeStream(stream, future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeStream(Http2Stream stream, ChannelFuture future) {
/* 589 */     stream.close();
/*     */     
/* 591 */     if (future.isDone()) {
/* 592 */       checkCloseConnection(future);
/*     */     } else {
/* 594 */       future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 597 */               Http2ConnectionHandler.this.checkCloseConnection(future);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(ChannelHandlerContext ctx, boolean outbound, Throwable cause) {
/* 608 */     Http2Exception embedded = Http2CodecUtil.getEmbeddedHttp2Exception(cause);
/* 609 */     if (Http2Exception.isStreamError(embedded)) {
/* 610 */       onStreamError(ctx, outbound, cause, (Http2Exception.StreamException)embedded);
/* 611 */     } else if (embedded instanceof Http2Exception.CompositeStreamException) {
/* 612 */       Http2Exception.CompositeStreamException compositException = (Http2Exception.CompositeStreamException)embedded;
/* 613 */       for (Http2Exception.StreamException streamException : compositException) {
/* 614 */         onStreamError(ctx, outbound, cause, streamException);
/*     */       }
/*     */     } else {
/* 617 */       onConnectionError(ctx, outbound, cause, embedded);
/*     */     } 
/* 619 */     ctx.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isGracefulShutdownComplete() {
/* 628 */     return (connection().numActiveStreams() == 0);
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
/*     */   protected void onConnectionError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception http2Ex) {
/* 643 */     if (http2Ex == null) {
/* 644 */       http2Ex = new Http2Exception(Http2Error.INTERNAL_ERROR, cause.getMessage(), cause);
/*     */     }
/*     */     
/* 647 */     ChannelPromise promise = ctx.newPromise();
/* 648 */     ChannelFuture future = goAway(ctx, http2Ex);
/* 649 */     switch (http2Ex.shutdownHint()) {
/*     */       case GRACEFUL_SHUTDOWN:
/* 651 */         doGracefulShutdown(ctx, future, promise);
/*     */         return;
/*     */     } 
/* 654 */     future.addListener((GenericFutureListener)new ClosingChannelFutureListener(ctx, promise));
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
/*     */   protected void onStreamError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception.StreamException http2Ex) {
/* 670 */     int streamId = http2Ex.streamId();
/* 671 */     Http2Stream stream = connection().stream(streamId);
/*     */ 
/*     */     
/* 674 */     if (http2Ex instanceof Http2Exception.HeaderListSizeException && ((Http2Exception.HeaderListSizeException)http2Ex)
/* 675 */       .duringDecode() && 
/* 676 */       connection().isServer()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 684 */       if (stream == null) {
/*     */         try {
/* 686 */           stream = this.encoder.connection().remote().createStream(streamId, true);
/* 687 */         } catch (Http2Exception e) {
/* 688 */           resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/*     */       
/* 694 */       if (stream != null && !stream.isHeadersSent()) {
/*     */         try {
/* 696 */           handleServerHeaderDecodeSizeError(ctx, stream);
/* 697 */         } catch (Throwable cause2) {
/* 698 */           onError(ctx, outbound, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause2, "Error DecodeSizeError", new Object[0]));
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 703 */     if (stream == null) {
/* 704 */       resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
/*     */     } else {
/* 706 */       resetStream(ctx, stream, http2Ex.error().code(), ctx.newPromise());
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
/*     */   protected void handleServerHeaderDecodeSizeError(ChannelHandlerContext ctx, Http2Stream stream) {
/* 718 */     encoder().writeHeaders(ctx, stream.id(), HEADERS_TOO_LARGE_HEADERS, 0, true, ctx.newPromise());
/*     */   }
/*     */   
/*     */   protected Http2FrameWriter frameWriter() {
/* 722 */     return encoder().frameWriter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture resetUnknownStream(final ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/* 732 */     ChannelFuture future = frameWriter().writeRstStream(ctx, streamId, errorCode, promise);
/* 733 */     if (future.isDone()) {
/* 734 */       closeConnectionOnError(ctx, future);
/*     */     } else {
/* 736 */       future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 739 */               Http2ConnectionHandler.this.closeConnectionOnError(ctx, future);
/*     */             }
/*     */           });
/*     */     } 
/* 743 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture resetStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/* 749 */     Http2Stream stream = connection().stream(streamId);
/* 750 */     if (stream == null) {
/* 751 */       return resetUnknownStream(ctx, streamId, errorCode, promise.unvoid());
/*     */     }
/*     */     
/* 754 */     return resetStream(ctx, stream, errorCode, promise);
/*     */   }
/*     */   
/*     */   private ChannelFuture resetStream(final ChannelHandlerContext ctx, final Http2Stream stream, long errorCode, ChannelPromise promise) {
/*     */     ChannelFuture future;
/* 759 */     promise = promise.unvoid();
/* 760 */     if (stream.isResetSent())
/*     */     {
/* 762 */       return (ChannelFuture)promise.setSuccess();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 767 */     if (stream.state() == Http2Stream.State.IDLE || (
/* 768 */       connection().local().created(stream) && !stream.isHeadersSent() && !stream.isPushPromiseSent())) {
/* 769 */       ChannelPromise channelPromise = promise.setSuccess();
/*     */     } else {
/* 771 */       future = frameWriter().writeRstStream(ctx, stream.id(), errorCode, promise);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 776 */     stream.resetSent();
/*     */     
/* 778 */     if (future.isDone()) {
/* 779 */       processRstStreamWriteResult(ctx, stream, future);
/*     */     } else {
/* 781 */       future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 784 */               Http2ConnectionHandler.this.processRstStreamWriteResult(ctx, stream, future);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 789 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture goAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, ChannelPromise promise) {
/*     */     try {
/* 796 */       promise = promise.unvoid();
/* 797 */       Http2Connection connection = connection();
/* 798 */       if (connection().goAwaySent()) {
/*     */ 
/*     */         
/* 801 */         if (lastStreamId == connection().remote().lastStreamKnownByPeer()) {
/*     */           
/* 803 */           debugData.release();
/* 804 */           return (ChannelFuture)promise.setSuccess();
/*     */         } 
/* 806 */         if (lastStreamId > connection.remote().lastStreamKnownByPeer()) {
/* 807 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Last stream identifier must not increase between sending multiple GOAWAY frames (was '%d', is '%d').", new Object[] {
/*     */                 
/* 809 */                 Integer.valueOf(connection.remote().lastStreamKnownByPeer()), Integer.valueOf(lastStreamId)
/*     */               });
/*     */         }
/*     */       } 
/* 813 */       connection.goAwaySent(lastStreamId, errorCode, debugData);
/*     */ 
/*     */ 
/*     */       
/* 817 */       debugData.retain();
/* 818 */       ChannelFuture future = frameWriter().writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */       
/* 820 */       if (future.isDone()) {
/* 821 */         processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future);
/*     */       } else {
/* 823 */         future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 826 */                 Http2ConnectionHandler.processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future);
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 831 */       return future;
/* 832 */     } catch (Throwable cause) {
/* 833 */       debugData.release();
/* 834 */       return (ChannelFuture)promise.setFailure(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCloseConnection(ChannelFuture future) {
/* 845 */     if (this.closeListener != null && isGracefulShutdownComplete()) {
/* 846 */       ChannelFutureListener closeListener = this.closeListener;
/*     */ 
/*     */       
/* 849 */       this.closeListener = null;
/*     */       try {
/* 851 */         closeListener.operationComplete((Future)future);
/* 852 */       } catch (Exception e) {
/* 853 */         throw new IllegalStateException("Close listener threw an unexpected exception", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture goAway(ChannelHandlerContext ctx, Http2Exception cause) {
/* 863 */     long errorCode = (cause != null) ? cause.error().code() : Http2Error.NO_ERROR.code();
/* 864 */     int lastKnownStream = connection().remote().lastStreamCreated();
/* 865 */     return goAway(ctx, lastKnownStream, errorCode, Http2CodecUtil.toByteBuf(ctx, cause), ctx.newPromise());
/*     */   }
/*     */   
/*     */   private void processRstStreamWriteResult(ChannelHandlerContext ctx, Http2Stream stream, ChannelFuture future) {
/* 869 */     if (future.isSuccess()) {
/* 870 */       closeStream(stream, future);
/*     */     } else {
/*     */       
/* 873 */       onConnectionError(ctx, true, future.cause(), (Http2Exception)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeConnectionOnError(ChannelHandlerContext ctx, ChannelFuture future) {
/* 878 */     if (!future.isSuccess()) {
/* 879 */       onConnectionError(ctx, true, future.cause(), (Http2Exception)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf clientPrefaceString(Http2Connection connection) {
/* 887 */     return connection.isServer() ? Http2CodecUtil.connectionPrefaceBuf() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void processGoAwayWriteResult(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelFuture future) {
/*     */     try {
/* 893 */       if (future.isSuccess()) {
/* 894 */         if (errorCode != Http2Error.NO_ERROR.code()) {
/* 895 */           if (logger.isDebugEnabled()) {
/* 896 */             logger.debug("{} Sent GOAWAY: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", new Object[] { ctx
/*     */                   
/* 898 */                   .channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause() });
/*     */           }
/* 900 */           ctx.close();
/*     */         } 
/*     */       } else {
/* 903 */         if (logger.isDebugEnabled()) {
/* 904 */           logger.debug("{} Sending GOAWAY failed: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", new Object[] { ctx
/*     */                 
/* 906 */                 .channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause() });
/*     */         }
/* 908 */         ctx.close();
/*     */       } 
/*     */     } finally {
/*     */       
/* 912 */       debugData.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ClosingChannelFutureListener
/*     */     implements ChannelFutureListener
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelPromise promise;
/*     */     private final ScheduledFuture<?> timeoutTask;
/*     */     
/*     */     ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 925 */       this.ctx = ctx;
/* 926 */       this.promise = promise;
/* 927 */       this.timeoutTask = null;
/*     */     }
/*     */ 
/*     */     
/*     */     ClosingChannelFutureListener(final ChannelHandlerContext ctx, final ChannelPromise promise, long timeout, TimeUnit unit) {
/* 932 */       this.ctx = ctx;
/* 933 */       this.promise = promise;
/* 934 */       this.timeoutTask = ctx.executor().schedule(new Runnable()
/*     */           {
/*     */             public void run() {
/* 937 */               ctx.close(promise);
/*     */             }
/*     */           },  timeout, unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void operationComplete(ChannelFuture sentGoAwayFuture) throws Exception {
/* 944 */       if (this.timeoutTask != null) {
/* 945 */         this.timeoutTask.cancel(false);
/*     */       }
/* 947 */       this.ctx.close(this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */