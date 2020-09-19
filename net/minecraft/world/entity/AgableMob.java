/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public abstract class AgableMob
/*     */   extends PathfinderMob {
/*  17 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgableMob.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */   
/*     */   protected int age;
/*     */   
/*     */   protected int forcedAge;
/*     */   
/*     */   protected int forcedAgeTimer;
/*     */ 
/*     */   
/*     */   protected AgableMob(EntityType<? extends AgableMob> debug1, Level debug2) {
/*  28 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  33 */     if (debug4 == null) {
/*  34 */       debug4 = new AgableMobGroupData(true);
/*     */     }
/*     */     
/*  37 */     AgableMobGroupData debug6 = (AgableMobGroupData)debug4;
/*     */     
/*  39 */     if (debug6.isShouldSpawnBaby() && debug6.getGroupSize() > 0 && this.random.nextFloat() <= debug6.getBabySpawnChance()) {
/*  40 */       setAge(-24000);
/*     */     }
/*     */     
/*  43 */     debug6.increaseGroupSizeByOne();
/*     */     
/*  45 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract AgableMob getBreedOffspring(ServerLevel paramServerLevel, AgableMob paramAgableMob);
/*     */   
/*     */   protected void defineSynchedData() {
/*  53 */     super.defineSynchedData();
/*  54 */     this.entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canBreed() {
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public int getAge() {
/*  62 */     if (this.level.isClientSide) {
/*  63 */       return ((Boolean)this.entityData.get(DATA_BABY_ID)).booleanValue() ? -1 : 1;
/*     */     }
/*  65 */     return this.age;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ageUp(int debug1, boolean debug2) {
/*  70 */     int debug3 = getAge();
/*  71 */     int debug4 = debug3;
/*  72 */     debug3 += debug1 * 20;
/*  73 */     if (debug3 > 0) {
/*  74 */       debug3 = 0;
/*     */     }
/*  76 */     int debug5 = debug3 - debug4;
/*  77 */     setAge(debug3);
/*  78 */     if (debug2) {
/*  79 */       this.forcedAge += debug5;
/*  80 */       if (this.forcedAgeTimer == 0) {
/*  81 */         this.forcedAgeTimer = 40;
/*     */       }
/*     */     } 
/*  84 */     if (getAge() == 0) {
/*  85 */       setAge(this.forcedAge);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ageUp(int debug1) {
/*  90 */     ageUp(debug1, false);
/*     */   }
/*     */   
/*     */   public void setAge(int debug1) {
/*  94 */     int debug2 = this.age;
/*  95 */     this.age = debug1;
/*     */     
/*  97 */     if ((debug2 < 0 && debug1 >= 0) || (debug2 >= 0 && debug1 < 0)) {
/*  98 */       this.entityData.set(DATA_BABY_ID, Boolean.valueOf((debug1 < 0)));
/*  99 */       ageBoundaryReached();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 105 */     super.addAdditionalSaveData(debug1);
/* 106 */     debug1.putInt("Age", getAge());
/* 107 */     debug1.putInt("ForcedAge", this.forcedAge);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 112 */     super.readAdditionalSaveData(debug1);
/* 113 */     setAge(debug1.getInt("Age"));
/* 114 */     this.forcedAge = debug1.getInt("ForcedAge");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 119 */     if (DATA_BABY_ID.equals(debug1)) {
/* 120 */       refreshDimensions();
/*     */     }
/* 122 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 127 */     super.aiStep();
/*     */     
/* 129 */     if (this.level.isClientSide) {
/* 130 */       if (this.forcedAgeTimer > 0) {
/* 131 */         if (this.forcedAgeTimer % 4 == 0) {
/* 132 */           this.level.addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */         }
/* 134 */         this.forcedAgeTimer--;
/*     */       } 
/* 136 */     } else if (isAlive()) {
/* 137 */       int debug1 = getAge();
/* 138 */       if (debug1 < 0) {
/* 139 */         debug1++;
/* 140 */         setAge(debug1);
/* 141 */       } else if (debug1 > 0) {
/* 142 */         debug1--;
/* 143 */         setAge(debug1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {}
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 153 */     return (getAge() < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean debug1) {
/* 158 */     setAge(debug1 ? -24000 : 0);
/*     */   }
/*     */   
/*     */   public static class AgableMobGroupData implements SpawnGroupData {
/*     */     private int groupSize;
/*     */     private final boolean shouldSpawnBaby;
/*     */     private final float babySpawnChance;
/*     */     
/*     */     private AgableMobGroupData(boolean debug1, float debug2) {
/* 167 */       this.shouldSpawnBaby = debug1;
/* 168 */       this.babySpawnChance = debug2;
/*     */     }
/*     */     
/*     */     public AgableMobGroupData(boolean debug1) {
/* 172 */       this(debug1, 0.05F);
/*     */     }
/*     */     
/*     */     public AgableMobGroupData(float debug1) {
/* 176 */       this(true, debug1);
/*     */     }
/*     */     
/*     */     public int getGroupSize() {
/* 180 */       return this.groupSize;
/*     */     }
/*     */     
/*     */     public void increaseGroupSizeByOne() {
/* 184 */       this.groupSize++;
/*     */     }
/*     */     
/*     */     public boolean isShouldSpawnBaby() {
/* 188 */       return this.shouldSpawnBaby;
/*     */     }
/*     */     
/*     */     public float getBabySpawnChance() {
/* 192 */       return this.babySpawnChance;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\AgableMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */