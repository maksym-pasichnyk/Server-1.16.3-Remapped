/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import javax.annotation.Nullable;
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
/*     */ final class RegularImmutableSet<E>
/*     */   extends ImmutableSet.Indexed<E>
/*     */ {
/*  33 */   static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
/*     */   
/*     */   private final transient Object[] elements;
/*     */   
/*     */   @VisibleForTesting
/*     */   final transient Object[] table;
/*     */   
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   
/*     */   RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
/*  44 */     this.elements = elements;
/*  45 */     this.table = table;
/*  46 */     this.mask = mask;
/*  47 */     this.hashCode = hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object target) {
/*  52 */     Object[] table = this.table;
/*  53 */     if (target == null || table == null) {
/*  54 */       return false;
/*     */     }
/*  56 */     for (int i = Hashing.smearedHash(target);; i++) {
/*  57 */       i &= this.mask;
/*  58 */       Object candidate = table[i];
/*  59 */       if (candidate == null)
/*  60 */         return false; 
/*  61 */       if (candidate.equals(target)) {
/*  62 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  69 */     return this.elements.length;
/*     */   }
/*     */   
/*     */   E get(int i) {
/*  73 */     return (E)this.elements[i];
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  78 */     return Spliterators.spliterator(this.elements, 1297);
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  83 */     System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
/*  84 */     return offset + this.elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/*  89 */     return (this.table == null) ? ImmutableList.<E>of() : new RegularImmutableAsList<>(this, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 104 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */