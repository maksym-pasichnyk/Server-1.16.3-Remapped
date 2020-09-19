/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.DataPackConfig;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.levelgen.WorldGenSettings;
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
/*    */ public interface WorldData
/*    */ {
/*    */   default void fillCrashReportCategory(CrashReportCategory debug1) {
/* 32 */     debug1.setDetail("Known server brands", () -> String.join(", ", (Iterable)getKnownServerBrands()));
/* 33 */     debug1.setDetail("Level was modded", () -> Boolean.toString(wasModded()));
/* 34 */     debug1.setDetail("Level storage version", () -> {
/*    */           int debug1 = getVersion();
/*    */           return String.format("0x%05X - %s", new Object[] { Integer.valueOf(debug1), getStorageVersionName(debug1) });
/*    */         });
/*    */   }
/*    */   
/*    */   default String getStorageVersionName(int debug1) {
/* 41 */     switch (debug1) {
/*    */       case 19133:
/* 43 */         return "Anvil";
/*    */       case 19132:
/* 45 */         return "McRegion";
/*    */     } 
/* 47 */     return "Unknown?";
/*    */   }
/*    */   
/*    */   DataPackConfig getDataPackConfig();
/*    */   
/*    */   void setDataPackConfig(DataPackConfig paramDataPackConfig);
/*    */   
/*    */   boolean wasModded();
/*    */   
/*    */   Set<String> getKnownServerBrands();
/*    */   
/*    */   void setModdedInfo(String paramString, boolean paramBoolean);
/*    */   
/*    */   @Nullable
/*    */   CompoundTag getCustomBossEvents();
/*    */   
/*    */   void setCustomBossEvents(@Nullable CompoundTag paramCompoundTag);
/*    */   
/*    */   ServerLevelData overworldData();
/*    */   
/*    */   CompoundTag createTag(RegistryAccess paramRegistryAccess, @Nullable CompoundTag paramCompoundTag);
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   int getVersion();
/*    */   
/*    */   String getLevelName();
/*    */   
/*    */   GameType getGameType();
/*    */   
/*    */   void setGameType(GameType paramGameType);
/*    */   
/*    */   boolean getAllowCommands();
/*    */   
/*    */   Difficulty getDifficulty();
/*    */   
/*    */   void setDifficulty(Difficulty paramDifficulty);
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */   
/*    */   void setDifficultyLocked(boolean paramBoolean);
/*    */   
/*    */   GameRules getGameRules();
/*    */   
/*    */   CompoundTag getLoadedPlayerTag();
/*    */   
/*    */   CompoundTag endDragonFightData();
/*    */   
/*    */   void setEndDragonFightData(CompoundTag paramCompoundTag);
/*    */   
/*    */   WorldGenSettings worldGenSettings();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\WorldData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */