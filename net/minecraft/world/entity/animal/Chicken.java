/*     */ package net.minecraft.world.entity.animal;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Chicken extends Animal {
/*  38 */   private static final Ingredient FOOD_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.WHEAT_SEEDS, (ItemLike)Items.MELON_SEEDS, (ItemLike)Items.PUMPKIN_SEEDS, (ItemLike)Items.BEETROOT_SEEDS });
/*     */   
/*     */   public float flap;
/*     */   public float flapSpeed;
/*     */   public float oFlapSpeed;
/*     */   public float oFlap;
/*  44 */   public float flapping = 1.0F;
/*     */   public int eggTime;
/*     */   public boolean isChickenJockey;
/*     */   
/*     */   public Chicken(EntityType<? extends Chicken> debug1, Level debug2) {
/*  49 */     super((EntityType)debug1, debug2);
/*     */     
/*  51 */     this.eggTime = this.random.nextInt(6000) + 6000;
/*     */     
/*  53 */     setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  58 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  59 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.4D));
/*  60 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  61 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, false, FOOD_ITEMS));
/*  62 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.1D));
/*  63 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  64 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  65 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  70 */     return isBaby() ? (debug2.height * 0.85F) : (debug2.height * 0.92F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  74 */     return Mob.createMobAttributes()
/*  75 */       .add(Attributes.MAX_HEALTH, 4.0D)
/*  76 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  81 */     super.aiStep();
/*     */     
/*  83 */     this.oFlap = this.flap;
/*  84 */     this.oFlapSpeed = this.flapSpeed;
/*     */     
/*  86 */     this.flapSpeed = (float)(this.flapSpeed + (this.onGround ? -1 : 4) * 0.3D);
/*  87 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/*     */     
/*  89 */     if (!this.onGround && this.flapping < 1.0F) {
/*  90 */       this.flapping = 1.0F;
/*     */     }
/*  92 */     this.flapping = (float)(this.flapping * 0.9D);
/*     */     
/*  94 */     Vec3 debug1 = getDeltaMovement();
/*  95 */     if (!this.onGround && debug1.y < 0.0D) {
/*  96 */       setDeltaMovement(debug1.multiply(1.0D, 0.6D, 1.0D));
/*     */     }
/*     */     
/*  99 */     this.flap += this.flapping * 2.0F;
/*     */     
/* 101 */     if (!this.level.isClientSide && isAlive() && !isBaby() && !isChickenJockey() && --this.eggTime <= 0) {
/* 102 */       playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 103 */       spawnAtLocation((ItemLike)Items.EGG);
/* 104 */       this.eggTime = this.random.nextInt(6000) + 6000;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 115 */     return SoundEvents.CHICKEN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 120 */     return SoundEvents.CHICKEN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 125 */     return SoundEvents.CHICKEN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 130 */     playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Chicken getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 135 */     return (Chicken)EntityType.CHICKEN.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 140 */     return FOOD_ITEMS.test(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperienceReward(Player debug1) {
/* 145 */     if (isChickenJockey()) {
/* 146 */       return 10;
/*     */     }
/* 148 */     return super.getExperienceReward(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 153 */     super.readAdditionalSaveData(debug1);
/* 154 */     this.isChickenJockey = debug1.getBoolean("IsChickenJockey");
/* 155 */     if (debug1.contains("EggLayTime")) {
/* 156 */       this.eggTime = debug1.getInt("EggLayTime");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 162 */     super.addAdditionalSaveData(debug1);
/* 163 */     debug1.putBoolean("IsChickenJockey", this.isChickenJockey);
/* 164 */     debug1.putInt("EggLayTime", this.eggTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 169 */     return isChickenJockey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void positionRider(Entity debug1) {
/* 174 */     super.positionRider(debug1);
/* 175 */     float debug2 = Mth.sin(this.yBodyRot * 0.017453292F);
/* 176 */     float debug3 = Mth.cos(this.yBodyRot * 0.017453292F);
/* 177 */     float debug4 = 0.1F;
/* 178 */     float debug5 = 0.0F;
/*     */     
/* 180 */     debug1.setPos(getX() + (0.1F * debug2), getY(0.5D) + debug1.getMyRidingOffset() + 0.0D, getZ() - (0.1F * debug3));
/* 181 */     if (debug1 instanceof LivingEntity) {
/* 182 */       ((LivingEntity)debug1).yBodyRot = this.yBodyRot;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isChickenJockey() {
/* 187 */     return this.isChickenJockey;
/*     */   }
/*     */   
/*     */   public void setChickenJockey(boolean debug1) {
/* 191 */     this.isChickenJockey = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Chicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */