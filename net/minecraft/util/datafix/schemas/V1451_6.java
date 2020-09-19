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
/*    */ public class V1451_6
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_6(int debug1, Schema debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 25 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 27 */     Supplier<TypeTemplate> debug4 = () -> DSL.compoundList(References.ITEM_NAME.in(debug0), DSL.constType(DSL.intType()));
/*    */     
/* 29 */     debug1.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:crafted", debug1.get(), "minecraft:used", debug1.get(), "minecraft:broken", debug1.get(), "minecraft:picked_up", debug1.get(), DSL.optionalFields("minecraft:dropped", debug1.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */