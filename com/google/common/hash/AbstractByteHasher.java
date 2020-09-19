/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CanIgnoreReturnValue
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  36 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b) {
/*  47 */     update(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b, int off, int len) {
/*  54 */     for (int i = off; i < off + len; i++) {
/*  55 */       update(b[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putByte(byte b) {
/*  61 */     update(b);
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes) {
/*  67 */     Preconditions.checkNotNull(bytes);
/*  68 */     update(bytes);
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  74 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  75 */     update(bytes, off, len);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Hasher update(int bytes) {
/*     */     try {
/*  84 */       update(this.scratch.array(), 0, bytes);
/*     */     } finally {
/*  86 */       this.scratch.clear();
/*     */     } 
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putShort(short s) {
/*  93 */     this.scratch.putShort(s);
/*  94 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putInt(int i) {
/*  99 */     this.scratch.putInt(i);
/* 100 */     return update(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putLong(long l) {
/* 105 */     this.scratch.putLong(l);
/* 106 */     return update(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putChar(char c) {
/* 111 */     this.scratch.putChar(c);
/* 112 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 117 */     funnel.funnel(instance, this);
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract void update(byte paramByte);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */