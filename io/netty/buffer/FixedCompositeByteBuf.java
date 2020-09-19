/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.RecyclableArrayList;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ReadOnlyBufferException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.ScatteringByteChannel;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ final class FixedCompositeByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*  37 */   private static final ByteBuf[] EMPTY = new ByteBuf[] { Unpooled.EMPTY_BUFFER };
/*     */   private final int nioBufferCount;
/*     */   private final int capacity;
/*     */   private final ByteBufAllocator allocator;
/*     */   private final ByteOrder order;
/*     */   private final Object[] buffers;
/*     */   private final boolean direct;
/*     */   
/*     */   FixedCompositeByteBuf(ByteBufAllocator allocator, ByteBuf... buffers) {
/*  46 */     super(2147483647);
/*  47 */     if (buffers.length == 0) {
/*  48 */       this.buffers = (Object[])EMPTY;
/*  49 */       this.order = ByteOrder.BIG_ENDIAN;
/*  50 */       this.nioBufferCount = 1;
/*  51 */       this.capacity = 0;
/*  52 */       this.direct = false;
/*     */     } else {
/*  54 */       ByteBuf b = buffers[0];
/*  55 */       this.buffers = new Object[buffers.length];
/*  56 */       this.buffers[0] = b;
/*  57 */       boolean direct = true;
/*  58 */       int nioBufferCount = b.nioBufferCount();
/*  59 */       int capacity = b.readableBytes();
/*  60 */       this.order = b.order();
/*  61 */       for (int i = 1; i < buffers.length; i++) {
/*  62 */         b = buffers[i];
/*  63 */         if (buffers[i].order() != this.order) {
/*  64 */           throw new IllegalArgumentException("All ByteBufs need to have same ByteOrder");
/*     */         }
/*  66 */         nioBufferCount += b.nioBufferCount();
/*  67 */         capacity += b.readableBytes();
/*  68 */         if (!b.isDirect()) {
/*  69 */           direct = false;
/*     */         }
/*  71 */         this.buffers[i] = b;
/*     */       } 
/*  73 */       this.nioBufferCount = nioBufferCount;
/*  74 */       this.capacity = capacity;
/*  75 */       this.direct = direct;
/*     */     } 
/*  77 */     setIndex(0, capacity());
/*  78 */     this.allocator = allocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(int size) {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf discardReadBytes() {
/*  93 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/*  98 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 103 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 108 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 113 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 118 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 123 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 128 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 133 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 138 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 143 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 148 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 153 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 158 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 163 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 168 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 173 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 178 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) {
/* 183 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) {
/* 193 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 198 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxCapacity() {
/* 203 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/* 208 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator alloc() {
/* 213 */     return this.allocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 218 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf unwrap() {
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirect() {
/* 228 */     return this.direct;
/*     */   }
/*     */   
/*     */   private Component findComponent(int index) {
/* 232 */     int readable = 0;
/* 233 */     for (int i = 0; i < this.buffers.length; i++) {
/* 234 */       ByteBuf b; boolean isBuffer; Component comp = null;
/*     */       
/* 236 */       Object obj = this.buffers[i];
/*     */       
/* 238 */       if (obj instanceof ByteBuf) {
/* 239 */         b = (ByteBuf)obj;
/* 240 */         isBuffer = true;
/*     */       } else {
/* 242 */         comp = (Component)obj;
/* 243 */         b = comp.buf;
/* 244 */         isBuffer = false;
/*     */       } 
/* 246 */       readable += b.readableBytes();
/* 247 */       if (index < readable) {
/* 248 */         if (isBuffer) {
/*     */ 
/*     */           
/* 251 */           comp = new Component(i, readable - b.readableBytes(), b);
/* 252 */           this.buffers[i] = comp;
/*     */         } 
/* 254 */         return comp;
/*     */       } 
/*     */     } 
/* 257 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf buffer(int i) {
/* 264 */     Object obj = this.buffers[i];
/* 265 */     if (obj instanceof ByteBuf) {
/* 266 */       return (ByteBuf)obj;
/*     */     }
/* 268 */     return ((Component)obj).buf;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 273 */     return _getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 278 */     Component c = findComponent(index);
/* 279 */     return c.buf.getByte(index - c.offset);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 284 */     Component c = findComponent(index);
/* 285 */     if (index + 2 <= c.endOffset)
/* 286 */       return c.buf.getShort(index - c.offset); 
/* 287 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 288 */       return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*     */     }
/* 290 */     return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 296 */     Component c = findComponent(index);
/* 297 */     if (index + 2 <= c.endOffset)
/* 298 */       return c.buf.getShortLE(index - c.offset); 
/* 299 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 300 */       return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*     */     }
/* 302 */     return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 308 */     Component c = findComponent(index);
/* 309 */     if (index + 3 <= c.endOffset)
/* 310 */       return c.buf.getUnsignedMedium(index - c.offset); 
/* 311 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 312 */       return (_getShort(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*     */     }
/* 314 */     return _getShort(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 320 */     Component c = findComponent(index);
/* 321 */     if (index + 3 <= c.endOffset)
/* 322 */       return c.buf.getUnsignedMediumLE(index - c.offset); 
/* 323 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 324 */       return _getShortLE(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*     */     }
/* 326 */     return (_getShortLE(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 332 */     Component c = findComponent(index);
/* 333 */     if (index + 4 <= c.endOffset)
/* 334 */       return c.buf.getInt(index - c.offset); 
/* 335 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 336 */       return (_getShort(index) & 0xFFFF) << 16 | _getShort(index + 2) & 0xFFFF;
/*     */     }
/* 338 */     return _getShort(index) & 0xFFFF | (_getShort(index + 2) & 0xFFFF) << 16;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 344 */     Component c = findComponent(index);
/* 345 */     if (index + 4 <= c.endOffset)
/* 346 */       return c.buf.getIntLE(index - c.offset); 
/* 347 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 348 */       return _getShortLE(index) & 0xFFFF | (_getShortLE(index + 2) & 0xFFFF) << 16;
/*     */     }
/* 350 */     return (_getShortLE(index) & 0xFFFF) << 16 | _getShortLE(index + 2) & 0xFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 356 */     Component c = findComponent(index);
/* 357 */     if (index + 8 <= c.endOffset)
/* 358 */       return c.buf.getLong(index - c.offset); 
/* 359 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 360 */       return (_getInt(index) & 0xFFFFFFFFL) << 32L | _getInt(index + 4) & 0xFFFFFFFFL;
/*     */     }
/* 362 */     return _getInt(index) & 0xFFFFFFFFL | (_getInt(index + 4) & 0xFFFFFFFFL) << 32L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 368 */     Component c = findComponent(index);
/* 369 */     if (index + 8 <= c.endOffset)
/* 370 */       return c.buf.getLongLE(index - c.offset); 
/* 371 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 372 */       return _getIntLE(index) & 0xFFFFFFFFL | (_getIntLE(index + 4) & 0xFFFFFFFFL) << 32L;
/*     */     }
/* 374 */     return (_getIntLE(index) & 0xFFFFFFFFL) << 32L | _getIntLE(index + 4) & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 380 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 381 */     if (length == 0) {
/* 382 */       return this;
/*     */     }
/*     */     
/* 385 */     Component c = findComponent(index);
/* 386 */     int i = c.index;
/* 387 */     int adjustment = c.offset;
/* 388 */     ByteBuf s = c.buf;
/*     */     while (true) {
/* 390 */       int localLength = Math.min(length, s.readableBytes() - index - adjustment);
/* 391 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 392 */       index += localLength;
/* 393 */       dstIndex += localLength;
/* 394 */       length -= localLength;
/* 395 */       adjustment += s.readableBytes();
/* 396 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 399 */       s = buffer(++i);
/*     */     } 
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 406 */     int limit = dst.limit();
/* 407 */     int length = dst.remaining();
/*     */     
/* 409 */     checkIndex(index, length);
/* 410 */     if (length == 0) {
/* 411 */       return this;
/*     */     }
/*     */     
/*     */     try {
/* 415 */       Component c = findComponent(index);
/* 416 */       int i = c.index;
/* 417 */       int adjustment = c.offset;
/* 418 */       ByteBuf s = c.buf;
/*     */       while (true) {
/* 420 */         int localLength = Math.min(length, s.readableBytes() - index - adjustment);
/* 421 */         dst.limit(dst.position() + localLength);
/* 422 */         s.getBytes(index - adjustment, dst);
/* 423 */         index += localLength;
/* 424 */         length -= localLength;
/* 425 */         adjustment += s.readableBytes();
/* 426 */         if (length <= 0) {
/*     */           break;
/*     */         }
/* 429 */         s = buffer(++i);
/*     */       } 
/*     */     } finally {
/* 432 */       dst.limit(limit);
/*     */     } 
/* 434 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 439 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 440 */     if (length == 0) {
/* 441 */       return this;
/*     */     }
/*     */     
/* 444 */     Component c = findComponent(index);
/* 445 */     int i = c.index;
/* 446 */     int adjustment = c.offset;
/* 447 */     ByteBuf s = c.buf;
/*     */     while (true) {
/* 449 */       int localLength = Math.min(length, s.readableBytes() - index - adjustment);
/* 450 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 451 */       index += localLength;
/* 452 */       dstIndex += localLength;
/* 453 */       length -= localLength;
/* 454 */       adjustment += s.readableBytes();
/* 455 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 458 */       s = buffer(++i);
/*     */     } 
/* 460 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 466 */     int count = nioBufferCount();
/* 467 */     if (count == 1) {
/* 468 */       return out.write(internalNioBuffer(index, length));
/*     */     }
/* 470 */     long writtenBytes = out.write(nioBuffers(index, length));
/* 471 */     if (writtenBytes > 2147483647L) {
/* 472 */       return Integer.MAX_VALUE;
/*     */     }
/* 474 */     return (int)writtenBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 482 */     int count = nioBufferCount();
/* 483 */     if (count == 1) {
/* 484 */       return out.write(internalNioBuffer(index, length), position);
/*     */     }
/* 486 */     long writtenBytes = 0L;
/* 487 */     for (ByteBuffer buf : nioBuffers(index, length)) {
/* 488 */       writtenBytes += out.write(buf, position + writtenBytes);
/*     */     }
/* 490 */     if (writtenBytes > 2147483647L) {
/* 491 */       return Integer.MAX_VALUE;
/*     */     }
/* 493 */     return (int)writtenBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 500 */     checkIndex(index, length);
/* 501 */     if (length == 0) {
/* 502 */       return this;
/*     */     }
/*     */     
/* 505 */     Component c = findComponent(index);
/* 506 */     int i = c.index;
/* 507 */     int adjustment = c.offset;
/* 508 */     ByteBuf s = c.buf;
/*     */     while (true) {
/* 510 */       int localLength = Math.min(length, s.readableBytes() - index - adjustment);
/* 511 */       s.getBytes(index - adjustment, out, localLength);
/* 512 */       index += localLength;
/* 513 */       length -= localLength;
/* 514 */       adjustment += s.readableBytes();
/* 515 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 518 */       s = buffer(++i);
/*     */     } 
/* 520 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 525 */     checkIndex(index, length);
/* 526 */     boolean release = true;
/* 527 */     ByteBuf buf = alloc().buffer(length);
/*     */     try {
/* 529 */       buf.writeBytes(this, index, length);
/* 530 */       release = false;
/* 531 */       return buf;
/*     */     } finally {
/* 533 */       if (release) {
/* 534 */         buf.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int nioBufferCount() {
/* 541 */     return this.nioBufferCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/* 546 */     checkIndex(index, length);
/* 547 */     if (this.buffers.length == 1) {
/* 548 */       ByteBuf buf = buffer(0);
/* 549 */       if (buf.nioBufferCount() == 1) {
/* 550 */         return buf.nioBuffer(index, length);
/*     */       }
/*     */     } 
/* 553 */     ByteBuffer merged = ByteBuffer.allocate(length).order(order());
/* 554 */     ByteBuffer[] buffers = nioBuffers(index, length);
/*     */ 
/*     */     
/* 557 */     for (int i = 0; i < buffers.length; i++) {
/* 558 */       merged.put(buffers[i]);
/*     */     }
/*     */     
/* 561 */     merged.flip();
/* 562 */     return merged;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 567 */     if (this.buffers.length == 1) {
/* 568 */       return buffer(0).internalNioBuffer(index, length);
/*     */     }
/* 570 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 575 */     checkIndex(index, length);
/* 576 */     if (length == 0) {
/* 577 */       return EmptyArrays.EMPTY_BYTE_BUFFERS;
/*     */     }
/*     */     
/* 580 */     RecyclableArrayList array = RecyclableArrayList.newInstance(this.buffers.length);
/*     */     try {
/* 582 */       Component c = findComponent(index);
/* 583 */       int i = c.index;
/* 584 */       int adjustment = c.offset;
/* 585 */       ByteBuf s = c.buf;
/*     */       while (true) {
/* 587 */         int localLength = Math.min(length, s.readableBytes() - index - adjustment);
/* 588 */         switch (s.nioBufferCount()) {
/*     */           case 0:
/* 590 */             throw new UnsupportedOperationException();
/*     */           case 1:
/* 592 */             array.add(s.nioBuffer(index - adjustment, localLength));
/*     */             break;
/*     */           default:
/* 595 */             Collections.addAll((Collection<? super ByteBuffer>)array, s.nioBuffers(index - adjustment, localLength));
/*     */             break;
/*     */         } 
/* 598 */         index += localLength;
/* 599 */         length -= localLength;
/* 600 */         adjustment += s.readableBytes();
/* 601 */         if (length <= 0) {
/*     */           break;
/*     */         }
/* 604 */         s = buffer(++i);
/*     */       } 
/*     */       
/* 607 */       return (ByteBuffer[])array.toArray((Object[])new ByteBuffer[array.size()]);
/*     */     } finally {
/* 609 */       array.recycle();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 615 */     switch (this.buffers.length) {
/*     */       case 0:
/* 617 */         return true;
/*     */       case 1:
/* 619 */         return buffer(0).hasArray();
/*     */     } 
/* 621 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 627 */     switch (this.buffers.length) {
/*     */       case 0:
/* 629 */         return EmptyArrays.EMPTY_BYTES;
/*     */       case 1:
/* 631 */         return buffer(0).array();
/*     */     } 
/* 633 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/* 639 */     switch (this.buffers.length) {
/*     */       case 0:
/* 641 */         return 0;
/*     */       case 1:
/* 643 */         return buffer(0).arrayOffset();
/*     */     } 
/* 645 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 651 */     switch (this.buffers.length) {
/*     */       case 0:
/* 653 */         return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
/*     */       case 1:
/* 655 */         return buffer(0).hasMemoryAddress();
/*     */     } 
/* 657 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 663 */     switch (this.buffers.length) {
/*     */       case 0:
/* 665 */         return Unpooled.EMPTY_BUFFER.memoryAddress();
/*     */       case 1:
/* 667 */         return buffer(0).memoryAddress();
/*     */     } 
/* 669 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 675 */     for (int i = 0; i < this.buffers.length; i++) {
/* 676 */       buffer(i).release();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 682 */     String result = super.toString();
/* 683 */     result = result.substring(0, result.length() - 1);
/* 684 */     return result + ", components=" + this.buffers.length + ')';
/*     */   }
/*     */   
/*     */   private static final class Component {
/*     */     private final int index;
/*     */     private final int offset;
/*     */     private final ByteBuf buf;
/*     */     private final int endOffset;
/*     */     
/*     */     Component(int index, int offset, ByteBuf buf) {
/* 694 */       this.index = index;
/* 695 */       this.offset = offset;
/* 696 */       this.endOffset = offset + buf.readableBytes();
/* 697 */       this.buf = buf;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\FixedCompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */