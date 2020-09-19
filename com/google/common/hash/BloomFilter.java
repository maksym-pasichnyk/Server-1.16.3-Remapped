/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class BloomFilter<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private final BloomFilterStrategies.BitArray bits;
/*     */   private final int numHashFunctions;
/*     */   private final Funnel<? super T> funnel;
/*     */   private final Strategy strategy;
/*     */   
/*     */   private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<? super T> funnel, Strategy strategy) {
/* 112 */     Preconditions.checkArgument((numHashFunctions > 0), "numHashFunctions (%s) must be > 0", numHashFunctions);
/* 113 */     Preconditions.checkArgument((numHashFunctions <= 255), "numHashFunctions (%s) must be <= 255", numHashFunctions);
/*     */     
/* 115 */     this.bits = (BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits);
/* 116 */     this.numHashFunctions = numHashFunctions;
/* 117 */     this.funnel = (Funnel<? super T>)Preconditions.checkNotNull(funnel);
/* 118 */     this.strategy = (Strategy)Preconditions.checkNotNull(strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BloomFilter<T> copy() {
/* 128 */     return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mightContain(T object) {
/* 136 */     return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(T input) {
/* 146 */     return mightContain(input);
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(T object) {
/* 162 */     return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
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
/*     */   public double expectedFpp() {
/* 178 */     return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   long bitSize() {
/* 186 */     return this.bits.bitSize();
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
/*     */   public boolean isCompatible(BloomFilter<T> that) {
/* 205 */     Preconditions.checkNotNull(that);
/* 206 */     return (this != that && this.numHashFunctions == that.numHashFunctions && 
/*     */       
/* 208 */       bitSize() == that.bitSize() && this.strategy
/* 209 */       .equals(that.strategy) && this.funnel
/* 210 */       .equals(that.funnel));
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
/*     */   public void putAll(BloomFilter<T> that) {
/* 224 */     Preconditions.checkNotNull(that);
/* 225 */     Preconditions.checkArgument((this != that), "Cannot combine a BloomFilter with itself.");
/* 226 */     Preconditions.checkArgument((this.numHashFunctions == that.numHashFunctions), "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     Preconditions.checkArgument(
/* 232 */         (bitSize() == that.bitSize()), "BloomFilters must have the same size underlying bit arrays (%s != %s)", 
/*     */         
/* 234 */         bitSize(), that
/* 235 */         .bitSize());
/* 236 */     Preconditions.checkArgument(this.strategy
/* 237 */         .equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
/*     */ 
/*     */ 
/*     */     
/* 241 */     Preconditions.checkArgument(this.funnel
/* 242 */         .equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
/*     */ 
/*     */ 
/*     */     
/* 246 */     this.bits.putAll(that.bits);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 251 */     if (object == this) {
/* 252 */       return true;
/*     */     }
/* 254 */     if (object instanceof BloomFilter) {
/* 255 */       BloomFilter<?> that = (BloomFilter)object;
/* 256 */       return (this.numHashFunctions == that.numHashFunctions && this.funnel
/* 257 */         .equals(that.funnel) && this.bits
/* 258 */         .equals(that.bits) && this.strategy
/* 259 */         .equals(that.strategy));
/*     */     } 
/* 261 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 266 */     return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions, double fpp) {
/* 291 */     return create(funnel, expectedInsertions, fpp);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp) {
/* 317 */     return create(funnel, expectedInsertions, fpp, BloomFilterStrategies.MURMUR128_MITZ_64);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy) {
/* 323 */     Preconditions.checkNotNull(funnel);
/* 324 */     Preconditions.checkArgument((expectedInsertions >= 0L), "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 326 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 327 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 328 */     Preconditions.checkNotNull(strategy);
/*     */     
/* 330 */     if (expectedInsertions == 0L) {
/* 331 */       expectedInsertions = 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     long numBits = optimalNumOfBits(expectedInsertions, fpp);
/* 339 */     int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
/*     */     try {
/* 341 */       return new BloomFilter<>(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
/* 342 */     } catch (IllegalArgumentException e) {
/* 343 */       throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions) {
/* 367 */     return create(funnel, expectedInsertions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions) {
/* 391 */     return create(funnel, expectedInsertions, 0.03D);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int optimalNumOfHashFunctions(long n, long m) {
/* 418 */     return Math.max(1, (int)Math.round(m / n * Math.log(2.0D)));
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
/*     */   @VisibleForTesting
/*     */   static long optimalNumOfBits(long n, double p) {
/* 432 */     if (p == 0.0D) {
/* 433 */       p = Double.MIN_VALUE;
/*     */     }
/* 435 */     return (long)(-n * Math.log(p) / Math.log(2.0D) * Math.log(2.0D));
/*     */   }
/*     */   
/*     */   private Object writeReplace() {
/* 439 */     return new SerialForm<>(this);
/*     */   }
/*     */   
/*     */   private static class SerialForm<T>
/*     */     implements Serializable
/*     */   {
/*     */     final long[] data;
/*     */     final int numHashFunctions;
/*     */     
/*     */     SerialForm(BloomFilter<T> bf) {
/* 449 */       this.data = bf.bits.data;
/* 450 */       this.numHashFunctions = bf.numHashFunctions;
/* 451 */       this.funnel = bf.funnel;
/* 452 */       this.strategy = bf.strategy;
/*     */     }
/*     */     final Funnel<? super T> funnel; final BloomFilter.Strategy strategy; private static final long serialVersionUID = 1L;
/*     */     Object readResolve() {
/* 456 */       return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 475 */     DataOutputStream dout = new DataOutputStream(out);
/* 476 */     dout.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
/* 477 */     dout.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
/* 478 */     dout.writeInt(this.bits.data.length);
/* 479 */     for (long value : this.bits.data) {
/* 480 */       dout.writeLong(value);
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
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<T> funnel) throws IOException {
/* 496 */     Preconditions.checkNotNull(in, "InputStream");
/* 497 */     Preconditions.checkNotNull(funnel, "Funnel");
/* 498 */     int strategyOrdinal = -1;
/* 499 */     int numHashFunctions = -1;
/* 500 */     int dataLength = -1;
/*     */     try {
/* 502 */       DataInputStream din = new DataInputStream(in);
/*     */ 
/*     */ 
/*     */       
/* 506 */       strategyOrdinal = din.readByte();
/* 507 */       numHashFunctions = UnsignedBytes.toInt(din.readByte());
/* 508 */       dataLength = din.readInt();
/*     */       
/* 510 */       Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
/* 511 */       long[] data = new long[dataLength];
/* 512 */       for (int i = 0; i < data.length; i++) {
/* 513 */         data[i] = din.readLong();
/*     */       }
/* 515 */       return new BloomFilter<>(new BloomFilterStrategies.BitArray(data), numHashFunctions, funnel, strategy);
/* 516 */     } catch (RuntimeException e) {
/* 517 */       String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 525 */       throw new IOException(message, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface Strategy extends Serializable {
/*     */     <T> boolean put(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
/*     */     
/*     */     <T> boolean mightContain(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
/*     */     
/*     */     int ordinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\BloomFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */