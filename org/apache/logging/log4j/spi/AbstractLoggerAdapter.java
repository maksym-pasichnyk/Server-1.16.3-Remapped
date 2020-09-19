/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ public abstract class AbstractLoggerAdapter<L>
/*     */   implements LoggerAdapter<L>
/*     */ {
/*  40 */   protected final Map<LoggerContext, ConcurrentMap<String, L>> registry = new WeakHashMap<>();
/*     */   
/*  42 */   private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
/*     */ 
/*     */   
/*     */   public L getLogger(String name) {
/*  46 */     LoggerContext context = getContext();
/*  47 */     ConcurrentMap<String, L> loggers = getLoggersInContext(context);
/*  48 */     L logger = loggers.get(name);
/*  49 */     if (logger != null) {
/*  50 */       return logger;
/*     */     }
/*  52 */     loggers.putIfAbsent(name, newLogger(name, context));
/*  53 */     return loggers.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMap<String, L> getLoggersInContext(LoggerContext context) {
/*     */     ConcurrentMap<String, L> loggers;
/*  64 */     this.lock.readLock().lock();
/*     */     try {
/*  66 */       loggers = this.registry.get(context);
/*     */     } finally {
/*  68 */       this.lock.readLock().unlock();
/*     */     } 
/*     */     
/*  71 */     if (loggers != null) {
/*  72 */       return loggers;
/*     */     }
/*  74 */     this.lock.writeLock().lock();
/*     */     try {
/*  76 */       loggers = this.registry.get(context);
/*  77 */       if (loggers == null) {
/*  78 */         loggers = new ConcurrentHashMap<>();
/*  79 */         this.registry.put(context, loggers);
/*     */       } 
/*  81 */       return loggers;
/*     */     } finally {
/*  83 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract L newLogger(String paramString, LoggerContext paramLoggerContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract LoggerContext getContext();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoggerContext getContext(Class<?> callerClass) {
/* 115 */     ClassLoader cl = null;
/* 116 */     if (callerClass != null) {
/* 117 */       cl = callerClass.getClassLoader();
/*     */     }
/* 119 */     if (cl == null) {
/* 120 */       cl = LoaderUtil.getThreadContextClassLoader();
/*     */     }
/* 122 */     return LogManager.getContext(cl, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 127 */     this.lock.writeLock().lock();
/*     */     try {
/* 129 */       this.registry.clear();
/*     */     } finally {
/* 131 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\AbstractLoggerAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */