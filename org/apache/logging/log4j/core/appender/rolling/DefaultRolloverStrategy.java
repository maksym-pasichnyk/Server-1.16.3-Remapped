/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
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
/*     */ @Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
/*     */ public class DefaultRolloverStrategy
/*     */   extends AbstractRolloverStrategy
/*     */ {
/*     */   private static final int MIN_WINDOW_SIZE = 1;
/*     */   private static final int DEFAULT_WINDOW_SIZE = 7;
/*     */   private final int maxIndex;
/*     */   private final int minIndex;
/*     */   private final boolean useMax;
/*     */   private final int compressionLevel;
/*     */   private final List<Action> customActions;
/*     */   private final boolean stopCustomActionsOnError;
/*     */   
/*     */   @PluginFactory
/*     */   public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String max, @PluginAttribute("min") String min, @PluginAttribute("fileIndex") String fileIndex, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginElement("Actions") Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) boolean stopCustomActionsOnError, @PluginConfiguration Configuration config) {
/*     */     int minIndex, maxIndex;
/*     */     boolean useMax;
/* 112 */     if (fileIndex != null && fileIndex.equalsIgnoreCase("nomax")) {
/* 113 */       minIndex = Integer.MIN_VALUE;
/* 114 */       maxIndex = Integer.MAX_VALUE;
/* 115 */       useMax = false;
/*     */     } else {
/* 117 */       useMax = (fileIndex == null) ? true : fileIndex.equalsIgnoreCase("max");
/* 118 */       minIndex = 1;
/* 119 */       if (min != null) {
/* 120 */         minIndex = Integer.parseInt(min);
/* 121 */         if (minIndex < 1) {
/* 122 */           LOGGER.error("Minimum window size too small. Limited to 1");
/* 123 */           minIndex = 1;
/*     */         } 
/*     */       } 
/* 126 */       maxIndex = 7;
/* 127 */       if (max != null) {
/* 128 */         maxIndex = Integer.parseInt(max);
/* 129 */         if (maxIndex < minIndex) {
/* 130 */           maxIndex = (minIndex < 7) ? 7 : minIndex;
/* 131 */           LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 135 */     int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
/* 136 */     return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, config.getStrSubstitutor(), customActions, stopCustomActionsOnError);
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
/*     */   protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError) {
/* 165 */     super(strSubstitutor);
/* 166 */     this.minIndex = minIndex;
/* 167 */     this.maxIndex = maxIndex;
/* 168 */     this.useMax = useMax;
/* 169 */     this.compressionLevel = compressionLevel;
/* 170 */     this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 171 */     this.customActions = (customActions == null) ? Collections.<Action>emptyList() : Arrays.<Action>asList(customActions);
/*     */   }
/*     */   
/*     */   public int getCompressionLevel() {
/* 175 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */   public List<Action> getCustomActions() {
/* 179 */     return this.customActions;
/*     */   }
/*     */   
/*     */   public int getMaxIndex() {
/* 183 */     return this.maxIndex;
/*     */   }
/*     */   
/*     */   public int getMinIndex() {
/* 187 */     return this.minIndex;
/*     */   }
/*     */   
/*     */   public boolean isStopCustomActionsOnError() {
/* 191 */     return this.stopCustomActionsOnError;
/*     */   }
/*     */   
/*     */   public boolean isUseMax() {
/* 195 */     return this.useMax;
/*     */   }
/*     */   
/*     */   private int purge(int lowIndex, int highIndex, RollingFileManager manager) {
/* 199 */     return this.useMax ? purgeAscending(lowIndex, highIndex, manager) : purgeDescending(lowIndex, highIndex, manager);
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
/*     */   private int purgeAscending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 212 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 213 */     int maxFiles = highIndex - lowIndex + 1;
/*     */     
/* 215 */     boolean renameFiles = false;
/* 216 */     while (eligibleFiles.size() >= maxFiles) {
/*     */       try {
/* 218 */         LOGGER.debug("Eligible files: {}", eligibleFiles);
/* 219 */         Integer key = eligibleFiles.firstKey();
/* 220 */         LOGGER.debug("Deleting {}", ((Path)eligibleFiles.get(key)).toFile().getAbsolutePath());
/* 221 */         Files.delete(eligibleFiles.get(key));
/* 222 */         eligibleFiles.remove(key);
/* 223 */         renameFiles = true;
/* 224 */       } catch (IOException ioe) {
/* 225 */         LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 229 */     StringBuilder buf = new StringBuilder();
/* 230 */     if (renameFiles) {
/* 231 */       for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
/* 232 */         buf.setLength(0);
/*     */         
/* 234 */         manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() - 1));
/* 235 */         String currentName = ((Path)entry.getValue()).toFile().getName();
/* 236 */         String renameTo = buf.toString();
/* 237 */         int suffixLength = suffixLength(renameTo);
/* 238 */         if (suffixLength > 0 && suffixLength(currentName) == 0) {
/* 239 */           renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
/*     */         }
/* 241 */         FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
/*     */         try {
/* 243 */           LOGGER.debug("DefaultRolloverStrategy.purgeAscending executing {}", fileRenameAction);
/* 244 */           if (!fileRenameAction.execute()) {
/* 245 */             return -1;
/*     */           }
/* 247 */         } catch (Exception ex) {
/* 248 */           LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 249 */           return -1;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 254 */     return (eligibleFiles.size() > 0) ? ((((Integer)eligibleFiles.lastKey()).intValue() < highIndex) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : highIndex) : lowIndex;
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
/*     */   private int purgeDescending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 269 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager, false);
/* 270 */     int maxFiles = highIndex - lowIndex + 1;
/*     */     
/* 272 */     while (eligibleFiles.size() >= maxFiles) {
/*     */       try {
/* 274 */         Integer key = eligibleFiles.firstKey();
/* 275 */         Files.delete(eligibleFiles.get(key));
/* 276 */         eligibleFiles.remove(key);
/* 277 */       } catch (IOException ioe) {
/* 278 */         LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 282 */     StringBuilder buf = new StringBuilder();
/* 283 */     for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
/* 284 */       buf.setLength(0);
/*     */       
/* 286 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() + 1));
/* 287 */       String currentName = ((Path)entry.getValue()).toFile().getName();
/* 288 */       String renameTo = buf.toString();
/* 289 */       int suffixLength = suffixLength(renameTo);
/* 290 */       if (suffixLength > 0 && suffixLength(currentName) == 0) {
/* 291 */         renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
/*     */       }
/* 293 */       FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
/*     */       try {
/* 295 */         LOGGER.debug("DefaultRolloverStrategy.purgeDescending executing {}", fileRenameAction);
/* 296 */         if (!fileRenameAction.execute()) {
/* 297 */           return -1;
/*     */         }
/* 299 */       } catch (Exception ex) {
/* 300 */         LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 301 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     return lowIndex;
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
/*     */     int fileIndex;
/* 318 */     if (this.minIndex == Integer.MIN_VALUE) {
/* 319 */       SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 320 */       fileIndex = (eligibleFiles.size() > 0) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : 1;
/*     */     } else {
/* 322 */       if (this.maxIndex < 0) {
/* 323 */         return null;
/*     */       }
/* 325 */       long startNanos = System.nanoTime();
/* 326 */       fileIndex = purge(this.minIndex, this.maxIndex, manager);
/* 327 */       if (fileIndex < 0) {
/* 328 */         return null;
/*     */       }
/* 330 */       if (LOGGER.isTraceEnabled()) {
/* 331 */         double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
/* 332 */         LOGGER.trace("DefaultRolloverStrategy.purge() took {} milliseconds", Double.valueOf(durationMillis));
/*     */       } 
/*     */     } 
/* 335 */     StringBuilder buf = new StringBuilder(255);
/* 336 */     manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
/* 337 */     String currentFileName = manager.getFileName();
/*     */     
/* 339 */     String renameTo = buf.toString();
/* 340 */     String compressedName = renameTo;
/* 341 */     Action compressAction = null;
/*     */     
/* 343 */     FileExtension fileExtension = manager.getFileExtension();
/* 344 */     if (fileExtension != null) {
/* 345 */       renameTo = renameTo.substring(0, renameTo.length() - fileExtension.length());
/* 346 */       compressAction = fileExtension.createCompressAction(renameTo, compressedName, true, this.compressionLevel);
/*     */     } 
/*     */ 
/*     */     
/* 350 */     if (currentFileName.equals(renameTo)) {
/* 351 */       LOGGER.warn("Attempt to rename file {} to itself will be ignored", currentFileName);
/* 352 */       return new RolloverDescriptionImpl(currentFileName, false, null, null);
/*     */     } 
/*     */     
/* 355 */     FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), manager.isRenameEmptyFiles());
/*     */ 
/*     */     
/* 358 */     Action asyncAction = merge(compressAction, this.customActions, this.stopCustomActionsOnError);
/* 359 */     return new RolloverDescriptionImpl(currentFileName, false, (Action)renameAction, asyncAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 364 */     return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ", useMax=" + this.useMax + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\DefaultRolloverStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */