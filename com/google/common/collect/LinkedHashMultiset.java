/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class LinkedHashMultiset<E>
/*     */   extends AbstractMapBasedMultiset<E>
/*     */ {
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E> LinkedHashMultiset<E> create() {
/*  51 */     return new LinkedHashMultiset<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedHashMultiset<E> create(int distinctElements) {
/*  62 */     return new LinkedHashMultiset<>(distinctElements);
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
/*     */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements) {
/*  74 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*  75 */     Iterables.addAll(multiset, elements);
/*  76 */     return multiset;
/*     */   }
/*     */   
/*     */   private LinkedHashMultiset() {
/*  80 */     super(new LinkedHashMap<>());
/*     */   }
/*     */   
/*     */   private LinkedHashMultiset(int distinctElements) {
/*  84 */     super(Maps.newLinkedHashMapWithExpectedSize(distinctElements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  93 */     stream.defaultWriteObject();
/*  94 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  99 */     stream.defaultReadObject();
/* 100 */     int distinctElements = Serialization.readCount(stream);
/* 101 */     setBackingMap(new LinkedHashMap<>());
/* 102 */     Serialization.populateMultiset(this, stream, distinctElements);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\LinkedHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */