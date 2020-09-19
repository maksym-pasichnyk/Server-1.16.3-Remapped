/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Stack;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReflectionUtil
/*     */ {
/*     */   static final int JDK_7u25_OFFSET;
/*  56 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final boolean SUN_REFLECTION_SUPPORTED;
/*     */   
/*     */   private static final Method GET_CALLER_CLASS;
/*     */   
/*     */   static {
/*  63 */     int java7u25CompensationOffset = 0;
/*     */     try {
/*  65 */       Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
/*  66 */       getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", new Class[] { int.class });
/*  67 */       Object o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
/*  68 */       Object test1 = getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
/*  69 */       if (o == null || o != sunReflectionClass) {
/*  70 */         LOGGER.warn("Unexpected return value from Reflection.getCallerClass(): {}", test1);
/*  71 */         getCallerClass = null;
/*  72 */         java7u25CompensationOffset = -1;
/*     */       } else {
/*  74 */         o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(1) });
/*  75 */         if (o == sunReflectionClass) {
/*  76 */           LOGGER.warn("You are using Java 1.7.0_25 which has a broken implementation of Reflection.getCallerClass.");
/*     */           
/*  78 */           LOGGER.warn("You should upgrade to at least Java 1.7.0_40 or later.");
/*  79 */           LOGGER.debug("Using stack depth compensation offset of 1 due to Java 7u25.");
/*  80 */           java7u25CompensationOffset = 1;
/*     */         } 
/*     */       } 
/*  83 */     } catch (Exception|LinkageError e) {
/*  84 */       LOGGER.info("sun.reflect.Reflection.getCallerClass is not supported. ReflectionUtil.getCallerClass will be much slower due to this.", e);
/*     */       
/*  86 */       getCallerClass = null;
/*  87 */       java7u25CompensationOffset = -1;
/*     */     } 
/*     */     
/*  90 */     SUN_REFLECTION_SUPPORTED = (getCallerClass != null);
/*  91 */     GET_CALLER_CLASS = getCallerClass;
/*  92 */     JDK_7u25_OFFSET = java7u25CompensationOffset;
/*     */ 
/*     */     
/*     */     try {
/*  96 */       SecurityManager sm = System.getSecurityManager();
/*  97 */       if (sm != null) {
/*  98 */         sm.checkPermission(new RuntimePermission("createSecurityManager"));
/*     */       }
/* 100 */       psm = new PrivateSecurityManager();
/* 101 */     } catch (SecurityException ignored) {
/* 102 */       LOGGER.debug("Not allowed to create SecurityManager. Falling back to slowest ReflectionUtil implementation.");
/*     */       
/* 104 */       psm = null;
/*     */     } 
/* 106 */     SECURITY_MANAGER = psm;
/*     */   } private static final PrivateSecurityManager SECURITY_MANAGER;
/*     */   static {
/*     */     Method getCallerClass;
/*     */     PrivateSecurityManager psm;
/*     */   }
/*     */   public static boolean supportsFastReflection() {
/* 113 */     return SUN_REFLECTION_SUPPORTED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(int depth) {
/* 122 */     if (depth < 0) {
/* 123 */       throw new IndexOutOfBoundsException(Integer.toString(depth));
/*     */     }
/*     */ 
/*     */     
/* 127 */     if (supportsFastReflection()) {
/*     */       try {
/* 129 */         return (Class)GET_CALLER_CLASS.invoke(null, new Object[] { Integer.valueOf(depth + 1 + JDK_7u25_OFFSET) });
/* 130 */       } catch (Exception e) {
/*     */         
/* 132 */         LOGGER.error("Error in ReflectionUtil.getCallerClass({}).", Integer.valueOf(depth), e);
/*     */         
/* 134 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 139 */     StackTraceElement element = getEquivalentStackTraceElement(depth + 1);
/*     */     try {
/* 141 */       return LoaderUtil.loadClass(element.getClassName());
/* 142 */     } catch (ClassNotFoundException e) {
/* 143 */       LOGGER.error("Could not find class in ReflectionUtil.getCallerClass({}).", Integer.valueOf(depth), e);
/*     */ 
/*     */       
/* 146 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static StackTraceElement getEquivalentStackTraceElement(int depth) {
/* 152 */     StackTraceElement[] elements = (new Throwable()).getStackTrace();
/* 153 */     int i = 0;
/* 154 */     for (StackTraceElement element : elements) {
/* 155 */       if (isValid(element)) {
/* 156 */         if (i == depth) {
/* 157 */           return element;
/*     */         }
/* 159 */         i++;
/*     */       } 
/*     */     } 
/* 162 */     LOGGER.error("Could not find an appropriate StackTraceElement at index {}", Integer.valueOf(depth));
/* 163 */     throw new IndexOutOfBoundsException(Integer.toString(depth));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValid(StackTraceElement element) {
/* 168 */     if (element.isNativeMethod()) {
/* 169 */       return false;
/*     */     }
/* 171 */     String cn = element.getClassName();
/*     */     
/* 173 */     if (cn.startsWith("sun.reflect.")) {
/* 174 */       return false;
/*     */     }
/* 176 */     String mn = element.getMethodName();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
/* 182 */       return false;
/*     */     }
/*     */     
/* 185 */     if (cn.startsWith("jdk.internal.reflect.")) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
/* 194 */       return false;
/*     */     }
/*     */     
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(String fqcn) {
/* 203 */     return getCallerClass(fqcn, "");
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(String fqcn, String pkg) {
/* 209 */     if (supportsFastReflection()) {
/* 210 */       boolean next = false;
/*     */       Class<?> clazz;
/* 212 */       for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
/* 213 */         if (fqcn.equals(clazz.getName())) {
/* 214 */           next = true;
/*     */         
/*     */         }
/* 217 */         else if (next && clazz.getName().startsWith(pkg)) {
/* 218 */           return clazz;
/*     */         } 
/*     */       } 
/*     */       
/* 222 */       return null;
/*     */     } 
/* 224 */     if (SECURITY_MANAGER != null) {
/* 225 */       return SECURITY_MANAGER.getCallerClass(fqcn, pkg);
/*     */     }
/*     */     try {
/* 228 */       return LoaderUtil.loadClass(getCallerClassName(fqcn, pkg, (new Throwable()).getStackTrace()));
/* 229 */     } catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */ 
/*     */       
/* 233 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(Class<?> anchor) {
/* 239 */     if (supportsFastReflection()) {
/* 240 */       boolean next = false;
/*     */       Class<?> clazz;
/* 242 */       for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
/* 243 */         if (anchor.equals(clazz)) {
/* 244 */           next = true;
/*     */         
/*     */         }
/* 247 */         else if (next) {
/* 248 */           return clazz;
/*     */         } 
/*     */       } 
/* 251 */       return Object.class;
/*     */     } 
/* 253 */     if (SECURITY_MANAGER != null) {
/* 254 */       return SECURITY_MANAGER.getCallerClass(anchor);
/*     */     }
/*     */     try {
/* 257 */       return LoaderUtil.loadClass(getCallerClassName(anchor.getName(), "", (new Throwable()).getStackTrace()));
/*     */     }
/* 259 */     catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */       
/* 262 */       return Object.class;
/*     */     } 
/*     */   }
/*     */   private static String getCallerClassName(String fqcn, String pkg, StackTraceElement... elements) {
/* 266 */     boolean next = false;
/* 267 */     for (StackTraceElement element : elements) {
/* 268 */       String className = element.getClassName();
/* 269 */       if (className.equals(fqcn)) {
/* 270 */         next = true;
/*     */       
/*     */       }
/* 273 */       else if (next && className.startsWith(pkg)) {
/* 274 */         return className;
/*     */       } 
/*     */     } 
/* 277 */     return Object.class.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Stack<Class<?>> getCurrentStackTrace() {
/* 284 */     if (SECURITY_MANAGER != null) {
/* 285 */       Class<?>[] array = SECURITY_MANAGER.getClassContext();
/* 286 */       Stack<Class<?>> classes = new Stack<>();
/* 287 */       classes.ensureCapacity(array.length);
/* 288 */       for (Class<?> clazz : array) {
/* 289 */         classes.push(clazz);
/*     */       }
/* 291 */       return classes;
/*     */     } 
/*     */     
/* 294 */     if (supportsFastReflection()) {
/* 295 */       Stack<Class<?>> classes = new Stack<>();
/*     */       Class<?> clazz;
/* 297 */       for (int i = 1; null != (clazz = getCallerClass(i)); i++) {
/* 298 */         classes.push(clazz);
/*     */       }
/* 300 */       return classes;
/*     */     } 
/* 302 */     return new Stack<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PrivateSecurityManager
/*     */     extends SecurityManager
/*     */   {
/*     */     protected Class<?>[] getClassContext() {
/* 312 */       return super.getClassContext();
/*     */     }
/*     */     
/*     */     protected Class<?> getCallerClass(String fqcn, String pkg) {
/* 316 */       boolean next = false;
/* 317 */       for (Class<?> clazz : getClassContext()) {
/* 318 */         if (fqcn.equals(clazz.getName())) {
/* 319 */           next = true;
/*     */         
/*     */         }
/* 322 */         else if (next && clazz.getName().startsWith(pkg)) {
/* 323 */           return clazz;
/*     */         } 
/*     */       } 
/*     */       
/* 327 */       return null;
/*     */     }
/*     */     
/*     */     protected Class<?> getCallerClass(Class<?> anchor) {
/* 331 */       boolean next = false;
/* 332 */       for (Class<?> clazz : getClassContext()) {
/* 333 */         if (anchor.equals(clazz)) {
/* 334 */           next = true;
/*     */         
/*     */         }
/* 337 */         else if (next) {
/* 338 */           return clazz;
/*     */         } 
/*     */       } 
/* 341 */       return Object.class;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\ReflectionUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */