/*    */ package org.apache.logging.log4j.util;
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
/*    */ public final class Constants
/*    */ {
/* 30 */   public static final boolean IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", isClassAvailable("javax.servlet.Servlet"));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static final boolean ENABLE_THREADLOCALS = (!IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true));
/*    */ 
/*    */   
/* 43 */   public static final int JAVA_MAJOR_VERSION = getMajorVersion();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isClassAvailable(String className) {
/*    */     try {
/* 53 */       return (LoaderUtil.loadClass(className) != null);
/* 54 */     } catch (Throwable e) {
/* 55 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int getMajorVersion() {
/* 66 */     String version = System.getProperty("java.version");
/* 67 */     String[] parts = version.split("-|\\.");
/*    */     
/*    */     try {
/* 70 */       int token = Integer.parseInt(parts[0]);
/* 71 */       boolean isJEP223 = (token != 1);
/* 72 */       if (isJEP223) {
/* 73 */         return token;
/*    */       }
/* 75 */       return Integer.parseInt(parts[1]);
/* 76 */     } catch (Exception ex) {
/* 77 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */