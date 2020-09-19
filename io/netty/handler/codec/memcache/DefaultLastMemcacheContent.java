/*    */ package io.netty.handler.codec.memcache;
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
/*    */ public class DefaultLastMemcacheContent
/*    */   extends DefaultMemcacheContent
/*    */   implements LastMemcacheContent
/*    */ {
/*    */   public DefaultLastMemcacheContent() {
/* 30 */     super(Unpooled.buffer());
/*    */   }
/*    */   
/*    */   public DefaultLastMemcacheContent(ByteBuf content) {
/* 34 */     super(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent retain() {
/* 39 */     super.retain();
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent retain(int increment) {
/* 45 */     super.retain(increment);
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent touch() {
/* 51 */     super.touch();
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent touch(Object hint) {
/* 57 */     super.touch(hint);
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent copy() {
/* 63 */     return replace(content().copy());
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent duplicate() {
/* 68 */     return replace(content().duplicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent retainedDuplicate() {
/* 73 */     return replace(content().retainedDuplicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public LastMemcacheContent replace(ByteBuf content) {
/* 78 */     return new DefaultLastMemcacheContent(content);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\DefaultLastMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */