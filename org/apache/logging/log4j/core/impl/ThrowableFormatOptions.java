/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.logging.log4j.core.pattern.JAnsiTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public final class ThrowableFormatOptions
/*     */ {
/*     */   private static final int DEFAULT_LINES = 2147483647;
/*  41 */   protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FULL = "full";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NONE = "none";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SHORT = "short";
/*     */ 
/*     */ 
/*     */   
/*     */   private final TextRenderer textRenderer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int lines;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String separator;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<String> ignorePackages;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CLASS_NAME = "short.className";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String METHOD_NAME = "short.methodName";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LINE_NUMBER = "short.lineNumber";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FILE_NAME = "short.fileName";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MESSAGE = "short.message";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LOCALIZED_MESSAGE = "short.localizedMessage";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(int lines, String separator, List<String> ignorePackages, TextRenderer textRenderer) {
/*  99 */     this.lines = lines;
/* 100 */     this.separator = (separator == null) ? Strings.LINE_SEPARATOR : separator;
/* 101 */     this.ignorePackages = ignorePackages;
/* 102 */     this.textRenderer = (textRenderer == null) ? (TextRenderer)PlainTextRenderer.getInstance() : textRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(List<String> packages) {
/* 112 */     this(2147483647, null, packages, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions() {
/* 119 */     this(2147483647, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLines() {
/* 128 */     return this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 137 */     return this.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextRenderer getTextRenderer() {
/* 146 */     return this.textRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getIgnorePackages() {
/* 155 */     return this.ignorePackages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean allLines() {
/* 164 */     return (this.lines == Integer.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean anyLines() {
/* 173 */     return (this.lines > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int minLines(int maxLines) {
/* 184 */     return (this.lines > maxLines) ? maxLines : this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPackages() {
/* 193 */     return (this.ignorePackages != null && !this.ignorePackages.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     StringBuilder s = new StringBuilder();
/* 202 */     s.append('{').append(allLines() ? "full" : ((this.lines == 2) ? "short" : (anyLines() ? String.valueOf(this.lines) : "none"))).append('}');
/*     */ 
/*     */     
/* 205 */     s.append("{separator(").append(this.separator).append(")}");
/* 206 */     if (hasPackages()) {
/* 207 */       s.append("{filters(");
/* 208 */       for (String p : this.ignorePackages) {
/* 209 */         s.append(p).append(',');
/*     */       }
/* 211 */       s.deleteCharAt(s.length() - 1);
/* 212 */       s.append(")}");
/*     */     } 
/* 214 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThrowableFormatOptions newInstance(String[] options) {
/*     */     JAnsiTextRenderer jAnsiTextRenderer;
/* 225 */     if (options == null || options.length == 0) {
/* 226 */       return DEFAULT;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     if (options.length == 1 && Strings.isNotEmpty(options[0])) {
/* 235 */       String[] opts = options[0].split(Patterns.COMMA_SEPARATOR, 2);
/* 236 */       String first = opts[0].trim();
/* 237 */       try (Scanner scanner = new Scanner(first)) {
/* 238 */         if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first.equalsIgnoreCase("none") || scanner.hasNextInt()))
/*     */         {
/* 240 */           options = new String[] { first, opts[1].trim() };
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     int lines = DEFAULT.lines;
/* 246 */     String separator = DEFAULT.separator;
/* 247 */     List<String> packages = DEFAULT.ignorePackages;
/* 248 */     TextRenderer ansiRenderer = DEFAULT.textRenderer;
/* 249 */     for (String rawOption : options) {
/* 250 */       if (rawOption != null) {
/* 251 */         String option = rawOption.trim();
/* 252 */         if (!option.isEmpty())
/*     */         {
/* 254 */           if (option.startsWith("separator(") && option.endsWith(")")) {
/* 255 */             separator = option.substring("separator(".length(), option.length() - 1);
/* 256 */           } else if (option.startsWith("filters(") && option.endsWith(")")) {
/* 257 */             String filterStr = option.substring("filters(".length(), option.length() - 1);
/* 258 */             if (filterStr.length() > 0) {
/* 259 */               String[] array = filterStr.split(Patterns.COMMA_SEPARATOR);
/* 260 */               if (array.length > 0) {
/* 261 */                 packages = new ArrayList<>(array.length);
/* 262 */                 for (String token : array) {
/* 263 */                   token = token.trim();
/* 264 */                   if (token.length() > 0) {
/* 265 */                     packages.add(token);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/* 270 */           } else if (option.equalsIgnoreCase("none")) {
/* 271 */             lines = 0;
/* 272 */           } else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option.equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option.equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option.equalsIgnoreCase("short.localizedMessage")) {
/*     */ 
/*     */ 
/*     */             
/* 276 */             lines = 2;
/* 277 */           } else if ((option.startsWith("ansi(") && option.endsWith(")")) || option.equals("ansi")) {
/* 278 */             if (Loader.isJansiAvailable()) {
/* 279 */               String styleMapStr = option.equals("ansi") ? "" : option.substring("ansi(".length(), option.length() - 1);
/*     */               
/* 281 */               jAnsiTextRenderer = new JAnsiTextRenderer(new String[] { null, styleMapStr }, JAnsiTextRenderer.DefaultExceptionStyleMap);
/*     */             } else {
/*     */               
/* 284 */               StatusLogger.getLogger().warn("You requested ANSI exception rendering but JANSI is not on the classpath. Please see https://logging.apache.org/log4j/2.x/runtime-dependencies.html");
/*     */             }
/*     */           
/* 287 */           } else if (!option.equalsIgnoreCase("full")) {
/* 288 */             lines = Integer.parseInt(option);
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 292 */     return new ThrowableFormatOptions(lines, separator, packages, (TextRenderer)jAnsiTextRenderer);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ThrowableFormatOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */