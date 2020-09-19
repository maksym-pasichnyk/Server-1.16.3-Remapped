/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ final class TopKSelector<T>
/*     */ {
/*     */   private final int k;
/*     */   private final Comparator<? super T> comparator;
/*     */   private final T[] buffer;
/*     */   private int bufferSize;
/*     */   private T threshold;
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
/*  63 */     return least(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
/*  74 */     return greatest(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
/*  84 */     return new TopKSelector<>(comparator, k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
/*  94 */     return new TopKSelector<>(Ordering.<T>from(comparator).reverse(), k);
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
/*     */   private TopKSelector(Comparator<? super T> comparator, int k) {
/* 115 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator, "comparator");
/* 116 */     this.k = k;
/* 117 */     Preconditions.checkArgument((k >= 0), "k must be nonnegative, was %s", k);
/* 118 */     this.buffer = (T[])new Object[k * 2];
/* 119 */     this.bufferSize = 0;
/* 120 */     this.threshold = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offer(@Nullable T elem) {
/* 128 */     if (this.k == 0)
/*     */       return; 
/* 130 */     if (this.bufferSize == 0) {
/* 131 */       this.buffer[0] = elem;
/* 132 */       this.threshold = elem;
/* 133 */       this.bufferSize = 1;
/* 134 */     } else if (this.bufferSize < this.k) {
/* 135 */       this.buffer[this.bufferSize++] = elem;
/* 136 */       if (this.comparator.compare(elem, this.threshold) > 0) {
/* 137 */         this.threshold = elem;
/*     */       }
/* 139 */     } else if (this.comparator.compare(elem, this.threshold) < 0) {
/*     */       
/* 141 */       this.buffer[this.bufferSize++] = elem;
/* 142 */       if (this.bufferSize == 2 * this.k) {
/* 143 */         trim();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 153 */     int left = 0;
/* 154 */     int right = 2 * this.k - 1;
/*     */     
/* 156 */     int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */     
/* 160 */     int iterations = 0;
/* 161 */     int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
/* 162 */     while (left < right) {
/* 163 */       int pivotIndex = left + right + 1 >>> 1;
/*     */       
/* 165 */       int pivotNewIndex = partition(left, right, pivotIndex);
/*     */       
/* 167 */       if (pivotNewIndex > this.k) {
/* 168 */         right = pivotNewIndex - 1;
/* 169 */       } else if (pivotNewIndex < this.k) {
/* 170 */         left = Math.max(pivotNewIndex, left + 1);
/* 171 */         minThresholdPosition = pivotNewIndex;
/*     */       } else {
/*     */         break;
/*     */       } 
/* 175 */       iterations++;
/* 176 */       if (iterations >= maxIterations) {
/*     */         
/* 178 */         Arrays.sort(this.buffer, left, right, this.comparator);
/*     */         break;
/*     */       } 
/*     */     } 
/* 182 */     this.bufferSize = this.k;
/*     */     
/* 184 */     this.threshold = this.buffer[minThresholdPosition];
/* 185 */     for (int i = minThresholdPosition + 1; i < this.k; i++) {
/* 186 */       if (this.comparator.compare(this.buffer[i], this.threshold) > 0) {
/* 187 */         this.threshold = this.buffer[i];
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
/*     */   private int partition(int left, int right, int pivotIndex) {
/* 199 */     T pivotValue = this.buffer[pivotIndex];
/* 200 */     this.buffer[pivotIndex] = this.buffer[right];
/*     */     
/* 202 */     int pivotNewIndex = left;
/* 203 */     for (int i = left; i < right; i++) {
/* 204 */       if (this.comparator.compare(this.buffer[i], pivotValue) < 0) {
/* 205 */         swap(pivotNewIndex, i);
/* 206 */         pivotNewIndex++;
/*     */       } 
/*     */     } 
/* 209 */     this.buffer[right] = this.buffer[pivotNewIndex];
/* 210 */     this.buffer[pivotNewIndex] = pivotValue;
/* 211 */     return pivotNewIndex;
/*     */   }
/*     */   
/*     */   private void swap(int i, int j) {
/* 215 */     T tmp = this.buffer[i];
/* 216 */     this.buffer[i] = this.buffer[j];
/* 217 */     this.buffer[j] = tmp;
/*     */   }
/*     */   
/*     */   TopKSelector<T> combine(TopKSelector<T> other) {
/* 221 */     for (int i = 0; i < other.bufferSize; i++) {
/* 222 */       offer(other.buffer[i]);
/*     */     }
/* 224 */     return this;
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
/*     */   public void offerAll(Iterable<? extends T> elements) {
/* 236 */     offerAll(elements.iterator());
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
/*     */   public void offerAll(Iterator<? extends T> elements) {
/* 249 */     while (elements.hasNext()) {
/* 250 */       offer(elements.next());
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
/*     */   public List<T> topK() {
/* 263 */     Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
/* 264 */     if (this.bufferSize > this.k) {
/* 265 */       Arrays.fill((Object[])this.buffer, this.k, this.buffer.length, (Object)null);
/* 266 */       this.bufferSize = this.k;
/* 267 */       this.threshold = this.buffer[this.k - 1];
/*     */     } 
/*     */     
/* 270 */     return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TopKSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */