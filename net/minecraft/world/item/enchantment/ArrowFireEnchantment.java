/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class ArrowFireEnchantment extends Enchantment {
/*    */   public ArrowFireEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  7 */     super(debug1, EnchantmentCategory.BOW, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 12 */     return 20;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 17 */     return 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 22 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\ArrowFireEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */