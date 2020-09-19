/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.core.ContextDataInjector;
/*    */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.LoaderUtil;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContextDataInjectorFactory
/*    */ {
/*    */   public static ContextDataInjector createInjector() {
/* 65 */     String className = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextDataInjector");
/* 66 */     if (className == null) {
/* 67 */       return createDefaultInjector();
/*    */     }
/*    */     try {
/* 70 */       Class<? extends ContextDataInjector> cls = LoaderUtil.loadClass(className).asSubclass(ContextDataInjector.class);
/*    */       
/* 72 */       return cls.newInstance();
/* 73 */     } catch (Exception dynamicFailed) {
/* 74 */       ContextDataInjector result = createDefaultInjector();
/* 75 */       StatusLogger.getLogger().warn("Could not create ContextDataInjector for '{}', using default {}: {}", className, result.getClass().getName(), dynamicFailed);
/*    */ 
/*    */       
/* 78 */       return result;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static ContextDataInjector createDefaultInjector() {
/* 83 */     ReadOnlyThreadContextMap threadContextMap = ThreadContext.getThreadContextMap();
/*    */ 
/*    */     
/* 86 */     if (threadContextMap instanceof org.apache.logging.log4j.spi.DefaultThreadContextMap || threadContextMap == null) {
/* 87 */       return new ThreadContextDataInjector.ForDefaultThreadContextMap();
/*    */     }
/* 89 */     if (threadContextMap instanceof org.apache.logging.log4j.spi.CopyOnWrite) {
/* 90 */       return new ThreadContextDataInjector.ForCopyOnWriteThreadContextMap();
/*    */     }
/* 92 */     return new ThreadContextDataInjector.ForGarbageFreeThreadContextMap();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ContextDataInjectorFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */