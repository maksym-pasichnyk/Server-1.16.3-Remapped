/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonTakeoffPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   private boolean firstTick;
/*    */   private Path currentPath;
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonTakeoffPhase(EnderDragon debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 24 */     if (this.firstTick || this.currentPath == null) {
/* 25 */       this.firstTick = false;
/* 26 */       findNewTarget();
/*    */     } else {
/* 28 */       BlockPos debug1 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
/* 29 */       if (!debug1.closerThan((Position)this.dragon.position(), 10.0D)) {
/* 30 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 37 */     this.firstTick = true;
/* 38 */     this.currentPath = null;
/* 39 */     this.targetLocation = null;
/*    */   }
/*    */   
/*    */   private void findNewTarget() {
/* 43 */     int debug1 = this.dragon.findClosestNode();
/* 44 */     Vec3 debug2 = this.dragon.getHeadLookVector(1.0F);
/* 45 */     int debug3 = this.dragon.findClosestNode(-debug2.x * 40.0D, 105.0D, -debug2.z * 40.0D);
/*    */     
/* 47 */     if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*    */       
/* 49 */       debug3 -= 12;
/* 50 */       debug3 &= 0x7;
/* 51 */       debug3 += 12;
/*    */     } else {
/*    */       
/* 54 */       debug3 %= 12;
/* 55 */       if (debug3 < 0) {
/* 56 */         debug3 += 12;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     this.currentPath = this.dragon.findPath(debug1, debug3, null);
/*    */     
/* 62 */     navigateToNextPathNode();
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 66 */     if (this.currentPath != null) {
/* 67 */       this.currentPath.advance();
/* 68 */       if (!this.currentPath.isDone()) {
/* 69 */         double debug2; BlockPos blockPos = this.currentPath.getNextNodePos();
/* 70 */         this.currentPath.advance();
/*    */ 
/*    */         
/*    */         do {
/* 74 */           debug2 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 75 */         } while (debug2 < blockPos.getY());
/*    */         
/* 77 */         this.targetLocation = new Vec3(blockPos.getX(), debug2, blockPos.getZ());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 85 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonTakeoffPhase> getPhase() {
/* 90 */     return EnderDragonPhase.TAKEOFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonTakeoffPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */