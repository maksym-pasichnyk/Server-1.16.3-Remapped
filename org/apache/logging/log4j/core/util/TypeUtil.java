/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeUtil
/*     */ {
/*     */   public static List<Field> getAllDeclaredFields(Class<?> cls) {
/*  54 */     List<Field> fields = new ArrayList<>();
/*  55 */     while (cls != null) {
/*  56 */       for (Field field : cls.getDeclaredFields()) {
/*  57 */         fields.add(field);
/*     */       }
/*  59 */       cls = cls.getSuperclass();
/*     */     } 
/*  61 */     return fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAssignable(Type lhs, Type rhs) {
/*  72 */     Objects.requireNonNull(lhs, "No left hand side type provided");
/*  73 */     Objects.requireNonNull(rhs, "No right hand side type provided");
/*  74 */     if (lhs.equals(rhs)) {
/*  75 */       return true;
/*     */     }
/*  77 */     if (Object.class.equals(lhs))
/*     */     {
/*  79 */       return true;
/*     */     }
/*     */     
/*  82 */     if (lhs instanceof Class) {
/*  83 */       Class<?> lhsClass = (Class)lhs;
/*  84 */       if (rhs instanceof Class) {
/*     */         
/*  86 */         Class<?> rhsClass = (Class)rhs;
/*  87 */         return lhsClass.isAssignableFrom(rhsClass);
/*     */       } 
/*  89 */       if (rhs instanceof ParameterizedType) {
/*     */         
/*  91 */         Type rhsRawType = ((ParameterizedType)rhs).getRawType();
/*  92 */         if (rhsRawType instanceof Class) {
/*  93 */           return lhsClass.isAssignableFrom((Class)rhsRawType);
/*     */         }
/*     */       } 
/*  96 */       if (lhsClass.isArray() && rhs instanceof GenericArrayType)
/*     */       {
/*  98 */         return isAssignable(lhsClass.getComponentType(), ((GenericArrayType)rhs).getGenericComponentType());
/*     */       }
/*     */     } 
/*     */     
/* 102 */     if (lhs instanceof ParameterizedType) {
/* 103 */       ParameterizedType lhsType = (ParameterizedType)lhs;
/* 104 */       if (rhs instanceof Class) {
/* 105 */         Type lhsRawType = lhsType.getRawType();
/* 106 */         if (lhsRawType instanceof Class) {
/* 107 */           return ((Class)lhsRawType).isAssignableFrom((Class)rhs);
/*     */         }
/* 109 */       } else if (rhs instanceof ParameterizedType) {
/* 110 */         ParameterizedType rhsType = (ParameterizedType)rhs;
/* 111 */         return isParameterizedAssignable(lhsType, rhsType);
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     if (lhs instanceof GenericArrayType) {
/* 116 */       Type lhsComponentType = ((GenericArrayType)lhs).getGenericComponentType();
/* 117 */       if (rhs instanceof Class) {
/*     */         
/* 119 */         Class<?> rhsClass = (Class)rhs;
/* 120 */         if (rhsClass.isArray()) {
/* 121 */           return isAssignable(lhsComponentType, rhsClass.getComponentType());
/*     */         }
/* 123 */       } else if (rhs instanceof GenericArrayType) {
/* 124 */         return isAssignable(lhsComponentType, ((GenericArrayType)rhs).getGenericComponentType());
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     if (lhs instanceof WildcardType) {
/* 129 */       return isWildcardAssignable((WildcardType)lhs, rhs);
/*     */     }
/*     */     
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isParameterizedAssignable(ParameterizedType lhs, ParameterizedType rhs) {
/* 136 */     if (lhs.equals(rhs))
/*     */     {
/* 138 */       return true;
/*     */     }
/* 140 */     Type[] lhsTypeArguments = lhs.getActualTypeArguments();
/* 141 */     Type[] rhsTypeArguments = rhs.getActualTypeArguments();
/* 142 */     int size = lhsTypeArguments.length;
/* 143 */     if (rhsTypeArguments.length != size)
/*     */     {
/* 145 */       return false;
/*     */     }
/* 147 */     for (int i = 0; i < size; i++) {
/*     */       
/* 149 */       Type lhsArgument = lhsTypeArguments[i];
/* 150 */       Type rhsArgument = rhsTypeArguments[i];
/* 151 */       if (!lhsArgument.equals(rhsArgument) && (!(lhsArgument instanceof WildcardType) || !isWildcardAssignable((WildcardType)lhsArgument, rhsArgument)))
/*     */       {
/*     */         
/* 154 */         return false;
/*     */       }
/*     */     } 
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isWildcardAssignable(WildcardType lhs, Type rhs) {
/* 161 */     Type[] lhsUpperBounds = getEffectiveUpperBounds(lhs);
/* 162 */     Type[] lhsLowerBounds = getEffectiveLowerBounds(lhs);
/* 163 */     if (rhs instanceof WildcardType) {
/*     */       
/* 165 */       WildcardType rhsType = (WildcardType)rhs;
/* 166 */       Type[] rhsUpperBounds = getEffectiveUpperBounds(rhsType);
/* 167 */       Type[] rhsLowerBounds = getEffectiveLowerBounds(rhsType);
/* 168 */       for (Type lhsUpperBound : lhsUpperBounds) {
/* 169 */         for (Type rhsUpperBound : rhsUpperBounds) {
/* 170 */           if (!isBoundAssignable(lhsUpperBound, rhsUpperBound)) {
/* 171 */             return false;
/*     */           }
/*     */         } 
/* 174 */         for (Type rhsLowerBound : rhsLowerBounds) {
/* 175 */           if (!isBoundAssignable(lhsUpperBound, rhsLowerBound)) {
/* 176 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 180 */       for (Type lhsLowerBound : lhsLowerBounds) {
/* 181 */         for (Type rhsUpperBound : rhsUpperBounds) {
/* 182 */           if (!isBoundAssignable(rhsUpperBound, lhsLowerBound)) {
/* 183 */             return false;
/*     */           }
/*     */         } 
/* 186 */         for (Type rhsLowerBound : rhsLowerBounds) {
/* 187 */           if (!isBoundAssignable(rhsLowerBound, lhsLowerBound)) {
/* 188 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 194 */       for (Type lhsUpperBound : lhsUpperBounds) {
/* 195 */         if (!isBoundAssignable(lhsUpperBound, rhs)) {
/* 196 */           return false;
/*     */         }
/*     */       } 
/* 199 */       for (Type lhsLowerBound : lhsLowerBounds) {
/* 200 */         if (!isBoundAssignable(lhsLowerBound, rhs)) {
/* 201 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   private static Type[] getEffectiveUpperBounds(WildcardType type) {
/* 209 */     Type[] upperBounds = type.getUpperBounds();
/* 210 */     (new Type[1])[0] = Object.class; return (upperBounds.length == 0) ? new Type[1] : upperBounds;
/*     */   }
/*     */   
/*     */   private static Type[] getEffectiveLowerBounds(WildcardType type) {
/* 214 */     Type[] lowerBounds = type.getLowerBounds();
/* 215 */     (new Type[1])[0] = null; return (lowerBounds.length == 0) ? new Type[1] : lowerBounds;
/*     */   }
/*     */   
/*     */   private static boolean isBoundAssignable(Type lhs, Type rhs) {
/* 219 */     return (rhs == null || (lhs != null && isAssignable(lhs, rhs)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\TypeUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */