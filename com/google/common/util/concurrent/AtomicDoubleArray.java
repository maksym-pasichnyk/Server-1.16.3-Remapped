/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public class AtomicDoubleArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private transient AtomicLongArray longs;
/*     */   
/*     */   public AtomicDoubleArray(int length) {
/*  60 */     this.longs = new AtomicLongArray(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDoubleArray(double[] array) {
/*  71 */     int len = array.length;
/*  72 */     long[] longArray = new long[len];
/*  73 */     for (int i = 0; i < len; i++) {
/*  74 */       longArray[i] = Double.doubleToRawLongBits(array[i]);
/*     */     }
/*  76 */     this.longs = new AtomicLongArray(longArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int length() {
/*  85 */     return this.longs.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get(int i) {
/*  95 */     return Double.longBitsToDouble(this.longs.get(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(int i, double newValue) {
/* 105 */     long next = Double.doubleToRawLongBits(newValue);
/* 106 */     this.longs.set(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(int i, double newValue) {
/* 116 */     set(i, newValue);
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
/*     */   public final double getAndSet(int i, double newValue) {
/* 131 */     long next = Double.doubleToRawLongBits(newValue);
/* 132 */     return Double.longBitsToDouble(this.longs.getAndSet(i, next));
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
/*     */   public final boolean compareAndSet(int i, double expect, double update) {
/* 148 */     return this.longs.compareAndSet(i, 
/* 149 */         Double.doubleToRawLongBits(expect), 
/* 150 */         Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(int i, double expect, double update) {
/* 171 */     return this.longs.weakCompareAndSet(i, 
/* 172 */         Double.doubleToRawLongBits(expect), 
/* 173 */         Double.doubleToRawLongBits(update));
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
/*     */   public final double getAndAdd(int i, double delta) {
/*     */     while (true) {
/* 186 */       long current = this.longs.get(i);
/* 187 */       double currentVal = Double.longBitsToDouble(current);
/* 188 */       double nextVal = currentVal + delta;
/* 189 */       long next = Double.doubleToRawLongBits(nextVal);
/* 190 */       if (this.longs.compareAndSet(i, current, next)) {
/* 191 */         return currentVal;
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
/*     */   @CanIgnoreReturnValue
/*     */   public double addAndGet(int i, double delta) {
/*     */     while (true) {
/* 206 */       long current = this.longs.get(i);
/* 207 */       double currentVal = Double.longBitsToDouble(current);
/* 208 */       double nextVal = currentVal + delta;
/* 209 */       long next = Double.doubleToRawLongBits(nextVal);
/* 210 */       if (this.longs.compareAndSet(i, current, next)) {
/* 211 */         return nextVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     int iMax = length() - 1;
/* 222 */     if (iMax == -1) {
/* 223 */       return "[]";
/*     */     }
/*     */ 
/*     */     
/* 227 */     StringBuilder b = new StringBuilder(19 * (iMax + 1));
/* 228 */     b.append('[');
/* 229 */     for (int i = 0;; i++) {
/* 230 */       b.append(Double.longBitsToDouble(this.longs.get(i)));
/* 231 */       if (i == iMax) {
/* 232 */         return b.append(']').toString();
/*     */       }
/* 234 */       b.append(',').append(' ');
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
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 246 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 249 */     int length = length();
/* 250 */     s.writeInt(length);
/*     */ 
/*     */     
/* 253 */     for (int i = 0; i < length; i++) {
/* 254 */       s.writeDouble(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 263 */     s.defaultReadObject();
/*     */ 
/*     */     
/* 266 */     int length = s.readInt();
/* 267 */     this.longs = new AtomicLongArray(length);
/*     */ 
/*     */     
/* 270 */     for (int i = 0; i < length; i++)
/* 271 */       set(i, s.readDouble()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AtomicDoubleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */