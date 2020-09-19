/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.DatePatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.FormattingInfo;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternProcessor
/*     */ {
/*  41 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String KEY = "FileConverter";
/*     */   private static final char YEAR_CHAR = 'y';
/*     */   private static final char MONTH_CHAR = 'M';
/*  46 */   private static final char[] WEEK_CHARS = new char[] { 'w', 'W' };
/*  47 */   private static final char[] DAY_CHARS = new char[] { 'D', 'd', 'F', 'E' };
/*  48 */   private static final char[] HOUR_CHARS = new char[] { 'H', 'K', 'h', 'k' };
/*     */   
/*     */   private static final char MINUTE_CHAR = 'm';
/*     */   
/*     */   private static final char SECOND_CHAR = 's';
/*     */   private static final char MILLIS_CHAR = 'S';
/*     */   private final ArrayPatternConverter[] patternConverters;
/*     */   private final FormattingInfo[] patternFields;
/*  56 */   private long prevFileTime = 0L;
/*  57 */   private long nextFileTime = 0L;
/*  58 */   private long currentFileTime = 0L;
/*     */   
/*  60 */   private RolloverFrequency frequency = null;
/*     */   
/*     */   private final String pattern;
/*     */   
/*     */   public String getPattern() {
/*  65 */     return this.pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor(String pattern) {
/*  78 */     this.pattern = pattern;
/*  79 */     PatternParser parser = createPatternParser();
/*  80 */     List<PatternConverter> converters = new ArrayList<>();
/*  81 */     List<FormattingInfo> fields = new ArrayList<>();
/*  82 */     parser.parse(pattern, converters, fields, false, false, false);
/*  83 */     FormattingInfo[] infoArray = new FormattingInfo[fields.size()];
/*  84 */     this.patternFields = fields.<FormattingInfo>toArray(infoArray);
/*  85 */     ArrayPatternConverter[] converterArray = new ArrayPatternConverter[converters.size()];
/*  86 */     this.patternConverters = converters.<ArrayPatternConverter>toArray(converterArray);
/*     */     
/*  88 */     for (ArrayPatternConverter converter : this.patternConverters) {
/*  89 */       if (converter instanceof DatePatternConverter) {
/*  90 */         DatePatternConverter dateConverter = (DatePatternConverter)converter;
/*  91 */         this.frequency = calculateFrequency(dateConverter.getPattern());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getCurrentFileTime() {
/*  97 */     return this.currentFileTime;
/*     */   }
/*     */   
/*     */   public void setCurrentFileTime(long currentFileTime) {
/* 101 */     this.currentFileTime = currentFileTime;
/*     */   }
/*     */   
/*     */   public long getPrevFileTime() {
/* 105 */     return this.prevFileTime;
/*     */   }
/*     */   
/*     */   public void setPrevFileTime(long prevFileTime) {
/* 109 */     LOGGER.debug("Setting prev file time to {}", new Date(prevFileTime));
/* 110 */     this.prevFileTime = prevFileTime;
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
/*     */   public long getNextTime(long currentMillis, int increment, boolean modulus) {
/* 125 */     this.prevFileTime = this.nextFileTime;
/*     */ 
/*     */     
/* 128 */     if (this.frequency == null) {
/* 129 */       throw new IllegalStateException("Pattern does not contain a date");
/*     */     }
/* 131 */     Calendar currentCal = Calendar.getInstance();
/* 132 */     currentCal.setTimeInMillis(currentMillis);
/* 133 */     Calendar cal = Calendar.getInstance();
/* 134 */     currentCal.setMinimalDaysInFirstWeek(7);
/* 135 */     cal.setMinimalDaysInFirstWeek(7);
/* 136 */     cal.set(currentCal.get(1), 0, 1, 0, 0, 0);
/* 137 */     cal.set(14, 0);
/* 138 */     if (this.frequency == RolloverFrequency.ANNUALLY) {
/* 139 */       increment(cal, 1, increment, modulus);
/* 140 */       long l = cal.getTimeInMillis();
/* 141 */       cal.add(1, -1);
/* 142 */       this.nextFileTime = cal.getTimeInMillis();
/* 143 */       return debugGetNextTime(l);
/*     */     } 
/* 145 */     cal.set(2, currentCal.get(2));
/* 146 */     if (this.frequency == RolloverFrequency.MONTHLY) {
/* 147 */       increment(cal, 2, increment, modulus);
/* 148 */       long l = cal.getTimeInMillis();
/* 149 */       cal.add(2, -1);
/* 150 */       this.nextFileTime = cal.getTimeInMillis();
/* 151 */       return debugGetNextTime(l);
/*     */     } 
/* 153 */     if (this.frequency == RolloverFrequency.WEEKLY) {
/* 154 */       cal.set(3, currentCal.get(3));
/* 155 */       increment(cal, 3, increment, modulus);
/* 156 */       cal.set(7, currentCal.getFirstDayOfWeek());
/* 157 */       long l = cal.getTimeInMillis();
/* 158 */       cal.add(3, -1);
/* 159 */       this.nextFileTime = cal.getTimeInMillis();
/* 160 */       return debugGetNextTime(l);
/*     */     } 
/* 162 */     cal.set(6, currentCal.get(6));
/* 163 */     if (this.frequency == RolloverFrequency.DAILY) {
/* 164 */       increment(cal, 6, increment, modulus);
/* 165 */       long l = cal.getTimeInMillis();
/* 166 */       cal.add(6, -1);
/* 167 */       this.nextFileTime = cal.getTimeInMillis();
/* 168 */       return debugGetNextTime(l);
/*     */     } 
/* 170 */     cal.set(11, currentCal.get(11));
/* 171 */     if (this.frequency == RolloverFrequency.HOURLY) {
/* 172 */       increment(cal, 11, increment, modulus);
/* 173 */       long l = cal.getTimeInMillis();
/* 174 */       cal.add(11, -1);
/* 175 */       this.nextFileTime = cal.getTimeInMillis();
/* 176 */       return debugGetNextTime(l);
/*     */     } 
/* 178 */     cal.set(12, currentCal.get(12));
/* 179 */     if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
/* 180 */       increment(cal, 12, increment, modulus);
/* 181 */       long l = cal.getTimeInMillis();
/* 182 */       cal.add(12, -1);
/* 183 */       this.nextFileTime = cal.getTimeInMillis();
/* 184 */       return debugGetNextTime(l);
/*     */     } 
/* 186 */     cal.set(13, currentCal.get(13));
/* 187 */     if (this.frequency == RolloverFrequency.EVERY_SECOND) {
/* 188 */       increment(cal, 13, increment, modulus);
/* 189 */       long l = cal.getTimeInMillis();
/* 190 */       cal.add(13, -1);
/* 191 */       this.nextFileTime = cal.getTimeInMillis();
/* 192 */       return debugGetNextTime(l);
/*     */     } 
/* 194 */     cal.set(14, currentCal.get(14));
/* 195 */     increment(cal, 14, increment, modulus);
/* 196 */     long nextTime = cal.getTimeInMillis();
/* 197 */     cal.add(14, -1);
/* 198 */     this.nextFileTime = cal.getTimeInMillis();
/* 199 */     return debugGetNextTime(nextTime);
/*     */   }
/*     */   
/*     */   public void updateTime() {
/* 203 */     this.prevFileTime = this.nextFileTime;
/*     */   }
/*     */   
/*     */   private long debugGetNextTime(long nextTime) {
/* 207 */     if (LOGGER.isTraceEnabled()) {
/* 208 */       LOGGER.trace("PatternProcessor.getNextTime returning {}, nextFileTime={}, prevFileTime={}, current={}, freq={}", format(nextTime), format(this.nextFileTime), format(this.prevFileTime), format(System.currentTimeMillis()), this.frequency);
/*     */     }
/*     */     
/* 211 */     return nextTime;
/*     */   }
/*     */   
/*     */   private String format(long time) {
/* 215 */     return (new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS")).format(new Date(time));
/*     */   }
/*     */   
/*     */   private void increment(Calendar cal, int type, int increment, boolean modulate) {
/* 219 */     int interval = modulate ? (increment - cal.get(type) % increment) : increment;
/* 220 */     cal.add(type, interval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void formatFileName(StringBuilder buf, boolean useCurrentTime, Object obj) {
/* 229 */     long time = useCurrentTime ? this.currentFileTime : this.prevFileTime;
/* 230 */     if (time == 0L) {
/* 231 */       time = System.currentTimeMillis();
/*     */     }
/* 233 */     formatFileName(buf, new Object[] { new Date(time), obj });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void formatFileName(StrSubstitutor subst, StringBuilder buf, Object obj) {
/* 243 */     formatFileName(subst, buf, false, obj);
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
/*     */   public final void formatFileName(StrSubstitutor subst, StringBuilder buf, boolean useCurrentTime, Object obj) {
/* 256 */     long time = (useCurrentTime && this.currentFileTime != 0L) ? this.currentFileTime : ((this.prevFileTime != 0L) ? this.prevFileTime : System.currentTimeMillis());
/*     */     
/* 258 */     formatFileName(buf, new Object[] { new Date(time), obj });
/* 259 */     Log4jLogEvent log4jLogEvent = (new Log4jLogEvent.Builder()).setTimeMillis(time).build();
/* 260 */     String fileName = subst.replace((LogEvent)log4jLogEvent, buf);
/* 261 */     buf.setLength(0);
/* 262 */     buf.append(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void formatFileName(StringBuilder buf, Object... objects) {
/* 271 */     for (int i = 0; i < this.patternConverters.length; i++) {
/* 272 */       int fieldStart = buf.length();
/* 273 */       this.patternConverters[i].format(buf, objects);
/*     */       
/* 275 */       if (this.patternFields[i] != null) {
/* 276 */         this.patternFields[i].format(fieldStart, buf);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private RolloverFrequency calculateFrequency(String pattern) {
/* 282 */     if (patternContains(pattern, 'S')) {
/* 283 */       return RolloverFrequency.EVERY_MILLISECOND;
/*     */     }
/* 285 */     if (patternContains(pattern, 's')) {
/* 286 */       return RolloverFrequency.EVERY_SECOND;
/*     */     }
/* 288 */     if (patternContains(pattern, 'm')) {
/* 289 */       return RolloverFrequency.EVERY_MINUTE;
/*     */     }
/* 291 */     if (patternContains(pattern, HOUR_CHARS)) {
/* 292 */       return RolloverFrequency.HOURLY;
/*     */     }
/* 294 */     if (patternContains(pattern, DAY_CHARS)) {
/* 295 */       return RolloverFrequency.DAILY;
/*     */     }
/* 297 */     if (patternContains(pattern, WEEK_CHARS)) {
/* 298 */       return RolloverFrequency.WEEKLY;
/*     */     }
/* 300 */     if (patternContains(pattern, 'M')) {
/* 301 */       return RolloverFrequency.MONTHLY;
/*     */     }
/* 303 */     if (patternContains(pattern, 'y')) {
/* 304 */       return RolloverFrequency.ANNUALLY;
/*     */     }
/* 306 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private PatternParser createPatternParser() {
/* 311 */     return new PatternParser(null, "FileConverter", null);
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char... chars) {
/* 315 */     for (char character : chars) {
/* 316 */       if (patternContains(pattern, character)) {
/* 317 */         return true;
/*     */       }
/*     */     } 
/* 320 */     return false;
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char character) {
/* 324 */     return (pattern.indexOf(character) >= 0);
/*     */   }
/*     */   
/*     */   public RolloverFrequency getFrequency() {
/* 328 */     return this.frequency;
/*     */   }
/*     */   
/*     */   public long getNextFileTime() {
/* 332 */     return this.nextFileTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\PatternProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */