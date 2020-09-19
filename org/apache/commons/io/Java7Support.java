/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Java7Support
/*     */ {
/*     */   private static final boolean IS_JAVA7;
/*     */   private static Method isSymbolicLink;
/*     */   private static Method delete;
/*     */   private static Method toPath;
/*     */   private static Method exists;
/*     */   private static Method toFile;
/*     */   private static Method readSymlink;
/*     */   private static Method createSymlink;
/*     */   private static Object emptyLinkOpts;
/*     */   private static Object emptyFileAttributes;
/*     */   
/*     */   static {
/*  56 */     boolean isJava7x = true;
/*     */     try {
/*  58 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  59 */       Class<?> files = cl.loadClass("java.nio.file.Files");
/*  60 */       Class<?> path = cl.loadClass("java.nio.file.Path");
/*  61 */       Class<?> fa = cl.loadClass("java.nio.file.attribute.FileAttribute");
/*  62 */       Class<?> linkOption = cl.loadClass("java.nio.file.LinkOption");
/*  63 */       isSymbolicLink = files.getMethod("isSymbolicLink", new Class[] { path });
/*  64 */       delete = files.getMethod("delete", new Class[] { path });
/*  65 */       readSymlink = files.getMethod("readSymbolicLink", new Class[] { path });
/*     */       
/*  67 */       emptyFileAttributes = Array.newInstance(fa, 0);
/*  68 */       createSymlink = files.getMethod("createSymbolicLink", new Class[] { path, path, emptyFileAttributes.getClass() });
/*  69 */       emptyLinkOpts = Array.newInstance(linkOption, 0);
/*  70 */       exists = files.getMethod("exists", new Class[] { path, emptyLinkOpts.getClass() });
/*  71 */       toPath = File.class.getMethod("toPath", new Class[0]);
/*  72 */       toFile = path.getMethod("toFile", new Class[0]);
/*  73 */     } catch (ClassNotFoundException e) {
/*  74 */       isJava7x = false;
/*  75 */     } catch (NoSuchMethodException e) {
/*  76 */       isJava7x = false;
/*     */     } 
/*  78 */     IS_JAVA7 = isJava7x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSymLink(File file) {
/*     */     try {
/*  88 */       Object path = toPath.invoke(file, new Object[0]);
/*  89 */       Boolean result = (Boolean)isSymbolicLink.invoke(null, new Object[] { path });
/*  90 */       return result.booleanValue();
/*  91 */     } catch (IllegalAccessException e) {
/*  92 */       throw new RuntimeException(e);
/*  93 */     } catch (InvocationTargetException e) {
/*  94 */       throw new RuntimeException(e);
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
/*     */   public static File readSymbolicLink(File symlink) throws IOException {
/*     */     try {
/* 108 */       Object path = toPath.invoke(symlink, new Object[0]);
/* 109 */       Object resultPath = readSymlink.invoke(null, new Object[] { path });
/* 110 */       return (File)toFile.invoke(resultPath, new Object[0]);
/* 111 */     } catch (IllegalAccessException e) {
/* 112 */       throw new RuntimeException(e);
/* 113 */     } catch (InvocationTargetException e) {
/* 114 */       throw new RuntimeException(e);
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
/*     */   private static boolean exists(File file) throws IOException {
/*     */     try {
/* 128 */       Object path = toPath.invoke(file, new Object[0]);
/* 129 */       Boolean result = (Boolean)exists.invoke(null, new Object[] { path, emptyLinkOpts });
/* 130 */       return result.booleanValue();
/* 131 */     } catch (IllegalAccessException e) {
/* 132 */       throw new RuntimeException(e);
/* 133 */     } catch (InvocationTargetException e) {
/* 134 */       throw (RuntimeException)e.getTargetException();
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
/*     */   public static File createSymbolicLink(File symlink, File target) throws IOException {
/*     */     try {
/* 149 */       if (!exists(symlink)) {
/* 150 */         Object link = toPath.invoke(symlink, new Object[0]);
/* 151 */         Object path = createSymlink.invoke(null, new Object[] { link, toPath.invoke(target, new Object[0]), emptyFileAttributes });
/* 152 */         return (File)toFile.invoke(path, new Object[0]);
/*     */       } 
/* 154 */       return symlink;
/* 155 */     } catch (IllegalAccessException e) {
/* 156 */       throw new RuntimeException(e);
/* 157 */     } catch (InvocationTargetException e) {
/* 158 */       Throwable targetException = e.getTargetException();
/* 159 */       throw (IOException)targetException;
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
/*     */   public static void delete(File file) throws IOException {
/*     */     try {
/* 173 */       Object path = toPath.invoke(file, new Object[0]);
/* 174 */       delete.invoke(null, new Object[] { path });
/* 175 */     } catch (IllegalAccessException e) {
/* 176 */       throw new RuntimeException(e);
/* 177 */     } catch (InvocationTargetException e) {
/* 178 */       throw (IOException)e.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAtLeastJava7() {
/* 187 */     return IS_JAVA7;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\Java7Support.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */