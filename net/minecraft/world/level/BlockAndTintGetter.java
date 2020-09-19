/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BlockAndTintGetter
/*    */   extends BlockGetter
/*    */ {
/*    */   LevelLightEngine getLightEngine();
/*    */   
/*    */   default int getBrightness(LightLayer debug1, BlockPos debug2) {
/* 15 */     return getLightEngine().getLayerListener(debug1).getLightValue(debug2);
/*    */   }
/*    */   
/*    */   default int getRawBrightness(BlockPos debug1, int debug2) {
/* 19 */     return getLightEngine().getRawBrightness(debug1, debug2);
/*    */   }
/*    */   
/*    */   default boolean canSeeSky(BlockPos debug1) {
/* 23 */     return (getBrightness(LightLayer.SKY, debug1) >= getMaxLightLevel());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\BlockAndTintGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */