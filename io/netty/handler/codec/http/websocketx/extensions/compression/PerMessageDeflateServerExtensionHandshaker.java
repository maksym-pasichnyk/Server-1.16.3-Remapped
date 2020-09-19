/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
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
/*     */ public final class PerMessageDeflateServerExtensionHandshaker
/*     */   implements WebSocketServerExtensionHandshaker
/*     */ {
/*     */   public static final int MIN_WINDOW_SIZE = 8;
/*     */   public static final int MAX_WINDOW_SIZE = 15;
/*     */   static final String PERMESSAGE_DEFLATE_EXTENSION = "permessage-deflate";
/*     */   static final String CLIENT_MAX_WINDOW = "client_max_window_bits";
/*     */   static final String SERVER_MAX_WINDOW = "server_max_window_bits";
/*     */   static final String CLIENT_NO_CONTEXT = "client_no_context_takeover";
/*     */   static final String SERVER_NO_CONTEXT = "server_no_context_takeover";
/*     */   private final int compressionLevel;
/*     */   private final boolean allowServerWindowSize;
/*     */   private final int preferredClientWindowSize;
/*     */   private final boolean allowServerNoContext;
/*     */   private final boolean preferredClientNoContext;
/*     */   
/*     */   public PerMessageDeflateServerExtensionHandshaker() {
/*  54 */     this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), 15, false, false);
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
/*     */   public PerMessageDeflateServerExtensionHandshaker(int compressionLevel, boolean allowServerWindowSize, int preferredClientWindowSize, boolean allowServerNoContext, boolean preferredClientNoContext) {
/*  77 */     if (preferredClientWindowSize > 15 || preferredClientWindowSize < 8) {
/*  78 */       throw new IllegalArgumentException("preferredServerWindowSize: " + preferredClientWindowSize + " (expected: 8-15)");
/*     */     }
/*     */     
/*  81 */     if (compressionLevel < 0 || compressionLevel > 9) {
/*  82 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  85 */     this.compressionLevel = compressionLevel;
/*  86 */     this.allowServerWindowSize = allowServerWindowSize;
/*  87 */     this.preferredClientWindowSize = preferredClientWindowSize;
/*  88 */     this.allowServerNoContext = allowServerNoContext;
/*  89 */     this.preferredClientNoContext = preferredClientNoContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData) {
/*  94 */     if (!"permessage-deflate".equals(extensionData.name())) {
/*  95 */       return null;
/*     */     }
/*     */     
/*  98 */     boolean deflateEnabled = true;
/*  99 */     int clientWindowSize = 15;
/* 100 */     int serverWindowSize = 15;
/* 101 */     boolean serverNoContext = false;
/* 102 */     boolean clientNoContext = false;
/*     */ 
/*     */     
/* 105 */     Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
/* 106 */     while (deflateEnabled && parametersIterator.hasNext()) {
/* 107 */       Map.Entry<String, String> parameter = parametersIterator.next();
/*     */       
/* 109 */       if ("client_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 111 */         clientWindowSize = this.preferredClientWindowSize; continue;
/* 112 */       }  if ("server_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 114 */         if (this.allowServerWindowSize) {
/* 115 */           serverWindowSize = Integer.parseInt(parameter.getValue());
/* 116 */           if (serverWindowSize > 15 || serverWindowSize < 8)
/* 117 */             deflateEnabled = false; 
/*     */           continue;
/*     */         } 
/* 120 */         deflateEnabled = false; continue;
/*     */       } 
/* 122 */       if ("client_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 124 */         clientNoContext = this.preferredClientNoContext; continue;
/* 125 */       }  if ("server_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
/*     */         
/* 127 */         if (this.allowServerNoContext) {
/* 128 */           serverNoContext = true; continue;
/*     */         } 
/* 130 */         deflateEnabled = false;
/*     */         
/*     */         continue;
/*     */       } 
/* 134 */       deflateEnabled = false;
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (deflateEnabled) {
/* 139 */       return new PermessageDeflateExtension(this.compressionLevel, serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
/*     */     }
/*     */     
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PermessageDeflateExtension
/*     */     implements WebSocketServerExtension
/*     */   {
/*     */     private final int compressionLevel;
/*     */     private final boolean serverNoContext;
/*     */     private final int serverWindowSize;
/*     */     private final boolean clientNoContext;
/*     */     private final int clientWindowSize;
/*     */     
/*     */     public PermessageDeflateExtension(int compressionLevel, boolean serverNoContext, int serverWindowSize, boolean clientNoContext, int clientWindowSize) {
/* 156 */       this.compressionLevel = compressionLevel;
/* 157 */       this.serverNoContext = serverNoContext;
/* 158 */       this.serverWindowSize = serverWindowSize;
/* 159 */       this.clientNoContext = clientNoContext;
/* 160 */       this.clientWindowSize = clientWindowSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public int rsv() {
/* 165 */       return 4;
/*     */     }
/*     */ 
/*     */     
/*     */     public WebSocketExtensionEncoder newExtensionEncoder() {
/* 170 */       return new PerMessageDeflateEncoder(this.compressionLevel, this.clientWindowSize, this.clientNoContext);
/*     */     }
/*     */ 
/*     */     
/*     */     public WebSocketExtensionDecoder newExtensionDecoder() {
/* 175 */       return new PerMessageDeflateDecoder(this.serverNoContext);
/*     */     }
/*     */ 
/*     */     
/*     */     public WebSocketExtensionData newReponseData() {
/* 180 */       HashMap<String, String> parameters = new HashMap<String, String>(4);
/* 181 */       if (this.serverNoContext) {
/* 182 */         parameters.put("server_no_context_takeover", null);
/*     */       }
/* 184 */       if (this.clientNoContext) {
/* 185 */         parameters.put("client_no_context_takeover", null);
/*     */       }
/* 187 */       if (this.serverWindowSize != 15) {
/* 188 */         parameters.put("server_max_window_bits", Integer.toString(this.serverWindowSize));
/*     */       }
/* 190 */       if (this.clientWindowSize != 15) {
/* 191 */         parameters.put("client_max_window_bits", Integer.toString(this.clientWindowSize));
/*     */       }
/* 193 */       return new WebSocketExtensionData("permessage-deflate", parameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateServerExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */