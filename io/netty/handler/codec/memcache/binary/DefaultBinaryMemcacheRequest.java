/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.memcache.MemcacheMessage;
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
/*    */ 
/*    */ 
/*    */ public class DefaultBinaryMemcacheRequest
/*    */   extends AbstractBinaryMemcacheMessage
/*    */   implements BinaryMemcacheRequest
/*    */ {
/*    */   public static final byte REQUEST_MAGIC_BYTE = -128;
/*    */   private short reserved;
/*    */   
/*    */   public DefaultBinaryMemcacheRequest() {
/* 38 */     this(null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultBinaryMemcacheRequest(ByteBuf key) {
/* 47 */     this(key, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultBinaryMemcacheRequest(ByteBuf key, ByteBuf extras) {
/* 57 */     super(key, extras);
/* 58 */     setMagic(-128);
/*    */   }
/*    */ 
/*    */   
/*    */   public short reserved() {
/* 63 */     return this.reserved;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheRequest setReserved(short reserved) {
/* 68 */     this.reserved = reserved;
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheRequest retain() {
/* 74 */     super.retain();
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheRequest retain(int increment) {
/* 80 */     super.retain(increment);
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheRequest touch() {
/* 86 */     super.touch();
/* 87 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheRequest touch(Object hint) {
/* 92 */     super.touch(hint);
/* 93 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\DefaultBinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */