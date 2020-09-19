/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final transient E element;
/*     */   @LazyInit
/*     */   private transient int cachedHashCode;
/*     */   
/*     */   SingletonImmutableSet(E element) {
/*  47 */     this.element = (E)Preconditions.checkNotNull(element);
/*     */   }
/*     */ 
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode) {
/*  52 */     this.element = element;
/*  53 */     this.cachedHashCode = hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  58 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object target) {
/*  63 */     return this.element.equals(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  68 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/*  73 */     return ImmutableList.of(this.element);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  83 */     dst[offset] = this.element;
/*  84 */     return offset + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  90 */     int code = this.cachedHashCode;
/*  91 */     if (code == 0) {
/*  92 */       this.cachedHashCode = code = this.element.hashCode();
/*     */     }
/*  94 */     return code;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/*  99 */     return (this.cachedHashCode != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return '[' + this.element.toString() + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */