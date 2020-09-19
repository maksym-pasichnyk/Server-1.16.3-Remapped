/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodUtils
/*     */ {
/*     */   public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  94 */     return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class<?>[])null);
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
/*     */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 117 */     return invokeMethod(object, forceAccess, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
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
/*     */   public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 145 */     args = ArrayUtils.nullToEmpty(args);
/* 146 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 147 */     return invokeMethod(object, methodName, args, parameterTypes);
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
/*     */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 176 */     args = ArrayUtils.nullToEmpty(args);
/* 177 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 178 */     return invokeMethod(object, forceAccess, methodName, args, parameterTypes);
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
/*     */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 204 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 205 */     args = ArrayUtils.nullToEmpty(args);
/*     */ 
/*     */     
/* 208 */     Method method = null;
/* 209 */     boolean isOriginallyAccessible = false;
/* 210 */     Object result = null;
/*     */     try {
/*     */       String messagePrefix;
/* 213 */       if (forceAccess) {
/* 214 */         messagePrefix = "No such method: ";
/* 215 */         method = getMatchingMethod(object.getClass(), methodName, parameterTypes);
/*     */         
/* 217 */         if (method != null) {
/* 218 */           isOriginallyAccessible = method.isAccessible();
/* 219 */           if (!isOriginallyAccessible) {
/* 220 */             method.setAccessible(true);
/*     */           }
/*     */         } 
/*     */       } else {
/* 224 */         messagePrefix = "No such accessible method: ";
/* 225 */         method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
/*     */       } 
/*     */ 
/*     */       
/* 229 */       if (method == null) {
/* 230 */         throw new NoSuchMethodException(messagePrefix + methodName + "() on object: " + object
/*     */             
/* 232 */             .getClass().getName());
/*     */       }
/* 234 */       args = toVarArgs(method, args);
/*     */       
/* 236 */       result = method.invoke(object, args);
/*     */     } finally {
/*     */       
/* 239 */       if (method != null && forceAccess && method.isAccessible() != isOriginallyAccessible) {
/* 240 */         method.setAccessible(isOriginallyAccessible);
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return result;
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
/*     */   public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 270 */     return invokeMethod(object, false, methodName, args, parameterTypes);
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
/*     */   public static Object invokeExactMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 294 */     return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
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
/*     */   public static Object invokeExactMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 317 */     args = ArrayUtils.nullToEmpty(args);
/* 318 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 319 */     return invokeExactMethod(object, methodName, args, parameterTypes);
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
/*     */   public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 345 */     args = ArrayUtils.nullToEmpty(args);
/* 346 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 347 */     Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
/*     */     
/* 349 */     if (method == null) {
/* 350 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object
/*     */           
/* 352 */           .getClass().getName());
/*     */     }
/* 354 */     return method.invoke(object, args);
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
/*     */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 380 */     args = ArrayUtils.nullToEmpty(args);
/* 381 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 382 */     Method method = getAccessibleMethod(cls, methodName, parameterTypes);
/* 383 */     if (method == null) {
/* 384 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls
/* 385 */           .getName());
/*     */     }
/* 387 */     return method.invoke(null, args);
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
/*     */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 417 */     args = ArrayUtils.nullToEmpty(args);
/* 418 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 419 */     return invokeStaticMethod(cls, methodName, args, parameterTypes);
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
/*     */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 448 */     args = ArrayUtils.nullToEmpty(args);
/* 449 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 450 */     Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
/*     */     
/* 452 */     if (method == null) {
/* 453 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls
/* 454 */           .getName());
/*     */     }
/* 456 */     args = toVarArgs(method, args);
/* 457 */     return method.invoke(null, args);
/*     */   }
/*     */   
/*     */   private static Object[] toVarArgs(Method method, Object[] args) {
/* 461 */     if (method.isVarArgs()) {
/* 462 */       Class<?>[] methodParameterTypes = method.getParameterTypes();
/* 463 */       args = getVarArgs(args, methodParameterTypes);
/*     */     } 
/* 465 */     return args;
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
/*     */   static Object[] getVarArgs(Object[] args, Class<?>[] methodParameterTypes) {
/* 479 */     if (args.length == methodParameterTypes.length && args[args.length - 1]
/* 480 */       .getClass().equals(methodParameterTypes[methodParameterTypes.length - 1]))
/*     */     {
/* 482 */       return args;
/*     */     }
/*     */ 
/*     */     
/* 486 */     Object[] newArgs = new Object[methodParameterTypes.length];
/*     */ 
/*     */     
/* 489 */     System.arraycopy(args, 0, newArgs, 0, methodParameterTypes.length - 1);
/*     */ 
/*     */     
/* 492 */     Class<?> varArgComponentType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
/* 493 */     int varArgLength = args.length - methodParameterTypes.length + 1;
/*     */     
/* 495 */     Object varArgsArray = Array.newInstance(ClassUtils.primitiveToWrapper(varArgComponentType), varArgLength);
/*     */     
/* 497 */     System.arraycopy(args, methodParameterTypes.length - 1, varArgsArray, 0, varArgLength);
/*     */     
/* 499 */     if (varArgComponentType.isPrimitive())
/*     */     {
/* 501 */       varArgsArray = ArrayUtils.toPrimitive(varArgsArray);
/*     */     }
/*     */ 
/*     */     
/* 505 */     newArgs[methodParameterTypes.length - 1] = varArgsArray;
/*     */ 
/*     */     
/* 508 */     return newArgs;
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
/*     */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 532 */     args = ArrayUtils.nullToEmpty(args);
/* 533 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 534 */     return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
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
/*     */   public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*     */     try {
/* 552 */       return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
/*     */     }
/* 554 */     catch (NoSuchMethodException e) {
/* 555 */       return null;
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
/*     */   public static Method getAccessibleMethod(Method method) {
/* 568 */     if (!MemberUtils.isAccessible(method)) {
/* 569 */       return null;
/*     */     }
/*     */     
/* 572 */     Class<?> cls = method.getDeclaringClass();
/* 573 */     if (Modifier.isPublic(cls.getModifiers())) {
/* 574 */       return method;
/*     */     }
/* 576 */     String methodName = method.getName();
/* 577 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*     */ 
/*     */     
/* 580 */     method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
/*     */ 
/*     */ 
/*     */     
/* 584 */     if (method == null) {
/* 585 */       method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
/*     */     }
/*     */     
/* 588 */     return method;
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
/*     */   private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/* 603 */     Class<?> parentClass = cls.getSuperclass();
/* 604 */     while (parentClass != null) {
/* 605 */       if (Modifier.isPublic(parentClass.getModifiers())) {
/*     */         try {
/* 607 */           return parentClass.getMethod(methodName, parameterTypes);
/* 608 */         } catch (NoSuchMethodException e) {
/* 609 */           return null;
/*     */         } 
/*     */       }
/* 612 */       parentClass = parentClass.getSuperclass();
/*     */     } 
/* 614 */     return null;
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
/*     */   private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/* 635 */     for (; cls != null; cls = cls.getSuperclass()) {
/*     */ 
/*     */       
/* 638 */       Class<?>[] interfaces = cls.getInterfaces();
/* 639 */       for (int i = 0; i < interfaces.length; i++) {
/*     */         
/* 641 */         if (Modifier.isPublic(interfaces[i].getModifiers()))
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 646 */             return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
/*     */           }
/* 648 */           catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 655 */             Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
/*     */             
/* 657 */             if (method != null)
/* 658 */               return method; 
/*     */           }  
/*     */       } 
/*     */     } 
/* 662 */     return null;
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
/*     */   public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*     */     try {
/* 690 */       Method method = cls.getMethod(methodName, parameterTypes);
/* 691 */       MemberUtils.setAccessibleWorkaround(method);
/* 692 */       return method;
/* 693 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */       
/* 696 */       Method bestMatch = null;
/* 697 */       Method[] methods = cls.getMethods();
/* 698 */       for (Method method : methods) {
/*     */         
/* 700 */         if (method.getName().equals(methodName) && 
/* 701 */           MemberUtils.isMatchingMethod(method, parameterTypes)) {
/*     */           
/* 703 */           Method accessibleMethod = getAccessibleMethod(method);
/* 704 */           if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareMethodFit(accessibleMethod, bestMatch, parameterTypes) < 0))
/*     */           {
/*     */ 
/*     */             
/* 708 */             bestMatch = accessibleMethod;
/*     */           }
/*     */         } 
/*     */       } 
/* 712 */       if (bestMatch != null) {
/* 713 */         MemberUtils.setAccessibleWorkaround(bestMatch);
/*     */       }
/* 715 */       return bestMatch;
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
/*     */   public static Method getMatchingMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/* 730 */     Validate.notNull(cls, "Null class not allowed.", new Object[0]);
/* 731 */     Validate.notEmpty(methodName, "Null or blank methodName not allowed.", new Object[0]);
/*     */ 
/*     */     
/* 734 */     Method[] methodArray = cls.getDeclaredMethods();
/* 735 */     List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(cls);
/* 736 */     for (Class<?> klass : superclassList) {
/* 737 */       methodArray = (Method[])ArrayUtils.addAll((Object[])methodArray, (Object[])klass.getDeclaredMethods());
/*     */     }
/*     */     
/* 740 */     Method inexactMatch = null;
/* 741 */     for (Method method : methodArray) {
/* 742 */       if (methodName.equals(method.getName()) && 
/* 743 */         ArrayUtils.isEquals(parameterTypes, method.getParameterTypes()))
/* 744 */         return method; 
/* 745 */       if (methodName.equals(method.getName()) && 
/* 746 */         ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
/* 747 */         if (inexactMatch == null) {
/* 748 */           inexactMatch = method;
/* 749 */         } else if (distance(parameterTypes, method.getParameterTypes()) < 
/* 750 */           distance(parameterTypes, inexactMatch.getParameterTypes())) {
/* 751 */           inexactMatch = method;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 756 */     return inexactMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int distance(Class<?>[] classArray, Class<?>[] toClassArray) {
/* 767 */     int answer = 0;
/*     */     
/* 769 */     if (!ClassUtils.isAssignable(classArray, toClassArray, true)) {
/* 770 */       return -1;
/*     */     }
/* 772 */     for (int offset = 0; offset < classArray.length; offset++) {
/*     */       
/* 774 */       if (!classArray[offset].equals(toClassArray[offset]))
/*     */       {
/* 776 */         if (ClassUtils.isAssignable(classArray[offset], toClassArray[offset], true) && 
/* 777 */           !ClassUtils.isAssignable(classArray[offset], toClassArray[offset], false)) {
/* 778 */           answer++;
/*     */         } else {
/* 780 */           answer += 2;
/*     */         } 
/*     */       }
/*     */     } 
/* 784 */     return answer;
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
/*     */   public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfacesBehavior) {
/* 796 */     Validate.notNull(method);
/* 797 */     Set<Method> result = new LinkedHashSet<Method>();
/* 798 */     result.add(method);
/*     */     
/* 800 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*     */     
/* 802 */     Class<?> declaringClass = method.getDeclaringClass();
/*     */     
/* 804 */     Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
/*     */     
/* 806 */     hierarchy.next();
/* 807 */     label21: while (hierarchy.hasNext()) {
/* 808 */       Class<?> c = hierarchy.next();
/* 809 */       Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
/* 810 */       if (m == null) {
/*     */         continue;
/*     */       }
/* 813 */       if (Arrays.equals((Object[])m.getParameterTypes(), (Object[])parameterTypes)) {
/*     */         
/* 815 */         result.add(m);
/*     */         
/*     */         continue;
/*     */       } 
/* 819 */       Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
/* 820 */       for (int i = 0; i < parameterTypes.length; i++) {
/* 821 */         Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
/* 822 */         Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
/* 823 */         if (!TypeUtils.equals(childType, parentType)) {
/*     */           continue label21;
/*     */         }
/*     */       } 
/* 827 */       result.add(m);
/*     */     } 
/* 829 */     return result;
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
/*     */   public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 844 */     List<Method> annotatedMethodsList = getMethodsListWithAnnotation(cls, annotationCls);
/* 845 */     return annotatedMethodsList.<Method>toArray(new Method[annotatedMethodsList.size()]);
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
/*     */   public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 860 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 861 */     Validate.isTrue((annotationCls != null), "The annotation class must not be null", new Object[0]);
/* 862 */     Method[] allMethods = cls.getMethods();
/* 863 */     List<Method> annotatedMethods = new ArrayList<Method>();
/* 864 */     for (Method method : allMethods) {
/* 865 */       if (method.getAnnotation(annotationCls) != null) {
/* 866 */         annotatedMethods.add(method);
/*     */       }
/*     */     } 
/* 869 */     return annotatedMethods;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\reflect\MethodUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */