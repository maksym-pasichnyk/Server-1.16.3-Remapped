/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.RewriteResult;
/*    */ import com.mojang.datafixers.types.families.Algebra;
/*    */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*    */ import com.mojang.datafixers.types.families.TypeFamily;
/*    */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ 
/*    */ 
/*    */ final class Fold<A, B>
/*    */   extends PointFree<Function<A, B>>
/*    */ {
/* 20 */   private static final Map<Pair<RecursiveTypeFamily, Algebra>, IntFunction<RewriteResult<?, ?>>> HMAP_CACHE = Maps.newConcurrentMap();
/* 21 */   private static final Map<Pair<IntFunction<RewriteResult<?, ?>>, Integer>, RewriteResult<?, ?>> HMAP_APPLY_CACHE = Maps.newConcurrentMap();
/*    */   
/*    */   protected final RecursivePoint.RecursivePointType<A> aType;
/*    */   protected final RewriteResult<?, B> function;
/*    */   protected final Algebra algebra;
/*    */   protected final int index;
/*    */   
/*    */   public Fold(RecursivePoint.RecursivePointType<A> aType, RewriteResult<?, B> function, Algebra algebra, int index) {
/* 29 */     this.aType = aType;
/* 30 */     this.function = function;
/* 31 */     this.algebra = algebra;
/* 32 */     this.index = index;
/*    */   }
/*    */   
/*    */   private <FB> PointFree<Function<A, B>> cap(RewriteResult<?, B> op, RewriteResult<?, FB> resResult) {
/* 36 */     return Functions.comp(resResult.view().newType(), op.view().function(), resResult.view().function());
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, B>> eval() {
/* 41 */     return ops -> ();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 54 */     return "fold(" + this.aType + ", " + this.index + ", \n" + indent(level + 1) + this.algebra.toString(level + 1) + "\n" + indent(level) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 59 */     if (this == o) {
/* 60 */       return true;
/*    */     }
/* 62 */     if (o == null || getClass() != o.getClass()) {
/* 63 */       return false;
/*    */     }
/* 65 */     Fold<?, ?> fold = (Fold<?, ?>)o;
/* 66 */     return (Objects.equals(this.aType, fold.aType) && Objects.equals(this.algebra, fold.algebra));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 71 */     return Objects.hash(new Object[] { this.aType, this.algebra });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Fold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */