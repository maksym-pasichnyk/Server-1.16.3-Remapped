/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ 
/*     */ public class DataResult<R>
/*     */   implements App<DataResult.Mu, R>
/*     */ {
/*     */   private final Either<R, PartialResult<R>> result;
/*     */   private final Lifecycle lifecycle;
/*     */   
/*     */   public static final class Mu
/*     */     implements K1 {}
/*     */   
/*     */   public static <R> DataResult<R> unbox(App<Mu, R> box) {
/*  27 */     return (DataResult)box;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R> DataResult<R> success(R result) {
/*  34 */     return success(result, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   public static <R> DataResult<R> error(String message, R partialResult) {
/*  38 */     return error(message, partialResult, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   public static <R> DataResult<R> error(String message) {
/*  42 */     return error(message, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   public static <R> DataResult<R> success(R result, Lifecycle experimental) {
/*  46 */     return new DataResult<>(Either.left(result), experimental);
/*     */   }
/*     */   
/*     */   public static <R> DataResult<R> error(String message, R partialResult, Lifecycle lifecycle) {
/*  50 */     return new DataResult<>(Either.right(new PartialResult(message, Optional.of(partialResult))), lifecycle);
/*     */   }
/*     */   
/*     */   public static <R> DataResult<R> error(String message, Lifecycle lifecycle) {
/*  54 */     return new DataResult<>(Either.right(new PartialResult(message, Optional.empty())), lifecycle);
/*     */   }
/*     */   
/*     */   public static <K, V> Function<K, DataResult<V>> partialGet(Function<K, V> partialGet, Supplier<String> errorPrefix) {
/*  58 */     return name -> (DataResult)Optional.ofNullable(partialGet.apply(name)).map(DataResult::success).orElseGet(());
/*     */   }
/*     */   
/*     */   private static <R> DataResult<R> create(Either<R, PartialResult<R>> result, Lifecycle lifecycle) {
/*  62 */     return new DataResult<>(result, lifecycle);
/*     */   }
/*     */   
/*     */   private DataResult(Either<R, PartialResult<R>> result, Lifecycle lifecycle) {
/*  66 */     this.result = result;
/*  67 */     this.lifecycle = lifecycle;
/*     */   }
/*     */   
/*     */   public Either<R, PartialResult<R>> get() {
/*  71 */     return this.result;
/*     */   }
/*     */   
/*     */   public Optional<R> result() {
/*  75 */     return this.result.left();
/*     */   }
/*     */   
/*     */   public Lifecycle lifecycle() {
/*  79 */     return this.lifecycle;
/*     */   }
/*     */   
/*     */   public Optional<R> resultOrPartial(Consumer<String> onError) {
/*  83 */     return (Optional<R>)this.result.map(Optional::of, r -> {
/*     */           onError.accept(r.message);
/*     */           return r.partialResult;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R getOrThrow(boolean allowPartial, Consumer<String> onError) {
/*  93 */     return (R)this.result.map(l -> l, r -> {
/*     */           onError.accept(r.message);
/*     */           if (allowPartial && r.partialResult.isPresent()) {
/*     */             return r.partialResult.get();
/*     */           }
/*     */           throw new RuntimeException(r.message);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<PartialResult<R>> error() {
/* 106 */     return this.result.right();
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> map(Function<? super R, ? extends T> function) {
/* 110 */     return create(this.result.mapBoth(function, r -> new PartialResult(r.message, r.partialResult.map(function))), this.lifecycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<R> promotePartial(Consumer<String> onError) {
/* 117 */     return (DataResult<R>)this.result.map(r -> new DataResult(Either.left(r), this.lifecycle), r -> {
/*     */           onError.accept(r.message);
/*     */           return r.partialResult.map(()).orElseGet(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String appendMessages(String first, String second) {
/* 129 */     return first + "; " + second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> function) {
/* 136 */     return (DataResult<R2>)this.result.map(l -> {
/*     */           DataResult<R2> second = function.apply(l);
/*     */           return create(second.get(), this.lifecycle.add(second.lifecycle));
/*     */         }r -> (DataResult)r.partialResult.map(()).orElseGet(()));
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
/*     */   public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> functionResult) {
/* 156 */     return create((Either<R2, PartialResult<R2>>)this.result.map(arg -> functionResult.result.mapBoth((), ()), argError -> Either.right(functionResult.result.map((), ()))), this.lifecycle
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 168 */         .add(functionResult.lifecycle));
/*     */   }
/*     */   
/*     */   public <R2, S> DataResult<S> apply2(BiFunction<R, R2, S> function, DataResult<R2> second) {
/* 172 */     return unbox(instance().apply2(function, this, second));
/*     */   }
/*     */   
/*     */   public <R2, S> DataResult<S> apply2stable(BiFunction<R, R2, S> function, DataResult<R2> second) {
/* 176 */     Applicative<Mu, Instance.Mu> instance = instance();
/* 177 */     DataResult<BiFunction<R, R2, S>> f = unbox(instance.point(function)).setLifecycle(Lifecycle.stable());
/* 178 */     return unbox(instance.ap2(f, this, second));
/*     */   }
/*     */   
/*     */   public <R2, R3, S> DataResult<S> apply3(Function3<R, R2, R3, S> function, DataResult<R2> second, DataResult<R3> third) {
/* 182 */     return unbox(instance().apply3(function, this, second, third));
/*     */   }
/*     */   
/*     */   public DataResult<R> setPartial(Supplier<R> partial) {
/* 186 */     return create(this.result.mapRight(r -> new PartialResult(r.message, Optional.of(partial.get()))), this.lifecycle);
/*     */   }
/*     */   
/*     */   public DataResult<R> setPartial(R partial) {
/* 190 */     return create(this.result.mapRight(r -> new PartialResult(r.message, Optional.of(partial))), this.lifecycle);
/*     */   }
/*     */   
/*     */   public DataResult<R> mapError(UnaryOperator<String> function) {
/* 194 */     return create(this.result.mapRight(r -> new PartialResult(function.apply(r.message), r.partialResult)), this.lifecycle);
/*     */   }
/*     */   
/*     */   public DataResult<R> setLifecycle(Lifecycle lifecycle) {
/* 198 */     return create(this.result, lifecycle);
/*     */   }
/*     */   
/*     */   public DataResult<R> addLifecycle(Lifecycle lifecycle) {
/* 202 */     return create(this.result, this.lifecycle.add(lifecycle));
/*     */   }
/*     */   
/*     */   public static Instance instance() {
/* 206 */     return Instance.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 211 */     if (this == o) {
/* 212 */       return true;
/*     */     }
/* 214 */     if (o == null || getClass() != o.getClass()) {
/* 215 */       return false;
/*     */     }
/* 217 */     DataResult<?> that = (DataResult)o;
/* 218 */     return Objects.equals(this.result, that.result);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 223 */     return Objects.hash(new Object[] { this.result });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 228 */     return "DataResult[" + this.result + ']';
/*     */   }
/*     */   
/*     */   public static class PartialResult<R> {
/*     */     private final String message;
/*     */     private final Optional<R> partialResult;
/*     */     
/*     */     public PartialResult(String message, Optional<R> partialResult) {
/* 236 */       this.message = message;
/* 237 */       this.partialResult = partialResult;
/*     */     }
/*     */     
/*     */     public <R2> PartialResult<R2> map(Function<? super R, ? extends R2> function) {
/* 241 */       return new PartialResult(this.message, this.partialResult.map(function));
/*     */     }
/*     */     
/*     */     public <R2> PartialResult<R2> flatMap(Function<R, PartialResult<R2>> function) {
/* 245 */       if (this.partialResult.isPresent()) {
/* 246 */         PartialResult<R2> result = function.apply(this.partialResult.get());
/* 247 */         return new PartialResult(DataResult.appendMessages(this.message, result.message), result.partialResult);
/*     */       } 
/* 249 */       return new PartialResult(this.message, Optional.empty());
/*     */     }
/*     */     
/*     */     public String message() {
/* 253 */       return this.message;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 258 */       if (this == o) {
/* 259 */         return true;
/*     */       }
/* 261 */       if (o == null || getClass() != o.getClass()) {
/* 262 */         return false;
/*     */       }
/* 264 */       PartialResult<?> that = (PartialResult)o;
/* 265 */       return (Objects.equals(this.message, that.message) && Objects.equals(this.partialResult, that.partialResult));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 270 */       return Objects.hash(new Object[] { this.message, this.partialResult });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 275 */       return "DynamicException[" + this.message + ' ' + this.partialResult + ']';
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Instance implements Applicative<Mu, Instance.Mu> {
/* 280 */     INSTANCE;
/*     */     
/*     */     public static final class Mu
/*     */       implements Applicative.Mu {}
/*     */     
/*     */     public <T, R> App<DataResult.Mu, R> map(Function<? super T, ? extends R> func, App<DataResult.Mu, T> ts) {
/* 286 */       return DataResult.<T>unbox(ts).map(func);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<DataResult.Mu, A> point(A a) {
/* 291 */       return DataResult.success(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>> lift1(App<DataResult.Mu, Function<A, R>> function) {
/* 296 */       return fa -> ap(function, fa);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> App<DataResult.Mu, R> ap(App<DataResult.Mu, Function<A, R>> func, App<DataResult.Mu, A> arg) {
/* 301 */       return DataResult.<A>unbox(arg).ap(DataResult.unbox(func));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, R> App<DataResult.Mu, R> ap2(App<DataResult.Mu, BiFunction<A, B, R>> func, App<DataResult.Mu, A> a, App<DataResult.Mu, B> b) {
/* 306 */       DataResult<BiFunction<A, B, R>> fr = DataResult.unbox(func);
/* 307 */       DataResult<A> ra = DataResult.unbox(a);
/* 308 */       DataResult<B> rb = DataResult.unbox(b);
/*     */ 
/*     */       
/* 311 */       if (fr.result.left().isPresent() && ra
/* 312 */         .result.left().isPresent() && rb
/* 313 */         .result.left().isPresent())
/*     */       {
/* 315 */         return new DataResult<>(Either.left(((BiFunction)fr.result.left().get()).apply(ra
/* 316 */                 .result.left().get(), rb
/* 317 */                 .result.left().get())), fr
/* 318 */             .lifecycle.add(ra.lifecycle).add(rb.lifecycle));
/*     */       }
/*     */       
/* 321 */       return super.ap2(func, a, b);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, R> App<DataResult.Mu, R> ap3(App<DataResult.Mu, Function3<T1, T2, T3, R>> func, App<DataResult.Mu, T1> t1, App<DataResult.Mu, T2> t2, App<DataResult.Mu, T3> t3) {
/* 326 */       DataResult<Function3<T1, T2, T3, R>> fr = DataResult.unbox(func);
/* 327 */       DataResult<T1> dr1 = DataResult.unbox(t1);
/* 328 */       DataResult<T2> dr2 = DataResult.unbox(t2);
/* 329 */       DataResult<T3> dr3 = DataResult.unbox(t3);
/*     */ 
/*     */       
/* 332 */       if (fr.result.left().isPresent() && dr1
/* 333 */         .result.left().isPresent() && dr2
/* 334 */         .result.left().isPresent() && dr3
/* 335 */         .result.left().isPresent())
/*     */       {
/* 337 */         return new DataResult<>(Either.left(((Function3)fr.result.left().get()).apply(dr1
/* 338 */                 .result.left().get(), dr2
/* 339 */                 .result.left().get(), dr3
/* 340 */                 .result.left().get())), fr
/* 341 */             .lifecycle.add(dr1.lifecycle).add(dr2.lifecycle).add(dr3.lifecycle));
/*     */       }
/*     */       
/* 344 */       return super.ap3(func, t1, t2, t3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Mu implements Applicative.Mu {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\DataResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */