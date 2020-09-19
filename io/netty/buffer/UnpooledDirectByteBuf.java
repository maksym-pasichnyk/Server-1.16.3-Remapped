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
/*     */ public class UnpooledDirectByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final ByteBufAllocator alloc;
/*     */   private ByteBuffer buffer;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   private int capacity;
/*     */   private boolean doNotFree;
/*     */   
/*     */   public UnpooledDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
/*  51 */     super(maxCapacity);
/*  52 */     if (alloc == null) {
/*  53 */       throw new NullPointerException("alloc");
/*     */     }
/*  55 */     if (initialCapacity < 0) {
/*  56 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
/*     */     }
/*  58 */     if (maxCapacity < 0) {
/*  59 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
/*     */     }
/*  61 */     if (initialCapacity > maxCapacity) {
/*  62 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/*  63 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/*  66 */     this.alloc = alloc;
/*  67 */     setByteBuffer(ByteBuffer.allocateDirect(initialCapacity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnpooledDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity) {
/*  76 */     super(maxCapacity);
/*  77 */     if (alloc == null) {
/*  78 */       throw new NullPointerException("alloc");
/*     */     }
/*  80 */     if (initialBuffer == null) {
/*  81 */       throw new NullPointerException("initialBuffer");
/*     */     }
/*  83 */     if (!initialBuffer.isDirect()) {
/*  84 */       throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
/*     */     }
/*  86 */     if (initialBuffer.isReadOnly()) {
/*  87 */       throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
/*     */     }
/*     */     
/*  90 */     int initialCapacity = initialBuffer.remaining();
/*  91 */     if (initialCapacity > maxCapacity) {
/*  92 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/*  93 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/*  96 */     this.alloc = alloc;
/*  97 */     this.doNotFree = true;
/*  98 */     setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
/*  99 */     writerIndex(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuffer allocateDirect(int initialCapacity) {
/* 106 */     return ByteBuffer.allocateDirect(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void freeDirect(ByteBuffer buffer) {
/* 113 */     PlatformDependent.freeDirectBuffer(buffer);
/*     */   }
/*     */   
/*     */   private void setByteBuffer(ByteBuffer buffer) {
/* 117 */     ByteBuffer oldBuffer = this.buffer;
/* 118 */     if (oldBuffer != null) {
/* 119 */       if (this.doNotFree) {
/* 120 */         this.doNotFree = false;
/*     */       } else {
/* 122 */         freeDirect(oldBuffer);
/*     */       } 
/*     */     }
/*     */     
/* 126 */     this.buffer = buffer;
/* 127 */     this.tmpNioBuf = null;
/* 128 */     this.capacity = buffer.remaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 138 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/* 143 */     checkNewCapacity(newCapacity);
/*     */     
/* 145 */     int readerIndex = readerIndex();
/* 146 */     int writerIndex = writerIndex();
/*     */     
/* 148 */     int oldCapacity = this.capacity;
/* 149 */     if (newCapacity > oldCapacity) {
/* 150 */       ByteBuffer oldBuffer = this.buffer;
/* 151 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 152 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 153 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 154 */       newBuffer.put(oldBuffer);
/* 155 */       newBuffer.clear();
/* 156 */       setByteBuffer(newBuffer);
/* 157 */     } else if (newCapacity < oldCapacity) {
/* 158 */       ByteBuffer oldBuffer = this.buffer;
/* 159 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 160 */       if (readerIndex < newCapacity) {
/* 161 */         if (writerIndex > newCapacity) {
/* 162 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 164 */         oldBuffer.position(readerIndex).limit(writerIndex);
/* 165 */         newBuffer.position(readerIndex).limit(writerIndex);
/* 166 */         newBuffer.put(oldBuffer);
/* 167 */         newBuffer.clear();
/*     */       } else {
/* 169 */         setIndex(newCapacity, newCapacity);
/*     */       } 
/* 171 */       setByteBuffer(newBuffer);
/*     */     } 
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator alloc() {
/* 178 */     return this.alloc;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 183 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 193 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 198 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 208 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 213 */     ensureAccessible();
/* 214 */     return _getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 219 */     return this.buffer.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/* 224 */     ensureAccessible();
/* 225 */     return _getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 230 */     return this.buffer.getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 235 */     return ByteBufUtil.swapShort(this.buffer.getShort(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/* 240 */     ensureAccessible();
/* 241 */     return _getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 246 */     return (getByte(index) & 0xFF) << 16 | (
/* 247 */       getByte(index + 1) & 0xFF) << 8 | 
/* 248 */       getByte(index + 2) & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 253 */     return getByte(index) & 0xFF | (
/* 254 */       getByte(index + 1) & 0xFF) << 8 | (
/* 255 */       getByte(index + 2) & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 260 */     ensureAccessible();
/* 261 */     return _getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 266 */     return this.buffer.getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 271 */     return ByteBufUtil.swapInt(this.buffer.getInt(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 276 */     ensureAccessible();
/* 277 */     return _getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 282 */     return this.buffer.getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 287 */     return ByteBufUtil.swapLong(this.buffer.getLong(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 292 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 293 */     if (dst.hasArray()) {
/* 294 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/* 295 */     } else if (dst.nioBufferCount() > 0) {
/* 296 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/* 297 */         int bbLen = bb.remaining();
/* 298 */         getBytes(index, bb);
/* 299 */         index += bbLen;
/*     */       } 
/*     */     } else {
/* 302 */       dst.setBytes(dstIndex, this, index, length);
/*     */     } 
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 309 */     getBytes(index, dst, dstIndex, length, false);
/* 310 */     return this;
/*     */   }
/*     */   private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
/*     */     ByteBuffer tmpBuf;
/* 314 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */ 
/*     */     
/* 317 */     if (internal) {
/* 318 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 320 */       tmpBuf = this.buffer.duplicate();
/*     */     } 
/* 322 */     tmpBuf.clear().position(index).limit(index + length);
/* 323 */     tmpBuf.get(dst, dstIndex, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 328 */     checkReadableBytes(length);
/* 329 */     getBytes(this.readerIndex, dst, dstIndex, length, true);
/* 330 */     this.readerIndex += length;
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 336 */     getBytes(index, dst, false);
/* 337 */     return this;
/*     */   }
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/*     */     ByteBuffer tmpBuf;
/* 341 */     checkIndex(index, dst.remaining());
/*     */ 
/*     */     
/* 344 */     if (internal) {
/* 345 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 347 */       tmpBuf = this.buffer.duplicate();
/*     */     } 
/* 349 */     tmpBuf.clear().position(index).limit(index + dst.remaining());
/* 350 */     dst.put(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst) {
/* 355 */     int length = dst.remaining();
/* 356 */     checkReadableBytes(length);
/* 357 */     getBytes(this.readerIndex, dst, true);
/* 358 */     this.readerIndex += length;
/* 359 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 364 */     ensureAccessible();
/* 365 */     _setByte(index, value);
/* 366 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 371 */     this.buffer.put(index, (byte)value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 376 */     ensureAccessible();
/* 377 */     _setShort(index, value);
/* 378 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 383 */     this.buffer.putShort(index, (short)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 388 */     this.buffer.putShort(index, ByteBufUtil.swapShort((short)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 393 */     ensureAccessible();
/* 394 */     _setMedium(index, value);
/* 395 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 400 */     setByte(index, (byte)(value >>> 16));
/* 401 */     setByte(index + 1, (byte)(value >>> 8));
/* 402 */     setByte(index + 2, (byte)value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 407 */     setByte(index, (byte)value);
/* 408 */     setByte(index + 1, (byte)(value >>> 8));
/* 409 */     setByte(index + 2, (byte)(value >>> 16));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 414 */     ensureAccessible();
/* 415 */     _setInt(index, value);
/* 416 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 421 */     this.buffer.putInt(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 426 */     this.buffer.putInt(index, ByteBufUtil.swapInt(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 431 */     ensureAccessible();
/* 432 */     _setLong(index, value);
/* 433 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 438 */     this.buffer.putLong(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 443 */     this.buffer.putLong(index, ByteBufUtil.swapLong(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 448 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 449 */     if (src.nioBufferCount() > 0) {
/* 450 */       for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
/* 451 */         int bbLen = bb.remaining();
/* 452 */         setBytes(index, bb);
/* 453 */         index += bbLen;
/*     */       } 
/*     */     } else {
/* 456 */       src.getBytes(srcIndex, this, index, length);
/*     */     } 
/* 458 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 463 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 464 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 465 */     tmpBuf.clear().position(index).limit(index + length);
/* 466 */     tmpBuf.put(src, srcIndex, length);
/* 467 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 472 */     ensureAccessible();
/* 473 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 474 */     if (src == tmpBuf) {
/* 475 */       src = src.duplicate();
/*     */     }
/*     */     
/* 478 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 479 */     tmpBuf.put(src);
/* 480 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 485 */     getBytes(index, out, length, false);
/* 486 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
/* 490 */     ensureAccessible();
/* 491 */     if (length == 0) {
/*     */       return;
/*     */     }
/* 494 */     ByteBufUtil.readBytes(alloc(), internal ? internalNioBuffer() : this.buffer.duplicate(), index, length, out);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 499 */     checkReadableBytes(length);
/* 500 */     getBytes(this.readerIndex, out, length, true);
/* 501 */     this.readerIndex += length;
/* 502 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 507 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 511 */     ensureAccessible();
/* 512 */     if (length == 0) {
/* 513 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 517 */     if (internal) {
/* 518 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 520 */       tmpBuf = this.buffer.duplicate();
/*     */     } 
/* 522 */     tmpBuf.clear().position(index).limit(index + length);
/* 523 */     return out.write(tmpBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 528 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 532 */     ensureAccessible();
/* 533 */     if (length == 0) {
/* 534 */       return 0;
/*     */     }
/*     */     
/* 537 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : this.buffer.duplicate();
/* 538 */     tmpBuf.clear().position(index).limit(index + length);
/* 539 */     return out.write(tmpBuf, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 544 */     checkReadableBytes(length);
/* 545 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 546 */     this.readerIndex += readBytes;
/* 547 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 552 */     checkReadableBytes(length);
/* 553 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 554 */     this.readerIndex += readBytes;
/* 555 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 560 */     ensureAccessible();
/* 561 */     if (this.buffer.hasArray()) {
/* 562 */       return in.read(this.buffer.array(), this.buffer.arrayOffset() + index, length);
/*     */     }
/* 564 */     byte[] tmp = new byte[length];
/* 565 */     int readBytes = in.read(tmp);
/* 566 */     if (readBytes <= 0) {
/* 567 */       return readBytes;
/*     */     }
/* 569 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 570 */     tmpBuf.clear().position(index);
/* 571 */     tmpBuf.put(tmp, 0, readBytes);
/* 572 */     return readBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 578 */     ensureAccessible();
/* 579 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 580 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 582 */       return in.read(this.tmpNioBuf);
/* 583 */     } catch (ClosedChannelException ignored) {
/* 584 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 590 */     ensureAccessible();
/* 591 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 592 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 594 */       return in.read(this.tmpNioBuf, position);
/* 595 */     } catch (ClosedChannelException ignored) {
/* 596 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 602 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 607 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/*     */     ByteBuffer src;
/* 612 */     ensureAccessible();
/*     */     
/*     */     try {
/* 615 */       src = (ByteBuffer)this.buffer.duplicate().clear().position(index).limit(index + length);
/* 616 */     } catch (IllegalArgumentException ignored) {
/* 617 */       throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
/*     */     } 
/*     */     
/* 620 */     return alloc().directBuffer(length, maxCapacity()).writeBytes(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 625 */     checkIndex(index, length);
/* 626 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 630 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 631 */     if (tmpNioBuf == null) {
/* 632 */       this.tmpNioBuf = tmpNioBuf = this.buffer.duplicate();
/*     */     }
/* 634 */     return tmpNioBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 639 */     checkIndex(index, length);
/* 640 */     return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 645 */     ByteBuffer buffer = this.buffer;
/* 646 */     if (buffer == null) {
/*     */       return;
/*     */     }
/*     */     
/* 650 */     this.buffer = null;
/*     */     
/* 652 */     if (!this.doNotFree) {
/* 653 */       freeDirect(buffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf unwrap() {
/* 659 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnpooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */