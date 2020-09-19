/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public final class ListBox<T>
/*    */   implements App<ListBox.Mu, T> {
/*    */   private final List<T> value;
/*    */   
/*    */   public static final class Mu implements K1 {}
/*    */   
/*    */   public static <T> List<T> unbox(App<Mu, T> box) {
/* 15 */     return ((ListBox)box).value;
/*    */   }
/*    */   
/*    */   public static <T> ListBox<T> create(List<T> value) {
/* 19 */     return new ListBox<>(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ListBox(List<T> value) {
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static <F extends K1, A, B> App<F, List<B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, List<A> input) {
/* 29 */     return applicative.map(ListBox::unbox, Instance.INSTANCE.traverse(applicative, function, create(input)));
/*    */   }
/*    */   
/*    */   public static <F extends K1, A> App<F, List<A>> flip(Applicative<F, ?> applicative, List<App<F, A>> input) {
/* 33 */     return applicative.map(ListBox::unbox, Instance.INSTANCE.flip(applicative, create(input)));
/*    */   }
/*    */   
/*    */   public enum Instance implements Traversable<Mu, Instance.Mu> {
/* 37 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Traversable.Mu {}
/*    */     
/*    */     public <T, R> App<ListBox.Mu, R> map(Function<? super T, ? extends R> func, App<ListBox.Mu, T> ts) {
/* 43 */       return ListBox.create((List<R>)ListBox.<T>unbox(ts).stream().<R>map(func).collect(Collectors.toList()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <F extends K1, A, B> App<F, App<ListBox.Mu, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<ListBox.Mu, A> input) {
/* 48 */       List<? extends A> list = ListBox.unbox(input);
/*    */       
/* 50 */       App<F, ImmutableList.Builder<B>> result = applicative.point(ImmutableList.builder());
/*    */       
/* 52 */       for (A a : list) {
/* 53 */         App<F, B> fb = function.apply(a);
/* 54 */         result = applicative.ap2(applicative.point(ImmutableList.Builder::add), result, fb);
/*    */       } 
/*    */       
/* 57 */       return applicative.map(b -> ListBox.create((List<?>)b.build()), result);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\ListBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */