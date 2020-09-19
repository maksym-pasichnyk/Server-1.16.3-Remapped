/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SkullBlock extends AbstractSkullBlock {
/*    */   public enum Types implements Type {
/* 19 */     SKELETON, WITHER_SKELETON, PLAYER, ZOMBIE, CREEPER, DRAGON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 27 */   protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
/*    */   
/*    */   protected SkullBlock(Type debug1, BlockBehaviour.Properties debug2) {
/* 30 */     super(debug1, debug2);
/* 31 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getOcclusionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 41 */     return Shapes.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 46 */     return (BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(Mth.floor((debug1.getRotation() * 16.0F / 360.0F) + 0.5D) & 0xF));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 51 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.rotate(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 56 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.mirror(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 61 */     debug1.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */   
/*    */   public static interface Type {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SkullBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */