/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class IndexedQueueSizeUtil
/*    */ {
/*    */   public static int size(IndexedQueue iq) {
/* 29 */     long before, currentProducerIndex, after = iq.lvConsumerIndex();
/*    */ 
/*    */     
/*    */     do {
/* 33 */       before = after;
/* 34 */       currentProducerIndex = iq.lvProducerIndex();
/* 35 */       after = iq.lvConsumerIndex();
/* 36 */     } while (before != after);
/*    */     
/* 38 */     long size = currentProducerIndex - after;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     if (size > 2147483647L)
/*    */     {
/* 46 */       return Integer.MAX_VALUE;
/*    */     }
/*    */ 
/*    */     
/* 50 */     return (int)size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isEmpty(IndexedQueue iq) {
/* 60 */     return (iq.lvConsumerIndex() == iq.lvProducerIndex());
/*    */   }
/*    */   
/*    */   public static interface IndexedQueue {
/*    */     long lvConsumerIndex();
/*    */     
/*    */     long lvProducerIndex();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\IndexedQueueSizeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */