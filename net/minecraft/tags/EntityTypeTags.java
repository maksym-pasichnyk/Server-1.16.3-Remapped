/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public final class EntityTypeTags
/*    */ {
/*  9 */   protected static final StaticTagHelper<EntityType<?>> HELPER = StaticTags.create(new ResourceLocation("entity_type"), TagContainer::getEntityTypes);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Tag.Named<EntityType<?>> SKELETONS = bind("skeletons");
/* 15 */   public static final Tag.Named<EntityType<?>> RAIDERS = bind("raiders");
/* 16 */   public static final Tag.Named<EntityType<?>> BEEHIVE_INHABITORS = bind("beehive_inhabitors");
/* 17 */   public static final Tag.Named<EntityType<?>> ARROWS = bind("arrows");
/* 18 */   public static final Tag.Named<EntityType<?>> IMPACT_PROJECTILES = bind("impact_projectiles");
/*    */   
/*    */   private static Tag.Named<EntityType<?>> bind(String debug0) {
/* 21 */     return HELPER.bind(debug0);
/*    */   }
/*    */   
/*    */   public static TagCollection<EntityType<?>> getAllTags() {
/* 25 */     return HELPER.getAllTags();
/*    */   }
/*    */   
/*    */   public static List<? extends Tag.Named<EntityType<?>>> getWrappers() {
/* 29 */     return HELPER.getWrappers();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\EntityTypeTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */