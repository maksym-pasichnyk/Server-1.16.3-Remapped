/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class StrollThroughVillageGoal
/*    */   extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   private final int interval;
/*    */   @Nullable
/*    */   private BlockPos wantedPos;
/*    */   
/*    */   public StrollThroughVillageGoal(PathfinderMob debug1, int debug2) {
/* 25 */     this.mob = debug1;
/* 26 */     this.interval = debug2;
/* 27 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 32 */     if (this.mob.isVehicle()) {
/* 33 */       return false;
/*    */     }
/*    */     
/* 36 */     if (this.mob.level.isDay()) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     if (this.mob.getRandom().nextInt(this.interval) != 0) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/*    */     
/* 46 */     BlockPos debug2 = this.mob.blockPosition();
/* 47 */     if (!debug1.isCloseToVillage(debug2, 6)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     Vec3 debug3 = RandomPos.getLandPos(this.mob, 15, 7, debug1 -> -debug0.sectionsToVillage(SectionPos.of(debug1)));
/* 52 */     this.wantedPos = (debug3 == null) ? null : new BlockPos(debug3);
/* 53 */     return (this.wantedPos != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 58 */     return (this.wantedPos != null && !this.mob.getNavigation().isDone() && this.mob.getNavigation().getTargetPos().equals(this.wantedPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 63 */     if (this.wantedPos == null) {
/*    */       return;
/*    */     }
/* 66 */     PathNavigation debug1 = this.mob.getNavigation();
/* 67 */     if (debug1.isDone() && 
/* 68 */       !this.wantedPos.closerThan((Position)this.mob.position(), 10.0D)) {
/* 69 */       Vec3 debug2 = Vec3.atBottomCenterOf((Vec3i)this.wantedPos);
/*    */ 
/*    */       
/* 72 */       Vec3 debug3 = this.mob.position();
/* 73 */       Vec3 debug4 = debug3.subtract(debug2);
/*    */       
/* 75 */       debug2 = debug4.scale(0.4D).add(debug2);
/*    */       
/* 77 */       Vec3 debug5 = debug2.subtract(debug3).normalize().scale(10.0D).add(debug3);
/* 78 */       BlockPos debug6 = new BlockPos(debug5);
/* 79 */       debug6 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, debug6);
/*    */       
/* 81 */       if (!debug1.moveTo(debug6.getX(), debug6.getY(), debug6.getZ(), 1.0D))
/*    */       {
/* 83 */         moveRandomly();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void moveRandomly() {
/* 90 */     Random debug1 = this.mob.getRandom();
/* 91 */     BlockPos debug2 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + debug1.nextInt(16), 0, -8 + debug1.nextInt(16)));
/* 92 */     this.mob.getNavigation().moveTo(debug2.getX(), debug2.getY(), debug2.getZ(), 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\StrollThroughVillageGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */