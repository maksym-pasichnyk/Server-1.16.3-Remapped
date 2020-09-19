/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class SetItemDamageFunction extends LootItemConditionalFunction {
/* 16 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final RandomValueBounds damage;
/*    */   
/*    */   private SetItemDamageFunction(LootItemCondition[] debug1, RandomValueBounds debug2) {
/* 21 */     super(debug1);
/* 22 */     this.damage = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 27 */     return LootItemFunctions.SET_DAMAGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 32 */     if (debug1.isDamageableItem()) {
/* 33 */       float debug3 = 1.0F - this.damage.getFloat(debug2.getRandom());
/* 34 */       debug1.setDamageValue(Mth.floor(debug3 * debug1.getMaxDamage()));
/*    */     } else {
/* 36 */       LOGGER.warn("Couldn't set damage of loot item {}", debug1);
/*    */     } 
/* 38 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(RandomValueBounds debug0) {
/* 42 */     return simpleBuilder(debug1 -> new SetItemDamageFunction(debug1, debug0));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SetItemDamageFunction> {
/*    */     public void serialize(JsonObject debug1, SetItemDamageFunction debug2, JsonSerializationContext debug3) {
/* 48 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 50 */       debug1.add("damage", debug3.serialize(debug2.damage));
/*    */     }
/*    */ 
/*    */     
/*    */     public SetItemDamageFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 55 */       return new SetItemDamageFunction(debug3, (RandomValueBounds)GsonHelper.getAsObject(debug1, "damage", debug2, RandomValueBounds.class));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetItemDamageFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */