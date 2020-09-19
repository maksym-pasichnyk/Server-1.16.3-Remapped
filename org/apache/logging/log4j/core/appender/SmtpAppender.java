/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
/*     */ import org.apache.logging.log4j.core.filter.ThresholdFilter;
/*     */ import org.apache.logging.log4j.core.layout.HtmlLayout;
/*     */ import org.apache.logging.log4j.core.net.SmtpManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SMTP", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class SmtpAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 512;
/*     */   private final SmtpManager manager;
/*     */   
/*     */   private SmtpAppender(String name, Filter filter, Layout<? extends Serializable> layout, SmtpManager manager, boolean ignoreExceptions) {
/*  69 */     super(name, filter, layout, ignoreExceptions);
/*  70 */     this.manager = manager;
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
/*     */   public static SmtpAppender createAppender(@PluginConfiguration Configuration config, @PluginAttribute("name") @Required String name, @PluginAttribute("to") String to, @PluginAttribute("cc") String cc, @PluginAttribute("bcc") String bcc, @PluginAttribute("from") String from, @PluginAttribute("replyTo") String replyTo, @PluginAttribute("subject") String subject, @PluginAttribute("smtpProtocol") String smtpProtocol, @PluginAttribute("smtpHost") String smtpHost, @PluginAttribute(value = "smtpPort", defaultString = "0") @ValidPort String smtpPortStr, @PluginAttribute("smtpUsername") String smtpUsername, @PluginAttribute(value = "smtpPassword", sensitive = true) String smtpPassword, @PluginAttribute("smtpDebug") String smtpDebug, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     HtmlLayout htmlLayout;
/*     */     ThresholdFilter thresholdFilter;
/* 132 */     if (name == null) {
/* 133 */       LOGGER.error("No name provided for SmtpAppender");
/* 134 */       return null;
/*     */     } 
/*     */     
/* 137 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 138 */     int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
/* 139 */     boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
/* 140 */     int bufferSize = (bufferSizeStr == null) ? 512 : Integer.parseInt(bufferSizeStr);
/*     */     
/* 142 */     if (layout == null) {
/* 143 */       htmlLayout = HtmlLayout.createDefaultLayout();
/*     */     }
/* 145 */     if (filter == null) {
/* 146 */       thresholdFilter = ThresholdFilter.createFilter(null, null, null);
/*     */     }
/* 148 */     Configuration configuration = (config != null) ? config : (Configuration)new DefaultConfiguration();
/*     */     
/* 150 */     SmtpManager manager = SmtpManager.getSmtpManager(configuration, to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, thresholdFilter.toString(), bufferSize);
/*     */     
/* 152 */     if (manager == null) {
/* 153 */       return null;
/*     */     }
/*     */     
/* 156 */     return new SmtpAppender(name, (Filter)thresholdFilter, (Layout<? extends Serializable>)htmlLayout, manager, ignoreExceptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFiltered(LogEvent event) {
/* 166 */     boolean filtered = super.isFiltered(event);
/* 167 */     if (filtered) {
/* 168 */       this.manager.add(event);
/*     */     }
/* 170 */     return filtered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 181 */     this.manager.sendEvents(getLayout(), event);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\SmtpAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */