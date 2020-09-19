/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class DigDurabilityEnchantment
/*    */   extends Enchantment
/*    */ {
/*    */   protected DigDurabilityEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/* 11 */     super(debug1, EnchantmentCategory.BREAKABLE, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 16 */     return 5 + (debug1 - 1) * 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 21 */     return super.getMinCost(debug1) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 26 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEnchant(ItemStack debug1) {
/* 31 */     if (debug1.isDamageableItem()) {
/* 32 */       return true;
/*    */     }
/* 34 */     return super.canEnchant(debug1);
/*    */   }
/*    */   
/*    */   public static boolean shouldIgnoreDurabilityDrop(ItemStack debug0, int debug1, Random debug2) {
/* 38 */     if (debug0.getItem() instanceof net.minecraft.world.item.ArmorItem && debug2.nextFloat() < 0.6F) {
/* 39 */       return false;
/*    */     }
/* 41 */     return (debug2.nextInt(debug1 + 1) > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\DigDurabilityEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */