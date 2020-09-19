/*     */ package net.minecraft.world.item.enchantment;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class ProtectionEnchantment extends Enchantment {
/*     */   public final Type type;
/*     */   
/*     */   public enum Type {
/*  10 */     ALL("all", 1, 11),
/*  11 */     FIRE("fire", 10, 8),
/*  12 */     FALL("fall", 5, 6),
/*  13 */     EXPLOSION("explosion", 5, 8),
/*  14 */     PROJECTILE("projectile", 3, 6);
/*     */     
/*     */     private final String name;
/*     */     private final int minCost;
/*     */     private final int levelCost;
/*     */     
/*     */     Type(String debug3, int debug4, int debug5) {
/*  21 */       this.name = debug3;
/*  22 */       this.minCost = debug4;
/*  23 */       this.levelCost = debug5;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMinCost() {
/*  31 */       return this.minCost;
/*     */     }
/*     */     
/*     */     public int getLevelCost() {
/*  35 */       return this.levelCost;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtectionEnchantment(Enchantment.Rarity debug1, Type debug2, EquipmentSlot... debug3) {
/*  42 */     super(debug1, (debug2 == Type.FALL) ? EnchantmentCategory.ARMOR_FEET : EnchantmentCategory.ARMOR, debug3);
/*  43 */     this.type = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinCost(int debug1) {
/*  48 */     return this.type.getMinCost() + (debug1 - 1) * this.type.getLevelCost();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxCost(int debug1) {
/*  53 */     return getMinCost(debug1) + this.type.getLevelCost();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxLevel() {
/*  58 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageProtection(int debug1, DamageSource debug2) {
/*  63 */     if (debug2.isBypassInvul()) {
/*  64 */       return 0;
/*     */     }
/*     */     
/*  67 */     if (this.type == Type.ALL) {
/*  68 */       return debug1;
/*     */     }
/*  70 */     if (this.type == Type.FIRE && debug2.isFire()) {
/*  71 */       return debug1 * 2;
/*     */     }
/*  73 */     if (this.type == Type.FALL && debug2 == DamageSource.FALL) {
/*  74 */       return debug1 * 3;
/*     */     }
/*  76 */     if (this.type == Type.EXPLOSION && debug2.isExplosion()) {
/*  77 */       return debug1 * 2;
/*     */     }
/*  79 */     if (this.type == Type.PROJECTILE && debug2.isProjectile()) {
/*  80 */       return debug1 * 2;
/*     */     }
/*  82 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkCompatibility(Enchantment debug1) {
/*  87 */     if (debug1 instanceof ProtectionEnchantment) {
/*  88 */       ProtectionEnchantment debug2 = (ProtectionEnchantment)debug1;
/*     */       
/*  90 */       if (this.type == debug2.type) {
/*  91 */         return false;
/*     */       }
/*     */       
/*  94 */       return (this.type == Type.FALL || debug2.type == Type.FALL);
/*     */     } 
/*  96 */     return super.checkCompatibility(debug1);
/*     */   }
/*     */   
/*     */   public static int getFireAfterDampener(LivingEntity debug0, int debug1) {
/* 100 */     int debug2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, debug0);
/*     */     
/* 102 */     if (debug2 > 0) {
/* 103 */       debug1 -= Mth.floor(debug1 * debug2 * 0.15F);
/*     */     }
/*     */     
/* 106 */     return debug1;
/*     */   }
/*     */   
/*     */   public static double getExplosionKnockbackAfterDampener(LivingEntity debug0, double debug1) {
/* 110 */     int debug3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, debug0);
/*     */     
/* 112 */     if (debug3 > 0) {
/* 113 */       debug1 -= Mth.floor(debug1 * (debug3 * 0.15F));
/*     */     }
/*     */     
/* 116 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\ProtectionEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */