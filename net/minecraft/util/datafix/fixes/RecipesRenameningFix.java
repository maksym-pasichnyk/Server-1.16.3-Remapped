/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RecipesRenameningFix
/*    */   extends RecipesRenameFix {
/*  9 */   private static final Map<String, String> RECIPES = (Map<String, String>)ImmutableMap.builder()
/* 10 */     .put("minecraft:acacia_bark", "minecraft:acacia_wood")
/* 11 */     .put("minecraft:birch_bark", "minecraft:birch_wood")
/* 12 */     .put("minecraft:dark_oak_bark", "minecraft:dark_oak_wood")
/* 13 */     .put("minecraft:jungle_bark", "minecraft:jungle_wood")
/* 14 */     .put("minecraft:oak_bark", "minecraft:oak_wood")
/* 15 */     .put("minecraft:spruce_bark", "minecraft:spruce_wood")
/* 16 */     .build();
/*    */   
/*    */   public RecipesRenameningFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2, "Recipes renamening fix", debug0 -> (String)RECIPES.getOrDefault(debug0, debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\RecipesRenameningFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */