/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.util.Supplier;
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
/*    */ public class DefaultReliabilityStrategy
/*    */   implements ReliabilityStrategy
/*    */ {
/*    */   private final LoggerConfig loggerConfig;
/*    */   
/*    */   public DefaultReliabilityStrategy(LoggerConfig loggerConfig) {
/* 36 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig is null");
/*    */   }
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
/*    */   public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/* 49 */     this.loggerConfig.log(loggerName, fqcn, marker, level, data, t);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void log(Supplier<LoggerConfig> reconfigured, LogEvent event) {
/* 60 */     this.loggerConfig.log(event);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerConfig getActiveLoggerConfig(Supplier<LoggerConfig> next) {
/* 72 */     return this.loggerConfig;
/*    */   }
/*    */   
/*    */   public void afterLogEvent() {}
/*    */   
/*    */   public void beforeStopAppenders() {}
/*    */   
/*    */   public void beforeStopConfiguration(Configuration configuration) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\DefaultReliabilityStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */