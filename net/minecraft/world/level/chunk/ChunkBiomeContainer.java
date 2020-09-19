/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkBiomeContainer
/*     */   implements BiomeManager.NoiseBiomeSource
/*     */ {
/*  16 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*  19 */   private static final int WIDTH_BITS = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
/*  20 */   private static final int HEIGHT_BITS = (int)Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
/*     */   
/*  22 */   public static final int BIOMES_SIZE = 1 << WIDTH_BITS + WIDTH_BITS + HEIGHT_BITS;
/*  23 */   public static final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
/*  24 */   public static final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;
/*     */   
/*     */   private final IdMap<Biome> biomeRegistry;
/*     */   private final Biome[] biomes;
/*     */   
/*     */   public ChunkBiomeContainer(IdMap<Biome> debug1, Biome[] debug2) {
/*  30 */     this.biomeRegistry = debug1;
/*  31 */     this.biomes = debug2;
/*     */   }
/*     */   
/*     */   private ChunkBiomeContainer(IdMap<Biome> debug1) {
/*  35 */     this(debug1, new Biome[BIOMES_SIZE]);
/*     */   }
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
/*     */   
/*     */   public ChunkBiomeContainer(IdMap<Biome> debug1, ChunkPos debug2, BiomeSource debug3) {
/*  53 */     this(debug1);
/*     */     
/*  55 */     int debug4 = debug2.getMinBlockX() >> 2;
/*  56 */     int debug5 = debug2.getMinBlockZ() >> 2;
/*     */     
/*  58 */     for (int debug6 = 0; debug6 < this.biomes.length; debug6++) {
/*  59 */       int debug7 = debug6 & HORIZONTAL_MASK;
/*  60 */       int debug8 = debug6 >> WIDTH_BITS + WIDTH_BITS & VERTICAL_MASK;
/*  61 */       int debug9 = debug6 >> WIDTH_BITS & HORIZONTAL_MASK;
/*  62 */       this.biomes[debug6] = debug3.getNoiseBiome(debug4 + debug7, debug8, debug5 + debug9);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ChunkBiomeContainer(IdMap<Biome> debug1, ChunkPos debug2, BiomeSource debug3, @Nullable int[] debug4) {
/*  67 */     this(debug1);
/*     */     
/*  69 */     int debug5 = debug2.getMinBlockX() >> 2;
/*  70 */     int debug6 = debug2.getMinBlockZ() >> 2;
/*     */     
/*  72 */     if (debug4 != null) {
/*  73 */       for (int debug7 = 0; debug7 < debug4.length; debug7++) {
/*  74 */         this.biomes[debug7] = (Biome)debug1.byId(debug4[debug7]);
/*  75 */         if (this.biomes[debug7] == null) {
/*  76 */           int debug8 = debug7 & HORIZONTAL_MASK;
/*  77 */           int debug9 = debug7 >> WIDTH_BITS + WIDTH_BITS & VERTICAL_MASK;
/*  78 */           int debug10 = debug7 >> WIDTH_BITS & HORIZONTAL_MASK;
/*  79 */           this.biomes[debug7] = debug3.getNoiseBiome(debug5 + debug8, debug9, debug6 + debug10);
/*     */         } 
/*     */       } 
/*     */     } else {
/*  83 */       for (int debug7 = 0; debug7 < this.biomes.length; debug7++) {
/*  84 */         int debug8 = debug7 & HORIZONTAL_MASK;
/*  85 */         int debug9 = debug7 >> WIDTH_BITS + WIDTH_BITS & VERTICAL_MASK;
/*  86 */         int debug10 = debug7 >> WIDTH_BITS & HORIZONTAL_MASK;
/*  87 */         this.biomes[debug7] = debug3.getNoiseBiome(debug5 + debug8, debug9, debug6 + debug10);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int[] writeBiomes() {
/*  93 */     int[] debug1 = new int[this.biomes.length];
/*  94 */     for (int debug2 = 0; debug2 < this.biomes.length; debug2++) {
/*  95 */       debug1[debug2] = this.biomeRegistry.getId(this.biomes[debug2]);
/*     */     }
/*  97 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/* 102 */     int debug4 = debug1 & HORIZONTAL_MASK;
/* 103 */     int debug5 = Mth.clamp(debug2, 0, VERTICAL_MASK);
/* 104 */     int debug6 = debug3 & HORIZONTAL_MASK;
/* 105 */     return this.biomes[debug5 << WIDTH_BITS + WIDTH_BITS | debug6 << WIDTH_BITS | debug4];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ChunkBiomeContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */