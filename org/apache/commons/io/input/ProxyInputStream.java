/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProxyInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   public ProxyInputStream(InputStream proxy) {
/*  47 */     super(proxy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     try {
/*  59 */       beforeRead(1);
/*  60 */       int b = this.in.read();
/*  61 */       afterRead((b != -1) ? 1 : -1);
/*  62 */       return b;
/*  63 */     } catch (IOException e) {
/*  64 */       handleIOException(e);
/*  65 */       return -1;
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
/*     */   public int read(byte[] bts) throws IOException {
/*     */     try {
/*  78 */       beforeRead((bts != null) ? bts.length : 0);
/*  79 */       int n = this.in.read(bts);
/*  80 */       afterRead(n);
/*  81 */       return n;
/*  82 */     } catch (IOException e) {
/*  83 */       handleIOException(e);
/*  84 */       return -1;
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
/*     */   public int read(byte[] bts, int off, int len) throws IOException {
/*     */     try {
/*  99 */       beforeRead(len);
/* 100 */       int n = this.in.read(bts, off, len);
/* 101 */       afterRead(n);
/* 102 */       return n;
/* 103 */     } catch (IOException e) {
/* 104 */       handleIOException(e);
/* 105 */       return -1;
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
/*     */   public long skip(long ln) throws IOException {
/*     */     try {
/* 118 */       return this.in.skip(ln);
/* 119 */     } catch (IOException e) {
/* 120 */       handleIOException(e);
/* 121 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*     */     try {
/* 133 */       return super.available();
/* 134 */     } catch (IOException e) {
/* 135 */       handleIOException(e);
/* 136 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 147 */       this.in.close();
/* 148 */     } catch (IOException e) {
/* 149 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 159 */     this.in.mark(readlimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 169 */       this.in.reset();
/* 170 */     } catch (IOException e) {
/* 171 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 181 */     return this.in.markSupported();
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
/*     */   protected void beforeRead(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterRead(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleIOException(IOException e) throws IOException {
/* 237 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\ProxyInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */