/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class Slf4JLoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/* 29 */   public static final InternalLoggerFactory INSTANCE = new Slf4JLoggerFactory();
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public Slf4JLoggerFactory() {}
/*    */ 
/*    */ 
/*    */   
/*    */   Slf4JLoggerFactory(boolean failIfNOP) {
/* 39 */     assert failIfNOP;
/* 40 */     if (LoggerFactory.getILoggerFactory() instanceof org.slf4j.helpers.NOPLoggerFactory) {
/* 41 */       throw new NoClassDefFoundError("NOPLoggerFactory not supported");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public InternalLogger newInstance(String name) {
/* 47 */     return new Slf4JLogger(LoggerFactory.getLogger(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\Slf4JLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */