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
/*    */ public class V2501
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2501(int debug1, Schema debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private static void registerFurnace(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 23 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "RecipesUsed", DSL.compoundList(References.RECIPE.in(debug0), DSL.constType(DSL.intType()))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 31 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/* 32 */     registerFurnace(debug1, debug2, "minecraft:furnace");
/* 33 */     registerFurnace(debug1, debug2, "minecraft:smoker");
/* 34 */     registerFurnace(debug1, debug2, "minecraft:blast_furnace");
/* 35 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2501.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */