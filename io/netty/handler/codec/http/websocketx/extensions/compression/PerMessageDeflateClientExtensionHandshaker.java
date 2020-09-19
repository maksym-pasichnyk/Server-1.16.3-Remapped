/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PerMessageDeflateClientExtensionHandshaker
/*     */   implements WebSocketClientExtensionHandshaker
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final boolean allowClientWindowSize;
/*     */   private final int requestedServerWindowSize;
/*     */   private final boolean allowClientNoContext;
/*     */   private final boolean requestedServerNoContext;
/*     */   
/*     */   public PerMessageDeflateClientExtensionHandshaker() {
/*  48 */     this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), 15, false, false);
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
/*     */   
/*     */   public PerMessageDeflateClientExtensionHandshaker(int compressionLevel, boolean allowClientWindowSize, int requestedServerWindowSize, boolean allowClientNoContext, boolean requestedServerNoContext) {
/*  71 */     if (requestedServerWindowSize > 15 || requestedServerWindowSize < 8) {
/*  72 */       throw new IllegalArgumentException("requestedServerWindowSize: " + requestedServerWindowSize + " (expected: 8-15)");
/*     */     }
/*     */     
/*  75 */     if (compressionLevel < 0 || compressionLevel > 9) {
/*  76 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  79 */     this.compressionLevel = compressionLevel;
/*  80 */     this.allowClientWindowSize = allowClientWindowSize;
/*  81 */     this.requestedServerWindowSize = requestedServerWindowSize;
/*  82 */     this.allowClientNoContext = allowClientNoContext;
/*  83 */     this.requestedServerNoContext = requestedServerNoContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketExtensionData newRequestData() {
/*  88 */     HashMap<String, String> parameters = new HashMap<String, String>(4);
/*  89 */     if (this.requestedServerWindowSize != 15) {
/*  90 */       parameters.put("server_no_context_takeover", null);
/*     */     }
/*  92 */     if (this.allowClientNoContext) {
/*  93 */       parameters.put("client_no_context_takeover", null);
/*     */     }
/*  95 */     if (this.requestedServerWindowSize != 15) {
/*  96 */       parameters.put("server_max_window_bits", Integer.toString(this.requestedServerWindowSize));
/*     */     }
/*  98 */     if (this.allowClientWindowSize) {
/*  99 */       parameters.put("client_max_window_bits", null);
/*     */     }
/* 101 */     return new WebSocketExtensionData("permessage-deflate", parameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketClientExtension handshakeExtension(WebSocketExtensionData extensionData) {
/* 106 */     if (!"permessage-deflate".equals(extensionData.name())) {
/* 107 */       return null;
/*     */     }
/*     */     
/* 110 */     boolean succeed = true;
/* 111 */     int clientWindowSize = 15;
/* 112 */     int serverWindowSize = 15;
/* 113 */     boolean serverNoContext = false;
/* 114 */     boolean clientNoContext = false;
/*     */ 
/*     */     
/* 117 */     Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
/* 118 */     while (succeed && parametersIterator.hasNext()) {
/* 119 */       Map.Entry<String, String> parameter = parametersIterator.next();
/*     */       
/* 121 */       if ("client_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 123 */         if (this.allowClientWindowSize) {
/* 124 */           clientWindowSize = Integer.parseInt(parameter.getValue()); continue;
/*     */         } 
/* 126 */         succeed = false; continue;
/*     */       } 
/* 128 */       if ("server_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 130 */         serverWindowSize = Integer.parseInt(parameter.getValue());
/* 131 */         if (clientWindowSize > 15 || clientWindowSize < 8)
/* 132 */           succeed = false;  continue;
/*     */       } 
/* 134 */       if ("client_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 136 */         if (this.allowClientNoContext) {
/* 137 */           clientNoContext = true; continue;
/*     */         } 
/* 139 */         succeed = false; continue;
/*     */       } 
/* 141 */       if ("server_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 143 */         if (this.requestedServerNoContext) {
/* 144 */           serverNoContext = true; continue;
/*     */         } 
/* 146 */         succeed = false;
/*     */         
/*     */         continue;
/*     */       } 
/* 150 */       succeed = false;
/*     */     } 
/*     */ 
/*     */     
/* 154 */     if ((this.requestedServerNoContext && !serverNoContext) || this.requestedServerWindowSize != serverWindowSize)
/*     */     {
/* 156 */       succeed = false;
/*     */     }
/*     */     
/* 159 */     if (succeed) {
/* 160 */       return new PermessageDeflateExtension(serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
/*     */     }
/*     */     
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class PermessageDeflateExtension
/*     */     implements WebSocketClientExtension
/*     */   {
/*     */     private final boolean serverNoContext;
/*     */     private final int serverWindowSize;
/*     */     private final boolean clientNoContext;
/*     */     private final int clientWindowSize;
/*     */     
/*     */     public int rsv() {
/* 176 */       return 4;
/*     */     }
/*     */ 
/*     */     
/*     */     public PermessageDeflateExtension(boolean serverNoContext, int serverWindowSize, boolean clientNoContext, int clientWindowSize) {
/* 181 */       this.serverNoContext = serverNoContext;
/* 182 */       this.serverWindowSize = serverWindowSize;
/* 183 */       this.clientNoContext = clientNoContext;
/* 184 */       this.clientWindowSize = clientWindowSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public WebSocketExtensionEncoder newExtensionEncoder() {
/* 189 */       return new PerMessageDeflateEncoder(PerMessageDeflateClientExtensionHandshaker.this.compressionLevel, this.serverWindowSize, this.serverNoContext);
/*     */     }
/*     */ 
/*     */     
/*     */     public WebSocketExtensionDecoder newExtensionDecoder() {
/* 194 */       return new PerMessageDeflateDecoder(this.clientNoContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateClientExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */