/*    */ package org.apache.logging.log4j.core.lookup;
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
/*    */ public abstract class AbstractLookup
/*    */   implements StrLookup
/*    */ {
/*    */   public String lookup(String key) {
/* 33 */     return lookup(null, key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\AbstractLookup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */