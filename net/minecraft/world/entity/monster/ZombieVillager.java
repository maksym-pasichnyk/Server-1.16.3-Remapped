/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.ReputationEventHandler;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.VillagerData;
/*     */ import net.minecraft.world.entity.npc.VillagerDataHolder;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.VillagerType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class ZombieVillager
/*     */   extends Zombie
/*     */   implements VillagerDataHolder {
/*  52 */   private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
/*  53 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.VILLAGER_DATA);
/*     */ 
/*     */   
/*     */   private int villagerConversionTime;
/*     */   
/*     */   private UUID conversionStarter;
/*     */   
/*     */   private Tag gossips;
/*     */   
/*     */   private CompoundTag tradeOffers;
/*     */   
/*     */   private int villagerXp;
/*     */ 
/*     */   
/*     */   public ZombieVillager(EntityType<? extends ZombieVillager> debug1, Level debug2) {
/*  68 */     super((EntityType)debug1, debug2);
/*     */     
/*  70 */     setVillagerData(getVillagerData().setProfession((VillagerProfession)Registry.VILLAGER_PROFESSION.getRandom(this.random)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  75 */     super.defineSynchedData();
/*     */     
/*  77 */     this.entityData.define(DATA_CONVERTING_ID, Boolean.valueOf(false));
/*  78 */     this.entityData.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  83 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  85 */     VillagerData.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, getVillagerData())
/*  86 */       .resultOrPartial(LOGGER::error)
/*  87 */       .ifPresent(debug1 -> debug0.put("VillagerData", debug1));
/*     */     
/*  89 */     if (this.tradeOffers != null) {
/*  90 */       debug1.put("Offers", (Tag)this.tradeOffers);
/*     */     }
/*     */     
/*  93 */     if (this.gossips != null) {
/*  94 */       debug1.put("Gossips", this.gossips);
/*     */     }
/*     */     
/*  97 */     debug1.putInt("ConversionTime", isConverting() ? this.villagerConversionTime : -1);
/*     */     
/*  99 */     if (this.conversionStarter != null) {
/* 100 */       debug1.putUUID("ConversionPlayer", this.conversionStarter);
/*     */     }
/*     */     
/* 103 */     debug1.putInt("Xp", this.villagerXp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 108 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 110 */     if (debug1.contains("VillagerData", 10)) {
/* 111 */       DataResult<VillagerData> debug2 = VillagerData.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1.get("VillagerData")));
/* 112 */       debug2.resultOrPartial(LOGGER::error).ifPresent(this::setVillagerData);
/*     */     } 
/*     */     
/* 115 */     if (debug1.contains("Offers", 10)) {
/* 116 */       this.tradeOffers = debug1.getCompound("Offers");
/*     */     }
/*     */     
/* 119 */     if (debug1.contains("Gossips", 10)) {
/* 120 */       this.gossips = (Tag)debug1.getList("Gossips", 10);
/*     */     }
/*     */     
/* 123 */     if (debug1.contains("ConversionTime", 99) && debug1.getInt("ConversionTime") > -1) {
/* 124 */       startConverting(debug1.hasUUID("ConversionPlayer") ? debug1.getUUID("ConversionPlayer") : null, debug1.getInt("ConversionTime"));
/*     */     }
/*     */     
/* 127 */     if (debug1.contains("Xp", 3)) {
/* 128 */       this.villagerXp = debug1.getInt("Xp");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 134 */     if (!this.level.isClientSide && isAlive() && isConverting()) {
/* 135 */       int debug1 = getConversionProgress();
/*     */       
/* 137 */       this.villagerConversionTime -= debug1;
/*     */       
/* 139 */       if (this.villagerConversionTime <= 0) {
/* 140 */         finishConversion((ServerLevel)this.level);
/*     */       }
/*     */     } 
/*     */     
/* 144 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 149 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 150 */     if (debug3.getItem() == Items.GOLDEN_APPLE) {
/* 151 */       if (hasEffect(MobEffects.WEAKNESS)) {
/* 152 */         if (!debug1.abilities.instabuild) {
/* 153 */           debug3.shrink(1);
/*     */         }
/*     */         
/* 156 */         if (!this.level.isClientSide) {
/* 157 */           startConverting(debug1.getUUID(), this.random.nextInt(2401) + 3600);
/*     */         }
/*     */ 
/*     */         
/* 161 */         return InteractionResult.SUCCESS;
/*     */       } 
/* 163 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 166 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 176 */     return (!isConverting() && this.villagerXp == 0);
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 180 */     return ((Boolean)getEntityData().get(DATA_CONVERTING_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void startConverting(@Nullable UUID debug1, int debug2) {
/* 184 */     this.conversionStarter = debug1;
/* 185 */     this.villagerConversionTime = debug2;
/* 186 */     getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(true));
/*     */     
/* 188 */     removeEffect(MobEffects.WEAKNESS);
/* 189 */     addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, debug2, Math.min(this.level.getDifficulty().getId() - 1, 0)));
/*     */     
/* 191 */     this.level.broadcastEntityEvent((Entity)this, (byte)16);
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
/*     */   private void finishConversion(ServerLevel debug1) {
/* 206 */     Villager debug2 = (Villager)convertTo(EntityType.VILLAGER, false);
/*     */     
/* 208 */     for (EquipmentSlot debug6 : EquipmentSlot.values()) {
/* 209 */       ItemStack debug7 = getItemBySlot(debug6);
/* 210 */       if (!debug7.isEmpty())
/*     */       {
/* 212 */         if (EnchantmentHelper.hasBindingCurse(debug7)) {
/* 213 */           debug2.setSlot(debug6.getIndex() + 300, debug7);
/*     */         }
/*     */         else {
/*     */           
/* 217 */           double debug8 = getEquipmentDropChance(debug6);
/* 218 */           if (debug8 > 1.0D)
/* 219 */             spawnAtLocation(debug7); 
/*     */         } 
/*     */       }
/*     */     } 
/* 223 */     debug2.setVillagerData(getVillagerData());
/* 224 */     if (this.gossips != null) {
/* 225 */       debug2.setGossips(this.gossips);
/*     */     }
/* 227 */     if (this.tradeOffers != null) {
/* 228 */       debug2.setOffers(new MerchantOffers(this.tradeOffers));
/*     */     }
/* 230 */     debug2.setVillagerXp(this.villagerXp);
/* 231 */     debug2.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug2.blockPosition()), MobSpawnType.CONVERSION, null, null);
/*     */     
/* 233 */     if (this.conversionStarter != null) {
/* 234 */       Player debug3 = debug1.getPlayerByUUID(this.conversionStarter);
/* 235 */       if (debug3 instanceof ServerPlayer) {
/* 236 */         CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((ServerPlayer)debug3, this, debug2);
/* 237 */         debug1.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, (Entity)debug3, (ReputationEventHandler)debug2);
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     debug2.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
/* 242 */     if (!isSilent()) {
/* 243 */       debug1.levelEvent(null, 1027, blockPosition(), 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private int getConversionProgress() {
/* 248 */     int debug1 = 1;
/*     */     
/* 250 */     if (this.random.nextFloat() < 0.01F) {
/* 251 */       int debug2 = 0;
/*     */       
/* 253 */       BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/*     */       
/* 255 */       for (int debug4 = (int)getX() - 4; debug4 < (int)getX() + 4 && debug2 < 14; debug4++) {
/* 256 */         for (int debug5 = (int)getY() - 4; debug5 < (int)getY() + 4 && debug2 < 14; debug5++) {
/* 257 */           for (int debug6 = (int)getZ() - 4; debug6 < (int)getZ() + 4 && debug2 < 14; debug6++) {
/* 258 */             Block debug7 = this.level.getBlockState((BlockPos)debug3.set(debug4, debug5, debug6)).getBlock();
/* 259 */             if (debug7 == Blocks.IRON_BARS || debug7 instanceof net.minecraft.world.level.block.BedBlock) {
/* 260 */               if (this.random.nextFloat() < 0.3F) {
/* 261 */                 debug1++;
/*     */               }
/* 263 */               debug2++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 269 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getVoicePitch() {
/* 274 */     if (isBaby()) {
/* 275 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F;
/*     */     }
/* 277 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/* 282 */     return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getHurtSound(DamageSource debug1) {
/* 287 */     return SoundEvents.ZOMBIE_VILLAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getDeathSound() {
/* 292 */     return SoundEvents.ZOMBIE_VILLAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getStepSound() {
/* 297 */     return SoundEvents.ZOMBIE_VILLAGER_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getSkull() {
/* 302 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public void setTradeOffers(CompoundTag debug1) {
/* 306 */     this.tradeOffers = debug1;
/*     */   }
/*     */   
/*     */   public void setGossips(Tag debug1) {
/* 310 */     this.gossips = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 316 */     setVillagerData(getVillagerData().setType(VillagerType.byBiome(debug1.getBiomeName(blockPosition()))));
/*     */     
/* 318 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVillagerData(VillagerData debug1) {
/* 323 */     VillagerData debug2 = getVillagerData();
/* 324 */     if (debug2.getProfession() != debug1.getProfession()) {
/* 325 */       this.tradeOffers = null;
/*     */     }
/*     */     
/* 328 */     this.entityData.set(DATA_VILLAGER_DATA, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VillagerData getVillagerData() {
/* 333 */     return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVillagerXp(int debug1) {
/* 341 */     this.villagerXp = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\ZombieVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */