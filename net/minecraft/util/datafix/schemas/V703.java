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
/*    */ public class V703
/*    */   extends Schema
/*    */ {
/*    */   public V703(int debug1, Schema debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 20 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */     
/* 22 */     debug2.remove("EntityHorse");
/* 23 */     debug1.register(debug2, "Horse", () -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(debug0), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     debug1.register(debug2, "Donkey", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     debug1.register(debug2, "Mule", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     debug1.register(debug2, "ZombieHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 42 */     debug1.register(debug2, "SkeletonHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V703.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */