/*    */ package it.unimi.dsi.fastutil.longs;
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
/*    */ public interface LongComparator
/*    */   extends Comparator<Long>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Long ok1, Long ok2) {
/* 51 */     return compare(ok1.longValue(), ok2.longValue());
/*    */   }
/*    */   
/*    */   int compare(long paramLong1, long paramLong2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\longs\LongComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */