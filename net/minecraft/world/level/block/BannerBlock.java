/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BannerBlock extends AbstractBannerBlock {
/* 22 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 24 */   private static final Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();
/* 25 */   private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
/*    */   
/*    */   public BannerBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/* 28 */     super(debug1, debug2);
/* 29 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0)));
/*    */     
/* 31 */     BY_COLOR.put(debug1, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 36 */     return debug2.getBlockState(debug3.below()).getMaterial().isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 41 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 46 */     return (BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(Mth.floor(((180.0F + debug1.getRotation()) * 16.0F / 360.0F) + 0.5D) & 0xF));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 51 */     if (debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 52 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 55 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 60 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.rotate(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 65 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.mirror(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 70 */     debug1.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BannerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */