/*     */ package io.netty.handler.codec.http;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttpRequest
/*     */   extends DefaultHttpMessage
/*     */   implements HttpRequest
/*     */ {
/*     */   private static final int HASH_CODE_PRIME = 31;
/*     */   private HttpMethod method;
/*     */   private String uri;
/*     */   
/*     */   public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
/*  36 */     this(httpVersion, method, uri, true);
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
/*     */   public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
/*  48 */     super(httpVersion, validateHeaders, false);
/*  49 */     this.method = (HttpMethod)ObjectUtil.checkNotNull(method, "method");
/*  50 */     this.uri = (String)ObjectUtil.checkNotNull(uri, "uri");
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
/*     */   public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, HttpHeaders headers) {
/*  62 */     super(httpVersion, headers);
/*  63 */     this.method = (HttpMethod)ObjectUtil.checkNotNull(method, "method");
/*  64 */     this.uri = (String)ObjectUtil.checkNotNull(uri, "uri");
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpMethod getMethod() {
/*  70 */     return method();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod method() {
/*  75 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getUri() {
/*  81 */     return uri();
/*     */   }
/*     */ 
/*     */   
/*     */   public String uri() {
/*  86 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequest setMethod(HttpMethod method) {
/*  91 */     if (method == null) {
/*  92 */       throw new NullPointerException("method");
/*     */     }
/*  94 */     this.method = method;
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequest setUri(String uri) {
/* 100 */     if (uri == null) {
/* 101 */       throw new NullPointerException("uri");
/*     */     }
/* 103 */     this.uri = uri;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequest setProtocolVersion(HttpVersion version) {
/* 109 */     super.setProtocolVersion(version);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     int result = 1;
/* 116 */     result = 31 * result + this.method.hashCode();
/* 117 */     result = 31 * result + this.uri.hashCode();
/* 118 */     result = 31 * result + super.hashCode();
/* 119 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 124 */     if (!(o instanceof DefaultHttpRequest)) {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     DefaultHttpRequest other = (DefaultHttpRequest)o;
/*     */     
/* 130 */     return (method().equals(other.method()) && 
/* 131 */       uri().equalsIgnoreCase(other.uri()) && super
/* 132 */       .equals(o));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return HttpMessageUtil.appendRequest(new StringBuilder(256), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */