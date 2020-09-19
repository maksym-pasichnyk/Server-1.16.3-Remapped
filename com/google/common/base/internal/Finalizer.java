/*     */ package com.google.common.base.internal;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class Finalizer
/*     */   implements Runnable
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakReference<Class<?>> finalizableReferenceClassReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PhantomReference<Object> frqReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReferenceQueue<Object> queue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/*  73 */     if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
/*  74 */       throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
/*     */     }
/*     */     
/*  77 */     Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
/*  78 */     Thread thread = new Thread(finalizer);
/*  79 */     thread.setName(Finalizer.class.getName());
/*  80 */     thread.setDaemon(true);
/*     */     
/*     */     try {
/*  83 */       if (inheritableThreadLocals != null) {
/*  84 */         inheritableThreadLocals.set(thread, null);
/*     */       }
/*  86 */     } catch (Throwable t) {
/*  87 */       logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/* 107 */     this.queue = queue;
/*     */     
/* 109 */     this.finalizableReferenceClassReference = new WeakReference<>(finalizableReferenceClass);
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.frqReference = frqReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/*     */         do {
/*     */         
/* 124 */         } while (cleanUp(this.queue.remove()));
/*     */         
/*     */         break;
/* 127 */       } catch (InterruptedException interruptedException) {}
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
/*     */   private boolean cleanUp(Reference<?> reference) {
/* 140 */     Method finalizeReferentMethod = getFinalizeReferentMethod();
/* 141 */     if (finalizeReferentMethod == null) {
/* 142 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 149 */       reference.clear();
/*     */       
/* 151 */       if (reference == this.frqReference)
/*     */       {
/*     */ 
/*     */         
/* 155 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 159 */         finalizeReferentMethod.invoke(reference, new Object[0]);
/* 160 */       } catch (Throwable t) {
/* 161 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 168 */       if ((reference = this.queue.poll()) == null)
/* 169 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method getFinalizeReferentMethod() {
/* 175 */     Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
/* 176 */     if (finalizableReferenceClass == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 183 */       return null;
/*     */     }
/*     */     try {
/* 186 */       return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
/* 187 */     } catch (NoSuchMethodException e) {
/* 188 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Field getInheritableThreadLocalsField() {
/*     */     try {
/* 195 */       Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
/* 196 */       inheritableThreadLocals.setAccessible(true);
/* 197 */       return inheritableThreadLocals;
/* 198 */     } catch (Throwable t) {
/* 199 */       logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
/*     */ 
/*     */ 
/*     */       
/* 203 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\internal\Finalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */