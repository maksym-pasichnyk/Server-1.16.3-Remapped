/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ public class PerlinSimplexNoise implements SurfaceNoise {
/*    */   private final SimplexNoise[] noiseLevels;
/*    */   private final double highestFreqValueFactor;
/*    */   private final double highestFreqInputFactor;
/*    */   
/*    */   public PerlinSimplexNoise(WorldgenRandom debug1, IntStream debug2) {
/* 17 */     this(debug1, debug2.boxed().collect(ImmutableList.toImmutableList()));
/*    */   }
/*    */   
/*    */   public PerlinSimplexNoise(WorldgenRandom debug1, List<Integer> debug2) {
/* 21 */     this(debug1, (IntSortedSet)new IntRBTreeSet(debug2));
/*    */   }
/*    */   
/*    */   private PerlinSimplexNoise(WorldgenRandom debug1, IntSortedSet debug2) {
/* 25 */     if (debug2.isEmpty()) {
/* 26 */       throw new IllegalArgumentException("Need some octaves!");
/*    */     }
/*    */     
/* 29 */     int debug3 = -debug2.firstInt();
/* 30 */     int debug4 = debug2.lastInt();
/*    */     
/* 32 */     int debug5 = debug3 + debug4 + 1;
/* 33 */     if (debug5 < 1) {
/* 34 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
/*    */     }
/*    */     
/* 37 */     SimplexNoise debug6 = new SimplexNoise((Random)debug1);
/* 38 */     int debug7 = debug4;
/*    */     
/* 40 */     this.noiseLevels = new SimplexNoise[debug5];
/* 41 */     if (debug7 >= 0 && debug7 < debug5 && debug2.contains(0)) {
/* 42 */       this.noiseLevels[debug7] = debug6;
/*    */     }
/*    */     
/* 45 */     for (int debug8 = debug7 + 1; debug8 < debug5; debug8++) {
/* 46 */       if (debug8 >= 0 && debug2.contains(debug7 - debug8)) {
/* 47 */         this.noiseLevels[debug8] = new SimplexNoise((Random)debug1);
/*    */       } else {
/* 49 */         debug1.consumeCount(262);
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     if (debug4 > 0) {
/*    */       
/* 55 */       long l = (long)(debug6.getValue(debug6.xo, debug6.yo, debug6.zo) * 9.223372036854776E18D);
/* 56 */       WorldgenRandom debug10 = new WorldgenRandom(l);
/* 57 */       for (int debug11 = debug7 - 1; debug11 >= 0; debug11--) {
/* 58 */         if (debug11 < debug5 && debug2.contains(debug7 - debug11)) {
/* 59 */           this.noiseLevels[debug11] = new SimplexNoise((Random)debug10);
/*    */         } else {
/* 61 */           debug10.consumeCount(262);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     this.highestFreqInputFactor = Math.pow(2.0D, debug4);
/* 67 */     this.highestFreqValueFactor = 1.0D / (Math.pow(2.0D, debug5) - 1.0D);
/*    */   }
/*    */   
/*    */   public double getValue(double debug1, double debug3, boolean debug5) {
/* 71 */     double debug6 = 0.0D;
/* 72 */     double debug8 = this.highestFreqInputFactor;
/* 73 */     double debug10 = this.highestFreqValueFactor;
/*    */     
/* 75 */     for (SimplexNoise debug15 : this.noiseLevels) {
/* 76 */       if (debug15 != null) {
/* 77 */         debug6 += debug15.getValue(debug1 * debug8 + (debug5 ? debug15.xo : 0.0D), debug3 * debug8 + (debug5 ? debug15.yo : 0.0D)) * debug10;
/*    */       }
/* 79 */       debug8 /= 2.0D;
/* 80 */       debug10 *= 2.0D;
/*    */     } 
/*    */     
/* 83 */     return debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getSurfaceNoiseValue(double debug1, double debug3, double debug5, double debug7) {
/* 88 */     return getValue(debug1, debug3, true) * 0.55D;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\synth\PerlinSimplexNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */