/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.DyeColor;
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
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallBannerBlock extends AbstractBannerBlock {
/* 21 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 23 */   private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/* 24 */         Block.box(0.0D, 0.0D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, 
/* 25 */         Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.WEST, 
/* 26 */         Block.box(14.0D, 0.0D, 0.0D, 16.0D, 12.5D, 16.0D), Direction.EAST, 
/* 27 */         Block.box(0.0D, 0.0D, 0.0D, 2.0D, 12.5D, 16.0D)));
/*    */ 
/*    */   
/*    */   public WallBannerBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/* 31 */     super(debug1, debug2);
/* 32 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescriptionId() {
/* 37 */     return asItem().getDescriptionId();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 43 */     return debug2.getBlockState(debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite())).getMaterial().isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 48 */     if (debug2 == ((Direction)debug1.getValue((Property)FACING)).getOpposite() && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 49 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 52 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 57 */     return SHAPES.get(debug1.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 62 */     BlockState debug2 = defaultBlockState();
/*    */     
/* 64 */     Level level = debug1.getLevel();
/* 65 */     BlockPos debug4 = debug1.getClickedPos();
/*    */     
/* 67 */     Direction[] debug5 = debug1.getNearestLookingDirections();
/* 68 */     for (Direction debug9 : debug5) {
/* 69 */       if (debug9.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 73 */         Direction debug10 = debug9.getOpposite();
/*    */         
/* 75 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug10);
/* 76 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/* 77 */           return debug2;
/*    */         }
/*    */       } 
/*    */     } 
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 86 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 91 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 96 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WallBannerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */