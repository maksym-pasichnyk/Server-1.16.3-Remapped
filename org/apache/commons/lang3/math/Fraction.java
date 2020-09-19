/*     */ package org.apache.commons.lang3.math;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Fraction
/*     */   extends Number
/*     */   implements Comparable<Fraction>
/*     */ {
/*     */   private static final long serialVersionUID = 65382027393090L;
/*  46 */   public static final Fraction ZERO = new Fraction(0, 1);
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final Fraction ONE = new Fraction(1, 1);
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final Fraction ONE_HALF = new Fraction(1, 2);
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final Fraction ONE_THIRD = new Fraction(1, 3);
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int numerator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int denominator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private transient int hashCode = 0;
/*     */ 
/*     */ 
/*     */   
/* 109 */   private transient String toString = null;
/*     */ 
/*     */ 
/*     */   
/* 113 */   private transient String toProperString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Fraction(int numerator, int denominator) {
/* 124 */     this.numerator = numerator;
/* 125 */     this.denominator = denominator;
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
/*     */   public static Fraction getFraction(int numerator, int denominator) {
/* 141 */     if (denominator == 0) {
/* 142 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 144 */     if (denominator < 0) {
/* 145 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 146 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 148 */       numerator = -numerator;
/* 149 */       denominator = -denominator;
/*     */     } 
/* 151 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(int whole, int numerator, int denominator) {
/*     */     long numeratorValue;
/* 171 */     if (denominator == 0) {
/* 172 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 174 */     if (denominator < 0) {
/* 175 */       throw new ArithmeticException("The denominator must not be negative");
/*     */     }
/* 177 */     if (numerator < 0) {
/* 178 */       throw new ArithmeticException("The numerator must not be negative");
/*     */     }
/*     */     
/* 181 */     if (whole < 0) {
/* 182 */       numeratorValue = whole * denominator - numerator;
/*     */     } else {
/* 184 */       numeratorValue = whole * denominator + numerator;
/*     */     } 
/* 186 */     if (numeratorValue < -2147483648L || numeratorValue > 2147483647L) {
/* 187 */       throw new ArithmeticException("Numerator too large to represent as an Integer.");
/*     */     }
/* 189 */     return new Fraction((int)numeratorValue, denominator);
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
/*     */   public static Fraction getReducedFraction(int numerator, int denominator) {
/* 207 */     if (denominator == 0) {
/* 208 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 210 */     if (numerator == 0) {
/* 211 */       return ZERO;
/*     */     }
/*     */     
/* 214 */     if (denominator == Integer.MIN_VALUE && (numerator & 0x1) == 0) {
/* 215 */       numerator /= 2;
/* 216 */       denominator /= 2;
/*     */     } 
/* 218 */     if (denominator < 0) {
/* 219 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 220 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 222 */       numerator = -numerator;
/* 223 */       denominator = -denominator;
/*     */     } 
/*     */     
/* 226 */     int gcd = greatestCommonDivisor(numerator, denominator);
/* 227 */     numerator /= gcd;
/* 228 */     denominator /= gcd;
/* 229 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(double value) {
/*     */     double delta1;
/* 247 */     int sign = (value < 0.0D) ? -1 : 1;
/* 248 */     value = Math.abs(value);
/* 249 */     if (value > 2.147483647E9D || Double.isNaN(value)) {
/* 250 */       throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
/*     */     }
/* 252 */     int wholeNumber = (int)value;
/* 253 */     value -= wholeNumber;
/*     */     
/* 255 */     int numer0 = 0;
/* 256 */     int denom0 = 1;
/* 257 */     int numer1 = 1;
/* 258 */     int denom1 = 0;
/* 259 */     int numer2 = 0;
/* 260 */     int denom2 = 0;
/* 261 */     int a1 = (int)value;
/* 262 */     int a2 = 0;
/* 263 */     double x1 = 1.0D;
/* 264 */     double x2 = 0.0D;
/* 265 */     double y1 = value - a1;
/* 266 */     double y2 = 0.0D;
/* 267 */     double delta2 = Double.MAX_VALUE;
/*     */     
/* 269 */     int i = 1;
/*     */     
/*     */     do {
/* 272 */       delta1 = delta2;
/* 273 */       a2 = (int)(x1 / y1);
/* 274 */       x2 = y1;
/* 275 */       y2 = x1 - a2 * y1;
/* 276 */       numer2 = a1 * numer1 + numer0;
/* 277 */       denom2 = a1 * denom1 + denom0;
/* 278 */       double fraction = numer2 / denom2;
/* 279 */       delta2 = Math.abs(value - fraction);
/*     */       
/* 281 */       a1 = a2;
/* 282 */       x1 = x2;
/* 283 */       y1 = y2;
/* 284 */       numer0 = numer1;
/* 285 */       denom0 = denom1;
/* 286 */       numer1 = numer2;
/* 287 */       denom1 = denom2;
/* 288 */       i++;
/*     */     }
/* 290 */     while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
/* 291 */     if (i == 25) {
/* 292 */       throw new ArithmeticException("Unable to convert double to fraction");
/*     */     }
/* 294 */     return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
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
/*     */   public static Fraction getFraction(String str) {
/* 316 */     if (str == null) {
/* 317 */       throw new IllegalArgumentException("The string must not be null");
/*     */     }
/*     */     
/* 320 */     int pos = str.indexOf('.');
/* 321 */     if (pos >= 0) {
/* 322 */       return getFraction(Double.parseDouble(str));
/*     */     }
/*     */ 
/*     */     
/* 326 */     pos = str.indexOf(' ');
/* 327 */     if (pos > 0) {
/* 328 */       int whole = Integer.parseInt(str.substring(0, pos));
/* 329 */       str = str.substring(pos + 1);
/* 330 */       pos = str.indexOf('/');
/* 331 */       if (pos < 0) {
/* 332 */         throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
/*     */       }
/* 334 */       int i = Integer.parseInt(str.substring(0, pos));
/* 335 */       int j = Integer.parseInt(str.substring(pos + 1));
/* 336 */       return getFraction(whole, i, j);
/*     */     } 
/*     */ 
/*     */     
/* 340 */     pos = str.indexOf('/');
/* 341 */     if (pos < 0)
/*     */     {
/* 343 */       return getFraction(Integer.parseInt(str), 1);
/*     */     }
/* 345 */     int numer = Integer.parseInt(str.substring(0, pos));
/* 346 */     int denom = Integer.parseInt(str.substring(pos + 1));
/* 347 */     return getFraction(numer, denom);
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
/*     */   public int getNumerator() {
/* 362 */     return this.numerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDenominator() {
/* 371 */     return this.denominator;
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
/*     */   public int getProperNumerator() {
/* 386 */     return Math.abs(this.numerator % this.denominator);
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
/*     */   public int getProperWhole() {
/* 401 */     return this.numerator / this.denominator;
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
/*     */   public int intValue() {
/* 415 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 426 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 437 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 448 */     return this.numerator / this.denominator;
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
/*     */   public Fraction reduce() {
/* 464 */     if (this.numerator == 0) {
/* 465 */       return equals(ZERO) ? this : ZERO;
/*     */     }
/* 467 */     int gcd = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
/* 468 */     if (gcd == 1) {
/* 469 */       return this;
/*     */     }
/* 471 */     return getFraction(this.numerator / gcd, this.denominator / gcd);
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
/*     */   public Fraction invert() {
/* 484 */     if (this.numerator == 0) {
/* 485 */       throw new ArithmeticException("Unable to invert zero.");
/*     */     }
/* 487 */     if (this.numerator == Integer.MIN_VALUE) {
/* 488 */       throw new ArithmeticException("overflow: can't negate numerator");
/*     */     }
/* 490 */     if (this.numerator < 0) {
/* 491 */       return new Fraction(-this.denominator, -this.numerator);
/*     */     }
/* 493 */     return new Fraction(this.denominator, this.numerator);
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
/*     */   public Fraction negate() {
/* 505 */     if (this.numerator == Integer.MIN_VALUE) {
/* 506 */       throw new ArithmeticException("overflow: too large to negate");
/*     */     }
/* 508 */     return new Fraction(-this.numerator, this.denominator);
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
/*     */   public Fraction abs() {
/* 521 */     if (this.numerator >= 0) {
/* 522 */       return this;
/*     */     }
/* 524 */     return negate();
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
/*     */   public Fraction pow(int power) {
/* 540 */     if (power == 1)
/* 541 */       return this; 
/* 542 */     if (power == 0)
/* 543 */       return ONE; 
/* 544 */     if (power < 0) {
/* 545 */       if (power == Integer.MIN_VALUE) {
/* 546 */         return invert().pow(2).pow(-(power / 2));
/*     */       }
/* 548 */       return invert().pow(-power);
/*     */     } 
/* 550 */     Fraction f = multiplyBy(this);
/* 551 */     if (power % 2 == 0) {
/* 552 */       return f.pow(power / 2);
/*     */     }
/* 554 */     return f.pow(power / 2).multiplyBy(this);
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
/*     */   private static int greatestCommonDivisor(int u, int v) {
/* 570 */     if (u == 0 || v == 0) {
/* 571 */       if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
/* 572 */         throw new ArithmeticException("overflow: gcd is 2^31");
/*     */       }
/* 574 */       return Math.abs(u) + Math.abs(v);
/*     */     } 
/*     */     
/* 577 */     if (Math.abs(u) == 1 || Math.abs(v) == 1) {
/* 578 */       return 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 584 */     if (u > 0) {
/* 585 */       u = -u;
/*     */     }
/* 587 */     if (v > 0) {
/* 588 */       v = -v;
/*     */     }
/*     */     
/* 591 */     int k = 0;
/* 592 */     while ((u & 0x1) == 0 && (v & 0x1) == 0 && k < 31) {
/* 593 */       u /= 2;
/* 594 */       v /= 2;
/* 595 */       k++;
/*     */     } 
/* 597 */     if (k == 31) {
/* 598 */       throw new ArithmeticException("overflow: gcd is 2^31");
/*     */     }
/*     */ 
/*     */     
/* 602 */     int t = ((u & 0x1) == 1) ? v : -(u / 2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 608 */       while ((t & 0x1) == 0) {
/* 609 */         t /= 2;
/*     */       }
/*     */       
/* 612 */       if (t > 0) {
/* 613 */         u = -t;
/*     */       } else {
/* 615 */         v = t;
/*     */       } 
/*     */       
/* 618 */       t = (v - u) / 2;
/*     */ 
/*     */       
/* 621 */       if (t == 0) {
/* 622 */         return -u * (1 << k);
/*     */       }
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
/*     */   private static int mulAndCheck(int x, int y) {
/* 638 */     long m = x * y;
/* 639 */     if (m < -2147483648L || m > 2147483647L) {
/* 640 */       throw new ArithmeticException("overflow: mul");
/*     */     }
/* 642 */     return (int)m;
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
/*     */   private static int mulPosAndCheck(int x, int y) {
/* 656 */     long m = x * y;
/* 657 */     if (m > 2147483647L) {
/* 658 */       throw new ArithmeticException("overflow: mulPos");
/*     */     }
/* 660 */     return (int)m;
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
/*     */   private static int addAndCheck(int x, int y) {
/* 673 */     long s = x + y;
/* 674 */     if (s < -2147483648L || s > 2147483647L) {
/* 675 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 677 */     return (int)s;
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
/*     */   private static int subAndCheck(int x, int y) {
/* 690 */     long s = x - y;
/* 691 */     if (s < -2147483648L || s > 2147483647L) {
/* 692 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 694 */     return (int)s;
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
/*     */   public Fraction add(Fraction fraction) {
/* 708 */     return addSub(fraction, true);
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
/*     */   public Fraction subtract(Fraction fraction) {
/* 722 */     return addSub(fraction, false);
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
/*     */   private Fraction addSub(Fraction fraction, boolean isAdd) {
/* 736 */     if (fraction == null) {
/* 737 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/*     */     
/* 740 */     if (this.numerator == 0) {
/* 741 */       return isAdd ? fraction : fraction.negate();
/*     */     }
/* 743 */     if (fraction.numerator == 0) {
/* 744 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 748 */     int d1 = greatestCommonDivisor(this.denominator, fraction.denominator);
/* 749 */     if (d1 == 1) {
/*     */       
/* 751 */       int i = mulAndCheck(this.numerator, fraction.denominator);
/* 752 */       int j = mulAndCheck(fraction.numerator, this.denominator);
/* 753 */       return new Fraction(isAdd ? addAndCheck(i, j) : subAndCheck(i, j), mulPosAndCheck(this.denominator, fraction.denominator));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 759 */     BigInteger uvp = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf((fraction.denominator / d1)));
/* 760 */     BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf((this.denominator / d1)));
/* 761 */     BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
/*     */ 
/*     */     
/* 764 */     int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
/* 765 */     int d2 = (tmodd1 == 0) ? d1 : greatestCommonDivisor(tmodd1, d1);
/*     */ 
/*     */     
/* 768 */     BigInteger w = t.divide(BigInteger.valueOf(d2));
/* 769 */     if (w.bitLength() > 31) {
/* 770 */       throw new ArithmeticException("overflow: numerator too large after multiply");
/*     */     }
/* 772 */     return new Fraction(w.intValue(), mulPosAndCheck(this.denominator / d1, fraction.denominator / d2));
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
/*     */   public Fraction multiplyBy(Fraction fraction) {
/* 786 */     if (fraction == null) {
/* 787 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/* 789 */     if (this.numerator == 0 || fraction.numerator == 0) {
/* 790 */       return ZERO;
/*     */     }
/*     */ 
/*     */     
/* 794 */     int d1 = greatestCommonDivisor(this.numerator, fraction.denominator);
/* 795 */     int d2 = greatestCommonDivisor(fraction.numerator, this.denominator);
/* 796 */     return getReducedFraction(mulAndCheck(this.numerator / d1, fraction.numerator / d2), 
/* 797 */         mulPosAndCheck(this.denominator / d2, fraction.denominator / d1));
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
/*     */   public Fraction divideBy(Fraction fraction) {
/* 811 */     if (fraction == null) {
/* 812 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/* 814 */     if (fraction.numerator == 0) {
/* 815 */       throw new ArithmeticException("The fraction to divide by must not be zero");
/*     */     }
/* 817 */     return multiplyBy(fraction.invert());
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
/*     */   public boolean equals(Object obj) {
/* 833 */     if (obj == this) {
/* 834 */       return true;
/*     */     }
/* 836 */     if (!(obj instanceof Fraction)) {
/* 837 */       return false;
/*     */     }
/* 839 */     Fraction other = (Fraction)obj;
/* 840 */     return (getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 850 */     if (this.hashCode == 0)
/*     */     {
/* 852 */       this.hashCode = 37 * (629 + getNumerator()) + getDenominator();
/*     */     }
/* 854 */     return this.hashCode;
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
/*     */   public int compareTo(Fraction other) {
/* 871 */     if (this == other) {
/* 872 */       return 0;
/*     */     }
/* 874 */     if (this.numerator == other.numerator && this.denominator == other.denominator) {
/* 875 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 879 */     long first = this.numerator * other.denominator;
/* 880 */     long second = other.numerator * this.denominator;
/* 881 */     if (first == second)
/* 882 */       return 0; 
/* 883 */     if (first < second) {
/* 884 */       return -1;
/*     */     }
/* 886 */     return 1;
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
/*     */   public String toString() {
/* 899 */     if (this.toString == null) {
/* 900 */       this.toString = getNumerator() + "/" + getDenominator();
/*     */     }
/* 902 */     return this.toString;
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
/*     */   public String toProperString() {
/* 915 */     if (this.toProperString == null) {
/* 916 */       if (this.numerator == 0) {
/* 917 */         this.toProperString = "0";
/* 918 */       } else if (this.numerator == this.denominator) {
/* 919 */         this.toProperString = "1";
/* 920 */       } else if (this.numerator == -1 * this.denominator) {
/* 921 */         this.toProperString = "-1";
/* 922 */       } else if (((this.numerator > 0) ? -this.numerator : this.numerator) < -this.denominator) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 927 */         int properNumerator = getProperNumerator();
/* 928 */         if (properNumerator == 0) {
/* 929 */           this.toProperString = Integer.toString(getProperWhole());
/*     */         } else {
/* 931 */           this.toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
/*     */         } 
/*     */       } else {
/* 934 */         this.toProperString = getNumerator() + "/" + getDenominator();
/*     */       } 
/*     */     }
/* 937 */     return this.toProperString;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\math\Fraction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */