/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "Column", category = "Core", printObject = true)
/*     */ public final class ColumnConfig
/*     */ {
/*  40 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final String columnName;
/*     */   
/*     */   private final PatternLayout layout;
/*     */   private final String literalValue;
/*     */   private final boolean eventTimestamp;
/*     */   private final boolean unicode;
/*     */   private final boolean clob;
/*     */   
/*     */   private ColumnConfig(String columnName, PatternLayout layout, String literalValue, boolean eventDate, boolean unicode, boolean clob) {
/*  51 */     this.columnName = columnName;
/*  52 */     this.layout = layout;
/*  53 */     this.literalValue = literalValue;
/*  54 */     this.eventTimestamp = eventDate;
/*  55 */     this.unicode = unicode;
/*  56 */     this.clob = clob;
/*     */   }
/*     */   
/*     */   public String getColumnName() {
/*  60 */     return this.columnName;
/*     */   }
/*     */   
/*     */   public PatternLayout getLayout() {
/*  64 */     return this.layout;
/*     */   }
/*     */   
/*     */   public String getLiteralValue() {
/*  68 */     return this.literalValue;
/*     */   }
/*     */   
/*     */   public boolean isEventTimestamp() {
/*  72 */     return this.eventTimestamp;
/*     */   }
/*     */   
/*     */   public boolean isUnicode() {
/*  76 */     return this.unicode;
/*     */   }
/*     */   
/*     */   public boolean isClob() {
/*  80 */     return this.clob;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
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
/*     */   @Deprecated
/*     */   public static ColumnConfig createColumnConfig(Configuration config, String name, String pattern, String literalValue, String eventTimestamp, String unicode, String clob) {
/*  99 */     if (Strings.isEmpty(name)) {
/* 100 */       LOGGER.error("The column config is not valid because it does not contain a column name.");
/* 101 */       return null;
/*     */     } 
/*     */     
/* 104 */     boolean isEventTimestamp = Boolean.parseBoolean(eventTimestamp);
/* 105 */     boolean isUnicode = Booleans.parseBoolean(unicode, true);
/* 106 */     boolean isClob = Boolean.parseBoolean(clob);
/*     */     
/* 108 */     return newBuilder().setConfiguration(config).setName(name).setPattern(pattern).setLiteral(literalValue).setEventTimestamp(isEventTimestamp).setUnicode(isUnicode).setClob(isClob).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 121 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ColumnConfig>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No name provided")
/*     */     private String name;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String literal;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean isEventTimestamp;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean isUnicode = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean isClob;
/*     */ 
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 152 */       this.configuration = configuration;
/* 153 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setName(String name) {
/* 160 */       this.name = name;
/* 161 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPattern(String pattern) {
/* 169 */       this.pattern = pattern;
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLiteral(String literal) {
/* 178 */       this.literal = literal;
/* 179 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setEventTimestamp(boolean eventTimestamp) {
/* 187 */       this.isEventTimestamp = eventTimestamp;
/* 188 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setUnicode(boolean unicode) {
/* 195 */       this.isUnicode = unicode;
/* 196 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setClob(boolean clob) {
/* 203 */       this.isClob = clob;
/* 204 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ColumnConfig build() {
/* 209 */       if (Strings.isEmpty(this.name)) {
/* 210 */         ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
/* 211 */         return null;
/*     */       } 
/*     */       
/* 214 */       boolean isPattern = Strings.isNotEmpty(this.pattern);
/* 215 */       boolean isLiteralValue = Strings.isNotEmpty(this.literal);
/*     */       
/* 217 */       if ((isPattern && isLiteralValue) || (isPattern && this.isEventTimestamp) || (isLiteralValue && this.isEventTimestamp)) {
/* 218 */         ColumnConfig.LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
/* 219 */         return null;
/*     */       } 
/*     */       
/* 222 */       if (this.isEventTimestamp) {
/* 223 */         return new ColumnConfig(this.name, null, null, true, false, false);
/*     */       }
/*     */       
/* 226 */       if (isLiteralValue) {
/* 227 */         return new ColumnConfig(this.name, null, this.literal, false, false, false);
/*     */       }
/*     */       
/* 230 */       if (isPattern) {
/* 231 */         PatternLayout layout = PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 237 */         return new ColumnConfig(this.name, layout, null, false, this.isUnicode, this.isClob);
/*     */       } 
/*     */       
/* 240 */       ColumnConfig.LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
/* 241 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jdbc\ColumnConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */