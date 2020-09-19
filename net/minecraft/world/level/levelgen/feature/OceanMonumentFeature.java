/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.OceanMonumentPieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class OceanMonumentFeature extends StructureFeature<NoneFeatureConfiguration> {
/* 27 */   private static final List<MobSpawnSettings.SpawnerData> MONUMENT_ENEMIES = (List<MobSpawnSettings.SpawnerData>)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.GUARDIAN, 1, 2, 4));
/*    */   
/*    */   public OceanMonumentFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 30 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean linearSeparation() {
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, NoneFeatureConfiguration debug10) {
/* 41 */     Set<Biome> debug11 = debug2.getBiomesWithin(debug6 * 16 + 9, debug1.getSeaLevel(), debug7 * 16 + 9, 16);
/* 42 */     for (Biome debug13 : debug11) {
/* 43 */       if (!debug13.getGenerationSettings().isValidStart(this)) {
/* 44 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 48 */     Set<Biome> debug12 = debug2.getBiomesWithin(debug6 * 16 + 9, debug1.getSeaLevel(), debug7 * 16 + 9, 29);
/* 49 */     for (Biome debug14 : debug12) {
/* 50 */       if (debug14.getBiomeCategory() != Biome.BiomeCategory.OCEAN && debug14.getBiomeCategory() != Biome.BiomeCategory.RIVER) {
/* 51 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 60 */     return OceanMonumentStart::new;
/*    */   }
/*    */   
/*    */   public static class OceanMonumentStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     private boolean isCreated;
/*    */     
/*    */     public OceanMonumentStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 67 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 72 */       generatePieces(debug4, debug5);
/*    */     }
/*    */     
/*    */     private void generatePieces(int debug1, int debug2) {
/* 76 */       int debug3 = debug1 * 16 - 29;
/* 77 */       int debug4 = debug2 * 16 - 29;
/* 78 */       Direction debug5 = Direction.Plane.HORIZONTAL.getRandomDirection((Random)this.random);
/*    */       
/* 80 */       this.pieces.add(new OceanMonumentPieces.MonumentBuilding((Random)this.random, debug3, debug4, debug5));
/* 81 */       calculateBoundingBox();
/*    */       
/* 83 */       this.isCreated = true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void placeInChunk(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6) {
/* 88 */       if (!this.isCreated) {
/* 89 */         this.pieces.clear();
/* 90 */         generatePieces(getChunkX(), getChunkZ());
/*    */       } 
/* 92 */       super.placeInChunk(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
/* 98 */     return MONUMENT_ENEMIES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\OceanMonumentFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */