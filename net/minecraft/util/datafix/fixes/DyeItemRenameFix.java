/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class DyeItemRenameFix
/*    */ {
/*  8 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/*  9 */     .put("minecraft:cactus_green", "minecraft:green_dye")
/* 10 */     .put("minecraft:rose_red", "minecraft:red_dye")
/* 11 */     .put("minecraft:dandelion_yellow", "minecraft:yellow_dye")
/* 12 */     .build();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\DyeItemRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */