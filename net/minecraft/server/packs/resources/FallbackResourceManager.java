/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FallbackResourceManager
/*     */   implements ResourceManager
/*     */ {
/*  25 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  27 */   protected final List<PackResources> fallbacks = Lists.newArrayList();
/*     */   private final PackType type;
/*     */   private final String namespace;
/*     */   
/*     */   public FallbackResourceManager(PackType debug1, String debug2) {
/*  32 */     this.type = debug1;
/*  33 */     this.namespace = debug2;
/*     */   }
/*     */   
/*     */   public void add(PackResources debug1) {
/*  37 */     this.fallbacks.add(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(ResourceLocation debug1) throws IOException {
/*  47 */     validateLocation(debug1);
/*     */     
/*  49 */     PackResources debug2 = null;
/*  50 */     ResourceLocation debug3 = getMetadataLocation(debug1);
/*     */     
/*  52 */     for (int debug4 = this.fallbacks.size() - 1; debug4 >= 0; debug4--) {
/*  53 */       PackResources debug5 = this.fallbacks.get(debug4);
/*  54 */       if (debug2 == null && debug5.hasResource(this.type, debug3)) {
/*  55 */         debug2 = debug5;
/*     */       }
/*     */       
/*  58 */       if (debug5.hasResource(this.type, debug1)) {
/*  59 */         InputStream debug6 = null;
/*  60 */         if (debug2 != null) {
/*  61 */           debug6 = getWrappedResource(debug3, debug2);
/*     */         }
/*  63 */         return new SimpleResource(debug5.getName(), debug1, getWrappedResource(debug1, debug5), debug6);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     throw new FileNotFoundException(debug1.toString());
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
/*     */   protected InputStream getWrappedResource(ResourceLocation debug1, PackResources debug2) throws IOException {
/*  88 */     InputStream debug3 = debug2.getResource(this.type, debug1);
/*  89 */     return LOGGER.isDebugEnabled() ? new LeakedResourceWarningInputStream(debug3, debug1, debug2.getName()) : debug3;
/*     */   }
/*     */   
/*     */   private void validateLocation(ResourceLocation debug1) throws IOException {
/*  93 */     if (!isValidLocation(debug1)) {
/*  94 */       throw new IOException("Invalid relative path to resource: " + debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isValidLocation(ResourceLocation debug1) {
/*  99 */     return !debug1.getPath().contains("..");
/*     */   }
/*     */   
/*     */   static class LeakedResourceWarningInputStream
/*     */     extends FilterInputStream {
/*     */     private final String message;
/*     */     private boolean closed;
/*     */     
/*     */     public LeakedResourceWarningInputStream(InputStream debug1, ResourceLocation debug2, String debug3) {
/* 108 */       super(debug1);
/* 109 */       ByteArrayOutputStream debug4 = new ByteArrayOutputStream();
/* 110 */       (new Exception()).printStackTrace(new PrintStream(debug4));
/* 111 */       this.message = "Leaked resource: '" + debug2 + "' loaded from pack: '" + debug3 + "'\n" + debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 116 */       super.close();
/* 117 */       this.closed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void finalize() throws Throwable {
/* 122 */       if (!this.closed) {
/* 123 */         FallbackResourceManager.LOGGER.warn(this.message);
/*     */       }
/*     */       
/* 126 */       super.finalize();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Resource> getResources(ResourceLocation debug1) throws IOException {
/* 132 */     validateLocation(debug1);
/*     */     
/* 134 */     List<Resource> debug2 = Lists.newArrayList();
/* 135 */     ResourceLocation debug3 = getMetadataLocation(debug1);
/*     */     
/* 137 */     for (PackResources debug5 : this.fallbacks) {
/* 138 */       if (debug5.hasResource(this.type, debug1)) {
/* 139 */         InputStream debug6 = debug5.hasResource(this.type, debug3) ? getWrappedResource(debug3, debug5) : null;
/* 140 */         debug2.add(new SimpleResource(debug5.getName(), debug1, getWrappedResource(debug1, debug5), debug6));
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     if (debug2.isEmpty()) {
/* 145 */       throw new FileNotFoundException(debug1.toString());
/*     */     }
/*     */     
/* 148 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> listResources(String debug1, Predicate<String> debug2) {
/* 153 */     List<ResourceLocation> debug3 = Lists.newArrayList();
/*     */     
/* 155 */     for (PackResources debug5 : this.fallbacks) {
/* 156 */       debug3.addAll(debug5.getResources(this.type, this.namespace, debug1, 2147483647, debug2));
/*     */     }
/*     */     
/* 159 */     Collections.sort(debug3);
/*     */     
/* 161 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ResourceLocation getMetadataLocation(ResourceLocation debug0) {
/* 170 */     return new ResourceLocation(debug0.getNamespace(), debug0.getPath() + ".mcmeta");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\FallbackResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */