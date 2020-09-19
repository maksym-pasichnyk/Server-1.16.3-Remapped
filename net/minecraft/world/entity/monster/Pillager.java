/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
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
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Pillager extends AbstractIllager implements CrossbowAttackMob {
/*  59 */   private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final SimpleContainer inventory = new SimpleContainer(5);
/*     */   
/*     */   public Pillager(EntityType<? extends Pillager> debug1, Level debug2) {
/*  69 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  74 */     super.registerGoals();
/*     */     
/*  76 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  77 */     this.goalSelector.addGoal(2, (Goal)new Raider.HoldGroundAttackGoal(this, this, 10.0F));
/*  78 */     this.goalSelector.addGoal(3, (Goal)new RangedCrossbowAttackGoal((Monster)this, 1.0D, 8.0F));
/*  79 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  80 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0F, 1.0F));
/*  81 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 15.0F));
/*     */     
/*  83 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  84 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  85 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  86 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  90 */     return Monster.createMonsterAttributes()
/*  91 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  92 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  93 */       .add(Attributes.ATTACK_DAMAGE, 5.0D)
/*  94 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  99 */     super.defineSynchedData();
/*     */     
/* 101 */     this.entityData.define(IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFireProjectileWeapon(ProjectileWeaponItem debug1) {
/* 106 */     return (debug1 == Items.CROSSBOW);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChargingCrossbow(boolean debug1) {
/* 115 */     this.entityData.set(IS_CHARGING_CROSSBOW, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 120 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 125 */     super.addAdditionalSaveData(debug1);
/* 126 */     ListTag debug2 = new ListTag();
/* 127 */     for (int debug3 = 0; debug3 < this.inventory.getContainerSize(); debug3++) {
/* 128 */       ItemStack debug4 = this.inventory.getItem(debug3);
/* 129 */       if (!debug4.isEmpty()) {
/* 130 */         debug2.add(debug4.save(new CompoundTag()));
/*     */       }
/*     */     } 
/* 133 */     debug1.put("Inventory", (Tag)debug2);
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
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.readAdditionalSaveData(debug1);
/* 152 */     ListTag debug2 = debug1.getList("Inventory", 10);
/* 153 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 154 */       ItemStack debug4 = ItemStack.of(debug2.getCompound(debug3));
/* 155 */       if (!debug4.isEmpty()) {
/* 156 */         this.inventory.addItem(debug4);
/*     */       }
/*     */     } 
/*     */     
/* 160 */     setCanPickUpLoot(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 165 */     BlockState debug3 = debug2.getBlockState(debug1.below());
/* 166 */     if (debug3.is(Blocks.GRASS_BLOCK) || debug3.is(Blocks.SAND)) {
/* 167 */       return 10.0F;
/*     */     }
/* 169 */     return 0.5F - debug2.getBrightness(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 174 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 180 */     populateDefaultEquipmentSlots(debug2);
/* 181 */     populateDefaultEquipmentEnchantments(debug2);
/*     */     
/* 183 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 188 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.CROSSBOW));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enchantSpawnedWeapon(float debug1) {
/* 193 */     super.enchantSpawnedWeapon(debug1);
/*     */     
/* 195 */     if (this.random.nextInt(300) == 0) {
/* 196 */       ItemStack debug2 = getMainHandItem();
/* 197 */       if (debug2.getItem() == Items.CROSSBOW) {
/* 198 */         Map<Enchantment, Integer> debug3 = EnchantmentHelper.getEnchantments(debug2);
/* 199 */         debug3.putIfAbsent(Enchantments.PIERCING, Integer.valueOf(1));
/* 200 */         EnchantmentHelper.setEnchantments(debug3, debug2);
/* 201 */         setItemSlot(EquipmentSlot.MAINHAND, debug2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlliedTo(Entity debug1) {
/* 208 */     if (super.isAlliedTo(debug1)) {
/* 209 */       return true;
/*     */     }
/* 211 */     if (debug1 instanceof LivingEntity && ((LivingEntity)debug1).getMobType() == MobType.ILLAGER)
/*     */     {
/* 213 */       return (getTeam() == null && debug1.getTeam() == null);
/*     */     }
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 220 */     return SoundEvents.PILLAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 225 */     return SoundEvents.PILLAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 230 */     return SoundEvents.PILLAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 235 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shootCrossbowProjectile(LivingEntity debug1, ItemStack debug2, Projectile debug3, float debug4) {
/* 240 */     shootCrossbowProjectile((LivingEntity)this, debug1, debug3, debug4, 1.6F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ItemEntity debug1) {
/* 249 */     ItemStack debug2 = debug1.getItem();
/* 250 */     if (debug2.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 251 */       super.pickUpItem(debug1);
/*     */     } else {
/* 253 */       Item debug3 = debug2.getItem();
/* 254 */       if (wantsItem(debug3)) {
/* 255 */         onItemPickup(debug1);
/* 256 */         ItemStack debug4 = this.inventory.addItem(debug2);
/* 257 */         if (debug4.isEmpty()) {
/* 258 */           debug1.remove();
/*     */         } else {
/* 260 */           debug2.setCount(debug4.getCount());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean wantsItem(Item debug1) {
/* 267 */     return (hasActiveRaid() && debug1 == Items.WHITE_BANNER);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 272 */     if (super.setSlot(debug1, debug2)) {
/* 273 */       return true;
/*     */     }
/* 275 */     int debug3 = debug1 - 300;
/* 276 */     if (debug3 >= 0 && debug3 < this.inventory.getContainerSize()) {
/* 277 */       this.inventory.setItem(debug3, debug2);
/* 278 */       return true;
/*     */     } 
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {
/* 285 */     Raid debug3 = getCurrentRaid();
/* 286 */     boolean debug4 = (this.random.nextFloat() <= debug3.getEnchantOdds());
/*     */     
/* 288 */     if (debug4) {
/* 289 */       ItemStack debug5 = new ItemStack((ItemLike)Items.CROSSBOW);
/* 290 */       Map<Enchantment, Integer> debug6 = Maps.newHashMap();
/*     */       
/* 292 */       if (debug1 > debug3.getNumGroups(Difficulty.NORMAL)) {
/* 293 */         debug6.put(Enchantments.QUICK_CHARGE, Integer.valueOf(2));
/* 294 */       } else if (debug1 > debug3.getNumGroups(Difficulty.EASY)) {
/* 295 */         debug6.put(Enchantments.QUICK_CHARGE, Integer.valueOf(1));
/*     */       } 
/* 297 */       debug6.put(Enchantments.MULTISHOT, Integer.valueOf(1));
/*     */       
/* 299 */       EnchantmentHelper.setEnchantments(debug6, debug5);
/* 300 */       setItemSlot(EquipmentSlot.MAINHAND, debug5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 306 */     return SoundEvents.PILLAGER_CELEBRATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Pillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */