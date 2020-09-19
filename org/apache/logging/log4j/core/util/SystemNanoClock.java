/*    */ package org.apache.logging.log4j.core.util;
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
/*    */ public final class SystemNanoClock
/*    */   implements NanoClock
/*    */ {
/*    */   public long nanoTime() {
/* 30 */     return System.nanoTime();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\SystemNanoClock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */