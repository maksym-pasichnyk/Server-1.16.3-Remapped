/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.IOException;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class WrappedByteBuf
/*      */   extends ByteBuf
/*      */ {
/*      */   protected final ByteBuf buf;
/*      */   
/*      */   protected WrappedByteBuf(ByteBuf buf) {
/*   44 */     if (buf == null) {
/*   45 */       throw new NullPointerException("buf");
/*      */     }
/*   47 */     this.buf = buf;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasMemoryAddress() {
/*   52 */     return this.buf.hasMemoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long memoryAddress() {
/*   57 */     return this.buf.memoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int capacity() {
/*   62 */     return this.buf.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf capacity(int newCapacity) {
/*   67 */     this.buf.capacity(newCapacity);
/*   68 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int maxCapacity() {
/*   73 */     return this.buf.maxCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBufAllocator alloc() {
/*   78 */     return this.buf.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteOrder order() {
/*   83 */     return this.buf.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*   88 */     return this.buf.order(endianness);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf unwrap() {
/*   93 */     return this.buf;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*   98 */     return this.buf.asReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  103 */     return this.buf.isReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDirect() {
/*  108 */     return this.buf.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int readerIndex() {
/*  113 */     return this.buf.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf readerIndex(int readerIndex) {
/*  118 */     this.buf.readerIndex(readerIndex);
/*  119 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int writerIndex() {
/*  124 */     return this.buf.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf writerIndex(int writerIndex) {
/*  129 */     this.buf.writerIndex(writerIndex);
/*  130 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex) {
/*  135 */     this.buf.setIndex(readerIndex, writerIndex);
/*  136 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int readableBytes() {
/*  141 */     return this.buf.readableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int writableBytes() {
/*  146 */     return this.buf.writableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int maxWritableBytes() {
/*  151 */     return this.buf.maxWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isReadable() {
/*  156 */     return this.buf.isReadable();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWritable() {
/*  161 */     return this.buf.isWritable();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf clear() {
/*  166 */     this.buf.clear();
/*  167 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf markReaderIndex() {
/*  172 */     this.buf.markReaderIndex();
/*  173 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf resetReaderIndex() {
/*  178 */     this.buf.resetReaderIndex();
/*  179 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf markWriterIndex() {
/*  184 */     this.buf.markWriterIndex();
/*  185 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf resetWriterIndex() {
/*  190 */     this.buf.resetWriterIndex();
/*  191 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  196 */     this.buf.discardReadBytes();
/*  197 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/*  202 */     this.buf.discardSomeReadBytes();
/*  203 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int minWritableBytes) {
/*  208 */     this.buf.ensureWritable(minWritableBytes);
/*  209 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  214 */     return this.buf.ensureWritable(minWritableBytes, force);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  219 */     return this.buf.getBoolean(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  224 */     return this.buf.getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  229 */     return this.buf.getUnsignedByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  234 */     return this.buf.getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  239 */     return this.buf.getShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  244 */     return this.buf.getUnsignedShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  249 */     return this.buf.getUnsignedShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  254 */     return this.buf.getMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  259 */     return this.buf.getMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  264 */     return this.buf.getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  269 */     return this.buf.getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  274 */     return this.buf.getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  279 */     return this.buf.getIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  284 */     return this.buf.getUnsignedInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  289 */     return this.buf.getUnsignedIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  294 */     return this.buf.getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  299 */     return this.buf.getLongLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  304 */     return this.buf.getChar(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  309 */     return this.buf.getFloat(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  314 */     return this.buf.getDouble(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst) {
/*  319 */     this.buf.getBytes(index, dst);
/*  320 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  325 */     this.buf.getBytes(index, dst, length);
/*  326 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  331 */     this.buf.getBytes(index, dst, dstIndex, length);
/*  332 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst) {
/*  337 */     this.buf.getBytes(index, dst);
/*  338 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  343 */     this.buf.getBytes(index, dst, dstIndex, length);
/*  344 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/*  349 */     this.buf.getBytes(index, dst);
/*  350 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/*  355 */     this.buf.getBytes(index, out, length);
/*  356 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/*  361 */     return this.buf.getBytes(index, out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/*  366 */     return this.buf.getBytes(index, out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/*  371 */     return this.buf.getCharSequence(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value) {
/*  376 */     this.buf.setBoolean(index, value);
/*  377 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int index, int value) {
/*  382 */     this.buf.setByte(index, value);
/*  383 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int index, int value) {
/*  388 */     this.buf.setShort(index, value);
/*  389 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value) {
/*  394 */     this.buf.setShortLE(index, value);
/*  395 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int index, int value) {
/*  400 */     this.buf.setMedium(index, value);
/*  401 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value) {
/*  406 */     this.buf.setMediumLE(index, value);
/*  407 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int index, int value) {
/*  412 */     this.buf.setInt(index, value);
/*  413 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value) {
/*  418 */     this.buf.setIntLE(index, value);
/*  419 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int index, long value) {
/*  424 */     this.buf.setLong(index, value);
/*  425 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value) {
/*  430 */     this.buf.setLongLE(index, value);
/*  431 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int index, int value) {
/*  436 */     this.buf.setChar(index, value);
/*  437 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int index, float value) {
/*  442 */     this.buf.setFloat(index, value);
/*  443 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int index, double value) {
/*  448 */     this.buf.setDouble(index, value);
/*  449 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src) {
/*  454 */     this.buf.setBytes(index, src);
/*  455 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length) {
/*  460 */     this.buf.setBytes(index, src, length);
/*  461 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/*  466 */     this.buf.setBytes(index, src, srcIndex, length);
/*  467 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src) {
/*  472 */     this.buf.setBytes(index, src);
/*  473 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/*  478 */     this.buf.setBytes(index, src, srcIndex, length);
/*  479 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuffer src) {
/*  484 */     this.buf.setBytes(index, src);
/*  485 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException {
/*  490 */     return this.buf.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/*  495 */     return this.buf.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/*  500 */     return this.buf.setBytes(index, in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int index, int length) {
/*  505 */     this.buf.setZero(index, length);
/*  506 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/*  511 */     return this.buf.setCharSequence(index, sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  516 */     return this.buf.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  521 */     return this.buf.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  526 */     return this.buf.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  531 */     return this.buf.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  536 */     return this.buf.readShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  541 */     return this.buf.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  546 */     return this.buf.readUnsignedShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  551 */     return this.buf.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  556 */     return this.buf.readMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  561 */     return this.buf.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  566 */     return this.buf.readUnsignedMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  571 */     return this.buf.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  576 */     return this.buf.readIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  581 */     return this.buf.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  586 */     return this.buf.readUnsignedIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  591 */     return this.buf.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  596 */     return this.buf.readLongLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  601 */     return this.buf.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  606 */     return this.buf.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  611 */     return this.buf.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/*  616 */     return this.buf.readBytes(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/*  621 */     return this.buf.readSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/*  626 */     return this.buf.readRetainedSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst) {
/*  631 */     this.buf.readBytes(dst);
/*  632 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length) {
/*  637 */     this.buf.readBytes(dst, length);
/*  638 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/*  643 */     this.buf.readBytes(dst, dstIndex, length);
/*  644 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst) {
/*  649 */     this.buf.readBytes(dst);
/*  650 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/*  655 */     this.buf.readBytes(dst, dstIndex, length);
/*  656 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst) {
/*  661 */     this.buf.readBytes(dst);
/*  662 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) throws IOException {
/*  667 */     this.buf.readBytes(out, length);
/*  668 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/*  673 */     return this.buf.readBytes(out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/*  678 */     return this.buf.readBytes(out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/*  683 */     return this.buf.readCharSequence(length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int length) {
/*  688 */     this.buf.skipBytes(length);
/*  689 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value) {
/*  694 */     this.buf.writeBoolean(value);
/*  695 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int value) {
/*  700 */     this.buf.writeByte(value);
/*  701 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int value) {
/*  706 */     this.buf.writeShort(value);
/*  707 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int value) {
/*  712 */     this.buf.writeShortLE(value);
/*  713 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int value) {
/*  718 */     this.buf.writeMedium(value);
/*  719 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int value) {
/*  724 */     this.buf.writeMediumLE(value);
/*  725 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int value) {
/*  730 */     this.buf.writeInt(value);
/*  731 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int value) {
/*  736 */     this.buf.writeIntLE(value);
/*  737 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long value) {
/*  742 */     this.buf.writeLong(value);
/*  743 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long value) {
/*  748 */     this.buf.writeLongLE(value);
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int value) {
/*  754 */     this.buf.writeChar(value);
/*  755 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float value) {
/*  760 */     this.buf.writeFloat(value);
/*  761 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double value) {
/*  766 */     this.buf.writeDouble(value);
/*  767 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src) {
/*  772 */     this.buf.writeBytes(src);
/*  773 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length) {
/*  778 */     this.buf.writeBytes(src, length);
/*  779 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/*  784 */     this.buf.writeBytes(src, srcIndex, length);
/*  785 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src) {
/*  790 */     this.buf.writeBytes(src);
/*  791 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/*  796 */     this.buf.writeBytes(src, srcIndex, length);
/*  797 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src) {
/*  802 */     this.buf.writeBytes(src);
/*  803 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException {
/*  808 */     return this.buf.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
/*  813 */     return this.buf.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException {
/*  818 */     return this.buf.writeBytes(in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int length) {
/*  823 */     this.buf.writeZero(length);
/*  824 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/*  829 */     return this.buf.writeCharSequence(sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/*  834 */     return this.buf.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/*  839 */     return this.buf.bytesBefore(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/*  844 */     return this.buf.bytesBefore(length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/*  849 */     return this.buf.bytesBefore(index, length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/*  854 */     return this.buf.forEachByte(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/*  859 */     return this.buf.forEachByte(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/*  864 */     return this.buf.forEachByteDesc(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/*  869 */     return this.buf.forEachByteDesc(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/*  874 */     return this.buf.copy();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/*  879 */     return this.buf.copy(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/*  884 */     return this.buf.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/*  889 */     return this.buf.retainedSlice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/*  894 */     return this.buf.slice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/*  899 */     return this.buf.retainedSlice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/*  904 */     return this.buf.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/*  909 */     return this.buf.retainedDuplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/*  914 */     return this.buf.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/*  919 */     return this.buf.nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/*  924 */     return this.buf.nioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/*  929 */     return this.buf.nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  934 */     return this.buf.nioBuffers(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/*  939 */     return this.buf.internalNioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  944 */     return this.buf.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  949 */     return this.buf.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  954 */     return this.buf.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset charset) {
/*  959 */     return this.buf.toString(charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/*  964 */     return this.buf.toString(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  969 */     return this.buf.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  975 */     return this.buf.equals(obj);
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf buffer) {
/*  980 */     return this.buf.compareTo(buffer);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  985 */     return StringUtil.simpleClassName(this) + '(' + this.buf.toString() + ')';
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain(int increment) {
/*  990 */     this.buf.retain(increment);
/*  991 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain() {
/*  996 */     this.buf.retain();
/*  997 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch() {
/* 1002 */     this.buf.touch();
/* 1003 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch(Object hint) {
/* 1008 */     this.buf.touch(hint);
/* 1009 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isReadable(int size) {
/* 1014 */     return this.buf.isReadable(size);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWritable(int size) {
/* 1019 */     return this.buf.isWritable(size);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int refCnt() {
/* 1024 */     return this.buf.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1029 */     return this.buf.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int decrement) {
/* 1034 */     return this.buf.release(decrement);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\WrappedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */