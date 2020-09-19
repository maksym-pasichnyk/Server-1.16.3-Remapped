/*    */ package it.unimi.dsi.fastutil.floats;
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
/*    */ public interface FloatComparator
/*    */   extends Comparator<Float>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Float ok1, Float ok2) {
/* 51 */     return compare(ok1.floatValue(), ok2.floatValue());
/*    */   }
/*    */   
/*    */   int compare(float paramFloat1, float paramFloat2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */