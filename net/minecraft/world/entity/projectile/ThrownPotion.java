/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrownPotion
/*     */   extends ThrowableItemProjectile
/*     */ {
/*  40 */   public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;
/*     */   
/*     */   public ThrownPotion(EntityType<? extends ThrownPotion> debug1, Level debug2) {
/*  43 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public ThrownPotion(Level debug1, LivingEntity debug2) {
/*  47 */     super(EntityType.POTION, debug2, debug1);
/*     */   }
/*     */   
/*     */   public ThrownPotion(Level debug1, double debug2, double debug4, double debug6) {
/*  51 */     super(EntityType.POTION, debug2, debug4, debug6, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDefaultItem() {
/*  56 */     return Items.SPLASH_POTION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getGravity() {
/*  61 */     return 0.05F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/*  66 */     super.onHitBlock(debug1);
/*  67 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*  70 */     ItemStack debug2 = getItem();
/*  71 */     Potion debug3 = PotionUtils.getPotion(debug2);
/*  72 */     List<MobEffectInstance> debug4 = PotionUtils.getMobEffects(debug2);
/*  73 */     boolean debug5 = (debug3 == Potions.WATER && debug4.isEmpty());
/*  74 */     Direction debug6 = debug1.getDirection();
/*  75 */     BlockPos debug7 = debug1.getBlockPos();
/*  76 */     BlockPos debug8 = debug7.relative(debug6);
/*     */     
/*  78 */     if (debug5) {
/*  79 */       dowseFire(debug8, debug6);
/*  80 */       dowseFire(debug8.relative(debug6.getOpposite()), debug6);
/*  81 */       for (Direction debug10 : Direction.Plane.HORIZONTAL) {
/*  82 */         dowseFire(debug8.relative(debug10), debug10);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult debug1) {
/*  89 */     super.onHit(debug1);
/*  90 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*  93 */     ItemStack debug2 = getItem();
/*     */     
/*  95 */     Potion debug3 = PotionUtils.getPotion(debug2);
/*  96 */     List<MobEffectInstance> debug4 = PotionUtils.getMobEffects(debug2);
/*  97 */     boolean debug5 = (debug3 == Potions.WATER && debug4.isEmpty());
/*     */     
/*  99 */     if (debug5) {
/* 100 */       applyWater();
/* 101 */     } else if (!debug4.isEmpty()) {
/* 102 */       if (isLingering()) {
/* 103 */         makeAreaOfEffectCloud(debug2, debug3);
/*     */       } else {
/* 105 */         applySplash(debug4, (debug1.getType() == HitResult.Type.ENTITY) ? ((EntityHitResult)debug1).getEntity() : null);
/*     */       } 
/*     */     } 
/* 108 */     int debug6 = debug3.hasInstantEffects() ? 2007 : 2002;
/* 109 */     this.level.levelEvent(debug6, blockPosition(), PotionUtils.getColor(debug2));
/*     */     
/* 111 */     remove();
/*     */   }
/*     */   
/*     */   private void applyWater() {
/* 115 */     AABB debug1 = getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
/* 116 */     List<LivingEntity> debug2 = this.level.getEntitiesOfClass(LivingEntity.class, debug1, WATER_SENSITIVE);
/* 117 */     if (!debug2.isEmpty()) {
/* 118 */       for (LivingEntity debug4 : debug2) {
/* 119 */         double debug5 = distanceToSqr((Entity)debug4);
/* 120 */         if (debug5 < 16.0D && debug4.isSensitiveToWater()) {
/* 121 */           debug4.hurt(DamageSource.indirectMagic((Entity)debug4, getOwner()), 1.0F);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void applySplash(List<MobEffectInstance> debug1, @Nullable Entity debug2) {
/* 128 */     AABB debug3 = getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
/* 129 */     List<LivingEntity> debug4 = this.level.getEntitiesOfClass(LivingEntity.class, debug3);
/*     */     
/* 131 */     if (!debug4.isEmpty()) {
/* 132 */       for (LivingEntity debug6 : debug4) {
/* 133 */         if (!debug6.isAffectedByPotions()) {
/*     */           continue;
/*     */         }
/* 136 */         double debug7 = distanceToSqr((Entity)debug6);
/* 137 */         if (debug7 < 16.0D) {
/* 138 */           double debug9 = 1.0D - Math.sqrt(debug7) / 4.0D;
/* 139 */           if (debug6 == debug2) {
/* 140 */             debug9 = 1.0D;
/*     */           }
/*     */           
/* 143 */           for (MobEffectInstance debug12 : debug1) {
/* 144 */             MobEffect debug13 = debug12.getEffect();
/* 145 */             if (debug13.isInstantenous()) {
/* 146 */               debug13.applyInstantenousEffect(this, getOwner(), debug6, debug12.getAmplifier(), debug9); continue;
/*     */             } 
/* 148 */             int debug14 = (int)(debug9 * debug12.getDuration() + 0.5D);
/* 149 */             if (debug14 > 20) {
/* 150 */               debug6.addEffect(new MobEffectInstance(debug13, debug14, debug12.getAmplifier(), debug12.isAmbient(), debug12.isVisible()));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void makeAreaOfEffectCloud(ItemStack debug1, Potion debug2) {
/* 160 */     AreaEffectCloud debug3 = new AreaEffectCloud(this.level, getX(), getY(), getZ());
/* 161 */     Entity debug4 = getOwner();
/* 162 */     if (debug4 instanceof LivingEntity) {
/* 163 */       debug3.setOwner((LivingEntity)debug4);
/*     */     }
/* 165 */     debug3.setRadius(3.0F);
/* 166 */     debug3.setRadiusOnUse(-0.5F);
/* 167 */     debug3.setWaitTime(10);
/* 168 */     debug3.setRadiusPerTick(-debug3.getRadius() / debug3.getDuration());
/* 169 */     debug3.setPotion(debug2);
/* 170 */     for (MobEffectInstance debug6 : PotionUtils.getCustomEffects(debug1)) {
/* 171 */       debug3.addEffect(new MobEffectInstance(debug6));
/*     */     }
/*     */     
/* 174 */     CompoundTag debug5 = debug1.getTag();
/* 175 */     if (debug5 != null && debug5.contains("CustomPotionColor", 99)) {
/* 176 */       debug3.setFixedColor(debug5.getInt("CustomPotionColor"));
/*     */     }
/*     */     
/* 179 */     this.level.addFreshEntity((Entity)debug3);
/*     */   }
/*     */   
/*     */   private boolean isLingering() {
/* 183 */     return (getItem().getItem() == Items.LINGERING_POTION);
/*     */   }
/*     */   
/*     */   private void dowseFire(BlockPos debug1, Direction debug2) {
/* 187 */     BlockState debug3 = this.level.getBlockState(debug1);
/* 188 */     if (debug3.is((Tag)BlockTags.FIRE)) {
/* 189 */       this.level.removeBlock(debug1, false);
/* 190 */     } else if (CampfireBlock.isLitCampfire(debug3)) {
/* 191 */       this.level.levelEvent(null, 1009, debug1, 0);
/* 192 */       CampfireBlock.dowse((LevelAccessor)this.level, debug1, debug3);
/* 193 */       this.level.setBlockAndUpdate(debug1, (BlockState)debug3.setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrownPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */