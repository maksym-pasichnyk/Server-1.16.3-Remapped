/*    */ package org.apache.logging.log4j;
/*    */ 
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.message.StructuredDataMessage;
/*    */ import org.apache.logging.log4j.spi.ExtendedLogger;
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
/*    */ public final class EventLogger
/*    */ {
/* 30 */   public static final Marker EVENT_MARKER = MarkerManager.getMarker("EVENT");
/*    */   
/*    */   private static final String NAME = "EventLogger";
/*    */   
/* 34 */   private static final String FQCN = EventLogger.class.getName();
/*    */   
/* 36 */   private static final ExtendedLogger LOGGER = LogManager.getContext(false).getLogger("EventLogger");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void logEvent(StructuredDataMessage msg) {
/* 47 */     LOGGER.logIfEnabled(FQCN, Level.OFF, EVENT_MARKER, (Message)msg, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void logEvent(StructuredDataMessage msg, Level level) {
/* 56 */     LOGGER.logIfEnabled(FQCN, level, EVENT_MARKER, (Message)msg, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\EventLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */