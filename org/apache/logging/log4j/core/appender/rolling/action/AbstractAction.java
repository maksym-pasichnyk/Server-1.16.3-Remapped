/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public abstract class AbstractAction
/*    */   implements Action
/*    */ {
/* 33 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean complete = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean interrupted = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean execute() throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void run() {
/* 64 */     if (!this.interrupted) {
/*    */       try {
/* 66 */         execute();
/* 67 */       } catch (IOException ex) {
/* 68 */         reportException(ex);
/*    */       } 
/*    */       
/* 71 */       this.complete = true;
/* 72 */       this.interrupted = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void close() {
/* 81 */     this.interrupted = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isComplete() {
/* 91 */     return this.complete;
/*    */   }
/*    */   
/*    */   public boolean isInterrupted() {
/* 95 */     return this.interrupted;
/*    */   }
/*    */   
/*    */   protected void reportException(Exception ex) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\AbstractAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */