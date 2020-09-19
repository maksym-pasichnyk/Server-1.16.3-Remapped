/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
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
/*    */ public final class ContextAnchor
/*    */ {
/* 29 */   public static final ThreadLocal<LoggerContext> THREAD_CONTEXT = new ThreadLocal<>();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ContextAnchor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */