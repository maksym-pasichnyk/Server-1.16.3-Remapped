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
/*    */ public class V1906
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1906(int debug1, Schema debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 20 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 22 */     registerInventory(debug1, debug2, "minecraft:barrel");
/* 23 */     registerInventory(debug1, debug2, "minecraft:smoker");
/* 24 */     registerInventory(debug1, debug2, "minecraft:blast_furnace");
/*    */     
/* 26 */     debug1.register(debug2, "minecraft:lectern", debug1 -> DSL.optionalFields("Book", References.ITEM_STACK.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 30 */     debug1.registerSimple(debug2, "minecraft:bell");
/*    */     
/* 32 */     return debug2;
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 36 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1906.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */