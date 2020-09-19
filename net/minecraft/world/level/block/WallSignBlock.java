/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
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
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallSignBlock extends SignBlock {
/*  24 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/*  31 */         Block.box(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, 
/*  32 */         Block.box(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, 
/*  33 */         Block.box(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, 
/*  34 */         Block.box(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));
/*     */ 
/*     */   
/*     */   public WallSignBlock(BlockBehaviour.Properties debug1, WoodType debug2) {
/*  38 */     super(debug1, debug2);
/*  39 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/*  44 */     return asItem().getDescriptionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  49 */     return AABBS.get(debug1.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  54 */     return debug2.getBlockState(debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite())).getMaterial().isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  60 */     BlockState debug2 = defaultBlockState();
/*  61 */     FluidState debug3 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/*  63 */     Level level = debug1.getLevel();
/*  64 */     BlockPos debug5 = debug1.getClickedPos();
/*     */     
/*  66 */     Direction[] debug6 = debug1.getNearestLookingDirections();
/*  67 */     for (Direction debug10 : debug6) {
/*  68 */       if (debug10.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  72 */         Direction debug11 = debug10.getOpposite();
/*     */         
/*  74 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug11);
/*  75 */         if (debug2.canSurvive((LevelReader)level, debug5)) {
/*  76 */           return (BlockState)debug2.setValue((Property)WATERLOGGED, Boolean.valueOf((debug3.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  85 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  86 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  88 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  93 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/*  98 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 103 */     debug1.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WallSignBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */