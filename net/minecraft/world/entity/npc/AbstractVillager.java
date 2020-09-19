/*     */ package net.minecraft.world.entity.npc;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractVillager
/*     */   extends AgableMob
/*     */   implements Npc, Merchant
/*     */ {
/*  41 */   private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Player tradingPlayer;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MerchantOffers offers;
/*     */   
/*  51 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   public AbstractVillager(EntityType<? extends AbstractVillager> debug1, Level debug2) {
/*  54 */     super(debug1, debug2);
/*  55 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
/*  56 */     setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/*  62 */     if (debug4 == null) {
/*  63 */       agableMobGroupData = new AgableMob.AgableMobGroupData(false);
/*     */     }
/*     */     
/*  66 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */   
/*     */   public int getUnhappyCounter() {
/*  70 */     return ((Integer)this.entityData.get(DATA_UNHAPPY_COUNTER)).intValue();
/*     */   }
/*     */   
/*     */   public void setUnhappyCounter(int debug1) {
/*  74 */     this.entityData.set(DATA_UNHAPPY_COUNTER, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVillagerXp() {
/*  79 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  84 */     if (isBaby()) {
/*  85 */       return 0.81F;
/*     */     }
/*  87 */     return 1.62F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  92 */     super.defineSynchedData();
/*  93 */     this.entityData.define(DATA_UNHAPPY_COUNTER, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTradingPlayer(@Nullable Player debug1) {
/*  98 */     this.tradingPlayer = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Player getTradingPlayer() {
/* 104 */     return this.tradingPlayer;
/*     */   }
/*     */   
/*     */   public boolean isTrading() {
/* 108 */     return (this.tradingPlayer != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantOffers getOffers() {
/* 113 */     if (this.offers == null) {
/* 114 */       this.offers = new MerchantOffers();
/* 115 */       updateTrades();
/*     */     } 
/* 117 */     return this.offers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideXp(int debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyTrade(MerchantOffer debug1) {
/* 130 */     debug1.increaseUses();
/* 131 */     this.ambientSoundTime = -getAmbientSoundInterval();
/*     */     
/* 133 */     rewardTradeXp(debug1);
/*     */     
/* 135 */     if (this.tradingPlayer instanceof ServerPlayer) {
/* 136 */       CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, debug1.getResult());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void rewardTradeXp(MerchantOffer paramMerchantOffer);
/*     */   
/*     */   public boolean showProgressBar() {
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyTradeUpdated(ItemStack debug1) {
/* 149 */     if (!this.level.isClientSide && this.ambientSoundTime > -getAmbientSoundInterval() + 20) {
/* 150 */       this.ambientSoundTime = -getAmbientSoundInterval();
/* 151 */       playSound(getTradeUpdatedSound(!debug1.isEmpty()), getSoundVolume(), getVoicePitch());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getNotifyTradeSound() {
/* 157 */     return SoundEvents.VILLAGER_YES;
/*     */   }
/*     */   
/*     */   protected SoundEvent getTradeUpdatedSound(boolean debug1) {
/* 161 */     return debug1 ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
/*     */   }
/*     */   
/*     */   public void playCelebrateSound() {
/* 165 */     playSound(SoundEvents.VILLAGER_CELEBRATE, getSoundVolume(), getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 170 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 172 */     MerchantOffers debug2 = getOffers();
/* 173 */     if (!debug2.isEmpty()) {
/* 174 */       debug1.put("Offers", (Tag)debug2.createTag());
/*     */     }
/* 176 */     debug1.put("Inventory", (Tag)this.inventory.createTag());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 182 */     super.readAdditionalSaveData(debug1);
/*     */ 
/*     */     
/* 185 */     if (debug1.contains("Offers", 10)) {
/* 186 */       this.offers = new MerchantOffers(debug1.getCompound("Offers"));
/*     */     }
/* 188 */     this.inventory.fromTag(debug1.getList("Inventory", 10));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity changeDimension(ServerLevel debug1) {
/* 194 */     stopTrading();
/* 195 */     return super.changeDimension(debug1);
/*     */   }
/*     */   
/*     */   protected void stopTrading() {
/* 199 */     setTradingPlayer((Player)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource debug1) {
/* 204 */     super.die(debug1);
/* 205 */     stopTrading();
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
/*     */   public boolean canBeLeashed(Player debug1) {
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 224 */     return this.inventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 229 */     if (super.setSlot(debug1, debug2)) {
/* 230 */       return true;
/*     */     }
/* 232 */     int debug3 = debug1 - 300;
/* 233 */     if (debug3 >= 0 && debug3 < this.inventory.getContainerSize()) {
/* 234 */       this.inventory.setItem(debug3, debug2);
/* 235 */       return true;
/*     */     } 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 242 */     return this.level;
/*     */   }
/*     */   
/*     */   protected abstract void updateTrades();
/*     */   
/*     */   protected void addOffersFromItemListings(MerchantOffers debug1, VillagerTrades.ItemListing[] debug2, int debug3) {
/* 248 */     Set<Integer> debug4 = Sets.newHashSet();
/* 249 */     if (debug2.length > debug3) {
/* 250 */       while (debug4.size() < debug3) {
/* 251 */         debug4.add(Integer.valueOf(this.random.nextInt(debug2.length)));
/*     */       }
/*     */     } else {
/* 254 */       for (int debug5 = 0; debug5 < debug2.length; debug5++) {
/* 255 */         debug4.add(Integer.valueOf(debug5));
/*     */       }
/*     */     } 
/*     */     
/* 259 */     for (Integer debug6 : debug4) {
/* 260 */       VillagerTrades.ItemListing debug7 = debug2[debug6.intValue()];
/* 261 */       MerchantOffer debug8 = debug7.getOffer((Entity)this, this.random);
/* 262 */       if (debug8 != null)
/* 263 */         debug1.add(debug8); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\AbstractVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */