/*    */ package io.netty.handler.codec.smtp;
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
/*    */ public class DefaultSmtpContent
/*    */   extends DefaultByteBufHolder
/*    */   implements SmtpContent
/*    */ {
/*    */   public DefaultSmtpContent(ByteBuf data) {
/* 32 */     super(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent copy() {
/* 37 */     return (SmtpContent)super.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent duplicate() {
/* 42 */     return (SmtpContent)super.duplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent retainedDuplicate() {
/* 47 */     return (SmtpContent)super.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent replace(ByteBuf content) {
/* 52 */     return new DefaultSmtpContent(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent retain() {
/* 57 */     super.retain();
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent retain(int increment) {
/* 63 */     super.retain(increment);
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent touch() {
/* 69 */     super.touch();
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpContent touch(Object hint) {
/* 75 */     super.touch(hint);
/* 76 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\DefaultSmtpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */