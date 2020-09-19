/*    */ package it.unimi.dsi.fastutil.chars;
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
/*    */ public final class CharComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements CharComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(char a, char b) {
/* 30 */       return Character.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return CharComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final CharComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements CharComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(char a, char b) {
/* 43 */       return -Character.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return CharComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final CharComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements CharComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(CharComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final CharComparator comparator;
/*    */     public final int compare(char a, char b) {
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
/*    */   public static CharComparator oppositeComparator(CharComparator c) {
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
/*    */   public static CharComparator asCharComparator(final Comparator<? super Character> c) {
/* 81 */     if (c == null || c instanceof CharComparator)
/* 82 */       return (CharComparator)c; 
/* 83 */     return new CharComparator()
/*    */       {
/*    */         public int compare(char x, char y) {
/* 86 */           return c.compare(Character.valueOf(x), Character.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Character x, Character y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */