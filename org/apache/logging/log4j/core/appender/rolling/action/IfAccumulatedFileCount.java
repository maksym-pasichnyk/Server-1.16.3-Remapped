/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ @Plugin(name = "IfAccumulatedFileCount", category = "Core", printObject = true)
/*     */ public final class IfAccumulatedFileCount
/*     */   implements PathCondition
/*     */ {
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private final int threshold;
/*     */   private int count;
/*     */   private final PathCondition[] nestedConditions;
/*     */   
/*     */   private IfAccumulatedFileCount(int thresholdParam, PathCondition[] nestedConditions) {
/*  44 */     if (thresholdParam <= 0) {
/*  45 */       throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdParam);
/*     */     }
/*  47 */     this.threshold = thresholdParam;
/*  48 */     this.nestedConditions = (nestedConditions == null) ? new PathCondition[0] : Arrays.<PathCondition>copyOf(nestedConditions, nestedConditions.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThresholdCount() {
/*  53 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  57 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
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
/*  68 */     boolean result = (++this.count > this.threshold);
/*  69 */     String match = result ? ">" : "<=";
/*  70 */     String accept = result ? "ACCEPTED" : "REJECTED";
/*  71 */     LOGGER.trace("IfAccumulatedFileCount {}: {} count '{}' {} threshold '{}'", accept, relativePath, Integer.valueOf(this.count), match, Integer.valueOf(this.threshold));
/*     */     
/*  73 */     if (result) {
/*  74 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  86 */     this.count = 0;
/*  87 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   public static IfAccumulatedFileCount createFileCountCondition(@PluginAttribute(value = "exceeds", defaultInt = 2147483647) int threshold, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 103 */     if (threshold == Integer.MAX_VALUE) {
/* 104 */       LOGGER.error("IfAccumulatedFileCount invalid or missing threshold value.");
/*     */     }
/* 106 */     return new IfAccumulatedFileCount(threshold, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 112 */     return "IfAccumulatedFileCount(exceeds=" + this.threshold + nested + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAccumulatedFileCount.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */