/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarInputStream;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.FrameworkUtil;
/*     */ import org.osgi.framework.wiring.BundleWiring;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResolverUtil
/*     */ {
/*  83 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String VFSZIP = "vfszip";
/*     */   
/*     */   private static final String VFS = "vfs";
/*     */   
/*     */   private static final String BUNDLE_RESOURCE = "bundleresource";
/*     */   
/*  92 */   private final Set<Class<?>> classMatches = new HashSet<>();
/*     */ 
/*     */   
/*  95 */   private final Set<URI> resourceMatches = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader classloader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Class<?>> getClasses() {
/* 110 */     return this.classMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<URI> getResources() {
/* 119 */     return this.resourceMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 129 */     return (this.classloader != null) ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader aClassloader) {
/* 140 */     this.classloader = aClassloader;
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
/*     */   public void find(Test test, String... packageNames) {
/* 153 */     if (packageNames == null) {
/*     */       return;
/*     */     }
/*     */     
/* 157 */     for (String pkg : packageNames) {
/* 158 */       findInPackage(test, pkg);
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
/*     */   public void findInPackage(Test test, String packageName) {
/*     */     Enumeration<URL> urls;
/* 173 */     packageName = packageName.replace('.', '/');
/* 174 */     ClassLoader loader = getClassLoader();
/*     */ 
/*     */     
/*     */     try {
/* 178 */       urls = loader.getResources(packageName);
/* 179 */     } catch (IOException ioe) {
/* 180 */       LOGGER.warn("Could not read package: " + packageName, ioe);
/*     */       
/*     */       return;
/*     */     } 
/* 184 */     while (urls.hasMoreElements()) {
/*     */       try {
/* 186 */         URL url = urls.nextElement();
/* 187 */         String urlPath = extractPath(url);
/*     */         
/* 189 */         LOGGER.info("Scanning for classes in [" + urlPath + "] matching criteria: " + test);
/*     */         
/* 191 */         if ("vfszip".equals(url.getProtocol())) {
/* 192 */           String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
/* 193 */           URL newURL = new URL(url.getProtocol(), url.getHost(), path);
/*     */           
/* 195 */           JarInputStream stream = new JarInputStream(newURL.openStream());
/*     */           try {
/* 197 */             loadImplementationsInJar(test, packageName, path, stream);
/*     */           } finally {
/* 199 */             close(stream, newURL);
/*     */           }  continue;
/* 201 */         }  if ("vfs".equals(url.getProtocol())) {
/* 202 */           String containerPath = urlPath.substring(1, urlPath.length() - packageName.length() - 2);
/*     */           
/* 204 */           File containerFile = new File(containerPath);
/* 205 */           if (containerFile.isDirectory()) {
/* 206 */             loadImplementationsInDirectory(test, packageName, new File(containerFile, packageName)); continue;
/*     */           } 
/* 208 */           loadImplementationsInJar(test, packageName, containerFile); continue;
/*     */         } 
/* 210 */         if ("bundleresource".equals(url.getProtocol())) {
/* 211 */           loadImplementationsInBundle(test, packageName); continue;
/*     */         } 
/* 213 */         File file = new File(urlPath);
/* 214 */         if (file.isDirectory()) {
/* 215 */           loadImplementationsInDirectory(test, packageName, file); continue;
/*     */         } 
/* 217 */         loadImplementationsInJar(test, packageName, file);
/*     */       
/*     */       }
/* 220 */       catch (IOException|URISyntaxException ioe) {
/* 221 */         LOGGER.warn("Could not read entries", ioe);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String extractPath(URL url) throws UnsupportedEncodingException, URISyntaxException {
/* 227 */     String urlPath = url.getPath();
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (urlPath.startsWith("jar:")) {
/* 232 */       urlPath = urlPath.substring(4);
/*     */     }
/*     */     
/* 235 */     if (urlPath.startsWith("file:")) {
/* 236 */       urlPath = urlPath.substring(5);
/*     */     }
/*     */     
/* 239 */     int bangIndex = urlPath.indexOf('!');
/* 240 */     if (bangIndex > 0) {
/* 241 */       urlPath = urlPath.substring(0, bangIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 246 */     String protocol = url.getProtocol();
/* 247 */     List<String> neverDecode = Arrays.asList(new String[] { "vfs", "vfszip", "bundleresource" });
/* 248 */     if (neverDecode.contains(protocol)) {
/* 249 */       return urlPath;
/*     */     }
/* 251 */     String cleanPath = (new URI(urlPath)).getPath();
/* 252 */     if ((new File(cleanPath)).exists())
/*     */     {
/* 254 */       return cleanPath;
/*     */     }
/* 256 */     return URLDecoder.decode(urlPath, StandardCharsets.UTF_8.name());
/*     */   }
/*     */   
/*     */   private void loadImplementationsInBundle(Test test, String packageName) {
/* 260 */     BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
/* 261 */     Collection<String> list = wiring.listResources(packageName, "*.class", 1);
/*     */     
/* 263 */     for (String name : list) {
/* 264 */       addIfMatching(test, name);
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
/*     */   private void loadImplementationsInDirectory(Test test, String parent, File location) {
/* 284 */     File[] files = location.listFiles();
/* 285 */     if (files == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 290 */     for (File file : files) {
/* 291 */       StringBuilder builder = new StringBuilder();
/* 292 */       builder.append(parent).append('/').append(file.getName());
/* 293 */       String packageOrClass = (parent == null) ? file.getName() : builder.toString();
/*     */       
/* 295 */       if (file.isDirectory()) {
/* 296 */         loadImplementationsInDirectory(test, packageOrClass, file);
/* 297 */       } else if (isTestApplicable(test, file.getName())) {
/* 298 */         addIfMatching(test, packageOrClass);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isTestApplicable(Test test, String path) {
/* 304 */     return (test.doesMatchResource() || (path.endsWith(".class") && test.doesMatchClass()));
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
/*     */   private void loadImplementationsInJar(Test test, String parent, File jarFile) {
/* 320 */     JarInputStream jarStream = null;
/*     */     try {
/* 322 */       jarStream = new JarInputStream(new FileInputStream(jarFile));
/* 323 */       loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
/* 324 */     } catch (FileNotFoundException ex) {
/* 325 */       LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " file not found", ex);
/*     */     }
/* 327 */     catch (IOException ioe) {
/* 328 */       LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " due to an IOException", ioe);
/*     */     } finally {
/*     */       
/* 331 */       close(jarStream, jarFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void close(JarInputStream jarStream, Object source) {
/* 340 */     if (jarStream != null) {
/*     */       try {
/* 342 */         jarStream.close();
/* 343 */       } catch (IOException e) {
/* 344 */         LOGGER.error("Error closing JAR file stream for {}", source, e);
/*     */       } 
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
/*     */   private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream) {
/*     */     try {
/*     */       JarEntry entry;
/* 366 */       while ((entry = stream.getNextJarEntry()) != null) {
/* 367 */         String name = entry.getName();
/* 368 */         if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name)) {
/* 369 */           addIfMatching(test, name);
/*     */         }
/*     */       } 
/* 372 */     } catch (IOException ioe) {
/* 373 */       LOGGER.error("Could not search jar file '" + path + "' for classes matching criteria: " + test + " due to an IOException", ioe);
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
/*     */   protected void addIfMatching(Test test, String fqn) {
/*     */     try {
/* 389 */       ClassLoader loader = getClassLoader();
/* 390 */       if (test.doesMatchClass()) {
/* 391 */         String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
/* 392 */         if (LOGGER.isDebugEnabled()) {
/* 393 */           LOGGER.debug("Checking to see if class " + externalName + " matches criteria [" + test + ']');
/*     */         }
/*     */         
/* 396 */         Class<?> type = loader.loadClass(externalName);
/* 397 */         if (test.matches(type)) {
/* 398 */           this.classMatches.add(type);
/*     */         }
/*     */       } 
/* 401 */       if (test.doesMatchResource()) {
/* 402 */         URL url = loader.getResource(fqn);
/* 403 */         if (url == null) {
/* 404 */           url = loader.getResource(fqn.substring(1));
/*     */         }
/* 406 */         if (url != null && test.matches(url.toURI())) {
/* 407 */           this.resourceMatches.add(url.toURI());
/*     */         }
/*     */       } 
/* 410 */     } catch (Throwable t) {
/* 411 */       LOGGER.warn("Could not examine class '" + fqn, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Test {
/*     */     boolean matches(Class<?> param1Class);
/*     */     
/*     */     boolean matches(URI param1URI);
/*     */     
/*     */     boolean doesMatchClass();
/*     */     
/*     */     boolean doesMatchResource();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugin\\util\ResolverUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */