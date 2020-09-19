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
/*    */ 
/*    */ public final class Booleans
/*    */ {
/*    */   public static boolean parseBoolean(String s, boolean defaultValue) {
/* 37 */     return ("true".equalsIgnoreCase(s) || (defaultValue && !"false".equalsIgnoreCase(s)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Booleans.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */