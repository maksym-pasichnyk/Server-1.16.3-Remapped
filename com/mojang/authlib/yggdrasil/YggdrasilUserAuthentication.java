/*     */ package com.mojang.authlib.yggdrasil;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.HttpUserAuthentication;
/*     */ import com.mojang.authlib.UserType;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
/*     */ import com.mojang.authlib.yggdrasil.request.RefreshRequest;
/*     */ import com.mojang.authlib.yggdrasil.request.ValidateRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.RefreshResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.authlib.yggdrasil.response.User;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class YggdrasilUserAuthentication extends HttpUserAuthentication {
/*  28 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final URL routeAuthenticate;
/*     */   
/*     */   private final URL routeRefresh;
/*     */   
/*     */   private final URL routeValidate;
/*     */   private final URL routeInvalidate;
/*     */   private final URL routeSignout;
/*     */   private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
/*     */   private final Agent agent;
/*     */   private GameProfile[] profiles;
/*     */   private String accessToken;
/*     */   private boolean isOnline;
/*     */   
/*     */   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, Agent agent) {
/*  44 */     this(authenticationService, agent, YggdrasilEnvironment.PROD);
/*     */   }
/*     */   
/*     */   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, Agent agent, Environment env) {
/*  48 */     super(authenticationService);
/*  49 */     this.agent = agent;
/*     */     
/*  51 */     LOGGER.info("Environment: " + env.getName(), new Object[] { ". AuthHost: " + env.getAuthHost() });
/*  52 */     this.routeAuthenticate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/authenticate");
/*  53 */     this.routeRefresh = HttpAuthenticationService.constantURL(env.getAuthHost() + "/refresh");
/*  54 */     this.routeValidate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/validate");
/*  55 */     this.routeInvalidate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/invalidate");
/*  56 */     this.routeSignout = HttpAuthenticationService.constantURL(env.getAuthHost() + "/signout");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canLogIn() {
/*  61 */     return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && (StringUtils.isNotBlank(getPassword()) || StringUtils.isNotBlank(getAuthenticatedToken())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void logIn() throws AuthenticationException {
/*  66 */     if (StringUtils.isBlank(getUsername())) {
/*  67 */       throw new InvalidCredentialsException("Invalid username");
/*     */     }
/*     */     
/*  70 */     if (StringUtils.isNotBlank(getAuthenticatedToken())) {
/*  71 */       logInWithToken();
/*  72 */     } else if (StringUtils.isNotBlank(getPassword())) {
/*  73 */       logInWithPassword();
/*     */     } else {
/*  75 */       throw new InvalidCredentialsException("Invalid password");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void logInWithPassword() throws AuthenticationException {
/*  80 */     if (StringUtils.isBlank(getUsername())) {
/*  81 */       throw new InvalidCredentialsException("Invalid username");
/*     */     }
/*  83 */     if (StringUtils.isBlank(getPassword())) {
/*  84 */       throw new InvalidCredentialsException("Invalid password");
/*     */     }
/*     */     
/*  87 */     LOGGER.info("Logging in with username & password");
/*     */     
/*  89 */     AuthenticationRequest request = new AuthenticationRequest(this, getUsername(), getPassword());
/*  90 */     AuthenticationResponse response = getAuthenticationService().<AuthenticationResponse>makeRequest(this.routeAuthenticate, request, AuthenticationResponse.class);
/*     */     
/*  92 */     if (!response.getClientToken().equals(getAuthenticationService().getClientToken())) {
/*  93 */       throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
/*     */     }
/*     */     
/*  96 */     if (response.getSelectedProfile() != null) {
/*  97 */       setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*  98 */     } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
/*  99 */       setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*     */     } 
/*     */     
/* 102 */     User user = response.getUser();
/*     */     
/* 104 */     if (user != null && user.getId() != null) {
/* 105 */       setUserid(user.getId());
/*     */     } else {
/* 107 */       setUserid(getUsername());
/*     */     } 
/*     */     
/* 110 */     this.isOnline = true;
/* 111 */     this.accessToken = response.getAccessToken();
/* 112 */     this.profiles = response.getAvailableProfiles();
/* 113 */     setSelectedProfile(response.getSelectedProfile());
/* 114 */     getModifiableUserProperties().clear();
/*     */     
/* 116 */     updateUserProperties(user);
/*     */   }
/*     */   
/*     */   protected void updateUserProperties(User user) {
/* 120 */     if (user == null) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     if (user.getProperties() != null) {
/* 125 */       getModifiableUserProperties().putAll((Multimap)user.getProperties());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void logInWithToken() throws AuthenticationException {
/* 130 */     if (StringUtils.isBlank(getUserID())) {
/* 131 */       if (StringUtils.isBlank(getUsername())) {
/* 132 */         setUserid(getUsername());
/*     */       } else {
/* 134 */         throw new InvalidCredentialsException("Invalid uuid & username");
/*     */       } 
/*     */     }
/* 137 */     if (StringUtils.isBlank(getAuthenticatedToken())) {
/* 138 */       throw new InvalidCredentialsException("Invalid access token");
/*     */     }
/*     */     
/* 141 */     LOGGER.info("Logging in with access token");
/*     */     
/* 143 */     if (checkTokenValidity()) {
/* 144 */       LOGGER.debug("Skipping refresh call as we're safely logged in.");
/* 145 */       this.isOnline = true;
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     RefreshRequest request = new RefreshRequest(this);
/* 150 */     RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(this.routeRefresh, request, RefreshResponse.class);
/*     */     
/* 152 */     if (!response.getClientToken().equals(getAuthenticationService().getClientToken())) {
/* 153 */       throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
/*     */     }
/*     */     
/* 156 */     if (response.getSelectedProfile() != null) {
/* 157 */       setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/* 158 */     } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
/* 159 */       setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*     */     } 
/*     */     
/* 162 */     if (response.getUser() != null && response.getUser().getId() != null) {
/* 163 */       setUserid(response.getUser().getId());
/*     */     } else {
/* 165 */       setUserid(getUsername());
/*     */     } 
/*     */     
/* 168 */     this.isOnline = true;
/* 169 */     this.accessToken = response.getAccessToken();
/* 170 */     this.profiles = response.getAvailableProfiles();
/* 171 */     setSelectedProfile(response.getSelectedProfile());
/* 172 */     getModifiableUserProperties().clear();
/*     */     
/* 174 */     updateUserProperties(response.getUser());
/*     */   }
/*     */   
/*     */   protected boolean checkTokenValidity() throws AuthenticationException {
/* 178 */     ValidateRequest request = new ValidateRequest(this);
/*     */     try {
/* 180 */       getAuthenticationService().makeRequest(this.routeValidate, request, Response.class);
/* 181 */       return true;
/* 182 */     } catch (AuthenticationException ignored) {
/* 183 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void logOut() {
/* 189 */     super.logOut();
/*     */     
/* 191 */     this.accessToken = null;
/* 192 */     this.profiles = null;
/* 193 */     this.isOnline = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile[] getAvailableProfiles() {
/* 198 */     return this.profiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoggedIn() {
/* 203 */     return StringUtils.isNotBlank(this.accessToken);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlayOnline() {
/* 208 */     return (isLoggedIn() && getSelectedProfile() != null && this.isOnline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
/* 213 */     if (!isLoggedIn()) {
/* 214 */       throw new AuthenticationException("Cannot change game profile whilst not logged in");
/*     */     }
/* 216 */     if (getSelectedProfile() != null) {
/* 217 */       throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
/*     */     }
/* 219 */     if (profile == null || !ArrayUtils.contains((Object[])this.profiles, profile)) {
/* 220 */       throw new IllegalArgumentException("Invalid profile '" + profile + "'");
/*     */     }
/*     */     
/* 223 */     RefreshRequest request = new RefreshRequest(this, profile);
/* 224 */     RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(this.routeRefresh, request, RefreshResponse.class);
/*     */     
/* 226 */     if (!response.getClientToken().equals(getAuthenticationService().getClientToken())) {
/* 227 */       throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
/*     */     }
/*     */     
/* 230 */     this.isOnline = true;
/* 231 */     this.accessToken = response.getAccessToken();
/* 232 */     setSelectedProfile(response.getSelectedProfile());
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromStorage(Map<String, Object> credentials) {
/* 237 */     super.loadFromStorage(credentials);
/*     */     
/* 239 */     this.accessToken = String.valueOf(credentials.get("accessToken"));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> saveForStorage() {
/* 244 */     Map<String, Object> result = super.saveForStorage();
/*     */     
/* 246 */     if (StringUtils.isNotBlank(getAuthenticatedToken())) {
/* 247 */       result.put("accessToken", getAuthenticatedToken());
/*     */     }
/*     */     
/* 250 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getSessionToken() {
/* 258 */     if (isLoggedIn() && getSelectedProfile() != null && canPlayOnline()) {
/* 259 */       return String.format("token:%s:%s", new Object[] { getAuthenticatedToken(), getSelectedProfile().getId() });
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthenticatedToken() {
/* 267 */     return this.accessToken;
/*     */   }
/*     */   
/*     */   public Agent getAgent() {
/* 271 */     return this.agent;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 276 */     return "YggdrasilAuthenticationService{agent=" + this.agent + ", profiles=" + 
/*     */       
/* 278 */       Arrays.toString((Object[])this.profiles) + ", selectedProfile=" + 
/* 279 */       getSelectedProfile() + ", username='" + 
/* 280 */       getUsername() + '\'' + ", isLoggedIn=" + 
/* 281 */       isLoggedIn() + ", userType=" + 
/* 282 */       getUserType() + ", canPlayOnline=" + 
/* 283 */       canPlayOnline() + ", accessToken='" + this.accessToken + '\'' + ", clientToken='" + 
/*     */       
/* 285 */       getAuthenticationService().getClientToken() + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public YggdrasilAuthenticationService getAuthenticationService() {
/* 291 */     return (YggdrasilAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\YggdrasilUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */