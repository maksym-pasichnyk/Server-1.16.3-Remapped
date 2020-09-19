/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import java.util.logging.Logger;
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
/*    */ public class JdkLoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/* 28 */   public static final InternalLoggerFactory INSTANCE = new JdkLoggerFactory();
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
/* 39 */     return new JdkLogger(Logger.getLogger(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\JdkLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */