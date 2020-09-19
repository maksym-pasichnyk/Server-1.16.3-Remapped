/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.function.BiPredicate;
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
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class Equivalence<T>
/*     */   implements BiPredicate<T, T>
/*     */ {
/*     */   public final boolean equivalent(@Nullable T a, @Nullable T b) {
/*  67 */     if (a == b) {
/*  68 */       return true;
/*     */     }
/*  70 */     if (a == null || b == null) {
/*  71 */       return false;
/*     */     }
/*  73 */     return doEquivalent(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean test(@Nullable T t, @Nullable T u) {
/*  84 */     return equivalent(t, u);
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
/*     */   
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
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
/*     */   public final int hash(@Nullable T t) {
/* 114 */     if (t == null) {
/* 115 */       return 0;
/*     */     }
/* 117 */     return doHash(t);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int doHash(T paramT);
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
/*     */   public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
/* 152 */     return new FunctionalEquivalence<>(function, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <S extends T> Wrapper<S> wrap(@Nullable S reference) {
/* 163 */     return new Wrapper<>(this, reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Wrapper<T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final T reference;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference) {
/* 189 */       this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
/* 190 */       this.reference = reference;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T get() {
/* 196 */       return this.reference;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 206 */       if (obj == this) {
/* 207 */         return true;
/*     */       }
/* 209 */       if (obj instanceof Wrapper) {
/* 210 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 212 */         if (this.equivalence.equals(that.equivalence)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 218 */           Equivalence<Object> equivalence = (Equivalence)this.equivalence;
/* 219 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         } 
/*     */       } 
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 230 */       return this.equivalence.hash(this.reference);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 239 */       return this.equivalence + ".wrap(" + this.reference + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public final <S extends T> Equivalence<Iterable<S>> pairwise() {
/* 260 */     return new PairwiseEquivalence<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Predicate<T> equivalentTo(@Nullable T target) {
/* 270 */     return new EquivalentToPredicate<>(this, target);
/*     */   }
/*     */   
/*     */   private static final class EquivalentToPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Equivalence<T> equivalence;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
/* 279 */       this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
/* 280 */       this.target = target;
/*     */     }
/*     */     @Nullable
/*     */     private final T target; private static final long serialVersionUID = 0L;
/*     */     public boolean apply(@Nullable T input) {
/* 285 */       return this.equivalence.equivalent(input, this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 290 */       if (this == obj) {
/* 291 */         return true;
/*     */       }
/* 293 */       if (obj instanceof EquivalentToPredicate) {
/* 294 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 295 */         return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
/*     */       } 
/* 297 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 302 */       return Objects.hashCode(new Object[] { this.equivalence, this.target });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 307 */       return this.equivalence + ".equivalentTo(" + this.target + ")";
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
/*     */ 
/*     */   
/*     */   public static Equivalence<Object> equals() {
/* 324 */     return Equals.INSTANCE;
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
/*     */   public static Equivalence<Object> identity() {
/* 337 */     return Identity.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class Equals
/*     */     extends Equivalence<Object> implements Serializable {
/* 342 */     static final Equals INSTANCE = new Equals();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 346 */       return a.equals(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 351 */       return o.hashCode();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 355 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 363 */     static final Identity INSTANCE = new Identity();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 367 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 372 */       return System.identityHashCode(o);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 376 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Equivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */