/*      */ package com.google.common.reflect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ForwardingSet;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.common.primitives.Primitives;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public abstract class TypeToken<T>
/*      */   extends TypeCapture<T>
/*      */   implements Serializable
/*      */ {
/*      */   private final Type runtimeType;
/*      */   private transient TypeResolver typeResolver;
/*      */   
/*      */   protected TypeToken() {
/*  117 */     this.runtimeType = capture();
/*  118 */     Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeToken(Class<?> declaringClass) {
/*  145 */     Type captured = capture();
/*  146 */     if (captured instanceof Class) {
/*  147 */       this.runtimeType = captured;
/*      */     } else {
/*  149 */       this.runtimeType = (of((Class)declaringClass).resolveType(captured)).runtimeType;
/*      */     } 
/*      */   }
/*      */   
/*      */   private TypeToken(Type type) {
/*  154 */     this.runtimeType = (Type)Preconditions.checkNotNull(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> TypeToken<T> of(Class<T> type) {
/*  159 */     return new SimpleTypeToken<>(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static TypeToken<?> of(Type type) {
/*  164 */     return new SimpleTypeToken(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Class<? super T> getRawType() {
/*  183 */     Class<?> rawType = (Class)getRawTypes().iterator().next();
/*      */     
/*  185 */     Class<? super T> result = (Class)rawType;
/*  186 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  191 */     return this.runtimeType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
/*  212 */     TypeResolver resolver = (new TypeResolver()).where(
/*  213 */         (Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
/*      */ 
/*      */     
/*  216 */     return new SimpleTypeToken<>(resolver.resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
/*  235 */     return where(typeParam, of(typeArg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> resolveType(Type type) {
/*  246 */     Preconditions.checkNotNull(type);
/*  247 */     TypeResolver resolver = this.typeResolver;
/*  248 */     if (resolver == null) {
/*  249 */       resolver = this.typeResolver = TypeResolver.accordingTo(this.runtimeType);
/*      */     }
/*  251 */     return of(resolver.resolveType(type));
/*      */   }
/*      */   
/*      */   private Type[] resolveInPlace(Type[] types) {
/*  255 */     for (int i = 0; i < types.length; i++) {
/*  256 */       types[i] = resolveType(types[i]).getType();
/*      */     }
/*  258 */     return types;
/*      */   }
/*      */   
/*      */   private TypeToken<?> resolveSupertype(Type type) {
/*  262 */     TypeToken<?> supertype = resolveType(type);
/*      */     
/*  264 */     supertype.typeResolver = this.typeResolver;
/*  265 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   final TypeToken<? super T> getGenericSuperclass() {
/*  282 */     if (this.runtimeType instanceof TypeVariable)
/*      */     {
/*  284 */       return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
/*      */     }
/*  286 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  288 */       return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
/*      */     }
/*  290 */     Type superclass = getRawType().getGenericSuperclass();
/*  291 */     if (superclass == null) {
/*  292 */       return null;
/*      */     }
/*      */     
/*  295 */     TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
/*  296 */     return superToken;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private TypeToken<? super T> boundAsSuperclass(Type bound) {
/*  301 */     TypeToken<?> token = of(bound);
/*  302 */     if (token.getRawType().isInterface()) {
/*  303 */       return null;
/*      */     }
/*      */     
/*  306 */     TypeToken<? super T> superclass = (TypeToken)token;
/*  307 */     return superclass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
/*  323 */     if (this.runtimeType instanceof TypeVariable) {
/*  324 */       return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  326 */     if (this.runtimeType instanceof WildcardType) {
/*  327 */       return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  329 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  330 */     for (Type interfaceType : getRawType().getGenericInterfaces()) {
/*      */ 
/*      */       
/*  333 */       TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
/*  334 */       builder.add(resolvedInterface);
/*      */     } 
/*  336 */     return builder.build();
/*      */   }
/*      */   
/*      */   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
/*  340 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  341 */     for (Type bound : bounds) {
/*      */       
/*  343 */       TypeToken<? super T> boundType = (TypeToken)of(bound);
/*  344 */       if (boundType.getRawType().isInterface()) {
/*  345 */         builder.add(boundType);
/*      */       }
/*      */     } 
/*  348 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeSet getTypes() {
/*  363 */     return new TypeSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
/*  372 */     Preconditions.checkArgument(
/*  373 */         someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", superclass, this);
/*      */ 
/*      */ 
/*      */     
/*  377 */     if (this.runtimeType instanceof TypeVariable) {
/*  378 */       return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  380 */     if (this.runtimeType instanceof WildcardType) {
/*  381 */       return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  383 */     if (superclass.isArray()) {
/*  384 */       return getArraySupertype(superclass);
/*      */     }
/*      */ 
/*      */     
/*  388 */     TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
/*  389 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
/*  398 */     Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
/*      */     
/*  400 */     if (this.runtimeType instanceof WildcardType) {
/*  401 */       return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
/*      */     }
/*      */     
/*  404 */     if (isArray()) {
/*  405 */       return getArraySubtype(subclass);
/*      */     }
/*      */     
/*  408 */     Preconditions.checkArgument(
/*  409 */         getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
/*  410 */     Type resolvedTypeArgs = resolveTypeArgsForSubclass(subclass);
/*      */     
/*  412 */     TypeToken<? extends T> subtype = (TypeToken)of(resolvedTypeArgs);
/*  413 */     return subtype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(TypeToken<?> type) {
/*  425 */     return type.isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(Type type) {
/*  437 */     return of(type).isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(TypeToken<?> type) {
/*  449 */     return isSubtypeOf(type.getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(Type supertype) {
/*  461 */     Preconditions.checkNotNull(supertype);
/*  462 */     if (supertype instanceof WildcardType)
/*      */     {
/*      */ 
/*      */       
/*  466 */       return any(((WildcardType)supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
/*      */     }
/*      */ 
/*      */     
/*  470 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  472 */       return any(((WildcardType)this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
/*      */     }
/*      */ 
/*      */     
/*  476 */     if (this.runtimeType instanceof TypeVariable) {
/*  477 */       return (this.runtimeType.equals(supertype) || 
/*  478 */         any(((TypeVariable)this.runtimeType).getBounds()).isSubtypeOf(supertype));
/*      */     }
/*  480 */     if (this.runtimeType instanceof GenericArrayType) {
/*  481 */       return of(supertype).isSupertypeOfArray((GenericArrayType)this.runtimeType);
/*      */     }
/*      */     
/*  484 */     if (supertype instanceof Class)
/*  485 */       return someRawTypeIsSubclassOf((Class)supertype); 
/*  486 */     if (supertype instanceof ParameterizedType)
/*  487 */       return isSubtypeOfParameterizedType((ParameterizedType)supertype); 
/*  488 */     if (supertype instanceof GenericArrayType) {
/*  489 */       return isSubtypeOfArrayType((GenericArrayType)supertype);
/*      */     }
/*  491 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isArray() {
/*  500 */     return (getComponentType() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrimitive() {
/*  509 */     return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> wrap() {
/*  519 */     if (isPrimitive()) {
/*      */       
/*  521 */       Class<T> type = (Class<T>)this.runtimeType;
/*  522 */       return of(Primitives.wrap(type));
/*      */     } 
/*  524 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isWrapper() {
/*  528 */     return Primitives.allWrapperTypes().contains(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> unwrap() {
/*  538 */     if (isWrapper()) {
/*      */       
/*  540 */       Class<T> type = (Class<T>)this.runtimeType;
/*  541 */       return of(Primitives.unwrap(type));
/*      */     } 
/*  543 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final TypeToken<?> getComponentType() {
/*  552 */     Type componentType = Types.getComponentType(this.runtimeType);
/*  553 */     if (componentType == null) {
/*  554 */       return null;
/*      */     }
/*  556 */     return of(componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, Object> method(Method method) {
/*  565 */     Preconditions.checkArgument(
/*  566 */         someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", method, this);
/*      */ 
/*      */ 
/*      */     
/*  570 */     return new Invokable.MethodInvokable<T>(method)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  573 */           return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  578 */           return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  583 */           return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  588 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  593 */           return getOwnerType() + "." + super.toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, T> constructor(Constructor<?> constructor) {
/*  604 */     Preconditions.checkArgument(
/*  605 */         (constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", constructor, 
/*      */ 
/*      */         
/*  608 */         getRawType());
/*  609 */     return new Invokable.ConstructorInvokable<T>(constructor)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  612 */           return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  617 */           return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  622 */           return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  627 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  632 */           return getOwnerType() + "(" + Joiner.on(", ").join((Object[])getGenericParameterTypes()) + ")";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class TypeSet
/*      */     extends ForwardingSet<TypeToken<? super T>>
/*      */     implements Serializable
/*      */   {
/*      */     private transient ImmutableSet<TypeToken<? super T>> types;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     public TypeSet interfaces() {
/*  649 */       return new TypeToken.InterfaceSet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeSet classes() {
/*  654 */       return new TypeToken.ClassSet();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  659 */       ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
/*  660 */       if (filteredTypes == null) {
/*      */ 
/*      */ 
/*      */         
/*  664 */         ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
/*  665 */         return 
/*      */ 
/*      */           
/*  668 */           (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  670 */       return (Set<TypeToken<? super T>>)filteredTypes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  679 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  680 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class InterfaceSet
/*      */     extends TypeSet
/*      */   {
/*      */     private final transient TypeToken<T>.TypeSet allTypes;
/*      */     private transient ImmutableSet<TypeToken<? super T>> interfaces;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     InterfaceSet(TypeToken<T>.TypeSet allTypes) {
/*  692 */       this.allTypes = allTypes;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  697 */       ImmutableSet<TypeToken<? super T>> result = this.interfaces;
/*  698 */       if (result == null) {
/*  699 */         return 
/*  700 */           (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet());
/*      */       }
/*  702 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  708 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  716 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  717 */       return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes)
/*  718 */         .filter(new Predicate<Class<?>>()
/*      */           {
/*      */             public boolean apply(Class<?> type)
/*      */             {
/*  722 */               return type.isInterface();
/*      */             }
/*  725 */           }).toSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  730 */       throw new UnsupportedOperationException("interfaces().classes() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  734 */       return TypeToken.this.getTypes().interfaces();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ClassSet
/*      */     extends TypeSet {
/*      */     private transient ImmutableSet<TypeToken<? super T>> classes;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private ClassSet() {}
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  746 */       ImmutableSet<TypeToken<? super T>> result = this.classes;
/*  747 */       if (result == null) {
/*      */ 
/*      */ 
/*      */         
/*  751 */         ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
/*  752 */         return 
/*      */ 
/*      */           
/*  755 */           (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  757 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  763 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  771 */       ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getRawTypes());
/*  772 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  777 */       throw new UnsupportedOperationException("classes().interfaces() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  781 */       return TypeToken.this.getTypes().classes();
/*      */     }
/*      */   }
/*      */   
/*      */   private enum TypeFilter
/*      */     implements Predicate<TypeToken<?>>
/*      */   {
/*  788 */     IGNORE_TYPE_VARIABLE_OR_WILDCARD
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  791 */         return (!(type.runtimeType instanceof TypeVariable) && 
/*  792 */           !(type.runtimeType instanceof WildcardType));
/*      */       }
/*      */     },
/*  795 */     INTERFACE_ONLY
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  798 */         return type.getRawType().isInterface();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@Nullable Object o) {
/*  808 */     if (o instanceof TypeToken) {
/*  809 */       TypeToken<?> that = (TypeToken)o;
/*  810 */       return this.runtimeType.equals(that.runtimeType);
/*      */     } 
/*  812 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  817 */     return this.runtimeType.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  822 */     return Types.toString(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  829 */     return of((new TypeResolver()).resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   final TypeToken<T> rejectTypeVariables() {
/*  838 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> type) {
/*  841 */           throw new IllegalArgumentException(TypeToken.this
/*  842 */               .runtimeType + "contains a type variable and is not safe for the operation");
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType type) {
/*  847 */           visit(type.getLowerBounds());
/*  848 */           visit(type.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType type) {
/*  853 */           visit(type.getActualTypeArguments());
/*  854 */           visit(new Type[] { type.getOwnerType() });
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType type) {
/*  859 */           visit(new Type[] { type.getGenericComponentType() });
/*      */         }
/*  861 */       }).visit(new Type[] { this.runtimeType });
/*  862 */     return this;
/*      */   }
/*      */   
/*      */   private boolean someRawTypeIsSubclassOf(Class<?> superclass) {
/*  866 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = getRawTypes().iterator(); unmodifiableIterator.hasNext(); ) { Class<?> rawType = unmodifiableIterator.next();
/*  867 */       if (superclass.isAssignableFrom(rawType)) {
/*  868 */         return true;
/*      */       } }
/*      */     
/*  871 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfParameterizedType(ParameterizedType supertype) {
/*  875 */     Class<?> matchedClass = of(supertype).getRawType();
/*  876 */     if (!someRawTypeIsSubclassOf(matchedClass)) {
/*  877 */       return false;
/*      */     }
/*  879 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
/*  880 */     Type[] toTypeArgs = supertype.getActualTypeArguments();
/*  881 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  889 */       if (!resolveType(arrayOfTypeVariable[i]).is(toTypeArgs[i])) {
/*  890 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  896 */     return (Modifier.isStatic(((Class)supertype.getRawType()).getModifiers()) || supertype
/*  897 */       .getOwnerType() == null || 
/*  898 */       isOwnedBySubtypeOf(supertype.getOwnerType()));
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfArrayType(GenericArrayType supertype) {
/*  902 */     if (this.runtimeType instanceof Class) {
/*  903 */       Class<?> fromClass = (Class)this.runtimeType;
/*  904 */       if (!fromClass.isArray()) {
/*  905 */         return false;
/*      */       }
/*  907 */       return of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
/*  908 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  909 */       GenericArrayType fromArrayType = (GenericArrayType)this.runtimeType;
/*  910 */       return of(fromArrayType.getGenericComponentType())
/*  911 */         .isSubtypeOf(supertype.getGenericComponentType());
/*      */     } 
/*  913 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isSupertypeOfArray(GenericArrayType subtype) {
/*  918 */     if (this.runtimeType instanceof Class) {
/*  919 */       Class<?> thisClass = (Class)this.runtimeType;
/*  920 */       if (!thisClass.isArray()) {
/*  921 */         return thisClass.isAssignableFrom(Object[].class);
/*      */       }
/*  923 */       return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
/*  924 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  925 */       return of(subtype.getGenericComponentType())
/*  926 */         .isSubtypeOf(((GenericArrayType)this.runtimeType).getGenericComponentType());
/*      */     }
/*  928 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean is(Type formalType) {
/*  942 */     if (this.runtimeType.equals(formalType)) {
/*  943 */       return true;
/*      */     }
/*  945 */     if (formalType instanceof WildcardType)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  951 */       return (every(((WildcardType)formalType).getUpperBounds()).isSupertypeOf(this.runtimeType) && 
/*  952 */         every(((WildcardType)formalType).getLowerBounds()).isSubtypeOf(this.runtimeType));
/*      */     }
/*  954 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds every(Type[] bounds) {
/*  959 */     return new Bounds(bounds, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds any(Type[] bounds) {
/*  964 */     return new Bounds(bounds, true);
/*      */   }
/*      */   
/*      */   private static class Bounds {
/*      */     private final Type[] bounds;
/*      */     private final boolean target;
/*      */     
/*      */     Bounds(Type[] bounds, boolean target) {
/*  972 */       this.bounds = bounds;
/*  973 */       this.target = target;
/*      */     }
/*      */     
/*      */     boolean isSubtypeOf(Type supertype) {
/*  977 */       for (Type bound : this.bounds) {
/*  978 */         if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
/*  979 */           return this.target;
/*      */         }
/*      */       } 
/*  982 */       return !this.target;
/*      */     }
/*      */     
/*      */     boolean isSupertypeOf(Type subtype) {
/*  986 */       TypeToken<?> type = TypeToken.of(subtype);
/*  987 */       for (Type bound : this.bounds) {
/*  988 */         if (type.isSubtypeOf(bound) == this.target) {
/*  989 */           return this.target;
/*      */         }
/*      */       } 
/*  992 */       return !this.target;
/*      */     }
/*      */   }
/*      */   
/*      */   private ImmutableSet<Class<? super T>> getRawTypes() {
/*  997 */     final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
/*  998 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> t) {
/* 1001 */           visit(t.getBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType t) {
/* 1006 */           visit(t.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType t) {
/* 1011 */           builder.add(t.getRawType());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitClass(Class<?> t) {
/* 1016 */           builder.add(t);
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType t) {
/* 1021 */           builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
/*      */         }
/* 1023 */       }).visit(new Type[] { this.runtimeType });
/*      */ 
/*      */     
/* 1026 */     ImmutableSet<Class<? super T>> result = builder.build();
/* 1027 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isOwnedBySubtypeOf(Type supertype) {
/* 1031 */     for (TypeToken<?> type : (Iterable<TypeToken<?>>)getTypes()) {
/* 1032 */       Type ownerType = type.getOwnerTypeIfPresent();
/* 1033 */       if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
/* 1034 */         return true;
/*      */       }
/*      */     } 
/* 1037 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Type getOwnerTypeIfPresent() {
/* 1045 */     if (this.runtimeType instanceof ParameterizedType)
/* 1046 */       return ((ParameterizedType)this.runtimeType).getOwnerType(); 
/* 1047 */     if (this.runtimeType instanceof Class) {
/* 1048 */       return ((Class)this.runtimeType).getEnclosingClass();
/*      */     }
/* 1050 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
/* 1063 */     if (cls.isArray()) {
/*      */       
/* 1065 */       Type arrayOfGenericType = Types.newArrayType(
/*      */           
/* 1067 */           (toGenericType((Class)cls.getComponentType())).runtimeType);
/*      */       
/* 1069 */       TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
/* 1070 */       return result;
/*      */     } 
/* 1072 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */     
/* 1075 */     Type ownerType = (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())) ? (toGenericType((Class)cls.getEnclosingClass())).runtimeType : null;
/*      */ 
/*      */     
/* 1078 */     if (arrayOfTypeVariable.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
/*      */ 
/*      */ 
/*      */       
/* 1082 */       TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedTypeWithOwner(ownerType, cls, (Type[])arrayOfTypeVariable));
/* 1083 */       return type;
/*      */     } 
/* 1085 */     return of(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
/* 1091 */     for (Type upperBound : upperBounds) {
/*      */       
/* 1093 */       TypeToken<? super T> bound = (TypeToken)of(upperBound);
/* 1094 */       if (bound.isSubtypeOf(supertype)) {
/*      */         
/* 1096 */         TypeToken<? super T> result = bound.getSupertype(supertype);
/* 1097 */         return result;
/*      */       } 
/*      */     } 
/* 1100 */     throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
/* 1104 */     Type[] arrayOfType = lowerBounds; int i = arrayOfType.length; byte b = 0; if (b < i) { Type lowerBound = arrayOfType[b];
/*      */       
/* 1106 */       TypeToken<? extends T> bound = (TypeToken)of(lowerBound);
/*      */       
/* 1108 */       return bound.getSubtype(subclass); }
/*      */     
/* 1110 */     throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
/* 1118 */     TypeToken<?> componentType = (TypeToken)Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", supertype, this);
/*      */ 
/*      */     
/* 1121 */     TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1126 */     TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
/* 1127 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
/* 1132 */     TypeToken<?> componentSubtype = getComponentType().getSubtype(subclass.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1137 */     TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
/* 1138 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type resolveTypeArgsForSubclass(Class<?> subclass) {
/* 1146 */     if (this.runtimeType instanceof Class && ((subclass
/* 1147 */       .getTypeParameters()).length == 0 || (
/* 1148 */       getRawType().getTypeParameters()).length != 0))
/*      */     {
/* 1150 */       return subclass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1159 */     TypeToken<?> genericSubtype = toGenericType(subclass);
/*      */ 
/*      */     
/* 1162 */     Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
/* 1163 */     return (new TypeResolver())
/* 1164 */       .where(supertypeWithArgsFromSubtype, this.runtimeType)
/* 1165 */       .resolveType(genericSubtype.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type newArrayClassOrGenericArrayType(Type componentType) {
/* 1173 */     return Types.JavaVersion.JAVA7.newArrayType(componentType);
/*      */   }
/*      */   
/*      */   private static final class SimpleTypeToken<T> extends TypeToken<T> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SimpleTypeToken(Type type) {
/* 1179 */       super(type);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class TypeCollector<K>
/*      */   {
/*      */     private TypeCollector() {}
/*      */ 
/*      */ 
/*      */     
/* 1192 */     static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>()
/*      */       {
/*      */         Class<?> getRawType(TypeToken<?> type)
/*      */         {
/* 1196 */           return type.getRawType();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
/* 1201 */           return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
/*      */         }
/*      */ 
/*      */         
/*      */         @Nullable
/*      */         TypeToken<?> getSuperclass(TypeToken<?> type) {
/* 1207 */           return type.getGenericSuperclass();
/*      */         }
/*      */       };
/*      */     
/* 1211 */     static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>()
/*      */       {
/*      */         Class<?> getRawType(Class<?> type)
/*      */         {
/* 1215 */           return type;
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
/* 1220 */           return Arrays.asList(type.getInterfaces());
/*      */         }
/*      */ 
/*      */         
/*      */         @Nullable
/*      */         Class<?> getSuperclass(Class<?> type) {
/* 1226 */           return type.getSuperclass();
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*      */     final TypeCollector<K> classesOnly() {
/* 1232 */       return new ForwardingTypeCollector<K>(this)
/*      */         {
/*      */           Iterable<? extends K> getInterfaces(K type) {
/* 1235 */             return (Iterable<? extends K>)ImmutableSet.of();
/*      */           }
/*      */ 
/*      */           
/*      */           ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1240 */             ImmutableList.Builder<K> builder = ImmutableList.builder();
/* 1241 */             for (K type : types) {
/* 1242 */               if (!getRawType(type).isInterface()) {
/* 1243 */                 builder.add(type);
/*      */               }
/*      */             } 
/* 1246 */             return super.collectTypes((Iterable<? extends K>)builder.build());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     final ImmutableList<K> collectTypes(K type) {
/* 1252 */       return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1257 */       Map<K, Integer> map = Maps.newHashMap();
/* 1258 */       for (K type : types) {
/* 1259 */         collectTypes(type, map);
/*      */       }
/* 1261 */       return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     private int collectTypes(K type, Map<? super K, Integer> map) {
/* 1267 */       Integer existing = map.get(type);
/* 1268 */       if (existing != null)
/*      */       {
/* 1270 */         return existing.intValue();
/*      */       }
/*      */       
/* 1273 */       int aboveMe = getRawType(type).isInterface() ? 1 : 0;
/* 1274 */       for (K interfaceType : getInterfaces(type)) {
/* 1275 */         aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
/*      */       }
/* 1277 */       K superclass = getSuperclass(type);
/* 1278 */       if (superclass != null) {
/* 1279 */         aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1286 */       map.put(type, Integer.valueOf(aboveMe + 1));
/* 1287 */       return aboveMe + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
/* 1292 */       Ordering<K> keyOrdering = new Ordering<K>()
/*      */         {
/*      */           public int compare(K left, K right)
/*      */           {
/* 1296 */             return valueComparator.compare(map.get(left), map.get(right));
/*      */           }
/*      */         };
/* 1299 */       return keyOrdering.immutableSortedCopy(map.keySet());
/*      */     }
/*      */     
/*      */     abstract Class<?> getRawType(K param1K);
/*      */     
/*      */     abstract Iterable<? extends K> getInterfaces(K param1K);
/*      */     
/*      */     @Nullable
/*      */     abstract K getSuperclass(K param1K);
/*      */     
/*      */     private static class ForwardingTypeCollector<K>
/*      */       extends TypeCollector<K> {
/*      */       private final TypeToken.TypeCollector<K> delegate;
/*      */       
/*      */       ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
/* 1314 */         this.delegate = delegate;
/*      */       }
/*      */ 
/*      */       
/*      */       Class<?> getRawType(K type) {
/* 1319 */         return this.delegate.getRawType(type);
/*      */       }
/*      */ 
/*      */       
/*      */       Iterable<? extends K> getInterfaces(K type) {
/* 1324 */         return this.delegate.getInterfaces(type);
/*      */       }
/*      */ 
/*      */       
/*      */       K getSuperclass(K type) {
/* 1329 */         return this.delegate.getSuperclass(type);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\TypeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */