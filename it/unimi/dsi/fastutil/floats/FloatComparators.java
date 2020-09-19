/*    */ package it.unimi.dsi.fastutil.floats;
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
/*    */ public final class FloatComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements FloatComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(float a, float b) {
/* 30 */       return Float.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return FloatComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final FloatComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements FloatComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(float a, float b) {
/* 43 */       return -Float.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return FloatComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final FloatComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements FloatComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(FloatComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final FloatComparator comparator;
/*    */     public final int compare(float a, float b) {
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
/*    */   public static FloatComparator oppositeComparator(FloatComparator c) {
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
/*    */   public static FloatComparator asFloatComparator(final Comparator<? super Float> c) {
/* 81 */     if (c == null || c instanceof FloatComparator)
/* 82 */       return (FloatComparator)c; 
/* 83 */     return new FloatComparator()
/*    */       {
/*    */         public int compare(float x, float y) {
/* 86 */           return c.compare(Float.valueOf(x), Float.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Float x, Float y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */