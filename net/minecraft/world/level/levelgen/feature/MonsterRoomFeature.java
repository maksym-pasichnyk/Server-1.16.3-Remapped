/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class MonsterRoomFeature extends Feature<NoneFeatureConfiguration> {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*  27 */   private static final EntityType<?>[] MOBS = new EntityType[] { EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER };
/*  28 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public MonsterRoomFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  31 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/*  36 */     int debug6 = 3;
/*  37 */     int debug7 = debug3.nextInt(2) + 2;
/*  38 */     int debug8 = -debug7 - 1;
/*  39 */     int debug9 = debug7 + 1;
/*     */     
/*  41 */     int debug10 = -1;
/*  42 */     int debug11 = 4;
/*     */     
/*  44 */     int debug12 = debug3.nextInt(2) + 2;
/*  45 */     int debug13 = -debug12 - 1;
/*  46 */     int debug14 = debug12 + 1;
/*     */     
/*  48 */     int debug15 = 0; int i;
/*  49 */     for (i = debug8; i <= debug9; i++) {
/*  50 */       for (int debug17 = -1; debug17 <= 4; debug17++) {
/*  51 */         for (int debug18 = debug13; debug18 <= debug14; debug18++) {
/*  52 */           BlockPos debug19 = debug4.offset(i, debug17, debug18);
/*  53 */           Material debug20 = debug1.getBlockState(debug19).getMaterial();
/*  54 */           boolean debug21 = debug20.isSolid();
/*     */           
/*  56 */           if (debug17 == -1 && !debug21) {
/*  57 */             return false;
/*     */           }
/*  59 */           if (debug17 == 4 && !debug21) {
/*  60 */             return false;
/*     */           }
/*     */           
/*  63 */           if ((i == debug8 || i == debug9 || debug18 == debug13 || debug18 == debug14) && 
/*  64 */             debug17 == 0 && debug1.isEmptyBlock(debug19) && debug1.isEmptyBlock(debug19.above())) {
/*  65 */             debug15++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  72 */     if (debug15 < 1 || debug15 > 5) {
/*  73 */       return false;
/*     */     }
/*     */     
/*  76 */     for (i = debug8; i <= debug9; i++) {
/*  77 */       for (int debug17 = 3; debug17 >= -1; debug17--) {
/*  78 */         for (int debug18 = debug13; debug18 <= debug14; debug18++) {
/*  79 */           BlockPos debug19 = debug4.offset(i, debug17, debug18);
/*     */           
/*  81 */           BlockState debug20 = debug1.getBlockState(debug19);
/*  82 */           if (i == debug8 || debug17 == -1 || debug18 == debug13 || i == debug9 || debug17 == 4 || debug18 == debug14) {
/*  83 */             if (debug19.getY() >= 0 && !debug1.getBlockState(debug19.below()).getMaterial().isSolid()) {
/*  84 */               debug1.setBlock(debug19, AIR, 2);
/*  85 */             } else if (debug20.getMaterial().isSolid() && 
/*  86 */               !debug20.is(Blocks.CHEST)) {
/*  87 */               if (debug17 == -1 && debug3.nextInt(4) != 0) {
/*  88 */                 debug1.setBlock(debug19, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 2);
/*     */               } else {
/*  90 */                 debug1.setBlock(debug19, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*  95 */           } else if (!debug20.is(Blocks.CHEST) && !debug20.is(Blocks.SPAWNER)) {
/*  96 */             debug1.setBlock(debug19, AIR, 2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 103 */     for (i = 0; i < 2; i++) {
/* 104 */       for (int debug17 = 0; debug17 < 3; debug17++) {
/* 105 */         int debug18 = debug4.getX() + debug3.nextInt(debug7 * 2 + 1) - debug7;
/* 106 */         int debug19 = debug4.getY();
/* 107 */         int debug20 = debug4.getZ() + debug3.nextInt(debug12 * 2 + 1) - debug12;
/* 108 */         BlockPos debug21 = new BlockPos(debug18, debug19, debug20);
/*     */         
/* 110 */         if (debug1.isEmptyBlock(debug21)) {
/*     */ 
/*     */ 
/*     */           
/* 114 */           int debug22 = 0;
/* 115 */           for (Direction debug24 : Direction.Plane.HORIZONTAL) {
/* 116 */             if (debug1.getBlockState(debug21.relative(debug24)).getMaterial().isSolid()) {
/* 117 */               debug22++;
/*     */             }
/*     */           } 
/*     */           
/* 121 */           if (debug22 == 1) {
/*     */ 
/*     */ 
/*     */             
/* 125 */             debug1.setBlock(debug21, StructurePiece.reorient((BlockGetter)debug1, debug21, Blocks.CHEST.defaultBlockState()), 2);
/* 126 */             RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug1, debug3, debug21, BuiltInLootTables.SIMPLE_DUNGEON);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 132 */     debug1.setBlock(debug4, Blocks.SPAWNER.defaultBlockState(), 2);
/* 133 */     BlockEntity debug16 = debug1.getBlockEntity(debug4);
/*     */     
/* 135 */     if (debug16 instanceof SpawnerBlockEntity) {
/* 136 */       ((SpawnerBlockEntity)debug16).getSpawner().setEntityId(randomEntityId(debug3));
/*     */     } else {
/* 138 */       LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", Integer.valueOf(debug4.getX()), Integer.valueOf(debug4.getY()), Integer.valueOf(debug4.getZ()));
/*     */     } 
/*     */     
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   private EntityType<?> randomEntityId(Random debug1) {
/* 145 */     return (EntityType)Util.getRandom((Object[])MOBS, debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\MonsterRoomFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */