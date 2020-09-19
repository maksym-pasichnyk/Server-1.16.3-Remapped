/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.buffer.DefaultByteBufHolder;
/*    */ import io.netty.util.ReferenceCounted;
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
/*    */ public class DefaultBulkStringRedisContent
/*    */   extends DefaultByteBufHolder
/*    */   implements BulkStringRedisContent
/*    */ {
/*    */   public DefaultBulkStringRedisContent(ByteBuf content) {
/* 35 */     super(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent copy() {
/* 40 */     return (BulkStringRedisContent)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent duplicate() {
/* 45 */     return (BulkStringRedisContent)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent retainedDuplicate() {
/* 50 */     return (BulkStringRedisContent)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent replace(ByteBuf content) {
/* 55 */     return new DefaultBulkStringRedisContent(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent retain() {
/* 60 */     super.retain();
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent retain(int increment) {
/* 66 */     super.retain(increment);
/* 67 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent touch() {
/* 72 */     super.touch();
/* 73 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BulkStringRedisContent touch(Object hint) {
/* 78 */     super.touch(hint);
/* 79 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return StringUtil.simpleClassName(this) + '[' + 
/* 85 */       "content=" + 
/*    */       
/* 87 */       content() + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\DefaultBulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */