/*    */ package it.unimi.dsi.fastutil.doubles;
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
/*    */ public final class DoubleComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements DoubleComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(double a, double b) {
/* 30 */       return Double.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return DoubleComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final DoubleComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements DoubleComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(double a, double b) {
/* 43 */       return -Double.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return DoubleComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final DoubleComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements DoubleComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(DoubleComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final DoubleComparator comparator;
/*    */     public final int compare(double a, double b) {
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
/*    */   public static DoubleComparator oppositeComparator(DoubleComparator c) {
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
/*    */   public static DoubleComparator asDoubleComparator(final Comparator<? super Double> c) {
/* 81 */     if (c == null || c instanceof DoubleComparator)
/* 82 */       return (DoubleComparator)c; 
/* 83 */     return new DoubleComparator()
/*    */       {
/*    */         public int compare(double x, double y) {
/* 86 */           return c.compare(Double.valueOf(x), Double.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Double x, Double y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */