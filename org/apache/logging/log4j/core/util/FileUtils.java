/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
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
/*     */ public final class FileUtils
/*     */ {
/*     */   private static final String PROTOCOL_FILE = "file";
/*     */   private static final String JBOSS_FILE = "vfsfile";
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File fileFromUri(URI uri) {
/*  55 */     if (uri == null || (uri.getScheme() != null && !"file".equals(uri.getScheme()) && !"vfsfile".equals(uri.getScheme())))
/*     */     {
/*     */       
/*  58 */       return null;
/*     */     }
/*  60 */     if (uri.getScheme() == null) {
/*  61 */       File file = new File(uri.toString());
/*  62 */       if (file.exists()) {
/*  63 */         return file;
/*     */       }
/*     */       try {
/*  66 */         String path = uri.getPath();
/*  67 */         file = new File(path);
/*  68 */         if (file.exists()) {
/*  69 */           return file;
/*     */         }
/*  71 */         uri = (new File(path)).toURI();
/*  72 */       } catch (Exception ex) {
/*  73 */         LOGGER.warn("Invalid URI {}", uri);
/*  74 */         return null;
/*     */       } 
/*     */     } 
/*  77 */     String charsetName = StandardCharsets.UTF_8.name();
/*     */     try {
/*  79 */       String fileName = uri.toURL().getFile();
/*  80 */       if ((new File(fileName)).exists()) {
/*  81 */         return new File(fileName);
/*     */       }
/*  83 */       fileName = URLDecoder.decode(fileName, charsetName);
/*  84 */       return new File(fileName);
/*  85 */     } catch (MalformedURLException ex) {
/*  86 */       LOGGER.warn("Invalid URL {}", uri, ex);
/*  87 */     } catch (UnsupportedEncodingException uee) {
/*  88 */       LOGGER.warn("Invalid encoding: {}", charsetName, uee);
/*     */     } 
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isFile(URL url) {
/*  94 */     return (url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile")));
/*     */   }
/*     */   
/*     */   public static String getFileExtension(File file) {
/*  98 */     String fileName = file.getName();
/*  99 */     if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
/* 100 */       return fileName.substring(fileName.lastIndexOf(".") + 1);
/*     */     }
/* 102 */     return null;
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
/*     */   public static void mkdir(File dir, boolean createDirectoryIfNotExisting) throws IOException {
/* 114 */     if (!dir.exists()) {
/* 115 */       if (!createDirectoryIfNotExisting) {
/* 116 */         throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
/*     */       }
/* 118 */       if (!dir.mkdirs()) {
/* 119 */         throw new IOException("Could not create directory " + dir.getAbsolutePath());
/*     */       }
/*     */     } 
/* 122 */     if (!dir.isDirectory()) {
/* 123 */       throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeParentDirs(File file) throws IOException {
/* 134 */     File parent = ((File)Objects.<File>requireNonNull(file, "file")).getCanonicalFile().getParentFile();
/* 135 */     if (parent != null)
/* 136 */       mkdir(parent, true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\FileUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */