/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ final class PooledDirectByteBuf
/*     */   extends PooledByteBuf<ByteBuffer>
/*     */ {
/*  32 */   private static final Recycler<PooledDirectByteBuf> RECYCLER = new Recycler<PooledDirectByteBuf>()
/*     */     {
/*     */       protected PooledDirectByteBuf newObject(Recycler.Handle<PooledDirectByteBuf> handle) {
/*  35 */         return new PooledDirectByteBuf(handle, 0);
/*     */       }
/*     */     };
/*     */   
/*     */   static PooledDirectByteBuf newInstance(int maxCapacity) {
/*  40 */     PooledDirectByteBuf buf = (PooledDirectByteBuf)RECYCLER.get();
/*  41 */     buf.reuse(maxCapacity);
/*  42 */     return buf;
/*     */   }
/*     */   
/*     */   private PooledDirectByteBuf(Recycler.Handle<PooledDirectByteBuf> recyclerHandle, int maxCapacity) {
/*  46 */     super((Recycler.Handle)recyclerHandle, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ByteBuffer newInternalNioBuffer(ByteBuffer memory) {
/*  51 */     return memory.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  61 */     return this.memory.get(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  66 */     return this.memory.getShort(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/*  71 */     return ByteBufUtil.swapShort(_getShort(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  76 */     index = idx(index);
/*  77 */     return (this.memory.get(index) & 0xFF) << 16 | (this.memory
/*  78 */       .get(index + 1) & 0xFF) << 8 | this.memory
/*  79 */       .get(index + 2) & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/*  84 */     index = idx(index);
/*  85 */     return this.memory.get(index) & 0xFF | (this.memory
/*  86 */       .get(index + 1) & 0xFF) << 8 | (this.memory
/*  87 */       .get(index + 2) & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/*  92 */     return this.memory.getInt(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/*  97 */     return ByteBufUtil.swapInt(_getInt(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 102 */     return this.memory.getLong(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 107 */     return ByteBufUtil.swapLong(_getLong(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 112 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 113 */     if (dst.hasArray()) {
/* 114 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/* 115 */     } else if (dst.nioBufferCount() > 0) {
/* 116 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/* 117 */         int bbLen = bb.remaining();
/* 118 */         getBytes(index, bb);
/* 119 */         index += bbLen;
/*     */       } 
/*     */     } else {
/* 122 */       dst.setBytes(dstIndex, this, index, length);
/*     */     } 
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 129 */     getBytes(index, dst, dstIndex, length, false);
/* 130 */     return this;
/*     */   }
/*     */   private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
/*     */     ByteBuffer tmpBuf;
/* 134 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */     
/* 136 */     if (internal) {
/* 137 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 139 */       tmpBuf = this.memory.duplicate();
/*     */     } 
/* 141 */     index = idx(index);
/* 142 */     tmpBuf.clear().position(index).limit(index + length);
/* 143 */     tmpBuf.get(dst, dstIndex, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 148 */     checkReadableBytes(length);
/* 149 */     getBytes(this.readerIndex, dst, dstIndex, length, true);
/* 150 */     this.readerIndex += length;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 156 */     getBytes(index, dst, false);
/* 157 */     return this;
/*     */   }
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/*     */     ByteBuffer tmpBuf;
/* 161 */     checkIndex(index, dst.remaining());
/*     */     
/* 163 */     if (internal) {
/* 164 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 166 */       tmpBuf = this.memory.duplicate();
/*     */     } 
/* 168 */     index = idx(index);
/* 169 */     tmpBuf.clear().position(index).limit(index + dst.remaining());
/* 170 */     dst.put(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst) {
/* 175 */     int length = dst.remaining();
/* 176 */     checkReadableBytes(length);
/* 177 */     getBytes(this.readerIndex, dst, true);
/* 178 */     this.readerIndex += length;
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 184 */     getBytes(index, out, length, false);
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
/* 189 */     checkIndex(index, length);
/* 190 */     if (length == 0) {
/*     */       return;
/*     */     }
/* 193 */     ByteBufUtil.readBytes(alloc(), internal ? internalNioBuffer() : this.memory.duplicate(), idx(index), length, out);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 198 */     checkReadableBytes(length);
/* 199 */     getBytes(this.readerIndex, out, length, true);
/* 200 */     this.readerIndex += length;
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 206 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 210 */     checkIndex(index, length);
/* 211 */     if (length == 0) {
/* 212 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 216 */     if (internal) {
/* 217 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 219 */       tmpBuf = this.memory.duplicate();
/*     */     } 
/* 221 */     index = idx(index);
/* 222 */     tmpBuf.clear().position(index).limit(index + length);
/* 223 */     return out.write(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 228 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 232 */     checkIndex(index, length);
/* 233 */     if (length == 0) {
/* 234 */       return 0;
/*     */     }
/*     */     
/* 237 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : this.memory.duplicate();
/* 238 */     index = idx(index);
/* 239 */     tmpBuf.clear().position(index).limit(index + length);
/* 240 */     return out.write(tmpBuf, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 245 */     checkReadableBytes(length);
/* 246 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 247 */     this.readerIndex += readBytes;
/* 248 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 253 */     checkReadableBytes(length);
/* 254 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 255 */     this.readerIndex += readBytes;
/* 256 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 261 */     this.memory.put(idx(index), (byte)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 266 */     this.memory.putShort(idx(index), (short)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 271 */     _setShort(index, ByteBufUtil.swapShort((short)value));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 276 */     index = idx(index);
/* 277 */     this.memory.put(index, (byte)(value >>> 16));
/* 278 */     this.memory.put(index + 1, (byte)(value >>> 8));
/* 279 */     this.memory.put(index + 2, (byte)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 284 */     index = idx(index);
/* 285 */     this.memory.put(index, (byte)value);
/* 286 */     this.memory.put(index + 1, (byte)(value >>> 8));
/* 287 */     this.memory.put(index + 2, (byte)(value >>> 16));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 292 */     this.memory.putInt(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 297 */     _setInt(index, ByteBufUtil.swapInt(value));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 302 */     this.memory.putLong(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 307 */     _setLong(index, ByteBufUtil.swapLong(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 312 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 313 */     if (src.hasArray()) {
/* 314 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/* 315 */     } else if (src.nioBufferCount() > 0) {
/* 316 */       for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
/* 317 */         int bbLen = bb.remaining();
/* 318 */         setBytes(index, bb);
/* 319 */         index += bbLen;
/*     */       } 
/*     */     } else {
/* 322 */       src.getBytes(srcIndex, this, index, length);
/*     */     } 
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 329 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 330 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 331 */     index = idx(index);
/* 332 */     tmpBuf.clear().position(index).limit(index + length);
/* 333 */     tmpBuf.put(src, srcIndex, length);
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 339 */     checkIndex(index, src.remaining());
/* 340 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 341 */     if (src == tmpBuf) {
/* 342 */       src = src.duplicate();
/*     */     }
/*     */     
/* 345 */     index = idx(index);
/* 346 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 347 */     tmpBuf.put(src);
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 353 */     checkIndex(index, length);
/* 354 */     byte[] tmp = new byte[length];
/* 355 */     int readBytes = in.read(tmp);
/* 356 */     if (readBytes <= 0) {
/* 357 */       return readBytes;
/*     */     }
/* 359 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 360 */     tmpBuf.clear().position(idx(index));
/* 361 */     tmpBuf.put(tmp, 0, readBytes);
/* 362 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 367 */     checkIndex(index, length);
/* 368 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 369 */     index = idx(index);
/* 370 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 372 */       return in.read(tmpBuf);
/* 373 */     } catch (ClosedChannelException ignored) {
/* 374 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 380 */     checkIndex(index, length);
/* 381 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 382 */     index = idx(index);
/* 383 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 385 */       return in.read(tmpBuf, position);
/* 386 */     } catch (ClosedChannelException ignored) {
/* 387 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 393 */     checkIndex(index, length);
/* 394 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 395 */     copy.writeBytes(this, index, length);
/* 396 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 401 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 406 */     checkIndex(index, length);
/* 407 */     index = idx(index);
/* 408 */     return ((ByteBuffer)this.memory.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 413 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 418 */     checkIndex(index, length);
/* 419 */     index = idx(index);
/* 420 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 425 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 430 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 435 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 445 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */