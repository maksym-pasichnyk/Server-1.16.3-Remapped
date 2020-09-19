/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ final class KQueueEventArray
/*     */ {
/*  33 */   private static final int KQUEUE_EVENT_SIZE = Native.sizeofKEvent();
/*  34 */   private static final int KQUEUE_IDENT_OFFSET = Native.offsetofKEventIdent();
/*  35 */   private static final int KQUEUE_FILTER_OFFSET = Native.offsetofKEventFilter();
/*  36 */   private static final int KQUEUE_FFLAGS_OFFSET = Native.offsetofKEventFFlags();
/*  37 */   private static final int KQUEUE_FLAGS_OFFSET = Native.offsetofKEventFlags();
/*  38 */   private static final int KQUEUE_DATA_OFFSET = Native.offsetofKeventData();
/*     */   
/*     */   private long memoryAddress;
/*     */   private int size;
/*     */   private int capacity;
/*     */   
/*     */   KQueueEventArray(int capacity) {
/*  45 */     if (capacity < 1) {
/*  46 */       throw new IllegalArgumentException("capacity must be >= 1 but was " + capacity);
/*     */     }
/*  48 */     this.memoryAddress = PlatformDependent.allocateMemory((capacity * KQUEUE_EVENT_SIZE));
/*  49 */     this.capacity = capacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long memoryAddress() {
/*  56 */     return this.memoryAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int capacity() {
/*  64 */     return this.capacity;
/*     */   }
/*     */   
/*     */   int size() {
/*  68 */     return this.size;
/*     */   }
/*     */   
/*     */   void clear() {
/*  72 */     this.size = 0;
/*     */   }
/*     */   
/*     */   void evSet(AbstractKQueueChannel ch, short filter, short flags, int fflags) {
/*  76 */     checkSize();
/*  77 */     evSet(getKEventOffset(this.size++), ch, ch.socket.intValue(), filter, flags, fflags);
/*     */   }
/*     */   
/*     */   private void checkSize() {
/*  81 */     if (this.size == this.capacity) {
/*  82 */       realloc(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void realloc(boolean throwIfFail) {
/*  91 */     int newLength = (this.capacity <= 65536) ? (this.capacity << 1) : (this.capacity + this.capacity >> 1);
/*  92 */     long newMemoryAddress = PlatformDependent.reallocateMemory(this.memoryAddress, (newLength * KQUEUE_EVENT_SIZE));
/*  93 */     if (newMemoryAddress != 0L) {
/*  94 */       this.memoryAddress = newMemoryAddress;
/*  95 */       this.capacity = newLength;
/*     */       return;
/*     */     } 
/*  98 */     if (throwIfFail) {
/*  99 */       throw new OutOfMemoryError("unable to allocate " + newLength + " new bytes! Existing capacity is: " + this.capacity);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void free() {
/* 108 */     PlatformDependent.freeMemory(this.memoryAddress);
/* 109 */     this.memoryAddress = (this.size = this.capacity = 0);
/*     */   }
/*     */   
/*     */   long getKEventOffset(int index) {
/* 113 */     return this.memoryAddress + (index * KQUEUE_EVENT_SIZE);
/*     */   }
/*     */   
/*     */   short flags(int index) {
/* 117 */     return PlatformDependent.getShort(getKEventOffset(index) + KQUEUE_FLAGS_OFFSET);
/*     */   }
/*     */   
/*     */   short filter(int index) {
/* 121 */     return PlatformDependent.getShort(getKEventOffset(index) + KQUEUE_FILTER_OFFSET);
/*     */   }
/*     */   
/*     */   short fflags(int index) {
/* 125 */     return PlatformDependent.getShort(getKEventOffset(index) + KQUEUE_FFLAGS_OFFSET);
/*     */   }
/*     */   
/*     */   int fd(int index) {
/* 129 */     return PlatformDependent.getInt(getKEventOffset(index) + KQUEUE_IDENT_OFFSET);
/*     */   }
/*     */   
/*     */   long data(int index) {
/* 133 */     return PlatformDependent.getLong(getKEventOffset(index) + KQUEUE_DATA_OFFSET);
/*     */   }
/*     */   
/*     */   AbstractKQueueChannel channel(int index) {
/* 137 */     return getChannel(getKEventOffset(index));
/*     */   }
/*     */   
/*     */   private static native void evSet(long paramLong, AbstractKQueueChannel paramAbstractKQueueChannel, int paramInt1, short paramShort1, short paramShort2, int paramInt2);
/*     */   
/*     */   private static native AbstractKQueueChannel getChannel(long paramLong);
/*     */   
/*     */   static native void deleteGlobalRefs(long paramLong1, long paramLong2);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueEventArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */