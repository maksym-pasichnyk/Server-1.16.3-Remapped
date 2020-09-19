/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ListeningExecutorService;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.Hash;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.concurrent.ForkJoinWorkerThread;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*  62 */   private static final AtomicInteger WORKER_COUNT = new AtomicInteger(1);
/*  63 */   private static final ExecutorService BOOTSTRAP_EXECUTOR = makeExecutor("Bootstrap");
/*  64 */   private static final ExecutorService BACKGROUND_EXECUTOR = makeExecutor("Main");
/*  65 */   private static final ExecutorService IO_POOL = makeIoExecutor();
/*     */   
/*  67 */   public static LongSupplier timeSource = System::nanoTime;
/*  68 */   public static final UUID NIL_UUID = new UUID(0L, 0L);
/*     */   
/*  70 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public static <K, V> Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() {
/*  73 */     return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<T>> String getPropertyName(Property<T> debug0, Object debug1) {
/*  78 */     return debug0.getName((Comparable)debug1);
/*     */   }
/*     */   
/*     */   public static String makeDescriptionId(String debug0, @Nullable ResourceLocation debug1) {
/*  82 */     if (debug1 == null) {
/*  83 */       return debug0 + ".unregistered_sadface";
/*     */     }
/*  85 */     return debug0 + '.' + debug1.getNamespace() + '.' + debug1.getPath().replace('/', '.');
/*     */   }
/*     */   
/*     */   public static long getMillis() {
/*  89 */     return getNanos() / 1000000L;
/*     */   }
/*     */   
/*     */   public static long getNanos() {
/*  93 */     return timeSource.getAsLong();
/*     */   }
/*     */   
/*     */   public static long getEpochMillis() {
/*  97 */     return Instant.now().toEpochMilli();
/*     */   }
/*     */   private static ExecutorService makeExecutor(String debug0) {
/*     */     ExecutorService debug2;
/* 101 */     int debug1 = Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 7);
/*     */     
/* 103 */     if (debug1 <= 0) {
/* 104 */       ListeningExecutorService listeningExecutorService = MoreExecutors.newDirectExecutorService();
/*     */     } else {
/* 106 */       debug2 = new ForkJoinPool(debug1, debug1 -> {
/*     */             ForkJoinWorkerThread debug2 = new ForkJoinWorkerThread(debug1)
/*     */               {
/*     */                 protected void onTermination(Throwable debug1) {
/* 110 */                   if (debug1 != null) {
/* 111 */                     Util.LOGGER.warn("{} died", getName(), debug1);
/*     */                   } else {
/* 113 */                     Util.LOGGER.debug("{} shutdown", getName());
/*     */                   } 
/* 115 */                   super.onTermination(debug1);
/*     */                 }
/*     */               };
/*     */             debug2.setName("Worker-" + debug0 + "-" + WORKER_COUNT.getAndIncrement());
/*     */             return debug2;
/*     */           }Util::onThreadException, true);
/*     */     } 
/* 122 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Executor bootstrapExecutor() {
/* 127 */     return BOOTSTRAP_EXECUTOR;
/*     */   }
/*     */   
/*     */   public static Executor backgroundExecutor() {
/* 131 */     return BACKGROUND_EXECUTOR;
/*     */   }
/*     */   
/*     */   public static Executor ioPool() {
/* 135 */     return IO_POOL;
/*     */   }
/*     */   
/*     */   public static void shutdownExecutors() {
/* 139 */     shutdownExecutor(BACKGROUND_EXECUTOR);
/* 140 */     shutdownExecutor(IO_POOL);
/*     */   }
/*     */   private static void shutdownExecutor(ExecutorService debug0) {
/*     */     boolean debug1;
/* 144 */     debug0.shutdown();
/*     */     
/*     */     try {
/* 147 */       debug1 = debug0.awaitTermination(3L, TimeUnit.SECONDS);
/* 148 */     } catch (InterruptedException debug2) {
/* 149 */       debug1 = false;
/*     */     } 
/* 151 */     if (!debug1) {
/* 152 */       debug0.shutdownNow();
/*     */     }
/*     */   }
/*     */   
/*     */   private static ExecutorService makeIoExecutor() {
/* 157 */     return Executors.newCachedThreadPool(debug0 -> {
/*     */           Thread debug1 = new Thread(debug0);
/*     */           debug1.setName("IO-Worker-" + WORKER_COUNT.getAndIncrement());
/*     */           debug1.setUncaughtExceptionHandler(Util::onThreadException);
/*     */           return debug1;
/*     */         });
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
/*     */   private static void onThreadException(Thread debug0, Throwable debug1) {
/* 177 */     pauseInIde(debug1);
/* 178 */     if (debug1 instanceof java.util.concurrent.CompletionException) {
/* 179 */       debug1 = debug1.getCause();
/*     */     }
/* 181 */     if (debug1 instanceof ReportedException) {
/* 182 */       Bootstrap.realStdoutPrintln(((ReportedException)debug1).getReport().getFriendlyReport());
/* 183 */       System.exit(-1);
/*     */     } 
/* 185 */     LOGGER.error(String.format("Caught exception in thread %s", new Object[] { debug0 }), debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Type<?> fetchChoiceType(DSL.TypeReference debug0, String debug1) {
/* 190 */     if (!SharedConstants.CHECK_DATA_FIXER_SCHEMA) {
/* 191 */       return null;
/*     */     }
/* 193 */     return doFetchChoiceType(debug0, debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Type<?> doFetchChoiceType(DSL.TypeReference debug0, String debug1) {
/* 198 */     Type<?> debug2 = null;
/*     */     try {
/* 200 */       debug2 = DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getWorldVersion())).getChoiceType(debug0, debug1);
/* 201 */     } catch (IllegalArgumentException debug3) {
/* 202 */       LOGGER.error("No data fixer registered for {}", debug1);
/* 203 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 204 */         throw debug3;
/*     */       }
/*     */     } 
/* 207 */     return debug2;
/*     */   }
/*     */   
/*     */   public enum OS {
/* 211 */     LINUX,
/* 212 */     SOLARIS,
/* 213 */     WINDOWS
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*     */     },
/* 219 */     OSX
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*     */     },
/* 225 */     UNKNOWN;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OS getPlatform() {
/* 277 */     String debug0 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/* 278 */     if (debug0.contains("win")) {
/* 279 */       return OS.WINDOWS;
/*     */     }
/* 281 */     if (debug0.contains("mac")) {
/* 282 */       return OS.OSX;
/*     */     }
/* 284 */     if (debug0.contains("solaris")) {
/* 285 */       return OS.SOLARIS;
/*     */     }
/* 287 */     if (debug0.contains("sunos")) {
/* 288 */       return OS.SOLARIS;
/*     */     }
/* 290 */     if (debug0.contains("linux")) {
/* 291 */       return OS.LINUX;
/*     */     }
/* 293 */     if (debug0.contains("unix")) {
/* 294 */       return OS.LINUX;
/*     */     }
/* 296 */     return OS.UNKNOWN;
/*     */   }
/*     */   
/*     */   public static Stream<String> getVmArguments() {
/* 300 */     RuntimeMXBean debug0 = ManagementFactory.getRuntimeMXBean();
/* 301 */     return debug0.getInputArguments().stream().filter(debug0 -> debug0.startsWith("-X"));
/*     */   }
/*     */   
/*     */   public static <T> T lastOf(List<T> debug0) {
/* 305 */     return debug0.get(debug0.size() - 1);
/*     */   }
/*     */   
/*     */   public static <T> T findNextInIterable(Iterable<T> debug0, @Nullable T debug1) {
/* 309 */     Iterator<T> debug2 = debug0.iterator();
/* 310 */     T debug3 = debug2.next();
/*     */     
/* 312 */     if (debug1 != null) {
/* 313 */       T debug4 = debug3;
/*     */       while (true) {
/* 315 */         if (debug4 == debug1) {
/* 316 */           if (debug2.hasNext()) {
/* 317 */             return debug2.next();
/*     */           }
/*     */           
/*     */           break;
/*     */         } 
/* 322 */         if (debug2.hasNext()) {
/* 323 */           debug4 = debug2.next();
/*     */         }
/*     */       } 
/*     */     } 
/* 327 */     return debug3;
/*     */   }
/*     */   
/*     */   public static <T> T findPreviousInIterable(Iterable<T> debug0, @Nullable T debug1) {
/* 331 */     Iterator<T> debug2 = debug0.iterator();
/* 332 */     T debug3 = null;
/* 333 */     while (debug2.hasNext()) {
/* 334 */       T debug4 = debug2.next();
/* 335 */       if (debug4 == debug1) {
/* 336 */         if (debug3 == null) {
/* 337 */           debug3 = debug2.hasNext() ? (T)Iterators.getLast(debug2) : debug1;
/*     */         }
/*     */         break;
/*     */       } 
/* 341 */       debug3 = debug4;
/*     */     } 
/* 343 */     return debug3;
/*     */   }
/*     */   
/*     */   public static <T> T make(Supplier<T> debug0) {
/* 347 */     return debug0.get();
/*     */   }
/*     */   
/*     */   public static <T> T make(T debug0, Consumer<T> debug1) {
/* 351 */     debug1.accept(debug0);
/* 352 */     return debug0;
/*     */   }
/*     */   
/*     */   enum IdentityStrategy implements Hash.Strategy<Object> {
/* 356 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int hashCode(Object debug1) {
/* 360 */       return System.identityHashCode(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1, Object debug2) {
/* 365 */       return (debug1 == debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K> Hash.Strategy<K> identityStrategy() {
/* 371 */     return IdentityStrategy.INSTANCE;
/*     */   }
/*     */   
/*     */   public static <V> CompletableFuture<List<V>> sequence(List<? extends CompletableFuture<? extends V>> debug0) {
/* 375 */     List<V> debug1 = Lists.newArrayListWithCapacity(debug0.size());
/* 376 */     CompletableFuture[] arrayOfCompletableFuture = new CompletableFuture[debug0.size()];
/*     */     
/* 378 */     CompletableFuture<Void> debug3 = new CompletableFuture<>();
/*     */     
/* 380 */     debug0.forEach(debug3 -> {
/*     */           int debug4 = debug0.size();
/*     */ 
/*     */ 
/*     */           
/*     */           debug0.add(null);
/*     */ 
/*     */           
/*     */           debug1[debug4] = debug3.whenComplete(());
/*     */         });
/*     */ 
/*     */     
/* 392 */     return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).applyToEither(debug3, debug1 -> debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Stream<T> toStream(Optional<? extends T> debug0) {
/* 397 */     return (Stream<T>)DataFixUtils.orElseGet(debug0.map(Stream::of), Stream::empty);
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> ifElse(Optional<T> debug0, Consumer<T> debug1, Runnable debug2) {
/* 401 */     if (debug0.isPresent()) {
/* 402 */       debug1.accept(debug0.get());
/*     */     } else {
/* 404 */       debug2.run();
/*     */     } 
/* 406 */     return debug0;
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
/*     */   public static Runnable name(Runnable debug0, Supplier<String> debug1) {
/* 424 */     return debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Throwable> T pauseInIde(T debug0) {
/* 429 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 430 */       LOGGER.error("Trying to throw a fatal exception, pausing in IDE", (Throwable)debug0);
/*     */       try {
/*     */         while (true)
/* 433 */         { Thread.sleep(1000L);
/* 434 */           LOGGER.error("paused"); } 
/* 435 */       } catch (InterruptedException debug1) {
/* 436 */         return debug0;
/*     */       } 
/*     */     } 
/*     */     
/* 440 */     return debug0;
/*     */   }
/*     */   
/*     */   public static String describeError(Throwable debug0) {
/* 444 */     if (debug0.getCause() != null)
/* 445 */       return describeError(debug0.getCause()); 
/* 446 */     if (debug0.getMessage() != null) {
/* 447 */       return debug0.getMessage();
/*     */     }
/* 449 */     return debug0.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T getRandom(T[] debug0, Random debug1) {
/* 454 */     return debug0[debug1.nextInt(debug0.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getRandom(int[] debug0, Random debug1) {
/* 459 */     return debug0[debug1.nextInt(debug0.length)];
/*     */   }
/*     */   
/*     */   private static BooleanSupplier createRenamer(final Path from, final Path to) {
/* 463 */     return new BooleanSupplier()
/*     */       {
/*     */         public boolean getAsBoolean() {
/*     */           try {
/* 467 */             Files.move(from, to, new java.nio.file.CopyOption[0]);
/* 468 */             return true;
/* 469 */           } catch (IOException debug1) {
/* 470 */             Util.LOGGER.error("Failed to rename", debug1);
/* 471 */             return false;
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 477 */           return "rename " + from + " to " + to;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static BooleanSupplier createDeleter(final Path target) {
/* 483 */     return new BooleanSupplier()
/*     */       {
/*     */         public boolean getAsBoolean() {
/*     */           try {
/* 487 */             Files.deleteIfExists(target);
/* 488 */             return true;
/* 489 */           } catch (IOException debug1) {
/* 490 */             Util.LOGGER.warn("Failed to delete", debug1);
/* 491 */             return false;
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 497 */           return "delete old " + target;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static BooleanSupplier createFileDeletedCheck(final Path target) {
/* 503 */     return new BooleanSupplier()
/*     */       {
/*     */         public boolean getAsBoolean() {
/* 506 */           return !Files.exists(target, new java.nio.file.LinkOption[0]);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 511 */           return "verify that " + target + " is deleted";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static BooleanSupplier createFileCreatedCheck(final Path target) {
/* 517 */     return new BooleanSupplier()
/*     */       {
/*     */         public boolean getAsBoolean() {
/* 520 */           return Files.isRegularFile(target, new java.nio.file.LinkOption[0]);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 525 */           return "verify that " + target + " is present";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean executeInSequence(BooleanSupplier... debug0) {
/* 531 */     for (BooleanSupplier debug4 : debug0) {
/* 532 */       if (!debug4.getAsBoolean()) {
/* 533 */         LOGGER.warn("Failed to execute {}", debug4);
/* 534 */         return false;
/*     */       } 
/*     */     } 
/* 537 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean runWithRetries(int debug0, String debug1, BooleanSupplier... debug2) {
/* 541 */     for (int debug3 = 0; debug3 < debug0; debug3++) {
/* 542 */       if (executeInSequence(debug2)) {
/* 543 */         return true;
/*     */       }
/* 545 */       LOGGER.error("Failed to {}, retrying {}/{}", debug1, Integer.valueOf(debug3), Integer.valueOf(debug0));
/*     */     } 
/* 547 */     LOGGER.error("Failed to {}, aborting, progress might be lost", debug1);
/* 548 */     return false;
/*     */   }
/*     */   
/*     */   public static void safeReplaceFile(File debug0, File debug1, File debug2) {
/* 552 */     safeReplaceFile(debug0.toPath(), debug1.toPath(), debug2.toPath());
/*     */   }
/*     */   
/*     */   public static void safeReplaceFile(Path debug0, Path debug1, Path debug2) {
/* 556 */     int debug3 = 10;
/*     */     
/* 558 */     if (Files.exists(debug0, new java.nio.file.LinkOption[0]) && 
/* 559 */       !runWithRetries(10, "create backup " + debug2, new BooleanSupplier[] {
/* 560 */           createDeleter(debug2), 
/* 561 */           createRenamer(debug0, debug2), 
/* 562 */           createFileCreatedCheck(debug2)
/*     */         })) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 569 */     if (!runWithRetries(10, "remove old " + debug0, new BooleanSupplier[] {
/* 570 */           createDeleter(debug0), 
/* 571 */           createFileDeletedCheck(debug0)
/*     */         })) {
/*     */       return;
/*     */     }
/*     */     
/* 576 */     if (!runWithRetries(10, "replace " + debug0 + " with " + debug1, new BooleanSupplier[] {
/* 577 */           createRenamer(debug1, debug0), 
/* 578 */           createFileCreatedCheck(debug0)
/*     */         })) {
/* 580 */       runWithRetries(10, "restore " + debug0 + " from " + debug2, new BooleanSupplier[] {
/* 581 */             createRenamer(debug2, debug0), 
/* 582 */             createFileCreatedCheck(debug0)
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Consumer<String> prefix(String debug0, Consumer<String> debug1) {
/* 609 */     return debug2 -> debug0.accept(debug1 + debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataResult<int[]> fixedSize(IntStream debug0, int debug1) {
/* 616 */     int[] debug2 = debug0.limit((debug1 + 1)).toArray();
/* 617 */     if (debug2.length != debug1) {
/* 618 */       String debug3 = "Input is not a list of " + debug1 + " ints";
/* 619 */       if (debug2.length >= debug1) {
/* 620 */         return DataResult.error(debug3, Arrays.copyOf(debug2, debug1));
/*     */       }
/* 622 */       return DataResult.error(debug3);
/*     */     } 
/*     */     
/* 625 */     return DataResult.success(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startTimerHackThread() {
/* 631 */     Thread debug0 = new Thread("Timer hack thread")
/*     */       {
/*     */         public void run() {
/*     */           try {
/*     */             while (true)
/* 636 */               Thread.sleep(2147483647L); 
/* 637 */           } catch (InterruptedException debug1) {
/* 638 */             Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
/*     */             
/*     */             return;
/*     */           } 
/*     */         }
/*     */       };
/* 644 */     debug0.setDaemon(true);
/* 645 */     debug0.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 646 */     debug0.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */