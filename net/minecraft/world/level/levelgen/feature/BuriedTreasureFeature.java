/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.BuriedTreasurePieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class BuriedTreasureFeature
/*    */   extends StructureFeature<ProbabilityFeatureConfiguration> {
/*    */   public BuriedTreasureFeature(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, ProbabilityFeatureConfiguration debug10) {
/* 26 */     debug5.setLargeFeatureWithSalt(debug3, debug6, debug7, 10387320);
/* 27 */     return (debug5.nextFloat() < debug10.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<ProbabilityFeatureConfiguration> getStartFactory() {
/* 32 */     return BuriedTreasureStart::new;
/*    */   }
/*    */   
/*    */   public static class BuriedTreasureStart extends StructureStart<ProbabilityFeatureConfiguration> {
/*    */     public BuriedTreasureStart(StructureFeature<ProbabilityFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 37 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, ProbabilityFeatureConfiguration debug7) {
/* 42 */       int debug8 = debug4 * 16;
/* 43 */       int debug9 = debug5 * 16;
/*    */       
/* 45 */       BlockPos debug10 = new BlockPos(debug8 + 9, 90, debug9 + 9);
/*    */       
/* 47 */       this.pieces.add(new BuriedTreasurePieces.BuriedTreasurePiece(debug10));
/* 48 */       calculateBoundingBox();
/*    */     }
/*    */ 
/*    */     
/*    */     public BlockPos getLocatePos() {
/* 53 */       return new BlockPos((getChunkX() << 4) + 9, 0, (getChunkZ() << 4) + 9);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BuriedTreasureFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */