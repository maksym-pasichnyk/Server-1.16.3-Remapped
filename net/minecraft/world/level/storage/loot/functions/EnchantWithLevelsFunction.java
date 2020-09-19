/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*    */ import net.minecraft.world.level.storage.loot.RandomIntGenerators;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EnchantWithLevelsFunction
/*    */   extends LootItemConditionalFunction {
/*    */   private final RandomIntGenerator levels;
/*    */   private final boolean treasure;
/*    */   
/*    */   private EnchantWithLevelsFunction(LootItemCondition[] debug1, RandomIntGenerator debug2, boolean debug3) {
/* 21 */     super(debug1);
/* 22 */     this.levels = debug2;
/* 23 */     this.treasure = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 28 */     return LootItemFunctions.ENCHANT_WITH_LEVELS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 33 */     Random debug3 = debug2.getRandom();
/* 34 */     return EnchantmentHelper.enchantItem(debug3, debug1, this.levels.getInt(debug3), this.treasure);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final RandomIntGenerator levels;
/*    */     private boolean treasure;
/*    */     
/*    */     public Builder(RandomIntGenerator debug1) {
/* 42 */       this.levels = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 47 */       return this;
/*    */     }
/*    */     
/*    */     public Builder allowTreasure() {
/* 51 */       this.treasure = true;
/* 52 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 57 */       return new EnchantWithLevelsFunction(getConditions(), this.levels, this.treasure);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder enchantWithLevels(RandomIntGenerator debug0) {
/* 62 */     return new Builder(debug0);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<EnchantWithLevelsFunction> {
/*    */     public void serialize(JsonObject debug1, EnchantWithLevelsFunction debug2, JsonSerializationContext debug3) {
/* 68 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 70 */       debug1.add("levels", RandomIntGenerators.serialize(debug2.levels, debug3));
/* 71 */       debug1.addProperty("treasure", Boolean.valueOf(debug2.treasure));
/*    */     }
/*    */ 
/*    */     
/*    */     public EnchantWithLevelsFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 76 */       RandomIntGenerator debug4 = RandomIntGenerators.deserialize(debug1.get("levels"), debug2);
/* 77 */       boolean debug5 = GsonHelper.getAsBoolean(debug1, "treasure", false);
/* 78 */       return new EnchantWithLevelsFunction(debug3, debug4, debug5);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */