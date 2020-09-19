/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityZombifiedPiglinRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityZombifiedPiglinRenameFix(Schema debug1) {
/* 15 */     super("EntityZombifiedPiglinRenameFix", debug1, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String debug1) {
/* 20 */     return Objects.equals("minecraft:zombie_pigman", debug1) ? "minecraft:zombified_piglin" : debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityZombifiedPiglinRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */