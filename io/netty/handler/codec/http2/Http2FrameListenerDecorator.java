/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class Http2FrameListenerDecorator
/*     */   implements Http2FrameListener
/*     */ {
/*     */   protected final Http2FrameListener listener;
/*     */   
/*     */   public Http2FrameListenerDecorator(Http2FrameListener listener) {
/*  30 */     this.listener = (Http2FrameListener)ObjectUtil.checkNotNull(listener, "listener");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/*  36 */     return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
/*  42 */     this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
/*  48 */     this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
/*  54 */     this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
/*  59 */     this.listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
/*  64 */     this.listener.onSettingsAckRead(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
/*  69 */     this.listener.onSettingsRead(ctx, settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/*  74 */     this.listener.onPingRead(ctx, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
/*  79 */     this.listener.onPingAckRead(ctx, data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
/*  85 */     this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
/*  91 */     this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
/*  97 */     this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception {
/* 103 */     this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameListenerDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */