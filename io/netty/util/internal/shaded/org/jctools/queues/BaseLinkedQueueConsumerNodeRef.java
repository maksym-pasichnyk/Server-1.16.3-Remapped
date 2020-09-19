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
/*     */ abstract class BaseLinkedQueueConsumerNodeRef<E>
/*     */   extends BaseLinkedQueuePad1<E>
/*     */ {
/*     */   protected static final long C_NODE_OFFSET;
/*     */   protected LinkedQueueNode<E> consumerNode;
/*     */   
/*     */   static {
/*     */     try {
/*  86 */       Field cNodeField = BaseLinkedQueueConsumerNodeRef.class.getDeclaredField("consumerNode");
/*  87 */       C_NODE_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(cNodeField);
/*     */     }
/*  89 */     catch (NoSuchFieldException e) {
/*     */       
/*  91 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void spConsumerNode(LinkedQueueNode<E> newValue) {
/*  99 */     this.consumerNode = newValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LinkedQueueNode<E> lvConsumerNode() {
/* 105 */     return (LinkedQueueNode<E>)UnsafeAccess.UNSAFE.getObjectVolatile(this, C_NODE_OFFSET);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final LinkedQueueNode<E> lpConsumerNode() {
/* 110 */     return this.consumerNode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\BaseLinkedQueueConsumerNodeRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */