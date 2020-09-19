/*     */ package net.minecraft.world.entity.monster;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public class Vindicator extends AbstractIllager {
/*     */   private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
/*     */   
/*     */   static {
/*  53 */     DOOR_BREAKING_PREDICATE = (debug0 -> (debug0 == Difficulty.NORMAL || debug0 == Difficulty.HARD));
/*     */   }
/*     */   private boolean isJohnny;
/*     */   
/*     */   public Vindicator(EntityType<? extends Vindicator> debug1, Level debug2) {
/*  58 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  63 */     super.registerGoals();
/*     */     
/*  65 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  66 */     this.goalSelector.addGoal(1, (Goal)new VindicatorBreakDoorGoal((Mob)this));
/*  67 */     this.goalSelector.addGoal(2, (Goal)new AbstractIllager.RaiderOpenDoorGoal(this, this));
/*  68 */     this.goalSelector.addGoal(3, (Goal)new Raider.HoldGroundAttackGoal(this, this, 10.0F));
/*  69 */     this.goalSelector.addGoal(4, (Goal)new VindicatorMeleeAttackGoal(this));
/*  70 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  71 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  72 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, true));
/*  73 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*  74 */     this.targetSelector.addGoal(4, (Goal)new VindicatorJohnnyAttackGoal(this));
/*  75 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  76 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  77 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */   }
/*     */   
/*     */   class VindicatorMeleeAttackGoal extends MeleeAttackGoal {
/*     */     public VindicatorMeleeAttackGoal(Vindicator debug2) {
/*  82 */       super((PathfinderMob)debug2, 1.0D, false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getAttackReachSqr(LivingEntity debug1) {
/*  87 */       if (this.mob.getVehicle() instanceof Ravager) {
/*     */         
/*  89 */         float debug2 = this.mob.getVehicle().getBbWidth() - 0.1F;
/*  90 */         return (debug2 * 2.0F * debug2 * 2.0F + debug1.getBbWidth());
/*     */       } 
/*  92 */       return super.getAttackReachSqr(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/*  98 */     if (!isNoAi() && 
/*  99 */       GoalUtils.hasGroundPathNavigation((Mob)this)) {
/* 100 */       boolean debug1 = ((ServerLevel)this.level).isRaided(blockPosition());
/* 101 */       ((GroundPathNavigation)getNavigation()).setCanOpenDoors(debug1);
/*     */     } 
/*     */ 
/*     */     
/* 105 */     super.customServerAiStep();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 109 */     return Monster.createMonsterAttributes()
/* 110 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/* 111 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/* 112 */       .add(Attributes.MAX_HEALTH, 24.0D)
/* 113 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 118 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 120 */     if (this.isJohnny) {
/* 121 */       debug1.putBoolean("Johnny", true);
/*     */     }
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
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 137 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 139 */     if (debug1.contains("Johnny", 99)) {
/* 140 */       this.isJohnny = debug1.getBoolean("Johnny");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 146 */     return SoundEvents.VINDICATOR_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 152 */     SpawnGroupData debug6 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 154 */     ((GroundPathNavigation)getNavigation()).setCanOpenDoors(true);
/*     */     
/* 156 */     populateDefaultEquipmentSlots(debug2);
/* 157 */     populateDefaultEquipmentEnchantments(debug2);
/*     */     
/* 159 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 164 */     if (getCurrentRaid() == null) {
/* 165 */       setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_AXE));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlliedTo(Entity debug1) {
/* 171 */     if (super.isAlliedTo(debug1)) {
/* 172 */       return true;
/*     */     }
/* 174 */     if (debug1 instanceof LivingEntity && ((LivingEntity)debug1).getMobType() == MobType.ILLAGER)
/*     */     {
/* 176 */       return (getTeam() == null && debug1.getTeam() == null);
/*     */     }
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(@Nullable Component debug1) {
/* 183 */     super.setCustomName(debug1);
/* 184 */     if (!this.isJohnny && debug1 != null && debug1.getString().equals("Johnny")) {
/* 185 */       this.isJohnny = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 191 */     return SoundEvents.VINDICATOR_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 196 */     return SoundEvents.VINDICATOR_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 201 */     return SoundEvents.VINDICATOR_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {
/* 206 */     ItemStack debug3 = new ItemStack((ItemLike)Items.IRON_AXE);
/* 207 */     Raid debug4 = getCurrentRaid();
/* 208 */     int debug5 = 1;
/* 209 */     if (debug1 > debug4.getNumGroups(Difficulty.NORMAL)) {
/* 210 */       debug5 = 2;
/*     */     }
/*     */     
/* 213 */     boolean debug6 = (this.random.nextFloat() <= debug4.getEnchantOdds());
/* 214 */     if (debug6) {
/* 215 */       Map<Enchantment, Integer> debug7 = Maps.newHashMap();
/* 216 */       debug7.put(Enchantments.SHARPNESS, Integer.valueOf(debug5));
/* 217 */       EnchantmentHelper.setEnchantments(debug7, debug3);
/*     */     } 
/*     */     
/* 220 */     setItemSlot(EquipmentSlot.MAINHAND, debug3);
/*     */   }
/*     */   
/*     */   static class VindicatorBreakDoorGoal extends BreakDoorGoal {
/*     */     public VindicatorBreakDoorGoal(Mob debug1) {
/* 225 */       super(debug1, 6, Vindicator.DOOR_BREAKING_PREDICATE);
/* 226 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 231 */       Vindicator debug1 = (Vindicator)this.mob;
/* 232 */       return (debug1.hasActiveRaid() && super.canContinueToUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 237 */       Vindicator debug1 = (Vindicator)this.mob;
/* 238 */       return (debug1.hasActiveRaid() && debug1.random.nextInt(10) == 0 && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 243 */       super.start();
/* 244 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */   
/*     */   static class VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */     public VindicatorJohnnyAttackGoal(Vindicator debug1) {
/* 250 */       super((Mob)debug1, LivingEntity.class, 0, true, true, LivingEntity::attackable);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 255 */       return (((Vindicator)this.mob).isJohnny && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 260 */       super.start();
/* 261 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Vindicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */