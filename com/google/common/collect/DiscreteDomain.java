/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class DiscreteDomain<C extends Comparable>
/*     */ {
/*     */   public static DiscreteDomain<Integer> integers() {
/*  52 */     return IntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
/*  56 */     private static final IntegerDomain INSTANCE = new IntegerDomain();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Integer next(Integer value) {
/*  60 */       int i = value.intValue();
/*  61 */       return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer previous(Integer value) {
/*  66 */       int i = value.intValue();
/*  67 */       return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Integer start, Integer end) {
/*  72 */       return end.intValue() - start.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer minValue() {
/*  77 */       return Integer.valueOf(-2147483648);
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer maxValue() {
/*  82 */       return Integer.valueOf(2147483647);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/*  86 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return "DiscreteDomain.integers()";
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
/*     */   public static DiscreteDomain<Long> longs() {
/* 103 */     return LongDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
/* 107 */     private static final LongDomain INSTANCE = new LongDomain();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Long next(Long value) {
/* 111 */       long l = value.longValue();
/* 112 */       return (l == Long.MAX_VALUE) ? null : Long.valueOf(l + 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long previous(Long value) {
/* 117 */       long l = value.longValue();
/* 118 */       return (l == Long.MIN_VALUE) ? null : Long.valueOf(l - 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Long start, Long end) {
/* 123 */       long result = end.longValue() - start.longValue();
/* 124 */       if (end.longValue() > start.longValue() && result < 0L) {
/* 125 */         return Long.MAX_VALUE;
/*     */       }
/* 127 */       if (end.longValue() < start.longValue() && result > 0L) {
/* 128 */         return Long.MIN_VALUE;
/*     */       }
/* 130 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long minValue() {
/* 135 */       return Long.valueOf(Long.MIN_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long maxValue() {
/* 140 */       return Long.valueOf(Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 144 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 149 */       return "DiscreteDomain.longs()";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract C next(C paramC);
/*     */   
/*     */   public abstract C previous(C paramC);
/*     */   
/*     */   public abstract long distance(C paramC1, C paramC2);
/*     */   
/*     */   public static DiscreteDomain<BigInteger> bigIntegers() {
/* 161 */     return BigIntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BigIntegerDomain
/*     */     extends DiscreteDomain<BigInteger> implements Serializable {
/* 166 */     private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
/*     */     
/* 168 */     private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/* 169 */     private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public BigInteger next(BigInteger value) {
/* 173 */       return value.add(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger previous(BigInteger value) {
/* 178 */       return value.subtract(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(BigInteger start, BigInteger end) {
/* 183 */       return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 187 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 192 */       return "DiscreteDomain.bigIntegers()";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public C minValue() {
/* 253 */     throw new NoSuchElementException();
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
/*     */   public C maxValue() {
/* 269 */     throw new NoSuchElementException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\DiscreteDomain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */