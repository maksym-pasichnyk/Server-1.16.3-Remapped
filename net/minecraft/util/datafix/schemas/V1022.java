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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1022
/*    */   extends Schema
/*    */ {
/*    */   public V1022(int debug1, Schema debug2) {
/* 22 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 27 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 29 */     debug1.registerType(false, References.RECIPE, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 30 */     debug1.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(debug0)), "Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "EnderItems", DSL.list(References.ITEM_STACK.in(debug0)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(debug0), "ShoulderEntityRight", References.ENTITY_TREE.in(debug0), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(debug0)), "toBeDisplayed", DSL.list(References.RECIPE.in(debug0))))));
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
/*    */ 
/*    */     
/* 46 */     debug1.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(debug0))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1022.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */