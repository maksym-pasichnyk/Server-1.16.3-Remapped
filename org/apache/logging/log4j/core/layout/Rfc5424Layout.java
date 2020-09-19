/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.TlsSyslogFrame;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataId;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ @Plugin(name = "Rfc5424Layout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class Rfc5424Layout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
/*     */   public static final String DEFAULT_ID = "Audit";
/*  78 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Pattern PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
/*     */   
/*     */   public static final String DEFAULT_MDCID = "mdc";
/*     */   
/*     */   private static final String LF = "\n";
/*     */   
/*     */   private static final int TWO_DIGITS = 10;
/*     */   
/*     */   private static final int THREE_DIGITS = 100;
/*     */   
/*     */   private static final int MILLIS_PER_MINUTE = 60000;
/*     */   
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */   private static final String COMPONENT_KEY = "RFC5424-Converter";
/*     */   private final Facility facility;
/*     */   private final String defaultId;
/*     */   private final int enterpriseNumber;
/*     */   private final boolean includeMdc;
/*     */   private final String mdcId;
/*     */   private final StructuredDataId mdcSdId;
/*     */   private final String localHostName;
/*     */   private final String appName;
/*     */   private final String messageId;
/*     */   private final String configName;
/*     */   private final String mdcPrefix;
/*     */   private final String eventPrefix;
/*     */   private final List<String> mdcExcludes;
/*     */   private final List<String> mdcIncludes;
/*     */   private final List<String> mdcRequired;
/*     */   private final ListChecker listChecker;
/* 112 */   private final ListChecker noopChecker = new NoopChecker();
/*     */   
/*     */   private final boolean includeNewLine;
/*     */   private final String escapeNewLine;
/*     */   private final boolean useTlsMessageFormat;
/* 117 */   private long lastTimestamp = -1L;
/*     */   
/*     */   private String timestamppStr;
/*     */   
/*     */   private final List<PatternFormatter> exceptionFormatters;
/*     */   
/*     */   private final Map<String, FieldFormatter> fieldFormatters;
/*     */   
/*     */   private final String procId;
/*     */ 
/*     */   
/*     */   private Rfc5424Layout(Configuration config, Facility facility, String id, int ein, boolean includeMDC, boolean includeNL, String escapeNL, String mdcId, String mdcPrefix, String eventPrefix, String appName, String messageId, String excludes, String includes, String required, Charset charset, String exceptionPattern, boolean useTLSMessageFormat, LoggerFields[] loggerFields) {
/* 129 */     super(charset);
/* 130 */     PatternParser exceptionParser = createPatternParser(config, (Class)ThrowablePatternConverter.class);
/* 131 */     this.exceptionFormatters = (exceptionPattern == null) ? null : exceptionParser.parse(exceptionPattern);
/* 132 */     this.facility = facility;
/* 133 */     this.defaultId = (id == null) ? "Audit" : id;
/* 134 */     this.enterpriseNumber = ein;
/* 135 */     this.includeMdc = includeMDC;
/* 136 */     this.includeNewLine = includeNL;
/* 137 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
/* 138 */     this.mdcId = mdcId;
/* 139 */     this.mdcSdId = new StructuredDataId(mdcId, this.enterpriseNumber, null, null);
/* 140 */     this.mdcPrefix = mdcPrefix;
/* 141 */     this.eventPrefix = eventPrefix;
/* 142 */     this.appName = appName;
/* 143 */     this.messageId = messageId;
/* 144 */     this.useTlsMessageFormat = useTLSMessageFormat;
/* 145 */     this.localHostName = NetUtils.getLocalHostname();
/* 146 */     ListChecker c = null;
/* 147 */     if (excludes != null) {
/* 148 */       String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
/* 149 */       if (array.length > 0) {
/* 150 */         c = new ExcludeChecker();
/* 151 */         this.mdcExcludes = new ArrayList<>(array.length);
/* 152 */         for (String str : array) {
/* 153 */           this.mdcExcludes.add(str.trim());
/*     */         }
/*     */       } else {
/* 156 */         this.mdcExcludes = null;
/*     */       } 
/*     */     } else {
/* 159 */       this.mdcExcludes = null;
/*     */     } 
/* 161 */     if (includes != null) {
/* 162 */       String[] array = includes.split(Patterns.COMMA_SEPARATOR);
/* 163 */       if (array.length > 0) {
/* 164 */         c = new IncludeChecker();
/* 165 */         this.mdcIncludes = new ArrayList<>(array.length);
/* 166 */         for (String str : array) {
/* 167 */           this.mdcIncludes.add(str.trim());
/*     */         }
/*     */       } else {
/* 170 */         this.mdcIncludes = null;
/*     */       } 
/*     */     } else {
/* 173 */       this.mdcIncludes = null;
/*     */     } 
/* 175 */     if (required != null) {
/* 176 */       String[] array = required.split(Patterns.COMMA_SEPARATOR);
/* 177 */       if (array.length > 0) {
/* 178 */         this.mdcRequired = new ArrayList<>(array.length);
/* 179 */         for (String str : array) {
/* 180 */           this.mdcRequired.add(str.trim());
/*     */         }
/*     */       } else {
/* 183 */         this.mdcRequired = null;
/*     */       } 
/*     */     } else {
/*     */       
/* 187 */       this.mdcRequired = null;
/*     */     } 
/* 189 */     this.listChecker = (c != null) ? c : this.noopChecker;
/* 190 */     String name = (config == null) ? null : config.getName();
/* 191 */     this.configName = Strings.isNotEmpty(name) ? name : null;
/* 192 */     this.fieldFormatters = createFieldFormatters(loggerFields, config);
/*     */     
/* 194 */     this.procId = "-";
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, FieldFormatter> createFieldFormatters(LoggerFields[] loggerFields, Configuration config) {
/* 199 */     Map<String, FieldFormatter> sdIdMap = new HashMap<>((loggerFields == null) ? 0 : loggerFields.length);
/* 200 */     if (loggerFields != null) {
/* 201 */       for (LoggerFields loggerField : loggerFields) {
/* 202 */         StructuredDataId key = (loggerField.getSdId() == null) ? this.mdcSdId : loggerField.getSdId();
/* 203 */         Map<String, List<PatternFormatter>> sdParams = new HashMap<>();
/* 204 */         Map<String, String> fields = loggerField.getMap();
/* 205 */         if (!fields.isEmpty()) {
/* 206 */           PatternParser fieldParser = createPatternParser(config, (Class<? extends PatternConverter>)null);
/*     */           
/* 208 */           for (Map.Entry<String, String> entry : fields.entrySet()) {
/* 209 */             List<PatternFormatter> formatters = fieldParser.parse(entry.getValue());
/* 210 */             sdParams.put(entry.getKey(), formatters);
/*     */           } 
/* 212 */           FieldFormatter fieldFormatter = new FieldFormatter(sdParams, loggerField.getDiscardIfAllFieldsAreEmpty());
/*     */           
/* 214 */           sdIdMap.put(key.toString(), fieldFormatter);
/*     */         } 
/*     */       } 
/*     */     }
/* 218 */     return (sdIdMap.size() > 0) ? sdIdMap : null;
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
/*     */   private static PatternParser createPatternParser(Configuration config, Class<? extends PatternConverter> filterClass) {
/* 230 */     if (config == null) {
/* 231 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
/*     */     }
/* 233 */     PatternParser parser = (PatternParser)config.getComponent("RFC5424-Converter");
/* 234 */     if (parser == null) {
/* 235 */       parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
/* 236 */       config.addComponent("RFC5424-Converter", parser);
/* 237 */       parser = (PatternParser)config.getComponent("RFC5424-Converter");
/*     */     } 
/* 239 */     return parser;
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
/*     */   public Map<String, String> getContentFormat() {
/* 253 */     Map<String, String> result = new HashMap<>();
/* 254 */     result.put("structured", "true");
/* 255 */     result.put("formatType", "RFC5424");
/* 256 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 267 */     StringBuilder buf = getStringBuilder();
/* 268 */     appendPriority(buf, event.getLevel());
/* 269 */     appendTimestamp(buf, event.getTimeMillis());
/* 270 */     appendSpace(buf);
/* 271 */     appendHostName(buf);
/* 272 */     appendSpace(buf);
/* 273 */     appendAppName(buf);
/* 274 */     appendSpace(buf);
/* 275 */     appendProcessId(buf);
/* 276 */     appendSpace(buf);
/* 277 */     appendMessageId(buf, event.getMessage());
/* 278 */     appendSpace(buf);
/* 279 */     appendStructuredElements(buf, event);
/* 280 */     appendMessage(buf, event);
/* 281 */     if (this.useTlsMessageFormat) {
/* 282 */       return (new TlsSyslogFrame(buf.toString())).toString();
/*     */     }
/* 284 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void appendPriority(StringBuilder buffer, Level logLevel) {
/* 288 */     buffer.append('<');
/* 289 */     buffer.append(Priority.getPriority(this.facility, logLevel));
/* 290 */     buffer.append(">1 ");
/*     */   }
/*     */   
/*     */   private void appendTimestamp(StringBuilder buffer, long milliseconds) {
/* 294 */     buffer.append(computeTimeStampString(milliseconds));
/*     */   }
/*     */   
/*     */   private void appendSpace(StringBuilder buffer) {
/* 298 */     buffer.append(' ');
/*     */   }
/*     */   
/*     */   private void appendHostName(StringBuilder buffer) {
/* 302 */     buffer.append(this.localHostName);
/*     */   }
/*     */   
/*     */   private void appendAppName(StringBuilder buffer) {
/* 306 */     if (this.appName != null) {
/* 307 */       buffer.append(this.appName);
/* 308 */     } else if (this.configName != null) {
/* 309 */       buffer.append(this.configName);
/*     */     } else {
/* 311 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendProcessId(StringBuilder buffer) {
/* 316 */     buffer.append(getProcId());
/*     */   }
/*     */   
/*     */   private void appendMessageId(StringBuilder buffer, Message message) {
/* 320 */     boolean isStructured = message instanceof StructuredDataMessage;
/* 321 */     String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
/* 322 */     if (type != null) {
/* 323 */       buffer.append(type);
/* 324 */     } else if (this.messageId != null) {
/* 325 */       buffer.append(this.messageId);
/*     */     } else {
/* 327 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendMessage(StringBuilder buffer, LogEvent event) {
/* 332 */     Message message = event.getMessage();
/*     */     
/* 334 */     String text = (message instanceof StructuredDataMessage) ? message.getFormat() : message.getFormattedMessage();
/*     */ 
/*     */     
/* 337 */     if (text != null && text.length() > 0) {
/* 338 */       buffer.append(' ').append(escapeNewlines(text, this.escapeNewLine));
/*     */     }
/*     */     
/* 341 */     if (this.exceptionFormatters != null && event.getThrown() != null) {
/* 342 */       StringBuilder exception = new StringBuilder("\n");
/* 343 */       for (PatternFormatter formatter : this.exceptionFormatters) {
/* 344 */         formatter.format(event, exception);
/*     */       }
/* 346 */       buffer.append(escapeNewlines(exception.toString(), this.escapeNewLine));
/*     */     } 
/* 348 */     if (this.includeNewLine) {
/* 349 */       buffer.append("\n");
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendStructuredElements(StringBuilder buffer, LogEvent event) {
/* 354 */     Message message = event.getMessage();
/* 355 */     boolean isStructured = message instanceof StructuredDataMessage;
/*     */     
/* 357 */     if (!isStructured && this.fieldFormatters != null && this.fieldFormatters.isEmpty() && !this.includeMdc) {
/* 358 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 362 */     Map<String, StructuredDataElement> sdElements = new HashMap<>();
/* 363 */     Map<String, String> contextMap = event.getContextData().toMap();
/*     */     
/* 365 */     if (this.mdcRequired != null) {
/* 366 */       checkRequired(contextMap);
/*     */     }
/*     */     
/* 369 */     if (this.fieldFormatters != null) {
/* 370 */       for (Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet()) {
/* 371 */         String sdId = sdElement.getKey();
/* 372 */         StructuredDataElement elem = ((FieldFormatter)sdElement.getValue()).format(event);
/* 373 */         sdElements.put(sdId, elem);
/*     */       } 
/*     */     }
/*     */     
/* 377 */     if (this.includeMdc && contextMap.size() > 0) {
/* 378 */       String mdcSdIdStr = this.mdcSdId.toString();
/* 379 */       StructuredDataElement union = sdElements.get(mdcSdIdStr);
/* 380 */       if (union != null) {
/* 381 */         union.union(contextMap);
/* 382 */         sdElements.put(mdcSdIdStr, union);
/*     */       } else {
/* 384 */         StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, false);
/* 385 */         sdElements.put(mdcSdIdStr, formattedContextMap);
/*     */       } 
/*     */     } 
/*     */     
/* 389 */     if (isStructured) {
/* 390 */       StructuredDataMessage data = (StructuredDataMessage)message;
/* 391 */       Map<String, String> map = data.getData();
/* 392 */       StructuredDataId id = data.getId();
/* 393 */       String sdId = getId(id);
/*     */       
/* 395 */       if (sdElements.containsKey(sdId)) {
/* 396 */         StructuredDataElement union = sdElements.get(id.toString());
/* 397 */         union.union(map);
/* 398 */         sdElements.put(sdId, union);
/*     */       } else {
/* 400 */         StructuredDataElement formattedData = new StructuredDataElement(map, false);
/* 401 */         sdElements.put(sdId, formattedData);
/*     */       } 
/*     */     } 
/*     */     
/* 405 */     if (sdElements.isEmpty()) {
/* 406 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 410 */     for (Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
/* 411 */       formatStructuredElement(entry.getKey(), this.mdcPrefix, entry.getValue(), buffer, this.listChecker);
/*     */     }
/*     */   }
/*     */   
/*     */   private String escapeNewlines(String text, String replacement) {
/* 416 */     if (null == replacement) {
/* 417 */       return text;
/*     */     }
/* 419 */     return NEWLINE_PATTERN.matcher(text).replaceAll(replacement);
/*     */   }
/*     */   
/*     */   protected String getProcId() {
/* 423 */     return this.procId;
/*     */   }
/*     */   
/*     */   protected List<String> getMdcExcludes() {
/* 427 */     return this.mdcExcludes;
/*     */   }
/*     */   
/*     */   protected List<String> getMdcIncludes() {
/* 431 */     return this.mdcIncludes;
/*     */   }
/*     */   
/*     */   private String computeTimeStampString(long now) {
/*     */     long last;
/* 436 */     synchronized (this) {
/* 437 */       last = this.lastTimestamp;
/* 438 */       if (now == this.lastTimestamp) {
/* 439 */         return this.timestamppStr;
/*     */       }
/*     */     } 
/*     */     
/* 443 */     StringBuilder buffer = new StringBuilder();
/* 444 */     Calendar cal = new GregorianCalendar();
/* 445 */     cal.setTimeInMillis(now);
/* 446 */     buffer.append(Integer.toString(cal.get(1)));
/* 447 */     buffer.append('-');
/* 448 */     pad(cal.get(2) + 1, 10, buffer);
/* 449 */     buffer.append('-');
/* 450 */     pad(cal.get(5), 10, buffer);
/* 451 */     buffer.append('T');
/* 452 */     pad(cal.get(11), 10, buffer);
/* 453 */     buffer.append(':');
/* 454 */     pad(cal.get(12), 10, buffer);
/* 455 */     buffer.append(':');
/* 456 */     pad(cal.get(13), 10, buffer);
/* 457 */     buffer.append('.');
/* 458 */     pad(cal.get(14), 100, buffer);
/*     */     
/* 460 */     int tzmin = (cal.get(15) + cal.get(16)) / 60000;
/* 461 */     if (tzmin == 0) {
/* 462 */       buffer.append('Z');
/*     */     } else {
/* 464 */       if (tzmin < 0) {
/* 465 */         tzmin = -tzmin;
/* 466 */         buffer.append('-');
/*     */       } else {
/* 468 */         buffer.append('+');
/*     */       } 
/* 470 */       int tzhour = tzmin / 60;
/* 471 */       tzmin -= tzhour * 60;
/* 472 */       pad(tzhour, 10, buffer);
/* 473 */       buffer.append(':');
/* 474 */       pad(tzmin, 10, buffer);
/*     */     } 
/* 476 */     synchronized (this) {
/* 477 */       if (last == this.lastTimestamp) {
/* 478 */         this.lastTimestamp = now;
/* 479 */         this.timestamppStr = buffer.toString();
/*     */       } 
/*     */     } 
/* 482 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private void pad(int val, int max, StringBuilder buf) {
/* 486 */     while (max > 1) {
/* 487 */       if (val < max) {
/* 488 */         buf.append('0');
/*     */       }
/* 490 */       max /= 10;
/*     */     } 
/* 492 */     buf.append(Integer.toString(val));
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatStructuredElement(String id, String prefix, StructuredDataElement data, StringBuilder sb, ListChecker checker) {
/* 497 */     if ((id == null && this.defaultId == null) || data.discard()) {
/*     */       return;
/*     */     }
/*     */     
/* 501 */     sb.append('[');
/* 502 */     sb.append(id);
/* 503 */     if (!this.mdcSdId.toString().equals(id)) {
/* 504 */       appendMap(prefix, data.getFields(), sb, this.noopChecker);
/*     */     } else {
/* 506 */       appendMap(prefix, data.getFields(), sb, checker);
/*     */     } 
/* 508 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private String getId(StructuredDataId id) {
/* 512 */     StringBuilder sb = new StringBuilder();
/* 513 */     if (id == null || id.getName() == null) {
/* 514 */       sb.append(this.defaultId);
/*     */     } else {
/* 516 */       sb.append(id.getName());
/*     */     } 
/* 518 */     int ein = (id != null) ? id.getEnterpriseNumber() : this.enterpriseNumber;
/* 519 */     if (ein < 0) {
/* 520 */       ein = this.enterpriseNumber;
/*     */     }
/* 522 */     if (ein >= 0) {
/* 523 */       sb.append('@').append(ein);
/*     */     }
/* 525 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void checkRequired(Map<String, String> map) {
/* 529 */     for (String key : this.mdcRequired) {
/* 530 */       String value = map.get(key);
/* 531 */       if (value == null) {
/* 532 */         throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendMap(String prefix, Map<String, String> map, StringBuilder sb, ListChecker checker) {
/* 539 */     SortedMap<String, String> sorted = new TreeMap<>(map);
/* 540 */     for (Map.Entry<String, String> entry : sorted.entrySet()) {
/* 541 */       if (checker.check(entry.getKey()) && entry.getValue() != null) {
/* 542 */         sb.append(' ');
/* 543 */         if (prefix != null) {
/* 544 */           sb.append(prefix);
/*     */         }
/* 546 */         String safeKey = escapeNewlines(escapeSDParams(entry.getKey()), this.escapeNewLine);
/* 547 */         String safeValue = escapeNewlines(escapeSDParams(entry.getValue()), this.escapeNewLine);
/* 548 */         StringBuilders.appendKeyDqValue(sb, safeKey, safeValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String escapeSDParams(String value) {
/* 554 */     return PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface ListChecker
/*     */   {
/*     */     boolean check(String param1String);
/*     */   }
/*     */ 
/*     */   
/*     */   private class IncludeChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private IncludeChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 570 */       return Rfc5424Layout.this.mdcIncludes.contains(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ExcludeChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private ExcludeChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 580 */       return !Rfc5424Layout.this.mdcExcludes.contains(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private class NoopChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private NoopChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 590 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 596 */     StringBuilder sb = new StringBuilder();
/* 597 */     sb.append("facility=").append(this.facility.name());
/* 598 */     sb.append(" appName=").append(this.appName);
/* 599 */     sb.append(" defaultId=").append(this.defaultId);
/* 600 */     sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
/* 601 */     sb.append(" newLine=").append(this.includeNewLine);
/* 602 */     sb.append(" includeMDC=").append(this.includeMdc);
/* 603 */     sb.append(" messageId=").append(this.messageId);
/* 604 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static Rfc5424Layout createLayout(@PluginAttribute(value = "facility", defaultString = "LOCAL0") Facility facility, @PluginAttribute("id") String id, @PluginAttribute(value = "enterpriseNumber", defaultInt = 18060) int enterpriseNumber, @PluginAttribute(value = "includeMDC", defaultBoolean = true) boolean includeMDC, @PluginAttribute(value = "mdcId", defaultString = "mdc") String mdcId, @PluginAttribute("mdcPrefix") String mdcPrefix, @PluginAttribute("eventPrefix") String eventPrefix, @PluginAttribute("newLine") boolean newLine, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute("appName") String appName, @PluginAttribute("messageId") String msgId, @PluginAttribute("mdcExcludes") String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") String required, @PluginAttribute("exceptionPattern") String exceptionPattern, @PluginAttribute("useTlsMessageFormat") boolean useTlsMessageFormat, @PluginElement("LoggerFields") LoggerFields[] loggerFields, @PluginConfiguration Configuration config) {
/* 655 */     if (includes != null && excludes != null) {
/* 656 */       LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
/* 657 */       includes = null;
/*     */     } 
/*     */     
/* 660 */     return new Rfc5424Layout(config, facility, id, enterpriseNumber, includeMDC, newLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, StandardCharsets.UTF_8, exceptionPattern, useTlsMessageFormat, loggerFields);
/*     */   }
/*     */ 
/*     */   
/*     */   private class FieldFormatter
/*     */   {
/*     */     private final Map<String, List<PatternFormatter>> delegateMap;
/*     */     
/*     */     private final boolean discardIfEmpty;
/*     */     
/*     */     public FieldFormatter(Map<String, List<PatternFormatter>> fieldMap, boolean discardIfEmpty) {
/* 671 */       this.discardIfEmpty = discardIfEmpty;
/* 672 */       this.delegateMap = fieldMap;
/*     */     }
/*     */     
/*     */     public Rfc5424Layout.StructuredDataElement format(LogEvent event) {
/* 676 */       Map<String, String> map = new HashMap<>(this.delegateMap.size());
/*     */       
/* 678 */       for (Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet()) {
/* 679 */         StringBuilder buffer = new StringBuilder();
/* 680 */         for (PatternFormatter formatter : entry.getValue()) {
/* 681 */           formatter.format(event, buffer);
/*     */         }
/* 683 */         map.put(entry.getKey(), buffer.toString());
/*     */       } 
/* 685 */       return new Rfc5424Layout.StructuredDataElement(map, this.discardIfEmpty);
/*     */     }
/*     */   }
/*     */   
/*     */   private class StructuredDataElement
/*     */   {
/*     */     private final Map<String, String> fields;
/*     */     private final boolean discardIfEmpty;
/*     */     
/*     */     public StructuredDataElement(Map<String, String> fields, boolean discardIfEmpty) {
/* 695 */       this.discardIfEmpty = discardIfEmpty;
/* 696 */       this.fields = fields;
/*     */     }
/*     */     
/*     */     boolean discard() {
/* 700 */       if (!this.discardIfEmpty) {
/* 701 */         return false;
/*     */       }
/* 703 */       boolean foundNotEmptyValue = false;
/* 704 */       for (Map.Entry<String, String> entry : this.fields.entrySet()) {
/* 705 */         if (Strings.isNotEmpty(entry.getValue())) {
/* 706 */           foundNotEmptyValue = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 710 */       return !foundNotEmptyValue;
/*     */     }
/*     */     
/*     */     void union(Map<String, String> addFields) {
/* 714 */       this.fields.putAll(addFields);
/*     */     }
/*     */     
/*     */     Map<String, String> getFields() {
/* 718 */       return this.fields;
/*     */     }
/*     */   }
/*     */   
/*     */   public Facility getFacility() {
/* 723 */     return this.facility;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\Rfc5424Layout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */