/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class StoredUserList<K, V extends StoredUserEntry<K>>
/*     */ {
/*  27 */   protected static final Logger LOGGER = LogManager.getLogger();
/*  28 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*     */   
/*     */   private final File file;
/*  31 */   private final Map<String, V> map = Maps.newHashMap();
/*     */   
/*     */   public StoredUserList(File debug1) {
/*  34 */     this.file = debug1;
/*     */   }
/*     */   
/*     */   public File getFile() {
/*  38 */     return this.file;
/*     */   }
/*     */   
/*     */   public void add(V debug1) {
/*  42 */     this.map.put(getKeyForUser(debug1.getUser()), debug1);
/*     */     try {
/*  44 */       save();
/*  45 */     } catch (IOException debug2) {
/*  46 */       LOGGER.warn("Could not save the list after adding a user.", debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public V get(K debug1) {
/*  52 */     removeExpired();
/*  53 */     return this.map.get(getKeyForUser(debug1));
/*     */   }
/*     */   
/*     */   public void remove(K debug1) {
/*  57 */     this.map.remove(getKeyForUser(debug1));
/*     */     try {
/*  59 */       save();
/*  60 */     } catch (IOException debug2) {
/*  61 */       LOGGER.warn("Could not save the list after removing a user.", debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(StoredUserEntry<K> debug1) {
/*  66 */     remove(debug1.getUser());
/*     */   }
/*     */   
/*     */   public String[] getUserList() {
/*  70 */     return (String[])this.map.keySet().toArray((Object[])new String[this.map.size()]);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  74 */     return (this.map.size() < 1);
/*     */   }
/*     */   
/*     */   protected String getKeyForUser(K debug1) {
/*  78 */     return debug1.toString();
/*     */   }
/*     */   
/*     */   protected boolean contains(K debug1) {
/*  82 */     return this.map.containsKey(getKeyForUser(debug1));
/*     */   }
/*     */   
/*     */   private void removeExpired() {
/*  86 */     List<K> debug1 = Lists.newArrayList();
/*  87 */     for (StoredUserEntry<K> storedUserEntry : this.map.values()) {
/*  88 */       if (storedUserEntry.hasExpired()) {
/*  89 */         debug1.add(storedUserEntry.getUser());
/*     */       }
/*     */     } 
/*  92 */     for (K debug3 : debug1) {
/*  93 */       this.map.remove(getKeyForUser(debug3));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> getEntries() {
/* 100 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public void save() throws IOException {
/* 104 */     JsonArray debug1 = new JsonArray();
/* 105 */     this.map.values().stream().map(debug0 -> (JsonObject)Util.make(new JsonObject(), debug0::serialize)).forEach(debug1::add);
/* 106 */     try (BufferedWriter debug2 = Files.newWriter(this.file, StandardCharsets.UTF_8)) {
/* 107 */       GSON.toJson((JsonElement)debug1, debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load() throws IOException {
/* 114 */     if (!this.file.exists()) {
/*     */       return;
/*     */     }
/* 117 */     try (BufferedReader debug1 = Files.newReader(this.file, StandardCharsets.UTF_8)) {
/* 118 */       JsonArray debug3 = (JsonArray)GSON.fromJson(debug1, JsonArray.class);
/*     */       
/* 120 */       this.map.clear();
/* 121 */       for (JsonElement debug5 : debug3) {
/* 122 */         JsonObject debug6 = GsonHelper.convertToJsonObject(debug5, "entry");
/* 123 */         StoredUserEntry<K> debug7 = createEntry(debug6);
/* 124 */         if (debug7.getUser() != null)
/* 125 */           this.map.put(getKeyForUser(debug7.getUser()), (V)debug7); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract StoredUserEntry<K> createEntry(JsonObject paramJsonObject);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\StoredUserList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */