/*    */ package org.apache.logging.log4j.spi;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class NoOpThreadContextMap
/*    */   implements ThreadContextMap
/*    */ {
/*    */   public void clear() {}
/*    */   
/*    */   public boolean containsKey(String key) {
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String get(String key) {
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, String> getCopy() {
/* 45 */     return new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, String> getImmutableMapOrNull() {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   public void put(String key, String value) {}
/*    */   
/*    */   public void remove(String key) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\NoOpThreadContextMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */