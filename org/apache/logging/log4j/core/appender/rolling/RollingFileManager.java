/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
/*     */ import org.apache.logging.log4j.core.appender.FileManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingFileManager
/*     */   extends FileManager
/*     */ {
/*  53 */   private static RollingFileManagerFactory factory = new RollingFileManagerFactory();
/*     */   
/*     */   private static final int MAX_TRIES = 3;
/*     */   private static final int MIN_DURATION = 100;
/*     */   protected long size;
/*     */   private long initialTime;
/*     */   private final PatternProcessor patternProcessor;
/*  60 */   private final Semaphore semaphore = new Semaphore(1);
/*  61 */   private final Log4jThreadFactory threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
/*     */   
/*     */   private volatile TriggeringPolicy triggeringPolicy;
/*     */   
/*     */   private volatile RolloverStrategy rolloverStrategy;
/*     */   private volatile boolean renameEmptyFiles = false;
/*     */   private volatile boolean initialized = false;
/*     */   private volatile String fileName;
/*     */   private FileExtension fileExtension;
/*  70 */   private ExecutorService asyncExecutor = new ThreadPoolExecutor(0, 2147483647, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), (ThreadFactory)this.threadFactory);
/*     */ 
/*     */   
/*  73 */   private static final AtomicReferenceFieldUpdater<RollingFileManager, TriggeringPolicy> triggeringPolicyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, TriggeringPolicy.class, "triggeringPolicy");
/*     */ 
/*     */   
/*  76 */   private static final AtomicReferenceFieldUpdater<RollingFileManager, RolloverStrategy> rolloverStrategyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, RolloverStrategy.class, "rolloverStrategy");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long time, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean writeHeader) {
/*  84 */     this(fileName, pattern, os, append, size, time, triggeringPolicy, rolloverStrategy, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[Constants.ENCODER_BYTE_BUFFER_SIZE]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long time, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/*  93 */     super(fileName, os, append, false, advertiseURI, layout, writeHeader, buffer);
/*  94 */     this.size = size;
/*  95 */     this.initialTime = time;
/*  96 */     this.triggeringPolicy = triggeringPolicy;
/*  97 */     this.rolloverStrategy = rolloverStrategy;
/*  98 */     this.patternProcessor = new PatternProcessor(pattern);
/*  99 */     this.patternProcessor.setPrevFileTime(time);
/* 100 */     this.fileName = fileName;
/* 101 */     this.fileExtension = FileExtension.lookupForFile(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RollingFileManager(LoggerContext loggerContext, String fileName, String pattern, OutputStream os, boolean append, boolean createOnDemand, long size, long time, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/* 111 */     super(loggerContext, fileName, os, append, false, createOnDemand, advertiseURI, layout, writeHeader, buffer);
/* 112 */     this.size = size;
/* 113 */     this.initialTime = time;
/* 114 */     this.triggeringPolicy = triggeringPolicy;
/* 115 */     this.rolloverStrategy = rolloverStrategy;
/* 116 */     this.patternProcessor = new PatternProcessor(pattern);
/* 117 */     this.patternProcessor.setPrevFileTime(time);
/* 118 */     this.fileName = fileName;
/* 119 */     this.fileExtension = FileExtension.lookupForFile(pattern);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 124 */     if (!this.initialized) {
/* 125 */       LOGGER.debug("Initializing triggering policy {}", this.triggeringPolicy);
/* 126 */       this.initialized = true;
/* 127 */       this.triggeringPolicy.initialize(this);
/* 128 */       if (this.triggeringPolicy instanceof LifeCycle) {
/* 129 */         ((LifeCycle)this.triggeringPolicy).start();
/*     */       }
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
/*     */   public static RollingFileManager getFileManager(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, Configuration configuration) {
/* 154 */     String name = (fileName == null) ? pattern : fileName;
/* 155 */     return (RollingFileManager)getManager(name, new FactoryData(fileName, pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize, immediateFlush, createOnDemand, configuration), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 165 */     if (this.rolloverStrategy instanceof DirectFileRolloverStrategy) {
/* 166 */       this.fileName = ((DirectFileRolloverStrategy)this.rolloverStrategy).getCurrentFileName(this);
/*     */     }
/* 168 */     return this.fileName;
/*     */   }
/*     */   
/*     */   public FileExtension getFileExtension() {
/* 172 */     return this.fileExtension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 179 */     super.write(bytes, offset, length, immediateFlush);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/* 184 */     this.size += length;
/* 185 */     super.writeToDestination(bytes, offset, length);
/*     */   }
/*     */   
/*     */   public boolean isRenameEmptyFiles() {
/* 189 */     return this.renameEmptyFiles;
/*     */   }
/*     */   
/*     */   public void setRenameEmptyFiles(boolean renameEmptyFiles) {
/* 193 */     this.renameEmptyFiles = renameEmptyFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileSize() {
/* 201 */     return this.size + this.byteBuffer.position();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileTime() {
/* 209 */     return this.initialTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void checkRollover(LogEvent event) {
/* 217 */     if (this.triggeringPolicy.isTriggeringEvent(event)) {
/* 218 */       rollover();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/*     */     int i;
/* 224 */     LOGGER.debug("Shutting down RollingFileManager {}" + getName());
/* 225 */     boolean stopped = true;
/* 226 */     if (this.triggeringPolicy instanceof LifeCycle2) {
/* 227 */       stopped &= ((LifeCycle2)this.triggeringPolicy).stop(timeout, timeUnit);
/* 228 */     } else if (this.triggeringPolicy instanceof LifeCycle) {
/* 229 */       ((LifeCycle)this.triggeringPolicy).stop();
/* 230 */       i = stopped & true;
/*     */     } 
/* 232 */     boolean status = (super.releaseSub(timeout, timeUnit) && i != 0);
/* 233 */     this.asyncExecutor.shutdown();
/*     */     
/*     */     try {
/* 236 */       long millis = timeUnit.toMillis(timeout);
/* 237 */       long waitInterval = (100L < millis) ? millis : 100L;
/*     */       
/* 239 */       for (int count = 1; count <= 3 && !this.asyncExecutor.isTerminated(); count++) {
/* 240 */         this.asyncExecutor.awaitTermination(waitInterval * count, TimeUnit.MILLISECONDS);
/*     */       }
/* 242 */       if (this.asyncExecutor.isTerminated()) {
/* 243 */         LOGGER.debug("All asynchronous threads have terminated");
/*     */       } else {
/* 245 */         this.asyncExecutor.shutdownNow();
/*     */         try {
/* 247 */           this.asyncExecutor.awaitTermination(timeout, timeUnit);
/* 248 */           if (this.asyncExecutor.isTerminated()) {
/* 249 */             LOGGER.debug("All asynchronous threads have terminated");
/*     */           } else {
/* 251 */             LOGGER.debug("RollingFileManager shutting down but some asynchronous services may not have completed");
/*     */           } 
/* 253 */         } catch (InterruptedException inner) {
/* 254 */           LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
/*     */         } 
/*     */       } 
/* 257 */     } catch (InterruptedException ie) {
/* 258 */       this.asyncExecutor.shutdownNow();
/*     */       try {
/* 260 */         this.asyncExecutor.awaitTermination(timeout, timeUnit);
/* 261 */         if (this.asyncExecutor.isTerminated()) {
/* 262 */           LOGGER.debug("All asynchronous threads have terminated");
/*     */         }
/* 264 */       } catch (InterruptedException inner) {
/* 265 */         LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
/*     */       } 
/*     */       
/* 268 */       Thread.currentThread().interrupt();
/*     */     } 
/* 270 */     LOGGER.debug("RollingFileManager shutdown completed with status {}", Boolean.valueOf(status));
/* 271 */     return status;
/*     */   }
/*     */   
/*     */   public synchronized void rollover() {
/* 275 */     if (!hasOutputStream()) {
/*     */       return;
/*     */     }
/* 278 */     if (rollover(this.rolloverStrategy)) {
/*     */       try {
/* 280 */         this.size = 0L;
/* 281 */         this.initialTime = System.currentTimeMillis();
/* 282 */         createFileAfterRollover();
/* 283 */       } catch (IOException e) {
/* 284 */         logError("Failed to create file after rollover", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 290 */     setOutputStream(createOutputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor getPatternProcessor() {
/* 298 */     return this.patternProcessor;
/*     */   }
/*     */   
/*     */   public void setTriggeringPolicy(TriggeringPolicy triggeringPolicy) {
/* 302 */     triggeringPolicy.initialize(this);
/* 303 */     TriggeringPolicy policy = this.triggeringPolicy;
/* 304 */     int count = 0;
/* 305 */     boolean policyUpdated = false;
/*     */     do {
/* 307 */       count++;
/*     */     }
/* 309 */     while (!(policyUpdated = triggeringPolicyUpdater.compareAndSet(this, this.triggeringPolicy, triggeringPolicy)) && count < 3);
/* 310 */     if (policyUpdated) {
/* 311 */       if (triggeringPolicy instanceof LifeCycle) {
/* 312 */         ((LifeCycle)triggeringPolicy).start();
/*     */       }
/* 314 */       if (policy instanceof LifeCycle) {
/* 315 */         ((LifeCycle)policy).stop();
/*     */       }
/*     */     }
/* 318 */     else if (triggeringPolicy instanceof LifeCycle) {
/* 319 */       ((LifeCycle)triggeringPolicy).stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRolloverStrategy(RolloverStrategy rolloverStrategy) {
/* 325 */     rolloverStrategyUpdater.compareAndSet(this, this.rolloverStrategy, rolloverStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends TriggeringPolicy> T getTriggeringPolicy() {
/* 336 */     return (T)this.triggeringPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RolloverStrategy getRolloverStrategy() {
/* 344 */     return this.rolloverStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean rollover(RolloverStrategy strategy) {
/* 349 */     boolean releaseRequired = false;
/*     */     
/*     */     try {
/* 352 */       this.semaphore.acquire();
/* 353 */       releaseRequired = true;
/* 354 */     } catch (InterruptedException e) {
/* 355 */       logError("Thread interrupted while attempting to check rollover", e);
/* 356 */       return false;
/*     */     } 
/*     */     
/* 359 */     boolean success = true;
/*     */     
/*     */     try {
/* 362 */       RolloverDescription descriptor = strategy.rollover(this);
/* 363 */       if (descriptor != null) {
/* 364 */         writeFooter();
/* 365 */         closeOutputStream();
/* 366 */         if (descriptor.getSynchronous() != null) {
/* 367 */           LOGGER.debug("RollingFileManager executing synchronous {}", descriptor.getSynchronous());
/*     */           try {
/* 369 */             success = descriptor.getSynchronous().execute();
/* 370 */           } catch (Exception ex) {
/* 371 */             success = false;
/* 372 */             logError("Caught error in synchronous task", ex);
/*     */           } 
/*     */         } 
/*     */         
/* 376 */         if (success && descriptor.getAsynchronous() != null) {
/* 377 */           LOGGER.debug("RollingFileManager executing async {}", descriptor.getAsynchronous());
/* 378 */           this.asyncExecutor.execute((Runnable)new AsyncAction(descriptor.getAsynchronous(), this));
/* 379 */           releaseRequired = false;
/*     */         } 
/* 381 */         return true;
/*     */       } 
/* 383 */       return false;
/*     */     } finally {
/* 385 */       if (releaseRequired) {
/* 386 */         this.semaphore.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AsyncAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final Action action;
/*     */ 
/*     */ 
/*     */     
/*     */     private final RollingFileManager manager;
/*     */ 
/*     */ 
/*     */     
/*     */     public AsyncAction(Action act, RollingFileManager manager) {
/* 406 */       this.action = act;
/* 407 */       this.manager = manager;
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
/*     */     public boolean execute() throws IOException {
/*     */       try {
/* 421 */         return this.action.execute();
/*     */       } finally {
/* 423 */         this.manager.semaphore.release();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 432 */       this.action.close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isComplete() {
/* 442 */       return this.action.isComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 447 */       StringBuilder builder = new StringBuilder();
/* 448 */       builder.append(super.toString());
/* 449 */       builder.append("[action=");
/* 450 */       builder.append(this.action);
/* 451 */       builder.append(", manager=");
/* 452 */       builder.append(this.manager);
/* 453 */       builder.append(", isComplete()=");
/* 454 */       builder.append(isComplete());
/* 455 */       builder.append(", isInterrupted()=");
/* 456 */       builder.append(isInterrupted());
/* 457 */       builder.append("]");
/* 458 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */     extends ConfigurationFactoryData
/*     */   {
/*     */     private final String fileName;
/*     */ 
/*     */     
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean bufferedIO;
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final boolean immediateFlush;
/*     */     
/*     */     private final boolean createOnDemand;
/*     */     
/*     */     private final TriggeringPolicy policy;
/*     */     
/*     */     private final RolloverStrategy strategy;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, Configuration configuration) {
/* 494 */       super(configuration);
/* 495 */       this.fileName = fileName;
/* 496 */       this.pattern = pattern;
/* 497 */       this.append = append;
/* 498 */       this.bufferedIO = bufferedIO;
/* 499 */       this.bufferSize = bufferSize;
/* 500 */       this.policy = policy;
/* 501 */       this.strategy = strategy;
/* 502 */       this.advertiseURI = advertiseURI;
/* 503 */       this.layout = layout;
/* 504 */       this.immediateFlush = immediateFlush;
/* 505 */       this.createOnDemand = createOnDemand;
/*     */     }
/*     */ 
/*     */     
/*     */     public TriggeringPolicy getTriggeringPolicy() {
/* 510 */       return this.policy;
/*     */     }
/*     */ 
/*     */     
/*     */     public RolloverStrategy getRolloverStrategy() {
/* 515 */       return this.strategy;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 520 */       StringBuilder builder = new StringBuilder();
/* 521 */       builder.append(super.toString());
/* 522 */       builder.append("[pattern=");
/* 523 */       builder.append(this.pattern);
/* 524 */       builder.append(", append=");
/* 525 */       builder.append(this.append);
/* 526 */       builder.append(", bufferedIO=");
/* 527 */       builder.append(this.bufferedIO);
/* 528 */       builder.append(", bufferSize=");
/* 529 */       builder.append(this.bufferSize);
/* 530 */       builder.append(", policy=");
/* 531 */       builder.append(this.policy);
/* 532 */       builder.append(", strategy=");
/* 533 */       builder.append(this.strategy);
/* 534 */       builder.append(", advertiseURI=");
/* 535 */       builder.append(this.advertiseURI);
/* 536 */       builder.append(", layout=");
/* 537 */       builder.append(this.layout);
/* 538 */       builder.append("]");
/* 539 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateData(Object data) {
/* 546 */     FactoryData factoryData = (FactoryData)data;
/* 547 */     setRolloverStrategy(factoryData.getRolloverStrategy());
/* 548 */     setTriggeringPolicy(factoryData.getTriggeringPolicy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingFileManagerFactory
/*     */     implements ManagerFactory<RollingFileManager, FactoryData>
/*     */   {
/*     */     private RollingFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingFileManager createManager(String name, RollingFileManager.FactoryData data) {
/* 564 */       long size = 0L;
/* 565 */       boolean writeHeader = !data.append;
/* 566 */       File file = null;
/* 567 */       if (data.fileName != null) {
/* 568 */         file = new File(data.fileName);
/*     */         
/* 570 */         writeHeader = (!data.append || !file.exists());
/*     */         
/*     */         try {
/* 573 */           FileUtils.makeParentDirs(file);
/* 574 */           boolean created = data.createOnDemand ? false : file.createNewFile();
/* 575 */           RollingFileManager.LOGGER.trace("New file '{}' created = {}", name, Boolean.valueOf(created));
/* 576 */         } catch (IOException ioe) {
/* 577 */           RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
/* 578 */           return null;
/*     */         } 
/* 580 */         size = data.append ? file.length() : 0L;
/*     */       } 
/*     */       
/*     */       try {
/* 584 */         int actualSize = data.bufferedIO ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
/* 585 */         ByteBuffer buffer = ByteBuffer.wrap(new byte[actualSize]);
/* 586 */         OutputStream os = (data.createOnDemand || data.fileName == null) ? null : new FileOutputStream(data.fileName, data.append);
/*     */         
/* 588 */         long time = (data.createOnDemand || file == null) ? System.currentTimeMillis() : file.lastModified();
/*     */ 
/*     */         
/* 591 */         return new RollingFileManager(data.getLoggerContext(), data.fileName, data.pattern, os, data.append, data.createOnDemand, size, time, data.policy, data.strategy, data.advertiseURI, data.layout, writeHeader, buffer);
/*     */       
/*     */       }
/* 594 */       catch (IOException ex) {
/* 595 */         RollingFileManager.LOGGER.error("RollingFileManager (" + name + ") " + ex, ex);
/*     */         
/* 597 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyQueue extends ArrayBlockingQueue<Runnable> {
/*     */     EmptyQueue() {
/* 604 */       super(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remainingCapacity() {
/* 609 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(Runnable runnable) {
/* 614 */       throw new IllegalStateException("Queue is full");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(Runnable runnable) throws InterruptedException {
/* 620 */       throw new InterruptedException("Unable to insert into queue");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(Runnable runnable, long timeout, TimeUnit timeUnit) throws InterruptedException {
/* 625 */       Thread.sleep(timeUnit.toMillis(timeout));
/* 626 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends Runnable> collection) {
/* 631 */       if (collection.size() > 0) {
/* 632 */         throw new IllegalArgumentException("Too many items in collection");
/*     */       }
/* 634 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\RollingFileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */