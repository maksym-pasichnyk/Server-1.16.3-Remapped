/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class FoliagePlacerType<P extends FoliagePlacer> {
/*  7 */   public static final FoliagePlacerType<BlobFoliagePlacer> BLOB_FOLIAGE_PLACER = register("blob_foliage_placer", BlobFoliagePlacer.CODEC);
/*  8 */   public static final FoliagePlacerType<SpruceFoliagePlacer> SPRUCE_FOLIAGE_PLACER = register("spruce_foliage_placer", SpruceFoliagePlacer.CODEC);
/*  9 */   public static final FoliagePlacerType<PineFoliagePlacer> PINE_FOLIAGE_PLACER = register("pine_foliage_placer", PineFoliagePlacer.CODEC);
/* 10 */   public static final FoliagePlacerType<AcaciaFoliagePlacer> ACACIA_FOLIAGE_PLACER = register("acacia_foliage_placer", AcaciaFoliagePlacer.CODEC);
/* 11 */   public static final FoliagePlacerType<BushFoliagePlacer> BUSH_FOLIAGE_PLACER = register("bush_foliage_placer", BushFoliagePlacer.CODEC);
/* 12 */   public static final FoliagePlacerType<FancyFoliagePlacer> FANCY_FOLIAGE_PLACER = register("fancy_foliage_placer", FancyFoliagePlacer.CODEC);
/* 13 */   public static final FoliagePlacerType<MegaJungleFoliagePlacer> MEGA_JUNGLE_FOLIAGE_PLACER = register("jungle_foliage_placer", MegaJungleFoliagePlacer.CODEC);
/* 14 */   public static final FoliagePlacerType<MegaPineFoliagePlacer> MEGA_PINE_FOLIAGE_PLACER = register("mega_pine_foliage_placer", MegaPineFoliagePlacer.CODEC);
/* 15 */   public static final FoliagePlacerType<DarkOakFoliagePlacer> DARK_OAK_FOLIAGE_PLACER = register("dark_oak_foliage_placer", DarkOakFoliagePlacer.CODEC);
/*    */   
/*    */   private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String debug0, Codec<P> debug1) {
/* 18 */     return (FoliagePlacerType<P>)Registry.register(Registry.FOLIAGE_PLACER_TYPES, debug0, new FoliagePlacerType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private FoliagePlacerType(Codec<P> debug1) {
/* 24 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 28 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */