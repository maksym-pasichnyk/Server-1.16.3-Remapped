/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.bridge.game.GameVersion;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Date;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class DetectedVersion
/*     */   implements GameVersion
/*     */ {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  20 */   public static final GameVersion BUILT_IN = new DetectedVersion();
/*     */   
/*     */   private final String id;
/*     */   private final String name;
/*     */   private final boolean stable;
/*     */   private final int worldVersion;
/*     */   private final int protocolVersion;
/*     */   private final int packVersion;
/*     */   private final Date buildTime;
/*     */   private final String releaseTarget;
/*     */   
/*     */   private DetectedVersion() {
/*  32 */     this.id = UUID.randomUUID().toString().replaceAll("-", "");
/*  33 */     this.name = "1.16.3";
/*  34 */     this.stable = true;
/*  35 */     this.worldVersion = 2580;
/*  36 */     this.protocolVersion = 753;
/*  37 */     this.packVersion = 6;
/*  38 */     this.buildTime = new Date();
/*  39 */     this.releaseTarget = "1.16.3";
/*     */   }
/*     */   
/*     */   private DetectedVersion(JsonObject debug1) {
/*  43 */     this.id = GsonHelper.getAsString(debug1, "id");
/*  44 */     this.name = GsonHelper.getAsString(debug1, "name");
/*  45 */     this.releaseTarget = GsonHelper.getAsString(debug1, "release_target");
/*  46 */     this.stable = GsonHelper.getAsBoolean(debug1, "stable");
/*  47 */     this.worldVersion = GsonHelper.getAsInt(debug1, "world_version");
/*  48 */     this.protocolVersion = GsonHelper.getAsInt(debug1, "protocol_version");
/*  49 */     this.packVersion = GsonHelper.getAsInt(debug1, "pack_version");
/*  50 */     this.buildTime = Date.from(ZonedDateTime.parse(GsonHelper.getAsString(debug1, "build_time")).toInstant());
/*     */   }
/*     */   
/*     */   public static GameVersion tryDetectVersion() {
/*  54 */     try (InputStream debug0 = DetectedVersion.class.getResourceAsStream("/version.json")) {
/*  55 */       if (debug0 == null) {
/*  56 */         LOGGER.warn("Missing version information!");
/*  57 */         return BUILT_IN;
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*  63 */     catch (IOException|com.google.gson.JsonParseException debug0) {
/*  64 */       throw new IllegalStateException("Game version information is corrupt", debug0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  70 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReleaseTarget() {
/*  80 */     return this.releaseTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWorldVersion() {
/*  85 */     return this.worldVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProtocolVersion() {
/*  90 */     return this.protocolVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPackVersion() {
/*  95 */     return this.packVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getBuildTime() {
/* 100 */     return this.buildTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStable() {
/* 105 */     return this.stable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\DetectedVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */