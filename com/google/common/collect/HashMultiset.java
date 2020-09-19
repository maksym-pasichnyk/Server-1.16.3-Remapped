/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import java.util.function.ObjIntConsumer;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ public final class HashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   @GwtIncompatible
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> HashMultiset<E> create() {
/* 41 */     return new HashMultiset<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(int distinctElements) {
/* 52 */     return new HashMultiset<>(distinctElements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
/* 64 */     HashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/* 65 */     Iterables.addAll(multiset, elements);
/* 66 */     return multiset;
/*    */   }
/*    */   
/*    */   private HashMultiset() {
/* 70 */     super(new HashMap<>());
/*    */   }
/*    */   
/*    */   private HashMultiset(int distinctElements) {
/* 74 */     super(Maps.newHashMapWithExpectedSize(distinctElements));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 83 */     stream.defaultWriteObject();
/* 84 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 89 */     stream.defaultReadObject();
/* 90 */     int distinctElements = Serialization.readCount(stream);
/* 91 */     setBackingMap(Maps.newHashMap());
/* 92 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\HashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */