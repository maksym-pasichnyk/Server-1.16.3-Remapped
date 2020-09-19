/*    */ package org.apache.logging.log4j.core.net.mom.jms;
/*    */ 
/*    */ import org.apache.logging.log4j.core.net.server.JmsServer;
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
/*    */ public abstract class AbstractJmsReceiver
/*    */ {
/*    */   protected abstract void usage();
/*    */   
/*    */   protected void doMain(String... args) throws Exception {
/* 41 */     if (args.length != 4) {
/* 42 */       usage();
/* 43 */       System.exit(1);
/*    */     } 
/* 45 */     JmsServer server = new JmsServer(args[0], args[1], args[2], args[3]);
/* 46 */     server.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\mom\jms\AbstractJmsReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */