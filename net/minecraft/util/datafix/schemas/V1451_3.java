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
/*    */ public class V1451_3
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_3(int debug1, Schema debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 23 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */ 
/*    */     
/* 26 */     debug1.registerSimple(debug2, "minecraft:egg");
/* 27 */     debug1.registerSimple(debug2, "minecraft:ender_pearl");
/* 28 */     debug1.registerSimple(debug2, "minecraft:fireball");
/* 29 */     debug1.register(debug2, "minecraft:potion", debug1 -> DSL.optionalFields("Potion", References.ITEM_STACK.in(debug0)));
/*    */ 
/*    */     
/* 32 */     debug1.registerSimple(debug2, "minecraft:small_fireball");
/* 33 */     debug1.registerSimple(debug2, "minecraft:snowball");
/* 34 */     debug1.registerSimple(debug2, "minecraft:wither_skull");
/* 35 */     debug1.registerSimple(debug2, "minecraft:xp_bottle");
/*    */     
/* 37 */     debug1.register(debug2, "minecraft:arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */     
/* 40 */     debug1.register(debug2, "minecraft:enderman", () -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(debug0), V100.equipment(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 44 */     debug1.register(debug2, "minecraft:falling_block", () -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(debug0), "TileEntityData", References.BLOCK_ENTITY.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 48 */     debug1.register(debug2, "minecraft:spectral_arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */     
/* 51 */     debug1.register(debug2, "minecraft:chest_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*    */ 
/*    */ 
/*    */     
/* 55 */     debug1.register(debug2, "minecraft:commandblock_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */     
/* 58 */     debug1.register(debug2, "minecraft:furnace_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */     
/* 61 */     debug1.register(debug2, "minecraft:hopper_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*    */ 
/*    */ 
/*    */     
/* 65 */     debug1.register(debug2, "minecraft:minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */     
/* 68 */     debug1.register(debug2, "minecraft:spawner_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), References.UNTAGGED_SPAWNER.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 72 */     debug1.register(debug2, "minecraft:tnt_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 76 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */