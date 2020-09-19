/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EndPortalFrameBlock extends Block {
/*  25 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  26 */   public static final BooleanProperty HAS_EYE = BlockStateProperties.EYE;
/*  27 */   protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
/*  28 */   protected static final VoxelShape EYE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
/*  29 */   protected static final VoxelShape FULL_SHAPE = Shapes.or(BASE_SHAPE, EYE_SHAPE);
/*     */   private static BlockPattern portalShape;
/*     */   
/*     */   public EndPortalFrameBlock(BlockBehaviour.Properties debug1) {
/*  33 */     super(debug1);
/*  34 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HAS_EYE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  39 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  44 */     return ((Boolean)debug1.getValue((Property)HAS_EYE)).booleanValue() ? FULL_SHAPE : BASE_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  49 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite())).setValue((Property)HAS_EYE, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/*  59 */     if (((Boolean)debug1.getValue((Property)HAS_EYE)).booleanValue()) {
/*  60 */       return 15;
/*     */     }
/*     */     
/*  63 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  68 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/*  73 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  78 */     debug1.add(new Property[] { (Property)FACING, (Property)HAS_EYE });
/*     */   }
/*     */   
/*     */   public static BlockPattern getOrCreatePortalShape() {
/*  82 */     if (portalShape == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       portalShape = BlockPatternBuilder.start().aisle(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?" }).where('?', BlockInWorld.hasState(BlockStatePredicate.ANY)).where('^', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.SOUTH)))).where('>', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.WEST)))).where('v', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.NORTH)))).where('<', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.EAST)))).build();
/*     */     }
/*  98 */     return portalShape;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EndPortalFrameBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */