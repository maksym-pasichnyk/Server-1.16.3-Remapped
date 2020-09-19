/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.level.timers.TimerQueue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ServerLevelData
/*    */   extends WritableLevelData
/*    */ {
/*    */   default void fillCrashReportCategory(CrashReportCategory debug1) {
/* 28 */     super.fillCrashReportCategory(debug1);
/* 29 */     debug1.setDetail("Level name", this::getLevelName);
/* 30 */     debug1.setDetail("Level game mode", () -> String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { getGameType().getName(), Integer.valueOf(getGameType().getId()), Boolean.valueOf(isHardcore()), Boolean.valueOf(getAllowCommands()) }));
/* 31 */     debug1.setDetail("Level weather", () -> String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(getRainTime()), Boolean.valueOf(isRaining()), Integer.valueOf(getThunderTime()), Boolean.valueOf(isThundering()) }));
/*    */   }
/*    */   
/*    */   String getLevelName();
/*    */   
/*    */   void setThundering(boolean paramBoolean);
/*    */   
/*    */   int getRainTime();
/*    */   
/*    */   void setRainTime(int paramInt);
/*    */   
/*    */   void setThunderTime(int paramInt);
/*    */   
/*    */   int getThunderTime();
/*    */   
/*    */   int getClearWeatherTime();
/*    */   
/*    */   void setClearWeatherTime(int paramInt);
/*    */   
/*    */   int getWanderingTraderSpawnDelay();
/*    */   
/*    */   void setWanderingTraderSpawnDelay(int paramInt);
/*    */   
/*    */   int getWanderingTraderSpawnChance();
/*    */   
/*    */   void setWanderingTraderSpawnChance(int paramInt);
/*    */   
/*    */   void setWanderingTraderId(UUID paramUUID);
/*    */   
/*    */   GameType getGameType();
/*    */   
/*    */   void setWorldBorder(WorldBorder.Settings paramSettings);
/*    */   
/*    */   WorldBorder.Settings getWorldBorder();
/*    */   
/*    */   boolean isInitialized();
/*    */   
/*    */   void setInitialized(boolean paramBoolean);
/*    */   
/*    */   boolean getAllowCommands();
/*    */   
/*    */   void setGameType(GameType paramGameType);
/*    */   
/*    */   TimerQueue<MinecraftServer> getScheduledEvents();
/*    */   
/*    */   void setGameTime(long paramLong);
/*    */   
/*    */   void setDayTime(long paramLong);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\ServerLevelData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */