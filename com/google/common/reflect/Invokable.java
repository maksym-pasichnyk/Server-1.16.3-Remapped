/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ @Beta
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*     */   <M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member> Invokable(M member) {
/*  60 */     super(member);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Invokable<?, Object> from(Method method) {
/*  65 */     return new MethodInvokable(method);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
/*  70 */     return new ConstructorInvokable<>(constructor);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final R invoke(@Nullable T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
/* 100 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeToken<? extends R> getReturnType() {
/* 107 */     return (TypeToken)TypeToken.of(getGenericReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<Parameter> getParameters() {
/* 116 */     Type[] parameterTypes = getGenericParameterTypes();
/* 117 */     Annotation[][] annotations = getParameterAnnotations();
/* 118 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 119 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 120 */       builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i]));
/*     */     }
/* 122 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
/* 127 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 128 */     for (Type type : getGenericExceptionTypes()) {
/*     */ 
/*     */ 
/*     */       
/* 132 */       TypeToken<? extends Throwable> exceptionType = (TypeToken)TypeToken.of(type);
/* 133 */       builder.add(exceptionType);
/*     */     } 
/* 135 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
/* 145 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
/* 150 */     if (!returnType.isSupertypeOf(getReturnType())) {
/* 151 */       throw new IllegalArgumentException("Invokable is known to return " + 
/* 152 */           getReturnType() + ", not " + returnType);
/*     */     }
/*     */     
/* 155 */     Invokable<T, R1> specialized = this;
/* 156 */     return specialized;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? super T> getDeclaringClass() {
/* 162 */     return (Class)super.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeToken<T> getOwnerType() {
/* 170 */     return TypeToken.of((Class)getDeclaringClass());
/*     */   }
/*     */   
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */   abstract Object invokeInternal(@Nullable Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   static class MethodInvokable<T>
/*     */     extends Invokable<T, Object> {
/*     */     MethodInvokable(Method method) {
/* 190 */       super(method);
/* 191 */       this.method = method;
/*     */     }
/*     */     
/*     */     final Method method;
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 197 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 202 */       return this.method.getGenericReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 207 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 212 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 217 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */ 
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 222 */       return (TypeVariable<?>[])this.method.getTypeParameters();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 227 */       return (!isFinal() && 
/* 228 */         !isPrivate() && 
/* 229 */         !isStatic() && 
/* 230 */         !Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 235 */       return this.method.isVarArgs();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T>
/*     */     extends Invokable<T, T> {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 244 */       super(constructor);
/* 245 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/*     */       try {
/* 252 */         return this.constructor.newInstance(args);
/* 253 */       } catch (InstantiationException e) {
/* 254 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 263 */       Class<?> declaringClass = getDeclaringClass();
/* 264 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])declaringClass.getTypeParameters();
/* 265 */       if (arrayOfTypeVariable.length > 0) {
/* 266 */         return Types.newParameterizedType(declaringClass, (Type[])arrayOfTypeVariable);
/*     */       }
/* 268 */       return declaringClass;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 274 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 275 */       if (types.length > 0 && mayNeedHiddenThis()) {
/* 276 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 277 */         if (types.length == rawParamTypes.length && rawParamTypes[0] == 
/* 278 */           getDeclaringClass().getEnclosingClass())
/*     */         {
/* 280 */           return Arrays.<Type>copyOfRange(types, 1, types.length);
/*     */         }
/*     */       } 
/* 283 */       return types;
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 288 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 293 */       return this.constructor.getParameterAnnotations();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 307 */       TypeVariable[] arrayOfTypeVariable1 = (TypeVariable[])getDeclaringClass().getTypeParameters();
/* 308 */       TypeVariable[] arrayOfTypeVariable2 = (TypeVariable[])this.constructor.getTypeParameters();
/* 309 */       TypeVariable[] arrayOfTypeVariable3 = new TypeVariable[arrayOfTypeVariable1.length + arrayOfTypeVariable2.length];
/*     */       
/* 311 */       System.arraycopy(arrayOfTypeVariable1, 0, arrayOfTypeVariable3, 0, arrayOfTypeVariable1.length);
/* 312 */       System.arraycopy(arrayOfTypeVariable2, 0, arrayOfTypeVariable3, arrayOfTypeVariable1.length, arrayOfTypeVariable2.length);
/*     */ 
/*     */ 
/*     */       
/* 316 */       return (TypeVariable<?>[])arrayOfTypeVariable3;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 321 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 326 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 330 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 331 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 333 */         return true;
/*     */       }
/* 335 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 336 */       if (enclosingMethod != null)
/*     */       {
/* 338 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 346 */       return (declaringClass.getEnclosingClass() != null && 
/* 347 */         !Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */