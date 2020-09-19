/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http2ConnectionHandlerBuilder
/*     */   extends AbstractHttp2ConnectionHandlerBuilder<Http2ConnectionHandler, Http2ConnectionHandlerBuilder>
/*     */ {
/*     */   public Http2ConnectionHandlerBuilder validateHeaders(boolean validateHeaders) {
/*  31 */     return super.validateHeaders(validateHeaders);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder initialSettings(Http2Settings settings) {
/*  36 */     return super.initialSettings(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder frameListener(Http2FrameListener frameListener) {
/*  41 */     return super.frameListener(frameListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/*  46 */     return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder server(boolean isServer) {
/*  51 */     return super.server(isServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder connection(Http2Connection connection) {
/*  56 */     return super.connection(connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder maxReservedStreams(int maxReservedStreams) {
/*  61 */     return super.maxReservedStreams(maxReservedStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
/*  66 */     return super.codec(decoder, encoder);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder frameLogger(Http2FrameLogger frameLogger) {
/*  71 */     return super.frameLogger(frameLogger);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
/*  77 */     return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder encoderIgnoreMaxHeaderListSize(boolean encoderIgnoreMaxHeaderListSize) {
/*  82 */     return super.encoderIgnoreMaxHeaderListSize(encoderIgnoreMaxHeaderListSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
/*  87 */     return super.headerSensitivityDetector(headerSensitivityDetector);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandlerBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
/*  92 */     return super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2ConnectionHandler build() {
/*  97 */     return super.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
/* 103 */     return new Http2ConnectionHandler(decoder, encoder, initialSettings);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ConnectionHandlerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */