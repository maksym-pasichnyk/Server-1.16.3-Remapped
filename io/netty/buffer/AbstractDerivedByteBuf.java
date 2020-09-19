/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.nio.ByteBuffer;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractDerivedByteBuf
/*     */   extends AbstractByteBuf
/*     */ {
/*     */   protected AbstractDerivedByteBuf(int maxCapacity) {
/*  31 */     super(maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int refCnt() {
/*  36 */     return refCnt0();
/*     */   }
/*     */   
/*     */   int refCnt0() {
/*  40 */     return unwrap().refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retain() {
/*  45 */     return retain0();
/*     */   }
/*     */   
/*     */   ByteBuf retain0() {
/*  49 */     unwrap().retain();
/*  50 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retain(int increment) {
/*  55 */     return retain0(increment);
/*     */   }
/*     */   
/*     */   ByteBuf retain0(int increment) {
/*  59 */     unwrap().retain(increment);
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf touch() {
/*  65 */     return touch0();
/*     */   }
/*     */   
/*     */   ByteBuf touch0() {
/*  69 */     unwrap().touch();
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf touch(Object hint) {
/*  75 */     return touch0(hint);
/*     */   }
/*     */   
/*     */   ByteBuf touch0(Object hint) {
/*  79 */     unwrap().touch(hint);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean release() {
/*  85 */     return release0();
/*     */   }
/*     */   
/*     */   boolean release0() {
/*  89 */     return unwrap().release();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean release(int decrement) {
/*  94 */     return release0(decrement);
/*     */   }
/*     */   
/*     */   boolean release0(int decrement) {
/*  98 */     return unwrap().release(decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 103 */     return unwrap().isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 108 */     return nioBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 113 */     return unwrap().nioBuffer(index, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\AbstractDerivedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */