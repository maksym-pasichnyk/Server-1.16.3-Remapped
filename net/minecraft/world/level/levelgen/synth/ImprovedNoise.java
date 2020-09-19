/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ImprovedNoise
/*    */ {
/*    */   private final byte[] p;
/*    */   public final double xo;
/*    */   public final double yo;
/*    */   public final double zo;
/*    */   
/*    */   public ImprovedNoise(Random debug1) {
/* 16 */     this.xo = debug1.nextDouble() * 256.0D;
/* 17 */     this.yo = debug1.nextDouble() * 256.0D;
/* 18 */     this.zo = debug1.nextDouble() * 256.0D;
/*    */     
/* 20 */     this.p = new byte[256];
/*    */     int debug2;
/* 22 */     for (debug2 = 0; debug2 < 256; debug2++) {
/* 23 */       this.p[debug2] = (byte)debug2;
/*    */     }
/*    */     
/* 26 */     for (debug2 = 0; debug2 < 256; debug2++) {
/* 27 */       int debug3 = debug1.nextInt(256 - debug2);
/* 28 */       byte debug4 = this.p[debug2];
/* 29 */       this.p[debug2] = this.p[debug2 + debug3];
/* 30 */       this.p[debug2 + debug3] = debug4;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public double noise(double debug1, double debug3, double debug5, double debug7, double debug9) {
/* 36 */     double debug32, debug11 = debug1 + this.xo;
/* 37 */     double debug13 = debug3 + this.yo;
/* 38 */     double debug15 = debug5 + this.zo;
/*    */     
/* 40 */     int debug17 = Mth.floor(debug11);
/* 41 */     int debug18 = Mth.floor(debug13);
/* 42 */     int debug19 = Mth.floor(debug15);
/*    */ 
/*    */     
/* 45 */     double debug20 = debug11 - debug17;
/* 46 */     double debug22 = debug13 - debug18;
/* 47 */     double debug24 = debug15 - debug19;
/*    */ 
/*    */     
/* 50 */     double debug26 = Mth.smoothstep(debug20);
/* 51 */     double debug28 = Mth.smoothstep(debug22);
/* 52 */     double debug30 = Mth.smoothstep(debug24);
/*    */ 
/*    */     
/* 55 */     if (debug7 != 0.0D) {
/* 56 */       double debug34 = Math.min(debug9, debug22);
/* 57 */       debug32 = Mth.floor(debug34 / debug7) * debug7;
/*    */     } else {
/* 59 */       debug32 = 0.0D;
/*    */     } 
/*    */     
/* 62 */     return sampleAndLerp(debug17, debug18, debug19, debug20, debug22 - debug32, debug24, debug26, debug28, debug30);
/*    */   }
/*    */   
/*    */   private static double gradDot(int debug0, double debug1, double debug3, double debug5) {
/* 66 */     int debug7 = debug0 & 0xF;
/* 67 */     return SimplexNoise.dot(SimplexNoise.GRADIENT[debug7], debug1, debug3, debug5);
/*    */   }
/*    */   
/*    */   private int p(int debug1) {
/* 71 */     return this.p[debug1 & 0xFF] & 0xFF;
/*    */   }
/*    */ 
/*    */   
/*    */   public double sampleAndLerp(int debug1, int debug2, int debug3, double debug4, double debug6, double debug8, double debug10, double debug12, double debug14) {
/* 76 */     int debug16 = p(debug1) + debug2;
/* 77 */     int debug17 = p(debug16) + debug3;
/* 78 */     int debug18 = p(debug16 + 1) + debug3;
/*    */     
/* 80 */     int debug19 = p(debug1 + 1) + debug2;
/* 81 */     int debug20 = p(debug19) + debug3;
/* 82 */     int debug21 = p(debug19 + 1) + debug3;
/*    */ 
/*    */     
/* 85 */     double debug22 = gradDot(p(debug17), debug4, debug6, debug8);
/* 86 */     double debug24 = gradDot(p(debug20), debug4 - 1.0D, debug6, debug8);
/* 87 */     double debug26 = gradDot(p(debug18), debug4, debug6 - 1.0D, debug8);
/* 88 */     double debug28 = gradDot(p(debug21), debug4 - 1.0D, debug6 - 1.0D, debug8);
/* 89 */     double debug30 = gradDot(p(debug17 + 1), debug4, debug6, debug8 - 1.0D);
/* 90 */     double debug32 = gradDot(p(debug20 + 1), debug4 - 1.0D, debug6, debug8 - 1.0D);
/* 91 */     double debug34 = gradDot(p(debug18 + 1), debug4, debug6 - 1.0D, debug8 - 1.0D);
/* 92 */     double debug36 = gradDot(p(debug21 + 1), debug4 - 1.0D, debug6 - 1.0D, debug8 - 1.0D);
/*    */ 
/*    */     
/* 95 */     return Mth.lerp3(debug10, debug12, debug14, debug22, debug24, debug26, debug28, debug30, debug32, debug34, debug36);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\synth\ImprovedNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */