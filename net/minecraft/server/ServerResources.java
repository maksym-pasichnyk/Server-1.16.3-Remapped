/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*    */ import net.minecraft.server.packs.resources.ReloadableResourceManager;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
/*    */ import net.minecraft.tags.TagContainer;
/*    */ import net.minecraft.tags.TagManager;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import net.minecraft.world.level.storage.loot.LootTables;
/*    */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*    */ 
/*    */ public class ServerResources implements AutoCloseable {
/* 21 */   private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*    */   
/* 23 */   private final ReloadableResourceManager resources = (ReloadableResourceManager)new SimpleReloadableResourceManager(PackType.SERVER_DATA);
/*    */   
/*    */   private final Commands commands;
/* 26 */   private final RecipeManager recipes = new RecipeManager();
/* 27 */   private final TagManager tagManager = new TagManager();
/* 28 */   private final PredicateManager predicateManager = new PredicateManager();
/* 29 */   private final LootTables lootTables = new LootTables(this.predicateManager);
/* 30 */   private final ServerAdvancementManager advancements = new ServerAdvancementManager(this.predicateManager);
/*    */   private final ServerFunctionLibrary functionLibrary;
/*    */   
/*    */   public ServerResources(Commands.CommandSelection debug1, int debug2) {
/* 34 */     this.commands = new Commands(debug1);
/* 35 */     this.functionLibrary = new ServerFunctionLibrary(debug2, this.commands.getDispatcher());
/*    */     
/* 37 */     this.resources.registerReloadListener((PreparableReloadListener)this.tagManager);
/* 38 */     this.resources.registerReloadListener((PreparableReloadListener)this.predicateManager);
/* 39 */     this.resources.registerReloadListener((PreparableReloadListener)this.recipes);
/* 40 */     this.resources.registerReloadListener((PreparableReloadListener)this.lootTables);
/* 41 */     this.resources.registerReloadListener(this.functionLibrary);
/* 42 */     this.resources.registerReloadListener((PreparableReloadListener)this.advancements);
/*    */   }
/*    */   
/*    */   public ServerFunctionLibrary getFunctionLibrary() {
/* 46 */     return this.functionLibrary;
/*    */   }
/*    */   
/*    */   public PredicateManager getPredicateManager() {
/* 50 */     return this.predicateManager;
/*    */   }
/*    */   
/*    */   public LootTables getLootTables() {
/* 54 */     return this.lootTables;
/*    */   }
/*    */   
/*    */   public TagContainer getTags() {
/* 58 */     return this.tagManager.getTags();
/*    */   }
/*    */   
/*    */   public RecipeManager getRecipeManager() {
/* 62 */     return this.recipes;
/*    */   }
/*    */   
/*    */   public Commands getCommands() {
/* 66 */     return this.commands;
/*    */   }
/*    */   
/*    */   public ServerAdvancementManager getAdvancements() {
/* 70 */     return this.advancements;
/*    */   }
/*    */   
/*    */   public ResourceManager getResourceManager() {
/* 74 */     return (ResourceManager)this.resources;
/*    */   }
/*    */   
/*    */   public static CompletableFuture<ServerResources> loadResources(List<PackResources> debug0, Commands.CommandSelection debug1, int debug2, Executor debug3, Executor debug4) {
/* 78 */     ServerResources debug5 = new ServerResources(debug1, debug2);
/* 79 */     CompletableFuture<Unit> debug6 = debug5.resources.reload(debug3, debug4, debug0, DATA_RELOAD_INITIAL_TASK);
/* 80 */     return debug6.whenComplete((debug1, debug2) -> {
/*    */           if (debug2 != null) {
/*    */             debug0.close();
/*    */           }
/* 84 */         }).thenApply(debug1 -> debug0);
/*    */   }
/*    */   
/*    */   public void updateGlobals() {
/* 88 */     this.tagManager.getTags().bindToGlobal();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 93 */     this.resources.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */