/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
/*  25 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
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
/*  37 */   protected static final VoxelShape[] EAST_AABB = new VoxelShape[] {
/*  38 */       Block.box(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D), 
/*  39 */       Block.box(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D), 
/*  40 */       Block.box(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D)
/*     */     };
/*     */   
/*  43 */   protected static final VoxelShape[] WEST_AABB = new VoxelShape[] {
/*  44 */       Block.box(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D), 
/*  45 */       Block.box(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D), 
/*  46 */       Block.box(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D)
/*     */     };
/*     */   
/*  49 */   protected static final VoxelShape[] NORTH_AABB = new VoxelShape[] {
/*  50 */       Block.box(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D), 
/*  51 */       Block.box(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D), 
/*  52 */       Block.box(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D)
/*     */     };
/*     */   
/*  55 */   protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[] {
/*  56 */       Block.box(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D), 
/*  57 */       Block.box(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D), 
/*  58 */       Block.box(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D)
/*     */     };
/*     */   
/*     */   public CocoaBlock(BlockBehaviour.Properties debug1) {
/*  62 */     super(debug1);
/*  63 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  68 */     return (((Integer)debug1.getValue((Property)AGE)).intValue() < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  73 */     if (debug2.random.nextInt(5) == 0) {
/*  74 */       int debug5 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  75 */       if (debug5 < 2) {
/*  76 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug5 + 1)), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  83 */     Block debug4 = debug2.getBlockState(debug3.relative((Direction)debug1.getValue((Property)FACING))).getBlock();
/*  84 */     return debug4.is((Tag<Block>)BlockTags.JUNGLE_LOGS);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  89 */     int debug5 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  90 */     switch ((Direction)debug1.getValue((Property)FACING))
/*     */     { case SOUTH:
/*  92 */         return SOUTH_AABB[debug5];
/*     */       
/*     */       default:
/*  95 */         return NORTH_AABB[debug5];
/*     */       case WEST:
/*  97 */         return WEST_AABB[debug5];
/*     */       case EAST:
/*  99 */         break; }  return EAST_AABB[debug5];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 106 */     BlockState debug2 = defaultBlockState();
/*     */     
/* 108 */     Level level = debug1.getLevel();
/* 109 */     BlockPos debug4 = debug1.getClickedPos();
/*     */     
/* 111 */     for (Direction debug8 : debug1.getNearestLookingDirections()) {
/* 112 */       if (debug8.getAxis().isHorizontal()) {
/* 113 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug8);
/* 114 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/* 115 */           return debug2;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 125 */     if (debug2 == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 126 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 129 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 134 */     return (((Integer)debug3.getValue((Property)AGE)).intValue() < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 144 */     debug1.setBlock(debug3, (BlockState)debug4.setValue((Property)AGE, Integer.valueOf(((Integer)debug4.getValue((Property)AGE)).intValue() + 1)), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 149 */     debug1.add(new Property[] { (Property)FACING, (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 154 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CocoaBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */