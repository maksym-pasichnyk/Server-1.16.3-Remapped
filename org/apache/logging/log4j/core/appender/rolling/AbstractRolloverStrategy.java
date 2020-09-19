/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.pattern.NotANumber;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRolloverStrategy
/*     */   implements RolloverStrategy
/*     */ {
/*  53 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   protected final StrSubstitutor strSubstitutor;
/*     */   
/*     */   protected AbstractRolloverStrategy(StrSubstitutor strSubstitutor) {
/*  58 */     this.strSubstitutor = strSubstitutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public StrSubstitutor getStrSubstitutor() {
/*  63 */     return this.strSubstitutor;
/*     */   }
/*     */   
/*     */   protected Action merge(Action compressAction, List<Action> custom, boolean stopOnError) {
/*  67 */     if (custom.isEmpty()) {
/*  68 */       return compressAction;
/*     */     }
/*  70 */     if (compressAction == null) {
/*  71 */       return (Action)new CompositeAction(custom, stopOnError);
/*     */     }
/*  73 */     List<Action> all = new ArrayList<>();
/*  74 */     all.add(compressAction);
/*  75 */     all.addAll(custom);
/*  76 */     return (Action)new CompositeAction(all, stopOnError);
/*     */   }
/*     */   
/*     */   protected int suffixLength(String lowFilename) {
/*  80 */     for (FileExtension extension : FileExtension.values()) {
/*  81 */       if (extension.isExtensionFor(lowFilename)) {
/*  82 */         return extension.length();
/*     */       }
/*     */     } 
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager) {
/*  90 */     return getEligibleFiles(manager, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager, boolean isAscending) {
/*  95 */     StringBuilder buf = new StringBuilder();
/*  96 */     String pattern = manager.getPatternProcessor().getPattern();
/*  97 */     manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, NotANumber.NAN);
/*  98 */     return getEligibleFiles(buf.toString(), pattern, isAscending);
/*     */   }
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(String path, String pattern) {
/* 102 */     return getEligibleFiles(path, pattern, true);
/*     */   }
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(String path, String logfilePattern, boolean isAscending) {
/* 106 */     TreeMap<Integer, Path> eligibleFiles = new TreeMap<>();
/* 107 */     File file = new File(path);
/* 108 */     File parent = file.getParentFile();
/* 109 */     if (parent == null) {
/* 110 */       parent = new File(".");
/*     */     } else {
/* 112 */       parent.mkdirs();
/*     */     } 
/* 114 */     if (!logfilePattern.contains("%i")) {
/* 115 */       return eligibleFiles;
/*     */     }
/* 117 */     Path dir = parent.toPath();
/* 118 */     String fileName = file.getName();
/* 119 */     int suffixLength = suffixLength(fileName);
/* 120 */     if (suffixLength > 0) {
/* 121 */       fileName = fileName.substring(0, fileName.length() - suffixLength) + ".*";
/*     */     }
/* 123 */     String filePattern = fileName.replace("\000", "(\\d+)");
/* 124 */     Pattern pattern = Pattern.compile(filePattern);
/*     */     
/* 126 */     try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
/* 127 */       for (Path entry : stream) {
/* 128 */         Matcher matcher = pattern.matcher(entry.toFile().getName());
/* 129 */         if (matcher.matches()) {
/* 130 */           Integer index = Integer.valueOf(Integer.parseInt(matcher.group(1)));
/* 131 */           eligibleFiles.put(index, entry);
/*     */         } 
/*     */       } 
/* 134 */     } catch (IOException ioe) {
/* 135 */       throw new LoggingException("Error reading folder " + dir + " " + ioe.getMessage(), ioe);
/*     */     } 
/* 137 */     return isAscending ? eligibleFiles : eligibleFiles.descendingMap();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\AbstractRolloverStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */