/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.SerializationTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class TagEntry extends LootPoolSingletonContainer {
/*    */   private final Tag<Item> tag;
/*    */   private final boolean expand;
/*    */   
/*    */   private TagEntry(Tag<Item> debug1, boolean debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 24 */     super(debug3, debug4, debug5, debug6);
/* 25 */     this.tag = debug1;
/* 26 */     this.expand = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 31 */     return LootPoolEntries.TAG;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/* 36 */     this.tag.getValues().forEach(debug1 -> debug0.accept(new ItemStack((ItemLike)debug1)));
/*    */   }
/*    */   
/*    */   private boolean expandTag(LootContext debug1, Consumer<LootPoolEntry> debug2) {
/* 40 */     if (canRun(debug1)) {
/* 41 */       for (Item debug4 : this.tag.getValues()) {
/* 42 */         debug2.accept(new LootPoolSingletonContainer.EntryBase()
/*    */             {
/*    */               public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/* 45 */                 debug1.accept(new ItemStack((ItemLike)item));
/*    */               }
/*    */             });
/*    */       } 
/* 49 */       return true;
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean expand(LootContext debug1, Consumer<LootPoolEntry> debug2) {
/* 56 */     if (this.expand) {
/* 57 */       return expandTag(debug1, debug2);
/*    */     }
/* 59 */     return super.expand(debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> expandTag(Tag<Item> debug0) {
/* 68 */     return simpleBuilder((debug1, debug2, debug3, debug4) -> new TagEntry(debug0, true, debug1, debug2, debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootPoolSingletonContainer.Serializer<TagEntry> {
/*    */     public void serializeCustom(JsonObject debug1, TagEntry debug2, JsonSerializationContext debug3) {
/* 74 */       super.serializeCustom(debug1, debug2, debug3);
/*    */       
/* 76 */       debug1.addProperty("name", SerializationTags.getInstance().getItems().getIdOrThrow(debug2.tag).toString());
/* 77 */       debug1.addProperty("expand", Boolean.valueOf(debug2.expand));
/*    */     }
/*    */ 
/*    */     
/*    */     protected TagEntry deserialize(JsonObject debug1, JsonDeserializationContext debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 82 */       ResourceLocation debug7 = new ResourceLocation(GsonHelper.getAsString(debug1, "name"));
/*    */       
/* 84 */       Tag<Item> debug8 = SerializationTags.getInstance().getItems().getTag(debug7);
/* 85 */       if (debug8 == null) {
/* 86 */         throw new JsonParseException("Can't find tag: " + debug7);
/*    */       }
/*    */       
/* 89 */       boolean debug9 = GsonHelper.getAsBoolean(debug1, "expand");
/*    */       
/* 91 */       return new TagEntry(debug8, debug9, debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\TagEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */