/*    */ package it.unimi.dsi.fastutil.ints;
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
/*    */ public final class IntComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements IntComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(int a, int b) {
/* 30 */       return Integer.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return IntComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final IntComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements IntComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(int a, int b) {
/* 43 */       return -Integer.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return IntComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final IntComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements IntComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(IntComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final IntComparator comparator;
/*    */     public final int compare(int a, int b) {
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
/*    */   public static IntComparator oppositeComparator(IntComparator c) {
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
/*    */   public static IntComparator asIntComparator(final Comparator<? super Integer> c) {
/* 81 */     if (c == null || c instanceof IntComparator)
/* 82 */       return (IntComparator)c; 
/* 83 */     return new IntComparator()
/*    */       {
/*    */         public int compare(int x, int y) {
/* 86 */           return c.compare(Integer.valueOf(x), Integer.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Integer x, Integer y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */