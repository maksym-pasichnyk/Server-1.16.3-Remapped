/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ public class AuthenticationUnavailableException
/*    */   extends AuthenticationException {
/*    */   public AuthenticationUnavailableException() {}
/*    */   
/*    */   public AuthenticationUnavailableException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public AuthenticationUnavailableException(String message, Throwable cause) {
/* 12 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public AuthenticationUnavailableException(Throwable cause) {
/* 16 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\exceptions\AuthenticationUnavailableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */