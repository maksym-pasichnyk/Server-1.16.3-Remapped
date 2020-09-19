/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public interface TagContainer {
/* 12 */   public static final TagContainer EMPTY = of(TagCollection.empty(), TagCollection.empty(), TagCollection.empty(), TagCollection.empty());
/*    */   
/*    */   TagCollection<Block> getBlocks();
/*    */   
/*    */   TagCollection<Item> getItems();
/*    */   
/*    */   TagCollection<Fluid> getFluids();
/*    */   
/*    */   TagCollection<EntityType<?>> getEntityTypes();
/*    */   
/*    */   default void bindToGlobal() {
/* 23 */     StaticTags.resetAll(this);
/* 24 */     Blocks.rebuildCache();
/*    */   }
/*    */   
/*    */   default void serializeToNetwork(FriendlyByteBuf debug1) {
/* 28 */     getBlocks().serializeToNetwork(debug1, Registry.BLOCK);
/* 29 */     getItems().serializeToNetwork(debug1, Registry.ITEM);
/* 30 */     getFluids().serializeToNetwork(debug1, Registry.FLUID);
/* 31 */     getEntityTypes().serializeToNetwork(debug1, Registry.ENTITY_TYPE);
/*    */   }
/*    */   
/*    */   static TagContainer deserializeFromNetwork(FriendlyByteBuf debug0) {
/* 35 */     TagCollection<Block> debug1 = TagCollection.loadFromNetwork(debug0, (Registry<Block>)Registry.BLOCK);
/* 36 */     TagCollection<Item> debug2 = TagCollection.loadFromNetwork(debug0, (Registry<Item>)Registry.ITEM);
/* 37 */     TagCollection<Fluid> debug3 = TagCollection.loadFromNetwork(debug0, (Registry<Fluid>)Registry.FLUID);
/* 38 */     TagCollection<EntityType<?>> debug4 = TagCollection.loadFromNetwork(debug0, (Registry<EntityType<?>>)Registry.ENTITY_TYPE);
/* 39 */     return of(debug1, debug2, debug3, debug4);
/*    */   }
/*    */   
/*    */   static TagContainer of(final TagCollection<Block> blocks, final TagCollection<Item> items, final TagCollection<Fluid> fluids, final TagCollection<EntityType<?>> entityTypes) {
/* 43 */     return new TagContainer()
/*    */       {
/*    */         public TagCollection<Block> getBlocks() {
/* 46 */           return blocks;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagCollection<Item> getItems() {
/* 51 */           return items;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagCollection<Fluid> getFluids() {
/* 56 */           return fluids;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagCollection<EntityType<?>> getEntityTypes() {
/* 61 */           return entityTypes;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\TagContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */