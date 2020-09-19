/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.CocartesianLike;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.Traversable;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public abstract class Either<L, R>
/*     */   implements App<Either.Mu<R>, L>
/*     */ {
/*     */   public static final class Mu<R>
/*     */     implements K1 {}
/*     */   
/*     */   public static <L, R> Either<L, R> unbox(App<Mu<R>, L> box) {
/*  21 */     return (Either)box;
/*     */   }
/*     */   
/*     */   private static final class Left<L, R> extends Either<L, R> {
/*     */     private final L value;
/*     */     
/*     */     public Left(L value) {
/*  28 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> f1, Function<? super R, ? extends D> f2) {
/*  33 */       return new Left((L)f1.apply(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T map(Function<? super L, ? extends T> l, Function<? super R, ? extends T> r) {
/*  38 */       return l.apply(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifLeft(Consumer<? super L> consumer) {
/*  43 */       consumer.accept(this.value);
/*  44 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifRight(Consumer<? super R> consumer) {
/*  49 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<L> left() {
/*  54 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> right() {
/*  59 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  64 */       return "Left[" + this.value + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  69 */       if (this == o) {
/*  70 */         return true;
/*     */       }
/*  72 */       if (o == null || getClass() != o.getClass()) {
/*  73 */         return false;
/*     */       }
/*  75 */       Left<?, ?> left = (Left<?, ?>)o;
/*  76 */       return Objects.equals(this.value, left.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  81 */       return Objects.hash(new Object[] { this.value });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Right<L, R> extends Either<L, R> {
/*     */     private final R value;
/*     */     
/*     */     public Right(R value) {
/*  89 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> f1, Function<? super R, ? extends D> f2) {
/*  94 */       return new Right((R)f2.apply(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T map(Function<? super L, ? extends T> l, Function<? super R, ? extends T> r) {
/*  99 */       return r.apply(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifLeft(Consumer<? super L> consumer) {
/* 104 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifRight(Consumer<? super R> consumer) {
/* 109 */       consumer.accept(this.value);
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<L> left() {
/* 115 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> right() {
/* 120 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 125 */       return "Right[" + this.value + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 130 */       if (this == o) {
/* 131 */         return true;
/*     */       }
/* 133 */       if (o == null || getClass() != o.getClass()) {
/* 134 */         return false;
/*     */       }
/* 136 */       Right<?, ?> right = (Right<?, ?>)o;
/* 137 */       return Objects.equals(this.value, right.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 142 */       return Objects.hash(new Object[] { this.value });
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
/*     */   private Either() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> l) {
/* 162 */     return map(t -> left(l.apply(t)), Either::right);
/*     */   }
/*     */   
/*     */   public <T> Either<L, T> mapRight(Function<? super R, ? extends T> l) {
/* 166 */     return map(Either::left, t -> right(l.apply(t)));
/*     */   }
/*     */   
/*     */   public static <L, R> Either<L, R> left(L value) {
/* 170 */     return new Left<>(value);
/*     */   }
/*     */   
/*     */   public static <L, R> Either<L, R> right(R value) {
/* 174 */     return new Right<>(value);
/*     */   }
/*     */   
/*     */   public L orThrow() {
/* 178 */     return map(l -> l, r -> {
/*     */           if (r instanceof Throwable) {
/*     */             throw new RuntimeException((Throwable)r);
/*     */           }
/*     */           throw new RuntimeException(r.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   public Either<R, L> swap() {
/* 187 */     return map(Either::right, Either::left);
/*     */   } public abstract <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> paramFunction, Function<? super R, ? extends D> paramFunction1); public abstract <T> T map(Function<? super L, ? extends T> paramFunction, Function<? super R, ? extends T> paramFunction1);
/*     */   public abstract Either<L, R> ifLeft(Consumer<? super L> paramConsumer);
/*     */   public <L2> Either<L2, R> flatMap(Function<L, Either<L2, R>> function) {
/* 191 */     return map(function, Either::right);
/*     */   }
/*     */   public abstract Either<L, R> ifRight(Consumer<? super R> paramConsumer);
/*     */   public abstract Optional<L> left();
/*     */   
/*     */   public abstract Optional<R> right();
/*     */   
/*     */   public static final class Instance<R2> implements Applicative<Mu<R2>, Instance.Mu<R2>>, Traversable<Mu<R2>, Instance.Mu<R2>>, CocartesianLike<Mu<R2>, R2, Instance.Mu<R2>> { public <T, R> App<Either.Mu<R2>, R> map(Function<? super T, ? extends R> func, App<Either.Mu<R2>, T> ts) {
/* 199 */       return Either.<T, R2>unbox(ts).mapLeft(func);
/*     */     }
/*     */     public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {}
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> point(A a) {
/* 204 */       return Either.left(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> lift1(App<Either.Mu<R2>, Function<A, R>> function) {
/* 209 */       return a -> Either.unbox(function).flatMap(());
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, R> BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> lift2(App<Either.Mu<R2>, BiFunction<A, B, R>> function) {
/* 214 */       return (a, b) -> Either.unbox(function).flatMap(());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <F extends K1, A, B> App<F, App<Either.Mu<R2>, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<Either.Mu<R2>, A> input) {
/* 225 */       return (App<F, App<Either.Mu<R2>, B>>)Either.<A, R2>unbox(input).map(l -> {
/*     */             App<F, B> b = function.apply(l);
/*     */             return applicative.ap(Either::left, b);
/*     */           }r -> applicative.point(Either.right(r)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> to(App<Either.Mu<R2>, A> input) {
/* 236 */       return input;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> from(App<Either.Mu<R2>, A> input) {
/* 241 */       return input;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixer\\util\Either.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */