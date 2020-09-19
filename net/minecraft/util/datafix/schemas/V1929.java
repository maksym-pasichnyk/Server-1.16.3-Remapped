/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ public class V1929
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1929(int debug1, Schema debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 20 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/* 21 */     debug1.register(debug2, "minecraft:wandering_trader", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(debug0), "buyB", References.ITEM_STACK.in(debug0), "sell", References.ITEM_STACK.in(debug0)))), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     debug1.register(debug2, "minecraft:trader_llama", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), "DecorItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1929.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */