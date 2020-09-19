/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.zip.GZIPOutputStream;
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
/*     */ public final class GzCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8102;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   
/*     */   public GzCompressAction(File source, File destination, boolean deleteSource) {
/*  58 */     Objects.requireNonNull(source, "source");
/*  59 */     Objects.requireNonNull(destination, "destination");
/*     */     
/*  61 */     this.source = source;
/*  62 */     this.destination = destination;
/*  63 */     this.deleteSource = deleteSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws IOException {
/*  74 */     return execute(this.source, this.destination, this.deleteSource);
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
/*     */   public static boolean execute(File source, File destination, boolean deleteSource) throws IOException {
/*  89 */     if (source.exists()) {
/*  90 */       try(FileInputStream fis = new FileInputStream(source); 
/*  91 */           BufferedOutputStream os = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(destination)))) {
/*     */         
/*  93 */         byte[] inbuf = new byte[8102];
/*     */         
/*     */         int n;
/*  96 */         while ((n = fis.read(inbuf)) != -1) {
/*  97 */           os.write(inbuf, 0, n);
/*     */         }
/*     */       } 
/*     */       
/* 101 */       if (deleteSource && !source.delete()) {
/* 102 */         LOGGER.warn("Unable to delete " + source.toString() + '.');
/*     */       }
/*     */       
/* 105 */       return true;
/*     */     } 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 119 */     LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   public File getSource() {
/* 129 */     return this.source;
/*     */   }
/*     */   
/*     */   public File getDestination() {
/* 133 */     return this.destination;
/*     */   }
/*     */   
/*     */   public boolean isDeleteSource() {
/* 137 */     return this.deleteSource;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\GzCompressAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */