/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LavaFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public Fluid getFlowing() {
/*  34 */     return Fluids.FLOWING_LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  39 */     return Fluids.LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  44 */     return Items.LAVA_BUCKET;
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
/*     */   public void randomTick(Level debug1, BlockPos debug2, FluidState debug3, Random debug4) {
/*  67 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     int debug5 = debug4.nextInt(3);
/*  72 */     if (debug5 > 0) {
/*  73 */       BlockPos debug6 = debug2;
/*     */       
/*  75 */       for (int debug7 = 0; debug7 < debug5; debug7++) {
/*  76 */         debug6 = debug6.offset(debug4.nextInt(3) - 1, 1, debug4.nextInt(3) - 1);
/*  77 */         if (!debug1.isLoaded(debug6)) {
/*     */           return;
/*     */         }
/*  80 */         BlockState debug8 = debug1.getBlockState(debug6);
/*  81 */         if (debug8.isAir()) {
/*  82 */           if (hasFlammableNeighbours((LevelReader)debug1, debug6)) {
/*  83 */             debug1.setBlockAndUpdate(debug6, BaseFireBlock.getState((BlockGetter)debug1, debug6));
/*     */             return;
/*     */           } 
/*  86 */         } else if (debug8.getMaterial().blocksMotion()) {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  91 */       for (int debug6 = 0; debug6 < 3; debug6++) {
/*  92 */         BlockPos debug7 = debug2.offset(debug4.nextInt(3) - 1, 0, debug4.nextInt(3) - 1);
/*  93 */         if (!debug1.isLoaded(debug7)) {
/*     */           return;
/*     */         }
/*  96 */         if (debug1.isEmptyBlock(debug7.above()) && isFlammable((LevelReader)debug1, debug7)) {
/*  97 */           debug1.setBlockAndUpdate(debug7.above(), BaseFireBlock.getState((BlockGetter)debug1, debug7));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasFlammableNeighbours(LevelReader debug1, BlockPos debug2) {
/* 104 */     for (Direction debug6 : Direction.values()) {
/* 105 */       if (isFlammable(debug1, debug2.relative(debug6))) {
/* 106 */         return true;
/*     */       }
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFlammable(LevelReader debug1, BlockPos debug2) {
/* 113 */     if (debug2.getY() >= 0 && debug2.getY() < 256 && !debug1.hasChunkAt(debug2)) {
/* 114 */       return false;
/*     */     }
/* 116 */     return debug1.getBlockState(debug2).getMaterial().isFlammable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/* 127 */     fizz(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader debug1) {
/* 132 */     return debug1.dimensionType().ultraWarm() ? 4 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState debug1) {
/* 137 */     return (BlockState)Blocks.LAVA.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(debug1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid debug1) {
/* 142 */     return (debug1 == Fluids.LAVA || debug1 == Fluids.FLOWING_LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader debug1) {
/* 147 */     return debug1.dimensionType().ultraWarm() ? 1 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState debug1, BlockGetter debug2, BlockPos debug3, Fluid debug4, Direction debug5) {
/* 152 */     return (debug1.getHeight(debug2, debug3) >= 0.44444445F && debug4.is((Tag<Fluid>)FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader debug1) {
/* 157 */     return debug1.dimensionType().ultraWarm() ? 10 : 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpreadDelay(Level debug1, BlockPos debug2, FluidState debug3, FluidState debug4) {
/* 162 */     int debug5 = getTickDelay((LevelReader)debug1);
/*     */     
/* 164 */     if (!debug3.isEmpty() && !debug4.isEmpty() && !((Boolean)debug3.getValue((Property)FALLING)).booleanValue() && !((Boolean)debug4.getValue((Property)FALLING)).booleanValue() && debug4.getHeight((BlockGetter)debug1, debug2) > debug3.getHeight((BlockGetter)debug1, debug2) && debug1.getRandom().nextInt(4) != 0) {
/* 165 */       debug5 *= 4;
/*     */     }
/* 167 */     return debug5;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor debug1, BlockPos debug2) {
/* 171 */     debug1.levelEvent(1501, debug2, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canConvertToSource() {
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Direction debug4, FluidState debug5) {
/* 181 */     if (debug4 == Direction.DOWN) {
/* 182 */       FluidState debug6 = debug1.getFluidState(debug2);
/* 183 */       if (is((Tag<Fluid>)FluidTags.LAVA) && debug6.is((Tag<Fluid>)FluidTags.WATER)) {
/* 184 */         if (debug3.getBlock() instanceof LiquidBlock) {
/* 185 */           debug1.setBlock(debug2, Blocks.STONE.defaultBlockState(), 3);
/*     */         }
/* 187 */         fizz(debug1, debug2);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 192 */     super.spreadTo(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 202 */     return 100.0F;
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends LavaFluid {
/*     */     public int getAmount(FluidState debug1) {
/* 208 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState debug1) {
/* 213 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends LavaFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> debug1) {
/* 220 */       super.createFluidStateDefinition(debug1);
/* 221 */       debug1.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState debug1) {
/* 226 */       return ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState debug1) {
/* 231 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\LavaFluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */