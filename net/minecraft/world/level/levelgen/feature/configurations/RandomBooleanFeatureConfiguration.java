/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ConfiguredFeature.CODEC.fieldOf("feature_true").forGetter(()), (App)ConfiguredFeature.CODEC.fieldOf("feature_false").forGetter(())).apply((Applicative)debug0, RandomBooleanFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomBooleanFeatureConfiguration> CODEC;
/*    */   public final Supplier<ConfiguredFeature<?, ?>> featureTrue;
/*    */   public final Supplier<ConfiguredFeature<?, ?>> featureFalse;
/*    */   
/*    */   public RandomBooleanFeatureConfiguration(Supplier<ConfiguredFeature<?, ?>> debug1, Supplier<ConfiguredFeature<?, ?>> debug2) {
/* 20 */     this.featureTrue = debug1;
/* 21 */     this.featureFalse = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 26 */     return Stream.concat(((ConfiguredFeature)this.featureTrue.get()).getFeatures(), ((ConfiguredFeature)this.featureFalse.get()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomBooleanFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */