/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Apply<A, B>
/*    */   extends PointFree<B>
/*    */ {
/*    */   protected final PointFree<Function<A, B>> func;
/*    */   protected final PointFree<A> arg;
/*    */   protected final Type<A> argType;
/*    */   
/*    */   public Apply(PointFree<Function<A, B>> func, PointFree<A> arg, Type<A> argType) {
/* 19 */     this.func = func;
/* 20 */     this.arg = arg;
/* 21 */     this.argType = argType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, B> eval() {
/* 26 */     return ops -> ((Function)this.func.evalCached().apply(ops)).apply(this.arg.evalCached().apply(ops));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 31 */     return "(ap " + this.func.toString(level + 1) + "\n" + indent(level + 1) + this.arg.toString(level + 1) + "\n" + indent(level) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<B>> all(PointFreeRule rule, Type<B> type) {
/* 36 */     return Optional.of(Functions.app(rule
/* 37 */           .<Function<A, B>>rewrite(DSL.func(this.argType, type), this.func).map(f1 -> f1).orElse(this.func), rule
/* 38 */           .<A>rewrite(this.argType, this.arg).map(f -> f).orElse(this.arg), this.argType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<B>> one(PointFreeRule rule, Type<B> type) {
/* 45 */     return rule.<Function<A, B>>rewrite(DSL.func(this.argType, type), this.func).map(f -> Optional.of(Functions.app(f, this.arg, this.argType)))
/* 46 */       .orElseGet(() -> rule.<A>rewrite(this.argType, this.arg).map(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 51 */     if (this == o) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (!(o instanceof Apply)) {
/* 55 */       return false;
/*    */     }
/* 57 */     Apply<?, ?> apply = (Apply<?, ?>)o;
/* 58 */     return (Objects.equals(this.func, apply.func) && Objects.equals(this.arg, apply.arg));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hash(new Object[] { this.func, this.arg });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Apply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */