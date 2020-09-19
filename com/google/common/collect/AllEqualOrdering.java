/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class AllEqualOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 31 */   static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(@Nullable Object left, @Nullable Object right) {
/* 35 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> List<E> sortedCopy(Iterable<E> iterable) {
/* 40 */     return Lists.newArrayList(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
/* 45 */     return ImmutableList.copyOf(iterable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> Ordering<S> reverse() {
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 55 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "Ordering.allEqual()";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AllEqualOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */