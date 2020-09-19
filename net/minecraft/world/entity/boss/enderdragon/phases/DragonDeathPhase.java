/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonDeathPhase extends AbstractDragonPhaseInstance {
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonDeathPhase(EnderDragon debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */   private int time;
/*    */   
/*    */   public void doClientTick() {
/* 22 */     if (this.time++ % 10 == 0) {
/* 23 */       float debug1 = (this.dragon.getRandom().nextFloat() - 0.5F) * 8.0F;
/* 24 */       float debug2 = (this.dragon.getRandom().nextFloat() - 0.5F) * 4.0F;
/* 25 */       float debug3 = (this.dragon.getRandom().nextFloat() - 0.5F) * 8.0F;
/* 26 */       this.dragon.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, this.dragon.getX() + debug1, this.dragon.getY() + 2.0D + debug2, this.dragon.getZ() + debug3, 0.0D, 0.0D, 0.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 32 */     this.time++;
/*    */     
/* 34 */     if (this.targetLocation == null) {
/* 35 */       BlockPos blockPos = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION);
/* 36 */       this.targetLocation = Vec3.atBottomCenterOf((Vec3i)blockPos);
/*    */     } 
/*    */     
/* 39 */     double debug1 = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 40 */     if (debug1 < 100.0D || debug1 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 41 */       this.dragon.setHealth(0.0F);
/*    */     } else {
/* 43 */       this.dragon.setHealth(1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 49 */     this.targetLocation = null;
/* 50 */     this.time = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 55 */     return 3.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 61 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonDeathPhase> getPhase() {
/* 66 */     return EnderDragonPhase.DYING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonDeathPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */