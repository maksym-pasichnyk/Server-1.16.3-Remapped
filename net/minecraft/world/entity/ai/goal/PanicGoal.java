/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PanicGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final PathfinderMob mob;
/*     */   protected final double speedModifier;
/*     */   protected double posX;
/*     */   
/*     */   public PanicGoal(PathfinderMob debug1, double debug2) {
/*  23 */     this.mob = debug1;
/*  24 */     this.speedModifier = debug2;
/*  25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   protected double posY; protected double posZ; protected boolean isRunning;
/*     */   
/*     */   public boolean canUse() {
/*  30 */     if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     if (this.mob.isOnFire()) {
/*  35 */       BlockPos debug1 = lookForWater((BlockGetter)this.mob.level, (Entity)this.mob, 5, 4);
/*  36 */       if (debug1 != null) {
/*  37 */         this.posX = debug1.getX();
/*  38 */         this.posY = debug1.getY();
/*  39 */         this.posZ = debug1.getZ();
/*     */         
/*  41 */         return true;
/*     */       } 
/*     */     } 
/*  44 */     return findRandomPosition();
/*     */   }
/*     */   
/*     */   protected boolean findRandomPosition() {
/*  48 */     Vec3 debug1 = RandomPos.getPos(this.mob, 5, 4);
/*  49 */     if (debug1 == null) {
/*  50 */       return false;
/*     */     }
/*  52 */     this.posX = debug1.x;
/*  53 */     this.posY = debug1.y;
/*  54 */     this.posZ = debug1.z;
/*     */     
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  60 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  65 */     this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
/*  66 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  71 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  76 */     return !this.mob.getNavigation().isDone();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected BlockPos lookForWater(BlockGetter debug1, Entity debug2, int debug3, int debug4) {
/*  81 */     BlockPos debug5 = debug2.blockPosition();
/*  82 */     int debug6 = debug5.getX();
/*  83 */     int debug7 = debug5.getY();
/*  84 */     int debug8 = debug5.getZ();
/*     */     
/*  86 */     float debug9 = (debug3 * debug3 * debug4 * 2);
/*  87 */     BlockPos debug10 = null;
/*     */     
/*  89 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/*  90 */     for (int debug12 = debug6 - debug3; debug12 <= debug6 + debug3; debug12++) {
/*  91 */       for (int debug13 = debug7 - debug4; debug13 <= debug7 + debug4; debug13++) {
/*  92 */         for (int debug14 = debug8 - debug3; debug14 <= debug8 + debug3; debug14++) {
/*  93 */           debug11.set(debug12, debug13, debug14);
/*  94 */           if (debug1.getFluidState((BlockPos)debug11).is((Tag)FluidTags.WATER)) {
/*  95 */             float debug15 = ((debug12 - debug6) * (debug12 - debug6) + (debug13 - debug7) * (debug13 - debug7) + (debug14 - debug8) * (debug14 - debug8));
/*  96 */             if (debug15 < debug9) {
/*  97 */               debug9 = debug15;
/*  98 */               debug10 = new BlockPos((Vec3i)debug11);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 104 */     return debug10;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\PanicGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */