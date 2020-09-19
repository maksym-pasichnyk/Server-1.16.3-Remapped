/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class MinecartTNT extends AbstractMinecart {
/*  25 */   private int fuse = -1;
/*     */   
/*     */   public MinecartTNT(EntityType<? extends MinecartTNT> debug1, Level debug2) {
/*  28 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public MinecartTNT(Level debug1, double debug2, double debug4, double debug6) {
/*  32 */     super(EntityType.TNT_MINECART, debug1, debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractMinecart.Type getMinecartType() {
/*  37 */     return AbstractMinecart.Type.TNT;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  42 */     return Blocks.TNT.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  47 */     super.tick();
/*     */     
/*  49 */     if (this.fuse > 0) {
/*  50 */       this.fuse--;
/*  51 */       this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*  52 */     } else if (this.fuse == 0) {
/*  53 */       explode(getHorizontalDistanceSqr(getDeltaMovement()));
/*     */     } 
/*     */     
/*  56 */     if (this.horizontalCollision) {
/*  57 */       double debug1 = getHorizontalDistanceSqr(getDeltaMovement());
/*     */       
/*  59 */       if (debug1 >= 0.009999999776482582D) {
/*  60 */         explode(debug1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/*  67 */     Entity debug3 = debug1.getDirectEntity();
/*  68 */     if (debug3 instanceof AbstractArrow) {
/*  69 */       AbstractArrow debug4 = (AbstractArrow)debug3;
/*  70 */       if (debug4.isOnFire()) {
/*  71 */         explode(debug4.getDeltaMovement().lengthSqr());
/*     */       }
/*     */     } 
/*  74 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(DamageSource debug1) {
/*  79 */     double debug2 = getHorizontalDistanceSqr(getDeltaMovement());
/*     */     
/*  81 */     if (debug1.isFire() || debug1.isExplosion() || debug2 >= 0.009999999776482582D) {
/*  82 */       if (this.fuse < 0) {
/*  83 */         primeFuse();
/*  84 */         this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     super.destroy(debug1);
/*     */     
/*  91 */     if (!debug1.isExplosion() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/*  92 */       spawnAtLocation((ItemLike)Blocks.TNT);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void explode(double debug1) {
/*  97 */     if (!this.level.isClientSide) {
/*  98 */       double debug3 = Math.sqrt(debug1);
/*  99 */       if (debug3 > 5.0D) {
/* 100 */         debug3 = 5.0D;
/*     */       }
/* 102 */       this.level.explode(this, getX(), getY(), getZ(), (float)(4.0D + this.random.nextDouble() * 1.5D * debug3), Explosion.BlockInteraction.BREAK);
/* 103 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 109 */     if (debug1 >= 3.0F) {
/* 110 */       float debug3 = debug1 / 10.0F;
/* 111 */       explode((debug3 * debug3));
/*     */     } 
/*     */     
/* 114 */     return super.causeFallDamage(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(int debug1, int debug2, int debug3, boolean debug4) {
/* 119 */     if (debug4 && this.fuse < 0) {
/* 120 */       primeFuse();
/*     */     }
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
/*     */   public void primeFuse() {
/* 134 */     this.fuse = 80;
/*     */     
/* 136 */     if (!this.level.isClientSide) {
/* 137 */       this.level.broadcastEntityEvent(this, (byte)10);
/* 138 */       if (!isSilent()) {
/* 139 */         this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimed() {
/* 149 */     return (this.fuse > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5, float debug6) {
/* 154 */     if (isPrimed() && (debug4.is((Tag)BlockTags.RAILS) || debug2.getBlockState(debug3.above()).is((Tag)BlockTags.RAILS))) {
/* 155 */       return 0.0F;
/*     */     }
/*     */     
/* 158 */     return super.getBlockExplosionResistance(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldBlockExplode(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, float debug5) {
/* 163 */     if (isPrimed() && (debug4.is((Tag)BlockTags.RAILS) || debug2.getBlockState(debug3.above()).is((Tag)BlockTags.RAILS))) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     return super.shouldBlockExplode(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 172 */     super.readAdditionalSaveData(debug1);
/* 173 */     if (debug1.contains("TNTFuse", 99)) {
/* 174 */       this.fuse = debug1.getInt("TNTFuse");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 180 */     super.addAdditionalSaveData(debug1);
/* 181 */     debug1.putInt("TNTFuse", this.fuse);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartTNT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */