/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1510
/*    */   extends NamespacedSchema {
/*    */   public V1510(int debug1, Schema debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 16 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */     
/* 18 */     debug2.put("minecraft:command_block_minecart", debug2.remove("minecraft:commandblock_minecart"));
/* 19 */     debug2.put("minecraft:end_crystal", debug2.remove("minecraft:ender_crystal"));
/* 20 */     debug2.put("minecraft:snow_golem", debug2.remove("minecraft:snowman"));
/* 21 */     debug2.put("minecraft:evoker", debug2.remove("minecraft:evocation_illager"));
/* 22 */     debug2.put("minecraft:evoker_fangs", debug2.remove("minecraft:evocation_fangs"));
/* 23 */     debug2.put("minecraft:illusioner", debug2.remove("minecraft:illusion_illager"));
/* 24 */     debug2.put("minecraft:vindicator", debug2.remove("minecraft:vindication_illager"));
/* 25 */     debug2.put("minecraft:iron_golem", debug2.remove("minecraft:villager_golem"));
/* 26 */     debug2.put("minecraft:experience_orb", debug2.remove("minecraft:xp_orb"));
/* 27 */     debug2.put("minecraft:experience_bottle", debug2.remove("minecraft:xp_bottle"));
/* 28 */     debug2.put("minecraft:eye_of_ender", debug2.remove("minecraft:eye_of_ender_signal"));
/* 29 */     debug2.put("minecraft:firework_rocket", debug2.remove("minecraft:fireworks_rocket"));
/*    */     
/* 31 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1510.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */