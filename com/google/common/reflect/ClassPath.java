/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.io.ByteSource;
/*     */ import com.google.common.io.CharSource;
/*     */ import com.google.common.io.Resources;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Logger;
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
/*     */ @Beta
/*     */ public final class ClassPath
/*     */ {
/*  63 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */   
/*  65 */   private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>()
/*     */     {
/*     */       public boolean apply(ClassPath.ClassInfo info)
/*     */       {
/*  69 */         return (info.className.indexOf('$') == -1);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources) {
/*  82 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader) throws IOException {
/*  96 */     DefaultScanner scanner = new DefaultScanner();
/*  97 */     scanner.scan(classloader);
/*  98 */     return new ClassPath(scanner.getResources());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ResourceInfo> getResources() {
/* 106 */     return this.resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getAllClasses() {
/* 115 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses() {
/* 120 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
/* 125 */     Preconditions.checkNotNull(packageName);
/* 126 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 127 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 128 */       if (classInfo.getPackageName().equals(packageName)) {
/* 129 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 132 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
/* 140 */     Preconditions.checkNotNull(packageName);
/* 141 */     String packagePrefix = packageName + '.';
/* 142 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 143 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 144 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 145 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 148 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final String resourceName;
/*     */ 
/*     */     
/*     */     final ClassLoader loader;
/*     */ 
/*     */ 
/*     */     
/*     */     static ResourceInfo of(String resourceName, ClassLoader loader) {
/* 164 */       if (resourceName.endsWith(".class")) {
/* 165 */         return new ClassPath.ClassInfo(resourceName, loader);
/*     */       }
/* 167 */       return new ResourceInfo(resourceName, loader);
/*     */     }
/*     */ 
/*     */     
/*     */     ResourceInfo(String resourceName, ClassLoader loader) {
/* 172 */       this.resourceName = (String)Preconditions.checkNotNull(resourceName);
/* 173 */       this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
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
/*     */     public final URL url() {
/* 185 */       URL url = this.loader.getResource(this.resourceName);
/* 186 */       if (url == null) {
/* 187 */         throw new NoSuchElementException(this.resourceName);
/*     */       }
/* 189 */       return url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ByteSource asByteSource() {
/* 200 */       return Resources.asByteSource(url());
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
/*     */     public final CharSource asCharSource(Charset charset) {
/* 212 */       return Resources.asCharSource(url(), charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getResourceName() {
/* 217 */       return this.resourceName;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 222 */       return this.resourceName.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 227 */       if (obj instanceof ResourceInfo) {
/* 228 */         ResourceInfo that = (ResourceInfo)obj;
/* 229 */         return (this.resourceName.equals(that.resourceName) && this.loader == that.loader);
/*     */       } 
/* 231 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 237 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class ClassInfo
/*     */     extends ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     ClassInfo(String resourceName, ClassLoader loader) {
/* 251 */       super(resourceName, loader);
/* 252 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPackageName() {
/* 262 */       return Reflection.getPackageName(this.className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSimpleName() {
/* 272 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 273 */       if (lastDollarSign != -1) {
/* 274 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */ 
/*     */         
/* 277 */         return CharMatcher.digit().trimLeadingFrom(innerClassName);
/*     */       } 
/* 279 */       String packageName = getPackageName();
/* 280 */       if (packageName.isEmpty()) {
/* 281 */         return this.className;
/*     */       }
/*     */ 
/*     */       
/* 285 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 295 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> load() {
/*     */       try {
/* 306 */         return this.loader.loadClass(this.className);
/* 307 */       } catch (ClassNotFoundException e) {
/*     */         
/* 309 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 315 */       return this.className;
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
/*     */   static abstract class Scanner
/*     */   {
/* 328 */     private final Set<File> scannedUris = Sets.newHashSet();
/*     */     
/*     */     public final void scan(ClassLoader classloader) throws IOException {
/* 331 */       for (UnmodifiableIterator<Map.Entry<File, ClassLoader>> unmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<File, ClassLoader> entry = unmodifiableIterator.next();
/* 332 */         scan(entry.getKey(), entry.getValue()); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract void scanDirectory(ClassLoader param1ClassLoader, File param1File) throws IOException;
/*     */ 
/*     */     
/*     */     protected abstract void scanJarFile(ClassLoader param1ClassLoader, JarFile param1JarFile) throws IOException;
/*     */     
/*     */     @VisibleForTesting
/*     */     final void scan(File file, ClassLoader classloader) throws IOException {
/* 344 */       if (this.scannedUris.add(file.getCanonicalFile())) {
/* 345 */         scanFrom(file, classloader);
/*     */       }
/*     */     }
/*     */     
/*     */     private void scanFrom(File file, ClassLoader classloader) throws IOException {
/*     */       try {
/* 351 */         if (!file.exists()) {
/*     */           return;
/*     */         }
/* 354 */       } catch (SecurityException e) {
/* 355 */         ClassPath.logger.warning("Cannot access " + file + ": " + e);
/*     */         
/*     */         return;
/*     */       } 
/* 359 */       if (file.isDirectory()) {
/* 360 */         scanDirectory(classloader, file);
/*     */       } else {
/* 362 */         scanJar(file, classloader);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void scanJar(File file, ClassLoader classloader) throws IOException {
/*     */       JarFile jarFile;
/*     */       try {
/* 369 */         jarFile = new JarFile(file);
/* 370 */       } catch (IOException e) {
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 375 */         for (UnmodifiableIterator<File> unmodifiableIterator = getClassPathFromManifest(file, jarFile.getManifest()).iterator(); unmodifiableIterator.hasNext(); ) { File path = unmodifiableIterator.next();
/* 376 */           scan(path, classloader); }
/*     */         
/* 378 */         scanJarFile(classloader, jarFile);
/*     */       } finally {
/*     */         try {
/* 381 */           jarFile.close();
/* 382 */         } catch (IOException iOException) {}
/*     */       } 
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
/*     */     @VisibleForTesting
/*     */     static ImmutableSet<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
/* 396 */       if (manifest == null) {
/* 397 */         return ImmutableSet.of();
/*     */       }
/* 399 */       ImmutableSet.Builder<File> builder = ImmutableSet.builder();
/*     */       
/* 401 */       String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/* 402 */       if (classpathAttribute != null) {
/* 403 */         for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
/*     */           URL url;
/*     */           try {
/* 406 */             url = getClassPathEntry(jarFile, path);
/* 407 */           } catch (MalformedURLException e) {
/*     */             
/* 409 */             ClassPath.logger.warning("Invalid Class-Path entry: " + path);
/*     */             continue;
/*     */           } 
/* 412 */           if (url.getProtocol().equals("file")) {
/* 413 */             builder.add(new File(url.getFile()));
/*     */           }
/*     */         } 
/*     */       }
/* 417 */       return builder.build();
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 422 */       LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */       
/* 424 */       ClassLoader parent = classloader.getParent();
/* 425 */       if (parent != null) {
/* 426 */         entries.putAll((Map<? extends File, ? extends ClassLoader>)getClassPathEntries(parent));
/*     */       }
/* 428 */       if (classloader instanceof URLClassLoader) {
/* 429 */         URLClassLoader urlClassLoader = (URLClassLoader)classloader;
/* 430 */         for (URL entry : urlClassLoader.getURLs()) {
/* 431 */           if (entry.getProtocol().equals("file")) {
/* 432 */             File file = new File(entry.getFile());
/* 433 */             if (!entries.containsKey(file)) {
/* 434 */               entries.put(file, classloader);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 439 */       return ImmutableMap.copyOf(entries);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
/* 450 */       return new URL(jarFile.toURI().toURL(), path);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class DefaultScanner
/*     */     extends Scanner {
/* 457 */     private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();
/*     */     
/*     */     ImmutableSet<ClassPath.ResourceInfo> getResources() {
/* 460 */       ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
/* 461 */       for (Map.Entry<ClassLoader, String> entry : (Iterable<Map.Entry<ClassLoader, String>>)this.resources.entries()) {
/* 462 */         builder.add(ClassPath.ResourceInfo.of(entry.getValue(), entry.getKey()));
/*     */       }
/* 464 */       return builder.build();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void scanJarFile(ClassLoader classloader, JarFile file) {
/* 469 */       Enumeration<JarEntry> entries = file.entries();
/* 470 */       while (entries.hasMoreElements()) {
/* 471 */         JarEntry entry = entries.nextElement();
/* 472 */         if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */           continue;
/*     */         }
/* 475 */         this.resources.get(classloader).add(entry.getName());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
/* 481 */       scanDirectory(directory, classloader, "");
/*     */     }
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix) throws IOException {
/* 486 */       File[] files = directory.listFiles();
/* 487 */       if (files == null) {
/* 488 */         ClassPath.logger.warning("Cannot read directory " + directory);
/*     */         
/*     */         return;
/*     */       } 
/* 492 */       for (File f : files) {
/* 493 */         String name = f.getName();
/* 494 */         if (f.isDirectory()) {
/* 495 */           scanDirectory(f, classloader, packagePrefix + name + "/");
/*     */         } else {
/* 497 */           String resourceName = packagePrefix + name;
/* 498 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 499 */             this.resources.get(classloader).add(resourceName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 508 */     int classNameEnd = filename.length() - ".class".length();
/* 509 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */