/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceReader
/*     */   extends Reader
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3724187752191401220L;
/*     */   private final CharSequence charSequence;
/*     */   private int idx;
/*     */   private int mark;
/*     */   
/*     */   public CharSequenceReader(CharSequence charSequence) {
/*  46 */     this.charSequence = (charSequence != null) ? charSequence : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  54 */     this.idx = 0;
/*  55 */     this.mark = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) {
/*  65 */     this.mark = this.idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() {
/*  86 */     if (this.idx >= this.charSequence.length()) {
/*  87 */       return -1;
/*     */     }
/*  89 */     return this.charSequence.charAt(this.idx++);
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
/*     */   public int read(char[] array, int offset, int length) {
/* 104 */     if (this.idx >= this.charSequence.length()) {
/* 105 */       return -1;
/*     */     }
/* 107 */     if (array == null) {
/* 108 */       throw new NullPointerException("Character array is missing");
/*     */     }
/* 110 */     if (length < 0 || offset < 0 || offset + length > array.length) {
/* 111 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
/*     */     }
/*     */     
/* 114 */     int count = 0;
/* 115 */     for (int i = 0; i < length; i++) {
/* 116 */       int c = read();
/* 117 */       if (c == -1) {
/* 118 */         return count;
/*     */       }
/* 120 */       array[offset + i] = (char)c;
/* 121 */       count++;
/*     */     } 
/* 123 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 132 */     this.idx = this.mark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) {
/* 143 */     if (n < 0L) {
/* 144 */       throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
/*     */     }
/*     */     
/* 147 */     if (this.idx >= this.charSequence.length()) {
/* 148 */       return -1L;
/*     */     }
/* 150 */     int dest = (int)Math.min(this.charSequence.length(), this.idx + n);
/* 151 */     int count = dest - this.idx;
/* 152 */     this.idx = dest;
/* 153 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     return this.charSequence.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\CharSequenceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */