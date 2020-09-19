/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.MobType;
/*    */ 
/*    */ public class TridentImpalerEnchantment extends Enchantment {
/*    */   public TridentImpalerEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  8 */     super(debug1, EnchantmentCategory.TRIDENT, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 13 */     return 1 + (debug1 - 1) * 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 18 */     return getMinCost(debug1) + 20;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 23 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDamageBonus(int debug1, MobType debug2) {
/* 28 */     if (debug2 == MobType.WATER) {
/* 29 */       return debug1 * 2.5F;
/*    */     }
/* 31 */     return 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\TridentImpalerEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */