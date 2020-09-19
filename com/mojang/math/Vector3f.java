/*     */ package com.mojang.math;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public final class Vector3f
/*     */ {
/*   8 */   public static Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
/*   9 */   public static Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
/*  10 */   public static Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
/*  11 */   public static Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
/*  12 */   public static Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
/*  13 */   public static Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);
/*     */   
/*     */   private float x;
/*     */   
/*     */   private float y;
/*     */   private float z;
/*     */   
/*     */   public Vector3f() {}
/*     */   
/*     */   public Vector3f(float debug1, float debug2, float debug3) {
/*  23 */     this.x = debug1;
/*  24 */     this.y = debug2;
/*  25 */     this.z = debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3f(Vec3 debug1) {
/*  33 */     this((float)debug1.x, (float)debug1.y, (float)debug1.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  38 */     if (this == debug1) {
/*  39 */       return true;
/*     */     }
/*  41 */     if (debug1 == null || getClass() != debug1.getClass()) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     Vector3f debug2 = (Vector3f)debug1;
/*  46 */     if (Float.compare(debug2.x, this.x) != 0) {
/*  47 */       return false;
/*     */     }
/*  49 */     if (Float.compare(debug2.y, this.y) != 0) {
/*  50 */       return false;
/*     */     }
/*  52 */     return (Float.compare(debug2.z, this.z) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  57 */     int debug1 = Float.floatToIntBits(this.x);
/*  58 */     debug1 = 31 * debug1 + Float.floatToIntBits(this.y);
/*  59 */     debug1 = 31 * debug1 + Float.floatToIntBits(this.z);
/*  60 */     return debug1;
/*     */   }
/*     */   
/*     */   public float x() {
/*  64 */     return this.x;
/*     */   }
/*     */   
/*     */   public float y() {
/*  68 */     return this.y;
/*     */   }
/*     */   
/*     */   public float z() {
/*  72 */     return this.z;
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
/*     */   public void set(float debug1, float debug2, float debug3) {
/* 100 */     this.x = debug1;
/* 101 */     this.y = debug2;
/* 102 */     this.z = debug3;
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
/*     */   public void transform(Quaternion debug1) {
/* 180 */     Quaternion debug2 = new Quaternion(debug1);
/* 181 */     debug2.mul(new Quaternion(x(), y(), z(), 0.0F));
/* 182 */     Quaternion debug3 = new Quaternion(debug1);
/*     */     
/* 184 */     debug3.conj();
/* 185 */     debug2.mul(debug3);
/* 186 */     set(debug2.i(), debug2.j(), debug2.k());
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
/*     */   public String toString() {
/* 216 */     return "[" + this.x + ", " + this.y + ", " + this.z + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\Vector3f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */