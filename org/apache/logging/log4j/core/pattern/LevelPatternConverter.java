/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ @Plugin(name = "LevelPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"p", "level"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class LevelPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final String OPTION_LENGTH = "length";
/*     */   private static final String OPTION_LOWER = "lowerCase";
/*  42 */   private static final LevelPatternConverter INSTANCE = new LevelPatternConverter(null);
/*     */ 
/*     */   
/*     */   private final Map<Level, String> levelMap;
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelPatternConverter(Map<Level, String> map) {
/*  50 */     super("Level", "level");
/*  51 */     this.levelMap = map;
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
/*     */   public static LevelPatternConverter newInstance(String[] options) {
/*  63 */     if (options == null || options.length == 0) {
/*  64 */       return INSTANCE;
/*     */     }
/*  66 */     Map<Level, String> levelMap = new HashMap<>();
/*  67 */     int length = Integer.MAX_VALUE;
/*  68 */     boolean lowerCase = false;
/*  69 */     String[] definitions = options[0].split(Patterns.COMMA_SEPARATOR);
/*  70 */     for (String def : definitions) {
/*  71 */       String[] pair = def.split("=");
/*  72 */       if (pair == null || pair.length != 2) {
/*  73 */         LOGGER.error("Invalid option {}", def);
/*     */       } else {
/*     */         
/*  76 */         String key = pair[0].trim();
/*  77 */         String value = pair[1].trim();
/*  78 */         if ("length".equalsIgnoreCase(key)) {
/*  79 */           length = Integer.parseInt(value);
/*  80 */         } else if ("lowerCase".equalsIgnoreCase(key)) {
/*  81 */           lowerCase = Boolean.parseBoolean(value);
/*     */         } else {
/*  83 */           Level level = Level.toLevel(key, null);
/*  84 */           if (level == null) {
/*  85 */             LOGGER.error("Invalid Level {}", key);
/*     */           } else {
/*  87 */             levelMap.put(level, value);
/*     */           } 
/*     */         } 
/*     */       } 
/*  91 */     }  if (levelMap.isEmpty() && length == Integer.MAX_VALUE && !lowerCase) {
/*  92 */       return INSTANCE;
/*     */     }
/*  94 */     for (Level level : Level.values()) {
/*  95 */       if (!levelMap.containsKey(level)) {
/*  96 */         String left = left(level, length);
/*  97 */         levelMap.put(level, lowerCase ? left.toLowerCase(Locale.US) : left);
/*     */       } 
/*     */     } 
/* 100 */     return new LevelPatternConverter(levelMap);
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
/*     */   private static String left(Level level, int length) {
/* 114 */     String string = level.toString();
/* 115 */     if (length >= string.length()) {
/* 116 */       return string;
/*     */     }
/* 118 */     return string.substring(0, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 126 */     output.append((this.levelMap == null) ? event.getLevel().toString() : this.levelMap.get(event.getLevel()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStyleClass(Object e) {
/* 134 */     if (e instanceof LogEvent) {
/* 135 */       return "level " + ((LogEvent)e).getLevel().name().toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */     
/* 138 */     return "level";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\LevelPatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */