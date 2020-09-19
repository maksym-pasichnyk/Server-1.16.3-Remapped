/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.animal.AbstractSchoolingFish;
/*    */ 
/*    */ 
/*    */ public class FollowFlockLeaderGoal
/*    */   extends Goal
/*    */ {
/*    */   private final AbstractSchoolingFish mob;
/*    */   private int timeToRecalcPath;
/*    */   private int nextStartTick;
/*    */   
/*    */   public FollowFlockLeaderGoal(AbstractSchoolingFish debug1) {
/* 16 */     this.mob = debug1;
/* 17 */     this.nextStartTick = nextStartTick(debug1);
/*    */   }
/*    */   
/*    */   protected int nextStartTick(AbstractSchoolingFish debug1) {
/* 21 */     return 200 + debug1.getRandom().nextInt(200) % 20;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 26 */     if (this.mob.hasFollowers()) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     if (this.mob.isFollower()) {
/* 31 */       return true;
/*    */     }
/*    */     
/* 34 */     if (this.nextStartTick > 0) {
/* 35 */       this.nextStartTick--;
/* 36 */       return false;
/*    */     } 
/*    */     
/* 39 */     this.nextStartTick = nextStartTick(this.mob);
/*    */     
/* 41 */     Predicate<AbstractSchoolingFish> debug1 = debug0 -> (debug0.canBeFollowed() || !debug0.isFollower());
/* 42 */     List<AbstractSchoolingFish> debug2 = this.mob.level.getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), debug1);
/*    */     
/* 44 */     AbstractSchoolingFish debug3 = debug2.stream().filter(AbstractSchoolingFish::canBeFollowed).findAny().orElse(this.mob);
/*    */     
/* 46 */     debug3.addFollowers(debug2.stream().filter(debug0 -> !debug0.isFollower()));
/*    */     
/* 48 */     return this.mob.isFollower();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 53 */     return (this.mob.isFollower() && this.mob.inRangeOfLeader());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 58 */     this.timeToRecalcPath = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 63 */     this.mob.stopFollowing();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 68 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 71 */     this.timeToRecalcPath = 10;
/*    */     
/* 73 */     this.mob.pathToLeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowFlockLeaderGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */