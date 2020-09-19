/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeParameterMatcher
/*     */ {
/*  29 */   private static final TypeParameterMatcher NOOP = new TypeParameterMatcher()
/*     */     {
/*     */       public boolean match(Object msg) {
/*  32 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static TypeParameterMatcher get(Class<?> parameterType) {
/*  38 */     Map<Class<?>, TypeParameterMatcher> getCache = InternalThreadLocalMap.get().typeParameterMatcherGetCache();
/*     */     
/*  40 */     TypeParameterMatcher matcher = getCache.get(parameterType);
/*  41 */     if (matcher == null) {
/*  42 */       if (parameterType == Object.class) {
/*  43 */         matcher = NOOP;
/*     */       } else {
/*  45 */         matcher = new ReflectiveMatcher(parameterType);
/*     */       } 
/*  47 */       getCache.put(parameterType, matcher);
/*     */     } 
/*     */     
/*  50 */     return matcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeParameterMatcher find(Object object, Class<?> parametrizedSuperclass, String typeParamName) {
/*  57 */     Map<Class<?>, Map<String, TypeParameterMatcher>> findCache = InternalThreadLocalMap.get().typeParameterMatcherFindCache();
/*  58 */     Class<?> thisClass = object.getClass();
/*     */     
/*  60 */     Map<String, TypeParameterMatcher> map = findCache.get(thisClass);
/*  61 */     if (map == null) {
/*  62 */       map = new HashMap<String, TypeParameterMatcher>();
/*  63 */       findCache.put(thisClass, map);
/*     */     } 
/*     */     
/*  66 */     TypeParameterMatcher matcher = map.get(typeParamName);
/*  67 */     if (matcher == null) {
/*  68 */       matcher = get(find0(object, parametrizedSuperclass, typeParamName));
/*  69 */       map.put(typeParamName, matcher);
/*     */     } 
/*     */     
/*  72 */     return matcher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> find0(Object object, Class<?> parametrizedSuperclass, String typeParamName) {
/*  78 */     Class<?> thisClass = object.getClass();
/*  79 */     Class<?> currentClass = thisClass;
/*     */     while (true) {
/*  81 */       while (currentClass.getSuperclass() == parametrizedSuperclass) {
/*  82 */         int typeParamIndex = -1;
/*  83 */         TypeVariable[] arrayOfTypeVariable = currentClass.getSuperclass().getTypeParameters();
/*  84 */         for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  85 */           if (typeParamName.equals(arrayOfTypeVariable[i].getName())) {
/*  86 */             typeParamIndex = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*  91 */         if (typeParamIndex < 0) {
/*  92 */           throw new IllegalStateException("unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);
/*     */         }
/*     */ 
/*     */         
/*  96 */         Type genericSuperType = currentClass.getGenericSuperclass();
/*  97 */         if (!(genericSuperType instanceof ParameterizedType)) {
/*  98 */           return Object.class;
/*     */         }
/*     */         
/* 101 */         Type[] actualTypeParams = ((ParameterizedType)genericSuperType).getActualTypeArguments();
/*     */         
/* 103 */         Type actualTypeParam = actualTypeParams[typeParamIndex];
/* 104 */         if (actualTypeParam instanceof ParameterizedType) {
/* 105 */           actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
/*     */         }
/* 107 */         if (actualTypeParam instanceof Class) {
/* 108 */           return (Class)actualTypeParam;
/*     */         }
/* 110 */         if (actualTypeParam instanceof GenericArrayType) {
/* 111 */           Type componentType = ((GenericArrayType)actualTypeParam).getGenericComponentType();
/* 112 */           if (componentType instanceof ParameterizedType) {
/* 113 */             componentType = ((ParameterizedType)componentType).getRawType();
/*     */           }
/* 115 */           if (componentType instanceof Class) {
/* 116 */             return Array.newInstance((Class)componentType, 0).getClass();
/*     */           }
/*     */         } 
/* 119 */         if (actualTypeParam instanceof TypeVariable) {
/*     */           
/* 121 */           TypeVariable<?> v = (TypeVariable)actualTypeParam;
/* 122 */           currentClass = thisClass;
/* 123 */           if (!(v.getGenericDeclaration() instanceof Class)) {
/* 124 */             return Object.class;
/*     */           }
/*     */           
/* 127 */           parametrizedSuperclass = (Class)v.getGenericDeclaration();
/* 128 */           typeParamName = v.getName();
/* 129 */           if (parametrizedSuperclass.isAssignableFrom(thisClass)) {
/*     */             continue;
/*     */           }
/* 132 */           return Object.class;
/*     */         } 
/*     */ 
/*     */         
/* 136 */         return fail(thisClass, typeParamName);
/*     */       } 
/* 138 */       currentClass = currentClass.getSuperclass();
/* 139 */       if (currentClass == null) {
/* 140 */         return fail(thisClass, typeParamName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Class<?> fail(Class<?> type, String typeParamName) {
/* 146 */     throw new IllegalStateException("cannot determine the type of the type parameter '" + typeParamName + "': " + type);
/*     */   }
/*     */   
/*     */   public abstract boolean match(Object paramObject);
/*     */   
/*     */   private static final class ReflectiveMatcher
/*     */     extends TypeParameterMatcher {
/*     */     private final Class<?> type;
/*     */     
/*     */     ReflectiveMatcher(Class<?> type) {
/* 156 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(Object msg) {
/* 161 */       return this.type.isInstance(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\TypeParameterMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */