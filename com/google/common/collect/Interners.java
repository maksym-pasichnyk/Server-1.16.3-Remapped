/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ public final class Interners
/*     */ {
/*     */   public static class InternerBuilder
/*     */   {
/*  44 */     private final MapMaker mapMaker = new MapMaker();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean strong = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder strong() {
/*  56 */       this.strong = true;
/*  57 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("java.lang.ref.WeakReference")
/*     */     public InternerBuilder weak() {
/*  67 */       this.strong = false;
/*  68 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder concurrencyLevel(int concurrencyLevel) {
/*  77 */       this.mapMaker.concurrencyLevel(concurrencyLevel);
/*  78 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Interner<E> build() {
/*  82 */       return this.strong ? new Interners.StrongInterner<>(this.mapMaker) : new Interners.WeakInterner<>(this.mapMaker);
/*     */     }
/*     */     
/*     */     private InternerBuilder() {} }
/*     */   
/*     */   public static InternerBuilder newBuilder() {
/*  88 */     return new InternerBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Interner<E> newStrongInterner() {
/*  97 */     return newBuilder().strong().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public static <E> Interner<E> newWeakInterner() {
/* 108 */     return newBuilder().weak().build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class StrongInterner<E> implements Interner<E> {
/*     */     @VisibleForTesting
/*     */     final ConcurrentMap<E, E> map;
/*     */     
/*     */     private StrongInterner(MapMaker mapMaker) {
/* 117 */       this.map = mapMaker.makeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public E intern(E sample) {
/* 122 */       E canonical = this.map.putIfAbsent((E)Preconditions.checkNotNull(sample), sample);
/* 123 */       return (canonical == null) ? sample : canonical;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class WeakInterner<E>
/*     */     implements Interner<E> {
/*     */     @VisibleForTesting
/*     */     final MapMakerInternalMap<E, Dummy, ?, ?> map;
/*     */     
/*     */     private WeakInterner(MapMaker mapMaker) {
/* 134 */       this.map = mapMaker.weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E intern(E sample) {
/*     */       while (true) {
/* 141 */         MapMakerInternalMap.InternalEntry<E, Dummy, ?> entry = (MapMakerInternalMap.InternalEntry<E, Dummy, ?>)this.map.getEntry(sample);
/* 142 */         if (entry != null) {
/* 143 */           E canonical = entry.getKey();
/* 144 */           if (canonical != null) {
/* 145 */             return canonical;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 150 */         Dummy sneaky = this.map.putIfAbsent(sample, Dummy.VALUE);
/* 151 */         if (sneaky == null) {
/* 152 */           return sample;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private enum Dummy
/*     */     {
/* 165 */       VALUE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Function<E, E> asFunction(Interner<E> interner) {
/* 175 */     return new InternerFunction<>((Interner<E>)Preconditions.checkNotNull(interner));
/*     */   }
/*     */   
/*     */   private static class InternerFunction<E>
/*     */     implements Function<E, E> {
/*     */     private final Interner<E> interner;
/*     */     
/*     */     public InternerFunction(Interner<E> interner) {
/* 183 */       this.interner = interner;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(E input) {
/* 188 */       return this.interner.intern(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 193 */       return this.interner.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 198 */       if (other instanceof InternerFunction) {
/* 199 */         InternerFunction<?> that = (InternerFunction)other;
/* 200 */         return this.interner.equals(that.interner);
/*     */       } 
/*     */       
/* 203 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Interners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */