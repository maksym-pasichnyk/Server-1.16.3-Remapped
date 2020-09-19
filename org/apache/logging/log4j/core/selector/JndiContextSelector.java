/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiContextSelector
/*     */   implements NamedContextSelector
/*     */ {
/*  88 */   private static final LoggerContext CONTEXT = new LoggerContext("Default");
/*     */   
/*  90 */   private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*  93 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  97 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 104 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 105 */     if (lc != null) {
/* 106 */       return lc;
/*     */     }
/*     */     
/* 109 */     String loggingContextName = null;
/*     */     
/* 111 */     JndiManager jndiManager = JndiManager.getDefaultManager();
/*     */     try {
/* 113 */       loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
/* 114 */     } catch (NamingException ne) {
/* 115 */       LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
/*     */     } finally {
/* 117 */       jndiManager.close();
/*     */     } 
/*     */     
/* 120 */     return (loggingContextName == null) ? CONTEXT : locateContext(loggingContextName, null, configLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext locateContext(String name, Object externalContext, URI configLocation) {
/* 125 */     if (name == null) {
/* 126 */       LOGGER.error("A context name is required to locate a LoggerContext");
/* 127 */       return null;
/*     */     } 
/* 129 */     if (!CONTEXT_MAP.containsKey(name)) {
/* 130 */       LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
/* 131 */       CONTEXT_MAP.putIfAbsent(name, ctx);
/*     */     } 
/* 133 */     return CONTEXT_MAP.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 139 */     for (Map.Entry<String, LoggerContext> entry : CONTEXT_MAP.entrySet()) {
/* 140 */       if (((LoggerContext)entry.getValue()).equals(context)) {
/* 141 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext removeContext(String name) {
/* 148 */     return CONTEXT_MAP.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/* 153 */     List<LoggerContext> list = new ArrayList<>(CONTEXT_MAP.values());
/* 154 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\selector\JndiContextSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */