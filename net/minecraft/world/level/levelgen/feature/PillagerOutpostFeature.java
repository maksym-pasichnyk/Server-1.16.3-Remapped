/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*    */ 
/*    */ public class PillagerOutpostFeature extends JigsawFeature {
/* 18 */   private static final List<MobSpawnSettings.SpawnerData> OUTPOST_ENEMIES = (List<MobSpawnSettings.SpawnerData>)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 1, 1, 1));
/*    */   
/*    */   public PillagerOutpostFeature(Codec<JigsawConfiguration> debug1) {
/* 21 */     super(debug1, 0, true, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
/* 26 */     return OUTPOST_ENEMIES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, JigsawConfiguration debug10) {
/* 31 */     int debug11 = debug6 >> 4;
/* 32 */     int debug12 = debug7 >> 4;
/*    */ 
/*    */     
/* 35 */     debug5.setSeed((debug11 ^ debug12 << 4) ^ debug3);
/* 36 */     debug5.nextInt();
/*    */     
/* 38 */     if (debug5.nextInt(5) != 0) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return !isNearVillage(debug1, debug3, debug5, debug6, debug7);
/*    */   }
/*    */   
/*    */   private boolean isNearVillage(ChunkGenerator debug1, long debug2, WorldgenRandom debug4, int debug5, int debug6) {
/* 46 */     StructureFeatureConfiguration debug7 = debug1.getSettings().getConfig(StructureFeature.VILLAGE);
/* 47 */     if (debug7 == null) {
/* 48 */       return false;
/*    */     }
/* 50 */     for (int debug8 = debug5 - 10; debug8 <= debug5 + 10; debug8++) {
/* 51 */       for (int debug9 = debug6 - 10; debug9 <= debug6 + 10; debug9++) {
/* 52 */         ChunkPos debug10 = StructureFeature.VILLAGE.getPotentialFeatureChunk(debug7, debug2, debug4, debug8, debug9);
/* 53 */         if (debug8 == debug10.x && debug9 == debug10.z) {
/* 54 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\PillagerOutpostFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */