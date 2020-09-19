/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingCollection<E>
/*     */   extends ForwardingObject
/*     */   implements Collection<E>
/*     */ {
/*     */   public Iterator<E> iterator() {
/*  62 */     return delegate().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  67 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeAll(Collection<?> collection) {
/*  73 */     return delegate().removeAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  78 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  83 */     return delegate().contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/*  89 */     return delegate().add(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/*  95 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/* 100 */     return delegate().containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 106 */     return delegate().addAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean retainAll(Collection<?> collection) {
/* 112 */     return delegate().retainAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 117 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 122 */     return delegate().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] array) {
/* 128 */     return delegate().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContains(@Nullable Object object) {
/* 139 */     return Iterators.contains(iterator(), object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContainsAll(Collection<?> collection) {
/* 150 */     return Collections2.containsAllImpl(this, collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(Collection<? extends E> collection) {
/* 161 */     return Iterators.addAll(this, collection.iterator());
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
/*     */   protected boolean standardRemove(@Nullable Object object) {
/* 173 */     Iterator<E> iterator = iterator();
/* 174 */     while (iterator.hasNext()) {
/* 175 */       if (Objects.equal(iterator.next(), object)) {
/* 176 */         iterator.remove();
/* 177 */         return true;
/*     */       } 
/*     */     } 
/* 180 */     return false;
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
/*     */   protected boolean standardRemoveAll(Collection<?> collection) {
/* 192 */     return Iterators.removeAll(iterator(), collection);
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
/*     */   protected boolean standardRetainAll(Collection<?> collection) {
/* 204 */     return Iterators.retainAll(iterator(), collection);
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
/*     */   protected void standardClear() {
/* 216 */     Iterators.clear(iterator());
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
/*     */   protected boolean standardIsEmpty() {
/* 228 */     return !iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 239 */     return Collections2.toStringImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] standardToArray() {
/* 250 */     Object[] newArray = new Object[size()];
/* 251 */     return toArray(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T[] standardToArray(T[] array) {
/* 262 */     return ObjectArrays.toArrayImpl(this, array);
/*     */   }
/*     */   
/*     */   protected abstract Collection<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */