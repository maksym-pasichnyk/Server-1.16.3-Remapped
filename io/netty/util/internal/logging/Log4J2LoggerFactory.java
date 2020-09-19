/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import org.apache.logging.log4j.LogManager;
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
/*    */ public final class Log4J2LoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/* 22 */   public static final InternalLoggerFactory INSTANCE = new Log4J2LoggerFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InternalLogger newInstance(String name) {
/* 33 */     return new Log4J2Logger(LogManager.getLogger(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\Log4J2LoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */