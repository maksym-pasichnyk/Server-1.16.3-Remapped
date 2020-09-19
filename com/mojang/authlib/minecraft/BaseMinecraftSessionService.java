/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.mojang.authlib.AuthenticationService;
/*    */ 
/*    */ public abstract class BaseMinecraftSessionService implements MinecraftSessionService {
/*    */   private final AuthenticationService authenticationService;
/*    */   
/*    */   protected BaseMinecraftSessionService(AuthenticationService authenticationService) {
/*  9 */     this.authenticationService = authenticationService;
/*    */   }
/*    */   
/*    */   public AuthenticationService getAuthenticationService() {
/* 13 */     return this.authenticationService;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\minecraft\BaseMinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */