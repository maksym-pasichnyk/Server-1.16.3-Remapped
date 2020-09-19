/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class EntityTypeTagsProvider
/*    */   extends TagsProvider<EntityType<?>> {
/*    */   public EntityTypeTagsProvider(DataGenerator debug1) {
/* 13 */     super(debug1, (Registry<EntityType<?>>)Registry.ENTITY_TYPE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags() {
/* 18 */     tag(EntityTypeTags.SKELETONS).add((EntityType<?>[])new EntityType[] { EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON });
/* 19 */     tag(EntityTypeTags.RAIDERS).add((EntityType<?>[])new EntityType[] { EntityType.EVOKER, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH });
/* 20 */     tag(EntityTypeTags.BEEHIVE_INHABITORS).add(EntityType.BEE);
/* 21 */     tag(EntityTypeTags.ARROWS).add((EntityType<?>[])new EntityType[] { EntityType.ARROW, EntityType.SPECTRAL_ARROW });
/* 22 */     tag(EntityTypeTags.IMPACT_PROJECTILES).addTag(EntityTypeTags.ARROWS).add(new EntityType[] { EntityType.SNOWBALL, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.EGG, EntityType.TRIDENT, EntityType.DRAGON_FIREBALL, EntityType.WITHER_SKULL });
/*    */   }
/*    */ 
/*    */   
/*    */   protected Path getPath(ResourceLocation debug1) {
/* 27 */     return this.generator.getOutputFolder().resolve("data/" + debug1.getNamespace() + "/tags/entity_types/" + debug1.getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 32 */     return "Entity Type Tags";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\tags\EntityTypeTagsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */