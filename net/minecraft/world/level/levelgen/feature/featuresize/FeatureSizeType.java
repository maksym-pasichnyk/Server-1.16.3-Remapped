/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class FeatureSizeType<P extends FeatureSize> {
/*  7 */   public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
/*  8 */   public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
/*    */   
/*    */   private static <P extends FeatureSize> FeatureSizeType<P> register(String debug0, Codec<P> debug1) {
/* 11 */     return (FeatureSizeType<P>)Registry.register(Registry.FEATURE_SIZE_TYPES, debug0, new FeatureSizeType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private FeatureSizeType(Codec<P> debug1) {
/* 17 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 21 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSizeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */