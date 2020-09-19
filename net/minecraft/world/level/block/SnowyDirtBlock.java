/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SnowyDirtBlock extends Block {
/* 13 */   public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
/*    */   
/*    */   protected SnowyDirtBlock(BlockBehaviour.Properties debug1) {
/* 16 */     super(debug1);
/* 17 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SNOWY, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 22 */     if (debug2 == Direction.UP) {
/* 23 */       return (BlockState)debug1.setValue((Property)SNOWY, Boolean.valueOf((debug3.is(Blocks.SNOW_BLOCK) || debug3.is(Blocks.SNOW))));
/*    */     }
/* 25 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 30 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos().above());
/* 31 */     return (BlockState)defaultBlockState().setValue((Property)SNOWY, Boolean.valueOf((debug2.is(Blocks.SNOW_BLOCK) || debug2.is(Blocks.SNOW))));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 36 */     debug1.add(new Property[] { (Property)SNOWY });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SnowyDirtBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */