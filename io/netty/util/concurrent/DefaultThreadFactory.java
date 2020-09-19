/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class DefaultThreadFactory
/*     */   implements ThreadFactory
/*     */ {
/*  30 */   private static final AtomicInteger poolId = new AtomicInteger();
/*     */   
/*  32 */   private final AtomicInteger nextId = new AtomicInteger();
/*     */   private final String prefix;
/*     */   private final boolean daemon;
/*     */   private final int priority;
/*     */   protected final ThreadGroup threadGroup;
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType) {
/*  39 */     this(poolType, false, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName) {
/*  43 */     this(poolName, false, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, boolean daemon) {
/*  47 */     this(poolType, daemon, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName, boolean daemon) {
/*  51 */     this(poolName, daemon, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, int priority) {
/*  55 */     this(poolType, false, priority);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName, int priority) {
/*  59 */     this(poolName, false, priority);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
/*  63 */     this(toPoolName(poolType), daemon, priority);
/*     */   }
/*     */   
/*     */   public static String toPoolName(Class<?> poolType) {
/*  67 */     if (poolType == null) {
/*  68 */       throw new NullPointerException("poolType");
/*     */     }
/*     */     
/*  71 */     String poolName = StringUtil.simpleClassName(poolType);
/*  72 */     switch (poolName.length()) {
/*     */       case 0:
/*  74 */         return "unknown";
/*     */       case 1:
/*  76 */         return poolName.toLowerCase(Locale.US);
/*     */     } 
/*  78 */     if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
/*  79 */       return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
/*     */     }
/*  81 */     return poolName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup) {
/*  87 */     if (poolName == null) {
/*  88 */       throw new NullPointerException("poolName");
/*     */     }
/*  90 */     if (priority < 1 || priority > 10) {
/*  91 */       throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
/*     */     }
/*     */ 
/*     */     
/*  95 */     this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
/*  96 */     this.daemon = daemon;
/*  97 */     this.priority = priority;
/*  98 */     this.threadGroup = threadGroup;
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
/* 102 */     this(poolName, daemon, priority, (System.getSecurityManager() == null) ? 
/* 103 */         Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup());
/*     */   }
/*     */ 
/*     */   
/*     */   public Thread newThread(Runnable r) {
/* 108 */     Thread t = newThread(FastThreadLocalRunnable.wrap(r), this.prefix + this.nextId.incrementAndGet());
/*     */     try {
/* 110 */       if (t.isDaemon() != this.daemon) {
/* 111 */         t.setDaemon(this.daemon);
/*     */       }
/*     */       
/* 114 */       if (t.getPriority() != this.priority) {
/* 115 */         t.setPriority(this.priority);
/*     */       }
/* 117 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 120 */     return t;
/*     */   }
/*     */   
/*     */   protected Thread newThread(Runnable r, String name) {
/* 124 */     return new FastThreadLocalThread(this.threadGroup, r, name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\DefaultThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */