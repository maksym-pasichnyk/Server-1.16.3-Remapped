/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Lens<S, T, A, B>
/*    */   extends App2<Lens.Mu<A, B>, S, T>, Optic<Cartesian.Mu, S, T, A, B> {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   public static final class Mu2<S, T>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Lens<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 20 */     return (Lens)box;
/*    */   }
/*    */   
/*    */   static <S, T, A, B> Lens<S, T, A, B> unbox2(App2<Mu2<S, T>, B, A> box) {
/* 24 */     return ((Box)box).lens;
/*    */   }
/*    */   
/*    */   static <S, T, A, B> App2<Mu2<S, T>, B, A> box(Lens<S, T, A, B> lens) {
/* 28 */     return new Box<>(lens);
/*    */   }
/*    */   
/*    */   public static final class Box<S, T, A, B> implements App2<Mu2<S, T>, B, A> {
/*    */     private final Lens<S, T, A, B> lens;
/*    */     
/*    */     public Box(Lens<S, T, A, B> lens) {
/* 35 */       this.lens = lens;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cartesian.Mu, P> proofBox) {
/* 45 */     Cartesian<P, ? extends Cartesian.Mu> proof = Cartesian.unbox(proofBox);
/* 46 */     return a -> proof.dimap(proof.first(a), (), ());
/*    */   }
/*    */   
/*    */   A view(S paramS);
/*    */   
/*    */   T update(B paramB, S paramS);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Cartesian<Mu<A2, B2>, Cartesian.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Lens.Mu<A2, B2>, A, B>, App2<Lens.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 56 */       return l -> Optics.lens((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Lens.Mu<A2, B2>, A, B> input) {
/* 64 */       return Optics.lens(pair -> Lens.unbox(input).view(pair.getFirst()), (b2, pair) -> Pair.of(Lens.unbox(input).update(b2, pair.getFirst()), pair.getSecond()));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Lens.Mu<A2, B2>, A, B> input) {
/* 72 */       return Optics.lens(pair -> Lens.unbox(input).view(pair.getSecond()), (b2, pair) -> Pair.of(pair.getFirst(), Lens.unbox(input).update(b2, pair.getSecond())));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Lens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */