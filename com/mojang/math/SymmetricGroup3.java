/*    */ package com.mojang.math;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.Util;
/*    */ 
/*    */ public enum SymmetricGroup3
/*    */ {
/*  8 */   P123(0, 1, 2),
/*  9 */   P213(1, 0, 2),
/* 10 */   P132(0, 2, 1),
/* 11 */   P231(1, 2, 0),
/* 12 */   P312(2, 0, 1),
/* 13 */   P321(2, 1, 0);
/*    */   
/*    */   private final int[] permutation;
/*    */   private final Matrix3f transformation;
/*    */   private static final SymmetricGroup3[][] cayleyTable;
/*    */   
/*    */   SymmetricGroup3(int debug3, int debug4, int debug5) {
/* 20 */     this.permutation = new int[] { debug3, debug4, debug5 };
/* 21 */     this.transformation = new Matrix3f();
/* 22 */     this.transformation.set(0, permutation(0), 1.0F);
/* 23 */     this.transformation.set(1, permutation(1), 1.0F);
/* 24 */     this.transformation.set(2, permutation(2), 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 29 */     cayleyTable = (SymmetricGroup3[][])Util.make(new SymmetricGroup3[(values()).length][(values()).length], debug0 -> {
/*    */           for (SymmetricGroup3 debug4 : values()) {
/*    */             for (SymmetricGroup3 debug8 : values()) {
/*    */               int[] debug9 = new int[3];
/*    */               for (int i = 0; i < 3; i++) {
/*    */                 debug9[i] = debug4.permutation[debug8.permutation[i]];
/*    */               }
/*    */               SymmetricGroup3 debug10 = Arrays.<SymmetricGroup3>stream(values()).filter(()).findFirst().get();
/*    */               debug0[debug4.ordinal()][debug8.ordinal()] = debug10;
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SymmetricGroup3 compose(SymmetricGroup3 debug1) {
/* 46 */     return cayleyTable[ordinal()][debug1.ordinal()];
/*    */   }
/*    */   
/*    */   public int permutation(int debug1) {
/* 50 */     return this.permutation[debug1];
/*    */   }
/*    */   
/*    */   public Matrix3f transformation() {
/* 54 */     return this.transformation;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\SymmetricGroup3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */