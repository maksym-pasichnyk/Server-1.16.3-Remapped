/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ReadOnlyBufferException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.ScatteringByteChannel;
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
/*     */ class ReadOnlyByteBufferBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   protected final ByteBuffer buffer;
/*     */   private final ByteBufAllocator allocator;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   ReadOnlyByteBufferBuf(ByteBufAllocator allocator, ByteBuffer buffer) {
/*  41 */     super(buffer.remaining());
/*  42 */     if (!buffer.isReadOnly()) {
/*  43 */       throw new IllegalArgumentException("must be a readonly buffer: " + StringUtil.simpleClassName(buffer));
/*     */     }
/*     */     
/*  46 */     this.allocator = allocator;
/*  47 */     this.buffer = buffer.slice().order(ByteOrder.BIG_ENDIAN);
/*  48 */     writerIndex(this.buffer.limit());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {}
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(int numBytes) {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf ensureWritable(int minWritableBytes) {
/*  66 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  71 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/*  76 */     ensureAccessible();
/*  77 */     return _getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  82 */     return this.buffer.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/*  87 */     ensureAccessible();
/*  88 */     return _getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  93 */     return this.buffer.getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortLE(int index) {
/*  98 */     ensureAccessible();
/*  99 */     return _getShortLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 104 */     return ByteBufUtil.swapShort(this.buffer.getShort(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/* 109 */     ensureAccessible();
/* 110 */     return _getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 115 */     return (getByte(index) & 0xFF) << 16 | (
/* 116 */       getByte(index + 1) & 0xFF) << 8 | 
/* 117 */       getByte(index + 2) & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMediumLE(int index) {
/* 122 */     ensureAccessible();
/* 123 */     return _getUnsignedMediumLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 128 */     return getByte(index) & 0xFF | (
/* 129 */       getByte(index + 1) & 0xFF) << 8 | (
/* 130 */       getByte(index + 2) & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 135 */     ensureAccessible();
/* 136 */     return _getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 141 */     return this.buffer.getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntLE(int index) {
/* 146 */     ensureAccessible();
/* 147 */     return _getIntLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 152 */     return ByteBufUtil.swapInt(this.buffer.getInt(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 157 */     ensureAccessible();
/* 158 */     return _getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 163 */     return this.buffer.getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongLE(int index) {
/* 168 */     ensureAccessible();
/* 169 */     return _getLongLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 174 */     return ByteBufUtil.swapLong(this.buffer.getLong(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 179 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 180 */     if (dst.hasArray()) {
/* 181 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/* 182 */     } else if (dst.nioBufferCount() > 0) {
/* 183 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/* 184 */         int bbLen = bb.remaining();
/* 185 */         getBytes(index, bb);
/* 186 */         index += bbLen;
/*     */       } 
/*     */     } else {
/* 189 */       dst.setBytes(dstIndex, this, index, length);
/*     */     } 
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 196 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */     
/* 198 */     if (dstIndex < 0 || dstIndex > dst.length - length) {
/* 199 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] {
/* 200 */               Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length)
/*     */             }));
/*     */     }
/* 203 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 204 */     tmpBuf.clear().position(index).limit(index + length);
/* 205 */     tmpBuf.get(dst, dstIndex, length);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 211 */     checkIndex(index);
/* 212 */     if (dst == null) {
/* 213 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 216 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/* 217 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 218 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 219 */     dst.put(tmpBuf);
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 225 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 230 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 235 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 240 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShortLE(int index, int value) {
/* 245 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 250 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 255 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 260 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMediumLE(int index, int value) {
/* 265 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 270 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 275 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 280 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setIntLE(int index, int value) {
/* 285 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 290 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 295 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 300 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLongLE(int index, long value) {
/* 305 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 310 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 315 */     return maxCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/* 320 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator alloc() {
/* 325 */     return this.allocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 330 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf unwrap() {
/* 335 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 340 */     return this.buffer.isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/* 345 */     return this.buffer.isDirect();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 350 */     ensureAccessible();
/* 351 */     if (length == 0) {
/* 352 */       return this;
/*     */     }
/*     */     
/* 355 */     if (this.buffer.hasArray()) {
/* 356 */       out.write(this.buffer.array(), index + this.buffer.arrayOffset(), length);
/*     */     } else {
/* 358 */       byte[] tmp = new byte[length];
/* 359 */       ByteBuffer tmpBuf = internalNioBuffer();
/* 360 */       tmpBuf.clear().position(index);
/* 361 */       tmpBuf.get(tmp);
/* 362 */       out.write(tmp);
/*     */     } 
/* 364 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 369 */     ensureAccessible();
/* 370 */     if (length == 0) {
/* 371 */       return 0;
/*     */     }
/*     */     
/* 374 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 375 */     tmpBuf.clear().position(index).limit(index + length);
/* 376 */     return out.write(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 381 */     ensureAccessible();
/* 382 */     if (length == 0) {
/* 383 */       return 0;
/*     */     }
/*     */     
/* 386 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 387 */     tmpBuf.clear().position(index).limit(index + length);
/* 388 */     return out.write(tmpBuf, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 393 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 398 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 403 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 408 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 413 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 418 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected final ByteBuffer internalNioBuffer() {
/* 422 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 423 */     if (tmpNioBuf == null) {
/* 424 */       this.tmpNioBuf = tmpNioBuf = this.buffer.duplicate();
/*     */     }
/* 426 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/*     */     ByteBuffer src;
/* 431 */     ensureAccessible();
/*     */     
/*     */     try {
/* 434 */       src = (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/* 435 */     } catch (IllegalArgumentException ignored) {
/* 436 */       throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
/*     */     } 
/*     */     
/* 439 */     ByteBuf dst = src.isDirect() ? alloc().directBuffer(length) : alloc().heapBuffer(length);
/* 440 */     dst.writeBytes(src);
/* 441 */     return dst;
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 446 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 451 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 456 */     return (ByteBuffer)this.buffer.duplicate().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 461 */     ensureAccessible();
/* 462 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 467 */     return this.buffer.hasArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 472 */     return this.buffer.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 477 */     return this.buffer.arrayOffset();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 482 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 487 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\ReadOnlyByteBufferBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */