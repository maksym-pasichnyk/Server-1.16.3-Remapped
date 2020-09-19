/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataFixUtils
/*    */ {
/*    */   public static int smallestEncompassingPowerOfTwo(int input) {
/* 17 */     int result = input - 1;
/* 18 */     result |= result >> 1;
/* 19 */     result |= result >> 2;
/* 20 */     result |= result >> 4;
/* 21 */     result |= result >> 8;
/* 22 */     result |= result >> 16;
/* 23 */     return result + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean isPowerOfTwo(int input) {
/* 28 */     return (input != 0 && (input & input - 1) == 0);
/*    */   }
/*    */ 
/*    */   
/* 32 */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
/*    */ 
/*    */ 
/*    */   
/*    */   public static int ceillog2(int input) {
/* 37 */     input = isPowerOfTwo(input) ? input : smallestEncompassingPowerOfTwo(input);
/* 38 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(input * 125613361L >> 27L) & 0x1F];
/*    */   }
/*    */   
/*    */   public static <T> T make(Supplier<T> factory) {
/* 42 */     return factory.get();
/*    */   }
/*    */   
/*    */   public static <T> T make(T t, Consumer<T> consumer) {
/* 46 */     consumer.accept(t);
/* 47 */     return t;
/*    */   }
/*    */   
/*    */   public static <U> U orElse(Optional<? extends U> optional, U other) {
/* 51 */     if (optional.isPresent()) {
/* 52 */       return optional.get();
/*    */     }
/* 54 */     return other;
/*    */   }
/*    */   
/*    */   public static <U> U orElseGet(Optional<? extends U> optional, Supplier<? extends U> other) {
/* 58 */     if (optional.isPresent()) {
/* 59 */       return optional.get();
/*    */     }
/* 61 */     return other.get();
/*    */   }
/*    */   
/*    */   public static <U> Optional<U> or(Optional<? extends U> optional, Supplier<? extends Optional<? extends U>> other) {
/* 65 */     if (optional.isPresent()) {
/* 66 */       return optional.map(u -> u);
/*    */     }
/* 68 */     return ((Optional)other.get()).map(u -> u);
/*    */   }
/*    */   
/*    */   public static byte[] toArray(ByteBuffer input) {
/*    */     byte[] bytes;
/* 73 */     if (input.hasArray()) {
/* 74 */       bytes = input.array();
/*    */     } else {
/* 76 */       bytes = new byte[input.capacity()];
/* 77 */       input.get(bytes, 0, bytes.length);
/*    */     } 
/* 79 */     return bytes;
/*    */   }
/*    */   
/*    */   public static int makeKey(int version) {
/* 83 */     return makeKey(version, 0);
/*    */   }
/*    */   
/*    */   public static int makeKey(int version, int subVersion) {
/* 87 */     return version * 10 + subVersion;
/*    */   }
/*    */   
/*    */   public static int getVersion(int key) {
/* 91 */     return key / 10;
/*    */   }
/*    */   
/*    */   public static int getSubVersion(int key) {
/* 95 */     return key % 10;
/*    */   }
/*    */   
/*    */   public static <T> UnaryOperator<T> consumerToFunction(Consumer<T> consumer) {
/* 99 */     return s -> {
/*    */         consumer.accept(s);
/*    */         return s;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\DataFixUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */