/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityPufferfishRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:puffer_fish_spawn_egg", "minecraft:pufferfish_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityPufferfishRenameFix(Schema debug1, boolean debug2) {
/* 15 */     super("EntityPufferfishRenameFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String debug1) {
/* 20 */     return Objects.equals("minecraft:puffer_fish", debug1) ? "minecraft:pufferfish" : debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPufferfishRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */