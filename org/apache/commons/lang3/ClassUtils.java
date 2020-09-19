/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang3.mutable.MutableObject;
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
/*      */ public class ClassUtils
/*      */ {
/*      */   public static final char PACKAGE_SEPARATOR_CHAR = '.';
/*      */   
/*      */   public enum Interfaces
/*      */   {
/*   52 */     INCLUDE, EXCLUDE;
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
/*   63 */   public static final String PACKAGE_SEPARATOR = String.valueOf('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   73 */   public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   78 */   private static final Map<String, Class<?>> namePrimitiveMap = new HashMap<String, Class<?>>();
/*      */   static {
/*   80 */     namePrimitiveMap.put("boolean", boolean.class);
/*   81 */     namePrimitiveMap.put("byte", byte.class);
/*   82 */     namePrimitiveMap.put("char", char.class);
/*   83 */     namePrimitiveMap.put("short", short.class);
/*   84 */     namePrimitiveMap.put("int", int.class);
/*   85 */     namePrimitiveMap.put("long", long.class);
/*   86 */     namePrimitiveMap.put("double", double.class);
/*   87 */     namePrimitiveMap.put("float", float.class);
/*   88 */     namePrimitiveMap.put("void", void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   94 */   private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
/*      */   static {
/*   96 */     primitiveWrapperMap.put(boolean.class, Boolean.class);
/*   97 */     primitiveWrapperMap.put(byte.class, Byte.class);
/*   98 */     primitiveWrapperMap.put(char.class, Character.class);
/*   99 */     primitiveWrapperMap.put(short.class, Short.class);
/*  100 */     primitiveWrapperMap.put(int.class, Integer.class);
/*  101 */     primitiveWrapperMap.put(long.class, Long.class);
/*  102 */     primitiveWrapperMap.put(double.class, Double.class);
/*  103 */     primitiveWrapperMap.put(float.class, Float.class);
/*  104 */     primitiveWrapperMap.put(void.class, void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
/*      */   static {
/*  112 */     for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperMap.entrySet()) {
/*  113 */       Class<?> primitiveClass = entry.getKey();
/*  114 */       Class<?> wrapperClass = entry.getValue();
/*  115 */       if (!primitiveClass.equals(wrapperClass)) {
/*  116 */         wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
/*      */       }
/*      */     } 
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
/*  135 */     Map<String, String> m = new HashMap<String, String>();
/*  136 */     m.put("int", "I");
/*  137 */     m.put("boolean", "Z");
/*  138 */     m.put("float", "F");
/*  139 */     m.put("long", "J");
/*  140 */     m.put("short", "S");
/*  141 */     m.put("byte", "B");
/*  142 */     m.put("double", "D");
/*  143 */     m.put("char", "C");
/*  144 */     Map<String, String> r = new HashMap<String, String>();
/*  145 */     for (Map.Entry<String, String> e : m.entrySet()) {
/*  146 */       r.put(e.getValue(), e.getKey());
/*      */     }
/*  148 */     abbreviationMap = Collections.unmodifiableMap(m);
/*  149 */     reverseAbbreviationMap = Collections.unmodifiableMap(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Map<String, String> abbreviationMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Map<String, String> reverseAbbreviationMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(Object object, String valueIfNull) {
/*  174 */     if (object == null) {
/*  175 */       return valueIfNull;
/*      */     }
/*  177 */     return getShortClassName(object.getClass());
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
/*      */   public static String getShortClassName(Class<?> cls) {
/*  191 */     if (cls == null) {
/*  192 */       return "";
/*      */     }
/*  194 */     return getShortClassName(cls.getName());
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
/*      */   public static String getShortClassName(String className) {
/*  210 */     if (StringUtils.isEmpty(className)) {
/*  211 */       return "";
/*      */     }
/*      */     
/*  214 */     StringBuilder arrayPrefix = new StringBuilder();
/*      */ 
/*      */     
/*  217 */     if (className.startsWith("[")) {
/*  218 */       while (className.charAt(0) == '[') {
/*  219 */         className = className.substring(1);
/*  220 */         arrayPrefix.append("[]");
/*      */       } 
/*      */       
/*  223 */       if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/*  224 */         className = className.substring(1, className.length() - 1);
/*      */       }
/*      */       
/*  227 */       if (reverseAbbreviationMap.containsKey(className)) {
/*  228 */         className = reverseAbbreviationMap.get(className);
/*      */       }
/*      */     } 
/*      */     
/*  232 */     int lastDotIdx = className.lastIndexOf('.');
/*  233 */     int innerIdx = className.indexOf('$', (lastDotIdx == -1) ? 0 : (lastDotIdx + 1));
/*      */     
/*  235 */     String out = className.substring(lastDotIdx + 1);
/*  236 */     if (innerIdx != -1) {
/*  237 */       out = out.replace('$', '.');
/*      */     }
/*  239 */     return out + arrayPrefix;
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
/*      */   public static String getSimpleName(Class<?> cls) {
/*  251 */     if (cls == null) {
/*  252 */       return "";
/*      */     }
/*  254 */     return cls.getSimpleName();
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
/*      */   public static String getSimpleName(Object object, String valueIfNull) {
/*  267 */     if (object == null) {
/*  268 */       return valueIfNull;
/*      */     }
/*  270 */     return getSimpleName(object.getClass());
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
/*      */   public static String getPackageName(Object object, String valueIfNull) {
/*  283 */     if (object == null) {
/*  284 */       return valueIfNull;
/*      */     }
/*  286 */     return getPackageName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> cls) {
/*  296 */     if (cls == null) {
/*  297 */       return "";
/*      */     }
/*  299 */     return getPackageName(cls.getName());
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
/*      */   public static String getPackageName(String className) {
/*  312 */     if (StringUtils.isEmpty(className)) {
/*  313 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  317 */     while (className.charAt(0) == '[') {
/*  318 */       className = className.substring(1);
/*      */     }
/*      */     
/*  321 */     if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/*  322 */       className = className.substring(1);
/*      */     }
/*      */     
/*  325 */     int i = className.lastIndexOf('.');
/*  326 */     if (i == -1) {
/*  327 */       return "";
/*      */     }
/*  329 */     return className.substring(0, i);
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
/*      */   public static String getAbbreviatedName(Class<?> cls, int len) {
/*  345 */     if (cls == null) {
/*  346 */       return "";
/*      */     }
/*  348 */     return getAbbreviatedName(cls.getName(), len);
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
/*      */ 
/*      */   
/*      */   public static String getAbbreviatedName(String className, int len) {
/*  377 */     if (len <= 0) {
/*  378 */       throw new IllegalArgumentException("len must be > 0");
/*      */     }
/*  380 */     if (className == null) {
/*  381 */       return "";
/*      */     }
/*      */     
/*  384 */     int availableSpace = len;
/*  385 */     int packageLevels = StringUtils.countMatches(className, '.');
/*  386 */     String[] output = new String[packageLevels + 1];
/*  387 */     int endIndex = className.length() - 1;
/*  388 */     for (int level = packageLevels; level >= 0; level--) {
/*  389 */       int startIndex = className.lastIndexOf('.', endIndex);
/*  390 */       String part = className.substring(startIndex + 1, endIndex + 1);
/*  391 */       availableSpace -= part.length();
/*  392 */       if (level > 0)
/*      */       {
/*  394 */         availableSpace--;
/*      */       }
/*  396 */       if (level == packageLevels) {
/*      */         
/*  398 */         output[level] = part;
/*      */       }
/*  400 */       else if (availableSpace > 0) {
/*  401 */         output[level] = part;
/*      */       } else {
/*      */         
/*  404 */         output[level] = part.substring(0, 1);
/*      */       } 
/*      */       
/*  407 */       endIndex = startIndex - 1;
/*      */     } 
/*      */     
/*  410 */     return StringUtils.join((Object[])output, '.');
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
/*      */   public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
/*  423 */     if (cls == null) {
/*  424 */       return null;
/*      */     }
/*  426 */     List<Class<?>> classes = new ArrayList<Class<?>>();
/*  427 */     Class<?> superclass = cls.getSuperclass();
/*  428 */     while (superclass != null) {
/*  429 */       classes.add(superclass);
/*  430 */       superclass = superclass.getSuperclass();
/*      */     } 
/*  432 */     return classes;
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
/*      */   public static List<Class<?>> getAllInterfaces(Class<?> cls) {
/*  449 */     if (cls == null) {
/*  450 */       return null;
/*      */     }
/*      */     
/*  453 */     LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
/*  454 */     getAllInterfaces(cls, interfacesFound);
/*      */     
/*  456 */     return new ArrayList<Class<?>>(interfacesFound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
/*  466 */     while (cls != null) {
/*  467 */       Class<?>[] interfaces = cls.getInterfaces();
/*      */       
/*  469 */       for (Class<?> i : interfaces) {
/*  470 */         if (interfacesFound.add(i)) {
/*  471 */           getAllInterfaces(i, interfacesFound);
/*      */         }
/*      */       } 
/*      */       
/*  475 */       cls = cls.getSuperclass();
/*      */     } 
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
/*      */   public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
/*  494 */     if (classNames == null) {
/*  495 */       return null;
/*      */     }
/*  497 */     List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
/*  498 */     for (String className : classNames) {
/*      */       try {
/*  500 */         classes.add(Class.forName(className));
/*  501 */       } catch (Exception ex) {
/*  502 */         classes.add(null);
/*      */       } 
/*      */     } 
/*  505 */     return classes;
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
/*      */   public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
/*  521 */     if (classes == null) {
/*  522 */       return null;
/*      */     }
/*  524 */     List<String> classNames = new ArrayList<String>(classes.size());
/*  525 */     for (Class<?> cls : classes) {
/*  526 */       if (cls == null) {
/*  527 */         classNames.add(null); continue;
/*      */       } 
/*  529 */       classNames.add(cls.getName());
/*      */     } 
/*      */     
/*  532 */     return classNames;
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
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
/*  574 */     return isAssignable(classArray, toClassArray, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
/*  610 */     if (!ArrayUtils.isSameLength((Object[])classArray, (Object[])toClassArray)) {
/*  611 */       return false;
/*      */     }
/*  613 */     if (classArray == null) {
/*  614 */       classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/*  616 */     if (toClassArray == null) {
/*  617 */       toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/*  619 */     for (int i = 0; i < classArray.length; i++) {
/*  620 */       if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
/*  621 */         return false;
/*      */       }
/*      */     } 
/*  624 */     return true;
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
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> type) {
/*  638 */     if (type == null) {
/*  639 */       return false;
/*      */     }
/*  641 */     return (type.isPrimitive() || isPrimitiveWrapper(type));
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
/*      */   public static boolean isPrimitiveWrapper(Class<?> type) {
/*  655 */     return wrapperPrimitiveMap.containsKey(type);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
/*  690 */     return isAssignable(cls, toClass, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
/*  721 */     if (toClass == null) {
/*  722 */       return false;
/*      */     }
/*      */     
/*  725 */     if (cls == null) {
/*  726 */       return !toClass.isPrimitive();
/*      */     }
/*      */     
/*  729 */     if (autoboxing) {
/*  730 */       if (cls.isPrimitive() && !toClass.isPrimitive()) {
/*  731 */         cls = primitiveToWrapper(cls);
/*  732 */         if (cls == null) {
/*  733 */           return false;
/*      */         }
/*      */       } 
/*  736 */       if (toClass.isPrimitive() && !cls.isPrimitive()) {
/*  737 */         cls = wrapperToPrimitive(cls);
/*  738 */         if (cls == null) {
/*  739 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  743 */     if (cls.equals(toClass)) {
/*  744 */       return true;
/*      */     }
/*  746 */     if (cls.isPrimitive()) {
/*  747 */       if (!toClass.isPrimitive()) {
/*  748 */         return false;
/*      */       }
/*  750 */       if (int.class.equals(cls)) {
/*  751 */         return (long.class.equals(toClass) || float.class
/*  752 */           .equals(toClass) || double.class
/*  753 */           .equals(toClass));
/*      */       }
/*  755 */       if (long.class.equals(cls)) {
/*  756 */         return (float.class.equals(toClass) || double.class
/*  757 */           .equals(toClass));
/*      */       }
/*  759 */       if (boolean.class.equals(cls)) {
/*  760 */         return false;
/*      */       }
/*  762 */       if (double.class.equals(cls)) {
/*  763 */         return false;
/*      */       }
/*  765 */       if (float.class.equals(cls)) {
/*  766 */         return double.class.equals(toClass);
/*      */       }
/*  768 */       if (char.class.equals(cls)) {
/*  769 */         return (int.class.equals(toClass) || long.class
/*  770 */           .equals(toClass) || float.class
/*  771 */           .equals(toClass) || double.class
/*  772 */           .equals(toClass));
/*      */       }
/*  774 */       if (short.class.equals(cls)) {
/*  775 */         return (int.class.equals(toClass) || long.class
/*  776 */           .equals(toClass) || float.class
/*  777 */           .equals(toClass) || double.class
/*  778 */           .equals(toClass));
/*      */       }
/*  780 */       if (byte.class.equals(cls)) {
/*  781 */         return (short.class.equals(toClass) || int.class
/*  782 */           .equals(toClass) || long.class
/*  783 */           .equals(toClass) || float.class
/*  784 */           .equals(toClass) || double.class
/*  785 */           .equals(toClass));
/*      */       }
/*      */       
/*  788 */       return false;
/*      */     } 
/*  790 */     return toClass.isAssignableFrom(cls);
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
/*      */   public static Class<?> primitiveToWrapper(Class<?> cls) {
/*  806 */     Class<?> convertedClass = cls;
/*  807 */     if (cls != null && cls.isPrimitive()) {
/*  808 */       convertedClass = primitiveWrapperMap.get(cls);
/*      */     }
/*  810 */     return convertedClass;
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
/*      */   public static Class<?>[] primitivesToWrappers(Class<?>... classes) {
/*  824 */     if (classes == null) {
/*  825 */       return null;
/*      */     }
/*      */     
/*  828 */     if (classes.length == 0) {
/*  829 */       return classes;
/*      */     }
/*      */     
/*  832 */     Class<?>[] convertedClasses = new Class[classes.length];
/*  833 */     for (int i = 0; i < classes.length; i++) {
/*  834 */       convertedClasses[i] = primitiveToWrapper(classes[i]);
/*      */     }
/*  836 */     return convertedClasses;
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
/*      */   public static Class<?> wrapperToPrimitive(Class<?> cls) {
/*  856 */     return wrapperPrimitiveMap.get(cls);
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
/*      */   public static Class<?>[] wrappersToPrimitives(Class<?>... classes) {
/*  874 */     if (classes == null) {
/*  875 */       return null;
/*      */     }
/*      */     
/*  878 */     if (classes.length == 0) {
/*  879 */       return classes;
/*      */     }
/*      */     
/*  882 */     Class<?>[] convertedClasses = new Class[classes.length];
/*  883 */     for (int i = 0; i < classes.length; i++) {
/*  884 */       convertedClasses[i] = wrapperToPrimitive(classes[i]);
/*      */     }
/*  886 */     return convertedClasses;
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
/*      */   public static boolean isInnerClass(Class<?> cls) {
/*  899 */     return (cls != null && cls.getEnclosingClass() != null);
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
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
/*      */     try {
/*      */       Class<?> clazz;
/*  920 */       if (namePrimitiveMap.containsKey(className)) {
/*  921 */         clazz = namePrimitiveMap.get(className);
/*      */       } else {
/*  923 */         clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
/*      */       } 
/*  925 */       return clazz;
/*  926 */     } catch (ClassNotFoundException ex) {
/*      */       
/*  928 */       int lastDotIndex = className.lastIndexOf('.');
/*      */       
/*  930 */       if (lastDotIndex != -1) {
/*      */         try {
/*  932 */           return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className
/*  933 */               .substring(lastDotIndex + 1), initialize);
/*      */         }
/*  935 */         catch (ClassNotFoundException classNotFoundException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  940 */       throw ex;
/*      */     } 
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
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
/*  957 */     return getClass(classLoader, className, true);
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
/*      */   public static Class<?> getClass(String className) throws ClassNotFoundException {
/*  972 */     return getClass(className, true);
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
/*      */   public static Class<?> getClass(String className, boolean initialize) throws ClassNotFoundException {
/*  987 */     ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/*  988 */     ClassLoader loader = (contextCL == null) ? ClassUtils.class.getClassLoader() : contextCL;
/*  989 */     return getClass(loader, className, initialize);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
/* 1019 */     Method declaredMethod = cls.getMethod(methodName, parameterTypes);
/* 1020 */     if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
/* 1021 */       return declaredMethod;
/*      */     }
/*      */     
/* 1024 */     List<Class<?>> candidateClasses = new ArrayList<Class<?>>();
/* 1025 */     candidateClasses.addAll(getAllInterfaces(cls));
/* 1026 */     candidateClasses.addAll(getAllSuperclasses(cls));
/*      */     
/* 1028 */     for (Class<?> candidateClass : candidateClasses) {
/* 1029 */       Method candidateMethod; if (!Modifier.isPublic(candidateClass.getModifiers())) {
/*      */         continue;
/*      */       }
/*      */       
/*      */       try {
/* 1034 */         candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
/* 1035 */       } catch (NoSuchMethodException ex) {
/*      */         continue;
/*      */       } 
/* 1038 */       if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
/* 1039 */         return candidateMethod;
/*      */       }
/*      */     } 
/*      */     
/* 1043 */     throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + 
/* 1044 */         ArrayUtils.toString(parameterTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String toCanonicalName(String className) {
/* 1055 */     className = StringUtils.deleteWhitespace(className);
/* 1056 */     if (className == null)
/* 1057 */       throw new NullPointerException("className must not be null."); 
/* 1058 */     if (className.endsWith("[]")) {
/* 1059 */       StringBuilder classNameBuffer = new StringBuilder();
/* 1060 */       while (className.endsWith("[]")) {
/* 1061 */         className = className.substring(0, className.length() - 2);
/* 1062 */         classNameBuffer.append("[");
/*      */       } 
/* 1064 */       String abbreviation = abbreviationMap.get(className);
/* 1065 */       if (abbreviation != null) {
/* 1066 */         classNameBuffer.append(abbreviation);
/*      */       } else {
/* 1068 */         classNameBuffer.append("L").append(className).append(";");
/*      */       } 
/* 1070 */       className = classNameBuffer.toString();
/*      */     } 
/* 1072 */     return className;
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
/*      */   public static Class<?>[] toClass(Object... array) {
/* 1086 */     if (array == null)
/* 1087 */       return null; 
/* 1088 */     if (array.length == 0) {
/* 1089 */       return ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/* 1091 */     Class<?>[] classes = new Class[array.length];
/* 1092 */     for (int i = 0; i < array.length; i++) {
/* 1093 */       classes[i] = (array[i] == null) ? null : array[i].getClass();
/*      */     }
/* 1095 */     return classes;
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
/*      */   public static String getShortCanonicalName(Object object, String valueIfNull) {
/* 1109 */     if (object == null) {
/* 1110 */       return valueIfNull;
/*      */     }
/* 1112 */     return getShortCanonicalName(object.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(Class<?> cls) {
/* 1123 */     if (cls == null) {
/* 1124 */       return "";
/*      */     }
/* 1126 */     return getShortCanonicalName(cls.getName());
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
/*      */   public static String getShortCanonicalName(String canonicalName) {
/* 1139 */     return getShortClassName(getCanonicalName(canonicalName));
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
/*      */   public static String getPackageCanonicalName(Object object, String valueIfNull) {
/* 1153 */     if (object == null) {
/* 1154 */       return valueIfNull;
/*      */     }
/* 1156 */     return getPackageCanonicalName(object.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(Class<?> cls) {
/* 1167 */     if (cls == null) {
/* 1168 */       return "";
/*      */     }
/* 1170 */     return getPackageCanonicalName(cls.getName());
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
/*      */   public static String getPackageCanonicalName(String canonicalName) {
/* 1184 */     return getPackageName(getCanonicalName(canonicalName));
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
/*      */   private static String getCanonicalName(String className) {
/* 1204 */     className = StringUtils.deleteWhitespace(className);
/* 1205 */     if (className == null) {
/* 1206 */       return null;
/*      */     }
/* 1208 */     int dim = 0;
/* 1209 */     while (className.startsWith("[")) {
/* 1210 */       dim++;
/* 1211 */       className = className.substring(1);
/*      */     } 
/* 1213 */     if (dim < 1) {
/* 1214 */       return className;
/*      */     }
/* 1216 */     if (className.startsWith("L")) {
/* 1217 */       className = className.substring(1, 
/*      */           
/* 1219 */           className.endsWith(";") ? (className
/* 1220 */           .length() - 1) : className
/* 1221 */           .length());
/*      */     }
/* 1223 */     else if (className.length() > 0) {
/* 1224 */       className = reverseAbbreviationMap.get(className.substring(0, 1));
/*      */     } 
/*      */     
/* 1227 */     StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
/* 1228 */     for (int i = 0; i < dim; i++) {
/* 1229 */       canonicalClassNameBuffer.append("[]");
/*      */     }
/* 1231 */     return canonicalClassNameBuffer.toString();
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
/*      */   public static Iterable<Class<?>> hierarchy(Class<?> type) {
/* 1243 */     return hierarchy(type, Interfaces.EXCLUDE);
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
/*      */   public static Iterable<Class<?>> hierarchy(final Class<?> type, Interfaces interfacesBehavior) {
/* 1255 */     final Iterable<Class<?>> classes = new Iterable<Class<?>>()
/*      */       {
/*      */         public Iterator<Class<?>> iterator()
/*      */         {
/* 1259 */           final MutableObject<Class<?>> next = new MutableObject(type);
/* 1260 */           return new Iterator<Class<?>>()
/*      */             {
/*      */               public boolean hasNext()
/*      */               {
/* 1264 */                 return (next.getValue() != null);
/*      */               }
/*      */ 
/*      */               
/*      */               public Class<?> next() {
/* 1269 */                 Class<?> result = (Class)next.getValue();
/* 1270 */                 next.setValue(result.getSuperclass());
/* 1271 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/* 1276 */                 throw new UnsupportedOperationException();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */ 
/*      */     
/* 1283 */     if (interfacesBehavior != Interfaces.INCLUDE) {
/* 1284 */       return classes;
/*      */     }
/* 1286 */     return new Iterable<Class<?>>()
/*      */       {
/*      */         public Iterator<Class<?>> iterator()
/*      */         {
/* 1290 */           final Set<Class<?>> seenInterfaces = new HashSet<Class<?>>();
/* 1291 */           final Iterator<Class<?>> wrapped = classes.iterator();
/*      */           
/* 1293 */           return new Iterator<Class<?>>() {
/* 1294 */               Iterator<Class<?>> interfaces = Collections.<Class<?>>emptySet().iterator();
/*      */ 
/*      */               
/*      */               public boolean hasNext() {
/* 1298 */                 return (this.interfaces.hasNext() || wrapped.hasNext());
/*      */               }
/*      */ 
/*      */               
/*      */               public Class<?> next() {
/* 1303 */                 if (this.interfaces.hasNext()) {
/* 1304 */                   Class<?> nextInterface = this.interfaces.next();
/* 1305 */                   seenInterfaces.add(nextInterface);
/* 1306 */                   return nextInterface;
/*      */                 } 
/* 1308 */                 Class<?> nextSuperclass = wrapped.next();
/* 1309 */                 Set<Class<?>> currentInterfaces = new LinkedHashSet<Class<?>>();
/* 1310 */                 walkInterfaces(currentInterfaces, nextSuperclass);
/* 1311 */                 this.interfaces = currentInterfaces.iterator();
/* 1312 */                 return nextSuperclass;
/*      */               }
/*      */               
/*      */               private void walkInterfaces(Set<Class<?>> addTo, Class<?> c) {
/* 1316 */                 for (Class<?> iface : c.getInterfaces()) {
/* 1317 */                   if (!seenInterfaces.contains(iface)) {
/* 1318 */                     addTo.add(iface);
/*      */                   }
/* 1320 */                   walkInterfaces(addTo, iface);
/*      */                 } 
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/* 1326 */                 throw new UnsupportedOperationException();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\ClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */