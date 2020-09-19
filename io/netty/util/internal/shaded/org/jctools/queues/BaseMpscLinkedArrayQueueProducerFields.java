/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*    */ import java.lang.reflect.Field;
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
/*    */ abstract class BaseMpscLinkedArrayQueueProducerFields<E>
/*    */   extends BaseMpscLinkedArrayQueuePad1<E>
/*    */ {
/*    */   private static final long P_INDEX_OFFSET;
/*    */   protected long producerIndex;
/*    */   
/*    */   static {
/*    */     try {
/* 47 */       Field iField = BaseMpscLinkedArrayQueueProducerFields.class.getDeclaredField("producerIndex");
/* 48 */       P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
/*    */     }
/* 50 */     catch (NoSuchFieldException e) {
/*    */       
/* 52 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long lvProducerIndex() {
/* 61 */     return UnsafeAccess.UNSAFE.getLongVolatile(this, P_INDEX_OFFSET);
/*    */   }
/*    */ 
/*    */   
/*    */   final void soProducerIndex(long newValue) {
/* 66 */     UnsafeAccess.UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, newValue);
/*    */   }
/*    */ 
/*    */   
/*    */   final boolean casProducerIndex(long expect, long newValue) {
/* 71 */     return UnsafeAccess.UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\BaseMpscLinkedArrayQueueProducerFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */