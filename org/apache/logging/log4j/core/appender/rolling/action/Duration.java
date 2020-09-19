/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Duration
/*     */   implements Serializable, Comparable<Duration>
/*     */ {
/*     */   private static final long serialVersionUID = -3756810052716342061L;
/*  41 */   public static final Duration ZERO = new Duration(0L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int HOURS_PER_DAY = 24;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_MINUTE = 60;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_HOUR = 3600;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_DAY = 86400;
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final Pattern PATTERN = Pattern.compile("P?(?:([0-9]+)D)?(T?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)?S)?)?", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long seconds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Duration(long seconds) {
/*  82 */     this.seconds = seconds;
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
/*     */   public static Duration parse(CharSequence text) {
/* 118 */     Objects.requireNonNull(text, "text");
/* 119 */     Matcher matcher = PATTERN.matcher(text);
/* 120 */     if (matcher.matches())
/*     */     {
/* 122 */       if (!"T".equals(matcher.group(2))) {
/* 123 */         String dayMatch = matcher.group(1);
/* 124 */         String hourMatch = matcher.group(3);
/* 125 */         String minuteMatch = matcher.group(4);
/* 126 */         String secondMatch = matcher.group(5);
/* 127 */         if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
/* 128 */           long daysAsSecs = parseNumber(text, dayMatch, 86400, "days");
/* 129 */           long hoursAsSecs = parseNumber(text, hourMatch, 3600, "hours");
/* 130 */           long minsAsSecs = parseNumber(text, minuteMatch, 60, "minutes");
/* 131 */           long seconds = parseNumber(text, secondMatch, 1, "seconds");
/*     */           try {
/* 133 */             return create(daysAsSecs, hoursAsSecs, minsAsSecs, seconds);
/* 134 */           } catch (ArithmeticException ex) {
/* 135 */             throw new IllegalArgumentException("Text cannot be parsed to a Duration (overflow) " + text, ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 140 */     throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseNumber(CharSequence text, String parsed, int multiplier, String errorText) {
/* 146 */     if (parsed == null) {
/* 147 */       return 0L;
/*     */     }
/*     */     try {
/* 150 */       long val = Long.parseLong(parsed);
/* 151 */       return val * multiplier;
/* 152 */     } catch (Exception ex) {
/* 153 */       throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + errorText + " (in " + text + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Duration create(long daysAsSecs, long hoursAsSecs, long minsAsSecs, long secs) {
/* 159 */     return create(daysAsSecs + hoursAsSecs + minsAsSecs + secs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Duration create(long seconds) {
/* 168 */     if (seconds == 0L) {
/* 169 */       return ZERO;
/*     */     }
/* 171 */     return new Duration(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toMillis() {
/* 180 */     return this.seconds * 1000L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 185 */     if (obj == this) {
/* 186 */       return true;
/*     */     }
/* 188 */     if (!(obj instanceof Duration)) {
/* 189 */       return false;
/*     */     }
/* 191 */     Duration other = (Duration)obj;
/* 192 */     return (other.seconds == this.seconds);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return (int)(this.seconds ^ this.seconds >>> 32L);
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
/*     */   public String toString() {
/* 220 */     if (this == ZERO) {
/* 221 */       return "PT0S";
/*     */     }
/* 223 */     long days = this.seconds / 86400L;
/* 224 */     long hours = this.seconds % 86400L / 3600L;
/* 225 */     int minutes = (int)(this.seconds % 3600L / 60L);
/* 226 */     int secs = (int)(this.seconds % 60L);
/* 227 */     StringBuilder buf = new StringBuilder(24);
/* 228 */     buf.append("P");
/* 229 */     if (days != 0L) {
/* 230 */       buf.append(days).append('D');
/*     */     }
/* 232 */     if ((hours | minutes | secs) != 0L) {
/* 233 */       buf.append('T');
/*     */     }
/* 235 */     if (hours != 0L) {
/* 236 */       buf.append(hours).append('H');
/*     */     }
/* 238 */     if (minutes != 0) {
/* 239 */       buf.append(minutes).append('M');
/*     */     }
/* 241 */     if (secs == 0 && buf.length() > 0) {
/* 242 */       return buf.toString();
/*     */     }
/* 244 */     buf.append(secs).append('S');
/* 245 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Duration other) {
/* 255 */     return Long.signum(toMillis() - other.toMillis());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\Duration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */