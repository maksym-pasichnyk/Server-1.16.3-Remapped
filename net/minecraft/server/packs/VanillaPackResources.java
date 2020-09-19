/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class VanillaPackResources
/*     */   implements PackResources
/*     */ {
/*     */   public static Path generatedDir;
/*  38 */   private static final Logger LOGGER = LogManager.getLogger(); public static Class<?> clientObject;
/*     */   
/*     */   static {
/*  41 */     JAR_FILESYSTEM_BY_TYPE = (Map<PackType, FileSystem>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           synchronized (VanillaPackResources.class) {
/*     */             for (PackType debug5 : PackType.values()) {
/*     */               URL debug6 = VanillaPackResources.class.getResource("/" + debug5.getDirectory() + "/.mcassetsroot");
/*     */               try {
/*     */                 URI debug7 = debug6.toURI();
/*     */                 if ("jar".equals(debug7.getScheme())) {
/*     */                   FileSystem debug8;
/*     */                   try {
/*     */                     debug8 = FileSystems.getFileSystem(debug7);
/*  51 */                   } catch (FileSystemNotFoundException debug9) {
/*     */                     debug8 = FileSystems.newFileSystem(debug7, Collections.emptyMap());
/*     */                   } 
/*     */                   debug0.put(debug5, debug8);
/*     */                 } 
/*  56 */               } catch (URISyntaxException|IOException debug7) {
/*     */                 LOGGER.error("Couldn't get a list of all vanilla resources", debug7);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   private static final Map<PackType, FileSystem> JAR_FILESYSTEM_BY_TYPE; public final Set<String> namespaces;
/*     */   
/*     */   public VanillaPackResources(String... debug1) {
/*  66 */     this.namespaces = (Set<String>)ImmutableSet.copyOf((Object[])debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getRootResource(String debug1) throws IOException {
/*  71 */     if (debug1.contains("/") || debug1.contains("\\")) {
/*  72 */       throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
/*     */     }
/*  74 */     if (generatedDir != null) {
/*  75 */       Path debug2 = generatedDir.resolve(debug1);
/*  76 */       if (Files.exists(debug2, new java.nio.file.LinkOption[0])) {
/*  77 */         return Files.newInputStream(debug2, new java.nio.file.OpenOption[0]);
/*     */       }
/*     */     } 
/*  80 */     return getResourceAsStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getResource(PackType debug1, ResourceLocation debug2) throws IOException {
/*  85 */     InputStream debug3 = getResourceAsStream(debug1, debug2);
/*  86 */     if (debug3 != null) {
/*  87 */       return debug3;
/*     */     }
/*  89 */     throw new FileNotFoundException(debug2.getPath());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> getResources(PackType debug1, String debug2, String debug3, int debug4, Predicate<String> debug5) {
/*  94 */     Set<ResourceLocation> debug6 = Sets.newHashSet();
/*     */     
/*  96 */     if (generatedDir != null) {
/*     */       try {
/*  98 */         getResources(debug6, debug4, debug2, generatedDir.resolve(debug1.getDirectory()), debug3, debug5);
/*  99 */       } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */       
/* 103 */       if (debug1 == PackType.CLIENT_RESOURCES) {
/* 104 */         Enumeration<URL> debug7 = null;
/*     */         try {
/* 106 */           debug7 = clientObject.getClassLoader().getResources(debug1.getDirectory() + "/");
/* 107 */         } catch (IOException iOException) {}
/*     */         
/* 109 */         while (debug7 != null && debug7.hasMoreElements()) {
/*     */           try {
/* 111 */             URI debug8 = ((URL)debug7.nextElement()).toURI();
/* 112 */             if ("file".equals(debug8.getScheme())) {
/* 113 */               getResources(debug6, debug4, debug2, Paths.get(debug8), debug3, debug5);
/*     */             }
/* 115 */           } catch (URISyntaxException|IOException uRISyntaxException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 122 */     try { URL debug7 = VanillaPackResources.class.getResource("/" + debug1.getDirectory() + "/.mcassetsroot");
/* 123 */       if (debug7 == null) {
/* 124 */         LOGGER.error("Couldn't find .mcassetsroot, cannot load vanilla resources");
/* 125 */         return debug6;
/*     */       } 
/* 127 */       URI debug8 = debug7.toURI();
/* 128 */       if ("file".equals(debug8.getScheme())) {
/* 129 */         URL debug9 = new URL(debug7.toString().substring(0, debug7.toString().length() - ".mcassetsroot".length()));
/* 130 */         Path debug10 = Paths.get(debug9.toURI());
/* 131 */         getResources(debug6, debug4, debug2, debug10, debug3, debug5);
/* 132 */       } else if ("jar".equals(debug8.getScheme())) {
/* 133 */         Path debug9 = ((FileSystem)JAR_FILESYSTEM_BY_TYPE.get(debug1)).getPath("/" + debug1.getDirectory(), new String[0]);
/* 134 */         getResources(debug6, debug4, "minecraft", debug9, debug3, debug5);
/*     */       } else {
/* 136 */         LOGGER.error("Unsupported scheme {} trying to list vanilla resources (NYI?)", debug8);
/*     */       }  }
/* 138 */     catch (FileNotFoundException|java.nio.file.NoSuchFileException fileNotFoundException) {  }
/* 139 */     catch (URISyntaxException|IOException debug7)
/* 140 */     { LOGGER.error("Couldn't get a list of all vanilla resources", debug7); }
/*     */ 
/*     */     
/* 143 */     return debug6;
/*     */   }
/*     */   
/*     */   private static void getResources(Collection<ResourceLocation> debug0, int debug1, String debug2, Path debug3, String debug4, Predicate<String> debug5) throws IOException {
/* 147 */     Path debug6 = debug3.resolve(debug2);
/* 148 */     try (Stream<Path> debug7 = Files.walk(debug6.resolve(debug4), debug1, new java.nio.file.FileVisitOption[0])) {
/* 149 */       debug7
/* 150 */         .filter(debug1 -> (!debug1.endsWith(".mcmeta") && Files.isRegularFile(debug1, new java.nio.file.LinkOption[0]) && debug0.test(debug1.getFileName().toString())))
/* 151 */         .map(debug2 -> new ResourceLocation(debug0, debug1.relativize(debug2).toString().replaceAll("\\\\", "/")))
/* 152 */         .forEach(debug0::add);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected InputStream getResourceAsStream(PackType debug1, ResourceLocation debug2) {
/* 158 */     String debug3 = createPath(debug1, debug2);
/*     */     
/* 160 */     if (generatedDir != null) {
/* 161 */       Path debug4 = generatedDir.resolve(debug1.getDirectory() + "/" + debug2.getNamespace() + "/" + debug2.getPath());
/* 162 */       if (Files.exists(debug4, new java.nio.file.LinkOption[0])) {
/*     */         try {
/* 164 */           return Files.newInputStream(debug4, new java.nio.file.OpenOption[0]);
/* 165 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 171 */       URL debug4 = VanillaPackResources.class.getResource(debug3);
/* 172 */       if (isResourceUrlValid(debug3, debug4)) {
/* 173 */         return debug4.openStream();
/*     */       }
/* 175 */     } catch (IOException debug4) {
/* 176 */       return VanillaPackResources.class.getResourceAsStream(debug3);
/*     */     } 
/*     */     
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   private static String createPath(PackType debug0, ResourceLocation debug1) {
/* 183 */     return "/" + debug0.getDirectory() + "/" + debug1.getNamespace() + "/" + debug1.getPath();
/*     */   }
/*     */   
/*     */   private static boolean isResourceUrlValid(String debug0, @Nullable URL debug1) throws IOException {
/* 187 */     return (debug1 != null && (debug1.getProtocol().equals("jar") || FolderPackResources.validatePath(new File(debug1.getFile()), debug0)));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected InputStream getResourceAsStream(String debug1) {
/* 192 */     return VanillaPackResources.class.getResourceAsStream("/" + debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasResource(PackType debug1, ResourceLocation debug2) {
/* 197 */     String debug3 = createPath(debug1, debug2);
/*     */     
/* 199 */     if (generatedDir != null) {
/* 200 */       Path debug4 = generatedDir.resolve(debug1.getDirectory() + "/" + debug2.getNamespace() + "/" + debug2.getPath());
/* 201 */       if (Files.exists(debug4, new java.nio.file.LinkOption[0])) {
/* 202 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 207 */       URL debug4 = VanillaPackResources.class.getResource(debug3);
/* 208 */       return isResourceUrlValid(debug3, debug4);
/* 209 */     } catch (IOException iOException) {
/*     */ 
/*     */       
/* 212 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<String> getNamespaces(PackType debug1) {
/* 217 */     return this.namespaces;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getMetadataSection(MetadataSectionSerializer<T> debug1) throws IOException {
/* 223 */     try (InputStream debug2 = getRootResource("pack.mcmeta")) {
/* 224 */       return (T)AbstractPackResources.getMetadataFromStream((MetadataSectionSerializer)debug1, debug2);
/* 225 */     } catch (RuntimeException|FileNotFoundException debug2) {
/* 226 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 232 */     return "Default";
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\VanillaPackResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */