/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestHealableRaiderTargetGoal<T extends LivingEntity>
/*    */   extends NearestAttackableTargetGoal<T>
/*    */ {
/*    */   private int cooldown;
/*    */   
/*    */   public NearestHealableRaiderTargetGoal(Raider debug1, Class<T> debug2, boolean debug3, @Nullable Predicate<LivingEntity> debug4) {
/* 15 */     super((Mob)debug1, debug2, 500, debug3, false, debug4);
/* 16 */     this.cooldown = 0;
/*    */   }
/*    */   
/*    */   public int getCooldown() {
/* 20 */     return this.cooldown;
/*    */   }
/*    */   
/*    */   public void decrementCooldown() {
/* 24 */     this.cooldown--;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 29 */     if (this.cooldown > 0 || !this.mob.getRandom().nextBoolean()) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!((Raider)this.mob).hasActiveRaid()) {
/* 33 */       return false;
/*    */     }
/*    */     
/* 36 */     findTarget();
/* 37 */     return (this.target != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 42 */     this.cooldown = 200;
/* 43 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestHealableRaiderTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */