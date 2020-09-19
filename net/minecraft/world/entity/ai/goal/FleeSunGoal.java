/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FleeSunGoal extends Goal {
/*    */   protected final PathfinderMob mob;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   private final Level level;
/*    */   
/*    */   public FleeSunGoal(PathfinderMob debug1, double debug2) {
/* 22 */     this.mob = debug1;
/* 23 */     this.speedModifier = debug2;
/* 24 */     this.level = debug1.level;
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     if (this.mob.getTarget() != null) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!this.level.isDay()) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!this.mob.isOnFire()) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!this.level.canSeeSky(this.mob.blockPosition())) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     return setWantedPos();
/*    */   }
/*    */   
/*    */   protected boolean setWantedPos() {
/* 50 */     Vec3 debug1 = getHidePos();
/* 51 */     if (debug1 == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     this.wantedX = debug1.x;
/* 55 */     this.wantedY = debug1.y;
/* 56 */     this.wantedZ = debug1.z;
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 62 */     return !this.mob.getNavigation().isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 67 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getHidePos() {
/* 72 */     Random debug1 = this.mob.getRandom();
/* 73 */     BlockPos debug2 = this.mob.blockPosition();
/*    */     
/* 75 */     for (int debug3 = 0; debug3 < 10; debug3++) {
/* 76 */       BlockPos debug4 = debug2.offset(debug1.nextInt(20) - 10, debug1.nextInt(6) - 3, debug1.nextInt(20) - 10);
/*    */       
/* 78 */       if (!this.level.canSeeSky(debug4) && this.mob.getWalkTargetValue(debug4) < 0.0F) {
/* 79 */         return Vec3.atBottomCenterOf((Vec3i)debug4);
/*    */       }
/*    */     } 
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FleeSunGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */