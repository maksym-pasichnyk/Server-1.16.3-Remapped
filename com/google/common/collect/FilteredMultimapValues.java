/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.j2objc.annotations.Weak;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ final class FilteredMultimapValues<K, V>
/*    */   extends AbstractCollection<V>
/*    */ {
/*    */   @Weak
/*    */   private final FilteredMultimap<K, V> multimap;
/*    */   
/*    */   FilteredMultimapValues(FilteredMultimap<K, V> multimap) {
/* 41 */     this.multimap = (FilteredMultimap<K, V>)Preconditions.checkNotNull(multimap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<V> iterator() {
/* 46 */     return Maps.valueIterator(this.multimap.entries().iterator());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@Nullable Object o) {
/* 51 */     return this.multimap.containsValue(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 56 */     return this.multimap.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(@Nullable Object o) {
/* 61 */     Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
/* 62 */     Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
/* 63 */     while (unfilteredItr.hasNext()) {
/*    */       
/* 65 */       Map.Entry<K, V> entry = unfilteredItr.next();
/* 66 */       if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 67 */         unfilteredItr.remove();
/* 68 */         return true;
/*    */       } 
/*    */     } 
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeAll(Collection<?> c) {
/* 76 */     return Iterables.removeIf(this.multimap
/* 77 */         .unfiltered().entries(), 
/*    */         
/* 79 */         Predicates.and(this.multimap
/* 80 */           .entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean retainAll(Collection<?> c) {
/* 85 */     return Iterables.removeIf(this.multimap
/* 86 */         .unfiltered().entries(), 
/*    */         
/* 88 */         Predicates.and(this.multimap
/* 89 */           .entryPredicate(), 
/* 90 */           Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))));
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 95 */     this.multimap.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\FilteredMultimapValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */