/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public abstract class AbstractSchoolingFish extends AbstractFish {
/*  18 */   private int schoolSize = 1; private AbstractSchoolingFish leader;
/*     */   
/*     */   public AbstractSchoolingFish(EntityType<? extends AbstractSchoolingFish> debug1, Level debug2) {
/*  21 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  26 */     super.registerGoals();
/*     */     
/*  28 */     this.goalSelector.addGoal(5, (Goal)new FollowFlockLeaderGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/*  33 */     return getMaxSchoolSize();
/*     */   }
/*     */   
/*     */   public int getMaxSchoolSize() {
/*  37 */     return super.getMaxSpawnClusterSize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRandomSwim() {
/*  42 */     return !isFollower();
/*     */   }
/*     */   
/*     */   public boolean isFollower() {
/*  46 */     return (this.leader != null && this.leader.isAlive());
/*     */   }
/*     */   
/*     */   public AbstractSchoolingFish startFollowing(AbstractSchoolingFish debug1) {
/*  50 */     this.leader = debug1;
/*  51 */     debug1.addFollower();
/*     */     
/*  53 */     return debug1;
/*     */   }
/*     */   
/*     */   public void stopFollowing() {
/*  57 */     this.leader.removeFollower();
/*  58 */     this.leader = null;
/*     */   }
/*     */   
/*     */   private void addFollower() {
/*  62 */     this.schoolSize++;
/*     */   }
/*     */   
/*     */   private void removeFollower() {
/*  66 */     this.schoolSize--;
/*     */   }
/*     */   
/*     */   public boolean canBeFollowed() {
/*  70 */     return (hasFollowers() && this.schoolSize < getMaxSchoolSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  75 */     super.tick();
/*     */ 
/*     */     
/*  78 */     if (hasFollowers() && this.level.random.nextInt(200) == 1) {
/*  79 */       List<AbstractFish> debug1 = this.level.getEntitiesOfClass(getClass(), getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/*  80 */       if (debug1.size() <= 1) {
/*  81 */         this.schoolSize = 1;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasFollowers() {
/*  87 */     return (this.schoolSize > 1);
/*     */   }
/*     */   
/*     */   public boolean inRangeOfLeader() {
/*  91 */     return (distanceToSqr((Entity)this.leader) <= 121.0D);
/*     */   }
/*     */   
/*     */   public void pathToLeader() {
/*  95 */     if (isFollower()) {
/*  96 */       getNavigation().moveTo((Entity)this.leader, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addFollowers(Stream<AbstractSchoolingFish> debug1) {
/* 101 */     debug1.limit((getMaxSchoolSize() - this.schoolSize)).filter(debug1 -> (debug1 != this)).forEach(debug1 -> debug1.startFollowing(this));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 107 */     super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 109 */     if (debug4 == null) {
/* 110 */       debug4 = new SchoolSpawnGroupData(this);
/*     */     } else {
/* 112 */       startFollowing(((SchoolSpawnGroupData)debug4).leader);
/*     */     } 
/*     */     
/* 115 */     return debug4;
/*     */   }
/*     */   
/*     */   public static class SchoolSpawnGroupData implements SpawnGroupData {
/*     */     public final AbstractSchoolingFish leader;
/*     */     
/*     */     public SchoolSpawnGroupData(AbstractSchoolingFish debug1) {
/* 122 */       this.leader = debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\AbstractSchoolingFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */