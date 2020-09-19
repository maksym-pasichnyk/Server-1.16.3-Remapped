/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.WeightedConfiguredFeature;
/*    */ 
/*    */ public class RandomFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.apply2(RandomFeatureConfiguration::new, (App)WeightedConfiguredFeature.CODEC.listOf().fieldOf("features").forGetter(()), (App)ConfiguredFeature.CODEC.fieldOf("default").forGetter(())));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final List<WeightedConfiguredFeature> features;
/*    */   public final Supplier<ConfiguredFeature<?, ?>> defaultFeature;
/*    */   
/*    */   public RandomFeatureConfiguration(List<WeightedConfiguredFeature> debug1, ConfiguredFeature<?, ?> debug2) {
/* 23 */     this(debug1, () -> debug0);
/*    */   }
/*    */   
/*    */   private RandomFeatureConfiguration(List<WeightedConfiguredFeature> debug1, Supplier<ConfiguredFeature<?, ?>> debug2) {
/* 27 */     this.features = debug1;
/* 28 */     this.defaultFeature = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 33 */     return Stream.concat(this.features.stream().flatMap(debug0 -> ((ConfiguredFeature)debug0.feature.get()).getFeatures()), ((ConfiguredFeature)this.defaultFeature.get()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */