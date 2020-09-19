/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredFileOutputStream
/*     */   extends ThresholdingOutputStream
/*     */ {
/*     */   private ByteArrayOutputStream memoryOutputStream;
/*     */   private OutputStream currentOutputStream;
/*     */   private File outputFile;
/*     */   private final String prefix;
/*     */   private final String suffix;
/*     */   private final File directory;
/*     */   private boolean closed = false;
/*     */   
/*     */   public DeferredFileOutputStream(int threshold, File outputFile) {
/* 101 */     this(threshold, outputFile, null, null, null);
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
/*     */   public DeferredFileOutputStream(int threshold, String prefix, String suffix, File directory) {
/* 118 */     this(threshold, null, prefix, suffix, directory);
/* 119 */     if (prefix == null) {
/* 120 */       throw new IllegalArgumentException("Temporary file prefix is missing");
/*     */     }
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
/*     */   private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory) {
/* 136 */     super(threshold);
/* 137 */     this.outputFile = outputFile;
/*     */     
/* 139 */     this.memoryOutputStream = new ByteArrayOutputStream();
/* 140 */     this.currentOutputStream = this.memoryOutputStream;
/* 141 */     this.prefix = prefix;
/* 142 */     this.suffix = suffix;
/* 143 */     this.directory = directory;
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
/*     */   protected OutputStream getStream() throws IOException {
/* 161 */     return this.currentOutputStream;
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
/*     */   protected void thresholdReached() throws IOException {
/* 176 */     if (this.prefix != null) {
/* 177 */       this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
/*     */     }
/* 179 */     FileOutputStream fos = new FileOutputStream(this.outputFile);
/*     */     try {
/* 181 */       this.memoryOutputStream.writeTo(fos);
/* 182 */     } catch (IOException e) {
/* 183 */       fos.close();
/* 184 */       throw e;
/*     */     } 
/* 186 */     this.currentOutputStream = fos;
/* 187 */     this.memoryOutputStream = null;
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
/*     */   public boolean isInMemory() {
/* 203 */     return !isThresholdExceeded();
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
/*     */   public byte[] getData() {
/* 217 */     if (this.memoryOutputStream != null)
/*     */     {
/* 219 */       return this.memoryOutputStream.toByteArray();
/*     */     }
/* 221 */     return null;
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
/*     */   public File getFile() {
/* 241 */     return this.outputFile;
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
/*     */   public void close() throws IOException {
/* 253 */     super.close();
/* 254 */     this.closed = true;
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
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 270 */     if (!this.closed)
/*     */     {
/* 272 */       throw new IOException("Stream not closed");
/*     */     }
/*     */     
/* 275 */     if (isInMemory()) {
/*     */       
/* 277 */       this.memoryOutputStream.writeTo(out);
/*     */     }
/*     */     else {
/*     */       
/* 281 */       FileInputStream fis = new FileInputStream(this.outputFile);
/*     */       try {
/* 283 */         IOUtils.copy(fis, out);
/*     */       } finally {
/* 285 */         IOUtils.closeQuietly(fis);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\output\DeferredFileOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */