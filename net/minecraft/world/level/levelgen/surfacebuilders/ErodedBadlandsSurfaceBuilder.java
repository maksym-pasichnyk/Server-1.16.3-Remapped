/*     */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ public class ErodedBadlandsSurfaceBuilder
/*     */   extends BadlandsSurfaceBuilder {
/*  14 */   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
/*  15 */   private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/*  16 */   private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
/*     */   
/*     */   public ErodedBadlandsSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/*  19 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/*  24 */     double debug15 = 0.0D;
/*     */     
/*  26 */     double debug17 = Math.min(Math.abs(debug7), this.pillarNoise.getValue(debug4 * 0.25D, debug5 * 0.25D, false) * 15.0D);
/*  27 */     if (debug17 > 0.0D) {
/*  28 */       double d1 = 0.001953125D;
/*  29 */       double d2 = Math.abs(this.pillarRoofNoise.getValue(debug4 * 0.001953125D, debug5 * 0.001953125D, false));
/*  30 */       debug15 = debug17 * debug17 * 2.5D;
/*  31 */       double d3 = Math.ceil(d2 * 50.0D) + 14.0D;
/*  32 */       if (debug15 > d3) {
/*  33 */         debug15 = d3;
/*     */       }
/*  35 */       debug15 += 64.0D;
/*     */     } 
/*     */     
/*  38 */     int debug19 = debug4 & 0xF;
/*  39 */     int debug20 = debug5 & 0xF;
/*     */     
/*  41 */     BlockState debug21 = WHITE_TERRACOTTA;
/*  42 */     SurfaceBuilderConfiguration debug22 = debug3.getGenerationSettings().getSurfaceBuilderConfig();
/*  43 */     BlockState debug23 = debug22.getUnderMaterial();
/*  44 */     BlockState debug24 = debug22.getTopMaterial();
/*  45 */     BlockState debug25 = debug23;
/*     */     
/*  47 */     int debug26 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*  48 */     boolean debug27 = (Math.cos(debug7 / 3.0D * Math.PI) > 0.0D);
/*  49 */     int debug28 = -1;
/*  50 */     boolean debug29 = false;
/*     */     
/*  52 */     BlockPos.MutableBlockPos debug30 = new BlockPos.MutableBlockPos();
/*     */     
/*  54 */     for (int debug31 = Math.max(debug6, (int)debug15 + 1); debug31 >= 0; debug31--) {
/*  55 */       debug30.set(debug19, debug31, debug20);
/*  56 */       if (debug2.getBlockState((BlockPos)debug30).isAir() && debug31 < (int)debug15) {
/*  57 */         debug2.setBlockState((BlockPos)debug30, debug9, false);
/*     */       }
/*     */       
/*  60 */       BlockState debug32 = debug2.getBlockState((BlockPos)debug30);
/*     */       
/*  62 */       if (debug32.isAir()) {
/*  63 */         debug28 = -1;
/*  64 */       } else if (debug32.is(debug9.getBlock())) {
/*  65 */         if (debug28 == -1) {
/*  66 */           debug29 = false;
/*  67 */           if (debug26 <= 0) {
/*  68 */             debug21 = Blocks.AIR.defaultBlockState();
/*  69 */             debug25 = debug9;
/*  70 */           } else if (debug31 >= debug11 - 4 && debug31 <= debug11 + 1) {
/*  71 */             debug21 = WHITE_TERRACOTTA;
/*  72 */             debug25 = debug23;
/*     */           } 
/*     */           
/*  75 */           if (debug31 < debug11 && (debug21 == null || debug21.isAir())) {
/*  76 */             debug21 = debug10;
/*     */           }
/*     */           
/*  79 */           debug28 = debug26 + Math.max(0, debug31 - debug11);
/*  80 */           if (debug31 >= debug11 - 1) {
/*  81 */             if (debug31 > debug11 + 3 + debug26) {
/*     */               BlockState debug33;
/*  83 */               if (debug31 < 64 || debug31 > 127) {
/*  84 */                 debug33 = ORANGE_TERRACOTTA;
/*  85 */               } else if (debug27) {
/*  86 */                 debug33 = TERRACOTTA;
/*     */               } else {
/*  88 */                 debug33 = getBand(debug4, debug31, debug5);
/*     */               } 
/*  90 */               debug2.setBlockState((BlockPos)debug30, debug33, false);
/*     */             } else {
/*  92 */               debug2.setBlockState((BlockPos)debug30, debug24, false);
/*  93 */               debug29 = true;
/*     */             } 
/*     */           } else {
/*  96 */             debug2.setBlockState((BlockPos)debug30, debug25, false);
/*  97 */             Block debug33 = debug25.getBlock();
/*  98 */             if (debug33 == Blocks.WHITE_TERRACOTTA || debug33 == Blocks.ORANGE_TERRACOTTA || debug33 == Blocks.MAGENTA_TERRACOTTA || debug33 == Blocks.LIGHT_BLUE_TERRACOTTA || debug33 == Blocks.YELLOW_TERRACOTTA || debug33 == Blocks.LIME_TERRACOTTA || debug33 == Blocks.PINK_TERRACOTTA || debug33 == Blocks.GRAY_TERRACOTTA || debug33 == Blocks.LIGHT_GRAY_TERRACOTTA || debug33 == Blocks.CYAN_TERRACOTTA || debug33 == Blocks.PURPLE_TERRACOTTA || debug33 == Blocks.BLUE_TERRACOTTA || debug33 == Blocks.BROWN_TERRACOTTA || debug33 == Blocks.GREEN_TERRACOTTA || debug33 == Blocks.RED_TERRACOTTA || debug33 == Blocks.BLACK_TERRACOTTA)
/*     */             {
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
/* 115 */               debug2.setBlockState((BlockPos)debug30, ORANGE_TERRACOTTA, false);
/*     */             }
/*     */           } 
/* 118 */         } else if (debug28 > 0) {
/* 119 */           debug28--;
/*     */           
/* 121 */           if (debug29) {
/* 122 */             debug2.setBlockState((BlockPos)debug30, ORANGE_TERRACOTTA, false);
/*     */           } else {
/* 124 */             debug2.setBlockState((BlockPos)debug30, getBand(debug4, debug31, debug5), false);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\ErodedBadlandsSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */