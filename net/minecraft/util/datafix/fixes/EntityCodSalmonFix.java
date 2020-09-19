/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityCodSalmonFix
/*    */   extends SimplestEntityRenameFix {
/*  9 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 10 */     .put("minecraft:salmon_mob", "minecraft:salmon")
/* 11 */     .put("minecraft:cod_mob", "minecraft:cod")
/* 12 */     .build();
/*    */   
/* 14 */   public static final Map<String, String> RENAMED_EGG_IDS = (Map<String, String>)ImmutableMap.builder()
/* 15 */     .put("minecraft:salmon_mob_spawn_egg", "minecraft:salmon_spawn_egg")
/* 16 */     .put("minecraft:cod_mob_spawn_egg", "minecraft:cod_spawn_egg")
/* 17 */     .build();
/*    */   
/*    */   public EntityCodSalmonFix(Schema debug1, boolean debug2) {
/* 20 */     super("EntityCodSalmonFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String debug1) {
/* 25 */     return RENAMED_IDS.getOrDefault(debug1, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityCodSalmonFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */