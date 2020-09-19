/*    */ package it.unimi.dsi.fastutil.longs;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class LongComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements LongComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(long a, long b) {
/* 30 */       return Long.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return LongComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final LongComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements LongComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(long a, long b) {
/* 43 */       return -Long.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return LongComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final LongComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements LongComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(LongComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final LongComparator comparator;
/*    */     public final int compare(long a, long b) {
/* 59 */       return this.comparator.compare(b, a);
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LongComparator oppositeComparator(LongComparator c) {
/* 70 */     return new OppositeComparator(c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LongComparator asLongComparator(final Comparator<? super Long> c) {
/* 81 */     if (c == null || c instanceof LongComparator)
/* 82 */       return (LongComparator)c; 
/* 83 */     return new LongComparator()
/*    */       {
/*    */         public int compare(long x, long y) {
/* 86 */           return c.compare(Long.valueOf(x), Long.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Long x, Long y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\longs\LongComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */