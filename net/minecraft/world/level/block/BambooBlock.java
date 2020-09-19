/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BambooBlock
/*     */   extends Block
/*     */   implements BonemealableBlock
/*     */ {
/*  34 */   protected static final VoxelShape SMALL_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
/*  35 */   protected static final VoxelShape LARGE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
/*  36 */   protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D);
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
/*  39 */   public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
/*  40 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BambooBlock(BlockBehaviour.Properties debug1) {
/*  49 */     super(debug1);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)STAGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  55 */     debug1.add(new Property[] { (Property)AGE, (Property)LEAVES, (Property)STAGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockBehaviour.OffsetType getOffsetType() {
/*  60 */     return BlockBehaviour.OffsetType.XZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  70 */     VoxelShape debug5 = (debug1.getValue((Property)LEAVES) == BambooLeaves.LARGE) ? LARGE_SHAPE : SMALL_SHAPE;
/*  71 */     Vec3 debug6 = debug1.getOffset(debug2, debug3);
/*  72 */     return debug5.move(debug6.x, debug6.y, debug6.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  82 */     Vec3 debug5 = debug1.getOffset(debug2, debug3);
/*  83 */     return COLLISION_SHAPE.move(debug5.x, debug5.y, debug5.z);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  89 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*  90 */     if (!debug2.isEmpty()) {
/*  91 */       return null;
/*     */     }
/*     */     
/*  94 */     BlockState debug3 = debug1.getLevel().getBlockState(debug1.getClickedPos().below());
/*  95 */     if (debug3.is((Tag)BlockTags.BAMBOO_PLANTABLE_ON)) {
/*  96 */       if (debug3.is(Blocks.BAMBOO_SAPLING))
/*  97 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(0)); 
/*  98 */       if (debug3.is(Blocks.BAMBOO)) {
/*  99 */         int i = (((Integer)debug3.getValue((Property)AGE)).intValue() > 0) ? 1 : 0;
/* 100 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(i));
/*     */       } 
/* 102 */       BlockState debug4 = debug1.getLevel().getBlockState(debug1.getClickedPos().above());
/* 103 */       if (debug4.is(Blocks.BAMBOO) || debug4.is(Blocks.BAMBOO_SAPLING)) {
/* 104 */         return (BlockState)defaultBlockState().setValue((Property)AGE, debug4.getValue((Property)AGE));
/*     */       }
/* 106 */       return Blocks.BAMBOO_SAPLING.defaultBlockState();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 116 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 117 */       debug2.destroyBlock(debug3, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/* 123 */     return (((Integer)debug1.getValue((Property)STAGE)).intValue() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 128 */     if (((Integer)debug1.getValue((Property)STAGE)).intValue() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (debug4.nextInt(3) == 0 && debug2.isEmptyBlock(debug3.above()) && debug2.getRawBrightness(debug3.above(), 0) >= 9) {
/* 133 */       int debug5 = getHeightBelowUpToMax((BlockGetter)debug2, debug3) + 1;
/* 134 */       if (debug5 < 16) {
/* 135 */         growBamboo(debug1, (Level)debug2, debug3, debug4, debug5);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 142 */     return debug2.getBlockState(debug3.below()).is((Tag)BlockTags.BAMBOO_PLANTABLE_ON);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 147 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 148 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*     */     
/* 151 */     if (debug2 == Direction.UP && 
/* 152 */       debug3.is(Blocks.BAMBOO) && ((Integer)debug3.getValue((Property)AGE)).intValue() > ((Integer)debug1.getValue((Property)AGE)).intValue()) {
/* 153 */       debug4.setBlock(debug5, (BlockState)debug1.cycle((Property)AGE), 2);
/*     */     }
/*     */ 
/*     */     
/* 157 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 162 */     int debug5 = getHeightAboveUpToMax(debug1, debug2);
/* 163 */     int debug6 = getHeightBelowUpToMax(debug1, debug2);
/* 164 */     return (debug5 + debug6 + 1 < 16 && ((Integer)debug1.getBlockState(debug2.above(debug5)).getValue((Property)STAGE)).intValue() != 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 169 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 174 */     int debug5 = getHeightAboveUpToMax((BlockGetter)debug1, debug3);
/* 175 */     int debug6 = getHeightBelowUpToMax((BlockGetter)debug1, debug3);
/* 176 */     int debug7 = debug5 + debug6 + 1;
/*     */     
/* 178 */     int debug8 = 1 + debug2.nextInt(2);
/* 179 */     for (int debug9 = 0; debug9 < debug8; debug9++) {
/* 180 */       BlockPos debug10 = debug3.above(debug5);
/* 181 */       BlockState debug11 = debug1.getBlockState(debug10);
/* 182 */       if (debug7 >= 16 || ((Integer)debug11.getValue((Property)STAGE)).intValue() == 1 || !debug1.isEmptyBlock(debug10.above())) {
/*     */         return;
/*     */       }
/*     */       
/* 186 */       growBamboo(debug11, (Level)debug1, debug10, debug2, debug7);
/*     */       
/* 188 */       debug5++;
/* 189 */       debug7++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDestroyProgress(BlockState debug1, Player debug2, BlockGetter debug3, BlockPos debug4) {
/* 195 */     if (debug2.getMainHandItem().getItem() instanceof net.minecraft.world.item.SwordItem) {
/* 196 */       return 1.0F;
/*     */     }
/*     */     
/* 199 */     return super.getDestroyProgress(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   protected void growBamboo(BlockState debug1, Level debug2, BlockPos debug3, Random debug4, int debug5) {
/* 203 */     BlockState debug6 = debug2.getBlockState(debug3.below());
/* 204 */     BlockPos debug7 = debug3.below(2);
/* 205 */     BlockState debug8 = debug2.getBlockState(debug7);
/*     */     
/* 207 */     BambooLeaves debug9 = BambooLeaves.NONE;
/* 208 */     if (debug5 >= 1) {
/* 209 */       if (!debug6.is(Blocks.BAMBOO) || debug6.getValue((Property)LEAVES) == BambooLeaves.NONE) {
/* 210 */         debug9 = BambooLeaves.SMALL;
/* 211 */       } else if (debug6.is(Blocks.BAMBOO) && debug6.getValue((Property)LEAVES) != BambooLeaves.NONE) {
/* 212 */         debug9 = BambooLeaves.LARGE;
/*     */         
/* 214 */         if (debug8.is(Blocks.BAMBOO)) {
/* 215 */           debug2.setBlock(debug3.below(), (BlockState)debug6.setValue((Property)LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/* 216 */           debug2.setBlock(debug7, (BlockState)debug8.setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE), 3);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 221 */     int debug10 = (((Integer)debug1.getValue((Property)AGE)).intValue() == 1 || debug8.is(Blocks.BAMBOO)) ? 1 : 0;
/* 222 */     int debug11 = ((debug5 >= 11 && debug4.nextFloat() < 0.25F) || debug5 == 15) ? 1 : 0;
/* 223 */     debug2.setBlock(debug3.above(), (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(debug10))).setValue((Property)LEAVES, (Comparable)debug9)).setValue((Property)STAGE, Integer.valueOf(debug11)), 3);
/*     */   }
/*     */   
/*     */   protected int getHeightAboveUpToMax(BlockGetter debug1, BlockPos debug2) {
/* 227 */     int debug3 = 0;
/* 228 */     while (debug3 < 16 && debug1.getBlockState(debug2.above(debug3 + 1)).is(Blocks.BAMBOO)) {
/* 229 */       debug3++;
/*     */     }
/* 231 */     return debug3;
/*     */   }
/*     */   
/*     */   protected int getHeightBelowUpToMax(BlockGetter debug1, BlockPos debug2) {
/* 235 */     int debug3 = 0;
/* 236 */     while (debug3 < 16 && debug1.getBlockState(debug2.below(debug3 + 1)).is(Blocks.BAMBOO)) {
/* 237 */       debug3++;
/*     */     }
/* 239 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BambooBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */