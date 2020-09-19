/*    */ package io.netty.handler.codec.memcache;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
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
/*    */ public class DefaultMemcacheContent
/*    */   extends AbstractMemcacheObject
/*    */   implements MemcacheContent
/*    */ {
/*    */   private final ByteBuf content;
/*    */   
/*    */   public DefaultMemcacheContent(ByteBuf content) {
/* 34 */     if (content == null) {
/* 35 */       throw new NullPointerException("Content cannot be null.");
/*    */     }
/* 37 */     this.content = content;
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuf content() {
/* 42 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent copy() {
/* 47 */     return replace(this.content.copy());
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent duplicate() {
/* 52 */     return replace(this.content.duplicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent retainedDuplicate() {
/* 57 */     return replace(this.content.retainedDuplicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent replace(ByteBuf content) {
/* 62 */     return new DefaultMemcacheContent(content);
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent retain() {
/* 67 */     super.retain();
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent retain(int increment) {
/* 73 */     super.retain(increment);
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent touch() {
/* 79 */     super.touch();
/* 80 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MemcacheContent touch(Object hint) {
/* 85 */     this.content.touch(hint);
/* 86 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void deallocate() {
/* 91 */     this.content.release();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return StringUtil.simpleClassName(this) + "(data: " + 
/* 97 */       content() + ", decoderResult: " + decoderResult() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\DefaultMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */