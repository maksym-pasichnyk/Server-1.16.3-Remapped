/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.GameRules;
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
/*    */ public interface LevelData
/*    */ {
/*    */   default void fillCrashReportCategory(CrashReportCategory debug1) {
/* 36 */     debug1.setDetail("Level spawn location", () -> CrashReportCategory.formatLocation(getXSpawn(), getYSpawn(), getZSpawn()));
/* 37 */     debug1.setDetail("Level time", () -> String.format("%d game time, %d day time", new Object[] { Long.valueOf(getGameTime()), Long.valueOf(getDayTime()) }));
/*    */   }
/*    */   
/*    */   int getXSpawn();
/*    */   
/*    */   int getYSpawn();
/*    */   
/*    */   int getZSpawn();
/*    */   
/*    */   float getSpawnAngle();
/*    */   
/*    */   long getGameTime();
/*    */   
/*    */   long getDayTime();
/*    */   
/*    */   boolean isThundering();
/*    */   
/*    */   boolean isRaining();
/*    */   
/*    */   void setRaining(boolean paramBoolean);
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   GameRules getGameRules();
/*    */   
/*    */   Difficulty getDifficulty();
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\LevelData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */