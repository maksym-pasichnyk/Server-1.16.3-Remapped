/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.Optic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ final class ProfunctorTransformer<S, T, A, B>
/*    */   extends PointFree<Function<Function<A, B>, Function<S, T>>>
/*    */ {
/*    */   protected final Optic<? super FunctionType.Instance.Mu, S, T, A, B> optic;
/*    */   protected final Function<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, S, T>> func;
/*    */   private final Function<Function<A, B>, Function<S, T>> unwrappedFunction;
/*    */   
/*    */   public ProfunctorTransformer(Optic<? super FunctionType.Instance.Mu, S, T, A, B> optic) {
/* 19 */     this.optic = optic;
/* 20 */     this.func = optic.eval((App)FunctionType.Instance.INSTANCE);
/* 21 */     this.unwrappedFunction = (input -> FunctionType.unbox(this.func.apply(FunctionType.create(input))));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 26 */     return "Optic[" + this.optic + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<Function<A, B>, Function<S, T>>> eval() {
/* 31 */     return ops -> this.unwrappedFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (this == o) {
/* 37 */       return true;
/*    */     }
/* 39 */     if (o == null || getClass() != o.getClass()) {
/* 40 */       return false;
/*    */     }
/* 42 */     ProfunctorTransformer<?, ?, ?, ?> that = (ProfunctorTransformer<?, ?, ?, ?>)o;
/* 43 */     return Objects.equals(this.optic, that.optic);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 48 */     return Objects.hash(new Object[] { this.optic });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\ProfunctorTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */