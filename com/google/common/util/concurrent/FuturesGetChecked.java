/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.Nullable;
/*     */ import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class FuturesGetChecked
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/*  48 */     return getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @VisibleForTesting
/*     */   static <V, X extends Exception> V getChecked(GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass) throws X {
/*  58 */     validator.validateClass(exceptionClass);
/*     */     try {
/*  60 */       return future.get();
/*  61 */     } catch (InterruptedException e) {
/*  62 */       Thread.currentThread().interrupt();
/*  63 */       throw newWithCause(exceptionClass, e);
/*  64 */     } catch (ExecutionException e) {
/*  65 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  66 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/*  77 */     bestGetCheckedTypeValidator().validateClass(exceptionClass);
/*     */     try {
/*  79 */       return future.get(timeout, unit);
/*  80 */     } catch (InterruptedException e) {
/*  81 */       Thread.currentThread().interrupt();
/*  82 */       throw newWithCause(exceptionClass, e);
/*  83 */     } catch (TimeoutException e) {
/*  84 */       throw newWithCause(exceptionClass, e);
/*  85 */     } catch (ExecutionException e) {
/*  86 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  87 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
/*  97 */     return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator weakSetValidator() {
/* 102 */     return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator classValueValidator() {
/* 108 */     return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class GetCheckedTypeValidatorHolder
/*     */   {
/* 119 */     static final String CLASS_VALUE_VALIDATOR_NAME = GetCheckedTypeValidatorHolder.class
/* 120 */       .getName() + "$ClassValueValidator";
/*     */     
/* 122 */     static final FuturesGetChecked.GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     enum ClassValueValidator
/*     */       implements FuturesGetChecked.GetCheckedTypeValidator {
/* 127 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>()
/*     */         {
/*     */           protected Boolean computeValue(Class<?> type)
/*     */           {
/* 137 */             FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
/* 138 */             return Boolean.valueOf(true);
/*     */           }
/*     */         }; static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 144 */         isValidClass.get(exceptionClass);
/*     */       }
/*     */     }
/*     */     
/*     */     enum WeakSetValidator implements FuturesGetChecked.GetCheckedTypeValidator {
/* 149 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 159 */       private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>();
/*     */       static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 164 */         for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
/* 165 */           if (exceptionClass.equals(knownGood.get())) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */         
/* 170 */         FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         if (validClasses.size() > 1000) {
/* 182 */           validClasses.clear();
/*     */         }
/*     */         
/* 185 */         validClasses.add(new WeakReference<>(exceptionClass));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FuturesGetChecked.GetCheckedTypeValidator getBestValidator() {
/*     */       try {
/* 195 */         Class<?> theClass = Class.forName(CLASS_VALUE_VALIDATOR_NAME);
/* 196 */         return (FuturesGetChecked.GetCheckedTypeValidator)theClass.getEnumConstants()[0];
/* 197 */       } catch (Throwable t) {
/* 198 */         return FuturesGetChecked.weakSetValidator();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
/* 206 */     if (cause instanceof Error) {
/* 207 */       throw (X)new ExecutionError((Error)cause);
/*     */     }
/* 209 */     if (cause instanceof RuntimeException) {
/* 210 */       throw (X)new UncheckedExecutionException(cause);
/*     */     }
/* 212 */     throw newWithCause(exceptionClass, cause);
/*     */   }
/*     */   @IgnoreJRERequirement enum ClassValueValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE;
/*     */     private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>() { protected Boolean computeValue(Class<?> type) { FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class)); return Boolean.valueOf(true); } }
/*     */     ; static {  } public void validateClass(Class<? extends Exception> exceptionClass) { isValidClass.get(exceptionClass); } } enum WeakSetValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE; private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>(); static {  } public void validateClass(Class<? extends Exception> exceptionClass) { for (WeakReference<Class<? extends Exception>> knownGood : validClasses) { if (exceptionClass.equals(knownGood.get()))
/*     */           return;  }
/*     */        FuturesGetChecked.checkExceptionClassValidity(exceptionClass); if (validClasses.size() > 1000)
/*     */         validClasses.clear();  validClasses.add(new WeakReference<>(exceptionClass)); }
/*     */   } private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> exceptionClass) { try {
/* 223 */       Exception unused = newWithCause((Class)exceptionClass, new Exception());
/* 224 */       return true;
/* 225 */     } catch (Exception e) {
/* 226 */       return false;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
/* 233 */     List<Constructor<X>> constructors = (List)Arrays.asList(exceptionClass.getConstructors());
/* 234 */     for (Constructor<X> constructor : preferringStrings(constructors)) {
/* 235 */       Exception exception = newFromConstructor(constructor, cause);
/* 236 */       if (exception != null) {
/* 237 */         if (exception.getCause() == null) {
/* 238 */           exception.initCause(cause);
/*     */         }
/* 240 */         return (X)exception;
/*     */       } 
/*     */     } 
/* 243 */     throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
/* 252 */     return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
/*     */   }
/*     */ 
/*     */   
/* 256 */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural()
/* 257 */     .onResultOf(new Function<Constructor<?>, Boolean>()
/*     */       {
/*     */         public Boolean apply(Constructor<?> input)
/*     */         {
/* 261 */           return Boolean.valueOf(Arrays.<Class<?>>asList(input.getParameterTypes()).contains(String.class));
/*     */         }
/* 264 */       }).reverse();
/*     */   
/*     */   @Nullable
/*     */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 268 */     Class<?>[] paramTypes = constructor.getParameterTypes();
/* 269 */     Object[] params = new Object[paramTypes.length];
/* 270 */     for (int i = 0; i < paramTypes.length; i++) {
/* 271 */       Class<?> paramType = paramTypes[i];
/* 272 */       if (paramType.equals(String.class)) {
/* 273 */         params[i] = cause.toString();
/* 274 */       } else if (paramType.equals(Throwable.class)) {
/* 275 */         params[i] = cause;
/*     */       } else {
/* 277 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 281 */       return constructor.newInstance(params);
/* 282 */     } catch (IllegalArgumentException e) {
/* 283 */       return null;
/* 284 */     } catch (InstantiationException e) {
/* 285 */       return null;
/* 286 */     } catch (IllegalAccessException e) {
/* 287 */       return null;
/* 288 */     } catch (InvocationTargetException e) {
/* 289 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isCheckedException(Class<? extends Exception> type) {
/* 295 */     return !RuntimeException.class.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void checkExceptionClassValidity(Class<? extends Exception> exceptionClass) {
/* 300 */     Preconditions.checkArgument(
/* 301 */         isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
/*     */ 
/*     */     
/* 304 */     Preconditions.checkArgument(
/* 305 */         hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface GetCheckedTypeValidator {
/*     */     void validateClass(Class<? extends Exception> param1Class);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\FuturesGetChecked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */