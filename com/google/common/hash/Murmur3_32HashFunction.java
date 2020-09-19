/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Murmur3_32HashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private final int seed;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_32HashFunction(int seed) {
/*  53 */     this.seed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  58 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  63 */     return new Murmur3_32Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     return "Hashing.murmur3_32(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  73 */     if (object instanceof Murmur3_32HashFunction) {
/*  74 */       Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
/*  75 */       return (this.seed == other.seed);
/*     */     } 
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  82 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  87 */     int k1 = mixK1(input);
/*  88 */     int h1 = mixH1(this.seed, k1);
/*     */     
/*  90 */     return fmix(h1, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  95 */     int low = (int)input;
/*  96 */     int high = (int)(input >>> 32L);
/*     */     
/*  98 */     int k1 = mixK1(low);
/*  99 */     int h1 = mixH1(this.seed, k1);
/*     */     
/* 101 */     k1 = mixK1(high);
/* 102 */     h1 = mixH1(h1, k1);
/*     */     
/* 104 */     return fmix(h1, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/* 110 */     int h1 = this.seed;
/*     */ 
/*     */     
/* 113 */     for (int i = 1; i < input.length(); i += 2) {
/* 114 */       int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
/* 115 */       k1 = mixK1(k1);
/* 116 */       h1 = mixH1(h1, k1);
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if ((input.length() & 0x1) == 1) {
/* 121 */       int k1 = input.charAt(input.length() - 1);
/* 122 */       k1 = mixK1(k1);
/* 123 */       h1 ^= k1;
/*     */     } 
/*     */     
/* 126 */     return fmix(h1, 2 * input.length());
/*     */   }
/*     */   
/*     */   private static int mixK1(int k1) {
/* 130 */     k1 *= -862048943;
/* 131 */     k1 = Integer.rotateLeft(k1, 15);
/* 132 */     k1 *= 461845907;
/* 133 */     return k1;
/*     */   }
/*     */   
/*     */   private static int mixH1(int h1, int k1) {
/* 137 */     h1 ^= k1;
/* 138 */     h1 = Integer.rotateLeft(h1, 13);
/* 139 */     h1 = h1 * 5 + -430675100;
/* 140 */     return h1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashCode fmix(int h1, int length) {
/* 145 */     h1 ^= length;
/* 146 */     h1 ^= h1 >>> 16;
/* 147 */     h1 *= -2048144789;
/* 148 */     h1 ^= h1 >>> 13;
/* 149 */     h1 *= -1028477387;
/* 150 */     h1 ^= h1 >>> 16;
/* 151 */     return HashCode.fromInt(h1);
/*     */   }
/*     */   
/*     */   private static final class Murmur3_32Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
/*     */     private static final int CHUNK_SIZE = 4;
/*     */     private int h1;
/*     */     private int length;
/*     */     
/*     */     Murmur3_32Hasher(int seed) {
/* 160 */       super(4);
/* 161 */       this.h1 = seed;
/* 162 */       this.length = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer bb) {
/* 167 */       int k1 = Murmur3_32HashFunction.mixK1(bb.getInt());
/* 168 */       this.h1 = Murmur3_32HashFunction.mixH1(this.h1, k1);
/* 169 */       this.length += 4;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 174 */       this.length += bb.remaining();
/* 175 */       int k1 = 0;
/* 176 */       for (int i = 0; bb.hasRemaining(); i += 8) {
/* 177 */         k1 ^= UnsignedBytes.toInt(bb.get()) << i;
/*     */       }
/* 179 */       this.h1 ^= Murmur3_32HashFunction.mixK1(k1);
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 184 */       return Murmur3_32HashFunction.fmix(this.h1, this.length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\Murmur3_32HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */