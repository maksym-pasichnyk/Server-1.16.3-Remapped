/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
/*     */ public class ScriptPatternSelector
/*     */   implements PatternSelector
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ScriptPatternSelector>
/*     */   {
/*     */     @PluginElement("Script")
/*     */     private AbstractScript script;
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
/*     */     public ScriptPatternSelector build() {
/*  79 */       if (this.script == null) {
/*  80 */         ScriptPatternSelector.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
/*  81 */         return null;
/*     */       } 
/*  83 */       if (this.script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
/*  84 */         this.configuration.getScriptManager().getScript(this.script.getName()) == null) {
/*  85 */         ScriptPatternSelector.LOGGER.error("No script with name {} has been declared.", this.script.getName());
/*  86 */         return null;
/*     */       } 
/*     */       
/*  89 */       if (this.defaultPattern == null) {
/*  90 */         this.defaultPattern = "%m%n";
/*     */       }
/*  92 */       if (this.properties == null || this.properties.length == 0) {
/*  93 */         ScriptPatternSelector.LOGGER.warn("No marker patterns were provided");
/*  94 */         return null;
/*     */       } 
/*  96 */       return new ScriptPatternSelector(this.script, this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder setScript(AbstractScript script) {
/* 101 */       this.script = script;
/* 102 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProperties(PatternMatch[] properties) {
/* 106 */       this.properties = properties;
/* 107 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDefaultPattern(String defaultPattern) {
/* 111 */       this.defaultPattern = defaultPattern;
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 116 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 117 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDisableAnsi(boolean disableAnsi) {
/* 121 */       this.disableAnsi = disableAnsi;
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 126 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration config) {
/* 131 */       this.configuration = config;
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     private Builder() {} }
/* 136 */   private final Map<String, PatternFormatter[]> formatterMap = (Map)new HashMap<>();
/*     */   
/* 138 */   private final Map<String, String> patternMap = new HashMap<>();
/*     */   
/*     */   private final PatternFormatter[] defaultFormatters;
/*     */   
/*     */   private final String defaultPattern;
/*     */   
/* 144 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final AbstractScript script;
/*     */ 
/*     */   
/*     */   private final Configuration configuration;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScriptPatternSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config) {
/* 156 */     this.script = script;
/* 157 */     this.configuration = config;
/* 158 */     if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef)) {
/* 159 */       config.getScriptManager().addScript(script);
/*     */     }
/* 161 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 162 */     for (PatternMatch property : properties) {
/*     */       try {
/* 164 */         List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/* 165 */         this.formatterMap.put(property.getKey(), list.toArray(new PatternFormatter[list.size()]));
/* 166 */         this.patternMap.put(property.getKey(), property.getPattern());
/* 167 */       } catch (RuntimeException ex) {
/* 168 */         throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 172 */       List<PatternFormatter> list = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/* 173 */       this.defaultFormatters = list.<PatternFormatter>toArray(new PatternFormatter[list.size()]);
/* 174 */       this.defaultPattern = defaultPattern;
/* 175 */     } catch (RuntimeException ex) {
/* 176 */       throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PatternFormatter[] getFormatters(LogEvent event) {
/* 182 */     SimpleBindings bindings = new SimpleBindings();
/* 183 */     bindings.putAll(this.configuration.getProperties());
/* 184 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/* 185 */     bindings.put("logEvent", event);
/* 186 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/* 187 */     if (object == null) {
/* 188 */       return this.defaultFormatters;
/*     */     }
/* 190 */     PatternFormatter[] patternFormatter = this.formatterMap.get(object.toString());
/*     */     
/* 192 */     return (patternFormatter == null) ? this.defaultFormatters : patternFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 203 */     return new Builder();
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
/*     */   @Deprecated
/*     */   public static ScriptPatternSelector createSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration configuration) {
/* 226 */     Builder builder = newBuilder();
/* 227 */     builder.setScript(script);
/* 228 */     builder.setProperties(properties);
/* 229 */     builder.setDefaultPattern(defaultPattern);
/* 230 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 231 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 232 */     builder.setConfiguration(configuration);
/* 233 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 238 */     StringBuilder sb = new StringBuilder();
/* 239 */     boolean first = true;
/* 240 */     for (Map.Entry<String, String> entry : this.patternMap.entrySet()) {
/* 241 */       if (!first) {
/* 242 */         sb.append(", ");
/*     */       }
/* 244 */       sb.append("key=\"").append(entry.getKey()).append("\", pattern=\"").append(entry.getValue()).append("\"");
/* 245 */       first = false;
/*     */     } 
/* 247 */     if (!first) {
/* 248 */       sb.append(", ");
/*     */     }
/* 250 */     sb.append("default=\"").append(this.defaultPattern).append("\"");
/* 251 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\ScriptPatternSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */