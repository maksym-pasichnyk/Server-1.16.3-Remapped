/*     */ package io.netty.handler.codec.http.websocketx.extensions;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ public class WebSocketClientExtensionHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*     */   private final List<WebSocketClientExtensionHandshaker> extensionHandshakers;
/*     */   
/*     */   public WebSocketClientExtensionHandler(WebSocketClientExtensionHandshaker... extensionHandshakers) {
/*  53 */     if (extensionHandshakers == null) {
/*  54 */       throw new NullPointerException("extensionHandshakers");
/*     */     }
/*  56 */     if (extensionHandshakers.length == 0) {
/*  57 */       throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
/*     */     }
/*  59 */     this.extensionHandshakers = Arrays.asList(extensionHandshakers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  64 */     if (msg instanceof HttpRequest && WebSocketExtensionUtil.isWebsocketUpgrade(((HttpRequest)msg).headers())) {
/*  65 */       HttpRequest request = (HttpRequest)msg;
/*  66 */       String headerValue = request.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */       
/*  68 */       for (WebSocketClientExtensionHandshaker extensionHandshaker : this.extensionHandshakers) {
/*  69 */         WebSocketExtensionData extensionData = extensionHandshaker.newRequestData();
/*  70 */         headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData
/*  71 */             .name(), extensionData.parameters());
/*     */       } 
/*     */       
/*  74 */       request.headers().set((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
/*     */     } 
/*     */     
/*  77 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  83 */     if (msg instanceof HttpResponse) {
/*  84 */       HttpResponse response = (HttpResponse)msg;
/*     */       
/*  86 */       if (WebSocketExtensionUtil.isWebsocketUpgrade(response.headers())) {
/*  87 */         String extensionsHeader = response.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */         
/*  89 */         if (extensionsHeader != null) {
/*     */           
/*  91 */           List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
/*     */           
/*  93 */           List<WebSocketClientExtension> validExtensions = new ArrayList<WebSocketClientExtension>(extensions.size());
/*  94 */           int rsv = 0;
/*     */           
/*  96 */           for (WebSocketExtensionData extensionData : extensions) {
/*     */             
/*  98 */             Iterator<WebSocketClientExtensionHandshaker> extensionHandshakersIterator = this.extensionHandshakers.iterator();
/*  99 */             WebSocketClientExtension validExtension = null;
/*     */             
/* 101 */             while (validExtension == null && extensionHandshakersIterator.hasNext()) {
/*     */               
/* 103 */               WebSocketClientExtensionHandshaker extensionHandshaker = extensionHandshakersIterator.next();
/* 104 */               validExtension = extensionHandshaker.handshakeExtension(extensionData);
/*     */             } 
/*     */             
/* 107 */             if (validExtension != null && (validExtension.rsv() & rsv) == 0) {
/* 108 */               rsv |= validExtension.rsv();
/* 109 */               validExtensions.add(validExtension); continue;
/*     */             } 
/* 111 */             throw new CodecException("invalid WebSocket Extension handshake for \"" + extensionsHeader + '"');
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 116 */           for (WebSocketClientExtension validExtension : validExtensions) {
/* 117 */             WebSocketExtensionDecoder decoder = validExtension.newExtensionDecoder();
/* 118 */             WebSocketExtensionEncoder encoder = validExtension.newExtensionEncoder();
/* 119 */             ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), (ChannelHandler)decoder);
/* 120 */             ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), (ChannelHandler)encoder);
/*     */           } 
/*     */         } 
/*     */         
/* 124 */         ctx.pipeline().remove(ctx.name());
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     super.channelRead(ctx, msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketClientExtensionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */