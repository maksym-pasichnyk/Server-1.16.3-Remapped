/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.SwamplandHutPiece;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class SwamplandHutFeature extends StructureFeature<NoneFeatureConfiguration> {
/* 19 */   private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ENEMIES = (List<MobSpawnSettings.SpawnerData>)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1));
/* 20 */   private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ANIMALS = (List<MobSpawnSettings.SpawnerData>)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 1, 1));
/*    */   
/*    */   public SwamplandHutFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 23 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 28 */     return FeatureStart::new;
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     public FeatureStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 33 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 38 */       SwamplandHutPiece debug8 = new SwamplandHutPiece((Random)this.random, debug4 * 16, debug5 * 16);
/* 39 */       this.pieces.add(debug8);
/* 40 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
/* 46 */     return SWAMPHUT_ENEMIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MobSpawnSettings.SpawnerData> getSpecialAnimals() {
/* 51 */     return SWAMPHUT_ANIMALS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SwamplandHutFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */