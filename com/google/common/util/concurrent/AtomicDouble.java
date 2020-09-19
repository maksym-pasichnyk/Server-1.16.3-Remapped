/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class AtomicDouble
/*     */   extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private volatile transient long value;
/*  62 */   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble(double initialValue) {
/*  70 */     this.value = Double.doubleToRawLongBits(initialValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get() {
/*  86 */     return Double.longBitsToDouble(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(double newValue) {
/*  95 */     long next = Double.doubleToRawLongBits(newValue);
/*  96 */     this.value = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(double newValue) {
/* 105 */     set(newValue);
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
/*     */   public final double getAndSet(double newValue) {
/* 118 */     long next = Double.doubleToRawLongBits(newValue);
/* 119 */     return Double.longBitsToDouble(updater.getAndSet(this, next));
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
/*     */   public final boolean compareAndSet(double expect, double update) {
/* 133 */     return updater.compareAndSet(this, 
/* 134 */         Double.doubleToRawLongBits(expect), 
/* 135 */         Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(double expect, double update) {
/* 154 */     return updater.weakCompareAndSet(this, 
/* 155 */         Double.doubleToRawLongBits(expect), 
/* 156 */         Double.doubleToRawLongBits(update));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(double delta) {
/*     */     while (true) {
/* 168 */       long current = this.value;
/* 169 */       double currentVal = Double.longBitsToDouble(current);
/* 170 */       double nextVal = currentVal + delta;
/* 171 */       long next = Double.doubleToRawLongBits(nextVal);
/* 172 */       if (updater.compareAndSet(this, current, next)) {
/* 173 */         return currentVal;
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double addAndGet(double delta) {
/*     */     while (true) {
/* 187 */       long current = this.value;
/* 188 */       double currentVal = Double.longBitsToDouble(current);
/* 189 */       double nextVal = currentVal + delta;
/* 190 */       long next = Double.doubleToRawLongBits(nextVal);
/* 191 */       if (updater.compareAndSet(this, current, next)) {
/* 192 */         return nextVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 202 */     return Double.toString(get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 210 */     return (int)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 218 */     return (long)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 226 */     return (float)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 233 */     return get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 243 */     s.defaultWriteObject();
/*     */     
/* 245 */     s.writeDouble(get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 253 */     s.defaultReadObject();
/*     */     
/* 255 */     set(s.readDouble());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AtomicDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */