/*     */ package com.mojang.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Matrix4f
/*     */ {
/*     */   protected float m00;
/*     */   protected float m01;
/*     */   protected float m02;
/*     */   protected float m03;
/*     */   protected float m10;
/*     */   protected float m11;
/*     */   protected float m12;
/*     */   protected float m13;
/*     */   protected float m20;
/*     */   protected float m21;
/*     */   protected float m22;
/*     */   protected float m23;
/*     */   protected float m30;
/*     */   protected float m31;
/*     */   protected float m32;
/*     */   protected float m33;
/*     */   
/*     */   public Matrix4f() {}
/*     */   
/*     */   public Matrix4f(Matrix4f debug1) {
/*  32 */     this.m00 = debug1.m00;
/*  33 */     this.m01 = debug1.m01;
/*  34 */     this.m02 = debug1.m02;
/*  35 */     this.m03 = debug1.m03;
/*     */     
/*  37 */     this.m10 = debug1.m10;
/*  38 */     this.m11 = debug1.m11;
/*  39 */     this.m12 = debug1.m12;
/*  40 */     this.m13 = debug1.m13;
/*     */     
/*  42 */     this.m20 = debug1.m20;
/*  43 */     this.m21 = debug1.m21;
/*  44 */     this.m22 = debug1.m22;
/*  45 */     this.m23 = debug1.m23;
/*     */     
/*  47 */     this.m30 = debug1.m30;
/*  48 */     this.m31 = debug1.m31;
/*  49 */     this.m32 = debug1.m32;
/*  50 */     this.m33 = debug1.m33;
/*     */   }
/*     */ 
/*     */   
/*     */   public Matrix4f(Quaternion debug1) {
/*  55 */     float debug2 = debug1.i();
/*  56 */     float debug3 = debug1.j();
/*  57 */     float debug4 = debug1.k();
/*  58 */     float debug5 = debug1.r();
/*     */     
/*  60 */     float debug6 = 2.0F * debug2 * debug2;
/*  61 */     float debug7 = 2.0F * debug3 * debug3;
/*  62 */     float debug8 = 2.0F * debug4 * debug4;
/*     */     
/*  64 */     this.m00 = 1.0F - debug7 - debug8;
/*  65 */     this.m11 = 1.0F - debug8 - debug6;
/*  66 */     this.m22 = 1.0F - debug6 - debug7;
/*  67 */     this.m33 = 1.0F;
/*     */     
/*  69 */     float debug9 = debug2 * debug3;
/*  70 */     float debug10 = debug3 * debug4;
/*  71 */     float debug11 = debug4 * debug2;
/*     */     
/*  73 */     float debug12 = debug2 * debug5;
/*  74 */     float debug13 = debug3 * debug5;
/*  75 */     float debug14 = debug4 * debug5;
/*     */     
/*  77 */     this.m10 = 2.0F * (debug9 + debug14);
/*  78 */     this.m01 = 2.0F * (debug9 - debug14);
/*     */     
/*  80 */     this.m20 = 2.0F * (debug11 - debug13);
/*  81 */     this.m02 = 2.0F * (debug11 + debug13);
/*     */     
/*  83 */     this.m21 = 2.0F * (debug10 + debug12);
/*  84 */     this.m12 = 2.0F * (debug10 - debug12);
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
/*     */   public boolean equals(Object debug1) {
/* 112 */     if (this == debug1) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 116 */       return false;
/*     */     }
/*     */     
/* 119 */     Matrix4f debug2 = (Matrix4f)debug1;
/* 120 */     return (Float.compare(debug2.m00, this.m00) == 0 && Float.compare(debug2.m01, this.m01) == 0 && Float.compare(debug2.m02, this.m02) == 0 && Float.compare(debug2.m03, this.m03) == 0 && 
/* 121 */       Float.compare(debug2.m10, this.m10) == 0 && Float.compare(debug2.m11, this.m11) == 0 && Float.compare(debug2.m12, this.m12) == 0 && Float.compare(debug2.m13, this.m13) == 0 && 
/* 122 */       Float.compare(debug2.m20, this.m20) == 0 && Float.compare(debug2.m21, this.m21) == 0 && Float.compare(debug2.m22, this.m22) == 0 && Float.compare(debug2.m23, this.m23) == 0 && 
/* 123 */       Float.compare(debug2.m30, this.m30) == 0 && Float.compare(debug2.m31, this.m31) == 0 && Float.compare(debug2.m32, this.m32) == 0 && Float.compare(debug2.m33, this.m33) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     int debug1 = (this.m00 != 0.0F) ? Float.floatToIntBits(this.m00) : 0;
/* 129 */     debug1 = 31 * debug1 + ((this.m01 != 0.0F) ? Float.floatToIntBits(this.m01) : 0);
/* 130 */     debug1 = 31 * debug1 + ((this.m02 != 0.0F) ? Float.floatToIntBits(this.m02) : 0);
/* 131 */     debug1 = 31 * debug1 + ((this.m03 != 0.0F) ? Float.floatToIntBits(this.m03) : 0);
/* 132 */     debug1 = 31 * debug1 + ((this.m10 != 0.0F) ? Float.floatToIntBits(this.m10) : 0);
/* 133 */     debug1 = 31 * debug1 + ((this.m11 != 0.0F) ? Float.floatToIntBits(this.m11) : 0);
/* 134 */     debug1 = 31 * debug1 + ((this.m12 != 0.0F) ? Float.floatToIntBits(this.m12) : 0);
/* 135 */     debug1 = 31 * debug1 + ((this.m13 != 0.0F) ? Float.floatToIntBits(this.m13) : 0);
/* 136 */     debug1 = 31 * debug1 + ((this.m20 != 0.0F) ? Float.floatToIntBits(this.m20) : 0);
/* 137 */     debug1 = 31 * debug1 + ((this.m21 != 0.0F) ? Float.floatToIntBits(this.m21) : 0);
/* 138 */     debug1 = 31 * debug1 + ((this.m22 != 0.0F) ? Float.floatToIntBits(this.m22) : 0);
/* 139 */     debug1 = 31 * debug1 + ((this.m23 != 0.0F) ? Float.floatToIntBits(this.m23) : 0);
/* 140 */     debug1 = 31 * debug1 + ((this.m30 != 0.0F) ? Float.floatToIntBits(this.m30) : 0);
/* 141 */     debug1 = 31 * debug1 + ((this.m31 != 0.0F) ? Float.floatToIntBits(this.m31) : 0);
/* 142 */     debug1 = 31 * debug1 + ((this.m32 != 0.0F) ? Float.floatToIntBits(this.m32) : 0);
/* 143 */     debug1 = 31 * debug1 + ((this.m33 != 0.0F) ? Float.floatToIntBits(this.m33) : 0);
/* 144 */     return debug1;
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
/*     */   public String toString() {
/* 227 */     StringBuilder debug1 = new StringBuilder();
/* 228 */     debug1.append("Matrix4f:\n");
/* 229 */     debug1.append(this.m00);
/* 230 */     debug1.append(" ");
/* 231 */     debug1.append(this.m01);
/* 232 */     debug1.append(" ");
/* 233 */     debug1.append(this.m02);
/* 234 */     debug1.append(" ");
/* 235 */     debug1.append(this.m03);
/* 236 */     debug1.append("\n");
/*     */     
/* 238 */     debug1.append(this.m10);
/* 239 */     debug1.append(" ");
/* 240 */     debug1.append(this.m11);
/* 241 */     debug1.append(" ");
/* 242 */     debug1.append(this.m12);
/* 243 */     debug1.append(" ");
/* 244 */     debug1.append(this.m13);
/* 245 */     debug1.append("\n");
/*     */     
/* 247 */     debug1.append(this.m20);
/* 248 */     debug1.append(" ");
/* 249 */     debug1.append(this.m21);
/* 250 */     debug1.append(" ");
/* 251 */     debug1.append(this.m22);
/* 252 */     debug1.append(" ");
/* 253 */     debug1.append(this.m23);
/* 254 */     debug1.append("\n");
/*     */     
/* 256 */     debug1.append(this.m30);
/* 257 */     debug1.append(" ");
/* 258 */     debug1.append(this.m31);
/* 259 */     debug1.append(" ");
/* 260 */     debug1.append(this.m32);
/* 261 */     debug1.append(" ");
/* 262 */     debug1.append(this.m33);
/* 263 */     debug1.append("\n");
/* 264 */     return debug1.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\Matrix4f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */