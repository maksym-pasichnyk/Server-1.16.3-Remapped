/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.RegexReplacement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class PatternLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
/*     */   public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %notEmpty{%x }- %m%n";
/*     */   public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
/*     */   public static final String KEY = "Converter";
/*     */   private final String conversionPattern;
/*     */   private final PatternSelector patternSelector;
/*     */   private final AbstractStringLayout.Serializer eventSerializer;
/*     */   
/*     */   private PatternLayout(Configuration config, RegexReplacement replace, String eventPattern, PatternSelector patternSelector, Charset charset, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, String headerPattern, String footerPattern) {
/* 107 */     super(config, charset, newSerializerBuilder().setConfiguration(config).setReplace(replace).setPatternSelector(patternSelector).setAlwaysWriteExceptions(alwaysWriteExceptions).setDisableAnsi(disableAnsi).setNoConsoleNoAnsi(noConsoleNoAnsi).setPattern(headerPattern).build(), newSerializerBuilder().setConfiguration(config).setReplace(replace).setPatternSelector(patternSelector).setAlwaysWriteExceptions(alwaysWriteExceptions).setDisableAnsi(disableAnsi).setNoConsoleNoAnsi(noConsoleNoAnsi).setPattern(footerPattern).build());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.conversionPattern = eventPattern;
/* 127 */     this.patternSelector = patternSelector;
/* 128 */     this.eventSerializer = newSerializerBuilder().setConfiguration(config).setReplace(replace).setPatternSelector(patternSelector).setAlwaysWriteExceptions(alwaysWriteExceptions).setDisableAnsi(disableAnsi).setNoConsoleNoAnsi(noConsoleNoAnsi).setPattern(eventPattern).setDefaultPattern("%m%n").build();
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
/*     */   public static SerializerBuilder newSerializerBuilder() {
/* 141 */     return new SerializerBuilder();
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
/*     */   @Deprecated
/*     */   public static AbstractStringLayout.Serializer createSerializer(Configuration configuration, RegexReplacement replace, String pattern, String defaultPattern, PatternSelector patternSelector, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
/* 161 */     SerializerBuilder builder = newSerializerBuilder();
/* 162 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 163 */     builder.setConfiguration(configuration);
/* 164 */     builder.setDefaultPattern(defaultPattern);
/* 165 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 166 */     builder.setPattern(pattern);
/* 167 */     builder.setPatternSelector(patternSelector);
/* 168 */     builder.setReplace(replace);
/* 169 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConversionPattern() {
/* 178 */     return this.conversionPattern;
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
/*     */   public Map<String, String> getContentFormat() {
/* 193 */     Map<String, String> result = new HashMap<>();
/* 194 */     result.put("structured", "false");
/* 195 */     result.put("formatType", "conversion");
/* 196 */     result.put("format", this.conversionPattern);
/* 197 */     return result;
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
/* 208 */     return this.eventSerializer.toSerializable(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 213 */     if (!(this.eventSerializer instanceof AbstractStringLayout.Serializer2)) {
/* 214 */       super.encode(event, destination);
/*     */       return;
/*     */     } 
/* 217 */     StringBuilder text = toText((AbstractStringLayout.Serializer2)this.eventSerializer, event, getStringBuilder());
/* 218 */     Encoder<StringBuilder> encoder = getStringBuilderEncoder();
/* 219 */     encoder.encode(text, destination);
/* 220 */     trimToMaxSize(text);
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
/*     */   private StringBuilder toText(AbstractStringLayout.Serializer2 serializer, LogEvent event, StringBuilder destination) {
/* 232 */     return serializer.toSerializable(event, destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PatternParser createPatternParser(Configuration config) {
/* 241 */     if (config == null) {
/* 242 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class);
/*     */     }
/* 244 */     PatternParser parser = (PatternParser)config.getComponent("Converter");
/* 245 */     if (parser == null) {
/* 246 */       parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
/* 247 */       config.addComponent("Converter", parser);
/* 248 */       parser = (PatternParser)config.getComponent("Converter");
/*     */     } 
/* 250 */     return parser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 255 */     return (this.patternSelector == null) ? this.conversionPattern : this.patternSelector.toString();
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
/*     */   @PluginFactory
/*     */   @Deprecated
/*     */   public static PatternLayout createLayout(@PluginAttribute(value = "pattern", defaultString = "%m%n") String pattern, @PluginElement("PatternSelector") PatternSelector patternSelector, @PluginConfiguration Configuration config, @PluginElement("Replace") RegexReplacement replace, @PluginAttribute("charset") Charset charset, @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions, @PluginAttribute("noConsoleNoAnsi") boolean noConsoleNoAnsi, @PluginAttribute("header") String headerPattern, @PluginAttribute("footer") String footerPattern) {
/* 295 */     return newBuilder().withPattern(pattern).withPatternSelector(patternSelector).withConfiguration(config).withRegexReplacement(replace).withCharset(charset).withAlwaysWriteExceptions(alwaysWriteExceptions).withNoConsoleNoAnsi(noConsoleNoAnsi).withHeader(headerPattern).withFooter(footerPattern).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternSerializer
/*     */     implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2
/*     */   {
/*     */     private final PatternFormatter[] formatters;
/*     */ 
/*     */ 
/*     */     
/*     */     private final RegexReplacement replace;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PatternSerializer(PatternFormatter[] formatters, RegexReplacement replace) {
/* 315 */       this.formatters = formatters;
/* 316 */       this.replace = replace;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 321 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 323 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 325 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
/* 331 */       int len = this.formatters.length;
/* 332 */       for (int i = 0; i < len; i++) {
/* 333 */         this.formatters[i].format(event, buffer);
/*     */       }
/* 335 */       if (this.replace != null) {
/* 336 */         String str = buffer.toString();
/* 337 */         str = this.replace.format(str);
/* 338 */         buffer.setLength(0);
/* 339 */         buffer.append(str);
/*     */       } 
/* 341 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 346 */       StringBuilder builder = new StringBuilder();
/* 347 */       builder.append(super.toString());
/* 348 */       builder.append("[formatters=");
/* 349 */       builder.append(Arrays.toString((Object[])this.formatters));
/* 350 */       builder.append(", replace=");
/* 351 */       builder.append(this.replace);
/* 352 */       builder.append("]");
/* 353 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SerializerBuilder
/*     */     implements org.apache.logging.log4j.core.util.Builder<AbstractStringLayout.Serializer>
/*     */   {
/*     */     private Configuration configuration;
/*     */     private RegexReplacement replace;
/*     */     private String pattern;
/*     */     private String defaultPattern;
/*     */     private PatternSelector patternSelector;
/*     */     private boolean alwaysWriteExceptions;
/*     */     private boolean disableAnsi;
/*     */     private boolean noConsoleNoAnsi;
/*     */     
/*     */     public AbstractStringLayout.Serializer build() {
/* 370 */       if (Strings.isEmpty(this.pattern) && Strings.isEmpty(this.defaultPattern)) {
/* 371 */         return null;
/*     */       }
/* 373 */       if (this.patternSelector == null) {
/*     */         try {
/* 375 */           PatternParser parser = PatternLayout.createPatternParser(this.configuration);
/* 376 */           List<PatternFormatter> list = parser.parse((this.pattern == null) ? this.defaultPattern : this.pattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi);
/*     */           
/* 378 */           PatternFormatter[] formatters = list.<PatternFormatter>toArray(new PatternFormatter[0]);
/* 379 */           return new PatternLayout.PatternSerializer(formatters, this.replace);
/* 380 */         } catch (RuntimeException ex) {
/* 381 */           throw new IllegalArgumentException("Cannot parse pattern '" + this.pattern + "'", ex);
/*     */         } 
/*     */       }
/* 384 */       return new PatternLayout.PatternSelectorSerializer(this.patternSelector, this.replace);
/*     */     }
/*     */     
/*     */     public SerializerBuilder setConfiguration(Configuration configuration) {
/* 388 */       this.configuration = configuration;
/* 389 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setReplace(RegexReplacement replace) {
/* 393 */       this.replace = replace;
/* 394 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setPattern(String pattern) {
/* 398 */       this.pattern = pattern;
/* 399 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setDefaultPattern(String defaultPattern) {
/* 403 */       this.defaultPattern = defaultPattern;
/* 404 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setPatternSelector(PatternSelector patternSelector) {
/* 408 */       this.patternSelector = patternSelector;
/* 409 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 413 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 414 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setDisableAnsi(boolean disableAnsi) {
/* 418 */       this.disableAnsi = disableAnsi;
/* 419 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 423 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 424 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PatternSelectorSerializer
/*     */     implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2
/*     */   {
/*     */     private final PatternSelector patternSelector;
/*     */     private final RegexReplacement replace;
/*     */     
/*     */     private PatternSelectorSerializer(PatternSelector patternSelector, RegexReplacement replace) {
/* 436 */       this.patternSelector = patternSelector;
/* 437 */       this.replace = replace;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 442 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 444 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 446 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
/* 452 */       PatternFormatter[] formatters = this.patternSelector.getFormatters(event);
/* 453 */       int len = formatters.length;
/* 454 */       for (int i = 0; i < len; i++) {
/* 455 */         formatters[i].format(event, buffer);
/*     */       }
/* 457 */       if (this.replace != null) {
/* 458 */         String str = buffer.toString();
/* 459 */         str = this.replace.format(str);
/* 460 */         buffer.setLength(0);
/* 461 */         buffer.append(str);
/*     */       } 
/* 463 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 468 */       StringBuilder builder = new StringBuilder();
/* 469 */       builder.append(super.toString());
/* 470 */       builder.append("[patternSelector=");
/* 471 */       builder.append(this.patternSelector);
/* 472 */       builder.append(", replace=");
/* 473 */       builder.append(this.replace);
/* 474 */       builder.append("]");
/* 475 */       return builder.toString();
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
/*     */   public static PatternLayout createDefaultLayout() {
/* 487 */     return newBuilder().build();
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
/*     */   public static PatternLayout createDefaultLayout(Configuration configuration) {
/* 500 */     return newBuilder().withConfiguration(configuration).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 510 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PatternLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/* 518 */     private String pattern = "%m%n";
/*     */ 
/*     */     
/*     */     @PluginElement("PatternSelector")
/*     */     private PatternSelector patternSelector;
/*     */     
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginElement("Replace")
/*     */     private RegexReplacement regexReplacement;
/*     */     
/*     */     @PluginBuilderAttribute
/* 531 */     private Charset charset = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean alwaysWriteExceptions = true;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean disableAnsi;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean noConsoleNoAnsi;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String header;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String footer;
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPattern(String pattern) {
/* 558 */       this.pattern = pattern;
/* 559 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPatternSelector(PatternSelector patternSelector) {
/* 567 */       this.patternSelector = patternSelector;
/* 568 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 576 */       this.configuration = configuration;
/* 577 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withRegexReplacement(RegexReplacement regexReplacement) {
/* 585 */       this.regexReplacement = regexReplacement;
/* 586 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 595 */       if (charset != null) {
/* 596 */         this.charset = charset;
/*     */       }
/* 598 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 606 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 607 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withDisableAnsi(boolean disableAnsi) {
/* 615 */       this.disableAnsi = disableAnsi;
/* 616 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 624 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 625 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withHeader(String header) {
/* 633 */       this.header = header;
/* 634 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFooter(String footer) {
/* 642 */       this.footer = footer;
/* 643 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternLayout build() {
/* 649 */       if (this.configuration == null) {
/* 650 */         this.configuration = (Configuration)new DefaultConfiguration();
/*     */       }
/* 652 */       return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.patternSelector, this.charset, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.header, this.footer);
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\PatternLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */