/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ 
/*     */ 
/*     */ public class PerlinNoise
/*     */   implements SurfaceNoise
/*     */ {
/*     */   private final ImprovedNoise[] noiseLevels;
/*     */   private final DoubleList amplitudes;
/*     */   private final double lowestFreqValueFactor;
/*     */   private final double lowestFreqInputFactor;
/*     */   
/*     */   public PerlinNoise(WorldgenRandom debug1, IntStream debug2) {
/*  27 */     this(debug1, debug2.boxed().collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   public PerlinNoise(WorldgenRandom debug1, List<Integer> debug2) {
/*  31 */     this(debug1, (IntSortedSet)new IntRBTreeSet(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PerlinNoise create(WorldgenRandom debug0, int debug1, DoubleList debug2) {
/*  39 */     return new PerlinNoise(debug0, Pair.of(Integer.valueOf(debug1), debug2));
/*     */   }
/*     */   
/*     */   private static Pair<Integer, DoubleList> makeAmplitudes(IntSortedSet debug0) {
/*  43 */     if (debug0.isEmpty()) {
/*  44 */       throw new IllegalArgumentException("Need some octaves!");
/*     */     }
/*     */     
/*  47 */     int debug1 = -debug0.firstInt();
/*  48 */     int debug2 = debug0.lastInt();
/*     */     
/*  50 */     int debug3 = debug1 + debug2 + 1;
/*  51 */     if (debug3 < 1) {
/*  52 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
/*     */     }
/*     */     
/*  55 */     DoubleArrayList doubleArrayList = new DoubleArrayList(new double[debug3]);
/*  56 */     IntBidirectionalIterator debug5 = debug0.iterator();
/*  57 */     while (debug5.hasNext()) {
/*  58 */       int debug6 = debug5.nextInt();
/*  59 */       doubleArrayList.set(debug6 + debug1, 1.0D);
/*     */     } 
/*     */     
/*  62 */     return Pair.of(Integer.valueOf(-debug1), doubleArrayList);
/*     */   }
/*     */   
/*     */   private PerlinNoise(WorldgenRandom debug1, IntSortedSet debug2) {
/*  66 */     this(debug1, makeAmplitudes(debug2));
/*     */   }
/*     */   
/*     */   private PerlinNoise(WorldgenRandom debug1, Pair<Integer, DoubleList> debug2) {
/*  70 */     int debug3 = ((Integer)debug2.getFirst()).intValue();
/*  71 */     this.amplitudes = (DoubleList)debug2.getSecond();
/*  72 */     ImprovedNoise debug4 = new ImprovedNoise((Random)debug1);
/*  73 */     int debug5 = this.amplitudes.size();
/*  74 */     int debug6 = -debug3;
/*     */     
/*  76 */     this.noiseLevels = new ImprovedNoise[debug5];
/*  77 */     if (debug6 >= 0 && debug6 < debug5) {
/*  78 */       double d = this.amplitudes.getDouble(debug6);
/*  79 */       if (d != 0.0D) {
/*  80 */         this.noiseLevels[debug6] = debug4;
/*     */       }
/*     */     } 
/*     */     
/*  84 */     for (int debug7 = debug6 - 1; debug7 >= 0; debug7--) {
/*  85 */       if (debug7 < debug5) {
/*  86 */         double debug8 = this.amplitudes.getDouble(debug7);
/*  87 */         if (debug8 != 0.0D) {
/*  88 */           this.noiseLevels[debug7] = new ImprovedNoise((Random)debug1);
/*     */         } else {
/*  90 */           debug1.consumeCount(262);
/*     */         } 
/*     */       } else {
/*  93 */         debug1.consumeCount(262);
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     if (debug6 < debug5 - 1) {
/*     */       
/*  99 */       long l = (long)(debug4.noise(0.0D, 0.0D, 0.0D, 0.0D, 0.0D) * 9.223372036854776E18D);
/* 100 */       WorldgenRandom debug9 = new WorldgenRandom(l);
/* 101 */       for (int debug10 = debug6 + 1; debug10 < debug5; debug10++) {
/* 102 */         if (debug10 >= 0) {
/* 103 */           double debug11 = this.amplitudes.getDouble(debug10);
/* 104 */           if (debug11 != 0.0D) {
/* 105 */             this.noiseLevels[debug10] = new ImprovedNoise((Random)debug9);
/*     */           } else {
/* 107 */             debug9.consumeCount(262);
/*     */           } 
/*     */         } else {
/* 110 */           debug9.consumeCount(262);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     this.lowestFreqInputFactor = Math.pow(2.0D, -debug6);
/* 116 */     this.lowestFreqValueFactor = Math.pow(2.0D, (debug5 - 1)) / (Math.pow(2.0D, debug5) - 1.0D);
/*     */   }
/*     */   
/*     */   public double getValue(double debug1, double debug3, double debug5) {
/* 120 */     return getValue(debug1, debug3, debug5, 0.0D, 0.0D, false);
/*     */   }
/*     */   
/*     */   public double getValue(double debug1, double debug3, double debug5, double debug7, double debug9, boolean debug11) {
/* 124 */     double debug12 = 0.0D;
/* 125 */     double debug14 = this.lowestFreqInputFactor;
/* 126 */     double debug16 = this.lowestFreqValueFactor;
/*     */     
/* 128 */     for (int debug18 = 0; debug18 < this.noiseLevels.length; debug18++) {
/* 129 */       ImprovedNoise debug19 = this.noiseLevels[debug18];
/* 130 */       if (debug19 != null) {
/* 131 */         debug12 += this.amplitudes.getDouble(debug18) * debug19.noise(wrap(debug1 * debug14), debug11 ? -debug19.yo : wrap(debug3 * debug14), wrap(debug5 * debug14), debug7 * debug14, debug9 * debug14) * debug16;
/*     */       }
/* 133 */       debug14 *= 2.0D;
/* 134 */       debug16 /= 2.0D;
/*     */     } 
/*     */     
/* 137 */     return debug12;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ImprovedNoise getOctaveNoise(int debug1) {
/* 142 */     return this.noiseLevels[this.noiseLevels.length - 1 - debug1];
/*     */   }
/*     */   
/*     */   public static double wrap(double debug0) {
/* 146 */     return debug0 - Mth.lfloor(debug0 / 3.3554432E7D + 0.5D) * 3.3554432E7D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getSurfaceNoiseValue(double debug1, double debug3, double debug5, double debug7) {
/* 151 */     return getValue(debug1, debug3, 0.0D, debug5, debug7, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\synth\PerlinNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */