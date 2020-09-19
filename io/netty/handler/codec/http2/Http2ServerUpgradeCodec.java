/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpServerUpgradeHandler;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.CharBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Http2ServerUpgradeCodec
/*     */   implements HttpServerUpgradeHandler.UpgradeCodec
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Http2ServerUpgradeCodec.class);
/*     */   
/*  51 */   private static final List<CharSequence> REQUIRED_UPGRADE_HEADERS = Collections.singletonList(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
/*  52 */   private static final ChannelHandler[] EMPTY_HANDLERS = new ChannelHandler[0];
/*     */ 
/*     */   
/*     */   private final String handlerName;
/*     */ 
/*     */   
/*     */   private final Http2ConnectionHandler connectionHandler;
/*     */   
/*     */   private final ChannelHandler[] handlers;
/*     */   
/*     */   private final Http2FrameReader frameReader;
/*     */   
/*     */   private Http2Settings settings;
/*     */ 
/*     */   
/*     */   public Http2ServerUpgradeCodec(Http2ConnectionHandler connectionHandler) {
/*  68 */     this(null, connectionHandler, EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ServerUpgradeCodec(Http2MultiplexCodec http2Codec) {
/*  78 */     this(null, http2Codec, EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ServerUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler) {
/*  89 */     this(handlerName, connectionHandler, EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ServerUpgradeCodec(String handlerName, Http2MultiplexCodec http2Codec) {
/*  99 */     this(handlerName, http2Codec, EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2ServerUpgradeCodec(Http2FrameCodec http2Codec, ChannelHandler... handlers) {
/* 110 */     this(null, http2Codec, handlers);
/*     */   }
/*     */ 
/*     */   
/*     */   private Http2ServerUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler, ChannelHandler... handlers) {
/* 115 */     this.handlerName = handlerName;
/* 116 */     this.connectionHandler = connectionHandler;
/* 117 */     this.handlers = handlers;
/* 118 */     this.frameReader = new DefaultHttp2FrameReader();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<CharSequence> requiredUpgradeHeaders() {
/* 123 */     return REQUIRED_UPGRADE_HEADERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean prepareUpgradeResponse(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest, HttpHeaders headers) {
/*     */     try {
/* 132 */       List<String> upgradeHeaders = upgradeRequest.headers().getAll(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
/* 133 */       if (upgradeHeaders.isEmpty() || upgradeHeaders.size() > 1) {
/* 134 */         throw new IllegalArgumentException("There must be 1 and only 1 " + Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER + " header.");
/*     */       }
/*     */       
/* 137 */       this.settings = decodeSettingsHeader(ctx, upgradeHeaders.get(0));
/*     */       
/* 139 */       return true;
/* 140 */     } catch (Throwable cause) {
/* 141 */       logger.info("Error during upgrade to HTTP/2", cause);
/* 142 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeTo(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest) {
/*     */     try {
/* 150 */       ctx.pipeline().addAfter(ctx.name(), this.handlerName, (ChannelHandler)this.connectionHandler);
/* 151 */       this.connectionHandler.onHttpServerUpgrade(this.settings);
/*     */     }
/* 153 */     catch (Http2Exception e) {
/* 154 */       ctx.fireExceptionCaught(e);
/* 155 */       ctx.close();
/*     */       
/*     */       return;
/*     */     } 
/* 159 */     if (this.handlers != null) {
/* 160 */       String name = ctx.pipeline().context((ChannelHandler)this.connectionHandler).name();
/* 161 */       for (int i = this.handlers.length - 1; i >= 0; i--) {
/* 162 */         ctx.pipeline().addAfter(name, null, this.handlers[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Http2Settings decodeSettingsHeader(ChannelHandlerContext ctx, CharSequence settingsHeader) throws Http2Exception {
/* 172 */     ByteBuf header = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(settingsHeader), CharsetUtil.UTF_8);
/*     */     
/*     */     try {
/* 175 */       ByteBuf payload = Base64.decode(header, Base64Dialect.URL_SAFE);
/*     */ 
/*     */       
/* 178 */       ByteBuf frame = createSettingsFrame(ctx, payload);
/*     */ 
/*     */       
/* 181 */       return decodeSettings(ctx, frame);
/*     */     } finally {
/* 183 */       header.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Http2Settings decodeSettings(ChannelHandlerContext ctx, ByteBuf frame) throws Http2Exception {
/*     */     try {
/* 192 */       final Http2Settings decodedSettings = new Http2Settings();
/* 193 */       this.frameReader.readFrame(ctx, frame, new Http2FrameAdapter()
/*     */           {
/*     */             public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) {
/* 196 */               decodedSettings.copyFrom(settings);
/*     */             }
/*     */           });
/* 199 */       return decodedSettings;
/*     */     } finally {
/* 201 */       frame.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf createSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload) {
/* 209 */     ByteBuf frame = ctx.alloc().buffer(9 + payload.readableBytes());
/* 210 */     Http2CodecUtil.writeFrameHeader(frame, payload.readableBytes(), (byte)4, new Http2Flags(), 0);
/* 211 */     frame.writeBytes(payload);
/* 212 */     payload.release();
/* 213 */     return frame;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ServerUpgradeCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */