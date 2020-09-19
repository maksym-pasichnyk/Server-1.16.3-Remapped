/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum AnsiEscape
/*     */ {
/*  42 */   CSI("\033["),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   SUFFIX("m"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   SEPARATOR(";"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   NORMAL("0"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   BRIGHT("1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   DIM("2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   UNDERLINE("3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   BLINK("5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   REVERSE("7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   HIDDEN("8"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   FG_BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   FG_RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   FG_GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   FG_YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   FG_BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   FG_MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   FG_CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   FG_WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   FG_DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   BG_BLACK("40"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   BG_RED("41"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   BG_GREEN("42"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   BG_YELLOW("43"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   BG_BLUE("44"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   BG_MAGENTA("45"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   BG_CYAN("46"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   BG_WHITE("47"); private static final String DEFAULT_STYLE;
/*     */   static {
/* 219 */     DEFAULT_STYLE = CSI.getCode() + SUFFIX.getCode();
/*     */   }
/*     */   private final String code;
/*     */   
/*     */   AnsiEscape(String code) {
/* 224 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDefaultStyle() {
/* 233 */     return DEFAULT_STYLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 242 */     return this.code;
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
/*     */   public static Map<String, String> createMap(String values, String[] dontEscapeKeys) {
/* 266 */     return createMap(values.split(Patterns.COMMA_SEPARATOR), dontEscapeKeys);
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
/*     */   public static Map<String, String> createMap(String[] values, String[] dontEscapeKeys) {
/* 292 */     String[] sortedIgnoreKeys = (dontEscapeKeys != null) ? (String[])dontEscapeKeys.clone() : new String[0];
/* 293 */     Arrays.sort((Object[])sortedIgnoreKeys);
/* 294 */     Map<String, String> map = new HashMap<>();
/* 295 */     for (String string : values) {
/* 296 */       String[] keyValue = string.split(Patterns.toWhitespaceSeparator("="));
/* 297 */       if (keyValue.length > 1) {
/* 298 */         String key = keyValue[0].toUpperCase(Locale.ENGLISH);
/* 299 */         String value = keyValue[1];
/* 300 */         boolean escape = (Arrays.binarySearch((Object[])sortedIgnoreKeys, key) < 0);
/* 301 */         map.put(key, escape ? createSequence(value.split("\\s")) : value);
/*     */       } 
/*     */     } 
/* 304 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createSequence(String... names) {
/* 315 */     if (names == null) {
/* 316 */       return getDefaultStyle();
/*     */     }
/* 318 */     StringBuilder sb = new StringBuilder(CSI.getCode());
/* 319 */     boolean first = true;
/* 320 */     for (String name : names) {
/*     */       try {
/* 322 */         AnsiEscape escape = (AnsiEscape)EnglishEnums.valueOf(AnsiEscape.class, name.trim());
/* 323 */         if (!first) {
/* 324 */           sb.append(SEPARATOR.getCode());
/*     */         }
/* 326 */         first = false;
/* 327 */         sb.append(escape.getCode());
/* 328 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 332 */     sb.append(SUFFIX.getCode());
/* 333 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\AnsiEscape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */