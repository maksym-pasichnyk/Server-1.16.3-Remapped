/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ScaffoldingBlock extends Block implements SimpleWaterloggedBlock {
/*     */   private static final VoxelShape STABLE_SHAPE;
/*  30 */   private static final VoxelShape UNSTABLE_SHAPE_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D); private static final VoxelShape UNSTABLE_SHAPE;
/*  31 */   private static final VoxelShape BELOW_BLOCK = Shapes.block().move(0.0D, -1.0D, 0.0D);
/*     */ 
/*     */   
/*  34 */   public static final IntegerProperty DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
/*  35 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  36 */   public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
/*     */   
/*     */   static {
/*  39 */     VoxelShape debug0 = Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  40 */     VoxelShape debug1 = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D);
/*  41 */     VoxelShape debug2 = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
/*  42 */     VoxelShape debug3 = Block.box(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D);
/*  43 */     VoxelShape debug4 = Block.box(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
/*  44 */     STABLE_SHAPE = Shapes.or(debug0, new VoxelShape[] { debug1, debug2, debug3, debug4 });
/*     */     
/*  46 */     VoxelShape debug5 = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D);
/*  47 */     VoxelShape debug6 = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
/*  48 */     VoxelShape debug7 = Block.box(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 16.0D);
/*  49 */     VoxelShape debug8 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 2.0D);
/*  50 */     UNSTABLE_SHAPE = Shapes.or(UNSTABLE_SHAPE_BOTTOM, new VoxelShape[] { STABLE_SHAPE, debug6, debug5, debug8, debug7 });
/*     */   }
/*     */   
/*     */   protected ScaffoldingBlock(BlockBehaviour.Properties debug1) {
/*  54 */     super(debug1);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DISTANCE, Integer.valueOf(7))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)BOTTOM, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  60 */     debug1.add(new Property[] { (Property)DISTANCE, (Property)WATERLOGGED, (Property)BOTTOM });
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  65 */     if (!debug4.isHoldingItem(debug1.getBlock().asItem())) {
/*  66 */       return ((Boolean)debug1.getValue((Property)BOTTOM)).booleanValue() ? UNSTABLE_SHAPE : STABLE_SHAPE;
/*     */     }
/*  68 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getInteractionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  73 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/*  78 */     return (debug2.getItemInHand().getItem() == asItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  83 */     BlockPos debug2 = debug1.getClickedPos();
/*  84 */     Level debug3 = debug1.getLevel();
/*     */     
/*  86 */     int debug4 = getDistance((BlockGetter)debug3, debug2);
/*  87 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/*  88 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((debug3.getFluidState(debug2).getType() == Fluids.WATER))))
/*  89 */       .setValue((Property)DISTANCE, Integer.valueOf(debug4)))
/*  90 */       .setValue((Property)BOTTOM, Boolean.valueOf(isBottom((BlockGetter)debug3, debug2, debug4)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  95 */     if (!debug2.isClientSide) {
/*  96 */       debug2.getBlockTicks().scheduleTick(debug3, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 102 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 103 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/* 106 */     if (!debug4.isClientSide()) {
/* 107 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*     */     
/* 110 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 115 */     int debug5 = getDistance((BlockGetter)debug2, debug3);
/*     */ 
/*     */     
/* 118 */     BlockState debug6 = (BlockState)((BlockState)debug1.setValue((Property)DISTANCE, Integer.valueOf(debug5))).setValue((Property)BOTTOM, Boolean.valueOf(isBottom((BlockGetter)debug2, debug3, debug5)));
/*     */     
/* 120 */     if (((Integer)debug6.getValue((Property)DISTANCE)).intValue() == 7) {
/* 121 */       if (((Integer)debug1.getValue((Property)DISTANCE)).intValue() == 7) {
/*     */         
/* 123 */         debug2.addFreshEntity((Entity)new FallingBlockEntity((Level)debug2, debug3.getX() + 0.5D, debug3.getY(), debug3.getZ() + 0.5D, (BlockState)debug6.setValue((Property)WATERLOGGED, Boolean.valueOf(false))));
/*     */       } else {
/*     */         
/* 126 */         debug2.destroyBlock(debug3, true);
/*     */       } 
/* 128 */     } else if (debug1 != debug6) {
/* 129 */       debug2.setBlock(debug3, debug6, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 135 */     return (getDistance((BlockGetter)debug2, debug3) < 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 140 */     if (!debug4.isAbove(Shapes.block(), debug3, true) || debug4.isDescending()) {
/* 141 */       if (((Integer)debug1.getValue((Property)DISTANCE)).intValue() != 0 && ((Boolean)debug1.getValue((Property)BOTTOM)).booleanValue() && debug4.isAbove(BELOW_BLOCK, debug3, true)) {
/* 142 */         return UNSTABLE_SHAPE_BOTTOM;
/*     */       }
/* 144 */       return Shapes.empty();
/*     */     } 
/* 146 */     return STABLE_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 151 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 152 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 154 */     return super.getFluidState(debug1);
/*     */   }
/*     */   
/*     */   private boolean isBottom(BlockGetter debug1, BlockPos debug2, int debug3) {
/* 158 */     return (debug3 > 0 && !debug1.getBlockState(debug2.below()).is(this));
/*     */   }
/*     */   
/*     */   public static int getDistance(BlockGetter debug0, BlockPos debug1) {
/* 162 */     BlockPos.MutableBlockPos debug2 = debug1.mutable().move(Direction.DOWN);
/* 163 */     BlockState debug3 = debug0.getBlockState((BlockPos)debug2);
/*     */     
/* 165 */     int debug4 = 7;
/* 166 */     if (debug3.is(Blocks.SCAFFOLDING)) {
/* 167 */       debug4 = ((Integer)debug3.getValue((Property)DISTANCE)).intValue();
/*     */     }
/* 169 */     else if (debug3.isFaceSturdy(debug0, (BlockPos)debug2, Direction.UP)) {
/* 170 */       return 0;
/*     */     } 
/*     */     
/* 173 */     for (Direction debug6 : Direction.Plane.HORIZONTAL) {
/* 174 */       BlockState debug7 = debug0.getBlockState((BlockPos)debug2.setWithOffset((Vec3i)debug1, debug6));
/* 175 */       if (!debug7.is(Blocks.SCAFFOLDING)) {
/*     */         continue;
/*     */       }
/*     */       
/* 179 */       debug4 = Math.min(debug4, ((Integer)debug7.getValue((Property)DISTANCE)).intValue() + 1);
/*     */       
/* 181 */       if (debug4 == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 185 */     return debug4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ScaffoldingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */