/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ public class BowItem
/*     */   extends ProjectileWeaponItem
/*     */   implements Vanishable
/*     */ {
/*     */   public BowItem(Item.Properties debug1) {
/*  23 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseUsing(ItemStack debug1, Level debug2, LivingEntity debug3, int debug4) {
/*  28 */     if (!(debug3 instanceof Player)) {
/*     */       return;
/*     */     }
/*     */     
/*  32 */     Player debug5 = (Player)debug3;
/*  33 */     boolean debug6 = (debug5.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, debug1) > 0);
/*  34 */     ItemStack debug7 = debug5.getProjectile(debug1);
/*     */     
/*  36 */     if (debug7.isEmpty() && !debug6) {
/*     */       return;
/*     */     }
/*     */     
/*  40 */     if (debug7.isEmpty()) {
/*  41 */       debug7 = new ItemStack(Items.ARROW);
/*     */     }
/*     */     
/*  44 */     int debug8 = getUseDuration(debug1) - debug4;
/*  45 */     float debug9 = getPowerForTime(debug8);
/*  46 */     if (debug9 < 0.1D) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     boolean debug10 = (debug6 && debug7.getItem() == Items.ARROW);
/*  51 */     if (!debug2.isClientSide) {
/*  52 */       ArrowItem debug11 = (debug7.getItem() instanceof ArrowItem) ? (ArrowItem)debug7.getItem() : (ArrowItem)Items.ARROW;
/*     */       
/*  54 */       AbstractArrow debug12 = debug11.createArrow(debug2, debug7, (LivingEntity)debug5);
/*  55 */       debug12.shootFromRotation((Entity)debug5, debug5.xRot, debug5.yRot, 0.0F, debug9 * 3.0F, 1.0F);
/*  56 */       if (debug9 == 1.0F) {
/*  57 */         debug12.setCritArrow(true);
/*     */       }
/*  59 */       int debug13 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, debug1);
/*  60 */       if (debug13 > 0) {
/*  61 */         debug12.setBaseDamage(debug12.getBaseDamage() + debug13 * 0.5D + 0.5D);
/*     */       }
/*  63 */       int debug14 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, debug1);
/*  64 */       if (debug14 > 0) {
/*  65 */         debug12.setKnockback(debug14);
/*     */       }
/*  67 */       if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, debug1) > 0) {
/*  68 */         debug12.setSecondsOnFire(100);
/*     */       }
/*  70 */       debug1.hurtAndBreak(1, debug5, debug1 -> debug1.broadcastBreakEvent(debug0.getUsedItemHand()));
/*     */       
/*  72 */       if (debug10 || (debug5.abilities.instabuild && (debug7.getItem() == Items.SPECTRAL_ARROW || debug7.getItem() == Items.TIPPED_ARROW))) {
/*  73 */         debug12.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
/*     */       }
/*     */       
/*  76 */       debug2.addFreshEntity((Entity)debug12);
/*     */     } 
/*     */     
/*  79 */     debug2.playSound(null, debug5.getX(), debug5.getY(), debug5.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + debug9 * 0.5F);
/*  80 */     if (!debug10 && !debug5.abilities.instabuild) {
/*  81 */       debug7.shrink(1);
/*  82 */       if (debug7.isEmpty())
/*     */       {
/*  84 */         debug5.inventory.removeItem(debug7);
/*     */       }
/*     */     } 
/*  87 */     debug5.awardStat(Stats.ITEM_USED.get(this));
/*     */   }
/*     */   
/*     */   public static float getPowerForTime(int debug0) {
/*  91 */     float debug1 = debug0 / 20.0F;
/*  92 */     debug1 = (debug1 * debug1 + debug1 * 2.0F) / 3.0F;
/*  93 */     if (debug1 > 1.0F) {
/*  94 */       debug1 = 1.0F;
/*     */     }
/*  96 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack debug1) {
/* 101 */     return 72000;
/*     */   }
/*     */ 
/*     */   
/*     */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 106 */     return UseAnim.BOW;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 111 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 112 */     boolean debug5 = !debug2.getProjectile(debug4).isEmpty();
/* 113 */     if (debug2.abilities.instabuild || debug5) {
/* 114 */       debug2.startUsingItem(debug3);
/* 115 */       return InteractionResultHolder.consume(debug4);
/*     */     } 
/* 117 */     return InteractionResultHolder.fail(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getAllSupportedProjectiles() {
/* 122 */     return ARROW_ONLY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultProjectileRange() {
/* 127 */     return 15;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */