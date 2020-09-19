/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class StemBlock
/*     */   extends BushBlock
/*     */   implements BonemealableBlock
/*     */ {
/*  24 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
/*     */ 
/*     */   
/*  27 */   protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
/*  28 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D), 
/*  29 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 4.0D, 9.0D), 
/*  30 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D), 
/*  31 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 8.0D, 9.0D), 
/*  32 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D), 
/*  33 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 12.0D, 9.0D), 
/*  34 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D), 
/*  35 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D)
/*     */     };
/*     */   
/*     */   private final StemGrownBlock fruit;
/*     */   
/*     */   protected StemBlock(StemGrownBlock debug1, BlockBehaviour.Properties debug2) {
/*  41 */     super(debug2);
/*  42 */     this.fruit = debug1;
/*  43 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  48 */     return SHAPE_BY_AGE[((Integer)debug1.getValue((Property)AGE)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  53 */     return debug1.is(Blocks.FARMLAND);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  58 */     if (debug2.getRawBrightness(debug3, 0) < 9) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     float debug5 = CropBlock.getGrowthSpeed(this, (BlockGetter)debug2, debug3);
/*  63 */     if (debug4.nextInt((int)(25.0F / debug5) + 1) == 0) {
/*  64 */       int debug6 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  65 */       if (debug6 < 7) {
/*  66 */         debug1 = (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug6 + 1));
/*  67 */         debug2.setBlock(debug3, debug1, 2);
/*     */       } else {
/*  69 */         Direction debug7 = Direction.Plane.HORIZONTAL.getRandomDirection(debug4);
/*  70 */         BlockPos debug8 = debug3.relative(debug7);
/*     */         
/*  72 */         BlockState debug9 = debug2.getBlockState(debug8.below());
/*  73 */         if (debug2.getBlockState(debug8).isAir() && (debug9.is(Blocks.FARMLAND) || debug9.is(Blocks.DIRT) || debug9.is(Blocks.COARSE_DIRT) || debug9.is(Blocks.PODZOL) || debug9.is(Blocks.GRASS_BLOCK))) {
/*  74 */           debug2.setBlockAndUpdate(debug8, this.fruit.defaultBlockState());
/*  75 */           debug2.setBlockAndUpdate(debug3, (BlockState)this.fruit.getAttachedStem().defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)debug7));
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 103 */     return (((Integer)debug3.getValue((Property)AGE)).intValue() != 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 113 */     int debug5 = Math.min(7, ((Integer)debug4.getValue((Property)AGE)).intValue() + Mth.nextInt(debug1.random, 2, 5));
/* 114 */     BlockState debug6 = (BlockState)debug4.setValue((Property)AGE, Integer.valueOf(debug5));
/* 115 */     debug1.setBlock(debug3, debug6, 2);
/* 116 */     if (debug5 == 7) {
/* 117 */       debug6.randomTick(debug1, debug3, debug1.random);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 123 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public StemGrownBlock getFruit() {
/* 127 */     return this.fruit;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StemBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */