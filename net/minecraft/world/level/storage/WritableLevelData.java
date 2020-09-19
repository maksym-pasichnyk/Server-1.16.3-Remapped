/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public interface WritableLevelData extends LevelData {
/*    */   void setXSpawn(int paramInt);
/*    */   
/*    */   void setYSpawn(int paramInt);
/*    */   
/*    */   void setZSpawn(int paramInt);
/*    */   
/*    */   void setSpawnAngle(float paramFloat);
/*    */   
/*    */   default void setSpawn(BlockPos debug1, float debug2) {
/* 15 */     setXSpawn(debug1.getX());
/* 16 */     setYSpawn(debug1.getY());
/* 17 */     setZSpawn(debug1.getZ());
/* 18 */     setSpawnAngle(debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\WritableLevelData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */