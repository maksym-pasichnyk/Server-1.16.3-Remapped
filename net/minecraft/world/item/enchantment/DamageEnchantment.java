/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DamageEnchantment
/*    */   extends Enchantment
/*    */ {
/* 18 */   private static final String[] NAMES = new String[] { "all", "undead", "arthropods" };
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final int[] MIN_COST = new int[] { 1, 5, 5 };
/*    */ 
/*    */ 
/*    */   
/* 26 */   private static final int[] LEVEL_COST = new int[] { 11, 8, 8 };
/*    */ 
/*    */ 
/*    */   
/* 30 */   private static final int[] LEVEL_COST_SPAN = new int[] { 20, 20, 20 };
/*    */ 
/*    */   
/*    */   public final int type;
/*    */ 
/*    */   
/*    */   public DamageEnchantment(Enchantment.Rarity debug1, int debug2, EquipmentSlot... debug3) {
/* 37 */     super(debug1, EnchantmentCategory.WEAPON, debug3);
/* 38 */     this.type = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 43 */     return MIN_COST[this.type] + (debug1 - 1) * LEVEL_COST[this.type];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 48 */     return getMinCost(debug1) + LEVEL_COST_SPAN[this.type];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 53 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDamageBonus(int debug1, MobType debug2) {
/* 58 */     if (this.type == 0) {
/* 59 */       return 1.0F + Math.max(0, debug1 - 1) * 0.5F;
/*    */     }
/* 61 */     if (this.type == 1 && debug2 == MobType.UNDEAD) {
/* 62 */       return debug1 * 2.5F;
/*    */     }
/* 64 */     if (this.type == 2 && debug2 == MobType.ARTHROPOD) {
/* 65 */       return debug1 * 2.5F;
/*    */     }
/* 67 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkCompatibility(Enchantment debug1) {
/* 72 */     return !(debug1 instanceof DamageEnchantment);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEnchant(ItemStack debug1) {
/* 77 */     if (debug1.getItem() instanceof net.minecraft.world.item.AxeItem) {
/* 78 */       return true;
/*    */     }
/* 80 */     return super.canEnchant(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doPostAttack(LivingEntity debug1, Entity debug2, int debug3) {
/* 85 */     if (debug2 instanceof LivingEntity) {
/* 86 */       LivingEntity debug4 = (LivingEntity)debug2;
/*    */       
/* 88 */       if (this.type == 2 && debug4.getMobType() == MobType.ARTHROPOD) {
/* 89 */         int debug5 = 20 + debug1.getRandom().nextInt(10 * debug3);
/* 90 */         debug4.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, debug5, 3));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\DamageEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */