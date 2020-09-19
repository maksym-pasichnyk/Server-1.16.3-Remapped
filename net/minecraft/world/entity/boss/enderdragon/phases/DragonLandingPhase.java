/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonLandingPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   public DragonLandingPhase(EnderDragon debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public void doClientTick() {
/* 23 */     Vec3 debug1 = this.dragon.getHeadLookVector(1.0F).normalize();
/* 24 */     debug1.yRot(-0.7853982F);
/*    */     
/* 26 */     double debug2 = this.dragon.head.getX();
/* 27 */     double debug4 = this.dragon.head.getY(0.5D);
/* 28 */     double debug6 = this.dragon.head.getZ();
/* 29 */     for (int debug8 = 0; debug8 < 8; debug8++) {
/* 30 */       Random debug9 = this.dragon.getRandom();
/* 31 */       double debug10 = debug2 + debug9.nextGaussian() / 2.0D;
/* 32 */       double debug12 = debug4 + debug9.nextGaussian() / 2.0D;
/* 33 */       double debug14 = debug6 + debug9.nextGaussian() / 2.0D;
/* 34 */       Vec3 debug16 = this.dragon.getDeltaMovement();
/* 35 */       this.dragon.level.addParticle((ParticleOptions)ParticleTypes.DRAGON_BREATH, debug10, debug12, debug14, -debug1.x * 0.07999999821186066D + debug16.x, -debug1.y * 0.30000001192092896D + debug16.y, -debug1.z * 0.07999999821186066D + debug16.z);
/* 36 */       debug1.yRot(0.19634955F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 42 */     if (this.targetLocation == null) {
/* 43 */       this.targetLocation = Vec3.atBottomCenterOf((Vec3i)this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION));
/*    */     }
/*    */     
/* 46 */     if (this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ()) < 1.0D) {
/* 47 */       ((DragonSittingFlamingPhase)this.dragon.getPhaseManager().<DragonSittingFlamingPhase>getPhase(EnderDragonPhase.SITTING_FLAMING)).resetFlameCount();
/* 48 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 54 */     return 1.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 59 */     float debug1 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.dragon.getDeltaMovement())) + 1.0F;
/* 60 */     float debug2 = Math.min(debug1, 40.0F);
/*    */     
/* 62 */     return debug2 / debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 67 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 73 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonLandingPhase> getPhase() {
/* 78 */     return EnderDragonPhase.LANDING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */