/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class InjTagged<K, A, B>
/*    */   implements Prism<Pair<K, ?>, Pair<K, ?>, A, B>
/*    */ {
/*    */   private final K key;
/*    */   
/*    */   public InjTagged(K key) {
/* 17 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Either<Pair<K, ?>, A> match(Pair<K, ?> pair) {
/* 23 */     return Objects.equals(this.key, pair.getFirst()) ? Either.right(pair.getSecond()) : Either.left(pair);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<K, ?> build(B b) {
/* 28 */     return Pair.of(this.key, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 33 */     return "inj[" + this.key + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 38 */     return (obj instanceof InjTagged && Objects.equals(((InjTagged)obj).key, this.key));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\InjTagged.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */