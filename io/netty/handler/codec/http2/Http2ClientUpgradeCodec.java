/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpClientUpgradeHandler;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.collection.CharObjectMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class Http2ClientUpgradeCodec
/*     */   implements HttpClientUpgradeHandler.UpgradeCodec
/*     */ {
/*  45 */   private static final List<CharSequence> UPGRADE_HEADERS = Collections.singletonList(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
/*     */   
/*     */   private final String handlerName;
/*     */   private final Http2ConnectionHandler connectionHandler;
/*     */   private final ChannelHandler upgradeToHandler;
/*     */   
/*     */   public Http2ClientUpgradeCodec(Http2FrameCodec frameCodec, ChannelHandler upgradeToHandler) {
/*  52 */     this((String)null, frameCodec, upgradeToHandler);
/*     */   }
/*     */   
/*     */   public Http2ClientUpgradeCodec(String handlerName, Http2FrameCodec frameCodec, ChannelHandler upgradeToHandler) {
/*  56 */     this(handlerName, frameCodec, upgradeToHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ClientUpgradeCodec(Http2ConnectionHandler connectionHandler) {
/*  66 */     this((String)null, connectionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ClientUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler) {
/*  77 */     this(handlerName, connectionHandler, (ChannelHandler)connectionHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   private Http2ClientUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler, ChannelHandler upgradeToHandler) {
/*  82 */     this.handlerName = handlerName;
/*  83 */     this.connectionHandler = (Http2ConnectionHandler)ObjectUtil.checkNotNull(connectionHandler, "connectionHandler");
/*  84 */     this.upgradeToHandler = (ChannelHandler)ObjectUtil.checkNotNull(upgradeToHandler, "upgradeToHandler");
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence protocol() {
/*  89 */     return Http2CodecUtil.HTTP_UPGRADE_PROTOCOL_NAME;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<CharSequence> setUpgradeHeaders(ChannelHandlerContext ctx, HttpRequest upgradeRequest) {
/*  95 */     CharSequence settingsValue = getSettingsHeaderValue(ctx);
/*  96 */     upgradeRequest.headers().set(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER, settingsValue);
/*  97 */     return UPGRADE_HEADERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeTo(ChannelHandlerContext ctx, FullHttpResponse upgradeResponse) throws Exception {
/* 104 */     ctx.pipeline().addAfter(ctx.name(), this.handlerName, this.upgradeToHandler);
/*     */ 
/*     */     
/* 107 */     this.connectionHandler.onHttpClientUpgrade();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharSequence getSettingsHeaderValue(ChannelHandlerContext ctx) {
/* 115 */     ByteBuf buf = null;
/* 116 */     ByteBuf encodedBuf = null;
/*     */     
/*     */     try {
/* 119 */       Http2Settings settings = this.connectionHandler.decoder().localSettings();
/*     */ 
/*     */       
/* 122 */       int payloadLength = 6 * settings.size();
/* 123 */       buf = ctx.alloc().buffer(payloadLength);
/* 124 */       for (CharObjectMap.PrimitiveEntry<Long> entry : (Iterable<CharObjectMap.PrimitiveEntry<Long>>)settings.entries()) {
/* 125 */         buf.writeChar(entry.key());
/* 126 */         buf.writeInt(((Long)entry.value()).intValue());
/*     */       } 
/*     */ 
/*     */       
/* 130 */       encodedBuf = Base64.encode(buf, Base64Dialect.URL_SAFE);
/* 131 */       return encodedBuf.toString(CharsetUtil.UTF_8);
/*     */     } finally {
/* 133 */       ReferenceCountUtil.release(buf);
/* 134 */       ReferenceCountUtil.release(encodedBuf);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ClientUpgradeCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */