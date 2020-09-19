/*    */ package io.netty.buffer;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.nio.ByteBuffer;
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
/*    */ class UnpooledUnsafeNoCleanerDirectByteBuf
/*    */   extends UnpooledUnsafeDirectByteBuf
/*    */ {
/*    */   UnpooledUnsafeNoCleanerDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
/* 25 */     super(alloc, initialCapacity, maxCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ByteBuffer allocateDirect(int initialCapacity) {
/* 30 */     return PlatformDependent.allocateDirectNoCleaner(initialCapacity);
/*    */   }
/*    */   
/*    */   ByteBuffer reallocateDirect(ByteBuffer oldBuffer, int initialCapacity) {
/* 34 */     return PlatformDependent.reallocateDirectNoCleaner(oldBuffer, initialCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void freeDirect(ByteBuffer buffer) {
/* 39 */     PlatformDependent.freeDirectNoCleaner(buffer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuf capacity(int newCapacity) {
/* 44 */     checkNewCapacity(newCapacity);
/*    */     
/* 46 */     int oldCapacity = capacity();
/* 47 */     if (newCapacity == oldCapacity) {
/* 48 */       return this;
/*    */     }
/*    */     
/* 51 */     ByteBuffer newBuffer = reallocateDirect(this.buffer, newCapacity);
/*    */     
/* 53 */     if (newCapacity < oldCapacity) {
/* 54 */       if (readerIndex() < newCapacity) {
/* 55 */         if (writerIndex() > newCapacity) {
/* 56 */           writerIndex(newCapacity);
/*    */         }
/*    */       } else {
/* 59 */         setIndex(newCapacity, newCapacity);
/*    */       } 
/*    */     }
/* 62 */     setByteBuffer(newBuffer, false);
/* 63 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\UnpooledUnsafeNoCleanerDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */