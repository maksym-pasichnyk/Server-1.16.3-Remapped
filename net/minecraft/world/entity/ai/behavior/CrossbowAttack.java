/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class CrossbowAttack<E extends Mob & CrossbowAttackMob, T extends LivingEntity>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private int attackDelay;
/*     */   
/*     */   enum CrossbowState {
/*  24 */     UNCHARGED,
/*  25 */     CHARGING,
/*  26 */     CHARGED,
/*  27 */     READY_TO_ATTACK;
/*     */   }
/*     */   
/*  30 */   private CrossbowState crossbowState = CrossbowState.UNCHARGED;
/*     */   
/*     */   public CrossbowAttack() {
/*  33 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/*  41 */     LivingEntity debug3 = getAttackTarget((LivingEntity)debug2);
/*  42 */     return (debug2.isHolding(Items.CROSSBOW) && BehaviorUtils.canSee((LivingEntity)debug2, debug3) && BehaviorUtils.isWithinAttackRange((Mob)debug2, debug3, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, E debug2, long debug3) {
/*  47 */     return (debug2.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && checkExtraStartConditions(debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, E debug2, long debug3) {
/*  53 */     LivingEntity debug5 = getAttackTarget((LivingEntity)debug2);
/*  54 */     lookAtTarget((Mob)debug2, debug5);
/*  55 */     crossbowAttack(debug2, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, E debug2, long debug3) {
/*  60 */     if (debug2.isUsingItem()) {
/*  61 */       debug2.stopUsingItem();
/*     */     }
/*  63 */     if (debug2.isHolding(Items.CROSSBOW)) {
/*  64 */       ((CrossbowAttackMob)debug2).setChargingCrossbow(false);
/*  65 */       CrossbowItem.setCharged(debug2.getUseItem(), false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void crossbowAttack(E debug1, LivingEntity debug2) {
/*  71 */     if (this.crossbowState == CrossbowState.UNCHARGED) {
/*  72 */       debug1.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)debug1, Items.CROSSBOW));
/*  73 */       this.crossbowState = CrossbowState.CHARGING;
/*  74 */       ((CrossbowAttackMob)debug1).setChargingCrossbow(true);
/*     */     }
/*  76 */     else if (this.crossbowState == CrossbowState.CHARGING) {
/*  77 */       if (!debug1.isUsingItem()) {
/*  78 */         this.crossbowState = CrossbowState.UNCHARGED;
/*     */       }
/*  80 */       int debug3 = debug1.getTicksUsingItem();
/*  81 */       ItemStack debug4 = debug1.getUseItem();
/*  82 */       if (debug3 >= CrossbowItem.getChargeDuration(debug4)) {
/*  83 */         debug1.releaseUsingItem();
/*  84 */         this.crossbowState = CrossbowState.CHARGED;
/*  85 */         this.attackDelay = 20 + debug1.getRandom().nextInt(20);
/*  86 */         ((CrossbowAttackMob)debug1).setChargingCrossbow(false);
/*     */       }
/*     */     
/*  89 */     } else if (this.crossbowState == CrossbowState.CHARGED) {
/*  90 */       this.attackDelay--;
/*  91 */       if (this.attackDelay == 0) {
/*  92 */         this.crossbowState = CrossbowState.READY_TO_ATTACK;
/*     */       }
/*     */     }
/*  95 */     else if (this.crossbowState == CrossbowState.READY_TO_ATTACK) {
/*  96 */       ((RangedAttackMob)debug1).performRangedAttack(debug2, 1.0F);
/*     */       
/*  98 */       ItemStack debug3 = debug1.getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)debug1, Items.CROSSBOW));
/*  99 */       CrossbowItem.setCharged(debug3, false);
/* 100 */       this.crossbowState = CrossbowState.UNCHARGED;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void lookAtTarget(Mob debug1, LivingEntity debug2) {
/* 105 */     debug1.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug2, true));
/*     */   }
/*     */   
/*     */   private static LivingEntity getAttackTarget(LivingEntity debug0) {
/* 109 */     return debug0.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CrossbowAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */