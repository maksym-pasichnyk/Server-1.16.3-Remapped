/*    */ package com.mojang.authlib.legacy;
/*    */ 
/*    */ import com.mojang.authlib.AuthenticationService;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*    */ import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
/*    */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class LegacyMinecraftSessionService
/*    */   extends HttpMinecraftSessionService
/*    */ {
/*    */   private static final String BASE_URL = "http://session.minecraft.net/game/";
/* 22 */   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/joinserver.jsp");
/* 23 */   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/checkserver.jsp");
/*    */   
/*    */   protected LegacyMinecraftSessionService(LegacyAuthenticationService authenticationService) {
/* 26 */     super(authenticationService);
/*    */   }
/*    */ 
/*    */   
/*    */   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
/* 31 */     Map<String, Object> arguments = new HashMap<>();
/*    */     
/* 33 */     arguments.put("user", profile.getName());
/* 34 */     arguments.put("sessionId", authenticationToken);
/* 35 */     arguments.put("serverId", serverId);
/*    */     
/* 37 */     URL url = HttpAuthenticationService.concatenateURL(JOIN_URL, HttpAuthenticationService.buildQuery(arguments));
/*    */     
/*    */     try {
/* 40 */       String response = getAuthenticationService().performGetRequest(url);
/*    */       
/* 42 */       if (!"OK".equals(response)) {
/* 43 */         throw new AuthenticationException(response);
/*    */       }
/* 45 */     } catch (IOException e) {
/* 46 */       throw new AuthenticationUnavailableException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile hasJoinedServer(GameProfile user, String serverId, InetAddress address) throws AuthenticationUnavailableException {
/* 52 */     Map<String, Object> arguments = new HashMap<>();
/*    */     
/* 54 */     arguments.put("user", user.getName());
/* 55 */     arguments.put("serverId", serverId);
/*    */     
/* 57 */     URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));
/*    */     
/*    */     try {
/* 60 */       String response = getAuthenticationService().performGetRequest(url);
/*    */       
/* 62 */       return "YES".equals(response) ? user : null;
/* 63 */     } catch (IOException e) {
/* 64 */       throw new AuthenticationUnavailableException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
/* 70 */     return new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
/* 75 */     return profile;
/*    */   }
/*    */ 
/*    */   
/*    */   public LegacyAuthenticationService getAuthenticationService() {
/* 80 */     return (LegacyAuthenticationService)super.getAuthenticationService();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\legacy\LegacyMinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */