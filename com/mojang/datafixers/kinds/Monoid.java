/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Monoid<T>
/*    */ {
/*    */   T point();
/*    */   
/*    */   T add(T paramT1, T paramT2);
/*    */   
/*    */   static <T> Monoid<List<T>> listMonoid() {
/* 16 */     return (Monoid)new Monoid<List<List<T>>>()
/*    */       {
/*    */         public List<T> point() {
/* 19 */           return (List<T>)ImmutableList.of();
/*    */         }
/*    */ 
/*    */         
/*    */         public List<T> add(List<T> first, List<T> second) {
/* 24 */           ImmutableList.Builder<T> builder = ImmutableList.builder();
/* 25 */           builder.addAll(first);
/* 26 */           builder.addAll(second);
/* 27 */           return (List<T>)builder.build();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Monoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */