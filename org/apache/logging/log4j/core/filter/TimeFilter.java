/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class TimeFilter
/*     */   extends AbstractFilter
/*     */ {
/*  44 */   private static final Clock CLOCK = ClockFactory.getClock();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long HOUR_MS = 3600000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MINUTE_MS = 60000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long SECOND_MS = 1000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private final long start;
/*     */ 
/*     */ 
/*     */   
/*     */   private final long end;
/*     */ 
/*     */ 
/*     */   
/*     */   private final TimeZone timezone;
/*     */ 
/*     */ 
/*     */   
/*     */   private long midnightToday;
/*     */ 
/*     */   
/*     */   private long midnightTomorrow;
/*     */ 
/*     */ 
/*     */   
/*     */   private TimeFilter(long start, long end, TimeZone tz, Filter.Result onMatch, Filter.Result onMismatch) {
/*  80 */     super(onMatch, onMismatch);
/*  81 */     this.start = start;
/*  82 */     this.end = end;
/*  83 */     this.timezone = tz;
/*  84 */     initMidnight(start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void initMidnight(long now) {
/*  92 */     Calendar calendar = Calendar.getInstance(this.timezone);
/*  93 */     calendar.setTimeInMillis(now);
/*  94 */     calendar.set(11, 0);
/*  95 */     calendar.set(12, 0);
/*  96 */     calendar.set(13, 0);
/*  97 */     calendar.set(14, 0);
/*  98 */     this.midnightToday = calendar.getTimeInMillis();
/*     */     
/* 100 */     calendar.add(5, 1);
/* 101 */     this.midnightTomorrow = calendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Filter.Result filter(long currentTimeMillis) {
/* 112 */     if (currentTimeMillis >= this.midnightTomorrow || currentTimeMillis < this.midnightToday) {
/* 113 */       initMidnight(currentTimeMillis);
/*     */     }
/* 115 */     return (currentTimeMillis >= this.midnightToday + this.start && currentTimeMillis <= this.midnightToday + this.end) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 122 */     return filter(event.getTimeMillis());
/*     */   }
/*     */   
/*     */   private Filter.Result filter() {
/* 126 */     return filter(CLOCK.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 132 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 138 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 144 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 150 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 156 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 162 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 168 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 174 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 180 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 187 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 194 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 201 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 208 */     return filter();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 213 */     StringBuilder sb = new StringBuilder();
/* 214 */     sb.append("start=").append(this.start);
/* 215 */     sb.append(", end=").append(this.end);
/* 216 */     sb.append(", timezone=").append(this.timezone.toString());
/* 217 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static TimeFilter createFilter(@PluginAttribute("start") String start, @PluginAttribute("end") String end, @PluginAttribute("timezone") String tz, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 236 */     long s = parseTimestamp(start, 0L);
/* 237 */     long e = parseTimestamp(end, Long.MAX_VALUE);
/* 238 */     TimeZone timezone = (tz == null) ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);
/* 239 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/* 240 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/* 241 */     return new TimeFilter(s, e, timezone, onMatch, onMismatch);
/*     */   }
/*     */   
/*     */   private static long parseTimestamp(String timestamp, long defaultValue) {
/* 245 */     if (timestamp == null) {
/* 246 */       return defaultValue;
/*     */     }
/* 248 */     SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
/* 249 */     stf.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     try {
/* 251 */       return stf.parse(timestamp).getTime();
/* 252 */     } catch (ParseException e) {
/* 253 */       LOGGER.warn("Error parsing TimeFilter timestamp value {}", timestamp, e);
/* 254 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\TimeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */