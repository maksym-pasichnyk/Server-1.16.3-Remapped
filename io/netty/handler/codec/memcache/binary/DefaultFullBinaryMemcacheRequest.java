/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.memcache.FullMemcacheMessage;
/*     */ import io.netty.handler.codec.memcache.LastMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.MemcacheContent;
/*     */ import io.netty.handler.codec.memcache.MemcacheMessage;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultFullBinaryMemcacheRequest
/*     */   extends DefaultBinaryMemcacheRequest
/*     */   implements FullBinaryMemcacheRequest
/*     */ {
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultFullBinaryMemcacheRequest(ByteBuf key, ByteBuf extras) {
/*  38 */     this(key, extras, Unpooled.buffer(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFullBinaryMemcacheRequest(ByteBuf key, ByteBuf extras, ByteBuf content) {
/*  50 */     super(key, extras);
/*  51 */     if (content == null) {
/*  52 */       throw new NullPointerException("Supplied content is null.");
/*     */     }
/*     */     
/*  55 */     this.content = content;
/*  56 */     setTotalBodyLength(keyLength() + extrasLength() + content.readableBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  61 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest retain() {
/*  66 */     super.retain();
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest retain(int increment) {
/*  72 */     super.retain(increment);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest touch() {
/*  78 */     super.touch();
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest touch(Object hint) {
/*  84 */     super.touch(hint);
/*  85 */     this.content.touch(hint);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/*  91 */     super.deallocate();
/*  92 */     this.content.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest copy() {
/*  97 */     ByteBuf key = key();
/*  98 */     if (key != null) {
/*  99 */       key = key.copy();
/*     */     }
/* 101 */     ByteBuf extras = extras();
/* 102 */     if (extras != null) {
/* 103 */       extras = extras.copy();
/*     */     }
/* 105 */     return new DefaultFullBinaryMemcacheRequest(key, extras, content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest duplicate() {
/* 110 */     ByteBuf key = key();
/* 111 */     if (key != null) {
/* 112 */       key = key.duplicate();
/*     */     }
/* 114 */     ByteBuf extras = extras();
/* 115 */     if (extras != null) {
/* 116 */       extras = extras.duplicate();
/*     */     }
/* 118 */     return new DefaultFullBinaryMemcacheRequest(key, extras, content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest retainedDuplicate() {
/* 123 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBinaryMemcacheRequest replace(ByteBuf content) {
/* 128 */     ByteBuf key = key();
/* 129 */     if (key != null) {
/* 130 */       key = key.retainedDuplicate();
/*     */     }
/* 132 */     ByteBuf extras = extras();
/* 133 */     if (extras != null) {
/* 134 */       extras = extras.retainedDuplicate();
/*     */     }
/* 136 */     return new DefaultFullBinaryMemcacheRequest(key, extras, content);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\DefaultFullBinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */