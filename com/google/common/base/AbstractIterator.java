/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ @GwtCompatible
/*    */ abstract class AbstractIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   private T next;
/* 31 */   private State state = State.NOT_READY;
/*    */   
/*    */   protected abstract T computeNext();
/*    */   
/*    */   private enum State {
/* 36 */     READY,
/* 37 */     NOT_READY,
/* 38 */     DONE,
/* 39 */     FAILED;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   @CanIgnoreReturnValue
/*    */   protected final T endOfData() {
/* 49 */     this.state = State.DONE;
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 55 */     Preconditions.checkState((this.state != State.FAILED));
/* 56 */     switch (this.state) {
/*    */       case READY:
/* 58 */         return true;
/*    */       case DONE:
/* 60 */         return false;
/*    */     } 
/*    */     
/* 63 */     return tryToComputeNext();
/*    */   }
/*    */   
/*    */   private boolean tryToComputeNext() {
/* 67 */     this.state = State.FAILED;
/* 68 */     this.next = computeNext();
/* 69 */     if (this.state != State.DONE) {
/* 70 */       this.state = State.READY;
/* 71 */       return true;
/*    */     } 
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 78 */     if (!hasNext()) {
/* 79 */       throw new NoSuchElementException();
/*    */     }
/* 81 */     this.state = State.NOT_READY;
/* 82 */     T result = this.next;
/* 83 */     this.next = null;
/* 84 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void remove() {
/* 89 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */