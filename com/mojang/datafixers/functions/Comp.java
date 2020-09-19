/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Func;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Comp<A, B, C>
/*    */   extends PointFree<Function<A, C>>
/*    */ {
/*    */   protected final Type<B> middleType;
/*    */   protected final PointFree<Function<B, C>> first;
/*    */   protected final PointFree<Function<A, B>> second;
/*    */   
/*    */   public Comp(Type<B> middleType, PointFree<Function<B, C>> first, PointFree<Function<A, B>> second) {
/* 20 */     this.middleType = middleType;
/* 21 */     this.first = first;
/* 22 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 27 */     return "(\n" + indent(level + 1) + this.first.toString(level + 1) + "\n" + indent(level + 1) + "◦\n" + indent(level + 1) + this.second.toString(level + 1) + "\n" + indent(level) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<Function<A, C>>> all(PointFreeRule rule, Type<Function<A, C>> type) {
/* 32 */     Func<A, C> funcType = (Func)type;
/* 33 */     return Optional.of(Functions.comp(this.middleType, rule
/*    */           
/* 35 */           .<Function<B, C>>rewrite(DSL.func(this.middleType, funcType.second()), this.first).map(f -> f).orElse(this.first), rule
/* 36 */           .<Function<A, B>>rewrite(DSL.func(funcType.first(), this.middleType), this.second).map(f1 -> f1).orElse(this.second)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<Function<A, C>>> one(PointFreeRule rule, Type<Function<A, C>> type) {
/* 42 */     Func<A, C> funcType = (Func)type;
/* 43 */     return rule.<Function<B, C>>rewrite(DSL.func(this.middleType, funcType.second()), this.first).map(f -> Optional.of(Functions.comp(this.middleType, f, this.second)))
/* 44 */       .orElseGet(() -> rule.<Function<A, B>>rewrite(DSL.func(funcType.first(), this.middleType), this.second).map(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 49 */     if (this == o) {
/* 50 */       return true;
/*    */     }
/* 52 */     if (o == null || getClass() != o.getClass()) {
/* 53 */       return false;
/*    */     }
/* 55 */     Comp<?, ?, ?> comp = (Comp<?, ?, ?>)o;
/* 56 */     return (Objects.equals(this.first, comp.first) && Objects.equals(this.second, comp.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, C>> eval() {
/* 66 */     return ops -> ();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Comp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */