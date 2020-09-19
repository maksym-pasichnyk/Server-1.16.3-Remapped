/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ 
/*    */ public final class Inj1<F, G, F2>
/*    */   implements Prism<Either<F, G>, Either<F2, G>, F, F2>
/*    */ {
/*    */   public Either<Either<F2, G>, F> match(Either<F, G> either) {
/* 10 */     return (Either<Either<F2, G>, F>)either.map(Either::right, g -> Either.left(Either.right(g)));
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<F2, G> build(F2 f2) {
/* 15 */     return Either.left(f2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return "inj1";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 25 */     return obj instanceof Inj1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Inj1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */