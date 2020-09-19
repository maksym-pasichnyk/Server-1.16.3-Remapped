/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.NetherBridgePieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class NetherFortressFeature extends StructureFeature<NoneFeatureConfiguration> {
/* 23 */   private static final List<MobSpawnSettings.SpawnerData> FORTRESS_ENEMIES = (List<MobSpawnSettings.SpawnerData>)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 10, 2, 3), new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4), new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 8, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 3, 4, 4));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NetherFortressFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 32 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, NoneFeatureConfiguration debug10) {
/* 38 */     return (debug5.nextInt(5) < 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 43 */     return NetherBridgeStart::new;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
/* 48 */     return FORTRESS_ENEMIES;
/*    */   }
/*    */   
/*    */   public static class NetherBridgeStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     public NetherBridgeStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 53 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 58 */       NetherBridgePieces.StartPiece debug8 = new NetherBridgePieces.StartPiece((Random)this.random, (debug4 << 4) + 2, (debug5 << 4) + 2);
/* 59 */       this.pieces.add(debug8);
/* 60 */       debug8.addChildren((StructurePiece)debug8, this.pieces, (Random)this.random);
/*    */       
/* 62 */       List<StructurePiece> debug9 = debug8.pendingChildren;
/* 63 */       while (!debug9.isEmpty()) {
/* 64 */         int debug10 = this.random.nextInt(debug9.size());
/* 65 */         StructurePiece debug11 = debug9.remove(debug10);
/* 66 */         debug11.addChildren((StructurePiece)debug8, this.pieces, (Random)this.random);
/*    */       } 
/*    */       
/* 69 */       calculateBoundingBox();
/* 70 */       moveInsideHeights((Random)this.random, 48, 70);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\NetherFortressFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */