/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class IglooFeature extends StructureFeature<NoneFeatureConfiguration> {
/*    */   public IglooFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 22 */     return FeatureStart::new;
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     public FeatureStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 27 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 32 */       int debug8 = debug4 * 16;
/* 33 */       int debug9 = debug5 * 16;
/*    */       
/* 35 */       BlockPos debug10 = new BlockPos(debug8, 90, debug9);
/* 36 */       Rotation debug11 = Rotation.getRandom((Random)this.random);
/* 37 */       IglooPieces.addPieces(debug3, debug10, debug11, this.pieces, (Random)this.random);
/* 38 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\IglooFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */