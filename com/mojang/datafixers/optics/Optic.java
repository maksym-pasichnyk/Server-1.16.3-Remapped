/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Optic<Proof extends K1, S, T, A, B>
/*    */ {
/*    */   <P extends com.mojang.datafixers.kinds.K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> paramApp);
/*    */   
/*    */   default <Proof2 extends Proof, A1, B1> Optic<Proof2, S, T, A1, B1> compose(Optic<? super Proof2, A, B, A1, B1> optic) {
/* 20 */     return new CompositionOptic<>(this, optic);
/*    */   }
/*    */ 
/*    */   
/*    */   default <Proof2 extends K1, A1, B1> Optic<?, S, T, A1, B1> composeUnchecked(Optic<?, A, B, A1, B1> optic) {
/* 25 */     return new CompositionOptic<>(this, optic);
/*    */   }
/*    */   
/*    */   public static final class CompositionOptic<Proof extends K1, S, T, A, B, A1, B1> implements Optic<Proof, S, T, A1, B1> {
/*    */     protected final Optic<? super Proof, S, T, A, B> outer;
/*    */     protected final Optic<? super Proof, A, B, A1, B1> inner;
/*    */     
/*    */     public CompositionOptic(Optic<? super Proof, S, T, A, B> outer, Optic<? super Proof, A, B, A1, B1> inner) {
/* 33 */       this.outer = outer;
/* 34 */       this.inner = inner;
/*    */     }
/*    */ 
/*    */     
/*    */     public <P extends com.mojang.datafixers.kinds.K2> Function<App2<P, A1, B1>, App2<P, S, T>> eval(App<? extends Proof, P> proof) {
/* 39 */       return this.outer.<P>eval(proof).compose(this.inner.eval(proof));
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 44 */       return "(" + this.outer + " ◦ " + this.inner + ")";
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object o) {
/* 49 */       if (this == o) {
/* 50 */         return true;
/*    */       }
/* 52 */       if (o == null || getClass() != o.getClass()) {
/* 53 */         return false;
/*    */       }
/* 55 */       CompositionOptic<?, ?, ?, ?, ?, ?, ?> that = (CompositionOptic<?, ?, ?, ?, ?, ?, ?>)o;
/* 56 */       return (Objects.equals(this.outer, that.outer) && Objects.equals(this.inner, that.inner));
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 61 */       return Objects.hash(new Object[] { this.outer, this.inner });
/*    */     }
/*    */     
/*    */     public Optic<? super Proof, S, T, A, B> outer() {
/* 65 */       return this.outer;
/*    */     }
/*    */     
/*    */     public Optic<? super Proof, A, B, A1, B1> inner() {
/* 69 */       return this.inner;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   default <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(Set<TypeToken<? extends K1>> proofBounds, TypeToken<Proof2> proof) {
/* 75 */     if (proofBounds.stream().allMatch(bound -> bound.isSupertypeOf(proof))) {
/* 76 */       return Optional.of(this);
/*    */     }
/* 78 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Optic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */