/*    */ package io.netty.handler.codec.http.cookie;
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
/*    */ public abstract class CookieEncoder
/*    */ {
/*    */   protected final boolean strict;
/*    */   
/*    */   protected CookieEncoder(boolean strict) {
/* 30 */     this.strict = strict;
/*    */   }
/*    */   
/*    */   protected void validateCookie(String name, String value) {
/* 34 */     if (this.strict) {
/*    */       int pos;
/*    */       
/* 37 */       if ((pos = CookieUtil.firstInvalidCookieNameOctet(name)) >= 0) {
/* 38 */         throw new IllegalArgumentException("Cookie name contains an invalid char: " + name.charAt(pos));
/*    */       }
/*    */       
/* 41 */       CharSequence unwrappedValue = CookieUtil.unwrapValue(value);
/* 42 */       if (unwrappedValue == null) {
/* 43 */         throw new IllegalArgumentException("Cookie value wrapping quotes are not balanced: " + value);
/*    */       }
/*    */       
/* 46 */       if ((pos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0)
/* 47 */         throw new IllegalArgumentException("Cookie value contains an invalid char: " + value.charAt(pos)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\cookie\CookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */