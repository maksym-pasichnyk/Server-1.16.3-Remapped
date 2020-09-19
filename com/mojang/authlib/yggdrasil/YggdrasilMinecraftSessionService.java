/*     */ package com.mojang.authlib.yggdrasil;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
/*     */ import com.mojang.authlib.minecraft.InsecureTextureException;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class YggdrasilMinecraftSessionService extends HttpMinecraftSessionService {
/*  44 */   private static final String[] WHITELISTED_DOMAINS = new String[] { ".minecraft.net", ".mojang.com" };
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final String baseUrl;
/*     */   private final URL joinUrl;
/*     */   private final URL checkUrl;
/*     */   private final PublicKey publicKey;
/*  54 */   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
/*     */   
/*  56 */   private final LoadingCache<GameProfile, GameProfile> insecureProfiles = CacheBuilder.newBuilder()
/*  57 */     .expireAfterWrite(6L, TimeUnit.HOURS)
/*  58 */     .build(new CacheLoader<GameProfile, GameProfile>()
/*     */       {
/*     */         public GameProfile load(GameProfile key) throws Exception {
/*  61 */           return YggdrasilMinecraftSessionService.this.fillGameProfile(key, false);
/*     */         }
/*     */       });
/*     */   
/*     */   protected YggdrasilMinecraftSessionService(YggdrasilAuthenticationService service, Environment env) {
/*  66 */     super(service);
/*     */     
/*  68 */     this.baseUrl = env.getSessionHost() + "/session/minecraft/";
/*     */     
/*  70 */     this.joinUrl = HttpAuthenticationService.constantURL(this.baseUrl + "join");
/*  71 */     this.checkUrl = HttpAuthenticationService.constantURL(this.baseUrl + "hasJoined");
/*     */     
/*     */     try {
/*  74 */       X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
/*  75 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/*  76 */       this.publicKey = keyFactory.generatePublic(spec);
/*  77 */     } catch (Exception ignored) {
/*  78 */       throw new Error("Missing/invalid yggdrasil public key!");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
/*  84 */     JoinMinecraftServerRequest request = new JoinMinecraftServerRequest();
/*  85 */     request.accessToken = authenticationToken;
/*  86 */     request.selectedProfile = profile.getId();
/*  87 */     request.serverId = serverId;
/*     */     
/*  89 */     getAuthenticationService().makeRequest(this.joinUrl, request, Response.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile hasJoinedServer(GameProfile user, String serverId, InetAddress address) throws AuthenticationUnavailableException {
/*  94 */     Map<String, Object> arguments = new HashMap<>();
/*     */     
/*  96 */     arguments.put("username", user.getName());
/*  97 */     arguments.put("serverId", serverId);
/*     */     
/*  99 */     if (address != null) {
/* 100 */       arguments.put("ip", address.getHostAddress());
/*     */     }
/*     */     
/* 103 */     URL url = HttpAuthenticationService.concatenateURL(this.checkUrl, HttpAuthenticationService.buildQuery(arguments));
/*     */     
/*     */     try {
/* 106 */       HasJoinedMinecraftServerResponse response = getAuthenticationService().<HasJoinedMinecraftServerResponse>makeRequest(url, null, HasJoinedMinecraftServerResponse.class);
/*     */       
/* 108 */       if (response != null && response.getId() != null) {
/* 109 */         GameProfile result = new GameProfile(response.getId(), user.getName());
/*     */         
/* 111 */         if (response.getProperties() != null) {
/* 112 */           result.getProperties().putAll((Multimap)response.getProperties());
/*     */         }
/*     */         
/* 115 */         return result;
/*     */       } 
/* 117 */       return null;
/*     */     }
/* 119 */     catch (AuthenticationUnavailableException e) {
/* 120 */       throw e;
/* 121 */     } catch (AuthenticationException ignored) {
/* 122 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
/*     */     MinecraftTexturesPayload result;
/* 128 */     Property textureProperty = (Property)Iterables.getFirst(profile.getProperties().get("textures"), null);
/*     */     
/* 130 */     if (textureProperty == null) {
/* 131 */       return new HashMap<>();
/*     */     }
/*     */     
/* 134 */     if (requireSecure) {
/* 135 */       if (!textureProperty.hasSignature()) {
/* 136 */         LOGGER.error("Signature is missing from textures payload");
/* 137 */         throw new InsecureTextureException("Signature is missing from textures payload");
/*     */       } 
/*     */       
/* 140 */       if (!textureProperty.isSignatureValid(this.publicKey)) {
/* 141 */         LOGGER.error("Textures payload has been tampered with (signature invalid)");
/* 142 */         throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 148 */       String json = new String(Base64.decodeBase64(textureProperty.getValue()), Charsets.UTF_8);
/* 149 */       result = (MinecraftTexturesPayload)this.gson.fromJson(json, MinecraftTexturesPayload.class);
/* 150 */     } catch (JsonParseException e) {
/* 151 */       LOGGER.error("Could not decode textures payload", (Throwable)e);
/* 152 */       return new HashMap<>();
/*     */     } 
/*     */     
/* 155 */     if (result == null || result.getTextures() == null) {
/* 156 */       return new HashMap<>();
/*     */     }
/*     */     
/* 159 */     for (Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture> entry : (Iterable<Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture>>)result.getTextures().entrySet()) {
/* 160 */       if (!isWhitelistedDomain(((MinecraftProfileTexture)entry.getValue()).getUrl())) {
/* 161 */         LOGGER.error("Textures payload has been tampered with (non-whitelisted domain)");
/* 162 */         return new HashMap<>();
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     return result.getTextures();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
/* 171 */     if (profile.getId() == null) {
/* 172 */       return profile;
/*     */     }
/*     */     
/* 175 */     if (!requireSecure) {
/* 176 */       return (GameProfile)this.insecureProfiles.getUnchecked(profile);
/*     */     }
/*     */     
/* 179 */     return fillGameProfile(profile, true);
/*     */   }
/*     */   
/*     */   protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
/*     */     try {
/* 184 */       URL url = HttpAuthenticationService.constantURL(this.baseUrl + "profile/" + UUIDTypeAdapter.fromUUID(profile.getId()));
/* 185 */       url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + (!requireSecure ? 1 : 0));
/* 186 */       MinecraftProfilePropertiesResponse response = getAuthenticationService().<MinecraftProfilePropertiesResponse>makeRequest(url, null, MinecraftProfilePropertiesResponse.class);
/*     */       
/* 188 */       if (response == null) {
/* 189 */         LOGGER.debug("Couldn't fetch profile properties for " + profile + " as the profile does not exist");
/* 190 */         return profile;
/*     */       } 
/* 192 */       GameProfile result = new GameProfile(response.getId(), response.getName());
/* 193 */       result.getProperties().putAll((Multimap)response.getProperties());
/* 194 */       profile.getProperties().putAll((Multimap)response.getProperties());
/* 195 */       LOGGER.debug("Successfully fetched profile properties for " + profile);
/* 196 */       return result;
/*     */     }
/* 198 */     catch (AuthenticationException e) {
/* 199 */       LOGGER.warn("Couldn't look up profile properties for " + profile, (Throwable)e);
/* 200 */       return profile;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public YggdrasilAuthenticationService getAuthenticationService() {
/* 206 */     return (YggdrasilAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */   
/*     */   private static boolean isWhitelistedDomain(String url) {
/* 210 */     URI uri = null;
/*     */     
/*     */     try {
/* 213 */       uri = new URI(url);
/* 214 */     } catch (URISyntaxException ignored) {
/* 215 */       throw new IllegalArgumentException("Invalid URL '" + url + "'");
/*     */     } 
/*     */     
/* 218 */     String domain = uri.getHost();
/*     */     
/* 220 */     for (int i = 0; i < WHITELISTED_DOMAINS.length; i++) {
/* 221 */       if (domain.endsWith(WHITELISTED_DOMAINS[i])) {
/* 222 */         return true;
/*     */       }
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\YggdrasilMinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */