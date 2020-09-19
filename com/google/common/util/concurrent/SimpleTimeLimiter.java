/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*     */   public SimpleTimeLimiter(ExecutorService executor) {
/*  63 */     this.executor = (ExecutorService)Preconditions.checkNotNull(executor);
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
/*     */   public SimpleTimeLimiter() {
/*  75 */     this(Executors.newCachedThreadPool());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
/*  84 */     Preconditions.checkNotNull(target);
/*  85 */     Preconditions.checkNotNull(interfaceType);
/*  86 */     Preconditions.checkNotNull(timeoutUnit);
/*  87 */     Preconditions.checkArgument((timeoutDuration > 0L), "bad timeout: %s", timeoutDuration);
/*  88 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */     
/*  90 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */     
/*  92 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */         
/*     */         public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable
/*     */         {
/*  97 */           Callable<Object> callable = new Callable()
/*     */             {
/*     */               public Object call() throws Exception
/*     */               {
/*     */                 try {
/* 102 */                   return method.invoke(target, args);
/* 103 */                 } catch (InvocationTargetException e) {
/* 104 */                   throw SimpleTimeLimiter.throwCause(e, false);
/*     */                 } 
/*     */               }
/*     */             };
/* 108 */           return SimpleTimeLimiter.this.callWithTimeout(callable, timeoutDuration, timeoutUnit, interruptibleMethods
/* 109 */               .contains(method));
/*     */         }
/*     */       };
/* 112 */     return newProxy(interfaceType, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 121 */     Preconditions.checkNotNull(callable);
/* 122 */     Preconditions.checkNotNull(timeoutUnit);
/* 123 */     Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", timeoutDuration);
/* 124 */     Future<T> future = this.executor.submit(callable);
/*     */     try {
/* 126 */       if (amInterruptible) {
/*     */         try {
/* 128 */           return future.get(timeoutDuration, timeoutUnit);
/* 129 */         } catch (InterruptedException e) {
/* 130 */           future.cancel(true);
/* 131 */           throw e;
/*     */         } 
/*     */       }
/* 134 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     }
/* 136 */     catch (ExecutionException e) {
/* 137 */       throw throwCause(e, true);
/* 138 */     } catch (TimeoutException e) {
/* 139 */       future.cancel(true);
/* 140 */       throw new UncheckedTimeoutException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 145 */     Throwable cause = e.getCause();
/* 146 */     if (cause == null) {
/* 147 */       throw e;
/*     */     }
/* 149 */     if (combineStackTraces) {
/*     */       
/* 151 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat((Object[])cause.getStackTrace(), (Object[])e.getStackTrace(), StackTraceElement.class);
/* 152 */       cause.setStackTrace(combined);
/*     */     } 
/* 154 */     if (cause instanceof Exception) {
/* 155 */       throw (Exception)cause;
/*     */     }
/* 157 */     if (cause instanceof Error) {
/* 158 */       throw (Error)cause;
/*     */     }
/*     */     
/* 161 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 165 */     Set<Method> set = Sets.newHashSet();
/* 166 */     for (Method m : interfaceType.getMethods()) {
/* 167 */       if (declaresInterruptedEx(m)) {
/* 168 */         set.add(m);
/*     */       }
/*     */     } 
/* 171 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 175 */     for (Class<?> exType : method.getExceptionTypes()) {
/*     */       
/* 177 */       if (exType == InterruptedException.class) {
/* 178 */         return true;
/*     */       }
/*     */     } 
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 187 */     Object object = Proxy.newProxyInstance(interfaceType
/* 188 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 189 */     return interfaceType.cast(object);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\SimpleTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */