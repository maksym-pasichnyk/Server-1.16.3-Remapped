/*     */ package io.netty.handler.codec.http2;
/*     */ 
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
/*     */ 
/*     */ public class Http2FrameCodecBuilder
/*     */   extends AbstractHttp2ConnectionHandlerBuilder<Http2FrameCodec, Http2FrameCodecBuilder>
/*     */ {
/*     */   private Http2FrameWriter frameWriter;
/*     */   
/*     */   Http2FrameCodecBuilder(boolean server) {
/*  33 */     server(server);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Http2FrameCodecBuilder forClient() {
/*  40 */     return new Http2FrameCodecBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Http2FrameCodecBuilder forServer() {
/*  47 */     return new Http2FrameCodecBuilder(true);
/*     */   }
/*     */ 
/*     */   
/*     */   Http2FrameCodecBuilder frameWriter(Http2FrameWriter frameWriter) {
/*  52 */     this.frameWriter = (Http2FrameWriter)ObjectUtil.checkNotNull(frameWriter, "frameWriter");
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Settings initialSettings() {
/*  58 */     return super.initialSettings();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder initialSettings(Http2Settings settings) {
/*  63 */     return super.initialSettings(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public long gracefulShutdownTimeoutMillis() {
/*  68 */     return super.gracefulShutdownTimeoutMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/*  73 */     return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isServer() {
/*  78 */     return super.isServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxReservedStreams() {
/*  83 */     return super.maxReservedStreams();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder maxReservedStreams(int maxReservedStreams) {
/*  88 */     return super.maxReservedStreams(maxReservedStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidateHeaders() {
/*  93 */     return super.isValidateHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder validateHeaders(boolean validateHeaders) {
/*  98 */     return super.validateHeaders(validateHeaders);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameLogger frameLogger() {
/* 103 */     return super.frameLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder frameLogger(Http2FrameLogger frameLogger) {
/* 108 */     return super.frameLogger(frameLogger);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean encoderEnforceMaxConcurrentStreams() {
/* 113 */     return super.encoderEnforceMaxConcurrentStreams();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
/* 118 */     return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
/* 123 */     return super.headerSensitivityDetector();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
/* 129 */     return super.headerSensitivityDetector(headerSensitivityDetector);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
/* 134 */     return super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameCodecBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
/* 139 */     return super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2FrameCodec build() {
/* 147 */     Http2FrameWriter frameWriter = this.frameWriter;
/* 148 */     if (frameWriter != null) {
/*     */ 
/*     */       
/* 151 */       DefaultHttp2Connection connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
/* 152 */       Long maxHeaderListSize = initialSettings().maxHeaderListSize();
/*     */ 
/*     */       
/* 155 */       Http2FrameReader frameReader = new DefaultHttp2FrameReader((maxHeaderListSize == null) ? new DefaultHttp2HeadersDecoder(true) : new DefaultHttp2HeadersDecoder(true, maxHeaderListSize.longValue()));
/*     */       
/* 157 */       if (frameLogger() != null) {
/* 158 */         frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger());
/* 159 */         frameReader = new Http2InboundFrameLogger(frameReader, frameLogger());
/*     */       } 
/* 161 */       Http2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
/* 162 */       if (encoderEnforceMaxConcurrentStreams()) {
/* 163 */         encoder = new StreamBufferingEncoder(encoder);
/*     */       }
/* 165 */       Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, frameReader);
/*     */       
/* 167 */       return build(decoder, encoder, initialSettings());
/*     */     } 
/* 169 */     return super.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2FrameCodec build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
/* 175 */     return new Http2FrameCodec(encoder, decoder, initialSettings);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameCodecBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */