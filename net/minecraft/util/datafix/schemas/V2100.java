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
/*    */ public class V2100
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2100(int debug1, Schema debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 20 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 25 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/* 26 */     registerMob(debug1, debug2, "minecraft:bee");
/* 27 */     registerMob(debug1, debug2, "minecraft:bee_stinger");
/* 28 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 33 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 35 */     debug1.register(debug2, "minecraft:beehive", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "Bees", DSL.list(DSL.optionalFields("EntityData", References.ENTITY_TREE.in(debug0)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2100.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */