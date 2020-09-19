/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public final class ThreadFactoryBuilder
/*     */ {
/*  47 */   private String nameFormat = null;
/*  48 */   private Boolean daemon = null;
/*  49 */   private Integer priority = null;
/*  50 */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
/*  51 */   private ThreadFactory backingThreadFactory = null;
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
/*     */   public ThreadFactoryBuilder setNameFormat(String nameFormat) {
/*  70 */     String unused = format(nameFormat, new Object[] { Integer.valueOf(0) });
/*  71 */     this.nameFormat = nameFormat;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadFactoryBuilder setDaemon(boolean daemon) {
/*  82 */     this.daemon = Boolean.valueOf(daemon);
/*  83 */     return this;
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
/*     */   public ThreadFactoryBuilder setPriority(int priority) {
/*  95 */     Preconditions.checkArgument((priority >= 1), "Thread priority (%s) must be >= %s", priority, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     Preconditions.checkArgument((priority <= 10), "Thread priority (%s) must be <= %s", priority, 10);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.priority = Integer.valueOf(priority);
/* 106 */     return this;
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
/*     */   public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/* 118 */     this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)Preconditions.checkNotNull(uncaughtExceptionHandler);
/* 119 */     return this;
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
/*     */   
/*     */   public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
/* 133 */     this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(backingThreadFactory);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public ThreadFactory build() {
/* 146 */     return build(this);
/*     */   }
/*     */   
/*     */   private static ThreadFactory build(ThreadFactoryBuilder builder) {
/* 150 */     final String nameFormat = builder.nameFormat;
/* 151 */     final Boolean daemon = builder.daemon;
/* 152 */     final Integer priority = builder.priority;
/* 153 */     final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
/*     */ 
/*     */ 
/*     */     
/* 157 */     final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
/* 158 */     final AtomicLong count = (nameFormat != null) ? new AtomicLong(0L) : null;
/* 159 */     return new ThreadFactory()
/*     */       {
/*     */         public Thread newThread(Runnable runnable) {
/* 162 */           Thread thread = backingThreadFactory.newThread(runnable);
/* 163 */           if (nameFormat != null) {
/* 164 */             thread.setName(ThreadFactoryBuilder.format(nameFormat, new Object[] { Long.valueOf(this.val$count.getAndIncrement()) }));
/*     */           }
/* 166 */           if (daemon != null) {
/* 167 */             thread.setDaemon(daemon.booleanValue());
/*     */           }
/* 169 */           if (priority != null) {
/* 170 */             thread.setPriority(priority.intValue());
/*     */           }
/* 172 */           if (uncaughtExceptionHandler != null) {
/* 173 */             thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
/*     */           }
/* 175 */           return thread;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 181 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ThreadFactoryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */