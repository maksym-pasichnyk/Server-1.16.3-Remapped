/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class RootsBlock extends BushBlock {
/* 12 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
/*    */   
/*    */   protected RootsBlock(BlockBehaviour.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 20 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 25 */     return (debug1.is((Tag)BlockTags.NYLIUM) || debug1.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(debug1, debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBehaviour.OffsetType getOffsetType() {
/* 30 */     return BlockBehaviour.OffsetType.XZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RootsBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */