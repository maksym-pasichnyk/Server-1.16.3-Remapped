/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class PathfinderMob extends Mob {
/*    */   protected PathfinderMob(EntityType<? extends PathfinderMob> debug1, Level debug2) {
/* 12 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public float getWalkTargetValue(BlockPos debug1) {
/* 16 */     return getWalkTargetValue(debug1, (LevelReader)this.level);
/*    */   }
/*    */   
/*    */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 20 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkSpawnRules(LevelAccessor debug1, MobSpawnType debug2) {
/* 25 */     return (getWalkTargetValue(blockPosition(), (LevelReader)debug1) >= 0.0F);
/*    */   }
/*    */   
/*    */   public boolean isPathFinding() {
/* 29 */     return !getNavigation().isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tickLeash() {
/* 34 */     super.tickLeash();
/*    */     
/* 36 */     Entity debug1 = getLeashHolder();
/* 37 */     if (debug1 != null && debug1.level == this.level) {
/*    */       
/* 39 */       restrictTo(debug1.blockPosition(), 5);
/*    */       
/* 41 */       float debug2 = distanceTo(debug1);
/*    */       
/* 43 */       if (this instanceof TamableAnimal && ((TamableAnimal)this).isInSittingPose()) {
/* 44 */         if (debug2 > 10.0F) {
/* 45 */           dropLeash(true, true);
/*    */         }
/*    */         
/*    */         return;
/*    */       } 
/* 50 */       onLeashDistance(debug2);
/*    */       
/* 52 */       if (debug2 > 10.0F) {
/* 53 */         dropLeash(true, true);
/* 54 */         this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
/* 55 */       } else if (debug2 > 6.0F) {
/*    */         
/* 57 */         double debug3 = (debug1.getX() - getX()) / debug2;
/* 58 */         double debug5 = (debug1.getY() - getY()) / debug2;
/* 59 */         double debug7 = (debug1.getZ() - getZ()) / debug2;
/*    */         
/* 61 */         setDeltaMovement(getDeltaMovement().add(
/* 62 */               Math.copySign(debug3 * debug3 * 0.4D, debug3), 
/* 63 */               Math.copySign(debug5 * debug5 * 0.4D, debug5), 
/* 64 */               Math.copySign(debug7 * debug7 * 0.4D, debug7)));
/*    */       } else {
/*    */         
/* 67 */         this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
/* 68 */         float debug3 = 2.0F;
/*    */         
/* 70 */         Vec3 debug4 = (new Vec3(debug1.getX() - getX(), debug1.getY() - getY(), debug1.getZ() - getZ())).normalize().scale(Math.max(debug2 - 2.0F, 0.0F));
/* 71 */         getNavigation().moveTo(getX() + debug4.x, getY() + debug4.y, getZ() + debug4.z, followLeashSpeed());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected double followLeashSpeed() {
/* 77 */     return 1.0D;
/*    */   }
/*    */   
/*    */   protected void onLeashDistance(float debug1) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\PathfinderMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */