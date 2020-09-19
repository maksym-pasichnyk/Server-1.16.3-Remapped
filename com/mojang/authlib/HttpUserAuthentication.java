/*    */ package com.mojang.authlib;
/*    */ 
/*    */ public abstract class HttpUserAuthentication extends BaseUserAuthentication {
/*    */   protected HttpUserAuthentication(HttpAuthenticationService authenticationService) {
/*  5 */     super(authenticationService);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpAuthenticationService getAuthenticationService() {
/* 10 */     return (HttpAuthenticationService)super.getAuthenticationService();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\HttpUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */