/*     */ package io.netty.buffer;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UnpooledUnsafeHeapByteBuf
/*     */   extends UnpooledHeapByteBuf
/*     */ {
/*     */   UnpooledUnsafeHeapByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
/*  29 */     super(alloc, initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] allocateArray(int initialCapacity) {
/*  34 */     return PlatformDependent.allocateUninitializedArray(initialCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/*  39 */     checkIndex(index);
/*  40 */     return _getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  45 */     return UnsafeByteBufUtil.getByte(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/*  50 */     checkIndex(index, 2);
/*  51 */     return _getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  56 */     return UnsafeByteBufUtil.getShort(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortLE(int index) {
/*  61 */     checkIndex(index, 2);
/*  62 */     return _getShortLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/*  67 */     return UnsafeByteBufUtil.getShortLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/*  72 */     checkIndex(index, 3);
/*  73 */     return _getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  78 */     return UnsafeByteBufUtil.getUnsignedMedium(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMediumLE(int index) {
/*  83 */     checkIndex(index, 3);
/*  84 */     return _getUnsignedMediumLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/*  89 */     return UnsafeByteBufUtil.getUnsignedMediumLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/*  94 */     checkIndex(index, 4);
/*  95 */     return _getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 100 */     return UnsafeByteBufUtil.getInt(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntLE(int index) {
/* 105 */     checkIndex(index, 4);
/* 106 */     return _getIntLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 111 */     return UnsafeByteBufUtil.getIntLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 116 */     checkIndex(index, 8);
/* 117 */     return _getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 122 */     return UnsafeByteBufUtil.getLong(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongLE(int index) {
/* 127 */     checkIndex(index, 8);
/* 128 */     return _getLongLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 133 */     return UnsafeByteBufUtil.getLongLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 138 */     checkIndex(index);
/* 139 */     _setByte(index, value);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 145 */     UnsafeByteBufUtil.setByte(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 150 */     checkIndex(index, 2);
/* 151 */     _setShort(index, value);
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 157 */     UnsafeByteBufUtil.setShort(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShortLE(int index, int value) {
/* 162 */     checkIndex(index, 2);
/* 163 */     _setShortLE(index, value);
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 169 */     UnsafeByteBufUtil.setShortLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 174 */     checkIndex(index, 3);
/* 175 */     _setMedium(index, value);
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 181 */     UnsafeByteBufUtil.setMedium(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMediumLE(int index, int value) {
/* 186 */     checkIndex(index, 3);
/* 187 */     _setMediumLE(index, value);
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 193 */     UnsafeByteBufUtil.setMediumLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 198 */     checkIndex(index, 4);
/* 199 */     _setInt(index, value);
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 205 */     UnsafeByteBufUtil.setInt(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setIntLE(int index, int value) {
/* 210 */     checkIndex(index, 4);
/* 211 */     _setIntLE(index, value);
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 217 */     UnsafeByteBufUtil.setIntLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 222 */     checkIndex(index, 8);
/* 223 */     _setLong(index, value);
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 229 */     UnsafeByteBufUtil.setLong(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLongLE(int index, long value) {
/* 234 */     checkIndex(index, 8);
/* 235 */     _setLongLE(index, value);
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 241 */     UnsafeByteBufUtil.setLongLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setZero(int index, int length) {
/* 246 */     if (PlatformDependent.javaVersion() >= 7) {
/*     */       
/* 248 */       checkIndex(index, length);
/* 249 */       UnsafeByteBufUtil.setZero(this.array, index, length);
/* 250 */       return this;
/*     */     } 
/* 252 */     return super.setZero(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf writeZero(int length) {
/* 257 */     if (PlatformDependent.javaVersion() >= 7) {
/*     */       
/* 259 */       ensureWritable(length);
/* 260 */       int wIndex = this.writerIndex;
/* 261 */       UnsafeByteBufUtil.setZero(this.array, wIndex, length);
/* 262 */       this.writerIndex = wIndex + length;
/* 263 */       return this;
/*     */     } 
/* 265 */     return super.writeZero(length);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SwappedByteBuf newSwappedByteBuf() {
/* 271 */     if (PlatformDependent.isUnaligned())
/*     */     {
/* 273 */       return new UnsafeHeapSwappedByteBuf(this);
/*     */     }
/* 275 */     return super.newSwappedByteBuf();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnpooledUnsafeHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */