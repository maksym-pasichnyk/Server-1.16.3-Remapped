/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class OptionalBox<T> implements App<OptionalBox.Mu, T> {
/*    */   private final Optional<T> value;
/*    */   
/*    */   public static final class Mu implements K1 {}
/*    */   
/*    */   public static <T> Optional<T> unbox(App<Mu, T> box) {
/* 13 */     return ((OptionalBox)box).value;
/*    */   }
/*    */   
/*    */   public static <T> OptionalBox<T> create(Optional<T> value) {
/* 17 */     return new OptionalBox<>(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private OptionalBox(Optional<T> value) {
/* 23 */     this.value = value;
/*    */   }
/*    */   
/*    */   public enum Instance implements Applicative<Mu, Instance.Mu>, Traversable<Mu, Instance.Mu> {
/* 27 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Applicative.Mu, Traversable.Mu {}
/*    */     
/*    */     public <T, R> App<OptionalBox.Mu, R> map(Function<? super T, ? extends R> func, App<OptionalBox.Mu, T> ts) {
/* 33 */       return OptionalBox.create(OptionalBox.<T>unbox(ts).map(func));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<OptionalBox.Mu, A> point(A a) {
/* 38 */       return OptionalBox.create(Optional.of(a));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>> lift1(App<OptionalBox.Mu, Function<A, R>> function) {
/* 43 */       return a -> OptionalBox.create(OptionalBox.unbox(function).flatMap(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>> lift2(App<OptionalBox.Mu, BiFunction<A, B, R>> function) {
/* 48 */       return (a, b) -> OptionalBox.create(OptionalBox.unbox(function).flatMap(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <F extends K1, A, B> App<F, App<OptionalBox.Mu, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<OptionalBox.Mu, A> input) {
/* 53 */       Optional<App<F, B>> traversed = OptionalBox.<A>unbox(input).map(function);
/* 54 */       if (traversed.isPresent()) {
/* 55 */         return applicative.map(b -> OptionalBox.create(Optional.of(b)), traversed.get());
/*    */       }
/* 57 */       return applicative.point(OptionalBox.create(Optional.empty()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\OptionalBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */