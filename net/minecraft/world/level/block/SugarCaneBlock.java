/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SugarCaneBlock extends Block {
/* 21 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*    */ 
/*    */   
/* 24 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
/*    */   
/*    */   protected SugarCaneBlock(BlockBehaviour.Properties debug1) {
/* 27 */     super(debug1);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 33 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 38 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 39 */       debug2.destroyBlock(debug3, true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 45 */     if (debug2.isEmptyBlock(debug3.above())) {
/* 46 */       int debug5 = 1;
/* 47 */       while (debug2.getBlockState(debug3.below(debug5)).is(this)) {
/* 48 */         debug5++;
/*    */       }
/* 50 */       if (debug5 < 3) {
/* 51 */         int debug6 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/* 52 */         if (debug6 == 15) {
/* 53 */           debug2.setBlockAndUpdate(debug3.above(), defaultBlockState());
/* 54 */           debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(0)), 4);
/*    */         } else {
/* 56 */           debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug6 + 1)), 4);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 64 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 65 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*    */     }
/*    */     
/* 68 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 73 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/* 74 */     if (debug4.getBlock() == this) {
/* 75 */       return true;
/*    */     }
/*    */     
/* 78 */     if (debug4.is(Blocks.GRASS_BLOCK) || debug4.is(Blocks.DIRT) || debug4.is(Blocks.COARSE_DIRT) || debug4.is(Blocks.PODZOL) || debug4.is(Blocks.SAND) || debug4.is(Blocks.RED_SAND)) {
/* 79 */       BlockPos debug5 = debug3.below();
/* 80 */       for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/* 81 */         BlockState debug8 = debug2.getBlockState(debug5.relative(debug7));
/* 82 */         FluidState debug9 = debug2.getFluidState(debug5.relative(debug7));
/* 83 */         if (debug9.is((Tag)FluidTags.WATER) || debug8.is(Blocks.FROSTED_ICE)) {
/* 84 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 94 */     debug1.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SugarCaneBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */