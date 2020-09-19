/*    */ package it.unimi.dsi.fastutil.shorts;
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
/*    */ public final class ShortComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements ShortComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(short a, short b) {
/* 30 */       return Short.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return ShortComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final ShortComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements ShortComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(short a, short b) {
/* 43 */       return -Short.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return ShortComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final ShortComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements ShortComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(ShortComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final ShortComparator comparator;
/*    */     public final int compare(short a, short b) {
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
/*    */   public static ShortComparator oppositeComparator(ShortComparator c) {
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
/*    */   public static ShortComparator asShortComparator(final Comparator<? super Short> c) {
/* 81 */     if (c == null || c instanceof ShortComparator)
/* 82 */       return (ShortComparator)c; 
/* 83 */     return new ShortComparator()
/*    */       {
/*    */         public int compare(short x, short y) {
/* 86 */           return c.compare(Short.valueOf(x), Short.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Short x, Short y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */