/*    */ package it.unimi.dsi.fastutil.booleans;
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
/*    */ public interface BooleanComparator
/*    */   extends Comparator<Boolean>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Boolean ok1, Boolean ok2) {
/* 51 */     return compare(ok1.booleanValue(), ok2.booleanValue());
/*    */   }
/*    */   
/*    */   int compare(boolean paramBoolean1, boolean paramBoolean2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */