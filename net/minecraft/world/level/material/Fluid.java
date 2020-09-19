/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.IdMapper;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Fluid
/*    */ {
/* 21 */   public static final IdMapper<FluidState> FLUID_STATE_REGISTRY = new IdMapper();
/*    */   
/*    */   protected final StateDefinition<Fluid, FluidState> stateDefinition;
/*    */   private FluidState defaultFluidState;
/*    */   
/*    */   protected Fluid() {
/* 27 */     StateDefinition.Builder<Fluid, FluidState> debug1 = new StateDefinition.Builder(this);
/* 28 */     createFluidStateDefinition(debug1);
/* 29 */     this.stateDefinition = debug1.create(Fluid::defaultFluidState, FluidState::new);
/* 30 */     registerDefaultState((FluidState)this.stateDefinition.any());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> debug1) {}
/*    */   
/*    */   public StateDefinition<Fluid, FluidState> getStateDefinition() {
/* 37 */     return this.stateDefinition;
/*    */   }
/*    */   
/*    */   protected final void registerDefaultState(FluidState debug1) {
/* 41 */     this.defaultFluidState = debug1;
/*    */   }
/*    */   
/*    */   public final FluidState defaultFluidState() {
/* 45 */     return this.defaultFluidState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Item getBucket();
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(Level debug1, BlockPos debug2, FluidState debug3) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(Level debug1, BlockPos debug2, FluidState debug3, Random debug4) {}
/*    */ 
/*    */   
/*    */   protected abstract boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection);
/*    */ 
/*    */   
/*    */   protected abstract Vec3 getFlow(BlockGetter paramBlockGetter, BlockPos paramBlockPos, FluidState paramFluidState);
/*    */ 
/*    */   
/*    */   public abstract int getTickDelay(LevelReader paramLevelReader);
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking() {
/* 71 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean isEmpty() {
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract float getExplosionResistance();
/*    */   
/*    */   public abstract float getHeight(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*    */   
/*    */   public abstract float getOwnHeight(FluidState paramFluidState);
/*    */   
/*    */   protected abstract BlockState createLegacyBlock(FluidState paramFluidState);
/*    */   
/*    */   public abstract boolean isSource(FluidState paramFluidState);
/*    */   
/*    */   public abstract int getAmount(FluidState paramFluidState);
/*    */   
/*    */   public boolean isSame(Fluid debug1) {
/* 91 */     return (debug1 == this);
/*    */   }
/*    */   
/*    */   public boolean is(Tag<Fluid> debug1) {
/* 95 */     return debug1.contains(this);
/*    */   }
/*    */   
/*    */   public abstract VoxelShape getShape(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\Fluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */