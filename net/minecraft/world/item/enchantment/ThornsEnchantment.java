/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThornsEnchantment
/*    */   extends Enchantment
/*    */ {
/*    */   public ThornsEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/* 17 */     super(debug1, EnchantmentCategory.ARMOR_CHEST, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 22 */     return 10 + 20 * (debug1 - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 27 */     return super.getMinCost(debug1) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 32 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEnchant(ItemStack debug1) {
/* 37 */     if (debug1.getItem() instanceof net.minecraft.world.item.ArmorItem) {
/* 38 */       return true;
/*    */     }
/* 40 */     return super.canEnchant(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doPostHurt(LivingEntity debug1, Entity debug2, int debug3) {
/* 45 */     Random debug4 = debug1.getRandom();
/* 46 */     Map.Entry<EquipmentSlot, ItemStack> debug5 = EnchantmentHelper.getRandomItemWith(Enchantments.THORNS, debug1);
/*    */     
/* 48 */     if (shouldHit(debug3, debug4)) {
/* 49 */       if (debug2 != null) {
/* 50 */         debug2.hurt(DamageSource.thorns((Entity)debug1), getDamage(debug3, debug4));
/*    */       }
/*    */       
/* 53 */       if (debug5 != null) {
/* 54 */         ((ItemStack)debug5.getValue()).hurtAndBreak(2, debug1, debug1 -> debug1.broadcastBreakEvent((EquipmentSlot)debug0.getKey()));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean shouldHit(int debug0, Random debug1) {
/* 60 */     if (debug0 <= 0) {
/* 61 */       return false;
/*    */     }
/* 63 */     return (debug1.nextFloat() < 0.15F * debug0);
/*    */   }
/*    */   
/*    */   public static int getDamage(int debug0, Random debug1) {
/* 67 */     if (debug0 > 10) {
/* 68 */       return debug0 - 10;
/*    */     }
/* 70 */     return 1 + debug1.nextInt(4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\ThornsEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */