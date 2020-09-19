/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeletingVisitor
/*    */   extends SimpleFileVisitor<Path>
/*    */ {
/* 36 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   private final Path basePath;
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean testMode;
/*    */ 
/*    */ 
/*    */   
/*    */   private final List<? extends PathCondition> pathConditions;
/*    */ 
/*    */ 
/*    */   
/*    */   public DeletingVisitor(Path basePath, List<? extends PathCondition> pathConditions, boolean testMode) {
/* 53 */     this.testMode = testMode;
/* 54 */     this.basePath = Objects.<Path>requireNonNull(basePath, "basePath");
/* 55 */     this.pathConditions = Objects.<List<? extends PathCondition>>requireNonNull(pathConditions, "pathConditions");
/* 56 */     for (PathCondition condition : pathConditions) {
/* 57 */       condition.beforeFileTreeWalk();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 63 */     for (PathCondition pathFilter : this.pathConditions) {
/* 64 */       Path relative = this.basePath.relativize(file);
/* 65 */       if (!pathFilter.accept(this.basePath, relative, attrs)) {
/* 66 */         LOGGER.trace("Not deleting base={}, relative={}", this.basePath, relative);
/* 67 */         return FileVisitResult.CONTINUE;
/*    */       } 
/*    */     } 
/* 70 */     if (isTestMode()) {
/* 71 */       LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", file);
/*    */     } else {
/* 73 */       delete(file);
/*    */     } 
/* 75 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void delete(Path file) throws IOException {
/* 85 */     LOGGER.trace("Deleting {}", file);
/* 86 */     Files.deleteIfExists(file);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTestMode() {
/* 95 */     return this.testMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\DeletingVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */