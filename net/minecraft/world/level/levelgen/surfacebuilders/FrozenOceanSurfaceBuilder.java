/*     */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class FrozenOceanSurfaceBuilder
/*     */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/*  19 */   protected static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
/*  20 */   protected static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
/*  21 */   private static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  22 */   private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/*  23 */   private static final BlockState ICE = Blocks.ICE.defaultBlockState();
/*     */   
/*     */   private PerlinSimplexNoise icebergNoise;
/*     */   private PerlinSimplexNoise icebergRoofNoise;
/*     */   private long seed;
/*     */   
/*     */   public FrozenOceanSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/*  30 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/*  35 */     double debug15 = 0.0D;
/*  36 */     double debug17 = 0.0D;
/*  37 */     BlockPos.MutableBlockPos debug19 = new BlockPos.MutableBlockPos();
/*  38 */     float debug20 = debug3.getTemperature((BlockPos)debug19.set(debug4, 63, debug5));
/*     */     
/*  40 */     double debug21 = Math.min(Math.abs(debug7), this.icebergNoise.getValue(debug4 * 0.1D, debug5 * 0.1D, false) * 15.0D);
/*     */     
/*  42 */     if (debug21 > 1.8D) {
/*  43 */       double d1 = 0.09765625D;
/*  44 */       double d2 = Math.abs(this.icebergRoofNoise.getValue(debug4 * 0.09765625D, debug5 * 0.09765625D, false));
/*  45 */       debug15 = debug21 * debug21 * 1.2D;
/*  46 */       double d3 = Math.ceil(d2 * 40.0D) + 14.0D;
/*  47 */       if (debug15 > d3) {
/*  48 */         debug15 = d3;
/*     */       }
/*     */       
/*  51 */       if (debug20 > 0.1F) {
/*  52 */         debug15 -= 2.0D;
/*     */       }
/*     */       
/*  55 */       if (debug15 > 2.0D) {
/*  56 */         debug17 = debug11 - debug15 - 7.0D;
/*  57 */         debug15 += debug11;
/*     */       } else {
/*  59 */         debug15 = 0.0D;
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     int debug23 = debug4 & 0xF;
/*  64 */     int debug24 = debug5 & 0xF;
/*     */     
/*  66 */     SurfaceBuilderConfiguration debug25 = debug3.getGenerationSettings().getSurfaceBuilderConfig();
/*  67 */     BlockState debug26 = debug25.getUnderMaterial();
/*  68 */     BlockState debug27 = debug25.getTopMaterial();
/*  69 */     BlockState debug28 = debug26;
/*  70 */     BlockState debug29 = debug27;
/*     */     
/*  72 */     int debug30 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*  73 */     int debug31 = -1;
/*  74 */     int debug32 = 0;
/*  75 */     int debug33 = 2 + debug1.nextInt(4);
/*  76 */     int debug34 = debug11 + 18 + debug1.nextInt(10);
/*     */     
/*  78 */     for (int debug35 = Math.max(debug6, (int)debug15 + 1); debug35 >= 0; debug35--) {
/*  79 */       debug19.set(debug23, debug35, debug24);
/*     */       
/*  81 */       if (debug2.getBlockState((BlockPos)debug19).isAir() && debug35 < (int)debug15 && debug1.nextDouble() > 0.01D) {
/*  82 */         debug2.setBlockState((BlockPos)debug19, PACKED_ICE, false);
/*  83 */       } else if (debug2.getBlockState((BlockPos)debug19).getMaterial() == Material.WATER && debug35 > (int)debug17 && debug35 < debug11 && debug17 != 0.0D && debug1.nextDouble() > 0.15D) {
/*  84 */         debug2.setBlockState((BlockPos)debug19, PACKED_ICE, false);
/*     */       } 
/*     */       
/*  87 */       BlockState debug36 = debug2.getBlockState((BlockPos)debug19);
/*  88 */       if (debug36.isAir()) {
/*  89 */         debug31 = -1;
/*     */ 
/*     */       
/*     */       }
/*  93 */       else if (debug36.is(debug9.getBlock())) {
/*  94 */         if (debug31 == -1) {
/*  95 */           if (debug30 <= 0) {
/*  96 */             debug29 = AIR;
/*  97 */             debug28 = debug9;
/*  98 */           } else if (debug35 >= debug11 - 4 && debug35 <= debug11 + 1) {
/*  99 */             debug29 = debug27;
/* 100 */             debug28 = debug26;
/*     */           } 
/*     */           
/* 103 */           if (debug35 < debug11 && (debug29 == null || debug29.isAir())) {
/* 104 */             if (debug3.getTemperature((BlockPos)debug19.set(debug4, debug35, debug5)) < 0.15F) {
/* 105 */               debug29 = ICE;
/*     */             } else {
/* 107 */               debug29 = debug10;
/*     */             } 
/*     */           }
/*     */           
/* 111 */           debug31 = debug30;
/* 112 */           if (debug35 >= debug11 - 1) {
/* 113 */             debug2.setBlockState((BlockPos)debug19, debug29, false);
/* 114 */           } else if (debug35 < debug11 - 7 - debug30) {
/* 115 */             debug29 = AIR;
/* 116 */             debug28 = debug9;
/* 117 */             debug2.setBlockState((BlockPos)debug19, GRAVEL, false);
/*     */           } else {
/* 119 */             debug2.setBlockState((BlockPos)debug19, debug28, false);
/*     */           } 
/* 121 */         } else if (debug31 > 0) {
/* 122 */           debug31--;
/* 123 */           debug2.setBlockState((BlockPos)debug19, debug28, false);
/* 124 */           if (debug31 == 0 && debug28.is(Blocks.SAND) && debug30 > 1) {
/* 125 */             debug31 = debug1.nextInt(4) + Math.max(0, debug35 - 63);
/* 126 */             debug28 = debug28.is(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
/*     */           } 
/*     */         } 
/* 129 */       } else if (debug36.is(Blocks.PACKED_ICE) && 
/* 130 */         debug32 <= debug33 && debug35 > debug34) {
/* 131 */         debug2.setBlockState((BlockPos)debug19, SNOW_BLOCK, false);
/* 132 */         debug32++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initNoise(long debug1) {
/* 140 */     if (this.seed != debug1 || this.icebergNoise == null || this.icebergRoofNoise == null) {
/* 141 */       WorldgenRandom debug3 = new WorldgenRandom(debug1);
/* 142 */       this.icebergNoise = new PerlinSimplexNoise(debug3, IntStream.rangeClosed(-3, 0));
/* 143 */       this.icebergRoofNoise = new PerlinSimplexNoise(debug3, (List)ImmutableList.of(Integer.valueOf(0)));
/*     */     } 
/* 145 */     this.seed = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\FrozenOceanSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */