/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RedisCodecUtil
/*    */ {
/*    */   static byte[] longToAsciiBytes(long value) {
/* 30 */     return Long.toString(value).getBytes(CharsetUtil.US_ASCII);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static short makeShort(char first, char second) {
/* 37 */     return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (short)(second << 8 | first) : (short)(first << 8 | second);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static byte[] shortToBytes(short value) {
/* 45 */     byte[] bytes = new byte[2];
/* 46 */     if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
/* 47 */       bytes[1] = (byte)(value >> 8 & 0xFF);
/* 48 */       bytes[0] = (byte)(value & 0xFF);
/*    */     } else {
/* 50 */       bytes[0] = (byte)(value >> 8 & 0xFF);
/* 51 */       bytes[1] = (byte)(value & 0xFF);
/*    */     } 
/* 53 */     return bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisCodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */