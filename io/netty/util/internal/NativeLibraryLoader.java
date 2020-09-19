/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeLibraryLoader
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
/*     */   
/*     */   private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
/*     */   
/*     */   private static final File WORKDIR;
/*     */   
/*     */   static {
/*  52 */     String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
/*  53 */     if (workdir != null) {
/*  54 */       File f = new File(workdir);
/*  55 */       f.mkdirs();
/*     */       
/*     */       try {
/*  58 */         f = f.getAbsoluteFile();
/*  59 */       } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/*  63 */       WORKDIR = f;
/*  64 */       logger.debug("-Dio.netty.native.workdir: " + WORKDIR);
/*     */     } else {
/*  66 */       WORKDIR = PlatformDependent.tmpdir();
/*  67 */       logger.debug("-Dio.netty.native.workdir: " + WORKDIR + " (io.netty.tmpdir)");
/*     */     } 
/*     */   }
/*  70 */   private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertyUtil.getBoolean("io.netty.native.deleteLibAfterLoading", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadFirstAvailable(ClassLoader loader, String... names) {
/*  82 */     List<Throwable> suppressed = new ArrayList<Throwable>();
/*  83 */     for (String name : names) {
/*     */       try {
/*  85 */         load(name, loader);
/*     */         return;
/*  87 */       } catch (Throwable t) {
/*  88 */         suppressed.add(t);
/*  89 */         logger.debug("Unable to load the library '{}', trying next name...", name, t);
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     IllegalArgumentException iae = new IllegalArgumentException("Failed to load any of the given libraries: " + Arrays.toString((Object[])names));
/*  94 */     ThrowableUtil.addSuppressedAndClear(iae, suppressed);
/*  95 */     throw iae;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String calculatePackagePrefix() {
/* 104 */     String maybeShaded = NativeLibraryLoader.class.getName();
/*     */     
/* 106 */     String expected = "io!netty!util!internal!NativeLibraryLoader".replace('!', '.');
/* 107 */     if (!maybeShaded.endsWith(expected)) {
/* 108 */       throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", new Object[] { expected, maybeShaded }));
/*     */     }
/*     */ 
/*     */     
/* 112 */     return maybeShaded.substring(0, maybeShaded.length() - expected.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void load(String originalName, ClassLoader loader) {
/* 120 */     String name = calculatePackagePrefix().replace('.', '_') + originalName;
/* 121 */     List<Throwable> suppressed = new ArrayList<Throwable>();
/*     */     
/*     */     try {
/* 124 */       loadLibrary(loader, name, false);
/*     */       return;
/* 126 */     } catch (Throwable ex) {
/* 127 */       URL url; suppressed.add(ex);
/* 128 */       logger.debug("{} cannot be loaded from java.libary.path, now trying export to -Dio.netty.native.workdir: {}", new Object[] { name, WORKDIR, ex });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       String libname = System.mapLibraryName(name);
/* 134 */       String path = "META-INF/native/" + libname;
/*     */       
/* 136 */       InputStream in = null;
/* 137 */       OutputStream out = null;
/* 138 */       File tmpFile = null;
/*     */       
/* 140 */       if (loader == null) {
/* 141 */         url = ClassLoader.getSystemResource(path);
/*     */       } else {
/* 143 */         url = loader.getResource(path);
/*     */       } 
/*     */       try {
/* 146 */         if (url == null) {
/* 147 */           if (PlatformDependent.isOsx()) {
/* 148 */             String fileName = path.endsWith(".jnilib") ? ("META-INF/native/lib" + name + ".dynlib") : ("META-INF/native/lib" + name + ".jnilib");
/*     */             
/* 150 */             if (loader == null) {
/* 151 */               url = ClassLoader.getSystemResource(fileName);
/*     */             } else {
/* 153 */               url = loader.getResource(fileName);
/*     */             } 
/* 155 */             if (url == null) {
/* 156 */               FileNotFoundException fnf = new FileNotFoundException(fileName);
/* 157 */               ThrowableUtil.addSuppressedAndClear(fnf, suppressed);
/* 158 */               throw fnf;
/*     */             } 
/*     */           } else {
/* 161 */             FileNotFoundException fnf = new FileNotFoundException(path);
/* 162 */             ThrowableUtil.addSuppressedAndClear(fnf, suppressed);
/* 163 */             throw fnf;
/*     */           } 
/*     */         }
/*     */         
/* 167 */         int index = libname.lastIndexOf('.');
/* 168 */         String prefix = libname.substring(0, index);
/* 169 */         String suffix = libname.substring(index, libname.length());
/*     */         
/* 171 */         tmpFile = File.createTempFile(prefix, suffix, WORKDIR);
/* 172 */         in = url.openStream();
/* 173 */         out = new FileOutputStream(tmpFile);
/*     */         
/* 175 */         byte[] buffer = new byte[8192];
/*     */         int length;
/* 177 */         while ((length = in.read(buffer)) > 0) {
/* 178 */           out.write(buffer, 0, length);
/*     */         }
/* 180 */         out.flush();
/*     */ 
/*     */ 
/*     */         
/* 184 */         closeQuietly(out);
/* 185 */         out = null;
/*     */         
/* 187 */         loadLibrary(loader, tmpFile.getPath(), true);
/* 188 */       } catch (UnsatisfiedLinkError e) {
/*     */         try {
/* 190 */           if (tmpFile != null && tmpFile.isFile() && tmpFile.canRead() && 
/* 191 */             !NoexecVolumeDetector.canExecuteExecutable(tmpFile)) {
/* 192 */             logger.info("{} exists but cannot be executed even when execute permissions set; check volume for \"noexec\" flag; use -Dio.netty.native.workdir=[path] to set native working directory separately.", tmpFile
/*     */ 
/*     */                 
/* 195 */                 .getPath());
/*     */           }
/* 197 */         } catch (Throwable t) {
/* 198 */           suppressed.add(t);
/* 199 */           logger.debug("Error checking if {} is on a file store mounted with noexec", tmpFile, t);
/*     */         } 
/*     */         
/* 202 */         ThrowableUtil.addSuppressedAndClear(e, suppressed);
/* 203 */         throw e;
/* 204 */       } catch (Exception e) {
/* 205 */         UnsatisfiedLinkError ule = new UnsatisfiedLinkError("could not load a native library: " + name);
/* 206 */         ule.initCause(e);
/* 207 */         ThrowableUtil.addSuppressedAndClear(ule, suppressed);
/* 208 */         throw ule;
/*     */       } finally {
/* 210 */         closeQuietly(in);
/* 211 */         closeQuietly(out);
/*     */ 
/*     */ 
/*     */         
/* 215 */         if (tmpFile != null && (!DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile.delete())) {
/* 216 */           tmpFile.deleteOnExit();
/*     */         }
/*     */       } 
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadLibrary(ClassLoader loader, String name, boolean absolute) {
/* 228 */     Throwable suppressed = null;
/*     */ 
/*     */ 
/*     */     
/* 232 */     try { Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
/* 233 */       loadLibraryByHelper(newHelper, name, absolute);
/* 234 */       logger.debug("Successfully loaded the library {}", name);
/*     */       return; }
/* 236 */     catch (UnsatisfiedLinkError e)
/* 237 */     { suppressed = e;
/* 238 */       logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 243 */       NativeLibraryUtil.loadLibrary(name, absolute);
/* 244 */       logger.debug("Successfully loaded the library {}", name); } catch (Exception e) { suppressed = e; logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e); NativeLibraryUtil.loadLibrary(name, absolute); logger.debug("Successfully loaded the library {}", name); }
/* 245 */     catch (UnsatisfiedLinkError ule)
/* 246 */     { if (suppressed != null) {
/* 247 */         ThrowableUtil.addSuppressed(ule, suppressed);
/*     */       }
/* 249 */       throw ule; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadLibraryByHelper(final Class<?> helper, final String name, final boolean absolute) throws UnsatisfiedLinkError {
/* 255 */     Object ret = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           
/*     */           public Object run()
/*     */           {
/*     */             try {
/* 261 */               Method method = helper.getMethod("loadLibrary", new Class[] { String.class, boolean.class });
/* 262 */               method.setAccessible(true);
/* 263 */               return method.invoke(null, new Object[] { this.val$name, Boolean.valueOf(this.val$absolute) });
/* 264 */             } catch (Exception e) {
/* 265 */               return e;
/*     */             } 
/*     */           }
/*     */         });
/* 269 */     if (ret instanceof Throwable) {
/* 270 */       Throwable t = (Throwable)ret;
/* 271 */       assert !(t instanceof UnsatisfiedLinkError) : t + " should be a wrapper throwable";
/* 272 */       Throwable cause = t.getCause();
/* 273 */       if (cause instanceof UnsatisfiedLinkError) {
/* 274 */         throw (UnsatisfiedLinkError)cause;
/*     */       }
/* 276 */       UnsatisfiedLinkError ule = new UnsatisfiedLinkError(t.getMessage());
/* 277 */       ule.initCause(t);
/* 278 */       throw ule;
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
/*     */   private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper) throws ClassNotFoundException {
/*     */     try {
/* 292 */       return Class.forName(helper.getName(), false, loader);
/* 293 */     } catch (ClassNotFoundException e1) {
/* 294 */       if (loader == null)
/*     */       {
/* 296 */         throw e1;
/*     */       }
/*     */       
/*     */       try {
/* 300 */         final byte[] classBinary = classToByteArray(helper);
/* 301 */         return AccessController.<Class<?>>doPrivileged(new PrivilegedAction<Class<?>>()
/*     */             {
/*     */               
/*     */               public Class<?> run()
/*     */               {
/*     */                 try {
/* 307 */                   Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
/*     */                   
/* 309 */                   defineClass.setAccessible(true);
/* 310 */                   return (Class)defineClass.invoke(loader, new Object[] { this.val$helper.getName(), this.val$classBinary, Integer.valueOf(0), 
/* 311 */                         Integer.valueOf(this.val$classBinary.length) });
/* 312 */                 } catch (Exception e) {
/* 313 */                   throw new IllegalStateException("Define class failed!", e);
/*     */                 } 
/*     */               }
/*     */             });
/* 317 */       } catch (ClassNotFoundException e2) {
/* 318 */         ThrowableUtil.addSuppressed(e2, e1);
/* 319 */         throw e2;
/* 320 */       } catch (RuntimeException e2) {
/* 321 */         ThrowableUtil.addSuppressed(e2, e1);
/* 322 */         throw e2;
/* 323 */       } catch (Error e2) {
/* 324 */         ThrowableUtil.addSuppressed(e2, e1);
/* 325 */         throw e2;
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
/*     */   private static byte[] classToByteArray(Class<?> clazz) throws ClassNotFoundException {
/* 337 */     String fileName = clazz.getName();
/* 338 */     int lastDot = fileName.lastIndexOf('.');
/* 339 */     if (lastDot > 0) {
/* 340 */       fileName = fileName.substring(lastDot + 1);
/*     */     }
/* 342 */     URL classUrl = clazz.getResource(fileName + ".class");
/* 343 */     if (classUrl == null) {
/* 344 */       throw new ClassNotFoundException(clazz.getName());
/*     */     }
/* 346 */     byte[] buf = new byte[1024];
/* 347 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 348 */     InputStream in = null;
/*     */     try {
/* 350 */       in = classUrl.openStream(); int r;
/* 351 */       while ((r = in.read(buf)) != -1) {
/* 352 */         out.write(buf, 0, r);
/*     */       }
/* 354 */       return out.toByteArray();
/* 355 */     } catch (IOException ex) {
/* 356 */       throw new ClassNotFoundException(clazz.getName(), ex);
/*     */     } finally {
/* 358 */       closeQuietly(in);
/* 359 */       closeQuietly(out);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeQuietly(Closeable c) {
/* 364 */     if (c != null) {
/*     */       try {
/* 366 */         c.close();
/* 367 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoexecVolumeDetector
/*     */   {
/*     */     private static boolean canExecuteExecutable(File file) throws IOException {
/* 380 */       if (PlatformDependent.javaVersion() < 7)
/*     */       {
/*     */         
/* 383 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 387 */       if (file.canExecute()) {
/* 388 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 398 */       Set<PosixFilePermission> existingFilePermissions = Files.getPosixFilePermissions(file.toPath(), new java.nio.file.LinkOption[0]);
/*     */       
/* 400 */       Set<PosixFilePermission> executePermissions = EnumSet.of(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_EXECUTE);
/*     */ 
/*     */       
/* 403 */       if (existingFilePermissions.containsAll(executePermissions)) {
/* 404 */         return false;
/*     */       }
/*     */       
/* 407 */       Set<PosixFilePermission> newPermissions = EnumSet.copyOf(existingFilePermissions);
/* 408 */       newPermissions.addAll(executePermissions);
/* 409 */       Files.setPosixFilePermissions(file.toPath(), newPermissions);
/* 410 */       return file.canExecute();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\NativeLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */