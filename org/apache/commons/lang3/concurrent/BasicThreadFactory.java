/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public class BasicThreadFactory
/*     */   implements ThreadFactory
/*     */ {
/*     */   private final AtomicLong threadCounter;
/*     */   private final ThreadFactory wrappedFactory;
/*     */   private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*     */   private final String namingPattern;
/*     */   private final Integer priority;
/*     */   private final Boolean daemonFlag;
/*     */   
/*     */   private BasicThreadFactory(Builder builder) {
/* 115 */     if (builder.wrappedFactory == null) {
/* 116 */       this.wrappedFactory = Executors.defaultThreadFactory();
/*     */     } else {
/* 118 */       this.wrappedFactory = builder.wrappedFactory;
/*     */     } 
/*     */     
/* 121 */     this.namingPattern = builder.namingPattern;
/* 122 */     this.priority = builder.priority;
/* 123 */     this.daemonFlag = builder.daemonFlag;
/* 124 */     this.uncaughtExceptionHandler = builder.exceptionHandler;
/*     */     
/* 126 */     this.threadCounter = new AtomicLong();
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
/*     */   public final ThreadFactory getWrappedFactory() {
/* 138 */     return this.wrappedFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getNamingPattern() {
/* 148 */     return this.namingPattern;
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
/*     */   public final Boolean getDaemonFlag() {
/* 160 */     return this.daemonFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Integer getPriority() {
/* 170 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
/* 180 */     return this.uncaughtExceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThreadCount() {
/* 191 */     return this.threadCounter.get();
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
/*     */   
/*     */   public Thread newThread(Runnable r) {
/* 204 */     Thread t = getWrappedFactory().newThread(r);
/* 205 */     initializeThread(t);
/*     */     
/* 207 */     return t;
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
/*     */   
/*     */   private void initializeThread(Thread t) {
/* 220 */     if (getNamingPattern() != null) {
/* 221 */       Long count = Long.valueOf(this.threadCounter.incrementAndGet());
/* 222 */       t.setName(String.format(getNamingPattern(), new Object[] { count }));
/*     */     } 
/*     */     
/* 225 */     if (getUncaughtExceptionHandler() != null) {
/* 226 */       t.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
/*     */     }
/*     */     
/* 229 */     if (getPriority() != null) {
/* 230 */       t.setPriority(getPriority().intValue());
/*     */     }
/*     */     
/* 233 */     if (getDaemonFlag() != null) {
/* 234 */       t.setDaemon(getDaemonFlag().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.commons.lang3.builder.Builder<BasicThreadFactory>
/*     */   {
/*     */     private ThreadFactory wrappedFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Thread.UncaughtExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String namingPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Integer priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Boolean daemonFlag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder wrappedFactory(ThreadFactory factory) {
/* 281 */       if (factory == null) {
/* 282 */         throw new NullPointerException("Wrapped ThreadFactory must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 286 */       this.wrappedFactory = factory;
/* 287 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder namingPattern(String pattern) {
/* 299 */       if (pattern == null) {
/* 300 */         throw new NullPointerException("Naming pattern must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 304 */       this.namingPattern = pattern;
/* 305 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder daemon(boolean f) {
/* 317 */       this.daemonFlag = Boolean.valueOf(f);
/* 318 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder priority(int prio) {
/* 329 */       this.priority = Integer.valueOf(prio);
/* 330 */       return this;
/*     */     }
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
/*     */     public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
/* 344 */       if (handler == null) {
/* 345 */         throw new NullPointerException("Uncaught exception handler must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 349 */       this.exceptionHandler = handler;
/* 350 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reset() {
/* 360 */       this.wrappedFactory = null;
/* 361 */       this.exceptionHandler = null;
/* 362 */       this.namingPattern = null;
/* 363 */       this.priority = null;
/* 364 */       this.daemonFlag = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BasicThreadFactory build() {
/* 376 */       BasicThreadFactory factory = new BasicThreadFactory(this);
/* 377 */       reset();
/* 378 */       return factory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\BasicThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */