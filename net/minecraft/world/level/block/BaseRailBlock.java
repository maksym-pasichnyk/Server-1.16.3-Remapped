/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BaseRailBlock extends Block {
/*  18 */   protected static final VoxelShape FLAT_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
/*  19 */   protected static final VoxelShape HALF_BLOCK_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
/*     */   
/*     */   private final boolean isStraight;
/*     */   
/*     */   public static boolean isRail(Level debug0, BlockPos debug1) {
/*  24 */     return isRail(debug0.getBlockState(debug1));
/*     */   }
/*     */   
/*     */   public static boolean isRail(BlockState debug0) {
/*  28 */     return (debug0.is((Tag)BlockTags.RAILS) && debug0.getBlock() instanceof BaseRailBlock);
/*     */   }
/*     */   
/*     */   protected BaseRailBlock(boolean debug1, BlockBehaviour.Properties debug2) {
/*  32 */     super(debug2);
/*  33 */     this.isStraight = debug1;
/*     */   }
/*     */   
/*     */   public boolean isStraight() {
/*  37 */     return this.isStraight;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  42 */     RailShape debug5 = debug1.is(this) ? (RailShape)debug1.getValue(getShapeProperty()) : null;
/*  43 */     if (debug5 != null && debug5.isAscending()) {
/*  44 */       return HALF_BLOCK_AABB;
/*     */     }
/*  46 */     return FLAT_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  52 */     return canSupportRigidBlock((BlockGetter)debug2, debug3.below());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  57 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/*  60 */     updateState(debug1, debug2, debug3, debug5);
/*     */   }
/*     */   
/*     */   protected BlockState updateState(BlockState debug1, Level debug2, BlockPos debug3, boolean debug4) {
/*  64 */     debug1 = updateDir(debug2, debug3, debug1, true);
/*     */     
/*  66 */     if (this.isStraight) {
/*  67 */       debug1.neighborChanged(debug2, debug3, this, debug3, debug4);
/*     */     }
/*     */     
/*  70 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  75 */     if (debug2.isClientSide || !debug2.getBlockState(debug3).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     RailShape debug7 = (RailShape)debug1.getValue(getShapeProperty());
/*     */     
/*  81 */     if (shouldBeRemoved(debug3, debug2, debug7)) {
/*  82 */       dropResources(debug1, debug2, debug3);
/*  83 */       debug2.removeBlock(debug3, debug6);
/*     */     } else {
/*  85 */       updateState(debug1, debug2, debug3, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldBeRemoved(BlockPos debug0, Level debug1, RailShape debug2) {
/*  90 */     if (!canSupportRigidBlock((BlockGetter)debug1, debug0.below())) {
/*  91 */       return true;
/*     */     }
/*  93 */     switch (debug2) {
/*     */       case ASCENDING_EAST:
/*  95 */         return !canSupportRigidBlock((BlockGetter)debug1, debug0.east());
/*     */       case ASCENDING_WEST:
/*  97 */         return !canSupportRigidBlock((BlockGetter)debug1, debug0.west());
/*     */       case ASCENDING_NORTH:
/*  99 */         return !canSupportRigidBlock((BlockGetter)debug1, debug0.north());
/*     */       case ASCENDING_SOUTH:
/* 101 */         return !canSupportRigidBlock((BlockGetter)debug1, debug0.south());
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState debug1, Level debug2, BlockPos debug3, Block debug4) {}
/*     */ 
/*     */   
/*     */   protected BlockState updateDir(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 111 */     if (debug1.isClientSide) {
/* 112 */       return debug3;
/*     */     }
/* 114 */     RailShape debug5 = (RailShape)debug3.getValue(getShapeProperty());
/* 115 */     return (new RailState(debug1, debug2, debug3)).place(debug1.hasNeighborSignal(debug2), debug4, debug5).getState();
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 120 */     return PushReaction.NORMAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 125 */     if (debug5) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 131 */     if (((RailShape)debug1.getValue(getShapeProperty())).isAscending()) {
/* 132 */       debug2.updateNeighborsAt(debug3.above(), this);
/*     */     }
/*     */     
/* 135 */     if (this.isStraight) {
/* 136 */       debug2.updateNeighborsAt(debug3, this);
/* 137 */       debug2.updateNeighborsAt(debug3.below(), this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 143 */     BlockState debug2 = defaultBlockState();
/* 144 */     Direction debug3 = debug1.getHorizontalDirection();
/* 145 */     boolean debug4 = (debug3 == Direction.EAST || debug3 == Direction.WEST);
/* 146 */     return (BlockState)debug2.setValue(getShapeProperty(), debug4 ? (Comparable)RailShape.EAST_WEST : (Comparable)RailShape.NORTH_SOUTH);
/*     */   }
/*     */   
/*     */   public abstract Property<RailShape> getShapeProperty();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BaseRailBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */