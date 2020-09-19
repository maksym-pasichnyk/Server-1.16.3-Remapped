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
/*    */ public class DefaultBinaryMemcacheResponse
/*    */   extends AbstractBinaryMemcacheMessage
/*    */   implements BinaryMemcacheResponse
/*    */ {
/*    */   public static final byte RESPONSE_MAGIC_BYTE = -127;
/*    */   private short status;
/*    */   
/*    */   public DefaultBinaryMemcacheResponse() {
/* 38 */     this(null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultBinaryMemcacheResponse(ByteBuf key) {
/* 47 */     this(key, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultBinaryMemcacheResponse(ByteBuf key, ByteBuf extras) {
/* 57 */     super(key, extras);
/* 58 */     setMagic((byte)-127);
/*    */   }
/*    */ 
/*    */   
/*    */   public short status() {
/* 63 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheResponse setStatus(short status) {
/* 68 */     this.status = status;
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheResponse retain() {
/* 74 */     super.retain();
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheResponse retain(int increment) {
/* 80 */     super.retain(increment);
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheResponse touch() {
/* 86 */     super.touch();
/* 87 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BinaryMemcacheResponse touch(Object hint) {
/* 92 */     super.touch(hint);
/* 93 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\DefaultBinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */