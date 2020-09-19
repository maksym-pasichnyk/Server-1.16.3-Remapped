/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.horse.Llama;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LlamaSpit
/*     */   extends Projectile {
/*     */   public LlamaSpit(EntityType<? extends LlamaSpit> debug1, Level debug2) {
/*  21 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public LlamaSpit(Level debug1, Llama debug2) {
/*  25 */     this(EntityType.LLAMA_SPIT, debug1);
/*  26 */     setOwner((Entity)debug2);
/*  27 */     setPos(debug2.getX() - (debug2.getBbWidth() + 1.0F) * 0.5D * Mth.sin(debug2.yBodyRot * 0.017453292F), debug2.getEyeY() - 0.10000000149011612D, debug2.getZ() + (debug2.getBbWidth() + 1.0F) * 0.5D * Mth.cos(debug2.yBodyRot * 0.017453292F));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  44 */     super.tick();
/*     */     
/*  46 */     Vec3 debug1 = getDeltaMovement();
/*  47 */     HitResult debug2 = ProjectileUtil.getHitResult(this, this::canHitEntity);
/*     */     
/*  49 */     if (debug2 != null) {
/*  50 */       onHit(debug2);
/*     */     }
/*     */     
/*  53 */     double debug3 = getX() + debug1.x;
/*  54 */     double debug5 = getY() + debug1.y;
/*  55 */     double debug7 = getZ() + debug1.z;
/*     */     
/*  57 */     updateRotation();
/*     */     
/*  59 */     float debug9 = 0.99F;
/*  60 */     float debug10 = 0.06F;
/*     */     
/*  62 */     if (this.level.getBlockStates(getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
/*  63 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  67 */     if (isInWaterOrBubble()) {
/*  68 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     setDeltaMovement(debug1.scale(0.9900000095367432D));
/*  73 */     if (!isNoGravity()) {
/*  74 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.05999999865889549D, 0.0D));
/*     */     }
/*     */     
/*  77 */     setPos(debug3, debug5, debug7);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/*  82 */     super.onHitEntity(debug1);
/*  83 */     Entity debug2 = getOwner();
/*  84 */     if (debug2 instanceof LivingEntity) {
/*  85 */       debug1.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity)debug2).setProjectile(), 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/*  91 */     super.onHitBlock(debug1);
/*     */     
/*  93 */     if (!this.level.isClientSide) {
/*  94 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 104 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\LlamaSpit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */