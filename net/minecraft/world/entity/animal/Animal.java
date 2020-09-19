/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ 
/*     */ public abstract class Animal
/*     */   extends AgableMob
/*     */ {
/*     */   private int inLove;
/*     */   private UUID loveCause;
/*     */   
/*     */   protected Animal(EntityType<? extends Animal> debug1, Level debug2) {
/*  39 */     super(debug1, debug2);
/*  40 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
/*  41 */     setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/*  46 */     if (getAge() != 0) {
/*  47 */       this.inLove = 0;
/*     */     }
/*  49 */     super.customServerAiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  54 */     super.aiStep();
/*     */     
/*  56 */     if (getAge() != 0) {
/*  57 */       this.inLove = 0;
/*     */     }
/*     */     
/*  60 */     if (this.inLove > 0) {
/*  61 */       this.inLove--;
/*  62 */       if (this.inLove % 10 == 0) {
/*  63 */         double debug1 = this.random.nextGaussian() * 0.02D;
/*  64 */         double debug3 = this.random.nextGaussian() * 0.02D;
/*  65 */         double debug5 = this.random.nextGaussian() * 0.02D;
/*  66 */         this.level.addParticle((ParticleOptions)ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), debug1, debug3, debug5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/*  73 */     if (isInvulnerableTo(debug1)) {
/*  74 */       return false;
/*     */     }
/*  76 */     this.inLove = 0;
/*  77 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/*  82 */     if (debug2.getBlockState(debug1.below()).is(Blocks.GRASS_BLOCK)) {
/*  83 */       return 10.0F;
/*     */     }
/*  85 */     return debug2.getBrightness(debug1) - 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  90 */     super.addAdditionalSaveData(debug1);
/*  91 */     debug1.putInt("InLove", this.inLove);
/*  92 */     if (this.loveCause != null) {
/*  93 */       debug1.putUUID("LoveCause", this.loveCause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/*  99 */     return 0.14D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 104 */     super.readAdditionalSaveData(debug1);
/* 105 */     this.inLove = debug1.getInt("InLove");
/* 106 */     this.loveCause = debug1.hasUUID("LoveCause") ? debug1.getUUID("LoveCause") : null;
/*     */   }
/*     */   
/*     */   public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 110 */     return (debug1.getBlockState(debug3.below()).is(Blocks.GRASS_BLOCK) && debug1
/* 111 */       .getRawBrightness(debug3, 0) > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 116 */     return 120;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperienceReward(Player debug1) {
/* 126 */     return 1 + this.level.random.nextInt(3);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 130 */     return (debug1.getItem() == Items.WHEAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 135 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 136 */     if (isFood(debug3)) {
/* 137 */       int debug4 = getAge();
/* 138 */       if (!this.level.isClientSide && debug4 == 0 && canFallInLove()) {
/* 139 */         usePlayerItem(debug1, debug3);
/* 140 */         setInLove(debug1);
/* 141 */         return InteractionResult.SUCCESS;
/* 142 */       }  if (isBaby()) {
/* 143 */         usePlayerItem(debug1, debug3);
/*     */         
/* 145 */         ageUp((int)((-debug4 / 20) * 0.1F), true);
/* 146 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/* 148 */       if (this.level.isClientSide) {
/* 149 */         return InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/* 153 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected void usePlayerItem(Player debug1, ItemStack debug2) {
/* 157 */     if (!debug1.abilities.instabuild) {
/* 158 */       debug2.shrink(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/* 163 */     return (this.inLove <= 0);
/*     */   }
/*     */   
/*     */   public void setInLove(@Nullable Player debug1) {
/* 167 */     this.inLove = 600;
/*     */     
/* 169 */     if (debug1 != null) {
/* 170 */       this.loveCause = debug1.getUUID();
/*     */     }
/*     */     
/* 173 */     this.level.broadcastEntityEvent((Entity)this, (byte)18);
/*     */   }
/*     */   
/*     */   public void setInLoveTime(int debug1) {
/* 177 */     this.inLove = debug1;
/*     */   }
/*     */   
/*     */   public int getInLoveTime() {
/* 181 */     return this.inLove;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ServerPlayer getLoveCause() {
/* 186 */     if (this.loveCause == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     Player debug1 = this.level.getPlayerByUUID(this.loveCause);
/* 190 */     if (debug1 instanceof ServerPlayer) {
/* 191 */       return (ServerPlayer)debug1;
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInLove() {
/* 198 */     return (this.inLove > 0);
/*     */   }
/*     */   
/*     */   public void resetLove() {
/* 202 */     this.inLove = 0;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 206 */     if (debug1 == this) {
/* 207 */       return false;
/*     */     }
/* 209 */     if (debug1.getClass() != getClass()) {
/* 210 */       return false;
/*     */     }
/* 212 */     return (isInLove() && debug1.isInLove());
/*     */   }
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel debug1, Animal debug2) {
/* 216 */     AgableMob debug3 = getBreedOffspring(debug1, debug2);
/* 217 */     if (debug3 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 221 */     ServerPlayer debug4 = getLoveCause();
/* 222 */     if (debug4 == null && debug2.getLoveCause() != null) {
/* 223 */       debug4 = debug2.getLoveCause();
/*     */     }
/*     */     
/* 226 */     if (debug4 != null) {
/* 227 */       debug4.awardStat(Stats.ANIMALS_BRED);
/*     */       
/* 229 */       CriteriaTriggers.BRED_ANIMALS.trigger(debug4, this, debug2, debug3);
/*     */     } 
/*     */     
/* 232 */     setAge(6000);
/* 233 */     debug2.setAge(6000);
/* 234 */     resetLove();
/* 235 */     debug2.resetLove();
/* 236 */     debug3.setBaby(true);
/* 237 */     debug3.moveTo(getX(), getY(), getZ(), 0.0F, 0.0F);
/* 238 */     debug1.addFreshEntityWithPassengers((Entity)debug3);
/*     */     
/* 240 */     debug1.broadcastEntityEvent((Entity)this, (byte)18);
/*     */     
/* 242 */     if (debug1.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
/* 243 */       debug1.addFreshEntity((Entity)new ExperienceOrb((Level)debug1, getX(), getY(), getZ(), getRandom().nextInt(7) + 1)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Animal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */