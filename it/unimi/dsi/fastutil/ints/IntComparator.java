/*    */ package it.unimi.dsi.fastutil.ints;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ @FunctionalInterface
/*    */ public interface IntComparator
/*    */   extends Comparator<Integer>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Integer ok1, Integer ok2) {
/* 51 */     return compare(ok1.intValue(), ok2.intValue());
/*    */   }
/*    */   
/*    */   int compare(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */