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
/*    */ public class V1800
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1800(int debug1, Schema debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 19 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 24 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */     
/* 26 */     registerMob(debug1, debug2, "minecraft:panda");
/* 27 */     debug1.register(debug2, "minecraft:pillager", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1800.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */