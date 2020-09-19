/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.commons.io.filefilter.DirectoryFileFilter;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class FolderPackResources
/*     */   extends AbstractPackResources {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  28 */   private static final boolean ON_WINDOWS = (Util.getPlatform() == Util.OS.WINDOWS);
/*  29 */   private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');
/*     */   
/*     */   public FolderPackResources(File debug1) {
/*  32 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static boolean validatePath(File debug0, String debug1) throws IOException {
/*  36 */     String debug2 = debug0.getCanonicalPath();
/*     */ 
/*     */     
/*  39 */     if (ON_WINDOWS) {
/*  40 */       debug2 = BACKSLASH_MATCHER.replaceFrom(debug2, '/');
/*     */     }
/*  42 */     return debug2.endsWith(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getResource(String debug1) throws IOException {
/*  47 */     File debug2 = getFile(debug1);
/*  48 */     if (debug2 == null) {
/*  49 */       throw new ResourcePackFileNotFoundException(this.file, debug1);
/*     */     }
/*     */     
/*  52 */     return new FileInputStream(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasResource(String debug1) {
/*  57 */     return (getFile(debug1) != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private File getFile(String debug1) {
/*     */     try {
/*  63 */       File debug2 = new File(this.file, debug1);
/*  64 */       if (debug2.isFile() && validatePath(debug2, debug1)) {
/*  65 */         return debug2;
/*     */       }
/*  67 */     } catch (IOException iOException) {}
/*     */     
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType debug1) {
/*  74 */     Set<String> debug2 = Sets.newHashSet();
/*  75 */     File debug3 = new File(this.file, debug1.getDirectory());
/*     */     
/*  77 */     File[] debug4 = debug3.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
/*  78 */     if (debug4 != null) {
/*  79 */       for (File debug8 : debug4) {
/*  80 */         String debug9 = getRelativePath(debug3, debug8);
/*     */         
/*  82 */         if (debug9.equals(debug9.toLowerCase(Locale.ROOT))) {
/*  83 */           debug2.add(debug9.substring(0, debug9.length() - 1));
/*     */         } else {
/*  85 */           logWarning(debug9);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  90 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> getResources(PackType debug1, String debug2, String debug3, int debug4, Predicate<String> debug5) {
/*  99 */     File debug6 = new File(this.file, debug1.getDirectory());
/* 100 */     List<ResourceLocation> debug7 = Lists.newArrayList();
/* 101 */     listResources(new File(new File(debug6, debug2), debug3), debug4, debug2, debug7, debug3 + "/", debug5);
/* 102 */     return debug7;
/*     */   }
/*     */   
/*     */   private void listResources(File debug1, int debug2, String debug3, List<ResourceLocation> debug4, String debug5, Predicate<String> debug6) {
/* 106 */     File[] debug7 = debug1.listFiles();
/* 107 */     if (debug7 != null)
/* 108 */       for (File debug11 : debug7) {
/* 109 */         if (debug11.isDirectory()) {
/* 110 */           if (debug2 > 0) {
/* 111 */             listResources(debug11, debug2 - 1, debug3, debug4, debug5 + debug11.getName() + "/", debug6);
/*     */           }
/*     */         }
/* 114 */         else if (!debug11.getName().endsWith(".mcmeta") && debug6.test(debug11.getName())) {
/*     */           try {
/* 116 */             debug4.add(new ResourceLocation(debug3, debug5 + debug11.getName()));
/* 117 */           } catch (ResourceLocationException debug12) {
/* 118 */             LOGGER.error(debug12.getMessage());
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\FolderPackResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */