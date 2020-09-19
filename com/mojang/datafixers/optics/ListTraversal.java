/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ListTraversal<A, B>
/*    */   implements Traversal<List<A>, List<B>, A, B>
/*    */ {
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<List<A>, App<F, List<B>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> input) {
/* 16 */     return as -> {
/*    */         App<F, ImmutableList.Builder<B>> result = applicative.point(ImmutableList.builder());
/*    */         for (A a : as) {
/*    */           result = applicative.ap2(applicative.point(ImmutableList.Builder::add), result, (App)input.apply(a));
/*    */         }
/*    */         return applicative.map(ImmutableList.Builder::build, result);
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 27 */     return obj instanceof ListTraversal;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return "ListTraversal";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ListTraversal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */