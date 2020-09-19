/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class BonusLevelTableCondition
/*    */   implements LootItemCondition
/*    */ {
/*    */   private final Enchantment enchantment;
/*    */   private final float[] values;
/*    */   
/*    */   private BonusLevelTableCondition(Enchantment debug1, float[] debug2) {
/* 26 */     this.enchantment = debug1;
/* 27 */     this.values = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 32 */     return LootItemConditions.TABLE_BONUS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 37 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.TOOL);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 42 */     ItemStack debug2 = (ItemStack)debug1.getParamOrNull(LootContextParams.TOOL);
/*    */     
/* 44 */     int debug3 = (debug2 != null) ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, debug2) : 0;
/* 45 */     float debug4 = this.values[Math.min(debug3, this.values.length - 1)];
/* 46 */     return (debug1.getRandom().nextFloat() < debug4);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder bonusLevelFlatChance(Enchantment debug0, float... debug1) {
/* 50 */     return () -> new BonusLevelTableCondition(debug0, debug1);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<BonusLevelTableCondition> {
/*    */     public void serialize(JsonObject debug1, BonusLevelTableCondition debug2, JsonSerializationContext debug3) {
/* 56 */       debug1.addProperty("enchantment", Registry.ENCHANTMENT.getKey(debug2.enchantment).toString());
/* 57 */       debug1.add("chances", debug3.serialize(debug2.values));
/*    */     }
/*    */ 
/*    */     
/*    */     public BonusLevelTableCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 62 */       ResourceLocation debug3 = new ResourceLocation(GsonHelper.getAsString(debug1, "enchantment"));
/*    */       
/* 64 */       Enchantment debug4 = (Enchantment)Registry.ENCHANTMENT.getOptional(debug3).orElseThrow(() -> new JsonParseException("Invalid enchantment id: " + debug0));
/* 65 */       float[] debug5 = (float[])GsonHelper.getAsObject(debug1, "chances", debug2, float[].class);
/* 66 */       return new BonusLevelTableCondition(debug4, debug5);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\BonusLevelTableCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */