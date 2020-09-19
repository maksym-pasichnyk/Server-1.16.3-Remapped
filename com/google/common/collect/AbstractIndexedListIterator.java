/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractIndexedListIterator<E>
/*     */   extends UnmodifiableListIterator<E>
/*     */ {
/*     */   private final int size;
/*     */   private int position;
/*     */   
/*     */   protected abstract E get(int paramInt);
/*     */   
/*     */   protected AbstractIndexedListIterator(int size) {
/*  52 */     this(size, 0);
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
/*     */   protected AbstractIndexedListIterator(int size, int position) {
/*  67 */     Preconditions.checkPositionIndex(position, size);
/*  68 */     this.size = size;
/*  69 */     this.position = position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNext() {
/*  74 */     return (this.position < this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public final E next() {
/*  79 */     if (!hasNext()) {
/*  80 */       throw new NoSuchElementException();
/*     */     }
/*  82 */     return get(this.position++);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int nextIndex() {
/*  87 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasPrevious() {
/*  92 */     return (this.position > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final E previous() {
/*  97 */     if (!hasPrevious()) {
/*  98 */       throw new NoSuchElementException();
/*     */     }
/* 100 */     return get(--this.position);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int previousIndex() {
/* 105 */     return this.position - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractIndexedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */