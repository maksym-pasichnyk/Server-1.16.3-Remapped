/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   public enum KeyPresentBehavior
/*     */   {
/*  51 */     ANY_PRESENT
/*     */     {
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  55 */         return foundIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */     
/*  61 */     LAST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  67 */         int lower = foundIndex;
/*  68 */         int upper = list.size() - 1;
/*     */         
/*  70 */         while (lower < upper) {
/*  71 */           int middle = lower + upper + 1 >>> 1;
/*  72 */           int c = comparator.compare(list.get(middle), key);
/*  73 */           if (c > 0) {
/*  74 */             upper = middle - 1; continue;
/*     */           } 
/*  76 */           lower = middle;
/*     */         } 
/*     */         
/*  79 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */     
/*  85 */     FIRST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  91 */         int lower = 0;
/*  92 */         int upper = foundIndex;
/*     */ 
/*     */         
/*  95 */         while (lower < upper) {
/*  96 */           int middle = lower + upper >>> 1;
/*  97 */           int c = comparator.compare(list.get(middle), key);
/*  98 */           if (c < 0) {
/*  99 */             lower = middle + 1; continue;
/*     */           } 
/* 101 */           upper = middle;
/*     */         } 
/*     */         
/* 104 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     FIRST_AFTER
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 115 */         return LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     LAST_BEFORE
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 126 */         return FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <E> int resultIndex(Comparator<? super E> param1Comparator, E param1E, List<? extends E> param1List, int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum KeyAbsentBehavior
/*     */   {
/* 143 */     NEXT_LOWER
/*     */     {
/*     */       int resultIndex(int higherIndex) {
/* 146 */         return higherIndex - 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     NEXT_HIGHER
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 156 */         return higherIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     INVERTED_INSERTION_INDEX
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 174 */         return higherIndex ^ 0xFFFFFFFF;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract int resultIndex(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 193 */     Preconditions.checkNotNull(e);
/* 194 */     return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 209 */     return binarySearch(list, keyFunction, key, 
/* 210 */         Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 227 */     return binarySearch(
/* 228 */         Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */   public static <E> int binarySearch(List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 260 */     Preconditions.checkNotNull(comparator);
/* 261 */     Preconditions.checkNotNull(list);
/* 262 */     Preconditions.checkNotNull(presentBehavior);
/* 263 */     Preconditions.checkNotNull(absentBehavior);
/* 264 */     if (!(list instanceof java.util.RandomAccess)) {
/* 265 */       list = Lists.newArrayList(list);
/*     */     }
/*     */ 
/*     */     
/* 269 */     int lower = 0;
/* 270 */     int upper = list.size() - 1;
/*     */     
/* 272 */     while (lower <= upper) {
/* 273 */       int middle = lower + upper >>> 1;
/* 274 */       int c = comparator.compare(key, list.get(middle));
/* 275 */       if (c < 0) {
/* 276 */         upper = middle - 1; continue;
/* 277 */       }  if (c > 0) {
/* 278 */         lower = middle + 1; continue;
/*     */       } 
/* 280 */       return lower + presentBehavior
/* 281 */         .<E>resultIndex(comparator, key, list
/* 282 */           .subList(lower, upper + 1), middle - lower);
/*     */     } 
/*     */     
/* 285 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */