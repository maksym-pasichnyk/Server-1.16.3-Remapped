/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
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
/*     */ enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  44 */   MURMUR128_MITZ_32
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits)
/*     */     {
/*  48 */       long bitSize = bits.bitSize();
/*  49 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  50 */       int hash1 = (int)hash64;
/*  51 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  53 */       boolean bitsChanged = false;
/*  54 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  55 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  57 */         if (combinedHash < 0) {
/*  58 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  60 */         bitsChanged |= bits.set(combinedHash % bitSize);
/*     */       } 
/*  62 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
/*  68 */       long bitSize = bits.bitSize();
/*  69 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  70 */       int hash1 = (int)hash64;
/*  71 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  73 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  74 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  76 */         if (combinedHash < 0) {
/*  77 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  79 */         if (!bits.get(combinedHash % bitSize)) {
/*  80 */           return false;
/*     */         }
/*     */       } 
/*  83 */       return true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   MURMUR128_MITZ_64
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits)
/*     */     {
/*  96 */       long bitSize = bits.bitSize();
/*  97 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/*  98 */       long hash1 = lowerEight(bytes);
/*  99 */       long hash2 = upperEight(bytes);
/*     */       
/* 101 */       boolean bitsChanged = false;
/* 102 */       long combinedHash = hash1;
/* 103 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 105 */         bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
/* 106 */         combinedHash += hash2;
/*     */       } 
/* 108 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
/* 114 */       long bitSize = bits.bitSize();
/* 115 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/* 116 */       long hash1 = lowerEight(bytes);
/* 117 */       long hash2 = upperEight(bytes);
/*     */       
/* 119 */       long combinedHash = hash1;
/* 120 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 122 */         if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
/* 123 */           return false;
/*     */         }
/* 125 */         combinedHash += hash2;
/*     */       } 
/* 127 */       return true;
/*     */     }
/*     */     
/*     */     private long lowerEight(byte[] bytes) {
/* 131 */       return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     private long upperEight(byte[] bytes) {
/* 136 */       return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
/*     */     }
/*     */   };
/*     */ 
/*     */   
/*     */   static final class BitArray
/*     */   {
/*     */     final long[] data;
/*     */     long bitCount;
/*     */     
/*     */     BitArray(long bits) {
/* 147 */       this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
/*     */     }
/*     */ 
/*     */     
/*     */     BitArray(long[] data) {
/* 152 */       Preconditions.checkArgument((data.length > 0), "data length is zero!");
/* 153 */       this.data = data;
/* 154 */       long bitCount = 0L;
/* 155 */       for (long value : data) {
/* 156 */         bitCount += Long.bitCount(value);
/*     */       }
/* 158 */       this.bitCount = bitCount;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean set(long index) {
/* 163 */       if (!get(index)) {
/* 164 */         this.data[(int)(index >>> 6L)] = this.data[(int)(index >>> 6L)] | 1L << (int)index;
/* 165 */         this.bitCount++;
/* 166 */         return true;
/*     */       } 
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     boolean get(long index) {
/* 172 */       return ((this.data[(int)(index >>> 6L)] & 1L << (int)index) != 0L);
/*     */     }
/*     */ 
/*     */     
/*     */     long bitSize() {
/* 177 */       return this.data.length * 64L;
/*     */     }
/*     */ 
/*     */     
/*     */     long bitCount() {
/* 182 */       return this.bitCount;
/*     */     }
/*     */     
/*     */     BitArray copy() {
/* 186 */       return new BitArray((long[])this.data.clone());
/*     */     }
/*     */ 
/*     */     
/*     */     void putAll(BitArray array) {
/* 191 */       Preconditions.checkArgument((this.data.length == array.data.length), "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       this.bitCount = 0L;
/* 197 */       for (int i = 0; i < this.data.length; i++) {
/* 198 */         this.data[i] = this.data[i] | array.data[i];
/* 199 */         this.bitCount += Long.bitCount(this.data[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 205 */       if (o instanceof BitArray) {
/* 206 */         BitArray bitArray = (BitArray)o;
/* 207 */         return Arrays.equals(this.data, bitArray.data);
/*     */       } 
/* 209 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 214 */       return Arrays.hashCode(this.data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */