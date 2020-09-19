/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.EntityDamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.EnderDragonPart;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.level.pathfinder.BinaryHeap;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EnderDragon
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  59 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  61 */   public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);
/*     */   
/*  63 */   private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = (new TargetingConditions()).range(64.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public final double[][] positions = new double[64][3];
/*  70 */   public int posPointer = -1;
/*     */   
/*     */   private final EnderDragonPart[] subEntities;
/*     */   
/*     */   public final EnderDragonPart head;
/*     */   
/*     */   private final EnderDragonPart neck;
/*     */   
/*     */   private final EnderDragonPart body;
/*     */   private final EnderDragonPart tail1;
/*     */   private final EnderDragonPart tail2;
/*     */   private final EnderDragonPart tail3;
/*     */   private final EnderDragonPart wing1;
/*     */   private final EnderDragonPart wing2;
/*     */   public float oFlapTime;
/*     */   public float flapTime;
/*     */   public boolean inWall;
/*     */   public int dragonDeathTime;
/*     */   public float yRotA;
/*     */   @Nullable
/*     */   public EndCrystal nearestCrystal;
/*     */   @Nullable
/*     */   private final EndDragonFight dragonFight;
/*     */   private final EnderDragonPhaseManager phaseManager;
/*  94 */   private int growlTime = 100;
/*     */   private int sittingDamageReceived;
/*  96 */   private final Node[] nodes = new Node[24];
/*  97 */   private final int[] nodeAdjacency = new int[24];
/*  98 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public EnderDragon(EntityType<? extends EnderDragon> debug1, Level debug2) {
/* 101 */     super(EntityType.ENDER_DRAGON, debug2);
/*     */     
/* 103 */     this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
/* 104 */     this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
/* 105 */     this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
/* 106 */     this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 107 */     this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 108 */     this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 109 */     this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 110 */     this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/*     */     
/* 112 */     this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
/*     */     
/* 114 */     setHealth(getMaxHealth());
/*     */     
/* 116 */     this.noPhysics = true;
/*     */     
/* 118 */     this.noCulling = true;
/*     */     
/* 120 */     if (debug2 instanceof ServerLevel) {
/* 121 */       this.dragonFight = ((ServerLevel)debug2).dragonFight();
/*     */     } else {
/* 123 */       this.dragonFight = null;
/*     */     } 
/*     */     
/* 126 */     this.phaseManager = new EnderDragonPhaseManager(this);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 130 */     return Mob.createMobAttributes()
/* 131 */       .add(Attributes.MAX_HEALTH, 200.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 136 */     super.defineSynchedData();
/* 137 */     getEntityData().define(DATA_PHASE, Integer.valueOf(EnderDragonPhase.HOVERING.getId()));
/*     */   }
/*     */   
/*     */   public double[] getLatencyPos(int debug1, float debug2) {
/* 141 */     if (isDeadOrDying()) {
/* 142 */       debug2 = 0.0F;
/*     */     }
/*     */     
/* 145 */     debug2 = 1.0F - debug2;
/*     */     
/* 147 */     int debug3 = this.posPointer - debug1 & 0x3F;
/* 148 */     int debug4 = this.posPointer - debug1 - 1 & 0x3F;
/* 149 */     double[] debug5 = new double[3];
/* 150 */     double debug6 = this.positions[debug3][0];
/* 151 */     double debug8 = Mth.wrapDegrees(this.positions[debug4][0] - debug6);
/* 152 */     debug5[0] = debug6 + debug8 * debug2;
/*     */     
/* 154 */     debug6 = this.positions[debug3][1];
/* 155 */     debug8 = this.positions[debug4][1] - debug6;
/*     */     
/* 157 */     debug5[1] = debug6 + debug8 * debug2;
/* 158 */     debug5[2] = Mth.lerp(debug2, this.positions[debug3][2], this.positions[debug4][2]);
/* 159 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 164 */     if (this.level.isClientSide) {
/* 165 */       setHealth(getHealth());
/*     */       
/* 167 */       if (!isSilent()) {
/* 168 */         float f1 = Mth.cos(this.flapTime * 6.2831855F);
/* 169 */         float f2 = Mth.cos(this.oFlapTime * 6.2831855F);
/*     */         
/* 171 */         if (f2 <= -0.3F && f1 >= -0.3F) {
/* 172 */           this.level.playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_FLAP, getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
/*     */         }
/*     */         
/* 175 */         if (!this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
/* 176 */           this.level.playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, getSoundSource(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
/* 177 */           this.growlTime = 200 + this.random.nextInt(200);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     this.oFlapTime = this.flapTime;
/*     */     
/* 184 */     if (isDeadOrDying()) {
/* 185 */       float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 186 */       float f2 = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 187 */       float f3 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 188 */       this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, getX() + f1, getY() + 2.0D + f2, getZ() + f3, 0.0D, 0.0D, 0.0D);
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     checkCrystals();
/*     */     
/* 194 */     Vec3 debug1 = getDeltaMovement();
/* 195 */     float debug2 = 0.2F / (Mth.sqrt(getHorizontalDistanceSqr(debug1)) * 10.0F + 1.0F);
/* 196 */     debug2 *= (float)Math.pow(2.0D, debug1.y);
/* 197 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 198 */       this.flapTime += 0.1F;
/* 199 */     } else if (this.inWall) {
/* 200 */       this.flapTime += debug2 * 0.5F;
/*     */     } else {
/* 202 */       this.flapTime += debug2;
/*     */     } 
/*     */     
/* 205 */     this.yRot = Mth.wrapDegrees(this.yRot);
/*     */     
/* 207 */     if (isNoAi()) {
/* 208 */       this.flapTime = 0.5F;
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     if (this.posPointer < 0) {
/* 213 */       for (int j = 0; j < this.positions.length; j++) {
/* 214 */         this.positions[j][0] = this.yRot;
/* 215 */         this.positions[j][1] = getY();
/*     */       } 
/*     */     }
/*     */     
/* 219 */     if (++this.posPointer == this.positions.length) {
/* 220 */       this.posPointer = 0;
/*     */     }
/* 222 */     this.positions[this.posPointer][0] = this.yRot;
/* 223 */     this.positions[this.posPointer][1] = getY();
/*     */     
/* 225 */     if (this.level.isClientSide) {
/* 226 */       if (this.lerpSteps > 0) {
/* 227 */         double d1 = getX() + (this.lerpX - getX()) / this.lerpSteps;
/* 228 */         double d2 = getY() + (this.lerpY - getY()) / this.lerpSteps;
/* 229 */         double d3 = getZ() + (this.lerpZ - getZ()) / this.lerpSteps;
/*     */         
/* 231 */         double d4 = Mth.wrapDegrees(this.lerpYRot - this.yRot);
/*     */         
/* 233 */         this.yRot = (float)(this.yRot + d4 / this.lerpSteps);
/* 234 */         this.xRot = (float)(this.xRot + (this.lerpXRot - this.xRot) / this.lerpSteps);
/*     */         
/* 236 */         this.lerpSteps--;
/* 237 */         setPos(d1, d2, d3);
/* 238 */         setRot(this.yRot, this.xRot);
/*     */       } 
/*     */       
/* 241 */       this.phaseManager.getCurrentPhase().doClientTick();
/*     */     } else {
/* 243 */       DragonPhaseInstance dragonPhaseInstance = this.phaseManager.getCurrentPhase();
/* 244 */       dragonPhaseInstance.doServerTick();
/*     */       
/* 246 */       if (this.phaseManager.getCurrentPhase() != dragonPhaseInstance) {
/* 247 */         dragonPhaseInstance = this.phaseManager.getCurrentPhase();
/* 248 */         dragonPhaseInstance.doServerTick();
/*     */       } 
/*     */       
/* 251 */       Vec3 vec3 = dragonPhaseInstance.getFlyTargetLocation();
/*     */       
/* 253 */       if (vec3 != null) {
/* 254 */         double d1 = vec3.x - getX();
/* 255 */         double d2 = vec3.y - getY();
/* 256 */         double d3 = vec3.z - getZ();
/*     */         
/* 258 */         double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/* 259 */         float f1 = dragonPhaseInstance.getFlySpeed();
/* 260 */         double d5 = Mth.sqrt(d1 * d1 + d3 * d3);
/* 261 */         if (d5 > 0.0D) {
/* 262 */           d2 = Mth.clamp(d2 / d5, -f1, f1);
/*     */         }
/* 264 */         setDeltaMovement(getDeltaMovement().add(0.0D, d2 * 0.01D, 0.0D));
/* 265 */         this.yRot = Mth.wrapDegrees(this.yRot);
/*     */         
/* 267 */         double debug16 = Mth.clamp(Mth.wrapDegrees(180.0D - Mth.atan2(d1, d3) * 57.2957763671875D - this.yRot), -50.0D, 50.0D);
/* 268 */         Vec3 debug18 = vec3.subtract(getX(), getY(), getZ()).normalize();
/* 269 */         Vec3 debug19 = (new Vec3(Mth.sin(this.yRot * 0.017453292F), (getDeltaMovement()).y, -Mth.cos(this.yRot * 0.017453292F))).normalize();
/* 270 */         float debug20 = Math.max(((float)debug19.dot(debug18) + 0.5F) / 1.5F, 0.0F);
/*     */         
/* 272 */         this.yRotA *= 0.8F;
/* 273 */         this.yRotA = (float)(this.yRotA + debug16 * dragonPhaseInstance.getTurnSpeed());
/* 274 */         this.yRot += this.yRotA * 0.1F;
/*     */         
/* 276 */         float debug21 = (float)(2.0D / (d4 + 1.0D));
/* 277 */         float debug22 = 0.06F;
/* 278 */         moveRelative(0.06F * (debug20 * debug21 + 1.0F - debug21), new Vec3(0.0D, 0.0D, -1.0D));
/* 279 */         if (this.inWall) {
/* 280 */           move(MoverType.SELF, getDeltaMovement().scale(0.800000011920929D));
/*     */         } else {
/* 282 */           move(MoverType.SELF, getDeltaMovement());
/*     */         } 
/*     */         
/* 285 */         Vec3 debug23 = getDeltaMovement().normalize();
/* 286 */         double debug24 = 0.8D + 0.15D * (debug23.dot(debug19) + 1.0D) / 2.0D;
/*     */         
/* 288 */         setDeltaMovement(getDeltaMovement().multiply(debug24, 0.9100000262260437D, debug24));
/*     */       } 
/*     */     } 
/*     */     
/* 292 */     this.yBodyRot = this.yRot;
/*     */     
/* 294 */     Vec3[] debug3 = new Vec3[this.subEntities.length];
/* 295 */     for (int i = 0; i < this.subEntities.length; i++) {
/* 296 */       debug3[i] = new Vec3(this.subEntities[i].getX(), this.subEntities[i].getY(), this.subEntities[i].getZ());
/*     */     }
/*     */     
/* 299 */     float debug4 = (float)(getLatencyPos(5, 1.0F)[1] - getLatencyPos(10, 1.0F)[1]) * 10.0F * 0.017453292F;
/* 300 */     float debug5 = Mth.cos(debug4);
/* 301 */     float debug6 = Mth.sin(debug4);
/*     */     
/* 303 */     float debug7 = this.yRot * 0.017453292F;
/* 304 */     float debug8 = Mth.sin(debug7);
/* 305 */     float debug9 = Mth.cos(debug7);
/*     */     
/* 307 */     tickPart(this.body, (debug8 * 0.5F), 0.0D, (-debug9 * 0.5F));
/* 308 */     tickPart(this.wing1, (debug9 * 4.5F), 2.0D, (debug8 * 4.5F));
/* 309 */     tickPart(this.wing2, (debug9 * -4.5F), 2.0D, (debug8 * -4.5F));
/*     */     
/* 311 */     if (!this.level.isClientSide && this.hurtTime == 0) {
/* 312 */       knockBack(this.level.getEntities((Entity)this, this.wing1.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(0.0D, -2.0D, 0.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
/* 313 */       knockBack(this.level.getEntities((Entity)this, this.wing2.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(0.0D, -2.0D, 0.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
/* 314 */       hurt(this.level.getEntities((Entity)this, this.head.getBoundingBox().inflate(1.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
/* 315 */       hurt(this.level.getEntities((Entity)this, this.neck.getBoundingBox().inflate(1.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
/*     */     } 
/*     */     
/* 318 */     float debug10 = Mth.sin(this.yRot * 0.017453292F - this.yRotA * 0.01F);
/* 319 */     float debug11 = Mth.cos(this.yRot * 0.017453292F - this.yRotA * 0.01F);
/* 320 */     float debug12 = getHeadYOffset();
/* 321 */     tickPart(this.head, (debug10 * 6.5F * debug5), (debug12 + debug6 * 6.5F), (-debug11 * 6.5F * debug5));
/* 322 */     tickPart(this.neck, (debug10 * 5.5F * debug5), (debug12 + debug6 * 5.5F), (-debug11 * 5.5F * debug5));
/*     */ 
/*     */     
/* 325 */     double[] debug13 = getLatencyPos(5, 1.0F); int debug14;
/* 326 */     for (debug14 = 0; debug14 < 3; debug14++) {
/* 327 */       EnderDragonPart debug15 = null;
/*     */       
/* 329 */       if (debug14 == 0) {
/* 330 */         debug15 = this.tail1;
/*     */       }
/* 332 */       if (debug14 == 1) {
/* 333 */         debug15 = this.tail2;
/*     */       }
/* 335 */       if (debug14 == 2) {
/* 336 */         debug15 = this.tail3;
/*     */       }
/*     */       
/* 339 */       double[] debug16 = getLatencyPos(12 + debug14 * 2, 1.0F);
/*     */       
/* 341 */       float debug17 = this.yRot * 0.017453292F + rotWrap(debug16[0] - debug13[0]) * 0.017453292F;
/* 342 */       float debug18 = Mth.sin(debug17);
/* 343 */       float debug19 = Mth.cos(debug17);
/*     */       
/* 345 */       float debug20 = 1.5F;
/* 346 */       float debug21 = (debug14 + 1) * 2.0F;
/* 347 */       tickPart(debug15, (-(debug8 * 1.5F + debug18 * debug21) * debug5), debug16[1] - debug13[1] - ((debug21 + 1.5F) * debug6) + 1.5D, ((debug9 * 1.5F + debug19 * debug21) * debug5));
/*     */     } 
/*     */     
/* 350 */     if (!this.level.isClientSide) {
/*     */       
/* 352 */       this.inWall = checkWalls(this.head.getBoundingBox()) | checkWalls(this.neck.getBoundingBox()) | checkWalls(this.body.getBoundingBox());
/*     */       
/* 354 */       if (this.dragonFight != null) {
/* 355 */         this.dragonFight.updateDragon(this);
/*     */       }
/*     */     } 
/* 358 */     for (debug14 = 0; debug14 < this.subEntities.length; debug14++) {
/* 359 */       (this.subEntities[debug14]).xo = (debug3[debug14]).x;
/* 360 */       (this.subEntities[debug14]).yo = (debug3[debug14]).y;
/* 361 */       (this.subEntities[debug14]).zo = (debug3[debug14]).z;
/* 362 */       (this.subEntities[debug14]).xOld = (debug3[debug14]).x;
/* 363 */       (this.subEntities[debug14]).yOld = (debug3[debug14]).y;
/* 364 */       (this.subEntities[debug14]).zOld = (debug3[debug14]).z;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickPart(EnderDragonPart debug1, double debug2, double debug4, double debug6) {
/* 369 */     debug1.setPos(getX() + debug2, getY() + debug4, getZ() + debug6);
/*     */   }
/*     */   
/*     */   private float getHeadYOffset() {
/* 373 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 374 */       return -1.0F;
/*     */     }
/* 376 */     double[] debug1 = getLatencyPos(5, 1.0F);
/* 377 */     double[] debug2 = getLatencyPos(0, 1.0F);
/* 378 */     return (float)(debug1[1] - debug2[1]);
/*     */   }
/*     */   
/*     */   private void checkCrystals() {
/* 382 */     if (this.nearestCrystal != null) {
/* 383 */       if (this.nearestCrystal.removed) {
/* 384 */         this.nearestCrystal = null;
/* 385 */       } else if (this.tickCount % 10 == 0 && 
/* 386 */         getHealth() < getMaxHealth()) {
/* 387 */         setHealth(getHealth() + 1.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 392 */     if (this.random.nextInt(10) == 0) {
/* 393 */       List<EndCrystal> debug1 = this.level.getEntitiesOfClass(EndCrystal.class, getBoundingBox().inflate(32.0D));
/*     */       
/* 395 */       EndCrystal debug2 = null;
/* 396 */       double debug3 = Double.MAX_VALUE;
/* 397 */       for (EndCrystal debug6 : debug1) {
/* 398 */         double debug7 = debug6.distanceToSqr((Entity)this);
/* 399 */         if (debug7 < debug3) {
/* 400 */           debug3 = debug7;
/* 401 */           debug2 = debug6;
/*     */         } 
/*     */       } 
/*     */       
/* 405 */       this.nearestCrystal = debug2;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void knockBack(List<Entity> debug1) {
/* 410 */     double debug2 = ((this.body.getBoundingBox()).minX + (this.body.getBoundingBox()).maxX) / 2.0D;
/* 411 */     double debug4 = ((this.body.getBoundingBox()).minZ + (this.body.getBoundingBox()).maxZ) / 2.0D;
/*     */     
/* 413 */     for (Entity debug7 : debug1) {
/* 414 */       if (debug7 instanceof LivingEntity) {
/* 415 */         double debug8 = debug7.getX() - debug2;
/* 416 */         double debug10 = debug7.getZ() - debug4;
/* 417 */         double debug12 = debug8 * debug8 + debug10 * debug10;
/* 418 */         debug7.push(debug8 / debug12 * 4.0D, 0.20000000298023224D, debug10 / debug12 * 4.0D);
/* 419 */         if (!this.phaseManager.getCurrentPhase().isSitting() && ((LivingEntity)debug7).getLastHurtByMobTimestamp() < debug7.tickCount - 2) {
/* 420 */           debug7.hurt(DamageSource.mobAttack((LivingEntity)this), 5.0F);
/* 421 */           doEnchantDamageEffects((LivingEntity)this, debug7);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hurt(List<Entity> debug1) {
/* 428 */     for (Entity debug3 : debug1) {
/* 429 */       if (debug3 instanceof LivingEntity) {
/* 430 */         debug3.hurt(DamageSource.mobAttack((LivingEntity)this), 10.0F);
/* 431 */         doEnchantDamageEffects((LivingEntity)this, debug3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float rotWrap(double debug1) {
/* 437 */     return (float)Mth.wrapDegrees(debug1);
/*     */   }
/*     */   
/*     */   private boolean checkWalls(AABB debug1) {
/* 441 */     int debug2 = Mth.floor(debug1.minX);
/* 442 */     int debug3 = Mth.floor(debug1.minY);
/* 443 */     int debug4 = Mth.floor(debug1.minZ);
/* 444 */     int debug5 = Mth.floor(debug1.maxX);
/* 445 */     int debug6 = Mth.floor(debug1.maxY);
/* 446 */     int debug7 = Mth.floor(debug1.maxZ);
/* 447 */     boolean debug8 = false;
/* 448 */     boolean debug9 = false;
/* 449 */     for (int debug10 = debug2; debug10 <= debug5; debug10++) {
/* 450 */       for (int debug11 = debug3; debug11 <= debug6; debug11++) {
/* 451 */         for (int debug12 = debug4; debug12 <= debug7; debug12++) {
/* 452 */           BlockPos debug13 = new BlockPos(debug10, debug11, debug12);
/* 453 */           BlockState debug14 = this.level.getBlockState(debug13);
/* 454 */           Block debug15 = debug14.getBlock();
/* 455 */           if (!debug14.isAir() && debug14.getMaterial() != Material.FIRE)
/*     */           {
/* 457 */             if (!this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || BlockTags.DRAGON_IMMUNE.contains(debug15)) {
/* 458 */               debug8 = true;
/*     */             } else {
/* 460 */               debug9 = (this.level.removeBlock(debug13, false) || debug9);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 466 */     if (debug9) {
/*     */ 
/*     */ 
/*     */       
/* 470 */       BlockPos blockPos = new BlockPos(debug2 + this.random.nextInt(debug5 - debug2 + 1), debug3 + this.random.nextInt(debug6 - debug3 + 1), debug4 + this.random.nextInt(debug7 - debug4 + 1));
/*     */       
/* 472 */       this.level.levelEvent(2008, blockPos, 0);
/*     */     } 
/*     */     
/* 475 */     return debug8;
/*     */   }
/*     */   
/*     */   public boolean hurt(EnderDragonPart debug1, DamageSource debug2, float debug3) {
/* 479 */     if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
/* 480 */       return false;
/*     */     }
/*     */     
/* 483 */     debug3 = this.phaseManager.getCurrentPhase().onHurt(debug2, debug3);
/*     */     
/* 485 */     if (debug1 != this.head) {
/* 486 */       debug3 = debug3 / 4.0F + Math.min(debug3, 1.0F);
/*     */     }
/*     */     
/* 489 */     if (debug3 < 0.01F) {
/* 490 */       return false;
/*     */     }
/*     */     
/* 493 */     if (debug2.getEntity() instanceof Player || debug2.isExplosion()) {
/* 494 */       float debug4 = getHealth();
/* 495 */       reallyHurt(debug2, debug3);
/*     */       
/* 497 */       if (isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
/* 498 */         setHealth(1.0F);
/* 499 */         this.phaseManager.setPhase(EnderDragonPhase.DYING);
/*     */       } 
/*     */       
/* 502 */       if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 503 */         this.sittingDamageReceived = (int)(this.sittingDamageReceived + debug4 - getHealth());
/*     */         
/* 505 */         if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
/* 506 */           this.sittingDamageReceived = 0;
/* 507 */           this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 512 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 517 */     if (debug1 instanceof EntityDamageSource && ((EntityDamageSource)debug1).isThorns()) {
/* 518 */       hurt(this.body, debug1, debug2);
/*     */     }
/* 520 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean reallyHurt(DamageSource debug1, float debug2) {
/* 524 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill() {
/* 529 */     remove();
/*     */     
/* 531 */     if (this.dragonFight != null) {
/* 532 */       this.dragonFight.updateDragon(this);
/* 533 */       this.dragonFight.setDragonKilled(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickDeath() {
/* 539 */     if (this.dragonFight != null) {
/* 540 */       this.dragonFight.updateDragon(this);
/*     */     }
/*     */     
/* 543 */     this.dragonDeathTime++;
/* 544 */     if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
/* 545 */       float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 546 */       float f2 = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 547 */       float debug3 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 548 */       this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, getX() + f1, getY() + 2.0D + f2, getZ() + debug3, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */     
/* 551 */     boolean debug1 = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
/* 552 */     int debug2 = 500;
/* 553 */     if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
/* 554 */       debug2 = 12000;
/*     */     }
/*     */     
/* 557 */     if (!this.level.isClientSide) {
/* 558 */       if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && debug1) {
/* 559 */         dropExperience(Mth.floor(debug2 * 0.08F));
/*     */       }
/* 561 */       if (this.dragonDeathTime == 1 && !isSilent()) {
/* 562 */         this.level.globalLevelEvent(1028, blockPosition(), 0);
/*     */       }
/*     */     } 
/* 565 */     move(MoverType.SELF, new Vec3(0.0D, 0.10000000149011612D, 0.0D));
/* 566 */     this.yRot += 20.0F;
/* 567 */     this.yBodyRot = this.yRot;
/*     */     
/* 569 */     if (this.dragonDeathTime == 200 && !this.level.isClientSide) {
/* 570 */       if (debug1) {
/* 571 */         dropExperience(Mth.floor(debug2 * 0.2F));
/*     */       }
/* 573 */       if (this.dragonFight != null) {
/* 574 */         this.dragonFight.setDragonKilled(this);
/*     */       }
/* 576 */       remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dropExperience(int debug1) {
/* 581 */     while (debug1 > 0) {
/* 582 */       int debug2 = ExperienceOrb.getExperienceValue(debug1);
/* 583 */       debug1 -= debug2;
/* 584 */       this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, getX(), getY(), getZ(), debug2));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int findClosestNode() {
/* 590 */     if (this.nodes[0] == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 596 */       for (int debug1 = 0; debug1 < 24; debug1++) {
/* 597 */         int debug4, debug5, debug2 = 5;
/* 598 */         int debug3 = debug1;
/*     */ 
/*     */ 
/*     */         
/* 602 */         if (debug1 < 12) {
/* 603 */           debug4 = Mth.floor(60.0F * Mth.cos(2.0F * (-3.1415927F + 0.2617994F * debug3)));
/* 604 */           debug5 = Mth.floor(60.0F * Mth.sin(2.0F * (-3.1415927F + 0.2617994F * debug3)));
/* 605 */         } else if (debug1 < 20) {
/* 606 */           debug3 -= 12;
/* 607 */           debug4 = Mth.floor(40.0F * Mth.cos(2.0F * (-3.1415927F + 0.3926991F * debug3)));
/* 608 */           debug5 = Mth.floor(40.0F * Mth.sin(2.0F * (-3.1415927F + 0.3926991F * debug3)));
/* 609 */           debug2 += 10;
/*     */         } else {
/* 611 */           debug3 -= 20;
/* 612 */           debug4 = Mth.floor(20.0F * Mth.cos(2.0F * (-3.1415927F + 0.7853982F * debug3)));
/* 613 */           debug5 = Mth.floor(20.0F * Mth.sin(2.0F * (-3.1415927F + 0.7853982F * debug3)));
/*     */         } 
/*     */ 
/*     */         
/* 617 */         int debug6 = Math.max(this.level.getSeaLevel() + 10, this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(debug4, 0, debug5)).getY() + debug2);
/*     */         
/* 619 */         this.nodes[debug1] = new Node(debug4, debug6, debug5);
/*     */       } 
/*     */       
/* 622 */       this.nodeAdjacency[0] = 6146;
/* 623 */       this.nodeAdjacency[1] = 8197;
/* 624 */       this.nodeAdjacency[2] = 8202;
/* 625 */       this.nodeAdjacency[3] = 16404;
/* 626 */       this.nodeAdjacency[4] = 32808;
/* 627 */       this.nodeAdjacency[5] = 32848;
/* 628 */       this.nodeAdjacency[6] = 65696;
/* 629 */       this.nodeAdjacency[7] = 131392;
/* 630 */       this.nodeAdjacency[8] = 131712;
/* 631 */       this.nodeAdjacency[9] = 263424;
/* 632 */       this.nodeAdjacency[10] = 526848;
/* 633 */       this.nodeAdjacency[11] = 525313;
/*     */       
/* 635 */       this.nodeAdjacency[12] = 1581057;
/* 636 */       this.nodeAdjacency[13] = 3166214;
/* 637 */       this.nodeAdjacency[14] = 2138120;
/* 638 */       this.nodeAdjacency[15] = 6373424;
/* 639 */       this.nodeAdjacency[16] = 4358208;
/* 640 */       this.nodeAdjacency[17] = 12910976;
/* 641 */       this.nodeAdjacency[18] = 9044480;
/* 642 */       this.nodeAdjacency[19] = 9706496;
/*     */       
/* 644 */       this.nodeAdjacency[20] = 15216640;
/* 645 */       this.nodeAdjacency[21] = 13688832;
/* 646 */       this.nodeAdjacency[22] = 11763712;
/* 647 */       this.nodeAdjacency[23] = 8257536;
/*     */     } 
/*     */     
/* 650 */     return findClosestNode(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public int findClosestNode(double debug1, double debug3, double debug5) {
/* 654 */     float debug7 = 10000.0F;
/* 655 */     int debug8 = 0;
/* 656 */     Node debug9 = new Node(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5));
/* 657 */     int debug10 = 0;
/*     */     
/* 659 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 661 */       debug10 = 12;
/*     */     }
/*     */     
/* 664 */     for (int debug11 = debug10; debug11 < 24; debug11++) {
/* 665 */       if (this.nodes[debug11] != null) {
/* 666 */         float debug12 = this.nodes[debug11].distanceToSqr(debug9);
/* 667 */         if (debug12 < debug7) {
/* 668 */           debug7 = debug12;
/* 669 */           debug8 = debug11;
/*     */         } 
/*     */       } 
/*     */     } 
/* 673 */     return debug8;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Path findPath(int debug1, int debug2, @Nullable Node debug3) {
/* 678 */     for (int i = 0; i < 24; i++) {
/* 679 */       Node node = this.nodes[i];
/* 680 */       node.closed = false;
/* 681 */       node.f = 0.0F;
/* 682 */       node.g = 0.0F;
/* 683 */       node.h = 0.0F;
/* 684 */       node.cameFrom = null;
/* 685 */       node.heapIdx = -1;
/*     */     } 
/*     */     
/* 688 */     Node debug4 = this.nodes[debug1];
/* 689 */     Node debug5 = this.nodes[debug2];
/*     */     
/* 691 */     debug4.g = 0.0F;
/* 692 */     debug4.h = debug4.distanceTo(debug5);
/* 693 */     debug4.f = debug4.h;
/*     */     
/* 695 */     this.openSet.clear();
/* 696 */     this.openSet.insert(debug4);
/*     */     
/* 698 */     Node debug6 = debug4;
/*     */     
/* 700 */     int debug7 = 0;
/* 701 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 703 */       debug7 = 12;
/*     */     }
/*     */     
/* 706 */     while (!this.openSet.isEmpty()) {
/* 707 */       Node debug8 = this.openSet.pop();
/*     */       
/* 709 */       if (debug8.equals(debug5)) {
/* 710 */         if (debug3 != null) {
/* 711 */           debug3.cameFrom = debug5;
/* 712 */           debug5 = debug3;
/*     */         } 
/* 714 */         return reconstructPath(debug4, debug5);
/*     */       } 
/*     */       
/* 717 */       if (debug8.distanceTo(debug5) < debug6.distanceTo(debug5)) {
/* 718 */         debug6 = debug8;
/*     */       }
/* 720 */       debug8.closed = true;
/*     */       
/* 722 */       int debug9 = 0; int debug10;
/* 723 */       for (debug10 = 0; debug10 < 24; debug10++) {
/* 724 */         if (this.nodes[debug10] == debug8) {
/* 725 */           debug9 = debug10;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 730 */       for (debug10 = debug7; debug10 < 24; debug10++) {
/* 731 */         if ((this.nodeAdjacency[debug9] & 1 << debug10) > 0) {
/* 732 */           Node debug11 = this.nodes[debug10];
/*     */           
/* 734 */           if (!debug11.closed) {
/*     */ 
/*     */ 
/*     */             
/* 738 */             float debug12 = debug8.g + debug8.distanceTo(debug11);
/* 739 */             if (!debug11.inOpenSet() || debug12 < debug11.g) {
/* 740 */               debug11.cameFrom = debug8;
/* 741 */               debug11.g = debug12;
/* 742 */               debug11.h = debug11.distanceTo(debug5);
/* 743 */               if (debug11.inOpenSet()) {
/* 744 */                 this.openSet.changeCost(debug11, debug11.g + debug11.h);
/*     */               } else {
/* 746 */                 debug11.f = debug11.g + debug11.h;
/* 747 */                 this.openSet.insert(debug11);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 754 */     if (debug6 == debug4) {
/* 755 */       return null;
/*     */     }
/* 757 */     LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(debug1), Integer.valueOf(debug2));
/* 758 */     if (debug3 != null) {
/* 759 */       debug3.cameFrom = debug6;
/* 760 */       debug6 = debug3;
/*     */     } 
/* 762 */     return reconstructPath(debug4, debug6);
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node debug1, Node debug2) {
/* 766 */     List<Node> debug3 = Lists.newArrayList();
/* 767 */     Node debug4 = debug2;
/* 768 */     debug3.add(0, debug4);
/* 769 */     while (debug4.cameFrom != null) {
/* 770 */       debug4 = debug4.cameFrom;
/* 771 */       debug3.add(0, debug4);
/*     */     } 
/* 773 */     return new Path(debug3, new BlockPos(debug2.x, debug2.y, debug2.z), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 778 */     super.addAdditionalSaveData(debug1);
/* 779 */     debug1.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 784 */     super.readAdditionalSaveData(debug1);
/* 785 */     if (debug1.contains("DragonPhase")) {
/* 786 */       this.phaseManager.setPhase(EnderDragonPhase.getById(debug1.getInt("DragonPhase")));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {}
/*     */ 
/*     */   
/*     */   public EnderDragonPart[] getSubEntities() {
/* 795 */     return this.subEntities;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 800 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 805 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 810 */     return SoundEvents.ENDER_DRAGON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 815 */     return SoundEvents.ENDER_DRAGON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 820 */     return 5.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getHeadLookVector(float debug1) {
/*     */     Vec3 debug4;
/* 846 */     DragonPhaseInstance debug2 = this.phaseManager.getCurrentPhase();
/* 847 */     EnderDragonPhase<? extends DragonPhaseInstance> debug3 = debug2.getPhase();
/*     */ 
/*     */     
/* 850 */     if (debug3 == EnderDragonPhase.LANDING || debug3 == EnderDragonPhase.TAKEOFF) {
/* 851 */       BlockPos debug5 = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
/* 852 */       float debug6 = Math.max(Mth.sqrt(debug5.distSqr((Position)position(), true)) / 4.0F, 1.0F);
/* 853 */       float debug7 = 6.0F / debug6;
/*     */       
/* 855 */       float debug8 = this.xRot;
/* 856 */       float debug9 = 1.5F;
/* 857 */       this.xRot = -debug7 * 1.5F * 5.0F;
/*     */       
/* 859 */       debug4 = getViewVector(debug1);
/* 860 */       this.xRot = debug8;
/* 861 */     } else if (debug2.isSitting()) {
/* 862 */       float debug5 = this.xRot;
/* 863 */       float debug6 = 1.5F;
/* 864 */       this.xRot = -45.0F;
/*     */       
/* 866 */       debug4 = getViewVector(debug1);
/* 867 */       this.xRot = debug5;
/*     */     } else {
/* 869 */       debug4 = getViewVector(debug1);
/*     */     } 
/*     */     
/* 872 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal debug1, BlockPos debug2, DamageSource debug3) {
/*     */     Player debug4;
/* 878 */     if (debug3.getEntity() instanceof Player) {
/* 879 */       debug4 = (Player)debug3.getEntity();
/*     */     } else {
/* 881 */       debug4 = this.level.getNearestPlayer(CRYSTAL_DESTROY_TARGETING, debug2.getX(), debug2.getY(), debug2.getZ());
/*     */     } 
/*     */     
/* 884 */     if (debug1 == this.nearestCrystal) {
/* 885 */       hurt(this.head, DamageSource.explosion((LivingEntity)debug4), 10.0F);
/*     */     }
/*     */     
/* 888 */     this.phaseManager.getCurrentPhase().onCrystalDestroyed(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 893 */     if (DATA_PHASE.equals(debug1) && this.level.isClientSide) {
/* 894 */       this.phaseManager.setPhase(EnderDragonPhase.getById(((Integer)getEntityData().get(DATA_PHASE)).intValue()));
/*     */     }
/*     */     
/* 897 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */   
/*     */   public EnderDragonPhaseManager getPhaseManager() {
/* 901 */     return this.phaseManager;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public EndDragonFight getDragonFight() {
/* 906 */     return this.dragonFight;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addEffect(MobEffectInstance debug1) {
/* 911 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity debug1) {
/* 916 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canChangeDimensions() {
/* 921 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */