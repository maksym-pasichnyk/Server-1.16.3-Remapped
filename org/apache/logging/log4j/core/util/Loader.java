/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Loader
/*     */ {
/*  32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader() {
/*  44 */     return getClassLoader(Loader.class, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getThreadContextClassLoader() {
/*  55 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
/*  60 */     ClassLoader threadContextClassLoader = getThreadContextClassLoader();
/*  61 */     ClassLoader loader1 = (class1 == null) ? null : class1.getClassLoader();
/*  62 */     ClassLoader loader2 = (class2 == null) ? null : class2.getClassLoader();
/*     */     
/*  64 */     if (isChild(threadContextClassLoader, loader1)) {
/*  65 */       return isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
/*     */     }
/*  67 */     return isChild(loader1, loader2) ? loader1 : loader2;
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
/*     */   public static URL getResource(String resource, ClassLoader defaultLoader) {
/*     */     try {
/*  92 */       ClassLoader classLoader = getThreadContextClassLoader();
/*  93 */       if (classLoader != null) {
/*  94 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
/*  95 */         URL url = classLoader.getResource(resource);
/*  96 */         if (url != null) {
/*  97 */           return url;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 102 */       classLoader = Loader.class.getClassLoader();
/* 103 */       if (classLoader != null) {
/* 104 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
/* 105 */         URL url = classLoader.getResource(resource);
/* 106 */         if (url != null) {
/* 107 */           return url;
/*     */         }
/*     */       } 
/*     */       
/* 111 */       if (defaultLoader != null) {
/* 112 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
/* 113 */         URL url = defaultLoader.getResource(resource);
/* 114 */         if (url != null) {
/* 115 */           return url;
/*     */         }
/*     */       } 
/* 118 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 122 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
/* 130 */     return ClassLoader.getSystemResource(resource);
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
/*     */   public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
/*     */     try {
/* 154 */       ClassLoader classLoader = getThreadContextClassLoader();
/*     */       
/* 156 */       if (classLoader != null) {
/* 157 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
/* 158 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 159 */         if (is != null) {
/* 160 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 165 */       classLoader = Loader.class.getClassLoader();
/* 166 */       if (classLoader != null) {
/* 167 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
/* 168 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 169 */         if (is != null) {
/* 170 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 175 */       if (defaultLoader != null) {
/* 176 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
/* 177 */         InputStream is = defaultLoader.getResourceAsStream(resource);
/* 178 */         if (is != null) {
/* 179 */           return is;
/*     */         }
/*     */       } 
/* 182 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 186 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
/* 194 */     return ClassLoader.getSystemResourceAsStream(resource);
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
/*     */   private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
/* 206 */     if (loader1 != null && loader2 != null) {
/* 207 */       ClassLoader parent = loader1.getParent();
/* 208 */       while (parent != null && parent != loader2) {
/* 209 */         parent = parent.getParent();
/*     */       }
/*     */       
/* 212 */       return (parent != null);
/*     */     } 
/* 214 */     return (loader1 != null);
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
/*     */   public static Class<?> initializeClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 227 */     return Class.forName(className, true, loader);
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
/*     */   public static Class<?> loadClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 240 */     return (loader != null) ? loader.loadClass(className) : null;
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
/*     */   public static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
/*     */     try {
/* 253 */       return Class.forName(className, true, ClassLoader.getSystemClassLoader());
/* 254 */     } catch (Throwable t) {
/* 255 */       LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", className, t);
/* 256 */       return Class.forName(className);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 277 */     return LoaderUtil.newInstanceOf(className);
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
/*     */   public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 300 */     return (T)LoaderUtil.newCheckedInstanceOf(className, clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClassAvailable(String className) {
/* 310 */     return LoaderUtil.isClassAvailable(className);
/*     */   }
/*     */   
/*     */   public static boolean isJansiAvailable() {
/* 314 */     return isClassAvailable("org.fusesource.jansi.AnsiRenderer");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Loader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */