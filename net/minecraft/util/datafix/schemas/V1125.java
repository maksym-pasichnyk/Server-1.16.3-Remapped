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
/*    */ public class V1125
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1125(int debug1, Schema debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 24 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 26 */     debug1.registerSimple(debug2, "minecraft:bed");
/*    */     
/* 28 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 33 */     super.registerTypes(debug1, debug2, debug3);
/* 34 */     debug1.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(debug0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string())))));
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
/* 48 */     debug1.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 49 */     debug1.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1125.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */