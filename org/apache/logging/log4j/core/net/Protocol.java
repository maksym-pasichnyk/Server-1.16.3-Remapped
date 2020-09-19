/*    */ package org.apache.logging.log4j.core.net;
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
/*    */ public enum Protocol
/*    */ {
/* 24 */   TCP,
/*    */   
/* 26 */   SSL,
/*    */   
/* 28 */   UDP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEqual(String name) {
/* 36 */     return name().equalsIgnoreCase(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */