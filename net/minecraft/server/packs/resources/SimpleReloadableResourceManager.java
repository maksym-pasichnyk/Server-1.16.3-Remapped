/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.util.Unit;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ public class SimpleReloadableResourceManager
/*     */   implements ReloadableResourceManager
/*     */ {
/*  28 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  30 */   private final Map<String, FallbackResourceManager> namespacedPacks = Maps.newHashMap();
/*  31 */   private final List<PreparableReloadListener> listeners = Lists.newArrayList();
/*  32 */   private final List<PreparableReloadListener> recentlyRegistered = Lists.newArrayList();
/*  33 */   private final Set<String> namespaces = Sets.newLinkedHashSet();
/*  34 */   private final List<PackResources> packs = Lists.newArrayList();
/*     */   private final PackType type;
/*     */   
/*     */   public SimpleReloadableResourceManager(PackType debug1) {
/*  38 */     this.type = debug1;
/*     */   }
/*     */   
/*     */   public void add(PackResources debug1) {
/*  42 */     this.packs.add(debug1);
/*  43 */     for (String debug3 : debug1.getNamespaces(this.type)) {
/*  44 */       this.namespaces.add(debug3);
/*  45 */       FallbackResourceManager debug4 = this.namespacedPacks.get(debug3);
/*  46 */       if (debug4 == null) {
/*  47 */         debug4 = new FallbackResourceManager(this.type, debug3);
/*  48 */         this.namespacedPacks.put(debug3, debug4);
/*     */       } 
/*  50 */       debug4.add(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(ResourceLocation debug1) throws IOException {
/*  61 */     ResourceManager debug2 = this.namespacedPacks.get(debug1.getNamespace());
/*     */     
/*  63 */     if (debug2 != null) {
/*  64 */       return debug2.getResource(debug1);
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
/*     */   public List<Resource> getResources(ResourceLocation debug1) throws IOException {
/*  83 */     ResourceManager debug2 = this.namespacedPacks.get(debug1.getNamespace());
/*     */     
/*  85 */     if (debug2 != null) {
/*  86 */       return debug2.getResources(debug1);
/*     */     }
/*     */     
/*  89 */     throw new FileNotFoundException(debug1.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> listResources(String debug1, Predicate<String> debug2) {
/*  94 */     Set<ResourceLocation> debug3 = Sets.newHashSet();
/*     */     
/*  96 */     for (FallbackResourceManager debug5 : this.namespacedPacks.values()) {
/*  97 */       debug3.addAll(debug5.listResources(debug1, debug2));
/*     */     }
/*     */     
/* 100 */     List<ResourceLocation> debug4 = Lists.newArrayList(debug3);
/* 101 */     Collections.sort(debug4);
/* 102 */     return debug4;
/*     */   }
/*     */   
/*     */   private void clear() {
/* 106 */     this.namespacedPacks.clear();
/* 107 */     this.namespaces.clear();
/* 108 */     this.packs.forEach(PackResources::close);
/* 109 */     this.packs.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 114 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerReloadListener(PreparableReloadListener debug1) {
/* 119 */     this.listeners.add(debug1);
/* 120 */     this.recentlyRegistered.add(debug1);
/*     */   }
/*     */   
/*     */   protected ReloadInstance createReload(Executor debug1, Executor debug2, List<PreparableReloadListener> debug3, CompletableFuture<Unit> debug4) {
/*     */     ReloadInstance<Void> debug5;
/* 125 */     if (LOGGER.isDebugEnabled()) {
/* 126 */       debug5 = new ProfiledReloadInstance(this, Lists.newArrayList(debug3), debug1, debug2, debug4);
/*     */     } else {
/* 128 */       debug5 = SimpleReloadInstance.of(this, Lists.newArrayList(debug3), debug1, debug2, debug4);
/*     */     } 
/* 130 */     this.recentlyRegistered.clear();
/* 131 */     return debug5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReloadInstance createFullReload(Executor debug1, Executor debug2, CompletableFuture<Unit> debug3, List<PackResources> debug4) {
/* 141 */     clear();
/*     */     
/* 143 */     LOGGER.info("Reloading ResourceManager: {}", new Supplier[] { () -> (String)debug0.stream().map(PackResources::getName).collect(Collectors.joining(", ")) });
/*     */     
/* 145 */     for (PackResources debug6 : debug4) {
/*     */       try {
/* 147 */         add(debug6);
/* 148 */       } catch (Exception debug7) {
/* 149 */         LOGGER.error("Failed to add resource pack {}", debug6.getName(), debug7);
/* 150 */         return new FailingReloadInstance(new ResourcePackLoadingFailure(debug6, debug7));
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     return createReload(debug1, debug2, this.listeners, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ResourcePackLoadingFailure
/*     */     extends RuntimeException
/*     */   {
/*     */     private final PackResources pack;
/*     */ 
/*     */     
/*     */     public ResourcePackLoadingFailure(PackResources debug1, Throwable debug2) {
/* 166 */       super(debug1.getName(), debug2);
/* 167 */       this.pack = debug1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class FailingReloadInstance
/*     */     implements ReloadInstance
/*     */   {
/*     */     private final SimpleReloadableResourceManager.ResourcePackLoadingFailure exception;
/*     */     
/*     */     private final CompletableFuture<Unit> failedFuture;
/*     */     
/*     */     public FailingReloadInstance(SimpleReloadableResourceManager.ResourcePackLoadingFailure debug1) {
/* 180 */       this.exception = debug1;
/* 181 */       this.failedFuture = new CompletableFuture<>();
/* 182 */       this.failedFuture.completeExceptionally(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public CompletableFuture<Unit> done() {
/* 187 */       return this.failedFuture;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\SimpleReloadableResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */