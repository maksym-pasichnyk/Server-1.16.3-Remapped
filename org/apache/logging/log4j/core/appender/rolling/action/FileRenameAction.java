/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileRenameAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean renameEmptyFiles;
/*     */   
/*     */   public FileRenameAction(File src, File dst, boolean renameEmptyFiles) {
/*  54 */     this.source = src;
/*  55 */     this.destination = dst;
/*  56 */     this.renameEmptyFiles = renameEmptyFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() {
/*  66 */     return execute(this.source, this.destination, this.renameEmptyFiles);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestination() {
/*  75 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSource() {
/*  84 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRenameEmptyFiles() {
/*  93 */     return this.renameEmptyFiles;
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
/*     */   public static boolean execute(File source, File destination, boolean renameEmptyFiles) {
/* 105 */     if (renameEmptyFiles || source.length() > 0L) {
/* 106 */       File parent = destination.getParentFile();
/* 107 */       if (parent != null && !parent.exists()) {
/*     */ 
/*     */ 
/*     */         
/* 111 */         parent.mkdirs();
/* 112 */         if (!parent.exists()) {
/* 113 */           LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
/* 114 */           return false;
/*     */         } 
/*     */       } 
/*     */       
/*     */       try {
/* 119 */         Files.move(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]), new CopyOption[] { StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING });
/*     */         
/* 121 */         LOGGER.trace("Renamed file {} to {} with Files.move", source.getAbsolutePath(), destination.getAbsolutePath());
/*     */         
/* 123 */         return true;
/* 124 */       } catch (IOException exMove) {
/* 125 */         LOGGER.error("Unable to move file {} to {}: {} {}", source.getAbsolutePath(), destination.getAbsolutePath(), exMove.getClass().getName(), exMove.getMessage());
/*     */         
/* 127 */         boolean result = source.renameTo(destination);
/* 128 */         if (!result) {
/*     */           try {
/* 130 */             Files.copy(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */             
/*     */             try {
/* 133 */               Files.delete(Paths.get(source.getAbsolutePath(), new String[0]));
/* 134 */               LOGGER.trace("Renamed file {} to {} using copy and delete", source.getAbsolutePath(), destination.getAbsolutePath());
/*     */             }
/* 136 */             catch (IOException exDelete) {
/* 137 */               LOGGER.error("Unable to delete file {}: {} {}", source.getAbsolutePath(), exDelete.getClass().getName(), exDelete.getMessage());
/*     */               
/*     */               try {
/* 140 */                 (new PrintWriter(source.getAbsolutePath())).close();
/* 141 */                 LOGGER.trace("Renamed file {} to {} with copy and truncation", source.getAbsolutePath(), destination.getAbsolutePath());
/*     */               }
/* 143 */               catch (IOException exOwerwrite) {
/* 144 */                 LOGGER.error("Unable to overwrite file {}: {} {}", source.getAbsolutePath(), exOwerwrite.getClass().getName(), exOwerwrite.getMessage());
/*     */               }
/*     */             
/*     */             }
/*     */           
/* 149 */           } catch (IOException exCopy) {
/* 150 */             LOGGER.error("Unable to copy file {} to {}: {} {}", source.getAbsolutePath(), destination.getAbsolutePath(), exCopy.getClass().getName(), exCopy.getMessage());
/*     */           } 
/*     */         } else {
/*     */           
/* 154 */           LOGGER.trace("Renamed file {} to {} with source.renameTo", source.getAbsolutePath(), destination.getAbsolutePath());
/*     */         } 
/*     */         
/* 157 */         return result;
/*     */       }
/* 159 */       catch (RuntimeException ex) {
/* 160 */         LOGGER.error("Unable to rename file {} to {}: {} {}", source.getAbsolutePath(), destination.getAbsolutePath(), ex.getClass().getName(), ex.getMessage());
/*     */       } 
/*     */     } else {
/*     */       
/*     */       try {
/* 165 */         source.delete();
/* 166 */       } catch (Exception exDelete) {
/* 167 */         LOGGER.error("Unable to delete empty file {}: {} {}", source.getAbsolutePath(), exDelete.getClass().getName(), exDelete.getMessage());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return FileRenameAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", renameEmptyFiles=" + this.renameEmptyFiles + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\FileRenameAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */