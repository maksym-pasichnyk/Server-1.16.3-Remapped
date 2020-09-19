/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RotatedPillarBlock extends Block {
/* 11 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*    */   
/*    */   public RotatedPillarBlock(BlockBehaviour.Properties debug1) {
/* 14 */     super(debug1);
/* 15 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 20 */     switch (debug2) {
/*    */       case COUNTERCLOCKWISE_90:
/*    */       case CLOCKWISE_90:
/* 23 */         switch ((Direction.Axis)debug1.getValue((Property)AXIS)) {
/*    */           case COUNTERCLOCKWISE_90:
/* 25 */             return (BlockState)debug1.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*    */           case CLOCKWISE_90:
/* 27 */             return (BlockState)debug1.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*    */         } 
/* 29 */         return debug1;
/*    */     } 
/*    */     
/* 32 */     return debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 38 */     debug1.add(new Property[] { (Property)AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 43 */     return (BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)debug1.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RotatedPillarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */