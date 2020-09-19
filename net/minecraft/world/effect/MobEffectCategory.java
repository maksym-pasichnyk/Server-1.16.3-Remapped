/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public enum MobEffectCategory {
/*  6 */   BENEFICIAL(ChatFormatting.BLUE),
/*  7 */   HARMFUL(ChatFormatting.RED),
/*  8 */   NEUTRAL(ChatFormatting.BLUE);
/*    */   
/*    */   private final ChatFormatting tooltipFormatting;
/*    */   
/*    */   MobEffectCategory(ChatFormatting debug3) {
/* 13 */     this.tooltipFormatting = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\MobEffectCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */