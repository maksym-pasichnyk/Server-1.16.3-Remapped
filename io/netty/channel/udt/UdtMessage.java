/*    */ package io.netty.channel.udt;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.buffer.DefaultByteBufHolder;
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
/*    */ 
/*    */ @Deprecated
/*    */ public final class UdtMessage
/*    */   extends DefaultByteBufHolder
/*    */ {
/*    */   public UdtMessage(ByteBuf data) {
/* 34 */     super(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage copy() {
/* 39 */     return (UdtMessage)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage duplicate() {
/* 44 */     return (UdtMessage)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage retainedDuplicate() {
/* 49 */     return (UdtMessage)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage replace(ByteBuf content) {
/* 54 */     return new UdtMessage(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage retain() {
/* 59 */     super.retain();
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage retain(int increment) {
/* 65 */     super.retain(increment);
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage touch() {
/* 71 */     super.touch();
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public UdtMessage touch(Object hint) {
/* 77 */     super.touch(hint);
/* 78 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\UdtMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */