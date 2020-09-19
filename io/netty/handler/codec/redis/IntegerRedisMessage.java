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
/*    */ 
/*    */ 
/*    */ public final class IntegerRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final long value;
/*    */   
/*    */   public IntegerRedisMessage(long value) {
/* 35 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long value() {
/* 44 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return StringUtil.simpleClassName(this) + '[' + 
/* 50 */       "value=" + 
/* 51 */       this.value + 
/* 52 */       ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\IntegerRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */