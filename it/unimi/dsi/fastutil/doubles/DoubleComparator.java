/*    */ package it.unimi.dsi.fastutil.doubles;
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
/*    */ public interface DoubleComparator
/*    */   extends Comparator<Double>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Double ok1, Double ok2) {
/* 51 */     return compare(ok1.doubleValue(), ok2.doubleValue());
/*    */   }
/*    */   
/*    */   int compare(double paramDouble1, double paramDouble2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */