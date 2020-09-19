/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "ArrayBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
/*    */ public class ArrayBlockingQueueFactory<E>
/*    */   implements BlockingQueueFactory<E>
/*    */ {
/*    */   public BlockingQueue<E> create(int capacity) {
/* 35 */     return new ArrayBlockingQueue<>(capacity);
/*    */   }
/*    */   
/*    */   @PluginFactory
/*    */   public static <E> ArrayBlockingQueueFactory<E> createFactory() {
/* 40 */     return new ArrayBlockingQueueFactory<>();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\ArrayBlockingQueueFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */