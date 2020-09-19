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
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootTableReference
/*    */   extends LootPoolSingletonContainer {
/*    */   private final ResourceLocation name;
/*    */   
/*    */   private LootTableReference(ResourceLocation debug1, int debug2, int debug3, LootItemCondition[] debug4, LootItemFunction[] debug5) {
/* 21 */     super(debug2, debug3, debug4, debug5);
/* 22 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 27 */     return LootPoolEntries.REFERENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/* 32 */     LootTable debug3 = debug2.getLootTable(this.name);
/* 33 */     debug3.getRandomItemsRaw(debug2, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 38 */     if (debug1.hasVisitedTable(this.name)) {
/* 39 */       debug1.reportProblem("Table " + this.name + " is recursively called");
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     super.validate(debug1);
/*    */     
/* 45 */     LootTable debug2 = debug1.resolveLootTable(this.name);
/* 46 */     if (debug2 == null) {
/* 47 */       debug1.reportProblem("Unknown loot table called " + this.name);
/*    */     } else {
/* 49 */       debug2.validate(debug1.enterTable("->{" + this.name + "}", this.name));
/*    */     } 
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceLocation debug0) {
/* 54 */     return simpleBuilder((debug1, debug2, debug3, debug4) -> new LootTableReference(debug0, debug1, debug2, debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootPoolSingletonContainer.Serializer<LootTableReference> {
/*    */     public void serializeCustom(JsonObject debug1, LootTableReference debug2, JsonSerializationContext debug3) {
/* 60 */       super.serializeCustom(debug1, debug2, debug3);
/* 61 */       debug1.addProperty("name", debug2.name.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootTableReference deserialize(JsonObject debug1, JsonDeserializationContext debug2, int debug3, int debug4, LootItemCondition[] debug5, LootItemFunction[] debug6) {
/* 66 */       ResourceLocation debug7 = new ResourceLocation(GsonHelper.getAsString(debug1, "name"));
/* 67 */       return new LootTableReference(debug7, debug3, debug4, debug5, debug6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootTableReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */