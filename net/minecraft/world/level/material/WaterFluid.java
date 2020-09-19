/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WaterFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public Fluid getFlowing() {
/*  29 */     return Fluids.FLOWING_WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  34 */     return Fluids.WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  39 */     return Items.WATER_BUCKET;
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
/*     */   protected boolean canConvertToSource() {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/*  66 */     BlockEntity debug4 = debug3.getBlock().isEntityBlock() ? debug1.getBlockEntity(debug2) : null;
/*  67 */     Block.dropResources(debug3, debug1, debug2, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader debug1) {
/*  72 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState debug1) {
/*  77 */     return (BlockState)Blocks.WATER.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(debug1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid debug1) {
/*  82 */     return (debug1 == Fluids.WATER || debug1 == Fluids.FLOWING_WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader debug1) {
/*  87 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader debug1) {
/*  92 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState debug1, BlockGetter debug2, BlockPos debug3, Fluid debug4, Direction debug5) {
/*  97 */     return (debug5 == Direction.DOWN && !debug4.is((Tag<Fluid>)FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 102 */     return 100.0F;
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends WaterFluid {
/*     */     public int getAmount(FluidState debug1) {
/* 108 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState debug1) {
/* 113 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends WaterFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> debug1) {
/* 120 */       super.createFluidStateDefinition(debug1);
/* 121 */       debug1.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState debug1) {
/* 126 */       return ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState debug1) {
/* 131 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\WaterFluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */