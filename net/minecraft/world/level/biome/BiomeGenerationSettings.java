/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.data.worldgen.SurfaceBuilders;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class BiomeGenerationSettings {
/*  33 */   public static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  35 */   public static final BiomeGenerationSettings EMPTY = new BiomeGenerationSettings(() -> SurfaceBuilders.NOPE, 
/*     */       
/*  37 */       (Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>>)ImmutableMap.of(), 
/*  38 */       (List<List<Supplier<ConfiguredFeature<?, ?>>>>)ImmutableList.of(), 
/*  39 */       (List<Supplier<ConfiguredStructureFeature<?, ?>>>)ImmutableList.of()); public static final MapCodec<BiomeGenerationSettings> CODEC;
/*     */   
/*     */   static {
/*  42 */     CODEC = RecordCodecBuilder.mapCodec(debug0 -> debug0.group((App)ConfiguredSurfaceBuilder.CODEC.fieldOf("surface_builder").forGetter(()), (App)Codec.simpleMap(GenerationStep.Carving.CODEC, ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", LOGGER::error)), StringRepresentable.keys((StringRepresentable[])GenerationStep.Carving.values())).fieldOf("carvers").forGetter(()), (App)ConfiguredFeature.LIST_CODEC.promotePartial(Util.prefix("Feature: ", LOGGER::error)).listOf().fieldOf("features").forGetter(()), (App)ConfiguredStructureFeature.LIST_CODEC.promotePartial(Util.prefix("Structure start: ", LOGGER::error)).fieldOf("starts").forGetter(())).apply((Applicative)debug0, BiomeGenerationSettings::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder;
/*     */ 
/*     */   
/*     */   private final Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers;
/*     */ 
/*     */   
/*     */   private final List<List<Supplier<ConfiguredFeature<?, ?>>>> features;
/*     */   
/*     */   private final List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;
/*     */   
/*     */   private final List<ConfiguredFeature<?, ?>> flowerFeatures;
/*     */ 
/*     */   
/*     */   private BiomeGenerationSettings(Supplier<ConfiguredSurfaceBuilder<?>> debug1, Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> debug2, List<List<Supplier<ConfiguredFeature<?, ?>>>> debug3, List<Supplier<ConfiguredStructureFeature<?, ?>>> debug4) {
/*  61 */     this.surfaceBuilder = debug1;
/*  62 */     this.carvers = debug2;
/*  63 */     this.features = debug3;
/*  64 */     this.structureStarts = debug4;
/*     */     
/*  66 */     this.flowerFeatures = (List<ConfiguredFeature<?, ?>>)debug3.stream().flatMap(Collection::stream).map(Supplier::get).flatMap(ConfiguredFeature::getFeatures).filter(debug0 -> (debug0.feature == Feature.FLOWER)).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */   
/*     */   public List<Supplier<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving debug1) {
/*  70 */     return (List<Supplier<ConfiguredWorldCarver<?>>>)this.carvers.getOrDefault(debug1, ImmutableList.of());
/*     */   }
/*     */   
/*     */   public boolean isValidStart(StructureFeature<?> debug1) {
/*  74 */     return this.structureStarts.stream().anyMatch(debug1 -> (((ConfiguredStructureFeature)debug1.get()).feature == debug0));
/*     */   }
/*     */   
/*     */   public Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures() {
/*  78 */     return this.structureStarts;
/*     */   }
/*     */   
/*     */   public ConfiguredStructureFeature<?, ?> withBiomeConfig(ConfiguredStructureFeature<?, ?> debug1) {
/*  82 */     return (ConfiguredStructureFeature<?, ?>)DataFixUtils.orElse(this.structureStarts.stream().map(Supplier::get).filter(debug1 -> (debug1.feature == debug0.feature)).findAny(), debug1);
/*     */   }
/*     */   
/*     */   public List<ConfiguredFeature<?, ?>> getFlowerFeatures() {
/*  86 */     return this.flowerFeatures;
/*     */   }
/*     */   
/*     */   public List<List<Supplier<ConfiguredFeature<?, ?>>>> features() {
/*  90 */     return this.features;
/*     */   }
/*     */   
/*     */   public Supplier<ConfiguredSurfaceBuilder<?>> getSurfaceBuilder() {
/*  94 */     return this.surfaceBuilder;
/*     */   }
/*     */   
/*     */   public SurfaceBuilderConfiguration getSurfaceBuilderConfig() {
/*  98 */     return ((ConfiguredSurfaceBuilder)this.surfaceBuilder.get()).config();
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 102 */     private Optional<Supplier<ConfiguredSurfaceBuilder<?>>> surfaceBuilder = Optional.empty();
/* 103 */     private final Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers = Maps.newLinkedHashMap();
/* 104 */     private final List<List<Supplier<ConfiguredFeature<?, ?>>>> features = Lists.newArrayList();
/* 105 */     private final List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts = Lists.newArrayList();
/*     */     
/*     */     public Builder surfaceBuilder(ConfiguredSurfaceBuilder<?> debug1) {
/* 108 */       return surfaceBuilder(() -> debug0);
/*     */     }
/*     */     
/*     */     public Builder surfaceBuilder(Supplier<ConfiguredSurfaceBuilder<?>> debug1) {
/* 112 */       this.surfaceBuilder = Optional.of(debug1);
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addFeature(GenerationStep.Decoration debug1, ConfiguredFeature<?, ?> debug2) {
/* 117 */       return addFeature(debug1.ordinal(), () -> debug0);
/*     */     }
/*     */     
/*     */     public Builder addFeature(int debug1, Supplier<ConfiguredFeature<?, ?>> debug2) {
/* 121 */       addFeatureStepsUpTo(debug1);
/* 122 */       ((List<Supplier<ConfiguredFeature<?, ?>>>)this.features.get(debug1)).add(debug2);
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public <C extends net.minecraft.world.level.levelgen.carver.CarverConfiguration> Builder addCarver(GenerationStep.Carving debug1, ConfiguredWorldCarver<C> debug2) {
/* 127 */       ((List<Supplier>)this.carvers.computeIfAbsent(debug1, debug0 -> Lists.newArrayList())).add(() -> debug0);
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addStructureStart(ConfiguredStructureFeature<?, ?> debug1) {
/* 132 */       this.structureStarts.add(() -> debug0);
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     private void addFeatureStepsUpTo(int debug1) {
/* 137 */       while (this.features.size() <= debug1) {
/* 138 */         this.features.add(Lists.newArrayList());
/*     */       }
/*     */     }
/*     */     
/*     */     public BiomeGenerationSettings build() {
/* 143 */       return new BiomeGenerationSettings(this.surfaceBuilder
/* 144 */           .<Throwable>orElseThrow(() -> new IllegalStateException("Missing surface builder")), (Map)this.carvers
/* 145 */           .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, debug0 -> ImmutableList.copyOf((Collection)debug0.getValue()))), (List)this.features
/* 146 */           .stream().map(ImmutableList::copyOf).collect(ImmutableList.toImmutableList()), 
/* 147 */           (List)ImmutableList.copyOf(this.structureStarts));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */