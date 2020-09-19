/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DragonSittingFlamingPhase
/*     */   extends AbstractDragonSittingPhase
/*     */ {
/*     */   private int flameTicks;
/*     */   private int flameCount;
/*     */   private AreaEffectCloud flame;
/*     */   
/*     */   public DragonSittingFlamingPhase(EnderDragon debug1) {
/*  23 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doClientTick() {
/*  28 */     this.flameTicks++;
/*     */     
/*  30 */     if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
/*  31 */       Vec3 debug1 = this.dragon.getHeadLookVector(1.0F).normalize();
/*  32 */       debug1.yRot(-0.7853982F);
/*  33 */       double debug2 = this.dragon.head.getX();
/*  34 */       double debug4 = this.dragon.head.getY(0.5D);
/*  35 */       double debug6 = this.dragon.head.getZ();
/*  36 */       for (int debug8 = 0; debug8 < 8; debug8++) {
/*  37 */         double debug9 = debug2 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  38 */         double debug11 = debug4 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  39 */         double debug13 = debug6 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  40 */         for (int debug15 = 0; debug15 < 6; debug15++) {
/*  41 */           this.dragon.level.addParticle((ParticleOptions)ParticleTypes.DRAGON_BREATH, debug9, debug11, debug13, -debug1.x * 0.07999999821186066D * debug15, -debug1.y * 0.6000000238418579D, -debug1.z * 0.07999999821186066D * debug15);
/*     */         }
/*  43 */         debug1.yRot(0.19634955F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick() {
/*  50 */     this.flameTicks++;
/*     */     
/*  52 */     if (this.flameTicks >= 200) {
/*  53 */       if (this.flameCount >= 4) {
/*  54 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/*     */       } else {
/*  56 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*     */       } 
/*  58 */     } else if (this.flameTicks == 10) {
/*  59 */       Vec3 debug1 = (new Vec3(this.dragon.head.getX() - this.dragon.getX(), 0.0D, this.dragon.head.getZ() - this.dragon.getZ())).normalize();
/*  60 */       float debug2 = 5.0F;
/*  61 */       double debug3 = this.dragon.head.getX() + debug1.x * 5.0D / 2.0D;
/*  62 */       double debug5 = this.dragon.head.getZ() + debug1.z * 5.0D / 2.0D;
/*  63 */       double debug7 = this.dragon.head.getY(0.5D);
/*  64 */       double debug9 = debug7;
/*     */       
/*  66 */       BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos(debug3, debug9, debug5);
/*  67 */       while (this.dragon.level.isEmptyBlock((BlockPos)debug11)) {
/*  68 */         debug9--;
/*  69 */         if (debug9 < 0.0D) {
/*  70 */           debug9 = debug7;
/*     */           break;
/*     */         } 
/*  73 */         debug11.set(debug3, debug9, debug5);
/*     */       } 
/*  75 */       debug9 = (Mth.floor(debug9) + 1);
/*  76 */       this.flame = new AreaEffectCloud(this.dragon.level, debug3, debug9, debug5);
/*  77 */       this.flame.setOwner((LivingEntity)this.dragon);
/*  78 */       this.flame.setRadius(5.0F);
/*  79 */       this.flame.setDuration(200);
/*  80 */       this.flame.setParticle((ParticleOptions)ParticleTypes.DRAGON_BREATH);
/*  81 */       this.flame.addEffect(new MobEffectInstance(MobEffects.HARM));
/*  82 */       this.dragon.level.addFreshEntity((Entity)this.flame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  88 */     this.flameTicks = 0;
/*  89 */     this.flameCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  94 */     if (this.flame != null) {
/*  95 */       this.flame.remove();
/*  96 */       this.flame = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonSittingFlamingPhase> getPhase() {
/* 102 */     return EnderDragonPhase.SITTING_FLAMING;
/*     */   }
/*     */   
/*     */   public void resetFlameCount() {
/* 106 */     this.flameCount = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingFlamingPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */