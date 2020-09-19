/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.Recycler;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class PooledSlicedByteBuf
/*     */   extends AbstractPooledDerivedByteBuf
/*     */ {
/*  35 */   private static final Recycler<PooledSlicedByteBuf> RECYCLER = new Recycler<PooledSlicedByteBuf>()
/*     */     {
/*     */       protected PooledSlicedByteBuf newObject(Recycler.Handle<PooledSlicedByteBuf> handle) {
/*  38 */         return new PooledSlicedByteBuf(handle);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static PooledSlicedByteBuf newInstance(AbstractByteBuf unwrapped, ByteBuf wrapped, int index, int length) {
/*  44 */     AbstractUnpooledSlicedByteBuf.checkSliceOutOfBounds(index, length, unwrapped);
/*  45 */     return newInstance0(unwrapped, wrapped, index, length);
/*     */   }
/*     */   int adjustment;
/*     */   
/*     */   private static PooledSlicedByteBuf newInstance0(AbstractByteBuf unwrapped, ByteBuf wrapped, int adjustment, int length) {
/*  50 */     PooledSlicedByteBuf slice = (PooledSlicedByteBuf)RECYCLER.get();
/*  51 */     slice.init(unwrapped, wrapped, 0, length, length);
/*  52 */     slice.discardMarks();
/*  53 */     slice.adjustment = adjustment;
/*     */     
/*  55 */     return slice;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PooledSlicedByteBuf(Recycler.Handle<PooledSlicedByteBuf> handle) {
/*  61 */     super((Recycler.Handle)handle);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  66 */     return maxCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/*  71 */     throw new UnsupportedOperationException("sliced buffer");
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/*  76 */     return idx(unwrap().arrayOffset());
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/*  81 */     return unwrap().memoryAddress() + this.adjustment;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/*  86 */     checkIndex0(index, length);
/*  87 */     return unwrap().nioBuffer(idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  92 */     checkIndex0(index, length);
/*  93 */     return unwrap().nioBuffers(idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/*  98 */     checkIndex0(index, length);
/*  99 */     return unwrap().copy(idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf slice(int index, int length) {
/* 104 */     checkIndex0(index, length);
/* 105 */     return super.slice(idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retainedSlice(int index, int length) {
/* 110 */     checkIndex0(index, length);
/* 111 */     return newInstance0(unwrap(), this, idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf duplicate() {
/* 116 */     return duplicate0().setIndex(idx(readerIndex()), idx(writerIndex()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retainedDuplicate() {
/* 121 */     return PooledDuplicatedByteBuf.newInstance(unwrap(), this, idx(readerIndex()), idx(writerIndex()));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 126 */     checkIndex0(index, 1);
/* 127 */     return unwrap().getByte(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 132 */     return unwrap()._getByte(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/* 137 */     checkIndex0(index, 2);
/* 138 */     return unwrap().getShort(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 143 */     return unwrap()._getShort(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortLE(int index) {
/* 148 */     checkIndex0(index, 2);
/* 149 */     return unwrap().getShortLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 154 */     return unwrap()._getShortLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/* 159 */     checkIndex0(index, 3);
/* 160 */     return unwrap().getUnsignedMedium(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 165 */     return unwrap()._getUnsignedMedium(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMediumLE(int index) {
/* 170 */     checkIndex0(index, 3);
/* 171 */     return unwrap().getUnsignedMediumLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 176 */     return unwrap()._getUnsignedMediumLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 181 */     checkIndex0(index, 4);
/* 182 */     return unwrap().getInt(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 187 */     return unwrap()._getInt(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntLE(int index) {
/* 192 */     checkIndex0(index, 4);
/* 193 */     return unwrap().getIntLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 198 */     return unwrap()._getIntLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 203 */     checkIndex0(index, 8);
/* 204 */     return unwrap().getLong(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 209 */     return unwrap()._getLong(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongLE(int index) {
/* 214 */     checkIndex0(index, 8);
/* 215 */     return unwrap().getLongLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 220 */     return unwrap()._getLongLE(idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 225 */     checkIndex0(index, length);
/* 226 */     unwrap().getBytes(idx(index), dst, dstIndex, length);
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 232 */     checkIndex0(index, length);
/* 233 */     unwrap().getBytes(idx(index), dst, dstIndex, length);
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 239 */     checkIndex0(index, dst.remaining());
/* 240 */     unwrap().getBytes(idx(index), dst);
/* 241 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 246 */     checkIndex0(index, 1);
/* 247 */     unwrap().setByte(idx(index), value);
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 253 */     unwrap()._setByte(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 258 */     checkIndex0(index, 2);
/* 259 */     unwrap().setShort(idx(index), value);
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 265 */     unwrap()._setShort(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShortLE(int index, int value) {
/* 270 */     checkIndex0(index, 2);
/* 271 */     unwrap().setShortLE(idx(index), value);
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 277 */     unwrap()._setShortLE(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 282 */     checkIndex0(index, 3);
/* 283 */     unwrap().setMedium(idx(index), value);
/* 284 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 289 */     unwrap()._setMedium(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMediumLE(int index, int value) {
/* 294 */     checkIndex0(index, 3);
/* 295 */     unwrap().setMediumLE(idx(index), value);
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 301 */     unwrap()._setMediumLE(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 306 */     checkIndex0(index, 4);
/* 307 */     unwrap().setInt(idx(index), value);
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 313 */     unwrap()._setInt(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setIntLE(int index, int value) {
/* 318 */     checkIndex0(index, 4);
/* 319 */     unwrap().setIntLE(idx(index), value);
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 325 */     unwrap()._setIntLE(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 330 */     checkIndex0(index, 8);
/* 331 */     unwrap().setLong(idx(index), value);
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 337 */     unwrap()._setLong(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLongLE(int index, long value) {
/* 342 */     checkIndex0(index, 8);
/* 343 */     unwrap().setLongLE(idx(index), value);
/* 344 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 349 */     unwrap().setLongLE(idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 354 */     checkIndex0(index, length);
/* 355 */     unwrap().setBytes(idx(index), src, srcIndex, length);
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 361 */     checkIndex0(index, length);
/* 362 */     unwrap().setBytes(idx(index), src, srcIndex, length);
/* 363 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 368 */     checkIndex0(index, src.remaining());
/* 369 */     unwrap().setBytes(idx(index), src);
/* 370 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 376 */     checkIndex0(index, length);
/* 377 */     unwrap().getBytes(idx(index), out, length);
/* 378 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 384 */     checkIndex0(index, length);
/* 385 */     return unwrap().getBytes(idx(index), out, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 391 */     checkIndex0(index, length);
/* 392 */     return unwrap().getBytes(idx(index), out, position, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 398 */     checkIndex0(index, length);
/* 399 */     return unwrap().setBytes(idx(index), in, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 405 */     checkIndex0(index, length);
/* 406 */     return unwrap().setBytes(idx(index), in, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 412 */     checkIndex0(index, length);
/* 413 */     return unwrap().setBytes(idx(index), in, position, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int forEachByte(int index, int length, ByteProcessor processor) {
/* 418 */     checkIndex0(index, length);
/* 419 */     int ret = unwrap().forEachByte(idx(index), length, processor);
/* 420 */     if (ret < this.adjustment) {
/* 421 */       return -1;
/*     */     }
/* 423 */     return ret - this.adjustment;
/*     */   }
/*     */ 
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/* 428 */     checkIndex0(index, length);
/* 429 */     int ret = unwrap().forEachByteDesc(idx(index), length, processor);
/* 430 */     if (ret < this.adjustment) {
/* 431 */       return -1;
/*     */     }
/* 433 */     return ret - this.adjustment;
/*     */   }
/*     */   
/*     */   private int idx(int index) {
/* 437 */     return index + this.adjustment;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledSlicedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */