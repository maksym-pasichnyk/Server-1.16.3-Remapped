/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Throwables
/*     */ {
/*     */   @GwtIncompatible
/*     */   private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
/*  73 */     Preconditions.checkNotNull(throwable);
/*  74 */     if (declaredType.isInstance(throwable)) {
/*  75 */       throw (X)declaredType.cast(throwable);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfInstanceOf(@Nullable Throwable throwable, Class<X> declaredType) throws X {
/* 102 */     if (throwable != null) {
/* 103 */       throwIfInstanceOf(throwable, declaredType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void throwIfUnchecked(Throwable throwable) {
/* 127 */     Preconditions.checkNotNull(throwable);
/* 128 */     if (throwable instanceof RuntimeException) {
/* 129 */       throw (RuntimeException)throwable;
/*     */     }
/* 131 */     if (throwable instanceof Error) {
/* 132 */       throw (Error)throwable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static void propagateIfPossible(@Nullable Throwable throwable) {
/* 157 */     if (throwable != null) {
/* 158 */       throwIfUnchecked(throwable);
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
/*     */ 
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
/*     */   public static <X extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X> declaredType) throws X {
/* 183 */     propagateIfInstanceOf(throwable, declaredType);
/* 184 */     propagateIfPossible(throwable);
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
/*     */   @GwtIncompatible
/*     */   public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2 {
/* 202 */     Preconditions.checkNotNull(declaredType2);
/* 203 */     propagateIfInstanceOf(throwable, declaredType1);
/* 204 */     propagateIfPossible(throwable, declaredType2);
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static RuntimeException propagate(Throwable throwable) {
/* 239 */     throwIfUnchecked(throwable);
/* 240 */     throw new RuntimeException(throwable);
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
/*     */   public static Throwable getRootCause(Throwable throwable) {
/*     */     Throwable cause;
/* 253 */     while ((cause = throwable.getCause()) != null) {
/* 254 */       throwable = cause;
/*     */     }
/* 256 */     return throwable;
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
/*     */   @Beta
/*     */   public static List<Throwable> getCausalChain(Throwable throwable) {
/* 276 */     Preconditions.checkNotNull(throwable);
/* 277 */     List<Throwable> causes = new ArrayList<>(4);
/* 278 */     while (throwable != null) {
/* 279 */       causes.add(throwable);
/* 280 */       throwable = throwable.getCause();
/*     */     } 
/* 282 */     return Collections.unmodifiableList(causes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static String getStackTraceAsString(Throwable throwable) {
/* 293 */     StringWriter stringWriter = new StringWriter();
/* 294 */     throwable.printStackTrace(new PrintWriter(stringWriter));
/* 295 */     return stringWriter.toString();
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
/* 329 */     return lazyStackTraceIsLazy() ? 
/* 330 */       jlaStackTrace(throwable) : 
/* 331 */       Collections.<StackTraceElement>unmodifiableList(Arrays.asList(throwable.getStackTrace()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static boolean lazyStackTraceIsLazy() {
/* 343 */     return ((getStackTraceElementMethod != null)) & ((getStackTraceDepthMethod != null));
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
/* 348 */     Preconditions.checkNotNull(t);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     return new AbstractList<StackTraceElement>()
/*     */       {
/*     */         public StackTraceElement get(int n) {
/* 358 */           return 
/* 359 */             (StackTraceElement)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, new Object[] { this.val$t, Integer.valueOf(n) });
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 364 */           return ((Integer)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, new Object[] { this.val$t })).intValue();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params) {
/*     */     try {
/* 373 */       return method.invoke(receiver, params);
/* 374 */     } catch (IllegalAccessException e) {
/* 375 */       throw new RuntimeException(e);
/* 376 */     } catch (InvocationTargetException e) {
/* 377 */       throw propagate(e.getCause());
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
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 393 */   private static final Object jla = getJLA();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 401 */   private static final Method getStackTraceElementMethod = (jla == null) ? null : getGetMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 409 */   private static final Method getStackTraceDepthMethod = (jla == null) ? null : getSizeMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Object getJLA() {
/*     */     try {
/* 423 */       Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, null);
/* 424 */       Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", new Class[0]);
/* 425 */       return langAccess.invoke(null, new Object[0]);
/* 426 */     } catch (ThreadDeath death) {
/* 427 */       throw death;
/* 428 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 433 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getGetMethod() {
/* 444 */     return getJlaMethod("getStackTraceElement", new Class[] { Throwable.class, int.class });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getSizeMethod() {
/* 454 */     return getJlaMethod("getStackTraceDepth", new Class[] { Throwable.class });
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
/*     */     try {
/* 461 */       return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(name, parameterTypes);
/* 462 */     } catch (ThreadDeath death) {
/* 463 */       throw death;
/* 464 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 469 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */