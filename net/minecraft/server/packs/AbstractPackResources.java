/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public abstract class AbstractPackResources
/*    */   implements PackResources {
/* 20 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   protected final File file;
/*    */   
/*    */   public AbstractPackResources(File debug1) {
/* 25 */     this.file = debug1;
/*    */   }
/*    */   
/*    */   private static String getPathFromLocation(PackType debug0, ResourceLocation debug1) {
/* 29 */     return String.format("%s/%s/%s", new Object[] { debug0.getDirectory(), debug1.getNamespace(), debug1.getPath() });
/*    */   }
/*    */   
/*    */   protected static String getRelativePath(File debug0, File debug1) {
/* 33 */     return debug0.toURI().relativize(debug1.toURI()).getPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getResource(PackType debug1, ResourceLocation debug2) throws IOException {
/* 38 */     return getResource(getPathFromLocation(debug1, debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasResource(PackType debug1, ResourceLocation debug2) {
/* 43 */     return hasResource(getPathFromLocation(debug1, debug2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract InputStream getResource(String paramString) throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean hasResource(String paramString);
/*    */ 
/*    */ 
/*    */   
/*    */   protected void logWarning(String debug1) {
/* 59 */     LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", debug1, this.file);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T getMetadataSection(MetadataSectionSerializer<T> debug1) throws IOException {
/* 65 */     try (InputStream debug2 = getResource("pack.mcmeta")) {
/* 66 */       return (T)getMetadataFromStream((MetadataSectionSerializer)debug1, debug2);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static <T> T getMetadataFromStream(MetadataSectionSerializer<T> debug0, InputStream debug1) {
/*    */     JsonObject debug2;
/* 73 */     try (BufferedReader debug3 = new BufferedReader(new InputStreamReader(debug1, StandardCharsets.UTF_8))) {
/* 74 */       debug2 = GsonHelper.parse(debug3);
/* 75 */     } catch (IOException|JsonParseException debug3) {
/* 76 */       LOGGER.error("Couldn't load {} metadata", debug0.getMetadataSectionName(), debug3);
/* 77 */       return null;
/*    */     } 
/*    */     
/* 80 */     if (!debug2.has(debug0.getMetadataSectionName())) {
/* 81 */       return null;
/*    */     }
/*    */     try {
/* 84 */       return (T)debug0.fromJson(GsonHelper.getAsJsonObject(debug2, debug0.getMetadataSectionName()));
/* 85 */     } catch (JsonParseException debug3) {
/* 86 */       LOGGER.error("Couldn't load {} metadata", debug0.getMetadataSectionName(), debug3);
/* 87 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 93 */     return this.file.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\AbstractPackResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */