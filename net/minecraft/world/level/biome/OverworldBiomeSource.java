/*     */ package net.minecraft.world.level.biome;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.RegistryLookupCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.newbiome.layer.Layers;
/*     */ 
/*     */ public class OverworldBiomeSource extends BiomeSource {
/*     */   static {
/*  16 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(()), (App)Codec.BOOL.optionalFieldOf("legacy_biome_init_layer", Boolean.valueOf(false), Lifecycle.stable()).forGetter(()), (App)Codec.BOOL.fieldOf("large_biomes").orElse(Boolean.valueOf(false)).stable().forGetter(()), (App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(())).apply((Applicative)debug0, debug0.stable(OverworldBiomeSource::new)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<OverworldBiomeSource> CODEC;
/*     */   
/*     */   private final Layer noiseBiomeLayer;
/*     */   
/*  25 */   private static final List<ResourceKey<Biome>> POSSIBLE_BIOMES = (List<ResourceKey<Biome>>)ImmutableList.of(Biomes.OCEAN, Biomes.PLAINS, Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS, (Object[])new ResourceKey[] { Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS, Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE, Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES, Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS, Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long seed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean legacyBiomeInitLayer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean largeBiomes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Registry<Biome> biomes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OverworldBiomeSource(long debug1, boolean debug3, boolean debug4, Registry<Biome> debug5) {
/* 100 */     super(POSSIBLE_BIOMES.stream().map(debug1 -> ()));
/* 101 */     this.seed = debug1;
/* 102 */     this.legacyBiomeInitLayer = debug3;
/* 103 */     this.largeBiomes = debug4;
/* 104 */     this.biomes = debug5;
/* 105 */     this.noiseBiomeLayer = Layers.getDefaultLayer(debug1, debug3, debug4 ? 6 : 4, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends BiomeSource> codec() {
/* 110 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/* 120 */     return this.noiseBiomeLayer.get(this.biomes, debug1, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\OverworldBiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */