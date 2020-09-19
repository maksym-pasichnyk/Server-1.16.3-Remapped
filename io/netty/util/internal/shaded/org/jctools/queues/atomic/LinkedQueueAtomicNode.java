/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class LinkedQueueAtomicNode<E>
/*    */   extends AtomicReference<LinkedQueueAtomicNode<E>>
/*    */ {
/*    */   private static final long serialVersionUID = 2404266111789071508L;
/*    */   private E value;
/*    */   
/*    */   LinkedQueueAtomicNode() {}
/*    */   
/*    */   LinkedQueueAtomicNode(E val) {
/* 30 */     spValue(val);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public E getAndNullValue() {
/* 40 */     E temp = lpValue();
/* 41 */     spValue((E)null);
/* 42 */     return temp;
/*    */   }
/*    */ 
/*    */   
/*    */   public E lpValue() {
/* 47 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void spValue(E newValue) {
/* 52 */     this.value = newValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void soNext(LinkedQueueAtomicNode<E> n) {
/* 57 */     lazySet(n);
/*    */   }
/*    */ 
/*    */   
/*    */   public LinkedQueueAtomicNode<E> lvNext() {
/* 62 */     return get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\LinkedQueueAtomicNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */