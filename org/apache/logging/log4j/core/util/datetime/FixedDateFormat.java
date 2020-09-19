/*     */ package org.apache.logging.log4j.core.util.datetime;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedDateFormat
/*     */ {
/*     */   private final FixedFormat fixedFormat;
/*     */   private final TimeZone timeZone;
/*     */   private final int length;
/*     */   private final FastDateFormat fastDateFormat;
/*     */   private final char timeSeparatorChar;
/*     */   private final char millisSeparatorChar;
/*     */   private final int timeSeparatorLength;
/*     */   private final int millisSeparatorLength;
/*     */   
/*     */   public enum FixedFormat
/*     */   {
/*  41 */     ABSOLUTE("HH:mm:ss,SSS", null, 0, ':', 1, ',', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     ABSOLUTE_PERIOD("HH:mm:ss.SSS", null, 0, ':', 1, '.', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     COMPACT("yyyyMMddHHmmssSSS", "yyyyMMdd", 0, ' ', 0, ' ', 0),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     DATE("dd MMM yyyy HH:mm:ss,SSS", "dd MMM yyyy ", 0, ':', 1, ',', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     DATE_PERIOD("dd MMM yyyy HH:mm:ss.SSS", "dd MMM yyyy ", 0, ':', 1, '.', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     DEFAULT("yyyy-MM-dd HH:mm:ss,SSS", "yyyy-MM-dd ", 0, ':', 1, ',', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     DEFAULT_PERIOD("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd ", 0, ':', 1, '.', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     ISO8601_BASIC("yyyyMMdd'T'HHmmss,SSS", "yyyyMMdd'T'", 2, ' ', 0, ',', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     ISO8601_BASIC_PERIOD("yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'", 2, ' ', 0, '.', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     ISO8601("yyyy-MM-dd'T'HH:mm:ss,SSS", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     ISO8601_PERIOD("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1);
/*     */     
/*     */     private final String pattern;
/*     */     
/*     */     private final String datePattern;
/*     */     private final int escapeCount;
/*     */     private final char timeSeparatorChar;
/*     */     private final int timeSeparatorLength;
/*     */     private final char millisSeparatorChar;
/*     */     private final int millisSeparatorLength;
/*     */     
/*     */     FixedFormat(String pattern, String datePattern, int escapeCount, char timeSeparator, int timeSepLength, char millisSeparator, int millisSepLength) {
/* 103 */       this.timeSeparatorChar = timeSeparator;
/* 104 */       this.timeSeparatorLength = timeSepLength;
/* 105 */       this.millisSeparatorChar = millisSeparator;
/* 106 */       this.millisSeparatorLength = millisSepLength;
/* 107 */       this.pattern = Objects.<String>requireNonNull(pattern);
/* 108 */       this.datePattern = datePattern;
/* 109 */       this.escapeCount = escapeCount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPattern() {
/* 118 */       return this.pattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDatePattern() {
/* 127 */       return this.datePattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static FixedFormat lookup(String nameOrPattern) {
/* 137 */       for (FixedFormat type : values()) {
/* 138 */         if (type.name().equals(nameOrPattern) || type.getPattern().equals(nameOrPattern)) {
/* 139 */           return type;
/*     */         }
/*     */       } 
/* 142 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getLength() {
/* 151 */       return this.pattern.length() - this.escapeCount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getDatePatternLength() {
/* 160 */       return (getDatePattern() == null) ? 0 : (getDatePattern().length() - this.escapeCount);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastDateFormat getFastDateFormat() {
/* 170 */       return getFastDateFormat((TimeZone)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastDateFormat getFastDateFormat(TimeZone tz) {
/* 181 */       return (getDatePattern() == null) ? null : FastDateFormat.getInstance(getDatePattern(), tz);
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
/* 194 */   private volatile long midnightToday = 0L;
/* 195 */   private volatile long midnightTomorrow = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char[] cachedDate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int dateLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FixedDateFormat(FixedFormat fixedFormat, TimeZone tz) {
/* 213 */     this.fixedFormat = Objects.<FixedFormat>requireNonNull(fixedFormat);
/* 214 */     this.timeZone = Objects.<TimeZone>requireNonNull(tz);
/* 215 */     this.timeSeparatorChar = fixedFormat.timeSeparatorChar;
/* 216 */     this.timeSeparatorLength = fixedFormat.timeSeparatorLength;
/* 217 */     this.millisSeparatorChar = fixedFormat.millisSeparatorChar;
/* 218 */     this.millisSeparatorLength = fixedFormat.millisSeparatorLength;
/* 219 */     this.length = fixedFormat.getLength();
/* 220 */     this.fastDateFormat = fixedFormat.getFastDateFormat(tz);
/*     */   }
/*     */   public static FixedDateFormat createIfSupported(String... options) {
/*     */     TimeZone tz;
/* 224 */     if (options == null || options.length == 0 || options[0] == null) {
/* 225 */       return new FixedDateFormat(FixedFormat.DEFAULT, TimeZone.getDefault());
/*     */     }
/*     */     
/* 228 */     if (options.length > 1)
/* 229 */     { if (options[1] != null) {
/* 230 */         tz = TimeZone.getTimeZone(options[1]);
/*     */       } else {
/* 232 */         tz = TimeZone.getDefault();
/*     */       }  }
/* 234 */     else { if (options.length > 2) {
/* 235 */         return null;
/*     */       }
/* 237 */       tz = TimeZone.getDefault(); }
/*     */ 
/*     */     
/* 240 */     FixedFormat type = FixedFormat.lookup(options[0]);
/* 241 */     return (type == null) ? null : new FixedDateFormat(type, tz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FixedDateFormat create(FixedFormat format) {
/* 251 */     return new FixedDateFormat(format, TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FixedDateFormat create(FixedFormat format, TimeZone tz) {
/* 262 */     return new FixedDateFormat(format, (tz != null) ? tz : TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 271 */     return this.fixedFormat.getPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 281 */     return this.timeZone;
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
/*     */   public long millisSinceMidnight(long currentTime) {
/* 296 */     if (currentTime >= this.midnightTomorrow || currentTime < this.midnightToday) {
/* 297 */       updateMidnightMillis(currentTime);
/*     */     }
/* 299 */     return currentTime - this.midnightToday;
/*     */   }
/*     */   
/*     */   private void updateMidnightMillis(long now) {
/* 303 */     if (now >= this.midnightTomorrow || now < this.midnightToday) {
/* 304 */       synchronized (this) {
/* 305 */         updateCachedDate(now);
/* 306 */         this.midnightToday = calcMidnightMillis(now, 0);
/* 307 */         this.midnightTomorrow = calcMidnightMillis(now, 1);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private long calcMidnightMillis(long time, int addDays) {
/* 313 */     Calendar cal = Calendar.getInstance(this.timeZone);
/* 314 */     cal.setTimeInMillis(time);
/* 315 */     cal.set(11, 0);
/* 316 */     cal.set(12, 0);
/* 317 */     cal.set(13, 0);
/* 318 */     cal.set(14, 0);
/* 319 */     cal.add(5, addDays);
/* 320 */     return cal.getTimeInMillis();
/*     */   }
/*     */   
/*     */   private void updateCachedDate(long now) {
/* 324 */     if (this.fastDateFormat != null) {
/* 325 */       StringBuilder result = this.fastDateFormat.<StringBuilder>format(now, new StringBuilder());
/* 326 */       this.cachedDate = result.toString().toCharArray();
/* 327 */       this.dateLength = result.length();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(long time) {
/* 334 */     char[] result = new char[this.length << 1];
/* 335 */     int written = format(time, result, 0);
/* 336 */     return new String(result, 0, written);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int format(long time, char[] buffer, int startPos) {
/* 347 */     int ms = (int)millisSinceMidnight(time);
/* 348 */     writeDate(buffer, startPos);
/* 349 */     return writeTime(ms, buffer, startPos + this.dateLength) - startPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeDate(char[] buffer, int startPos) {
/* 355 */     if (this.cachedDate != null) {
/* 356 */       System.arraycopy(this.cachedDate, 0, buffer, startPos, this.dateLength);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int writeTime(int ms, char[] buffer, int pos) {
/* 363 */     int hours = ms / 3600000;
/* 364 */     ms -= 3600000 * hours;
/*     */     
/* 366 */     int minutes = ms / 60000;
/* 367 */     ms -= 60000 * minutes;
/*     */     
/* 369 */     int seconds = ms / 1000;
/* 370 */     ms -= 1000 * seconds;
/*     */ 
/*     */     
/* 373 */     int temp = hours / 10;
/* 374 */     buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */     
/* 377 */     buffer[pos++] = (char)(hours - 10 * temp + 48);
/* 378 */     buffer[pos] = this.timeSeparatorChar;
/* 379 */     pos += this.timeSeparatorLength;
/*     */ 
/*     */     
/* 382 */     temp = minutes / 10;
/* 383 */     buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */     
/* 386 */     buffer[pos++] = (char)(minutes - 10 * temp + 48);
/* 387 */     buffer[pos] = this.timeSeparatorChar;
/* 388 */     pos += this.timeSeparatorLength;
/*     */ 
/*     */     
/* 391 */     temp = seconds / 10;
/* 392 */     buffer[pos++] = (char)(temp + 48);
/* 393 */     buffer[pos++] = (char)(seconds - 10 * temp + 48);
/* 394 */     buffer[pos] = this.millisSeparatorChar;
/* 395 */     pos += this.millisSeparatorLength;
/*     */ 
/*     */     
/* 398 */     temp = ms / 100;
/* 399 */     buffer[pos++] = (char)(temp + 48);
/*     */     
/* 401 */     ms -= 100 * temp;
/* 402 */     temp = ms / 10;
/* 403 */     buffer[pos++] = (char)(temp + 48);
/*     */     
/* 405 */     ms -= 10 * temp;
/* 406 */     buffer[pos++] = (char)(ms + 48);
/* 407 */     return pos;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\datetime\FixedDateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */