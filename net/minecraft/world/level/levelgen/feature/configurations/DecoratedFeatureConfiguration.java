/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
/*    */ 
/*    */ public class DecoratedFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(()), (App)ConfiguredDecorator.CODEC.fieldOf("decorator").forGetter(())).apply((Applicative)debug0, DecoratedFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DecoratedFeatureConfiguration> CODEC;
/*    */   public final Supplier<ConfiguredFeature<?, ?>> feature;
/*    */   public final ConfiguredDecorator<?> decorator;
/*    */   
/*    */   public DecoratedFeatureConfiguration(Supplier<ConfiguredFeature<?, ?>> debug1, ConfiguredDecorator<?> debug2) {
/* 22 */     this.feature = debug1;
/* 23 */     this.decorator = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return String.format("< %s [%s | %s] >", new Object[] { getClass().getSimpleName(), Registry.FEATURE.getKey(((ConfiguredFeature)this.feature.get()).feature()), this.decorator });
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 33 */     return ((ConfiguredFeature)this.feature.get()).getFeatures();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\DecoratedFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */