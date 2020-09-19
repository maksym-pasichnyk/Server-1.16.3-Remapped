/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedLoggerWrapper
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ExtendedLogger logger;
/*     */   
/*     */   public ExtendedLoggerWrapper(ExtendedLogger logger, String name, MessageFactory messageFactory) {
/*  44 */     super(name, messageFactory);
/*  45 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  50 */     return this.logger.getLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/*  64 */     return this.logger.isEnabled(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/*  78 */     return this.logger.isEnabled(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/*  92 */     return this.logger.isEnabled(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 105 */     return this.logger.isEnabled(level, marker, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 119 */     return this.logger.isEnabled(level, marker, message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 124 */     return this.logger.isEnabled(level, marker, message, p0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 130 */     return this.logger.isEnabled(level, marker, message, p0, p1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 136 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 142 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 149 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 156 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 163 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 171 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 179 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 187 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 201 */     return this.logger.isEnabled(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
/* 217 */     this.logger.logMessage(fqcn, level, marker, message, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\ExtendedLoggerWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */