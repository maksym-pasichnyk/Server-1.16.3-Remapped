/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.optics.Optic;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OpticParts<A, B>
/*    */ {
/*    */   private final Set<TypeToken<? extends K1>> bounds;
/*    */   private final Optic<?, ?, ?, A, B> optic;
/*    */   
/*    */   public OpticParts(Set<TypeToken<? extends K1>> bounds, Optic<?, ?, ?, A, B> optic) {
/* 19 */     this.bounds = bounds;
/* 20 */     this.optic = optic;
/*    */   }
/*    */   
/*    */   public Set<TypeToken<? extends K1>> bounds() {
/* 24 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public Optic<?, ?, ?, A, B> optic() {
/* 28 */     return this.optic;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\OpticParts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */