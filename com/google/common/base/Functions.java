/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible
/*     */ public final class Functions
/*     */ {
/*     */   public static Function<Object, String> toStringFunction() {
/*  61 */     return ToStringFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String> {
/*  66 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public String apply(Object o) {
/*  70 */       Preconditions.checkNotNull(o);
/*  71 */       return o.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  76 */       return "Functions.toStringFunction()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Function<E, E> identity() {
/*  86 */     return IdentityFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object> {
/*  91 */     INSTANCE;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object apply(@Nullable Object o) {
/*  96 */       return o;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 101 */       return "Functions.identity()";
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
/* 119 */     return new FunctionForMapNoDefault<>(map);
/*     */   }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     
/*     */     FunctionForMapNoDefault(Map<K, V> map) {
/* 126 */       this.map = Preconditions.<Map<K, V>>checkNotNull(map);
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 131 */       V result = this.map.get(key);
/* 132 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", key);
/* 133 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 138 */       if (o instanceof FunctionForMapNoDefault) {
/* 139 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
/* 140 */         return this.map.equals(that.map);
/*     */       } 
/* 142 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 147 */       return this.map.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 152 */       return "Functions.forMap(" + this.map + ")";
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 172 */     return new ForMapWithDefault<>(map, defaultValue);
/*     */   }
/*     */   
/*     */   private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable { final Map<K, ? extends V> map;
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 180 */       this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
/* 181 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 186 */       V result = this.map.get(key);
/* 187 */       return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 192 */       if (o instanceof ForMapWithDefault) {
/* 193 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
/* 194 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/* 196 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 201 */       return Objects.hashCode(new Object[] { this.map, this.defaultValue });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 207 */       return "Functions.forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
/* 226 */     return new FunctionComposition<>(g, f);
/*     */   }
/*     */   
/*     */   private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable { private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 234 */       this.g = Preconditions.<Function<B, C>>checkNotNull(g);
/* 235 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public C apply(@Nullable A a) {
/* 240 */       return this.g.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 245 */       if (obj instanceof FunctionComposition) {
/* 246 */         FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
/* 247 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 249 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 254 */       return this.f.hashCode() ^ this.g.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 260 */       return this.g + "(" + this.f + ")";
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
/* 275 */     return new PredicateFunction<>(predicate);
/*     */   }
/*     */   
/*     */   private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private PredicateFunction(Predicate<T> predicate) {
/* 283 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean apply(@Nullable T t) {
/* 288 */       return Boolean.valueOf(this.predicate.apply(t));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 293 */       if (obj instanceof PredicateFunction) {
/* 294 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 295 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 297 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 302 */       return this.predicate.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 307 */       return "Functions.forPredicate(" + this.predicate + ")";
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
/*     */   public static <E> Function<Object, E> constant(@Nullable E value) {
/* 322 */     return new ConstantFunction<>(value);
/*     */   }
/*     */   
/*     */   private static class ConstantFunction<E> implements Function<Object, E>, Serializable { private final E value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ConstantFunction(@Nullable E value) {
/* 329 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(@Nullable Object from) {
/* 334 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 339 */       if (obj instanceof ConstantFunction) {
/* 340 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 341 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 343 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 348 */       return (this.value == null) ? 0 : this.value.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 353 */       return "Functions.constant(" + this.value + ")";
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
/* 367 */     return new SupplierFunction<>(supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierFunction<T>
/*     */     implements Function<Object, T>, Serializable {
/*     */     private final Supplier<T> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SupplierFunction(Supplier<T> supplier) {
/* 376 */       this.supplier = Preconditions.<Supplier<T>>checkNotNull(supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public T apply(@Nullable Object input) {
/* 381 */       return this.supplier.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 386 */       if (obj instanceof SupplierFunction) {
/* 387 */         SupplierFunction<?> that = (SupplierFunction)obj;
/* 388 */         return this.supplier.equals(that.supplier);
/*     */       } 
/* 390 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 395 */       return this.supplier.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 400 */       return "Functions.forSupplier(" + this.supplier + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */