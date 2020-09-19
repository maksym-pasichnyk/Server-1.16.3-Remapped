/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractPooledDerivedByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final Recycler.Handle<AbstractPooledDerivedByteBuf> recyclerHandle;
/*     */   private AbstractByteBuf rootParent;
/*     */   private ByteBuf parent;
/*     */   
/*     */   AbstractPooledDerivedByteBuf(Recycler.Handle<? extends AbstractPooledDerivedByteBuf> recyclerHandle) {
/*  42 */     super(0);
/*  43 */     this.recyclerHandle = (Recycler.Handle)recyclerHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   final void parent(ByteBuf newParent) {
/*  48 */     assert newParent instanceof SimpleLeakAwareByteBuf;
/*  49 */     this.parent = newParent;
/*     */   }
/*     */ 
/*     */   
/*     */   public final AbstractByteBuf unwrap() {
/*  54 */     return this.rootParent;
/*     */   }
/*     */ 
/*     */   
/*     */   final <U extends AbstractPooledDerivedByteBuf> U init(AbstractByteBuf unwrapped, ByteBuf wrapped, int readerIndex, int writerIndex, int maxCapacity) {
/*  59 */     wrapped.retain();
/*  60 */     this.parent = wrapped;
/*  61 */     this.rootParent = unwrapped;
/*     */     
/*     */     try {
/*  64 */       maxCapacity(maxCapacity);
/*  65 */       setIndex0(readerIndex, writerIndex);
/*  66 */       setRefCnt(1);
/*     */ 
/*     */       
/*  69 */       AbstractPooledDerivedByteBuf abstractPooledDerivedByteBuf = this;
/*  70 */       wrapped = null;
/*  71 */       return (U)abstractPooledDerivedByteBuf;
/*     */     } finally {
/*  73 */       if (wrapped != null) {
/*  74 */         this.parent = this.rootParent = null;
/*  75 */         wrapped.release();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void deallocate() {
/*  85 */     ByteBuf parent = this.parent;
/*  86 */     this.recyclerHandle.recycle(this);
/*  87 */     parent.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBufAllocator alloc() {
/*  92 */     return unwrap().alloc();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final ByteOrder order() {
/*  98 */     return unwrap().order();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 103 */     return unwrap().isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDirect() {
/* 108 */     return unwrap().isDirect();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasArray() {
/* 113 */     return unwrap().hasArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 118 */     return unwrap().array();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMemoryAddress() {
/* 123 */     return unwrap().hasMemoryAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int nioBufferCount() {
/* 128 */     return unwrap().nioBufferCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuffer internalNioBuffer(int index, int length) {
/* 133 */     return nioBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf retainedSlice() {
/* 138 */     int index = readerIndex();
/* 139 */     return retainedSlice(index, writerIndex() - index);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf slice(int index, int length) {
/* 144 */     ensureAccessible();
/*     */     
/* 146 */     return new PooledNonRetainedSlicedByteBuf(this, unwrap(), index, length);
/*     */   }
/*     */   
/*     */   final ByteBuf duplicate0() {
/* 150 */     ensureAccessible();
/*     */     
/* 152 */     return new PooledNonRetainedDuplicateByteBuf(this, unwrap());
/*     */   }
/*     */   
/*     */   private static final class PooledNonRetainedDuplicateByteBuf extends UnpooledDuplicatedByteBuf {
/*     */     private final ReferenceCounted referenceCountDelegate;
/*     */     
/*     */     PooledNonRetainedDuplicateByteBuf(ReferenceCounted referenceCountDelegate, AbstractByteBuf buffer) {
/* 159 */       super(buffer);
/* 160 */       this.referenceCountDelegate = referenceCountDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     int refCnt0() {
/* 165 */       return this.referenceCountDelegate.refCnt();
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf retain0() {
/* 170 */       this.referenceCountDelegate.retain();
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf retain0(int increment) {
/* 176 */       this.referenceCountDelegate.retain(increment);
/* 177 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf touch0() {
/* 182 */       this.referenceCountDelegate.touch();
/* 183 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf touch0(Object hint) {
/* 188 */       this.referenceCountDelegate.touch(hint);
/* 189 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean release0() {
/* 194 */       return this.referenceCountDelegate.release();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean release0(int decrement) {
/* 199 */       return this.referenceCountDelegate.release(decrement);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf duplicate() {
/* 204 */       ensureAccessible();
/* 205 */       return new PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf retainedDuplicate() {
/* 210 */       return PooledDuplicatedByteBuf.newInstance(unwrap(), this, readerIndex(), writerIndex());
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf slice(int index, int length) {
/* 215 */       checkIndex(index, length);
/* 216 */       return new AbstractPooledDerivedByteBuf.PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, unwrap(), index, length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteBuf retainedSlice() {
/* 222 */       return retainedSlice(readerIndex(), capacity());
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf retainedSlice(int index, int length) {
/* 227 */       return PooledSlicedByteBuf.newInstance(unwrap(), this, index, length);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PooledNonRetainedSlicedByteBuf
/*     */     extends UnpooledSlicedByteBuf {
/*     */     private final ReferenceCounted referenceCountDelegate;
/*     */     
/*     */     PooledNonRetainedSlicedByteBuf(ReferenceCounted referenceCountDelegate, AbstractByteBuf buffer, int index, int length) {
/* 236 */       super(buffer, index, length);
/* 237 */       this.referenceCountDelegate = referenceCountDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     int refCnt0() {
/* 242 */       return this.referenceCountDelegate.refCnt();
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf retain0() {
/* 247 */       this.referenceCountDelegate.retain();
/* 248 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf retain0(int increment) {
/* 253 */       this.referenceCountDelegate.retain(increment);
/* 254 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf touch0() {
/* 259 */       this.referenceCountDelegate.touch();
/* 260 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ByteBuf touch0(Object hint) {
/* 265 */       this.referenceCountDelegate.touch(hint);
/* 266 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean release0() {
/* 271 */       return this.referenceCountDelegate.release();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean release0(int decrement) {
/* 276 */       return this.referenceCountDelegate.release(decrement);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf duplicate() {
/* 281 */       ensureAccessible();
/* 282 */       return (new AbstractPooledDerivedByteBuf.PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, unwrap()))
/* 283 */         .setIndex(idx(readerIndex()), idx(writerIndex()));
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf retainedDuplicate() {
/* 288 */       return PooledDuplicatedByteBuf.newInstance(unwrap(), this, idx(readerIndex()), idx(writerIndex()));
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf slice(int index, int length) {
/* 293 */       checkIndex(index, length);
/* 294 */       return new PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, unwrap(), idx(index), length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteBuf retainedSlice() {
/* 300 */       return retainedSlice(0, capacity());
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf retainedSlice(int index, int length) {
/* 305 */       return PooledSlicedByteBuf.newInstance(unwrap(), this, idx(index), length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\AbstractPooledDerivedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */