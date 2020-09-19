/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
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
/*     */ class PooledHeapByteBuf
/*     */   extends PooledByteBuf<byte[]>
/*     */ {
/*  31 */   private static final Recycler<PooledHeapByteBuf> RECYCLER = new Recycler<PooledHeapByteBuf>()
/*     */     {
/*     */       protected PooledHeapByteBuf newObject(Recycler.Handle<PooledHeapByteBuf> handle) {
/*  34 */         return new PooledHeapByteBuf(handle, 0);
/*     */       }
/*     */     };
/*     */   
/*     */   static PooledHeapByteBuf newInstance(int maxCapacity) {
/*  39 */     PooledHeapByteBuf buf = (PooledHeapByteBuf)RECYCLER.get();
/*  40 */     buf.reuse(maxCapacity);
/*  41 */     return buf;
/*     */   }
/*     */   
/*     */   PooledHeapByteBuf(Recycler.Handle<? extends PooledHeapByteBuf> recyclerHandle, int maxCapacity) {
/*  45 */     super((Recycler.Handle)recyclerHandle, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDirect() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  55 */     return HeapByteBufUtil.getByte(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  60 */     return HeapByteBufUtil.getShort(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShortLE(int index) {
/*  65 */     return HeapByteBufUtil.getShortLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  70 */     return HeapByteBufUtil.getUnsignedMedium(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMediumLE(int index) {
/*  75 */     return HeapByteBufUtil.getUnsignedMediumLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/*  80 */     return HeapByteBufUtil.getInt(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getIntLE(int index) {
/*  85 */     return HeapByteBufUtil.getIntLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/*  90 */     return HeapByteBufUtil.getLong(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLongLE(int index) {
/*  95 */     return HeapByteBufUtil.getLongLE(this.memory, idx(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 100 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 101 */     if (dst.hasMemoryAddress()) {
/* 102 */       PlatformDependent.copyMemory(this.memory, idx(index), dst.memoryAddress() + dstIndex, length);
/* 103 */     } else if (dst.hasArray()) {
/* 104 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/* 106 */       dst.setBytes(dstIndex, this.memory, idx(index), length);
/*     */     } 
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 113 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 114 */     System.arraycopy(this.memory, idx(index), dst, dstIndex, length);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf getBytes(int index, ByteBuffer dst) {
/* 120 */     checkIndex(index, dst.remaining());
/* 121 */     dst.put(this.memory, idx(index), dst.remaining());
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 127 */     checkIndex(index, length);
/* 128 */     out.write(this.memory, idx(index), length);
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 134 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/*     */     ByteBuffer tmpBuf;
/* 138 */     checkIndex(index, length);
/* 139 */     index = idx(index);
/*     */     
/* 141 */     if (internal) {
/* 142 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 144 */       tmpBuf = ByteBuffer.wrap(this.memory);
/*     */     } 
/* 146 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 151 */     return getBytes(index, out, position, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, FileChannel out, long position, int length, boolean internal) throws IOException {
/* 155 */     checkIndex(index, length);
/* 156 */     index = idx(index);
/* 157 */     ByteBuffer tmpBuf = internal ? internalNioBuffer() : ByteBuffer.wrap(this.memory);
/* 158 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length), position);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 163 */     checkReadableBytes(length);
/* 164 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 165 */     this.readerIndex += readBytes;
/* 166 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int readBytes(FileChannel out, long position, int length) throws IOException {
/* 171 */     checkReadableBytes(length);
/* 172 */     int readBytes = getBytes(this.readerIndex, out, position, length, true);
/* 173 */     this.readerIndex += readBytes;
/* 174 */     return readBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setByte(int index, int value) {
/* 179 */     HeapByteBufUtil.setByte(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShort(int index, int value) {
/* 184 */     HeapByteBufUtil.setShort(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setShortLE(int index, int value) {
/* 189 */     HeapByteBufUtil.setShortLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMedium(int index, int value) {
/* 194 */     HeapByteBufUtil.setMedium(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setMediumLE(int index, int value) {
/* 199 */     HeapByteBufUtil.setMediumLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setInt(int index, int value) {
/* 204 */     HeapByteBufUtil.setInt(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setIntLE(int index, int value) {
/* 209 */     HeapByteBufUtil.setIntLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLong(int index, long value) {
/* 214 */     HeapByteBufUtil.setLong(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _setLongLE(int index, long value) {
/* 219 */     HeapByteBufUtil.setLongLE(this.memory, idx(index), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 224 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 225 */     if (src.hasMemoryAddress()) {
/* 226 */       PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.memory, idx(index), length);
/* 227 */     } else if (src.hasArray()) {
/* 228 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/*     */     } else {
/* 230 */       src.getBytes(srcIndex, this.memory, idx(index), length);
/*     */     } 
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 237 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 238 */     System.arraycopy(src, srcIndex, this.memory, idx(index), length);
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf setBytes(int index, ByteBuffer src) {
/* 244 */     int length = src.remaining();
/* 245 */     checkIndex(index, length);
/* 246 */     src.get(this.memory, idx(index), length);
/* 247 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int setBytes(int index, InputStream in, int length) throws IOException {
/* 252 */     checkIndex(index, length);
/* 253 */     return in.read(this.memory, idx(index), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 258 */     checkIndex(index, length);
/* 259 */     index = idx(index);
/*     */     try {
/* 261 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length));
/* 262 */     } catch (ClosedChannelException ignored) {
/* 263 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 269 */     checkIndex(index, length);
/* 270 */     index = idx(index);
/*     */     try {
/* 272 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length), position);
/* 273 */     } catch (ClosedChannelException ignored) {
/* 274 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf copy(int index, int length) {
/* 280 */     checkIndex(index, length);
/* 281 */     ByteBuf copy = alloc().heapBuffer(length, maxCapacity());
/* 282 */     copy.writeBytes(this.memory, idx(index), length);
/* 283 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int nioBufferCount() {
/* 288 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuffer[] nioBuffers(int index, int length) {
/* 293 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuffer nioBuffer(int index, int length) {
/* 298 */     checkIndex(index, length);
/* 299 */     index = idx(index);
/* 300 */     ByteBuffer buf = ByteBuffer.wrap(this.memory, index, length);
/* 301 */     return buf.slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuffer internalNioBuffer(int index, int length) {
/* 306 */     checkIndex(index, length);
/* 307 */     index = idx(index);
/* 308 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasArray() {
/* 313 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte[] array() {
/* 318 */     ensureAccessible();
/* 319 */     return this.memory;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int arrayOffset() {
/* 324 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasMemoryAddress() {
/* 329 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long memoryAddress() {
/* 334 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final ByteBuffer newInternalNioBuffer(byte[] memory) {
/* 339 */     return ByteBuffer.wrap(memory);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */