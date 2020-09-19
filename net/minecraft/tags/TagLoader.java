/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TagLoader<T> {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*  31 */   private static final Gson GSON = new Gson();
/*     */   
/*  33 */   private static final int PATH_SUFFIX_LENGTH = ".json".length();
/*     */   
/*     */   private final Function<ResourceLocation, Optional<T>> idToValue;
/*     */   private final String directory;
/*     */   private final String name;
/*     */   
/*     */   public TagLoader(Function<ResourceLocation, Optional<T>> debug1, String debug2, String debug3) {
/*  40 */     this.idToValue = debug1;
/*  41 */     this.directory = debug2;
/*  42 */     this.name = debug3;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Map<ResourceLocation, Tag.Builder>> prepare(ResourceManager debug1, Executor debug2) {
/*  46 */     return CompletableFuture.supplyAsync(() -> {
/*     */           Map<ResourceLocation, Tag.Builder> debug2 = Maps.newHashMap();
/*     */           
/*     */           for (ResourceLocation debug4 : debug1.listResources(this.directory, ())) {
/*     */             String debug5 = debug4.getPath();
/*     */             
/*     */             ResourceLocation debug6 = new ResourceLocation(debug4.getNamespace(), debug5.substring(this.directory.length() + 1, debug5.length() - PATH_SUFFIX_LENGTH));
/*     */             try {
/*     */               for (Resource debug8 : debug1.getResources(debug4)) {
/*     */                 try(InputStream debug9 = debug8.getInputStream(); Reader debug11 = new BufferedReader(new InputStreamReader(debug9, StandardCharsets.UTF_8))) {
/*     */                   JsonObject debug13 = (JsonObject)GsonHelper.fromJson(GSON, debug11, JsonObject.class);
/*     */                   if (debug13 == null) {
/*     */                     LOGGER.error("Couldn't load {} tag list {} from {} in data pack {} as it is empty or null", this.name, debug6, debug4, debug8.getSourceName());
/*     */                   } else {
/*     */                     ((Tag.Builder)debug2.computeIfAbsent(debug6, ())).addFromJson(debug13, debug8.getSourceName());
/*     */                   } 
/*  62 */                 } catch (IOException|RuntimeException debug9) {
/*     */                   LOGGER.error("Couldn't read {} tag list {} from {} in data pack {}", this.name, debug6, debug4, debug8.getSourceName(), debug9);
/*     */                 } finally {
/*     */                   IOUtils.closeQuietly((Closeable)debug8);
/*     */                 } 
/*     */               } 
/*  68 */             } catch (IOException debug7) {
/*     */               LOGGER.error("Couldn't read {} tag list {} from {}", this.name, debug6, debug4, debug7);
/*     */             } 
/*     */           } 
/*     */           return debug2;
/*     */         }debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public TagCollection<T> load(Map<ResourceLocation, Tag.Builder> debug1) {
/*  78 */     Map<ResourceLocation, Tag<T>> debug2 = Maps.newHashMap();
/*  79 */     Function<ResourceLocation, Tag<T>> debug3 = debug2::get;
/*  80 */     Function<ResourceLocation, T> debug4 = debug1 -> ((Optional)this.idToValue.apply(debug1)).orElse(null);
/*     */     
/*  82 */     while (!debug1.isEmpty()) {
/*  83 */       boolean debug5 = false;
/*  84 */       for (Iterator<Map.Entry<ResourceLocation, Tag.Builder>> debug6 = debug1.entrySet().iterator(); debug6.hasNext(); ) {
/*  85 */         Map.Entry<ResourceLocation, Tag.Builder> debug7 = debug6.next();
/*  86 */         Optional<Tag<T>> debug8 = ((Tag.Builder)debug7.getValue()).build(debug3, debug4);
/*  87 */         if (debug8.isPresent()) {
/*  88 */           debug2.put(debug7.getKey(), debug8.get());
/*  89 */           debug6.remove();
/*  90 */           debug5 = true;
/*     */         } 
/*     */       } 
/*  93 */       if (!debug5) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     debug1.forEach((debug3, debug4) -> LOGGER.error("Couldn't load {} tag {} as it is missing following references: {}", this.name, debug3, debug4.getUnresolvedEntries(debug1, debug2).<CharSequence>map(Objects::toString).collect(Collectors.joining(","))));
/*     */     
/* 100 */     return TagCollection.of(debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\TagLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */