/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ abstract class PooledByteBuf<T>
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final Recycler.Handle<PooledByteBuf<T>> recyclerHandle;
/*     */   protected PoolChunk<T> chunk;
/*     */   protected long handle;
/*     */   protected T memory;
/*     */   protected int offset;
/*     */   protected int length;
/*     */   int maxLength;
/*     */   PoolThreadCache cache;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   private ByteBufAllocator allocator;
/*     */   
/*     */   protected PooledByteBuf(Recycler.Handle<? extends PooledByteBuf<T>> recyclerHandle, int maxCapacity) {
/*  41 */     super(maxCapacity);
/*  42 */     this.recyclerHandle = (Recycler.Handle)recyclerHandle;
/*     */   }
/*     */   
/*     */   void init(PoolChunk<T> chunk, long handle, int offset, int length, int maxLength, PoolThreadCache cache) {
/*  46 */     init0(chunk, handle, offset, length, maxLength, cache);
/*     */   }
/*     */   
/*     */   void initUnpooled(PoolChunk<T> chunk, int length) {
/*  50 */     init0(chunk, 0L, chunk.offset, length, length, (PoolThreadCache)null);
/*     */   }
/*     */   
/*     */   private void init0(PoolChunk<T> chunk, long handle, int offset, int length, int maxLength, PoolThreadCache cache) {
/*  54 */     assert handle >= 0L;
/*  55 */     assert chunk != null;
/*     */     
/*  57 */     this.chunk = chunk;
/*  58 */     this.memory = chunk.memory;
/*  59 */     this.allocator = chunk.arena.parent;
/*  60 */     this.cache = cache;
/*  61 */     this.handle = handle;
/*  62 */     this.offset = offset;
/*  63 */     this.length = length;
/*  64 */     this.maxLength = maxLength;
/*  65 */     this.tmpNioBuf = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void reuse(int maxCapacity) {
/*  72 */     maxCapacity(maxCapacity);
/*  73 */     setRefCnt(1);
/*  74 */     setIndex0(0, 0);
/*  75 */     discardMarks();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int capacity() {
/*  80 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf capacity(int newCapacity) {
/*  85 */     checkNewCapacity(newCapacity);
/*     */ 
/*     */     
/*  88 */     if (this.chunk.unpooled) {
/*  89 */       if (newCapacity == this.length) {
/*  90 */         return this;
/*     */       }
/*     */     }
/*  93 */     else if (newCapacity > this.length) {
/*  94 */       if (newCapacity <= this.maxLength) {
/*  95 */         this.length = newCapacity;
/*  96 */         return this;
/*     */       } 
/*  98 */     } else if (newCapacity < this.length) {
/*  99 */       if (newCapacity > this.maxLength >>> 1) {
/* 100 */         if (this.maxLength <= 512) {
/* 101 */           if (newCapacity > this.maxLength - 16) {
/* 102 */             this.length = newCapacity;
/* 103 */             setIndex(Math.min(readerIndex(), newCapacity), Math.min(writerIndex(), newCapacity));
/* 104 */             return this;
/*     */           } 
/*     */         } else {
/* 107 */           this.length = newCapacity;
/* 108 */           setIndex(Math.min(readerIndex(), newCapacity), Math.min(writerIndex(), newCapacity));
/* 109 */           return this;
/*     */         } 
/*     */       }
/*     */     } else {
/* 113 */       return this;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.chunk.arena.reallocate(this, newCapacity, true);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBufAllocator alloc() {
/* 124 */     return this.allocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteOrder order() {
/* 129 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf unwrap() {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retainedDuplicate() {
/* 139 */     return PooledDuplicatedByteBuf.newInstance(this, this, readerIndex(), writerIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retainedSlice() {
/* 144 */     int index = readerIndex();
/* 145 */     return retainedSlice(index, writerIndex() - index);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retainedSlice(int index, int length) {
/* 150 */     return PooledSlicedByteBuf.newInstance(this, this, index, length);
/*     */   }
/*     */   
/*     */   protected final ByteBuffer internalNioBuffer() {
/* 154 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 155 */     if (tmpNioBuf == null) {
/* 156 */       this.tmpNioBuf = tmpNioBuf = newInternalNioBuffer(this.memory);
/*     */     }
/* 158 */     return tmpNioBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract ByteBuffer newInternalNioBuffer(T paramT);
/*     */   
/*     */   protected final void deallocate() {
/* 165 */     if (this.handle >= 0L) {
/* 166 */       long handle = this.handle;
/* 167 */       this.handle = -1L;
/* 168 */       this.memory = null;
/* 169 */       this.tmpNioBuf = null;
/* 170 */       this.chunk.arena.free(this.chunk, handle, this.maxLength, this.cache);
/* 171 */       this.chunk = null;
/* 172 */       recycle();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recycle() {
/* 177 */     this.recyclerHandle.recycle(this);
/*     */   }
/*     */   
/*     */   protected final int idx(int index) {
/* 181 */     return this.offset + index;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */