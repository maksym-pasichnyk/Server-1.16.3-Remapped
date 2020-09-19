/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class FilePackResources
/*     */   extends AbstractPackResources {
/*  23 */   public static final Splitter SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
/*     */   private ZipFile zipFile;
/*     */   
/*     */   public FilePackResources(File debug1) {
/*  27 */     super(debug1);
/*     */   }
/*     */   
/*     */   private ZipFile getOrCreateZipFile() throws IOException {
/*  31 */     if (this.zipFile == null) {
/*  32 */       this.zipFile = new ZipFile(this.file);
/*     */     }
/*     */     
/*  35 */     return this.zipFile;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getResource(String debug1) throws IOException {
/*  40 */     ZipFile debug2 = getOrCreateZipFile();
/*  41 */     ZipEntry debug3 = debug2.getEntry(debug1);
/*     */     
/*  43 */     if (debug3 == null) {
/*  44 */       throw new ResourcePackFileNotFoundException(this.file, debug1);
/*     */     }
/*     */     
/*  47 */     return debug2.getInputStream(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasResource(String debug1) {
/*     */     try {
/*  53 */       return (getOrCreateZipFile().getEntry(debug1) != null);
/*  54 */     } catch (IOException debug2) {
/*  55 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType debug1) {
/*     */     ZipFile debug2;
/*     */     try {
/*  63 */       debug2 = getOrCreateZipFile();
/*  64 */     } catch (IOException iOException) {
/*  65 */       return Collections.emptySet();
/*     */     } 
/*     */     
/*  68 */     Enumeration<? extends ZipEntry> debug3 = debug2.entries();
/*     */     
/*  70 */     Set<String> debug4 = Sets.newHashSet();
/*     */     
/*  72 */     while (debug3.hasMoreElements()) {
/*  73 */       ZipEntry debug5 = debug3.nextElement();
/*     */       
/*  75 */       String debug6 = debug5.getName();
/*  76 */       if (debug6.startsWith(debug1.getDirectory() + "/")) {
/*  77 */         List<String> debug7 = Lists.newArrayList(SPLITTER.split(debug6));
/*  78 */         if (debug7.size() > 1) {
/*  79 */           String debug8 = debug7.get(1);
/*  80 */           if (debug8.equals(debug8.toLowerCase(Locale.ROOT))) {
/*  81 */             debug4.add(debug8); continue;
/*     */           } 
/*  83 */           logWarning(debug8);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  89 */     return debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  95 */     close();
/*  96 */     super.finalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 101 */     if (this.zipFile != null) {
/* 102 */       IOUtils.closeQuietly(this.zipFile);
/* 103 */       this.zipFile = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> getResources(PackType debug1, String debug2, String debug3, int debug4, Predicate<String> debug5) {
/*     */     ZipFile debug6;
/*     */     try {
/* 111 */       debug6 = getOrCreateZipFile();
/* 112 */     } catch (IOException iOException) {
/* 113 */       return Collections.emptySet();
/*     */     } 
/* 115 */     Enumeration<? extends ZipEntry> debug7 = debug6.entries();
/* 116 */     List<ResourceLocation> debug8 = Lists.newArrayList();
/* 117 */     String debug9 = debug1.getDirectory() + "/" + debug2 + "/";
/* 118 */     String debug10 = debug9 + debug3 + "/";
/*     */     
/* 120 */     while (debug7.hasMoreElements()) {
/* 121 */       ZipEntry debug11 = debug7.nextElement();
/* 122 */       if (debug11.isDirectory()) {
/*     */         continue;
/*     */       }
/*     */       
/* 126 */       String debug12 = debug11.getName();
/* 127 */       if (debug12.endsWith(".mcmeta") || !debug12.startsWith(debug10)) {
/*     */         continue;
/*     */       }
/*     */       
/* 131 */       String debug13 = debug12.substring(debug9.length());
/* 132 */       String[] debug14 = debug13.split("/");
/* 133 */       if (debug14.length >= debug4 + 1 && debug5.test(debug14[debug14.length - 1])) {
/* 134 */         debug8.add(new ResourceLocation(debug2, debug13));
/*     */       }
/*     */     } 
/*     */     
/* 138 */     return debug8;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\FilePackResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */