/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.time.LocalDate;
/*     */ import java.time.temporal.ChronoField;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.animal.Wolf;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class AbstractSkeleton extends Monster implements RangedAttackMob {
/*  52 */   private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal(this, 1.0D, 20, 15.0F);
/*  53 */   private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false)
/*     */     {
/*     */       public void stop() {
/*  56 */         super.stop();
/*  57 */         AbstractSkeleton.this.setAggressive(false);
/*     */       }
/*     */ 
/*     */       
/*     */       public void start() {
/*  62 */         super.start();
/*  63 */         AbstractSkeleton.this.setAggressive(true);
/*     */       }
/*     */     };
/*     */   
/*     */   protected AbstractSkeleton(EntityType<? extends AbstractSkeleton> debug1, Level debug2) {
/*  68 */     super((EntityType)debug1, debug2);
/*     */     
/*  70 */     reassessWeaponGoal();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  75 */     this.goalSelector.addGoal(2, (Goal)new RestrictSunGoal(this));
/*  76 */     this.goalSelector.addGoal(3, (Goal)new FleeSunGoal(this, 1.0D));
/*  77 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Wolf.class, 6.0F, 1.0D, 1.2D));
/*  78 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  79 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  80 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  82 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*  83 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  84 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*  85 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  89 */     return Monster.createMonsterAttributes()
/*  90 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  95 */     playSound(getStepSound(), 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   abstract SoundEvent getStepSound();
/*     */   
/*     */   public MobType getMobType() {
/* 102 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 107 */     boolean debug1 = isSunBurnTick();
/* 108 */     if (debug1) {
/* 109 */       ItemStack debug2 = getItemBySlot(EquipmentSlot.HEAD);
/* 110 */       if (!debug2.isEmpty()) {
/* 111 */         if (debug2.isDamageableItem()) {
/* 112 */           debug2.setDamageValue(debug2.getDamageValue() + this.random.nextInt(2));
/* 113 */           if (debug2.getDamageValue() >= debug2.getMaxDamage()) {
/* 114 */             broadcastBreakEvent(EquipmentSlot.HEAD);
/* 115 */             setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
/*     */           } 
/*     */         } 
/*     */         
/* 119 */         debug1 = false;
/*     */       } 
/*     */       
/* 122 */       if (debug1) {
/* 123 */         setSecondsOnFire(8);
/*     */       }
/*     */     } 
/*     */     
/* 127 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public void rideTick() {
/* 132 */     super.rideTick();
/*     */     
/* 134 */     if (getVehicle() instanceof PathfinderMob) {
/* 135 */       PathfinderMob debug1 = (PathfinderMob)getVehicle();
/* 136 */       this.yBodyRot = debug1.yBodyRot;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 142 */     super.populateDefaultEquipmentSlots(debug1);
/*     */     
/* 144 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.BOW));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 150 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 152 */     populateDefaultEquipmentSlots(debug2);
/* 153 */     populateDefaultEquipmentEnchantments(debug2);
/* 154 */     reassessWeaponGoal();
/*     */     
/* 156 */     setCanPickUpLoot((this.random.nextFloat() < 0.55F * debug2.getSpecialMultiplier()));
/*     */     
/* 158 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 159 */       LocalDate debug6 = LocalDate.now();
/* 160 */       int debug7 = debug6.get(ChronoField.DAY_OF_MONTH);
/* 161 */       int debug8 = debug6.get(ChronoField.MONTH_OF_YEAR);
/*     */       
/* 163 */       if (debug8 == 10 && debug7 == 31 && this.random.nextFloat() < 0.25F) {
/*     */         
/* 165 */         setItemSlot(EquipmentSlot.HEAD, new ItemStack((this.random.nextFloat() < 0.1F) ? (ItemLike)Blocks.JACK_O_LANTERN : (ItemLike)Blocks.CARVED_PUMPKIN));
/* 166 */         this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
/*     */       } 
/*     */     } 
/* 169 */     return debug4;
/*     */   }
/*     */   
/*     */   public void reassessWeaponGoal() {
/* 173 */     if (this.level == null || this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     this.goalSelector.removeGoal((Goal)this.meleeGoal);
/* 178 */     this.goalSelector.removeGoal((Goal)this.bowGoal);
/*     */     
/* 180 */     ItemStack debug1 = getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, Items.BOW));
/* 181 */     if (debug1.getItem() == Items.BOW) {
/*     */       
/* 183 */       int debug2 = 20;
/* 184 */       if (this.level.getDifficulty() != Difficulty.HARD) {
/* 185 */         debug2 = 40;
/*     */       }
/* 187 */       this.bowGoal.setMinAttackInterval(debug2);
/* 188 */       this.goalSelector.addGoal(4, (Goal)this.bowGoal);
/*     */     } else {
/* 190 */       this.goalSelector.addGoal(4, (Goal)this.meleeGoal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 196 */     ItemStack debug3 = getProjectile(getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, Items.BOW)));
/* 197 */     AbstractArrow debug4 = getArrow(debug3, debug2);
/*     */     
/* 199 */     double debug5 = debug1.getX() - getX();
/* 200 */     double debug7 = debug1.getY(0.3333333333333333D) - debug4.getY();
/* 201 */     double debug9 = debug1.getZ() - getZ();
/* 202 */     double debug11 = Mth.sqrt(debug5 * debug5 + debug9 * debug9);
/* 203 */     debug4.shoot(debug5, debug7 + debug11 * 0.20000000298023224D, debug9, 1.6F, (14 - this.level.getDifficulty().getId() * 4));
/* 204 */     playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/* 205 */     this.level.addFreshEntity((Entity)debug4);
/*     */   }
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack debug1, float debug2) {
/* 209 */     return ProjectileUtil.getMobArrow((LivingEntity)this, debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFireProjectileWeapon(ProjectileWeaponItem debug1) {
/* 214 */     return (debug1 == Items.BOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 219 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 221 */     reassessWeaponGoal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemSlot(EquipmentSlot debug1, ItemStack debug2) {
/* 226 */     super.setItemSlot(debug1, debug2);
/*     */     
/* 228 */     if (!this.level.isClientSide) {
/* 229 */       reassessWeaponGoal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 235 */     return 1.74F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/* 240 */     return -0.6D;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\AbstractSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */