/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.nio.ByteOrder;
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
/*     */ final class UnreleasableByteBuf
/*     */   extends WrappedByteBuf
/*     */ {
/*     */   private SwappedByteBuf swappedBuf;
/*     */   
/*     */   UnreleasableByteBuf(ByteBuf buf) {
/*  29 */     super((buf instanceof UnreleasableByteBuf) ? buf.unwrap() : buf);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf order(ByteOrder endianness) {
/*  34 */     if (endianness == null) {
/*  35 */       throw new NullPointerException("endianness");
/*     */     }
/*  37 */     if (endianness == order()) {
/*  38 */       return this;
/*     */     }
/*     */     
/*  41 */     SwappedByteBuf swappedBuf = this.swappedBuf;
/*  42 */     if (swappedBuf == null) {
/*  43 */       this.swappedBuf = swappedBuf = new SwappedByteBuf(this);
/*     */     }
/*  45 */     return swappedBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf asReadOnly() {
/*  50 */     return this.buf.isReadOnly() ? this : new UnreleasableByteBuf(this.buf.asReadOnly());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readSlice(int length) {
/*  55 */     return new UnreleasableByteBuf(this.buf.readSlice(length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf readRetainedSlice(int length) {
/*  63 */     return readSlice(length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf slice() {
/*  68 */     return new UnreleasableByteBuf(this.buf.slice());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf retainedSlice() {
/*  76 */     return slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf slice(int index, int length) {
/*  81 */     return new UnreleasableByteBuf(this.buf.slice(index, length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf retainedSlice(int index, int length) {
/*  89 */     return slice(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf duplicate() {
/*  94 */     return new UnreleasableByteBuf(this.buf.duplicate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf retainedDuplicate() {
/* 102 */     return duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retain(int increment) {
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retain() {
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf touch() {
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf touch(Object hint) {
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 132 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnreleasableByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */