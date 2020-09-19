/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BaseCoralWallFanBlock extends BaseCoralFanBlock {
/* 22 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 24 */   private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/* 25 */         Block.box(0.0D, 4.0D, 5.0D, 16.0D, 12.0D, 16.0D), Direction.SOUTH, 
/* 26 */         Block.box(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 11.0D), Direction.WEST, 
/* 27 */         Block.box(5.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D), Direction.EAST, 
/* 28 */         Block.box(0.0D, 4.0D, 0.0D, 11.0D, 12.0D, 16.0D)));
/*    */ 
/*    */   
/*    */   protected BaseCoralWallFanBlock(BlockBehaviour.Properties debug1) {
/* 32 */     super(debug1);
/* 33 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 38 */     return SHAPES.get(debug1.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 43 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 48 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 53 */     debug1.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 58 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 59 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 62 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 63 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 66 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 71 */     Direction debug4 = (Direction)debug1.getValue((Property)FACING);
/* 72 */     BlockPos debug5 = debug3.relative(debug4.getOpposite());
/* 73 */     BlockState debug6 = debug2.getBlockState(debug5);
/*    */     
/* 75 */     return debug6.isFaceSturdy((BlockGetter)debug2, debug5, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 81 */     BlockState debug2 = super.getStateForPlacement(debug1);
/*    */     
/* 83 */     Level level = debug1.getLevel();
/* 84 */     BlockPos debug4 = debug1.getClickedPos();
/*    */     
/* 86 */     Direction[] debug5 = debug1.getNearestLookingDirections();
/* 87 */     for (Direction debug9 : debug5) {
/* 88 */       if (debug9.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 92 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug9.getOpposite());
/* 93 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/* 94 */           return debug2;
/*    */         }
/*    */       } 
/*    */     } 
/* 98 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BaseCoralWallFanBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */