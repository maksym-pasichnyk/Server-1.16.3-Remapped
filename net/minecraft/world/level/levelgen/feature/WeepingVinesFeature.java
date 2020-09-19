/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class WeepingVinesFeature extends Feature<NoneFeatureConfiguration> {
/*  19 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   public WeepingVinesFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  22 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/*  27 */     if (!debug1.isEmptyBlock(debug4)) {
/*  28 */       return false;
/*     */     }
/*     */     
/*  31 */     BlockState debug6 = debug1.getBlockState(debug4.above());
/*  32 */     if (!debug6.is(Blocks.NETHERRACK) && !debug6.is(Blocks.NETHER_WART_BLOCK)) {
/*  33 */       return false;
/*     */     }
/*     */     
/*  36 */     placeRoofNetherWart((LevelAccessor)debug1, debug3, debug4);
/*  37 */     placeRoofWeepingVines((LevelAccessor)debug1, debug3, debug4);
/*     */     
/*  39 */     return true;
/*     */   }
/*     */   
/*     */   private void placeRoofNetherWart(LevelAccessor debug1, Random debug2, BlockPos debug3) {
/*  43 */     debug1.setBlock(debug3, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
/*     */     
/*  45 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/*  46 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*     */     
/*  48 */     for (int debug6 = 0; debug6 < 200; debug6++) {
/*  49 */       debug4.setWithOffset((Vec3i)debug3, debug2.nextInt(6) - debug2.nextInt(6), debug2.nextInt(2) - debug2.nextInt(5), debug2.nextInt(6) - debug2.nextInt(6));
/*  50 */       if (debug1.isEmptyBlock((BlockPos)debug4)) {
/*     */ 
/*     */ 
/*     */         
/*  54 */         int debug7 = 0;
/*  55 */         for (Direction debug11 : DIRECTIONS) {
/*  56 */           BlockState debug12 = debug1.getBlockState((BlockPos)debug5.setWithOffset((Vec3i)debug4, debug11));
/*  57 */           if (debug12.is(Blocks.NETHERRACK) || debug12.is(Blocks.NETHER_WART_BLOCK)) {
/*  58 */             debug7++;
/*     */           }
/*     */           
/*  61 */           if (debug7 > 1) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/*  66 */         if (debug7 == 1)
/*  67 */           debug1.setBlock((BlockPos)debug4, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeRoofWeepingVines(LevelAccessor debug1, Random debug2, BlockPos debug3) {
/*  73 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/*     */     
/*  75 */     for (int debug5 = 0; debug5 < 100; debug5++) {
/*  76 */       debug4.setWithOffset((Vec3i)debug3, debug2.nextInt(8) - debug2.nextInt(8), debug2.nextInt(2) - debug2.nextInt(7), debug2.nextInt(8) - debug2.nextInt(8));
/*  77 */       if (debug1.isEmptyBlock((BlockPos)debug4)) {
/*     */ 
/*     */ 
/*     */         
/*  81 */         BlockState debug6 = debug1.getBlockState(debug4.above());
/*  82 */         if (debug6.is(Blocks.NETHERRACK) || debug6.is(Blocks.NETHER_WART_BLOCK)) {
/*     */ 
/*     */ 
/*     */           
/*  86 */           int debug7 = Mth.nextInt(debug2, 1, 8);
/*  87 */           if (debug2.nextInt(6) == 0) {
/*  88 */             debug7 *= 2;
/*     */           }
/*  90 */           if (debug2.nextInt(5) == 0) {
/*  91 */             debug7 = 1;
/*     */           }
/*     */           
/*  94 */           int debug8 = 17;
/*  95 */           int debug9 = 25;
/*  96 */           placeWeepingVinesColumn(debug1, debug2, debug4, debug7, 17, 25);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } public static void placeWeepingVinesColumn(LevelAccessor debug0, Random debug1, BlockPos.MutableBlockPos debug2, int debug3, int debug4, int debug5) {
/* 101 */     for (int debug6 = 0; debug6 <= debug3; debug6++) {
/* 102 */       if (debug0.isEmptyBlock((BlockPos)debug2)) {
/* 103 */         if (debug6 == debug3 || !debug0.isEmptyBlock(debug2.below())) {
/* 104 */           debug0.setBlock((BlockPos)debug2, (BlockState)Blocks.WEEPING_VINES.defaultBlockState().setValue((Property)GrowingPlantHeadBlock.AGE, Integer.valueOf(Mth.nextInt(debug1, debug4, debug5))), 2);
/*     */           break;
/*     */         } 
/* 107 */         debug0.setBlock((BlockPos)debug2, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
/*     */       } 
/*     */ 
/*     */       
/* 111 */       debug2.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WeepingVinesFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */