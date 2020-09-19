/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.SmeltingRecipe;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class SmeltItemFunction extends LootItemConditionalFunction {
/* 17 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private SmeltItemFunction(LootItemCondition[] debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 25 */     return LootItemFunctions.FURNACE_SMELT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 30 */     if (debug1.isEmpty()) {
/* 31 */       return debug1;
/*    */     }
/*    */     
/* 34 */     Optional<SmeltingRecipe> debug3 = debug2.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, (Container)new SimpleContainer(new ItemStack[] { debug1 }, ), (Level)debug2.getLevel());
/* 35 */     if (debug3.isPresent()) {
/* 36 */       ItemStack debug4 = ((SmeltingRecipe)debug3.get()).getResultItem();
/*    */       
/* 38 */       if (!debug4.isEmpty()) {
/* 39 */         ItemStack debug5 = debug4.copy();
/* 40 */         debug5.setCount(debug1.getCount());
/* 41 */         return debug5;
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", debug1);
/* 46 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> smelted() {
/* 50 */     return simpleBuilder(SmeltItemFunction::new);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SmeltItemFunction> {
/*    */     public SmeltItemFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 56 */       return new SmeltItemFunction(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SmeltItemFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */