/*    */ package org.apache.commons.lang3.concurrent;
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
/*    */ public class ConcurrentException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6622707671812226130L;
/*    */   
/*    */   protected ConcurrentException() {}
/*    */   
/*    */   public ConcurrentException(Throwable cause) {
/* 55 */     super(ConcurrentUtils.checkedException(cause));
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
/*    */   public ConcurrentException(String msg, Throwable cause) {
/* 67 */     super(msg, ConcurrentUtils.checkedException(cause));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\ConcurrentException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */