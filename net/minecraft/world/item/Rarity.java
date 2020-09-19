/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public enum Rarity {
/*  6 */   COMMON(ChatFormatting.WHITE),
/*  7 */   UNCOMMON(ChatFormatting.YELLOW),
/*  8 */   RARE(ChatFormatting.AQUA),
/*  9 */   EPIC(ChatFormatting.LIGHT_PURPLE);
/*    */   
/*    */   public final ChatFormatting color;
/*    */   
/*    */   Rarity(ChatFormatting debug3) {
/* 14 */     this.color = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\Rarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */