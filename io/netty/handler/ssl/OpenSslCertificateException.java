/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.internal.tcnative.CertificateVerifier;
/*    */ import java.security.cert.CertificateException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OpenSslCertificateException
/*    */   extends CertificateException
/*    */ {
/*    */   private static final long serialVersionUID = 5542675253797129798L;
/*    */   private final int errorCode;
/*    */   
/*    */   public OpenSslCertificateException(int errorCode) {
/* 36 */     this((String)null, errorCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OpenSslCertificateException(String msg, int errorCode) {
/* 44 */     super(msg);
/* 45 */     this.errorCode = checkErrorCode(errorCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OpenSslCertificateException(String message, Throwable cause, int errorCode) {
/* 53 */     super(message, cause);
/* 54 */     this.errorCode = checkErrorCode(errorCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OpenSslCertificateException(Throwable cause, int errorCode) {
/* 62 */     this(null, cause, errorCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int errorCode() {
/* 69 */     return this.errorCode;
/*    */   }
/*    */   
/*    */   private static int checkErrorCode(int errorCode) {
/* 73 */     if (!CertificateVerifier.isValid(errorCode)) {
/* 74 */       throw new IllegalArgumentException("errorCode '" + errorCode + "' invalid, see https://www.openssl.org/docs/man1.0.2/apps/verify.html.");
/*    */     }
/*    */     
/* 77 */     return errorCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSslCertificateException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */