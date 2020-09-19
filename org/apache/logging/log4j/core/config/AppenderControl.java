/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.filter.Filterable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AppenderControl
/*     */   extends AbstractFilterable
/*     */ {
/*  35 */   private final ThreadLocal<AppenderControl> recursive = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private final Appender appender;
/*     */ 
/*     */   
/*     */   private final Level level;
/*     */   
/*     */   private final int intLevel;
/*     */   
/*     */   private final String appenderName;
/*     */ 
/*     */   
/*     */   public AppenderControl(Appender appender, Level level, Filter filter) {
/*  49 */     super(filter);
/*  50 */     this.appender = appender;
/*  51 */     this.appenderName = appender.getName();
/*  52 */     this.level = level;
/*  53 */     this.intLevel = (level == null) ? Level.ALL.intLevel() : level.intLevel();
/*  54 */     start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAppenderName() {
/*  63 */     return this.appenderName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender() {
/*  72 */     return this.appender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void callAppender(LogEvent event) {
/*  81 */     if (shouldSkip(event)) {
/*     */       return;
/*     */     }
/*  84 */     callAppenderPreventRecursion(event);
/*     */   }
/*     */   
/*     */   private boolean shouldSkip(LogEvent event) {
/*  88 */     return (isFilteredByAppenderControl(event) || isFilteredByLevel(event) || isRecursiveCall());
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isFilteredByAppenderControl(LogEvent event) {
/*  93 */     Filter filter = getFilter();
/*  94 */     return (filter != null && Filter.Result.DENY == filter.filter(event));
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isFilteredByLevel(LogEvent event) {
/*  99 */     return (this.level != null && this.intLevel < event.getLevel().intLevel());
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isRecursiveCall() {
/* 104 */     if (this.recursive.get() != null) {
/* 105 */       appenderErrorHandlerMessage("Recursive call to appender ");
/* 106 */       return true;
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private String appenderErrorHandlerMessage(String prefix) {
/* 112 */     String result = createErrorMsg(prefix);
/* 113 */     this.appender.getHandler().error(result);
/* 114 */     return result;
/*     */   }
/*     */   
/*     */   private void callAppenderPreventRecursion(LogEvent event) {
/*     */     try {
/* 119 */       this.recursive.set(this);
/* 120 */       callAppender0(event);
/*     */     } finally {
/* 122 */       this.recursive.set(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callAppender0(LogEvent event) {
/* 127 */     ensureAppenderStarted();
/* 128 */     if (!isFilteredByAppender(event)) {
/* 129 */       tryCallAppender(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAppenderStarted() {
/* 134 */     if (!this.appender.isStarted()) {
/* 135 */       handleError("Attempted to append to non-started appender ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleError(String prefix) {
/* 140 */     String msg = appenderErrorHandlerMessage(prefix);
/* 141 */     if (!this.appender.ignoreExceptions()) {
/* 142 */       throw new AppenderLoggingException(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   private String createErrorMsg(String prefix) {
/* 147 */     return prefix + this.appender.getName();
/*     */   }
/*     */   
/*     */   private boolean isFilteredByAppender(LogEvent event) {
/* 151 */     return (this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(event));
/*     */   }
/*     */   
/*     */   private void tryCallAppender(LogEvent event) {
/*     */     try {
/* 156 */       this.appender.append(event);
/* 157 */     } catch (RuntimeException ex) {
/* 158 */       handleAppenderError(ex);
/* 159 */     } catch (Exception ex) {
/* 160 */       handleAppenderError((RuntimeException)new AppenderLoggingException(ex));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleAppenderError(RuntimeException ex) {
/* 165 */     this.appender.getHandler().error(createErrorMsg("An exception occurred processing Appender "), ex);
/* 166 */     if (!this.appender.ignoreExceptions()) {
/* 167 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 178 */     if (obj == this) {
/* 179 */       return true;
/*     */     }
/* 181 */     if (!(obj instanceof AppenderControl)) {
/* 182 */       return false;
/*     */     }
/* 184 */     AppenderControl other = (AppenderControl)obj;
/* 185 */     return Objects.equals(this.appenderName, other.appenderName);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 190 */     return this.appenderName.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 195 */     return super.toString() + "[appender=" + this.appender + ", appenderName=" + this.appenderName + ", level=" + this.level + ", intLevel=" + this.intLevel + ", recursive=" + this.recursive + ", filter=" + getFilter() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\AppenderControl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */