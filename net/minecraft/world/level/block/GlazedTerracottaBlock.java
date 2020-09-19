/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ 
/*    */ public class GlazedTerracottaBlock extends HorizontalDirectionalBlock {
/*    */   public GlazedTerracottaBlock(BlockBehaviour.Properties debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 15 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 20 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 25 */     return PushReaction.PUSH_ONLY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GlazedTerracottaBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */