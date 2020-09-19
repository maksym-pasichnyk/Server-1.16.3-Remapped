/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public final class SetUtils
/*    */ {
/*    */   public static String[] prefixSet(Set<String> set, String prefix) {
/* 37 */     Set<String> prefixSet = new HashSet<>();
/* 38 */     for (String str : set) {
/* 39 */       if (str.startsWith(prefix)) {
/* 40 */         prefixSet.add(str);
/*    */       }
/*    */     } 
/* 43 */     return prefixSet.<String>toArray(new String[prefixSet.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\SetUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */