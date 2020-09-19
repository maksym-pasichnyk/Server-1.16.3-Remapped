/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DeadBushBlock extends BushBlock {
/* 11 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
/*    */   
/*    */   protected DeadBushBlock(BlockBehaviour.Properties debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 19 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 24 */     Block debug4 = debug1.getBlock();
/* 25 */     return (debug4 == Blocks.SAND || debug4 == Blocks.RED_SAND || debug4 == Blocks.TERRACOTTA || debug4 == Blocks.WHITE_TERRACOTTA || debug4 == Blocks.ORANGE_TERRACOTTA || debug4 == Blocks.MAGENTA_TERRACOTTA || debug4 == Blocks.LIGHT_BLUE_TERRACOTTA || debug4 == Blocks.YELLOW_TERRACOTTA || debug4 == Blocks.LIME_TERRACOTTA || debug4 == Blocks.PINK_TERRACOTTA || debug4 == Blocks.GRAY_TERRACOTTA || debug4 == Blocks.LIGHT_GRAY_TERRACOTTA || debug4 == Blocks.CYAN_TERRACOTTA || debug4 == Blocks.PURPLE_TERRACOTTA || debug4 == Blocks.BLUE_TERRACOTTA || debug4 == Blocks.BROWN_TERRACOTTA || debug4 == Blocks.GREEN_TERRACOTTA || debug4 == Blocks.RED_TERRACOTTA || debug4 == Blocks.BLACK_TERRACOTTA || debug4 == Blocks.DIRT || debug4 == Blocks.COARSE_DIRT || debug4 == Blocks.PODZOL);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DeadBushBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */