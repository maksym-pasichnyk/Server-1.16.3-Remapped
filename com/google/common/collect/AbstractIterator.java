/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class AbstractIterator<T>
/*     */   extends UnmodifiableIterator<T>
/*     */ {
/*  65 */   private State state = State.NOT_READY;
/*     */   private T next;
/*     */   
/*     */   protected abstract T computeNext();
/*     */   
/*     */   private enum State
/*     */   {
/*  72 */     READY,
/*     */ 
/*     */     
/*  75 */     NOT_READY,
/*     */ 
/*     */     
/*  78 */     DONE,
/*     */ 
/*     */     
/*  81 */     FAILED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   protected final T endOfData() {
/* 125 */     this.state = State.DONE;
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean hasNext() {
/* 132 */     Preconditions.checkState((this.state != State.FAILED));
/* 133 */     switch (this.state) {
/*     */       case DONE:
/* 135 */         return false;
/*     */       case READY:
/* 137 */         return true;
/*     */     } 
/*     */     
/* 140 */     return tryToComputeNext();
/*     */   }
/*     */   
/*     */   private boolean tryToComputeNext() {
/* 144 */     this.state = State.FAILED;
/* 145 */     this.next = computeNext();
/* 146 */     if (this.state != State.DONE) {
/* 147 */       this.state = State.READY;
/* 148 */       return true;
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final T next() {
/* 156 */     if (!hasNext()) {
/* 157 */       throw new NoSuchElementException();
/*     */     }
/* 159 */     this.state = State.NOT_READY;
/* 160 */     T result = this.next;
/* 161 */     this.next = null;
/* 162 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T peek() {
/* 173 */     if (!hasNext()) {
/* 174 */       throw new NoSuchElementException();
/*     */     }
/* 176 */     return this.next;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */