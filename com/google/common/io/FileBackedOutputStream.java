/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private OutputStream out;
/*     */   private MemoryOutput memory;
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() {}
/*     */     
/*     */     byte[] getBuffer() {
/*  53 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  57 */       return this.count;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized File getFile() {
/*  64 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBackedOutputStream(int fileThreshold) {
/*  74 */     this(fileThreshold, false);
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
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/*  86 */     this.fileThreshold = fileThreshold;
/*  87 */     this.resetOnFinalize = resetOnFinalize;
/*  88 */     this.memory = new MemoryOutput();
/*  89 */     this.out = this.memory;
/*     */     
/*  91 */     if (resetOnFinalize) {
/*  92 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/*  96 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void finalize() {
/*     */             try {
/* 102 */               FileBackedOutputStream.this.reset();
/* 103 */             } catch (Throwable t) {
/* 104 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 109 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 113 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteSource asByteSource() {
/* 125 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 129 */     if (this.file != null) {
/* 130 */       return new FileInputStream(this.file);
/*     */     }
/* 132 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 144 */       close();
/*     */     } finally {
/* 146 */       if (this.memory == null) {
/* 147 */         this.memory = new MemoryOutput();
/*     */       } else {
/* 149 */         this.memory.reset();
/*     */       } 
/* 151 */       this.out = this.memory;
/* 152 */       if (this.file != null) {
/* 153 */         File deleteMe = this.file;
/* 154 */         this.file = null;
/* 155 */         if (!deleteMe.delete()) {
/* 156 */           throw new IOException("Could not delete: " + deleteMe);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 164 */     update(1);
/* 165 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException {
/* 170 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException {
/* 175 */     update(len);
/* 176 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 181 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 186 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void update(int len) throws IOException {
/* 194 */     if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
/* 195 */       File temp = File.createTempFile("FileBackedOutputStream", null);
/* 196 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 199 */         temp.deleteOnExit();
/*     */       }
/* 201 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 202 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 203 */       transfer.flush();
/*     */ 
/*     */       
/* 206 */       this.out = transfer;
/* 207 */       this.file = temp;
/* 208 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\FileBackedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */