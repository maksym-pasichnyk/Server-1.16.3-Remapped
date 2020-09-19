/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedInts;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @Beta
/*     */ public abstract class HashCode
/*     */ {
/*     */   public abstract int bits();
/*     */   
/*     */   public abstract int asInt();
/*     */   
/*     */   public abstract long asLong();
/*     */   
/*     */   public abstract long padToLong();
/*     */   
/*     */   public abstract byte[] asBytes();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int writeBytesTo(byte[] dest, int offset, int maxLength) {
/*  89 */     maxLength = Ints.min(new int[] { maxLength, bits() / 8 });
/*  90 */     Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
/*  91 */     writeBytesToImpl(dest, offset, maxLength);
/*  92 */     return maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void writeBytesToImpl(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getBytesInternal() {
/* 103 */     return asBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean equalsSameBits(HashCode paramHashCode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromInt(int hash) {
/* 119 */     return new IntHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class IntHashCode extends HashCode implements Serializable {
/*     */     final int hash;
/*     */     
/*     */     IntHashCode(int hash) {
/* 126 */       this.hash = hash;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int bits() {
/* 131 */       return 32;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 136 */       return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 146 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 151 */       throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 156 */       return UnsignedInts.toLong(this.hash);
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 161 */       for (int i = 0; i < maxLength; i++) {
/* 162 */         dest[offset + i] = (byte)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 168 */       return (this.hash == that.asInt());
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
/*     */   public static HashCode fromLong(long hash) {
/* 181 */     return new LongHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class LongHashCode extends HashCode implements Serializable { final long hash;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongHashCode(long hash) {
/* 188 */       this.hash = hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 193 */       return 64;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 198 */       return new byte[] { (byte)(int)this.hash, (byte)(int)(this.hash >> 8L), (byte)(int)(this.hash >> 16L), (byte)(int)(this.hash >> 24L), (byte)(int)(this.hash >> 32L), (byte)(int)(this.hash >> 40L), (byte)(int)(this.hash >> 48L), (byte)(int)(this.hash >> 56L) };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 212 */       return (int)this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 217 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 222 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 227 */       for (int i = 0; i < maxLength; i++) {
/* 228 */         dest[offset + i] = (byte)(int)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 234 */       return (this.hash == that.asLong());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromBytes(byte[] bytes) {
/* 247 */     Preconditions.checkArgument((bytes.length >= 1), "A HashCode must contain at least 1 byte.");
/* 248 */     return fromBytesNoCopy((byte[])bytes.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HashCode fromBytesNoCopy(byte[] bytes) {
/* 256 */     return new BytesHashCode(bytes);
/*     */   }
/*     */   
/*     */   private static final class BytesHashCode extends HashCode implements Serializable { final byte[] bytes;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BytesHashCode(byte[] bytes) {
/* 263 */       this.bytes = (byte[])Preconditions.checkNotNull(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 268 */       return this.bytes.length * 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 273 */       return (byte[])this.bytes.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 278 */       Preconditions.checkState((this.bytes.length >= 4), "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 282 */       return this.bytes[0] & 0xFF | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 290 */       Preconditions.checkState((this.bytes.length >= 8), "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 294 */       return padToLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 299 */       long retVal = (this.bytes[0] & 0xFF);
/* 300 */       for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
/* 301 */         retVal |= (this.bytes[i] & 0xFFL) << i * 8;
/*     */       }
/* 303 */       return retVal;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 308 */       System.arraycopy(this.bytes, 0, dest, offset, maxLength);
/*     */     }
/*     */ 
/*     */     
/*     */     byte[] getBytesInternal() {
/* 313 */       return this.bytes;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/*     */       int j;
/* 320 */       if (this.bytes.length != (that.getBytesInternal()).length) {
/* 321 */         return false;
/*     */       }
/*     */       
/* 324 */       boolean areEqual = true;
/* 325 */       for (int i = 0; i < this.bytes.length; i++) {
/* 326 */         j = areEqual & ((this.bytes[i] == that.getBytesInternal()[i]) ? 1 : 0);
/*     */       }
/* 328 */       return j;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromString(String string) {
/* 345 */     Preconditions.checkArgument(
/* 346 */         (string.length() >= 2), "input string (%s) must have at least 2 characters", string);
/* 347 */     Preconditions.checkArgument(
/* 348 */         (string.length() % 2 == 0), "input string (%s) must have an even number of characters", string);
/*     */ 
/*     */ 
/*     */     
/* 352 */     byte[] bytes = new byte[string.length() / 2];
/* 353 */     for (int i = 0; i < string.length(); i += 2) {
/* 354 */       int ch1 = decode(string.charAt(i)) << 4;
/* 355 */       int ch2 = decode(string.charAt(i + 1));
/* 356 */       bytes[i / 2] = (byte)(ch1 + ch2);
/*     */     } 
/* 358 */     return fromBytesNoCopy(bytes);
/*     */   }
/*     */   
/*     */   private static int decode(char ch) {
/* 362 */     if (ch >= '0' && ch <= '9') {
/* 363 */       return ch - 48;
/*     */     }
/* 365 */     if (ch >= 'a' && ch <= 'f') {
/* 366 */       return ch - 97 + 10;
/*     */     }
/* 368 */     throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
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
/*     */   public final boolean equals(@Nullable Object object) {
/* 380 */     if (object instanceof HashCode) {
/* 381 */       HashCode that = (HashCode)object;
/* 382 */       return (bits() == that.bits() && equalsSameBits(that));
/*     */     } 
/* 384 */     return false;
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
/*     */   public final int hashCode() {
/* 396 */     if (bits() >= 32) {
/* 397 */       return asInt();
/*     */     }
/*     */     
/* 400 */     byte[] bytes = getBytesInternal();
/* 401 */     int val = bytes[0] & 0xFF;
/* 402 */     for (int i = 1; i < bytes.length; i++) {
/* 403 */       val |= (bytes[i] & 0xFF) << i * 8;
/*     */     }
/* 405 */     return val;
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
/*     */   public final String toString() {
/* 421 */     byte[] bytes = getBytesInternal();
/* 422 */     StringBuilder sb = new StringBuilder(2 * bytes.length);
/* 423 */     for (byte b : bytes) {
/* 424 */       sb.append(hexDigits[b >> 4 & 0xF]).append(hexDigits[b & 0xF]);
/*     */     }
/* 426 */     return sb.toString();
/*     */   }
/*     */   
/* 429 */   private static final char[] hexDigits = "0123456789abcdef".toCharArray();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\HashCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */