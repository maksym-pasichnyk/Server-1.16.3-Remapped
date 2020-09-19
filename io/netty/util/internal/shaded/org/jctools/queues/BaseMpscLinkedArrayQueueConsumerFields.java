/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*     */ import java.lang.reflect.Field;
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
/*     */ abstract class BaseMpscLinkedArrayQueueConsumerFields<E>
/*     */   extends BaseMpscLinkedArrayQueuePad2<E>
/*     */ {
/*     */   private static final long C_INDEX_OFFSET;
/*     */   protected long consumerMask;
/*     */   protected E[] consumerBuffer;
/*     */   protected long consumerIndex;
/*     */   
/*     */   static {
/*     */     try {
/*  90 */       Field iField = BaseMpscLinkedArrayQueueConsumerFields.class.getDeclaredField("consumerIndex");
/*  91 */       C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
/*     */     }
/*  93 */     catch (NoSuchFieldException e) {
/*     */       
/*  95 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long lvConsumerIndex() {
/* 106 */     return UnsafeAccess.UNSAFE.getLongVolatile(this, C_INDEX_OFFSET);
/*     */   }
/*     */ 
/*     */   
/*     */   final void soConsumerIndex(long newValue) {
/* 111 */     UnsafeAccess.UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, newValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\BaseMpscLinkedArrayQueueConsumerFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */