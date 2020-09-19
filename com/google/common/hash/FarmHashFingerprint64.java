/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ final class FarmHashFingerprint64
/*     */   extends AbstractNonStreamingHashFunction
/*     */ {
/*     */   private static final long K0 = -4348849565147123417L;
/*     */   private static final long K1 = -5435081209227447693L;
/*     */   private static final long K2 = -7286425919675154353L;
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  50 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/*  51 */     return HashCode.fromLong(fingerprint(input, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  56 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  61 */     return "Hashing.farmHashFingerprint64()";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long fingerprint(byte[] bytes, int offset, int length) {
/*  68 */     if (length <= 32) {
/*  69 */       if (length <= 16) {
/*  70 */         return hashLength0to16(bytes, offset, length);
/*     */       }
/*  72 */       return hashLength17to32(bytes, offset, length);
/*     */     } 
/*  74 */     if (length <= 64) {
/*  75 */       return hashLength33To64(bytes, offset, length);
/*     */     }
/*  77 */     return hashLength65Plus(bytes, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long shiftMix(long val) {
/*  82 */     return val ^ val >>> 47L;
/*     */   }
/*     */   
/*     */   private static long hashLength16(long u, long v, long mul) {
/*  86 */     long a = (u ^ v) * mul;
/*  87 */     a ^= a >>> 47L;
/*  88 */     long b = (v ^ a) * mul;
/*  89 */     b ^= b >>> 47L;
/*  90 */     b *= mul;
/*  91 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
/* 101 */     long part1 = LittleEndianByteArray.load64(bytes, offset);
/* 102 */     long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
/* 103 */     long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
/* 104 */     long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
/*     */     
/* 106 */     seedA += part1;
/* 107 */     seedB = Long.rotateRight(seedB + seedA + part4, 21);
/* 108 */     long c = seedA;
/* 109 */     seedA += part2;
/* 110 */     seedA += part3;
/* 111 */     seedB += Long.rotateRight(seedA, 44);
/* 112 */     output[0] = seedA + part4;
/* 113 */     output[1] = seedB + c;
/*     */   }
/*     */   
/*     */   private static long hashLength0to16(byte[] bytes, int offset, int length) {
/* 117 */     if (length >= 8) {
/* 118 */       long mul = -7286425919675154353L + (length * 2);
/* 119 */       long a = LittleEndianByteArray.load64(bytes, offset) + -7286425919675154353L;
/* 120 */       long b = LittleEndianByteArray.load64(bytes, offset + length - 8);
/* 121 */       long c = Long.rotateRight(b, 37) * mul + a;
/* 122 */       long d = (Long.rotateRight(a, 25) + b) * mul;
/* 123 */       return hashLength16(c, d, mul);
/*     */     } 
/* 125 */     if (length >= 4) {
/* 126 */       long mul = -7286425919675154353L + (length * 2);
/* 127 */       long a = LittleEndianByteArray.load32(bytes, offset) & 0xFFFFFFFFL;
/* 128 */       return hashLength16(length + (a << 3L), LittleEndianByteArray.load32(bytes, offset + length - 4) & 0xFFFFFFFFL, mul);
/*     */     } 
/* 130 */     if (length > 0) {
/* 131 */       byte a = bytes[offset];
/* 132 */       byte b = bytes[offset + (length >> 1)];
/* 133 */       byte c = bytes[offset + length - 1];
/* 134 */       int y = (a & 0xFF) + ((b & 0xFF) << 8);
/* 135 */       int z = length + ((c & 0xFF) << 2);
/* 136 */       return shiftMix(y * -7286425919675154353L ^ z * -4348849565147123417L) * -7286425919675154353L;
/*     */     } 
/* 138 */     return -7286425919675154353L;
/*     */   }
/*     */   
/*     */   private static long hashLength17to32(byte[] bytes, int offset, int length) {
/* 142 */     long mul = -7286425919675154353L + (length * 2);
/* 143 */     long a = LittleEndianByteArray.load64(bytes, offset) * -5435081209227447693L;
/* 144 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 145 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 146 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 147 */     return hashLength16(
/* 148 */         Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/*     */   }
/*     */   
/*     */   private static long hashLength33To64(byte[] bytes, int offset, int length) {
/* 152 */     long mul = -7286425919675154353L + (length * 2);
/* 153 */     long a = LittleEndianByteArray.load64(bytes, offset) * -7286425919675154353L;
/* 154 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 155 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 156 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 157 */     long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
/* 158 */     long z = hashLength16(y, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/* 159 */     long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
/* 160 */     long f = LittleEndianByteArray.load64(bytes, offset + 24);
/* 161 */     long g = (y + LittleEndianByteArray.load64(bytes, offset + length - 32)) * mul;
/* 162 */     long h = (z + LittleEndianByteArray.load64(bytes, offset + length - 24)) * mul;
/* 163 */     return hashLength16(
/* 164 */         Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long hashLength65Plus(byte[] bytes, int offset, int length) {
/* 171 */     int seed = 81;
/*     */     
/* 173 */     long x = 81L;
/*     */     
/* 175 */     long y = 2480279821605975764L;
/* 176 */     long z = shiftMix(y * -7286425919675154353L + 113L) * -7286425919675154353L;
/* 177 */     long[] v = new long[2], w = new long[2];
/* 178 */     x = x * -7286425919675154353L + LittleEndianByteArray.load64(bytes, offset);
/*     */ 
/*     */     
/* 181 */     int end = offset + (length - 1) / 64 * 64;
/* 182 */     int last64offset = end + (length - 1 & 0x3F) - 63;
/*     */     while (true) {
/* 184 */       x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * -5435081209227447693L;
/* 185 */       y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * -5435081209227447693L;
/* 186 */       x ^= w[1];
/* 187 */       y += v[0] + LittleEndianByteArray.load64(bytes, offset + 40);
/* 188 */       z = Long.rotateRight(z + w[0], 33) * -5435081209227447693L;
/* 189 */       weakHashLength32WithSeeds(bytes, offset, v[1] * -5435081209227447693L, x + w[0], v);
/* 190 */       weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 191 */       long tmp = x;
/* 192 */       x = z;
/* 193 */       z = tmp;
/* 194 */       offset += 64;
/* 195 */       if (offset == end) {
/* 196 */         long mul = -5435081209227447693L + ((z & 0xFFL) << 1L);
/*     */         
/* 198 */         offset = last64offset;
/* 199 */         w[0] = w[0] + (length - 1 & 0x3F);
/* 200 */         v[0] = v[0] + w[0];
/* 201 */         w[0] = w[0] + v[0];
/* 202 */         x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * mul;
/* 203 */         y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * mul;
/* 204 */         x ^= w[1] * 9L;
/* 205 */         y += v[0] * 9L + LittleEndianByteArray.load64(bytes, offset + 40);
/* 206 */         z = Long.rotateRight(z + w[0], 33) * mul;
/* 207 */         weakHashLength32WithSeeds(bytes, offset, v[1] * mul, x + w[0], v);
/* 208 */         weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 209 */         return hashLength16(
/* 210 */             hashLength16(v[0], w[0], mul) + shiftMix(y) * -4348849565147123417L + x, 
/* 211 */             hashLength16(v[1], w[1], mul) + z, mul);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\FarmHashFingerprint64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */