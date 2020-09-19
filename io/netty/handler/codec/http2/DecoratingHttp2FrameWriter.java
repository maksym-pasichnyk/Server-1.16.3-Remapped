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
/*     */ public class DecoratingHttp2FrameWriter
/*     */   implements Http2FrameWriter
/*     */ {
/*     */   private final Http2FrameWriter delegate;
/*     */   
/*     */   public DecoratingHttp2FrameWriter(Http2FrameWriter delegate) {
/*  33 */     this.delegate = (Http2FrameWriter)ObjectUtil.checkNotNull(delegate, "delegate");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise) {
/*  39 */     return this.delegate.writeData(ctx, streamId, data, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
/*  45 */     return this.delegate.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise) {
/*  52 */     return this.delegate
/*  53 */       .writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
/*  59 */     return this.delegate.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/*  65 */     return this.delegate.writeRstStream(ctx, streamId, errorCode, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
/*  70 */     return this.delegate.writeSettings(ctx, settings, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
/*  75 */     return this.delegate.writeSettingsAck(ctx, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
/*  80 */     return this.delegate.writePing(ctx, ack, data, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
/*  86 */     return this.delegate.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
/*  92 */     return this.delegate.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
/*  98 */     return this.delegate.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
/* 104 */     return this.delegate.writeFrame(ctx, frameType, streamId, flags, payload, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration() {
/* 109 */     return this.delegate.configuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 114 */     this.delegate.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DecoratingHttp2FrameWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */