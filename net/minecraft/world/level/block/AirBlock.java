/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AirBlock extends Block {
/*    */   protected AirBlock(BlockBehaviour.Properties debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 17 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 22 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AirBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */