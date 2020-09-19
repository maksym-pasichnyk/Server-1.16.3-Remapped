/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {
/* 16 */   public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
/*    */   
/*    */   protected FaceAttachedHorizontalDirectionalBlock(BlockBehaviour.Properties debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 24 */     return canAttach(debug2, debug3, getConnectedDirection(debug1).getOpposite());
/*    */   }
/*    */   
/*    */   public static boolean canAttach(LevelReader debug0, BlockPos debug1, Direction debug2) {
/* 28 */     BlockPos debug3 = debug1.relative(debug2);
/* 29 */     return debug0.getBlockState(debug3).isFaceSturdy((BlockGetter)debug0, debug3, debug2.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 35 */     for (Direction debug5 : debug1.getNearestLookingDirections()) {
/*    */       BlockState debug6;
/* 37 */       if (debug5.getAxis() == Direction.Axis.Y) {
/* 38 */         debug6 = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (debug5 == Direction.UP) ? (Comparable)AttachFace.CEILING : (Comparable)AttachFace.FLOOR)).setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection());
/*    */       } else {
/* 40 */         debug6 = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (Comparable)AttachFace.WALL)).setValue((Property)FACING, (Comparable)debug5.getOpposite());
/*    */       } 
/*    */       
/* 43 */       if (debug6.canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/* 44 */         return debug6;
/*    */       }
/*    */     } 
/*    */     
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 53 */     if (getConnectedDirection(debug1).getOpposite() == debug2 && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 54 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 56 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   protected static Direction getConnectedDirection(BlockState debug0) {
/* 60 */     switch ((AttachFace)debug0.getValue((Property)FACE)) {
/*    */       case CEILING:
/* 62 */         return Direction.DOWN;
/*    */       case FLOOR:
/* 64 */         return Direction.UP;
/*    */     } 
/* 66 */     return (Direction)debug0.getValue((Property)FACING);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FaceAttachedHorizontalDirectionalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */