/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ final class PooledUnsafeHeapByteBuf
/*     */   extends PooledHeapByteBuf
/*     */ {
/*  24 */   private static final Recycler<PooledUnsafeHeapByteBuf> RECYCLER = new Recycler<PooledUnsafeHeapByteBuf>()
/*     */     {
/*     */       protected PooledUnsafeHeapByteBuf newObject(Recycler.Handle<PooledUnsafeHeapByteBuf> handle) {
/*  27 */         return new PooledUnsafeHeapByteBuf(handle, 0);
/*     */       }
/*     */     };
/*     */   
/*     */   static PooledUnsafeHeapByteBuf newUnsafeInstance(int maxCapacity) {
/*  32 */     PooledUnsafeHeapByteBuf buf = (PooledUnsafeHeapByteBuf)RECYCLER.get();
/*  33 */     buf.reuse(maxCapacity);
/*  34 */     return buf;
/*     */   }
/*     */   
/*     */   private PooledUnsafeHeapByteBuf(Recycler.Handle<PooledUnsafeHeapByteBuf> recyclerHandle, int maxCapacity) {
/*  38 */     super((Recycler.Handle)recyclerHandle, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  43 */     return UnsafeByteBufUtil.getByte(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  48 */     return UnsafeByteBufUtil.getShort(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/*  53 */     return UnsafeByteBufUtil.getShortLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  58 */     return UnsafeByteBufUtil.getUnsignedMedium(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/*  63 */     return UnsafeByteBufUtil.getUnsignedMediumLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/*  68 */     return UnsafeByteBufUtil.getInt(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/*  73 */     return UnsafeByteBufUtil.getIntLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/*  78 */     return UnsafeByteBufUtil.getLong(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/*  83 */     return UnsafeByteBufUtil.getLongLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/*  88 */     UnsafeByteBufUtil.setByte(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/*  93 */     UnsafeByteBufUtil.setShort(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/*  98 */     UnsafeByteBufUtil.setShortLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 103 */     UnsafeByteBufUtil.setMedium(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 108 */     UnsafeByteBufUtil.setMediumLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 113 */     UnsafeByteBufUtil.setInt(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 118 */     UnsafeByteBufUtil.setIntLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 123 */     UnsafeByteBufUtil.setLong(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 128 */     UnsafeByteBufUtil.setLongLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setZero(int index, int length) {
/* 133 */     if (PlatformDependent.javaVersion() >= 7) {
/* 134 */       checkIndex(index, length);
/*     */       
/* 136 */       UnsafeByteBufUtil.setZero(this.memory, idx(index), length);
/* 137 */       return this;
/*     */     } 
/* 139 */     return super.setZero(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf writeZero(int length) {
/* 144 */     if (PlatformDependent.javaVersion() >= 7) {
/*     */       
/* 146 */       ensureWritable(length);
/* 147 */       int wIndex = this.writerIndex;
/* 148 */       UnsafeByteBufUtil.setZero(this.memory, idx(wIndex), length);
/* 149 */       this.writerIndex = wIndex + length;
/* 150 */       return this;
/*     */     } 
/* 152 */     return super.writeZero(length);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SwappedByteBuf newSwappedByteBuf() {
/* 158 */     if (PlatformDependent.isUnaligned())
/*     */     {
/* 160 */       return new UnsafeHeapSwappedByteBuf(this);
/*     */     }
/* 162 */     return super.newSwappedByteBuf();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledUnsafeHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */