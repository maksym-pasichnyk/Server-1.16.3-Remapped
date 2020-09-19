/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class SweepingEdgeEnchantment extends Enchantment {
/*    */   public SweepingEdgeEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  7 */     super(debug1, EnchantmentCategory.WEAPON, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 12 */     return 5 + (debug1 - 1) * 9;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 17 */     return getMinCost(debug1) + 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 22 */     return 3;
/*    */   }
/*    */   
/*    */   public static float getSweepingDamageRatio(int debug0) {
/* 26 */     return 1.0F - 1.0F / (debug0 + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\SweepingEdgeEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */