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
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumMultiset<E extends Enum<E>>
/*     */   extends AbstractMapBasedMultiset<E>
/*     */ {
/*     */   private transient Class<E> type;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
/*  41 */     return new EnumMultiset<>(type);
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
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
/*  54 */     Iterator<E> iterator = elements.iterator();
/*  55 */     Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
/*  56 */     EnumMultiset<E> multiset = new EnumMultiset<>(((Enum<E>)iterator.next()).getDeclaringClass());
/*  57 */     Iterables.addAll(multiset, elements);
/*  58 */     return multiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements, Class<E> type) {
/*  69 */     EnumMultiset<E> result = create(type);
/*  70 */     Iterables.addAll(result, elements);
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumMultiset(Class<E> type) {
/*  78 */     super(WellBehavedMap.wrap(new EnumMap<>(type)));
/*  79 */     this.type = type;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  84 */     stream.defaultWriteObject();
/*  85 */     stream.writeObject(this.type);
/*  86 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  96 */     stream.defaultReadObject();
/*     */     
/*  98 */     Class<E> localType = (Class<E>)stream.readObject();
/*  99 */     this.type = localType;
/* 100 */     setBackingMap(WellBehavedMap.wrap(new EnumMap<>(this.type)));
/* 101 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\EnumMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */