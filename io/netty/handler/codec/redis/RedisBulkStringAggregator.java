/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.codec.MessageAggregator;
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
/*    */ public final class RedisBulkStringAggregator
/*    */   extends MessageAggregator<RedisMessage, BulkStringHeaderRedisMessage, BulkStringRedisContent, FullBulkStringRedisMessage>
/*    */ {
/*    */   public RedisBulkStringAggregator() {
/* 50 */     super(536870912);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isStartMessage(RedisMessage msg) throws Exception {
/* 55 */     return (msg instanceof BulkStringHeaderRedisMessage && !isAggregated(msg));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentMessage(RedisMessage msg) throws Exception {
/* 60 */     return msg instanceof BulkStringRedisContent;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLastContentMessage(BulkStringRedisContent msg) throws Exception {
/* 65 */     return msg instanceof LastBulkStringRedisContent;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isAggregated(RedisMessage msg) throws Exception {
/* 70 */     return msg instanceof FullBulkStringRedisMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isContentLengthInvalid(BulkStringHeaderRedisMessage start, int maxContentLength) throws Exception {
/* 76 */     return (start.bulkStringLength() > maxContentLength);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object newContinueResponse(BulkStringHeaderRedisMessage start, int maxContentLength, ChannelPipeline pipeline) throws Exception {
/* 82 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean closeAfterContinueResponse(Object msg) throws Exception {
/* 87 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean ignoreContentAfterContinueResponse(Object msg) throws Exception {
/* 92 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected FullBulkStringRedisMessage beginAggregation(BulkStringHeaderRedisMessage start, ByteBuf content) throws Exception {
/* 98 */     return new FullBulkStringRedisMessage(content);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisBulkStringAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */