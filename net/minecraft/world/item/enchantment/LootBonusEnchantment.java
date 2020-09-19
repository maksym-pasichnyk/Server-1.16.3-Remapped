/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class LootBonusEnchantment extends Enchantment {
/*    */   protected LootBonusEnchantment(Enchantment.Rarity debug1, EnchantmentCategory debug2, EquipmentSlot... debug3) {
/*  7 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 12 */     return 15 + (debug1 - 1) * 9;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 17 */     return super.getMinCost(debug1) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 22 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkCompatibility(Enchantment debug1) {
/* 27 */     return (super.checkCompatibility(debug1) && debug1 != Enchantments.SILK_TOUCH);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\LootBonusEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */