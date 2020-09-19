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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHttp2ConnectionHandlerBuilder<T extends Http2ConnectionHandler, B extends AbstractHttp2ConnectionHandlerBuilder<T, B>>
/*     */ {
/*  81 */   private static final Http2HeadersEncoder.SensitivityDetector DEFAULT_HEADER_SENSITIVITY_DETECTOR = Http2HeadersEncoder.NEVER_SENSITIVE;
/*     */ 
/*     */   
/*  84 */   private Http2Settings initialSettings = Http2Settings.defaultSettings();
/*     */   private Http2FrameListener frameListener;
/*  86 */   private long gracefulShutdownTimeoutMillis = Http2CodecUtil.DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS;
/*     */   
/*     */   private Boolean isServer;
/*     */   
/*     */   private Integer maxReservedStreams;
/*     */   
/*     */   private Http2Connection connection;
/*     */   
/*     */   private Http2ConnectionDecoder decoder;
/*     */   
/*     */   private Http2ConnectionEncoder encoder;
/*     */   
/*     */   private Boolean validateHeaders;
/*     */   
/*     */   private Http2FrameLogger frameLogger;
/*     */   
/*     */   private Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector;
/*     */   
/*     */   private Boolean encoderEnforceMaxConcurrentStreams;
/*     */   
/*     */   private Boolean encoderIgnoreMaxHeaderListSize;
/*     */   
/* 108 */   private int initialHuffmanDecodeCapacity = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2Settings initialSettings() {
/* 114 */     return this.initialSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B initialSettings(Http2Settings settings) {
/* 121 */     this.initialSettings = (Http2Settings)ObjectUtil.checkNotNull(settings, "settings");
/* 122 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2FrameListener frameListener() {
/* 131 */     return this.frameListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B frameListener(Http2FrameListener frameListener) {
/* 139 */     this.frameListener = (Http2FrameListener)ObjectUtil.checkNotNull(frameListener, "frameListener");
/* 140 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long gracefulShutdownTimeoutMillis() {
/* 148 */     return this.gracefulShutdownTimeoutMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
/* 155 */     if (gracefulShutdownTimeoutMillis < -1L) {
/* 156 */       throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + gracefulShutdownTimeoutMillis + " (expected: -1 for indefinite or >= 0)");
/*     */     }
/*     */     
/* 159 */     this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
/* 160 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isServer() {
/* 168 */     return (this.isServer != null) ? this.isServer.booleanValue() : true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B server(boolean isServer) {
/* 176 */     enforceConstraint("server", "connection", this.connection);
/* 177 */     enforceConstraint("server", "codec", this.decoder);
/* 178 */     enforceConstraint("server", "codec", this.encoder);
/*     */     
/* 180 */     this.isServer = Boolean.valueOf(isServer);
/* 181 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int maxReservedStreams() {
/* 192 */     return (this.maxReservedStreams != null) ? this.maxReservedStreams.intValue() : 100;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B maxReservedStreams(int maxReservedStreams) {
/* 199 */     enforceConstraint("server", "connection", this.connection);
/* 200 */     enforceConstraint("server", "codec", this.decoder);
/* 201 */     enforceConstraint("server", "codec", this.encoder);
/*     */     
/* 203 */     this.maxReservedStreams = Integer.valueOf(ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams"));
/* 204 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2Connection connection() {
/* 213 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B connection(Http2Connection connection) {
/* 220 */     enforceConstraint("connection", "maxReservedStreams", this.maxReservedStreams);
/* 221 */     enforceConstraint("connection", "server", this.isServer);
/* 222 */     enforceConstraint("connection", "codec", this.decoder);
/* 223 */     enforceConstraint("connection", "codec", this.encoder);
/*     */     
/* 225 */     this.connection = (Http2Connection)ObjectUtil.checkNotNull(connection, "connection");
/*     */     
/* 227 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2ConnectionDecoder decoder() {
/* 236 */     return this.decoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2ConnectionEncoder encoder() {
/* 245 */     return this.encoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
/* 252 */     enforceConstraint("codec", "server", this.isServer);
/* 253 */     enforceConstraint("codec", "maxReservedStreams", this.maxReservedStreams);
/* 254 */     enforceConstraint("codec", "connection", this.connection);
/* 255 */     enforceConstraint("codec", "frameLogger", this.frameLogger);
/* 256 */     enforceConstraint("codec", "validateHeaders", this.validateHeaders);
/* 257 */     enforceConstraint("codec", "headerSensitivityDetector", this.headerSensitivityDetector);
/* 258 */     enforceConstraint("codec", "encoderEnforceMaxConcurrentStreams", this.encoderEnforceMaxConcurrentStreams);
/*     */     
/* 260 */     ObjectUtil.checkNotNull(decoder, "decoder");
/* 261 */     ObjectUtil.checkNotNull(encoder, "encoder");
/*     */     
/* 263 */     if (decoder.connection() != encoder.connection()) {
/* 264 */       throw new IllegalArgumentException("The specified encoder and decoder have different connections.");
/*     */     }
/*     */     
/* 267 */     this.decoder = decoder;
/* 268 */     this.encoder = encoder;
/*     */     
/* 270 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidateHeaders() {
/* 278 */     return (this.validateHeaders != null) ? this.validateHeaders.booleanValue() : true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B validateHeaders(boolean validateHeaders) {
/* 286 */     enforceNonCodecConstraints("validateHeaders");
/* 287 */     this.validateHeaders = Boolean.valueOf(validateHeaders);
/* 288 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2FrameLogger frameLogger() {
/* 297 */     return this.frameLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B frameLogger(Http2FrameLogger frameLogger) {
/* 304 */     enforceNonCodecConstraints("frameLogger");
/* 305 */     this.frameLogger = (Http2FrameLogger)ObjectUtil.checkNotNull(frameLogger, "frameLogger");
/* 306 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean encoderEnforceMaxConcurrentStreams() {
/* 314 */     return (this.encoderEnforceMaxConcurrentStreams != null) ? this.encoderEnforceMaxConcurrentStreams.booleanValue() : false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams) {
/* 322 */     enforceNonCodecConstraints("encoderEnforceMaxConcurrentStreams");
/* 323 */     this.encoderEnforceMaxConcurrentStreams = Boolean.valueOf(encoderEnforceMaxConcurrentStreams);
/* 324 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
/* 331 */     return (this.headerSensitivityDetector != null) ? this.headerSensitivityDetector : DEFAULT_HEADER_SENSITIVITY_DETECTOR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
/* 338 */     enforceNonCodecConstraints("headerSensitivityDetector");
/* 339 */     this.headerSensitivityDetector = (Http2HeadersEncoder.SensitivityDetector)ObjectUtil.checkNotNull(headerSensitivityDetector, "headerSensitivityDetector");
/* 340 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize) {
/* 351 */     enforceNonCodecConstraints("encoderIgnoreMaxHeaderListSize");
/* 352 */     this.encoderIgnoreMaxHeaderListSize = Boolean.valueOf(ignoreMaxHeaderListSize);
/* 353 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected B initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity) {
/* 362 */     enforceNonCodecConstraints("initialHuffmanDecodeCapacity");
/* 363 */     this.initialHuffmanDecodeCapacity = ObjectUtil.checkPositive(initialHuffmanDecodeCapacity, "initialHuffmanDecodeCapacity");
/* 364 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T build() {
/* 371 */     if (this.encoder != null) {
/* 372 */       assert this.decoder != null;
/* 373 */       return buildFromCodec(this.decoder, this.encoder);
/*     */     } 
/*     */     
/* 376 */     Http2Connection connection = this.connection;
/* 377 */     if (connection == null) {
/* 378 */       connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
/*     */     }
/*     */     
/* 381 */     return buildFromConnection(connection);
/*     */   }
/*     */   
/*     */   private T buildFromConnection(Http2Connection connection) {
/* 385 */     Long maxHeaderListSize = this.initialSettings.maxHeaderListSize();
/*     */     
/* 387 */     Http2FrameReader reader = new DefaultHttp2FrameReader(new DefaultHttp2HeadersDecoder(isValidateHeaders(), (maxHeaderListSize == null) ? 8192L : maxHeaderListSize.longValue(), this.initialHuffmanDecodeCapacity));
/*     */ 
/*     */ 
/*     */     
/* 391 */     Http2FrameWriter writer = (this.encoderIgnoreMaxHeaderListSize == null) ? new DefaultHttp2FrameWriter(headerSensitivityDetector()) : new DefaultHttp2FrameWriter(headerSensitivityDetector(), this.encoderIgnoreMaxHeaderListSize.booleanValue());
/*     */     
/* 393 */     if (this.frameLogger != null) {
/* 394 */       reader = new Http2InboundFrameLogger(reader, this.frameLogger);
/* 395 */       writer = new Http2OutboundFrameLogger(writer, this.frameLogger);
/*     */     } 
/*     */     
/* 398 */     Http2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection, writer);
/* 399 */     boolean encoderEnforceMaxConcurrentStreams = encoderEnforceMaxConcurrentStreams();
/*     */     
/* 401 */     if (encoderEnforceMaxConcurrentStreams) {
/* 402 */       if (connection.isServer()) {
/* 403 */         encoder.close();
/* 404 */         reader.close();
/* 405 */         throw new IllegalArgumentException("encoderEnforceMaxConcurrentStreams: " + encoderEnforceMaxConcurrentStreams + " not supported for server");
/*     */       } 
/*     */ 
/*     */       
/* 409 */       encoder = new StreamBufferingEncoder(encoder);
/*     */     } 
/*     */     
/* 412 */     Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, reader);
/* 413 */     return buildFromCodec(decoder, encoder);
/*     */   }
/*     */ 
/*     */   
/*     */   private T buildFromCodec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
/*     */     T handler;
/*     */     try {
/* 420 */       handler = build(decoder, encoder, this.initialSettings);
/* 421 */     } catch (Throwable t) {
/* 422 */       encoder.close();
/* 423 */       decoder.close();
/* 424 */       throw new IllegalStateException("failed to build a Http2ConnectionHandler", t);
/*     */     } 
/*     */ 
/*     */     
/* 428 */     handler.gracefulShutdownTimeoutMillis(this.gracefulShutdownTimeoutMillis);
/* 429 */     if (handler.decoder().frameListener() == null) {
/* 430 */       handler.decoder().frameListener(this.frameListener);
/*     */     }
/* 432 */     return handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T build(Http2ConnectionDecoder paramHttp2ConnectionDecoder, Http2ConnectionEncoder paramHttp2ConnectionEncoder, Http2Settings paramHttp2Settings) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final B self() {
/* 452 */     return (B)this;
/*     */   }
/*     */   
/*     */   private void enforceNonCodecConstraints(String rejected) {
/* 456 */     enforceConstraint(rejected, "server/connection", this.decoder);
/* 457 */     enforceConstraint(rejected, "server/connection", this.encoder);
/*     */   }
/*     */   
/*     */   private static void enforceConstraint(String methodName, String rejectorName, Object value) {
/* 461 */     if (value != null)
/* 462 */       throw new IllegalStateException(methodName + "() cannot be called because " + rejectorName + "() has been called already."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\AbstractHttp2ConnectionHandlerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */