/*    */ package com.mojang.authlib.legacy;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.UserAuthentication;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import java.net.Proxy;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class LegacyAuthenticationService
/*    */   extends HttpAuthenticationService
/*    */ {
/*    */   protected LegacyAuthenticationService(Proxy proxy) {
/* 21 */     super(proxy);
/*    */   }
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
/*    */   public LegacyUserAuthentication createUserAuthentication(Agent agent) {
/* 35 */     Validate.notNull(agent);
/* 36 */     if (agent != Agent.MINECRAFT) {
/* 37 */       throw new IllegalArgumentException("Legacy authentication cannot handle anything but Minecraft");
/*    */     }
/* 39 */     return new LegacyUserAuthentication(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public LegacyMinecraftSessionService createMinecraftSessionService() {
/* 44 */     return new LegacyMinecraftSessionService(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfileRepository createProfileRepository() {
/* 49 */     throw new UnsupportedOperationException("Legacy authentication service has no profile repository");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\legacy\LegacyAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */