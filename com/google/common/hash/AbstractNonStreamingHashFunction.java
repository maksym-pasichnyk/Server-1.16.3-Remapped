/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractNonStreamingHashFunction
/*     */   implements HashFunction
/*     */ {
/*     */   public Hasher newHasher() {
/*  32 */     return new BufferingHasher(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  37 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  38 */     return new BufferingHasher(expectedInputSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
/*  43 */     return newHasher().<T>putObject(instance, funnel).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  48 */     int len = input.length();
/*  49 */     Hasher hasher = newHasher(len * 2);
/*  50 */     for (int i = 0; i < len; i++) {
/*  51 */       hasher.putChar(input.charAt(i));
/*     */     }
/*  53 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  58 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  63 */     return newHasher(4).putInt(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  68 */     return newHasher(8).putLong(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(byte[] input) {
/*  73 */     return hashBytes(input, 0, input.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private final class BufferingHasher
/*     */     extends AbstractHasher
/*     */   {
/*     */     final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
/*     */     static final int BOTTOM_BYTE = 255;
/*     */     
/*     */     BufferingHasher(int expectedInputSize) {
/*  84 */       this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/*  89 */       this.stream.write(b);
/*  90 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes) {
/*     */       try {
/*  96 */         this.stream.write(bytes);
/*  97 */       } catch (IOException e) {
/*  98 */         throw new RuntimeException(e);
/*     */       } 
/* 100 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/* 105 */       this.stream.write(bytes, off, len);
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putShort(short s) {
/* 111 */       this.stream.write(s & 0xFF);
/* 112 */       this.stream.write(s >>> 8 & 0xFF);
/* 113 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putInt(int i) {
/* 118 */       this.stream.write(i & 0xFF);
/* 119 */       this.stream.write(i >>> 8 & 0xFF);
/* 120 */       this.stream.write(i >>> 16 & 0xFF);
/* 121 */       this.stream.write(i >>> 24 & 0xFF);
/* 122 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putLong(long l) {
/* 127 */       for (int i = 0; i < 64; i += 8) {
/* 128 */         this.stream.write((byte)(int)(l >>> i & 0xFFL));
/*     */       }
/* 130 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putChar(char c) {
/* 135 */       this.stream.write(c & 0xFF);
/* 136 */       this.stream.write(c >>> 8 & 0xFF);
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 142 */       funnel.funnel(instance, this);
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 148 */       return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExposedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream {
/*     */     ExposedByteArrayOutputStream(int expectedInputSize) {
/* 155 */       super(expectedInputSize);
/*     */     }
/*     */     
/*     */     byte[] byteArray() {
/* 159 */       return this.buf;
/*     */     }
/*     */     
/*     */     int length() {
/* 163 */       return this.count;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\AbstractNonStreamingHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */