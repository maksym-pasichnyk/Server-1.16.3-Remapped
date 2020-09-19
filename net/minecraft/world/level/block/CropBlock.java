/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
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
/*     */ public class CropBlock
/*     */   extends BushBlock
/*     */   implements BonemealableBlock
/*     */ {
/*  26 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
/*     */   
/*  28 */   private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
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
/*     */   protected CropBlock(BlockBehaviour.Properties debug1) {
/*  40 */     super(debug1);
/*  41 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)getAgeProperty(), Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  46 */     return SHAPE_BY_AGE[((Integer)debug1.getValue((Property)getAgeProperty())).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  51 */     return debug1.is(Blocks.FARMLAND);
/*     */   }
/*     */   
/*     */   public IntegerProperty getAgeProperty() {
/*  55 */     return AGE;
/*     */   }
/*     */   
/*     */   public int getMaxAge() {
/*  59 */     return 7;
/*     */   }
/*     */   
/*     */   protected int getAge(BlockState debug1) {
/*  63 */     return ((Integer)debug1.getValue((Property)getAgeProperty())).intValue();
/*     */   }
/*     */   
/*     */   public BlockState getStateForAge(int debug1) {
/*  67 */     return (BlockState)defaultBlockState().setValue((Property)getAgeProperty(), Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isMaxAge(BlockState debug1) {
/*  71 */     return (((Integer)debug1.getValue((Property)getAgeProperty())).intValue() >= getMaxAge());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  76 */     return !isMaxAge(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  81 */     if (debug2.getRawBrightness(debug3, 0) >= 9) {
/*  82 */       int debug5 = getAge(debug1);
/*  83 */       if (debug5 < getMaxAge()) {
/*  84 */         float debug6 = getGrowthSpeed(this, (BlockGetter)debug2, debug3);
/*     */         
/*  86 */         if (debug4.nextInt((int)(25.0F / debug6) + 1) == 0) {
/*  87 */           debug2.setBlock(debug3, getStateForAge(debug5 + 1), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void growCrops(Level debug1, BlockPos debug2, BlockState debug3) {
/*  94 */     int debug4 = getAge(debug3) + getBonemealAgeIncrease(debug1);
/*  95 */     int debug5 = getMaxAge();
/*  96 */     if (debug4 > debug5) {
/*  97 */       debug4 = debug5;
/*     */     }
/*  99 */     debug1.setBlock(debug2, getStateForAge(debug4), 2);
/*     */   }
/*     */   
/*     */   protected int getBonemealAgeIncrease(Level debug1) {
/* 103 */     return Mth.nextInt(debug1.random, 2, 5);
/*     */   }
/*     */   
/*     */   protected static float getGrowthSpeed(Block debug0, BlockGetter debug1, BlockPos debug2) {
/* 107 */     float debug3 = 1.0F;
/*     */     
/* 109 */     BlockPos debug4 = debug2.below();
/* 110 */     for (int i = -1; i <= 1; i++) {
/* 111 */       for (int j = -1; j <= 1; j++) {
/* 112 */         float f = 0.0F;
/*     */         
/* 114 */         BlockState blockState = debug1.getBlockState(debug4.offset(i, 0, j));
/* 115 */         if (blockState.is(Blocks.FARMLAND)) {
/* 116 */           f = 1.0F;
/* 117 */           if (((Integer)blockState.getValue((Property)FarmBlock.MOISTURE)).intValue() > 0) {
/* 118 */             f = 3.0F;
/*     */           }
/*     */         } 
/*     */         
/* 122 */         if (i != 0 || j != 0) {
/* 123 */           f /= 4.0F;
/*     */         }
/*     */         
/* 126 */         debug3 += f;
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     BlockPos debug5 = debug2.north();
/* 131 */     BlockPos debug6 = debug2.south();
/* 132 */     BlockPos debug7 = debug2.west();
/* 133 */     BlockPos debug8 = debug2.east();
/*     */     
/* 135 */     boolean debug9 = (debug0 == debug1.getBlockState(debug7).getBlock() || debug0 == debug1.getBlockState(debug8).getBlock());
/* 136 */     boolean debug10 = (debug0 == debug1.getBlockState(debug5).getBlock() || debug0 == debug1.getBlockState(debug6).getBlock());
/*     */     
/* 138 */     if (debug9 && debug10) {
/* 139 */       debug3 /= 2.0F;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 145 */       boolean debug11 = (debug0 == debug1.getBlockState(debug7.north()).getBlock() || debug0 == debug1.getBlockState(debug8.north()).getBlock() || debug0 == debug1.getBlockState(debug8.south()).getBlock() || debug0 == debug1.getBlockState(debug7.south()).getBlock());
/*     */       
/* 147 */       if (debug11) {
/* 148 */         debug3 /= 2.0F;
/*     */       }
/*     */     } 
/*     */     
/* 152 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 157 */     return ((debug2.getRawBrightness(debug3, 0) >= 8 || debug2.canSeeSky(debug3)) && super.canSurvive(debug1, debug2, debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 162 */     if (debug4 instanceof net.minecraft.world.entity.monster.Ravager && debug2.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 163 */       debug2.destroyBlock(debug3, true, debug4);
/*     */     }
/* 165 */     super.entityInside(debug1, debug2, debug3, debug4);
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
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 180 */     return !isMaxAge(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 190 */     growCrops((Level)debug1, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 195 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CropBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */