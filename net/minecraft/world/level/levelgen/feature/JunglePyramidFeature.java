/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.JunglePyramidPiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class JunglePyramidFeature extends StructureFeature<NoneFeatureConfiguration> {
/*    */   public JunglePyramidFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 20 */     return FeatureStart::new;
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     public FeatureStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 25 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 30 */       JunglePyramidPiece debug8 = new JunglePyramidPiece((Random)this.random, debug4 * 16, debug5 * 16);
/* 31 */       this.pieces.add(debug8);
/* 32 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\JunglePyramidFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */