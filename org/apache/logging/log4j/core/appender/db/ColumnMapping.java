/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ColumnMapping", category = "Core", printObject = true)
/*     */ public class ColumnMapping
/*     */ {
/*  45 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final String name;
/*     */   private final StringLayout layout;
/*     */   private final String literalValue;
/*     */   private final Class<?> type;
/*     */   
/*     */   private ColumnMapping(String name, StringLayout layout, String literalValue, Class<?> type) {
/*  53 */     this.name = name;
/*  54 */     this.layout = layout;
/*  55 */     this.literalValue = literalValue;
/*  56 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  60 */     return this.name;
/*     */   }
/*     */   
/*     */   public StringLayout getLayout() {
/*  64 */     return this.layout;
/*     */   }
/*     */   
/*     */   public String getLiteralValue() {
/*  68 */     return this.literalValue;
/*     */   }
/*     */   
/*     */   public Class<?> getType() {
/*  72 */     return this.type;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  77 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ColumnMapping>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No column name provided")
/*     */     private String name;
/*     */     @PluginElement("Layout")
/*     */     private StringLayout layout;
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     @PluginBuilderAttribute
/*     */     private String literal;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No conversion type provided")
/*  95 */     private Class<?> type = String.class;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setName(String name) {
/* 106 */       this.name = name;
/* 107 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLayout(StringLayout layout) {
/* 115 */       this.layout = layout;
/* 116 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPattern(String pattern) {
/* 124 */       this.pattern = pattern;
/* 125 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLiteral(String literal) {
/* 133 */       this.literal = literal;
/* 134 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setType(Class<?> type) {
/* 143 */       this.type = type;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 148 */       this.configuration = configuration;
/* 149 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ColumnMapping build() {
/* 154 */       if (this.pattern != null) {
/* 155 */         this.layout = (StringLayout)PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).build();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 160 */       if (this.layout == null && this.literal == null && !Date.class.isAssignableFrom(this.type) && !ReadOnlyStringMap.class.isAssignableFrom(this.type) && !ThreadContextMap.class.isAssignableFrom(this.type) && !ThreadContextStack.class.isAssignableFrom(this.type)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 166 */         ColumnMapping.LOGGER.error("No layout or literal value specified and type ({}) is not compatible with ThreadContextMap, ThreadContextStack, or java.util.Date", this.type);
/*     */         
/* 168 */         return null;
/*     */       } 
/* 170 */       return new ColumnMapping(this.name, this.layout, this.literal, this.type);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\ColumnMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */