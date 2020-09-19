/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceLeakDetector<T>
/*     */ {
/*     */   private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
/*     */   private static final String PROP_LEVEL = "io.netty.leakDetection.level";
/*     */   
/*     */   static {
/*     */     boolean disabled;
/*     */   }
/*     */   
/*  44 */   private static final Level DEFAULT_LEVEL = Level.SIMPLE;
/*     */ 
/*     */   
/*     */   private static final String PROP_TARGET_RECORDS = "io.netty.leakDetection.targetRecords";
/*     */   
/*     */   private static final int DEFAULT_TARGET_RECORDS = 4;
/*     */   
/*     */   private static final int TARGET_RECORDS;
/*     */   
/*     */   private static Level level;
/*     */ 
/*     */   
/*     */   public enum Level
/*     */   {
/*  58 */     DISABLED,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     SIMPLE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     ADVANCED,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     PARANOID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static Level parseLevel(String levelStr) {
/*  82 */       String trimmedLevelStr = levelStr.trim();
/*  83 */       for (Level l : values()) {
/*  84 */         if (trimmedLevelStr.equalsIgnoreCase(l.name()) || trimmedLevelStr.equals(String.valueOf(l.ordinal()))) {
/*  85 */           return l;
/*     */         }
/*     */       } 
/*  88 */       return ResourceLeakDetector.DEFAULT_LEVEL;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
/*     */   static final int DEFAULT_SAMPLING_INTERVAL = 128;
/*     */   
/*     */   static {
/*  98 */     if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
/*  99 */       disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
/* 100 */       logger.debug("-Dio.netty.noResourceLeakDetection: {}", Boolean.valueOf(disabled));
/* 101 */       logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetection.level", DEFAULT_LEVEL
/*     */           
/* 103 */           .name().toLowerCase());
/*     */     } else {
/* 105 */       disabled = false;
/*     */     } 
/*     */     
/* 108 */     Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;
/*     */ 
/*     */     
/* 111 */     String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name());
/*     */ 
/*     */     
/* 114 */     levelStr = SystemPropertyUtil.get("io.netty.leakDetection.level", levelStr);
/* 115 */     Level level = Level.parseLevel(levelStr);
/*     */     
/* 117 */     TARGET_RECORDS = SystemPropertyUtil.getInt("io.netty.leakDetection.targetRecords", 4);
/*     */     
/* 119 */     ResourceLeakDetector.level = level;
/* 120 */     if (logger.isDebugEnabled()) {
/* 121 */       logger.debug("-D{}: {}", "io.netty.leakDetection.level", level.name().toLowerCase());
/* 122 */       logger.debug("-D{}: {}", "io.netty.leakDetection.targetRecords", Integer.valueOf(TARGET_RECORDS));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 525 */     excludedMethods = (AtomicReference)new AtomicReference<String>(EmptyArrays.EMPTY_STRINGS);
/*     */   }
/*     */   @Deprecated public static void setEnabled(boolean enabled) { setLevel(enabled ? Level.SIMPLE : Level.DISABLED); }
/*     */   public static boolean isEnabled() { return (getLevel().ordinal() > Level.DISABLED.ordinal()); }
/* 529 */   public static void setLevel(Level level) { if (level == null) throw new NullPointerException("level");  ResourceLeakDetector.level = level; } public static Level getLevel() { return level; } private final ConcurrentMap<DefaultResourceLeak<?>, LeakEntry> allLeaks = PlatformDependent.newConcurrentHashMap(); private final ReferenceQueue<Object> refQueue = new ReferenceQueue(); private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap(); private final String resourceType; private final int samplingInterval; private static final AtomicReference<String[]> excludedMethods; @Deprecated public ResourceLeakDetector(Class<?> resourceType) { this(StringUtil.simpleClassName(resourceType)); } @Deprecated public ResourceLeakDetector(String resourceType) { this(resourceType, 128, Long.MAX_VALUE); } public static void addExclusions(Class clz, String... methodNames) { String[] oldMethods, newMethods; Set<String> nameSet = new HashSet<String>(Arrays.asList(methodNames));
/*     */ 
/*     */     
/* 532 */     for (Method method : clz.getDeclaredMethods()) {
/* 533 */       if (nameSet.remove(method.getName()) && nameSet.isEmpty()) {
/*     */         break;
/*     */       }
/*     */     } 
/* 537 */     if (!nameSet.isEmpty()) {
/* 538 */       throw new IllegalArgumentException("Can't find '" + nameSet + "' in " + clz.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     do {
/* 543 */       oldMethods = excludedMethods.get();
/* 544 */       newMethods = Arrays.<String>copyOf(oldMethods, oldMethods.length + 2 * methodNames.length);
/* 545 */       for (int i = 0; i < methodNames.length; i++) {
/* 546 */         newMethods[oldMethods.length + i * 2] = clz.getName();
/* 547 */         newMethods[oldMethods.length + i * 2 + 1] = methodNames[i];
/*     */       } 
/* 549 */     } while (!excludedMethods.compareAndSet(oldMethods, newMethods)); }
/*     */   @Deprecated public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive) { this(resourceType, samplingInterval); }
/*     */   public ResourceLeakDetector(Class<?> resourceType, int samplingInterval) { this(StringUtil.simpleClassName(resourceType), samplingInterval, Long.MAX_VALUE); }
/*     */   @Deprecated public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive) { if (resourceType == null) throw new NullPointerException("resourceType");  this.resourceType = resourceType; this.samplingInterval = samplingInterval; } @Deprecated public final ResourceLeak open(T obj) { return track0(obj); } public final ResourceLeakTracker<T> track(T obj) { return track0(obj); } private DefaultResourceLeak track0(T obj) { Level level = ResourceLeakDetector.level; if (level == Level.DISABLED) return null;  if (level.ordinal() < Level.PARANOID.ordinal()) { if (PlatformDependent.threadLocalRandom().nextInt(this.samplingInterval) == 0) { reportLeak(); return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks); }  return null; }  reportLeak(); return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks); } private void clearRefQueue() { while (true) { DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll(); if (ref == null) break;  ref.dispose(); }  } private void reportLeak() { if (!logger.isErrorEnabled()) { clearRefQueue(); return; }  while (true) { DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll(); if (ref == null) break;  if (!ref.dispose()) continue;  String records = ref.toString(); if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) { if (records.isEmpty()) { reportUntracedLeak(this.resourceType); continue; }  reportTracedLeak(this.resourceType, records); }  }  } protected void reportTracedLeak(String resourceType, String records) { logger.error("LEAK: {}.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.{}", resourceType, records); } protected void reportUntracedLeak(String resourceType) { logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See http://netty.io/wiki/reference-counted-objects.html for more information.", new Object[] { resourceType, "io.netty.leakDetection.level", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this) }); } @Deprecated protected void reportInstancesLeak(String resourceType) {} private static final class DefaultResourceLeak<T> extends WeakReference<Object> implements ResourceLeakTracker<T>, ResourceLeak {
/*     */     private static final AtomicReferenceFieldUpdater<DefaultResourceLeak<?>, ResourceLeakDetector.Record> headUpdater = AtomicReferenceFieldUpdater.newUpdater((Class)DefaultResourceLeak.class, ResourceLeakDetector.Record.class, "head"); private static final AtomicIntegerFieldUpdater<DefaultResourceLeak<?>> droppedRecordsUpdater = AtomicIntegerFieldUpdater.newUpdater((Class)DefaultResourceLeak.class, "droppedRecords"); private volatile ResourceLeakDetector.Record head; private volatile int droppedRecords; private final ConcurrentMap<DefaultResourceLeak<?>, ResourceLeakDetector.LeakEntry> allLeaks; private final int trackedHash; DefaultResourceLeak(Object referent, ReferenceQueue<Object> refQueue, ConcurrentMap<DefaultResourceLeak<?>, ResourceLeakDetector.LeakEntry> allLeaks) { super(referent, refQueue); assert referent != null; this.trackedHash = System.identityHashCode(referent); allLeaks.put(this, ResourceLeakDetector.LeakEntry.INSTANCE); headUpdater.set(this, new ResourceLeakDetector.Record(ResourceLeakDetector.Record.BOTTOM)); this.allLeaks = allLeaks; } public void record() { record0(null); } public void record(Object hint) { record0(hint); } private void record0(Object hint) { if (ResourceLeakDetector.TARGET_RECORDS > 0) while (true) { boolean dropped; ResourceLeakDetector.Record oldHead, prevHead; if ((prevHead = oldHead = headUpdater.get(this)) == null) return;  int numElements = oldHead.pos + 1; if (numElements >= ResourceLeakDetector.TARGET_RECORDS) { int backOffFactor = Math.min(numElements - ResourceLeakDetector.TARGET_RECORDS, 30); if (dropped = (PlatformDependent.threadLocalRandom().nextInt(1 << backOffFactor) != 0)) prevHead = oldHead.next;  } else { dropped = false; }  ResourceLeakDetector.Record newHead = (hint != null) ? new ResourceLeakDetector.Record(prevHead, hint) : new ResourceLeakDetector.Record(prevHead); if (headUpdater.compareAndSet(this, oldHead, newHead)) { if (dropped) droppedRecordsUpdater.incrementAndGet(this);  break; }  }   } boolean dispose() { clear(); return this.allLeaks.remove(this, ResourceLeakDetector.LeakEntry.INSTANCE); } public boolean close() { if (this.allLeaks.remove(this, ResourceLeakDetector.LeakEntry.INSTANCE)) { clear(); headUpdater.set(this, null); return true; }  return false; } public boolean close(T trackedObject) { assert this.trackedHash == System.identityHashCode(trackedObject); return (close() && trackedObject != null); } public String toString() { ResourceLeakDetector.Record oldHead = headUpdater.getAndSet(this, null); if (oldHead == null) return "";  int dropped = droppedRecordsUpdater.get(this); int duped = 0; int present = oldHead.pos + 1; StringBuilder buf = (new StringBuilder(present * 2048)).append(StringUtil.NEWLINE); buf.append("Recent access records: ").append(StringUtil.NEWLINE); int i = 1; Set<String> seen = new HashSet<String>(present); for (; oldHead != ResourceLeakDetector.Record.BOTTOM; oldHead = oldHead.next) { String s = oldHead.toString(); if (seen.add(s)) { if (oldHead.next == ResourceLeakDetector.Record.BOTTOM) { buf.append("Created at:").append(StringUtil.NEWLINE).append(s); } else { buf.append('#').append(i++).append(':').append(StringUtil.NEWLINE).append(s); }  } else { duped++; }  }  if (duped > 0) buf.append(": ").append(dropped).append(" leak records were discarded because they were duplicates").append(StringUtil.NEWLINE);  if (dropped > 0) buf.append(": ").append(dropped).append(" leak records were discarded because the leak record count is targeted to ").append(ResourceLeakDetector.TARGET_RECORDS).append(". Use system property ").append("io.netty.leakDetection.targetRecords").append(" to increase the limit.").append(StringUtil.NEWLINE);  buf.setLength(buf.length() - StringUtil.NEWLINE.length()); return buf.toString(); }
/*     */   } private static final class Record extends Throwable {
/* 555 */     private static final long serialVersionUID = 6065153674892850720L; private static final Record BOTTOM = new Record();
/*     */     
/*     */     private final String hintString;
/*     */     
/*     */     private final Record next;
/*     */     private final int pos;
/*     */     
/*     */     Record(Record next, Object hint) {
/* 563 */       this.hintString = (hint instanceof ResourceLeakHint) ? ((ResourceLeakHint)hint).toHintString() : hint.toString();
/* 564 */       this.next = next;
/* 565 */       next.pos++;
/*     */     }
/*     */     
/*     */     Record(Record next) {
/* 569 */       this.hintString = null;
/* 570 */       this.next = next;
/* 571 */       next.pos++;
/*     */     }
/*     */ 
/*     */     
/*     */     private Record() {
/* 576 */       this.hintString = null;
/* 577 */       this.next = null;
/* 578 */       this.pos = -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 583 */       StringBuilder buf = new StringBuilder(2048);
/* 584 */       if (this.hintString != null) {
/* 585 */         buf.append("\tHint: ").append(this.hintString).append(StringUtil.NEWLINE);
/*     */       }
/*     */ 
/*     */       
/* 589 */       StackTraceElement[] array = getStackTrace();
/*     */       
/* 591 */       for (int i = 3; i < array.length; i++) {
/* 592 */         StackTraceElement element = array[i];
/*     */         
/* 594 */         String[] exclusions = ResourceLeakDetector.excludedMethods.get();
/* 595 */         int k = 0; while (true) { if (k < exclusions.length) {
/* 596 */             if (exclusions[k].equals(element.getClassName()) && exclusions[k + 1]
/* 597 */               .equals(element.getMethodName()))
/*     */               break; 
/*     */             k += 2;
/*     */             continue;
/*     */           } 
/* 602 */           buf.append('\t');
/* 603 */           buf.append(element.toString());
/* 604 */           buf.append(StringUtil.NEWLINE); break; }
/*     */       
/* 606 */       }  return buf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LeakEntry {
/* 611 */     static final LeakEntry INSTANCE = new LeakEntry();
/* 612 */     private static final int HASH = System.identityHashCode(INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 619 */       return HASH;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 624 */       return (obj == this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ResourceLeakDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */