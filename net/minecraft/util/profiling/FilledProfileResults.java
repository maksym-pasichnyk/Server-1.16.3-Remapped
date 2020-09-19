/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class FilledProfileResults
/*     */   implements ProfileResults {
/*  28 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  30 */   private static final ProfilerPathEntry EMPTY = new ProfilerPathEntry()
/*     */     {
/*     */       public long getDuration() {
/*  33 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getCount() {
/*  38 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/*     */       public Object2LongMap<String> getCounters() {
/*  43 */         return Object2LongMaps.emptyMap();
/*     */       }
/*     */     };
/*     */   private static final Comparator<Map.Entry<String, CounterCollector>> COUNTER_ENTRY_COMPARATOR;
/*  47 */   private static final Splitter SPLITTER = Splitter.on('\036'); static {
/*  48 */     COUNTER_ENTRY_COMPARATOR = Map.Entry.<String, CounterCollector>comparingByValue(Comparator.comparingLong(debug0 -> debug0.totalValue)).reversed();
/*     */   }
/*     */   private final Map<String, ? extends ProfilerPathEntry> entries;
/*     */   private final long startTimeNano;
/*     */   private final int startTimeTicks;
/*     */   private final long endTimeNano;
/*     */   private final int endTimeTicks;
/*     */   private final int tickDuration;
/*     */   
/*     */   public FilledProfileResults(Map<String, ? extends ProfilerPathEntry> debug1, long debug2, int debug4, long debug5, int debug7) {
/*  58 */     this.entries = debug1;
/*  59 */     this.startTimeNano = debug2;
/*  60 */     this.startTimeTicks = debug4;
/*  61 */     this.endTimeNano = debug5;
/*  62 */     this.endTimeTicks = debug7;
/*  63 */     this.tickDuration = debug7 - debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   private ProfilerPathEntry getEntry(String debug1) {
/*  68 */     ProfilerPathEntry debug2 = this.entries.get(debug1);
/*  69 */     return (debug2 != null) ? debug2 : EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResultField> getTimes(String debug1) {
/*  74 */     String debug2 = debug1;
/*  75 */     ProfilerPathEntry debug3 = getEntry("root");
/*  76 */     long debug4 = debug3.getDuration();
/*  77 */     ProfilerPathEntry debug6 = getEntry(debug1);
/*  78 */     long debug7 = debug6.getDuration();
/*  79 */     long debug9 = debug6.getCount();
/*     */     
/*  81 */     List<ResultField> debug11 = Lists.newArrayList();
/*     */     
/*  83 */     if (!debug1.isEmpty()) {
/*  84 */       debug1 = debug1 + '\036';
/*     */     }
/*  86 */     long debug12 = 0L;
/*     */     
/*  88 */     for (String debug15 : this.entries.keySet()) {
/*  89 */       if (isDirectChild(debug1, debug15)) {
/*  90 */         debug12 += getEntry(debug15).getDuration();
/*     */       }
/*     */     } 
/*     */     
/*  94 */     float debug14 = (float)debug12;
/*  95 */     if (debug12 < debug7) {
/*  96 */       debug12 = debug7;
/*     */     }
/*  98 */     if (debug4 < debug12) {
/*  99 */       debug4 = debug12;
/*     */     }
/*     */     
/* 102 */     for (String debug16 : this.entries.keySet()) {
/* 103 */       if (isDirectChild(debug1, debug16)) {
/* 104 */         ProfilerPathEntry debug17 = getEntry(debug16);
/* 105 */         long debug18 = debug17.getDuration();
/* 106 */         double debug20 = debug18 * 100.0D / debug12;
/* 107 */         double debug22 = debug18 * 100.0D / debug4;
/* 108 */         String debug24 = debug16.substring(debug1.length());
/* 109 */         debug11.add(new ResultField(debug24, debug20, debug22, debug17.getCount()));
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     if ((float)debug12 > debug14) {
/* 114 */       debug11.add(new ResultField("unspecified", ((float)debug12 - debug14) * 100.0D / debug12, ((float)debug12 - debug14) * 100.0D / debug4, debug9));
/*     */     }
/*     */     
/* 117 */     Collections.sort(debug11);
/* 118 */     debug11.add(0, new ResultField(debug2, 100.0D, debug12 * 100.0D / debug4, debug9));
/* 119 */     return debug11;
/*     */   }
/*     */   
/*     */   private static boolean isDirectChild(String debug0, String debug1) {
/* 123 */     return (debug1.length() > debug0.length() && debug1.startsWith(debug0) && debug1.indexOf('\036', debug0.length() + 1) < 0);
/*     */   }
/*     */   
/*     */   private Map<String, CounterCollector> getCounterValues() {
/* 127 */     Map<String, CounterCollector> debug1 = Maps.newTreeMap();
/* 128 */     this.entries.forEach((debug1, debug2) -> {
/*     */           Object2LongMap<String> debug3 = debug2.getCounters();
/*     */           
/*     */           if (!debug3.isEmpty()) {
/*     */             List<String> debug4 = SPLITTER.splitToList(debug1);
/*     */             
/*     */             debug3.forEach(());
/*     */           } 
/*     */         });
/* 137 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getStartTimeNano() {
/* 142 */     return this.startTimeNano;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartTimeTicks() {
/* 147 */     return this.startTimeTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getEndTimeNano() {
/* 152 */     return this.endTimeNano;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndTimeTicks() {
/* 157 */     return this.endTimeTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveResults(File debug1) {
/* 162 */     debug1.getParentFile().mkdirs();
/*     */     
/* 164 */     Writer debug2 = null;
/*     */     try {
/* 166 */       debug2 = new OutputStreamWriter(new FileOutputStream(debug1), StandardCharsets.UTF_8);
/* 167 */       debug2.write(getProfilerResults(getNanoDuration(), getTickDuration()));
/* 168 */       return true;
/* 169 */     } catch (Throwable debug3) {
/* 170 */       LOGGER.error("Could not save profiler results to {}", debug1, debug3);
/* 171 */       return false;
/*     */     } finally {
/* 173 */       IOUtils.closeQuietly(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getProfilerResults(long debug1, int debug3) {
/* 178 */     StringBuilder debug4 = new StringBuilder();
/*     */     
/* 180 */     debug4.append("---- Minecraft Profiler Results ----\n");
/* 181 */     debug4.append("// ");
/* 182 */     debug4.append(getComment());
/* 183 */     debug4.append("\n\n");
/*     */     
/* 185 */     debug4.append("Version: ").append(SharedConstants.getCurrentVersion().getId()).append('\n');
/* 186 */     debug4.append("Time span: ").append(debug1 / 1000000L).append(" ms\n");
/* 187 */     debug4.append("Tick span: ").append(debug3).append(" ticks\n");
/* 188 */     debug4.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(debug3 / (float)debug1 / 1.0E9F) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
/*     */     
/* 190 */     debug4.append("--- BEGIN PROFILE DUMP ---\n\n");
/*     */     
/* 192 */     appendProfilerResults(0, "root", debug4);
/*     */     
/* 194 */     debug4.append("--- END PROFILE DUMP ---\n\n");
/*     */     
/* 196 */     Map<String, CounterCollector> debug5 = getCounterValues();
/*     */     
/* 198 */     if (!debug5.isEmpty()) {
/* 199 */       debug4.append("--- BEGIN COUNTER DUMP ---\n\n");
/* 200 */       appendCounters(debug5, debug4, debug3);
/* 201 */       debug4.append("--- END COUNTER DUMP ---\n\n");
/*     */     } 
/*     */     
/* 204 */     return debug4.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder indentLine(StringBuilder debug0, int debug1) {
/* 215 */     debug0.append(String.format("[%02d] ", new Object[] { Integer.valueOf(debug1) }));
/* 216 */     for (int debug2 = 0; debug2 < debug1; debug2++) {
/* 217 */       debug0.append("|   ");
/*     */     }
/* 219 */     return debug0;
/*     */   }
/*     */   
/*     */   private void appendProfilerResults(int debug1, String debug2, StringBuilder debug3) {
/* 223 */     List<ResultField> debug4 = getTimes(debug2);
/*     */     
/* 225 */     Object2LongMap<String> debug5 = ((ProfilerPathEntry)ObjectUtils.firstNonNull((Object[])new ProfilerPathEntry[] { this.entries.get(debug2), EMPTY })).getCounters();
/* 226 */     debug5.forEach((debug3, debug4) -> indentLine(debug1, debug2).append('#').append(debug3).append(' ').append(debug4).append('/').append(debug4.longValue() / this.tickDuration).append('\n'));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (debug4.size() < 3) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 242 */     for (int debug6 = 1; debug6 < debug4.size(); debug6++) {
/* 243 */       ResultField debug7 = debug4.get(debug6);
/*     */       
/* 245 */       indentLine(debug3, debug1)
/* 246 */         .append(debug7.name)
/* 247 */         .append('(')
/* 248 */         .append(debug7.count)
/* 249 */         .append('/')
/* 250 */         .append(String.format(Locale.ROOT, "%.0f", new Object[] { Float.valueOf((float)debug7.count / this.tickDuration)
/* 251 */             })).append(')')
/* 252 */         .append(" - ")
/* 253 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug7.percentage) })).append("%/")
/* 254 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug7.globalPercentage) })).append("%\n");
/*     */       
/* 256 */       if (!"unspecified".equals(debug7.name)) {
/*     */         try {
/* 258 */           appendProfilerResults(debug1 + 1, debug2 + '\036' + debug7.name, debug3);
/* 259 */         } catch (Exception debug8) {
/* 260 */           debug3.append("[[ EXCEPTION ").append(debug8).append(" ]]");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendCounterResults(int debug1, String debug2, CounterCollector debug3, int debug4, StringBuilder debug5) {
/* 267 */     indentLine(debug5, debug1)
/* 268 */       .append(debug2).append(" total:")
/* 269 */       .append(debug3.selfValue).append('/')
/* 270 */       .append(debug3.totalValue).append(" average: ")
/* 271 */       .append(debug3.selfValue / debug4)
/* 272 */       .append('/')
/* 273 */       .append(debug3.totalValue / debug4)
/* 274 */       .append('\n');
/* 275 */     debug3.children.entrySet().stream().sorted(COUNTER_ENTRY_COMPARATOR).forEach(debug4 -> appendCounterResults(debug1 + 1, (String)debug4.getKey(), (CounterCollector)debug4.getValue(), debug2, debug3));
/*     */   }
/*     */   
/*     */   private void appendCounters(Map<String, CounterCollector> debug1, StringBuilder debug2, int debug3) {
/* 279 */     debug1.forEach((debug3, debug4) -> {
/*     */           debug1.append("-- Counter: ").append(debug3).append(" --\n");
/*     */           appendCounterResults(0, "root", (CounterCollector)debug4.children.get("root"), debug2, debug1);
/*     */           debug1.append("\n\n");
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getComment() {
/* 288 */     String[] debug0 = { "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 306 */       return debug0[(int)(Util.getNanos() % debug0.length)];
/* 307 */     } catch (Throwable debug1) {
/* 308 */       return "Witty comment unavailable :(";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDuration() {
/* 314 */     return this.tickDuration;
/*     */   }
/*     */   
/*     */   static class CounterCollector {
/*     */     private long selfValue;
/*     */     private long totalValue;
/* 320 */     private final Map<String, CounterCollector> children = Maps.newHashMap();
/*     */     
/*     */     public void addValue(Iterator<String> debug1, long debug2) {
/* 323 */       this.totalValue += debug2;
/* 324 */       if (!debug1.hasNext()) {
/* 325 */         this.selfValue += debug2;
/*     */       } else {
/* 327 */         ((CounterCollector)this.children.computeIfAbsent(debug1.next(), debug0 -> new CounterCollector())).addValue(debug1, debug2);
/*     */       } 
/*     */     }
/*     */     
/*     */     private CounterCollector() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\FilledProfileResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */