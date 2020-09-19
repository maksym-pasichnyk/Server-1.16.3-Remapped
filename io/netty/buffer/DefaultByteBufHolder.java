/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class DefaultByteBufHolder
/*     */   implements ByteBufHolder
/*     */ {
/*     */   private final ByteBuf data;
/*     */   
/*     */   public DefaultByteBufHolder(ByteBuf data) {
/*  30 */     if (data == null) {
/*  31 */       throw new NullPointerException("data");
/*     */     }
/*  33 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  38 */     if (this.data.refCnt() <= 0) {
/*  39 */       throw new IllegalReferenceCountException(this.data.refCnt());
/*     */     }
/*  41 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufHolder copy() {
/*  51 */     return replace(this.data.copy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufHolder duplicate() {
/*  61 */     return replace(this.data.duplicate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufHolder retainedDuplicate() {
/*  71 */     return replace(this.data.retainedDuplicate());
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
/*     */   public ByteBufHolder replace(ByteBuf content) {
/*  83 */     return new DefaultByteBufHolder(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  88 */     return this.data.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufHolder retain() {
/*  93 */     this.data.retain();
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufHolder retain(int increment) {
/*  99 */     this.data.retain(increment);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufHolder touch() {
/* 105 */     this.data.touch();
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufHolder touch(Object hint) {
/* 111 */     this.data.touch(hint);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 117 */     return this.data.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 122 */     return this.data.release(decrement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String contentToString() {
/* 130 */     return this.data.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return StringUtil.simpleClassName(this) + '(' + contentToString() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 140 */     if (this == o) {
/* 141 */       return true;
/*     */     }
/* 143 */     if (o instanceof ByteBufHolder) {
/* 144 */       return this.data.equals(((ByteBufHolder)o).content());
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 151 */     return this.data.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\DefaultByteBufHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */