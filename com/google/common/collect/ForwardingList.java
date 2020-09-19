/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingList<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements List<E>
/*     */ {
/*     */   public void add(int index, E element) {
/*  69 */     delegate().add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(int index, Collection<? extends E> elements) {
/*  75 */     return delegate().addAll(index, elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  80 */     return delegate().get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object element) {
/*  85 */     return delegate().indexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object element) {
/*  90 */     return delegate().lastIndexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/*  95 */     return delegate().listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 100 */     return delegate().listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove(int index) {
/* 106 */     return delegate().remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E set(int index, E element) {
/* 112 */     return delegate().set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 117 */     return delegate().subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 122 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 127 */     return delegate().hashCode();
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
/*     */   protected boolean standardAdd(E element) {
/* 139 */     add(size(), element);
/* 140 */     return true;
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
/*     */   protected boolean standardAddAll(int index, Iterable<? extends E> elements) {
/* 152 */     return Lists.addAllImpl(this, index, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardIndexOf(@Nullable Object element) {
/* 163 */     return Lists.indexOfImpl(this, element);
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
/*     */   protected int standardLastIndexOf(@Nullable Object element) {
/* 175 */     return Lists.lastIndexOfImpl(this, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<E> standardIterator() {
/* 186 */     return listIterator();
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
/*     */   protected ListIterator<E> standardListIterator() {
/* 198 */     return listIterator(0);
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
/*     */   @Beta
/*     */   protected ListIterator<E> standardListIterator(int start) {
/* 212 */     return Lists.listIteratorImpl(this, start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected List<E> standardSubList(int fromIndex, int toIndex) {
/* 224 */     return Lists.subListImpl(this, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected boolean standardEquals(@Nullable Object object) {
/* 236 */     return Lists.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected int standardHashCode() {
/* 248 */     return Lists.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   protected abstract List<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */