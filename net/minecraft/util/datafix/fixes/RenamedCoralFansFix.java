/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RenamedCoralFansFix
/*    */ {
/*  8 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/*  9 */     .put("minecraft:tube_coral_fan", "minecraft:tube_coral_wall_fan")
/* 10 */     .put("minecraft:brain_coral_fan", "minecraft:brain_coral_wall_fan")
/* 11 */     .put("minecraft:bubble_coral_fan", "minecraft:bubble_coral_wall_fan")
/* 12 */     .put("minecraft:fire_coral_fan", "minecraft:fire_coral_wall_fan")
/* 13 */     .put("minecraft:horn_coral_fan", "minecraft:horn_coral_wall_fan")
/* 14 */     .build();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\RenamedCoralFansFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */