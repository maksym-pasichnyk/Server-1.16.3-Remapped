/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.animal.Dolphin;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DolphinJumpGoal extends JumpGoal {
/*  14 */   private static final int[] STEPS_TO_CHECK = new int[] { 0, 1, 4, 5, 6, 7 };
/*     */   
/*     */   private final Dolphin dolphin;
/*     */   
/*     */   private final int interval;
/*     */   
/*     */   private boolean breached;
/*     */   
/*     */   public DolphinJumpGoal(Dolphin debug1, int debug2) {
/*  23 */     this.dolphin = debug1;
/*  24 */     this.interval = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  29 */     if (this.dolphin.getRandom().nextInt(this.interval) != 0) {
/*  30 */       return false;
/*     */     }
/*     */     
/*  33 */     Direction debug1 = this.dolphin.getMotionDirection();
/*  34 */     int debug2 = debug1.getStepX();
/*  35 */     int debug3 = debug1.getStepZ();
/*  36 */     BlockPos debug4 = this.dolphin.blockPosition();
/*     */     
/*  38 */     for (int debug8 : STEPS_TO_CHECK) {
/*  39 */       if (!waterIsClear(debug4, debug2, debug3, debug8) || !surfaceIsClear(debug4, debug2, debug3, debug8)) {
/*  40 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  44 */     return true;
/*     */   }
/*     */   
/*     */   private boolean waterIsClear(BlockPos debug1, int debug2, int debug3, int debug4) {
/*  48 */     BlockPos debug5 = debug1.offset(debug2 * debug4, 0, debug3 * debug4);
/*  49 */     return (this.dolphin.level.getFluidState(debug5).is((Tag)FluidTags.WATER) && !this.dolphin.level.getBlockState(debug5).getMaterial().blocksMotion());
/*     */   }
/*     */   
/*     */   private boolean surfaceIsClear(BlockPos debug1, int debug2, int debug3, int debug4) {
/*  53 */     return (this.dolphin.level.getBlockState(debug1.offset(debug2 * debug4, 1, debug3 * debug4)).isAir() && this.dolphin.level
/*  54 */       .getBlockState(debug1.offset(debug2 * debug4, 2, debug3 * debug4)).isAir());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  59 */     double debug1 = (this.dolphin.getDeltaMovement()).y;
/*  60 */     return ((debug1 * debug1 >= 0.029999999329447746D || this.dolphin.xRot == 0.0F || Math.abs(this.dolphin.xRot) >= 10.0F || !this.dolphin.isInWater()) && !this.dolphin.isOnGround());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  71 */     Direction debug1 = this.dolphin.getMotionDirection();
/*  72 */     this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(debug1
/*  73 */           .getStepX() * 0.6D, 0.7D, debug1
/*     */           
/*  75 */           .getStepZ() * 0.6D));
/*     */ 
/*     */     
/*  78 */     this.dolphin.getNavigation().stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  83 */     this.dolphin.xRot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  88 */     boolean debug1 = this.breached;
/*  89 */     if (!debug1) {
/*  90 */       FluidState fluidState = this.dolphin.level.getFluidState(this.dolphin.blockPosition());
/*  91 */       this.breached = fluidState.is((Tag)FluidTags.WATER);
/*     */     } 
/*     */     
/*  94 */     if (this.breached && !debug1) {
/*  95 */       this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
/*     */     }
/*     */     
/*  98 */     Vec3 debug2 = this.dolphin.getDeltaMovement();
/*  99 */     if (debug2.y * debug2.y < 0.029999999329447746D && this.dolphin.xRot != 0.0F) {
/* 100 */       this.dolphin.xRot = Mth.rotlerp(this.dolphin.xRot, 0.0F, 0.2F);
/*     */     } else {
/* 102 */       double debug3 = Math.sqrt(Entity.getHorizontalDistanceSqr(debug2));
/* 103 */       double debug5 = Math.signum(-debug2.y) * Math.acos(debug3 / debug2.length()) * 57.2957763671875D;
/* 104 */       this.dolphin.xRot = (float)debug5;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\DolphinJumpGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */