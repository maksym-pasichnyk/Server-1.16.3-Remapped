/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerLootTable
/*    */   extends LootItemConditionalFunction {
/*    */   private final ResourceLocation name;
/*    */   private final long seed;
/*    */   
/*    */   private SetContainerLootTable(LootItemCondition[] debug1, ResourceLocation debug2, long debug3) {
/* 22 */     super(debug1);
/* 23 */     this.name = debug2;
/* 24 */     this.seed = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 29 */     return LootItemFunctions.SET_LOOT_TABLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 34 */     if (debug1.isEmpty()) {
/* 35 */       return debug1;
/*    */     }
/*    */     
/* 38 */     CompoundTag debug3 = new CompoundTag();
/* 39 */     debug3.putString("LootTable", this.name.toString());
/* 40 */     if (this.seed != 0L) {
/* 41 */       debug3.putLong("LootTableSeed", this.seed);
/*    */     }
/* 43 */     debug1.getOrCreateTag().put("BlockEntityTag", (Tag)debug3);
/* 44 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 49 */     if (debug1.hasVisitedTable(this.name)) {
/* 50 */       debug1.reportProblem("Table " + this.name + " is recursively called");
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     super.validate(debug1);
/*    */     
/* 56 */     LootTable debug2 = debug1.resolveLootTable(this.name);
/* 57 */     if (debug2 == null) {
/* 58 */       debug1.reportProblem("Unknown loot table called " + this.name);
/*    */     } else {
/* 60 */       debug2.validate(debug1.enterTable("->{" + this.name + "}", this.name));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SetContainerLootTable>
/*    */   {
/*    */     public void serialize(JsonObject debug1, SetContainerLootTable debug2, JsonSerializationContext debug3) {
/* 75 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 77 */       debug1.addProperty("name", debug2.name.toString());
/* 78 */       if (debug2.seed != 0L) {
/* 79 */         debug1.addProperty("seed", Long.valueOf(debug2.seed));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public SetContainerLootTable deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 85 */       ResourceLocation debug4 = new ResourceLocation(GsonHelper.getAsString(debug1, "name"));
/* 86 */       long debug5 = GsonHelper.getAsLong(debug1, "seed", 0L);
/* 87 */       return new SetContainerLootTable(debug3, debug4, debug5);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerLootTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */