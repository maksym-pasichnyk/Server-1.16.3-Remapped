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
/*    */ public class V808
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V808(int debug1, Schema debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 19 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 26 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 28 */     registerInventory(debug1, debug2, "minecraft:shulker_box");
/*    */     
/* 30 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V808.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */