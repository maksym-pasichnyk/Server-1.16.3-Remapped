/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class ConfiguredStructureFeature<FC extends FeatureConfiguration, F extends StructureFeature<FC>> {
/*    */   static {
/* 21 */     DIRECT_CODEC = Registry.STRUCTURE_FEATURE.dispatch(debug0 -> debug0.feature, StructureFeature::configuredStructureCodec);
/*    */   }
/* 23 */   public static final Codec<Supplier<ConfiguredStructureFeature<?, ?>>> CODEC = (Codec<Supplier<ConfiguredStructureFeature<?, ?>>>)RegistryFileCodec.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, DIRECT_CODEC); public static final Codec<ConfiguredStructureFeature<?, ?>> DIRECT_CODEC;
/* 24 */   public static final Codec<List<Supplier<ConfiguredStructureFeature<?, ?>>>> LIST_CODEC = RegistryFileCodec.homogeneousList(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, DIRECT_CODEC);
/*    */   
/*    */   public final F feature;
/*    */   public final FC config;
/*    */   
/*    */   public ConfiguredStructureFeature(F debug1, FC debug2) {
/* 30 */     this.feature = debug1;
/* 31 */     this.config = debug2;
/*    */   }
/*    */   
/*    */   public StructureStart<?> generate(RegistryAccess debug1, ChunkGenerator debug2, BiomeSource debug3, StructureManager debug4, long debug5, ChunkPos debug7, Biome debug8, int debug9, StructureFeatureConfiguration debug10) {
/* 35 */     return this.feature.generate(debug1, debug2, debug3, debug4, debug5, debug7, debug8, debug9, new WorldgenRandom(), debug10, this.config);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ConfiguredStructureFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */