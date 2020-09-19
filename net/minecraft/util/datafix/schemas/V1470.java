/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1470
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1470(int debug1, Schema debug2) {
/* 14 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 18 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 23 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */ 
/*    */     
/* 26 */     registerMob(debug1, debug2, "minecraft:turtle");
/* 27 */     registerMob(debug1, debug2, "minecraft:cod_mob");
/* 28 */     registerMob(debug1, debug2, "minecraft:tropical_fish");
/* 29 */     registerMob(debug1, debug2, "minecraft:salmon_mob");
/* 30 */     registerMob(debug1, debug2, "minecraft:puffer_fish");
/* 31 */     registerMob(debug1, debug2, "minecraft:phantom");
/* 32 */     registerMob(debug1, debug2, "minecraft:dolphin");
/* 33 */     registerMob(debug1, debug2, "minecraft:drowned");
/*    */     
/* 35 */     debug1.register(debug2, "minecraft:trident", debug1 -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 39 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1470.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */