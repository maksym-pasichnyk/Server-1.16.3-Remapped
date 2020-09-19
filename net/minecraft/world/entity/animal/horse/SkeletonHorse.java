/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class SkeletonHorse extends AbstractHorse {
/*  25 */   private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
/*     */   
/*     */   private boolean isTrap;
/*     */   
/*     */   private int trapTime;
/*     */   
/*     */   public SkeletonHorse(EntityType<? extends SkeletonHorse> debug1, Level debug2) {
/*  32 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  36 */     return createBaseHorseAttributes()
/*  37 */       .add(Attributes.MAX_HEALTH, 15.0D)
/*  38 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes() {
/*  43 */     getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateRandomJumpStrength());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  53 */     super.getAmbientSound();
/*  54 */     if (isEyeInFluid((Tag)FluidTags.WATER)) {
/*  55 */       return SoundEvents.SKELETON_HORSE_AMBIENT_WATER;
/*     */     }
/*  57 */     return SoundEvents.SKELETON_HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  63 */     super.getDeathSound();
/*  64 */     return SoundEvents.SKELETON_HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  69 */     super.getHurtSound(debug1);
/*  70 */     return SoundEvents.SKELETON_HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  75 */     if (this.onGround) {
/*  76 */       if (isVehicle()) {
/*  77 */         this.gallopSoundCounter++;
/*  78 */         if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0)
/*  79 */           return SoundEvents.SKELETON_HORSE_GALLOP_WATER; 
/*  80 */         if (this.gallopSoundCounter <= 5) {
/*  81 */           return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */         }
/*     */       } else {
/*  84 */         return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */       } 
/*     */     }
/*  87 */     return SoundEvents.SKELETON_HORSE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playSwimSound(float debug1) {
/*  92 */     if (this.onGround) {
/*  93 */       super.playSwimSound(0.3F);
/*     */     } else {
/*  95 */       super.playSwimSound(Math.min(0.1F, debug1 * 25.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playJumpSound() {
/* 101 */     if (isInWater()) {
/* 102 */       playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
/*     */     } else {
/* 104 */       super.playJumpSound();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 110 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 115 */     return super.getPassengersRidingOffset() - 0.1875D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 120 */     super.aiStep();
/*     */     
/* 122 */     if (isTrap() && this.trapTime++ >= 18000) {
/* 123 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 129 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 131 */     debug1.putBoolean("SkeletonTrap", isTrap());
/* 132 */     debug1.putInt("SkeletonTrapTime", this.trapTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 137 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 139 */     setTrap(debug1.getBoolean("SkeletonTrap"));
/* 140 */     this.trapTime = debug1.getInt("SkeletonTrapTime");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean rideableUnderWater() {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 150 */     return 0.96F;
/*     */   }
/*     */   
/*     */   public boolean isTrap() {
/* 154 */     return this.isTrap;
/*     */   }
/*     */   
/*     */   public void setTrap(boolean debug1) {
/* 158 */     if (debug1 == this.isTrap) {
/*     */       return;
/*     */     }
/*     */     
/* 162 */     this.isTrap = debug1;
/* 163 */     if (debug1) {
/* 164 */       this.goalSelector.addGoal(1, this.skeletonTrapGoal);
/*     */     } else {
/* 166 */       this.goalSelector.removeGoal(this.skeletonTrapGoal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 173 */     return (AgableMob)EntityType.SKELETON_HORSE.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 178 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/* 180 */     if (!isTamed()) {
/* 181 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 184 */     if (isBaby()) {
/* 185 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/* 188 */     if (debug1.isSecondaryUseActive()) {
/* 189 */       openInventory(debug1);
/* 190 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 193 */     if (isVehicle()) {
/* 194 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/* 197 */     if (!debug3.isEmpty()) {
/* 198 */       if (debug3.getItem() == Items.SADDLE && !isSaddled()) {
/* 199 */         openInventory(debug1);
/* 200 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */ 
/*     */       
/* 204 */       InteractionResult debug4 = debug3.interactLivingEntity(debug1, (LivingEntity)this, debug2);
/* 205 */       if (debug4.consumesAction()) {
/* 206 */         return debug4;
/*     */       }
/*     */     } 
/*     */     
/* 210 */     doPlayerRide(debug1);
/* 211 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\SkeletonHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */