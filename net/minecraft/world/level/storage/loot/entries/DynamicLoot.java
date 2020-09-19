/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DynamicLoot
/*    */   extends LootPoolSingletonContainer {
/*    */   private final ResourceLocation name;
/*    */   
/*    */   private DynamicLoot(ResourceLocation debug1, int debug2, int debug3, LootItemCondition[] debug4, LootItemFunction[] debug5) {
/* 19 */     super(debug2, debug3, debug4, debug5);
/* 20 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 25 */     return LootPoolEntries.DYNAMIC;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/* 30 */     debug2.addDynamicDrops(this.name, debug1);
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> dynamicEntry(ResourceLocation debug0) {
/* 34 */     return simpleBuilder((debug1, debug2, debug3, debug4) -> new DynamicLoot(debug0, debug1, debug2, debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootPoolSingletonContainer.Serializer<DynamicLoot> {
/*    */     public void serializeCustom(JsonObject debug1, DynamicLoot debug2, JsonSerializationContext debug3) {
/* 40 */       super.serializeCustom(debug1, debug2, debug3);
/* 41 */       debug1.addProperty("name", debug2.name.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     protected DynamicLoot deserialize(JsonObject debug1, JsonDeserializationContext debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 46 */       ResourceLocation debug7 = new ResourceLocation(GsonHelper.getAsString(debug1, "name"));
/* 47 */       return new DynamicLoot(debug7, debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\DynamicLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */