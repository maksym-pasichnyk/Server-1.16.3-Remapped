/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
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
/*    */ public class DefaultLastStompContentSubframe
/*    */   extends DefaultStompContentSubframe
/*    */   implements LastStompContentSubframe
/*    */ {
/*    */   public DefaultLastStompContentSubframe(ByteBuf content) {
/* 26 */     super(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe copy() {
/* 31 */     return (LastStompContentSubframe)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe duplicate() {
/* 36 */     return (LastStompContentSubframe)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe retainedDuplicate() {
/* 41 */     return (LastStompContentSubframe)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe replace(ByteBuf content) {
/* 46 */     return new DefaultLastStompContentSubframe(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultLastStompContentSubframe retain() {
/* 51 */     super.retain();
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe retain(int increment) {
/* 57 */     super.retain(increment);
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe touch() {
/* 63 */     super.touch();
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastStompContentSubframe touch(Object hint) {
/* 69 */     super.touch(hint);
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "DefaultLastStompContent{decoderResult=" + 
/* 76 */       decoderResult() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\DefaultLastStompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */