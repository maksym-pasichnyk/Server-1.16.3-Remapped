/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ 
/*     */ public class TemptGoal
/*     */   extends Goal {
/*  14 */   private static final TargetingConditions TEMP_TARGETING = (new TargetingConditions()).range(10.0D).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
/*     */   
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*     */   private double px;
/*     */   private double py;
/*     */   private double pz;
/*     */   private double pRotX;
/*     */   private double pRotY;
/*     */   protected Player player;
/*     */   private int calmDown;
/*     */   private boolean isRunning;
/*     */   private final Ingredient items;
/*     */   private final boolean canScare;
/*     */   
/*     */   public TemptGoal(PathfinderMob debug1, double debug2, Ingredient debug4, boolean debug5) {
/*  30 */     this(debug1, debug2, debug5, debug4);
/*     */   }
/*     */   
/*     */   public TemptGoal(PathfinderMob debug1, double debug2, boolean debug4, Ingredient debug5) {
/*  34 */     this.mob = debug1;
/*  35 */     this.speedModifier = debug2;
/*  36 */     this.items = debug5;
/*  37 */     this.canScare = debug4;
/*  38 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  39 */     if (!(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/*  40 */       throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  46 */     if (this.calmDown > 0) {
/*  47 */       this.calmDown--;
/*  48 */       return false;
/*     */     } 
/*  50 */     this.player = this.mob.level.getNearestPlayer(TEMP_TARGETING, (LivingEntity)this.mob);
/*  51 */     if (this.player == null) {
/*  52 */       return false;
/*     */     }
/*  54 */     return (shouldFollowItem(this.player.getMainHandItem()) || shouldFollowItem(this.player.getOffhandItem()));
/*     */   }
/*     */   
/*     */   protected boolean shouldFollowItem(ItemStack debug1) {
/*  58 */     return this.items.test(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  63 */     if (canScare()) {
/*  64 */       if (this.mob.distanceToSqr((Entity)this.player) < 36.0D) {
/*  65 */         if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D) {
/*  66 */           return false;
/*     */         }
/*  68 */         if (Math.abs(this.player.xRot - this.pRotX) > 5.0D || Math.abs(this.player.yRot - this.pRotY) > 5.0D) {
/*  69 */           return false;
/*     */         }
/*     */       } else {
/*  72 */         this.px = this.player.getX();
/*  73 */         this.py = this.player.getY();
/*  74 */         this.pz = this.player.getZ();
/*     */       } 
/*  76 */       this.pRotX = this.player.xRot;
/*  77 */       this.pRotY = this.player.yRot;
/*     */     } 
/*  79 */     return canUse();
/*     */   }
/*     */   
/*     */   protected boolean canScare() {
/*  83 */     return this.canScare;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  88 */     this.px = this.player.getX();
/*  89 */     this.py = this.player.getY();
/*  90 */     this.pz = this.player.getZ();
/*  91 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  96 */     this.player = null;
/*  97 */     this.mob.getNavigation().stop();
/*  98 */     this.calmDown = 100;
/*  99 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     this.mob.getLookControl().setLookAt((Entity)this.player, (this.mob.getMaxHeadYRot() + 20), this.mob.getMaxHeadXRot());
/* 105 */     if (this.mob.distanceToSqr((Entity)this.player) < 6.25D) {
/* 106 */       this.mob.getNavigation().stop();
/*     */     } else {
/* 108 */       this.mob.getNavigation().moveTo((Entity)this.player, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/* 113 */     return this.isRunning;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\TemptGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */