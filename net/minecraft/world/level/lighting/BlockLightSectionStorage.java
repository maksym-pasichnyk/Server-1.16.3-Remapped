/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*    */ 
/*    */ public class BlockLightSectionStorage extends LayerLightSectionStorage<BlockLightSectionStorage.BlockDataLayerStorageMap> {
/*    */   protected BlockLightSectionStorage(LightChunkGetter debug1) {
/* 12 */     super(LightLayer.BLOCK, debug1, new BlockDataLayerStorageMap(new Long2ObjectOpenHashMap()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLightValue(long debug1) {
/* 17 */     long debug3 = SectionPos.blockToSection(debug1);
/* 18 */     DataLayer debug5 = getDataLayer(debug3, false);
/* 19 */     if (debug5 == null) {
/* 20 */       return 0;
/*    */     }
/* 22 */     return debug5.get(
/* 23 */         SectionPos.sectionRelative(BlockPos.getX(debug1)), 
/* 24 */         SectionPos.sectionRelative(BlockPos.getY(debug1)), 
/* 25 */         SectionPos.sectionRelative(BlockPos.getZ(debug1)));
/*    */   }
/*    */   
/*    */   public static final class BlockDataLayerStorageMap
/*    */     extends DataLayerStorageMap<BlockDataLayerStorageMap> {
/*    */     public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> debug1) {
/* 31 */       super(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public BlockDataLayerStorageMap copy() {
/* 36 */       return new BlockDataLayerStorageMap(this.map.clone());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */