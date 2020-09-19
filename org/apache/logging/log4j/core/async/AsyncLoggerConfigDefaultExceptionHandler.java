/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.ExceptionHandler;
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
/*    */ public class AsyncLoggerConfigDefaultExceptionHandler
/*    */   implements ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>
/*    */ {
/*    */   public void handleEventException(Throwable throwable, long sequence, AsyncLoggerConfigDisruptor.Log4jEventWrapper event) {
/* 30 */     StringBuilder sb = new StringBuilder(512);
/* 31 */     sb.append("AsyncLogger error handling event seq=").append(sequence).append(", value='");
/*    */     try {
/* 33 */       sb.append(event);
/* 34 */     } catch (Exception ignored) {
/* 35 */       sb.append("[ERROR calling ").append(event.getClass()).append(".toString(): ");
/* 36 */       sb.append(ignored).append("]");
/*    */     } 
/* 38 */     sb.append("':");
/* 39 */     System.err.println(sb);
/* 40 */     throwable.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleOnStartException(Throwable throwable) {
/* 45 */     System.err.println("AsyncLogger error starting:");
/* 46 */     throwable.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleOnShutdownException(Throwable throwable) {
/* 51 */     System.err.println("AsyncLogger error shutting down:");
/* 52 */     throwable.printStackTrace();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigDefaultExceptionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */