/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpToHttp2ConnectionHandlerBuilder
/*    */   extends AbstractHttp2ConnectionHandlerBuilder<HttpToHttp2ConnectionHandler, HttpToHttp2ConnectionHandlerBuilder>
/*    */ {
/*    */   public HttpToHttp2ConnectionHandlerBuilder validateHeaders(boolean validateHeaders) {
/* 31 */     return super.validateHeaders(validateHeaders);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder initialSettings(Http2Settings settings) {
/* 36 */     return super.initialSettings(settings);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder frameListener(Http2FrameListener frameListener) {
/* 41 */     return super.frameListener(frameListener);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/* 46 */     return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder server(boolean isServer) {
/* 51 */     return super.server(isServer);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder connection(Http2Connection connection) {
/* 56 */     return super.connection(connection);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
/* 62 */     return super.codec(decoder, encoder);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder frameLogger(Http2FrameLogger frameLogger) {
/* 67 */     return super.frameLogger(frameLogger);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
/* 73 */     return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
/* 79 */     return super.headerSensitivityDetector(headerSensitivityDetector);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandlerBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
/* 84 */     return super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpToHttp2ConnectionHandler build() {
/* 89 */     return super.build();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpToHttp2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
/* 95 */     return new HttpToHttp2ConnectionHandler(decoder, encoder, initialSettings, isValidateHeaders());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HttpToHttp2ConnectionHandlerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */