/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class MemberUtils
/*     */ {
/*     */   private static final int ACCESS_TEST = 7;
/*  39 */   private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[] { byte.class, short.class, char.class, int.class, long.class, float.class, double.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean setAccessibleWorkaround(AccessibleObject o) {
/*  56 */     if (o == null || o.isAccessible()) {
/*  57 */       return false;
/*     */     }
/*  59 */     Member m = (Member)o;
/*  60 */     if (!o.isAccessible() && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
/*     */       try {
/*  62 */         o.setAccessible(true);
/*  63 */         return true;
/*  64 */       } catch (SecurityException securityException) {}
/*     */     }
/*     */ 
/*     */     
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPackageAccess(int modifiers) {
/*  77 */     return ((modifiers & 0x7) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAccessible(Member m) {
/*  86 */     return (m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic());
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
/*     */   static int compareConstructorFit(Constructor<?> left, Constructor<?> right, Class<?>[] actual) {
/* 103 */     return compareParameterTypes(Executable.of(left), Executable.of(right), actual);
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
/*     */   static int compareMethodFit(Method left, Method right, Class<?>[] actual) {
/* 120 */     return compareParameterTypes(Executable.of(left), Executable.of(right), actual);
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
/*     */   private static int compareParameterTypes(Executable left, Executable right, Class<?>[] actual) {
/* 136 */     float leftCost = getTotalTransformationCost(actual, left);
/* 137 */     float rightCost = getTotalTransformationCost(actual, right);
/* 138 */     return (leftCost < rightCost) ? -1 : ((rightCost < leftCost) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getTotalTransformationCost(Class<?>[] srcArgs, Executable executable) {
/* 149 */     Class<?>[] destArgs = executable.getParameterTypes();
/* 150 */     boolean isVarArgs = executable.isVarArgs();
/*     */ 
/*     */     
/* 153 */     float totalCost = 0.0F;
/* 154 */     long normalArgsLen = isVarArgs ? (destArgs.length - 1) : destArgs.length;
/* 155 */     if (srcArgs.length < normalArgsLen) {
/* 156 */       return Float.MAX_VALUE;
/*     */     }
/* 158 */     for (int i = 0; i < normalArgsLen; i++) {
/* 159 */       totalCost += getObjectTransformationCost(srcArgs[i], destArgs[i]);
/*     */     }
/* 161 */     if (isVarArgs) {
/*     */ 
/*     */       
/* 164 */       boolean noVarArgsPassed = (srcArgs.length < destArgs.length);
/* 165 */       boolean explicitArrayForVarags = (srcArgs.length == destArgs.length && srcArgs[srcArgs.length - 1].isArray());
/*     */       
/* 167 */       float varArgsCost = 0.001F;
/* 168 */       Class<?> destClass = destArgs[destArgs.length - 1].getComponentType();
/* 169 */       if (noVarArgsPassed) {
/*     */         
/* 171 */         totalCost += getObjectTransformationCost(destClass, Object.class) + 0.001F;
/*     */       }
/* 173 */       else if (explicitArrayForVarags) {
/* 174 */         Class<?> sourceClass = srcArgs[srcArgs.length - 1].getComponentType();
/* 175 */         totalCost += getObjectTransformationCost(sourceClass, destClass) + 0.001F;
/*     */       }
/*     */       else {
/*     */         
/* 179 */         for (int j = destArgs.length - 1; j < srcArgs.length; j++) {
/* 180 */           Class<?> srcClass = srcArgs[j];
/* 181 */           totalCost += getObjectTransformationCost(srcClass, destClass) + 0.001F;
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     return totalCost;
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
/*     */   private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
/* 197 */     if (destClass.isPrimitive()) {
/* 198 */       return getPrimitivePromotionCost(srcClass, destClass);
/*     */     }
/* 200 */     float cost = 0.0F;
/* 201 */     while (srcClass != null && !destClass.equals(srcClass)) {
/* 202 */       if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 208 */         cost += 0.25F;
/*     */         break;
/*     */       } 
/* 211 */       cost++;
/* 212 */       srcClass = srcClass.getSuperclass();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     if (srcClass == null) {
/* 219 */       cost += 1.5F;
/*     */     }
/* 221 */     return cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
/* 232 */     float cost = 0.0F;
/* 233 */     Class<?> cls = srcClass;
/* 234 */     if (!cls.isPrimitive()) {
/*     */       
/* 236 */       cost += 0.1F;
/* 237 */       cls = ClassUtils.wrapperToPrimitive(cls);
/*     */     } 
/* 239 */     for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
/* 240 */       if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
/* 241 */         cost += 0.1F;
/* 242 */         if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
/* 243 */           cls = ORDERED_PRIMITIVE_TYPES[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 247 */     return cost;
/*     */   }
/*     */   
/*     */   static boolean isMatchingMethod(Method method, Class<?>[] parameterTypes) {
/* 251 */     return isMatchingExecutable(Executable.of(method), parameterTypes);
/*     */   }
/*     */   
/*     */   static boolean isMatchingConstructor(Constructor<?> method, Class<?>[] parameterTypes) {
/* 255 */     return isMatchingExecutable(Executable.of(method), parameterTypes);
/*     */   }
/*     */   
/*     */   private static boolean isMatchingExecutable(Executable method, Class<?>[] parameterTypes) {
/* 259 */     Class<?>[] methodParameterTypes = method.getParameterTypes();
/* 260 */     if (method.isVarArgs()) {
/*     */       int i;
/* 262 */       for (i = 0; i < methodParameterTypes.length - 1 && i < parameterTypes.length; i++) {
/* 263 */         if (!ClassUtils.isAssignable(parameterTypes[i], methodParameterTypes[i], true)) {
/* 264 */           return false;
/*     */         }
/*     */       } 
/* 267 */       Class<?> varArgParameterType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
/* 268 */       for (; i < parameterTypes.length; i++) {
/* 269 */         if (!ClassUtils.isAssignable(parameterTypes[i], varArgParameterType, true)) {
/* 270 */           return false;
/*     */         }
/*     */       } 
/* 273 */       return true;
/*     */     } 
/* 275 */     return ClassUtils.isAssignable(parameterTypes, methodParameterTypes, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Executable
/*     */   {
/*     */     private final Class<?>[] parameterTypes;
/*     */     
/*     */     private final boolean isVarArgs;
/*     */     
/*     */     private static Executable of(Method method) {
/* 286 */       return new Executable(method); } private static Executable of(Constructor<?> constructor) {
/* 287 */       return new Executable(constructor);
/*     */     }
/*     */     private Executable(Method method) {
/* 290 */       this.parameterTypes = method.getParameterTypes();
/* 291 */       this.isVarArgs = method.isVarArgs();
/*     */     }
/*     */     
/*     */     private Executable(Constructor<?> constructor) {
/* 295 */       this.parameterTypes = constructor.getParameterTypes();
/* 296 */       this.isVarArgs = constructor.isVarArgs();
/*     */     }
/*     */     public Class<?>[] getParameterTypes() {
/* 299 */       return this.parameterTypes;
/*     */     } public boolean isVarArgs() {
/* 301 */       return this.isVarArgs;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\reflect\MemberUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */