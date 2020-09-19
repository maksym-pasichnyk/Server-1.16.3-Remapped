/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ public class DefaultAsyncQueueFullPolicy
/*    */   implements AsyncQueueFullPolicy
/*    */ {
/*    */   public EventRoute getRoute(long backgroundThreadId, Level level) {
/* 33 */     return EventRoute.SYNCHRONOUS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\DefaultAsyncQueueFullPolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */