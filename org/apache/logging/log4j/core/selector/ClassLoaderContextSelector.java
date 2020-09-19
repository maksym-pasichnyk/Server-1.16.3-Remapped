/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReflectionUtil;
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
/*     */ public class ClassLoaderContextSelector
/*     */   implements ContextSelector
/*     */ {
/*  48 */   private static final AtomicReference<LoggerContext> DEFAULT_CONTEXT = new AtomicReference<>();
/*     */   
/*  50 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*  52 */   protected static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  57 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/*  63 */     if (currentContext) {
/*  64 */       LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  65 */       if (ctx != null) {
/*  66 */         return ctx;
/*     */       }
/*  68 */       return getDefault();
/*  69 */     }  if (loader != null) {
/*  70 */       return locateContext(loader, configLocation);
/*     */     }
/*  72 */     Class<?> clazz = ReflectionUtil.getCallerClass(fqcn);
/*  73 */     if (clazz != null) {
/*  74 */       return locateContext(clazz.getClassLoader(), configLocation);
/*     */     }
/*  76 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/*  77 */     if (lc != null) {
/*  78 */       return lc;
/*     */     }
/*  80 */     return getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/*  86 */     for (Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : CONTEXT_MAP.entrySet()) {
/*  87 */       LoggerContext ctx = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)entry.getValue()).get()).get();
/*  88 */       if (ctx == context) {
/*  89 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/*  96 */     List<LoggerContext> list = new ArrayList<>();
/*  97 */     Collection<AtomicReference<WeakReference<LoggerContext>>> coll = CONTEXT_MAP.values();
/*  98 */     for (AtomicReference<WeakReference<LoggerContext>> ref : coll) {
/*  99 */       LoggerContext ctx = ((WeakReference<LoggerContext>)ref.get()).get();
/* 100 */       if (ctx != null) {
/* 101 */         list.add(ctx);
/*     */       }
/*     */     } 
/* 104 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */   
/*     */   private LoggerContext locateContext(ClassLoader loaderOrNull, URI configLocation) {
/* 109 */     ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
/* 110 */     String name = toContextMapKey(loader);
/* 111 */     AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
/* 112 */     if (ref == null) {
/* 113 */       if (configLocation == null) {
/* 114 */         ClassLoader parent = loader.getParent();
/* 115 */         while (parent != null) {
/*     */           
/* 117 */           ref = CONTEXT_MAP.get(toContextMapKey(parent));
/* 118 */           if (ref != null) {
/* 119 */             WeakReference<LoggerContext> weakReference = ref.get();
/* 120 */             LoggerContext loggerContext1 = weakReference.get();
/* 121 */             if (loggerContext1 != null) {
/* 122 */               return loggerContext1;
/*     */             }
/*     */           } 
/* 125 */           parent = parent.getParent();
/*     */         } 
/*     */       } 
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
/* 145 */       LoggerContext loggerContext = createContext(name, configLocation);
/* 146 */       AtomicReference<WeakReference<LoggerContext>> r = new AtomicReference<>();
/* 147 */       r.set(new WeakReference<>(loggerContext));
/* 148 */       CONTEXT_MAP.putIfAbsent(name, r);
/* 149 */       loggerContext = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name)).get()).get();
/* 150 */       return loggerContext;
/*     */     } 
/* 152 */     WeakReference<LoggerContext> weakRef = ref.get();
/* 153 */     LoggerContext ctx = weakRef.get();
/* 154 */     if (ctx != null) {
/* 155 */       if (ctx.getConfigLocation() == null && configLocation != null) {
/* 156 */         LOGGER.debug("Setting configuration to {}", configLocation);
/* 157 */         ctx.setConfigLocation(configLocation);
/* 158 */       } else if (ctx.getConfigLocation() != null && configLocation != null && !ctx.getConfigLocation().equals(configLocation)) {
/*     */         
/* 160 */         LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx.getConfigLocation());
/*     */       } 
/*     */       
/* 163 */       return ctx;
/*     */     } 
/* 165 */     ctx = createContext(name, configLocation);
/* 166 */     ref.compareAndSet(weakRef, new WeakReference<>(ctx));
/* 167 */     return ctx;
/*     */   }
/*     */   
/*     */   protected LoggerContext createContext(String name, URI configLocation) {
/* 171 */     return new LoggerContext(name, null, configLocation);
/*     */   }
/*     */   
/*     */   protected String toContextMapKey(ClassLoader loader) {
/* 175 */     return Integer.toHexString(System.identityHashCode(loader));
/*     */   }
/*     */   
/*     */   protected LoggerContext getDefault() {
/* 179 */     LoggerContext ctx = DEFAULT_CONTEXT.get();
/* 180 */     if (ctx != null) {
/* 181 */       return ctx;
/*     */     }
/* 183 */     DEFAULT_CONTEXT.compareAndSet(null, createContext(defaultContextName(), null));
/* 184 */     return DEFAULT_CONTEXT.get();
/*     */   }
/*     */   
/*     */   protected String defaultContextName() {
/* 188 */     return "Default";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\selector\ClassLoaderContextSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */