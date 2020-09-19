/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Failover", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FailoverAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_INTERVAL_SECONDS = 60;
/*     */   private final String primaryRef;
/*     */   private final String[] failovers;
/*     */   private final Configuration config;
/*     */   private AppenderControl primary;
/*  58 */   private final List<AppenderControl> failoverAppenders = new ArrayList<>();
/*     */   
/*     */   private final long intervalNanos;
/*     */   
/*  62 */   private volatile long nextCheckNanos = 0L;
/*     */ 
/*     */   
/*     */   private FailoverAppender(String name, Filter filter, String primary, String[] failovers, int intervalMillis, Configuration config, boolean ignoreExceptions) {
/*  66 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions);
/*  67 */     this.primaryRef = primary;
/*  68 */     this.failovers = failovers;
/*  69 */     this.config = config;
/*  70 */     this.intervalNanos = TimeUnit.MILLISECONDS.toNanos(intervalMillis);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  76 */     Map<String, Appender> map = this.config.getAppenders();
/*  77 */     int errors = 0;
/*  78 */     Appender appender = map.get(this.primaryRef);
/*  79 */     if (appender != null) {
/*  80 */       this.primary = new AppenderControl(appender, null, null);
/*     */     } else {
/*  82 */       LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
/*  83 */       errors++;
/*     */     } 
/*  85 */     for (String name : this.failovers) {
/*  86 */       Appender foAppender = map.get(name);
/*  87 */       if (foAppender != null) {
/*  88 */         this.failoverAppenders.add(new AppenderControl(foAppender, null, null));
/*     */       } else {
/*  90 */         LOGGER.error("Failover appender " + name + " is not configured");
/*     */       } 
/*     */     } 
/*  93 */     if (this.failoverAppenders.isEmpty()) {
/*  94 */       LOGGER.error("No failover appenders are available");
/*  95 */       errors++;
/*     */     } 
/*  97 */     if (errors == 0) {
/*  98 */       super.start();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 108 */     if (!isStarted()) {
/* 109 */       error("FailoverAppender " + getName() + " did not start successfully");
/*     */       return;
/*     */     } 
/* 112 */     long localCheckNanos = this.nextCheckNanos;
/* 113 */     if (localCheckNanos == 0L || System.nanoTime() - localCheckNanos > 0L) {
/* 114 */       callAppender(event);
/*     */     } else {
/* 116 */       failover(event, (Exception)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callAppender(LogEvent event) {
/*     */     try {
/* 122 */       this.primary.callAppender(event);
/* 123 */       this.nextCheckNanos = 0L;
/* 124 */     } catch (Exception ex) {
/* 125 */       this.nextCheckNanos = System.nanoTime() + this.intervalNanos;
/* 126 */       failover(event, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failover(LogEvent event, Exception ex) {
/* 131 */     LoggingException loggingException = (ex != null) ? ((ex instanceof LoggingException) ? (LoggingException)ex : new LoggingException(ex)) : null;
/*     */     
/* 133 */     boolean written = false;
/* 134 */     Exception failoverException = null;
/* 135 */     for (AppenderControl control : this.failoverAppenders) {
/*     */       try {
/* 137 */         control.callAppender(event);
/* 138 */         written = true;
/*     */         break;
/* 140 */       } catch (Exception fex) {
/* 141 */         if (failoverException == null) {
/* 142 */           failoverException = fex;
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     if (!written && !ignoreExceptions()) {
/* 147 */       if (loggingException != null) {
/* 148 */         throw loggingException;
/*     */       }
/* 150 */       throw new LoggingException("Unable to write to failover appenders", failoverException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 156 */     StringBuilder sb = new StringBuilder(getName());
/* 157 */     sb.append(" primary=").append(this.primary).append(", failover={");
/* 158 */     boolean first = true;
/* 159 */     for (String str : this.failovers) {
/* 160 */       if (!first) {
/* 161 */         sb.append(", ");
/*     */       }
/* 163 */       sb.append(str);
/* 164 */       first = false;
/*     */     } 
/* 166 */     sb.append('}');
/* 167 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static FailoverAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("primary") String primary, @PluginElement("Failovers") String[] failovers, @PluginAliases({"retryInterval"}) @PluginAttribute("retryIntervalSeconds") String retryIntervalSeconds, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     int retryIntervalMillis;
/* 192 */     if (name == null) {
/* 193 */       LOGGER.error("A name for the Appender must be specified");
/* 194 */       return null;
/*     */     } 
/* 196 */     if (primary == null) {
/* 197 */       LOGGER.error("A primary Appender must be specified");
/* 198 */       return null;
/*     */     } 
/* 200 */     if (failovers == null || failovers.length == 0) {
/* 201 */       LOGGER.error("At least one failover Appender must be specified");
/* 202 */       return null;
/*     */     } 
/*     */     
/* 205 */     int seconds = parseInt(retryIntervalSeconds, 60);
/*     */     
/* 207 */     if (seconds >= 0) {
/* 208 */       retryIntervalMillis = seconds * 1000;
/*     */     } else {
/* 210 */       LOGGER.warn("Interval " + retryIntervalSeconds + " is less than zero. Using default");
/* 211 */       retryIntervalMillis = 60000;
/*     */     } 
/*     */     
/* 214 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 216 */     return new FailoverAppender(name, filter, primary, failovers, retryIntervalMillis, config, ignoreExceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\FailoverAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */