/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.MappedRegistry;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.WritableRegistry;
/*    */ import net.minecraft.data.worldgen.Carvers;
/*    */ import net.minecraft.data.worldgen.Features;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.data.worldgen.ProcessorLists;
/*    */ import net.minecraft.data.worldgen.StructureFeatures;
/*    */ import net.minecraft.data.worldgen.SurfaceBuilders;
/*    */ import net.minecraft.data.worldgen.biome.Biomes;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuiltinRegistries
/*    */ {
/* 37 */   protected static final Logger LOGGER = LogManager.getLogger();
/*    */   
/* 39 */   private static final Map<ResourceLocation, Supplier<?>> LOADERS = Maps.newLinkedHashMap();
/*    */   
/* 41 */   private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = (WritableRegistry<WritableRegistry<?>>)new MappedRegistry(ResourceKey.createRegistryKey(new ResourceLocation("root")), Lifecycle.experimental());
/* 42 */   public static final Registry<? extends Registry<?>> REGISTRY = (Registry)WRITABLE_REGISTRY;
/*    */   
/* 44 */   public static final Registry<ConfiguredSurfaceBuilder<?>> CONFIGURED_SURFACE_BUILDER = registerSimple(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, () -> SurfaceBuilders.NOPE);
/* 45 */   public static final Registry<ConfiguredWorldCarver<?>> CONFIGURED_CARVER = registerSimple(Registry.CONFIGURED_CARVER_REGISTRY, () -> Carvers.CAVE);
/* 46 */   public static final Registry<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE = registerSimple(Registry.CONFIGURED_FEATURE_REGISTRY, () -> Features.OAK);
/* 47 */   public static final Registry<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURE = registerSimple(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, () -> StructureFeatures.MINESHAFT);
/*    */   
/* 49 */   public static final Registry<StructureProcessorList> PROCESSOR_LIST = registerSimple(Registry.PROCESSOR_LIST_REGISTRY, () -> ProcessorLists.ZOMBIE_PLAINS);
/* 50 */   public static final Registry<StructureTemplatePool> TEMPLATE_POOL = registerSimple(Registry.TEMPLATE_POOL_REGISTRY, Pools::bootstrap);
/*    */   
/* 52 */   public static final Registry<Biome> BIOME = registerSimple(Registry.BIOME_REGISTRY, () -> Biomes.PLAINS);
/*    */   
/* 54 */   public static final Registry<NoiseGeneratorSettings> NOISE_GENERATOR_SETTINGS = registerSimple(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, NoiseGeneratorSettings::bootstrap);
/*    */   
/*    */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> debug0, Supplier<T> debug1) {
/* 57 */     return registerSimple(debug0, Lifecycle.stable(), debug1);
/*    */   }
/*    */   
/*    */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> debug0, Lifecycle debug1, Supplier<T> debug2) {
/* 61 */     return (Registry<T>)internalRegister(debug0, new MappedRegistry(debug0, debug1), debug2, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> debug0, R debug1, Supplier<T> debug2, Lifecycle debug3) {
/* 66 */     ResourceLocation debug4 = debug0.location();
/* 67 */     LOADERS.put(debug4, debug2);
/*    */     
/* 69 */     WritableRegistry<WritableRegistry<?>> writableRegistry = WRITABLE_REGISTRY;
/* 70 */     return (R)writableRegistry.register(debug0, debug1, debug3);
/*    */   }
/*    */   
/*    */   static {
/* 74 */     LOADERS.forEach((debug0, debug1) -> {
/*    */           if (debug1.get() == null) {
/*    */             LOGGER.error("Unable to bootstrap registry '{}'", debug0);
/*    */           }
/*    */         });
/*    */     
/* 80 */     Registry.checkRegistry(WRITABLE_REGISTRY);
/*    */   }
/*    */   
/*    */   public static <T> T register(Registry<? super T> debug0, String debug1, T debug2) {
/* 84 */     return register(debug0, new ResourceLocation(debug1), debug2);
/*    */   }
/*    */   
/*    */   public static <V, T extends V> T register(Registry<V> debug0, ResourceLocation debug1, T debug2) {
/* 88 */     return (T)((WritableRegistry)debug0).register(ResourceKey.create(debug0.key(), debug1), debug2, Lifecycle.stable());
/*    */   }
/*    */   
/*    */   public static <V, T extends V> T registerMapping(Registry<V> debug0, int debug1, ResourceKey<V> debug2, T debug3) {
/* 92 */     return (T)((WritableRegistry)debug0).registerMapping(debug1, debug2, debug3, Lifecycle.stable());
/*    */   }
/*    */   
/*    */   public static void bootstrap() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\BuiltinRegistries.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */