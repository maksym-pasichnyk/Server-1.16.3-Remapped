/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.IllegalReferenceCountException;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.ResourceLeakDetectorFactory;
/*      */ import io.netty.util.internal.MathUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.SystemPropertyUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*      */ public abstract class AbstractByteBuf
/*      */   extends ByteBuf
/*      */ {
/*   45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractByteBuf.class);
/*      */ 
/*      */   
/*      */   private static final String PROP_MODE = "io.netty.buffer.bytebuf.checkAccessible";
/*      */   
/*   50 */   private static final boolean checkAccessible = SystemPropertyUtil.getBoolean("io.netty.buffer.bytebuf.checkAccessible", true); static {
/*   51 */     if (logger.isDebugEnabled()) {
/*   52 */       logger.debug("-D{}: {}", "io.netty.buffer.bytebuf.checkAccessible", Boolean.valueOf(checkAccessible));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*   57 */   static final ResourceLeakDetector<ByteBuf> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ByteBuf.class);
/*      */   
/*      */   int readerIndex;
/*      */   int writerIndex;
/*      */   private int markedReaderIndex;
/*      */   private int markedWriterIndex;
/*      */   private int maxCapacity;
/*      */   
/*      */   protected AbstractByteBuf(int maxCapacity) {
/*   66 */     if (maxCapacity < 0) {
/*   67 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)");
/*      */     }
/*   69 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*   74 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*   80 */     if (isReadOnly()) {
/*   81 */       return this;
/*      */     }
/*   83 */     return Unpooled.unmodifiableBuffer(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*   88 */     return this.maxCapacity;
/*      */   }
/*      */   
/*      */   protected final void maxCapacity(int maxCapacity) {
/*   92 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*   97 */     return this.readerIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex) {
/*  102 */     if (readerIndex < 0 || readerIndex > this.writerIndex)
/*  103 */       throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", new Object[] {
/*  104 */               Integer.valueOf(readerIndex), Integer.valueOf(this.writerIndex)
/*      */             })); 
/*  106 */     this.readerIndex = readerIndex;
/*  107 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/*  112 */     return this.writerIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex) {
/*  117 */     if (writerIndex < this.readerIndex || writerIndex > capacity())
/*  118 */       throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", new Object[] {
/*      */               
/*  120 */               Integer.valueOf(writerIndex), Integer.valueOf(this.readerIndex), Integer.valueOf(capacity())
/*      */             })); 
/*  122 */     this.writerIndex = writerIndex;
/*  123 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex) {
/*  128 */     if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity())
/*  129 */       throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", new Object[] {
/*      */               
/*  131 */               Integer.valueOf(readerIndex), Integer.valueOf(writerIndex), Integer.valueOf(capacity())
/*      */             })); 
/*  133 */     setIndex0(readerIndex, writerIndex);
/*  134 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf clear() {
/*  139 */     this.readerIndex = this.writerIndex = 0;
/*  140 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  145 */     return (this.writerIndex > this.readerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int numBytes) {
/*  150 */     return (this.writerIndex - this.readerIndex >= numBytes);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  155 */     return (capacity() > this.writerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int numBytes) {
/*  160 */     return (capacity() - this.writerIndex >= numBytes);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  165 */     return this.writerIndex - this.readerIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  170 */     return capacity() - this.writerIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  175 */     return maxCapacity() - this.writerIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markReaderIndex() {
/*  180 */     this.markedReaderIndex = this.readerIndex;
/*  181 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetReaderIndex() {
/*  186 */     readerIndex(this.markedReaderIndex);
/*  187 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markWriterIndex() {
/*  192 */     this.markedWriterIndex = this.writerIndex;
/*  193 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetWriterIndex() {
/*  198 */     writerIndex(this.markedWriterIndex);
/*  199 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  204 */     ensureAccessible();
/*  205 */     if (this.readerIndex == 0) {
/*  206 */       return this;
/*      */     }
/*      */     
/*  209 */     if (this.readerIndex != this.writerIndex) {
/*  210 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  211 */       this.writerIndex -= this.readerIndex;
/*  212 */       adjustMarkers(this.readerIndex);
/*  213 */       this.readerIndex = 0;
/*      */     } else {
/*  215 */       adjustMarkers(this.readerIndex);
/*  216 */       this.writerIndex = this.readerIndex = 0;
/*      */     } 
/*  218 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/*  223 */     ensureAccessible();
/*  224 */     if (this.readerIndex == 0) {
/*  225 */       return this;
/*      */     }
/*      */     
/*  228 */     if (this.readerIndex == this.writerIndex) {
/*  229 */       adjustMarkers(this.readerIndex);
/*  230 */       this.writerIndex = this.readerIndex = 0;
/*  231 */       return this;
/*      */     } 
/*      */     
/*  234 */     if (this.readerIndex >= capacity() >>> 1) {
/*  235 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  236 */       this.writerIndex -= this.readerIndex;
/*  237 */       adjustMarkers(this.readerIndex);
/*  238 */       this.readerIndex = 0;
/*      */     } 
/*  240 */     return this;
/*      */   }
/*      */   
/*      */   protected final void adjustMarkers(int decrement) {
/*  244 */     int markedReaderIndex = this.markedReaderIndex;
/*  245 */     if (markedReaderIndex <= decrement) {
/*  246 */       this.markedReaderIndex = 0;
/*  247 */       int markedWriterIndex = this.markedWriterIndex;
/*  248 */       if (markedWriterIndex <= decrement) {
/*  249 */         this.markedWriterIndex = 0;
/*      */       } else {
/*  251 */         this.markedWriterIndex = markedWriterIndex - decrement;
/*      */       } 
/*      */     } else {
/*  254 */       this.markedReaderIndex = markedReaderIndex - decrement;
/*  255 */       this.markedWriterIndex -= decrement;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int minWritableBytes) {
/*  261 */     if (minWritableBytes < 0)
/*  262 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] {
/*  263 */               Integer.valueOf(minWritableBytes)
/*      */             })); 
/*  265 */     ensureWritable0(minWritableBytes);
/*  266 */     return this;
/*      */   }
/*      */   
/*      */   final void ensureWritable0(int minWritableBytes) {
/*  270 */     ensureAccessible();
/*  271 */     if (minWritableBytes <= writableBytes()) {
/*      */       return;
/*      */     }
/*      */     
/*  275 */     if (minWritableBytes > this.maxCapacity - this.writerIndex) {
/*  276 */       throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", new Object[] {
/*      */               
/*  278 */               Integer.valueOf(this.writerIndex), Integer.valueOf(minWritableBytes), Integer.valueOf(this.maxCapacity), this
/*      */             }));
/*      */     }
/*      */     
/*  282 */     int newCapacity = alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
/*      */ 
/*      */     
/*  285 */     capacity(newCapacity);
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  290 */     ensureAccessible();
/*  291 */     if (minWritableBytes < 0) {
/*  292 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] {
/*  293 */               Integer.valueOf(minWritableBytes)
/*      */             }));
/*      */     }
/*  296 */     if (minWritableBytes <= writableBytes()) {
/*  297 */       return 0;
/*      */     }
/*      */     
/*  300 */     int maxCapacity = maxCapacity();
/*  301 */     int writerIndex = writerIndex();
/*  302 */     if (minWritableBytes > maxCapacity - writerIndex) {
/*  303 */       if (!force || capacity() == maxCapacity) {
/*  304 */         return 1;
/*      */       }
/*      */       
/*  307 */       capacity(maxCapacity);
/*  308 */       return 3;
/*      */     } 
/*      */ 
/*      */     
/*  312 */     int newCapacity = alloc().calculateNewCapacity(writerIndex + minWritableBytes, maxCapacity);
/*      */ 
/*      */     
/*  315 */     capacity(newCapacity);
/*  316 */     return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  321 */     if (endianness == null) {
/*  322 */       throw new NullPointerException("endianness");
/*      */     }
/*  324 */     if (endianness == order()) {
/*  325 */       return this;
/*      */     }
/*  327 */     return newSwappedByteBuf();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SwappedByteBuf newSwappedByteBuf() {
/*  334 */     return new SwappedByteBuf(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  339 */     checkIndex(index);
/*  340 */     return _getByte(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  347 */     return (getByte(index) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  352 */     return (short)(getByte(index) & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  357 */     checkIndex(index, 2);
/*  358 */     return _getShort(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  365 */     checkIndex(index, 2);
/*  366 */     return _getShortLE(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  373 */     return getShort(index) & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  378 */     return getShortLE(index) & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  383 */     checkIndex(index, 3);
/*  384 */     return _getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  391 */     checkIndex(index, 3);
/*  392 */     return _getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  399 */     int value = getUnsignedMedium(index);
/*  400 */     if ((value & 0x800000) != 0) {
/*  401 */       value |= 0xFF000000;
/*      */     }
/*  403 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  408 */     int value = getUnsignedMediumLE(index);
/*  409 */     if ((value & 0x800000) != 0) {
/*  410 */       value |= 0xFF000000;
/*      */     }
/*  412 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  417 */     checkIndex(index, 4);
/*  418 */     return _getInt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  425 */     checkIndex(index, 4);
/*  426 */     return _getIntLE(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  433 */     return getInt(index) & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  438 */     return getIntLE(index) & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  443 */     checkIndex(index, 8);
/*  444 */     return _getLong(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  451 */     checkIndex(index, 8);
/*  452 */     return _getLongLE(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  459 */     return (char)getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  464 */     return Float.intBitsToFloat(getInt(index));
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  469 */     return Double.longBitsToDouble(getLong(index));
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst) {
/*  474 */     getBytes(index, dst, 0, dst.length);
/*  475 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst) {
/*  480 */     getBytes(index, dst, dst.writableBytes());
/*  481 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  486 */     getBytes(index, dst, dst.writerIndex(), length);
/*  487 */     dst.writerIndex(dst.writerIndex() + length);
/*  488 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/*  494 */     return toString(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/*  499 */     CharSequence sequence = getCharSequence(this.readerIndex, length, charset);
/*  500 */     this.readerIndex += length;
/*  501 */     return sequence;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int index, int value) {
/*  506 */     checkIndex(index);
/*  507 */     _setByte(index, value);
/*  508 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value) {
/*  515 */     setByte(index, value ? 1 : 0);
/*  516 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int index, int value) {
/*  521 */     checkIndex(index, 2);
/*  522 */     _setShort(index, value);
/*  523 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value) {
/*  530 */     checkIndex(index, 2);
/*  531 */     _setShortLE(index, value);
/*  532 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int index, int value) {
/*  539 */     setShort(index, value);
/*  540 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int index, int value) {
/*  545 */     checkIndex(index, 3);
/*  546 */     _setMedium(index, value);
/*  547 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value) {
/*  554 */     checkIndex(index, 3);
/*  555 */     _setMediumLE(index, value);
/*  556 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int index, int value) {
/*  563 */     checkIndex(index, 4);
/*  564 */     _setInt(index, value);
/*  565 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value) {
/*  572 */     checkIndex(index, 4);
/*  573 */     _setIntLE(index, value);
/*  574 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int index, float value) {
/*  581 */     setInt(index, Float.floatToRawIntBits(value));
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int index, long value) {
/*  587 */     checkIndex(index, 8);
/*  588 */     _setLong(index, value);
/*  589 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value) {
/*  596 */     checkIndex(index, 8);
/*  597 */     _setLongLE(index, value);
/*  598 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int index, double value) {
/*  605 */     setLong(index, Double.doubleToRawLongBits(value));
/*  606 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src) {
/*  611 */     setBytes(index, src, 0, src.length);
/*  612 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src) {
/*  617 */     setBytes(index, src, src.readableBytes());
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length) {
/*  623 */     checkIndex(index, length);
/*  624 */     if (src == null) {
/*  625 */       throw new NullPointerException("src");
/*      */     }
/*  627 */     if (length > src.readableBytes()) {
/*  628 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] {
/*  629 */               Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src
/*      */             }));
/*      */     }
/*  632 */     setBytes(index, src, src.readerIndex(), length);
/*  633 */     src.readerIndex(src.readerIndex() + length);
/*  634 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int index, int length) {
/*  639 */     if (length == 0) {
/*  640 */       return this;
/*      */     }
/*      */     
/*  643 */     checkIndex(index, length);
/*      */     
/*  645 */     int nLong = length >>> 3;
/*  646 */     int nBytes = length & 0x7; int i;
/*  647 */     for (i = nLong; i > 0; i--) {
/*  648 */       _setLong(index, 0L);
/*  649 */       index += 8;
/*      */     } 
/*  651 */     if (nBytes == 4) {
/*  652 */       _setInt(index, 0);
/*      */     }
/*  654 */     else if (nBytes < 4) {
/*  655 */       for (i = nBytes; i > 0; i--) {
/*  656 */         _setByte(index, 0);
/*  657 */         index++;
/*      */       } 
/*      */     } else {
/*  660 */       _setInt(index, 0);
/*  661 */       index += 4;
/*  662 */       for (i = nBytes - 4; i > 0; i--) {
/*  663 */         _setByte(index, 0);
/*  664 */         index++;
/*      */       } 
/*      */     } 
/*  667 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/*  672 */     return setCharSequence0(index, sequence, charset, false);
/*      */   }
/*      */   
/*      */   private int setCharSequence0(int index, CharSequence sequence, Charset charset, boolean expand) {
/*  676 */     if (charset.equals(CharsetUtil.UTF_8)) {
/*  677 */       int length = ByteBufUtil.utf8MaxBytes(sequence);
/*  678 */       if (expand) {
/*  679 */         ensureWritable0(length);
/*  680 */         checkIndex0(index, length);
/*      */       } else {
/*  682 */         checkIndex(index, length);
/*      */       } 
/*  684 */       return ByteBufUtil.writeUtf8(this, index, sequence, sequence.length());
/*      */     } 
/*  686 */     if (charset.equals(CharsetUtil.US_ASCII) || charset.equals(CharsetUtil.ISO_8859_1)) {
/*  687 */       int length = sequence.length();
/*  688 */       if (expand) {
/*  689 */         ensureWritable0(length);
/*  690 */         checkIndex0(index, length);
/*      */       } else {
/*  692 */         checkIndex(index, length);
/*      */       } 
/*  694 */       return ByteBufUtil.writeAscii(this, index, sequence, length);
/*      */     } 
/*  696 */     byte[] bytes = sequence.toString().getBytes(charset);
/*  697 */     if (expand) {
/*  698 */       ensureWritable0(bytes.length);
/*      */     }
/*      */     
/*  701 */     setBytes(index, bytes);
/*  702 */     return bytes.length;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  707 */     checkReadableBytes0(1);
/*  708 */     int i = this.readerIndex;
/*  709 */     byte b = _getByte(i);
/*  710 */     this.readerIndex = i + 1;
/*  711 */     return b;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  716 */     return (readByte() != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  721 */     return (short)(readByte() & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  726 */     checkReadableBytes0(2);
/*  727 */     short v = _getShort(this.readerIndex);
/*  728 */     this.readerIndex += 2;
/*  729 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  734 */     checkReadableBytes0(2);
/*  735 */     short v = _getShortLE(this.readerIndex);
/*  736 */     this.readerIndex += 2;
/*  737 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  742 */     return readShort() & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  747 */     return readShortLE() & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  752 */     int value = readUnsignedMedium();
/*  753 */     if ((value & 0x800000) != 0) {
/*  754 */       value |= 0xFF000000;
/*      */     }
/*  756 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  761 */     int value = readUnsignedMediumLE();
/*  762 */     if ((value & 0x800000) != 0) {
/*  763 */       value |= 0xFF000000;
/*      */     }
/*  765 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  770 */     checkReadableBytes0(3);
/*  771 */     int v = _getUnsignedMedium(this.readerIndex);
/*  772 */     this.readerIndex += 3;
/*  773 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  778 */     checkReadableBytes0(3);
/*  779 */     int v = _getUnsignedMediumLE(this.readerIndex);
/*  780 */     this.readerIndex += 3;
/*  781 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  786 */     checkReadableBytes0(4);
/*  787 */     int v = _getInt(this.readerIndex);
/*  788 */     this.readerIndex += 4;
/*  789 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  794 */     checkReadableBytes0(4);
/*  795 */     int v = _getIntLE(this.readerIndex);
/*  796 */     this.readerIndex += 4;
/*  797 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  802 */     return readInt() & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  807 */     return readIntLE() & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  812 */     checkReadableBytes0(8);
/*  813 */     long v = _getLong(this.readerIndex);
/*  814 */     this.readerIndex += 8;
/*  815 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  820 */     checkReadableBytes0(8);
/*  821 */     long v = _getLongLE(this.readerIndex);
/*  822 */     this.readerIndex += 8;
/*  823 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  828 */     return (char)readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  833 */     return Float.intBitsToFloat(readInt());
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  838 */     return Double.longBitsToDouble(readLong());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/*  843 */     checkReadableBytes(length);
/*  844 */     if (length == 0) {
/*  845 */       return Unpooled.EMPTY_BUFFER;
/*      */     }
/*      */     
/*  848 */     ByteBuf buf = alloc().buffer(length, this.maxCapacity);
/*  849 */     buf.writeBytes(this, this.readerIndex, length);
/*  850 */     this.readerIndex += length;
/*  851 */     return buf;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/*  856 */     checkReadableBytes(length);
/*  857 */     ByteBuf slice = slice(this.readerIndex, length);
/*  858 */     this.readerIndex += length;
/*  859 */     return slice;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/*  864 */     checkReadableBytes(length);
/*  865 */     ByteBuf slice = retainedSlice(this.readerIndex, length);
/*  866 */     this.readerIndex += length;
/*  867 */     return slice;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/*  872 */     checkReadableBytes(length);
/*  873 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  874 */     this.readerIndex += length;
/*  875 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst) {
/*  880 */     readBytes(dst, 0, dst.length);
/*  881 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst) {
/*  886 */     readBytes(dst, dst.writableBytes());
/*  887 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length) {
/*  892 */     if (length > dst.writableBytes())
/*  893 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", new Object[] {
/*  894 */               Integer.valueOf(length), Integer.valueOf(dst.writableBytes()), dst
/*      */             })); 
/*  896 */     readBytes(dst, dst.writerIndex(), length);
/*  897 */     dst.writerIndex(dst.writerIndex() + length);
/*  898 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/*  903 */     checkReadableBytes(length);
/*  904 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  905 */     this.readerIndex += length;
/*  906 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst) {
/*  911 */     int length = dst.remaining();
/*  912 */     checkReadableBytes(length);
/*  913 */     getBytes(this.readerIndex, dst);
/*  914 */     this.readerIndex += length;
/*  915 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/*  921 */     checkReadableBytes(length);
/*  922 */     int readBytes = getBytes(this.readerIndex, out, length);
/*  923 */     this.readerIndex += readBytes;
/*  924 */     return readBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/*  930 */     checkReadableBytes(length);
/*  931 */     int readBytes = getBytes(this.readerIndex, out, position, length);
/*  932 */     this.readerIndex += readBytes;
/*  933 */     return readBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) throws IOException {
/*  938 */     checkReadableBytes(length);
/*  939 */     getBytes(this.readerIndex, out, length);
/*  940 */     this.readerIndex += length;
/*  941 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int length) {
/*  946 */     checkReadableBytes(length);
/*  947 */     this.readerIndex += length;
/*  948 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value) {
/*  953 */     writeByte(value ? 1 : 0);
/*  954 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int value) {
/*  959 */     ensureWritable0(1);
/*  960 */     _setByte(this.writerIndex++, value);
/*  961 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int value) {
/*  966 */     ensureWritable0(2);
/*  967 */     _setShort(this.writerIndex, value);
/*  968 */     this.writerIndex += 2;
/*  969 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int value) {
/*  974 */     ensureWritable0(2);
/*  975 */     _setShortLE(this.writerIndex, value);
/*  976 */     this.writerIndex += 2;
/*  977 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int value) {
/*  982 */     ensureWritable0(3);
/*  983 */     _setMedium(this.writerIndex, value);
/*  984 */     this.writerIndex += 3;
/*  985 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int value) {
/*  990 */     ensureWritable0(3);
/*  991 */     _setMediumLE(this.writerIndex, value);
/*  992 */     this.writerIndex += 3;
/*  993 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int value) {
/*  998 */     ensureWritable0(4);
/*  999 */     _setInt(this.writerIndex, value);
/* 1000 */     this.writerIndex += 4;
/* 1001 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int value) {
/* 1006 */     ensureWritable0(4);
/* 1007 */     _setIntLE(this.writerIndex, value);
/* 1008 */     this.writerIndex += 4;
/* 1009 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long value) {
/* 1014 */     ensureWritable0(8);
/* 1015 */     _setLong(this.writerIndex, value);
/* 1016 */     this.writerIndex += 8;
/* 1017 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long value) {
/* 1022 */     ensureWritable0(8);
/* 1023 */     _setLongLE(this.writerIndex, value);
/* 1024 */     this.writerIndex += 8;
/* 1025 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int value) {
/* 1030 */     writeShort(value);
/* 1031 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float value) {
/* 1036 */     writeInt(Float.floatToRawIntBits(value));
/* 1037 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double value) {
/* 1042 */     writeLong(Double.doubleToRawLongBits(value));
/* 1043 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1048 */     ensureWritable(length);
/* 1049 */     setBytes(this.writerIndex, src, srcIndex, length);
/* 1050 */     this.writerIndex += length;
/* 1051 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src) {
/* 1056 */     writeBytes(src, 0, src.length);
/* 1057 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src) {
/* 1062 */     writeBytes(src, src.readableBytes());
/* 1063 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length) {
/* 1068 */     if (length > src.readableBytes())
/* 1069 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] {
/* 1070 */               Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src
/*      */             })); 
/* 1072 */     writeBytes(src, src.readerIndex(), length);
/* 1073 */     src.readerIndex(src.readerIndex() + length);
/* 1074 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1079 */     ensureWritable(length);
/* 1080 */     setBytes(this.writerIndex, src, srcIndex, length);
/* 1081 */     this.writerIndex += length;
/* 1082 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src) {
/* 1087 */     int length = src.remaining();
/* 1088 */     ensureWritable0(length);
/* 1089 */     setBytes(this.writerIndex, src);
/* 1090 */     this.writerIndex += length;
/* 1091 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException {
/* 1097 */     ensureWritable(length);
/* 1098 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/* 1099 */     if (writtenBytes > 0) {
/* 1100 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1102 */     return writtenBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
/* 1107 */     ensureWritable(length);
/* 1108 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/* 1109 */     if (writtenBytes > 0) {
/* 1110 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1112 */     return writtenBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException {
/* 1117 */     ensureWritable(length);
/* 1118 */     int writtenBytes = setBytes(this.writerIndex, in, position, length);
/* 1119 */     if (writtenBytes > 0) {
/* 1120 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1122 */     return writtenBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int length) {
/* 1127 */     if (length == 0) {
/* 1128 */       return this;
/*      */     }
/*      */     
/* 1131 */     ensureWritable(length);
/* 1132 */     int wIndex = this.writerIndex;
/* 1133 */     checkIndex0(wIndex, length);
/*      */     
/* 1135 */     int nLong = length >>> 3;
/* 1136 */     int nBytes = length & 0x7; int i;
/* 1137 */     for (i = nLong; i > 0; i--) {
/* 1138 */       _setLong(wIndex, 0L);
/* 1139 */       wIndex += 8;
/*      */     } 
/* 1141 */     if (nBytes == 4) {
/* 1142 */       _setInt(wIndex, 0);
/* 1143 */       wIndex += 4;
/* 1144 */     } else if (nBytes < 4) {
/* 1145 */       for (i = nBytes; i > 0; i--) {
/* 1146 */         _setByte(wIndex, 0);
/* 1147 */         wIndex++;
/*      */       } 
/*      */     } else {
/* 1150 */       _setInt(wIndex, 0);
/* 1151 */       wIndex += 4;
/* 1152 */       for (i = nBytes - 4; i > 0; i--) {
/* 1153 */         _setByte(wIndex, 0);
/* 1154 */         wIndex++;
/*      */       } 
/*      */     } 
/* 1157 */     this.writerIndex = wIndex;
/* 1158 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/* 1163 */     int written = setCharSequence0(this.writerIndex, sequence, charset, true);
/* 1164 */     this.writerIndex += written;
/* 1165 */     return written;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/* 1170 */     return copy(this.readerIndex, readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/* 1175 */     ensureAccessible();
/* 1176 */     return new UnpooledDuplicatedByteBuf(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/* 1181 */     return duplicate().retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/* 1186 */     return slice(this.readerIndex, readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/* 1191 */     return slice().retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/* 1196 */     ensureAccessible();
/* 1197 */     return new UnpooledSlicedByteBuf(this, index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/* 1202 */     return slice(index, length).retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/* 1207 */     return nioBuffer(this.readerIndex, readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1212 */     return nioBuffers(this.readerIndex, readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset charset) {
/* 1217 */     return toString(this.readerIndex, readableBytes(), charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/* 1222 */     return ByteBufUtil.decodeString(this, index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/* 1227 */     return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/* 1232 */     return bytesBefore(readerIndex(), readableBytes(), value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/* 1237 */     checkReadableBytes(length);
/* 1238 */     return bytesBefore(readerIndex(), length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/* 1243 */     int endIndex = indexOf(index, index + length, value);
/* 1244 */     if (endIndex < 0) {
/* 1245 */       return -1;
/*      */     }
/* 1247 */     return endIndex - index;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/* 1252 */     ensureAccessible();
/*      */     try {
/* 1254 */       return forEachByteAsc0(this.readerIndex, this.writerIndex, processor);
/* 1255 */     } catch (Exception e) {
/* 1256 */       PlatformDependent.throwException(e);
/* 1257 */       return -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/* 1263 */     checkIndex(index, length);
/*      */     try {
/* 1265 */       return forEachByteAsc0(index, index + length, processor);
/* 1266 */     } catch (Exception e) {
/* 1267 */       PlatformDependent.throwException(e);
/* 1268 */       return -1;
/*      */     } 
/*      */   }
/*      */   
/*      */   private int forEachByteAsc0(int start, int end, ByteProcessor processor) throws Exception {
/* 1273 */     for (; start < end; start++) {
/* 1274 */       if (!processor.process(_getByte(start))) {
/* 1275 */         return start;
/*      */       }
/*      */     } 
/*      */     
/* 1279 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/* 1284 */     ensureAccessible();
/*      */     try {
/* 1286 */       return forEachByteDesc0(this.writerIndex - 1, this.readerIndex, processor);
/* 1287 */     } catch (Exception e) {
/* 1288 */       PlatformDependent.throwException(e);
/* 1289 */       return -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/* 1295 */     checkIndex(index, length);
/*      */     try {
/* 1297 */       return forEachByteDesc0(index + length - 1, index, processor);
/* 1298 */     } catch (Exception e) {
/* 1299 */       PlatformDependent.throwException(e);
/* 1300 */       return -1;
/*      */     } 
/*      */   }
/*      */   
/*      */   private int forEachByteDesc0(int rStart, int rEnd, ByteProcessor processor) throws Exception {
/* 1305 */     for (; rStart >= rEnd; rStart--) {
/* 1306 */       if (!processor.process(_getByte(rStart))) {
/* 1307 */         return rStart;
/*      */       }
/*      */     } 
/* 1310 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1315 */     return ByteBufUtil.hashCode(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/* 1320 */     return (this == o || (o instanceof ByteBuf && ByteBufUtil.equals(this, (ByteBuf)o)));
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf that) {
/* 1325 */     return ByteBufUtil.compare(this, that);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1330 */     if (refCnt() == 0) {
/* 1331 */       return StringUtil.simpleClassName(this) + "(freed)";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1338 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append("(ridx: ").append(this.readerIndex).append(", widx: ").append(this.writerIndex).append(", cap: ").append(capacity());
/* 1339 */     if (this.maxCapacity != Integer.MAX_VALUE) {
/* 1340 */       buf.append('/').append(this.maxCapacity);
/*      */     }
/*      */     
/* 1343 */     ByteBuf unwrapped = unwrap();
/* 1344 */     if (unwrapped != null) {
/* 1345 */       buf.append(", unwrapped: ").append(unwrapped);
/*      */     }
/* 1347 */     buf.append(')');
/* 1348 */     return buf.toString();
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index) {
/* 1352 */     checkIndex(index, 1);
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index, int fieldLength) {
/* 1356 */     ensureAccessible();
/* 1357 */     checkIndex0(index, fieldLength);
/*      */   }
/*      */   
/*      */   final void checkIndex0(int index, int fieldLength) {
/* 1361 */     if (MathUtil.isOutOfBounds(index, fieldLength, capacity()))
/* 1362 */       throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", new Object[] {
/* 1363 */               Integer.valueOf(index), Integer.valueOf(fieldLength), Integer.valueOf(capacity())
/*      */             })); 
/*      */   }
/*      */   
/*      */   protected final void checkSrcIndex(int index, int length, int srcIndex, int srcCapacity) {
/* 1368 */     checkIndex(index, length);
/* 1369 */     if (MathUtil.isOutOfBounds(srcIndex, length, srcCapacity))
/* 1370 */       throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", new Object[] {
/* 1371 */               Integer.valueOf(srcIndex), Integer.valueOf(length), Integer.valueOf(srcCapacity)
/*      */             })); 
/*      */   }
/*      */   
/*      */   protected final void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
/* 1376 */     checkIndex(index, length);
/* 1377 */     if (MathUtil.isOutOfBounds(dstIndex, length, dstCapacity)) {
/* 1378 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] {
/* 1379 */               Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dstCapacity)
/*      */             }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void checkReadableBytes(int minimumReadableBytes) {
/* 1389 */     if (minimumReadableBytes < 0) {
/* 1390 */       throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)");
/*      */     }
/* 1392 */     checkReadableBytes0(minimumReadableBytes);
/*      */   }
/*      */   
/*      */   protected final void checkNewCapacity(int newCapacity) {
/* 1396 */     ensureAccessible();
/* 1397 */     if (newCapacity < 0 || newCapacity > maxCapacity()) {
/* 1398 */       throw new IllegalArgumentException("newCapacity: " + newCapacity + " (expected: 0-" + maxCapacity() + ')');
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkReadableBytes0(int minimumReadableBytes) {
/* 1403 */     ensureAccessible();
/* 1404 */     if (this.readerIndex > this.writerIndex - minimumReadableBytes) {
/* 1405 */       throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", new Object[] {
/*      */               
/* 1407 */               Integer.valueOf(this.readerIndex), Integer.valueOf(minimumReadableBytes), Integer.valueOf(this.writerIndex), this
/*      */             }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void ensureAccessible() {
/* 1416 */     if (checkAccessible && refCnt() == 0) {
/* 1417 */       throw new IllegalReferenceCountException(0);
/*      */     }
/*      */   }
/*      */   
/*      */   final void setIndex0(int readerIndex, int writerIndex) {
/* 1422 */     this.readerIndex = readerIndex;
/* 1423 */     this.writerIndex = writerIndex;
/*      */   }
/*      */   
/*      */   final void discardMarks() {
/* 1427 */     this.markedReaderIndex = this.markedWriterIndex = 0;
/*      */   }
/*      */   
/*      */   protected abstract byte _getByte(int paramInt);
/*      */   
/*      */   protected abstract short _getShort(int paramInt);
/*      */   
/*      */   protected abstract short _getShortLE(int paramInt);
/*      */   
/*      */   protected abstract int _getUnsignedMedium(int paramInt);
/*      */   
/*      */   protected abstract int _getUnsignedMediumLE(int paramInt);
/*      */   
/*      */   protected abstract int _getInt(int paramInt);
/*      */   
/*      */   protected abstract int _getIntLE(int paramInt);
/*      */   
/*      */   protected abstract long _getLong(int paramInt);
/*      */   
/*      */   protected abstract long _getLongLE(int paramInt);
/*      */   
/*      */   protected abstract void _setByte(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setShort(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setShortLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setMedium(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setMediumLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setInt(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setIntLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setLong(int paramInt, long paramLong);
/*      */   
/*      */   protected abstract void _setLongLE(int paramInt, long paramLong);
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\AbstractByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */