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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Closer
/*    */ {
/*    */   public static void close(AutoCloseable closeable) throws Exception {
/* 36 */     if (closeable != null) {
/* 37 */       closeable.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean closeSilently(AutoCloseable closeable) {
/*    */     try {
/* 49 */       close(closeable);
/* 50 */       return true;
/* 51 */     } catch (Exception ignored) {
/* 52 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Closer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */