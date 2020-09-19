/*     */ package io.netty.util.internal.shaded.org.jctools.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnsafeRefArrayAccess
/*     */ {
/*     */   static {
/*  40 */     int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
/*  41 */     if (4 == scale) {
/*     */       
/*  43 */       REF_ELEMENT_SHIFT = 2;
/*     */     }
/*  45 */     else if (8 == scale) {
/*     */       
/*  47 */       REF_ELEMENT_SHIFT = 3;
/*     */     }
/*     */     else {
/*     */       
/*  51 */       throw new IllegalStateException("Unknown pointer size");
/*     */     } 
/*  53 */   } public static final long REF_ARRAY_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int REF_ELEMENT_SHIFT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> void spElement(E[] buffer, long offset, E e) {
/*  65 */     UnsafeAccess.UNSAFE.putObject(buffer, offset, e);
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
/*     */   public static <E> void soElement(E[] buffer, long offset, E e) {
/*  77 */     UnsafeAccess.UNSAFE.putOrderedObject(buffer, offset, e);
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
/*     */   public static <E> E lpElement(E[] buffer, long offset) {
/*  90 */     return (E)UnsafeAccess.UNSAFE.getObject(buffer, offset);
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
/*     */   public static <E> E lvElement(E[] buffer, long offset) {
/* 103 */     return (E)UnsafeAccess.UNSAFE.getObjectVolatile(buffer, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long calcElementOffset(long index) {
/* 112 */     return REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctool\\util\UnsafeRefArrayAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */