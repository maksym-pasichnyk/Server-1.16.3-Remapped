/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class TagManager
/*    */   implements PreparableReloadListener {
/* 20 */   private final TagLoader<Block> blocks = new TagLoader<>(Registry.BLOCK::getOptional, "tags/blocks", "block");
/* 21 */   private final TagLoader<Item> items = new TagLoader<>(Registry.ITEM::getOptional, "tags/items", "item");
/* 22 */   private final TagLoader<Fluid> fluids = new TagLoader<>(Registry.FLUID::getOptional, "tags/fluids", "fluid");
/* 23 */   private final TagLoader<EntityType<?>> entityTypes = new TagLoader<>(Registry.ENTITY_TYPE::getOptional, "tags/entity_types", "entity_type");
/*    */   
/* 25 */   private TagContainer tags = TagContainer.EMPTY;
/*    */   
/*    */   public TagContainer getTags() {
/* 28 */     return this.tags;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier debug1, ResourceManager debug2, ProfilerFiller debug3, ProfilerFiller debug4, Executor debug5, Executor debug6) {
/* 33 */     CompletableFuture<Map<ResourceLocation, Tag.Builder>> debug7 = this.blocks.prepare(debug2, debug5);
/* 34 */     CompletableFuture<Map<ResourceLocation, Tag.Builder>> debug8 = this.items.prepare(debug2, debug5);
/* 35 */     CompletableFuture<Map<ResourceLocation, Tag.Builder>> debug9 = this.fluids.prepare(debug2, debug5);
/* 36 */     CompletableFuture<Map<ResourceLocation, Tag.Builder>> debug10 = this.entityTypes.prepare(debug2, debug5);
/* 37 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { debug7, debug8, debug9, debug10
/* 38 */         }).thenCompose(debug1::wait)
/* 39 */       .thenAcceptAsync(debug5 -> {
/*    */           TagCollection<Block> debug6 = this.blocks.load(debug1.join());
/*    */           TagCollection<Item> debug7 = this.items.load(debug2.join());
/*    */           TagCollection<Fluid> debug8 = this.fluids.load(debug3.join());
/*    */           TagCollection<EntityType<?>> debug9 = this.entityTypes.load(debug4.join());
/*    */           TagContainer debug10 = TagContainer.of(debug6, debug7, debug8, debug9);
/*    */           Multimap<ResourceLocation, ResourceLocation> debug11 = StaticTags.getAllMissingTags(debug10);
/*    */           if (!debug11.isEmpty())
/*    */             throw new IllegalStateException("Missing required tags: " + (String)debug11.entries().stream().map(()).sorted().collect(Collectors.joining(","))); 
/*    */           SerializationTags.bind(debug10);
/*    */           this.tags = debug10;
/*    */         }debug6);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\TagManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */