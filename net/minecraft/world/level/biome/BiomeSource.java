/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ 
/*     */ public abstract class BiomeSource
/*     */   implements BiomeManager.NoiseBiomeSource {
/*     */   static {
/*  24 */     Registry.register(Registry.BIOME_SOURCE, "fixed", FixedBiomeSource.CODEC);
/*  25 */     Registry.register(Registry.BIOME_SOURCE, "multi_noise", MultiNoiseBiomeSource.CODEC);
/*  26 */     Registry.register(Registry.BIOME_SOURCE, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
/*  27 */     Registry.register(Registry.BIOME_SOURCE, "vanilla_layered", OverworldBiomeSource.CODEC);
/*  28 */     Registry.register(Registry.BIOME_SOURCE, "the_end", TheEndBiomeSource.CODEC);
/*     */   }
/*     */   
/*  31 */   public static final Codec<BiomeSource> CODEC = Registry.BIOME_SOURCE.dispatchStable(BiomeSource::codec, Function.identity());
/*     */   
/*  33 */   protected final Map<StructureFeature<?>, Boolean> supportedStructures = Maps.newHashMap();
/*  34 */   protected final Set<BlockState> surfaceBlocks = Sets.newHashSet();
/*     */   protected final List<Biome> possibleBiomes;
/*     */   
/*     */   protected BiomeSource(Stream<Supplier<Biome>> debug1) {
/*  38 */     this((List<Biome>)debug1.map(Supplier::get).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   protected BiomeSource(List<Biome> debug1) {
/*  42 */     this.possibleBiomes = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Biome> possibleBiomes() {
/*  50 */     return this.possibleBiomes;
/*     */   }
/*     */   
/*     */   public Set<Biome> getBiomesWithin(int debug1, int debug2, int debug3, int debug4) {
/*  54 */     int debug5 = debug1 - debug4 >> 2;
/*  55 */     int debug6 = debug2 - debug4 >> 2;
/*  56 */     int debug7 = debug3 - debug4 >> 2;
/*  57 */     int debug8 = debug1 + debug4 >> 2;
/*  58 */     int debug9 = debug2 + debug4 >> 2;
/*  59 */     int debug10 = debug3 + debug4 >> 2;
/*     */     
/*  61 */     int debug11 = debug8 - debug5 + 1;
/*  62 */     int debug12 = debug9 - debug6 + 1;
/*  63 */     int debug13 = debug10 - debug7 + 1;
/*     */     
/*  65 */     Set<Biome> debug14 = Sets.newHashSet();
/*     */     
/*  67 */     for (int debug15 = 0; debug15 < debug13; debug15++) {
/*  68 */       for (int debug16 = 0; debug16 < debug11; debug16++) {
/*  69 */         for (int debug17 = 0; debug17 < debug12; debug17++) {
/*  70 */           int debug18 = debug5 + debug16;
/*  71 */           int debug19 = debug6 + debug17;
/*  72 */           int debug20 = debug7 + debug15;
/*  73 */           debug14.add(getNoiseBiome(debug18, debug19, debug20));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     return debug14;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos findBiomeHorizontal(int debug1, int debug2, int debug3, int debug4, Predicate<Biome> debug5, Random debug6) {
/*  83 */     return findBiomeHorizontal(debug1, debug2, debug3, debug4, 1, debug5, debug6, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockPos findBiomeHorizontal(int debug1, int debug2, int debug3, int debug4, int debug5, Predicate<Biome> debug6, Random debug7, boolean debug8) {
/*  95 */     int debug9 = debug1 >> 2;
/*  96 */     int debug10 = debug3 >> 2;
/*  97 */     int debug11 = debug4 >> 2;
/*     */     
/*  99 */     int debug12 = debug2 >> 2;
/*     */     
/* 101 */     BlockPos debug13 = null;
/* 102 */     int debug14 = 0;
/*     */     
/* 104 */     int debug15 = debug8 ? 0 : debug11; int debug16;
/* 105 */     for (debug16 = debug15; debug16 <= debug11; debug16 += debug5) {
/* 106 */       int debug17; for (debug17 = -debug16; debug17 <= debug16; debug17 += debug5) {
/* 107 */         boolean debug18 = (Math.abs(debug17) == debug16); int debug19;
/* 108 */         for (debug19 = -debug16; debug19 <= debug16; debug19 += debug5) {
/* 109 */           if (debug8) {
/*     */             
/* 111 */             boolean bool = (Math.abs(debug19) == debug16);
/* 112 */             if (!bool && !debug18) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */           
/* 117 */           int debug20 = debug9 + debug19;
/* 118 */           int debug21 = debug10 + debug17;
/* 119 */           if (debug6.test(getNoiseBiome(debug20, debug12, debug21))) {
/* 120 */             if (debug13 == null || debug7.nextInt(debug14 + 1) == 0) {
/* 121 */               debug13 = new BlockPos(debug20 << 2, debug2, debug21 << 2);
/* 122 */               if (debug8) {
/* 123 */                 return debug13;
/*     */               }
/*     */             } 
/* 126 */             debug14++;
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/* 132 */     return debug13;
/*     */   }
/*     */   
/*     */   public boolean canGenerateStructure(StructureFeature<?> debug1) {
/* 136 */     return ((Boolean)this.supportedStructures.computeIfAbsent(debug1, debug1 -> Boolean.valueOf(this.possibleBiomes.stream().anyMatch(())))).booleanValue();
/*     */   }
/*     */   
/*     */   public Set<BlockState> getSurfaceBlocks() {
/* 140 */     if (this.surfaceBlocks.isEmpty()) {
/* 141 */       for (Biome debug2 : this.possibleBiomes) {
/* 142 */         this.surfaceBlocks.add(debug2.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial());
/*     */       }
/*     */     }
/* 145 */     return this.surfaceBlocks;
/*     */   }
/*     */   
/*     */   protected abstract Codec<? extends BiomeSource> codec();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\BiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */