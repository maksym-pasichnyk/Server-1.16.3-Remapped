/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleResource
/*     */   implements Resource
/*     */ {
/*     */   private final String sourceName;
/*     */   private final ResourceLocation location;
/*     */   private final InputStream resourceStream;
/*     */   private final InputStream metadataStream;
/*     */   
/*     */   public SimpleResource(String debug1, ResourceLocation debug2, InputStream debug3, @Nullable InputStream debug4) {
/*  25 */     this.sourceName = debug1;
/*  26 */     this.location = debug2;
/*  27 */     this.resourceStream = debug3;
/*  28 */     this.metadataStream = debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/*  38 */     return this.resourceStream;
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
/*     */   public String getSourceName() {
/*  77 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  82 */     if (this == debug1) {
/*  83 */       return true;
/*     */     }
/*  85 */     if (!(debug1 instanceof SimpleResource)) {
/*  86 */       return false;
/*     */     }
/*     */     
/*  89 */     SimpleResource debug2 = (SimpleResource)debug1;
/*     */     
/*  91 */     if ((this.location != null) ? !this.location.equals(debug2.location) : (debug2.location != null)) {
/*  92 */       return false;
/*     */     }
/*  94 */     if ((this.sourceName != null) ? !this.sourceName.equals(debug2.sourceName) : (debug2.sourceName != null)) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     int debug1 = (this.sourceName != null) ? this.sourceName.hashCode() : 0;
/* 104 */     debug1 = 31 * debug1 + ((this.location != null) ? this.location.hashCode() : 0);
/* 105 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 110 */     this.resourceStream.close();
/* 111 */     if (this.metadataStream != null)
/* 112 */       this.metadataStream.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\SimpleResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */