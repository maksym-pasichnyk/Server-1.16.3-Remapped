/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class FinalizableReferenceQueue
/*     */   implements Closeable
/*     */ {
/* 128 */   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
/*     */ 
/*     */   
/*     */   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
/*     */   
/*     */   private static final Method startFinalizer;
/*     */ 
/*     */   
/*     */   static {
/* 137 */     Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
/* 138 */     startFinalizer = getStartFinalizer(finalizer);
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
/* 158 */   final ReferenceQueue<Object> queue = new ReferenceQueue();
/* 159 */   final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue); public FinalizableReferenceQueue() {
/* 160 */     boolean threadStarted = false;
/*     */     try {
/* 162 */       startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
/* 163 */       threadStarted = true;
/* 164 */     } catch (IllegalAccessException impossible) {
/* 165 */       throw new AssertionError(impossible);
/* 166 */     } catch (Throwable t) {
/* 167 */       logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.threadStarted = threadStarted;
/*     */   }
/*     */   final boolean threadStarted;
/*     */   
/*     */   public void close() {
/* 179 */     this.frqRef.enqueue();
/* 180 */     cleanUp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanUp() {
/* 189 */     if (this.threadStarted) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Reference<?> reference;
/* 194 */     while ((reference = this.queue.poll()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       reference.clear();
/*     */       try {
/* 201 */         ((FinalizableReference)reference).finalizeReferent();
/* 202 */       } catch (Throwable t) {
/* 203 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
/* 214 */     for (FinalizerLoader loader : loaders) {
/* 215 */       Class<?> finalizer = loader.loadFinalizer();
/* 216 */       if (finalizer != null) {
/* 217 */         return finalizer;
/*     */       }
/*     */     } 
/*     */     
/* 221 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface FinalizerLoader
/*     */   {
/*     */     @Nullable
/*     */     Class<?> loadFinalizer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SystemLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     @VisibleForTesting
/*     */     static boolean disabled;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> loadFinalizer() {
/*     */       ClassLoader systemLoader;
/* 250 */       if (disabled) {
/* 251 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 255 */         systemLoader = ClassLoader.getSystemClassLoader();
/* 256 */       } catch (SecurityException e) {
/* 257 */         FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
/* 258 */         return null;
/*     */       } 
/* 260 */       if (systemLoader != null) {
/*     */         try {
/* 262 */           return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 263 */         } catch (ClassNotFoundException e) {
/*     */           
/* 265 */           return null;
/*     */         } 
/*     */       }
/* 268 */       return null;
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
/*     */   
/*     */   static class DecoupledLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 298 */         ClassLoader finalizerLoader = newLoader(getBaseUrl());
/* 299 */         return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 300 */       } catch (Exception e) {
/* 301 */         FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.", e);
/* 302 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseUrl() throws IOException {
/* 311 */       String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
/* 312 */       URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
/* 313 */       if (finalizerUrl == null) {
/* 314 */         throw new FileNotFoundException(finalizerPath);
/*     */       }
/*     */ 
/*     */       
/* 318 */       String urlString = finalizerUrl.toString();
/* 319 */       if (!urlString.endsWith(finalizerPath)) {
/* 320 */         throw new IOException("Unsupported path style: " + urlString);
/*     */       }
/* 322 */       urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
/* 323 */       return new URL(finalizerUrl, urlString);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URLClassLoader newLoader(URL base) {
/* 331 */       return new URLClassLoader(new URL[] { base }, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DirectLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 343 */         return Class.forName("com.google.common.base.internal.Finalizer");
/* 344 */       } catch (ClassNotFoundException e) {
/* 345 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Method getStartFinalizer(Class<?> finalizer) {
/*     */     try {
/* 355 */       return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
/*     */     }
/* 357 */     catch (NoSuchMethodException e) {
/* 358 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\FinalizableReferenceQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */