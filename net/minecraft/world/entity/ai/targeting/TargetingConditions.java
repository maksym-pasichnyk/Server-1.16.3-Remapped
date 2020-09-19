/*     */ package net.minecraft.world.entity.ai.targeting;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ 
/*     */ public class TargetingConditions {
/*  10 */   public static final TargetingConditions DEFAULT = new TargetingConditions();
/*     */ 
/*     */   
/*  13 */   private double range = -1.0D;
/*     */   private boolean allowInvulnerable;
/*     */   private boolean allowSameTeam;
/*     */   private boolean allowUnseeable;
/*     */   private boolean allowNonAttackable;
/*     */   private boolean testInvisible = true;
/*     */   private Predicate<LivingEntity> selector;
/*     */   
/*     */   public TargetingConditions range(double debug1) {
/*  22 */     this.range = debug1;
/*  23 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions allowInvulnerable() {
/*  27 */     this.allowInvulnerable = true;
/*  28 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions allowSameTeam() {
/*  32 */     this.allowSameTeam = true;
/*  33 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions allowUnseeable() {
/*  37 */     this.allowUnseeable = true;
/*  38 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions allowNonAttackable() {
/*  42 */     this.allowNonAttackable = true;
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions ignoreInvisibilityTesting() {
/*  47 */     this.testInvisible = false;
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public TargetingConditions selector(@Nullable Predicate<LivingEntity> debug1) {
/*  52 */     this.selector = debug1;
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public boolean test(@Nullable LivingEntity debug1, LivingEntity debug2) {
/*  57 */     if (debug1 == debug2) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (debug2.isSpectator()) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (!debug2.isAlive()) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (!this.allowInvulnerable && debug2.isInvulnerable()) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (this.selector != null && !this.selector.test(debug2)) {
/*  70 */       return false;
/*     */     }
/*     */     
/*  73 */     if (debug1 != null) {
/*  74 */       if (!this.allowNonAttackable) {
/*  75 */         if (!debug1.canAttack(debug2)) {
/*  76 */           return false;
/*     */         }
/*  78 */         if (!debug1.canAttackType(debug2.getType())) {
/*  79 */           return false;
/*     */         }
/*     */       } 
/*  82 */       if (!this.allowSameTeam && debug1.isAlliedTo((Entity)debug2)) {
/*  83 */         return false;
/*     */       }
/*     */       
/*  86 */       if (this.range > 0.0D) {
/*  87 */         double debug3 = this.testInvisible ? debug2.getVisibilityPercent((Entity)debug1) : 1.0D;
/*  88 */         double debug5 = Math.max(this.range * debug3, 2.0D);
/*  89 */         double debug7 = debug1.distanceToSqr(debug2.getX(), debug2.getY(), debug2.getZ());
/*  90 */         if (debug7 > debug5 * debug5) {
/*  91 */           return false;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  96 */       if (!this.allowUnseeable && debug1 instanceof Mob && !((Mob)debug1).getSensing().canSee((Entity)debug2)) {
/*  97 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\targeting\TargetingConditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */