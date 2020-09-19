/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.EnvironmentParser;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.exceptions.UserMigratedException;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.UUID;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class YggdrasilAuthenticationService
/*     */   extends HttpAuthenticationService {
/*  39 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final String clientToken;
/*     */   private final Gson gson;
/*     */   private final Environment environment;
/*     */   
/*     */   public YggdrasilAuthenticationService(Proxy proxy, String clientToken) {
/*  46 */     this(proxy, clientToken, determineEnvironment());
/*     */   }
/*     */   
/*     */   public YggdrasilAuthenticationService(Proxy proxy, String clientToken, Environment environment) {
/*  50 */     super(proxy);
/*  51 */     this.clientToken = clientToken;
/*  52 */     this.environment = environment;
/*  53 */     GsonBuilder builder = new GsonBuilder();
/*  54 */     builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
/*  55 */     builder.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
/*  56 */     builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
/*  57 */     builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
/*  58 */     this.gson = builder.create();
/*  59 */     LOGGER.info("Environment: " + environment.asString());
/*     */   }
/*     */   
/*     */   private static Environment determineEnvironment() {
/*  63 */     return 
/*  64 */       EnvironmentParser.getEnvironmentFromProperties()
/*  65 */       .orElse(YggdrasilEnvironment.PROD);
/*     */   }
/*     */ 
/*     */   
/*     */   public UserAuthentication createUserAuthentication(Agent agent) {
/*  70 */     return (UserAuthentication)new YggdrasilUserAuthentication(this, agent, this.environment);
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftSessionService createMinecraftSessionService() {
/*  75 */     return (MinecraftSessionService)new YggdrasilMinecraftSessionService(this, this.environment);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfileRepository createProfileRepository() {
/*  80 */     return new YggdrasilGameProfileRepository(this, this.environment);
/*     */   }
/*     */   
/*     */   protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT) throws AuthenticationException {
/*     */     try {
/*  85 */       String jsonResult = (input == null) ? performGetRequest(url) : performPostRequest(url, this.gson.toJson(input), "application/json");
/*  86 */       Response response = (Response)this.gson.fromJson(jsonResult, classOfT);
/*     */       
/*  88 */       if (response == null) {
/*  89 */         return null;
/*     */       }
/*     */       
/*  92 */       if (StringUtils.isNotBlank(response.getError())) {
/*  93 */         if ("UserMigratedException".equals(response.getCause()))
/*  94 */           throw new UserMigratedException(response.getErrorMessage()); 
/*  95 */         if ("ForbiddenOperationException".equals(response.getError())) {
/*  96 */           throw new InvalidCredentialsException(response.getErrorMessage());
/*     */         }
/*  98 */         throw new AuthenticationException(response.getErrorMessage());
/*     */       } 
/*     */ 
/*     */       
/* 102 */       return (T)response;
/* 103 */     } catch (IOException|IllegalStateException|JsonParseException e) {
/* 104 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getClientToken() {
/* 109 */     return this.clientToken;
/*     */   }
/*     */   
/*     */   private static class GameProfileSerializer
/*     */     implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
/*     */     public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 115 */       JsonObject object = (JsonObject)json;
/* 116 */       UUID id = object.has("id") ? (UUID)context.deserialize(object.get("id"), UUID.class) : null;
/* 117 */       String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
/* 118 */       return new GameProfile(id, name);
/*     */     }
/*     */     private GameProfileSerializer() {}
/*     */     
/*     */     public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
/* 123 */       JsonObject result = new JsonObject();
/* 124 */       if (src.getId() != null) {
/* 125 */         result.add("id", context.serialize(src.getId()));
/*     */       }
/* 127 */       if (src.getName() != null) {
/* 128 */         result.addProperty("name", src.getName());
/*     */       }
/* 130 */       return (JsonElement)result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\YggdrasilAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */