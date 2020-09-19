/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.buffer.DefaultByteBufHolder;
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
/*    */ public class DefaultStompContentSubframe
/*    */   extends DefaultByteBufHolder
/*    */   implements StompContentSubframe
/*    */ {
/* 27 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */   
/*    */   public DefaultStompContentSubframe(ByteBuf content) {
/* 30 */     super(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe copy() {
/* 35 */     return (StompContentSubframe)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe duplicate() {
/* 40 */     return (StompContentSubframe)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe retainedDuplicate() {
/* 45 */     return (StompContentSubframe)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe replace(ByteBuf content) {
/* 50 */     return new DefaultStompContentSubframe(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe retain() {
/* 55 */     super.retain();
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe retain(int increment) {
/* 61 */     super.retain(increment);
/* 62 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe touch() {
/* 67 */     super.touch();
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public StompContentSubframe touch(Object hint) {
/* 73 */     super.touch(hint);
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DecoderResult decoderResult() {
/* 79 */     return this.decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDecoderResult(DecoderResult decoderResult) {
/* 84 */     this.decoderResult = decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return "DefaultStompContent{decoderResult=" + this.decoderResult + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\DefaultStompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */