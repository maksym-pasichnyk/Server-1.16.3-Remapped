/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
/*    */ import java.util.ListIterator;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   public void add(E element) {
/* 51 */     delegate().add(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPrevious() {
/* 56 */     return delegate().hasPrevious();
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextIndex() {
/* 61 */     return delegate().nextIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public E previous() {
/* 67 */     return delegate().previous();
/*    */   }
/*    */ 
/*    */   
/*    */   public int previousIndex() {
/* 72 */     return delegate().previousIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(E element) {
/* 77 */     delegate().set(element);
/*    */   }
/*    */   
/*    */   protected abstract ListIterator<E> delegate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */