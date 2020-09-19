/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Decoratable;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ConfiguredFeature<FC extends FeatureConfiguration, F extends Feature<FC>> implements Decoratable<ConfiguredFeature<?, ?>> {
/*    */   static {
/* 22 */     DIRECT_CODEC = Registry.FEATURE.dispatch(debug0 -> debug0.feature, Feature::configuredCodec);
/*    */   }
/* 24 */   public static final Codec<Supplier<ConfiguredFeature<?, ?>>> CODEC = (Codec<Supplier<ConfiguredFeature<?, ?>>>)RegistryFileCodec.create(Registry.CONFIGURED_FEATURE_REGISTRY, DIRECT_CODEC); public static final Codec<ConfiguredFeature<?, ?>> DIRECT_CODEC;
/* 25 */   public static final Codec<List<Supplier<ConfiguredFeature<?, ?>>>> LIST_CODEC = RegistryFileCodec.homogeneousList(Registry.CONFIGURED_FEATURE_REGISTRY, DIRECT_CODEC);
/*    */   
/* 27 */   public static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public final F feature;
/*    */   public final FC config;
/*    */   
/*    */   public ConfiguredFeature(F debug1, FC debug2) {
/* 33 */     this.feature = debug1;
/* 34 */     this.config = debug2;
/*    */   }
/*    */   
/*    */   public F feature() {
/* 38 */     return this.feature;
/*    */   }
/*    */   
/*    */   public FC config() {
/* 42 */     return this.config;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfiguredFeature<?, ?> decorated(ConfiguredDecorator<?> debug1) {
/* 47 */     return Feature.DECORATED.configured(new DecoratedFeatureConfiguration(() -> this, debug1));
/*    */   }
/*    */   
/*    */   public WeightedConfiguredFeature weighted(float debug1) {
/* 51 */     return new WeightedConfiguredFeature(this, debug1);
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4) {
/* 55 */     return this.feature.place(debug1, debug2, debug3, debug4, this.config);
/*    */   }
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 59 */     return Stream.concat(Stream.of(this), this.config.getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ConfiguredFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */