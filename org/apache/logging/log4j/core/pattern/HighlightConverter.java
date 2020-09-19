/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "highlight", category = "Converter")
/*     */ @ConverterKeys({"highlight"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class HighlightConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*  83 */   private static final Map<Level, String> DEFAULT_STYLES = new HashMap<>();
/*     */   
/*  85 */   private static final Map<Level, String> LOGBACK_STYLES = new HashMap<>();
/*     */   
/*     */   private static final String STYLE_KEY = "STYLE";
/*     */   
/*     */   private static final String STYLE_KEY_DEFAULT = "DEFAULT";
/*     */   
/*     */   private static final String STYLE_KEY_LOGBACK = "LOGBACK";
/*     */   
/*  93 */   private static final Map<String, Map<Level, String>> STYLES = new HashMap<>();
/*     */   private final Map<Level, String> levelStyles;
/*     */   
/*     */   static {
/*  97 */     DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  98 */     DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  99 */     DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "YELLOW" }));
/* 100 */     DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "GREEN" }));
/* 101 */     DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence(new String[] { "CYAN" }));
/* 102 */     DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence(new String[] { "BLACK" }));
/*     */     
/* 104 */     LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED" }));
/* 105 */     LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/* 106 */     LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "RED" }));
/* 107 */     LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "BLUE" }));
/* 108 */     LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[])null));
/* 109 */     LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[])null));
/*     */     
/* 111 */     STYLES.put("DEFAULT", DEFAULT_STYLES);
/* 112 */     STYLES.put("LOGBACK", LOGBACK_STYLES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean noAnsi;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String defaultStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<Level, String> createLevelStyleMap(String[] options) {
/* 144 */     if (options.length < 2) {
/* 145 */       return DEFAULT_STYLES;
/*     */     }
/*     */     
/* 148 */     String string = options[1].replaceAll("disableAnsi=(true|false)", "").replaceAll("noConsoleNoAnsi=(true|false)", "");
/*     */ 
/*     */ 
/*     */     
/* 152 */     Map<String, String> styles = AnsiEscape.createMap(string, new String[] { "STYLE" });
/* 153 */     Map<Level, String> levelStyles = new HashMap<>(DEFAULT_STYLES);
/* 154 */     for (Map.Entry<String, String> entry : styles.entrySet()) {
/* 155 */       String key = ((String)entry.getKey()).toUpperCase(Locale.ENGLISH);
/* 156 */       String value = entry.getValue();
/* 157 */       if ("STYLE".equalsIgnoreCase(key)) {
/* 158 */         Map<Level, String> enumMap = STYLES.get(value.toUpperCase(Locale.ENGLISH));
/* 159 */         if (enumMap == null) {
/* 160 */           LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(STYLES.keySet().toArray()));
/*     */           continue;
/*     */         } 
/* 163 */         levelStyles.putAll(enumMap);
/*     */         continue;
/*     */       } 
/* 166 */       Level level = Level.toLevel(key);
/* 167 */       if (level == null) {
/* 168 */         LOGGER.error("Unknown level name: " + key + ". Use one of " + Arrays.toString(DEFAULT_STYLES.keySet().toArray()));
/*     */         continue;
/*     */       } 
/* 171 */       levelStyles.put(level, value);
/*     */     } 
/*     */ 
/*     */     
/* 175 */     return levelStyles;
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
/*     */   public static HighlightConverter newInstance(Configuration config, String[] options) {
/* 187 */     if (options.length < 1) {
/* 188 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/* 189 */       return null;
/*     */     } 
/* 191 */     if (options[0] == null) {
/* 192 */       LOGGER.error("No pattern supplied on style");
/* 193 */       return null;
/*     */     } 
/* 195 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 196 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 197 */     boolean disableAnsi = Arrays.toString((Object[])options).contains("disableAnsi=true");
/* 198 */     boolean noConsoleNoAnsi = Arrays.toString((Object[])options).contains("noConsoleNoAnsi=true");
/* 199 */     boolean hideAnsi = (disableAnsi || (noConsoleNoAnsi && System.console() == null));
/* 200 */     return new HighlightConverter(formatters, createLevelStyleMap(options), hideAnsi);
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
/*     */   private HighlightConverter(List<PatternFormatter> patternFormatters, Map<Level, String> levelStyles, boolean noAnsi) {
/* 220 */     super("style", "style");
/* 221 */     this.patternFormatters = patternFormatters;
/* 222 */     this.levelStyles = levelStyles;
/* 223 */     this.defaultStyle = AnsiEscape.getDefaultStyle();
/* 224 */     this.noAnsi = noAnsi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 232 */     int start = 0;
/* 233 */     int end = 0;
/* 234 */     if (!this.noAnsi) {
/* 235 */       start = toAppendTo.length();
/* 236 */       toAppendTo.append(this.levelStyles.get(event.getLevel()));
/* 237 */       end = toAppendTo.length();
/*     */     } 
/*     */ 
/*     */     
/* 241 */     for (int i = 0, size = this.patternFormatters.size(); i < size; i++) {
/* 242 */       ((PatternFormatter)this.patternFormatters.get(i)).format(event, toAppendTo);
/*     */     }
/*     */ 
/*     */     
/* 246 */     boolean empty = (toAppendTo.length() == end);
/* 247 */     if (!this.noAnsi) {
/* 248 */       if (empty) {
/* 249 */         toAppendTo.setLength(start);
/*     */       } else {
/* 251 */         toAppendTo.append(this.defaultStyle);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 258 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 259 */       if (formatter.handlesThrowable()) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/* 263 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\HighlightConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */