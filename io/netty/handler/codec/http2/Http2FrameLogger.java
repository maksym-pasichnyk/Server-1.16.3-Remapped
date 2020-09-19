/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.logging.LogLevel;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogLevel;
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
/*     */ public class Http2FrameLogger
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private static final int BUFFER_LENGTH_THRESHOLD = 64;
/*     */   private final InternalLogger logger;
/*     */   private final InternalLogLevel level;
/*     */   
/*     */   public enum Direction
/*     */   {
/*  38 */     INBOUND,
/*  39 */     OUTBOUND;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2FrameLogger(LogLevel level) {
/*  47 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(Http2FrameLogger.class));
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(LogLevel level, String name) {
/*  51 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(name));
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(LogLevel level, Class<?> clazz) {
/*  55 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(clazz));
/*     */   }
/*     */   
/*     */   private Http2FrameLogger(InternalLogLevel level, InternalLogger logger) {
/*  59 */     this.level = (InternalLogLevel)ObjectUtil.checkNotNull(level, "level");
/*  60 */     this.logger = (InternalLogger)ObjectUtil.checkNotNull(logger, "logger");
/*     */   }
/*     */ 
/*     */   
/*     */   public void logData(Direction direction, ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream) {
/*  65 */     this.logger.log(this.level, "{} {} DATA: streamId={} padding={} endStream={} length={} bytes={}", new Object[] { ctx.channel(), direction
/*  66 */           .name(), Integer.valueOf(streamId), Integer.valueOf(padding), Boolean.valueOf(endStream), Integer.valueOf(data.readableBytes()), toString(data) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) {
/*  71 */     this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} padding={} endStream={}", new Object[] { ctx.channel(), direction
/*  72 */           .name(), Integer.valueOf(streamId), headers, Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) {
/*  77 */     this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} streamDependency={} weight={} exclusive={} padding={} endStream={}", new Object[] { ctx
/*  78 */           .channel(), direction
/*  79 */           .name(), Integer.valueOf(streamId), headers, Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive), Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logPriority(Direction direction, ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) {
/*  84 */     this.logger.log(this.level, "{} {} PRIORITY: streamId={} streamDependency={} weight={} exclusive={}", new Object[] { ctx.channel(), direction
/*  85 */           .name(), Integer.valueOf(streamId), Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive) });
/*     */   }
/*     */   
/*     */   public void logRstStream(Direction direction, ChannelHandlerContext ctx, int streamId, long errorCode) {
/*  89 */     this.logger.log(this.level, "{} {} RST_STREAM: streamId={} errorCode={}", new Object[] { ctx.channel(), direction
/*  90 */           .name(), Integer.valueOf(streamId), Long.valueOf(errorCode) });
/*     */   }
/*     */   
/*     */   public void logSettingsAck(Direction direction, ChannelHandlerContext ctx) {
/*  94 */     this.logger.log(this.level, "{} {} SETTINGS: ack=true", ctx.channel(), direction.name());
/*     */   }
/*     */   
/*     */   public void logSettings(Direction direction, ChannelHandlerContext ctx, Http2Settings settings) {
/*  98 */     this.logger.log(this.level, "{} {} SETTINGS: ack=false settings={}", new Object[] { ctx.channel(), direction.name(), settings });
/*     */   }
/*     */   
/*     */   public void logPing(Direction direction, ChannelHandlerContext ctx, long data) {
/* 102 */     this.logger.log(this.level, "{} {} PING: ack=false bytes={}", new Object[] { ctx.channel(), direction
/* 103 */           .name(), Long.valueOf(data) });
/*     */   }
/*     */   
/*     */   public void logPingAck(Direction direction, ChannelHandlerContext ctx, long data) {
/* 107 */     this.logger.log(this.level, "{} {} PING: ack=true bytes={}", new Object[] { ctx.channel(), direction
/* 108 */           .name(), Long.valueOf(data) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logPushPromise(Direction direction, ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) {
/* 113 */     this.logger.log(this.level, "{} {} PUSH_PROMISE: streamId={} promisedStreamId={} headers={} padding={}", new Object[] { ctx.channel(), direction
/* 114 */           .name(), Integer.valueOf(streamId), Integer.valueOf(promisedStreamId), headers, Integer.valueOf(padding) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logGoAway(Direction direction, ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) {
/* 119 */     this.logger.log(this.level, "{} {} GO_AWAY: lastStreamId={} errorCode={} length={} bytes={}", new Object[] { ctx.channel(), direction
/* 120 */           .name(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), Integer.valueOf(debugData.readableBytes()), toString(debugData) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logWindowsUpdate(Direction direction, ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) {
/* 125 */     this.logger.log(this.level, "{} {} WINDOW_UPDATE: streamId={} windowSizeIncrement={}", new Object[] { ctx.channel(), direction
/* 126 */           .name(), Integer.valueOf(streamId), Integer.valueOf(windowSizeIncrement) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logUnknownFrame(Direction direction, ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf data) {
/* 131 */     this.logger.log(this.level, "{} {} UNKNOWN: frameType={} streamId={} flags={} length={} bytes={}", new Object[] { ctx.channel(), direction
/* 132 */           .name(), Integer.valueOf(frameType & 0xFF), Integer.valueOf(streamId), Short.valueOf(flags.value()), Integer.valueOf(data.readableBytes()), toString(data) });
/*     */   }
/*     */   
/*     */   private String toString(ByteBuf buf) {
/* 136 */     if (!this.logger.isEnabled(this.level)) {
/* 137 */       return "";
/*     */     }
/*     */     
/* 140 */     if (this.level == InternalLogLevel.TRACE || buf.readableBytes() <= 64)
/*     */     {
/* 142 */       return ByteBufUtil.hexDump(buf);
/*     */     }
/*     */ 
/*     */     
/* 146 */     int length = Math.min(buf.readableBytes(), 64);
/* 147 */     return ByteBufUtil.hexDump(buf, buf.readerIndex(), length) + "...";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */