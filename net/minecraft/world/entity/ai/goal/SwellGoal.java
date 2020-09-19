/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Creeper;
/*    */ 
/*    */ public class SwellGoal extends Goal {
/*    */   private final Creeper creeper;
/*    */   private LivingEntity target;
/*    */   
/*    */   public SwellGoal(Creeper debug1) {
/* 13 */     this.creeper = debug1;
/* 14 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 19 */     LivingEntity debug1 = this.creeper.getTarget();
/* 20 */     return (this.creeper.getSwellDir() > 0 || (debug1 != null && this.creeper.distanceToSqr((Entity)debug1) < 9.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 25 */     this.creeper.getNavigation().stop();
/* 26 */     this.target = this.creeper.getTarget();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 31 */     this.target = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     if (this.target == null) {
/* 37 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     if (this.creeper.distanceToSqr((Entity)this.target) > 49.0D) {
/* 42 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     if (!this.creeper.getSensing().canSee((Entity)this.target)) {
/* 47 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     this.creeper.setSwellDir(1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\SwellGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */