/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ public class UserMigratedException
/*    */   extends InvalidCredentialsException {
/*    */   public UserMigratedException() {}
/*    */   
/*    */   public UserMigratedException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public UserMigratedException(String message, Throwable cause) {
/* 12 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public UserMigratedException(Throwable cause) {
/* 16 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\exceptions\UserMigratedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */