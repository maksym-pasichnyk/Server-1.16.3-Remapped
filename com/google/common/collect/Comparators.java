/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class Comparators
/*     */ {
/*     */   public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
/*  62 */     return new LexicographicalOrdering<>((Comparator<? super S>)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  71 */     Preconditions.checkNotNull(comparator);
/*  72 */     Iterator<? extends T> it = iterable.iterator();
/*  73 */     if (it.hasNext()) {
/*  74 */       T prev = it.next();
/*  75 */       while (it.hasNext()) {
/*  76 */         T next = it.next();
/*  77 */         if (comparator.compare(prev, next) > 0) {
/*  78 */           return false;
/*     */         }
/*  80 */         prev = next;
/*     */       } 
/*     */     } 
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  93 */     Preconditions.checkNotNull(comparator);
/*  94 */     Iterator<? extends T> it = iterable.iterator();
/*  95 */     if (it.hasNext()) {
/*  96 */       T prev = it.next();
/*  97 */       while (it.hasNext()) {
/*  98 */         T next = it.next();
/*  99 */         if (comparator.compare(prev, next) >= 0) {
/* 100 */           return false;
/*     */         }
/* 102 */         prev = next;
/*     */       } 
/*     */     } 
/* 105 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Comparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */