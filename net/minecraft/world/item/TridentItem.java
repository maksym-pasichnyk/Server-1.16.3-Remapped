/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.ThrownTrident;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TridentItem
/*     */   extends Item
/*     */   implements Vanishable {
/*     */   private final Multimap<Attribute, AttributeModifier> defaultModifiers;
/*     */   
/*     */   public TridentItem(Item.Properties debug1) {
/*  35 */     super(debug1);
/*     */ 
/*     */     
/*  38 */     ImmutableMultimap.Builder<Attribute, AttributeModifier> debug2 = ImmutableMultimap.builder();
/*  39 */     debug2.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
/*  40 */     debug2.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9000000953674316D, AttributeModifier.Operation.ADDITION));
/*  41 */     this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)debug2.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackBlock(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/*  46 */     return !debug4.isCreative();
/*     */   }
/*     */ 
/*     */   
/*     */   public UseAnim getUseAnimation(ItemStack debug1) {
/*  51 */     return UseAnim.SPEAR;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack debug1) {
/*  56 */     return 72000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseUsing(ItemStack debug1, Level debug2, LivingEntity debug3, int debug4) {
/*  61 */     if (!(debug3 instanceof Player)) {
/*     */       return;
/*     */     }
/*     */     
/*  65 */     Player debug5 = (Player)debug3;
/*     */     
/*  67 */     int debug6 = getUseDuration(debug1) - debug4;
/*  68 */     if (debug6 < 10) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     int debug7 = EnchantmentHelper.getRiptide(debug1);
/*  73 */     if (debug7 > 0 && !debug5.isInWaterOrRain()) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     if (!debug2.isClientSide) {
/*  78 */       debug1.hurtAndBreak(1, debug5, debug1 -> debug1.broadcastBreakEvent(debug0.getUsedItemHand()));
/*     */       
/*  80 */       if (debug7 == 0) {
/*  81 */         ThrownTrident debug8 = new ThrownTrident(debug2, (LivingEntity)debug5, debug1);
/*  82 */         debug8.shootFromRotation((Entity)debug5, debug5.xRot, debug5.yRot, 0.0F, 2.5F + debug7 * 0.5F, 1.0F);
/*     */         
/*  84 */         if (debug5.abilities.instabuild) {
/*  85 */           debug8.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
/*     */         }
/*     */         
/*  88 */         debug2.addFreshEntity((Entity)debug8);
/*     */         
/*  90 */         debug2.playSound(null, (Entity)debug8, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
/*     */         
/*  92 */         if (!debug5.abilities.instabuild) {
/*  93 */           debug5.inventory.removeItem(debug1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     debug5.awardStat(Stats.ITEM_USED.get(this));
/*     */     
/* 100 */     if (debug7 > 0) {
/* 101 */       SoundEvent debug15; float debug8 = debug5.yRot;
/* 102 */       float debug9 = debug5.xRot;
/*     */ 
/*     */       
/* 105 */       float debug10 = -Mth.sin(debug8 * 0.017453292F) * Mth.cos(debug9 * 0.017453292F);
/* 106 */       float debug11 = -Mth.sin(debug9 * 0.017453292F);
/* 107 */       float debug12 = Mth.cos(debug8 * 0.017453292F) * Mth.cos(debug9 * 0.017453292F);
/* 108 */       float debug13 = Mth.sqrt(debug10 * debug10 + debug11 * debug11 + debug12 * debug12);
/* 109 */       float debug14 = 3.0F * (1.0F + debug7) / 4.0F;
/* 110 */       debug10 *= debug14 / debug13;
/* 111 */       debug11 *= debug14 / debug13;
/* 112 */       debug12 *= debug14 / debug13;
/* 113 */       debug5.push(debug10, debug11, debug12);
/*     */       
/* 115 */       debug5.startAutoSpinAttack(20);
/* 116 */       if (debug5.isOnGround()) {
/* 117 */         float f = 1.1999999F;
/* 118 */         debug5.move(MoverType.SELF, new Vec3(0.0D, 1.1999999284744263D, 0.0D));
/*     */       } 
/*     */ 
/*     */       
/* 122 */       if (debug7 >= 3) {
/* 123 */         debug15 = SoundEvents.TRIDENT_RIPTIDE_3;
/* 124 */       } else if (debug7 == 2) {
/* 125 */         debug15 = SoundEvents.TRIDENT_RIPTIDE_2;
/*     */       } else {
/* 127 */         debug15 = SoundEvents.TRIDENT_RIPTIDE_1;
/*     */       } 
/* 129 */       debug2.playSound(null, (Entity)debug5, debug15, SoundSource.PLAYERS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 135 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 136 */     if (debug4.getDamageValue() >= debug4.getMaxDamage() - 1)
/*     */     {
/* 138 */       return InteractionResultHolder.fail(debug4);
/*     */     }
/* 140 */     if (EnchantmentHelper.getRiptide(debug4) > 0 && !debug2.isInWaterOrRain())
/*     */     {
/* 142 */       return InteractionResultHolder.fail(debug4);
/*     */     }
/* 144 */     debug2.startUsingItem(debug3);
/* 145 */     return InteractionResultHolder.consume(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtEnemy(ItemStack debug1, LivingEntity debug2, LivingEntity debug3) {
/* 150 */     debug1.hurtAndBreak(1, debug3, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mineBlock(ItemStack debug1, Level debug2, BlockState debug3, BlockPos debug4, LivingEntity debug5) {
/* 157 */     if (debug3.getDestroySpeed((BlockGetter)debug2, debug4) != 0.0D) {
/* 158 */       debug1.hurtAndBreak(2, debug5, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/*     */     }
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot debug1) {
/* 165 */     if (debug1 == EquipmentSlot.MAINHAND) {
/* 166 */       return this.defaultModifiers;
/*     */     }
/* 168 */     return super.getDefaultAttributeModifiers(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEnchantmentValue() {
/* 173 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\TridentItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */