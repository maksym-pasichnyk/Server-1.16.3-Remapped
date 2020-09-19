/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public final class UnsignedLong
/*     */   extends Number
/*     */   implements Comparable<UnsignedLong>, Serializable
/*     */ {
/*     */   private static final long UNSIGNED_MASK = 9223372036854775807L;
/*  45 */   public static final UnsignedLong ZERO = new UnsignedLong(0L);
/*  46 */   public static final UnsignedLong ONE = new UnsignedLong(1L);
/*  47 */   public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1L);
/*     */   
/*     */   private final long value;
/*     */   
/*     */   private UnsignedLong(long value) {
/*  52 */     this.value = value;
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
/*     */   public static UnsignedLong fromLongBits(long bits) {
/*  70 */     return new UnsignedLong(bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(long value) {
/*  81 */     Preconditions.checkArgument((value >= 0L), "value (%s) is outside the range for an unsigned long value", value);
/*  82 */     return fromLongBits(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(BigInteger value) {
/*  93 */     Preconditions.checkNotNull(value);
/*  94 */     Preconditions.checkArgument((value
/*  95 */         .signum() >= 0 && value.bitLength() <= 64), "value (%s) is outside the range for an unsigned long value", value);
/*     */ 
/*     */     
/*  98 */     return fromLongBits(value.longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(String string) {
/* 110 */     return valueOf(string, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(String string, int radix) {
/* 123 */     return fromLongBits(UnsignedLongs.parseUnsignedLong(string, radix));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong plus(UnsignedLong val) {
/* 133 */     return fromLongBits(this.value + ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong minus(UnsignedLong val) {
/* 143 */     return fromLongBits(this.value - ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong times(UnsignedLong val) {
/* 153 */     return fromLongBits(this.value * ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong dividedBy(UnsignedLong val) {
/* 162 */     return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong mod(UnsignedLong val) {
/* 171 */     return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 179 */     return (int)this.value;
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
/*     */   public long longValue() {
/* 191 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 201 */     float fValue = (float)(this.value & Long.MAX_VALUE);
/* 202 */     if (this.value < 0L) {
/* 203 */       fValue += 9.223372E18F;
/*     */     }
/* 205 */     return fValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 215 */     double dValue = (this.value & Long.MAX_VALUE);
/* 216 */     if (this.value < 0L) {
/* 217 */       dValue += 9.223372036854776E18D;
/*     */     }
/* 219 */     return dValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 226 */     BigInteger bigInt = BigInteger.valueOf(this.value & Long.MAX_VALUE);
/* 227 */     if (this.value < 0L) {
/* 228 */       bigInt = bigInt.setBit(63);
/*     */     }
/* 230 */     return bigInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedLong o) {
/* 235 */     Preconditions.checkNotNull(o);
/* 236 */     return UnsignedLongs.compare(this.value, o.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 241 */     return Longs.hashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 246 */     if (obj instanceof UnsignedLong) {
/* 247 */       UnsignedLong other = (UnsignedLong)obj;
/* 248 */       return (this.value == other.value);
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 258 */     return UnsignedLongs.toString(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int radix) {
/* 267 */     return UnsignedLongs.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\UnsignedLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */