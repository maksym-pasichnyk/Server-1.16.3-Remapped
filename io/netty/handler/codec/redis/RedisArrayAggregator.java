/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.CodecException;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Deque;
/*    */ import java.util.List;
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
/*    */ public final class RedisArrayAggregator
/*    */   extends MessageToMessageDecoder<RedisMessage>
/*    */ {
/* 36 */   private final Deque<AggregateState> depths = new ArrayDeque<AggregateState>(4);
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, RedisMessage msg, List<Object> out) throws Exception {
/* 40 */     if (msg instanceof ArrayHeaderRedisMessage) {
/* 41 */       msg = decodeRedisArrayHeader((ArrayHeaderRedisMessage)msg);
/* 42 */       if (msg == null) {
/*    */         return;
/*    */       }
/*    */     } else {
/* 46 */       ReferenceCountUtil.retain(msg);
/*    */     } 
/*    */     
/* 49 */     while (!this.depths.isEmpty()) {
/* 50 */       AggregateState current = this.depths.peek();
/* 51 */       current.children.add(msg);
/*    */ 
/*    */       
/* 54 */       if (current.children.size() == current.length) {
/* 55 */         msg = new ArrayRedisMessage(current.children);
/* 56 */         this.depths.pop();
/*    */         
/*    */         continue;
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     out.add(msg);
/*    */   }
/*    */   
/*    */   private RedisMessage decodeRedisArrayHeader(ArrayHeaderRedisMessage header) {
/* 67 */     if (header.isNull())
/* 68 */       return ArrayRedisMessage.NULL_INSTANCE; 
/* 69 */     if (header.length() == 0L)
/* 70 */       return ArrayRedisMessage.EMPTY_INSTANCE; 
/* 71 */     if (header.length() > 0L) {
/*    */       
/* 73 */       if (header.length() > 2147483647L) {
/* 74 */         throw new CodecException("this codec doesn't support longer length than 2147483647");
/*    */       }
/*    */ 
/*    */       
/* 78 */       this.depths.push(new AggregateState((int)header.length()));
/* 79 */       return null;
/*    */     } 
/* 81 */     throw new CodecException("bad length: " + header.length());
/*    */   }
/*    */   
/*    */   private static final class AggregateState {
/*    */     private final int length;
/*    */     private final List<RedisMessage> children;
/*    */     
/*    */     AggregateState(int length) {
/* 89 */       this.length = length;
/* 90 */       this.children = new ArrayList<RedisMessage>(length);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisArrayAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */