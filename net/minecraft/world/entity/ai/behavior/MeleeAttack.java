/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ProjectileWeaponItem;
/*    */ 
/*    */ public class MeleeAttack
/*    */   extends Behavior<Mob> {
/*    */   private final int cooldownBetweenAttacks;
/*    */   
/*    */   public MeleeAttack(int debug1) {
/* 20 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     this.cooldownBetweenAttacks = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Mob debug2) {
/* 30 */     LivingEntity debug3 = getAttackTarget(debug2);
/* 31 */     return (!isHoldingUsableProjectileWeapon(debug2) && 
/* 32 */       BehaviorUtils.canSee((LivingEntity)debug2, debug3) && 
/* 33 */       BehaviorUtils.isWithinMeleeAttackRange((LivingEntity)debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean isHoldingUsableProjectileWeapon(Mob debug1) {
/* 38 */     return debug1.isHolding(debug1 -> (debug1 instanceof ProjectileWeaponItem && debug0.canFireProjectileWeapon((ProjectileWeaponItem)debug1)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Mob debug2, long debug3) {
/* 43 */     LivingEntity debug5 = getAttackTarget(debug2);
/* 44 */     BehaviorUtils.lookAtEntity((LivingEntity)debug2, debug5);
/* 45 */     debug2.swing(InteractionHand.MAIN_HAND);
/* 46 */     debug2.doHurtTarget((Entity)debug5);
/* 47 */     debug2.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, Boolean.valueOf(true), this.cooldownBetweenAttacks);
/*    */   }
/*    */   
/*    */   private LivingEntity getAttackTarget(Mob debug1) {
/* 51 */     return debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\MeleeAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */