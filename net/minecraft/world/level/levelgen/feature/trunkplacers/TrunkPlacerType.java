/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class TrunkPlacerType<P extends TrunkPlacer> {
/*  7 */   public static final TrunkPlacerType<StraightTrunkPlacer> STRAIGHT_TRUNK_PLACER = register("straight_trunk_placer", StraightTrunkPlacer.CODEC);
/*  8 */   public static final TrunkPlacerType<ForkingTrunkPlacer> FORKING_TRUNK_PLACER = register("forking_trunk_placer", ForkingTrunkPlacer.CODEC);
/*  9 */   public static final TrunkPlacerType<GiantTrunkPlacer> GIANT_TRUNK_PLACER = register("giant_trunk_placer", GiantTrunkPlacer.CODEC);
/* 10 */   public static final TrunkPlacerType<MegaJungleTrunkPlacer> MEGA_JUNGLE_TRUNK_PLACER = register("mega_jungle_trunk_placer", MegaJungleTrunkPlacer.CODEC);
/* 11 */   public static final TrunkPlacerType<DarkOakTrunkPlacer> DARK_OAK_TRUNK_PLACER = register("dark_oak_trunk_placer", DarkOakTrunkPlacer.CODEC);
/* 12 */   public static final TrunkPlacerType<FancyTrunkPlacer> FANCY_TRUNK_PLACER = register("fancy_trunk_placer", FancyTrunkPlacer.CODEC);
/*    */   
/*    */   private static <P extends TrunkPlacer> TrunkPlacerType<P> register(String debug0, Codec<P> debug1) {
/* 15 */     return (TrunkPlacerType<P>)Registry.register(Registry.TRUNK_PLACER_TYPES, debug0, new TrunkPlacerType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private TrunkPlacerType(Codec<P> debug1) {
/* 21 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 25 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */