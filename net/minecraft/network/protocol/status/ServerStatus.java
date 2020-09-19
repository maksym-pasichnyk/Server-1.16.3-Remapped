/*     */ package net.minecraft.network.protocol.status;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerStatus
/*     */ {
/*     */   private Component description;
/*     */   private Players players;
/*     */   private Version version;
/*     */   private String favicon;
/*     */   
/*     */   public Component getDescription() {
/*  28 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(Component debug1) {
/*  32 */     this.description = debug1;
/*     */   }
/*     */   
/*     */   public Players getPlayers() {
/*  36 */     return this.players;
/*     */   }
/*     */   
/*     */   public void setPlayers(Players debug1) {
/*  40 */     this.players = debug1;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/*  44 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(Version debug1) {
/*  48 */     this.version = debug1;
/*     */   }
/*     */   
/*     */   public void setFavicon(String debug1) {
/*  52 */     this.favicon = debug1;
/*     */   }
/*     */   
/*     */   public String getFavicon() {
/*  56 */     return this.favicon;
/*     */   }
/*     */   
/*     */   public static class Players {
/*     */     private final int maxPlayers;
/*     */     private final int numPlayers;
/*     */     private GameProfile[] sample;
/*     */     
/*     */     public Players(int debug1, int debug2) {
/*  65 */       this.maxPlayers = debug1;
/*  66 */       this.numPlayers = debug2;
/*     */     }
/*     */     
/*     */     public int getMaxPlayers() {
/*  70 */       return this.maxPlayers;
/*     */     }
/*     */     
/*     */     public int getNumPlayers() {
/*  74 */       return this.numPlayers;
/*     */     }
/*     */     
/*     */     public GameProfile[] getSample() {
/*  78 */       return this.sample;
/*     */     }
/*     */     
/*     */     public void setSample(GameProfile[] debug1) {
/*  82 */       this.sample = debug1;
/*     */     }
/*     */     
/*     */     public static class Serializer
/*     */       implements JsonDeserializer<Players>, JsonSerializer<Players> {
/*     */       public ServerStatus.Players deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/*  88 */         JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "players");
/*  89 */         ServerStatus.Players debug5 = new ServerStatus.Players(GsonHelper.getAsInt(debug4, "max"), GsonHelper.getAsInt(debug4, "online"));
/*     */         
/*  91 */         if (GsonHelper.isArrayNode(debug4, "sample")) {
/*  92 */           JsonArray debug6 = GsonHelper.getAsJsonArray(debug4, "sample");
/*  93 */           if (debug6.size() > 0) {
/*  94 */             GameProfile[] debug7 = new GameProfile[debug6.size()];
/*  95 */             for (int debug8 = 0; debug8 < debug7.length; debug8++) {
/*  96 */               JsonObject debug9 = GsonHelper.convertToJsonObject(debug6.get(debug8), "player[" + debug8 + "]");
/*  97 */               String debug10 = GsonHelper.getAsString(debug9, "id");
/*  98 */               debug7[debug8] = new GameProfile(UUID.fromString(debug10), GsonHelper.getAsString(debug9, "name"));
/*     */             } 
/* 100 */             debug5.setSample(debug7);
/*     */           } 
/*     */         } 
/*     */         
/* 104 */         return debug5;
/*     */       }
/*     */ 
/*     */       
/*     */       public JsonElement serialize(ServerStatus.Players debug1, Type debug2, JsonSerializationContext debug3) {
/* 109 */         JsonObject debug4 = new JsonObject();
/*     */         
/* 111 */         debug4.addProperty("max", Integer.valueOf(debug1.getMaxPlayers()));
/* 112 */         debug4.addProperty("online", Integer.valueOf(debug1.getNumPlayers()));
/*     */         
/* 114 */         if (debug1.getSample() != null && (debug1.getSample()).length > 0) {
/* 115 */           JsonArray debug5 = new JsonArray();
/*     */           
/* 117 */           for (int debug6 = 0; debug6 < (debug1.getSample()).length; debug6++) {
/* 118 */             JsonObject debug7 = new JsonObject();
/* 119 */             UUID debug8 = debug1.getSample()[debug6].getId();
/* 120 */             debug7.addProperty("id", (debug8 == null) ? "" : debug8.toString());
/* 121 */             debug7.addProperty("name", debug1.getSample()[debug6].getName());
/* 122 */             debug5.add((JsonElement)debug7);
/*     */           } 
/*     */           
/* 125 */           debug4.add("sample", (JsonElement)debug5);
/*     */         } 
/*     */         
/* 128 */         return (JsonElement)debug4;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Version {
/*     */     private final String name;
/*     */     private final int protocol;
/*     */     
/*     */     public Version(String debug1, int debug2) {
/* 138 */       this.name = debug1;
/* 139 */       this.protocol = debug2;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 143 */       return this.name;
/*     */     }
/*     */     
/*     */     public int getProtocol() {
/* 147 */       return this.protocol;
/*     */     }
/*     */     
/*     */     public static class Serializer
/*     */       implements JsonDeserializer<Version>, JsonSerializer<Version> {
/*     */       public ServerStatus.Version deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 153 */         JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "version");
/* 154 */         return new ServerStatus.Version(GsonHelper.getAsString(debug4, "name"), GsonHelper.getAsInt(debug4, "protocol"));
/*     */       }
/*     */ 
/*     */       
/*     */       public JsonElement serialize(ServerStatus.Version debug1, Type debug2, JsonSerializationContext debug3) {
/* 159 */         JsonObject debug4 = new JsonObject();
/* 160 */         debug4.addProperty("name", debug1.getName());
/* 161 */         debug4.addProperty("protocol", Integer.valueOf(debug1.getProtocol()));
/* 162 */         return (JsonElement)debug4;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<ServerStatus>, JsonSerializer<ServerStatus> {
/*     */     public ServerStatus deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 170 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "status");
/* 171 */       ServerStatus debug5 = new ServerStatus();
/*     */       
/* 173 */       if (debug4.has("description")) {
/* 174 */         debug5.setDescription((Component)debug3.deserialize(debug4.get("description"), Component.class));
/*     */       }
/*     */       
/* 177 */       if (debug4.has("players")) {
/* 178 */         debug5.setPlayers((ServerStatus.Players)debug3.deserialize(debug4.get("players"), ServerStatus.Players.class));
/*     */       }
/*     */       
/* 181 */       if (debug4.has("version")) {
/* 182 */         debug5.setVersion((ServerStatus.Version)debug3.deserialize(debug4.get("version"), ServerStatus.Version.class));
/*     */       }
/*     */       
/* 185 */       if (debug4.has("favicon")) {
/* 186 */         debug5.setFavicon(GsonHelper.getAsString(debug4, "favicon"));
/*     */       }
/*     */       
/* 189 */       return debug5;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(ServerStatus debug1, Type debug2, JsonSerializationContext debug3) {
/* 194 */       JsonObject debug4 = new JsonObject();
/*     */       
/* 196 */       if (debug1.getDescription() != null) {
/* 197 */         debug4.add("description", debug3.serialize(debug1.getDescription()));
/*     */       }
/*     */       
/* 200 */       if (debug1.getPlayers() != null) {
/* 201 */         debug4.add("players", debug3.serialize(debug1.getPlayers()));
/*     */       }
/*     */       
/* 204 */       if (debug1.getVersion() != null) {
/* 205 */         debug4.add("version", debug3.serialize(debug1.getVersion()));
/*     */       }
/*     */       
/* 208 */       if (debug1.getFavicon() != null) {
/* 209 */         debug4.addProperty("favicon", debug1.getFavicon());
/*     */       }
/*     */       
/* 212 */       return (JsonElement)debug4;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ServerStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */