/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ final class Types
/*     */ {
/*  56 */   private static final Function<Type, String> TYPE_NAME = new Function<Type, String>()
/*     */     {
/*     */       public String apply(Type from)
/*     */       {
/*  60 */         return Types.JavaVersion.CURRENT.typeName(from);
/*     */       }
/*     */     };
/*     */   
/*  64 */   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */   
/*     */   static Type newArrayType(Type componentType) {
/*  68 */     if (componentType instanceof WildcardType) {
/*  69 */       WildcardType wildcard = (WildcardType)componentType;
/*  70 */       Type[] lowerBounds = wildcard.getLowerBounds();
/*  71 */       Preconditions.checkArgument((lowerBounds.length <= 1), "Wildcard cannot have more than one lower bounds.");
/*  72 */       if (lowerBounds.length == 1) {
/*  73 */         return supertypeOf(newArrayType(lowerBounds[0]));
/*     */       }
/*  75 */       Type[] upperBounds = wildcard.getUpperBounds();
/*  76 */       Preconditions.checkArgument((upperBounds.length == 1), "Wildcard should have only one upper bound.");
/*  77 */       return subtypeOf(newArrayType(upperBounds[0]));
/*     */     } 
/*     */     
/*  80 */     return JavaVersion.CURRENT.newArrayType(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type ownerType, Class<?> rawType, Type... arguments) {
/*  89 */     if (ownerType == null) {
/*  90 */       return newParameterizedType(rawType, arguments);
/*     */     }
/*     */     
/*  93 */     Preconditions.checkNotNull(arguments);
/*  94 */     Preconditions.checkArgument((rawType.getEnclosingClass() != null), "Owner type for unenclosed %s", rawType);
/*  95 */     return new ParameterizedTypeImpl(ownerType, rawType, arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) {
/* 102 */     return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR
/* 103 */         .getOwnerType(rawType), rawType, arguments);
/*     */   }
/*     */   
/*     */   private enum ClassOwnership
/*     */   {
/* 108 */     OWNED_BY_ENCLOSING_CLASS
/*     */     {
/*     */       @Nullable
/*     */       Class<?> getOwnerType(Class<?> rawType) {
/* 112 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     },
/* 115 */     LOCAL_CLASS_HAS_NO_OWNER
/*     */     {
/*     */       @Nullable
/*     */       Class<?> getOwnerType(Class<?> rawType) {
/* 119 */         if (rawType.isLocalClass()) {
/* 120 */           return null;
/*     */         }
/* 122 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     static final ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();
/*     */ 
/*     */     
/*     */     private static ClassOwnership detectJvmBehavior() {
/* 134 */       Class<?> subclass = (new LocalClass<String>() {  }).getClass();
/* 135 */       ParameterizedType parameterizedType = (ParameterizedType)subclass.getGenericSuperclass();
/* 136 */       for (ClassOwnership behavior : values()) {
/* 137 */         if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
/* 138 */           return behavior;
/*     */         }
/*     */       } 
/* 141 */       throw new AssertionError();
/*     */     }
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     abstract Class<?> getOwnerType(Class<?> param1Class); }
/*     */   
/*     */   static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) {
/* 151 */     (new Type[1])[0] = Object.class; return newTypeVariableImpl(declaration, name, (bounds.length == 0) ? new Type[1] : bounds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType subtypeOf(Type upperBound) {
/* 158 */     return new WildcardTypeImpl(new Type[0], new Type[] { upperBound });
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType supertypeOf(Type lowerBound) {
/* 164 */     return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class });
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
/*     */   static String toString(Type type) {
/* 176 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static Type getComponentType(Type type) {
/* 181 */     Preconditions.checkNotNull(type);
/* 182 */     final AtomicReference<Type> result = new AtomicReference<>();
/* 183 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> t) {
/* 186 */           result.set(Types.subtypeOfComponentType(t.getBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType t) {
/* 191 */           result.set(Types.subtypeOfComponentType(t.getUpperBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType t) {
/* 196 */           result.set(t.getGenericComponentType());
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> t) {
/* 201 */           result.set(t.getComponentType());
/*     */         }
/* 203 */       }).visit(new Type[] { type });
/* 204 */     return result.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Type subtypeOfComponentType(Type[] bounds) {
/* 213 */     for (Type bound : bounds) {
/* 214 */       Type componentType = getComponentType(bound);
/* 215 */       if (componentType != null) {
/*     */ 
/*     */         
/* 218 */         if (componentType instanceof Class) {
/* 219 */           Class<?> componentClass = (Class)componentType;
/* 220 */           if (componentClass.isPrimitive()) {
/* 221 */             return componentClass;
/*     */           }
/*     */         } 
/* 224 */         return subtypeOf(componentType);
/*     */       } 
/*     */     } 
/* 227 */     return null;
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     GenericArrayTypeImpl(Type componentType) {
/* 235 */       this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(componentType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getGenericComponentType() {
/* 240 */       return this.componentType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 245 */       return Types.toString(this.componentType) + "[]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 250 */       return this.componentType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 255 */       if (obj instanceof GenericArrayType) {
/* 256 */         GenericArrayType that = (GenericArrayType)obj;
/* 257 */         return Objects.equal(getGenericComponentType(), that.getGenericComponentType());
/*     */       } 
/* 259 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ParameterizedTypeImpl
/*     */     implements ParameterizedType, Serializable
/*     */   {
/*     */     private final Type ownerType;
/*     */     private final ImmutableList<Type> argumentsList;
/*     */     private final Class<?> rawType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ParameterizedTypeImpl(@Nullable Type ownerType, Class<?> rawType, Type[] typeArguments) {
/* 272 */       Preconditions.checkNotNull(rawType);
/* 273 */       Preconditions.checkArgument((typeArguments.length == (rawType.getTypeParameters()).length));
/* 274 */       Types.disallowPrimitiveType(typeArguments, "type parameter");
/* 275 */       this.ownerType = ownerType;
/* 276 */       this.rawType = rawType;
/* 277 */       this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(typeArguments);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 282 */       return Types.toArray((Collection<Type>)this.argumentsList);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getRawType() {
/* 287 */       return this.rawType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getOwnerType() {
/* 292 */       return this.ownerType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 297 */       StringBuilder builder = new StringBuilder();
/* 298 */       if (this.ownerType != null) {
/* 299 */         builder.append(Types.JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
/*     */       }
/* 301 */       return builder
/* 302 */         .append(this.rawType.getName())
/* 303 */         .append('<')
/* 304 */         .append(Types.COMMA_JOINER.join(Iterables.transform((Iterable)this.argumentsList, Types.TYPE_NAME)))
/* 305 */         .append('>')
/* 306 */         .toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 311 */       return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList
/* 312 */         .hashCode() ^ this.rawType
/* 313 */         .hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 318 */       if (!(other instanceof ParameterizedType)) {
/* 319 */         return false;
/*     */       }
/* 321 */       ParameterizedType that = (ParameterizedType)other;
/* 322 */       return (getRawType().equals(that.getRawType()) && 
/* 323 */         Objects.equal(getOwnerType(), that.getOwnerType()) && 
/* 324 */         Arrays.equals((Object[])getActualTypeArguments(), (Object[])that.getActualTypeArguments()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 332 */     TypeVariableImpl<D> typeVariableImpl = new TypeVariableImpl<>(genericDeclaration, name, bounds);
/*     */ 
/*     */ 
/*     */     
/* 336 */     TypeVariable<D> typeVariable = Reflection.<TypeVariable<D>>newProxy((Class)TypeVariable.class, new TypeVariableInvocationHandler(typeVariableImpl));
/*     */     
/* 338 */     return typeVariable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TypeVariableInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private static final ImmutableMap<String, Method> typeVariableMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Types.TypeVariableImpl<?> typeVariableImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 365 */       ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
/* 366 */       for (Method method : Types.TypeVariableImpl.class.getMethods()) {
/* 367 */         if (method.getDeclaringClass().equals(Types.TypeVariableImpl.class)) {
/*     */           try {
/* 369 */             method.setAccessible(true);
/* 370 */           } catch (AccessControlException accessControlException) {}
/*     */ 
/*     */ 
/*     */           
/* 374 */           builder.put(method.getName(), method);
/*     */         } 
/*     */       } 
/* 377 */       typeVariableMethods = builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableInvocationHandler(Types.TypeVariableImpl<?> typeVariableImpl) {
/* 383 */       this.typeVariableImpl = typeVariableImpl;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 388 */       String methodName = method.getName();
/* 389 */       Method typeVariableMethod = (Method)typeVariableMethods.get(methodName);
/* 390 */       if (typeVariableMethod == null) {
/* 391 */         throw new UnsupportedOperationException(methodName);
/*     */       }
/*     */       try {
/* 394 */         return typeVariableMethod.invoke(this.typeVariableImpl, args);
/* 395 */       } catch (InvocationTargetException e) {
/* 396 */         throw e.getCause();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeVariableImpl<D extends GenericDeclaration>
/*     */   {
/*     */     private final D genericDeclaration;
/*     */     private final String name;
/*     */     private final ImmutableList<Type> bounds;
/*     */     
/*     */     TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 409 */       Types.disallowPrimitiveType(bounds, "bound for type variable");
/* 410 */       this.genericDeclaration = (D)Preconditions.checkNotNull(genericDeclaration);
/* 411 */       this.name = (String)Preconditions.checkNotNull(name);
/* 412 */       this.bounds = ImmutableList.copyOf((Object[])bounds);
/*     */     }
/*     */     
/*     */     public Type[] getBounds() {
/* 416 */       return Types.toArray((Collection<Type>)this.bounds);
/*     */     }
/*     */     
/*     */     public D getGenericDeclaration() {
/* 420 */       return this.genericDeclaration;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 424 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getTypeName() {
/* 428 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 433 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 438 */       return this.genericDeclaration.hashCode() ^ this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 443 */       if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
/*     */         
/* 445 */         if (obj != null && 
/* 446 */           Proxy.isProxyClass(obj.getClass()) && 
/* 447 */           Proxy.getInvocationHandler(obj) instanceof Types.TypeVariableInvocationHandler) {
/*     */           
/* 449 */           Types.TypeVariableInvocationHandler typeVariableInvocationHandler = (Types.TypeVariableInvocationHandler)Proxy.getInvocationHandler(obj);
/* 450 */           TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
/* 451 */           return (this.name.equals(that.getName()) && this.genericDeclaration
/* 452 */             .equals(that.getGenericDeclaration()) && this.bounds
/* 453 */             .equals(that.bounds));
/*     */         } 
/* 455 */         return false;
/*     */       } 
/*     */       
/* 458 */       if (obj instanceof TypeVariable) {
/* 459 */         TypeVariable<?> that = (TypeVariable)obj;
/* 460 */         return (this.name.equals(that.getName()) && this.genericDeclaration
/* 461 */           .equals(that.getGenericDeclaration()));
/*     */       } 
/* 463 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable {
/*     */     private final ImmutableList<Type> lowerBounds;
/*     */     private final ImmutableList<Type> upperBounds;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
/* 474 */       Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
/* 475 */       Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
/* 476 */       this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(lowerBounds);
/* 477 */       this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 482 */       return Types.toArray((Collection<Type>)this.lowerBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 487 */       return Types.toArray((Collection<Type>)this.upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 492 */       if (obj instanceof WildcardType) {
/* 493 */         WildcardType that = (WildcardType)obj;
/* 494 */         return (this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds
/* 495 */           .equals(Arrays.asList(that.getUpperBounds())));
/*     */       } 
/* 497 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 502 */       return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 507 */       StringBuilder builder = new StringBuilder("?");
/* 508 */       for (UnmodifiableIterator<Type> unmodifiableIterator = this.lowerBounds.iterator(); unmodifiableIterator.hasNext(); ) { Type lowerBound = unmodifiableIterator.next();
/* 509 */         builder.append(" super ").append(Types.JavaVersion.CURRENT.typeName(lowerBound)); }
/*     */       
/* 511 */       for (Type upperBound : Types.filterUpperBounds((Iterable<Type>)this.upperBounds)) {
/* 512 */         builder.append(" extends ").append(Types.JavaVersion.CURRENT.typeName(upperBound));
/*     */       }
/* 514 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type[] toArray(Collection<Type> types) {
/* 521 */     return types.<Type>toArray(new Type[types.size()]);
/*     */   }
/*     */   
/*     */   private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
/* 525 */     return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
/*     */   }
/*     */   
/*     */   private static void disallowPrimitiveType(Type[] types, String usedAs) {
/* 529 */     for (Type type : types) {
/* 530 */       if (type instanceof Class) {
/* 531 */         Class<?> cls = (Class)type;
/* 532 */         Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<?> getArrayClass(Class<?> componentType) {
/* 542 */     return Array.newInstance(componentType, 0).getClass();
/*     */   }
/*     */   
/*     */   enum JavaVersion
/*     */   {
/* 547 */     JAVA6
/*     */     {
/*     */       GenericArrayType newArrayType(Type componentType) {
/* 550 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 555 */         Preconditions.checkNotNull(type);
/* 556 */         if (type instanceof Class) {
/* 557 */           Class<?> cls = (Class)type;
/* 558 */           if (cls.isArray()) {
/* 559 */             return new Types.GenericArrayTypeImpl(cls.getComponentType());
/*     */           }
/*     */         } 
/* 562 */         return type;
/*     */       }
/*     */     },
/* 565 */     JAVA7
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 568 */         if (componentType instanceof Class) {
/* 569 */           return Types.getArrayClass((Class)componentType);
/*     */         }
/* 571 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 577 */         return (Type)Preconditions.checkNotNull(type);
/*     */       }
/*     */     },
/* 580 */     JAVA8
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 583 */         return JAVA7.newArrayType(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 588 */         return JAVA7.usedInGenericType(type);
/*     */       }
/*     */ 
/*     */       
/*     */       String typeName(Type type) {
/*     */         try {
/* 594 */           Method getTypeName = Type.class.getMethod("getTypeName", new Class[0]);
/* 595 */           return (String)getTypeName.invoke(type, new Object[0]);
/* 596 */         } catch (NoSuchMethodException e) {
/* 597 */           throw new AssertionError("Type.getTypeName should be available in Java 8");
/* 598 */         } catch (InvocationTargetException e) {
/* 599 */           throw new RuntimeException(e);
/* 600 */         } catch (IllegalAccessException e) {
/* 601 */           throw new RuntimeException(e);
/*     */         } 
/*     */       }
/*     */     };
/*     */     
/*     */     static final JavaVersion CURRENT;
/*     */     
/*     */     static {
/* 609 */       if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
/* 610 */         CURRENT = JAVA8;
/* 611 */       } else if ((new TypeCapture<int[]>() {  }).capture() instanceof Class) {
/* 612 */         CURRENT = JAVA7;
/*     */       } else {
/* 614 */         CURRENT = JAVA6;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String typeName(Type type) {
/* 623 */       return Types.toString(type);
/*     */     }
/*     */     
/*     */     final ImmutableList<Type> usedInGenericType(Type[] types) {
/* 627 */       ImmutableList.Builder<Type> builder = ImmutableList.builder();
/* 628 */       for (Type type : types) {
/* 629 */         builder.add(usedInGenericType(type));
/*     */       }
/* 631 */       return builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type newArrayType(Type param1Type);
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type usedInGenericType(Type param1Type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NativeTypeVariableEquals<X>
/*     */   {
/* 648 */     static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */