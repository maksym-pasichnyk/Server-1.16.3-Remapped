/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Silverfish
/*     */   extends Monster {
/*     */   private SilverfishWakeUpFriendsGoal friendsGoal;
/*     */   
/*     */   public Silverfish(EntityType<? extends Silverfish> debug1, Level debug2) {
/*  39 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  44 */     this.friendsGoal = new SilverfishWakeUpFriendsGoal(this);
/*     */     
/*  46 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*     */     
/*  48 */     this.goalSelector.addGoal(3, this.friendsGoal);
/*     */     
/*  50 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  51 */     this.goalSelector.addGoal(5, (Goal)new SilverfishMergeWithStoneGoal(this));
/*     */     
/*  53 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  54 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/*  59 */     return 0.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  64 */     return 0.13F;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  68 */     return Monster.createMonsterAttributes()
/*  69 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  70 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  71 */       .add(Attributes.ATTACK_DAMAGE, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  81 */     return SoundEvents.SILVERFISH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  86 */     return SoundEvents.SILVERFISH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  91 */     return SoundEvents.SILVERFISH_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  96 */     playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 101 */     if (isInvulnerableTo(debug1)) {
/* 102 */       return false;
/*     */     }
/* 104 */     if ((debug1 instanceof net.minecraft.world.damagesource.EntityDamageSource || debug1 == DamageSource.MAGIC) && this.friendsGoal != null) {
/* 105 */       this.friendsGoal.notifyHurt();
/*     */     }
/* 107 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 113 */     this.yBodyRot = this.yRot;
/*     */     
/* 115 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float debug1) {
/* 120 */     this.yRot = debug1;
/* 121 */     super.setYBodyRot(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 127 */     if (InfestedBlock.isCompatibleHostBlock(debug2.getBlockState(debug1.below()))) {
/* 128 */       return 10.0F;
/*     */     }
/* 130 */     return super.getWalkTargetValue(debug1, debug2);
/*     */   }
/*     */   
/*     */   public static boolean checkSliverfishSpawnRules(EntityType<Silverfish> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 134 */     if (checkAnyLightMonsterSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4)) {
/* 135 */       Player debug5 = debug1.getNearestPlayer(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, 5.0D, true);
/* 136 */       return (debug5 == null);
/*     */     } 
/*     */     
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 144 */     return MobType.ARTHROPOD;
/*     */   }
/*     */   
/*     */   static class SilverfishWakeUpFriendsGoal extends Goal {
/*     */     private final Silverfish silverfish;
/*     */     private int lookForFriends;
/*     */     
/*     */     public SilverfishWakeUpFriendsGoal(Silverfish debug1) {
/* 152 */       this.silverfish = debug1;
/*     */     }
/*     */     
/*     */     public void notifyHurt() {
/* 156 */       if (this.lookForFriends == 0) {
/* 157 */         this.lookForFriends = 20;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 163 */       return (this.lookForFriends > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 168 */       this.lookForFriends--;
/* 169 */       if (this.lookForFriends <= 0) {
/* 170 */         Level debug1 = this.silverfish.level;
/* 171 */         Random debug2 = this.silverfish.getRandom();
/*     */ 
/*     */         
/* 174 */         BlockPos debug3 = this.silverfish.blockPosition();
/*     */         
/*     */         int debug4;
/* 177 */         for (debug4 = 0; debug4 <= 5 && debug4 >= -5; debug4 = ((debug4 <= 0) ? 1 : 0) - debug4) {
/* 178 */           int debug5; for (debug5 = 0; debug5 <= 10 && debug5 >= -10; debug5 = ((debug5 <= 0) ? 1 : 0) - debug5) {
/* 179 */             int debug6; for (debug6 = 0; debug6 <= 10 && debug6 >= -10; debug6 = ((debug6 <= 0) ? 1 : 0) - debug6) {
/* 180 */               BlockPos debug7 = debug3.offset(debug5, debug4, debug6);
/* 181 */               BlockState debug8 = debug1.getBlockState(debug7);
/*     */               
/* 183 */               Block debug9 = debug8.getBlock();
/* 184 */               if (debug9 instanceof InfestedBlock) {
/* 185 */                 if (debug1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 186 */                   debug1.destroyBlock(debug7, true, (Entity)this.silverfish);
/*     */                 } else {
/* 188 */                   debug1.setBlock(debug7, ((InfestedBlock)debug9).getHostBlock().defaultBlockState(), 3);
/*     */                 } 
/* 190 */                 if (debug2.nextBoolean())
/*     */                   // Byte code: goto -> 237 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class SilverfishMergeWithStoneGoal
/*     */     extends RandomStrollGoal {
/*     */     private Direction selectedDirection;
/*     */     private boolean doMerge;
/*     */     
/*     */     public SilverfishMergeWithStoneGoal(Silverfish debug1) {
/* 206 */       super(debug1, 1.0D, 10);
/*     */       
/* 208 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 213 */       if (this.mob.getTarget() != null) {
/* 214 */         return false;
/*     */       }
/* 216 */       if (!this.mob.getNavigation().isDone()) {
/* 217 */         return false;
/*     */       }
/*     */       
/* 220 */       Random debug1 = this.mob.getRandom();
/* 221 */       if (this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && debug1.nextInt(10) == 0) {
/* 222 */         this.selectedDirection = Direction.getRandom(debug1);
/*     */         
/* 224 */         BlockPos debug2 = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.selectedDirection);
/* 225 */         BlockState debug3 = this.mob.level.getBlockState(debug2);
/* 226 */         if (InfestedBlock.isCompatibleHostBlock(debug3)) {
/* 227 */           this.doMerge = true;
/* 228 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 232 */       this.doMerge = false;
/* 233 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 238 */       if (this.doMerge) {
/* 239 */         return false;
/*     */       }
/* 241 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 246 */       if (!this.doMerge) {
/* 247 */         super.start();
/*     */         
/*     */         return;
/*     */       } 
/* 251 */       Level level = this.mob.level;
/* 252 */       BlockPos debug2 = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.selectedDirection);
/* 253 */       BlockState debug3 = level.getBlockState(debug2);
/*     */       
/* 255 */       if (InfestedBlock.isCompatibleHostBlock(debug3)) {
/* 256 */         level.setBlock(debug2, InfestedBlock.stateByHostBlock(debug3.getBlock()), 3);
/* 257 */         this.mob.spawnAnim();
/* 258 */         this.mob.remove();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Silverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */