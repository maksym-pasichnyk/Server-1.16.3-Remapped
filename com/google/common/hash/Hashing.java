/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.security.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits) {
/*  64 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  66 */     if (bits == 32) {
/*  67 */       return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
/*     */     }
/*  69 */     if (bits <= 128) {
/*  70 */       return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*     */     }
/*     */ 
/*     */     
/*  74 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  75 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  76 */     hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*  77 */     int seed = GOOD_FAST_HASH_SEED;
/*  78 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  79 */       seed += 1500450271;
/*  80 */       hashFunctions[i] = murmur3_128(seed);
/*     */     } 
/*  82 */     return new ConcatenatedHashFunction(hashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_32(int seed) {
/*  99 */     return new Murmur3_32HashFunction(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_32() {
/* 110 */     return Murmur3_32Holder.MURMUR3_32;
/*     */   }
/*     */   
/*     */   private static class Murmur3_32Holder {
/* 114 */     static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
/*     */ 
/*     */     
/* 117 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128(int seed) {
/* 128 */     return new Murmur3_128HashFunction(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128() {
/* 139 */     return Murmur3_128Holder.MURMUR3_128;
/*     */   }
/*     */   
/*     */   private static class Murmur3_128Holder {
/* 143 */     static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
/*     */ 
/*     */     
/* 146 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24() {
/* 156 */     return SipHash24Holder.SIP_HASH_24;
/*     */   }
/*     */   
/*     */   private static class SipHash24Holder {
/* 160 */     static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24(long k0, long k1) {
/* 171 */     return new SipHashFunction(2, 4, k0, k1);
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
/*     */   public static HashFunction md5() {
/* 183 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 187 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
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
/*     */   public static HashFunction sha1() {
/* 199 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 203 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha256() {
/* 211 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 215 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha384() {
/* 226 */     return Sha384Holder.SHA_384;
/*     */   }
/*     */   
/*     */   private static class Sha384Holder {
/* 230 */     static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha512() {
/* 239 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 243 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
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
/*     */   public static HashFunction hmacMd5(Key key) {
/* 257 */     return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
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
/*     */   public static HashFunction hmacMd5(byte[] key) {
/* 270 */     return hmacMd5(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5"));
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
/*     */   public static HashFunction hmacSha1(Key key) {
/* 283 */     return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
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
/*     */   public static HashFunction hmacSha1(byte[] key) {
/* 296 */     return hmacSha1(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1"));
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
/*     */   public static HashFunction hmacSha256(Key key) {
/* 309 */     return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
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
/*     */   public static HashFunction hmacSha256(byte[] key) {
/* 322 */     return hmacSha256(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256"));
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
/*     */   public static HashFunction hmacSha512(Key key) {
/* 335 */     return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
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
/*     */   public static HashFunction hmacSha512(byte[] key) {
/* 348 */     return hmacSha512(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512"));
/*     */   }
/*     */   
/*     */   private static String hmacToString(String methodName, Key key) {
/* 352 */     return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[] { methodName, key
/*     */ 
/*     */           
/* 355 */           .getAlgorithm(), key
/* 356 */           .getFormat() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction crc32c() {
/* 366 */     return Crc32cHolder.CRC_32_C;
/*     */   }
/*     */   
/*     */   private static final class Crc32cHolder {
/* 370 */     static final HashFunction CRC_32_C = new Crc32cHashFunction();
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
/*     */   public static HashFunction crc32() {
/* 383 */     return Crc32Holder.CRC_32;
/*     */   }
/*     */   
/*     */   private static class Crc32Holder {
/* 387 */     static final HashFunction CRC_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.CRC_32, "Hashing.crc32()");
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
/*     */   public static HashFunction adler32() {
/* 400 */     return Adler32Holder.ADLER_32;
/*     */   }
/*     */   
/*     */   private static class Adler32Holder
/*     */   {
/* 405 */     static final HashFunction ADLER_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.ADLER_32, "Hashing.adler32()");
/*     */   }
/*     */   
/*     */   private static HashFunction checksumHashFunction(ChecksumType type, String toString) {
/* 409 */     return new ChecksumHashFunction(type, type.bits, toString);
/*     */   }
/*     */   
/*     */   enum ChecksumType implements Supplier<Checksum> {
/* 413 */     CRC_32(32)
/*     */     {
/*     */       public Checksum get() {
/* 416 */         return new CRC32();
/*     */       }
/*     */     },
/* 419 */     ADLER_32(32)
/*     */     {
/*     */       public Checksum get() {
/* 422 */         return new Adler32();
/*     */       }
/*     */     };
/*     */     
/*     */     private final int bits;
/*     */     
/*     */     ChecksumType(int bits) {
/* 429 */       this.bits = bits;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract Checksum get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction farmHashFingerprint64() {
/* 449 */     return FarmHashFingerprint64Holder.FARMHASH_FINGERPRINT_64;
/*     */   }
/*     */   
/*     */   private static class FarmHashFingerprint64Holder {
/* 453 */     static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int consistentHash(HashCode hashCode, int buckets) {
/* 488 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int consistentHash(long input, int buckets) {
/* 523 */     Preconditions.checkArgument((buckets > 0), "buckets must be positive: %s", buckets);
/* 524 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 525 */     int candidate = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 530 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 531 */       if (next >= 0 && next < buckets) {
/* 532 */         candidate = next; continue;
/*     */       }  break;
/* 534 */     }  return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
/* 549 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 550 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 551 */     int bits = ((HashCode)iterator.next()).bits();
/* 552 */     byte[] resultBytes = new byte[bits / 8];
/* 553 */     for (HashCode hashCode : hashCodes) {
/* 554 */       byte[] nextBytes = hashCode.asBytes();
/* 555 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 557 */       for (int i = 0; i < nextBytes.length; i++) {
/* 558 */         resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
/*     */       }
/*     */     } 
/* 561 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
/* 574 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 575 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 576 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 577 */     for (HashCode hashCode : hashCodes) {
/* 578 */       byte[] nextBytes = hashCode.asBytes();
/* 579 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 581 */       for (int i = 0; i < nextBytes.length; i++) {
/* 582 */         resultBytes[i] = (byte)(resultBytes[i] + nextBytes[i]);
/*     */       }
/*     */     } 
/* 585 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits) {
/* 592 */     Preconditions.checkArgument((bits > 0), "Number of bits must be positive");
/* 593 */     return bits + 31 & 0xFFFFFFE0;
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
/*     */   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest) {
/* 609 */     List<HashFunction> list = new ArrayList<>();
/* 610 */     list.add(first);
/* 611 */     list.add(second);
/* 612 */     for (HashFunction hashFunc : rest) {
/* 613 */       list.add(hashFunc);
/*     */     }
/* 615 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
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
/*     */   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions) {
/* 629 */     Preconditions.checkNotNull(hashFunctions);
/*     */     
/* 631 */     List<HashFunction> list = new ArrayList<>();
/* 632 */     for (HashFunction hashFunction : hashFunctions) {
/* 633 */       list.add(hashFunction);
/*     */     }
/* 635 */     Preconditions.checkArgument((list.size() > 0), "number of hash functions (%s) must be > 0", list.size());
/* 636 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
/*     */     private final int bits;
/*     */     
/*     */     private ConcatenatedHashFunction(HashFunction... functions) {
/* 643 */       super(functions);
/* 644 */       int bitSum = 0;
/* 645 */       for (HashFunction function : functions) {
/* 646 */         bitSum += function.bits();
/* 647 */         Preconditions.checkArgument(
/* 648 */             (function.bits() % 8 == 0), "the number of bits (%s) in hashFunction (%s) must be divisible by 8", function
/*     */             
/* 650 */             .bits(), function);
/*     */       } 
/*     */       
/* 653 */       this.bits = bitSum;
/*     */     }
/*     */ 
/*     */     
/*     */     HashCode makeHash(Hasher[] hashers) {
/* 658 */       byte[] bytes = new byte[this.bits / 8];
/* 659 */       int i = 0;
/* 660 */       for (Hasher hasher : hashers) {
/* 661 */         HashCode newHash = hasher.hash();
/* 662 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       } 
/* 664 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 669 */       return this.bits;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 674 */       if (object instanceof ConcatenatedHashFunction) {
/* 675 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 676 */         return Arrays.equals((Object[])this.functions, (Object[])other.functions);
/*     */       } 
/* 678 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 683 */       return Arrays.hashCode((Object[])this.functions) * 31 + this.bits;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */ 
/*     */     
/*     */     public LinearCongruentialGenerator(long seed) {
/* 695 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 699 */       this.state = 2862933555777941757L * this.state + 1L;
/* 700 */       return ((int)(this.state >>> 33L) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */