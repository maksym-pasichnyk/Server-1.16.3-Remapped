/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public abstract class SimpleJsonResourceReloadListener
/*    */   extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>>
/*    */ {
/* 22 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/* 24 */   private static final int PATH_SUFFIX_LENGTH = ".json".length();
/*    */   
/*    */   private final Gson gson;
/*    */   private final String directory;
/*    */   
/*    */   public SimpleJsonResourceReloadListener(Gson debug1, String debug2) {
/* 30 */     this.gson = debug1;
/* 31 */     this.directory = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Map<ResourceLocation, JsonElement> prepare(ResourceManager debug1, ProfilerFiller debug2) {
/* 36 */     Map<ResourceLocation, JsonElement> debug3 = Maps.newHashMap();
/*    */     
/* 38 */     int debug4 = this.directory.length() + 1;
/*    */     
/* 40 */     for (ResourceLocation debug6 : debug1.listResources(this.directory, debug0 -> debug0.endsWith(".json"))) {
/* 41 */       String debug7 = debug6.getPath();
/* 42 */       ResourceLocation debug8 = new ResourceLocation(debug6.getNamespace(), debug7.substring(debug4, debug7.length() - PATH_SUFFIX_LENGTH));
/*    */       
/* 44 */       try(Resource debug9 = debug1.getResource(debug6); 
/* 45 */           InputStream debug11 = debug9.getInputStream(); 
/* 46 */           Reader debug13 = new BufferedReader(new InputStreamReader(debug11, StandardCharsets.UTF_8))) {
/* 47 */         JsonElement debug15 = (JsonElement)GsonHelper.fromJson(this.gson, debug13, JsonElement.class);
/* 48 */         if (debug15 != null) {
/* 49 */           JsonElement debug16 = debug3.put(debug8, debug15);
/* 50 */           if (debug16 != null) {
/* 51 */             throw new IllegalStateException("Duplicate data file ignored with ID " + debug8);
/*    */           }
/*    */         } else {
/* 54 */           LOGGER.error("Couldn't load data file {} from {} as it's null or empty", debug8, debug6);
/*    */         } 
/* 56 */       } catch (JsonParseException|IllegalArgumentException|java.io.IOException debug9) {
/* 57 */         LOGGER.error("Couldn't parse data file {} from {}", debug8, debug6, debug9);
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\SimpleJsonResourceReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */