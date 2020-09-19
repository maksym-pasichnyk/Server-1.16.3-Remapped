/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.LifecycleAware;
/*    */ import com.lmax.disruptor.Sequence;
/*    */ import com.lmax.disruptor.SequenceReportingEventHandler;
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
/*    */ public class RingBufferLogEventHandler
/*    */   implements SequenceReportingEventHandler<RingBufferLogEvent>, LifecycleAware
/*    */ {
/*    */   private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*    */   private Sequence sequenceCallback;
/*    */   private int counter;
/* 35 */   private long threadId = -1L;
/*    */ 
/*    */   
/*    */   public void setSequenceCallback(Sequence sequenceCallback) {
/* 39 */     this.sequenceCallback = sequenceCallback;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEvent(RingBufferLogEvent event, long sequence, boolean endOfBatch) throws Exception {
/* 45 */     event.execute(endOfBatch);
/* 46 */     event.clear();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     if (++this.counter > 50) {
/* 52 */       this.sequenceCallback.set(sequence);
/* 53 */       this.counter = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getThreadId() {
/* 63 */     return this.threadId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onStart() {
/* 68 */     this.threadId = Thread.currentThread().getId();
/*    */   }
/*    */   
/*    */   public void onShutdown() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */