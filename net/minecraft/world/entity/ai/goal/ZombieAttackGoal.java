/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.monster.Zombie;
/*    */ 
/*    */ public class ZombieAttackGoal extends MeleeAttackGoal {
/*    */   private final Zombie zombie;
/*    */   
/*    */   public ZombieAttackGoal(Zombie debug1, double debug2, boolean debug4) {
/* 10 */     super((PathfinderMob)debug1, debug2, debug4);
/* 11 */     this.zombie = debug1;
/*    */   }
/*    */   private int raiseArmTicks;
/*    */   
/*    */   public void start() {
/* 16 */     super.start();
/* 17 */     this.raiseArmTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 22 */     super.stop();
/* 23 */     this.zombie.setAggressive(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     super.tick();
/*    */     
/* 30 */     this.raiseArmTicks++;
/* 31 */     if (this.raiseArmTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2) {
/* 32 */       this.zombie.setAggressive(true);
/*    */     } else {
/* 34 */       this.zombie.setAggressive(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\ZombieAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */