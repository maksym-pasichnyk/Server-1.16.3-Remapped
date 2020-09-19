/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.unix.Limits;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class NativeLongArray
/*    */ {
/*    */   private long memoryAddress;
/*    */   private int capacity;
/*    */   private int size;
/*    */   
/*    */   NativeLongArray(int capacity) {
/* 28 */     if (capacity < 1) {
/* 29 */       throw new IllegalArgumentException("capacity must be >= 1 but was " + capacity);
/*    */     }
/* 31 */     this.memoryAddress = PlatformDependent.allocateMemory((capacity * Limits.SIZEOF_JLONG));
/* 32 */     this.capacity = capacity;
/*    */   }
/*    */   
/*    */   void add(long value) {
/* 36 */     checkSize();
/* 37 */     PlatformDependent.putLong(memoryOffset(this.size++), value);
/*    */   }
/*    */   
/*    */   void clear() {
/* 41 */     this.size = 0;
/*    */   }
/*    */   
/*    */   boolean isEmpty() {
/* 45 */     return (this.size == 0);
/*    */   }
/*    */   
/*    */   void free() {
/* 49 */     PlatformDependent.freeMemory(this.memoryAddress);
/* 50 */     this.memoryAddress = 0L;
/*    */   }
/*    */   
/*    */   long memoryAddress() {
/* 54 */     return this.memoryAddress;
/*    */   }
/*    */   
/*    */   long memoryAddressEnd() {
/* 58 */     return memoryOffset(this.size);
/*    */   }
/*    */   
/*    */   private long memoryOffset(int index) {
/* 62 */     return this.memoryAddress + (index * Limits.SIZEOF_JLONG);
/*    */   }
/*    */   
/*    */   private void checkSize() {
/* 66 */     if (this.size == this.capacity) {
/* 67 */       realloc();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void realloc() {
/* 73 */     int newLength = (this.capacity <= 65536) ? (this.capacity << 1) : (this.capacity + this.capacity >> 1);
/* 74 */     long newMemoryAddress = PlatformDependent.reallocateMemory(this.memoryAddress, (newLength * Limits.SIZEOF_JLONG));
/* 75 */     if (newMemoryAddress == 0L) {
/* 76 */       throw new OutOfMemoryError("unable to allocate " + newLength + " new bytes! Existing capacity is: " + this.capacity);
/*    */     }
/*    */     
/* 79 */     this.memoryAddress = newMemoryAddress;
/* 80 */     this.capacity = newLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return "memoryAddress: " + this.memoryAddress + " capacity: " + this.capacity + " size: " + this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\NativeLongArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */