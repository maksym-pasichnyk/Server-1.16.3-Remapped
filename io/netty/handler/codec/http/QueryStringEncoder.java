/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
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
/*     */ public class QueryStringEncoder
/*     */ {
/*     */   private final String charsetName;
/*     */   private final StringBuilder uriBuilder;
/*     */   private boolean hasParams;
/*     */   
/*     */   public QueryStringEncoder(String uri) {
/*  49 */     this(uri, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringEncoder(String uri, Charset charset) {
/*  57 */     this.uriBuilder = new StringBuilder(uri);
/*  58 */     this.charsetName = charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(String name, String value) {
/*  65 */     ObjectUtil.checkNotNull(name, "name");
/*  66 */     if (this.hasParams) {
/*  67 */       this.uriBuilder.append('&');
/*     */     } else {
/*  69 */       this.uriBuilder.append('?');
/*  70 */       this.hasParams = true;
/*     */     } 
/*  72 */     appendComponent(name, this.charsetName, this.uriBuilder);
/*  73 */     if (value != null) {
/*  74 */       this.uriBuilder.append('=');
/*  75 */       appendComponent(value, this.charsetName, this.uriBuilder);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI toUri() throws URISyntaxException {
/*  85 */     return new URI(toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return this.uriBuilder.toString();
/*     */   }
/*     */   
/*     */   private static void appendComponent(String s, String charset, StringBuilder sb) {
/*     */     try {
/* 100 */       s = URLEncoder.encode(s, charset);
/* 101 */     } catch (UnsupportedEncodingException ignored) {
/* 102 */       throw new UnsupportedCharsetException(charset);
/*     */     } 
/*     */     
/* 105 */     int idx = s.indexOf('+');
/* 106 */     if (idx == -1) {
/* 107 */       sb.append(s);
/*     */       return;
/*     */     } 
/* 110 */     sb.append(s, 0, idx).append("%20");
/* 111 */     int size = s.length();
/* 112 */     idx++;
/* 113 */     for (; idx < size; idx++) {
/* 114 */       char c = s.charAt(idx);
/* 115 */       if (c != '+') {
/* 116 */         sb.append(c);
/*     */       } else {
/* 118 */         sb.append("%20");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\QueryStringEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */