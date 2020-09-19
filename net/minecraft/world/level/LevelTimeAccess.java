/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public interface LevelTimeAccess extends LevelReader {
/*    */   long dayTime();
/*    */   
/*    */   default float getMoonBrightness() {
/*  9 */     return DimensionType.MOON_BRIGHTNESS_PER_PHASE[dimensionType().moonPhase(dayTime())];
/*    */   }
/*    */   
/*    */   default float getTimeOfDay(float debug1) {
/* 13 */     return dimensionType().timeOfDay(dayTime());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelTimeAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */