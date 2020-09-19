/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AppendableCharSequence
/*     */   implements CharSequence, Appendable
/*     */ {
/*     */   private char[] chars;
/*     */   private int pos;
/*     */   
/*     */   public AppendableCharSequence(int length) {
/*  26 */     if (length < 1) {
/*  27 */       throw new IllegalArgumentException("length: " + length + " (length: >= 1)");
/*     */     }
/*  29 */     this.chars = new char[length];
/*     */   }
/*     */   
/*     */   private AppendableCharSequence(char[] chars) {
/*  33 */     if (chars.length < 1) {
/*  34 */       throw new IllegalArgumentException("length: " + chars.length + " (length: >= 1)");
/*     */     }
/*  36 */     this.chars = chars;
/*  37 */     this.pos = chars.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/*  42 */     return this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  47 */     if (index > this.pos) {
/*  48 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  50 */     return this.chars[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char charAtUnsafe(int index) {
/*  61 */     return this.chars[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public AppendableCharSequence subSequence(int start, int end) {
/*  66 */     return new AppendableCharSequence(Arrays.copyOfRange(this.chars, start, end));
/*     */   }
/*     */ 
/*     */   
/*     */   public AppendableCharSequence append(char c) {
/*  71 */     if (this.pos == this.chars.length) {
/*  72 */       char[] old = this.chars;
/*  73 */       this.chars = new char[old.length << 1];
/*  74 */       System.arraycopy(old, 0, this.chars, 0, old.length);
/*     */     } 
/*  76 */     this.chars[this.pos++] = c;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AppendableCharSequence append(CharSequence csq) {
/*  82 */     return append(csq, 0, csq.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public AppendableCharSequence append(CharSequence csq, int start, int end) {
/*  87 */     if (csq.length() < end) {
/*  88 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  90 */     int length = end - start;
/*  91 */     if (length > this.chars.length - this.pos) {
/*  92 */       this.chars = expand(this.chars, this.pos + length, this.pos);
/*     */     }
/*  94 */     if (csq instanceof AppendableCharSequence) {
/*     */       
/*  96 */       AppendableCharSequence seq = (AppendableCharSequence)csq;
/*  97 */       char[] src = seq.chars;
/*  98 */       System.arraycopy(src, start, this.chars, this.pos, length);
/*  99 */       this.pos += length;
/* 100 */       return this;
/*     */     } 
/* 102 */     for (int i = start; i < end; i++) {
/* 103 */       this.chars[this.pos++] = csq.charAt(i);
/*     */     }
/*     */     
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 114 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     return new String(this.chars, 0, this.pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String substring(int start, int end) {
/* 126 */     int length = end - start;
/* 127 */     if (start > this.pos || length > this.pos) {
/* 128 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 130 */     return new String(this.chars, start, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String subStringUnsafe(int start, int end) {
/* 139 */     return new String(this.chars, start, end - start);
/*     */   }
/*     */   
/*     */   private static char[] expand(char[] array, int neededSpace, int size) {
/* 143 */     int newCapacity = array.length;
/*     */     
/*     */     do {
/* 146 */       newCapacity <<= 1;
/*     */       
/* 148 */       if (newCapacity < 0) {
/* 149 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/* 152 */     while (neededSpace > newCapacity);
/*     */     
/* 154 */     char[] newArray = new char[newCapacity];
/* 155 */     System.arraycopy(array, 0, newArray, 0, size);
/*     */     
/* 157 */     return newArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\AppendableCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */