/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @Beta
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*     */   public TypeResolver() {
/*  56 */     this.typeTable = new TypeTable();
/*     */   }
/*     */   
/*     */   private TypeResolver(TypeTable typeTable) {
/*  60 */     this.typeTable = typeTable;
/*     */   }
/*     */   
/*     */   static TypeResolver accordingTo(Type type) {
/*  64 */     return (new TypeResolver()).where((Map<TypeVariableKey, ? extends Type>)TypeMappingIntrospector.getTypeMappings(type));
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
/*     */   public TypeResolver where(Type formal, Type actual) {
/*  87 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/*  88 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/*  89 */     return where(mappings);
/*     */   }
/*     */ 
/*     */   
/*     */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
/*  94 */     return new TypeResolver(this.typeTable.where(mappings));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
/*  99 */     if (from.equals(to)) {
/*     */       return;
/*     */     }
/* 102 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> typeVariable) {
/* 105 */           mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType fromWildcardType) {
/* 110 */           if (!(to instanceof WildcardType)) {
/*     */             return;
/*     */           }
/* 113 */           WildcardType toWildcardType = (WildcardType)to;
/* 114 */           Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 115 */           Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 116 */           Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 117 */           Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 118 */           Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", fromWildcardType, to);
/*     */ 
/*     */           
/*     */           int i;
/*     */ 
/*     */           
/* 124 */           for (i = 0; i < fromUpperBounds.length; i++) {
/* 125 */             TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */           }
/* 127 */           for (i = 0; i < fromLowerBounds.length; i++) {
/* 128 */             TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitParameterizedType(ParameterizedType fromParameterizedType) {
/* 134 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 137 */           ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument((Class)ParameterizedType.class, to);
/* 138 */           if (fromParameterizedType.getOwnerType() != null && toParameterizedType
/* 139 */             .getOwnerType() != null) {
/* 140 */             TypeResolver.populateTypeMappings(mappings, fromParameterizedType
/* 141 */                 .getOwnerType(), toParameterizedType.getOwnerType());
/*     */           }
/* 143 */           Preconditions.checkArgument(fromParameterizedType
/* 144 */               .getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
/*     */ 
/*     */ 
/*     */           
/* 148 */           Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 149 */           Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 150 */           Preconditions.checkArgument((fromArgs.length == toArgs.length), "%s not compatible with %s", fromParameterizedType, toParameterizedType);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 155 */           for (int i = 0; i < fromArgs.length; i++) {
/* 156 */             TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType fromArrayType) {
/* 162 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 165 */           Type componentType = Types.getComponentType(to);
/* 166 */           Preconditions.checkArgument((componentType != null), "%s is not an array type.", to);
/* 167 */           TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> fromClass) {
/* 172 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 178 */           throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to);
/*     */         }
/* 180 */       }).visit(new Type[] { from });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type resolveType(Type type) {
/* 188 */     Preconditions.checkNotNull(type);
/* 189 */     if (type instanceof TypeVariable)
/* 190 */       return this.typeTable.resolve((TypeVariable)type); 
/* 191 */     if (type instanceof ParameterizedType)
/* 192 */       return resolveParameterizedType((ParameterizedType)type); 
/* 193 */     if (type instanceof GenericArrayType)
/* 194 */       return resolveGenericArrayType((GenericArrayType)type); 
/* 195 */     if (type instanceof WildcardType) {
/* 196 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 199 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   private Type[] resolveTypes(Type[] types) {
/* 204 */     Type[] result = new Type[types.length];
/* 205 */     for (int i = 0; i < types.length; i++) {
/* 206 */       result[i] = resolveType(types[i]);
/*     */     }
/* 208 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 212 */     Type[] lowerBounds = type.getLowerBounds();
/* 213 */     Type[] upperBounds = type.getUpperBounds();
/* 214 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 218 */     Type componentType = type.getGenericComponentType();
/* 219 */     Type resolvedComponentType = resolveType(componentType);
/* 220 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 224 */     Type owner = type.getOwnerType();
/* 225 */     Type resolvedOwner = (owner == null) ? null : resolveType(owner);
/* 226 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 228 */     Type[] args = type.getActualTypeArguments();
/* 229 */     Type[] resolvedArgs = resolveTypes(args);
/* 230 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg) {
/*     */     try {
/* 236 */       return type.cast(arg);
/* 237 */     } catch (ClassCastException e) {
/* 238 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/*     */     TypeTable() {
/* 247 */       this.map = ImmutableMap.of();
/*     */     }
/*     */     
/*     */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
/* 251 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
/* 256 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 257 */       builder.putAll((Map)this.map);
/* 258 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 259 */         TypeResolver.TypeVariableKey variable = mapping.getKey();
/* 260 */         Type type = mapping.getValue();
/* 261 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
/* 262 */         builder.put(variable, type);
/*     */       } 
/* 264 */       return new TypeTable(builder.build());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 268 */       final TypeTable unguarded = this;
/* 269 */       TypeTable guarded = new TypeTable()
/*     */         {
/*     */           public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent)
/*     */           {
/* 273 */             if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 274 */               return intermediateVar;
/*     */             }
/* 276 */             return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */           }
/*     */         };
/* 279 */       return resolveInternal(var, guarded);
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
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
/* 291 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 292 */       if (type == null) {
/* 293 */         Type[] bounds = var.getBounds();
/* 294 */         if (bounds.length == 0) {
/* 295 */           return var;
/*     */         }
/* 297 */         Type[] resolvedBounds = (new TypeResolver(forDependants)).resolveTypes(bounds);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 326 */         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && 
/* 327 */           Arrays.equals((Object[])bounds, (Object[])resolvedBounds)) {
/* 328 */           return var;
/*     */         }
/* 330 */         return Types.newArtificialTypeVariable((GenericDeclaration)var
/* 331 */             .getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       } 
/*     */       
/* 334 */       return (new TypeResolver(forDependants)).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector
/*     */     extends TypeVisitor {
/* 340 */     private static final TypeResolver.WildcardCapturer wildcardCapturer = new TypeResolver.WildcardCapturer();
/*     */     
/* 342 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType) {
/* 349 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 350 */       introspector.visit(new Type[] { wildcardCapturer.capture(contextType) });
/* 351 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */ 
/*     */     
/*     */     void visitClass(Class<?> clazz) {
/* 356 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 357 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType) {
/* 362 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 363 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawClass.getTypeParameters();
/* 364 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 365 */       Preconditions.checkState((arrayOfTypeVariable.length == typeArgs.length));
/* 366 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 367 */         map(new TypeResolver.TypeVariableKey(arrayOfTypeVariable[i]), typeArgs[i]);
/*     */       }
/* 369 */       visit(new Type[] { rawClass });
/* 370 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */ 
/*     */     
/*     */     void visitTypeVariable(TypeVariable<?> t) {
/* 375 */       visit(t.getBounds());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitWildcardType(WildcardType t) {
/* 380 */       visit(t.getUpperBounds());
/*     */     }
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 384 */       if (this.mappings.containsKey(var)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 393 */       for (Type t = arg; t != null; t = this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 394 */         if (var.equalsType(t)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 399 */           for (Type x = arg; x != null; x = this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x)));
/*     */           return;
/*     */         } 
/*     */       } 
/* 403 */       this.mappings.put(var, arg);
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
/*     */   private static final class WildcardCapturer
/*     */   {
/* 416 */     private final AtomicInteger id = new AtomicInteger();
/*     */     
/*     */     Type capture(Type type) {
/* 419 */       Preconditions.checkNotNull(type);
/* 420 */       if (type instanceof Class) {
/* 421 */         return type;
/*     */       }
/* 423 */       if (type instanceof TypeVariable) {
/* 424 */         return type;
/*     */       }
/* 426 */       if (type instanceof GenericArrayType) {
/* 427 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 428 */         return Types.newArrayType(capture(arrayType.getGenericComponentType()));
/*     */       } 
/* 430 */       if (type instanceof ParameterizedType) {
/* 431 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 432 */         return Types.newParameterizedTypeWithOwner(
/* 433 */             captureNullable(parameterizedType.getOwnerType()), (Class)parameterizedType
/* 434 */             .getRawType(), 
/* 435 */             capture(parameterizedType.getActualTypeArguments()));
/*     */       } 
/* 437 */       if (type instanceof WildcardType) {
/* 438 */         WildcardType wildcardType = (WildcardType)type;
/* 439 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 440 */         if (lowerBounds.length == 0) {
/* 441 */           Type[] upperBounds = wildcardType.getUpperBounds();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 446 */           String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join((Object[])upperBounds);
/* 447 */           return Types.newArtificialTypeVariable(WildcardCapturer.class, name, wildcardType
/* 448 */               .getUpperBounds());
/*     */         } 
/*     */         
/* 451 */         return type;
/*     */       } 
/*     */       
/* 454 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */     
/*     */     private Type captureNullable(@Nullable Type type) {
/* 458 */       if (type == null) {
/* 459 */         return null;
/*     */       }
/* 461 */       return capture(type);
/*     */     }
/*     */     
/*     */     private Type[] capture(Type[] types) {
/* 465 */       Type[] result = new Type[types.length];
/* 466 */       for (int i = 0; i < types.length; i++) {
/* 467 */         result[i] = capture(types[i]);
/*     */       }
/* 469 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private WildcardCapturer() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableKey(TypeVariable<?> var) {
/* 490 */       this.var = (TypeVariable)Preconditions.checkNotNull(var);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 495 */       return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 500 */       if (obj instanceof TypeVariableKey) {
/* 501 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 502 */         return equalsTypeVariable(that.var);
/*     */       } 
/* 504 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 510 */       return this.var.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     static TypeVariableKey forLookup(Type t) {
/* 515 */       if (t instanceof TypeVariable) {
/* 516 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 518 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsType(Type type) {
/* 527 */       if (type instanceof TypeVariable) {
/* 528 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 530 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean equalsTypeVariable(TypeVariable<?> that) {
/* 535 */       return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var
/* 536 */         .getName().equals(that.getName()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\TypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */