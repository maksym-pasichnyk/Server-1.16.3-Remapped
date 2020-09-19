/*    */ package it.unimi.dsi.fastutil.bytes;
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
/*    */ public final class ByteComparators
/*    */ {
/*    */   protected static class NaturalImplicitComparator
/*    */     implements ByteComparator, Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(byte a, byte b) {
/* 30 */       return Byte.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 33 */       return ByteComparators.NATURAL_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 37 */   public static final ByteComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
/*    */   
/*    */   protected static class OppositeImplicitComparator implements ByteComparator, Serializable {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public final int compare(byte a, byte b) {
/* 43 */       return -Byte.compare(a, b);
/*    */     }
/*    */     private Object readResolve() {
/* 46 */       return ByteComparators.OPPOSITE_COMPARATOR;
/*    */     }
/*    */   }
/*    */   
/* 50 */   public static final ByteComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
/*    */   
/*    */   protected static class OppositeComparator implements ByteComparator, Serializable { private static final long serialVersionUID = 1L;
/*    */     
/*    */     protected OppositeComparator(ByteComparator c) {
/* 55 */       this.comparator = c;
/*    */     }
/*    */     private final ByteComparator comparator;
/*    */     public final int compare(byte a, byte b) {
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
/*    */   public static ByteComparator oppositeComparator(ByteComparator c) {
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
/*    */   public static ByteComparator asByteComparator(final Comparator<? super Byte> c) {
/* 81 */     if (c == null || c instanceof ByteComparator)
/* 82 */       return (ByteComparator)c; 
/* 83 */     return new ByteComparator()
/*    */       {
/*    */         public int compare(byte x, byte y) {
/* 86 */           return c.compare(Byte.valueOf(x), Byte.valueOf(y));
/*    */         }
/*    */ 
/*    */         
/*    */         public int compare(Byte x, Byte y) {
/* 91 */           return c.compare(x, y);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\ByteComparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */