/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class CompoundOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final ImmutableList<Comparator<? super T>> comparators;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
/* 29 */     this.comparators = ImmutableList.of(primary, secondary);
/*    */   }
/*    */   
/*    */   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
/* 33 */     this.comparators = ImmutableList.copyOf(comparators);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 39 */     int size = this.comparators.size();
/* 40 */     for (int i = 0; i < size; i++) {
/* 41 */       int result = ((Comparator<T>)this.comparators.get(i)).compare(left, right);
/* 42 */       if (result != 0) {
/* 43 */         return result;
/*    */       }
/*    */     } 
/* 46 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 51 */     if (object == this) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (object instanceof CompoundOrdering) {
/* 55 */       CompoundOrdering<?> that = (CompoundOrdering)object;
/* 56 */       return this.comparators.equals(that.comparators);
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return this.comparators.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "Ordering.compound(" + this.comparators + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\CompoundOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */