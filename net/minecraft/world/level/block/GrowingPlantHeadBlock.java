/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {
/*  19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_25;
/*     */   
/*     */   private final double growPerTickProbability;
/*     */ 
/*     */   
/*     */   protected GrowingPlantHeadBlock(BlockBehaviour.Properties debug1, Direction debug2, VoxelShape debug3, boolean debug4, double debug5) {
/*  25 */     super(debug1, debug2, debug3, debug4);
/*  26 */     this.growPerTickProbability = debug5;
/*  27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(LevelAccessor debug1) {
/*  32 */     return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(debug1.getRandom().nextInt(25)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  37 */     return (((Integer)debug1.getValue((Property)AGE)).intValue() < 25);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  42 */     if (((Integer)debug1.getValue((Property)AGE)).intValue() < 25 && debug4.nextDouble() < this.growPerTickProbability) {
/*  43 */       BlockPos debug5 = debug3.relative(this.growthDirection);
/*  44 */       if (canGrowInto(debug2.getBlockState(debug5))) {
/*  45 */         debug2.setBlockAndUpdate(debug5, (BlockState)debug1.cycle((Property)AGE));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  52 */     if (debug2 == this.growthDirection.getOpposite() && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  53 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*  55 */     if (debug2 == this.growthDirection && (debug3.is(this) || debug3.is(getBodyBlock()))) {
/*  56 */       return getBodyBlock().defaultBlockState();
/*     */     }
/*  58 */     if (this.scheduleFluidTicks) {
/*  59 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/*  62 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  67 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/*  72 */     return canGrowInto(debug1.getBlockState(debug2.relative(this.growthDirection)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/*  82 */     BlockPos debug5 = debug3.relative(this.growthDirection);
/*  83 */     int debug6 = Math.min(((Integer)debug4.getValue((Property)AGE)).intValue() + 1, 25);
/*     */     
/*  85 */     int debug7 = getBlocksToGrowWhenBonemealed(debug2);
/*  86 */     for (int debug8 = 0; debug8 < debug7 && 
/*  87 */       canGrowInto(debug1.getBlockState(debug5)); debug8++) {
/*     */ 
/*     */       
/*  90 */       debug1.setBlockAndUpdate(debug5, (BlockState)debug4.setValue((Property)AGE, Integer.valueOf(debug6)));
/*     */       
/*  92 */       debug5 = debug5.relative(this.growthDirection);
/*  93 */       debug6 = Math.min(debug6 + 1, 25);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int getBlocksToGrowWhenBonemealed(Random paramRandom);
/*     */   
/*     */   protected abstract boolean canGrowInto(BlockState paramBlockState);
/*     */   
/*     */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 103 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantHeadBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */