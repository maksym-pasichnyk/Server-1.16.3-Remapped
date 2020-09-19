/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.projectile.DragonFireball;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class DragonStrafePlayerPhase
/*     */   extends AbstractDragonPhaseInstance {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private int fireballCharge;
/*     */   
/*     */   private Path currentPath;
/*     */   private Vec3 targetLocation;
/*     */   private LivingEntity attackTarget;
/*     */   private boolean holdingPatternClockwise;
/*     */   
/*     */   public DragonStrafePlayerPhase(EnderDragon debug1) {
/*  28 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick() {
/*  33 */     if (this.attackTarget == null) {
/*  34 */       LOGGER.warn("Skipping player strafe phase because no player was found");
/*  35 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */       
/*     */       return;
/*     */     } 
/*  39 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  40 */       double d1 = this.attackTarget.getX();
/*  41 */       double d2 = this.attackTarget.getZ();
/*     */       
/*  43 */       double debug5 = d1 - this.dragon.getX();
/*  44 */       double debug7 = d2 - this.dragon.getZ();
/*  45 */       double debug9 = Mth.sqrt(debug5 * debug5 + debug7 * debug7);
/*  46 */       double debug11 = Math.min(0.4000000059604645D + debug9 / 80.0D - 1.0D, 10.0D);
/*     */       
/*  48 */       this.targetLocation = new Vec3(d1, this.attackTarget.getY() + debug11, d2);
/*     */     } 
/*     */     
/*  51 */     double debug1 = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  52 */     if (debug1 < 100.0D || debug1 > 22500.0D) {
/*  53 */       findNewTarget();
/*     */     }
/*     */     
/*  56 */     double debug3 = 64.0D;
/*  57 */     if (this.attackTarget.distanceToSqr((Entity)this.dragon) < 4096.0D) {
/*  58 */       if (this.dragon.canSee((Entity)this.attackTarget)) {
/*  59 */         this.fireballCharge++;
/*  60 */         Vec3 debug5 = (new Vec3(this.attackTarget.getX() - this.dragon.getX(), 0.0D, this.attackTarget.getZ() - this.dragon.getZ())).normalize();
/*  61 */         Vec3 debug6 = (new Vec3(Mth.sin(this.dragon.yRot * 0.017453292F), 0.0D, -Mth.cos(this.dragon.yRot * 0.017453292F))).normalize();
/*  62 */         float debug7 = (float)debug6.dot(debug5);
/*  63 */         float debug8 = (float)(Math.acos(debug7) * 57.2957763671875D);
/*  64 */         debug8 += 0.5F;
/*     */         
/*  66 */         if (this.fireballCharge >= 5 && debug8 >= 0.0F && debug8 < 10.0F) {
/*  67 */           double debug9 = 1.0D;
/*  68 */           Vec3 debug11 = this.dragon.getViewVector(1.0F);
/*  69 */           double debug12 = this.dragon.head.getX() - debug11.x * 1.0D;
/*  70 */           double debug14 = this.dragon.head.getY(0.5D) + 0.5D;
/*  71 */           double debug16 = this.dragon.head.getZ() - debug11.z * 1.0D;
/*     */           
/*  73 */           double debug18 = this.attackTarget.getX() - debug12;
/*  74 */           double debug20 = this.attackTarget.getY(0.5D) - debug14;
/*  75 */           double debug22 = this.attackTarget.getZ() - debug16;
/*     */           
/*  77 */           if (!this.dragon.isSilent()) {
/*  78 */             this.dragon.level.levelEvent(null, 1017, this.dragon.blockPosition(), 0);
/*     */           }
/*  80 */           DragonFireball debug24 = new DragonFireball(this.dragon.level, (LivingEntity)this.dragon, debug18, debug20, debug22);
/*  81 */           debug24.moveTo(debug12, debug14, debug16, 0.0F, 0.0F);
/*  82 */           this.dragon.level.addFreshEntity((Entity)debug24);
/*  83 */           this.fireballCharge = 0;
/*     */           
/*  85 */           if (this.currentPath != null) {
/*  86 */             while (!this.currentPath.isDone()) {
/*  87 */               this.currentPath.advance();
/*     */             }
/*     */           }
/*     */           
/*  91 */           this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */         }
/*     */       
/*  94 */       } else if (this.fireballCharge > 0) {
/*  95 */         this.fireballCharge--;
/*     */       }
/*     */     
/*     */     }
/*  99 */     else if (this.fireballCharge > 0) {
/* 100 */       this.fireballCharge--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNewTarget() {
/* 106 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 107 */       int debug1 = this.dragon.findClosestNode();
/* 108 */       int debug2 = debug1;
/*     */       
/* 110 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/* 111 */         this.holdingPatternClockwise = !this.holdingPatternClockwise;
/* 112 */         debug2 += 6;
/*     */       } 
/*     */       
/* 115 */       if (this.holdingPatternClockwise) {
/* 116 */         debug2++;
/*     */       } else {
/* 118 */         debug2--;
/*     */       } 
/*     */       
/* 121 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*     */         
/* 123 */         debug2 -= 12;
/* 124 */         debug2 &= 0x7;
/* 125 */         debug2 += 12;
/*     */       } else {
/*     */         
/* 128 */         debug2 %= 12;
/* 129 */         if (debug2 < 0) {
/* 130 */           debug2 += 12;
/*     */         }
/*     */       } 
/*     */       
/* 134 */       this.currentPath = this.dragon.findPath(debug1, debug2, null);
/*     */       
/* 136 */       if (this.currentPath != null) {
/* 137 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 141 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 145 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 146 */       double debug4; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 148 */       this.currentPath.advance();
/* 149 */       double debug2 = blockPos.getX();
/*     */       
/* 151 */       double debug6 = blockPos.getZ();
/*     */       
/*     */       do {
/* 154 */         debug4 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 155 */       } while (debug4 < blockPos.getY());
/*     */       
/* 157 */       this.targetLocation = new Vec3(debug2, debug4, debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 163 */     this.fireballCharge = 0;
/* 164 */     this.targetLocation = null;
/* 165 */     this.currentPath = null;
/* 166 */     this.attackTarget = null;
/*     */   }
/*     */   
/*     */   public void setTarget(LivingEntity debug1) {
/* 170 */     this.attackTarget = debug1;
/*     */     
/* 172 */     int debug2 = this.dragon.findClosestNode();
/* 173 */     int debug3 = this.dragon.findClosestNode(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
/*     */     
/* 175 */     int debug4 = Mth.floor(this.attackTarget.getX());
/* 176 */     int debug5 = Mth.floor(this.attackTarget.getZ());
/*     */     
/* 178 */     double debug6 = debug4 - this.dragon.getX();
/* 179 */     double debug8 = debug5 - this.dragon.getZ();
/* 180 */     double debug10 = Mth.sqrt(debug6 * debug6 + debug8 * debug8);
/* 181 */     double debug12 = Math.min(0.4000000059604645D + debug10 / 80.0D - 1.0D, 10.0D);
/* 182 */     int debug14 = Mth.floor(this.attackTarget.getY() + debug12);
/*     */     
/* 184 */     Node debug15 = new Node(debug4, debug14, debug5);
/*     */     
/* 186 */     this.currentPath = this.dragon.findPath(debug2, debug3, debug15);
/*     */     
/* 188 */     if (this.currentPath != null) {
/* 189 */       this.currentPath.advance();
/*     */       
/* 191 */       navigateToNextPathNode();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Vec3 getFlyTargetLocation() {
/* 198 */     return this.targetLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonStrafePlayerPhase> getPhase() {
/* 203 */     return EnderDragonPhase.STRAFE_PLAYER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonStrafePlayerPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */