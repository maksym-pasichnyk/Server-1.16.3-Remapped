/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherSproutsBlock extends BushBlock {
/* 11 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
/*    */   
/*    */   public NetherSproutsBlock(BlockBehaviour.Properties debug1) {
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
/* 24 */     return (debug1.is((Tag)BlockTags.NYLIUM) || debug1.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(debug1, debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBehaviour.OffsetType getOffsetType() {
/* 29 */     return BlockBehaviour.OffsetType.XZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NetherSproutsBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */