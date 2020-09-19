/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Snowball;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SnowGolem extends AbstractGolem implements Shearable, RangedAttackMob {
/*  44 */   private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SnowGolem(EntityType<? extends SnowGolem> debug1, Level debug2) {
/*  50 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  55 */     this.goalSelector.addGoal(1, (Goal)new RangedAttackGoal(this, 1.25D, 20, 10.0F));
/*  56 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
/*  57 */     this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  58 */     this.goalSelector.addGoal(4, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  60 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 10, true, false, debug0 -> debug0 instanceof net.minecraft.world.entity.monster.Enemy));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  64 */     return Mob.createMobAttributes()
/*  65 */       .add(Attributes.MAX_HEALTH, 4.0D)
/*  66 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  71 */     super.defineSynchedData();
/*  72 */     this.entityData.define(DATA_PUMPKIN_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  77 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  79 */     debug1.putBoolean("Pumpkin", hasPumpkin());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  84 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  86 */     if (debug1.contains("Pumpkin")) {
/*  87 */       setPumpkin(debug1.getBoolean("Pumpkin"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  98 */     super.aiStep();
/*     */     
/* 100 */     if (!this.level.isClientSide) {
/* 101 */       int debug1 = Mth.floor(getX());
/* 102 */       int debug2 = Mth.floor(getY());
/* 103 */       int debug3 = Mth.floor(getZ());
/*     */       
/* 105 */       if (this.level.getBiome(new BlockPos(debug1, 0, debug3)).getTemperature(new BlockPos(debug1, debug2, debug3)) > 1.0F) {
/* 106 */         hurt(DamageSource.ON_FIRE, 1.0F);
/*     */       }
/*     */       
/* 109 */       if (!this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 114 */       BlockState debug4 = Blocks.SNOW.defaultBlockState();
/* 115 */       for (int debug5 = 0; debug5 < 4; debug5++) {
/* 116 */         debug1 = Mth.floor(getX() + ((debug5 % 2 * 2 - 1) * 0.25F));
/* 117 */         debug2 = Mth.floor(getY());
/* 118 */         debug3 = Mth.floor(getZ() + ((debug5 / 2 % 2 * 2 - 1) * 0.25F));
/* 119 */         BlockPos debug6 = new BlockPos(debug1, debug2, debug3);
/* 120 */         if (this.level.getBlockState(debug6).isAir() && this.level.getBiome(debug6).getTemperature(debug6) < 0.8F && debug4.canSurvive((LevelReader)this.level, debug6)) {
/* 121 */           this.level.setBlockAndUpdate(debug6, debug4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 129 */     Snowball debug3 = new Snowball(this.level, (LivingEntity)this);
/* 130 */     double debug4 = debug1.getEyeY() - 1.100000023841858D;
/* 131 */     double debug6 = debug1.getX() - getX();
/* 132 */     double debug8 = debug4 - debug3.getY();
/* 133 */     double debug10 = debug1.getZ() - getZ();
/* 134 */     float debug12 = Mth.sqrt(debug6 * debug6 + debug10 * debug10) * 0.2F;
/* 135 */     debug3.shoot(debug6, debug8 + debug12, debug10, 1.6F, 12.0F);
/*     */     
/* 137 */     playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
/* 138 */     this.level.addFreshEntity((Entity)debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 143 */     return 1.7F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 148 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 149 */     if (debug3.getItem() == Items.SHEARS && readyForShearing()) {
/* 150 */       shear(SoundSource.PLAYERS);
/* 151 */       if (!this.level.isClientSide) {
/* 152 */         debug3.hurtAndBreak(1, (LivingEntity)debug1, debug1 -> debug1.broadcastBreakEvent(debug0));
/*     */       }
/* 154 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/* 156 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(SoundSource debug1) {
/* 161 */     this.level.playSound(null, (Entity)this, SoundEvents.SNOW_GOLEM_SHEAR, debug1, 1.0F, 1.0F);
/*     */     
/* 163 */     if (!this.level.isClientSide()) {
/* 164 */       setPumpkin(false);
/* 165 */       spawnAtLocation(new ItemStack((ItemLike)Items.CARVED_PUMPKIN), 1.7F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 171 */     return (isAlive() && hasPumpkin());
/*     */   }
/*     */   
/*     */   public boolean hasPumpkin() {
/* 175 */     return ((((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   public void setPumpkin(boolean debug1) {
/* 179 */     byte debug2 = ((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue();
/* 180 */     if (debug1) {
/* 181 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(debug2 | 0x10)));
/*     */     } else {
/* 183 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(debug2 & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 190 */     return SoundEvents.SNOW_GOLEM_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 196 */     return SoundEvents.SNOW_GOLEM_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getDeathSound() {
/* 202 */     return SoundEvents.SNOW_GOLEM_DEATH;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\SnowGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */