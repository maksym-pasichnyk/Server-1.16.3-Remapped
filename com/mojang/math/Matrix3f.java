/*     */ package com.mojang.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Matrix3f
/*     */ {
/*  15 */   private static final float G = 3.0F + 2.0F * (float)Math.sqrt(2.0D);
/*  16 */   private static final float CS = (float)Math.cos(0.39269908169872414D);
/*  17 */   private static final float SS = (float)Math.sin(0.39269908169872414D);
/*  18 */   private static final float SQ2 = 1.0F / (float)Math.sqrt(2.0D);
/*     */   
/*     */   protected float m00;
/*     */   
/*     */   protected float m01;
/*     */   
/*     */   protected float m02;
/*     */   
/*     */   protected float m10;
/*     */   
/*     */   protected float m11;
/*     */   protected float m12;
/*     */   protected float m20;
/*     */   protected float m21;
/*     */   protected float m22;
/*     */   
/*     */   public Matrix3f() {}
/*     */   
/*     */   public Matrix3f(Quaternion debug1) {
/*  37 */     float debug2 = debug1.i();
/*  38 */     float debug3 = debug1.j();
/*  39 */     float debug4 = debug1.k();
/*  40 */     float debug5 = debug1.r();
/*     */     
/*  42 */     float debug6 = 2.0F * debug2 * debug2;
/*  43 */     float debug7 = 2.0F * debug3 * debug3;
/*  44 */     float debug8 = 2.0F * debug4 * debug4;
/*     */     
/*  46 */     this.m00 = 1.0F - debug7 - debug8;
/*  47 */     this.m11 = 1.0F - debug8 - debug6;
/*  48 */     this.m22 = 1.0F - debug6 - debug7;
/*     */     
/*  50 */     float debug9 = debug2 * debug3;
/*  51 */     float debug10 = debug3 * debug4;
/*  52 */     float debug11 = debug4 * debug2;
/*     */     
/*  54 */     float debug12 = debug2 * debug5;
/*  55 */     float debug13 = debug3 * debug5;
/*  56 */     float debug14 = debug4 * debug5;
/*     */     
/*  58 */     this.m10 = 2.0F * (debug9 + debug14);
/*  59 */     this.m01 = 2.0F * (debug9 - debug14);
/*     */     
/*  61 */     this.m20 = 2.0F * (debug11 - debug13);
/*  62 */     this.m02 = 2.0F * (debug11 + debug13);
/*     */     
/*  64 */     this.m21 = 2.0F * (debug10 + debug12);
/*  65 */     this.m12 = 2.0F * (debug10 - debug12);
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
/*     */   public Matrix3f(Matrix4f debug1) {
/*  77 */     this.m00 = debug1.m00;
/*  78 */     this.m01 = debug1.m01;
/*  79 */     this.m02 = debug1.m02;
/*     */     
/*  81 */     this.m10 = debug1.m10;
/*  82 */     this.m11 = debug1.m11;
/*  83 */     this.m12 = debug1.m12;
/*     */     
/*  85 */     this.m20 = debug1.m20;
/*  86 */     this.m21 = debug1.m21;
/*  87 */     this.m22 = debug1.m22;
/*     */   }
/*     */   
/*     */   public Matrix3f(Matrix3f debug1) {
/*  91 */     this.m00 = debug1.m00;
/*  92 */     this.m01 = debug1.m01;
/*  93 */     this.m02 = debug1.m02;
/*     */     
/*  95 */     this.m10 = debug1.m10;
/*  96 */     this.m11 = debug1.m11;
/*  97 */     this.m12 = debug1.m12;
/*     */     
/*  99 */     this.m20 = debug1.m20;
/* 100 */     this.m21 = debug1.m21;
/* 101 */     this.m22 = debug1.m22;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 388 */     if (this == debug1) {
/* 389 */       return true;
/*     */     }
/* 391 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 392 */       return false;
/*     */     }
/*     */     
/* 395 */     Matrix3f debug2 = (Matrix3f)debug1;
/* 396 */     return (Float.compare(debug2.m00, this.m00) == 0 && Float.compare(debug2.m01, this.m01) == 0 && Float.compare(debug2.m02, this.m02) == 0 && 
/* 397 */       Float.compare(debug2.m10, this.m10) == 0 && Float.compare(debug2.m11, this.m11) == 0 && Float.compare(debug2.m12, this.m12) == 0 && 
/* 398 */       Float.compare(debug2.m20, this.m20) == 0 && Float.compare(debug2.m21, this.m21) == 0 && Float.compare(debug2.m22, this.m22) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 403 */     int debug1 = (this.m00 != 0.0F) ? Float.floatToIntBits(this.m00) : 0;
/* 404 */     debug1 = 31 * debug1 + ((this.m01 != 0.0F) ? Float.floatToIntBits(this.m01) : 0);
/* 405 */     debug1 = 31 * debug1 + ((this.m02 != 0.0F) ? Float.floatToIntBits(this.m02) : 0);
/* 406 */     debug1 = 31 * debug1 + ((this.m10 != 0.0F) ? Float.floatToIntBits(this.m10) : 0);
/* 407 */     debug1 = 31 * debug1 + ((this.m11 != 0.0F) ? Float.floatToIntBits(this.m11) : 0);
/* 408 */     debug1 = 31 * debug1 + ((this.m12 != 0.0F) ? Float.floatToIntBits(this.m12) : 0);
/* 409 */     debug1 = 31 * debug1 + ((this.m20 != 0.0F) ? Float.floatToIntBits(this.m20) : 0);
/* 410 */     debug1 = 31 * debug1 + ((this.m21 != 0.0F) ? Float.floatToIntBits(this.m21) : 0);
/* 411 */     debug1 = 31 * debug1 + ((this.m22 != 0.0F) ? Float.floatToIntBits(this.m22) : 0);
/* 412 */     return debug1;
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
/*     */   public String toString() {
/* 471 */     StringBuilder debug1 = new StringBuilder();
/* 472 */     debug1.append("Matrix3f:\n");
/* 473 */     debug1.append(this.m00);
/* 474 */     debug1.append(" ");
/* 475 */     debug1.append(this.m01);
/* 476 */     debug1.append(" ");
/* 477 */     debug1.append(this.m02);
/* 478 */     debug1.append("\n");
/*     */     
/* 480 */     debug1.append(this.m10);
/* 481 */     debug1.append(" ");
/* 482 */     debug1.append(this.m11);
/* 483 */     debug1.append(" ");
/* 484 */     debug1.append(this.m12);
/* 485 */     debug1.append("\n");
/*     */     
/* 487 */     debug1.append(this.m20);
/* 488 */     debug1.append(" ");
/* 489 */     debug1.append(this.m21);
/* 490 */     debug1.append(" ");
/* 491 */     debug1.append(this.m22);
/* 492 */     debug1.append("\n");
/* 493 */     return debug1.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int debug1, int debug2, float debug3) {
/* 600 */     if (debug1 == 0) {
/* 601 */       if (debug2 == 0) {
/* 602 */         this.m00 = debug3;
/* 603 */       } else if (debug2 == 1) {
/* 604 */         this.m01 = debug3;
/*     */       } else {
/* 606 */         this.m02 = debug3;
/*     */       } 
/* 608 */     } else if (debug1 == 1) {
/* 609 */       if (debug2 == 0) {
/* 610 */         this.m10 = debug3;
/* 611 */       } else if (debug2 == 1) {
/* 612 */         this.m11 = debug3;
/*     */       } else {
/* 614 */         this.m12 = debug3;
/*     */       }
/*     */     
/* 617 */     } else if (debug2 == 0) {
/* 618 */       this.m20 = debug3;
/* 619 */     } else if (debug2 == 1) {
/* 620 */       this.m21 = debug3;
/*     */     } else {
/* 622 */       this.m22 = debug3;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mul(Matrix3f debug1) {
/* 628 */     float debug2 = this.m00 * debug1.m00 + this.m01 * debug1.m10 + this.m02 * debug1.m20;
/* 629 */     float debug3 = this.m00 * debug1.m01 + this.m01 * debug1.m11 + this.m02 * debug1.m21;
/* 630 */     float debug4 = this.m00 * debug1.m02 + this.m01 * debug1.m12 + this.m02 * debug1.m22;
/* 631 */     float debug5 = this.m10 * debug1.m00 + this.m11 * debug1.m10 + this.m12 * debug1.m20;
/* 632 */     float debug6 = this.m10 * debug1.m01 + this.m11 * debug1.m11 + this.m12 * debug1.m21;
/* 633 */     float debug7 = this.m10 * debug1.m02 + this.m11 * debug1.m12 + this.m12 * debug1.m22;
/* 634 */     float debug8 = this.m20 * debug1.m00 + this.m21 * debug1.m10 + this.m22 * debug1.m20;
/* 635 */     float debug9 = this.m20 * debug1.m01 + this.m21 * debug1.m11 + this.m22 * debug1.m21;
/* 636 */     float debug10 = this.m20 * debug1.m02 + this.m21 * debug1.m12 + this.m22 * debug1.m22;
/*     */     
/* 638 */     this.m00 = debug2;
/* 639 */     this.m01 = debug3;
/* 640 */     this.m02 = debug4;
/* 641 */     this.m10 = debug5;
/* 642 */     this.m11 = debug6;
/* 643 */     this.m12 = debug7;
/* 644 */     this.m20 = debug8;
/* 645 */     this.m21 = debug9;
/* 646 */     this.m22 = debug10;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\Matrix3f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */