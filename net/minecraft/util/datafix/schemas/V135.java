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
/*    */ public class V135
/*    */   extends Schema
/*    */ {
/*    */   public V135(int debug1, Schema debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 23 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 25 */     debug1.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(debug0)), "Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "EnderItems", DSL.list(References.ITEM_STACK.in(debug0))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     debug1.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(debug0)), References.ENTITY.in(debug0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V135.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */