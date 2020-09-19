/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class UnpooledHeapByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final ByteBufAllocator alloc;
/*     */   byte[] array;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   public UnpooledHeapByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
/*  51 */     super(maxCapacity);
/*     */     
/*  53 */     ObjectUtil.checkNotNull(alloc, "alloc");
/*     */     
/*  55 */     if (initialCapacity > maxCapacity) {
/*  56 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/*  57 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/*  60 */     this.alloc = alloc;
/*  61 */     setArray(allocateArray(initialCapacity));
/*  62 */     setIndex(0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnpooledHeapByteBuf(ByteBufAllocator alloc, byte[] initialArray, int maxCapacity) {
/*  72 */     super(maxCapacity);
/*     */     
/*  74 */     ObjectUtil.checkNotNull(alloc, "alloc");
/*  75 */     ObjectUtil.checkNotNull(initialArray, "initialArray");
/*     */     
/*  77 */     if (initialArray.length > maxCapacity) {
/*  78 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] {
/*  79 */               Integer.valueOf(initialArray.length), Integer.valueOf(maxCapacity)
/*     */             }));
/*     */     }
/*  82 */     this.alloc = alloc;
/*  83 */     setArray(initialArray);
/*  84 */     setIndex(0, initialArray.length);
/*     */   }
/*     */   
/*     */   byte[] allocateArray(int initialCapacity) {
/*  88 */     return new byte[initialCapacity];
/*     */   }
/*     */ 
/*     */   
/*     */   void freeArray(byte[] array) {}
/*     */ 
/*     */   
/*     */   private void setArray(byte[] initialArray) {
/*  96 */     this.array = initialArray;
/*  97 */     this.tmpNioBuf = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator alloc() {
/* 102 */     return this.alloc;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 107 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 117 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/* 122 */     checkNewCapacity(newCapacity);
/*     */     
/* 124 */     int oldCapacity = this.array.length;
/* 125 */     byte[] oldArray = this.array;
/* 126 */     if (newCapacity > oldCapacity) {
/* 127 */       byte[] newArray = allocateArray(newCapacity);
/* 128 */       System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/* 129 */       setArray(newArray);
/* 130 */       freeArray(oldArray);
/* 131 */     } else if (newCapacity < oldCapacity) {
/* 132 */       byte[] newArray = allocateArray(newCapacity);
/* 133 */       int readerIndex = readerIndex();
/* 134 */       if (readerIndex < newCapacity) {
/* 135 */         int writerIndex = writerIndex();
/* 136 */         if (writerIndex > newCapacity) {
/* 137 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 139 */         System.arraycopy(oldArray, readerIndex, newArray, readerIndex, writerIndex - readerIndex);
/*     */       } else {
/* 141 */         setIndex(newCapacity, newCapacity);
/*     */       } 
/* 143 */       setArray(newArray);
/* 144 */       freeArray(oldArray);
/*     */     } 
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 156 */     ensureAccessible();
/* 157 */     return this.array;
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 162 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 177 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 178 */     if (dst.hasMemoryAddress()) {
/* 179 */       PlatformDependent.copyMemory(this.array, index, dst.memoryAddress() + dstIndex, length);
/* 180 */     } else if (dst.hasArray()) {
/* 181 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/* 183 */       dst.setBytes(dstIndex, this.array, index, length);
/*     */     } 
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 190 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 191 */     System.arraycopy(this.array, index, dst, dstIndex, length);
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 197 */     checkIndex(index, dst.remaining());
/* 198 */     dst.put(this.array, index, dst.remaining());
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 204 */     ensureAccessible();
/* 205 */     out.write(this.array, index, length);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 211 */     ensureAccessible();
/* 212 */     return getBytes(index, out, length, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 217 */     ensureAccessible();
/* 218 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 222 */     ensureAccessible();
/*     */     
/* 224 */     if (internal) {
/* 225 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 227 */       tmpBuf = ByteBuffer.wrap(this.array);
/*     */     } 
/* 229 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 233 */     ensureAccessible();
/* 234 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : ByteBuffer.wrap(this.array);
/* 235 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length), position);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 240 */     checkReadableBytes(length);
/* 241 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 242 */     this.readerIndex += readBytes;
/* 243 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 248 */     checkReadableBytes(length);
/* 249 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 250 */     this.readerIndex += readBytes;
/* 251 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 256 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 257 */     if (src.hasMemoryAddress()) {
/* 258 */       PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.array, index, length);
/* 259 */     } else if (src.hasArray()) {
/* 260 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/*     */     } else {
/* 262 */       src.getBytes(srcIndex, this.array, index, length);
/*     */     } 
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 269 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 270 */     System.arraycopy(src, srcIndex, this.array, index, length);
/* 271 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 276 */     ensureAccessible();
/* 277 */     src.get(this.array, index, src.remaining());
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 283 */     ensureAccessible();
/* 284 */     return in.read(this.array, index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 289 */     ensureAccessible();
/*     */     try {
/* 291 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length));
/* 292 */     } catch (ClosedChannelException ignored) {
/* 293 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 299 */     ensureAccessible();
/*     */     try {
/* 301 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length), position);
/* 302 */     } catch (ClosedChannelException ignored) {
/* 303 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 309 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 314 */     ensureAccessible();
/* 315 */     return ByteBuffer.wrap(this.array, index, length).slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 320 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 325 */     checkIndex(index, length);
/* 326 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 331 */     ensureAccessible();
/* 332 */     return _getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 337 */     return HeapByteBufUtil.getByte(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/* 342 */     ensureAccessible();
/* 343 */     return _getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 348 */     return HeapByteBufUtil.getShort(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortLE(int index) {
/* 353 */     ensureAccessible();
/* 354 */     return _getShortLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 359 */     return HeapByteBufUtil.getShortLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/* 364 */     ensureAccessible();
/* 365 */     return _getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 370 */     return HeapByteBufUtil.getUnsignedMedium(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMediumLE(int index) {
/* 375 */     ensureAccessible();
/* 376 */     return _getUnsignedMediumLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 381 */     return HeapByteBufUtil.getUnsignedMediumLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 386 */     ensureAccessible();
/* 387 */     return _getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 392 */     return HeapByteBufUtil.getInt(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntLE(int index) {
/* 397 */     ensureAccessible();
/* 398 */     return _getIntLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 403 */     return HeapByteBufUtil.getIntLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 408 */     ensureAccessible();
/* 409 */     return _getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 414 */     return HeapByteBufUtil.getLong(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongLE(int index) {
/* 419 */     ensureAccessible();
/* 420 */     return _getLongLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 425 */     return HeapByteBufUtil.getLongLE(this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 430 */     ensureAccessible();
/* 431 */     _setByte(index, value);
/* 432 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 437 */     HeapByteBufUtil.setByte(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 442 */     ensureAccessible();
/* 443 */     _setShort(index, value);
/* 444 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 449 */     HeapByteBufUtil.setShort(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShortLE(int index, int value) {
/* 454 */     ensureAccessible();
/* 455 */     _setShortLE(index, value);
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 461 */     HeapByteBufUtil.setShortLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 466 */     ensureAccessible();
/* 467 */     _setMedium(index, value);
/* 468 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 473 */     HeapByteBufUtil.setMedium(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMediumLE(int index, int value) {
/* 478 */     ensureAccessible();
/* 479 */     _setMediumLE(index, value);
/* 480 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 485 */     HeapByteBufUtil.setMediumLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 490 */     ensureAccessible();
/* 491 */     _setInt(index, value);
/* 492 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 497 */     HeapByteBufUtil.setInt(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setIntLE(int index, int value) {
/* 502 */     ensureAccessible();
/* 503 */     _setIntLE(index, value);
/* 504 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 509 */     HeapByteBufUtil.setIntLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 514 */     ensureAccessible();
/* 515 */     _setLong(index, value);
/* 516 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 521 */     HeapByteBufUtil.setLong(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLongLE(int index, long value) {
/* 526 */     ensureAccessible();
/* 527 */     _setLongLE(index, value);
/* 528 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 533 */     HeapByteBufUtil.setLongLE(this.array, index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 538 */     checkIndex(index, length);
/* 539 */     byte[] copiedArray = new byte[length];
/* 540 */     System.arraycopy(this.array, index, copiedArray, 0, length);
/* 541 */     return new UnpooledHeapByteBuf(alloc(), copiedArray, maxCapacity());
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 545 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 546 */     if (tmpNioBuf == null) {
/* 547 */       this.tmpNioBuf = tmpNioBuf = ByteBuffer.wrap(this.array);
/*     */     }
/* 549 */     return tmpNioBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 554 */     freeArray(this.array);
/* 555 */     this.array = EmptyArrays.EMPTY_BYTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf unwrap() {
/* 560 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnpooledHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */