/*     */ package net.minecraft.world.entity.animal;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ItemBasedSteering;
/*     */ import net.minecraft.world.entity.ItemSteerable;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Saddleable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.monster.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Pig extends Animal implements ItemSteerable, Saddleable {
/*  52 */   private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.BOOLEAN);
/*  53 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.INT);
/*  54 */   private static final Ingredient FOOD_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.CARROT, (ItemLike)Items.POTATO, (ItemLike)Items.BEETROOT });
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   public Pig(EntityType<? extends Pig> debug1, Level debug2) {
/*  59 */     super((EntityType)debug1, debug2);
/*  60 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  65 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  66 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.25D));
/*  67 */     this.goalSelector.addGoal(3, (Goal)new BreedGoal(this, 1.0D));
/*  68 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.2D, Ingredient.of(new ItemLike[] { (ItemLike)Items.CARROT_ON_A_STICK }, ), false));
/*  69 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.2D, false, FOOD_ITEMS));
/*  70 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.1D));
/*  71 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  72 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  73 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  77 */     return Mob.createMobAttributes()
/*  78 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  79 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getControllingPassenger() {
/*  85 */     if (getPassengers().isEmpty()) {
/*  86 */       return null;
/*     */     }
/*  88 */     return getPassengers().get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeControlledByRider() {
/*  93 */     Entity debug1 = getControllingPassenger();
/*  94 */     if (!(debug1 instanceof Player)) {
/*  95 */       return false;
/*     */     }
/*  97 */     Player debug2 = (Player)debug1;
/*     */     
/*  99 */     if (debug2.getMainHandItem().getItem() == Items.CARROT_ON_A_STICK || debug2.getOffhandItem().getItem() == Items.CARROT_ON_A_STICK) {
/* 100 */       return true;
/*     */     }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 108 */     if (DATA_BOOST_TIME.equals(debug1) && this.level.isClientSide) {
/* 109 */       this.steering.onSynced();
/*     */     }
/* 111 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 116 */     super.defineSynchedData();
/* 117 */     this.entityData.define(DATA_SADDLE_ID, Boolean.valueOf(false));
/* 118 */     this.entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 123 */     super.addAdditionalSaveData(debug1);
/* 124 */     this.steering.addAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 129 */     super.readAdditionalSaveData(debug1);
/* 130 */     this.steering.readAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 135 */     return SoundEvents.PIG_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 140 */     return SoundEvents.PIG_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 145 */     return SoundEvents.PIG_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 150 */     playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 155 */     boolean debug3 = isFood(debug1.getItemInHand(debug2));
/*     */     
/* 157 */     if (!debug3 && isSaddled() && !isVehicle() && !debug1.isSecondaryUseActive()) {
/* 158 */       if (!this.level.isClientSide) {
/* 159 */         debug1.startRiding((Entity)this);
/*     */       }
/* 161 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 164 */     InteractionResult debug4 = super.mobInteract(debug1, debug2);
/* 165 */     if (!debug4.consumesAction()) {
/* 166 */       ItemStack debug5 = debug1.getItemInHand(debug2);
/* 167 */       if (debug5.getItem() == Items.SADDLE) {
/* 168 */         return debug5.interactLivingEntity(debug1, (LivingEntity)this, debug2);
/*     */       }
/* 170 */       return InteractionResult.PASS;
/*     */     } 
/* 172 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaddleable() {
/* 177 */     return (isAlive() && !isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropEquipment() {
/* 182 */     super.dropEquipment();
/* 183 */     if (isSaddled()) {
/* 184 */       spawnAtLocation((ItemLike)Items.SADDLE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaddled() {
/* 190 */     return this.steering.hasSaddle();
/*     */   }
/*     */ 
/*     */   
/*     */   public void equipSaddle(@Nullable SoundSource debug1) {
/* 195 */     this.steering.setSaddle(true);
/* 196 */     if (debug1 != null) {
/* 197 */       this.level.playSound(null, (Entity)this, SoundEvents.PIG_SADDLE, debug1, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 203 */     Direction debug2 = getMotionDirection();
/* 204 */     if (debug2.getAxis() == Direction.Axis.Y) {
/* 205 */       return super.getDismountLocationForPassenger(debug1);
/*     */     }
/*     */     
/* 208 */     int[][] debug3 = DismountHelper.offsetsForDirection(debug2);
/* 209 */     BlockPos debug4 = blockPosition();
/* 210 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*     */     
/* 212 */     for (UnmodifiableIterator<Pose> unmodifiableIterator = debug1.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose debug7 = unmodifiableIterator.next();
/* 213 */       AABB debug8 = debug1.getLocalBoundsForPose(debug7);
/*     */       
/* 215 */       for (int[] debug12 : debug3) {
/* 216 */         debug5.set(debug4.getX() + debug12[0], debug4.getY(), debug4.getZ() + debug12[1]);
/*     */         
/* 218 */         double debug13 = this.level.getBlockFloorHeight((BlockPos)debug5);
/* 219 */         if (DismountHelper.isBlockFloorValid(debug13)) {
/*     */ 
/*     */ 
/*     */           
/* 223 */           Vec3 debug15 = Vec3.upFromBottomCenterOf((Vec3i)debug5, debug13);
/* 224 */           if (DismountHelper.canDismountTo((CollisionGetter)this.level, debug1, debug8.move(debug15))) {
/* 225 */             debug1.setPose(debug7);
/* 226 */             return debug15;
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */     
/* 231 */     return super.getDismountLocationForPassenger(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/* 236 */     if (debug1.getDifficulty() != Difficulty.PEACEFUL) {
/* 237 */       ZombifiedPiglin debug3 = (ZombifiedPiglin)EntityType.ZOMBIFIED_PIGLIN.create((Level)debug1);
/* 238 */       debug3.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_SWORD));
/* 239 */       debug3.moveTo(getX(), getY(), getZ(), this.yRot, this.xRot);
/* 240 */       debug3.setNoAi(isNoAi());
/* 241 */       debug3.setBaby(isBaby());
/* 242 */       if (hasCustomName()) {
/* 243 */         debug3.setCustomName(getCustomName());
/* 244 */         debug3.setCustomNameVisible(isCustomNameVisible());
/*     */       } 
/* 246 */       debug3.setPersistenceRequired();
/* 247 */       debug1.addFreshEntity((Entity)debug3);
/* 248 */       remove();
/*     */     } else {
/* 250 */       super.thunderHit(debug1, debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 256 */     travel((Mob)this, this.steering, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSteeringSpeed() {
/* 261 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travelWithInput(Vec3 debug1) {
/* 266 */     super.travel(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean boost() {
/* 271 */     return this.steering.boost(getRandom());
/*     */   }
/*     */ 
/*     */   
/*     */   public Pig getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 276 */     return (Pig)EntityType.PIG.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 281 */     return FOOD_ITEMS.test(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Pig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */