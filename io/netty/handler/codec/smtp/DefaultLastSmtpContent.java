/*    */ package io.netty.handler.codec.smtp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DefaultLastSmtpContent
/*    */   extends DefaultSmtpContent
/*    */   implements LastSmtpContent
/*    */ {
/*    */   public DefaultLastSmtpContent(ByteBuf data) {
/* 31 */     super(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public LastSmtpContent copy() {
/* 36 */     return (LastSmtpContent)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastSmtpContent duplicate() {
/* 41 */     return (LastSmtpContent)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastSmtpContent retainedDuplicate() {
/* 46 */     return (LastSmtpContent)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public LastSmtpContent replace(ByteBuf content) {
/* 51 */     return new DefaultLastSmtpContent(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultLastSmtpContent retain() {
/* 56 */     super.retain();
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultLastSmtpContent retain(int increment) {
/* 62 */     super.retain(increment);
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultLastSmtpContent touch() {
/* 68 */     super.touch();
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultLastSmtpContent touch(Object hint) {
/* 74 */     super.touch(hint);
/* 75 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\DefaultLastSmtpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */