/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ public class TwistingVinesFeature extends Feature<NoneFeatureConfiguration> {
/*     */   public TwistingVinesFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  21 */     super(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/*  27 */     return place((LevelAccessor)debug1, debug3, debug4, 8, 4, 8);
/*     */   }
/*     */   
/*     */   public static boolean place(LevelAccessor debug0, Random debug1, BlockPos debug2, int debug3, int debug4, int debug5) {
/*  31 */     if (isInvalidPlacementLocation(debug0, debug2)) {
/*  32 */       return false;
/*     */     }
/*     */     
/*  35 */     placeTwistingVines(debug0, debug1, debug2, debug3, debug4, debug5);
/*  36 */     return true;
/*     */   }
/*     */   
/*     */   private static void placeTwistingVines(LevelAccessor debug0, Random debug1, BlockPos debug2, int debug3, int debug4, int debug5) {
/*  40 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/*     */     
/*  42 */     for (int debug7 = 0; debug7 < debug3 * debug3; debug7++) {
/*  43 */       debug6.set((Vec3i)debug2).move(
/*  44 */           Mth.nextInt(debug1, -debug3, debug3), 
/*  45 */           Mth.nextInt(debug1, -debug4, debug4), 
/*  46 */           Mth.nextInt(debug1, -debug3, debug3));
/*     */ 
/*     */       
/*  49 */       if (findFirstAirBlockAboveGround(debug0, debug6))
/*     */       {
/*     */ 
/*     */         
/*  53 */         if (!isInvalidPlacementLocation(debug0, (BlockPos)debug6)) {
/*     */ 
/*     */ 
/*     */           
/*  57 */           int debug8 = Mth.nextInt(debug1, 1, debug5);
/*  58 */           if (debug1.nextInt(6) == 0) {
/*  59 */             debug8 *= 2;
/*     */           }
/*  61 */           if (debug1.nextInt(5) == 0) {
/*  62 */             debug8 = 1;
/*     */           }
/*     */           
/*  65 */           int debug9 = 17;
/*  66 */           int debug10 = 25;
/*  67 */           placeWeepingVinesColumn(debug0, debug1, debug6, debug8, 17, 25);
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   private static boolean findFirstAirBlockAboveGround(LevelAccessor debug0, BlockPos.MutableBlockPos debug1) {
/*     */     while (true) {
/*  73 */       debug1.move(0, -1, 0);
/*  74 */       if (Level.isOutsideBuildHeight((BlockPos)debug1)) {
/*  75 */         return false;
/*     */       }
/*  77 */       if (!debug0.getBlockState((BlockPos)debug1).isAir()) {
/*  78 */         debug1.move(0, 1, 0);
/*  79 */         return true;
/*     */       } 
/*     */     } 
/*     */   } public static void placeWeepingVinesColumn(LevelAccessor debug0, Random debug1, BlockPos.MutableBlockPos debug2, int debug3, int debug4, int debug5) {
/*  83 */     for (int debug6 = 1; debug6 <= debug3; debug6++) {
/*  84 */       if (debug0.isEmptyBlock((BlockPos)debug2)) {
/*  85 */         if (debug6 == debug3 || !debug0.isEmptyBlock(debug2.above())) {
/*  86 */           debug0.setBlock((BlockPos)debug2, (BlockState)Blocks.TWISTING_VINES.defaultBlockState().setValue((Property)GrowingPlantHeadBlock.AGE, Integer.valueOf(Mth.nextInt(debug1, debug4, debug5))), 2);
/*     */           break;
/*     */         } 
/*  89 */         debug0.setBlock((BlockPos)debug2, Blocks.TWISTING_VINES_PLANT.defaultBlockState(), 2);
/*     */       } 
/*     */ 
/*     */       
/*  93 */       debug2.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isInvalidPlacementLocation(LevelAccessor debug0, BlockPos debug1) {
/*  98 */     if (!debug0.isEmptyBlock(debug1)) {
/*  99 */       return true;
/*     */     }
/*     */     
/* 102 */     BlockState debug2 = debug0.getBlockState(debug1.below());
/* 103 */     return (!debug2.is(Blocks.NETHERRACK) && !debug2.is(Blocks.WARPED_NYLIUM) && !debug2.is(Blocks.WARPED_WART_BLOCK));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\TwistingVinesFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */