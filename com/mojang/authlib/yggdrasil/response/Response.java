/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ public class Response {
/*    */   private String error;
/*    */   private String errorMessage;
/*    */   private String cause;
/*    */   
/*    */   public String getError() {
/*  9 */     return this.error;
/*    */   }
/*    */   
/*    */   public String getCause() {
/* 13 */     return this.cause;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 17 */     return this.errorMessage;
/*    */   }
/*    */   
/*    */   protected void setError(String error) {
/* 21 */     this.error = error;
/*    */   }
/*    */   
/*    */   protected void setErrorMessage(String errorMessage) {
/* 25 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   protected void setCause(String cause) {
/* 29 */     this.cause = cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\response\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */