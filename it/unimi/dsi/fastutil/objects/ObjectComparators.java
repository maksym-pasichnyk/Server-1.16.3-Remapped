/*    */ package it.unimi.dsi.fastutil.objects;
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
/*    */ 
/*    */ public final class ObjectComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements Comparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(Object a, Object b) {
/* 31 */       return ((Comparable<Object>)a).compareTo(b);
/*    */     }
/*    */     private Object readResolve() {
/* 34 */       return ObjectComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 38 */   public static final Comparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator
/*    */     implements Comparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(Object a, Object b) {
/* 45 */       return ((Comparable<Object>)b).compareTo(a);
/*    */     }
/*    */     private Object readResolve() {
/* 48 */       return ObjectComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 52 */   public static final Comparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator<K> implements Comparator<K>, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(Comparator<K> c) {
/* 57 */       this.comparator = c;
/*    */     }
/*    */     private final Comparator<K> comparator;
/*    */     public final int compare(K a, K b) {
/* 61 */       return this.comparator.compare(b, a);
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <K> Comparator<K> oppositeComparator(Comparator<K> c) {
/* 72 */     return new OppositeComparator<>(c);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */