/*     */ package net.minecraft.world.entity.projectile;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class ThrowableProjectile extends Projectile {
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> debug1, Level debug2) {
/*  20 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> debug1, double debug2, double debug4, double debug6, Level debug8) {
/*  24 */     this(debug1, debug8);
/*     */     
/*  26 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> debug1, LivingEntity debug2, Level debug3) {
/*  30 */     this(debug1, debug2.getX(), debug2.getEyeY() - 0.10000000149011612D, debug2.getZ(), debug3);
/*     */     
/*  32 */     setOwner((Entity)debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*     */     float debug10;
/*  47 */     super.tick();
/*     */     
/*  49 */     HitResult debug1 = ProjectileUtil.getHitResult(this, this::canHitEntity);
/*     */     
/*  51 */     boolean debug2 = false;
/*  52 */     if (debug1.getType() == HitResult.Type.BLOCK) {
/*  53 */       BlockPos blockPos = ((BlockHitResult)debug1).getBlockPos();
/*  54 */       BlockState blockState = this.level.getBlockState(blockPos);
/*  55 */       if (blockState.is(Blocks.NETHER_PORTAL)) {
/*  56 */         handleInsidePortal(blockPos);
/*  57 */         debug2 = true;
/*  58 */       } else if (blockState.is(Blocks.END_GATEWAY)) {
/*  59 */         BlockEntity debug5 = this.level.getBlockEntity(blockPos);
/*  60 */         if (debug5 instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
/*  61 */           ((TheEndGatewayBlockEntity)debug5).teleportEntity(this);
/*     */         }
/*  63 */         debug2 = true;
/*     */       } 
/*     */     } 
/*  66 */     if (debug1.getType() != HitResult.Type.MISS && !debug2) {
/*  67 */       onHit(debug1);
/*     */     }
/*     */     
/*  70 */     checkInsideBlocks();
/*  71 */     Vec3 debug3 = getDeltaMovement();
/*  72 */     double debug4 = getX() + debug3.x;
/*  73 */     double debug6 = getY() + debug3.y;
/*  74 */     double debug8 = getZ() + debug3.z;
/*     */     
/*  76 */     updateRotation();
/*     */ 
/*     */     
/*  79 */     if (isInWater()) {
/*  80 */       for (int debug11 = 0; debug11 < 4; debug11++) {
/*  81 */         float debug12 = 0.25F;
/*  82 */         this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, debug4 - debug3.x * 0.25D, debug6 - debug3.y * 0.25D, debug8 - debug3.z * 0.25D, debug3.x, debug3.y, debug3.z);
/*     */       } 
/*  84 */       debug10 = 0.8F;
/*     */     } else {
/*  86 */       debug10 = 0.99F;
/*     */     } 
/*     */     
/*  89 */     setDeltaMovement(debug3.scale(debug10));
/*     */     
/*  91 */     if (!isNoGravity()) {
/*  92 */       Vec3 debug11 = getDeltaMovement();
/*  93 */       setDeltaMovement(debug11.x, debug11.y - getGravity(), debug11.z);
/*     */     } 
/*     */     
/*  96 */     setPos(debug4, debug6, debug8);
/*     */   }
/*     */   
/*     */   protected float getGravity() {
/* 100 */     return 0.03F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 105 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrowableProjectile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */