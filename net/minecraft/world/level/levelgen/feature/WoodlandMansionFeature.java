/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.WoodlandMansionPieces;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ 
/*     */ public class WoodlandMansionFeature
/*     */   extends StructureFeature<NoneFeatureConfiguration> {
/*     */   public WoodlandMansionFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  32 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean linearSeparation() {
/*  37 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, NoneFeatureConfiguration debug10) {
/*  43 */     Set<Biome> debug11 = debug2.getBiomesWithin(debug6 * 16 + 9, debug1.getSeaLevel(), debug7 * 16 + 9, 32);
/*  44 */     for (Biome debug13 : debug11) {
/*  45 */       if (!debug13.getGenerationSettings().isValidStart(this)) {
/*  46 */         return false;
/*     */       }
/*     */     } 
/*  49 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/*  54 */     return WoodlandMansionStart::new;
/*     */   }
/*     */   
/*     */   public static class WoodlandMansionStart extends StructureStart<NoneFeatureConfiguration> {
/*     */     public WoodlandMansionStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/*  59 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     }
/*     */ 
/*     */     
/*     */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/*  64 */       Rotation debug8 = Rotation.getRandom((Random)this.random);
/*     */       
/*  66 */       int debug9 = 5;
/*  67 */       int debug10 = 5;
/*  68 */       if (debug8 == Rotation.CLOCKWISE_90) {
/*  69 */         debug9 = -5;
/*  70 */       } else if (debug8 == Rotation.CLOCKWISE_180) {
/*  71 */         debug9 = -5;
/*  72 */         debug10 = -5;
/*  73 */       } else if (debug8 == Rotation.COUNTERCLOCKWISE_90) {
/*  74 */         debug10 = -5;
/*     */       } 
/*     */       
/*  77 */       int debug11 = (debug4 << 4) + 7;
/*  78 */       int debug12 = (debug5 << 4) + 7;
/*  79 */       int debug13 = debug2.getFirstOccupiedHeight(debug11, debug12, Heightmap.Types.WORLD_SURFACE_WG);
/*  80 */       int debug14 = debug2.getFirstOccupiedHeight(debug11, debug12 + debug10, Heightmap.Types.WORLD_SURFACE_WG);
/*  81 */       int debug15 = debug2.getFirstOccupiedHeight(debug11 + debug9, debug12, Heightmap.Types.WORLD_SURFACE_WG);
/*  82 */       int debug16 = debug2.getFirstOccupiedHeight(debug11 + debug9, debug12 + debug10, Heightmap.Types.WORLD_SURFACE_WG);
/*     */       
/*  84 */       int debug17 = Math.min(Math.min(debug13, debug14), Math.min(debug15, debug16));
/*     */ 
/*     */ 
/*     */       
/*  88 */       if (debug17 < 60) {
/*     */         return;
/*     */       }
/*     */       
/*  92 */       BlockPos debug18 = new BlockPos(debug4 * 16 + 8, debug17 + 1, debug5 * 16 + 8);
/*  93 */       List<WoodlandMansionPieces.WoodlandMansionPiece> debug19 = Lists.newLinkedList();
/*  94 */       WoodlandMansionPieces.generateMansion(debug3, debug18, debug8, debug19, (Random)this.random);
/*  95 */       this.pieces.addAll(debug19);
/*     */       
/*  97 */       calculateBoundingBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public void placeInChunk(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6) {
/* 102 */       super.placeInChunk(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */ 
/*     */       
/* 105 */       int debug7 = this.boundingBox.y0;
/* 106 */       for (int debug8 = debug5.x0; debug8 <= debug5.x1; debug8++) {
/* 107 */         for (int debug9 = debug5.z0; debug9 <= debug5.z1; debug9++) {
/* 108 */           BlockPos debug10 = new BlockPos(debug8, debug7, debug9);
/* 109 */           if (!debug1.isEmptyBlock(debug10) && this.boundingBox.isInside((Vec3i)debug10)) {
/*     */             
/* 111 */             boolean debug11 = false;
/* 112 */             for (StructurePiece debug13 : this.pieces) {
/* 113 */               if (debug13.getBoundingBox().isInside((Vec3i)debug10)) {
/* 114 */                 debug11 = true;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 118 */             if (debug11)
/*     */             {
/*     */               
/* 121 */               for (int debug12 = debug7 - 1; debug12 > 1; ) {
/* 122 */                 BlockPos debug13 = new BlockPos(debug8, debug12, debug9);
/* 123 */                 if (debug1.isEmptyBlock(debug13) || debug1.getBlockState(debug13).getMaterial().isLiquid()) {
/* 124 */                   debug1.setBlock(debug13, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*     */                   debug12--;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WoodlandMansionFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */