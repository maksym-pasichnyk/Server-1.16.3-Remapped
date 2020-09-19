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
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SeaPickleBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
/*  28 */   public static final IntegerProperty PICKLES = BlockStateProperties.PICKLES;
/*  29 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  31 */   protected static final VoxelShape ONE_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
/*  32 */   protected static final VoxelShape TWO_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
/*  33 */   protected static final VoxelShape THREE_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
/*  34 */   protected static final VoxelShape FOUR_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);
/*     */   
/*     */   protected SeaPickleBlock(BlockBehaviour.Properties debug1) {
/*  37 */     super(debug1);
/*  38 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PICKLES, Integer.valueOf(1))).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  44 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos());
/*  45 */     if (debug2.is(this)) {
/*  46 */       return (BlockState)debug2.setValue((Property)PICKLES, Integer.valueOf(Math.min(4, ((Integer)debug2.getValue((Property)PICKLES)).intValue() + 1)));
/*     */     }
/*     */     
/*  49 */     FluidState debug3 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*  50 */     boolean debug4 = (debug3.getType() == Fluids.WATER);
/*  51 */     return (BlockState)super.getStateForPlacement(debug1).setValue((Property)WATERLOGGED, Boolean.valueOf(debug4));
/*     */   }
/*     */   
/*     */   public static boolean isDead(BlockState debug0) {
/*  55 */     return !((Boolean)debug0.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  60 */     return (!debug1.getCollisionShape(debug2, debug3).getFaceShape(Direction.UP).isEmpty() || debug1.isFaceSturdy(debug2, debug3, Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  65 */     BlockPos debug4 = debug3.below();
/*  66 */     return mayPlaceOn(debug2.getBlockState(debug4), (BlockGetter)debug2, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  71 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  72 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  75 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  76 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/*  79 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/*  84 */     if (debug2.getItemInHand().getItem() == asItem() && ((Integer)debug1.getValue((Property)PICKLES)).intValue() < 4) {
/*  85 */       return true;
/*     */     }
/*  87 */     return super.canBeReplaced(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  92 */     switch (((Integer)debug1.getValue((Property)PICKLES)).intValue())
/*     */     
/*     */     { default:
/*  95 */         return ONE_AABB;
/*     */       case 2:
/*  97 */         return TWO_AABB;
/*     */       case 3:
/*  99 */         return THREE_AABB;
/*     */       case 4:
/* 101 */         break; }  return FOUR_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 107 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 108 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/* 111 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 116 */     debug1.add(new Property[] { (Property)PICKLES, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 131 */     if (!isDead(debug4) && debug1.getBlockState(debug3.below()).is((Tag)BlockTags.CORAL_BLOCKS)) {
/* 132 */       int debug5 = 5;
/* 133 */       int debug6 = 1;
/* 134 */       int debug7 = 2;
/* 135 */       int debug8 = 0;
/*     */       
/* 137 */       int debug9 = debug3.getX() - 2;
/* 138 */       int debug10 = 0;
/*     */       
/* 140 */       for (int debug11 = 0; debug11 < 5; debug11++) {
/* 141 */         for (int debug12 = 0; debug12 < debug6; debug12++) {
/* 142 */           int debug13 = 2 + debug3.getY() - 1;
/* 143 */           for (int debug14 = debug13 - 2; debug14 < debug13; debug14++) {
/* 144 */             BlockPos debug15 = new BlockPos(debug9 + debug11, debug14, debug3.getZ() - debug10 + debug12);
/* 145 */             if (debug15 != debug3)
/*     */             {
/*     */ 
/*     */               
/* 149 */               if (debug2.nextInt(6) == 0 && debug1.getBlockState(debug15).is(Blocks.WATER)) {
/* 150 */                 BlockState debug16 = debug1.getBlockState(debug15.below());
/* 151 */                 if (debug16.is((Tag)BlockTags.CORAL_BLOCKS)) {
/* 152 */                   debug1.setBlock(debug15, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)PICKLES, Integer.valueOf(debug2.nextInt(4) + 1)), 3);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/* 158 */         if (debug8 < 2) {
/* 159 */           debug6 += 2;
/* 160 */           debug10++;
/*     */         } else {
/* 162 */           debug6 -= 2;
/* 163 */           debug10--;
/*     */         } 
/* 165 */         debug8++;
/*     */       } 
/*     */       
/* 168 */       debug1.setBlock(debug3, (BlockState)debug4.setValue((Property)PICKLES, Integer.valueOf(4)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 174 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SeaPickleBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */