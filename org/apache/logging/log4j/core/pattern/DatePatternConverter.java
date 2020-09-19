/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
/*     */ import org.apache.logging.log4j.core.util.datetime.FixedDateFormat;
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
/*     */ @Plugin(name = "DatePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"d", "date"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class DatePatternConverter
/*     */   extends LogEventPatternConverter
/*     */   implements ArrayPatternConverter
/*     */ {
/*     */   private static final String UNIX_FORMAT = "UNIX";
/*     */   private static final String UNIX_MILLIS_FORMAT = "UNIX_MILLIS";
/*     */   private final String[] options;
/*     */   
/*     */   private static abstract class Formatter
/*     */   {
/*     */     long previousTime;
/*     */     
/*     */     private Formatter() {}
/*     */     
/*     */     public String toPattern() {
/*  49 */       return null;
/*     */     }
/*     */     
/*     */     abstract String format(long param1Long);
/*     */     
/*     */     abstract void formatToBuffer(long param1Long, StringBuilder param1StringBuilder); }
/*     */   
/*     */   private static final class PatternFormatter extends Formatter {
/*  57 */     private final StringBuilder cachedBuffer = new StringBuilder(64); private final FastDateFormat fastDateFormat;
/*     */     
/*     */     PatternFormatter(FastDateFormat fastDateFormat) {
/*  60 */       this.fastDateFormat = fastDateFormat;
/*     */     }
/*     */ 
/*     */     
/*     */     String format(long timeMillis) {
/*  65 */       return this.fastDateFormat.format(timeMillis);
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(long timeMillis, StringBuilder destination) {
/*  70 */       if (this.previousTime != timeMillis) {
/*  71 */         this.cachedBuffer.setLength(0);
/*  72 */         this.fastDateFormat.format(timeMillis, this.cachedBuffer);
/*     */       } 
/*  74 */       destination.append(this.cachedBuffer);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toPattern() {
/*  79 */       return this.fastDateFormat.getPattern();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class FixedFormatter
/*     */     extends Formatter
/*     */   {
/*     */     private final FixedDateFormat fixedDateFormat;
/*  87 */     private final char[] cachedBuffer = new char[64];
/*  88 */     private int length = 0;
/*     */     
/*     */     FixedFormatter(FixedDateFormat fixedDateFormat) {
/*  91 */       this.fixedDateFormat = fixedDateFormat;
/*     */     }
/*     */ 
/*     */     
/*     */     String format(long timeMillis) {
/*  96 */       return this.fixedDateFormat.format(timeMillis);
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(long timeMillis, StringBuilder destination) {
/* 101 */       if (this.previousTime != timeMillis) {
/* 102 */         this.length = this.fixedDateFormat.format(timeMillis, this.cachedBuffer, 0);
/*     */       }
/* 104 */       destination.append(this.cachedBuffer, 0, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toPattern() {
/* 109 */       return this.fixedDateFormat.getFormat();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UnixFormatter extends Formatter {
/*     */     private UnixFormatter() {}
/*     */     
/*     */     String format(long timeMillis) {
/* 117 */       return Long.toString(timeMillis / 1000L);
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(long timeMillis, StringBuilder destination) {
/* 122 */       destination.append(timeMillis / 1000L);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UnixMillisFormatter extends Formatter {
/*     */     private UnixMillisFormatter() {}
/*     */     
/*     */     String format(long timeMillis) {
/* 130 */       return Long.toString(timeMillis);
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(long timeMillis, StringBuilder destination) {
/* 135 */       destination.append(timeMillis);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CachedTime {
/*     */     public long timestampMillis;
/*     */     public String formatted;
/*     */     
/*     */     public CachedTime(long timestampMillis) {
/* 144 */       this.timestampMillis = timestampMillis;
/* 145 */       this.formatted = DatePatternConverter.this.formatter.format(this.timestampMillis);
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
/* 160 */   private final ThreadLocal<Formatter> threadLocalFormatter = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private final AtomicReference<CachedTime> cachedTime;
/*     */ 
/*     */   
/*     */   private final Formatter formatter;
/*     */ 
/*     */   
/*     */   private DatePatternConverter(String[] options) {
/* 170 */     super("Date", "date");
/* 171 */     this.options = (options == null) ? null : Arrays.<String>copyOf(options, options.length);
/* 172 */     this.formatter = createFormatter(options);
/* 173 */     this.cachedTime = new AtomicReference<>(new CachedTime(System.currentTimeMillis()));
/*     */   }
/*     */   
/*     */   private Formatter createFormatter(String[] options) {
/* 177 */     FixedDateFormat fixedDateFormat = FixedDateFormat.createIfSupported(options);
/* 178 */     if (fixedDateFormat != null) {
/* 179 */       return createFixedFormatter(fixedDateFormat);
/*     */     }
/* 181 */     return createNonFixedFormatter(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DatePatternConverter newInstance(String[] options) {
/* 191 */     return new DatePatternConverter(options);
/*     */   }
/*     */   
/*     */   private static Formatter createFixedFormatter(FixedDateFormat fixedDateFormat) {
/* 195 */     return new FixedFormatter(fixedDateFormat);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Formatter createNonFixedFormatter(String[] options) {
/* 200 */     Objects.requireNonNull(options);
/* 201 */     if (options.length == 0) {
/* 202 */       throw new IllegalArgumentException("options array must have at least one element");
/*     */     }
/* 204 */     Objects.requireNonNull(options[0]);
/* 205 */     String patternOption = options[0];
/* 206 */     if ("UNIX".equals(patternOption)) {
/* 207 */       return new UnixFormatter();
/*     */     }
/* 209 */     if ("UNIX_MILLIS".equals(patternOption)) {
/* 210 */       return new UnixMillisFormatter();
/*     */     }
/*     */     
/* 213 */     FixedDateFormat.FixedFormat fixedFormat = FixedDateFormat.FixedFormat.lookup(patternOption);
/* 214 */     String pattern = (fixedFormat == null) ? patternOption : fixedFormat.getPattern();
/*     */ 
/*     */     
/* 217 */     TimeZone tz = null;
/* 218 */     if (options.length > 1 && options[1] != null) {
/* 219 */       tz = TimeZone.getTimeZone(options[1]);
/*     */     }
/*     */     
/*     */     try {
/* 223 */       FastDateFormat tempFormat = FastDateFormat.getInstance(pattern, tz);
/* 224 */       return new PatternFormatter(tempFormat);
/* 225 */     } catch (IllegalArgumentException e) {
/* 226 */       LOGGER.warn("Could not instantiate FastDateFormat with pattern " + pattern, e);
/*     */ 
/*     */       
/* 229 */       return createFixedFormatter(FixedDateFormat.create(FixedDateFormat.FixedFormat.DEFAULT, tz));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Date date, StringBuilder toAppendTo) {
/* 240 */     format(date.getTime(), toAppendTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 248 */     format(event.getTimeMillis(), output);
/*     */   }
/*     */   
/*     */   public void format(long timestampMillis, StringBuilder output) {
/* 252 */     if (Constants.ENABLE_THREADLOCALS) {
/* 253 */       formatWithoutAllocation(timestampMillis, output);
/*     */     } else {
/* 255 */       formatWithoutThreadLocals(timestampMillis, output);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatWithoutAllocation(long timestampMillis, StringBuilder output) {
/* 260 */     getThreadLocalFormatter().formatToBuffer(timestampMillis, output);
/*     */   }
/*     */   
/*     */   private Formatter getThreadLocalFormatter() {
/* 264 */     Formatter result = this.threadLocalFormatter.get();
/* 265 */     if (result == null) {
/* 266 */       result = createFormatter(this.options);
/* 267 */       this.threadLocalFormatter.set(result);
/*     */     } 
/* 269 */     return result;
/*     */   }
/*     */   
/*     */   private void formatWithoutThreadLocals(long timestampMillis, StringBuilder output) {
/* 273 */     CachedTime cached = this.cachedTime.get();
/* 274 */     if (timestampMillis != cached.timestampMillis) {
/* 275 */       CachedTime newTime = new CachedTime(timestampMillis);
/* 276 */       if (this.cachedTime.compareAndSet(cached, newTime)) {
/* 277 */         cached = newTime;
/*     */       } else {
/* 279 */         cached = this.cachedTime.get();
/*     */       } 
/*     */     } 
/* 282 */     output.append(cached.formatted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Object obj, StringBuilder output) {
/* 290 */     if (obj instanceof Date) {
/* 291 */       format((Date)obj, output);
/*     */     }
/* 293 */     super.format(obj, output);
/*     */   }
/*     */ 
/*     */   
/*     */   public void format(StringBuilder toAppendTo, Object... objects) {
/* 298 */     for (Object obj : objects) {
/* 299 */       if (obj instanceof Date) {
/* 300 */         format(obj, toAppendTo);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 312 */     return this.formatter.toPattern();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\DatePatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */