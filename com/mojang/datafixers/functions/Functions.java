/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.RewriteResult;
/*    */ import com.mojang.datafixers.optics.Optic;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.families.Algebra;
/*    */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Functions
/*    */ {
/* 17 */   private static final Id<?> ID = new Id();
/*    */ 
/*    */   
/*    */   public static <A, B, C> PointFree<Function<A, C>> comp(Type<B> middleType, PointFree<Function<B, C>> f1, PointFree<Function<A, B>> f2) {
/* 21 */     if (Objects.equals(f1, id())) {
/* 22 */       return f2;
/*    */     }
/* 24 */     if (Objects.equals(f2, id())) {
/* 25 */       return f1;
/*    */     }
/* 27 */     return new Comp<>(middleType, f1, f2);
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<Function<A, B>> fun(String name, Function<DynamicOps<?>, Function<A, B>> fun) {
/* 31 */     return new FunctionWrapper<>(name, fun);
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<B> app(PointFree<Function<A, B>> fun, PointFree<A> arg, Type<A> argType) {
/* 35 */     return new Apply<>(fun, arg, argType);
/*    */   }
/*    */   
/*    */   public static <S, T, A, B> PointFree<Function<Function<A, B>, Function<S, T>>> profunctorTransformer(Optic<? super FunctionType.Instance.Mu, S, T, A, B> lens) {
/* 39 */     return new ProfunctorTransformer<>(lens);
/*    */   }
/*    */   
/*    */   public static <A> Bang<A> bang() {
/* 43 */     return new Bang<>();
/*    */   }
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> in(RecursivePoint.RecursivePointType<A> type) {
/* 47 */     return new In<>(type);
/*    */   }
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> out(RecursivePoint.RecursivePointType<A> type) {
/* 51 */     return new Out<>(type);
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<Function<A, B>> fold(RecursivePoint.RecursivePointType<A> aType, RewriteResult<?, B> function, Algebra algebra, int index) {
/* 55 */     return new Fold<>(aType, function, algebra, index);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> id() {
/* 60 */     return (PointFree)ID;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */