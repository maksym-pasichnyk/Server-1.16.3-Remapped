/*     */ package io.netty.handler.codec.http.websocketx.extensions;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
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
/*     */ public class WebSocketServerExtensionHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*     */   private final List<WebSocketServerExtensionHandshaker> extensionHandshakers;
/*     */   private List<WebSocketServerExtension> validExtensions;
/*     */   
/*     */   public WebSocketServerExtensionHandler(WebSocketServerExtensionHandshaker... extensionHandshakers) {
/*  56 */     if (extensionHandshakers == null) {
/*  57 */       throw new NullPointerException("extensionHandshakers");
/*     */     }
/*  59 */     if (extensionHandshakers.length == 0) {
/*  60 */       throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
/*     */     }
/*  62 */     this.extensionHandshakers = Arrays.asList(extensionHandshakers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  68 */     if (msg instanceof HttpRequest) {
/*  69 */       HttpRequest request = (HttpRequest)msg;
/*     */       
/*  71 */       if (WebSocketExtensionUtil.isWebsocketUpgrade(request.headers())) {
/*  72 */         String extensionsHeader = request.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */         
/*  74 */         if (extensionsHeader != null) {
/*     */           
/*  76 */           List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
/*  77 */           int rsv = 0;
/*     */           
/*  79 */           for (WebSocketExtensionData extensionData : extensions) {
/*     */             
/*  81 */             Iterator<WebSocketServerExtensionHandshaker> extensionHandshakersIterator = this.extensionHandshakers.iterator();
/*  82 */             WebSocketServerExtension validExtension = null;
/*     */             
/*  84 */             while (validExtension == null && extensionHandshakersIterator.hasNext()) {
/*     */               
/*  86 */               WebSocketServerExtensionHandshaker extensionHandshaker = extensionHandshakersIterator.next();
/*  87 */               validExtension = extensionHandshaker.handshakeExtension(extensionData);
/*     */             } 
/*     */             
/*  90 */             if (validExtension != null && (validExtension.rsv() & rsv) == 0) {
/*  91 */               if (this.validExtensions == null) {
/*  92 */                 this.validExtensions = new ArrayList<WebSocketServerExtension>(1);
/*     */               }
/*  94 */               rsv |= validExtension.rsv();
/*  95 */               this.validExtensions.add(validExtension);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     super.channelRead(ctx, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 107 */     if (msg instanceof HttpResponse && 
/* 108 */       WebSocketExtensionUtil.isWebsocketUpgrade(((HttpResponse)msg).headers()) && this.validExtensions != null) {
/* 109 */       HttpResponse response = (HttpResponse)msg;
/* 110 */       String headerValue = response.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */       
/* 112 */       for (WebSocketServerExtension extension : this.validExtensions) {
/* 113 */         WebSocketExtensionData extensionData = extension.newReponseData();
/* 114 */         headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData
/* 115 */             .name(), extensionData.parameters());
/*     */       } 
/*     */       
/* 118 */       promise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 121 */               if (future.isSuccess()) {
/* 122 */                 for (WebSocketServerExtension extension : WebSocketServerExtensionHandler.this.validExtensions) {
/* 123 */                   WebSocketExtensionDecoder decoder = extension.newExtensionDecoder();
/* 124 */                   WebSocketExtensionEncoder encoder = extension.newExtensionEncoder();
/* 125 */                   ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), (ChannelHandler)decoder);
/* 126 */                   ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), (ChannelHandler)encoder);
/*     */                 } 
/*     */               }
/*     */               
/* 130 */               ctx.pipeline().remove(ctx.name());
/*     */             }
/*     */           });
/*     */       
/* 134 */       if (headerValue != null) {
/* 135 */         response.headers().set((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
/*     */       }
/*     */     } 
/*     */     
/* 139 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketServerExtensionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */