/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MarkerPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
/*     */ public class MarkerPatternSelector
/*     */   implements PatternSelector
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<MarkerPatternSelector>
/*     */   {
/*     */     @PluginElement("PatternMatch")
/*     */     private PatternMatch[] properties;
/*     */     @PluginBuilderAttribute("defaultPattern")
/*     */     private String defaultPattern;
/*     */     @PluginBuilderAttribute("alwaysWriteExceptions")
/*     */     private boolean alwaysWriteExceptions = true;
/*     */     @PluginBuilderAttribute("disableAnsi")
/*     */     private boolean disableAnsi;
/*     */     @PluginBuilderAttribute("noConsoleNoAnsi")
/*     */     private boolean noConsoleNoAnsi;
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     public MarkerPatternSelector build() {
/*  70 */       if (this.defaultPattern == null) {
/*  71 */         this.defaultPattern = "%m%n";
/*     */       }
/*  73 */       if (this.properties == null || this.properties.length == 0) {
/*  74 */         MarkerPatternSelector.LOGGER.warn("No marker patterns were provided with PatternMatch");
/*  75 */         return null;
/*     */       } 
/*  77 */       return new MarkerPatternSelector(this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder setProperties(PatternMatch[] properties) {
/*  82 */       this.properties = properties;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDefaultPattern(String defaultPattern) {
/*  87 */       this.defaultPattern = defaultPattern;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/*  92 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDisableAnsi(boolean disableAnsi) {
/*  97 */       this.disableAnsi = disableAnsi;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 102 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 103 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 107 */       this.configuration = configuration;
/* 108 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 113 */   private final Map<String, PatternFormatter[]> formatterMap = (Map)new HashMap<>();
/*     */   
/* 115 */   private final Map<String, String> patternMap = new HashMap<>();
/*     */   
/*     */   private final PatternFormatter[] defaultFormatters;
/*     */   
/*     */   private final String defaultPattern;
/*     */   
/* 121 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public MarkerPatternSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration config) {
/* 131 */     this(properties, defaultPattern, alwaysWriteExceptions, false, noConsoleNoAnsi, config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MarkerPatternSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config) {
/* 137 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 138 */     for (PatternMatch property : properties) {
/*     */       try {
/* 140 */         List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/*     */         
/* 142 */         this.formatterMap.put(property.getKey(), list.toArray(new PatternFormatter[list.size()]));
/* 143 */         this.patternMap.put(property.getKey(), property.getPattern());
/* 144 */       } catch (RuntimeException ex) {
/* 145 */         throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 149 */       List<PatternFormatter> list = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/*     */       
/* 151 */       this.defaultFormatters = list.<PatternFormatter>toArray(new PatternFormatter[list.size()]);
/* 152 */       this.defaultPattern = defaultPattern;
/* 153 */     } catch (RuntimeException ex) {
/* 154 */       throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PatternFormatter[] getFormatters(LogEvent event) {
/* 160 */     Marker marker = event.getMarker();
/* 161 */     if (marker == null) {
/* 162 */       return this.defaultFormatters;
/*     */     }
/* 164 */     for (String key : this.formatterMap.keySet()) {
/* 165 */       if (marker.isInstanceOf(key)) {
/* 166 */         return this.formatterMap.get(key);
/*     */       }
/*     */     } 
/* 169 */     return this.defaultFormatters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 179 */     return new Builder();
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
/*     */   public static MarkerPatternSelector createSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration configuration) {
/* 199 */     Builder builder = newBuilder();
/* 200 */     builder.setProperties(properties);
/* 201 */     builder.setDefaultPattern(defaultPattern);
/* 202 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 203 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 204 */     builder.setConfiguration(configuration);
/* 205 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 210 */     StringBuilder sb = new StringBuilder();
/* 211 */     boolean first = true;
/* 212 */     for (Map.Entry<String, String> entry : this.patternMap.entrySet()) {
/* 213 */       if (!first) {
/* 214 */         sb.append(", ");
/*     */       }
/* 216 */       sb.append("key=\"").append(entry.getKey()).append("\", pattern=\"").append(entry.getValue()).append("\"");
/* 217 */       first = false;
/*     */     } 
/* 219 */     if (!first) {
/* 220 */       sb.append(", ");
/*     */     }
/* 222 */     sb.append("default=\"").append(this.defaultPattern).append("\"");
/* 223 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\MarkerPatternSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */