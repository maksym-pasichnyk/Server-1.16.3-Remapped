/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.function.BiConsumer;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class TreeMultimap<K, V>
/*     */   extends AbstractSortedKeySortedSetMultimap<K, V>
/*     */ {
/*     */   private transient Comparator<? super K> keyComparator;
/*     */   private transient Comparator<? super V> valueComparator;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
/*  86 */     return new TreeMultimap<>(Ordering.natural(), Ordering.natural());
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
/*     */   public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/*  99 */     return new TreeMultimap<>((Comparator<? super K>)Preconditions.checkNotNull(keyComparator), (Comparator<? super V>)Preconditions.checkNotNull(valueComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 110 */     return new TreeMultimap<>(Ordering.natural(), Ordering.natural(), multimap);
/*     */   }
/*     */   
/*     */   TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/* 114 */     super(new TreeMap<>(keyComparator));
/* 115 */     this.keyComparator = keyComparator;
/* 116 */     this.valueComparator = valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
/* 123 */     this(keyComparator, valueComparator);
/* 124 */     putAll(multimap);
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
/*     */   SortedSet<V> createCollection() {
/* 137 */     return new TreeSet<>(this.valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createCollection(@Nullable K key) {
/* 142 */     if (key == null) {
/* 143 */       keyComparator().compare(key, key);
/*     */     }
/* 145 */     return super.createCollection(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Comparator<? super K> keyComparator() {
/* 156 */     return this.keyComparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super V> valueComparator() {
/* 161 */     return this.valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public NavigableSet<V> get(@Nullable K key) {
/* 170 */     return (NavigableSet<V>)super.get(key);
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
/*     */   public NavigableSet<K> keySet() {
/* 184 */     return (NavigableSet<K>)super.keySet();
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
/*     */   public NavigableMap<K, Collection<V>> asMap() {
/* 198 */     return (NavigableMap<K, Collection<V>>)super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 208 */     stream.defaultWriteObject();
/* 209 */     stream.writeObject(keyComparator());
/* 210 */     stream.writeObject(valueComparator());
/* 211 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 217 */     stream.defaultReadObject();
/* 218 */     this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(stream.readObject());
/* 219 */     this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(stream.readObject());
/* 220 */     setMap(new TreeMap<>(this.keyComparator));
/* 221 */     Serialization.populateMultimap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TreeMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */