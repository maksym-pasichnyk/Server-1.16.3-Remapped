/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ final class PooledUnsafeDirectByteBuf
/*     */   extends PooledByteBuf<ByteBuffer>
/*     */ {
/*  32 */   private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER = new Recycler<PooledUnsafeDirectByteBuf>()
/*     */     {
/*     */       protected PooledUnsafeDirectByteBuf newObject(Recycler.Handle<PooledUnsafeDirectByteBuf> handle) {
/*  35 */         return new PooledUnsafeDirectByteBuf(handle, 0);
/*     */       }
/*     */     };
/*     */   
/*     */   static PooledUnsafeDirectByteBuf newInstance(int maxCapacity) {
/*  40 */     PooledUnsafeDirectByteBuf buf = (PooledUnsafeDirectByteBuf)RECYCLER.get();
/*  41 */     buf.reuse(maxCapacity);
/*  42 */     return buf;
/*     */   }
/*     */   
/*     */   private long memoryAddress;
/*     */   
/*     */   private PooledUnsafeDirectByteBuf(Recycler.Handle<PooledUnsafeDirectByteBuf> recyclerHandle, int maxCapacity) {
/*  48 */     super((Recycler.Handle)recyclerHandle, maxCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void init(PoolChunk<ByteBuffer> chunk, long handle, int offset, int length, int maxLength, PoolThreadCache cache) {
/*  54 */     super.init(chunk, handle, offset, length, maxLength, cache);
/*  55 */     initMemoryAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   void initUnpooled(PoolChunk<ByteBuffer> chunk, int length) {
/*  60 */     super.initUnpooled(chunk, length);
/*  61 */     initMemoryAddress();
/*     */   }
/*     */   
/*     */   private void initMemoryAddress() {
/*  65 */     this.memoryAddress = PlatformDependent.directBufferAddress(this.memory) + this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ByteBuffer newInternalNioBuffer(ByteBuffer memory) {
/*  70 */     return memory.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  80 */     return UnsafeByteBufUtil.getByte(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  85 */     return UnsafeByteBufUtil.getShort(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/*  90 */     return UnsafeByteBufUtil.getShortLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  95 */     return UnsafeByteBufUtil.getUnsignedMedium(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 100 */     return UnsafeByteBufUtil.getUnsignedMediumLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 105 */     return UnsafeByteBufUtil.getInt(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 110 */     return UnsafeByteBufUtil.getIntLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 115 */     return UnsafeByteBufUtil.getLong(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 120 */     return UnsafeByteBufUtil.getLongLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 125 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst, dstIndex, length);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 131 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst, dstIndex, length);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 137 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst);
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst) {
/* 143 */     int length = dst.remaining();
/* 144 */     checkReadableBytes(length);
/* 145 */     getBytes(this.readerIndex, dst);
/* 146 */     this.readerIndex += length;
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 152 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, out, length);
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 158 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 162 */     checkIndex(index, length);
/* 163 */     if (length == 0) {
/* 164 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 168 */     if (internal) {
/* 169 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 171 */       tmpBuf = this.memory.duplicate();
/*     */     } 
/* 173 */     index = idx(index);
/* 174 */     tmpBuf.clear().position(index).limit(index + length);
/* 175 */     return out.write(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 180 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 184 */     checkIndex(index, length);
/* 185 */     if (length == 0) {
/* 186 */       return 0;
/*     */     }
/*     */     
/* 189 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : this.memory.duplicate();
/* 190 */     index = idx(index);
/* 191 */     tmpBuf.clear().position(index).limit(index + length);
/* 192 */     return out.write(tmpBuf, position);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 198 */     checkReadableBytes(length);
/* 199 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 200 */     this.readerIndex += readBytes;
/* 201 */     return readBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 207 */     checkReadableBytes(length);
/* 208 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 209 */     this.readerIndex += readBytes;
/* 210 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 215 */     UnsafeByteBufUtil.setByte(addr(index), (byte)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 220 */     UnsafeByteBufUtil.setShort(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 225 */     UnsafeByteBufUtil.setShortLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 230 */     UnsafeByteBufUtil.setMedium(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 235 */     UnsafeByteBufUtil.setMediumLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 240 */     UnsafeByteBufUtil.setInt(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 245 */     UnsafeByteBufUtil.setIntLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 250 */     UnsafeByteBufUtil.setLong(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 255 */     UnsafeByteBufUtil.setLongLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 260 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src, srcIndex, length);
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 266 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src, srcIndex, length);
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 272 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src);
/* 273 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 278 */     return UnsafeByteBufUtil.setBytes(this, addr(index), index, in, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 283 */     checkIndex(index, length);
/* 284 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 285 */     index = idx(index);
/* 286 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 288 */       return in.read(tmpBuf);
/* 289 */     } catch (ClosedChannelException ignored) {
/* 290 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 296 */     checkIndex(index, length);
/* 297 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 298 */     index = idx(index);
/* 299 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 301 */       return in.read(tmpBuf, position);
/* 302 */     } catch (ClosedChannelException ignored) {
/* 303 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 309 */     return UnsafeByteBufUtil.copy(this, addr(index), index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 314 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 319 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 324 */     checkIndex(index, length);
/* 325 */     index = idx(index);
/* 326 */     return ((ByteBuffer)this.memory.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 331 */     checkIndex(index, length);
/* 332 */     index = idx(index);
/* 333 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 343 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 348 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 353 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 358 */     ensureAccessible();
/* 359 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */   private long addr(int index) {
/* 363 */     return this.memoryAddress + index;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SwappedByteBuf newSwappedByteBuf() {
/* 368 */     if (PlatformDependent.isUnaligned())
/*     */     {
/* 370 */       return new UnsafeDirectSwappedByteBuf(this);
/*     */     }
/* 372 */     return super.newSwappedByteBuf();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setZero(int index, int length) {
/* 377 */     checkIndex(index, length);
/* 378 */     UnsafeByteBufUtil.setZero(addr(index), length);
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf writeZero(int length) {
/* 384 */     ensureWritable(length);
/* 385 */     int wIndex = this.writerIndex;
/* 386 */     UnsafeByteBufUtil.setZero(addr(wIndex), length);
/* 387 */     this.writerIndex = wIndex + length;
/* 388 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */