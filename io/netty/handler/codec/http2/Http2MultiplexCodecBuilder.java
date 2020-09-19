/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Http2MultiplexCodecBuilder
/*     */   extends AbstractHttp2ConnectionHandlerBuilder<Http2MultiplexCodec, Http2MultiplexCodecBuilder>
/*     */ {
/*     */   final ChannelHandler childHandler;
/*     */   
/*     */   Http2MultiplexCodecBuilder(boolean server, ChannelHandler childHandler) {
/*  34 */     server(server);
/*  35 */     this.childHandler = checkSharable((ChannelHandler)ObjectUtil.checkNotNull(childHandler, "childHandler"));
/*     */   }
/*     */   
/*     */   private static ChannelHandler checkSharable(ChannelHandler handler) {
/*  39 */     if (handler instanceof ChannelHandlerAdapter && !((ChannelHandlerAdapter)handler).isSharable() && 
/*  40 */       !handler.getClass().isAnnotationPresent((Class)ChannelHandler.Sharable.class)) {
/*  41 */       throw new IllegalArgumentException("The handler must be Sharable");
/*     */     }
/*  43 */     return handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Http2MultiplexCodecBuilder forClient(ChannelHandler childHandler) {
/*  53 */     return new Http2MultiplexCodecBuilder(false, childHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Http2MultiplexCodecBuilder forServer(ChannelHandler childHandler) {
/*  63 */     return new Http2MultiplexCodecBuilder(true, childHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Settings initialSettings() {
/*  68 */     return super.initialSettings();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder initialSettings(Http2Settings settings) {
/*  73 */     return super.initialSettings(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public long gracefulShutdownTimeoutMillis() {
/*  78 */     return super.gracefulShutdownTimeoutMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/*  83 */     return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isServer() {
/*  88 */     return super.isServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxReservedStreams() {
/*  93 */     return super.maxReservedStreams();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder maxReservedStreams(int maxReservedStreams) {
/*  98 */     return super.maxReservedStreams(maxReservedStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidateHeaders() {
/* 103 */     return super.isValidateHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder validateHeaders(boolean validateHeaders) {
/* 108 */     return super.validateHeaders(validateHeaders);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameLogger frameLogger() {
/* 113 */     return super.frameLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder frameLogger(Http2FrameLogger frameLogger) {
/* 118 */     return super.frameLogger(frameLogger);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean encoderEnforceMaxConcurrentStreams() {
/* 123 */     return super.encoderEnforceMaxConcurrentStreams();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
/* 128 */     return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
/* 133 */     return super.headerSensitivityDetector();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
/* 139 */     return super.headerSensitivityDetector(headerSensitivityDetector);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
/* 144 */     return super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodecBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
/* 149 */     return super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2MultiplexCodec build() {
/* 154 */     return super.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2MultiplexCodec build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
/* 160 */     return new Http2MultiplexCodec(encoder, decoder, initialSettings, this.childHandler);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2MultiplexCodecBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */