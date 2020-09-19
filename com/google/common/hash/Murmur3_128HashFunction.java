/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ final class Murmur3_128HashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final int seed;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_128HashFunction(int seed) {
/*  47 */     this.seed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  52 */     return 128;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  57 */     return new Murmur3_128Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  62 */     return "Hashing.murmur3_128(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  67 */     if (object instanceof Murmur3_128HashFunction) {
/*  68 */       Murmur3_128HashFunction other = (Murmur3_128HashFunction)object;
/*  69 */       return (this.seed == other.seed);
/*     */     } 
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  76 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */   
/*     */   private static final class Murmur3_128Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
/*     */     private static final int CHUNK_SIZE = 16;
/*     */     private static final long C1 = -8663945395140668459L;
/*     */     private static final long C2 = 5545529020109919103L;
/*     */     private long h1;
/*     */     private long h2;
/*     */     private int length;
/*     */     
/*     */     Murmur3_128Hasher(int seed) {
/*  88 */       super(16);
/*  89 */       this.h1 = seed;
/*  90 */       this.h2 = seed;
/*  91 */       this.length = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer bb) {
/*  96 */       long k1 = bb.getLong();
/*  97 */       long k2 = bb.getLong();
/*  98 */       bmix64(k1, k2);
/*  99 */       this.length += 16;
/*     */     }
/*     */     
/*     */     private void bmix64(long k1, long k2) {
/* 103 */       this.h1 ^= mixK1(k1);
/*     */       
/* 105 */       this.h1 = Long.rotateLeft(this.h1, 27);
/* 106 */       this.h1 += this.h2;
/* 107 */       this.h1 = this.h1 * 5L + 1390208809L;
/*     */       
/* 109 */       this.h2 ^= mixK2(k2);
/*     */       
/* 111 */       this.h2 = Long.rotateLeft(this.h2, 31);
/* 112 */       this.h2 += this.h1;
/* 113 */       this.h2 = this.h2 * 5L + 944331445L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 118 */       long k1 = 0L;
/* 119 */       long k2 = 0L;
/* 120 */       this.length += bb.remaining();
/* 121 */       switch (bb.remaining()) {
/*     */         case 15:
/* 123 */           k2 ^= UnsignedBytes.toInt(bb.get(14)) << 48L;
/*     */         case 14:
/* 125 */           k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40L;
/*     */         case 13:
/* 127 */           k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32L;
/*     */         case 12:
/* 129 */           k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24L;
/*     */         case 11:
/* 131 */           k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16L;
/*     */         case 10:
/* 133 */           k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8L;
/*     */         case 9:
/* 135 */           k2 ^= UnsignedBytes.toInt(bb.get(8));
/*     */         case 8:
/* 137 */           k1 ^= bb.getLong();
/*     */           break;
/*     */         case 7:
/* 140 */           k1 ^= UnsignedBytes.toInt(bb.get(6)) << 48L;
/*     */         case 6:
/* 142 */           k1 ^= UnsignedBytes.toInt(bb.get(5)) << 40L;
/*     */         case 5:
/* 144 */           k1 ^= UnsignedBytes.toInt(bb.get(4)) << 32L;
/*     */         case 4:
/* 146 */           k1 ^= UnsignedBytes.toInt(bb.get(3)) << 24L;
/*     */         case 3:
/* 148 */           k1 ^= UnsignedBytes.toInt(bb.get(2)) << 16L;
/*     */         case 2:
/* 150 */           k1 ^= UnsignedBytes.toInt(bb.get(1)) << 8L;
/*     */         case 1:
/* 152 */           k1 ^= UnsignedBytes.toInt(bb.get(0));
/*     */           break;
/*     */         default:
/* 155 */           throw new AssertionError("Should never get here.");
/*     */       } 
/* 157 */       this.h1 ^= mixK1(k1);
/* 158 */       this.h2 ^= mixK2(k2);
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 163 */       this.h1 ^= this.length;
/* 164 */       this.h2 ^= this.length;
/*     */       
/* 166 */       this.h1 += this.h2;
/* 167 */       this.h2 += this.h1;
/*     */       
/* 169 */       this.h1 = fmix64(this.h1);
/* 170 */       this.h2 = fmix64(this.h2);
/*     */       
/* 172 */       this.h1 += this.h2;
/* 173 */       this.h2 += this.h1;
/*     */       
/* 175 */       return HashCode.fromBytesNoCopy(
/* 176 */           ByteBuffer.wrap(new byte[16])
/* 177 */           .order(ByteOrder.LITTLE_ENDIAN)
/* 178 */           .putLong(this.h1)
/* 179 */           .putLong(this.h2)
/* 180 */           .array());
/*     */     }
/*     */     
/*     */     private static long fmix64(long k) {
/* 184 */       k ^= k >>> 33L;
/* 185 */       k *= -49064778989728563L;
/* 186 */       k ^= k >>> 33L;
/* 187 */       k *= -4265267296055464877L;
/* 188 */       k ^= k >>> 33L;
/* 189 */       return k;
/*     */     }
/*     */     
/*     */     private static long mixK1(long k1) {
/* 193 */       k1 *= -8663945395140668459L;
/* 194 */       k1 = Long.rotateLeft(k1, 31);
/* 195 */       k1 *= 5545529020109919103L;
/* 196 */       return k1;
/*     */     }
/*     */     
/*     */     private static long mixK2(long k2) {
/* 200 */       k2 *= 5545529020109919103L;
/* 201 */       k2 = Long.rotateLeft(k2, 33);
/* 202 */       k2 *= -8663945395140668459L;
/* 203 */       return k2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\Murmur3_128HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */