/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public interface LayerLightEventListener
/*    */   extends LightEventListener
/*    */ {
/*    */   @Nullable
/*    */   DataLayer getDataLayerData(SectionPos paramSectionPos);
/*    */   
/*    */   int getLightValue(BlockPos paramBlockPos);
/*    */   
/*    */   public enum DummyLightLayerEventListener
/*    */     implements LayerLightEventListener {
/* 18 */     INSTANCE;
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public DataLayer getDataLayerData(SectionPos debug1) {
/* 23 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public int getLightValue(BlockPos debug1) {
/* 28 */       return 0;
/*    */     }
/*    */     
/*    */     public void updateSectionStatus(SectionPos debug1, boolean debug2) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\LayerLightEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */