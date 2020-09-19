/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.util.ReferenceCounted;
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
/*    */ public interface LastBulkStringRedisContent
/*    */   extends BulkStringRedisContent
/*    */ {
/* 31 */   public static final LastBulkStringRedisContent EMPTY_LAST_CONTENT = new LastBulkStringRedisContent()
/*    */     {
/*    */       public ByteBuf content()
/*    */       {
/* 35 */         return Unpooled.EMPTY_BUFFER;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent copy() {
/* 40 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent duplicate() {
/* 45 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent retainedDuplicate() {
/* 50 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent replace(ByteBuf content) {
/* 55 */         return new DefaultLastBulkStringRedisContent(content);
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent retain(int increment) {
/* 60 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent retain() {
/* 65 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public int refCnt() {
/* 70 */         return 1;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent touch() {
/* 75 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastBulkStringRedisContent touch(Object hint) {
/* 80 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean release() {
/* 85 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean release(int decrement) {
/* 90 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   LastBulkStringRedisContent copy();
/*    */   
/*    */   LastBulkStringRedisContent duplicate();
/*    */   
/*    */   LastBulkStringRedisContent retainedDuplicate();
/*    */   
/*    */   LastBulkStringRedisContent replace(ByteBuf paramByteBuf);
/*    */   
/*    */   LastBulkStringRedisContent retain();
/*    */   
/*    */   LastBulkStringRedisContent retain(int paramInt);
/*    */   
/*    */   LastBulkStringRedisContent touch();
/*    */   
/*    */   LastBulkStringRedisContent touch(Object paramObject);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\LastBulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */