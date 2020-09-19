/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DerivedLevelData
/*     */   implements ServerLevelData
/*     */ {
/*     */   private final WorldData worldData;
/*     */   private final ServerLevelData wrapped;
/*     */   
/*     */   public DerivedLevelData(WorldData debug1, ServerLevelData debug2) {
/*  25 */     this.worldData = debug1;
/*  26 */     this.wrapped = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getXSpawn() {
/*  31 */     return this.wrapped.getXSpawn();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYSpawn() {
/*  36 */     return this.wrapped.getYSpawn();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZSpawn() {
/*  41 */     return this.wrapped.getZSpawn();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpawnAngle() {
/*  46 */     return this.wrapped.getSpawnAngle();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getGameTime() {
/*  51 */     return this.wrapped.getGameTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDayTime() {
/*  56 */     return this.wrapped.getDayTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevelName() {
/*  61 */     return this.worldData.getLevelName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getClearWeatherTime() {
/*  66 */     return this.wrapped.getClearWeatherTime();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClearWeatherTime(int debug1) {}
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/*  75 */     return this.wrapped.isThundering();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/*  80 */     return this.wrapped.getThunderTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/*  85 */     return this.wrapped.isRaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/*  90 */     return this.wrapped.getRainTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameType() {
/*  95 */     return this.worldData.getGameType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXSpawn(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYSpawn(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZSpawn(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawnAngle(float debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameTime(long debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDayTime(long debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawn(BlockPos debug1, float debug2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThundering(boolean debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThunderTime(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaining(boolean debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTime(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameType(GameType debug1) {}
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 148 */     return this.worldData.isHardcore();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowCommands() {
/* 153 */     return this.worldData.getAllowCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 158 */     return this.wrapped.isInitialized();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialized(boolean debug1) {}
/*     */ 
/*     */   
/*     */   public GameRules getGameRules() {
/* 167 */     return this.worldData.getGameRules();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder.Settings getWorldBorder() {
/* 172 */     return this.wrapped.getWorldBorder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWorldBorder(WorldBorder.Settings debug1) {}
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 181 */     return this.worldData.getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 186 */     return this.worldData.isDifficultyLocked();
/*     */   }
/*     */ 
/*     */   
/*     */   public TimerQueue<MinecraftServer> getScheduledEvents() {
/* 191 */     return this.wrapped.getScheduledEvents();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnDelay() {
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnDelay(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnChance() {
/* 206 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnChance(int debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderId(UUID debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory debug1) {
/* 225 */     debug1.setDetail("Derived", Boolean.valueOf(true));
/* 226 */     this.wrapped.fillCrashReportCategory(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\DerivedLevelData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */