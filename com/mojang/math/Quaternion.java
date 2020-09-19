/*     */ package com.mojang.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Quaternion
/*     */ {
/*   8 */   public static final Quaternion ONE = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
/*     */   
/*     */   private float i;
/*     */   private float j;
/*     */   private float k;
/*     */   private float r;
/*     */   
/*     */   public Quaternion(float debug1, float debug2, float debug3, float debug4) {
/*  16 */     this.i = debug1;
/*  17 */     this.j = debug2;
/*  18 */     this.k = debug3;
/*  19 */     this.r = debug4;
/*     */   }
/*     */   
/*     */   public Quaternion(Vector3f debug1, float debug2, boolean debug3) {
/*  23 */     if (debug3) {
/*  24 */       debug2 *= 0.017453292F;
/*     */     }
/*  26 */     float debug4 = sin(debug2 / 2.0F);
/*  27 */     this.i = debug1.x() * debug4;
/*  28 */     this.j = debug1.y() * debug4;
/*  29 */     this.k = debug1.z() * debug4;
/*  30 */     this.r = cos(debug2 / 2.0F);
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
/*     */   public Quaternion(Quaternion debug1) {
/*  58 */     this.i = debug1.i;
/*  59 */     this.j = debug1.j;
/*  60 */     this.k = debug1.k;
/*  61 */     this.r = debug1.r;
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
/*     */   public boolean equals(Object debug1) {
/* 144 */     if (this == debug1) {
/* 145 */       return true;
/*     */     }
/* 147 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 148 */       return false;
/*     */     }
/* 150 */     Quaternion debug2 = (Quaternion)debug1;
/* 151 */     if (Float.compare(debug2.i, this.i) != 0) {
/* 152 */       return false;
/*     */     }
/* 154 */     if (Float.compare(debug2.j, this.j) != 0) {
/* 155 */       return false;
/*     */     }
/* 157 */     if (Float.compare(debug2.k, this.k) != 0) {
/* 158 */       return false;
/*     */     }
/* 160 */     return (Float.compare(debug2.r, this.r) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 165 */     int debug1 = Float.floatToIntBits(this.i);
/* 166 */     debug1 = 31 * debug1 + Float.floatToIntBits(this.j);
/* 167 */     debug1 = 31 * debug1 + Float.floatToIntBits(this.k);
/* 168 */     debug1 = 31 * debug1 + Float.floatToIntBits(this.r);
/* 169 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     StringBuilder debug1 = new StringBuilder();
/* 175 */     debug1.append("Quaternion[").append(r()).append(" + ");
/* 176 */     debug1.append(i()).append("i + ");
/* 177 */     debug1.append(j()).append("j + ");
/* 178 */     debug1.append(k()).append("k]");
/* 179 */     return debug1.toString();
/*     */   }
/*     */   
/*     */   public float i() {
/* 183 */     return this.i;
/*     */   }
/*     */   
/*     */   public float j() {
/* 187 */     return this.j;
/*     */   }
/*     */   
/*     */   public float k() {
/* 191 */     return this.k;
/*     */   }
/*     */   
/*     */   public float r() {
/* 195 */     return this.r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mul(Quaternion debug1) {
/* 202 */     float debug2 = i();
/* 203 */     float debug3 = j();
/* 204 */     float debug4 = k();
/* 205 */     float debug5 = r();
/*     */     
/* 207 */     float debug6 = debug1.i();
/* 208 */     float debug7 = debug1.j();
/* 209 */     float debug8 = debug1.k();
/* 210 */     float debug9 = debug1.r();
/*     */     
/* 212 */     this.i = debug5 * debug6 + debug2 * debug9 + debug3 * debug8 - debug4 * debug7;
/* 213 */     this.j = debug5 * debug7 - debug2 * debug8 + debug3 * debug9 + debug4 * debug6;
/* 214 */     this.k = debug5 * debug8 + debug2 * debug7 - debug3 * debug6 + debug4 * debug9;
/* 215 */     this.r = debug5 * debug9 - debug2 * debug6 - debug3 * debug7 - debug4 * debug8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void conj() {
/* 226 */     this.i = -this.i;
/* 227 */     this.j = -this.j;
/* 228 */     this.k = -this.k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float cos(float debug0) {
/* 239 */     return (float)Math.cos(debug0);
/*     */   }
/*     */   
/*     */   private static float sin(float debug0) {
/* 243 */     return (float)Math.sin(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\Quaternion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */