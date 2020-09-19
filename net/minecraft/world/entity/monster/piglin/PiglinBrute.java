/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinBrute
/*     */   extends AbstractPiglin
/*     */ {
/*  41 */   protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBrute>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, (Object[])new MemoryModuleType[] { MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PiglinBrute(EntityType<? extends PiglinBrute> debug1, Level debug2) {
/*  72 */     super((EntityType)debug1, debug2);
/*  73 */     this.xpReward = 20;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  77 */     return Monster.createMonsterAttributes()
/*  78 */       .add(Attributes.MAX_HEALTH, 50.0D)
/*  79 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  80 */       .add(Attributes.ATTACK_DAMAGE, 7.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  86 */     PiglinBruteAi.initMemories(this);
/*  87 */     populateDefaultEquipmentSlots(debug2);
/*  88 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/*  93 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_AXE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<PiglinBrute> brainProvider() {
/*  98 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/* 103 */     return PiglinBruteAi.makeBrain(this, brainProvider().makeBrain(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<PiglinBrute> getBrain() {
/* 109 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHunt() {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ItemStack debug1) {
/* 119 */     if (debug1.getItem() == Items.GOLDEN_AXE) {
/* 120 */       return super.wantsToPickUp(debug1);
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 127 */     this.level.getProfiler().push("piglinBruteBrain");
/* 128 */     getBrain().tick((ServerLevel)this.level, (LivingEntity)this);
/* 129 */     this.level.getProfiler().pop();
/*     */     
/* 131 */     PiglinBruteAi.updateActivity(this);
/* 132 */     PiglinBruteAi.maybePlayActivitySound(this);
/*     */     
/* 134 */     super.customServerAiStep();
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
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 148 */     boolean debug3 = super.hurt(debug1, debug2);
/* 149 */     if (this.level.isClientSide) {
/* 150 */       return false;
/*     */     }
/* 152 */     if (debug3 && debug1.getEntity() instanceof LivingEntity) {
/* 153 */       PiglinBruteAi.wasHurtBy(this, (LivingEntity)debug1.getEntity());
/*     */     }
/* 155 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     return SoundEvents.PIGLIN_BRUTE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 165 */     return SoundEvents.PIGLIN_BRUTE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 170 */     return SoundEvents.PIGLIN_BRUTE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 175 */     playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 179 */     playSound(SoundEvents.PIGLIN_BRUTE_ANGRY, 1.0F, getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playConvertedSound() {
/* 184 */     playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0F, getVoicePitch());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\PiglinBrute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */