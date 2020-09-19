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
/*    */ public class AsyncLoggerDefaultExceptionHandler
/*    */   implements ExceptionHandler<RingBufferLogEvent>
/*    */ {
/*    */   public void handleEventException(Throwable throwable, long sequence, RingBufferLogEvent event) {
/* 28 */     StringBuilder sb = new StringBuilder(512);
/* 29 */     sb.append("AsyncLogger error handling event seq=").append(sequence).append(", value='");
/*    */     try {
/* 31 */       sb.append(event);
/* 32 */     } catch (Exception ignored) {
/* 33 */       sb.append("[ERROR calling ").append(event.getClass()).append(".toString(): ");
/* 34 */       sb.append(ignored).append("]");
/*    */     } 
/* 36 */     sb.append("':");
/* 37 */     System.err.println(sb);
/* 38 */     throwable.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleOnStartException(Throwable throwable) {
/* 43 */     System.err.println("AsyncLogger error starting:");
/* 44 */     throwable.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleOnShutdownException(Throwable throwable) {
/* 49 */     System.err.println("AsyncLogger error shutting down:");
/* 50 */     throwable.printStackTrace();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerDefaultExceptionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */