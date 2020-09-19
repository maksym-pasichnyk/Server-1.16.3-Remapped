/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.logging.Level;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Closer
/*     */   implements Closeable
/*     */ {
/*  96 */   private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final Suppressor suppressor;
/*     */ 
/*     */   
/*     */   public static Closer create() {
/* 104 */     return new Closer(SUPPRESSOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private final Deque<Closeable> stack = new ArrayDeque<>(4);
/*     */   private Throwable thrown;
/*     */   
/*     */   @VisibleForTesting
/*     */   Closer(Suppressor suppressor) {
/* 115 */     this.suppressor = (Suppressor)Preconditions.checkNotNull(suppressor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <C extends Closeable> C register(@Nullable C closeable) {
/* 127 */     if (closeable != null) {
/* 128 */       this.stack.addFirst((Closeable)closeable);
/*     */     }
/*     */     
/* 131 */     return closeable;
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
/*     */   public RuntimeException rethrow(Throwable e) throws IOException {
/* 148 */     Preconditions.checkNotNull(e);
/* 149 */     this.thrown = e;
/* 150 */     Throwables.propagateIfPossible(e, IOException.class);
/* 151 */     throw new RuntimeException(e);
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
/*     */   public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
/* 170 */     Preconditions.checkNotNull(e);
/* 171 */     this.thrown = e;
/* 172 */     Throwables.propagateIfPossible(e, IOException.class);
/* 173 */     Throwables.propagateIfPossible(e, declaredType);
/* 174 */     throw new RuntimeException(e);
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
/*     */   public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
/* 194 */     Preconditions.checkNotNull(e);
/* 195 */     this.thrown = e;
/* 196 */     Throwables.propagateIfPossible(e, IOException.class);
/* 197 */     Throwables.propagateIfPossible(e, declaredType1, declaredType2);
/* 198 */     throw new RuntimeException(e);
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
/*     */   public void close() throws IOException {
/* 210 */     Throwable throwable = this.thrown;
/*     */ 
/*     */     
/* 213 */     while (!this.stack.isEmpty()) {
/* 214 */       Closeable closeable = this.stack.removeFirst();
/*     */       try {
/* 216 */         closeable.close();
/* 217 */       } catch (Throwable e) {
/* 218 */         if (throwable == null) {
/* 219 */           throwable = e; continue;
/*     */         } 
/* 221 */         this.suppressor.suppress(closeable, throwable, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 226 */     if (this.thrown == null && throwable != null) {
/* 227 */       Throwables.propagateIfPossible(throwable, IOException.class);
/* 228 */       throw new AssertionError(throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface Suppressor
/*     */   {
/*     */     void suppress(Closeable param1Closeable, Throwable param1Throwable1, Throwable param1Throwable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class LoggingSuppressor
/*     */     implements Suppressor
/*     */   {
/* 251 */     static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 256 */       Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SuppressingSuppressor
/*     */     implements Suppressor
/*     */   {
/* 268 */     static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
/*     */     
/*     */     static boolean isAvailable() {
/* 271 */       return (addSuppressed != null);
/*     */     }
/*     */     
/* 274 */     static final Method addSuppressed = getAddSuppressed();
/*     */     
/*     */     private static Method getAddSuppressed() {
/*     */       try {
/* 278 */         return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
/* 279 */       } catch (Throwable e) {
/* 280 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 287 */       if (thrown == suppressed) {
/*     */         return;
/*     */       }
/*     */       try {
/* 291 */         addSuppressed.invoke(thrown, new Object[] { suppressed });
/* 292 */       } catch (Throwable e) {
/*     */         
/* 294 */         Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */