/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.npc.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public class TraderLlama
/*     */   extends Llama {
/*     */   public TraderLlama(EntityType<? extends TraderLlama> debug1, Level debug2) {
/*  27 */     super((EntityType)debug1, debug2);
/*  28 */     this.despawnDelay = 47999;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int despawnDelay;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Llama makeBabyLlama() {
/*  38 */     return (Llama)EntityType.TRADER_LLAMA.create(this.level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  43 */     super.addAdditionalSaveData(debug1);
/*  44 */     debug1.putInt("DespawnDelay", this.despawnDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  49 */     super.readAdditionalSaveData(debug1);
/*  50 */     if (debug1.contains("DespawnDelay", 99)) {
/*  51 */       this.despawnDelay = debug1.getInt("DespawnDelay");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  57 */     super.registerGoals();
/*     */     
/*  59 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*     */     
/*  61 */     this.targetSelector.addGoal(1, (Goal)new TraderLlamaDefendWanderingTraderGoal(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPlayerRide(Player debug1) {
/*  70 */     Entity debug2 = getLeashHolder();
/*  71 */     if (debug2 instanceof WanderingTrader) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     super.doPlayerRide(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  80 */     super.aiStep();
/*     */     
/*  82 */     if (!this.level.isClientSide) {
/*  83 */       maybeDespawn();
/*     */     }
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/*  88 */     if (!canDespawn()) {
/*     */       return;
/*     */     }
/*     */     
/*  92 */     this.despawnDelay = isLeashedToWanderingTrader() ? (((WanderingTrader)getLeashHolder()).getDespawnDelay() - 1) : (this.despawnDelay - 1);
/*     */     
/*  94 */     if (this.despawnDelay <= 0) {
/*  95 */       dropLeash(true, false);
/*  96 */       remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canDespawn() {
/* 101 */     return (!isTamed() && 
/* 102 */       !isLeashedToSomethingOtherThanTheWanderingTrader() && 
/* 103 */       !hasOnePlayerPassenger());
/*     */   }
/*     */   
/*     */   private boolean isLeashedToWanderingTrader() {
/* 107 */     return getLeashHolder() instanceof WanderingTrader;
/*     */   }
/*     */   
/*     */   private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
/* 111 */     return (isLeashed() && !isLeashedToWanderingTrader());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 117 */     if (debug3 == MobSpawnType.EVENT) {
/* 118 */       setAge(0);
/*     */     }
/*     */     
/* 121 */     if (debug4 == null) {
/* 122 */       agableMobGroupData = new AgableMob.AgableMobGroupData(false);
/*     */     }
/*     */     
/* 125 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */   
/*     */   public class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
/*     */     private final Llama llama;
/*     */     private LivingEntity ownerLastHurtBy;
/*     */     private int timestamp;
/*     */     
/*     */     public TraderLlamaDefendWanderingTraderGoal(Llama debug2) {
/* 134 */       super((Mob)debug2, false);
/* 135 */       this.llama = debug2;
/* 136 */       setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 141 */       if (!this.llama.isLeashed()) {
/* 142 */         return false;
/*     */       }
/* 144 */       Entity debug1 = this.llama.getLeashHolder();
/* 145 */       if (!(debug1 instanceof WanderingTrader)) {
/* 146 */         return false;
/*     */       }
/*     */       
/* 149 */       WanderingTrader debug2 = (WanderingTrader)debug1;
/* 150 */       this.ownerLastHurtBy = debug2.getLastHurtByMob();
/* 151 */       int debug3 = debug2.getLastHurtByMobTimestamp();
/* 152 */       return (debug3 != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 157 */       this.mob.setTarget(this.ownerLastHurtBy);
/*     */       
/* 159 */       Entity debug1 = this.llama.getLeashHolder();
/* 160 */       if (debug1 instanceof WanderingTrader) {
/* 161 */         this.timestamp = ((WanderingTrader)debug1).getLastHurtByMobTimestamp();
/*     */       }
/*     */       
/* 164 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\TraderLlama.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */