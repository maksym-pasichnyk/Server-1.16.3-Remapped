/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class HttpServerUpgradeHandler
/*     */   extends HttpObjectAggregator
/*     */ {
/*     */   private final SourceCodec sourceCodec;
/*     */   private final UpgradeCodecFactory upgradeCodecFactory;
/*     */   private boolean handlingUpgrade;
/*     */   
/*     */   public static final class UpgradeEvent
/*     */     implements ReferenceCounted
/*     */   {
/*     */     private final CharSequence protocol;
/*     */     private final FullHttpRequest upgradeRequest;
/*     */     
/*     */     UpgradeEvent(CharSequence protocol, FullHttpRequest upgradeRequest) {
/* 107 */       this.protocol = protocol;
/* 108 */       this.upgradeRequest = upgradeRequest;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CharSequence protocol() {
/* 115 */       return this.protocol;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FullHttpRequest upgradeRequest() {
/* 122 */       return this.upgradeRequest;
/*     */     }
/*     */ 
/*     */     
/*     */     public int refCnt() {
/* 127 */       return this.upgradeRequest.refCnt();
/*     */     }
/*     */ 
/*     */     
/*     */     public UpgradeEvent retain() {
/* 132 */       this.upgradeRequest.retain();
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public UpgradeEvent retain(int increment) {
/* 138 */       this.upgradeRequest.retain(increment);
/* 139 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public UpgradeEvent touch() {
/* 144 */       this.upgradeRequest.touch();
/* 145 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public UpgradeEvent touch(Object hint) {
/* 150 */       this.upgradeRequest.touch(hint);
/* 151 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release() {
/* 156 */       return this.upgradeRequest.release();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release(int decrement) {
/* 161 */       return this.upgradeRequest.release(decrement);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 166 */       return "UpgradeEvent [protocol=" + this.protocol + ", upgradeRequest=" + this.upgradeRequest + ']';
/*     */     }
/*     */   }
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
/*     */   public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory) {
/* 189 */     this(sourceCodec, upgradeCodecFactory, 0);
/*     */   }
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
/*     */   public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory, int maxContentLength) {
/* 202 */     super(maxContentLength);
/*     */     
/* 204 */     this.sourceCodec = (SourceCodec)ObjectUtil.checkNotNull(sourceCodec, "sourceCodec");
/* 205 */     this.upgradeCodecFactory = (UpgradeCodecFactory)ObjectUtil.checkNotNull(upgradeCodecFactory, "upgradeCodecFactory");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
/*     */     FullHttpRequest fullRequest;
/* 212 */     this.handlingUpgrade |= isUpgradeRequest(msg);
/* 213 */     if (!this.handlingUpgrade) {
/*     */       
/* 215 */       ReferenceCountUtil.retain(msg);
/* 216 */       out.add(msg);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 221 */     if (msg instanceof FullHttpRequest) {
/* 222 */       fullRequest = (FullHttpRequest)msg;
/* 223 */       ReferenceCountUtil.retain(msg);
/* 224 */       out.add(msg);
/*     */     } else {
/*     */       
/* 227 */       super.decode(ctx, msg, out);
/* 228 */       if (out.isEmpty()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 234 */       assert out.size() == 1;
/* 235 */       this.handlingUpgrade = false;
/* 236 */       fullRequest = (FullHttpRequest)out.get(0);
/*     */     } 
/*     */     
/* 239 */     if (upgrade(ctx, fullRequest))
/*     */     {
/*     */ 
/*     */       
/* 243 */       out.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isUpgradeRequest(HttpObject msg) {
/* 254 */     return (msg instanceof HttpRequest && ((HttpRequest)msg).headers().get((CharSequence)HttpHeaderNames.UPGRADE) != null);
/*     */   }
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
/*     */   private boolean upgrade(ChannelHandlerContext ctx, FullHttpRequest request) {
/* 267 */     List<CharSequence> requestedProtocols = splitHeader(request.headers().get((CharSequence)HttpHeaderNames.UPGRADE));
/* 268 */     int numRequestedProtocols = requestedProtocols.size();
/* 269 */     UpgradeCodec upgradeCodec = null;
/* 270 */     CharSequence upgradeProtocol = null;
/* 271 */     for (int i = 0; i < numRequestedProtocols; i++) {
/* 272 */       CharSequence p = requestedProtocols.get(i);
/* 273 */       UpgradeCodec c = this.upgradeCodecFactory.newUpgradeCodec(p);
/* 274 */       if (c != null) {
/* 275 */         upgradeProtocol = p;
/* 276 */         upgradeCodec = c;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 281 */     if (upgradeCodec == null)
/*     */     {
/* 283 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 287 */     CharSequence connectionHeader = request.headers().get((CharSequence)HttpHeaderNames.CONNECTION);
/* 288 */     if (connectionHeader == null) {
/* 289 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 293 */     Collection<CharSequence> requiredHeaders = upgradeCodec.requiredUpgradeHeaders();
/* 294 */     List<CharSequence> values = splitHeader(connectionHeader);
/* 295 */     if (!AsciiString.containsContentEqualsIgnoreCase(values, (CharSequence)HttpHeaderNames.UPGRADE) || 
/* 296 */       !AsciiString.containsAllContentEqualsIgnoreCase(values, requiredHeaders)) {
/* 297 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 301 */     for (CharSequence requiredHeader : requiredHeaders) {
/* 302 */       if (!request.headers().contains(requiredHeader)) {
/* 303 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 309 */     FullHttpResponse upgradeResponse = createUpgradeResponse(upgradeProtocol);
/* 310 */     if (!upgradeCodec.prepareUpgradeResponse(ctx, request, upgradeResponse.headers())) {
/* 311 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 315 */     UpgradeEvent event = new UpgradeEvent(upgradeProtocol, request);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 322 */       ChannelFuture writeComplete = ctx.writeAndFlush(upgradeResponse);
/*     */       
/* 324 */       this.sourceCodec.upgradeFrom(ctx);
/* 325 */       upgradeCodec.upgradeTo(ctx, request);
/*     */ 
/*     */       
/* 328 */       ctx.pipeline().remove((ChannelHandler)this);
/*     */ 
/*     */ 
/*     */       
/* 332 */       ctx.fireUserEventTriggered(event.retain());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 337 */       writeComplete.addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */     } finally {
/*     */       
/* 340 */       event.release();
/*     */     } 
/* 342 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FullHttpResponse createUpgradeResponse(CharSequence upgradeProtocol) {
/* 349 */     DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS, Unpooled.EMPTY_BUFFER, false);
/*     */     
/* 351 */     res.headers().add((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/* 352 */     res.headers().add((CharSequence)HttpHeaderNames.UPGRADE, upgradeProtocol);
/* 353 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<CharSequence> splitHeader(CharSequence header) {
/* 361 */     StringBuilder builder = new StringBuilder(header.length());
/* 362 */     List<CharSequence> protocols = new ArrayList<CharSequence>(4);
/* 363 */     for (int i = 0; i < header.length(); i++) {
/* 364 */       char c = header.charAt(i);
/* 365 */       if (!Character.isWhitespace(c))
/*     */       {
/*     */ 
/*     */         
/* 369 */         if (c == ',') {
/*     */           
/* 371 */           protocols.add(builder.toString());
/* 372 */           builder.setLength(0);
/*     */         } else {
/* 374 */           builder.append(c);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 379 */     if (builder.length() > 0) {
/* 380 */       protocols.add(builder.toString());
/*     */     }
/*     */     
/* 383 */     return protocols;
/*     */   }
/*     */   
/*     */   public static interface UpgradeCodecFactory {
/*     */     HttpServerUpgradeHandler.UpgradeCodec newUpgradeCodec(CharSequence param1CharSequence);
/*     */   }
/*     */   
/*     */   public static interface UpgradeCodec {
/*     */     Collection<CharSequence> requiredUpgradeHeaders();
/*     */     
/*     */     boolean prepareUpgradeResponse(ChannelHandlerContext param1ChannelHandlerContext, FullHttpRequest param1FullHttpRequest, HttpHeaders param1HttpHeaders);
/*     */     
/*     */     void upgradeTo(ChannelHandlerContext param1ChannelHandlerContext, FullHttpRequest param1FullHttpRequest);
/*     */   }
/*     */   
/*     */   public static interface SourceCodec {
/*     */     void upgradeFrom(ChannelHandlerContext param1ChannelHandlerContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpServerUpgradeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */