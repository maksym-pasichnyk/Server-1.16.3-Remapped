/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class SoulSpeedEnchantment extends Enchantment {
/*    */   public SoulSpeedEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
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
/*    */   public boolean isTreasureOnly() {
/* 22 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTradeable() {
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDiscoverable() {
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 37 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\SoulSpeedEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */