/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CrossCollisionBlock extends Block implements SimpleWaterloggedBlock {
/*  22 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  23 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  24 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  25 */   public static final BooleanProperty WEST = PipeBlock.WEST; protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*  26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; static {
/*  27 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(debug0 -> ((Direction)debug0.getKey()).getAxis().isHorizontal()).collect(Util.toMap());
/*     */   }
/*     */   protected final VoxelShape[] collisionShapeByIndex;
/*     */   protected final VoxelShape[] shapeByIndex;
/*  31 */   private final Object2IntMap<BlockState> stateToIndex = (Object2IntMap<BlockState>)new Object2IntOpenHashMap();
/*     */   
/*     */   protected CrossCollisionBlock(float debug1, float debug2, float debug3, float debug4, float debug5, BlockBehaviour.Properties debug6) {
/*  34 */     super(debug6);
/*     */     
/*  36 */     this.collisionShapeByIndex = makeShapes(debug1, debug2, debug5, 0.0F, debug5);
/*  37 */     this.shapeByIndex = makeShapes(debug1, debug2, debug3, 0.0F, debug4);
/*     */     
/*  39 */     for (UnmodifiableIterator<BlockState> unmodifiableIterator = this.stateDefinition.getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { BlockState debug8 = unmodifiableIterator.next();
/*  40 */       getAABBIndex(debug8); }
/*     */   
/*     */   }
/*     */   
/*     */   protected VoxelShape[] makeShapes(float debug1, float debug2, float debug3, float debug4, float debug5) {
/*  45 */     float debug6 = 8.0F - debug1;
/*  46 */     float debug7 = 8.0F + debug1;
/*     */     
/*  48 */     float debug8 = 8.0F - debug2;
/*  49 */     float debug9 = 8.0F + debug2;
/*     */     
/*  51 */     VoxelShape debug10 = Block.box(debug6, 0.0D, debug6, debug7, debug3, debug7);
/*  52 */     VoxelShape debug11 = Block.box(debug8, debug4, 0.0D, debug9, debug5, debug9);
/*  53 */     VoxelShape debug12 = Block.box(debug8, debug4, debug8, debug9, debug5, 16.0D);
/*  54 */     VoxelShape debug13 = Block.box(0.0D, debug4, debug8, debug9, debug5, debug9);
/*  55 */     VoxelShape debug14 = Block.box(debug8, debug4, debug8, 16.0D, debug5, debug9);
/*     */     
/*  57 */     VoxelShape debug15 = Shapes.or(debug11, debug14);
/*  58 */     VoxelShape debug16 = Shapes.or(debug12, debug13);
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
/*  76 */     VoxelShape[] debug17 = { Shapes.empty(), debug12, debug13, debug16, debug11, Shapes.or(debug12, debug11), Shapes.or(debug13, debug11), Shapes.or(debug16, debug11), debug14, Shapes.or(debug12, debug14), Shapes.or(debug13, debug14), Shapes.or(debug16, debug14), debug15, Shapes.or(debug12, debug15), Shapes.or(debug13, debug15), Shapes.or(debug16, debug15) };
/*     */     
/*  78 */     for (int debug18 = 0; debug18 < 16; debug18++) {
/*  79 */       debug17[debug18] = Shapes.or(debug10, debug17[debug18]);
/*     */     }
/*  81 */     return debug17;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  86 */     return !((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  91 */     return this.shapeByIndex[getAABBIndex(debug1)];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  96 */     return this.collisionShapeByIndex[getAABBIndex(debug1)];
/*     */   }
/*     */   
/*     */   private static int indexFor(Direction debug0) {
/* 100 */     return 1 << debug0.get2DDataValue();
/*     */   }
/*     */   
/*     */   protected int getAABBIndex(BlockState debug1) {
/* 104 */     return this.stateToIndex.computeIntIfAbsent(debug1, debug0 -> {
/*     */           int debug1 = 0;
/*     */           if (((Boolean)debug0.getValue((Property)NORTH)).booleanValue()) {
/*     */             debug1 |= indexFor(Direction.NORTH);
/*     */           }
/*     */           if (((Boolean)debug0.getValue((Property)EAST)).booleanValue()) {
/*     */             debug1 |= indexFor(Direction.EAST);
/*     */           }
/*     */           if (((Boolean)debug0.getValue((Property)SOUTH)).booleanValue()) {
/*     */             debug1 |= indexFor(Direction.SOUTH);
/*     */           }
/*     */           if (((Boolean)debug0.getValue((Property)WEST)).booleanValue()) {
/*     */             debug1 |= indexFor(Direction.WEST);
/*     */           }
/*     */           return debug1;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 124 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 125 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 127 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 137 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 139 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 141 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)EAST))).setValue((Property)EAST, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)NORTH));
/*     */       case null:
/* 143 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)WEST))).setValue((Property)EAST, debug1.getValue((Property)NORTH))).setValue((Property)SOUTH, debug1.getValue((Property)EAST))).setValue((Property)WEST, debug1.getValue((Property)SOUTH));
/*     */     } 
/* 145 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 151 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 153 */         return (BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 155 */         return (BlockState)((BlockState)debug1.setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 159 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CrossCollisionBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */