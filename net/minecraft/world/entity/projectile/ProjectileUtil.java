/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ArrowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public final class ProjectileUtil
/*     */ {
/*     */   public static HitResult getHitResult(Entity debug0, Predicate<Entity> debug1) {
/*     */     EntityHitResult entityHitResult1;
/*  27 */     Vec3 debug2 = debug0.getDeltaMovement();
/*  28 */     Level debug3 = debug0.level;
/*     */     
/*  30 */     Vec3 debug4 = debug0.position();
/*  31 */     Vec3 debug5 = debug4.add(debug2);
/*  32 */     BlockHitResult blockHitResult = debug3.clip(new ClipContext(debug4, debug5, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, debug0));
/*     */     
/*  34 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/*  35 */       debug5 = blockHitResult.getLocation();
/*     */     }
/*  37 */     EntityHitResult entityHitResult2 = getEntityHitResult(debug3, debug0, debug4, debug5, debug0.getBoundingBox().expandTowards(debug0.getDeltaMovement()).inflate(1.0D), debug1);
/*     */     
/*  39 */     if (entityHitResult2 != null) {
/*  40 */       entityHitResult1 = entityHitResult2;
/*     */     }
/*     */     
/*  43 */     return (HitResult)entityHitResult1;
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
/*     */   
/*     */   @Nullable
/*     */   public static EntityHitResult getEntityHitResult(Level debug0, Entity debug1, Vec3 debug2, Vec3 debug3, AABB debug4, Predicate<Entity> debug5) {
/*  90 */     double debug6 = Double.MAX_VALUE;
/*  91 */     Entity debug8 = null;
/*     */     
/*  93 */     for (Entity debug10 : debug0.getEntities(debug1, debug4, debug5)) {
/*  94 */       AABB debug11 = debug10.getBoundingBox().inflate(0.30000001192092896D);
/*  95 */       Optional<Vec3> debug12 = debug11.clip(debug2, debug3);
/*  96 */       if (debug12.isPresent()) {
/*  97 */         double debug13 = debug2.distanceToSqr(debug12.get());
/*  98 */         if (debug13 < debug6) {
/*  99 */           debug8 = debug10;
/* 100 */           debug6 = debug13;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (debug8 == null) {
/* 106 */       return null;
/*     */     }
/* 108 */     return new EntityHitResult(debug8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void rotateTowardsMovement(Entity debug0, float debug1) {
/* 118 */     Vec3 debug2 = debug0.getDeltaMovement();
/*     */     
/* 120 */     if (debug2.lengthSqr() == 0.0D) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     float debug3 = Mth.sqrt(Entity.getHorizontalDistanceSqr(debug2));
/* 125 */     debug0.yRot = (float)(Mth.atan2(debug2.z, debug2.x) * 57.2957763671875D) + 90.0F;
/* 126 */     debug0.xRot = (float)(Mth.atan2(debug3, debug2.y) * 57.2957763671875D) - 90.0F;
/*     */     
/* 128 */     while (debug0.xRot - debug0.xRotO < -180.0F) {
/* 129 */       debug0.xRotO -= 360.0F;
/*     */     }
/* 131 */     while (debug0.xRot - debug0.xRotO >= 180.0F) {
/* 132 */       debug0.xRotO += 360.0F;
/*     */     }
/*     */     
/* 135 */     while (debug0.yRot - debug0.yRotO < -180.0F) {
/* 136 */       debug0.yRotO -= 360.0F;
/*     */     }
/* 138 */     while (debug0.yRot - debug0.yRotO >= 180.0F) {
/* 139 */       debug0.yRotO += 360.0F;
/*     */     }
/*     */     
/* 142 */     debug0.xRot = Mth.lerp(debug1, debug0.xRotO, debug0.xRot);
/* 143 */     debug0.yRot = Mth.lerp(debug1, debug0.yRotO, debug0.yRot);
/*     */   }
/*     */   
/*     */   public static InteractionHand getWeaponHoldingHand(LivingEntity debug0, Item debug1) {
/* 147 */     return (debug0.getMainHandItem().getItem() == debug1) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
/*     */   }
/*     */   
/*     */   public static AbstractArrow getMobArrow(LivingEntity debug0, ItemStack debug1, float debug2) {
/* 151 */     ArrowItem debug3 = (debug1.getItem() instanceof ArrowItem) ? (ArrowItem)debug1.getItem() : (ArrowItem)Items.ARROW;
/* 152 */     AbstractArrow debug4 = debug3.createArrow(debug0.level, debug1, debug0);
/* 153 */     debug4.setEnchantmentEffectsFromEntity(debug0, debug2);
/*     */     
/* 155 */     if (debug1.getItem() == Items.TIPPED_ARROW && 
/* 156 */       debug4 instanceof Arrow) {
/* 157 */       ((Arrow)debug4).setEffectsFromItem(debug1);
/*     */     }
/*     */ 
/*     */     
/* 161 */     return debug4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ProjectileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */