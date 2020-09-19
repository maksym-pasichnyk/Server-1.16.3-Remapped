/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DefaultHttpMessage
/*    */   extends DefaultHttpObject
/*    */   implements HttpMessage
/*    */ {
/*    */   private static final int HASH_CODE_PRIME = 31;
/*    */   private HttpVersion version;
/*    */   private final HttpHeaders headers;
/*    */   
/*    */   protected DefaultHttpMessage(HttpVersion version) {
/* 32 */     this(version, true, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected DefaultHttpMessage(HttpVersion version, boolean validateHeaders, boolean singleFieldHeaders) {
/* 39 */     this(version, singleFieldHeaders ? new CombinedHttpHeaders(validateHeaders) : new DefaultHttpHeaders(validateHeaders));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected DefaultHttpMessage(HttpVersion version, HttpHeaders headers) {
/* 48 */     this.version = (HttpVersion)ObjectUtil.checkNotNull(version, "version");
/* 49 */     this.headers = (HttpHeaders)ObjectUtil.checkNotNull(headers, "headers");
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders headers() {
/* 54 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public HttpVersion getProtocolVersion() {
/* 60 */     return protocolVersion();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpVersion protocolVersion() {
/* 65 */     return this.version;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     int result = 1;
/* 71 */     result = 31 * result + this.headers.hashCode();
/* 72 */     result = 31 * result + this.version.hashCode();
/* 73 */     result = 31 * result + super.hashCode();
/* 74 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 79 */     if (!(o instanceof DefaultHttpMessage)) {
/* 80 */       return false;
/*    */     }
/*    */     
/* 83 */     DefaultHttpMessage other = (DefaultHttpMessage)o;
/*    */     
/* 85 */     return (headers().equals(other.headers()) && 
/* 86 */       protocolVersion().equals(other.protocolVersion()) && super
/* 87 */       .equals(o));
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessage setProtocolVersion(HttpVersion version) {
/* 92 */     if (version == null) {
/* 93 */       throw new NullPointerException("version");
/*    */     }
/* 95 */     this.version = version;
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultHttpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */