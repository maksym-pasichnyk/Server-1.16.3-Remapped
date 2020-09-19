/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class DiggingEnchantment extends Enchantment {
/*    */   protected DiggingEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/*  9 */     super(debug1, EnchantmentCategory.DIGGER, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 14 */     return 1 + 10 * (debug1 - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 19 */     return super.getMinCost(debug1) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 24 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEnchant(ItemStack debug1) {
/* 29 */     if (debug1.getItem() == Items.SHEARS) {
/* 30 */       return true;
/*    */     }
/* 32 */     return super.canEnchant(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\DiggingEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */