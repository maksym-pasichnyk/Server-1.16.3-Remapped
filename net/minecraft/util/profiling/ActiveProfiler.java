/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ public class ActiveProfiler implements ProfileCollector {
/*  23 */   private static final long WARNING_TIME_NANOS = Duration.ofMillis(100L).toNanos();
/*  24 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  26 */   private final List<String> paths = Lists.newArrayList();
/*  27 */   private final LongList startTimes = (LongList)new LongArrayList();
/*  28 */   private final Map<String, PathEntry> entries = Maps.newHashMap();
/*     */   private final IntSupplier getTickTime;
/*     */   private final LongSupplier getRealTime;
/*     */   private final long startTimeNano;
/*     */   private final int startTimeTicks;
/*  33 */   private String path = "";
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   @Nullable
/*     */   private PathEntry currentEntry;
/*     */   private final boolean warn;
/*     */   
/*     */   public ActiveProfiler(LongSupplier debug1, IntSupplier debug2, boolean debug3) {
/*  42 */     this.startTimeNano = debug1.getAsLong();
/*  43 */     this.getRealTime = debug1;
/*  44 */     this.startTimeTicks = debug2.getAsInt();
/*  45 */     this.getTickTime = debug2;
/*  46 */     this.warn = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  51 */     if (this.started) {
/*  52 */       LOGGER.error("Profiler tick already started - missing endTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  56 */     this.started = true;
/*  57 */     this.path = "";
/*  58 */     this.paths.clear();
/*  59 */     push("root");
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  64 */     if (!this.started) {
/*  65 */       LOGGER.error("Profiler tick already ended - missing startTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     pop();
/*  70 */     this.started = false;
/*     */     
/*  72 */     if (!this.path.isEmpty()) {
/*  73 */       LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", new Supplier[] { () -> ProfileResults.demanglePath(this.path) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String debug1) {
/*  79 */     if (!this.started) {
/*  80 */       LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", debug1);
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     if (!this.path.isEmpty()) {
/*  85 */       this.path += '\036';
/*     */     }
/*  87 */     this.path += debug1;
/*  88 */     this.paths.add(this.path);
/*  89 */     this.startTimes.add(Util.getNanos());
/*  90 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Supplier<String> debug1) {
/*  95 */     push(debug1.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public void pop() {
/* 100 */     if (!this.started) {
/* 101 */       LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
/*     */       return;
/*     */     } 
/* 104 */     if (this.startTimes.isEmpty()) {
/* 105 */       LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
/*     */       return;
/*     */     } 
/* 108 */     long debug1 = Util.getNanos();
/* 109 */     long debug3 = this.startTimes.removeLong(this.startTimes.size() - 1);
/* 110 */     this.paths.remove(this.paths.size() - 1);
/* 111 */     long debug5 = debug1 - debug3;
/*     */     
/* 113 */     PathEntry debug7 = getCurrentEntry();
/* 114 */     debug7.duration = debug7.duration + debug5;
/* 115 */     debug7.count = debug7.count + 1L;
/*     */     
/* 117 */     if (this.warn && debug5 > WARNING_TIME_NANOS) {
/* 118 */       LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", new Supplier[] { () -> ProfileResults.demanglePath(this.path), () -> Double.valueOf(debug0 / 1000000.0D) });
/*     */     }
/*     */     
/* 121 */     this.path = this.paths.isEmpty() ? "" : this.paths.get(this.paths.size() - 1);
/* 122 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String debug1) {
/* 127 */     pop();
/* 128 */     push(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PathEntry getCurrentEntry() {
/* 138 */     if (this.currentEntry == null) {
/* 139 */       this.currentEntry = this.entries.computeIfAbsent(this.path, debug0 -> new PathEntry());
/*     */     }
/*     */     
/* 142 */     return this.currentEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(String debug1) {
/* 147 */     (getCurrentEntry()).counters.addTo(debug1, 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(Supplier<String> debug1) {
/* 152 */     (getCurrentEntry()).counters.addTo(debug1.get(), 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfileResults getResults() {
/* 158 */     return new FilledProfileResults((Map)this.entries, this.startTimeNano, this.startTimeTicks, this.getRealTime.getAsLong(), this.getTickTime.getAsInt());
/*     */   }
/*     */   
/*     */   static class PathEntry implements ProfilerPathEntry {
/*     */     private long duration;
/*     */     private long count;
/* 164 */     private Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();
/*     */ 
/*     */     
/*     */     public long getDuration() {
/* 168 */       return this.duration;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getCount() {
/* 173 */       return this.count;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object2LongMap<String> getCounters() {
/* 178 */       return Object2LongMaps.unmodifiable((Object2LongMap)this.counters);
/*     */     }
/*     */     
/*     */     private PathEntry() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ActiveProfiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */