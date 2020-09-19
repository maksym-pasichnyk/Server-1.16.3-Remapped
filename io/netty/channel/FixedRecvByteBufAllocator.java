/*    */ package io.netty.channel;
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
/*    */ 
/*    */ public class FixedRecvByteBufAllocator
/*    */   extends DefaultMaxMessagesRecvByteBufAllocator
/*    */ {
/*    */   private final int bufferSize;
/*    */   
/*    */   private final class HandleImpl
/*    */     extends DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle
/*    */   {
/*    */     private final int bufferSize;
/*    */     
/*    */     public HandleImpl(int bufferSize) {
/* 30 */       this.bufferSize = bufferSize;
/*    */     }
/*    */ 
/*    */     
/*    */     public int guess() {
/* 35 */       return this.bufferSize;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedRecvByteBufAllocator(int bufferSize) {
/* 44 */     if (bufferSize <= 0) {
/* 45 */       throw new IllegalArgumentException("bufferSize must greater than 0: " + bufferSize);
/*    */     }
/*    */     
/* 48 */     this.bufferSize = bufferSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RecvByteBufAllocator.Handle newHandle() {
/* 54 */     return new HandleImpl(this.bufferSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public FixedRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
/* 59 */     super.respectMaybeMoreData(respectMaybeMoreData);
/* 60 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\FixedRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */