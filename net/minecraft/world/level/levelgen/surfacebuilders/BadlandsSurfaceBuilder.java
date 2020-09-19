/*     */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ 
/*     */ public class BadlandsSurfaceBuilder
/*     */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration>
/*     */ {
/*  21 */   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
/*  22 */   private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/*  23 */   private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
/*  24 */   private static final BlockState YELLOW_TERRACOTTA = Blocks.YELLOW_TERRACOTTA.defaultBlockState();
/*  25 */   private static final BlockState BROWN_TERRACOTTA = Blocks.BROWN_TERRACOTTA.defaultBlockState();
/*  26 */   private static final BlockState RED_TERRACOTTA = Blocks.RED_TERRACOTTA.defaultBlockState();
/*  27 */   private static final BlockState LIGHT_GRAY_TERRACOTTA = Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
/*     */   
/*     */   protected BlockState[] clayBands;
/*     */   protected long seed;
/*     */   protected PerlinSimplexNoise pillarNoise;
/*     */   protected PerlinSimplexNoise pillarRoofNoise;
/*     */   protected PerlinSimplexNoise clayBandsOffsetNoise;
/*     */   
/*     */   public BadlandsSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/*  36 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/*  41 */     int debug15 = debug4 & 0xF;
/*  42 */     int debug16 = debug5 & 0xF;
/*     */     
/*  44 */     BlockState debug17 = WHITE_TERRACOTTA;
/*  45 */     SurfaceBuilderConfiguration debug18 = debug3.getGenerationSettings().getSurfaceBuilderConfig();
/*  46 */     BlockState debug19 = debug18.getUnderMaterial();
/*  47 */     BlockState debug20 = debug18.getTopMaterial();
/*  48 */     BlockState debug21 = debug19;
/*     */     
/*  50 */     int debug22 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*  51 */     boolean debug23 = (Math.cos(debug7 / 3.0D * Math.PI) > 0.0D);
/*  52 */     int debug24 = -1;
/*  53 */     boolean debug25 = false;
/*  54 */     int debug26 = 0;
/*     */     
/*  56 */     BlockPos.MutableBlockPos debug27 = new BlockPos.MutableBlockPos();
/*     */     
/*  58 */     for (int debug28 = debug6; debug28 >= 0; debug28--) {
/*  59 */       if (debug26 < 15) {
/*  60 */         debug27.set(debug15, debug28, debug16);
/*  61 */         BlockState debug29 = debug2.getBlockState((BlockPos)debug27);
/*     */         
/*  63 */         if (debug29.isAir()) {
/*  64 */           debug24 = -1;
/*  65 */         } else if (debug29.is(debug9.getBlock())) {
/*  66 */           if (debug24 == -1) {
/*  67 */             debug25 = false;
/*  68 */             if (debug22 <= 0) {
/*  69 */               debug17 = Blocks.AIR.defaultBlockState();
/*  70 */               debug21 = debug9;
/*  71 */             } else if (debug28 >= debug11 - 4 && debug28 <= debug11 + 1) {
/*  72 */               debug17 = WHITE_TERRACOTTA;
/*  73 */               debug21 = debug19;
/*     */             } 
/*     */             
/*  76 */             if (debug28 < debug11 && (debug17 == null || debug17.isAir())) {
/*  77 */               debug17 = debug10;
/*     */             }
/*     */             
/*  80 */             debug24 = debug22 + Math.max(0, debug28 - debug11);
/*  81 */             if (debug28 >= debug11 - 1) {
/*  82 */               if (debug28 > debug11 + 3 + debug22) {
/*     */                 BlockState debug30;
/*  84 */                 if (debug28 < 64 || debug28 > 127) {
/*  85 */                   debug30 = ORANGE_TERRACOTTA;
/*  86 */                 } else if (debug23) {
/*  87 */                   debug30 = TERRACOTTA;
/*     */                 } else {
/*  89 */                   debug30 = getBand(debug4, debug28, debug5);
/*     */                 } 
/*  91 */                 debug2.setBlockState((BlockPos)debug27, debug30, false);
/*     */               } else {
/*  93 */                 debug2.setBlockState((BlockPos)debug27, debug20, false);
/*  94 */                 debug25 = true;
/*     */               } 
/*     */             } else {
/*  97 */               debug2.setBlockState((BlockPos)debug27, debug21, false);
/*  98 */               Block debug30 = debug21.getBlock();
/*  99 */               if (debug30 == Blocks.WHITE_TERRACOTTA || debug30 == Blocks.ORANGE_TERRACOTTA || debug30 == Blocks.MAGENTA_TERRACOTTA || debug30 == Blocks.LIGHT_BLUE_TERRACOTTA || debug30 == Blocks.YELLOW_TERRACOTTA || debug30 == Blocks.LIME_TERRACOTTA || debug30 == Blocks.PINK_TERRACOTTA || debug30 == Blocks.GRAY_TERRACOTTA || debug30 == Blocks.LIGHT_GRAY_TERRACOTTA || debug30 == Blocks.CYAN_TERRACOTTA || debug30 == Blocks.PURPLE_TERRACOTTA || debug30 == Blocks.BLUE_TERRACOTTA || debug30 == Blocks.BROWN_TERRACOTTA || debug30 == Blocks.GREEN_TERRACOTTA || debug30 == Blocks.RED_TERRACOTTA || debug30 == Blocks.BLACK_TERRACOTTA)
/*     */               {
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
/* 116 */                 debug2.setBlockState((BlockPos)debug27, ORANGE_TERRACOTTA, false);
/*     */               }
/*     */             } 
/* 119 */           } else if (debug24 > 0) {
/* 120 */             debug24--;
/*     */             
/* 122 */             if (debug25) {
/* 123 */               debug2.setBlockState((BlockPos)debug27, ORANGE_TERRACOTTA, false);
/*     */             } else {
/* 125 */               debug2.setBlockState((BlockPos)debug27, getBand(debug4, debug28, debug5), false);
/*     */             } 
/*     */           } 
/* 128 */           debug26++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initNoise(long debug1) {
/* 137 */     if (this.seed != debug1 || this.clayBands == null) {
/* 138 */       generateBands(debug1);
/*     */     }
/* 140 */     if (this.seed != debug1 || this.pillarNoise == null || this.pillarRoofNoise == null) {
/* 141 */       WorldgenRandom debug3 = new WorldgenRandom(debug1);
/* 142 */       this.pillarNoise = new PerlinSimplexNoise(debug3, IntStream.rangeClosed(-3, 0));
/* 143 */       this.pillarRoofNoise = new PerlinSimplexNoise(debug3, (List)ImmutableList.of(Integer.valueOf(0)));
/*     */     } 
/* 145 */     this.seed = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateBands(long debug1) {
/* 150 */     this.clayBands = new BlockState[64];
/* 151 */     Arrays.fill((Object[])this.clayBands, TERRACOTTA);
/*     */     
/* 153 */     WorldgenRandom debug3 = new WorldgenRandom(debug1);
/* 154 */     this.clayBandsOffsetNoise = new PerlinSimplexNoise(debug3, (List)ImmutableList.of(Integer.valueOf(0)));
/*     */     int debug4;
/* 156 */     for (debug4 = 0; debug4 < 64; debug4++) {
/* 157 */       debug4 += debug3.nextInt(5) + 1;
/* 158 */       if (debug4 < 64) {
/* 159 */         this.clayBands[debug4] = ORANGE_TERRACOTTA;
/*     */       }
/*     */     } 
/*     */     
/* 163 */     debug4 = debug3.nextInt(4) + 2; int debug5;
/* 164 */     for (debug5 = 0; debug5 < debug4; debug5++) {
/* 165 */       int i = debug3.nextInt(3) + 1;
/* 166 */       int j = debug3.nextInt(64);
/*     */       
/* 168 */       for (int k = 0; j + k < 64 && k < i; k++) {
/* 169 */         this.clayBands[j + k] = YELLOW_TERRACOTTA;
/*     */       }
/*     */     } 
/* 172 */     debug5 = debug3.nextInt(4) + 2; int debug6;
/* 173 */     for (debug6 = 0; debug6 < debug5; debug6++) {
/* 174 */       int i = debug3.nextInt(3) + 2;
/* 175 */       int j = debug3.nextInt(64);
/*     */       
/* 177 */       for (int k = 0; j + k < 64 && k < i; k++) {
/* 178 */         this.clayBands[j + k] = BROWN_TERRACOTTA;
/*     */       }
/*     */     } 
/* 181 */     debug6 = debug3.nextInt(4) + 2; int debug7;
/* 182 */     for (debug7 = 0; debug7 < debug6; debug7++) {
/* 183 */       int i = debug3.nextInt(3) + 1;
/* 184 */       int j = debug3.nextInt(64);
/*     */       
/* 186 */       for (int debug10 = 0; j + debug10 < 64 && debug10 < i; debug10++) {
/* 187 */         this.clayBands[j + debug10] = RED_TERRACOTTA;
/*     */       }
/*     */     } 
/* 190 */     debug7 = debug3.nextInt(3) + 3;
/* 191 */     int debug8 = 0;
/* 192 */     for (int debug9 = 0; debug9 < debug7; debug9++) {
/* 193 */       int debug10 = 1;
/* 194 */       debug8 += debug3.nextInt(16) + 4;
/*     */       
/* 196 */       for (int debug11 = 0; debug8 + debug11 < 64 && debug11 < 1; debug11++) {
/* 197 */         this.clayBands[debug8 + debug11] = WHITE_TERRACOTTA;
/* 198 */         if (debug8 + debug11 > 1 && debug3.nextBoolean()) {
/* 199 */           this.clayBands[debug8 + debug11 - 1] = LIGHT_GRAY_TERRACOTTA;
/*     */         }
/* 201 */         if (debug8 + debug11 < 63 && debug3.nextBoolean()) {
/* 202 */           this.clayBands[debug8 + debug11 + 1] = LIGHT_GRAY_TERRACOTTA;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState getBand(int debug1, int debug2, int debug3) {
/* 209 */     int debug4 = (int)Math.round(this.clayBandsOffsetNoise.getValue(debug1 / 512.0D, debug3 / 512.0D, false) * 2.0D);
/* 210 */     return this.clayBands[(debug2 + debug4 + 64) % 64];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\BadlandsSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */