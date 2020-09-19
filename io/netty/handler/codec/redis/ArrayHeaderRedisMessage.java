/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ 
/*    */ public class ArrayHeaderRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final long length;
/*    */   
/*    */   public ArrayHeaderRedisMessage(long length) {
/* 33 */     if (length < -1L) {
/* 34 */       throw new RedisCodecException("length: " + length + " (expected: >= " + -1 + ")");
/*    */     }
/* 36 */     this.length = length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long length() {
/* 43 */     return this.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNull() {
/* 52 */     return (this.length == -1L);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return StringUtil.simpleClassName(this) + '[' + 
/* 58 */       "length=" + 
/* 59 */       this.length + 
/* 60 */       ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\ArrayHeaderRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */