/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinearCongruentialGenerator
/*    */ {
/*    */   public static long next(long debug0, long debug2) {
/*  8 */     debug0 *= debug0 * 6364136223846793005L + 1442695040888963407L;
/*  9 */     debug0 += debug2;
/* 10 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\LinearCongruentialGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */