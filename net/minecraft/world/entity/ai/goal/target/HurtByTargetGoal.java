/*     */ package net.minecraft.world.entity.ai.goal.target;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class HurtByTargetGoal
/*     */   extends TargetGoal {
/*  18 */   private static final TargetingConditions HURT_BY_TARGETING = (new TargetingConditions()).allowUnseeable().ignoreInvisibilityTesting();
/*     */   
/*     */   private boolean alertSameType;
/*     */   
/*     */   private int timestamp;
/*     */   
/*     */   private final Class<?>[] toIgnoreDamage;
/*     */   
/*     */   private Class<?>[] toIgnoreAlert;
/*     */   
/*     */   public HurtByTargetGoal(PathfinderMob debug1, Class<?>... debug2) {
/*  29 */     super((Mob)debug1, true);
/*  30 */     this.toIgnoreDamage = debug2;
/*  31 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  36 */     int debug1 = this.mob.getLastHurtByMobTimestamp();
/*  37 */     LivingEntity debug2 = this.mob.getLastHurtByMob();
/*     */     
/*  39 */     if (debug1 == this.timestamp || debug2 == null) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     if (debug2.getType() == EntityType.PLAYER && this.mob.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER))
/*     */     {
/*  45 */       return false;
/*     */     }
/*     */     
/*  48 */     for (Class<?> debug6 : this.toIgnoreDamage) {
/*  49 */       if (debug6.isAssignableFrom(debug2.getClass())) {
/*  50 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  54 */     return canAttack(debug2, HURT_BY_TARGETING);
/*     */   }
/*     */   
/*     */   public HurtByTargetGoal setAlertOthers(Class<?>... debug1) {
/*  58 */     this.alertSameType = true;
/*  59 */     this.toIgnoreAlert = debug1;
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  65 */     this.mob.setTarget(this.mob.getLastHurtByMob());
/*  66 */     this.targetMob = this.mob.getTarget();
/*  67 */     this.timestamp = this.mob.getLastHurtByMobTimestamp();
/*  68 */     this.unseenMemoryTicks = 300;
/*     */     
/*  70 */     if (this.alertSameType) {
/*  71 */       alertOthers();
/*     */     }
/*     */     
/*  74 */     super.start();
/*     */   }
/*     */   
/*     */   protected void alertOthers() {
/*  78 */     double debug1 = getFollowDistance();
/*     */     
/*  80 */     AABB debug3 = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(debug1, 10.0D, debug1);
/*  81 */     List<Mob> debug4 = this.mob.level.getLoadedEntitiesOfClass(this.mob.getClass(), debug3);
/*  82 */     for (Mob debug6 : debug4) {
/*  83 */       if (this.mob == debug6) {
/*     */         continue;
/*     */       }
/*  86 */       if (debug6.getTarget() != null) {
/*     */         continue;
/*     */       }
/*  89 */       if (this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)debug6).getOwner()) {
/*     */         continue;
/*     */       }
/*  92 */       if (debug6.isAlliedTo((Entity)this.mob.getLastHurtByMob())) {
/*     */         continue;
/*     */       }
/*  95 */       if (this.toIgnoreAlert != null) {
/*  96 */         boolean debug7 = false;
/*  97 */         for (Class<?> debug11 : this.toIgnoreAlert) {
/*  98 */           if (debug6.getClass() == debug11) {
/*  99 */             debug7 = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 103 */         if (debug7) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/* 108 */       alertOther(debug6, this.mob.getLastHurtByMob());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void alertOther(Mob debug1, LivingEntity debug2) {
/* 113 */     debug1.setTarget(debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\HurtByTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */