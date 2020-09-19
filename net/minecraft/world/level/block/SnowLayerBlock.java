/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnowLayerBlock extends Block {
/*  25 */   public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
/*     */   
/*  27 */   protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[] {
/*  28 */       Shapes.empty(), 
/*  29 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), 
/*  30 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), 
/*  31 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), 
/*  32 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
/*  33 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), 
/*  34 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), 
/*  35 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), 
/*  36 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   protected SnowLayerBlock(BlockBehaviour.Properties debug1) {
/*  42 */     super(debug1);
/*  43 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LAYERS, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  48 */     switch (debug4) {
/*     */       case LAND:
/*  50 */         return (((Integer)debug1.getValue((Property)LAYERS)).intValue() < 5);
/*     */       case WATER:
/*  52 */         return false;
/*     */       case AIR:
/*  54 */         return false;
/*     */     } 
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  62 */     return SHAPE_BY_LAYER[((Integer)debug1.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  67 */     return SHAPE_BY_LAYER[((Integer)debug1.getValue((Property)LAYERS)).intValue() - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getBlockSupportShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  72 */     return SHAPE_BY_LAYER[((Integer)debug1.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getVisualShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  77 */     return SHAPE_BY_LAYER[((Integer)debug1.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  87 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/*     */     
/*  89 */     if (debug4.is(Blocks.ICE) || debug4.is(Blocks.PACKED_ICE) || debug4.is(Blocks.BARRIER)) {
/*  90 */       return false;
/*     */     }
/*  92 */     if (debug4.is(Blocks.HONEY_BLOCK) || debug4.is(Blocks.SOUL_SAND)) {
/*  93 */       return true;
/*     */     }
/*     */     
/*  96 */     return (Block.isFaceFull(debug4.getCollisionShape((BlockGetter)debug2, debug3.below()), Direction.UP) || (debug4.getBlock() == this && ((Integer)debug4.getValue((Property)LAYERS)).intValue() == 8));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 101 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 102 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 104 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 109 */     if (debug2.getBrightness(LightLayer.BLOCK, debug3) > 11) {
/* 110 */       dropResources(debug1, (Level)debug2, debug3);
/* 111 */       debug2.removeBlock(debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/* 117 */     int debug3 = ((Integer)debug1.getValue((Property)LAYERS)).intValue();
/*     */     
/* 119 */     if (debug2.getItemInHand().getItem() == asItem() && debug3 < 8) {
/* 120 */       if (debug2.replacingClickedOnBlock()) {
/* 121 */         return (debug2.getClickedFace() == Direction.UP);
/*     */       }
/* 123 */       return true;
/*     */     } 
/*     */     
/* 126 */     return (debug3 == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 132 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos());
/* 133 */     if (debug2.is(this)) {
/* 134 */       int debug3 = ((Integer)debug2.getValue((Property)LAYERS)).intValue();
/* 135 */       return (BlockState)debug2.setValue((Property)LAYERS, Integer.valueOf(Math.min(8, debug3 + 1)));
/*     */     } 
/*     */     
/* 138 */     return super.getStateForPlacement(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 143 */     debug1.add(new Property[] { (Property)LAYERS });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SnowLayerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */