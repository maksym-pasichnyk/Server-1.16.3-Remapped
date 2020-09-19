/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Node;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonLandingApproachPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/* 17 */   private static final TargetingConditions NEAR_EGG_TARGETING = (new TargetingConditions()).range(128.0D);
/*    */   
/*    */   private Path currentPath;
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonLandingApproachPhase(EnderDragon debug1) {
/* 23 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonLandingApproachPhase> getPhase() {
/* 28 */     return EnderDragonPhase.LANDING_APPROACH;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 33 */     this.currentPath = null;
/* 34 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 39 */     double debug1 = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 40 */     if (debug1 < 100.0D || debug1 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 41 */       findNewTarget();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 48 */     return this.targetLocation;
/*    */   }
/*    */   
/*    */   private void findNewTarget() {
/* 52 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 53 */       int debug4, debug1 = this.dragon.findClosestNode();
/* 54 */       BlockPos debug2 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
/* 55 */       Player debug3 = this.dragon.level.getNearestPlayer(NEAR_EGG_TARGETING, debug2.getX(), debug2.getY(), debug2.getZ());
/*    */ 
/*    */       
/* 58 */       if (debug3 != null) {
/* 59 */         Vec3 vec3 = (new Vec3(debug3.getX(), 0.0D, debug3.getZ())).normalize();
/* 60 */         debug4 = this.dragon.findClosestNode(-vec3.x * 40.0D, 105.0D, -vec3.z * 40.0D);
/*    */       } else {
/* 62 */         debug4 = this.dragon.findClosestNode(40.0D, debug2.getY(), 0.0D);
/*    */       } 
/*    */       
/* 65 */       Node debug5 = new Node(debug2.getX(), debug2.getY(), debug2.getZ());
/*    */       
/* 67 */       this.currentPath = this.dragon.findPath(debug1, debug4, debug5);
/*    */       
/* 69 */       if (this.currentPath != null) {
/* 70 */         this.currentPath.advance();
/*    */       }
/*    */     } 
/*    */     
/* 74 */     navigateToNextPathNode();
/*    */     
/* 76 */     if (this.currentPath != null && this.currentPath.isDone()) {
/* 77 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
/*    */     }
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 82 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 83 */       double debug6; BlockPos blockPos = this.currentPath.getNextNodePos();
/*    */       
/* 85 */       this.currentPath.advance();
/* 86 */       double debug2 = blockPos.getX();
/* 87 */       double debug4 = blockPos.getZ();
/*    */ 
/*    */       
/*    */       do {
/* 91 */         debug6 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 92 */       } while (debug6 < blockPos.getY());
/*    */       
/* 94 */       this.targetLocation = new Vec3(debug2, debug6, debug4);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingApproachPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */