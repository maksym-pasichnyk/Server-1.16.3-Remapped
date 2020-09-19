/*     */ package net.minecraft.data.tags;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.DataGenerator;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.HashCache;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.SetTag;
/*     */ import net.minecraft.tags.Tag;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class TagsProvider<T> implements DataProvider {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger();
/*  30 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*     */   
/*     */   protected final DataGenerator generator;
/*     */   
/*     */   protected final Registry<T> registry;
/*  35 */   private final Map<ResourceLocation, Tag.Builder> builders = Maps.newLinkedHashMap();
/*     */   
/*     */   protected TagsProvider(DataGenerator debug1, Registry<T> debug2) {
/*  38 */     this.generator = debug1;
/*  39 */     this.registry = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(HashCache debug1) {
/*  46 */     this.builders.clear();
/*  47 */     addTags();
/*     */     
/*  49 */     SetTag setTag = SetTag.empty();
/*  50 */     Function<ResourceLocation, Tag<T>> debug3 = debug2 -> this.builders.containsKey(debug2) ? debug1 : null;
/*  51 */     Function<ResourceLocation, T> debug4 = debug1 -> this.registry.getOptional(debug1).orElse(null);
/*  52 */     this.builders.forEach((debug4, debug5) -> {
/*     */           List<Tag.BuilderEntry> debug6 = (List<Tag.BuilderEntry>)debug5.getUnresolvedEntries(debug1, debug2).collect(Collectors.toList());
/*     */           
/*     */           if (!debug6.isEmpty()) {
/*     */             throw new IllegalArgumentException(String.format("Couldn't define tag %s as it is missing following references: %s", new Object[] { debug4, debug6.stream().map(Objects::toString).collect(Collectors.joining(",")) }));
/*     */           }
/*     */           
/*     */           JsonObject debug7 = debug5.serializeToJson();
/*     */           Path debug8 = getPath(debug4);
/*     */           try {
/*     */             String debug9 = GSON.toJson((JsonElement)debug7);
/*     */             String debug10 = SHA1.hashUnencodedChars(debug9).toString();
/*     */             if (!Objects.equals(debug3.getHash(debug8), debug10) || !Files.exists(debug8, new java.nio.file.LinkOption[0])) {
/*     */               Files.createDirectories(debug8.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*     */               try (BufferedWriter debug11 = Files.newBufferedWriter(debug8, new java.nio.file.OpenOption[0])) {
/*     */                 debug11.write(debug9);
/*     */               } 
/*     */             } 
/*     */             debug3.putNew(debug8, debug10);
/*  71 */           } catch (IOException debug9) {
/*     */             LOGGER.error("Couldn't save tags to {}", debug8, debug9);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TagAppender<T> tag(Tag.Named<T> debug1) {
/*  80 */     Tag.Builder debug2 = getOrCreateRawBuilder(debug1);
/*  81 */     return new TagAppender<>(debug2, this.registry, "vanilla");
/*     */   }
/*     */   
/*     */   protected Tag.Builder getOrCreateRawBuilder(Tag.Named<T> debug1) {
/*  85 */     return this.builders.computeIfAbsent(debug1.getName(), debug0 -> new Tag.Builder());
/*     */   }
/*     */   protected abstract void addTags();
/*     */   
/*     */   protected abstract Path getPath(ResourceLocation paramResourceLocation);
/*     */   
/*     */   public static class TagAppender<T> { private final Tag.Builder builder;
/*     */     
/*     */     private TagAppender(Tag.Builder debug1, Registry<T> debug2, String debug3) {
/*  94 */       this.builder = debug1;
/*  95 */       this.registry = debug2;
/*  96 */       this.source = debug3;
/*     */     }
/*     */     private final Registry<T> registry; private final String source;
/*     */     public TagAppender<T> add(T debug1) {
/* 100 */       this.builder.addElement(this.registry.getKey(debug1), this.source);
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TagAppender<T> addTag(Tag.Named<T> debug1) {
/* 110 */       this.builder.addTag(debug1.getName(), this.source);
/* 111 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @SafeVarargs
/*     */     public final TagAppender<T> add(T... debug1) {
/* 122 */       Stream.<T>of(debug1).map(this.registry::getKey).forEach(debug1 -> this.builder.addElement(debug1, this.source));
/* 123 */       return this;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\tags\TagsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */