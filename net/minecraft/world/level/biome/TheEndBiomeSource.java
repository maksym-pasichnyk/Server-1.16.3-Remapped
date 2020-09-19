/*     */ package net.minecraft.world.level.biome;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*     */ 
/*     */ public class TheEndBiomeSource extends BiomeSource {
/*     */   static {
/*  13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(()), (App)Codec.LONG.fieldOf("seed").stable().forGetter(())).apply((Applicative)debug0, debug0.stable(TheEndBiomeSource::new)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<TheEndBiomeSource> CODEC;
/*     */   
/*     */   private final SimplexNoise islandNoise;
/*     */   
/*     */   private final Registry<Biome> biomes;
/*     */   
/*     */   private final long seed;
/*     */   
/*     */   private final Biome end;
/*     */   private final Biome highlands;
/*     */   private final Biome midlands;
/*     */   private final Biome islands;
/*     */   private final Biome barrens;
/*     */   
/*     */   public TheEndBiomeSource(Registry<Biome> debug1, long debug2) {
/*  32 */     this(debug1, debug2, (Biome)debug1
/*     */ 
/*     */         
/*  35 */         .getOrThrow(Biomes.THE_END), (Biome)debug1
/*  36 */         .getOrThrow(Biomes.END_HIGHLANDS), (Biome)debug1
/*  37 */         .getOrThrow(Biomes.END_MIDLANDS), (Biome)debug1
/*  38 */         .getOrThrow(Biomes.SMALL_END_ISLANDS), (Biome)debug1
/*  39 */         .getOrThrow(Biomes.END_BARRENS));
/*     */   }
/*     */ 
/*     */   
/*     */   private TheEndBiomeSource(Registry<Biome> debug1, long debug2, Biome debug4, Biome debug5, Biome debug6, Biome debug7, Biome debug8) {
/*  44 */     super((List<Biome>)ImmutableList.of(debug4, debug5, debug6, debug7, debug8));
/*  45 */     this.biomes = debug1;
/*  46 */     this.seed = debug2;
/*  47 */     this.end = debug4;
/*  48 */     this.highlands = debug5;
/*  49 */     this.midlands = debug6;
/*  50 */     this.islands = debug7;
/*  51 */     this.barrens = debug8;
/*  52 */     WorldgenRandom debug9 = new WorldgenRandom(debug2);
/*     */     
/*  54 */     debug9.consumeCount(17292);
/*  55 */     this.islandNoise = new SimplexNoise((Random)debug9);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends BiomeSource> codec() {
/*  60 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/*  70 */     int debug4 = debug1 >> 2;
/*  71 */     int debug5 = debug3 >> 2;
/*     */     
/*  73 */     if (debug4 * debug4 + debug5 * debug5 <= 4096L) {
/*  74 */       return this.end;
/*     */     }
/*     */     
/*  77 */     float debug6 = getHeightValue(this.islandNoise, debug4 * 2 + 1, debug5 * 2 + 1);
/*  78 */     if (debug6 > 40.0F) {
/*  79 */       return this.highlands;
/*     */     }
/*     */     
/*  82 */     if (debug6 >= 0.0F) {
/*  83 */       return this.midlands;
/*     */     }
/*     */     
/*  86 */     if (debug6 < -20.0F) {
/*  87 */       return this.islands;
/*     */     }
/*     */     
/*  90 */     return this.barrens;
/*     */   }
/*     */   
/*     */   public boolean stable(long debug1) {
/*  94 */     return (this.seed == debug1);
/*     */   }
/*     */   
/*     */   public static float getHeightValue(SimplexNoise debug0, int debug1, int debug2) {
/*  98 */     int debug3 = debug1 / 2;
/*  99 */     int debug4 = debug2 / 2;
/* 100 */     int debug5 = debug1 % 2;
/* 101 */     int debug6 = debug2 % 2;
/*     */ 
/*     */     
/* 104 */     float debug7 = 100.0F - Mth.sqrt((debug1 * debug1 + debug2 * debug2)) * 8.0F;
/* 105 */     debug7 = Mth.clamp(debug7, -100.0F, 80.0F);
/*     */ 
/*     */     
/* 108 */     for (int debug8 = -12; debug8 <= 12; debug8++) {
/* 109 */       for (int debug9 = -12; debug9 <= 12; debug9++) {
/* 110 */         long debug10 = (debug3 + debug8);
/* 111 */         long debug12 = (debug4 + debug9);
/* 112 */         if (debug10 * debug10 + debug12 * debug12 > 4096L && debug0.getValue(debug10, debug12) < -0.8999999761581421D) {
/* 113 */           float debug14 = (Mth.abs((float)debug10) * 3439.0F + Mth.abs((float)debug12) * 147.0F) % 13.0F + 9.0F;
/* 114 */           float debug15 = (debug5 - debug8 * 2);
/* 115 */           float debug16 = (debug6 - debug9 * 2);
/* 116 */           float debug17 = 100.0F - Mth.sqrt(debug15 * debug15 + debug16 * debug16) * debug14;
/* 117 */           debug17 = Mth.clamp(debug17, -100.0F, 80.0F);
/* 118 */           debug7 = Math.max(debug7, debug17);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     return debug7;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\TheEndBiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */