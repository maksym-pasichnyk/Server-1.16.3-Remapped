/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.core.util.SystemNanoClock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternParser
/*     */ {
/*     */   static final String DISABLE_ANSI = "disableAnsi";
/*     */   static final String NO_CONSOLE_NO_ANSI = "noConsoleNoAnsi";
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   
/*     */   private enum ParserState
/*     */   {
/*  59 */     LITERAL_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     CONVERTER_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     DOT_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     MIN_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     MAX_STATE;
/*     */   }
/*     */   
/*  82 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final int BUF_SIZE = 32;
/*     */ 
/*     */   
/*     */   private static final int DECIMAL = 10;
/*     */ 
/*     */   
/*     */   private final Configuration config;
/*     */ 
/*     */   
/*     */   private final Map<String, Class<PatternConverter>> converterRules;
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternParser(String converterKey) {
/*  99 */     this(null, converterKey, null, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expected) {
/* 113 */     this(config, converterKey, expected, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expectedClass, Class<?> filterClass) {
/* 130 */     this.config = config;
/* 131 */     PluginManager manager = new PluginManager(converterKey);
/* 132 */     manager.collectPlugins((config == null) ? null : config.getPluginPackages());
/* 133 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 134 */     Map<String, Class<PatternConverter>> converters = new LinkedHashMap<>();
/*     */     
/* 136 */     for (PluginType<?> type : plugins.values()) {
/*     */       
/*     */       try {
/* 139 */         Class<PatternConverter> clazz = type.getPluginClass();
/* 140 */         if (filterClass != null && !filterClass.isAssignableFrom(clazz)) {
/*     */           continue;
/*     */         }
/* 143 */         ConverterKeys keys = clazz.<ConverterKeys>getAnnotation(ConverterKeys.class);
/* 144 */         if (keys != null) {
/* 145 */           for (String key : keys.value()) {
/* 146 */             if (converters.containsKey(key)) {
/* 147 */               LOGGER.warn("Converter key '{}' is already mapped to '{}'. Sorry, Dave, I can't let you do that! Ignoring plugin [{}].", key, converters.get(key), clazz);
/*     */             }
/*     */             else {
/*     */               
/* 151 */               converters.put(key, clazz);
/*     */             } 
/*     */           } 
/*     */         }
/* 155 */       } catch (Exception ex) {
/* 156 */         LOGGER.error("Error processing plugin " + type.getElementName(), ex);
/*     */       } 
/*     */     } 
/* 159 */     this.converterRules = converters;
/*     */   }
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern) {
/* 163 */     return parse(pattern, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
/* 168 */     return parse(pattern, alwaysWriteExceptions, false, noConsoleNoAnsi);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi) {
/* 173 */     List<PatternFormatter> list = new ArrayList<>();
/* 174 */     List<PatternConverter> converters = new ArrayList<>();
/* 175 */     List<FormattingInfo> fields = new ArrayList<>();
/*     */     
/* 177 */     parse(pattern, converters, fields, disableAnsi, noConsoleNoAnsi, true);
/*     */     
/* 179 */     Iterator<FormattingInfo> fieldIter = fields.iterator();
/* 180 */     boolean handlesThrowable = false;
/*     */     
/* 182 */     for (PatternConverter converter : converters) {
/* 183 */       LogEventPatternConverter pc; FormattingInfo field; if (converter instanceof NanoTimePatternConverter)
/*     */       {
/*     */         
/* 186 */         if (this.config != null) {
/* 187 */           this.config.setNanoClock((NanoClock)new SystemNanoClock());
/*     */         }
/*     */       }
/*     */       
/* 191 */       if (converter instanceof LogEventPatternConverter) {
/* 192 */         pc = (LogEventPatternConverter)converter;
/* 193 */         handlesThrowable |= pc.handlesThrowable();
/*     */       } else {
/* 195 */         pc = new LiteralPatternConverter(this.config, "", true);
/*     */       } 
/*     */ 
/*     */       
/* 199 */       if (fieldIter.hasNext()) {
/* 200 */         field = fieldIter.next();
/*     */       } else {
/* 202 */         field = FormattingInfo.getDefault();
/*     */       } 
/* 204 */       list.add(new PatternFormatter(pc, field));
/*     */     } 
/* 206 */     if (alwaysWriteExceptions && !handlesThrowable) {
/* 207 */       LogEventPatternConverter pc = ExtendedThrowablePatternConverter.newInstance(null);
/* 208 */       list.add(new PatternFormatter(pc, FormattingInfo.getDefault()));
/*     */     } 
/* 210 */     return list;
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
/*     */   private static int extractConverter(char lastChar, String pattern, int start, StringBuilder convBuf, StringBuilder currentLiteral) {
/* 238 */     int i = start;
/* 239 */     convBuf.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     if (!Character.isUnicodeIdentifierStart(lastChar)) {
/* 247 */       return i;
/*     */     }
/*     */     
/* 250 */     convBuf.append(lastChar);
/*     */     
/* 252 */     while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
/* 253 */       convBuf.append(pattern.charAt(i));
/* 254 */       currentLiteral.append(pattern.charAt(i));
/* 255 */       i++;
/*     */     } 
/*     */     
/* 258 */     return i;
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
/*     */   private static int extractOptions(String pattern, int start, List<String> options) {
/* 273 */     int i = start;
/* 274 */     while (i < pattern.length() && pattern.charAt(i) == '{') {
/* 275 */       int end, begin = i++;
/*     */       
/* 277 */       int depth = 0;
/*     */       do {
/* 279 */         end = pattern.indexOf('}', i);
/* 280 */         if (end == -1) {
/*     */           break;
/*     */         }
/* 283 */         int next = pattern.indexOf("{", i);
/* 284 */         if (next != -1 && next < end) {
/* 285 */           i = end + 1;
/* 286 */           depth++;
/* 287 */         } else if (depth > 0) {
/* 288 */           depth--;
/*     */         } 
/* 290 */       } while (depth > 0);
/*     */       
/* 292 */       if (end == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 296 */       String r = pattern.substring(begin + 1, end);
/* 297 */       options.add(r);
/* 298 */       i = end + 1;
/*     */     } 
/*     */     
/* 301 */     return i;
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
/*     */   public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 321 */     parse(pattern, patternConverters, formattingInfos, false, noConsoleNoAnsi, convertBackslashes);
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
/*     */   public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 343 */     Objects.requireNonNull(pattern, "pattern");
/*     */     
/* 345 */     StringBuilder currentLiteral = new StringBuilder(32);
/*     */     
/* 347 */     int patternLength = pattern.length();
/* 348 */     ParserState state = ParserState.LITERAL_STATE;
/*     */     
/* 350 */     int i = 0;
/* 351 */     FormattingInfo formattingInfo = FormattingInfo.getDefault();
/*     */     
/* 353 */     while (i < patternLength) {
/* 354 */       char c = pattern.charAt(i++);
/*     */       
/* 356 */       switch (state) {
/*     */ 
/*     */         
/*     */         case LITERAL_STATE:
/* 360 */           if (i == patternLength) {
/* 361 */             currentLiteral.append(c);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 366 */           if (c == '%') {
/*     */             
/* 368 */             switch (pattern.charAt(i)) {
/*     */               case '%':
/* 370 */                 currentLiteral.append(c);
/* 371 */                 i++;
/*     */                 continue;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 377 */             if (currentLiteral.length() != 0) {
/* 378 */               patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString(), convertBackslashes));
/*     */               
/* 380 */               formattingInfos.add(FormattingInfo.getDefault());
/*     */             } 
/*     */             
/* 383 */             currentLiteral.setLength(0);
/* 384 */             currentLiteral.append(c);
/* 385 */             state = ParserState.CONVERTER_STATE;
/* 386 */             formattingInfo = FormattingInfo.getDefault();
/*     */             continue;
/*     */           } 
/* 389 */           currentLiteral.append(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case CONVERTER_STATE:
/* 395 */           currentLiteral.append(c);
/*     */           
/* 397 */           switch (c) {
/*     */             case '-':
/* 399 */               formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate());
/*     */               continue;
/*     */ 
/*     */             
/*     */             case '.':
/* 404 */               state = ParserState.DOT_STATE;
/*     */               continue;
/*     */           } 
/*     */ 
/*     */           
/* 409 */           if (c >= '0' && c <= '9') {
/* 410 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate());
/*     */             
/* 412 */             state = ParserState.MIN_STATE; continue;
/*     */           } 
/* 414 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */ 
/*     */ 
/*     */           
/* 418 */           state = ParserState.LITERAL_STATE;
/* 419 */           formattingInfo = FormattingInfo.getDefault();
/* 420 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MIN_STATE:
/* 427 */           currentLiteral.append(c);
/*     */           
/* 429 */           if (c >= '0' && c <= '9') {
/*     */             
/* 431 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate()); continue;
/*     */           } 
/* 433 */           if (c == '.') {
/* 434 */             state = ParserState.DOT_STATE; continue;
/*     */           } 
/* 436 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */           
/* 438 */           state = ParserState.LITERAL_STATE;
/* 439 */           formattingInfo = FormattingInfo.getDefault();
/* 440 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case DOT_STATE:
/* 446 */           currentLiteral.append(c);
/* 447 */           switch (c) {
/*     */             case '-':
/* 449 */               formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength(), false);
/*     */               continue;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 455 */           if (c >= '0' && c <= '9') {
/* 456 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - 48, formattingInfo.isLeftTruncate());
/*     */             
/* 458 */             state = ParserState.MAX_STATE; continue;
/*     */           } 
/* 460 */           LOGGER.error("Error occurred in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */ 
/*     */           
/* 463 */           state = ParserState.LITERAL_STATE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MAX_STATE:
/* 470 */           currentLiteral.append(c);
/*     */           
/* 472 */           if (c >= '0' && c <= '9') {
/*     */             
/* 474 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + c - 48, formattingInfo.isLeftTruncate());
/*     */             continue;
/*     */           } 
/* 477 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */           
/* 479 */           state = ParserState.LITERAL_STATE;
/* 480 */           formattingInfo = FormattingInfo.getDefault();
/* 481 */           currentLiteral.setLength(0);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 489 */     if (currentLiteral.length() != 0) {
/* 490 */       patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString(), convertBackslashes));
/* 491 */       formattingInfos.add(FormattingInfo.getDefault());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PatternConverter createConverter(String converterId, StringBuilder currentLiteral, Map<String, Class<PatternConverter>> rules, List<String> options, boolean disableAnsi, boolean noConsoleNoAnsi) {
/* 516 */     String converterName = converterId;
/* 517 */     Class<PatternConverter> converterClass = null;
/*     */     
/* 519 */     if (rules == null) {
/* 520 */       LOGGER.error("Null rules for [" + converterId + ']');
/* 521 */       return null;
/*     */     } 
/* 523 */     for (int i = converterId.length(); i > 0 && converterClass == null; i--) {
/* 524 */       converterName = converterName.substring(0, i);
/* 525 */       converterClass = rules.get(converterName);
/*     */     } 
/*     */     
/* 528 */     if (converterClass == null) {
/* 529 */       LOGGER.error("Unrecognized format specifier [" + converterId + ']');
/* 530 */       return null;
/*     */     } 
/*     */     
/* 533 */     if (AnsiConverter.class.isAssignableFrom(converterClass)) {
/* 534 */       options.add("disableAnsi=" + disableAnsi);
/* 535 */       options.add("noConsoleNoAnsi=" + noConsoleNoAnsi);
/*     */     } 
/*     */ 
/*     */     
/* 539 */     Method[] methods = converterClass.getDeclaredMethods();
/* 540 */     Method newInstanceMethod = null;
/* 541 */     for (Method method : methods) {
/* 542 */       if (Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(converterClass) && method.getName().equals("newInstance"))
/*     */       {
/* 544 */         if (newInstanceMethod == null) {
/* 545 */           newInstanceMethod = method;
/* 546 */         } else if (method.getReturnType().equals(newInstanceMethod.getReturnType())) {
/* 547 */           LOGGER.error("Class " + converterClass + " cannot contain multiple static newInstance methods");
/* 548 */           return null;
/*     */         } 
/*     */       }
/*     */     } 
/* 552 */     if (newInstanceMethod == null) {
/* 553 */       LOGGER.error("Class " + converterClass + " does not contain a static newInstance method");
/* 554 */       return null;
/*     */     } 
/*     */     
/* 557 */     Class<?>[] parmTypes = newInstanceMethod.getParameterTypes();
/* 558 */     Object[] parms = (parmTypes.length > 0) ? new Object[parmTypes.length] : null;
/*     */     
/* 560 */     if (parms != null) {
/* 561 */       int j = 0;
/* 562 */       boolean errors = false;
/* 563 */       for (Class<?> clazz : parmTypes) {
/* 564 */         if (clazz.isArray() && clazz.getName().equals("[Ljava.lang.String;")) {
/* 565 */           String[] optionsArray = options.<String>toArray(new String[options.size()]);
/* 566 */           parms[j] = optionsArray;
/* 567 */         } else if (clazz.isAssignableFrom(Configuration.class)) {
/* 568 */           parms[j] = this.config;
/*     */         } else {
/* 570 */           LOGGER.error("Unknown parameter type " + clazz.getName() + " for static newInstance method of " + converterClass.getName());
/*     */           
/* 572 */           errors = true;
/*     */         } 
/* 574 */         j++;
/*     */       } 
/* 576 */       if (errors) {
/* 577 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 582 */       Object newObj = newInstanceMethod.invoke(null, parms);
/*     */       
/* 584 */       if (newObj instanceof PatternConverter) {
/* 585 */         currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
/*     */         
/* 587 */         return (PatternConverter)newObj;
/*     */       } 
/* 589 */       LOGGER.warn("Class {} does not extend PatternConverter.", converterClass.getName());
/* 590 */     } catch (Exception ex) {
/* 591 */       LOGGER.error("Error creating converter for " + converterId, ex);
/*     */     } 
/*     */     
/* 594 */     return null;
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
/*     */   private int finalizeConverter(char c, String pattern, int start, StringBuilder currentLiteral, FormattingInfo formattingInfo, Map<String, Class<PatternConverter>> rules, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 629 */     int i = start;
/* 630 */     StringBuilder convBuf = new StringBuilder();
/* 631 */     i = extractConverter(c, pattern, i, convBuf, currentLiteral);
/*     */     
/* 633 */     String converterId = convBuf.toString();
/*     */     
/* 635 */     List<String> options = new ArrayList<>();
/* 636 */     i = extractOptions(pattern, i, options);
/*     */     
/* 638 */     PatternConverter pc = createConverter(converterId, currentLiteral, rules, options, disableAnsi, noConsoleNoAnsi);
/*     */ 
/*     */     
/* 641 */     if (pc == null) {
/*     */       StringBuilder msg;
/*     */       
/* 644 */       if (Strings.isEmpty(converterId)) {
/* 645 */         msg = new StringBuilder("Empty conversion specifier starting at position ");
/*     */       } else {
/* 647 */         msg = new StringBuilder("Unrecognized conversion specifier [");
/* 648 */         msg.append(converterId);
/* 649 */         msg.append("] starting at position ");
/*     */       } 
/*     */       
/* 652 */       msg.append(Integer.toString(i));
/* 653 */       msg.append(" in conversion pattern.");
/*     */       
/* 655 */       LOGGER.error(msg.toString());
/*     */       
/* 657 */       patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString(), convertBackslashes));
/* 658 */       formattingInfos.add(FormattingInfo.getDefault());
/*     */     } else {
/* 660 */       patternConverters.add(pc);
/* 661 */       formattingInfos.add(formattingInfo);
/*     */       
/* 663 */       if (currentLiteral.length() > 0) {
/* 664 */         patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString(), convertBackslashes));
/*     */         
/* 666 */         formattingInfos.add(FormattingInfo.getDefault());
/*     */       } 
/*     */     } 
/*     */     
/* 670 */     currentLiteral.setLength(0);
/*     */     
/* 672 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\PatternParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */