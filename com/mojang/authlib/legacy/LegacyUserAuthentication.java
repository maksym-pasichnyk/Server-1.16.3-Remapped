/*     */ package com.mojang.authlib.legacy;
/*     */ 
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.HttpUserAuthentication;
/*     */ import com.mojang.authlib.UserType;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ @Deprecated
/*     */ public class LegacyUserAuthentication extends HttpUserAuthentication {
/*  19 */   private static final URL AUTHENTICATION_URL = HttpAuthenticationService.constantURL("https://login.minecraft.net");
/*     */   
/*     */   private static final int AUTHENTICATION_VERSION = 14;
/*     */   
/*     */   private static final int RESPONSE_PART_PROFILE_NAME = 2;
/*     */   
/*     */   private static final int RESPONSE_PART_SESSION_TOKEN = 3;
/*     */   
/*     */   private static final int RESPONSE_PART_PROFILE_ID = 4;
/*     */   private String sessionToken;
/*     */   
/*     */   protected LegacyUserAuthentication(LegacyAuthenticationService authenticationService) {
/*  31 */     super(authenticationService);
/*     */   }
/*     */   
/*     */   public void logIn() throws AuthenticationException {
/*     */     String response;
/*  36 */     if (StringUtils.isBlank(getUsername())) {
/*  37 */       throw new InvalidCredentialsException("Invalid username");
/*     */     }
/*  39 */     if (StringUtils.isBlank(getPassword())) {
/*  40 */       throw new InvalidCredentialsException("Invalid password");
/*     */     }
/*     */     
/*  43 */     Map<String, Object> args = new HashMap<>();
/*  44 */     args.put("user", getUsername());
/*  45 */     args.put("password", getPassword());
/*  46 */     args.put("version", Integer.valueOf(14));
/*     */ 
/*     */     
/*     */     try {
/*  50 */       response = getAuthenticationService().performPostRequest(AUTHENTICATION_URL, HttpAuthenticationService.buildQuery(args), "application/x-www-form-urlencoded").trim();
/*  51 */     } catch (IOException e) {
/*  52 */       throw new AuthenticationException("Authentication server is not responding", e);
/*     */     } 
/*     */     
/*  55 */     String[] split = response.split(":");
/*     */     
/*  57 */     if (split.length == 5) {
/*  58 */       String profileId = split[4];
/*  59 */       String profileName = split[2];
/*  60 */       String sessionToken = split[3];
/*     */       
/*  62 */       if (StringUtils.isBlank(profileId) || StringUtils.isBlank(profileName) || StringUtils.isBlank(sessionToken)) {
/*  63 */         throw new AuthenticationException("Unknown response from authentication server: " + response);
/*     */       }
/*     */       
/*  66 */       setSelectedProfile(new GameProfile(UUIDTypeAdapter.fromString(profileId), profileName));
/*  67 */       this.sessionToken = sessionToken;
/*  68 */       setUserType(UserType.LEGACY);
/*     */     } else {
/*  70 */       throw new InvalidCredentialsException(response);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void logOut() {
/*  76 */     super.logOut();
/*  77 */     this.sessionToken = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlayOnline() {
/*  82 */     return (isLoggedIn() && getSelectedProfile() != null && getAuthenticatedToken() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile[] getAvailableProfiles() {
/*  87 */     if (getSelectedProfile() != null) {
/*  88 */       return new GameProfile[] { getSelectedProfile() };
/*     */     }
/*  90 */     return new GameProfile[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
/* 101 */     throw new UnsupportedOperationException("Game profiles cannot be changed in the legacy authentication service");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAuthenticatedToken() {
/* 106 */     return this.sessionToken;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserID() {
/* 111 */     return getUsername();
/*     */   }
/*     */ 
/*     */   
/*     */   public LegacyAuthenticationService getAuthenticationService() {
/* 116 */     return (LegacyAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\legacy\LegacyUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */