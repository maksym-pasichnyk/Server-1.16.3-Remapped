/*     */ package net.minecraft.core;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.UnboundedMapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.resources.RegistryReadOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class RegistryAccess {
/*  33 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private static final Map<ResourceKey<? extends Registry<?>>, RegistryData<?>> REGISTRIES;
/*     */   private static final RegistryHolder BUILTIN;
/*     */   
/*     */   public <E> WritableRegistry<E> registryOrThrow(ResourceKey<? extends Registry<E>> debug1) {
/*  38 */     return (WritableRegistry<E>)registry(debug1).orElseThrow(() -> new IllegalStateException("Missing registry: " + debug0));
/*     */   }
/*     */   
/*     */   public Registry<DimensionType> dimensionTypes() {
/*  42 */     return registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
/*     */   }
/*     */   
/*     */   static final class RegistryData<E> {
/*     */     private final ResourceKey<? extends Registry<E>> key;
/*     */     private final Codec<E> codec;
/*     */     @Nullable
/*     */     private final Codec<E> networkCodec;
/*     */     
/*     */     public RegistryData(ResourceKey<? extends Registry<E>> debug1, Codec<E> debug2, @Nullable Codec<E> debug3) {
/*  52 */       this.key = debug1;
/*  53 */       this.codec = debug2;
/*  54 */       this.networkCodec = debug3;
/*     */     }
/*     */     
/*     */     public ResourceKey<? extends Registry<E>> key() {
/*  58 */       return this.key;
/*     */     }
/*     */     
/*     */     public Codec<E> codec() {
/*  62 */       return this.codec;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Codec<E> networkCodec() {
/*  67 */       return this.networkCodec;
/*     */     }
/*     */     
/*     */     public boolean sendToClient() {
/*  71 */       return (this.networkCodec != null);
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  76 */     REGISTRIES = (Map<ResourceKey<? extends Registry<?>>, RegistryData<?>>)Util.make(() -> {
/*     */           ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> debug0 = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.DIMENSION_TYPE_REGISTRY, DimensionType.DIRECT_CODEC, DimensionType.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC, Biome.NETWORK_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, ConfiguredSurfaceBuilder.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.CONFIGURED_CARVER_REGISTRY, ConfiguredWorldCarver.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.CONFIGURED_FEATURE_REGISTRY, ConfiguredFeature.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ConfiguredStructureFeature.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.PROCESSOR_LIST_REGISTRY, StructureProcessorType.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.TEMPLATE_POOL_REGISTRY, StructureTemplatePool.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           put(debug0, Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, NoiseGeneratorSettings.DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return debug0.build();
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     BUILTIN = (RegistryHolder)Util.make(() -> {
/*     */           RegistryHolder debug0 = new RegistryHolder();
/*     */           DimensionType.registerBuiltin(debug0);
/*     */           REGISTRIES.keySet().stream().filter(()).forEach(());
/*     */           return debug0;
/*     */         });
/*     */   } private static <E> void put(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> debug0, ResourceKey<? extends Registry<E>> debug1, Codec<E> debug2) {
/*     */     debug0.put(debug1, new RegistryData<>(debug1, debug2, null));
/* 158 */   } public static RegistryHolder builtin() { RegistryHolder debug0 = new RegistryHolder();
/* 159 */     RegistryReadOps.ResourceAccess.MemoryMap debug1 = new RegistryReadOps.ResourceAccess.MemoryMap();
/*     */     
/* 161 */     for (RegistryData<?> debug3 : REGISTRIES.values()) {
/* 162 */       addBuiltinElements(debug0, debug1, debug3);
/*     */     }
/*     */     
/* 165 */     RegistryReadOps.create((DynamicOps)JsonOps.INSTANCE, (RegistryReadOps.ResourceAccess)debug1, debug0);
/* 166 */     return debug0; }
/*     */   private static <E> void put(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> debug0, ResourceKey<? extends Registry<E>> debug1, Codec<E> debug2, Codec<E> debug3) { debug0.put(debug1, new RegistryData<>(debug1, debug2, debug3)); }
/*     */   public static final class RegistryHolder extends RegistryAccess {
/*     */     public static final Codec<RegistryHolder> NETWORK_CODEC = makeNetworkCodec();
/* 170 */     private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> registries; private static <E> Codec<RegistryHolder> makeNetworkCodec() { Codec<ResourceKey<? extends Registry<E>>> debug0 = ResourceLocation.CODEC.xmap(ResourceKey::createRegistryKey, ResourceKey::location); Codec<MappedRegistry<E>> debug1 = debug0.partialDispatch("type", debug0 -> DataResult.success(debug0.key()), debug0 -> getNetworkCodec(debug0).map(())); UnboundedMapCodec<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> debug2 = Codec.unboundedMap(debug0, debug1); return captureMap(debug2); } private static <K extends ResourceKey<? extends Registry<?>>, V extends MappedRegistry<?>> Codec<RegistryHolder> captureMap(UnboundedMapCodec<K, V> debug0) { return debug0.xmap(RegistryHolder::new, debug0 -> (Map)debug0.registries.entrySet().stream().filter(()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue))); } private static <E> DataResult<? extends Codec<E>> getNetworkCodec(ResourceKey<? extends Registry<E>> debug0) { return Optional.ofNullable(RegistryAccess.REGISTRIES.get(debug0)).map(debug0 -> debug0.networkCodec()).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown or not serializable registry: " + debug0)); } public RegistryHolder() { this((Map<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>>)RegistryAccess.REGISTRIES.keySet().stream().collect(Collectors.toMap(Function.identity(), RegistryHolder::createRegistry))); } private RegistryHolder(Map<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> debug1) { this.registries = debug1; } private static <E> MappedRegistry<?> createRegistry(ResourceKey<? extends Registry<?>> debug0) { return new MappedRegistry(debug0, Lifecycle.stable()); } public <E> Optional<WritableRegistry<E>> registry(ResourceKey<? extends Registry<E>> debug1) { return Optional.ofNullable(this.registries.get(debug1)).map(debug0 -> debug0); } } private static <E> void addBuiltinElements(RegistryHolder debug0, RegistryReadOps.ResourceAccess.MemoryMap debug1, RegistryData<E> debug2) { ResourceKey<? extends Registry<E>> debug3 = debug2.key();
/*     */     
/* 172 */     boolean debug4 = (!debug3.equals(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY) && !debug3.equals(Registry.DIMENSION_TYPE_REGISTRY));
/* 173 */     Registry<E> debug5 = BUILTIN.registryOrThrow(debug3);
/* 174 */     WritableRegistry<E> debug6 = debug0.registryOrThrow(debug3);
/* 175 */     for (Map.Entry<ResourceKey<E>, E> debug8 : debug5.entrySet()) {
/* 176 */       E debug9 = debug8.getValue();
/* 177 */       if (debug4) {
/* 178 */         debug1.add(BUILTIN, debug8.getKey(), (Encoder)debug2.codec(), debug5.getId(debug9), debug9, debug5.lifecycle(debug9)); continue;
/*     */       } 
/* 180 */       debug6.registerMapping(debug5.getId(debug9), debug8.getKey(), debug9, debug5.lifecycle(debug9));
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R extends Registry<?>> void copyBuiltin(RegistryHolder debug0, ResourceKey<R> debug1) {
/* 187 */     Registry<R> debug2 = BuiltinRegistries.REGISTRY;
/* 188 */     Registry<?> debug3 = (Registry)debug2.get(debug1);
/* 189 */     if (debug3 == null) {
/* 190 */       throw new IllegalStateException("Missing builtin registry: " + debug1);
/*     */     }
/* 192 */     copy(debug0, debug3);
/*     */   }
/*     */   
/*     */   private static <E> void copy(RegistryHolder debug0, Registry<E> debug1) {
/* 196 */     WritableRegistry<E> debug2 = (WritableRegistry<E>)debug0.registry(debug1.key()).orElseThrow(() -> new IllegalStateException("Missing registry: " + debug0.key()));
/* 197 */     for (Map.Entry<ResourceKey<E>, E> debug4 : debug1.entrySet()) {
/* 198 */       E debug5 = debug4.getValue();
/* 199 */       debug2.registerMapping(debug1.getId(debug5), debug4.getKey(), debug5, debug1.lifecycle(debug5));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void load(RegistryHolder debug0, RegistryReadOps<?> debug1) {
/* 204 */     for (RegistryData<?> debug3 : REGISTRIES.values()) {
/* 205 */       readRegistry(debug1, debug0, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> void readRegistry(RegistryReadOps<?> debug0, RegistryHolder debug1, RegistryData<E> debug2) {
/* 211 */     ResourceKey<? extends Registry<E>> debug3 = debug2.key();
/* 212 */     MappedRegistry<E> debug4 = (MappedRegistry<E>)Optional.ofNullable(debug1.registries.get(debug3)).map(debug0 -> debug0).orElseThrow(() -> new IllegalStateException("Missing registry: " + debug0));
/* 213 */     DataResult<MappedRegistry<E>> debug5 = debug0.decodeElements(debug4, debug2.key(), debug2.codec());
/* 214 */     debug5.error().ifPresent(debug0 -> LOGGER.error("Error loading registry data: {}", debug0.message()));
/*     */   }
/*     */   
/*     */   public abstract <E> Optional<WritableRegistry<E>> registry(ResourceKey<? extends Registry<E>> paramResourceKey);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\RegistryAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */