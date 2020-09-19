/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootItem
/*    */   extends LootPoolSingletonContainer {
/*    */   private final Item item;
/*    */   
/*    */   private LootItem(Item debug1, int debug2, int debug3, LootItemCondition[] debug4, LootItemFunction[] debug5) {
/* 22 */     super(debug2, debug3, debug4, debug5);
/* 23 */     this.item = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 28 */     return LootPoolEntries.ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/* 33 */     debug1.accept(new ItemStack((ItemLike)this.item));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike debug0) {
/* 37 */     return simpleBuilder((debug1, debug2, debug3, debug4) -> new LootItem(debug0.asItem(), debug1, debug2, debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootPoolSingletonContainer.Serializer<LootItem> {
/*    */     public void serializeCustom(JsonObject debug1, LootItem debug2, JsonSerializationContext debug3) {
/* 43 */       super.serializeCustom(debug1, debug2, debug3);
/*    */       
/* 45 */       ResourceLocation debug4 = Registry.ITEM.getKey(debug2.item);
/* 46 */       if (debug4 == null) {
/* 47 */         throw new IllegalArgumentException("Can't serialize unknown item " + debug2.item);
/*    */       }
/*    */       
/* 50 */       debug1.addProperty("name", debug4.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootItem deserialize(JsonObject debug1, JsonDeserializationContext debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 55 */       Item debug7 = GsonHelper.getAsItem(debug1, "name");
/* 56 */       return new LootItem(debug7, debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */