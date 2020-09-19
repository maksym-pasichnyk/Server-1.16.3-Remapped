/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.Random;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UniformInt
/*    */ {
/*    */   public static final Codec<UniformInt> CODEC;
/*    */   private final int baseValue;
/*    */   private final int spread;
/*    */   
/*    */   static {
/* 27 */     CODEC = Codec.either((Codec)Codec.INT, RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("base").forGetter(()), (App)Codec.INT.fieldOf("spread").forGetter(())).apply((Applicative)debug0, UniformInt::new)).comapFlatMap(debug0 -> (debug0.spread < 0) ? DataResult.error("Spread must be non-negative, got: " + debug0.spread) : DataResult.success(debug0), Function.identity())).xmap(debug0 -> (UniformInt)debug0.map(UniformInt::fixed, ()), debug0 -> (debug0.spread == 0) ? Either.left(Integer.valueOf(debug0.baseValue)) : Either.right(debug0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Codec<UniformInt> codec(int debug0, int debug1, int debug2) {
/* 33 */     Function<UniformInt, DataResult<UniformInt>> debug3 = debug3 -> 
/* 34 */       (debug3.baseValue >= debug0 && debug3.baseValue <= debug1) ? ((debug3.spread <= debug2) ? DataResult.success(debug3) : DataResult.error("Spread too big: " + debug3.spread + " > " + debug2)) : DataResult.error("Base value out of range: " + debug3.baseValue + " [" + debug0 + "-" + debug1 + "]");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     return CODEC.flatXmap(debug3, debug3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private UniformInt(int debug1, int debug2) {
/* 49 */     this.baseValue = debug1;
/* 50 */     this.spread = debug2;
/*    */   }
/*    */   
/*    */   public static UniformInt fixed(int debug0) {
/* 54 */     return new UniformInt(debug0, 0);
/*    */   }
/*    */   
/*    */   public static UniformInt of(int debug0, int debug1) {
/* 58 */     return new UniformInt(debug0, debug1);
/*    */   }
/*    */   
/*    */   public int sample(Random debug1) {
/* 62 */     if (this.spread == 0) {
/* 63 */       return this.baseValue;
/*    */     }
/* 65 */     return this.baseValue + debug1.nextInt(this.spread + 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 70 */     if (this == debug1) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 74 */       return false;
/*    */     }
/* 76 */     UniformInt debug2 = (UniformInt)debug1;
/* 77 */     return (this.baseValue == debug2.baseValue && this.spread == debug2.spread);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     return Objects.hash(new Object[] { Integer.valueOf(this.baseValue), Integer.valueOf(this.spread) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return "[" + this.baseValue + '-' + (this.baseValue + this.spread) + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\UniformInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */