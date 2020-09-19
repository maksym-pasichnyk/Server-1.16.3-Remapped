/*    */ package io.netty.handler.codec.smtp;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public final class DefaultSmtpResponse
/*    */   implements SmtpResponse
/*    */ {
/*    */   private final int code;
/*    */   private final List<CharSequence> details;
/*    */   
/*    */   public DefaultSmtpResponse(int code) {
/* 36 */     this(code, (List<CharSequence>)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSmtpResponse(int code, CharSequence... details) {
/* 43 */     this(code, SmtpUtils.toUnmodifiableList(details));
/*    */   }
/*    */   
/*    */   DefaultSmtpResponse(int code, List<CharSequence> details) {
/* 47 */     if (code < 100 || code > 599) {
/* 48 */       throw new IllegalArgumentException("code must be 100 <= code <= 599");
/*    */     }
/* 50 */     this.code = code;
/* 51 */     if (details == null) {
/* 52 */       this.details = Collections.emptyList();
/*    */     } else {
/* 54 */       this.details = Collections.unmodifiableList(details);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int code() {
/* 60 */     return this.code;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<CharSequence> details() {
/* 65 */     return this.details;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     return this.code * 31 + this.details.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 75 */     if (!(o instanceof DefaultSmtpResponse)) {
/* 76 */       return false;
/*    */     }
/*    */     
/* 79 */     if (o == this) {
/* 80 */       return true;
/*    */     }
/*    */     
/* 83 */     DefaultSmtpResponse other = (DefaultSmtpResponse)o;
/*    */     
/* 85 */     return (code() == other.code() && 
/* 86 */       details().equals(other.details()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return "DefaultSmtpResponse{code=" + this.code + ", details=" + this.details + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\DefaultSmtpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */