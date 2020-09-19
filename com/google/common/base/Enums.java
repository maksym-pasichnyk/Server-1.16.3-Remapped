/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Enums
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static Field getField(Enum<?> enumValue) {
/*  51 */     Class<?> clazz = enumValue.getDeclaringClass();
/*     */     try {
/*  53 */       return clazz.getDeclaredField(enumValue.name());
/*  54 */     } catch (NoSuchFieldException impossible) {
/*  55 */       throw new AssertionError(impossible);
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
/*     */   public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
/*  68 */     Preconditions.checkNotNull(enumClass);
/*  69 */     Preconditions.checkNotNull(value);
/*  70 */     return Platform.getEnumIfPresent(enumClass, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*  75 */   private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
/*  82 */     Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<>();
/*     */     
/*  84 */     for (Enum<?> enum_ : EnumSet.<T>allOf(enumClass)) {
/*  85 */       result.put(enum_.name(), new WeakReference<>(enum_));
/*     */     }
/*  87 */     enumConstantCache.put(enumClass, result);
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> enumClass) {
/*  94 */     synchronized (enumConstantCache) {
/*  95 */       Map<String, WeakReference<? extends Enum<?>>> constants = enumConstantCache.get(enumClass);
/*  96 */       if (constants == null) {
/*  97 */         constants = populateCache(enumClass);
/*     */       }
/*  99 */       return constants;
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
/*     */   public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> enumClass) {
/* 112 */     return new StringConverter<>(enumClass);
/*     */   }
/*     */   
/*     */   private static final class StringConverter<T extends Enum<T>>
/*     */     extends Converter<String, T> implements Serializable {
/*     */     private final Class<T> enumClass;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(Class<T> enumClass) {
/* 121 */       this.enumClass = Preconditions.<Class<T>>checkNotNull(enumClass);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doForward(String value) {
/* 126 */       return Enum.valueOf(this.enumClass, value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(T enumValue) {
/* 131 */       return enumValue.name();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 136 */       if (object instanceof StringConverter) {
/* 137 */         StringConverter<?> that = (StringConverter)object;
/* 138 */         return this.enumClass.equals(that.enumClass);
/*     */       } 
/* 140 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 145 */       return this.enumClass.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 150 */       return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Enums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */