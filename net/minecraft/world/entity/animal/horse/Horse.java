/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.HorseArmorItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ 
/*     */ public class Horse extends AbstractHorse {
/*  37 */   private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
/*     */   
/*  39 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);
/*     */   
/*     */   public Horse(EntityType<? extends Horse> debug1, Level debug2) {
/*  42 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes() {
/*  47 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateRandomMaxHealth());
/*  48 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateRandomSpeed());
/*  49 */     getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateRandomJumpStrength());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  54 */     super.defineSynchedData();
/*     */     
/*  56 */     this.entityData.define(DATA_ID_TYPE_VARIANT, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  61 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  63 */     debug1.putInt("Variant", getTypeVariant());
/*     */     
/*  65 */     if (!this.inventory.getItem(1).isEmpty()) {
/*  66 */       debug1.put("ArmorItem", (Tag)this.inventory.getItem(1).save(new CompoundTag()));
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack getArmor() {
/*  71 */     return getItemBySlot(EquipmentSlot.CHEST);
/*     */   }
/*     */   
/*     */   private void setArmor(ItemStack debug1) {
/*  75 */     setItemSlot(EquipmentSlot.CHEST, debug1);
/*  76 */     setDropChance(EquipmentSlot.CHEST, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  81 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  83 */     setTypeVariant(debug1.getInt("Variant"));
/*     */     
/*  85 */     if (debug1.contains("ArmorItem", 10)) {
/*  86 */       ItemStack debug2 = ItemStack.of(debug1.getCompound("ArmorItem"));
/*  87 */       if (!debug2.isEmpty() && isArmor(debug2)) {
/*  88 */         this.inventory.setItem(1, debug2);
/*     */       }
/*     */     } 
/*     */     
/*  92 */     updateContainerEquipment();
/*     */   }
/*     */   
/*     */   private void setTypeVariant(int debug1) {
/*  96 */     this.entityData.set(DATA_ID_TYPE_VARIANT, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private int getTypeVariant() {
/* 100 */     return ((Integer)this.entityData.get(DATA_ID_TYPE_VARIANT)).intValue();
/*     */   }
/*     */   
/*     */   private void setVariantAndMarkings(Variant debug1, Markings debug2) {
/* 104 */     setTypeVariant(debug1.getId() & 0xFF | debug2.getId() << 8 & 0xFF00);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 108 */     return Variant.byId(getTypeVariant() & 0xFF);
/*     */   }
/*     */   
/*     */   public Markings getMarkings() {
/* 112 */     return Markings.byId((getTypeVariant() & 0xFF00) >> 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateContainerEquipment() {
/* 117 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     super.updateContainerEquipment();
/*     */     
/* 123 */     setArmorEquipment(this.inventory.getItem(1));
/*     */     
/* 125 */     setDropChance(EquipmentSlot.CHEST, 0.0F);
/*     */   }
/*     */   
/*     */   private void setArmorEquipment(ItemStack debug1) {
/* 129 */     setArmor(debug1);
/*     */     
/* 131 */     if (!this.level.isClientSide) {
/* 132 */       getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
/* 133 */       if (isArmor(debug1)) {
/* 134 */         int debug2 = ((HorseArmorItem)debug1.getItem()).getProtection();
/* 135 */         if (debug2 != 0) {
/* 136 */           getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", debug2, AttributeModifier.Operation.ADDITION));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerChanged(Container debug1) {
/* 144 */     ItemStack debug2 = getArmor();
/*     */     
/* 146 */     super.containerChanged(debug1);
/*     */     
/* 148 */     ItemStack debug3 = getArmor();
/* 149 */     if (this.tickCount > 20 && isArmor(debug3) && debug2 != debug3) {
/* 150 */       playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playGallopSound(SoundType debug1) {
/* 156 */     super.playGallopSound(debug1);
/* 157 */     if (this.random.nextInt(10) == 0) {
/* 158 */       playSound(SoundEvents.HORSE_BREATHE, debug1.getVolume() * 0.6F, debug1.getPitch());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 164 */     super.getAmbientSound();
/* 165 */     return SoundEvents.HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 170 */     super.getDeathSound();
/* 171 */     return SoundEvents.HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getEatingSound() {
/* 177 */     return SoundEvents.HORSE_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 182 */     super.getHurtSound(debug1);
/* 183 */     return SoundEvents.HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 188 */     super.getAngrySound();
/* 189 */     return SoundEvents.HORSE_ANGRY;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 194 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/* 196 */     if (!isBaby()) {
/* 197 */       if (isTamed() && debug1.isSecondaryUseActive()) {
/* 198 */         openInventory(debug1);
/* 199 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */       
/* 202 */       if (isVehicle()) {
/* 203 */         return super.mobInteract(debug1, debug2);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 208 */     if (!debug3.isEmpty()) {
/* 209 */       if (isFood(debug3)) {
/* 210 */         return fedFood(debug1, debug3);
/*     */       }
/*     */       
/* 213 */       InteractionResult debug4 = debug3.interactLivingEntity(debug1, (LivingEntity)this, debug2);
/* 214 */       if (debug4.consumesAction()) {
/* 215 */         return debug4;
/*     */       }
/*     */       
/* 218 */       if (!isTamed()) {
/* 219 */         makeMad();
/* 220 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */       
/* 223 */       boolean debug5 = (!isBaby() && !isSaddled() && debug3.getItem() == Items.SADDLE);
/* 224 */       if (isArmor(debug3) || debug5) {
/* 225 */         openInventory(debug1);
/* 226 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */     } 
/* 229 */     if (isBaby()) {
/* 230 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/* 233 */     doPlayerRide(debug1);
/*     */     
/* 235 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 240 */     if (debug1 == this) {
/* 241 */       return false;
/*     */     }
/*     */     
/* 244 */     if (debug1 instanceof Donkey || debug1 instanceof Horse) {
/* 245 */       return (canParent() && ((AbstractHorse)debug1).canParent());
/*     */     }
/*     */     
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*     */     AbstractHorse debug3;
/* 254 */     if (debug2 instanceof Donkey) {
/* 255 */       debug3 = (AbstractHorse)EntityType.MULE.create((Level)debug1);
/*     */     } else {
/* 257 */       Variant debug5; Markings debug7; Horse debug4 = (Horse)debug2;
/*     */       
/* 259 */       debug3 = (AbstractHorse)EntityType.HORSE.create((Level)debug1);
/*     */       
/* 261 */       int debug6 = this.random.nextInt(9);
/* 262 */       if (debug6 < 4) {
/* 263 */         debug5 = getVariant();
/* 264 */       } else if (debug6 < 8) {
/* 265 */         debug5 = debug4.getVariant();
/*     */       } else {
/* 267 */         debug5 = (Variant)Util.getRandom((Object[])Variant.values(), this.random);
/*     */       } 
/*     */ 
/*     */       
/* 271 */       int debug8 = this.random.nextInt(5);
/* 272 */       if (debug8 < 2) {
/* 273 */         debug7 = getMarkings();
/* 274 */       } else if (debug8 < 4) {
/* 275 */         debug7 = debug4.getMarkings();
/*     */       } else {
/* 277 */         debug7 = (Markings)Util.getRandom((Object[])Markings.values(), this.random);
/*     */       } 
/*     */       
/* 280 */       ((Horse)debug3).setVariantAndMarkings(debug5, debug7);
/*     */     } 
/*     */     
/* 283 */     setOffspringAttributes(debug2, debug3);
/*     */     
/* 285 */     return (AgableMob)debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWearArmor() {
/* 290 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArmor(ItemStack debug1) {
/* 295 */     return debug1.getItem() instanceof HorseArmorItem;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     HorseGroupData horseGroupData;
/*     */     Variant debug6;
/* 302 */     if (debug4 instanceof HorseGroupData) {
/* 303 */       debug6 = ((HorseGroupData)debug4).variant;
/*     */     } else {
/* 305 */       debug6 = (Variant)Util.getRandom((Object[])Variant.values(), this.random);
/* 306 */       horseGroupData = new HorseGroupData(debug6);
/*     */     } 
/* 308 */     setVariantAndMarkings(debug6, (Markings)Util.getRandom((Object[])Markings.values(), this.random));
/*     */     
/* 310 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)horseGroupData, debug5);
/*     */   }
/*     */   
/*     */   public static class HorseGroupData extends AgableMob.AgableMobGroupData {
/*     */     public final Variant variant;
/*     */     
/*     */     public HorseGroupData(Variant debug1) {
/* 317 */       super(true);
/* 318 */       this.variant = debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Horse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */