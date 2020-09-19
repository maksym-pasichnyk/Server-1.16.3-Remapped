/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Keyable;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.ToIntFunction;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface StringRepresentable
/*    */ {
/*    */   static <E extends Enum<E> & StringRepresentable> Codec<E> fromEnum(Supplier<E[]> debug0, Function<? super String, ? extends E> debug1) {
/* 22 */     Enum[] arrayOfEnum = (Enum[])debug0.get();
/* 23 */     return (Codec)fromStringResolver(Enum::ordinal, debug1 -> debug0[debug1], debug1);
/*    */   }
/*    */   
/*    */   static <E extends StringRepresentable> Codec<E> fromStringResolver(final ToIntFunction<E> idResolver, final IntFunction<E> byId, final Function<? super String, ? extends E> resolver) {
/* 27 */     return new Codec<E>()
/*    */       {
/*    */         public <T> DataResult<T> encode(E debug1, DynamicOps<T> debug2, T debug3) {
/* 30 */           if (debug2.compressMaps()) {
/* 31 */             return debug2.mergeToPrimitive(debug3, debug2.createInt(idResolver.applyAsInt(debug1)));
/*    */           }
/* 33 */           return debug2.mergeToPrimitive(debug3, debug2.createString(debug1.getSerializedName()));
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> debug1, T debug2) {
/* 38 */           if (debug1.compressMaps()) {
/* 39 */             return debug1.getNumberValue(debug2).flatMap(debug1 -> (DataResult)Optional.ofNullable(debug0.apply(debug1.intValue())).map(DataResult::success).orElseGet(()))
/*    */ 
/*    */               
/* 42 */               .map(debug1 -> Pair.of(debug1, debug0.empty()));
/*    */           }
/* 44 */           return debug1.getStringValue(debug2).flatMap(debug1 -> (DataResult)Optional.ofNullable(debug0.apply(debug1)).map(DataResult::success).orElseGet(()))
/*    */ 
/*    */             
/* 47 */             .map(debug1 -> Pair.of(debug1, debug0.empty()));
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 52 */           return "StringRepresentable[" + idResolver + "]";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static Keyable keys(final StringRepresentable[] values) {
/* 58 */     return new Keyable()
/*    */       {
/*    */         public <T> Stream<T> keys(DynamicOps<T> debug1) {
/* 61 */           if (debug1.compressMaps()) {
/* 62 */             return IntStream.range(0, values.length).mapToObj(debug1::createInt);
/*    */           }
/* 64 */           return Arrays.<StringRepresentable>stream(values).map(StringRepresentable::getSerializedName).map(debug1::createString);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String getSerializedName();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\StringRepresentable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */