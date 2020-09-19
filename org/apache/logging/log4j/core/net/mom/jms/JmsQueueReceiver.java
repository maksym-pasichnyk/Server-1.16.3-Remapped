/*    */ package org.apache.logging.log4j.core.net.mom.jms;
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
/*    */ public class JmsQueueReceiver
/*    */   extends AbstractJmsReceiver
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 36 */     JmsQueueReceiver receiver = new JmsQueueReceiver();
/* 37 */     receiver.doMain(args);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void usage() {
/* 42 */     System.err.println("Wrong number of arguments.");
/* 43 */     System.err.println("Usage: java " + JmsQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\mom\jms\JmsQueueReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */