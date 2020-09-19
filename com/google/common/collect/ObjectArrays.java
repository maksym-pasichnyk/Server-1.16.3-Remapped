/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ObjectArrays
/*     */ {
/*  37 */   static final Object[] EMPTY_ARRAY = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <T> T[] newArray(Class<T> type, int length) {
/*  50 */     return (T[])Array.newInstance(type, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T[] newArray(T[] reference, int length) {
/*  61 */     return Platform.newArray(reference, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
/*  73 */     T[] result = newArray(type, first.length + second.length);
/*  74 */     System.arraycopy(first, 0, result, 0, first.length);
/*  75 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  76 */     return result;
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
/*     */   public static <T> T[] concat(@Nullable T element, T[] array) {
/*  89 */     T[] result = newArray(array, array.length + 1);
/*  90 */     result[0] = element;
/*  91 */     System.arraycopy(array, 0, result, 1, array.length);
/*  92 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, @Nullable T element) {
/* 105 */     T[] result = Arrays.copyOf(array, array.length + 1);
/* 106 */     result[array.length] = element;
/* 107 */     return result;
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
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
/* 135 */     int size = c.size();
/* 136 */     if (array.length < size) {
/* 137 */       array = newArray(array, size);
/*     */     }
/* 139 */     fillArray(c, (Object[])array);
/* 140 */     if (array.length > size) {
/* 141 */       array[size] = null;
/*     */     }
/* 143 */     return array;
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
/*     */   static <T> T[] toArrayImpl(Object[] src, int offset, int len, T[] dst) {
/* 158 */     Preconditions.checkPositionIndexes(offset, offset + len, src.length);
/* 159 */     if (dst.length < len) {
/* 160 */       dst = newArray(dst, len);
/* 161 */     } else if (dst.length > len) {
/* 162 */       dst[len] = null;
/*     */     } 
/* 164 */     System.arraycopy(src, offset, dst, 0, len);
/* 165 */     return dst;
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
/*     */   static Object[] toArrayImpl(Collection<?> c) {
/* 183 */     return fillArray(c, new Object[c.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object[] copyAsObjectArray(Object[] elements, int offset, int length) {
/* 191 */     Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
/* 192 */     if (length == 0) {
/* 193 */       return EMPTY_ARRAY;
/*     */     }
/* 195 */     Object[] result = new Object[length];
/* 196 */     System.arraycopy(elements, offset, result, 0, length);
/* 197 */     return result;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 202 */     int i = 0;
/* 203 */     for (Object element : elements) {
/* 204 */       array[i++] = element;
/*     */     }
/* 206 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void swap(Object[] array, int i, int j) {
/* 213 */     Object temp = array[i];
/* 214 */     array[i] = array[j];
/* 215 */     array[j] = temp;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object... array) {
/* 220 */     return checkElementsNotNull(array, array.length);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object[] array, int length) {
/* 225 */     for (int i = 0; i < length; i++) {
/* 226 */       checkElementNotNull(array[i], i);
/*     */     }
/* 228 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object checkElementNotNull(Object element, int index) {
/* 235 */     if (element == null) {
/* 236 */       throw new NullPointerException("at index " + index);
/*     */     }
/* 238 */     return element;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ObjectArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */