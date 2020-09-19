/*    */ package net.minecraft.tags;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class SerializationTags {
/*    */   static {
/* 12 */     instance = TagContainer.of(
/* 13 */         TagCollection.of((Map<ResourceLocation, Tag<Block>>)BlockTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, debug0 -> debug0))), 
/* 14 */         TagCollection.of((Map<ResourceLocation, Tag<Item>>)ItemTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, debug0 -> debug0))), 
/* 15 */         TagCollection.of((Map<ResourceLocation, Tag<Fluid>>)FluidTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, debug0 -> debug0))), 
/* 16 */         TagCollection.of((Map<ResourceLocation, Tag<EntityType<?>>>)EntityTypeTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, debug0 -> debug0))));
/*    */   }
/*    */   private static volatile TagContainer instance;
/*    */   public static TagContainer getInstance() {
/* 20 */     return instance;
/*    */   }
/*    */   
/*    */   public static void bind(TagContainer debug0) {
/* 24 */     instance = debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\SerializationTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */