/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class ReadOnlyUnsafeDirectByteBuf
/*     */   extends ReadOnlyByteBufferBuf
/*     */ {
/*     */   private final long memoryAddress;
/*     */   
/*     */   ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator allocator, ByteBuffer byteBuffer) {
/*  31 */     super(allocator, byteBuffer);
/*     */ 
/*     */     
/*  34 */     this.memoryAddress = PlatformDependent.directBufferAddress(this.buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte _getByte(int index) {
/*  39 */     return UnsafeByteBufUtil.getByte(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected short _getShort(int index) {
/*  44 */     return UnsafeByteBufUtil.getShort(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getUnsignedMedium(int index) {
/*  49 */     return UnsafeByteBufUtil.getUnsignedMedium(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int _getInt(int index) {
/*  54 */     return UnsafeByteBufUtil.getInt(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long _getLong(int index) {
/*  59 */     return UnsafeByteBufUtil.getLong(addr(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  64 */     checkIndex(index, length);
/*  65 */     if (dst == null) {
/*  66 */       throw new NullPointerException("dst");
/*     */     }
/*  68 */     if (dstIndex < 0 || dstIndex > dst.capacity() - length) {
/*  69 */       throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
/*     */     }
/*     */     
/*  72 */     if (dst.hasMemoryAddress()) {
/*  73 */       PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
/*  74 */     } else if (dst.hasArray()) {
/*  75 */       PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/*  77 */       dst.setBytes(dstIndex, this, index, length);
/*     */     } 
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  84 */     checkIndex(index, length);
/*  85 */     if (dst == null) {
/*  86 */       throw new NullPointerException("dst");
/*     */     }
/*  88 */     if (dstIndex < 0 || dstIndex > dst.length - length) {
/*  89 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] {
/*  90 */               Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length)
/*     */             }));
/*     */     }
/*  93 */     if (length != 0) {
/*  94 */       PlatformDependent.copyMemory(addr(index), dst, dstIndex, length);
/*     */     }
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst) {
/* 101 */     checkIndex(index);
/* 102 */     if (dst == null) {
/* 103 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 106 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/* 107 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 108 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 109 */     dst.put(tmpBuf);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf copy(int index, int length) {
/* 115 */     checkIndex(index, length);
/* 116 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 117 */     if (length != 0) {
/* 118 */       if (copy.hasMemoryAddress()) {
/* 119 */         PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
/* 120 */         copy.setIndex(0, length);
/*     */       } else {
/* 122 */         copy.writeBytes(this, index, length);
/*     */       } 
/*     */     }
/* 125 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long memoryAddress() {
/* 135 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */   private long addr(int index) {
/* 139 */     return this.memoryAddress + index;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\ReadOnlyUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */