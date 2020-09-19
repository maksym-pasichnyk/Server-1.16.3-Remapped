/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DragonHoldingPatternPhase extends AbstractDragonPhaseInstance {
/*  19 */   private static final TargetingConditions NEW_TARGET_TARGETING = (new TargetingConditions()).range(64.0D);
/*     */   
/*     */   private Path currentPath;
/*     */   private Vec3 targetLocation;
/*     */   private boolean clockwise;
/*     */   
/*     */   public DragonHoldingPatternPhase(EnderDragon debug1) {
/*  26 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonHoldingPatternPhase> getPhase() {
/*  31 */     return EnderDragonPhase.HOLDING_PATTERN;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick() {
/*  36 */     double debug1 = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  37 */     if (debug1 < 100.0D || debug1 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/*  38 */       findNewTarget();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  44 */     this.currentPath = null;
/*  45 */     this.targetLocation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Vec3 getFlyTargetLocation() {
/*  51 */     return this.targetLocation;
/*     */   }
/*     */   
/*     */   private void findNewTarget() {
/*  55 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  56 */       BlockPos debug1 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos((Vec3i)EndPodiumFeature.END_PODIUM_LOCATION));
/*     */ 
/*     */ 
/*     */       
/*  60 */       int debug2 = (this.dragon.getDragonFight() == null) ? 0 : this.dragon.getDragonFight().getCrystalsAlive();
/*     */       
/*  62 */       if (this.dragon.getRandom().nextInt(debug2 + 3) == 0) {
/*  63 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING_APPROACH);
/*     */         return;
/*     */       } 
/*  66 */       double debug3 = 64.0D;
/*  67 */       Player debug5 = this.dragon.level.getNearestPlayer(NEW_TARGET_TARGETING, debug1.getX(), debug1.getY(), debug1.getZ());
/*  68 */       if (debug5 != null) {
/*  69 */         debug3 = debug1.distSqr((Position)debug5.position(), true) / 512.0D;
/*     */       }
/*  71 */       if (debug5 != null && !debug5.abilities.invulnerable && (this.dragon.getRandom().nextInt(Mth.abs((int)debug3) + 2) == 0 || this.dragon.getRandom().nextInt(debug2 + 2) == 0)) {
/*     */         
/*  73 */         strafePlayer(debug5);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*  79 */     if (this.currentPath == null || this.currentPath.isDone()) {
/*  80 */       int debug1 = this.dragon.findClosestNode();
/*  81 */       int debug2 = debug1;
/*     */       
/*  83 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/*  84 */         this.clockwise = !this.clockwise;
/*  85 */         debug2 += 6;
/*     */       } 
/*     */       
/*  88 */       if (this.clockwise) {
/*  89 */         debug2++;
/*     */       } else {
/*  91 */         debug2--;
/*     */       } 
/*     */       
/*  94 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() < 0) {
/*     */         
/*  96 */         debug2 -= 12;
/*  97 */         debug2 &= 0x7;
/*  98 */         debug2 += 12;
/*     */       } else {
/*     */         
/* 101 */         debug2 %= 12;
/* 102 */         if (debug2 < 0) {
/* 103 */           debug2 += 12;
/*     */         }
/*     */       } 
/*     */       
/* 107 */       this.currentPath = this.dragon.findPath(debug1, debug2, null);
/* 108 */       if (this.currentPath != null) {
/* 109 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 113 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void strafePlayer(Player debug1) {
/* 117 */     this.dragon.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
/* 118 */     ((DragonStrafePlayerPhase)this.dragon.getPhaseManager().<DragonStrafePlayerPhase>getPhase(EnderDragonPhase.STRAFE_PLAYER)).setTarget((LivingEntity)debug1);
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 122 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 123 */       double debug6; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 125 */       this.currentPath.advance();
/* 126 */       double debug2 = blockPos.getX();
/* 127 */       double debug4 = blockPos.getZ();
/*     */ 
/*     */       
/*     */       do {
/* 131 */         debug6 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 132 */       } while (debug6 < blockPos.getY());
/*     */       
/* 134 */       this.targetLocation = new Vec3(debug2, debug6, debug4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal debug1, BlockPos debug2, DamageSource debug3, @Nullable Player debug4) {
/* 140 */     if (debug4 != null && !debug4.abilities.invulnerable)
/* 141 */       strafePlayer(debug4); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonHoldingPatternPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */