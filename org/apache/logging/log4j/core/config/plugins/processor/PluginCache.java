/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ public class PluginCache
/*     */ {
/*  35 */   private final Map<String, Map<String, PluginEntry>> categories = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Map<String, PluginEntry>> getAllCategories() {
/*  45 */     return this.categories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PluginEntry> getCategory(String category) {
/*  55 */     String key = category.toLowerCase();
/*  56 */     if (!this.categories.containsKey(key)) {
/*  57 */       this.categories.put(key, new LinkedHashMap<>());
/*     */     }
/*  59 */     return this.categories.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeCache(OutputStream os) throws IOException {
/*  70 */     try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os))) {
/*     */ 
/*     */       
/*  73 */       out.writeInt(this.categories.size());
/*  74 */       for (Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
/*  75 */         out.writeUTF(category.getKey());
/*  76 */         Map<String, PluginEntry> m = category.getValue();
/*  77 */         out.writeInt(m.size());
/*  78 */         for (Map.Entry<String, PluginEntry> entry : m.entrySet()) {
/*  79 */           PluginEntry plugin = entry.getValue();
/*  80 */           out.writeUTF(plugin.getKey());
/*  81 */           out.writeUTF(plugin.getClassName());
/*  82 */           out.writeUTF(plugin.getName());
/*  83 */           out.writeBoolean(plugin.isPrintable());
/*  84 */           out.writeBoolean(plugin.isDefer());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadCacheFiles(Enumeration<URL> resources) throws IOException {
/*  97 */     this.categories.clear();
/*  98 */     while (resources.hasMoreElements()) {
/*  99 */       URL url = resources.nextElement();
/* 100 */       try (DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()))) {
/* 101 */         int count = in.readInt();
/* 102 */         for (int i = 0; i < count; i++) {
/* 103 */           String category = in.readUTF();
/* 104 */           Map<String, PluginEntry> m = getCategory(category);
/* 105 */           int entries = in.readInt();
/* 106 */           for (int j = 0; j < entries; j++) {
/* 107 */             PluginEntry entry = new PluginEntry();
/* 108 */             entry.setKey(in.readUTF());
/* 109 */             entry.setClassName(in.readUTF());
/* 110 */             entry.setName(in.readUTF());
/* 111 */             entry.setPrintable(in.readBoolean());
/* 112 */             entry.setDefer(in.readBoolean());
/* 113 */             entry.setCategory(category);
/* 114 */             if (!m.containsKey(entry.getKey())) {
/* 115 */               m.put(entry.getKey(), entry);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 129 */     return this.categories.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */