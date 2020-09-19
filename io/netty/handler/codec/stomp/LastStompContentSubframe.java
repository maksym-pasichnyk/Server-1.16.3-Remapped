/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public interface LastStompContentSubframe
/*    */   extends StompContentSubframe
/*    */ {
/* 31 */   public static final LastStompContentSubframe EMPTY_LAST_CONTENT = new LastStompContentSubframe()
/*    */     {
/*    */       public ByteBuf content() {
/* 34 */         return Unpooled.EMPTY_BUFFER;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe copy() {
/* 39 */         return EMPTY_LAST_CONTENT;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe duplicate() {
/* 44 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe retainedDuplicate() {
/* 49 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe replace(ByteBuf content) {
/* 54 */         return new DefaultLastStompContentSubframe(content);
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe retain() {
/* 59 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe retain(int increment) {
/* 64 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe touch() {
/* 69 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public LastStompContentSubframe touch(Object hint) {
/* 74 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public int refCnt() {
/* 79 */         return 1;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean release() {
/* 84 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean release(int decrement) {
/* 89 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public DecoderResult decoderResult() {
/* 94 */         return DecoderResult.SUCCESS;
/*    */       }
/*    */ 
/*    */       
/*    */       public void setDecoderResult(DecoderResult result) {
/* 99 */         throw new UnsupportedOperationException("read only");
/*    */       }
/*    */     };
/*    */   
/*    */   LastStompContentSubframe copy();
/*    */   
/*    */   LastStompContentSubframe duplicate();
/*    */   
/*    */   LastStompContentSubframe retainedDuplicate();
/*    */   
/*    */   LastStompContentSubframe replace(ByteBuf paramByteBuf);
/*    */   
/*    */   LastStompContentSubframe retain();
/*    */   
/*    */   LastStompContentSubframe retain(int paramInt);
/*    */   
/*    */   LastStompContentSubframe touch();
/*    */   
/*    */   LastStompContentSubframe touch(Object paramObject);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\LastStompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */