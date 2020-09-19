/*     */ package net.minecraft.world.entity.animal;
/*     */ import java.util.Random;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFish extends WaterAnimal {
/*  43 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public AbstractFish(EntityType<? extends AbstractFish> debug1, Level debug2) {
/*  46 */     super((EntityType)debug1, debug2);
/*     */     
/*  48 */     this.moveControl = new FishMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  53 */     return debug2.height * 0.65F;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  57 */     return Mob.createMobAttributes()
/*  58 */       .add(Attributes.MAX_HEALTH, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/*  63 */     return (super.requiresCustomPersistence() || fromBucket());
/*     */   }
/*     */   
/*     */   public static boolean checkFishSpawnRules(EntityType<? extends AbstractFish> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/*  67 */     return (debug1.getBlockState(debug3).is(Blocks.WATER) && debug1.getBlockState(debug3.above()).is(Blocks.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/*  72 */     return (!fromBucket() && !hasCustomName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/*  77 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  82 */     super.defineSynchedData();
/*     */     
/*  84 */     this.entityData.define(FROM_BUCKET, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private boolean fromBucket() {
/*  88 */     return ((Boolean)this.entityData.get(FROM_BUCKET)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setFromBucket(boolean debug1) {
/*  92 */     this.entityData.set(FROM_BUCKET, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  97 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  99 */     debug1.putBoolean("FromBucket", fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 104 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 106 */     setFromBucket(debug1.getBoolean("FromBucket"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 111 */     super.registerGoals();
/*     */     
/* 113 */     this.goalSelector.addGoal(0, (Goal)new PanicGoal(this, 1.25D));
/* 114 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
/* 115 */     this.goalSelector.addGoal(4, (Goal)new FishSwimGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 120 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 125 */     if (isEffectiveAi() && isInWater()) {
/* 126 */       moveRelative(0.01F, debug1);
/* 127 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 129 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 130 */       if (getTarget() == null) {
/* 131 */         setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */       }
/*     */     } else {
/* 134 */       super.travel(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 140 */     if (!isInWater() && this.onGround && this.verticalCollision) {
/* 141 */       setDeltaMovement(getDeltaMovement().add(((this.random
/* 142 */             .nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645D, ((this.random
/*     */             
/* 144 */             .nextFloat() * 2.0F - 1.0F) * 0.05F)));
/*     */       
/* 146 */       this.onGround = false;
/* 147 */       this.hasImpulse = true;
/* 148 */       playSound(getFlopSound(), getSoundVolume(), getVoicePitch());
/*     */     } 
/*     */     
/* 151 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 156 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 157 */     if (debug3.getItem() == Items.WATER_BUCKET && isAlive()) {
/* 158 */       playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
/*     */       
/* 160 */       debug3.shrink(1);
/* 161 */       ItemStack debug4 = getBucketItemStack();
/* 162 */       saveToBucketTag(debug4);
/*     */       
/* 164 */       if (!this.level.isClientSide) {
/* 165 */         CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)debug1, debug4);
/*     */       }
/*     */       
/* 168 */       if (debug3.isEmpty()) {
/* 169 */         debug1.setItemInHand(debug2, debug4);
/* 170 */       } else if (!debug1.inventory.add(debug4)) {
/* 171 */         debug1.drop(debug4, false);
/*     */       } 
/*     */       
/* 174 */       remove();
/*     */       
/* 176 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 179 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected void saveToBucketTag(ItemStack debug1) {
/* 183 */     if (hasCustomName())
/* 184 */       debug1.setHoverName(getCustomName()); 
/*     */   }
/*     */   
/*     */   protected abstract ItemStack getBucketItemStack();
/*     */   
/*     */   static class FishSwimGoal
/*     */     extends RandomSwimmingGoal {
/*     */     private final AbstractFish fish;
/*     */     
/*     */     public FishSwimGoal(AbstractFish debug1) {
/* 194 */       super(debug1, 1.0D, 40);
/* 195 */       this.fish = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 200 */       return (this.fish.canRandomSwim() && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canRandomSwim() {
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract SoundEvent getFlopSound();
/*     */   
/*     */   static class FishMoveControl extends MoveControl {
/*     */     FishMoveControl(AbstractFish debug1) {
/* 212 */       super((Mob)debug1);
/* 213 */       this.fish = debug1;
/*     */     }
/*     */     private final AbstractFish fish;
/*     */     
/*     */     public void tick() {
/* 218 */       if (this.fish.isEyeInFluid((Tag)FluidTags.WATER))
/*     */       {
/* 220 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */       }
/*     */       
/* 223 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
/* 224 */         this.fish.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 228 */       float debug1 = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 229 */       this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), debug1));
/*     */       
/* 231 */       double debug2 = this.wantedX - this.fish.getX();
/* 232 */       double debug4 = this.wantedY - this.fish.getY();
/* 233 */       double debug6 = this.wantedZ - this.fish.getZ();
/*     */       
/* 235 */       if (debug4 != 0.0D) {
/* 236 */         double debug8 = Mth.sqrt(debug2 * debug2 + debug4 * debug4 + debug6 * debug6);
/*     */         
/* 238 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, this.fish.getSpeed() * debug4 / debug8 * 0.1D, 0.0D));
/*     */       } 
/*     */       
/* 241 */       if (debug2 != 0.0D || debug6 != 0.0D) {
/* 242 */         float debug8 = (float)(Mth.atan2(debug6, debug2) * 57.2957763671875D) - 90.0F;
/*     */         
/* 244 */         this.fish.yRot = rotlerp(this.fish.yRot, debug8, 90.0F);
/* 245 */         this.fish.yBodyRot = this.fish.yRot;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 254 */     return SoundEvents.FISH_SWIM;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\AbstractFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */