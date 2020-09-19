/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.Sheep;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class Evoker extends SpellcasterIllager {
/*     */   public Evoker(EntityType<? extends Evoker> debug1, Level debug2) {
/*  47 */     super((EntityType)debug1, debug2);
/*     */     
/*  49 */     this.xpReward = 10;
/*     */   }
/*     */   private Sheep wololoTarget;
/*     */   
/*     */   protected void registerGoals() {
/*  54 */     super.registerGoals();
/*     */     
/*  56 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  57 */     this.goalSelector.addGoal(1, new EvokerCastingSpellGoal());
/*  58 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 8.0F, 0.6D, 1.0D));
/*  59 */     this.goalSelector.addGoal(4, new EvokerSummonSpellGoal());
/*  60 */     this.goalSelector.addGoal(5, new EvokerAttackSpellGoal());
/*  61 */     this.goalSelector.addGoal(6, new EvokerWololoSpellGoal());
/*  62 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  63 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  64 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */     
/*  66 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  67 */     this.targetSelector.addGoal(2, (Goal)(new NearestAttackableTargetGoal((Mob)this, Player.class, true)).setUnseenMemoryTicks(300));
/*  68 */     this.targetSelector.addGoal(3, (Goal)(new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
/*  69 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  73 */     return Monster.createMonsterAttributes()
/*  74 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  75 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/*  76 */       .add(Attributes.MAX_HEALTH, 24.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  81 */     super.defineSynchedData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  86 */     super.readAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/*  91 */     return SoundEvents.EVOKER_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  96 */     super.addAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 101 */     super.customServerAiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlliedTo(Entity debug1) {
/* 106 */     if (debug1 == null) {
/* 107 */       return false;
/*     */     }
/* 109 */     if (debug1 == this) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (super.isAlliedTo(debug1)) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (debug1 instanceof Vex) {
/* 116 */       return isAlliedTo((Entity)((Vex)debug1).getOwner());
/*     */     }
/* 118 */     if (debug1 instanceof LivingEntity && ((LivingEntity)debug1).getMobType() == MobType.ILLAGER)
/*     */     {
/* 120 */       return (getTeam() == null && debug1.getTeam() == null);
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 127 */     return SoundEvents.EVOKER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 132 */     return SoundEvents.EVOKER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 137 */     return SoundEvents.EVOKER_HURT;
/*     */   }
/*     */   
/*     */   private void setWololoTarget(@Nullable Sheep debug1) {
/* 141 */     this.wololoTarget = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Sheep getWololoTarget() {
/* 146 */     return this.wololoTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getCastingSoundEvent() {
/* 151 */     return SoundEvents.EVOKER_CAST_SPELL;
/*     */   }
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {}
/*     */   
/*     */   class EvokerCastingSpellGoal
/*     */     extends SpellcasterIllager.SpellcasterCastingSpellGoal {
/*     */     private EvokerCastingSpellGoal() {}
/*     */     
/*     */     public void tick() {
/* 161 */       if (Evoker.this.getTarget() != null) {
/* 162 */         Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/* 163 */       } else if (Evoker.this.getWololoTarget() != null) {
/* 164 */         Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getWololoTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal { private EvokerAttackSpellGoal() {}
/*     */     
/*     */     protected int getCastingTime() {
/* 172 */       return 40;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingInterval() {
/* 177 */       return 100;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 183 */       LivingEntity debug1 = Evoker.this.getTarget();
/* 184 */       double debug2 = Math.min(debug1.getY(), Evoker.this.getY());
/* 185 */       double debug4 = Math.max(debug1.getY(), Evoker.this.getY()) + 1.0D;
/* 186 */       float debug6 = (float)Mth.atan2(debug1.getZ() - Evoker.this.getZ(), debug1.getX() - Evoker.this.getX());
/* 187 */       if (Evoker.this.distanceToSqr((Entity)debug1) < 9.0D) {
/*     */         int debug7;
/* 189 */         for (debug7 = 0; debug7 < 5; debug7++) {
/* 190 */           float debug8 = debug6 + debug7 * 3.1415927F * 0.4F;
/* 191 */           createSpellEntity(Evoker.this.getX() + Mth.cos(debug8) * 1.5D, Evoker.this.getZ() + Mth.sin(debug8) * 1.5D, debug2, debug4, debug8, 0);
/*     */         } 
/*     */         
/* 194 */         for (debug7 = 0; debug7 < 8; debug7++) {
/* 195 */           float debug8 = debug6 + debug7 * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
/* 196 */           createSpellEntity(Evoker.this.getX() + Mth.cos(debug8) * 2.5D, Evoker.this.getZ() + Mth.sin(debug8) * 2.5D, debug2, debug4, debug8, 3);
/*     */         } 
/*     */       } else {
/*     */         
/* 200 */         for (int debug7 = 0; debug7 < 16; debug7++) {
/* 201 */           double debug8 = 1.25D * (debug7 + 1);
/* 202 */           int debug10 = 1 * debug7;
/* 203 */           createSpellEntity(Evoker.this.getX() + Mth.cos(debug6) * debug8, Evoker.this.getZ() + Mth.sin(debug6) * debug8, debug2, debug4, debug6, debug10);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void createSpellEntity(double debug1, double debug3, double debug5, double debug7, float debug9, int debug10) {
/* 210 */       BlockPos debug11 = new BlockPos(debug1, debug7, debug3);
/* 211 */       boolean debug12 = false;
/* 212 */       double debug13 = 0.0D;
/*     */       do {
/* 214 */         BlockPos debug15 = debug11.below();
/* 215 */         BlockState debug16 = Evoker.this.level.getBlockState(debug15);
/* 216 */         if (debug16.isFaceSturdy((BlockGetter)Evoker.this.level, debug15, Direction.UP)) {
/* 217 */           if (!Evoker.this.level.isEmptyBlock(debug11)) {
/* 218 */             BlockState debug17 = Evoker.this.level.getBlockState(debug11);
/* 219 */             VoxelShape debug18 = debug17.getCollisionShape((BlockGetter)Evoker.this.level, debug11);
/* 220 */             if (!debug18.isEmpty()) {
/* 221 */               debug13 = debug18.max(Direction.Axis.Y);
/*     */             }
/*     */           } 
/* 224 */           debug12 = true;
/*     */           break;
/*     */         } 
/* 227 */         debug11 = debug11.below();
/* 228 */       } while (debug11.getY() >= Mth.floor(debug5) - 1);
/* 229 */       if (debug12) {
/* 230 */         Evoker.this.level.addFreshEntity((Entity)new EvokerFangs(Evoker.this.level, debug1, debug11.getY() + debug13, debug3, debug9, debug10, (LivingEntity)Evoker.this));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 236 */       return SoundEvents.EVOKER_PREPARE_ATTACK;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 241 */       return SpellcasterIllager.IllagerSpell.FANGS;
/*     */     } }
/*     */   
/*     */   class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private EvokerSummonSpellGoal() {
/* 246 */       this.vexCountTargeting = (new TargetingConditions()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
/*     */     }
/*     */     private final TargetingConditions vexCountTargeting;
/*     */     public boolean canUse() {
/* 250 */       if (!super.canUse()) {
/* 251 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 255 */       int debug1 = Evoker.this.level.getNearbyEntities(Vex.class, this.vexCountTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D)).size();
/* 256 */       return (Evoker.this.random.nextInt(8) + 1 > debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingTime() {
/* 261 */       return 100;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingInterval() {
/* 266 */       return 340;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 271 */       ServerLevel debug1 = (ServerLevel)Evoker.this.level;
/* 272 */       for (int debug2 = 0; debug2 < 3; debug2++) {
/* 273 */         BlockPos debug3 = Evoker.this.blockPosition().offset(-2 + Evoker.this.random.nextInt(5), 1, -2 + Evoker.this.random.nextInt(5));
/* 274 */         Vex debug4 = (Vex)EntityType.VEX.create(Evoker.this.level);
/* 275 */         debug4.moveTo(debug3, 0.0F, 0.0F);
/* 276 */         debug4.finalizeSpawn((ServerLevelAccessor)debug1, Evoker.this.level.getCurrentDifficultyAt(debug3), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
/* 277 */         debug4.setOwner((Mob)Evoker.this);
/* 278 */         debug4.setBoundOrigin(debug3);
/* 279 */         debug4.setLimitedLife(20 * (30 + Evoker.this.random.nextInt(90)));
/* 280 */         debug1.addFreshEntityWithPassengers((Entity)debug4);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 286 */       return SoundEvents.EVOKER_PREPARE_SUMMON;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 291 */       return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
/*     */     } }
/*     */   public class EvokerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal { private final TargetingConditions wololoTargeting;
/*     */     
/*     */     public EvokerWololoSpellGoal() {
/* 296 */       this.wololoTargeting = (new TargetingConditions()).range(16.0D).allowInvulnerable().selector(debug0 -> (((Sheep)debug0).getColor() == DyeColor.BLUE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 300 */       if (Evoker.this.getTarget() != null)
/*     */       {
/* 302 */         return false;
/*     */       }
/* 304 */       if (Evoker.this.isCastingSpell())
/*     */       {
/* 306 */         return false;
/*     */       }
/* 308 */       if (Evoker.this.tickCount < this.nextAttackTickCount) {
/* 309 */         return false;
/*     */       }
/* 311 */       if (!Evoker.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 312 */         return false;
/*     */       }
/*     */       
/* 315 */       List<Sheep> debug1 = Evoker.this.level.getNearbyEntities(Sheep.class, this.wololoTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
/*     */       
/* 317 */       if (debug1.isEmpty()) {
/* 318 */         return false;
/*     */       }
/* 320 */       Evoker.this.setWololoTarget(debug1.get(Evoker.this.random.nextInt(debug1.size())));
/* 321 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 327 */       return (Evoker.this.getWololoTarget() != null && this.attackWarmupDelay > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 332 */       super.stop();
/* 333 */       Evoker.this.setWololoTarget((Sheep)null);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 338 */       Sheep debug1 = Evoker.this.getWololoTarget();
/* 339 */       if (debug1 != null && debug1.isAlive()) {
/* 340 */         debug1.setColor(DyeColor.RED);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastWarmupTime() {
/* 346 */       return 40;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingTime() {
/* 351 */       return 60;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingInterval() {
/* 356 */       return 140;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 361 */       return SoundEvents.EVOKER_PREPARE_WOLOLO;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 366 */       return SpellcasterIllager.IllagerSpell.WOLOLO;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Evoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */