/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class SimplexNoise
/*     */ {
/*   8 */   protected static final int[][] GRADIENT = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 } };
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
/*     */ 
/*     */ 
/*     */   
/*  27 */   private static final double SQRT_3 = Math.sqrt(3.0D);
/*  28 */   private static final double F2 = 0.5D * (SQRT_3 - 1.0D);
/*  29 */   private static final double G2 = (3.0D - SQRT_3) / 6.0D;
/*     */   
/*  31 */   private final int[] p = new int[512];
/*     */   
/*     */   public final double xo;
/*     */   public final double yo;
/*     */   public final double zo;
/*     */   
/*     */   public SimplexNoise(Random debug1) {
/*  38 */     this.xo = debug1.nextDouble() * 256.0D;
/*  39 */     this.yo = debug1.nextDouble() * 256.0D;
/*  40 */     this.zo = debug1.nextDouble() * 256.0D; int debug2;
/*  41 */     for (debug2 = 0; debug2 < 256; debug2++) {
/*  42 */       this.p[debug2] = debug2;
/*     */     }
/*     */     
/*  45 */     for (debug2 = 0; debug2 < 256; debug2++) {
/*  46 */       int debug3 = debug1.nextInt(256 - debug2);
/*  47 */       int debug4 = this.p[debug2];
/*  48 */       this.p[debug2] = this.p[debug3 + debug2];
/*  49 */       this.p[debug3 + debug2] = debug4;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int p(int debug1) {
/*  54 */     return this.p[debug1 & 0xFF];
/*     */   }
/*     */   
/*     */   protected static double dot(int[] debug0, double debug1, double debug3, double debug5) {
/*  58 */     return debug0[0] * debug1 + debug0[1] * debug3 + debug0[2] * debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   private double getCornerNoise3D(int debug1, double debug2, double debug4, double debug6, double debug8) {
/*  63 */     double debug10, debug12 = debug8 - debug2 * debug2 - debug4 * debug4 - debug6 * debug6;
/*  64 */     if (debug12 < 0.0D) {
/*  65 */       debug10 = 0.0D;
/*     */     } else {
/*  67 */       debug12 *= debug12;
/*  68 */       debug10 = debug12 * debug12 * dot(GRADIENT[debug1], debug2, debug4, debug6);
/*     */     } 
/*  70 */     return debug10;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getValue(double debug1, double debug3) {
/*     */     int debug19, debug20;
/*  76 */     double debug5 = (debug1 + debug3) * F2;
/*  77 */     int debug7 = Mth.floor(debug1 + debug5);
/*  78 */     int debug8 = Mth.floor(debug3 + debug5);
/*     */ 
/*     */     
/*  81 */     double debug9 = (debug7 + debug8) * G2;
/*  82 */     double debug11 = debug7 - debug9;
/*  83 */     double debug13 = debug8 - debug9;
/*     */ 
/*     */     
/*  86 */     double debug15 = debug1 - debug11;
/*  87 */     double debug17 = debug3 - debug13;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     if (debug15 > debug17) {
/*     */       
/*  97 */       debug19 = 1;
/*  98 */       debug20 = 0;
/*     */     } else {
/*     */       
/* 101 */       debug19 = 0;
/* 102 */       debug20 = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     double debug21 = debug15 - debug19 + G2;
/* 110 */     double debug23 = debug17 - debug20 + G2;
/*     */ 
/*     */     
/* 113 */     double debug25 = debug15 - 1.0D + 2.0D * G2;
/* 114 */     double debug27 = debug17 - 1.0D + 2.0D * G2;
/*     */ 
/*     */     
/* 117 */     int debug29 = debug7 & 0xFF;
/* 118 */     int debug30 = debug8 & 0xFF;
/*     */     
/* 120 */     int debug31 = p(debug29 + p(debug30)) % 12;
/* 121 */     int debug32 = p(debug29 + debug19 + p(debug30 + debug20)) % 12;
/* 122 */     int debug33 = p(debug29 + 1 + p(debug30 + 1)) % 12;
/*     */ 
/*     */ 
/*     */     
/* 126 */     double debug34 = getCornerNoise3D(debug31, debug15, debug17, 0.0D, 0.5D);
/* 127 */     double debug36 = getCornerNoise3D(debug32, debug21, debug23, 0.0D, 0.5D);
/* 128 */     double debug38 = getCornerNoise3D(debug33, debug25, debug27, 0.0D, 0.5D);
/*     */ 
/*     */ 
/*     */     
/* 132 */     return 70.0D * (debug34 + debug36 + debug38);
/*     */   }
/*     */   
/*     */   public double getValue(double debug1, double debug3, double debug5) {
/*     */     int debug30, debug31, debug32, debug33, debug34, debug35;
/* 137 */     double debug7 = 0.3333333333333333D;
/* 138 */     double debug9 = (debug1 + debug3 + debug5) * 0.3333333333333333D;
/*     */     
/* 140 */     int debug11 = Mth.floor(debug1 + debug9);
/* 141 */     int debug12 = Mth.floor(debug3 + debug9);
/* 142 */     int debug13 = Mth.floor(debug5 + debug9);
/* 143 */     double debug14 = 0.16666666666666666D;
/* 144 */     double debug16 = (debug11 + debug12 + debug13) * 0.16666666666666666D;
/*     */     
/* 146 */     double debug18 = debug11 - debug16;
/* 147 */     double debug20 = debug12 - debug16;
/* 148 */     double debug22 = debug13 - debug16;
/*     */     
/* 150 */     double debug24 = debug1 - debug18;
/* 151 */     double debug26 = debug3 - debug20;
/* 152 */     double debug28 = debug5 - debug22;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (debug24 >= debug26) {
/* 162 */       if (debug26 >= debug28) {
/*     */         
/* 164 */         debug30 = 1;
/* 165 */         debug31 = 0;
/* 166 */         debug32 = 0;
/* 167 */         debug33 = 1;
/* 168 */         debug34 = 1;
/* 169 */         debug35 = 0;
/* 170 */       } else if (debug24 >= debug28) {
/*     */         
/* 172 */         debug30 = 1;
/* 173 */         debug31 = 0;
/* 174 */         debug32 = 0;
/* 175 */         debug33 = 1;
/* 176 */         debug34 = 0;
/* 177 */         debug35 = 1;
/*     */       } else {
/*     */         
/* 180 */         debug30 = 0;
/* 181 */         debug31 = 0;
/* 182 */         debug32 = 1;
/* 183 */         debug33 = 1;
/* 184 */         debug34 = 0;
/* 185 */         debug35 = 1;
/*     */       }
/*     */     
/*     */     }
/* 189 */     else if (debug26 < debug28) {
/*     */       
/* 191 */       debug30 = 0;
/* 192 */       debug31 = 0;
/* 193 */       debug32 = 1;
/* 194 */       debug33 = 0;
/* 195 */       debug34 = 1;
/* 196 */       debug35 = 1;
/* 197 */     } else if (debug24 < debug28) {
/*     */       
/* 199 */       debug30 = 0;
/* 200 */       debug31 = 1;
/* 201 */       debug32 = 0;
/* 202 */       debug33 = 0;
/* 203 */       debug34 = 1;
/* 204 */       debug35 = 1;
/*     */     } else {
/*     */       
/* 207 */       debug30 = 0;
/* 208 */       debug31 = 1;
/* 209 */       debug32 = 0;
/* 210 */       debug33 = 1;
/* 211 */       debug34 = 1;
/* 212 */       debug35 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     double debug36 = debug24 - debug30 + 0.16666666666666666D;
/* 222 */     double debug38 = debug26 - debug31 + 0.16666666666666666D;
/* 223 */     double debug40 = debug28 - debug32 + 0.16666666666666666D;
/*     */ 
/*     */     
/* 226 */     double debug42 = debug24 - debug33 + 0.3333333333333333D;
/* 227 */     double debug44 = debug26 - debug34 + 0.3333333333333333D;
/* 228 */     double debug46 = debug28 - debug35 + 0.3333333333333333D;
/*     */ 
/*     */     
/* 231 */     double debug48 = debug24 - 1.0D + 0.5D;
/* 232 */     double debug50 = debug26 - 1.0D + 0.5D;
/* 233 */     double debug52 = debug28 - 1.0D + 0.5D;
/*     */ 
/*     */     
/* 236 */     int debug54 = debug11 & 0xFF;
/* 237 */     int debug55 = debug12 & 0xFF;
/* 238 */     int debug56 = debug13 & 0xFF;
/*     */     
/* 240 */     int debug57 = p(debug54 + p(debug55 + p(debug56))) % 12;
/* 241 */     int debug58 = p(debug54 + debug30 + p(debug55 + debug31 + p(debug56 + debug32))) % 12;
/* 242 */     int debug59 = p(debug54 + debug33 + p(debug55 + debug34 + p(debug56 + debug35))) % 12;
/* 243 */     int debug60 = p(debug54 + 1 + p(debug55 + 1 + p(debug56 + 1))) % 12;
/*     */ 
/*     */     
/* 246 */     double debug61 = getCornerNoise3D(debug57, debug24, debug26, debug28, 0.6D);
/* 247 */     double debug63 = getCornerNoise3D(debug58, debug36, debug38, debug40, 0.6D);
/* 248 */     double debug65 = getCornerNoise3D(debug59, debug42, debug44, debug46, 0.6D);
/* 249 */     double debug67 = getCornerNoise3D(debug60, debug48, debug50, debug52, 0.6D);
/*     */ 
/*     */ 
/*     */     
/* 253 */     return 32.0D * (debug61 + debug63 + debug65 + debug67);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\synth\SimplexNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */