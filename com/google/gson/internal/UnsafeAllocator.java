/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UnsafeAllocator
/*     */ {
/*     */   public static UnsafeAllocator create() {
/*     */     try {
/*  40 */       Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
/*  41 */       Field f = unsafeClass.getDeclaredField("theUnsafe");
/*  42 */       f.setAccessible(true);
/*  43 */       final Object unsafe = f.get(null);
/*  44 */       final Method allocateInstance = unsafeClass.getMethod("allocateInstance", new Class[] { Class.class });
/*  45 */       return new UnsafeAllocator()
/*     */         {
/*     */           public <T> T newInstance(Class<T> c) throws Exception
/*     */           {
/*  49 */             UnsafeAllocator.assertInstantiable(c);
/*  50 */             return (T)allocateInstance.invoke(unsafe, new Object[] { c });
/*     */           }
/*     */         };
/*  53 */     } catch (Exception exception) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  63 */         Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
/*  64 */         getConstructorId.setAccessible(true);
/*  65 */         final int constructorId = ((Integer)getConstructorId.invoke(null, new Object[] { Object.class })).intValue();
/*     */         
/*  67 */         final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, int.class });
/*  68 */         newInstance.setAccessible(true);
/*  69 */         return new UnsafeAllocator()
/*     */           {
/*     */             public <T> T newInstance(Class<T> c) throws Exception
/*     */             {
/*  73 */               UnsafeAllocator.assertInstantiable(c);
/*  74 */               return (T)newInstance.invoke(null, new Object[] { c, Integer.valueOf(this.val$constructorId) });
/*     */             }
/*     */           };
/*  77 */       } catch (Exception exception1) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  87 */           final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
/*  88 */           newInstance.setAccessible(true);
/*  89 */           return new UnsafeAllocator()
/*     */             {
/*     */               public <T> T newInstance(Class<T> c) throws Exception
/*     */               {
/*  93 */                 UnsafeAllocator.assertInstantiable(c);
/*  94 */                 return (T)newInstance.invoke(null, new Object[] { c, Object.class });
/*     */               }
/*     */             };
/*  97 */         } catch (Exception exception2) {
/*     */ 
/*     */ 
/*     */           
/* 101 */           return new UnsafeAllocator()
/*     */             {
/*     */               public <T> T newInstance(Class<T> c) {
/* 104 */                 throw new UnsupportedOperationException("Cannot allocate " + c);
/*     */               }
/*     */             };
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void assertInstantiable(Class<?> c) {
/* 115 */     int modifiers = c.getModifiers();
/* 116 */     if (Modifier.isInterface(modifiers)) {
/* 117 */       throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + c.getName());
/*     */     }
/* 119 */     if (Modifier.isAbstract(modifiers))
/* 120 */       throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + c.getName()); 
/*     */   }
/*     */   
/*     */   public abstract <T> T newInstance(Class<T> paramClass) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\gson\internal\UnsafeAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */