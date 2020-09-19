/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
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
/*     */ public final class LoaderUtil
/*     */ {
/*     */   public static final String IGNORE_TCCL_PROPERTY = "log4j.ignoreTCL";
/*  46 */   private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
/*     */ 
/*     */   
/*     */   private static Boolean ignoreTCCL;
/*     */ 
/*     */   
/*     */   private static final boolean GET_CLASS_LOADER_DISABLED;
/*     */   
/*  54 */   private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter();
/*     */   
/*     */   static {
/*  57 */     if (SECURITY_MANAGER != null) {
/*     */       boolean getClassLoaderDisabled;
/*     */       try {
/*  60 */         SECURITY_MANAGER.checkPermission(new RuntimePermission("getClassLoader"));
/*  61 */         getClassLoaderDisabled = false;
/*  62 */       } catch (SecurityException ignored) {
/*  63 */         getClassLoaderDisabled = true;
/*     */       } 
/*  65 */       GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
/*     */     } else {
/*  67 */       GET_CLASS_LOADER_DISABLED = false;
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
/*     */   public static ClassLoader getThreadContextClassLoader() {
/*  83 */     if (GET_CLASS_LOADER_DISABLED)
/*     */     {
/*     */       
/*  86 */       return LoaderUtil.class.getClassLoader();
/*     */     }
/*  88 */     return (SECURITY_MANAGER == null) ? TCCL_GETTER.run() : AccessController.<ClassLoader>doPrivileged(TCCL_GETTER);
/*     */   }
/*     */   
/*     */   private static class ThreadContextClassLoaderGetter
/*     */     implements PrivilegedAction<ClassLoader>
/*     */   {
/*     */     private ThreadContextClassLoaderGetter() {}
/*     */     
/*     */     public ClassLoader run() {
/*  97 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  98 */       if (cl != null) {
/*  99 */         return cl;
/*     */       }
/* 101 */       ClassLoader ccl = LoaderUtil.class.getClassLoader();
/* 102 */       return (ccl == null && !LoaderUtil.GET_CLASS_LOADER_DISABLED) ? ClassLoader.getSystemClassLoader() : ccl;
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
/*     */   public static boolean isClassAvailable(String className) {
/*     */     try {
/* 115 */       Class<?> clazz = loadClass(className);
/* 116 */       return (clazz != null);
/* 117 */     } catch (ClassNotFoundException e) {
/* 118 */       return false;
/* 119 */     } catch (Throwable e) {
/* 120 */       LowLevelLogUtil.logException("Unknown error checking for existence of class: " + className, e);
/* 121 */       return false;
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
/*     */   public static Class<?> loadClass(String className) throws ClassNotFoundException {
/* 135 */     if (isIgnoreTccl()) {
/* 136 */       return Class.forName(className);
/*     */     }
/*     */     try {
/* 139 */       return getThreadContextClassLoader().loadClass(className);
/* 140 */     } catch (Throwable ignored) {
/* 141 */       return Class.forName(className);
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
/*     */   public static <T> T newInstanceOf(Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
/*     */     try {
/* 158 */       return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 159 */     } catch (NoSuchMethodException ignored) {
/*     */       
/* 161 */       return clazz.newInstance();
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
/*     */   public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 180 */     return newInstanceOf((Class)loadClass(className));
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
/*     */   public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
/* 201 */     return clazz.cast(newInstanceOf(className));
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
/*     */   public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
/* 222 */     String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
/* 223 */     if (className == null) {
/* 224 */       return null;
/*     */     }
/* 226 */     return newCheckedInstanceOf(className, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isIgnoreTccl() {
/* 231 */     if (ignoreTCCL == null) {
/* 232 */       String ignoreTccl = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
/* 233 */       ignoreTCCL = Boolean.valueOf((ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim())));
/*     */     } 
/* 235 */     return ignoreTCCL.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<URL> findResources(String resource) {
/* 246 */     Collection<UrlResource> urlResources = findUrlResources(resource);
/* 247 */     Collection<URL> resources = new LinkedHashSet<>(urlResources.size());
/* 248 */     for (UrlResource urlResource : urlResources) {
/* 249 */       resources.add(urlResource.getUrl());
/*     */     }
/* 251 */     return resources;
/*     */   }
/*     */   
/*     */   static Collection<UrlResource> findUrlResources(String resource) {
/* 255 */     ClassLoader[] candidates = { getThreadContextClassLoader(), LoaderUtil.class.getClassLoader(), GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader() };
/*     */     
/* 257 */     Collection<UrlResource> resources = new LinkedHashSet<>();
/* 258 */     for (ClassLoader cl : candidates) {
/* 259 */       if (cl != null) {
/*     */         try {
/* 261 */           Enumeration<URL> resourceEnum = cl.getResources(resource);
/* 262 */           while (resourceEnum.hasMoreElements()) {
/* 263 */             resources.add(new UrlResource(cl, resourceEnum.nextElement()));
/*     */           }
/* 265 */         } catch (IOException e) {
/* 266 */           LowLevelLogUtil.logException(e);
/*     */         } 
/*     */       }
/*     */     } 
/* 270 */     return resources;
/*     */   }
/*     */ 
/*     */   
/*     */   static class UrlResource
/*     */   {
/*     */     private final ClassLoader classLoader;
/*     */     
/*     */     private final URL url;
/*     */     
/*     */     UrlResource(ClassLoader classLoader, URL url) {
/* 281 */       this.classLoader = classLoader;
/* 282 */       this.url = url;
/*     */     }
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 286 */       return this.classLoader;
/*     */     }
/*     */     
/*     */     public URL getUrl() {
/* 290 */       return this.url;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 295 */       if (this == o) {
/* 296 */         return true;
/*     */       }
/* 298 */       if (o == null || getClass() != o.getClass()) {
/* 299 */         return false;
/*     */       }
/*     */       
/* 302 */       UrlResource that = (UrlResource)o;
/*     */       
/* 304 */       if ((this.classLoader != null) ? !this.classLoader.equals(that.classLoader) : (that.classLoader != null)) {
/* 305 */         return false;
/*     */       }
/* 307 */       if ((this.url != null) ? !this.url.equals(that.url) : (that.url != null)) {
/* 308 */         return false;
/*     */       }
/*     */       
/* 311 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 316 */       return Objects.hashCode(this.classLoader) + Objects.hashCode(this.url);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\LoaderUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */