/*     */ package net.minecraft.world.entity.npc;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.InteractGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.UseItemGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.monster.Evoker;
/*     */ import net.minecraft.world.entity.monster.Illusioner;
/*     */ import net.minecraft.world.entity.monster.Pillager;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Vindicator;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WanderingTrader extends AbstractVillager {
/*     */   @Nullable
/*     */   private BlockPos wanderTarget;
/*     */   
/*     */   public WanderingTrader(EntityType<? extends WanderingTrader> debug1, Level debug2) {
/*  58 */     super((EntityType)debug1, debug2);
/*  59 */     this.forcedLoading = true;
/*     */   }
/*     */   private int despawnDelay;
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  65 */     this.goalSelector.addGoal(0, (Goal)new UseItemGoal((Mob)this, PotionUtils.setPotion(new ItemStack((ItemLike)Items.POTION), Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, debug1 -> (this.level.isNight() && !debug1.isInvisible())));
/*  66 */     this.goalSelector.addGoal(0, (Goal)new UseItemGoal((Mob)this, new ItemStack((ItemLike)Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, debug1 -> (this.level.isDay() && debug1.isInvisible())));
/*  67 */     this.goalSelector.addGoal(1, (Goal)new TradeWithPlayerGoal(this));
/*  68 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Zombie.class, 8.0F, 0.5D, 0.5D));
/*  69 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Evoker.class, 12.0F, 0.5D, 0.5D));
/*  70 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Vindicator.class, 8.0F, 0.5D, 0.5D));
/*  71 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Vex.class, 8.0F, 0.5D, 0.5D));
/*  72 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Pillager.class, 15.0F, 0.5D, 0.5D));
/*  73 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Illusioner.class, 12.0F, 0.5D, 0.5D));
/*  74 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Zoglin.class, 10.0F, 0.5D, 0.5D));
/*  75 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 0.5D));
/*  76 */     this.goalSelector.addGoal(1, (Goal)new LookAtTradingPlayerGoal(this));
/*  77 */     this.goalSelector.addGoal(2, new WanderToPositionGoal(this, 2.0D, 0.35D));
/*  78 */     this.goalSelector.addGoal(4, (Goal)new MoveTowardsRestrictionGoal((PathfinderMob)this, 0.35D));
/*  79 */     this.goalSelector.addGoal(8, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.35D));
/*  80 */     this.goalSelector.addGoal(9, (Goal)new InteractGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  81 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showProgressBar() {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  98 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*  99 */     if (debug3.getItem() != Items.VILLAGER_SPAWN_EGG && isAlive() && !isTrading() && !isBaby()) {
/*     */       
/* 101 */       if (debug2 == InteractionHand.MAIN_HAND) {
/* 102 */         debug1.awardStat(Stats.TALKED_TO_VILLAGER);
/*     */       }
/*     */       
/* 105 */       if (getOffers().isEmpty()) {
/* 106 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       }
/*     */       
/* 109 */       if (!this.level.isClientSide) {
/*     */         
/* 111 */         setTradingPlayer(debug1);
/* 112 */         openTradingScreen(debug1, getDisplayName(), 1);
/*     */       } 
/* 114 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/* 116 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateTrades() {
/* 121 */     VillagerTrades.ItemListing[] debug1 = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(1);
/* 122 */     VillagerTrades.ItemListing[] debug2 = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(2);
/*     */     
/* 124 */     if (debug1 == null || debug2 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     MerchantOffers debug3 = getOffers();
/* 129 */     addOffersFromItemListings(debug3, debug1, 5);
/*     */     
/* 131 */     int debug4 = this.random.nextInt(debug2.length);
/* 132 */     VillagerTrades.ItemListing debug5 = debug2[debug4];
/* 133 */     MerchantOffer debug6 = debug5.getOffer((Entity)this, this.random);
/* 134 */     if (debug6 != null) {
/* 135 */       debug3.add(debug6);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 141 */     super.addAdditionalSaveData(debug1);
/* 142 */     debug1.putInt("DespawnDelay", this.despawnDelay);
/*     */     
/* 144 */     if (this.wanderTarget != null) {
/* 145 */       debug1.put("WanderTarget", (Tag)NbtUtils.writeBlockPos(this.wanderTarget));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.readAdditionalSaveData(debug1);
/* 152 */     if (debug1.contains("DespawnDelay", 99)) {
/* 153 */       this.despawnDelay = debug1.getInt("DespawnDelay");
/*     */     }
/* 155 */     if (debug1.contains("WanderTarget")) {
/* 156 */       this.wanderTarget = NbtUtils.readBlockPos(debug1.getCompound("WanderTarget"));
/*     */     }
/*     */     
/* 159 */     setAge(Math.max(0, getAge()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rewardTradeXp(MerchantOffer debug1) {
/* 169 */     if (debug1.shouldRewardExp()) {
/* 170 */       int debug2 = 3 + this.random.nextInt(4);
/* 171 */       this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, getX(), getY() + 0.5D, getZ(), debug2));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 177 */     if (isTrading()) {
/* 178 */       return SoundEvents.WANDERING_TRADER_TRADE;
/*     */     }
/* 180 */     return SoundEvents.WANDERING_TRADER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 185 */     return SoundEvents.WANDERING_TRADER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 190 */     return SoundEvents.WANDERING_TRADER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDrinkingSound(ItemStack debug1) {
/* 195 */     Item debug2 = debug1.getItem();
/* 196 */     if (debug2 == Items.MILK_BUCKET) {
/* 197 */       return SoundEvents.WANDERING_TRADER_DRINK_MILK;
/*     */     }
/* 199 */     return SoundEvents.WANDERING_TRADER_DRINK_POTION;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getTradeUpdatedSound(boolean debug1) {
/* 205 */     return debug1 ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getNotifyTradeSound() {
/* 210 */     return SoundEvents.WANDERING_TRADER_YES;
/*     */   }
/*     */   
/*     */   public void setDespawnDelay(int debug1) {
/* 214 */     this.despawnDelay = debug1;
/*     */   }
/*     */   
/*     */   public int getDespawnDelay() {
/* 218 */     return this.despawnDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 223 */     super.aiStep();
/*     */     
/* 225 */     if (!this.level.isClientSide) {
/* 226 */       maybeDespawn();
/*     */     }
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/* 231 */     if (this.despawnDelay > 0 && !isTrading() && --this.despawnDelay == 0) {
/* 232 */       remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setWanderTarget(@Nullable BlockPos debug1) {
/* 237 */     this.wanderTarget = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockPos getWanderTarget() {
/* 242 */     return this.wanderTarget;
/*     */   }
/*     */   
/*     */   class WanderToPositionGoal extends Goal {
/*     */     final WanderingTrader trader;
/*     */     final double stopDistance;
/*     */     final double speedModifier;
/*     */     
/*     */     WanderToPositionGoal(WanderingTrader debug2, double debug3, double debug5) {
/* 251 */       this.trader = debug2;
/* 252 */       this.stopDistance = debug3;
/* 253 */       this.speedModifier = debug5;
/* 254 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 259 */       this.trader.setWanderTarget((BlockPos)null);
/* 260 */       WanderingTrader.this.navigation.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 265 */       BlockPos debug1 = this.trader.getWanderTarget();
/* 266 */       return (debug1 != null && isTooFarAway(debug1, this.stopDistance));
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 271 */       BlockPos debug1 = this.trader.getWanderTarget();
/* 272 */       if (debug1 != null && WanderingTrader.this.navigation.isDone()) {
/* 273 */         if (isTooFarAway(debug1, 10.0D)) {
/*     */           
/* 275 */           Vec3 debug2 = (new Vec3(debug1.getX() - this.trader.getX(), debug1.getY() - this.trader.getY(), debug1.getZ() - this.trader.getZ())).normalize();
/* 276 */           Vec3 debug3 = debug2.scale(10.0D).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
/* 277 */           WanderingTrader.this.navigation.moveTo(debug3.x, debug3.y, debug3.z, this.speedModifier);
/*     */         } else {
/* 279 */           WanderingTrader.this.navigation.moveTo(debug1.getX(), debug1.getY(), debug1.getZ(), this.speedModifier);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isTooFarAway(BlockPos debug1, double debug2) {
/* 285 */       return !debug1.closerThan((Position)this.trader.position(), debug2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\WanderingTrader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */