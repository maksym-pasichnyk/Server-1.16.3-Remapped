/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EmptyLootItem
/*    */   extends LootPoolSingletonContainer {
/*    */   private EmptyLootItem(int debug1, int debug2, LootItemCondition[] debug3, LootItemFunction[] debug4) {
/* 14 */     super(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 19 */     return LootPoolEntries.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {}
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> emptyItem() {
/* 27 */     return simpleBuilder(EmptyLootItem::new);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootPoolSingletonContainer.Serializer<EmptyLootItem> {
/*    */     public EmptyLootItem deserialize(JsonObject debug1, JsonDeserializationContext debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 33 */       return new EmptyLootItem(debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\EmptyLootItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */