/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
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
/*     */ public class Http2OutboundFrameLogger
/*     */   implements Http2FrameWriter
/*     */ {
/*     */   private final Http2FrameWriter writer;
/*     */   private final Http2FrameLogger logger;
/*     */   
/*     */   public Http2OutboundFrameLogger(Http2FrameWriter writer, Http2FrameLogger logger) {
/*  36 */     this.writer = (Http2FrameWriter)ObjectUtil.checkNotNull(writer, "writer");
/*  37 */     this.logger = (Http2FrameLogger)ObjectUtil.checkNotNull(logger, "logger");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise) {
/*  43 */     this.logger.logData(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, data, padding, endStream);
/*  44 */     return this.writer.writeData(ctx, streamId, data, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
/*  50 */     this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, headers, padding, endStream);
/*  51 */     return this.writer.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise) {
/*  58 */     this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */     
/*  60 */     return this.writer.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
/*  67 */     this.logger.logPriority(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, streamDependency, weight, exclusive);
/*  68 */     return this.writer.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/*  74 */     this.logger.logRstStream(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, errorCode);
/*  75 */     return this.writer.writeRstStream(ctx, streamId, errorCode, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
/*  81 */     this.logger.logSettings(Http2FrameLogger.Direction.OUTBOUND, ctx, settings);
/*  82 */     return this.writer.writeSettings(ctx, settings, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
/*  87 */     this.logger.logSettingsAck(Http2FrameLogger.Direction.OUTBOUND, ctx);
/*  88 */     return this.writer.writeSettingsAck(ctx, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
/*  94 */     if (ack) {
/*  95 */       this.logger.logPingAck(Http2FrameLogger.Direction.OUTBOUND, ctx, data);
/*     */     } else {
/*  97 */       this.logger.logPing(Http2FrameLogger.Direction.OUTBOUND, ctx, data);
/*     */     } 
/*  99 */     return this.writer.writePing(ctx, ack, data, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
/* 105 */     this.logger.logPushPromise(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, promisedStreamId, headers, padding);
/* 106 */     return this.writer.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
/* 112 */     this.logger.logGoAway(Http2FrameLogger.Direction.OUTBOUND, ctx, lastStreamId, errorCode, debugData);
/* 113 */     return this.writer.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
/* 119 */     this.logger.logWindowsUpdate(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, windowSizeIncrement);
/* 120 */     return this.writer.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
/* 126 */     this.logger.logUnknownFrame(Http2FrameLogger.Direction.OUTBOUND, ctx, frameType, streamId, flags, payload);
/* 127 */     return this.writer.writeFrame(ctx, frameType, streamId, flags, payload, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 132 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration() {
/* 137 */     return this.writer.configuration();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2OutboundFrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */