/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundedReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int INVALID = -1;
/*     */   private final Reader target;
/*  45 */   private int charsRead = 0;
/*     */   
/*  47 */   private int markedAt = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private int readAheadLimit;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxCharsFromTargetReader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundedReader(Reader target, int maxCharsFromTargetReader) throws IOException {
/*  61 */     this.target = target;
/*  62 */     this.maxCharsFromTargetReader = maxCharsFromTargetReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  72 */     this.target.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/*  82 */     this.charsRead = this.markedAt;
/*  83 */     this.target.reset();
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
/*     */   public void mark(int readAheadLimit) throws IOException {
/* 100 */     this.readAheadLimit = readAheadLimit - this.charsRead;
/*     */     
/* 102 */     this.markedAt = this.charsRead;
/*     */     
/* 104 */     this.target.mark(readAheadLimit);
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
/*     */   public int read() throws IOException {
/* 116 */     if (this.charsRead >= this.maxCharsFromTargetReader) {
/* 117 */       return -1;
/*     */     }
/*     */     
/* 120 */     if (this.markedAt >= 0 && this.charsRead - this.markedAt >= this.readAheadLimit) {
/* 121 */       return -1;
/*     */     }
/* 123 */     this.charsRead++;
/* 124 */     return this.target.read();
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
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 139 */     for (int i = 0; i < len; i++) {
/* 140 */       int c = read();
/* 141 */       if (c == -1) {
/* 142 */         return i;
/*     */       }
/* 144 */       cbuf[off + i] = (char)c;
/*     */     } 
/* 146 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\BoundedReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */