/*    */ package net.minecraft.world.level.material;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class Fluids {
/*  6 */   public static final Fluid EMPTY = register("empty", new EmptyFluid());
/*  7 */   public static final FlowingFluid FLOWING_WATER = register("flowing_water", new WaterFluid.Flowing());
/*  8 */   public static final FlowingFluid WATER = register("water", new WaterFluid.Source());
/*  9 */   public static final FlowingFluid FLOWING_LAVA = register("flowing_lava", new LavaFluid.Flowing());
/* 10 */   public static final FlowingFluid LAVA = register("lava", new LavaFluid.Source());
/*    */   
/*    */   private static <T extends Fluid> T register(String debug0, T debug1) {
/* 13 */     return (T)Registry.register((Registry)Registry.FLUID, debug0, debug1);
/*    */   }
/*    */   
/*    */   static {
/* 17 */     for (Fluid debug1 : Registry.FLUID) {
/* 18 */       for (UnmodifiableIterator<FluidState> unmodifiableIterator = debug1.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { FluidState debug3 = unmodifiableIterator.next();
/* 19 */         Fluid.FLUID_STATE_REGISTRY.add(debug3); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\Fluids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */