/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class SimpleRandomFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<SimpleRandomFeatureConfiguration> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = ConfiguredFeature.LIST_CODEC.fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, debug0 -> debug0.features).codec();
/*    */   }
/*    */   public final List<Supplier<ConfiguredFeature<?, ?>>> features;
/*    */   
/*    */   public SimpleRandomFeatureConfiguration(List<Supplier<ConfiguredFeature<?, ?>>> debug1) {
/* 16 */     this.features = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 21 */     return this.features.stream().flatMap(debug0 -> ((ConfiguredFeature)debug0.get()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SimpleRandomFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */