/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ 
/*    */ 
/*    */ public class FollowParentGoal
/*    */   extends Goal
/*    */ {
/*    */   private final Animal animal;
/*    */   private Animal parent;
/*    */   private final double speedModifier;
/*    */   private int timeToRecalcPath;
/*    */   
/*    */   public FollowParentGoal(Animal debug1, double debug2) {
/* 17 */     this.animal = debug1;
/* 18 */     this.speedModifier = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (this.animal.getAge() >= 0) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     List<Animal> debug1 = this.animal.level.getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/*    */     
/* 29 */     Animal debug2 = null;
/* 30 */     double debug3 = Double.MAX_VALUE;
/* 31 */     for (Animal debug6 : debug1) {
/* 32 */       if (debug6.getAge() < 0) {
/*    */         continue;
/*    */       }
/* 35 */       double debug7 = this.animal.distanceToSqr((Entity)debug6);
/* 36 */       if (debug7 > debug3) {
/*    */         continue;
/*    */       }
/* 39 */       debug3 = debug7;
/* 40 */       debug2 = debug6;
/*    */     } 
/*    */     
/* 43 */     if (debug2 == null) {
/* 44 */       return false;
/*    */     }
/* 46 */     if (debug3 < 9.0D) {
/* 47 */       return false;
/*    */     }
/* 49 */     this.parent = debug2;
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 55 */     if (this.animal.getAge() >= 0) {
/* 56 */       return false;
/*    */     }
/* 58 */     if (!this.parent.isAlive()) {
/* 59 */       return false;
/*    */     }
/* 61 */     double debug1 = this.animal.distanceToSqr((Entity)this.parent);
/* 62 */     if (debug1 < 9.0D || debug1 > 256.0D) {
/* 63 */       return false;
/*    */     }
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 70 */     this.timeToRecalcPath = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 75 */     this.parent = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 80 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 83 */     this.timeToRecalcPath = 10;
/* 84 */     this.animal.getNavigation().moveTo((Entity)this.parent, this.speedModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowParentGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */