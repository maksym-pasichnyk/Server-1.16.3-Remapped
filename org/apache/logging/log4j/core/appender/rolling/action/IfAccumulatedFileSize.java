/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.rolling.FileSize;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "IfAccumulatedFileSize", category = "Core", printObject = true)
/*     */ public final class IfAccumulatedFileSize
/*     */   implements PathCondition
/*     */ {
/*  39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private final long thresholdBytes;
/*     */   private long accumulatedSize;
/*     */   private final PathCondition[] nestedConditions;
/*     */   
/*     */   private IfAccumulatedFileSize(long thresholdSize, PathCondition[] nestedConditions) {
/*  45 */     if (thresholdSize <= 0L) {
/*  46 */       throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdSize);
/*     */     }
/*  48 */     this.thresholdBytes = thresholdSize;
/*  49 */     this.nestedConditions = (nestedConditions == null) ? new PathCondition[0] : Arrays.<PathCondition>copyOf(nestedConditions, nestedConditions.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThresholdBytes() {
/*  54 */     return this.thresholdBytes;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  58 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
/*  69 */     this.accumulatedSize += attrs.size();
/*  70 */     boolean result = (this.accumulatedSize > this.thresholdBytes);
/*  71 */     String match = result ? ">" : "<=";
/*  72 */     String accept = result ? "ACCEPTED" : "REJECTED";
/*  73 */     LOGGER.trace("IfAccumulatedFileSize {}: {} accumulated size '{}' {} thresholdBytes '{}'", accept, relativePath, Long.valueOf(this.accumulatedSize), match, Long.valueOf(this.thresholdBytes));
/*     */     
/*  75 */     if (result) {
/*  76 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  88 */     this.accumulatedSize = 0L;
/*  89 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   @PluginFactory
/*     */   public static IfAccumulatedFileSize createFileSizeCondition(@PluginAttribute("exceeds") String size, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 105 */     if (size == null) {
/* 106 */       LOGGER.error("IfAccumulatedFileSize missing mandatory size threshold.");
/*     */     }
/* 108 */     long threshold = (size == null) ? Long.MAX_VALUE : FileSize.parse(size, Long.MAX_VALUE);
/* 109 */     return new IfAccumulatedFileSize(threshold, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 115 */     return "IfAccumulatedFileSize(exceeds=" + this.thresholdBytes + nested + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAccumulatedFileSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */