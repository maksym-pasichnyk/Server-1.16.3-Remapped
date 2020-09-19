/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayUtils
/*     */ {
/*     */   public static int getLength(Object array) {
/*  48 */     if (array == null) {
/*  49 */       return 0;
/*     */     }
/*  51 */     return Array.getLength(array);
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
/*     */   private static Object remove(Object array, int index) {
/*  76 */     int length = getLength(array);
/*  77 */     if (index < 0 || index >= length) {
/*  78 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*     */     }
/*     */     
/*  81 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
/*  82 */     System.arraycopy(array, 0, result, 0, index);
/*  83 */     if (index < length - 1) {
/*  84 */       System.arraycopy(array, index + 1, result, index, length - index - 1);
/*     */     }
/*     */     
/*  87 */     return result;
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
/*     */   public static <T> T[] remove(T[] array, int index) {
/* 121 */     return (T[])remove(array, index);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\ArrayUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */