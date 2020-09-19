/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import org.apache.logging.log4j.util.Strings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Integers
/*    */ {
/*    */   private static final int BITS_PER_INT = 32;
/*    */   
/*    */   public static int parseInt(String s, int defaultValue) {
/* 40 */     return Strings.isEmpty(s) ? defaultValue : Integer.parseInt(s);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int parseInt(String s) {
/* 51 */     return parseInt(s, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int ceilingNextPowerOfTwo(int x) {
/* 63 */     return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Integers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */