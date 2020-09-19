/*    */ package net.minecraft.world.level.levelgen.placement.nether;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.DecorationContext;
/*    */ import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
/*    */ 
/*    */ public class CountMultiLayerDecorator
/*    */   extends FeatureDecorator<CountConfiguration> {
/*    */   public CountMultiLayerDecorator(Codec<CountConfiguration> debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, CountConfiguration debug3, BlockPos debug4) {
/*    */     boolean debug6;
/* 25 */     List<BlockPos> debug5 = Lists.newArrayList();
/*    */     
/* 27 */     int debug7 = 0;
/*    */     do {
/* 29 */       debug6 = false;
/* 30 */       for (int debug8 = 0; debug8 < debug3.count().sample(debug2); debug8++) {
/* 31 */         int debug9 = debug2.nextInt(16) + debug4.getX();
/* 32 */         int debug10 = debug2.nextInt(16) + debug4.getZ();
/* 33 */         int debug11 = debug1.getHeight(Heightmap.Types.MOTION_BLOCKING, debug9, debug10);
/* 34 */         int debug12 = findOnGroundYPosition(debug1, debug9, debug11, debug10, debug7);
/* 35 */         if (debug12 != Integer.MAX_VALUE) {
/* 36 */           debug5.add(new BlockPos(debug9, debug12, debug10));
/* 37 */           debug6 = true;
/*    */         } 
/*    */       } 
/* 40 */       debug7++;
/* 41 */     } while (debug6);
/*    */     
/* 43 */     return debug5.stream();
/*    */   }
/*    */ 
/*    */   
/*    */   private static int findOnGroundYPosition(DecorationContext debug0, int debug1, int debug2, int debug3, int debug4) {
/* 48 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos(debug1, debug2, debug3);
/*    */     
/* 50 */     int debug6 = 0;
/* 51 */     BlockState debug7 = debug0.getBlockState((BlockPos)debug5);
/* 52 */     for (int debug8 = debug2; debug8 >= 1; debug8--) {
/* 53 */       debug5.setY(debug8 - 1);
/* 54 */       BlockState debug9 = debug0.getBlockState((BlockPos)debug5);
/* 55 */       if (!isEmpty(debug9) && isEmpty(debug7) && !debug9.is(Blocks.BEDROCK)) {
/* 56 */         if (debug6 == debug4) {
/* 57 */           return debug5.getY() + 1;
/*    */         }
/* 59 */         debug6++;
/*    */       } 
/* 61 */       debug7 = debug9;
/*    */     } 
/* 63 */     return Integer.MAX_VALUE;
/*    */   }
/*    */   
/*    */   private static boolean isEmpty(BlockState debug0) {
/* 67 */     return (debug0.isAir() || debug0.is(Blocks.WATER) || debug0.is(Blocks.LAVA));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\nether\CountMultiLayerDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */