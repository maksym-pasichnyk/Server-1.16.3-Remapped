/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
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
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class ChorusFlowerBlock extends Block {
/*  22 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
/*     */   
/*     */   private final ChorusPlantBlock plant;
/*     */   
/*     */   protected ChorusFlowerBlock(ChorusPlantBlock debug1, BlockBehaviour.Properties debug2) {
/*  27 */     super(debug2);
/*  28 */     this.plant = debug1;
/*  29 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  34 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/*  35 */       debug2.destroyBlock(debug3, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  41 */     return (((Integer)debug1.getValue((Property)AGE)).intValue() < 5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  47 */     BlockPos debug5 = debug3.above();
/*  48 */     if (!debug2.isEmptyBlock(debug5) || debug5.getY() >= 256) {
/*     */       return;
/*     */     }
/*     */     
/*  52 */     int debug6 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  53 */     if (debug6 >= 5) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     boolean debug7 = false;
/*  58 */     boolean debug8 = false;
/*     */     
/*  60 */     BlockState debug9 = debug2.getBlockState(debug3.below());
/*  61 */     Block debug10 = debug9.getBlock();
/*  62 */     if (debug10 == Blocks.END_STONE) {
/*  63 */       debug7 = true;
/*  64 */     } else if (debug10 == this.plant) {
/*  65 */       int debug11 = 1;
/*  66 */       for (int debug12 = 0; debug12 < 4; debug12++) {
/*  67 */         Block debug13 = debug2.getBlockState(debug3.below(debug11 + 1)).getBlock();
/*  68 */         if (debug13 == this.plant) {
/*  69 */           debug11++;
/*     */         } else {
/*  71 */           if (debug13 == Blocks.END_STONE) {
/*  72 */             debug8 = true;
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*  77 */       if (debug11 < 2 || debug11 <= debug4.nextInt(debug8 ? 5 : 4)) {
/*  78 */         debug7 = true;
/*     */       }
/*  80 */     } else if (debug9.isAir()) {
/*  81 */       debug7 = true;
/*     */     } 
/*     */     
/*  84 */     if (debug7 && allNeighborsEmpty((LevelReader)debug2, debug5, (Direction)null) && debug2.isEmptyBlock(debug3.above(2))) {
/*  85 */       debug2.setBlock(debug3, this.plant.getStateForPlacement((BlockGetter)debug2, debug3), 2);
/*  86 */       placeGrownFlower((Level)debug2, debug5, debug6);
/*  87 */     } else if (debug6 < 4) {
/*  88 */       int debug11 = debug4.nextInt(4);
/*  89 */       if (debug8) {
/*  90 */         debug11++;
/*     */       }
/*     */       
/*  93 */       boolean debug12 = false;
/*  94 */       for (int debug13 = 0; debug13 < debug11; debug13++) {
/*  95 */         Direction debug14 = Direction.Plane.HORIZONTAL.getRandomDirection(debug4);
/*  96 */         BlockPos debug15 = debug3.relative(debug14);
/*  97 */         if (debug2.isEmptyBlock(debug15) && debug2.isEmptyBlock(debug15.below()) && allNeighborsEmpty((LevelReader)debug2, debug15, debug14.getOpposite())) {
/*  98 */           placeGrownFlower((Level)debug2, debug15, debug6 + 1);
/*  99 */           debug12 = true;
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       if (debug12) {
/* 104 */         debug2.setBlock(debug3, this.plant.getStateForPlacement((BlockGetter)debug2, debug3), 2);
/*     */       } else {
/* 106 */         placeDeadFlower((Level)debug2, debug3);
/*     */       } 
/*     */     } else {
/* 109 */       placeDeadFlower((Level)debug2, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeGrownFlower(Level debug1, BlockPos debug2, int debug3) {
/* 114 */     debug1.setBlock(debug2, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(debug3)), 2);
/* 115 */     debug1.levelEvent(1033, debug2, 0);
/*     */   }
/*     */   
/*     */   private void placeDeadFlower(Level debug1, BlockPos debug2) {
/* 119 */     debug1.setBlock(debug2, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2);
/* 120 */     debug1.levelEvent(1034, debug2, 0);
/*     */   }
/*     */   
/*     */   private static boolean allNeighborsEmpty(LevelReader debug0, BlockPos debug1, @Nullable Direction debug2) {
/* 124 */     for (Direction debug4 : Direction.Plane.HORIZONTAL) {
/* 125 */       if (debug4 != debug2 && !debug0.isEmptyBlock(debug1.relative(debug4))) {
/* 126 */         return false;
/*     */       }
/*     */     } 
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 134 */     if (debug2 != Direction.UP && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 135 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*     */     
/* 138 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 143 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/* 144 */     if (debug4.getBlock() == this.plant || debug4.is(Blocks.END_STONE)) {
/* 145 */       return true;
/*     */     }
/* 147 */     if (!debug4.isAir()) {
/* 148 */       return false;
/*     */     }
/*     */     
/* 151 */     boolean debug5 = false;
/* 152 */     for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/* 153 */       BlockState debug8 = debug2.getBlockState(debug3.relative(debug7));
/* 154 */       if (debug8.is(this.plant)) {
/* 155 */         if (debug5) {
/* 156 */           return false;
/*     */         }
/* 158 */         debug5 = true; continue;
/* 159 */       }  if (!debug8.isAir()) {
/* 160 */         return false;
/*     */       }
/*     */     } 
/* 163 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 168 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public static void generatePlant(LevelAccessor debug0, BlockPos debug1, Random debug2, int debug3) {
/* 172 */     debug0.setBlock(debug1, ((ChorusPlantBlock)Blocks.CHORUS_PLANT).getStateForPlacement((BlockGetter)debug0, debug1), 2);
/* 173 */     growTreeRecursive(debug0, debug1, debug2, debug1, debug3, 0);
/*     */   }
/*     */   
/*     */   private static void growTreeRecursive(LevelAccessor debug0, BlockPos debug1, Random debug2, BlockPos debug3, int debug4, int debug5) {
/* 177 */     ChorusPlantBlock debug6 = (ChorusPlantBlock)Blocks.CHORUS_PLANT;
/*     */     
/* 179 */     int debug7 = debug2.nextInt(4) + 1;
/* 180 */     if (debug5 == 0) {
/* 181 */       debug7++;
/*     */     }
/*     */     
/* 184 */     for (int i = 0; i < debug7; i++) {
/* 185 */       BlockPos debug9 = debug1.above(i + 1);
/* 186 */       if (!allNeighborsEmpty((LevelReader)debug0, debug9, (Direction)null)) {
/*     */         return;
/*     */       }
/*     */       
/* 190 */       debug0.setBlock(debug9, debug6.getStateForPlacement((BlockGetter)debug0, debug9), 2);
/* 191 */       debug0.setBlock(debug9.below(), debug6.getStateForPlacement((BlockGetter)debug0, debug9.below()), 2);
/*     */     } 
/*     */     
/* 194 */     boolean debug8 = false;
/* 195 */     if (debug5 < 4) {
/* 196 */       int debug9 = debug2.nextInt(4);
/* 197 */       if (debug5 == 0) {
/* 198 */         debug9++;
/*     */       }
/* 200 */       for (int debug10 = 0; debug10 < debug9; debug10++) {
/* 201 */         Direction debug11 = Direction.Plane.HORIZONTAL.getRandomDirection(debug2);
/* 202 */         BlockPos debug12 = debug1.above(debug7).relative(debug11);
/* 203 */         if (Math.abs(debug12.getX() - debug3.getX()) < debug4 && Math.abs(debug12.getZ() - debug3.getZ()) < debug4)
/*     */         {
/*     */           
/* 206 */           if (debug0.isEmptyBlock(debug12) && debug0.isEmptyBlock(debug12.below()) && allNeighborsEmpty((LevelReader)debug0, debug12, debug11.getOpposite())) {
/* 207 */             debug8 = true;
/* 208 */             debug0.setBlock(debug12, debug6.getStateForPlacement((BlockGetter)debug0, debug12), 2);
/* 209 */             debug0.setBlock(debug12.relative(debug11.getOpposite()), debug6.getStateForPlacement((BlockGetter)debug0, debug12.relative(debug11.getOpposite())), 2);
/* 210 */             growTreeRecursive(debug0, debug12, debug2, debug3, debug4, debug5 + 1);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     if (!debug8) {
/* 216 */       debug0.setBlock(debug1.above(debug7), (BlockState)Blocks.CHORUS_FLOWER.defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/* 222 */     if (debug4.getType().is((Tag)EntityTypeTags.IMPACT_PROJECTILES)) {
/* 223 */       BlockPos debug5 = debug3.getBlockPos();
/* 224 */       debug1.destroyBlock(debug5, true, (Entity)debug4);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ChorusFlowerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */