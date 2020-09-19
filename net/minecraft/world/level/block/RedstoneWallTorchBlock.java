/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedstoneWallTorchBlock
/*     */   extends RedstoneTorchBlock {
/*  22 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  23 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   protected RedstoneWallTorchBlock(BlockBehaviour.Properties debug1) {
/*  26 */     super(debug1);
/*  27 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/*  32 */     return asItem().getDescriptionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  37 */     return WallTorchBlock.getShape(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  42 */     return Blocks.WALL_TORCH.canSurvive(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  47 */     return Blocks.WALL_TORCH.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  53 */     BlockState debug2 = Blocks.WALL_TORCH.getStateForPlacement(debug1);
/*  54 */     return (debug2 == null) ? null : (BlockState)defaultBlockState().setValue((Property)FACING, debug2.getValue((Property)FACING));
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNeighborSignal(Level debug1, BlockPos debug2, BlockState debug3) {
/*  74 */     Direction debug4 = ((Direction)debug3.getValue((Property)FACING)).getOpposite();
/*     */     
/*  76 */     return debug1.hasSignal(debug2.relative(debug4), debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  81 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue() && debug1.getValue((Property)FACING) != debug4) {
/*  82 */       return 15;
/*     */     }
/*     */     
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  90 */     return Blocks.WALL_TORCH.rotate(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/*  95 */     return Blocks.WALL_TORCH.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 100 */     debug1.add(new Property[] { (Property)FACING, (Property)LIT });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RedstoneWallTorchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */