/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ abstract class MultitransformedIterator<F, T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   final Iterator<? extends F> backingIterator;
/* 35 */   private Iterator<? extends T> current = Iterators.emptyIterator();
/*    */   private Iterator<? extends T> removeFrom;
/*    */   
/*    */   MultitransformedIterator(Iterator<? extends F> backingIterator) {
/* 39 */     this.backingIterator = (Iterator<? extends F>)Preconditions.checkNotNull(backingIterator);
/*    */   }
/*    */ 
/*    */   
/*    */   abstract Iterator<? extends T> transform(F paramF);
/*    */   
/*    */   public boolean hasNext() {
/* 46 */     Preconditions.checkNotNull(this.current);
/* 47 */     if (this.current.hasNext()) {
/* 48 */       return true;
/*    */     }
/* 50 */     while (this.backingIterator.hasNext()) {
/*    */       
/* 52 */       Preconditions.checkNotNull(this.current = transform(this.backingIterator.next()));
/* 53 */       if (this.current.hasNext()) {
/* 54 */         return true;
/*    */       }
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public T next() {
/* 62 */     if (!hasNext()) {
/* 63 */       throw new NoSuchElementException();
/*    */     }
/* 65 */     this.removeFrom = this.current;
/* 66 */     return this.current.next();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 71 */     CollectPreconditions.checkRemove((this.removeFrom != null));
/* 72 */     this.removeFrom.remove();
/* 73 */     this.removeFrom = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\MultitransformedIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */