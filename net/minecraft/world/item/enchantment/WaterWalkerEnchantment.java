/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class WaterWalkerEnchantment extends Enchantment {
/*    */   public WaterWalkerEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  7 */     super(debug1, EnchantmentCategory.ARMOR_FEET, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 12 */     return debug1 * 10;
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
/*    */   
/*    */   public boolean checkCompatibility(Enchantment debug1) {
/* 27 */     return (super.checkCompatibility(debug1) && debug1 != Enchantments.FROST_WALKER);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\WaterWalkerEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */