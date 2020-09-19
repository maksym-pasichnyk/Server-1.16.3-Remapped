/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
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
/*    */ public class SortingVisitor
/*    */   extends SimpleFileVisitor<Path>
/*    */ {
/*    */   private final PathSorter sorter;
/* 36 */   private final List<PathWithAttributes> collected = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SortingVisitor(PathSorter sorter) {
/* 45 */     this.sorter = Objects.<PathSorter>requireNonNull(sorter, "sorter");
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 50 */     this.collected.add(new PathWithAttributes(path, attrs));
/* 51 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */   
/*    */   public List<PathWithAttributes> getSortedPaths() {
/* 55 */     Collections.sort(this.collected, this.sorter);
/* 56 */     return this.collected;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\SortingVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */