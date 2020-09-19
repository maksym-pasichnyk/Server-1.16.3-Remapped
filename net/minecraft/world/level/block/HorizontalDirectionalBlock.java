/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class HorizontalDirectionalBlock extends Block {
/*  8 */   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   protected HorizontalDirectionalBlock(BlockBehaviour.Properties debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 16 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 21 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\HorizontalDirectionalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */