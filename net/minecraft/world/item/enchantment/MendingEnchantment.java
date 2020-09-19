/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class MendingEnchantment extends Enchantment {
/*    */   public MendingEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  7 */     super(debug1, EnchantmentCategory.BREAKABLE, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 12 */     return debug1 * 25;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 17 */     return getMinCost(debug1) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTreasureOnly() {
/* 22 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 27 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\MendingEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */