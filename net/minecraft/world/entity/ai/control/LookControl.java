/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LookControl
/*     */ {
/*     */   protected final Mob mob;
/*     */   protected float yMaxRotSpeed;
/*     */   protected float xMaxRotAngle;
/*     */   protected boolean hasWanted;
/*     */   protected double wantedX;
/*     */   protected double wantedY;
/*     */   protected double wantedZ;
/*     */   
/*     */   public LookControl(Mob debug1) {
/*  19 */     this.mob = debug1;
/*     */   }
/*     */   
/*     */   public void setLookAt(Vec3 debug1) {
/*  23 */     setLookAt(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookAt(Entity debug1, float debug2, float debug3) {
/*  31 */     setLookAt(debug1.getX(), getWantedY(debug1), debug1.getZ(), debug2, debug3);
/*     */   }
/*     */   
/*     */   public void setLookAt(double debug1, double debug3, double debug5) {
/*  35 */     setLookAt(debug1, debug3, debug5, this.mob.getHeadRotSpeed(), this.mob.getMaxHeadXRot());
/*     */   }
/*     */   
/*     */   public void setLookAt(double debug1, double debug3, double debug5, float debug7, float debug8) {
/*  39 */     this.wantedX = debug1;
/*  40 */     this.wantedY = debug3;
/*  41 */     this.wantedZ = debug5;
/*  42 */     this.yMaxRotSpeed = debug7;
/*  43 */     this.xMaxRotAngle = debug8;
/*  44 */     this.hasWanted = true;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  48 */     if (resetXRotOnTick()) {
/*  49 */       this.mob.xRot = 0.0F;
/*     */     }
/*     */     
/*  52 */     if (this.hasWanted) {
/*  53 */       this.hasWanted = false;
/*     */       
/*  55 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, getYRotD(), this.yMaxRotSpeed);
/*  56 */       this.mob.xRot = rotateTowards(this.mob.xRot, getXRotD(), this.xMaxRotAngle);
/*     */     } else {
/*  58 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
/*     */     } 
/*     */     
/*  61 */     if (!this.mob.getNavigation().isDone())
/*     */     {
/*  63 */       this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean resetXRotOnTick() {
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isHasWanted() {
/*  73 */     return this.hasWanted;
/*     */   }
/*     */   
/*     */   public double getWantedX() {
/*  77 */     return this.wantedX;
/*     */   }
/*     */   
/*     */   public double getWantedY() {
/*  81 */     return this.wantedY;
/*     */   }
/*     */   
/*     */   public double getWantedZ() {
/*  85 */     return this.wantedZ;
/*     */   }
/*     */   
/*     */   protected float getXRotD() {
/*  89 */     double debug1 = this.wantedX - this.mob.getX();
/*  90 */     double debug3 = this.wantedY - this.mob.getEyeY();
/*  91 */     double debug5 = this.wantedZ - this.mob.getZ();
/*  92 */     double debug7 = Mth.sqrt(debug1 * debug1 + debug5 * debug5);
/*  93 */     return (float)-(Mth.atan2(debug3, debug7) * 57.2957763671875D);
/*     */   }
/*     */   
/*     */   protected float getYRotD() {
/*  97 */     double debug1 = this.wantedX - this.mob.getX();
/*  98 */     double debug3 = this.wantedZ - this.mob.getZ();
/*  99 */     return (float)(Mth.atan2(debug3, debug1) * 57.2957763671875D) - 90.0F;
/*     */   }
/*     */   
/*     */   protected float rotateTowards(float debug1, float debug2, float debug3) {
/* 103 */     float debug4 = Mth.degreesDifference(debug1, debug2);
/* 104 */     float debug5 = Mth.clamp(debug4, -debug3, debug3);
/* 105 */     return debug1 + debug5;
/*     */   }
/*     */   
/*     */   private static double getWantedY(Entity debug0) {
/* 109 */     if (debug0 instanceof net.minecraft.world.entity.LivingEntity) {
/* 110 */       return debug0.getEyeY();
/*     */     }
/* 112 */     return ((debug0.getBoundingBox()).minY + (debug0.getBoundingBox()).maxY) / 2.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\LookControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */