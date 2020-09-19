/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LiquidBlock extends Block implements BucketPickup {
/*  33 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*     */   
/*     */   protected final FlowingFluid fluid;
/*     */   
/*     */   private final List<FluidState> stateCache;
/*  38 */   public static final VoxelShape STABLE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
/*     */   
/*     */   protected LiquidBlock(FlowingFluid debug1, BlockBehaviour.Properties debug2) {
/*  41 */     super(debug2);
/*  42 */     this.fluid = debug1;
/*  43 */     this.stateCache = Lists.newArrayList();
/*  44 */     this.stateCache.add(debug1.getSource(false));
/*  45 */     for (int debug3 = 1; debug3 < 8; debug3++) {
/*  46 */       this.stateCache.add(debug1.getFlowing(8 - debug3, false));
/*     */     }
/*  48 */     this.stateCache.add(debug1.getFlowing(8, true));
/*  49 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  55 */     if (debug4.isAbove(STABLE_SHAPE, debug3, true) && ((Integer)debug1.getValue((Property)LEVEL)).intValue() == 0 && debug4.canStandOnFluid(debug2.getFluidState(debug3.above()), this.fluid)) {
/*  56 */       return STABLE_SHAPE;
/*     */     }
/*  58 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  63 */     return debug1.getFluidState().isRandomlyTicking();
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  68 */     debug1.getFluidState().randomTick((Level)debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  78 */     return !this.fluid.is((Tag)FluidTags.LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/*  83 */     int debug2 = ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*  84 */     return this.stateCache.get(Math.min(debug2, 8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  94 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/*  99 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 104 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 109 */     if (shouldSpreadLiquid(debug2, debug3, debug1)) {
/* 110 */       debug2.getLiquidTicks().scheduleTick(debug3, debug1.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)debug2));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 116 */     if (debug1.getFluidState().isSource() || debug3.getFluidState().isSource()) {
/* 117 */       debug4.getLiquidTicks().scheduleTick(debug5, debug1.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/* 120 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 125 */     if (shouldSpreadLiquid(debug2, debug3, debug1)) {
/* 126 */       debug2.getLiquidTicks().scheduleTick(debug3, debug1.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)debug2));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldSpreadLiquid(Level debug1, BlockPos debug2, BlockState debug3) {
/* 131 */     if (this.fluid.is((Tag)FluidTags.LAVA)) {
/* 132 */       boolean debug4 = debug1.getBlockState(debug2.below()).is(Blocks.SOUL_SOIL);
/*     */       
/* 134 */       for (Direction debug8 : Direction.values()) {
/* 135 */         if (debug8 != Direction.DOWN) {
/*     */ 
/*     */           
/* 138 */           BlockPos debug9 = debug2.relative(debug8);
/*     */           
/* 140 */           if (debug1.getFluidState(debug9).is((Tag)FluidTags.WATER)) {
/* 141 */             Block debug10 = debug1.getFluidState(debug2).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
/* 142 */             debug1.setBlockAndUpdate(debug2, debug10.defaultBlockState());
/* 143 */             fizz((LevelAccessor)debug1, debug2);
/* 144 */             return false;
/*     */           } 
/*     */           
/* 147 */           if (debug4 && debug1.getBlockState(debug9).is(Blocks.BLUE_ICE)) {
/* 148 */             debug1.setBlockAndUpdate(debug2, Blocks.BASALT.defaultBlockState());
/* 149 */             fizz((LevelAccessor)debug1, debug2);
/* 150 */             return false;
/*     */           } 
/*     */         } 
/*     */       } 
/* 154 */     }  return true;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor debug1, BlockPos debug2) {
/* 158 */     debug1.levelEvent(1501, debug2, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 163 */     debug1.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid takeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/* 168 */     if (((Integer)debug3.getValue((Property)LEVEL)).intValue() == 0) {
/* 169 */       debug1.setBlock(debug2, Blocks.AIR.defaultBlockState(), 11);
/* 170 */       return (Fluid)this.fluid;
/*     */     } 
/* 172 */     return Fluids.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LiquidBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */