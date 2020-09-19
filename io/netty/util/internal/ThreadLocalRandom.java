/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ThreadLocalRandom
/*     */   extends Random
/*     */ {
/*  63 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
/*     */   
/*  65 */   private static final AtomicLong seedUniquifier = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static volatile long initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L); private static final Thread seedGeneratorThread; private static final BlockingQueue<Long> seedQueue; private static final long seedGeneratorStartTime; private static volatile long seedGeneratorEndTime; private static final long multiplier = 25214903917L; private static final long addend = 11L; private static final long mask = 281474976710655L; private long rnd; boolean initialized; static {
/*  76 */     if (initialSeedUniquifier == 0L) {
/*  77 */       boolean secureRandom = SystemPropertyUtil.getBoolean("java.util.secureRandomSeed", false);
/*  78 */       if (secureRandom) {
/*  79 */         seedQueue = new LinkedBlockingQueue<Long>();
/*  80 */         seedGeneratorStartTime = System.nanoTime();
/*     */ 
/*     */ 
/*     */         
/*  84 */         seedGeneratorThread = new Thread("initialSeedUniquifierGenerator")
/*     */           {
/*     */             public void run() {
/*  87 */               SecureRandom random = new SecureRandom();
/*  88 */               byte[] seed = random.generateSeed(8);
/*  89 */               ThreadLocalRandom.seedGeneratorEndTime = System.nanoTime();
/*  90 */               long s = (seed[0] & 0xFFL) << 56L | (seed[1] & 0xFFL) << 48L | (seed[2] & 0xFFL) << 40L | (seed[3] & 0xFFL) << 32L | (seed[4] & 0xFFL) << 24L | (seed[5] & 0xFFL) << 16L | (seed[6] & 0xFFL) << 8L | seed[7] & 0xFFL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  98 */               ThreadLocalRandom.seedQueue.add(Long.valueOf(s));
/*     */             }
/*     */           };
/* 101 */         seedGeneratorThread.setDaemon(true);
/* 102 */         seedGeneratorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
/*     */             {
/*     */               public void uncaughtException(Thread t, Throwable e) {
/* 105 */                 ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
/*     */               }
/*     */             });
/* 108 */         seedGeneratorThread.start();
/*     */       } else {
/* 110 */         initialSeedUniquifier = mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
/* 111 */         seedGeneratorThread = null;
/* 112 */         seedQueue = null;
/* 113 */         seedGeneratorStartTime = 0L;
/*     */       } 
/*     */     } else {
/* 116 */       seedGeneratorThread = null;
/* 117 */       seedQueue = null;
/* 118 */       seedGeneratorStartTime = 0L;
/*     */     } 
/*     */   }
/*     */   private long pad0; private long pad1; private long pad2; private long pad3; private long pad4; private long pad5; private long pad6; private long pad7; private static final long serialVersionUID = -5851777807851030925L;
/*     */   public static void setInitialSeedUniquifier(long initialSeedUniquifier) {
/* 123 */     ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getInitialSeedUniquifier() {
/* 128 */     long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
/* 129 */     if (initialSeedUniquifier != 0L) {
/* 130 */       return initialSeedUniquifier;
/*     */     }
/*     */     
/* 133 */     synchronized (ThreadLocalRandom.class) {
/* 134 */       initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
/* 135 */       if (initialSeedUniquifier != 0L) {
/* 136 */         return initialSeedUniquifier;
/*     */       }
/*     */ 
/*     */       
/* 140 */       long timeoutSeconds = 3L;
/* 141 */       long deadLine = seedGeneratorStartTime + TimeUnit.SECONDS.toNanos(3L);
/* 142 */       boolean interrupted = false;
/*     */       while (true) {
/* 144 */         long waitTime = deadLine - System.nanoTime();
/*     */         try {
/*     */           Long seed;
/* 147 */           if (waitTime <= 0L) {
/* 148 */             seed = seedQueue.poll();
/*     */           } else {
/* 150 */             seed = seedQueue.poll(waitTime, TimeUnit.NANOSECONDS);
/*     */           } 
/*     */           
/* 153 */           if (seed != null) {
/* 154 */             initialSeedUniquifier = seed.longValue();
/*     */             break;
/*     */           } 
/* 157 */         } catch (InterruptedException e) {
/* 158 */           interrupted = true;
/* 159 */           logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
/*     */           
/*     */           break;
/*     */         } 
/* 163 */         if (waitTime <= 0L) {
/* 164 */           seedGeneratorThread.interrupt();
/* 165 */           logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entropy?", 
/*     */               
/* 167 */               Long.valueOf(3L));
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 174 */       initialSeedUniquifier ^= 0x3255ECDC33BAE119L;
/* 175 */       initialSeedUniquifier ^= Long.reverse(System.nanoTime());
/*     */       
/* 177 */       ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
/*     */       
/* 179 */       if (interrupted) {
/*     */         
/* 181 */         Thread.currentThread().interrupt();
/*     */ 
/*     */ 
/*     */         
/* 185 */         seedGeneratorThread.interrupt();
/*     */       } 
/*     */       
/* 188 */       if (seedGeneratorEndTime == 0L) {
/* 189 */         seedGeneratorEndTime = System.nanoTime();
/*     */       }
/*     */       
/* 192 */       return initialSeedUniquifier;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static long newSeed() {
/*     */     while (true) {
/* 198 */       long current = seedUniquifier.get();
/* 199 */       long actualCurrent = (current != 0L) ? current : getInitialSeedUniquifier();
/*     */ 
/*     */       
/* 202 */       long next = actualCurrent * 181783497276652981L;
/*     */       
/* 204 */       if (seedUniquifier.compareAndSet(current, next)) {
/* 205 */         if (current == 0L && logger.isDebugEnabled()) {
/* 206 */           if (seedGeneratorEndTime != 0L) {
/* 207 */             logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", new Object[] {
/*     */                     
/* 209 */                     Long.valueOf(actualCurrent), 
/* 210 */                     Long.valueOf(TimeUnit.NANOSECONDS.toMillis(seedGeneratorEndTime - seedGeneratorStartTime)) }));
/*     */           } else {
/* 212 */             logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", new Object[] { Long.valueOf(actualCurrent) }));
/*     */           } 
/*     */         }
/* 215 */         return next ^ System.nanoTime();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long mix64(long z) {
/* 223 */     z = (z ^ z >>> 33L) * -49064778989728563L;
/* 224 */     z = (z ^ z >>> 33L) * -4265267296055464877L;
/* 225 */     return z ^ z >>> 33L;
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
/*     */   ThreadLocalRandom() {
/* 255 */     super(newSeed());
/* 256 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThreadLocalRandom current() {
/* 265 */     return InternalThreadLocalMap.get().random();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/* 275 */     if (this.initialized) {
/* 276 */       throw new UnsupportedOperationException();
/*     */     }
/* 278 */     this.rnd = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
/*     */   }
/*     */   
/*     */   protected int next(int bits) {
/* 282 */     this.rnd = this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 283 */     return (int)(this.rnd >>> 48 - bits);
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
/*     */   public int nextInt(int least, int bound) {
/* 297 */     if (least >= bound) {
/* 298 */       throw new IllegalArgumentException();
/*     */     }
/* 300 */     return nextInt(bound - least) + least;
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
/*     */   public long nextLong(long n) {
/* 313 */     if (n <= 0L) {
/* 314 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     long offset = 0L;
/* 323 */     while (n >= 2147483647L) {
/* 324 */       int bits = next(2);
/* 325 */       long half = n >>> 1L;
/* 326 */       long nextn = ((bits & 0x2) == 0) ? half : (n - half);
/* 327 */       if ((bits & 0x1) == 0) {
/* 328 */         offset += n - nextn;
/*     */       }
/* 330 */       n = nextn;
/*     */     } 
/* 332 */     return offset + nextInt((int)n);
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
/*     */   public long nextLong(long least, long bound) {
/* 346 */     if (least >= bound) {
/* 347 */       throw new IllegalArgumentException();
/*     */     }
/* 349 */     return nextLong(bound - least) + least;
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
/*     */   public double nextDouble(double n) {
/* 362 */     if (n <= 0.0D) {
/* 363 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/* 365 */     return nextDouble() * n;
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
/*     */   public double nextDouble(double least, double bound) {
/* 379 */     if (least >= bound) {
/* 380 */       throw new IllegalArgumentException();
/*     */     }
/* 382 */     return nextDouble() * (bound - least) + least;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\ThreadLocalRandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */