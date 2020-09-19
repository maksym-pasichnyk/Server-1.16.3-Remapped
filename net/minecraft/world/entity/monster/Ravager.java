/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ravager extends Raider {
/*     */   static {
/*  51 */     NO_RAVAGER_AND_ALIVE = (debug0 -> (debug0.isAlive() && !(debug0 instanceof Ravager)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE;
/*     */ 
/*     */   
/*     */   private int attackTick;
/*     */ 
/*     */   
/*     */   private int stunnedTick;
/*     */   
/*     */   private int roarTick;
/*     */ 
/*     */   
/*     */   public Ravager(EntityType<? extends Ravager> debug1, Level debug2) {
/*  68 */     super(debug1, debug2);
/*     */     
/*  70 */     this.maxUpStep = 1.0F;
/*  71 */     this.xpReward = 20;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  76 */     super.registerGoals();
/*     */     
/*  78 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  79 */     this.goalSelector.addGoal(4, (Goal)new RavagerMeleeAttackGoal());
/*  80 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.4D));
/*  81 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  82 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */     
/*  84 */     this.targetSelector.addGoal(2, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  85 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  86 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, true));
/*  87 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateControlFlags() {
/*  92 */     boolean debug1 = (!(getControllingPassenger() instanceof Mob) || getControllingPassenger().getType().is((Tag)EntityTypeTags.RAIDERS));
/*  93 */     boolean debug2 = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.Boat);
/*  94 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, debug1);
/*  95 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (debug1 && debug2));
/*  96 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, debug1);
/*  97 */     this.goalSelector.setControlFlag(Goal.Flag.TARGET, debug1);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 101 */     return Monster.createMonsterAttributes()
/* 102 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 103 */       .add(Attributes.MOVEMENT_SPEED, 0.3D)
/* 104 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
/* 105 */       .add(Attributes.ATTACK_DAMAGE, 12.0D)
/* 106 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 107 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 112 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 114 */     debug1.putInt("AttackTick", this.attackTick);
/* 115 */     debug1.putInt("StunTick", this.stunnedTick);
/* 116 */     debug1.putInt("RoarTick", this.roarTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 121 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 123 */     this.attackTick = debug1.getInt("AttackTick");
/* 124 */     this.stunnedTick = debug1.getInt("StunTick");
/* 125 */     this.roarTick = debug1.getInt("RoarTick");
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 130 */     return SoundEvents.RAVAGER_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 135 */     return (PathNavigation)new RavagerNavigation((Mob)this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 140 */     return 45;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 145 */     return 2.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeControlledByRider() {
/* 150 */     return (!isNoAi() && getControllingPassenger() instanceof LivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getControllingPassenger() {
/* 156 */     if (getPassengers().isEmpty()) {
/* 157 */       return null;
/*     */     }
/* 159 */     return getPassengers().get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 164 */     super.aiStep();
/*     */     
/* 166 */     if (!isAlive()) {
/*     */       return;
/*     */     }
/*     */     
/* 170 */     if (isImmobile()) {
/* 171 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
/*     */     } else {
/* 173 */       double debug1 = (getTarget() != null) ? 0.35D : 0.3D;
/* 174 */       double debug3 = getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
/* 175 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1D, debug3, debug1));
/*     */     } 
/*     */     
/* 178 */     if (this.horizontalCollision && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 179 */       boolean debug1 = false;
/* 180 */       AABB debug2 = getBoundingBox().inflate(0.2D);
/* 181 */       for (BlockPos debug4 : BlockPos.betweenClosed(Mth.floor(debug2.minX), Mth.floor(debug2.minY), Mth.floor(debug2.minZ), Mth.floor(debug2.maxX), Mth.floor(debug2.maxY), Mth.floor(debug2.maxZ))) {
/* 182 */         BlockState debug5 = this.level.getBlockState(debug4);
/* 183 */         Block debug6 = debug5.getBlock();
/* 184 */         if (debug6 instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 185 */           debug1 = (this.level.destroyBlock(debug4, true, (Entity)this) || debug1);
/*     */         }
/*     */       } 
/*     */       
/* 189 */       if (!debug1 && this.onGround) {
/* 190 */         jumpFromGround();
/*     */       }
/*     */     } 
/*     */     
/* 194 */     if (this.roarTick > 0) {
/* 195 */       this.roarTick--;
/*     */       
/* 197 */       if (this.roarTick == 10) {
/* 198 */         roar();
/*     */       }
/*     */     } 
/* 201 */     if (this.attackTick > 0) {
/* 202 */       this.attackTick--;
/*     */     }
/* 204 */     if (this.stunnedTick > 0) {
/* 205 */       this.stunnedTick--;
/* 206 */       stunEffect();
/*     */       
/* 208 */       if (this.stunnedTick == 0) {
/* 209 */         playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
/* 210 */         this.roarTick = 20;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stunEffect() {
/* 216 */     if (this.random.nextInt(6) == 0) {
/* 217 */       double debug1 = getX() - getBbWidth() * Math.sin((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/* 218 */       double debug3 = getY() + getBbHeight() - 0.3D;
/* 219 */       double debug5 = getZ() + getBbWidth() * Math.cos((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/*     */       
/* 221 */       this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, debug1, debug3, debug5, 0.4980392156862745D, 0.5137254901960784D, 0.5725490196078431D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isImmobile() {
/* 227 */     return (super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSee(Entity debug1) {
/* 232 */     if (this.stunnedTick > 0 || this.roarTick > 0) {
/* 233 */       return false;
/*     */     }
/* 235 */     return super.canSee(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByShield(LivingEntity debug1) {
/* 240 */     if (this.roarTick == 0) {
/* 241 */       if (this.random.nextDouble() < 0.5D) {
/* 242 */         this.stunnedTick = 40;
/* 243 */         playSound(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
/* 244 */         this.level.broadcastEntityEvent((Entity)this, (byte)39);
/*     */         
/* 246 */         debug1.push((Entity)this);
/*     */       } else {
/* 248 */         strongKnockback((Entity)debug1);
/*     */       } 
/* 250 */       debug1.hurtMarked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void roar() {
/* 255 */     if (isAlive()) {
/* 256 */       List<Entity> debug1 = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), NO_RAVAGER_AND_ALIVE);
/* 257 */       for (Entity entity : debug1) {
/* 258 */         if (!(entity instanceof AbstractIllager)) {
/* 259 */           entity.hurt(DamageSource.mobAttack((LivingEntity)this), 6.0F);
/*     */         }
/* 261 */         strongKnockback(entity);
/*     */       } 
/*     */       
/* 264 */       Vec3 debug2 = getBoundingBox().getCenter();
/* 265 */       for (int debug3 = 0; debug3 < 40; debug3++) {
/* 266 */         double debug4 = this.random.nextGaussian() * 0.2D;
/* 267 */         double debug6 = this.random.nextGaussian() * 0.2D;
/* 268 */         double debug8 = this.random.nextGaussian() * 0.2D;
/* 269 */         this.level.addParticle((ParticleOptions)ParticleTypes.POOF, debug2.x, debug2.y, debug2.z, debug4, debug6, debug8);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void strongKnockback(Entity debug1) {
/* 275 */     double debug2 = debug1.getX() - getX();
/* 276 */     double debug4 = debug1.getZ() - getZ();
/* 277 */     double debug6 = Math.max(debug2 * debug2 + debug4 * debug4, 0.001D);
/* 278 */     debug1.push(debug2 / debug6 * 4.0D, 0.2D, debug4 / debug6 * 4.0D);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 306 */     this.attackTick = 10;
/* 307 */     this.level.broadcastEntityEvent((Entity)this, (byte)4);
/* 308 */     playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/*     */     
/* 310 */     return super.doHurtTarget(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 316 */     return SoundEvents.RAVAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 321 */     return SoundEvents.RAVAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 326 */     return SoundEvents.RAVAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 331 */     playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 336 */     return !debug1.containsAnyLiquid(getBoundingBox());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {}
/*     */ 
/*     */   
/*     */   public boolean canBeLeader() {
/* 345 */     return false;
/*     */   }
/*     */   
/*     */   class RavagerMeleeAttackGoal extends MeleeAttackGoal {
/*     */     public RavagerMeleeAttackGoal() {
/* 350 */       super((PathfinderMob)Ravager.this, 1.0D, true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected double getAttackReachSqr(LivingEntity debug1) {
/* 356 */       float debug2 = Ravager.this.getBbWidth() - 0.1F;
/* 357 */       return (debug2 * 2.0F * debug2 * 2.0F + debug1.getBbWidth());
/*     */     }
/*     */   }
/*     */   
/*     */   static class RavagerNavigation extends GroundPathNavigation {
/*     */     public RavagerNavigation(Mob debug1, Level debug2) {
/* 363 */       super(debug1, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int debug1) {
/* 368 */       this.nodeEvaluator = (NodeEvaluator)new Ravager.RavagerNodeEvaluator();
/* 369 */       return new PathFinder(this.nodeEvaluator, debug1);
/*     */     } }
/*     */   
/*     */   static class RavagerNodeEvaluator extends WalkNodeEvaluator {
/*     */     private RavagerNodeEvaluator() {}
/*     */     
/*     */     protected BlockPathTypes evaluateBlockPathType(BlockGetter debug1, boolean debug2, boolean debug3, BlockPos debug4, BlockPathTypes debug5) {
/* 376 */       if (debug5 == BlockPathTypes.LEAVES) {
/* 377 */         return BlockPathTypes.OPEN;
/*     */       }
/* 379 */       return super.evaluateBlockPathType(debug1, debug2, debug3, debug4, debug5);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Ravager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */