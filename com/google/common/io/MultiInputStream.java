/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
/*  44 */     this.it = (Iterator<? extends ByteSource>)Preconditions.checkNotNull(it);
/*  45 */     advance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  50 */     if (this.in != null) {
/*     */       try {
/*  52 */         this.in.close();
/*     */       } finally {
/*  54 */         this.in = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void advance() throws IOException {
/*  63 */     close();
/*  64 */     if (this.it.hasNext()) {
/*  65 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  71 */     if (this.in == null) {
/*  72 */       return 0;
/*     */     }
/*  74 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  84 */     if (this.in == null) {
/*  85 */       return -1;
/*     */     }
/*  87 */     int result = this.in.read();
/*  88 */     if (result == -1) {
/*  89 */       advance();
/*  90 */       return read();
/*     */     } 
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(@Nullable byte[] b, int off, int len) throws IOException {
/*  97 */     if (this.in == null) {
/*  98 */       return -1;
/*     */     }
/* 100 */     int result = this.in.read(b, off, len);
/* 101 */     if (result == -1) {
/* 102 */       advance();
/* 103 */       return read(b, off, len);
/*     */     } 
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 110 */     if (this.in == null || n <= 0L) {
/* 111 */       return 0L;
/*     */     }
/* 113 */     long result = this.in.skip(n);
/* 114 */     if (result != 0L) {
/* 115 */       return result;
/*     */     }
/* 117 */     if (read() == -1) {
/* 118 */       return 0L;
/*     */     }
/* 120 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\MultiInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */