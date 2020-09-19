/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ public class AuthenticationException
/*    */   extends Exception {
/*    */   public AuthenticationException() {}
/*    */   
/*    */   public AuthenticationException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public AuthenticationException(String message, Throwable cause) {
/* 12 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public AuthenticationException(Throwable cause) {
/* 16 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\exceptions\AuthenticationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */