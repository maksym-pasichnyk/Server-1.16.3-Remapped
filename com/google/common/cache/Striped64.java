/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class Striped64
/*     */   extends Number
/*     */ {
/*     */   static final class Cell
/*     */   {
/*     */     volatile long p0;
/*     */     volatile long p1;
/*     */     volatile long p2;
/*     */     volatile long p3;
/*     */     volatile long p4;
/*     */     volatile long p5;
/*     */     volatile long p6;
/*     */     volatile long value;
/*     */     volatile long q0;
/*     */     volatile long q1;
/*     */     volatile long q2;
/*     */     volatile long q3;
/*     */     volatile long q4;
/*     */     volatile long q5;
/*     */     volatile long q6;
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long valueOffset;
/*     */     
/*     */     Cell(long x) {
/*  99 */       this.value = x;
/*     */     }
/*     */     final boolean cas(long cmp, long val) {
/* 102 */       return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       try {
/* 110 */         UNSAFE = Striped64.getUnsafe();
/* 111 */         Class<?> ak = Cell.class;
/*     */         
/* 113 */         valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
/* 114 */       } catch (Exception e) {
/* 115 */         throw new Error(e);
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
/* 127 */   static final ThreadLocal<int[]> threadHashCode = (ThreadLocal)new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   static final Random rng = new Random();
/*     */ 
/*     */   
/* 135 */   static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient Cell[] cells;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient long base;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient int busy;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Unsafe UNSAFE;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long baseOffset;
/*     */ 
/*     */   
/*     */   private static final long busyOffset;
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean casBase(long cmp, long val) {
/* 163 */     return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean casBusy() {
/* 170 */     return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
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
/*     */   final void retryUpdate(long x, int[] hc, boolean wasUncontended) {
/*     */     int h;
/* 197 */     if (hc == null) {
/* 198 */       threadHashCode.set(hc = new int[1]);
/* 199 */       int r = rng.nextInt();
/* 200 */       h = hc[0] = (r == 0) ? 1 : r;
/*     */     } else {
/*     */       
/* 203 */       h = hc[0];
/* 204 */     }  boolean collide = false; while (true) {
/*     */       Cell[] as;
/*     */       int n;
/* 207 */       if ((as = this.cells) != null && (n = as.length) > 0) {
/* 208 */         Cell a; if ((a = as[n - 1 & h]) == null)
/* 209 */         { if (this.busy == 0) {
/* 210 */             Cell r = new Cell(x);
/* 211 */             if (this.busy == 0 && casBusy()) {
/* 212 */               boolean created = false; try {
/*     */                 Cell[] rs; int m;
/*     */                 int j;
/* 215 */                 if ((rs = this.cells) != null && (m = rs.length) > 0 && rs[j = m - 1 & h] == null) {
/*     */ 
/*     */                   
/* 218 */                   rs[j] = r;
/* 219 */                   created = true;
/*     */                 } 
/*     */               } finally {
/* 222 */                 this.busy = 0;
/*     */               } 
/* 224 */               if (created)
/*     */                 break; 
/*     */               continue;
/*     */             } 
/*     */           } 
/* 229 */           collide = false; }
/*     */         
/* 231 */         else if (!wasUncontended)
/* 232 */         { wasUncontended = true; }
/* 233 */         else { long l; if (a.cas(l = a.value, fn(l, x)))
/*     */             break; 
/* 235 */           if (n >= NCPU || this.cells != as) {
/* 236 */             collide = false;
/* 237 */           } else if (!collide) {
/* 238 */             collide = true;
/* 239 */           } else if (this.busy == 0 && casBusy()) {
/*     */             try {
/* 241 */               if (this.cells == as) {
/* 242 */                 Cell[] rs = new Cell[n << 1];
/* 243 */                 for (int i = 0; i < n; i++)
/* 244 */                   rs[i] = as[i]; 
/* 245 */                 this.cells = rs;
/*     */               } 
/*     */             } finally {
/* 248 */               this.busy = 0;
/*     */             } 
/* 250 */             collide = false; continue;
/*     */           }  }
/*     */         
/* 253 */         h ^= h << 13;
/* 254 */         h ^= h >>> 17;
/* 255 */         h ^= h << 5;
/* 256 */         hc[0] = h; continue;
/*     */       } 
/* 258 */       if (this.busy == 0 && this.cells == as && casBusy()) {
/* 259 */         boolean init = false;
/*     */         try {
/* 261 */           if (this.cells == as) {
/* 262 */             Cell[] rs = new Cell[2];
/* 263 */             rs[h & 0x1] = new Cell(x);
/* 264 */             this.cells = rs;
/* 265 */             init = true;
/*     */           } 
/*     */         } finally {
/* 268 */           this.busy = 0;
/*     */         } 
/* 270 */         if (init)
/*     */           break;  continue;
/*     */       }  long v;
/* 273 */       if (casBase(v = this.base, fn(v, x))) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void internalReset(long initialValue) {
/* 282 */     Cell[] as = this.cells;
/* 283 */     this.base = initialValue;
/* 284 */     if (as != null) {
/* 285 */       int n = as.length;
/* 286 */       for (int i = 0; i < n; i++) {
/* 287 */         Cell a = as[i];
/* 288 */         if (a != null) {
/* 289 */           a.value = initialValue;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 300 */       UNSAFE = getUnsafe();
/* 301 */       Class<?> sk = Striped64.class;
/*     */       
/* 303 */       baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
/*     */       
/* 305 */       busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
/* 306 */     } catch (Exception e) {
/* 307 */       throw new Error(e);
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
/*     */   private static Unsafe getUnsafe() {
/*     */     try {
/* 320 */       return Unsafe.getUnsafe();
/* 321 */     } catch (SecurityException securityException) {
/*     */       try {
/* 323 */         return 
/* 324 */           AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
/*     */               public Unsafe run() throws Exception {
/* 326 */                 Class<Unsafe> k = Unsafe.class;
/* 327 */                 for (Field f : k.getDeclaredFields()) {
/* 328 */                   f.setAccessible(true);
/* 329 */                   Object x = f.get(null);
/* 330 */                   if (k.isInstance(x))
/* 331 */                     return k.cast(x); 
/*     */                 } 
/* 333 */                 throw new NoSuchFieldError("the Unsafe"); }
/*     */             });
/* 335 */       } catch (PrivilegedActionException e) {
/* 336 */         throw new RuntimeException("Could not initialize intrinsics", e
/* 337 */             .getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract long fn(long paramLong1, long paramLong2);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\Striped64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */