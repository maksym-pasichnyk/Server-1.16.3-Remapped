/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Rewrite", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RewriteAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final Configuration config;
/*  44 */   private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<>();
/*     */   
/*     */   private final RewritePolicy rewritePolicy;
/*     */   
/*     */   private final AppenderRef[] appenderRefs;
/*     */   
/*     */   private RewriteAppender(String name, Filter filter, boolean ignoreExceptions, AppenderRef[] appenderRefs, RewritePolicy rewritePolicy, Configuration config) {
/*  51 */     super(name, filter, null, ignoreExceptions);
/*  52 */     this.config = config;
/*  53 */     this.rewritePolicy = rewritePolicy;
/*  54 */     this.appenderRefs = appenderRefs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  59 */     for (AppenderRef ref : this.appenderRefs) {
/*  60 */       String name = ref.getRef();
/*  61 */       Appender appender = this.config.getAppender(name);
/*  62 */       if (appender != null) {
/*  63 */         Filter filter = (appender instanceof AbstractAppender) ? ((AbstractAppender)appender).getFilter() : null;
/*     */         
/*  65 */         this.appenders.put(name, new AppenderControl(appender, ref.getLevel(), filter));
/*     */       } else {
/*  67 */         LOGGER.error("Appender " + ref + " cannot be located. Reference ignored");
/*     */       } 
/*     */     } 
/*  70 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*  79 */     if (this.rewritePolicy != null) {
/*  80 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/*  82 */     for (AppenderControl control : this.appenders.values()) {
/*  83 */       control.callAppender(event);
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
/*     */   @PluginFactory
/*     */   public static RewriteAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("AppenderRef") AppenderRef[] appenderRefs, @PluginConfiguration Configuration config, @PluginElement("RewritePolicy") RewritePolicy rewritePolicy, @PluginElement("Filter") Filter filter) {
/* 107 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 108 */     if (name == null) {
/* 109 */       LOGGER.error("No name provided for RewriteAppender");
/* 110 */       return null;
/*     */     } 
/* 112 */     if (appenderRefs == null) {
/* 113 */       LOGGER.error("No appender references defined for RewriteAppender");
/* 114 */       return null;
/*     */     } 
/* 116 */     return new RewriteAppender(name, filter, ignoreExceptions, appenderRefs, rewritePolicy, config);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rewrite\RewriteAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */