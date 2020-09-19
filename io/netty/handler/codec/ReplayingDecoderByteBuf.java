/*      */ package io.netty.handler.codec;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.SwappedByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.Signal;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class ReplayingDecoderByteBuf
/*      */   extends ByteBuf
/*      */ {
/*   40 */   private static final Signal REPLAY = ReplayingDecoder.REPLAY;
/*      */   
/*      */   private ByteBuf buffer;
/*      */   
/*      */   private boolean terminated;
/*      */   private SwappedByteBuf swapped;
/*   46 */   static final ReplayingDecoderByteBuf EMPTY_BUFFER = new ReplayingDecoderByteBuf(Unpooled.EMPTY_BUFFER);
/*      */   
/*      */   static {
/*   49 */     EMPTY_BUFFER.terminate();
/*      */   }
/*      */   
/*      */   ReplayingDecoderByteBuf() {}
/*      */   
/*      */   ReplayingDecoderByteBuf(ByteBuf buffer) {
/*   55 */     setCumulation(buffer);
/*      */   }
/*      */   
/*      */   void setCumulation(ByteBuf buffer) {
/*   59 */     this.buffer = buffer;
/*      */   }
/*      */   
/*      */   void terminate() {
/*   63 */     this.terminated = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int capacity() {
/*   68 */     if (this.terminated) {
/*   69 */       return this.buffer.capacity();
/*      */     }
/*   71 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf capacity(int newCapacity) {
/*   77 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*   82 */     return capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*   87 */     return this.buffer.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*   92 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*   98 */     return Unpooled.unmodifiableBuffer(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  103 */     return this.buffer.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  108 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  113 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  118 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/*  123 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/*  128 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf clear() {
/*  133 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  138 */     return (this == obj);
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf buffer) {
/*  143 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/*  148 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/*  153 */     checkIndex(index, length);
/*  154 */     return this.buffer.copy(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  159 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int writableBytes) {
/*  164 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  169 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/*  174 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/*  179 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  184 */     checkIndex(index, 1);
/*  185 */     return this.buffer.getBoolean(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  190 */     checkIndex(index, 1);
/*  191 */     return this.buffer.getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  196 */     checkIndex(index, 1);
/*  197 */     return this.buffer.getUnsignedByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  202 */     checkIndex(index, length);
/*  203 */     this.buffer.getBytes(index, dst, dstIndex, length);
/*  204 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst) {
/*  209 */     checkIndex(index, dst.length);
/*  210 */     this.buffer.getBytes(index, dst);
/*  211 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/*  216 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  221 */     checkIndex(index, length);
/*  222 */     this.buffer.getBytes(index, dst, dstIndex, length);
/*  223 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  228 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst) {
/*  233 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) {
/*  238 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) {
/*  243 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, OutputStream out, int length) {
/*  248 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  253 */     checkIndex(index, 4);
/*  254 */     return this.buffer.getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  259 */     checkIndex(index, 4);
/*  260 */     return this.buffer.getIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  265 */     checkIndex(index, 4);
/*  266 */     return this.buffer.getUnsignedInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  271 */     checkIndex(index, 4);
/*  272 */     return this.buffer.getUnsignedIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  277 */     checkIndex(index, 8);
/*  278 */     return this.buffer.getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  283 */     checkIndex(index, 8);
/*  284 */     return this.buffer.getLongLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  289 */     checkIndex(index, 3);
/*  290 */     return this.buffer.getMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  295 */     checkIndex(index, 3);
/*  296 */     return this.buffer.getMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  301 */     checkIndex(index, 3);
/*  302 */     return this.buffer.getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  307 */     checkIndex(index, 3);
/*  308 */     return this.buffer.getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  313 */     checkIndex(index, 2);
/*  314 */     return this.buffer.getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  319 */     checkIndex(index, 2);
/*  320 */     return this.buffer.getShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  325 */     checkIndex(index, 2);
/*  326 */     return this.buffer.getUnsignedShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  331 */     checkIndex(index, 2);
/*  332 */     return this.buffer.getUnsignedShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  337 */     checkIndex(index, 2);
/*  338 */     return this.buffer.getChar(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  343 */     checkIndex(index, 4);
/*  344 */     return this.buffer.getFloat(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  349 */     checkIndex(index, 8);
/*  350 */     return this.buffer.getDouble(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/*  355 */     checkIndex(index, length);
/*  356 */     return this.buffer.getCharSequence(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  361 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/*  366 */     if (fromIndex == toIndex) {
/*  367 */       return -1;
/*      */     }
/*      */     
/*  370 */     if (Math.max(fromIndex, toIndex) > this.buffer.writerIndex()) {
/*  371 */       throw REPLAY;
/*      */     }
/*      */     
/*  374 */     return this.buffer.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/*  379 */     int bytes = this.buffer.bytesBefore(value);
/*  380 */     if (bytes < 0) {
/*  381 */       throw REPLAY;
/*      */     }
/*  383 */     return bytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/*  388 */     return bytesBefore(this.buffer.readerIndex(), length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/*  393 */     int writerIndex = this.buffer.writerIndex();
/*  394 */     if (index >= writerIndex) {
/*  395 */       throw REPLAY;
/*      */     }
/*      */     
/*  398 */     if (index <= writerIndex - length) {
/*  399 */       return this.buffer.bytesBefore(index, length, value);
/*      */     }
/*      */     
/*  402 */     int res = this.buffer.bytesBefore(index, writerIndex - index, value);
/*  403 */     if (res < 0) {
/*  404 */       throw REPLAY;
/*      */     }
/*  406 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/*  412 */     int ret = this.buffer.forEachByte(processor);
/*  413 */     if (ret < 0) {
/*  414 */       throw REPLAY;
/*      */     }
/*  416 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/*  422 */     int writerIndex = this.buffer.writerIndex();
/*  423 */     if (index >= writerIndex) {
/*  424 */       throw REPLAY;
/*      */     }
/*      */     
/*  427 */     if (index <= writerIndex - length) {
/*  428 */       return this.buffer.forEachByte(index, length, processor);
/*      */     }
/*      */     
/*  431 */     int ret = this.buffer.forEachByte(index, writerIndex - index, processor);
/*  432 */     if (ret < 0) {
/*  433 */       throw REPLAY;
/*      */     }
/*  435 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/*  441 */     if (this.terminated) {
/*  442 */       return this.buffer.forEachByteDesc(processor);
/*      */     }
/*  444 */     throw reject();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/*  450 */     if (index + length > this.buffer.writerIndex()) {
/*  451 */       throw REPLAY;
/*      */     }
/*      */     
/*  454 */     return this.buffer.forEachByteDesc(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markReaderIndex() {
/*  459 */     this.buffer.markReaderIndex();
/*  460 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markWriterIndex() {
/*  465 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  470 */     return this.buffer.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  475 */     if (endianness == null) {
/*  476 */       throw new NullPointerException("endianness");
/*      */     }
/*  478 */     if (endianness == order()) {
/*  479 */       return this;
/*      */     }
/*      */     
/*  482 */     SwappedByteBuf swapped = this.swapped;
/*  483 */     if (swapped == null) {
/*  484 */       this.swapped = swapped = new SwappedByteBuf(this);
/*      */     }
/*  486 */     return (ByteBuf)swapped;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  491 */     return this.terminated ? this.buffer.isReadable() : true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int size) {
/*  496 */     return this.terminated ? this.buffer.isReadable(size) : true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  501 */     if (this.terminated) {
/*  502 */       return this.buffer.readableBytes();
/*      */     }
/*  504 */     return Integer.MAX_VALUE - this.buffer.readerIndex();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  510 */     checkReadableBytes(1);
/*  511 */     return this.buffer.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  516 */     checkReadableBytes(1);
/*  517 */     return this.buffer.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  522 */     checkReadableBytes(1);
/*  523 */     return this.buffer.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/*  528 */     checkReadableBytes(length);
/*  529 */     this.buffer.readBytes(dst, dstIndex, length);
/*  530 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst) {
/*  535 */     checkReadableBytes(dst.length);
/*  536 */     this.buffer.readBytes(dst);
/*  537 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst) {
/*  542 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/*  547 */     checkReadableBytes(length);
/*  548 */     this.buffer.readBytes(dst, dstIndex, length);
/*  549 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length) {
/*  554 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst) {
/*  559 */     checkReadableBytes(dst.writableBytes());
/*  560 */     this.buffer.readBytes(dst);
/*  561 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) {
/*  566 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) {
/*  571 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/*  576 */     checkReadableBytes(length);
/*  577 */     return this.buffer.readBytes(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/*  582 */     checkReadableBytes(length);
/*  583 */     return this.buffer.readSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/*  588 */     checkReadableBytes(length);
/*  589 */     return this.buffer.readRetainedSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) {
/*  594 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*  599 */     return this.buffer.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex) {
/*  604 */     this.buffer.readerIndex(readerIndex);
/*  605 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  610 */     checkReadableBytes(4);
/*  611 */     return this.buffer.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  616 */     checkReadableBytes(4);
/*  617 */     return this.buffer.readIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  622 */     checkReadableBytes(4);
/*  623 */     return this.buffer.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  628 */     checkReadableBytes(4);
/*  629 */     return this.buffer.readUnsignedIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  634 */     checkReadableBytes(8);
/*  635 */     return this.buffer.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  640 */     checkReadableBytes(8);
/*  641 */     return this.buffer.readLongLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  646 */     checkReadableBytes(3);
/*  647 */     return this.buffer.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  652 */     checkReadableBytes(3);
/*  653 */     return this.buffer.readMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  658 */     checkReadableBytes(3);
/*  659 */     return this.buffer.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  664 */     checkReadableBytes(3);
/*  665 */     return this.buffer.readUnsignedMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  670 */     checkReadableBytes(2);
/*  671 */     return this.buffer.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  676 */     checkReadableBytes(2);
/*  677 */     return this.buffer.readShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  682 */     checkReadableBytes(2);
/*  683 */     return this.buffer.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  688 */     checkReadableBytes(2);
/*  689 */     return this.buffer.readUnsignedShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  694 */     checkReadableBytes(2);
/*  695 */     return this.buffer.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  700 */     checkReadableBytes(4);
/*  701 */     return this.buffer.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  706 */     checkReadableBytes(8);
/*  707 */     return this.buffer.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/*  712 */     checkReadableBytes(length);
/*  713 */     return this.buffer.readCharSequence(length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetReaderIndex() {
/*  718 */     this.buffer.resetReaderIndex();
/*  719 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetWriterIndex() {
/*  724 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value) {
/*  729 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int index, int value) {
/*  734 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/*  739 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src) {
/*  744 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuffer src) {
/*  749 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/*  754 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length) {
/*  759 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src) {
/*  764 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) {
/*  769 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int index, int length) {
/*  774 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) {
/*  779 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) {
/*  784 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex) {
/*  789 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int index, int value) {
/*  794 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value) {
/*  799 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int index, long value) {
/*  804 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value) {
/*  809 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int index, int value) {
/*  814 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value) {
/*  819 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int index, int value) {
/*  824 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value) {
/*  829 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int index, int value) {
/*  834 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int index, float value) {
/*  839 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int index, double value) {
/*  844 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int length) {
/*  849 */     checkReadableBytes(length);
/*  850 */     this.buffer.skipBytes(length);
/*  851 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/*  856 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/*  861 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/*  866 */     checkIndex(index, length);
/*  867 */     return this.buffer.slice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/*  872 */     checkIndex(index, length);
/*  873 */     return this.buffer.slice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/*  878 */     return this.buffer.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/*  883 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/*  888 */     checkIndex(index, length);
/*  889 */     return this.buffer.nioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/*  894 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  899 */     checkIndex(index, length);
/*  900 */     return this.buffer.nioBuffers(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/*  905 */     checkIndex(index, length);
/*  906 */     return this.buffer.internalNioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/*  911 */     checkIndex(index, length);
/*  912 */     return this.buffer.toString(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset charsetName) {
/*  917 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  922 */     return StringUtil.simpleClassName(this) + '(' + "ridx=" + 
/*      */       
/*  924 */       readerIndex() + ", widx=" + 
/*      */ 
/*      */       
/*  927 */       writerIndex() + ')';
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  933 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int size) {
/*  938 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  943 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  948 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value) {
/*  953 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int value) {
/*  958 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/*  963 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src) {
/*  968 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src) {
/*  973 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/*  978 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length) {
/*  983 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src) {
/*  988 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) {
/*  993 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) {
/*  998 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) {
/* 1003 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int value) {
/* 1008 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int value) {
/* 1013 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long value) {
/* 1018 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long value) {
/* 1023 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int value) {
/* 1028 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int value) {
/* 1033 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int length) {
/* 1038 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/* 1043 */     return this.buffer.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex) {
/* 1048 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int value) {
/* 1053 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int value) {
/* 1058 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int value) {
/* 1063 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float value) {
/* 1068 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double value) {
/* 1073 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/* 1078 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/* 1083 */     throw reject();
/*      */   }
/*      */   
/*      */   private void checkIndex(int index, int length) {
/* 1087 */     if (index + length > this.buffer.writerIndex()) {
/* 1088 */       throw REPLAY;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkReadableBytes(int readableBytes) {
/* 1093 */     if (this.buffer.readableBytes() < readableBytes) {
/* 1094 */       throw REPLAY;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/* 1100 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public int refCnt() {
/* 1105 */     return this.buffer.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain() {
/* 1110 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain(int increment) {
/* 1115 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch() {
/* 1120 */     this.buffer.touch();
/* 1121 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch(Object hint) {
/* 1126 */     this.buffer.touch(hint);
/* 1127 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1132 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int decrement) {
/* 1137 */     throw reject();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/* 1142 */     throw reject();
/*      */   }
/*      */   
/*      */   private static UnsupportedOperationException reject() {
/* 1146 */     return new UnsupportedOperationException("not a replayable operation");
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\ReplayingDecoderByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */