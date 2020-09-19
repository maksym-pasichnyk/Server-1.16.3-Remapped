/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BambooBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class BambooFeature extends Feature<ProbabilityFeatureConfiguration> {
/* 19 */   private static final BlockState BAMBOO_TRUNK = (BlockState)((BlockState)((BlockState)Blocks.BAMBOO.defaultBlockState().setValue((Property)BambooBlock.AGE, Integer.valueOf(1))).setValue((Property)BambooBlock.LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)BambooBlock.STAGE, Integer.valueOf(0));
/* 20 */   private static final BlockState BAMBOO_FINAL_LARGE = (BlockState)((BlockState)BAMBOO_TRUNK.setValue((Property)BambooBlock.LEAVES, (Comparable)BambooLeaves.LARGE)).setValue((Property)BambooBlock.STAGE, Integer.valueOf(1));
/* 21 */   private static final BlockState BAMBOO_TOP_LARGE = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooBlock.LEAVES, (Comparable)BambooLeaves.LARGE);
/* 22 */   private static final BlockState BAMBOO_TOP_SMALL = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooBlock.LEAVES, (Comparable)BambooLeaves.SMALL);
/*    */   
/*    */   public BambooFeature(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, ProbabilityFeatureConfiguration debug5) {
/* 30 */     int debug6 = 0;
/*    */     
/* 32 */     BlockPos.MutableBlockPos debug7 = debug4.mutable();
/* 33 */     BlockPos.MutableBlockPos debug8 = debug4.mutable();
/* 34 */     if (debug1.isEmptyBlock((BlockPos)debug7)) {
/* 35 */       if (Blocks.BAMBOO.defaultBlockState().canSurvive((LevelReader)debug1, (BlockPos)debug7)) {
/* 36 */         int debug9 = debug3.nextInt(12) + 5;
/*    */ 
/*    */         
/* 39 */         if (debug3.nextFloat() < debug5.probability) {
/* 40 */           int i = debug3.nextInt(4) + 1;
/* 41 */           for (int debug11 = debug4.getX() - i; debug11 <= debug4.getX() + i; debug11++) {
/* 42 */             for (int debug12 = debug4.getZ() - i; debug12 <= debug4.getZ() + i; debug12++) {
/* 43 */               int debug13 = debug11 - debug4.getX();
/* 44 */               int debug14 = debug12 - debug4.getZ();
/* 45 */               if (debug13 * debug13 + debug14 * debug14 <= i * i) {
/*    */ 
/*    */ 
/*    */                 
/* 49 */                 debug8.set(debug11, debug1.getHeight(Heightmap.Types.WORLD_SURFACE, debug11, debug12) - 1, debug12);
/* 50 */                 if (isDirt(debug1.getBlockState((BlockPos)debug8).getBlock())) {
/* 51 */                   debug1.setBlock((BlockPos)debug8, Blocks.PODZOL.defaultBlockState(), 2);
/*    */                 }
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/* 57 */         for (int debug10 = 0; debug10 < debug9 && 
/* 58 */           debug1.isEmptyBlock((BlockPos)debug7); debug10++) {
/* 59 */           debug1.setBlock((BlockPos)debug7, BAMBOO_TRUNK, 2);
/*    */ 
/*    */ 
/*    */           
/* 63 */           debug7.move(Direction.UP, 1);
/*    */         } 
/*    */         
/* 66 */         if (debug7.getY() - debug4.getY() >= 3) {
/* 67 */           debug1.setBlock((BlockPos)debug7, BAMBOO_FINAL_LARGE, 2);
/* 68 */           debug1.setBlock((BlockPos)debug7.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
/* 69 */           debug1.setBlock((BlockPos)debug7.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
/*    */         } 
/*    */       } 
/* 72 */       debug6++;
/*    */     } 
/*    */     
/* 75 */     return (debug6 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BambooFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */