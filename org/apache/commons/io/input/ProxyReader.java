/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProxyReader
/*     */   extends FilterReader
/*     */ {
/*     */   public ProxyReader(Reader proxy) {
/*  45 */     super(proxy);
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
/*  57 */       beforeRead(1);
/*  58 */       int c = this.in.read();
/*  59 */       afterRead((c != -1) ? 1 : -1);
/*  60 */       return c;
/*  61 */     } catch (IOException e) {
/*  62 */       handleIOException(e);
/*  63 */       return -1;
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
/*     */   public int read(char[] chr) throws IOException {
/*     */     try {
/*  76 */       beforeRead((chr != null) ? chr.length : 0);
/*  77 */       int n = this.in.read(chr);
/*  78 */       afterRead(n);
/*  79 */       return n;
/*  80 */     } catch (IOException e) {
/*  81 */       handleIOException(e);
/*  82 */       return -1;
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
/*     */   public int read(char[] chr, int st, int len) throws IOException {
/*     */     try {
/*  97 */       beforeRead(len);
/*  98 */       int n = this.in.read(chr, st, len);
/*  99 */       afterRead(n);
/* 100 */       return n;
/* 101 */     } catch (IOException e) {
/* 102 */       handleIOException(e);
/* 103 */       return -1;
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
/*     */   public int read(CharBuffer target) throws IOException {
/*     */     try {
/* 117 */       beforeRead((target != null) ? target.length() : 0);
/* 118 */       int n = this.in.read(target);
/* 119 */       afterRead(n);
/* 120 */       return n;
/* 121 */     } catch (IOException e) {
/* 122 */       handleIOException(e);
/* 123 */       return -1;
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
/* 136 */       return this.in.skip(ln);
/* 137 */     } catch (IOException e) {
/* 138 */       handleIOException(e);
/* 139 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/*     */     try {
/* 151 */       return this.in.ready();
/* 152 */     } catch (IOException e) {
/* 153 */       handleIOException(e);
/* 154 */       return false;
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
/* 165 */       this.in.close();
/* 166 */     } catch (IOException e) {
/* 167 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int idx) throws IOException {
/*     */     try {
/* 179 */       this.in.mark(idx);
/* 180 */     } catch (IOException e) {
/* 181 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 192 */       this.in.reset();
/* 193 */     } catch (IOException e) {
/* 194 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 204 */     return this.in.markSupported();
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
/*     */   protected void handleIOException(IOException e) throws IOException {
/* 258 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\ProxyReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */