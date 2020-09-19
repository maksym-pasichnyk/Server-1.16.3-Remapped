/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import javax.script.Bindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.core.script.ScriptManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptAppenderSelector", category = "Core", elementType = "appender", printObject = true)
/*     */ public class ScriptAppenderSelector
/*     */   extends AbstractAppender
/*     */ {
/*     */   public static final class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<Appender>
/*     */   {
/*     */     @PluginElement("AppenderSet")
/*     */     @Required
/*     */     private AppenderSet appenderSet;
/*     */     @PluginConfiguration
/*     */     @Required
/*     */     private Configuration configuration;
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String name;
/*     */     @PluginElement("Script")
/*     */     @Required
/*     */     private AbstractScript script;
/*     */     
/*     */     public Appender build() {
/*  65 */       if (this.name == null) {
/*  66 */         ScriptAppenderSelector.LOGGER.error("Name missing.");
/*  67 */         return null;
/*     */       } 
/*  69 */       if (this.script == null) {
/*  70 */         ScriptAppenderSelector.LOGGER.error("Script missing for ScriptAppenderSelector appender {}", this.name);
/*  71 */         return null;
/*     */       } 
/*  73 */       if (this.appenderSet == null) {
/*  74 */         ScriptAppenderSelector.LOGGER.error("AppenderSet missing for ScriptAppenderSelector appender {}", this.name);
/*  75 */         return null;
/*     */       } 
/*  77 */       if (this.configuration == null) {
/*  78 */         ScriptAppenderSelector.LOGGER.error("Configuration missing for ScriptAppenderSelector appender {}", this.name);
/*  79 */         return null;
/*     */       } 
/*  81 */       ScriptManager scriptManager = this.configuration.getScriptManager();
/*  82 */       scriptManager.addScript(this.script);
/*  83 */       Bindings bindings = scriptManager.createBindings(this.script);
/*  84 */       Object object = scriptManager.execute(this.script.getName(), bindings);
/*  85 */       String appenderName = Objects.toString(object, null);
/*  86 */       Appender appender = this.appenderSet.createAppender(appenderName, this.name);
/*  87 */       return appender;
/*     */     }
/*     */     
/*     */     public AppenderSet getAppenderSet() {
/*  91 */       return this.appenderSet;
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  95 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  99 */       return this.name;
/*     */     }
/*     */     
/*     */     public AbstractScript getScript() {
/* 103 */       return this.script;
/*     */     }
/*     */     
/*     */     public Builder withAppenderNodeSet(AppenderSet appenderSet) {
/* 107 */       this.appenderSet = appenderSet;
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 112 */       this.configuration = configuration;
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withName(String name) {
/* 117 */       this.name = name;
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withScript(AbstractScript script) {
/* 122 */       this.script = script;
/* 123 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 130 */     return new Builder();
/*     */   }
/*     */   
/*     */   private ScriptAppenderSelector(String name, Filter filter, Layout<? extends Serializable> layout) {
/* 134 */     super(name, filter, layout);
/*     */   }
/*     */   
/*     */   public void append(LogEvent event) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\ScriptAppenderSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */