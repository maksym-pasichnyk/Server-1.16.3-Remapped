/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ final class ImmutableEnumSet<E extends Enum<E>>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   private final transient EnumSet<E> delegate;
/*     */   @LazyInit
/*     */   private transient int hashCode;
/*     */   
/*     */   static ImmutableSet asImmutable(EnumSet<?> set) {
/*  38 */     switch (set.size()) {
/*     */       case 0:
/*  40 */         return ImmutableSet.of();
/*     */       case 1:
/*  42 */         return ImmutableSet.of(Iterables.getOnlyElement(set));
/*     */     } 
/*  44 */     return new ImmutableEnumSet(set);
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
/*     */   private ImmutableEnumSet(EnumSet<E> delegate) {
/*  59 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  69 */     return Iterators.unmodifiableIterator(this.delegate.iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  74 */     return this.delegate.spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  79 */     this.delegate.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  84 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  89 */     return this.delegate.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  94 */     if (collection instanceof ImmutableEnumSet) {
/*  95 */       collection = ((ImmutableEnumSet)collection).delegate;
/*     */     }
/*  97 */     return this.delegate.containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 102 */     return this.delegate.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object<E> object) {
/* 107 */     if (object == this) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (object instanceof ImmutableEnumSet) {
/* 111 */       object = (Object<E>)((ImmutableEnumSet)object).delegate;
/*     */     }
/* 113 */     return this.delegate.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 126 */     int result = this.hashCode;
/* 127 */     return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 138 */     return new EnumSerializedForm<>(this.delegate);
/*     */   }
/*     */   
/*     */   private static class EnumSerializedForm<E extends Enum<E>>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumSet<E> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumSet<E> delegate) {
/* 148 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 153 */       return new ImmutableEnumSet<>(this.delegate.clone());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableEnumSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */