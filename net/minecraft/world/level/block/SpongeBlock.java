/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.Tuple;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class SpongeBlock
/*    */   extends Block {
/*    */   protected SpongeBlock(BlockBehaviour.Properties debug1) {
/* 22 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 27 */     if (debug4.is(debug1.getBlock())) {
/*    */       return;
/*    */     }
/* 30 */     tryAbsorbWater(debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 35 */     tryAbsorbWater(debug2, debug3);
/* 36 */     super.neighborChanged(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   protected void tryAbsorbWater(Level debug1, BlockPos debug2) {
/* 40 */     if (removeWaterBreadthFirstSearch(debug1, debug2)) {
/*    */       
/* 42 */       debug1.setBlock(debug2, Blocks.WET_SPONGE.defaultBlockState(), 2);
/* 43 */       debug1.levelEvent(2001, debug2, Block.getId(Blocks.WATER.defaultBlockState()));
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean removeWaterBreadthFirstSearch(Level debug1, BlockPos debug2) {
/* 48 */     Queue<Tuple<BlockPos, Integer>> debug3 = Lists.newLinkedList();
/* 49 */     debug3.add(new Tuple(debug2, Integer.valueOf(0)));
/*    */     
/* 51 */     int debug4 = 0;
/* 52 */     while (!debug3.isEmpty()) {
/* 53 */       Tuple<BlockPos, Integer> debug5 = debug3.poll();
/* 54 */       BlockPos debug6 = (BlockPos)debug5.getA();
/* 55 */       int debug7 = ((Integer)debug5.getB()).intValue();
/*    */       
/* 57 */       for (Direction debug11 : Direction.values()) {
/* 58 */         BlockPos debug12 = debug6.relative(debug11);
/* 59 */         BlockState debug13 = debug1.getBlockState(debug12);
/* 60 */         FluidState debug14 = debug1.getFluidState(debug12);
/* 61 */         Material debug15 = debug13.getMaterial();
/* 62 */         if (debug14.is((Tag)FluidTags.WATER)) {
/* 63 */           if (debug13.getBlock() instanceof BucketPickup && ((BucketPickup)debug13.getBlock()).takeLiquid((LevelAccessor)debug1, debug12, debug13) != Fluids.EMPTY) {
/* 64 */             debug4++;
/* 65 */             if (debug7 < 6) {
/* 66 */               debug3.add(new Tuple(debug12, Integer.valueOf(debug7 + 1)));
/*    */             }
/* 68 */           } else if (debug13.getBlock() instanceof LiquidBlock) {
/* 69 */             debug1.setBlock(debug12, Blocks.AIR.defaultBlockState(), 3);
/* 70 */             debug4++;
/* 71 */             if (debug7 < 6) {
/* 72 */               debug3.add(new Tuple(debug12, Integer.valueOf(debug7 + 1)));
/*    */             }
/* 74 */           } else if (debug15 == Material.WATER_PLANT || debug15 == Material.REPLACEABLE_WATER_PLANT) {
/* 75 */             BlockEntity debug16 = debug13.getBlock().isEntityBlock() ? debug1.getBlockEntity(debug12) : null;
/* 76 */             dropResources(debug13, (LevelAccessor)debug1, debug12, debug16);
/* 77 */             debug1.setBlock(debug12, Blocks.AIR.defaultBlockState(), 3);
/* 78 */             debug4++;
/* 79 */             if (debug7 < 6) {
/* 80 */               debug3.add(new Tuple(debug12, Integer.valueOf(debug7 + 1)));
/*    */             }
/*    */           } 
/*    */         }
/*    */       } 
/* 85 */       if (debug4 > 64) {
/*    */         break;
/*    */       }
/*    */     } 
/* 89 */     return (debug4 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SpongeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */