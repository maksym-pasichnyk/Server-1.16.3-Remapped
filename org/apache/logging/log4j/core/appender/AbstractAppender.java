/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.ErrorHandler;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAppender
/*     */   extends AbstractFilterable
/*     */   implements Appender
/*     */ {
/*     */   private final String name;
/*     */   private final boolean ignoreExceptions;
/*     */   private final Layout<? extends Serializable> layout;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractFilterable.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean ignoreExceptions = true;
/*     */     @PluginElement("Layout")
/*     */     private Layout<? extends Serializable> layout;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No appender name provided")
/*     */     private String name;
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     public String getName() {
/*  64 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isIgnoreExceptions() {
/*  68 */       return this.ignoreExceptions;
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getLayout() {
/*  72 */       return this.layout;
/*     */     }
/*     */     
/*     */     public B withName(String name) {
/*  76 */       this.name = name;
/*  77 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withIgnoreExceptions(boolean ignoreExceptions) {
/*  81 */       this.ignoreExceptions = ignoreExceptions;
/*  82 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLayout(Layout<? extends Serializable> layout) {
/*  86 */       this.layout = layout;
/*  87 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getOrCreateLayout() {
/*  91 */       if (this.layout == null) {
/*  92 */         return (Layout<? extends Serializable>)PatternLayout.createDefaultLayout();
/*     */       }
/*  94 */       return this.layout;
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getOrCreateLayout(Charset charset) {
/*  98 */       if (this.layout == null) {
/*  99 */         return (Layout<? extends Serializable>)PatternLayout.newBuilder().withCharset(charset).build();
/*     */       }
/* 101 */       return this.layout;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withConfiguration(Configuration configuration) {
/* 109 */       this.configuration = configuration;
/* 110 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setConfiguration(Configuration configuration) {
/* 114 */       this.configuration = configuration;
/* 115 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/* 119 */       return this.configuration;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private ErrorHandler handler = new DefaultErrorHandler(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
/* 137 */     this(name, filter, layout, true);
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
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
/* 151 */     super(filter);
/* 152 */     this.name = Objects.<String>requireNonNull(name, "name");
/* 153 */     this.layout = layout;
/* 154 */     this.ignoreExceptions = ignoreExceptions;
/*     */   }
/*     */   
/*     */   public static int parseInt(String s, int defaultValue) {
/*     */     try {
/* 159 */       return Integers.parseInt(s, defaultValue);
/* 160 */     } catch (NumberFormatException e) {
/* 161 */       LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", s, Integer.valueOf(defaultValue), e);
/* 162 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 172 */     this.handler.error(msg);
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
/*     */   public void error(String msg, LogEvent event, Throwable t) {
/* 184 */     this.handler.error(msg, event, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 194 */     this.handler.error(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler getHandler() {
/* 204 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layout<? extends Serializable> getLayout() {
/* 214 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 224 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoreExceptions() {
/* 235 */     return this.ignoreExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ErrorHandler handler) {
/* 245 */     if (handler == null) {
/* 246 */       LOGGER.error("The handler cannot be set to null");
/*     */     }
/* 248 */     if (isStarted()) {
/* 249 */       LOGGER.error("The handler cannot be changed once the appender is started");
/*     */       return;
/*     */     } 
/* 252 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AbstractAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */