/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class ForwardingSortedSet<E>
/*     */   extends ForwardingSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   public Comparator<? super E> comparator() {
/*  69 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/*  74 */     return delegate().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/*  79 */     return delegate().headSet(toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/*  84 */     return delegate().last();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/*  89 */     return delegate().subSet(fromElement, toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/*  94 */     return delegate().tailSet(fromElement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object o1, Object o2) {
/* 100 */     Comparator<? super E> comparator = comparator();
/* 101 */     return (comparator == null) ? ((Comparable<Object>)o1)
/* 102 */       .compareTo(o2) : comparator
/* 103 */       .compare((E)o1, (E)o2);
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
/*     */   @Beta
/*     */   protected boolean standardContains(@Nullable Object object) {
/*     */     try {
/* 119 */       SortedSet<Object> self = (SortedSet)this;
/* 120 */       Object ceiling = self.tailSet(object).first();
/* 121 */       return (unsafeCompare(ceiling, object) == 0);
/* 122 */     } catch (ClassCastException e) {
/* 123 */       return false;
/* 124 */     } catch (NoSuchElementException e) {
/* 125 */       return false;
/* 126 */     } catch (NullPointerException e) {
/* 127 */       return false;
/*     */     } 
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
/*     */   @Beta
/*     */   protected boolean standardRemove(@Nullable Object object) {
/*     */     try {
/* 144 */       SortedSet<Object> self = (SortedSet)this;
/* 145 */       Iterator<Object> iterator = self.tailSet(object).iterator();
/* 146 */       if (iterator.hasNext()) {
/* 147 */         Object ceiling = iterator.next();
/* 148 */         if (unsafeCompare(ceiling, object) == 0) {
/* 149 */           iterator.remove();
/* 150 */           return true;
/*     */         } 
/*     */       } 
/* 153 */     } catch (ClassCastException e) {
/* 154 */       return false;
/* 155 */     } catch (NullPointerException e) {
/* 156 */       return false;
/*     */     } 
/* 158 */     return false;
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
/*     */   @Beta
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
/* 171 */     return tailSet(fromElement).headSet(toElement);
/*     */   }
/*     */   
/*     */   protected abstract SortedSet<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */