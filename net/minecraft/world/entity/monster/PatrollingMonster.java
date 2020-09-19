/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class PatrollingMonster extends Monster {
/*     */   private BlockPos patrolTarget;
/*     */   
/*     */   protected PatrollingMonster(EntityType<? extends PatrollingMonster> debug1, Level debug2) {
/*  33 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   private boolean patrolLeader; private boolean patrolling;
/*     */   
/*     */   protected void registerGoals() {
/*  38 */     super.registerGoals();
/*  39 */     this.goalSelector.addGoal(4, new LongDistancePatrolGoal<>(this, 0.7D, 0.595D));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  44 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  46 */     if (this.patrolTarget != null) {
/*  47 */       debug1.put("PatrolTarget", (Tag)NbtUtils.writeBlockPos(this.patrolTarget));
/*     */     }
/*     */     
/*  50 */     debug1.putBoolean("PatrolLeader", this.patrolLeader);
/*  51 */     debug1.putBoolean("Patrolling", this.patrolling);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  56 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  58 */     if (debug1.contains("PatrolTarget")) {
/*  59 */       this.patrolTarget = NbtUtils.readBlockPos(debug1.getCompound("PatrolTarget"));
/*     */     }
/*     */     
/*  62 */     this.patrolLeader = debug1.getBoolean("PatrolLeader");
/*  63 */     this.patrolling = debug1.getBoolean("Patrolling");
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/*  68 */     return -0.45D;
/*     */   }
/*     */   
/*     */   public boolean canBeLeader() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  80 */     if (debug3 != MobSpawnType.PATROL && debug3 != MobSpawnType.EVENT && debug3 != MobSpawnType.STRUCTURE && 
/*  81 */       this.random.nextFloat() < 0.06F && canBeLeader()) {
/*  82 */       this.patrolLeader = true;
/*     */     }
/*     */ 
/*     */     
/*  86 */     if (isPatrolLeader()) {
/*  87 */       setItemSlot(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
/*  88 */       setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */     } 
/*     */     
/*  91 */     if (debug3 == MobSpawnType.PATROL) {
/*  92 */       this.patrolling = true;
/*     */     }
/*     */     
/*  95 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public static boolean checkPatrollingMonsterSpawnRules(EntityType<? extends PatrollingMonster> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/*  99 */     if (debug1.getBrightness(LightLayer.BLOCK, debug3) > 8) {
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     return checkAnyLightMonsterSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 108 */     return (!this.patrolling || debug1 > 16384.0D);
/*     */   }
/*     */   
/*     */   public void setPatrolTarget(BlockPos debug1) {
/* 112 */     this.patrolTarget = debug1;
/* 113 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   public BlockPos getPatrolTarget() {
/* 117 */     return this.patrolTarget;
/*     */   }
/*     */   
/*     */   public boolean hasPatrolTarget() {
/* 121 */     return (this.patrolTarget != null);
/*     */   }
/*     */   
/*     */   public void setPatrolLeader(boolean debug1) {
/* 125 */     this.patrolLeader = debug1;
/* 126 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   public boolean isPatrolLeader() {
/* 130 */     return this.patrolLeader;
/*     */   }
/*     */   
/*     */   public boolean canJoinPatrol() {
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   public void findPatrolTarget() {
/* 138 */     this.patrolTarget = blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
/* 139 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   protected boolean isPatrolling() {
/* 143 */     return this.patrolling;
/*     */   }
/*     */   
/*     */   protected void setPatrolling(boolean debug1) {
/* 147 */     this.patrolling = debug1;
/*     */   }
/*     */   
/*     */   public static class LongDistancePatrolGoal<T extends PatrollingMonster>
/*     */     extends Goal
/*     */   {
/*     */     private final T mob;
/*     */     private final double speedModifier;
/*     */     private final double leaderSpeedModifier;
/*     */     private long cooldownUntil;
/*     */     
/*     */     public LongDistancePatrolGoal(T debug1, double debug2, double debug4) {
/* 159 */       this.mob = debug1;
/* 160 */       this.speedModifier = debug2;
/* 161 */       this.leaderSpeedModifier = debug4;
/* 162 */       this.cooldownUntil = -1L;
/* 163 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 168 */       boolean debug1 = (((PatrollingMonster)this.mob).level.getGameTime() < this.cooldownUntil);
/* 169 */       return (this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.isVehicle() && this.mob.hasPatrolTarget() && !debug1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {}
/*     */ 
/*     */     
/*     */     public void tick() {
/* 182 */       boolean debug1 = this.mob.isPatrolLeader();
/* 183 */       PathNavigation debug2 = this.mob.getNavigation();
/* 184 */       if (debug2.isDone()) {
/* 185 */         List<PatrollingMonster> debug3 = findPatrolCompanions();
/* 186 */         if (this.mob.isPatrolling() && debug3.isEmpty()) {
/* 187 */           this.mob.setPatrolling(false);
/* 188 */         } else if (!debug1 || !this.mob.getPatrolTarget().closerThan((Position)this.mob.position(), 10.0D)) {
/* 189 */           Vec3 debug4 = Vec3.atBottomCenterOf((Vec3i)this.mob.getPatrolTarget());
/*     */ 
/*     */           
/* 192 */           Vec3 debug5 = this.mob.position();
/* 193 */           Vec3 debug6 = debug5.subtract(debug4);
/*     */           
/* 195 */           debug4 = debug6.yRot(90.0F).scale(0.4D).add(debug4);
/*     */           
/* 197 */           Vec3 debug7 = debug4.subtract(debug5).normalize().scale(10.0D).add(debug5);
/* 198 */           BlockPos debug8 = new BlockPos(debug7);
/* 199 */           debug8 = ((PatrollingMonster)this.mob).level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, debug8);
/*     */           
/* 201 */           if (!debug2.moveTo(debug8.getX(), debug8.getY(), debug8.getZ(), debug1 ? this.leaderSpeedModifier : this.speedModifier)) {
/*     */             
/* 203 */             moveRandomly();
/* 204 */             this.cooldownUntil = ((PatrollingMonster)this.mob).level.getGameTime() + 200L;
/* 205 */           } else if (debug1) {
/* 206 */             for (PatrollingMonster debug10 : debug3) {
/* 207 */               debug10.setPatrolTarget(debug8);
/*     */             }
/*     */           } 
/*     */         } else {
/* 211 */           this.mob.findPatrolTarget();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private List<PatrollingMonster> findPatrolCompanions() {
/* 217 */       return ((PatrollingMonster)this.mob).level.getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0D), debug1 -> (debug1.canJoinPatrol() && !debug1.is((Entity)this.mob)));
/*     */     }
/*     */     
/*     */     private boolean moveRandomly() {
/* 221 */       Random debug1 = this.mob.getRandom();
/* 222 */       BlockPos debug2 = ((PatrollingMonster)this.mob).level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + debug1.nextInt(16), 0, -8 + debug1.nextInt(16)));
/* 223 */       return this.mob.getNavigation().moveTo(debug2.getX(), debug2.getY(), debug2.getZ(), this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\PatrollingMonster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */