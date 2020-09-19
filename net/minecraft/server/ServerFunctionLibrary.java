/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.commands.CommandFunction;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.tags.TagCollection;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerFunctionLibrary implements PreparableReloadListener {
/*  35 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*  38 */   private static final int PATH_PREFIX_LENGTH = "functions/".length();
/*  39 */   private static final int PATH_SUFFIX_LENGTH = ".mcfunction".length();
/*     */   
/*  41 */   private volatile Map<ResourceLocation, CommandFunction> functions = (Map<ResourceLocation, CommandFunction>)ImmutableMap.of();
/*  42 */   private final TagLoader<CommandFunction> tagsLoader = new TagLoader(this::getFunction, "tags/functions", "function");
/*  43 */   private volatile TagCollection<CommandFunction> tags = TagCollection.empty();
/*     */   
/*     */   private final int functionCompilationLevel;
/*     */   private final CommandDispatcher<CommandSourceStack> dispatcher;
/*     */   
/*     */   public Optional<CommandFunction> getFunction(ResourceLocation debug1) {
/*  49 */     return Optional.ofNullable(this.functions.get(debug1));
/*     */   }
/*     */   
/*     */   public Map<ResourceLocation, CommandFunction> getFunctions() {
/*  53 */     return this.functions;
/*     */   }
/*     */   
/*     */   public TagCollection<CommandFunction> getTags() {
/*  57 */     return this.tags;
/*     */   }
/*     */   
/*     */   public Tag<CommandFunction> getTag(ResourceLocation debug1) {
/*  61 */     return this.tags.getTagOrEmpty(debug1);
/*     */   }
/*     */   
/*     */   public ServerFunctionLibrary(int debug1, CommandDispatcher<CommandSourceStack> debug2) {
/*  65 */     this.functionCompilationLevel = debug1;
/*  66 */     this.dispatcher = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier debug1, ResourceManager debug2, ProfilerFiller debug3, ProfilerFiller debug4, Executor debug5, Executor debug6) {
/*  71 */     CompletableFuture<Map<ResourceLocation, Tag.Builder>> debug7 = this.tagsLoader.prepare(debug2, debug5);
/*     */ 
/*     */ 
/*     */     
/*  75 */     CompletableFuture<Map<ResourceLocation, CompletableFuture<CommandFunction>>> debug8 = CompletableFuture.supplyAsync(() -> debug0.listResources("functions", ()), debug5).thenCompose(debug3 -> {
/*     */           Map<ResourceLocation, CompletableFuture<CommandFunction>> debug4 = Maps.newHashMap();
/*     */ 
/*     */           
/*     */           CommandSourceStack debug5 = new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, this.functionCompilationLevel, "", TextComponent.EMPTY, null, null);
/*     */ 
/*     */           
/*     */           for (ResourceLocation debug7 : debug3) {
/*     */             String debug8 = debug7.getPath();
/*     */             
/*     */             ResourceLocation debug9 = new ResourceLocation(debug7.getNamespace(), debug8.substring(PATH_PREFIX_LENGTH, debug8.length() - PATH_SUFFIX_LENGTH));
/*     */             
/*     */             debug4.put(debug9, CompletableFuture.supplyAsync((), debug2));
/*     */           } 
/*     */           
/*     */           CompletableFuture[] arrayOfCompletableFuture = (CompletableFuture[])debug4.values().toArray((Object[])new CompletableFuture[0]);
/*     */           
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).handle(());
/*     */         });
/*     */     
/*  95 */     return debug7.thenCombine(debug8, Pair::of)
/*  96 */       .thenCompose(debug1::wait)
/*  97 */       .thenAcceptAsync(debug1 -> {
/*     */           Map<ResourceLocation, CompletableFuture<CommandFunction>> debug2 = (Map<ResourceLocation, CompletableFuture<CommandFunction>>)debug1.getSecond();
/*     */           ImmutableMap.Builder<ResourceLocation, CommandFunction> debug3 = ImmutableMap.builder();
/*     */           debug2.forEach(());
/*     */           this.functions = (Map<ResourceLocation, CommandFunction>)debug3.build();
/*     */           this.tags = this.tagsLoader.load((Map)debug1.getFirst());
/*     */         }debug6);
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
/*     */   private static List<String> readLines(ResourceManager debug0, ResourceLocation debug1) {
/* 117 */     try (Resource debug2 = debug0.getResource(debug1)) {
/* 118 */       return IOUtils.readLines(debug2.getInputStream(), StandardCharsets.UTF_8);
/* 119 */     } catch (IOException debug2) {
/* 120 */       throw new CompletionException(debug2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerFunctionLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */