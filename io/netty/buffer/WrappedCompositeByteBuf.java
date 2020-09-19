/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ class WrappedCompositeByteBuf
/*      */   extends CompositeByteBuf
/*      */ {
/*      */   private final CompositeByteBuf wrapped;
/*      */   
/*      */   WrappedCompositeByteBuf(CompositeByteBuf wrapped) {
/*   37 */     super(wrapped.alloc());
/*   38 */     this.wrapped = wrapped;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/*   43 */     return this.wrapped.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int decrement) {
/*   48 */     return this.wrapped.release(decrement);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int maxCapacity() {
/*   53 */     return this.wrapped.maxCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int readerIndex() {
/*   58 */     return this.wrapped.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int writerIndex() {
/*   63 */     return this.wrapped.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isReadable() {
/*   68 */     return this.wrapped.isReadable();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isReadable(int numBytes) {
/*   73 */     return this.wrapped.isReadable(numBytes);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWritable() {
/*   78 */     return this.wrapped.isWritable();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWritable(int numBytes) {
/*   83 */     return this.wrapped.isWritable(numBytes);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int readableBytes() {
/*   88 */     return this.wrapped.readableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int writableBytes() {
/*   93 */     return this.wrapped.writableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int maxWritableBytes() {
/*   98 */     return this.wrapped.maxWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  103 */     return this.wrapped.ensureWritable(minWritableBytes, force);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  108 */     return this.wrapped.order(endianness);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  113 */     return this.wrapped.getBoolean(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  118 */     return this.wrapped.getUnsignedByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  123 */     return this.wrapped.getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  128 */     return this.wrapped.getShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  133 */     return this.wrapped.getUnsignedShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  138 */     return this.wrapped.getUnsignedShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  143 */     return this.wrapped.getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  148 */     return this.wrapped.getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  153 */     return this.wrapped.getMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  158 */     return this.wrapped.getMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  163 */     return this.wrapped.getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  168 */     return this.wrapped.getIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  173 */     return this.wrapped.getUnsignedInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  178 */     return this.wrapped.getUnsignedIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  183 */     return this.wrapped.getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  188 */     return this.wrapped.getLongLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  193 */     return this.wrapped.getChar(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  198 */     return this.wrapped.getFloat(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  203 */     return this.wrapped.getDouble(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value) {
/*  208 */     return this.wrapped.setShortLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value) {
/*  213 */     return this.wrapped.setMediumLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value) {
/*  218 */     return this.wrapped.setIntLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value) {
/*  223 */     return this.wrapped.setLongLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  228 */     return this.wrapped.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  233 */     return this.wrapped.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  238 */     return this.wrapped.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  243 */     return this.wrapped.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  248 */     return this.wrapped.readShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  253 */     return this.wrapped.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  258 */     return this.wrapped.readUnsignedShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  263 */     return this.wrapped.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  268 */     return this.wrapped.readMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  273 */     return this.wrapped.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  278 */     return this.wrapped.readUnsignedMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  283 */     return this.wrapped.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  288 */     return this.wrapped.readIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  293 */     return this.wrapped.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  298 */     return this.wrapped.readUnsignedIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  303 */     return this.wrapped.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  308 */     return this.wrapped.readLongLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  313 */     return this.wrapped.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  318 */     return this.wrapped.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  323 */     return this.wrapped.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/*  328 */     return this.wrapped.readBytes(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/*  333 */     return this.wrapped.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/*  338 */     return this.wrapped.retainedSlice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/*  343 */     return this.wrapped.slice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/*  348 */     return this.wrapped.retainedSlice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/*  353 */     return this.wrapped.nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset charset) {
/*  358 */     return this.wrapped.toString(charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/*  363 */     return this.wrapped.toString(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/*  368 */     return this.wrapped.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/*  373 */     return this.wrapped.bytesBefore(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/*  378 */     return this.wrapped.bytesBefore(length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/*  383 */     return this.wrapped.bytesBefore(index, length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/*  388 */     return this.wrapped.forEachByte(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/*  393 */     return this.wrapped.forEachByte(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/*  398 */     return this.wrapped.forEachByteDesc(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/*  403 */     return this.wrapped.forEachByteDesc(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int hashCode() {
/*  408 */     return this.wrapped.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean equals(Object o) {
/*  413 */     return this.wrapped.equals(o);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int compareTo(ByteBuf that) {
/*  418 */     return this.wrapped.compareTo(that);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int refCnt() {
/*  423 */     return this.wrapped.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/*  428 */     return this.wrapped.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/*  433 */     return this.wrapped.retainedDuplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/*  438 */     return this.wrapped.readSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/*  443 */     return this.wrapped.readRetainedSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/*  448 */     return this.wrapped.readBytes(out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int value) {
/*  453 */     return this.wrapped.writeShortLE(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int value) {
/*  458 */     return this.wrapped.writeMediumLE(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int value) {
/*  463 */     return this.wrapped.writeIntLE(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long value) {
/*  468 */     return this.wrapped.writeLongLE(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException {
/*  473 */     return this.wrapped.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
/*  478 */     return this.wrapped.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/*  483 */     return this.wrapped.copy();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(ByteBuf buffer) {
/*  488 */     this.wrapped.addComponent(buffer);
/*  489 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(ByteBuf... buffers) {
/*  494 */     this.wrapped.addComponents(buffers);
/*  495 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers) {
/*  500 */     this.wrapped.addComponents(buffers);
/*  501 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer) {
/*  506 */     this.wrapped.addComponent(cIndex, buffer);
/*  507 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers) {
/*  512 */     this.wrapped.addComponents(cIndex, buffers);
/*  513 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers) {
/*  518 */     this.wrapped.addComponents(cIndex, buffers);
/*  519 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer) {
/*  524 */     this.wrapped.addComponent(increaseWriterIndex, buffer);
/*  525 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, ByteBuf... buffers) {
/*  530 */     this.wrapped.addComponents(increaseWriterIndex, buffers);
/*  531 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, Iterable<ByteBuf> buffers) {
/*  536 */     this.wrapped.addComponents(increaseWriterIndex, buffers);
/*  537 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, int cIndex, ByteBuf buffer) {
/*  542 */     this.wrapped.addComponent(increaseWriterIndex, cIndex, buffer);
/*  543 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf removeComponent(int cIndex) {
/*  548 */     this.wrapped.removeComponent(cIndex);
/*  549 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf removeComponents(int cIndex, int numComponents) {
/*  554 */     this.wrapped.removeComponents(cIndex, numComponents);
/*  555 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<ByteBuf> iterator() {
/*  560 */     return this.wrapped.iterator();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ByteBuf> decompose(int offset, int length) {
/*  565 */     return this.wrapped.decompose(offset, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDirect() {
/*  570 */     return this.wrapped.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasArray() {
/*  575 */     return this.wrapped.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte[] array() {
/*  580 */     return this.wrapped.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int arrayOffset() {
/*  585 */     return this.wrapped.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasMemoryAddress() {
/*  590 */     return this.wrapped.hasMemoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long memoryAddress() {
/*  595 */     return this.wrapped.memoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int capacity() {
/*  600 */     return this.wrapped.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf capacity(int newCapacity) {
/*  605 */     this.wrapped.capacity(newCapacity);
/*  606 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBufAllocator alloc() {
/*  611 */     return this.wrapped.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteOrder order() {
/*  616 */     return this.wrapped.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int numComponents() {
/*  621 */     return this.wrapped.numComponents();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int maxNumComponents() {
/*  626 */     return this.wrapped.maxNumComponents();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int toComponentIndex(int offset) {
/*  631 */     return this.wrapped.toComponentIndex(offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int toByteIndex(int cIndex) {
/*  636 */     return this.wrapped.toByteIndex(cIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  641 */     return this.wrapped.getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final byte _getByte(int index) {
/*  646 */     return this.wrapped._getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final short _getShort(int index) {
/*  651 */     return this.wrapped._getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final short _getShortLE(int index) {
/*  656 */     return this.wrapped._getShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _getUnsignedMedium(int index) {
/*  661 */     return this.wrapped._getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _getUnsignedMediumLE(int index) {
/*  666 */     return this.wrapped._getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _getInt(int index) {
/*  671 */     return this.wrapped._getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _getIntLE(int index) {
/*  676 */     return this.wrapped._getIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final long _getLong(int index) {
/*  681 */     return this.wrapped._getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final long _getLongLE(int index) {
/*  686 */     return this.wrapped._getLongLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  691 */     this.wrapped.getBytes(index, dst, dstIndex, length);
/*  692 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuffer dst) {
/*  697 */     this.wrapped.getBytes(index, dst);
/*  698 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  703 */     this.wrapped.getBytes(index, dst, dstIndex, length);
/*  704 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/*  709 */     return this.wrapped.getBytes(index, out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/*  714 */     this.wrapped.getBytes(index, out, length);
/*  715 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setByte(int index, int value) {
/*  720 */     this.wrapped.setByte(index, value);
/*  721 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setByte(int index, int value) {
/*  726 */     this.wrapped._setByte(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setShort(int index, int value) {
/*  731 */     this.wrapped.setShort(index, value);
/*  732 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setShort(int index, int value) {
/*  737 */     this.wrapped._setShort(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setShortLE(int index, int value) {
/*  742 */     this.wrapped._setShortLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setMedium(int index, int value) {
/*  747 */     this.wrapped.setMedium(index, value);
/*  748 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setMedium(int index, int value) {
/*  753 */     this.wrapped._setMedium(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setMediumLE(int index, int value) {
/*  758 */     this.wrapped._setMediumLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setInt(int index, int value) {
/*  763 */     this.wrapped.setInt(index, value);
/*  764 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setInt(int index, int value) {
/*  769 */     this.wrapped._setInt(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setIntLE(int index, int value) {
/*  774 */     this.wrapped._setIntLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setLong(int index, long value) {
/*  779 */     this.wrapped.setLong(index, value);
/*  780 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setLong(int index, long value) {
/*  785 */     this.wrapped._setLong(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _setLongLE(int index, long value) {
/*  790 */     this.wrapped._setLongLE(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/*  795 */     this.wrapped.setBytes(index, src, srcIndex, length);
/*  796 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuffer src) {
/*  801 */     this.wrapped.setBytes(index, src);
/*  802 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/*  807 */     this.wrapped.setBytes(index, src, srcIndex, length);
/*  808 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException {
/*  813 */     return this.wrapped.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/*  818 */     return this.wrapped.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/*  823 */     return this.wrapped.copy(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf component(int cIndex) {
/*  828 */     return this.wrapped.component(cIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf componentAtOffset(int offset) {
/*  833 */     return this.wrapped.componentAtOffset(offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf internalComponent(int cIndex) {
/*  838 */     return this.wrapped.internalComponent(cIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf internalComponentAtOffset(int offset) {
/*  843 */     return this.wrapped.internalComponentAtOffset(offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/*  848 */     return this.wrapped.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/*  853 */     return this.wrapped.internalNioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/*  858 */     return this.wrapped.nioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  863 */     return this.wrapped.nioBuffers(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf consolidate() {
/*  868 */     this.wrapped.consolidate();
/*  869 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf consolidate(int cIndex, int numComponents) {
/*  874 */     this.wrapped.consolidate(cIndex, numComponents);
/*  875 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardReadComponents() {
/*  880 */     this.wrapped.discardReadComponents();
/*  881 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardReadBytes() {
/*  886 */     this.wrapped.discardReadBytes();
/*  887 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String toString() {
/*  892 */     return this.wrapped.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf readerIndex(int readerIndex) {
/*  897 */     this.wrapped.readerIndex(readerIndex);
/*  898 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf writerIndex(int writerIndex) {
/*  903 */     this.wrapped.writerIndex(writerIndex);
/*  904 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf setIndex(int readerIndex, int writerIndex) {
/*  909 */     this.wrapped.setIndex(readerIndex, writerIndex);
/*  910 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf clear() {
/*  915 */     this.wrapped.clear();
/*  916 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf markReaderIndex() {
/*  921 */     this.wrapped.markReaderIndex();
/*  922 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf resetReaderIndex() {
/*  927 */     this.wrapped.resetReaderIndex();
/*  928 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf markWriterIndex() {
/*  933 */     this.wrapped.markWriterIndex();
/*  934 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CompositeByteBuf resetWriterIndex() {
/*  939 */     this.wrapped.resetWriterIndex();
/*  940 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf ensureWritable(int minWritableBytes) {
/*  945 */     this.wrapped.ensureWritable(minWritableBytes);
/*  946 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst) {
/*  951 */     this.wrapped.getBytes(index, dst);
/*  952 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  957 */     this.wrapped.getBytes(index, dst, length);
/*  958 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst) {
/*  963 */     this.wrapped.getBytes(index, dst);
/*  964 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBoolean(int index, boolean value) {
/*  969 */     this.wrapped.setBoolean(index, value);
/*  970 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setChar(int index, int value) {
/*  975 */     this.wrapped.setChar(index, value);
/*  976 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setFloat(int index, float value) {
/*  981 */     this.wrapped.setFloat(index, value);
/*  982 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setDouble(int index, double value) {
/*  987 */     this.wrapped.setDouble(index, value);
/*  988 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src) {
/*  993 */     this.wrapped.setBytes(index, src);
/*  994 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int length) {
/*  999 */     this.wrapped.setBytes(index, src, length);
/* 1000 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src) {
/* 1005 */     this.wrapped.setBytes(index, src);
/* 1006 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setZero(int index, int length) {
/* 1011 */     this.wrapped.setZero(index, length);
/* 1012 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst) {
/* 1017 */     this.wrapped.readBytes(dst);
/* 1018 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int length) {
/* 1023 */     this.wrapped.readBytes(dst, length);
/* 1024 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/* 1029 */     this.wrapped.readBytes(dst, dstIndex, length);
/* 1030 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst) {
/* 1035 */     this.wrapped.readBytes(dst);
/* 1036 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 1041 */     this.wrapped.readBytes(dst, dstIndex, length);
/* 1042 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuffer dst) {
/* 1047 */     this.wrapped.readBytes(dst);
/* 1048 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 1053 */     this.wrapped.readBytes(out, length);
/* 1054 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 1059 */     return this.wrapped.getBytes(index, out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 1064 */     return this.wrapped.setBytes(index, in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/* 1069 */     return this.wrapped.isReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/* 1074 */     return this.wrapped.asReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   protected SwappedByteBuf newSwappedByteBuf() {
/* 1079 */     return this.wrapped.newSwappedByteBuf();
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/* 1084 */     return this.wrapped.getCharSequence(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/* 1089 */     return this.wrapped.readCharSequence(length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/* 1094 */     return this.wrapped.setCharSequence(index, sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 1099 */     return this.wrapped.readBytes(out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException {
/* 1104 */     return this.wrapped.writeBytes(in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/* 1109 */     return this.wrapped.writeCharSequence(sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf skipBytes(int length) {
/* 1114 */     this.wrapped.skipBytes(length);
/* 1115 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBoolean(boolean value) {
/* 1120 */     this.wrapped.writeBoolean(value);
/* 1121 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeByte(int value) {
/* 1126 */     this.wrapped.writeByte(value);
/* 1127 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeShort(int value) {
/* 1132 */     this.wrapped.writeShort(value);
/* 1133 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeMedium(int value) {
/* 1138 */     this.wrapped.writeMedium(value);
/* 1139 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeInt(int value) {
/* 1144 */     this.wrapped.writeInt(value);
/* 1145 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeLong(long value) {
/* 1150 */     this.wrapped.writeLong(value);
/* 1151 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeChar(int value) {
/* 1156 */     this.wrapped.writeChar(value);
/* 1157 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeFloat(float value) {
/* 1162 */     this.wrapped.writeFloat(value);
/* 1163 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeDouble(double value) {
/* 1168 */     this.wrapped.writeDouble(value);
/* 1169 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src) {
/* 1174 */     this.wrapped.writeBytes(src);
/* 1175 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int length) {
/* 1180 */     this.wrapped.writeBytes(src, length);
/* 1181 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1186 */     this.wrapped.writeBytes(src, srcIndex, length);
/* 1187 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src) {
/* 1192 */     this.wrapped.writeBytes(src);
/* 1193 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1198 */     this.wrapped.writeBytes(src, srcIndex, length);
/* 1199 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuffer src) {
/* 1204 */     this.wrapped.writeBytes(src);
/* 1205 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeZero(int length) {
/* 1210 */     this.wrapped.writeZero(length);
/* 1211 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf retain(int increment) {
/* 1216 */     this.wrapped.retain(increment);
/* 1217 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf retain() {
/* 1222 */     this.wrapped.retain();
/* 1223 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf touch() {
/* 1228 */     this.wrapped.touch();
/* 1229 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf touch(Object hint) {
/* 1234 */     this.wrapped.touch(hint);
/* 1235 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1240 */     return this.wrapped.nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardSomeReadBytes() {
/* 1245 */     this.wrapped.discardSomeReadBytes();
/* 1246 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void deallocate() {
/* 1251 */     this.wrapped.deallocate();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteBuf unwrap() {
/* 1256 */     return this.wrapped;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\WrappedCompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */