/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class GameProfileCache
/*     */ {
/*  43 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private static boolean usesAuthentication;
/*     */   
/*  48 */   private final Map<String, GameProfileInfo> profilesByName = Maps.newConcurrentMap();
/*  49 */   private final Map<UUID, GameProfileInfo> profilesByUUID = Maps.newConcurrentMap();
/*     */   private final GameProfileRepository profileRepository;
/*  51 */   private final Gson gson = (new GsonBuilder()).create();
/*     */   private final File file;
/*  53 */   private final AtomicLong operationCount = new AtomicLong();
/*     */   
/*     */   public GameProfileCache(GameProfileRepository debug1, File debug2) {
/*  56 */     this.profileRepository = debug1;
/*  57 */     this.file = debug2;
/*     */     
/*  59 */     Lists.reverse(load()).forEach(this::safeAdd);
/*     */   }
/*     */   
/*     */   private void safeAdd(GameProfileInfo debug1) {
/*  63 */     GameProfile debug2 = debug1.getProfile();
/*  64 */     debug1.setLastAccess(getNextOperation());
/*  65 */     String debug3 = debug2.getName();
/*  66 */     if (debug3 != null) {
/*  67 */       this.profilesByName.put(debug3.toLowerCase(Locale.ROOT), debug1);
/*     */     }
/*  69 */     UUID debug4 = debug2.getId();
/*  70 */     if (debug4 != null) {
/*  71 */       this.profilesByUUID.put(debug4, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static GameProfile lookupGameProfile(GameProfileRepository debug0, String debug1) {
/*  77 */     final AtomicReference<GameProfile> result = new AtomicReference<>();
/*  78 */     ProfileLookupCallback debug3 = new ProfileLookupCallback()
/*     */       {
/*     */         public void onProfileLookupSucceeded(GameProfile debug1) {
/*  81 */           result.set(debug1);
/*     */         }
/*     */ 
/*     */         
/*     */         public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/*  86 */           result.set(null);
/*     */         }
/*     */       };
/*     */     
/*  90 */     debug0.findProfilesByNames(new String[] { debug1 }, Agent.MINECRAFT, debug3);
/*  91 */     GameProfile debug4 = debug2.get();
/*  92 */     if (!usesAuthentication() && debug4 == null) {
/*  93 */       UUID debug5 = Player.createPlayerUUID(new GameProfile(null, debug1));
/*  94 */       debug4 = new GameProfile(debug5, debug1);
/*     */     } 
/*  96 */     return debug4;
/*     */   }
/*     */   
/*     */   public static void setUsesAuthentication(boolean debug0) {
/* 100 */     usesAuthentication = debug0;
/*     */   }
/*     */   
/*     */   private static boolean usesAuthentication() {
/* 104 */     return usesAuthentication;
/*     */   }
/*     */   
/*     */   public void add(GameProfile debug1) {
/* 108 */     Calendar debug2 = Calendar.getInstance();
/* 109 */     debug2.setTime(new Date());
/* 110 */     debug2.add(2, 1);
/* 111 */     Date debug3 = debug2.getTime();
/*     */     
/* 113 */     GameProfileInfo debug4 = new GameProfileInfo(debug1, debug3);
/* 114 */     safeAdd(debug4);
/* 115 */     save();
/*     */   }
/*     */   
/*     */   private long getNextOperation() {
/* 119 */     return this.operationCount.incrementAndGet();
/*     */   }
/*     */   @Nullable
/*     */   public GameProfile get(String debug1) {
/*     */     GameProfile debug5;
/* 124 */     String debug2 = debug1.toLowerCase(Locale.ROOT);
/* 125 */     GameProfileInfo debug3 = this.profilesByName.get(debug2);
/*     */     
/* 127 */     boolean debug4 = false;
/*     */     
/* 129 */     if (debug3 != null && (new Date()).getTime() >= debug3.expirationDate.getTime()) {
/*     */       
/* 131 */       this.profilesByUUID.remove(debug3.getProfile().getId());
/* 132 */       this.profilesByName.remove(debug3.getProfile().getName().toLowerCase(Locale.ROOT));
/* 133 */       debug4 = true;
/* 134 */       debug3 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (debug3 != null) {
/* 140 */       debug3.setLastAccess(getNextOperation());
/* 141 */       debug5 = debug3.getProfile();
/*     */     } else {
/* 143 */       debug5 = lookupGameProfile(this.profileRepository, debug2);
/* 144 */       if (debug5 != null) {
/* 145 */         add(debug5);
/*     */         
/* 147 */         debug4 = false;
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     if (debug4) {
/* 152 */       save();
/*     */     }
/* 154 */     return debug5;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public GameProfile get(UUID debug1) {
/* 159 */     GameProfileInfo debug2 = this.profilesByUUID.get(debug1);
/* 160 */     if (debug2 == null) {
/* 161 */       return null;
/*     */     }
/* 163 */     debug2.setLastAccess(getNextOperation());
/* 164 */     return debug2.getProfile();
/*     */   }
/*     */   
/*     */   private static DateFormat createDateFormat() {
/* 168 */     return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*     */   }
/*     */   
/*     */   public List<GameProfileInfo> load() {
/* 172 */     List<GameProfileInfo> debug1 = Lists.newArrayList();
/* 173 */     try (Reader debug2 = Files.newReader(this.file, StandardCharsets.UTF_8)) {
/* 174 */       JsonArray debug4 = (JsonArray)this.gson.fromJson(debug2, JsonArray.class);
/* 175 */       if (debug4 == null) {
/* 176 */         return debug1;
/*     */       }
/* 178 */       DateFormat debug5 = createDateFormat();
/* 179 */       debug4.forEach(debug2 -> {
/*     */             GameProfileInfo debug3 = readGameProfile(debug2, debug0);
/*     */             if (debug3 != null) {
/*     */               debug1.add(debug3);
/*     */             }
/*     */           });
/* 185 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */     
/* 187 */     } catch (IOException|com.google.gson.JsonParseException debug2) {
/* 188 */       LOGGER.warn("Failed to load profile cache {}", this.file, debug2);
/*     */     } 
/* 190 */     return debug1;
/*     */   }
/*     */   
/*     */   public void save() {
/* 194 */     JsonArray debug1 = new JsonArray();
/* 195 */     DateFormat debug2 = createDateFormat();
/* 196 */     getTopMRUProfiles(1000).forEach(debug2 -> debug0.add(writeGameProfile(debug2, debug1)));
/*     */     
/* 198 */     String debug3 = this.gson.toJson((JsonElement)debug1);
/* 199 */     try (Writer debug4 = Files.newWriter(this.file, StandardCharsets.UTF_8)) {
/* 200 */       debug4.write(debug3);
/* 201 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Stream<GameProfileInfo> getTopMRUProfiles(int debug1) {
/* 207 */     return ImmutableList.copyOf(this.profilesByUUID.values()).stream().sorted(Comparator.<GameProfileInfo, Comparable>comparing(GameProfileInfo::getLastAccess).reversed()).limit(debug1);
/*     */   }
/*     */   
/*     */   private static JsonElement writeGameProfile(GameProfileInfo debug0, DateFormat debug1) {
/* 211 */     JsonObject debug2 = new JsonObject();
/* 212 */     debug2.addProperty("name", debug0.getProfile().getName());
/* 213 */     UUID debug3 = debug0.getProfile().getId();
/* 214 */     debug2.addProperty("uuid", (debug3 == null) ? "" : debug3.toString());
/* 215 */     debug2.addProperty("expiresOn", debug1.format(debug0.getExpirationDate()));
/* 216 */     return (JsonElement)debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static GameProfileInfo readGameProfile(JsonElement debug0, DateFormat debug1) {
/* 221 */     if (debug0.isJsonObject()) {
/* 222 */       UUID debug9; JsonObject debug2 = debug0.getAsJsonObject();
/* 223 */       JsonElement debug3 = debug2.get("name");
/* 224 */       JsonElement debug4 = debug2.get("uuid");
/* 225 */       JsonElement debug5 = debug2.get("expiresOn");
/* 226 */       if (debug3 == null || debug4 == null) {
/* 227 */         return null;
/*     */       }
/* 229 */       String debug6 = debug4.getAsString();
/* 230 */       String debug7 = debug3.getAsString();
/* 231 */       Date debug8 = null;
/* 232 */       if (debug5 != null) {
/*     */         try {
/* 234 */           debug8 = debug1.parse(debug5.getAsString());
/* 235 */         } catch (ParseException parseException) {}
/*     */       }
/*     */       
/* 238 */       if (debug7 == null || debug6 == null || debug8 == null) {
/* 239 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 243 */         debug9 = UUID.fromString(debug6);
/* 244 */       } catch (Throwable debug10) {
/* 245 */         return null;
/*     */       } 
/* 247 */       return new GameProfileInfo(new GameProfile(debug9, debug7), debug8);
/*     */     } 
/* 249 */     return null;
/*     */   }
/*     */   
/*     */   static class GameProfileInfo
/*     */   {
/*     */     private final GameProfile profile;
/*     */     private final Date expirationDate;
/*     */     private volatile long lastAccess;
/*     */     
/*     */     private GameProfileInfo(GameProfile debug1, Date debug2) {
/* 259 */       this.profile = debug1;
/* 260 */       this.expirationDate = debug2;
/*     */     }
/*     */     
/*     */     public GameProfile getProfile() {
/* 264 */       return this.profile;
/*     */     }
/*     */     
/*     */     public Date getExpirationDate() {
/* 268 */       return this.expirationDate;
/*     */     }
/*     */     
/*     */     public void setLastAccess(long debug1) {
/* 272 */       this.lastAccess = debug1;
/*     */     }
/*     */     
/*     */     public long getLastAccess() {
/* 276 */       return this.lastAccess;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\GameProfileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */