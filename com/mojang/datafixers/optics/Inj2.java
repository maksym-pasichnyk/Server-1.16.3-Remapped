/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ 
/*    */ public final class Inj2<F, G, G2>
/*    */   implements Prism<Either<F, G>, Either<F, G2>, G, G2>
/*    */ {
/*    */   public Either<Either<F, G2>, G> match(Either<F, G> either) {
/* 10 */     return (Either<Either<F, G2>, G>)either.map(f -> Either.left(Either.left(f)), Either::right);
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<F, G2> build(G2 g2) {
/* 15 */     return Either.right(g2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return "inj2";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 25 */     return obj instanceof Inj2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Inj2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */