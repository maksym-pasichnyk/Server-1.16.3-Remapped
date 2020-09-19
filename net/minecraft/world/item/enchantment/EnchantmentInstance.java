/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.util.WeighedRandom;
/*    */ 
/*    */ public class EnchantmentInstance extends WeighedRandom.WeighedRandomItem {
/*    */   public final Enchantment enchantment;
/*    */   public final int level;
/*    */   
/*    */   public EnchantmentInstance(Enchantment debug1, int debug2) {
/* 10 */     super(debug1.getRarity().getWeight());
/* 11 */     this.enchantment = debug1;
/* 12 */     this.level = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */