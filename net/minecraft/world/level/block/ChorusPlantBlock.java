/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public class ChorusPlantBlock extends PipeBlock {
/*    */   protected ChorusPlantBlock(BlockBehaviour.Properties debug1) {
/* 18 */     super(0.3125F, debug1);
/*    */     
/* 20 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)DOWN, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 25 */     return getStateForPlacement((BlockGetter)debug1.getLevel(), debug1.getClickedPos());
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockGetter debug1, BlockPos debug2) {
/* 29 */     Block debug3 = debug1.getBlockState(debug2.below()).getBlock();
/* 30 */     Block debug4 = debug1.getBlockState(debug2.above()).getBlock();
/* 31 */     Block debug5 = debug1.getBlockState(debug2.north()).getBlock();
/* 32 */     Block debug6 = debug1.getBlockState(debug2.east()).getBlock();
/* 33 */     Block debug7 = debug1.getBlockState(debug2.south()).getBlock();
/* 34 */     Block debug8 = debug1.getBlockState(debug2.west()).getBlock();
/*    */     
/* 36 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 37 */       .setValue((Property)DOWN, Boolean.valueOf((debug3 == this || debug3 == Blocks.CHORUS_FLOWER || debug3 == Blocks.END_STONE))))
/* 38 */       .setValue((Property)UP, Boolean.valueOf((debug4 == this || debug4 == Blocks.CHORUS_FLOWER))))
/* 39 */       .setValue((Property)NORTH, Boolean.valueOf((debug5 == this || debug5 == Blocks.CHORUS_FLOWER))))
/* 40 */       .setValue((Property)EAST, Boolean.valueOf((debug6 == this || debug6 == Blocks.CHORUS_FLOWER))))
/* 41 */       .setValue((Property)SOUTH, Boolean.valueOf((debug7 == this || debug7 == Blocks.CHORUS_FLOWER))))
/* 42 */       .setValue((Property)WEST, Boolean.valueOf((debug8 == this || debug8 == Blocks.CHORUS_FLOWER)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 48 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 49 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/* 50 */       return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     } 
/*    */     
/* 53 */     boolean debug7 = (debug3.getBlock() == this || debug3.is(Blocks.CHORUS_FLOWER) || (debug2 == Direction.DOWN && debug3.is(Blocks.END_STONE)));
/*    */     
/* 55 */     return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), Boolean.valueOf(debug7));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 60 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 61 */       debug2.destroyBlock(debug3, true);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 70 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/* 71 */     boolean debug5 = (!debug2.getBlockState(debug3.above()).isAir() && !debug4.isAir());
/*    */     
/* 73 */     for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/* 74 */       BlockPos debug8 = debug3.relative(debug7);
/* 75 */       Block debug9 = debug2.getBlockState(debug8).getBlock();
/* 76 */       if (debug9 == this) {
/* 77 */         if (debug5) {
/* 78 */           return false;
/*    */         }
/* 80 */         Block debug10 = debug2.getBlockState(debug8.below()).getBlock();
/* 81 */         if (debug10 == this || debug10 == Blocks.END_STONE) {
/* 82 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/* 86 */     Block debug6 = debug4.getBlock();
/* 87 */     return (debug6 == this || debug6 == Blocks.END_STONE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 92 */     debug1.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP, (Property)DOWN });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ChorusPlantBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */