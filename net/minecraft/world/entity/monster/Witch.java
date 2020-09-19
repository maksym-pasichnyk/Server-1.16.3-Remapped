/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ThrownPotion;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Witch extends Raider implements RangedAttackMob {
/*  48 */   private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
/*  49 */   private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
/*     */   
/*  51 */   private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int usingTime;
/*     */   
/*     */   private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
/*     */   private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
/*     */   
/*     */   public Witch(EntityType<? extends Witch> debug1, Level debug2) {
/*  59 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*     */ 
/*     */     
/*  67 */     this.healRaidersGoal = new NearestHealableRaiderTargetGoal(this, Raider.class, true, debug1 -> (debug1 != null && hasActiveRaid() && debug1.getType() != EntityType.WITCH));
/*  68 */     this.attackPlayersGoal = new NearestAttackableWitchTargetGoal(this, Player.class, 10, true, false, null);
/*     */     
/*  70 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  71 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 60, 10.0F));
/*  72 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  73 */     this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  74 */     this.goalSelector.addGoal(3, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  76 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class }));
/*  77 */     this.targetSelector.addGoal(2, (Goal)this.healRaidersGoal);
/*  78 */     this.targetSelector.addGoal(3, (Goal)this.attackPlayersGoal);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  83 */     super.defineSynchedData();
/*     */     
/*  85 */     getEntityData().define(DATA_USING_ITEM, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  90 */     return SoundEvents.WITCH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  95 */     return SoundEvents.WITCH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 100 */     return SoundEvents.WITCH_DEATH;
/*     */   }
/*     */   
/*     */   public void setUsingItem(boolean debug1) {
/* 104 */     getEntityData().set(DATA_USING_ITEM, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isDrinkingPotion() {
/* 108 */     return ((Boolean)getEntityData().get(DATA_USING_ITEM)).booleanValue();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 112 */     return Monster.createMonsterAttributes()
/* 113 */       .add(Attributes.MAX_HEALTH, 26.0D)
/* 114 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 119 */     if (!this.level.isClientSide && isAlive()) {
/* 120 */       this.healRaidersGoal.decrementCooldown();
/*     */       
/* 122 */       if (this.healRaidersGoal.getCooldown() <= 0) {
/* 123 */         this.attackPlayersGoal.setCanAttack(true);
/*     */       } else {
/* 125 */         this.attackPlayersGoal.setCanAttack(false);
/*     */       } 
/*     */       
/* 128 */       if (isDrinkingPotion()) {
/* 129 */         if (this.usingTime-- <= 0) {
/* 130 */           setUsingItem(false);
/* 131 */           ItemStack debug1 = getMainHandItem();
/* 132 */           setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */           
/* 134 */           if (debug1.getItem() == Items.POTION) {
/* 135 */             List<MobEffectInstance> debug2 = PotionUtils.getMobEffects(debug1);
/* 136 */             if (debug2 != null) {
/* 137 */               for (MobEffectInstance debug4 : debug2) {
/* 138 */                 addEffect(new MobEffectInstance(debug4));
/*     */               }
/*     */             }
/*     */           } 
/*     */           
/* 143 */           getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
/*     */         } 
/*     */       } else {
/* 146 */         Potion debug1 = null;
/*     */         
/* 148 */         if (this.random.nextFloat() < 0.15F && isEyeInFluid((Tag)FluidTags.WATER) && !hasEffect(MobEffects.WATER_BREATHING)) {
/* 149 */           debug1 = Potions.WATER_BREATHING;
/* 150 */         } else if (this.random.nextFloat() < 0.15F && (isOnFire() || (getLastDamageSource() != null && getLastDamageSource().isFire())) && !hasEffect(MobEffects.FIRE_RESISTANCE)) {
/* 151 */           debug1 = Potions.FIRE_RESISTANCE;
/* 152 */         } else if (this.random.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
/* 153 */           debug1 = Potions.HEALING;
/* 154 */         } else if (this.random.nextFloat() < 0.5F && getTarget() != null && !hasEffect(MobEffects.MOVEMENT_SPEED) && getTarget().distanceToSqr((Entity)this) > 121.0D) {
/* 155 */           debug1 = Potions.SWIFTNESS;
/*     */         } 
/*     */         
/* 158 */         if (debug1 != null) {
/* 159 */           setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack((ItemLike)Items.POTION), debug1));
/* 160 */           this.usingTime = getMainHandItem().getUseDuration();
/* 161 */           setUsingItem(true);
/* 162 */           if (!isSilent()) {
/* 163 */             this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_DRINK, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */           }
/* 165 */           AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/* 166 */           debug2.removeModifier(SPEED_MODIFIER_DRINKING);
/* 167 */           debug2.addTransientModifier(SPEED_MODIFIER_DRINKING);
/*     */         } 
/*     */       } 
/*     */       
/* 171 */       if (this.random.nextFloat() < 7.5E-4F) {
/* 172 */         this.level.broadcastEntityEvent((Entity)this, (byte)15);
/*     */       }
/*     */     } 
/*     */     
/* 176 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 181 */     return SoundEvents.WITCH_CELEBRATE;
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
/*     */   protected float getDamageAfterMagicAbsorb(DamageSource debug1, float debug2) {
/* 197 */     debug2 = super.getDamageAfterMagicAbsorb(debug1, debug2);
/*     */     
/* 199 */     if (debug1.getEntity() == this) {
/* 200 */       debug2 = 0.0F;
/*     */     }
/* 202 */     if (debug1.isMagic()) {
/* 203 */       debug2 = (float)(debug2 * 0.15D);
/*     */     }
/*     */     
/* 206 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 211 */     if (isDrinkingPotion()) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     Vec3 debug3 = debug1.getDeltaMovement();
/* 216 */     double debug4 = debug1.getX() + debug3.x - getX();
/* 217 */     double debug6 = debug1.getEyeY() - 1.100000023841858D - getY();
/* 218 */     double debug8 = debug1.getZ() + debug3.z - getZ();
/* 219 */     float debug10 = Mth.sqrt(debug4 * debug4 + debug8 * debug8);
/* 220 */     Potion debug11 = Potions.HARMING;
/*     */ 
/*     */     
/* 223 */     if (debug1 instanceof Raider) {
/* 224 */       if (debug1.getHealth() <= 4.0F) {
/* 225 */         debug11 = Potions.HEALING;
/*     */       } else {
/* 227 */         debug11 = Potions.REGENERATION;
/*     */       } 
/* 229 */       setTarget(null);
/*     */     }
/* 231 */     else if (debug10 >= 8.0F && !debug1.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
/* 232 */       debug11 = Potions.SLOWNESS;
/* 233 */     } else if (debug1.getHealth() >= 8.0F && !debug1.hasEffect(MobEffects.POISON)) {
/* 234 */       debug11 = Potions.POISON;
/* 235 */     } else if (debug10 <= 3.0F && !debug1.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
/* 236 */       debug11 = Potions.WEAKNESS;
/*     */     } 
/*     */ 
/*     */     
/* 240 */     ThrownPotion debug12 = new ThrownPotion(this.level, (LivingEntity)this);
/* 241 */     debug12.setItem(PotionUtils.setPotion(new ItemStack((ItemLike)Items.SPLASH_POTION), debug11));
/* 242 */     debug12.xRot -= -20.0F;
/* 243 */     debug12.shoot(debug4, debug6 + (debug10 * 0.2F), debug8, 0.75F, 8.0F);
/* 244 */     if (!isSilent()) {
/* 245 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_THROW, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */     }
/*     */     
/* 248 */     this.level.addFreshEntity((Entity)debug12);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 253 */     return 1.62F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {}
/*     */ 
/*     */   
/*     */   public boolean canBeLeader() {
/* 262 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Witch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */