/*    */ package io.netty.handler.codec.redis;
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
/*    */ 
/*    */ public class BulkStringHeaderRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final int bulkStringLength;
/*    */   
/*    */   public BulkStringHeaderRedisMessage(int bulkStringLength) {
/* 34 */     if (bulkStringLength <= 0) {
/* 35 */       throw new RedisCodecException("bulkStringLength: " + bulkStringLength + " (expected: > 0)");
/*    */     }
/* 37 */     this.bulkStringLength = bulkStringLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int bulkStringLength() {
/* 44 */     return this.bulkStringLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNull() {
/* 53 */     return (this.bulkStringLength == -1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\BulkStringHeaderRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */