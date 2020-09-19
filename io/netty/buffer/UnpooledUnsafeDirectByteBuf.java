/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class UnpooledUnsafeDirectByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final ByteBufAllocator alloc;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   private int capacity;
/*     */   private boolean doNotFree;
/*     */   ByteBuffer buffer;
/*     */   long memoryAddress;
/*     */   
/*     */   public UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
/*  52 */     super(maxCapacity);
/*  53 */     if (alloc == null) {
/*  54 */       throw new NullPointerException("alloc");
/*     */     }
/*  56 */     if (initialCapacity < 0) {
/*  57 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
/*     */     }
/*  59 */     if (maxCapacity < 0) {
/*  60 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
/*     */     }
/*  62 */     if (initialCapacity > maxCapacity) {
/*  63 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/*  64 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/*  67 */     this.alloc = alloc;
/*  68 */     setByteBuffer(allocateDirect(initialCapacity), false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity) {
/*  86 */     this(alloc, initialBuffer.slice(), maxCapacity, false);
/*     */   }
/*     */   
/*     */   UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity, boolean doFree) {
/*  90 */     super(maxCapacity);
/*  91 */     if (alloc == null) {
/*  92 */       throw new NullPointerException("alloc");
/*     */     }
/*  94 */     if (initialBuffer == null) {
/*  95 */       throw new NullPointerException("initialBuffer");
/*     */     }
/*  97 */     if (!initialBuffer.isDirect()) {
/*  98 */       throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
/*     */     }
/* 100 */     if (initialBuffer.isReadOnly()) {
/* 101 */       throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
/*     */     }
/*     */     
/* 104 */     int initialCapacity = initialBuffer.remaining();
/* 105 */     if (initialCapacity > maxCapacity) {
/* 106 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/* 107 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/* 110 */     this.alloc = alloc;
/* 111 */     this.doNotFree = !doFree;
/* 112 */     setByteBuffer(initialBuffer.order(ByteOrder.BIG_ENDIAN), false);
/* 113 */     writerIndex(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuffer allocateDirect(int initialCapacity) {
/* 120 */     return ByteBuffer.allocateDirect(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void freeDirect(ByteBuffer buffer) {
/* 127 */     PlatformDependent.freeDirectBuffer(buffer);
/*     */   }
/*     */   
/*     */   final void setByteBuffer(ByteBuffer buffer, boolean tryFree) {
/* 131 */     if (tryFree) {
/* 132 */       ByteBuffer oldBuffer = this.buffer;
/* 133 */       if (oldBuffer != null) {
/* 134 */         if (this.doNotFree) {
/* 135 */           this.doNotFree = false;
/*     */         } else {
/* 137 */           freeDirect(oldBuffer);
/*     */         } 
/*     */       }
/*     */     } 
/* 141 */     this.buffer = buffer;
/* 142 */     this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
/* 143 */     this.tmpNioBuf = null;
/* 144 */     this.capacity = buffer.remaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 154 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/* 159 */     checkNewCapacity(newCapacity);
/*     */     
/* 161 */     int readerIndex = readerIndex();
/* 162 */     int writerIndex = writerIndex();
/*     */     
/* 164 */     int oldCapacity = this.capacity;
/* 165 */     if (newCapacity > oldCapacity) {
/* 166 */       ByteBuffer oldBuffer = this.buffer;
/* 167 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 168 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 169 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 170 */       newBuffer.put(oldBuffer);
/* 171 */       newBuffer.clear();
/* 172 */       setByteBuffer(newBuffer, true);
/* 173 */     } else if (newCapacity < oldCapacity) {
/* 174 */       ByteBuffer oldBuffer = this.buffer;
/* 175 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 176 */       if (readerIndex < newCapacity) {
/* 177 */         if (writerIndex > newCapacity) {
/* 178 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 180 */         oldBuffer.position(readerIndex).limit(writerIndex);
/* 181 */         newBuffer.position(readerIndex).limit(writerIndex);
/* 182 */         newBuffer.put(oldBuffer);
/* 183 */         newBuffer.clear();
/*     */       } else {
/* 185 */         setIndex(newCapacity, newCapacity);
/*     */       } 
/* 187 */       setByteBuffer(newBuffer, true);
/*     */     } 
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator alloc() {
/* 194 */     return this.alloc;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 199 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 209 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 214 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 219 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 224 */     ensureAccessible();
/* 225 */     return this.memoryAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 230 */     return UnsafeByteBufUtil.getByte(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 235 */     return UnsafeByteBufUtil.getShort(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 240 */     return UnsafeByteBufUtil.getShortLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 245 */     return UnsafeByteBufUtil.getUnsignedMedium(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 250 */     return UnsafeByteBufUtil.getUnsignedMediumLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 255 */     return UnsafeByteBufUtil.getInt(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 260 */     return UnsafeByteBufUtil.getIntLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 265 */     return UnsafeByteBufUtil.getLong(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 270 */     return UnsafeByteBufUtil.getLongLE(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 275 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst, dstIndex, length);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 281 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst, dstIndex, length);
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 287 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, dst);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst) {
/* 293 */     int length = dst.remaining();
/* 294 */     checkReadableBytes(length);
/* 295 */     getBytes(this.readerIndex, dst);
/* 296 */     this.readerIndex += length;
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 302 */     UnsafeByteBufUtil.setByte(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 307 */     UnsafeByteBufUtil.setShort(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 312 */     UnsafeByteBufUtil.setShortLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 317 */     UnsafeByteBufUtil.setMedium(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 322 */     UnsafeByteBufUtil.setMediumLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 327 */     UnsafeByteBufUtil.setInt(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 332 */     UnsafeByteBufUtil.setIntLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 337 */     UnsafeByteBufUtil.setLong(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 342 */     UnsafeByteBufUtil.setLongLE(addr(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 347 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src, srcIndex, length);
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 353 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src, srcIndex, length);
/* 354 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 359 */     UnsafeByteBufUtil.setBytes(this, addr(index), index, src);
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 365 */     UnsafeByteBufUtil.getBytes(this, addr(index), index, out, length);
/* 366 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 371 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 375 */     ensureAccessible();
/* 376 */     if (length == 0) {
/* 377 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 381 */     if (internal) {
/* 382 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 384 */       tmpBuf = this.buffer.duplicate();
/*     */     } 
/* 386 */     tmpBuf.clear().position(index).limit(index + length);
/* 387 */     return out.write(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 392 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 396 */     ensureAccessible();
/* 397 */     if (length == 0) {
/* 398 */       return 0;
/*     */     }
/*     */     
/* 401 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : this.buffer.duplicate();
/* 402 */     tmpBuf.clear().position(index).limit(index + length);
/* 403 */     return out.write(tmpBuf, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 408 */     checkReadableBytes(length);
/* 409 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 410 */     this.readerIndex += readBytes;
/* 411 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 416 */     checkReadableBytes(length);
/* 417 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 418 */     this.readerIndex += readBytes;
/* 419 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 424 */     return UnsafeByteBufUtil.setBytes(this, addr(index), index, in, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 429 */     ensureAccessible();
/* 430 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 431 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 433 */       return in.read(tmpBuf);
/* 434 */     } catch (ClosedChannelException ignored) {
/* 435 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 441 */     ensureAccessible();
/* 442 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 443 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 445 */       return in.read(tmpBuf, position);
/* 446 */     } catch (ClosedChannelException ignored) {
/* 447 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 453 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 458 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 463 */     return UnsafeByteBufUtil.copy(this, addr(index), index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 468 */     checkIndex(index, length);
/* 469 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 473 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 474 */     if (tmpNioBuf == null) {
/* 475 */       this.tmpNioBuf = tmpNioBuf = this.buffer.duplicate();
/*     */     }
/* 477 */     return tmpNioBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 482 */     checkIndex(index, length);
/* 483 */     return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 488 */     ByteBuffer buffer = this.buffer;
/* 489 */     if (buffer == null) {
/*     */       return;
/*     */     }
/*     */     
/* 493 */     this.buffer = null;
/*     */     
/* 495 */     if (!this.doNotFree) {
/* 496 */       freeDirect(buffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf unwrap() {
/* 502 */     return null;
/*     */   }
/*     */   
/*     */   long addr(int index) {
/* 506 */     return this.memoryAddress + index;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SwappedByteBuf newSwappedByteBuf() {
/* 511 */     if (PlatformDependent.isUnaligned())
/*     */     {
/* 513 */       return new UnsafeDirectSwappedByteBuf(this);
/*     */     }
/* 515 */     return super.newSwappedByteBuf();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setZero(int index, int length) {
/* 520 */     checkIndex(index, length);
/* 521 */     UnsafeByteBufUtil.setZero(addr(index), length);
/* 522 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf writeZero(int length) {
/* 527 */     ensureWritable(length);
/* 528 */     int wIndex = this.writerIndex;
/* 529 */     UnsafeByteBufUtil.setZero(addr(wIndex), length);
/* 530 */     this.writerIndex = wIndex + length;
/* 531 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnpooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */