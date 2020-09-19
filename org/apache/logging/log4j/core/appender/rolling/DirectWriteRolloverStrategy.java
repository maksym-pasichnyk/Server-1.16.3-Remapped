/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
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
/*     */ @Plugin(name = "DirectWriteRolloverStrategy", category = "Core", printObject = true)
/*     */ public class DirectWriteRolloverStrategy
/*     */   extends AbstractRolloverStrategy
/*     */   implements DirectFileRolloverStrategy
/*     */ {
/*     */   private static final int DEFAULT_MAX_FILES = 7;
/*     */   private final int maxFiles;
/*     */   private final int compressionLevel;
/*     */   private final List<Action> customActions;
/*     */   private final boolean stopCustomActionsOnError;
/*     */   private volatile String currentFileName;
/*     */   
/*     */   @PluginFactory
/*     */   public static DirectWriteRolloverStrategy createStrategy(@PluginAttribute("maxFiles") String maxFiles, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginElement("Actions") Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) boolean stopCustomActionsOnError, @PluginConfiguration Configuration config) {
/*  77 */     int maxIndex = Integer.MAX_VALUE;
/*  78 */     if (maxFiles != null) {
/*  79 */       maxIndex = Integer.parseInt(maxFiles);
/*  80 */       if (maxIndex < 0) {
/*  81 */         maxIndex = Integer.MAX_VALUE;
/*  82 */       } else if (maxIndex < 2) {
/*  83 */         LOGGER.error("Maximum files too small. Limited to 7");
/*  84 */         maxIndex = 7;
/*     */       } 
/*     */     } 
/*  87 */     int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
/*  88 */     return new DirectWriteRolloverStrategy(maxIndex, compressionLevel, config.getStrSubstitutor(), customActions, stopCustomActionsOnError);
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
/* 100 */   private int nextIndex = -1;
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
/*     */   protected DirectWriteRolloverStrategy(int maxFiles, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError) {
/* 112 */     super(strSubstitutor);
/* 113 */     this.maxFiles = maxFiles;
/* 114 */     this.compressionLevel = compressionLevel;
/* 115 */     this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 116 */     this.customActions = (customActions == null) ? Collections.<Action>emptyList() : Arrays.<Action>asList(customActions);
/*     */   }
/*     */   
/*     */   public int getCompressionLevel() {
/* 120 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */   public List<Action> getCustomActions() {
/* 124 */     return this.customActions;
/*     */   }
/*     */   
/*     */   public int getMaxFiles() {
/* 128 */     return this.maxFiles;
/*     */   }
/*     */   
/*     */   public boolean isStopCustomActionsOnError() {
/* 132 */     return this.stopCustomActionsOnError;
/*     */   }
/*     */   
/*     */   private int purge(RollingFileManager manager) {
/* 136 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 137 */     LOGGER.debug("Found {} eligible files, max is  {}", Integer.valueOf(eligibleFiles.size()), Integer.valueOf(this.maxFiles));
/* 138 */     while (eligibleFiles.size() >= this.maxFiles) {
/*     */       try {
/* 140 */         Integer key = eligibleFiles.firstKey();
/* 141 */         Files.delete(eligibleFiles.get(key));
/* 142 */         eligibleFiles.remove(key);
/* 143 */       } catch (IOException ioe) {
/* 144 */         LOGGER.error("Unable to delete {}", eligibleFiles.firstKey(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 148 */     return (eligibleFiles.size() > 0) ? ((Integer)eligibleFiles.lastKey()).intValue() : 1;
/*     */   }
/*     */   
/*     */   public String getCurrentFileName(RollingFileManager manager) {
/* 152 */     if (this.currentFileName == null) {
/* 153 */       SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 154 */       int fileIndex = (eligibleFiles.size() > 0) ? ((this.nextIndex > 0) ? this.nextIndex : eligibleFiles.size()) : 1;
/* 155 */       StringBuilder buf = new StringBuilder(255);
/* 156 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, true, Integer.valueOf(fileIndex));
/* 157 */       int suffixLength = suffixLength(buf.toString());
/* 158 */       String name = (suffixLength > 0) ? buf.substring(0, buf.length() - suffixLength) : buf.toString();
/* 159 */       this.currentFileName = name;
/*     */     } 
/* 161 */     return this.currentFileName;
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
/*     */   public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
/* 173 */     LOGGER.debug("Rolling " + this.currentFileName);
/* 174 */     if (this.maxFiles < 0) {
/* 175 */       return null;
/*     */     }
/* 177 */     long startNanos = System.nanoTime();
/* 178 */     int fileIndex = purge(manager);
/* 179 */     if (LOGGER.isTraceEnabled()) {
/* 180 */       double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
/* 181 */       LOGGER.trace("DirectWriteRolloverStrategy.purge() took {} milliseconds", Double.valueOf(durationMillis));
/*     */     } 
/* 183 */     Action compressAction = null;
/* 184 */     String sourceName = this.currentFileName;
/* 185 */     this.currentFileName = null;
/* 186 */     this.nextIndex = fileIndex + 1;
/* 187 */     FileExtension fileExtension = manager.getFileExtension();
/* 188 */     if (fileExtension != null) {
/* 189 */       compressAction = fileExtension.createCompressAction(sourceName, sourceName + fileExtension.getExtension(), true, this.compressionLevel);
/*     */     }
/*     */ 
/*     */     
/* 193 */     Action asyncAction = merge(compressAction, this.customActions, this.stopCustomActionsOnError);
/* 194 */     return new RolloverDescriptionImpl(sourceName, false, null, asyncAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 199 */     return "DirectWriteRolloverStrategy(maxFiles=" + this.maxFiles + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\DirectWriteRolloverStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */