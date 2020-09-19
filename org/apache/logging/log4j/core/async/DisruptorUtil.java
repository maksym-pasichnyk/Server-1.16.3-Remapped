/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.BlockingWaitStrategy;
/*     */ import com.lmax.disruptor.BusySpinWaitStrategy;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.SleepingWaitStrategy;
/*     */ import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.YieldingWaitStrategy;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ final class DisruptorUtil
/*     */ {
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final int RINGBUFFER_MIN_SIZE = 128;
/*     */   
/*     */   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
/*     */   
/*     */   private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4096;
/*     */   
/*     */   static long getTimeout(String propertyName, long defaultTimeout) {
/*  47 */     return PropertiesUtil.getProperties().getLongProperty(propertyName, defaultTimeout);
/*     */   }
/*     */   
/*     */   static WaitStrategy createWaitStrategy(String propertyName) {
/*  51 */     String key = propertyName.startsWith("AsyncLogger.") ? "AsyncLogger.Timeout" : "AsyncLoggerConfig.Timeout";
/*     */ 
/*     */     
/*  54 */     long timeoutMillis = getTimeout(key, 10L);
/*  55 */     return createWaitStrategy(propertyName, timeoutMillis);
/*     */   }
/*     */   
/*     */   static WaitStrategy createWaitStrategy(String propertyName, long timeoutMillis) {
/*  59 */     String strategy = PropertiesUtil.getProperties().getStringProperty(propertyName, "TIMEOUT");
/*  60 */     LOGGER.trace("property {}={}", propertyName, strategy);
/*  61 */     String strategyUp = strategy.toUpperCase(Locale.ROOT);
/*  62 */     switch (strategyUp) {
/*     */       case "SLEEP":
/*  64 */         return (WaitStrategy)new SleepingWaitStrategy();
/*     */       case "YIELD":
/*  66 */         return (WaitStrategy)new YieldingWaitStrategy();
/*     */       case "BLOCK":
/*  68 */         return (WaitStrategy)new BlockingWaitStrategy();
/*     */       case "BUSYSPIN":
/*  70 */         return (WaitStrategy)new BusySpinWaitStrategy();
/*     */       case "TIMEOUT":
/*  72 */         return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } 
/*  74 */     return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculateRingBufferSize(String propertyName) {
/*  79 */     int ringBufferSize = Constants.ENABLE_THREADLOCALS ? 4096 : 262144;
/*  80 */     String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, String.valueOf(ringBufferSize));
/*     */     
/*     */     try {
/*  83 */       int size = Integer.parseInt(userPreferredRBSize);
/*  84 */       if (size < 128) {
/*  85 */         size = 128;
/*  86 */         LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, Integer.valueOf(128));
/*     */       } 
/*     */       
/*  89 */       ringBufferSize = size;
/*  90 */     } catch (Exception ex) {
/*  91 */       LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, Integer.valueOf(ringBufferSize));
/*     */     } 
/*  93 */     return Integers.ceilingNextPowerOfTwo(ringBufferSize);
/*     */   }
/*     */   
/*     */   static ExceptionHandler<RingBufferLogEvent> getAsyncLoggerExceptionHandler() {
/*  97 */     String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ExceptionHandler");
/*  98 */     if (cls == null) {
/*  99 */       return new AsyncLoggerDefaultExceptionHandler();
/*     */     }
/*     */     
/*     */     try {
/* 103 */       Class<? extends ExceptionHandler<RingBufferLogEvent>> klass = LoaderUtil.loadClass(cls);
/*     */       
/* 105 */       return klass.newInstance();
/* 106 */     } catch (Exception ignored) {
/* 107 */       LOGGER.debug("Invalid AsyncLogger.ExceptionHandler value: error creating {}: ", cls, ignored);
/* 108 */       return new AsyncLoggerDefaultExceptionHandler();
/*     */     } 
/*     */   }
/*     */   
/*     */   static ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> getAsyncLoggerConfigExceptionHandler() {
/* 113 */     String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLoggerConfig.ExceptionHandler");
/* 114 */     if (cls == null) {
/* 115 */       return new AsyncLoggerConfigDefaultExceptionHandler();
/*     */     }
/*     */     
/*     */     try {
/* 119 */       Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>> klass = LoaderUtil.loadClass(cls);
/*     */       
/* 121 */       return klass.newInstance();
/* 122 */     } catch (Exception ignored) {
/* 123 */       LOGGER.debug("Invalid AsyncLoggerConfig.ExceptionHandler value: error creating {}: ", cls, ignored);
/* 124 */       return new AsyncLoggerConfigDefaultExceptionHandler();
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
/*     */   public static long getExecutorThreadId(ExecutorService executor) {
/* 136 */     Future<Long> result = executor.submit(new Callable<Long>()
/*     */         {
/*     */           public Long call() {
/* 139 */             return Long.valueOf(Thread.currentThread().getId());
/*     */           }
/*     */         });
/*     */     try {
/* 143 */       return ((Long)result.get()).longValue();
/* 144 */     } catch (Exception ex) {
/* 145 */       String msg = "Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.";
/*     */       
/* 147 */       throw new IllegalStateException("Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\DisruptorUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */