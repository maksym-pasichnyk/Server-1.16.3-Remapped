/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LightEventListener
/*    */ {
/*    */   default void updateSectionStatus(BlockPos debug1, boolean debug2) {
/* 17 */     updateSectionStatus(SectionPos.of(debug1), debug2);
/*    */   }
/*    */   
/*    */   void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\LightEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */