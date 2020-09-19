/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
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
/*    */ public final class JndiCloser
/*    */ {
/*    */   public static void close(Context context) throws NamingException {
/* 40 */     if (context != null) {
/* 41 */       context.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean closeSilently(Context context) {
/*    */     try {
/* 52 */       close(context);
/* 53 */       return true;
/* 54 */     } catch (NamingException ignored) {
/*    */       
/* 56 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\JndiCloser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */