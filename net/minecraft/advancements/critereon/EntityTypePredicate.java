/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.SerializationTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public abstract class EntityTypePredicate
/*    */ {
/* 18 */   public static final EntityTypePredicate ANY = new EntityTypePredicate()
/*    */     {
/*    */       public boolean matches(EntityType<?> debug1) {
/* 21 */         return true;
/*    */       }
/*    */ 
/*    */       
/*    */       public JsonElement serializeToJson() {
/* 26 */         return (JsonElement)JsonNull.INSTANCE;
/*    */       }
/*    */     };
/*    */   
/* 30 */   private static final Joiner COMMA_JOINER = Joiner.on(", ");
/*    */ 
/*    */   
/*    */   static class TypePredicate
/*    */     extends EntityTypePredicate
/*    */   {
/*    */     private final EntityType<?> type;
/*    */ 
/*    */     
/*    */     public TypePredicate(EntityType<?> debug1) {
/* 40 */       this.type = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(EntityType<?> debug1) {
/* 45 */       return (this.type == debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonElement serializeToJson() {
/* 50 */       return (JsonElement)new JsonPrimitive(Registry.ENTITY_TYPE.getKey(this.type).toString());
/*    */     }
/*    */   }
/*    */   
/*    */   static class TagPredicate extends EntityTypePredicate {
/*    */     private final Tag<EntityType<?>> tag;
/*    */     
/*    */     public TagPredicate(Tag<EntityType<?>> debug1) {
/* 58 */       this.tag = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(EntityType<?> debug1) {
/* 63 */       return this.tag.contains(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonElement serializeToJson() {
/* 68 */       return (JsonElement)new JsonPrimitive("#" + SerializationTags.getInstance().getEntityTypes().getIdOrThrow(this.tag));
/*    */     }
/*    */   }
/*    */   
/*    */   public static EntityTypePredicate fromJson(@Nullable JsonElement debug0) {
/* 73 */     if (debug0 == null || debug0.isJsonNull()) {
/* 74 */       return ANY;
/*    */     }
/*    */     
/* 77 */     String debug1 = GsonHelper.convertToString(debug0, "type");
/*    */     
/* 79 */     if (debug1.startsWith("#")) {
/* 80 */       ResourceLocation resourceLocation = new ResourceLocation(debug1.substring(1));
/* 81 */       return new TagPredicate(SerializationTags.getInstance().getEntityTypes().getTagOrEmpty(resourceLocation));
/*    */     } 
/* 83 */     ResourceLocation debug2 = new ResourceLocation(debug1);
/*    */     
/* 85 */     EntityType<?> debug3 = (EntityType)Registry.ENTITY_TYPE.getOptional(debug2).orElseThrow(() -> new JsonSyntaxException("Unknown entity type '" + debug0 + "', valid types are: " + COMMA_JOINER.join(Registry.ENTITY_TYPE.keySet())));
/* 86 */     return new TypePredicate(debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public static EntityTypePredicate of(EntityType<?> debug0) {
/* 91 */     return new TypePredicate(debug0);
/*    */   }
/*    */   
/*    */   public static EntityTypePredicate of(Tag<EntityType<?>> debug0) {
/* 95 */     return new TagPredicate(debug0);
/*    */   }
/*    */   
/*    */   public abstract boolean matches(EntityType<?> paramEntityType);
/*    */   
/*    */   public abstract JsonElement serializeToJson();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EntityTypePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */