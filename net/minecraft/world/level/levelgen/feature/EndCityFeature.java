/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.EndCityPieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class EndCityFeature
/*    */   extends StructureFeature<NoneFeatureConfiguration>
/*    */ {
/*    */   public EndCityFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean linearSeparation() {
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, NoneFeatureConfiguration debug10) {
/* 35 */     return (getYPositionForFeature(debug6, debug7, debug1) >= 60);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 40 */     return EndCityStart::new;
/*    */   }
/*    */   
/*    */   private static int getYPositionForFeature(int debug0, int debug1, ChunkGenerator debug2) {
/* 44 */     Random debug3 = new Random((debug0 + debug1 * 10387313));
/* 45 */     Rotation debug4 = Rotation.getRandom(debug3);
/*    */     
/* 47 */     int debug5 = 5;
/* 48 */     int debug6 = 5;
/* 49 */     if (debug4 == Rotation.CLOCKWISE_90) {
/* 50 */       debug5 = -5;
/* 51 */     } else if (debug4 == Rotation.CLOCKWISE_180) {
/* 52 */       debug5 = -5;
/* 53 */       debug6 = -5;
/* 54 */     } else if (debug4 == Rotation.COUNTERCLOCKWISE_90) {
/* 55 */       debug6 = -5;
/*    */     } 
/*    */     
/* 58 */     int debug7 = (debug0 << 4) + 7;
/* 59 */     int debug8 = (debug1 << 4) + 7;
/* 60 */     int debug9 = debug2.getFirstOccupiedHeight(debug7, debug8, Heightmap.Types.WORLD_SURFACE_WG);
/* 61 */     int debug10 = debug2.getFirstOccupiedHeight(debug7, debug8 + debug6, Heightmap.Types.WORLD_SURFACE_WG);
/* 62 */     int debug11 = debug2.getFirstOccupiedHeight(debug7 + debug5, debug8, Heightmap.Types.WORLD_SURFACE_WG);
/* 63 */     int debug12 = debug2.getFirstOccupiedHeight(debug7 + debug5, debug8 + debug6, Heightmap.Types.WORLD_SURFACE_WG);
/*    */     
/* 65 */     return Math.min(Math.min(debug9, debug10), Math.min(debug11, debug12));
/*    */   }
/*    */   
/*    */   public static class EndCityStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     public EndCityStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 70 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 75 */       Rotation debug8 = Rotation.getRandom((Random)this.random);
/*    */       
/* 77 */       int debug9 = EndCityFeature.getYPositionForFeature(debug4, debug5, debug2);
/*    */ 
/*    */       
/* 80 */       if (debug9 < 60) {
/*    */         return;
/*    */       }
/*    */       
/* 84 */       BlockPos debug10 = new BlockPos(debug4 * 16 + 8, debug9, debug5 * 16 + 8);
/* 85 */       EndCityPieces.startHouseTower(debug3, debug10, debug8, this.pieces, (Random)this.random);
/*    */       
/* 87 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndCityFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */