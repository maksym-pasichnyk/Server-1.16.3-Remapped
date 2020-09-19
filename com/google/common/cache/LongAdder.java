/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x) {
/*  55 */     return v + x;
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
/*     */   public void add(long x) {
/*  71 */     boolean uncontended = true; Striped64.Cell[] as; long b, v; int[] hc; Striped64.Cell a; int n;
/*  72 */     if (((as = this.cells) != null || !casBase(b = this.base, b + x)) && ((hc = threadHashCode.get()) == null || as == null || (n = as.length) < 1 || (a = as[n - 1 & hc[0]]) == null || 
/*     */ 
/*     */       
/*  75 */       !(uncontended = a.cas(v = a.value, v + x)))) {
/*  76 */       retryUpdate(x, hc, uncontended);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increment() {
/*  84 */     add(1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decrement() {
/*  91 */     add(-1L);
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
/*     */   public long sum() {
/* 104 */     long sum = this.base;
/* 105 */     Striped64.Cell[] as = this.cells;
/* 106 */     if (as != null) {
/* 107 */       int n = as.length;
/* 108 */       for (int i = 0; i < n; i++) {
/* 109 */         Striped64.Cell a = as[i];
/* 110 */         if (a != null)
/* 111 */           sum += a.value; 
/*     */       } 
/*     */     } 
/* 114 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 125 */     internalReset(0L);
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
/*     */   public long sumThenReset() {
/* 139 */     long sum = this.base;
/* 140 */     Striped64.Cell[] as = this.cells;
/* 141 */     this.base = 0L;
/* 142 */     if (as != null) {
/* 143 */       int n = as.length;
/* 144 */       for (int i = 0; i < n; i++) {
/* 145 */         Striped64.Cell a = as[i];
/* 146 */         if (a != null) {
/* 147 */           sum += a.value;
/* 148 */           a.value = 0L;
/*     */         } 
/*     */       } 
/*     */     } 
/* 152 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return Long.toString(sum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 169 */     return sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 177 */     return (int)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 185 */     return (float)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 193 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 197 */     s.defaultWriteObject();
/* 198 */     s.writeLong(sum());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 203 */     s.defaultReadObject();
/* 204 */     this.busy = 0;
/* 205 */     this.cells = null;
/* 206 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */