/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ 
/*    */ public class ValidateRequest {
/*    */   private String clientToken;
/*    */   private String accessToken;
/*    */   
/*    */   public ValidateRequest(YggdrasilUserAuthentication authenticationService) {
/* 10 */     this.clientToken = authenticationService.getAuthenticationService().getClientToken();
/* 11 */     this.accessToken = authenticationService.getAuthenticatedToken();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\request\ValidateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */