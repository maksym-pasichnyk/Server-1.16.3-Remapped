/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.ReadOnlyBufferException;
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
/*      */ public final class EmptyByteBuf
/*      */   extends ByteBuf
/*      */ {
/*      */   static final int EMPTY_BYTE_BUF_HASH_CODE = 1;
/*   40 */   private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
/*      */   private static final long EMPTY_BYTE_BUFFER_ADDRESS;
/*      */   
/*      */   static {
/*   44 */     long emptyByteBufferAddress = 0L;
/*      */     try {
/*   46 */       if (PlatformDependent.hasUnsafe()) {
/*   47 */         emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
/*      */       }
/*   49 */     } catch (Throwable throwable) {}
/*      */ 
/*      */     
/*   52 */     EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
/*      */   }
/*      */   
/*      */   private final ByteBufAllocator alloc;
/*      */   private final ByteOrder order;
/*      */   private final String str;
/*      */   private EmptyByteBuf swapped;
/*      */   
/*      */   public EmptyByteBuf(ByteBufAllocator alloc) {
/*   61 */     this(alloc, ByteOrder.BIG_ENDIAN);
/*      */   }
/*      */   
/*      */   private EmptyByteBuf(ByteBufAllocator alloc, ByteOrder order) {
/*   65 */     if (alloc == null) {
/*   66 */       throw new NullPointerException("alloc");
/*      */     }
/*      */     
/*   69 */     this.alloc = alloc;
/*   70 */     this.order = order;
/*   71 */     this.str = StringUtil.simpleClassName(this) + ((order == ByteOrder.BIG_ENDIAN) ? "BE" : "LE");
/*      */   }
/*      */ 
/*      */   
/*      */   public int capacity() {
/*   76 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf capacity(int newCapacity) {
/*   81 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*   86 */     return this.alloc;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*   91 */     return this.order;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/*   96 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*  101 */     return Unpooled.unmodifiableBuffer(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  106 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  111 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*  116 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  121 */     if (endianness == null) {
/*  122 */       throw new NullPointerException("endianness");
/*      */     }
/*  124 */     if (endianness == order()) {
/*  125 */       return this;
/*      */     }
/*      */     
/*  128 */     EmptyByteBuf swapped = this.swapped;
/*  129 */     if (swapped != null) {
/*  130 */       return swapped;
/*      */     }
/*      */     
/*  133 */     this.swapped = swapped = new EmptyByteBuf(alloc(), endianness);
/*  134 */     return swapped;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*  139 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex) {
/*  144 */     return checkIndex(readerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/*  149 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex) {
/*  154 */     return checkIndex(writerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex) {
/*  159 */     checkIndex(readerIndex);
/*  160 */     checkIndex(writerIndex);
/*  161 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  166 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  171 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  176 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  181 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  186 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf clear() {
/*  191 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markReaderIndex() {
/*  196 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetReaderIndex() {
/*  201 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markWriterIndex() {
/*  206 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetWriterIndex() {
/*  211 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  216 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/*  221 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int minWritableBytes) {
/*  226 */     if (minWritableBytes < 0) {
/*  227 */       throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
/*      */     }
/*  229 */     if (minWritableBytes != 0) {
/*  230 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  232 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  237 */     if (minWritableBytes < 0) {
/*  238 */       throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
/*      */     }
/*      */     
/*  241 */     if (minWritableBytes == 0) {
/*  242 */       return 0;
/*      */     }
/*      */     
/*  245 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  250 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  255 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  260 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  265 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  270 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  275 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  280 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  285 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  290 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  295 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  300 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  305 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  310 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  315 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  320 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  325 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  330 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  335 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  340 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  345 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst) {
/*  350 */     return checkIndex(index, dst.writableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  355 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  360 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst) {
/*  365 */     return checkIndex(index, dst.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  370 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/*  375 */     return checkIndex(index, dst.remaining());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, OutputStream out, int length) {
/*  380 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) {
/*  385 */     checkIndex(index, length);
/*  386 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) {
/*  391 */     checkIndex(index, length);
/*  392 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/*  397 */     checkIndex(index, length);
/*  398 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value) {
/*  403 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int index, int value) {
/*  408 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int index, int value) {
/*  413 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value) {
/*  418 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int index, int value) {
/*  423 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value) {
/*  428 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int index, int value) {
/*  433 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value) {
/*  438 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int index, long value) {
/*  443 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value) {
/*  448 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int index, int value) {
/*  453 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int index, float value) {
/*  458 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int index, double value) {
/*  463 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src) {
/*  468 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length) {
/*  473 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/*  478 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src) {
/*  483 */     return checkIndex(index, src.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/*  488 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuffer src) {
/*  493 */     return checkIndex(index, src.remaining());
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) {
/*  498 */     checkIndex(index, length);
/*  499 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) {
/*  504 */     checkIndex(index, length);
/*  505 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) {
/*  510 */     checkIndex(index, length);
/*  511 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int index, int length) {
/*  516 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/*  521 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  526 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  531 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  536 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  541 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  546 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  551 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  556 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  561 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  566 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  571 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  576 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  581 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  586 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  591 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  596 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  601 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  606 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  611 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  616 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  621 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/*  626 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/*  631 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/*  636 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst) {
/*  641 */     return checkLength(dst.writableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length) {
/*  646 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/*  651 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst) {
/*  656 */     return checkLength(dst.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/*  661 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst) {
/*  666 */     return checkLength(dst.remaining());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) {
/*  671 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) {
/*  676 */     checkLength(length);
/*  677 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) {
/*  682 */     checkLength(length);
/*  683 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/*  688 */     checkLength(length);
/*  689 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int length) {
/*  694 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value) {
/*  699 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int value) {
/*  704 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int value) {
/*  709 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int value) {
/*  714 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int value) {
/*  719 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int value) {
/*  724 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int value) {
/*  729 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int value) {
/*  734 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long value) {
/*  739 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long value) {
/*  744 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int value) {
/*  749 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float value) {
/*  754 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double value) {
/*  759 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src) {
/*  764 */     return checkLength(src.readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length) {
/*  769 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/*  774 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src) {
/*  779 */     return checkLength(src.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/*  784 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src) {
/*  789 */     return checkLength(src.remaining());
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) {
/*  794 */     checkLength(length);
/*  795 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) {
/*  800 */     checkLength(length);
/*  801 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) {
/*  806 */     checkLength(length);
/*  807 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int length) {
/*  812 */     return checkLength(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/*  817 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/*  822 */     checkIndex(fromIndex);
/*  823 */     checkIndex(toIndex);
/*  824 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/*  829 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/*  834 */     checkLength(length);
/*  835 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/*  840 */     checkIndex(index, length);
/*  841 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/*  846 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/*  851 */     checkIndex(index, length);
/*  852 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/*  857 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/*  862 */     checkIndex(index, length);
/*  863 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/*  868 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/*  873 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/*  878 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/*  883 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/*  888 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/*  893 */     return checkIndex(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/*  898 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/*  903 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/*  908 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/*  913 */     return EMPTY_BYTE_BUFFER;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/*  918 */     checkIndex(index, length);
/*  919 */     return nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/*  924 */     return new ByteBuffer[] { EMPTY_BYTE_BUFFER };
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  929 */     checkIndex(index, length);
/*  930 */     return nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/*  935 */     return EMPTY_BYTE_BUFFER;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  940 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  945 */     return EmptyArrays.EMPTY_BYTES;
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  950 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/*  955 */     return (EMPTY_BYTE_BUFFER_ADDRESS != 0L);
/*      */   }
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/*  960 */     if (hasMemoryAddress()) {
/*  961 */       return EMPTY_BYTE_BUFFER_ADDRESS;
/*      */     }
/*  963 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(Charset charset) {
/*  969 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/*  974 */     checkIndex(index, length);
/*  975 */     return toString(charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  980 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  985 */     return (obj instanceof ByteBuf && !((ByteBuf)obj).isReadable());
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf buffer) {
/*  990 */     return buffer.isReadable() ? -1 : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  995 */     return this.str;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int size) {
/* 1000 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int size) {
/* 1005 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int refCnt() {
/* 1010 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain() {
/* 1015 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain(int increment) {
/* 1020 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch() {
/* 1025 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch(Object hint) {
/* 1030 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1035 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int decrement) {
/* 1040 */     return false;
/*      */   }
/*      */   
/*      */   private ByteBuf checkIndex(int index) {
/* 1044 */     if (index != 0) {
/* 1045 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 1047 */     return this;
/*      */   }
/*      */   
/*      */   private ByteBuf checkIndex(int index, int length) {
/* 1051 */     if (length < 0) {
/* 1052 */       throw new IllegalArgumentException("length: " + length);
/*      */     }
/* 1054 */     if (index != 0 || length != 0) {
/* 1055 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 1057 */     return this;
/*      */   }
/*      */   
/*      */   private ByteBuf checkLength(int length) {
/* 1061 */     if (length < 0) {
/* 1062 */       throw new IllegalArgumentException("length: " + length + " (expected: >= 0)");
/*      */     }
/* 1064 */     if (length != 0) {
/* 1065 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 1067 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\EmptyByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */