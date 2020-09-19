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
/*     */ final class PooledDuplicatedByteBuf
/*     */   extends AbstractPooledDerivedByteBuf
/*     */ {
/*  33 */   private static final Recycler<PooledDuplicatedByteBuf> RECYCLER = new Recycler<PooledDuplicatedByteBuf>()
/*     */     {
/*     */       protected PooledDuplicatedByteBuf newObject(Recycler.Handle<PooledDuplicatedByteBuf> handle) {
/*  36 */         return new PooledDuplicatedByteBuf(handle);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static PooledDuplicatedByteBuf newInstance(AbstractByteBuf unwrapped, ByteBuf wrapped, int readerIndex, int writerIndex) {
/*  42 */     PooledDuplicatedByteBuf duplicate = (PooledDuplicatedByteBuf)RECYCLER.get();
/*  43 */     duplicate.init(unwrapped, wrapped, readerIndex, writerIndex, unwrapped.maxCapacity());
/*  44 */     duplicate.markReaderIndex();
/*  45 */     duplicate.markWriterIndex();
/*     */     
/*  47 */     return duplicate;
/*     */   }
/*     */   
/*     */   private PooledDuplicatedByteBuf(Recycler.Handle<PooledDuplicatedByteBuf> handle) {
/*  51 */     super((Recycler.Handle)handle);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  56 */     return unwrap().capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf capacity(int newCapacity) {
/*  61 */     unwrap().capacity(newCapacity);
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int arrayOffset() {
/*  67 */     return unwrap().arrayOffset();
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/*  72 */     return unwrap().memoryAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length) {
/*  77 */     return unwrap().nioBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length) {
/*  82 */     return unwrap().nioBuffers(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/*  87 */     return unwrap().copy(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retainedSlice(int index, int length) {
/*  92 */     return PooledSlicedByteBuf.newInstance(unwrap(), this, index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf duplicate() {
/*  97 */     return duplicate0().setIndex(readerIndex(), writerIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retainedDuplicate() {
/* 102 */     return newInstance(unwrap(), this, readerIndex(), writerIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 107 */     return unwrap().getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/* 112 */     return unwrap()._getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/* 117 */     return unwrap().getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/* 122 */     return unwrap()._getShort(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortLE(int index) {
/* 127 */     return unwrap().getShortLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/* 132 */     return unwrap()._getShortLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMedium(int index) {
/* 137 */     return unwrap().getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/* 142 */     return unwrap()._getUnsignedMedium(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnsignedMediumLE(int index) {
/* 147 */     return unwrap().getUnsignedMediumLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/* 152 */     return unwrap()._getUnsignedMediumLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 157 */     return unwrap().getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/* 162 */     return unwrap()._getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntLE(int index) {
/* 167 */     return unwrap().getIntLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/* 172 */     return unwrap()._getIntLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 177 */     return unwrap().getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/* 182 */     return unwrap()._getLong(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongLE(int index) {
/* 187 */     return unwrap().getLongLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/* 192 */     return unwrap()._getLongLE(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 197 */     unwrap().getBytes(index, dst, dstIndex, length);
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 203 */     unwrap().getBytes(index, dst, dstIndex, length);
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 209 */     unwrap().getBytes(index, dst);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setByte(int index, int value) {
/* 215 */     unwrap().setByte(index, value);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 221 */     unwrap()._setByte(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShort(int index, int value) {
/* 226 */     unwrap().setShort(index, value);
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 232 */     unwrap()._setShort(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setShortLE(int index, int value) {
/* 237 */     unwrap().setShortLE(index, value);
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 243 */     unwrap()._setShortLE(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMedium(int index, int value) {
/* 248 */     unwrap().setMedium(index, value);
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 254 */     unwrap()._setMedium(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setMediumLE(int index, int value) {
/* 259 */     unwrap().setMediumLE(index, value);
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 265 */     unwrap()._setMediumLE(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setInt(int index, int value) {
/* 270 */     unwrap().setInt(index, value);
/* 271 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 276 */     unwrap()._setInt(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setIntLE(int index, int value) {
/* 281 */     unwrap().setIntLE(index, value);
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 287 */     unwrap()._setIntLE(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLong(int index, long value) {
/* 292 */     unwrap().setLong(index, value);
/* 293 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 298 */     unwrap()._setLong(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setLongLE(int index, long value) {
/* 303 */     unwrap().setLongLE(index, value);
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 309 */     unwrap().setLongLE(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 314 */     unwrap().setBytes(index, src, srcIndex, length);
/* 315 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 320 */     unwrap().setBytes(index, src, srcIndex, length);
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src) {
/* 326 */     unwrap().setBytes(index, src);
/* 327 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 333 */     unwrap().getBytes(index, out, length);
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 340 */     return unwrap().getBytes(index, out, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 346 */     return unwrap().getBytes(index, out, position, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 352 */     return unwrap().setBytes(index, in, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 358 */     return unwrap().setBytes(index, in, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 364 */     return unwrap().setBytes(index, in, position, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int forEachByte(int index, int length, ByteProcessor processor) {
/* 369 */     return unwrap().forEachByte(index, length, processor);
/*     */   }
/*     */ 
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/* 374 */     return unwrap().forEachByteDesc(index, length, processor);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledDuplicatedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */