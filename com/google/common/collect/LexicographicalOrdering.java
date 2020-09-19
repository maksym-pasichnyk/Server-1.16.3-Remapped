/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class LexicographicalOrdering<T>
/*    */   extends Ordering<Iterable<T>>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<? super T> elementOrder;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   LexicographicalOrdering(Comparator<? super T> elementOrder) {
/* 34 */     this.elementOrder = elementOrder;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Iterable<T> leftIterable, Iterable<T> rightIterable) {
/* 39 */     Iterator<T> left = leftIterable.iterator();
/* 40 */     Iterator<T> right = rightIterable.iterator();
/* 41 */     while (left.hasNext()) {
/* 42 */       if (!right.hasNext()) {
/* 43 */         return 1;
/*    */       }
/* 45 */       int result = this.elementOrder.compare(left.next(), right.next());
/* 46 */       if (result != 0) {
/* 47 */         return result;
/*    */       }
/*    */     } 
/* 50 */     if (right.hasNext()) {
/* 51 */       return -1;
/*    */     }
/* 53 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 58 */     if (object == this) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (object instanceof LexicographicalOrdering) {
/* 62 */       LexicographicalOrdering<?> that = (LexicographicalOrdering)object;
/* 63 */       return this.elementOrder.equals(that.elementOrder);
/*    */     } 
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     return this.elementOrder.hashCode() ^ 0x7BB78CF5;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return this.elementOrder + ".lexicographical()";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\LexicographicalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */