/*     */ package net.minecraft.world.level.dimension;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.biome.TheEndBiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ 
/*     */ public final class LevelStem {
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)DimensionType.CODEC.fieldOf("type").forGetter(LevelStem::typeSupplier), (App)ChunkGenerator.CODEC.fieldOf("generator").forGetter(LevelStem::generator)).apply((Applicative)debug0, debug0.stable(LevelStem::new)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<LevelStem> CODEC;
/*  30 */   public static final ResourceKey<LevelStem> OVERWORLD = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("overworld"));
/*  31 */   public static final ResourceKey<LevelStem> NETHER = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("the_nether"));
/*  32 */   public static final ResourceKey<LevelStem> END = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("the_end"));
/*     */   
/*  34 */   private static final LinkedHashSet<ResourceKey<LevelStem>> BUILTIN_ORDER = Sets.newLinkedHashSet((Iterable)ImmutableList.of(OVERWORLD, NETHER, END));
/*     */ 
/*     */   
/*     */   private final Supplier<DimensionType> type;
/*     */ 
/*     */   
/*     */   private final ChunkGenerator generator;
/*     */ 
/*     */   
/*     */   public LevelStem(Supplier<DimensionType> debug1, ChunkGenerator debug2) {
/*  44 */     this.type = debug1;
/*  45 */     this.generator = debug2;
/*     */   }
/*     */   
/*     */   public Supplier<DimensionType> typeSupplier() {
/*  49 */     return this.type;
/*     */   }
/*     */   
/*     */   public DimensionType type() {
/*  53 */     return this.type.get();
/*     */   }
/*     */   
/*     */   public ChunkGenerator generator() {
/*  57 */     return this.generator;
/*     */   }
/*     */   
/*     */   public static MappedRegistry<LevelStem> sortMap(MappedRegistry<LevelStem> debug0) {
/*  61 */     MappedRegistry<LevelStem> debug1 = new MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
/*  62 */     for (ResourceKey<LevelStem> debug3 : BUILTIN_ORDER) {
/*  63 */       LevelStem debug4 = (LevelStem)debug0.get(debug3);
/*  64 */       if (debug4 != null) {
/*  65 */         debug1.register(debug3, debug4, debug0.lifecycle(debug4));
/*     */       }
/*     */     } 
/*  68 */     for (Map.Entry<ResourceKey<LevelStem>, LevelStem> debug3 : (Iterable<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)debug0.entrySet()) {
/*  69 */       ResourceKey<LevelStem> debug4 = debug3.getKey();
/*  70 */       if (BUILTIN_ORDER.contains(debug4)) {
/*     */         continue;
/*     */       }
/*  73 */       debug1.register(debug4, debug3.getValue(), debug0.lifecycle(debug3.getValue()));
/*     */     } 
/*  75 */     return debug1;
/*     */   }
/*     */   
/*     */   public static boolean stable(long debug0, MappedRegistry<LevelStem> debug2) {
/*  79 */     List<Map.Entry<ResourceKey<LevelStem>, LevelStem>> debug3 = Lists.newArrayList(debug2.entrySet());
/*     */     
/*  81 */     if (debug3.size() != BUILTIN_ORDER.size()) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     Map.Entry<ResourceKey<LevelStem>, LevelStem> debug4 = debug3.get(0);
/*  86 */     Map.Entry<ResourceKey<LevelStem>, LevelStem> debug5 = debug3.get(1);
/*  87 */     Map.Entry<ResourceKey<LevelStem>, LevelStem> debug6 = debug3.get(2);
/*     */     
/*  89 */     if (debug4.getKey() != OVERWORLD || debug5.getKey() != NETHER || debug6.getKey() != END) {
/*  90 */       return false;
/*     */     }
/*     */     
/*  93 */     if (!((LevelStem)debug4.getValue()).type().equalTo(DimensionType.DEFAULT_OVERWORLD) && ((LevelStem)debug4.getValue()).type() != DimensionType.DEFAULT_OVERWORLD_CAVES) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     if (!((LevelStem)debug5.getValue()).type().equalTo(DimensionType.DEFAULT_NETHER)) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     if (!((LevelStem)debug6.getValue()).type().equalTo(DimensionType.DEFAULT_END)) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     if (!(((LevelStem)debug5.getValue()).generator() instanceof NoiseBasedChunkGenerator) || !(((LevelStem)debug6.getValue()).generator() instanceof NoiseBasedChunkGenerator)) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     NoiseBasedChunkGenerator debug7 = (NoiseBasedChunkGenerator)((LevelStem)debug5.getValue()).generator();
/* 110 */     NoiseBasedChunkGenerator debug8 = (NoiseBasedChunkGenerator)((LevelStem)debug6.getValue()).generator();
/*     */     
/* 112 */     if (!debug7.stable(debug0, NoiseGeneratorSettings.NETHER)) {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     if (!debug8.stable(debug0, NoiseGeneratorSettings.END)) {
/* 117 */       return false;
/*     */     }
/*     */     
/* 120 */     if (!(debug7.getBiomeSource() instanceof MultiNoiseBiomeSource)) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     MultiNoiseBiomeSource debug9 = (MultiNoiseBiomeSource)debug7.getBiomeSource();
/* 125 */     if (!debug9.stable(debug0)) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     if (!(debug8.getBiomeSource() instanceof TheEndBiomeSource)) {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     TheEndBiomeSource debug10 = (TheEndBiomeSource)debug8.getBiomeSource();
/* 134 */     if (!debug10.stable(debug0)) {
/* 135 */       return false;
/*     */     }
/*     */     
/* 138 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\dimension\LevelStem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */