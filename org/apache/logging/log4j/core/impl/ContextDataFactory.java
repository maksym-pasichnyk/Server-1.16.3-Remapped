/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.invoke.MethodType;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextDataFactory
/*     */ {
/*  48 */   private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
/*  49 */   private static final String CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
/*  50 */   private static final Class<? extends StringMap> CACHED_CLASS = createCachedClass(CLASS_NAME);
/*  51 */   private static final MethodHandle DEFAULT_CONSTRUCTOR = createDefaultConstructor(CACHED_CLASS);
/*  52 */   private static final MethodHandle INITIAL_CAPACITY_CONSTRUCTOR = createInitialCapacityConstructor(CACHED_CLASS);
/*     */   
/*  54 */   private static final StringMap EMPTY_STRING_MAP = createContextData(1);
/*     */   static {
/*  56 */     EMPTY_STRING_MAP.freeze();
/*     */   }
/*     */   
/*     */   private static Class<? extends StringMap> createCachedClass(String className) {
/*  60 */     if (className == null) {
/*  61 */       return null;
/*     */     }
/*     */     try {
/*  64 */       return LoaderUtil.loadClass(className).asSubclass(StringMap.class);
/*  65 */     } catch (Exception any) {
/*  66 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static MethodHandle createDefaultConstructor(Class<? extends StringMap> cachedClass) {
/*  71 */     if (cachedClass == null) {
/*  72 */       return null;
/*     */     }
/*     */     try {
/*  75 */       return LOOKUP.findConstructor(cachedClass, MethodType.methodType(void.class));
/*  76 */     } catch (NoSuchMethodException|IllegalAccessException ignored) {
/*  77 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static MethodHandle createInitialCapacityConstructor(Class<? extends StringMap> cachedClass) {
/*  82 */     if (cachedClass == null) {
/*  83 */       return null;
/*     */     }
/*     */     try {
/*  86 */       return LOOKUP.findConstructor(cachedClass, MethodType.methodType(void.class, int.class));
/*  87 */     } catch (NoSuchMethodException|IllegalAccessException ignored) {
/*  88 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static StringMap createContextData() {
/*  93 */     if (DEFAULT_CONSTRUCTOR == null) {
/*  94 */       return (StringMap)new SortedArrayStringMap();
/*     */     }
/*     */     try {
/*  97 */       return DEFAULT_CONSTRUCTOR.invoke();
/*  98 */     } catch (Throwable ignored) {
/*  99 */       return (StringMap)new SortedArrayStringMap();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static StringMap createContextData(int initialCapacity) {
/* 104 */     if (INITIAL_CAPACITY_CONSTRUCTOR == null) {
/* 105 */       return (StringMap)new SortedArrayStringMap(initialCapacity);
/*     */     }
/*     */     try {
/* 108 */       return INITIAL_CAPACITY_CONSTRUCTOR.invoke(initialCapacity);
/* 109 */     } catch (Throwable ignored) {
/* 110 */       return (StringMap)new SortedArrayStringMap(initialCapacity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringMap emptyFrozenContextData() {
/* 120 */     return EMPTY_STRING_MAP;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ContextDataFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */