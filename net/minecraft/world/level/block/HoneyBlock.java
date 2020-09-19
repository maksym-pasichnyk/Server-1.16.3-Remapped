/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*     */ public class HoneyBlock
/*     */   extends HalfTransparentBlock
/*     */ {
/*  61 */   protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
/*     */   
/*     */   public HoneyBlock(BlockBehaviour.Properties debug1) {
/*  64 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean doesEntityDoHoneyBlockSlideEffects(Entity debug0) {
/*  69 */     return (debug0 instanceof net.minecraft.world.entity.LivingEntity || debug0 instanceof net.minecraft.world.entity.vehicle.AbstractMinecart || debug0 instanceof net.minecraft.world.entity.item.PrimedTnt || debug0 instanceof net.minecraft.world.entity.vehicle.Boat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  77 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/*  82 */     debug3.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */     
/*  84 */     if (!debug1.isClientSide)
/*     */     {
/*     */       
/*  87 */       debug1.broadcastEntityEvent(debug3, (byte)54);
/*     */     }
/*     */     
/*  90 */     if (debug3.causeFallDamage(debug4, 0.2F)) {
/*  91 */       debug3.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  97 */     if (isSlidingDown(debug3, debug4)) {
/*  98 */       maybeDoSlideAchievement(debug4, debug3);
/*  99 */       doSlideMovement(debug4);
/* 100 */       maybeDoSlideEffects(debug2, debug4);
/*     */     } 
/* 102 */     super.entityInside(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private boolean isSlidingDown(BlockPos debug1, Entity debug2) {
/* 106 */     if (debug2.isOnGround()) {
/* 107 */       return false;
/*     */     }
/* 109 */     if (debug2.getY() > debug1.getY() + 0.9375D - 1.0E-7D)
/*     */     {
/* 111 */       return false;
/*     */     }
/* 113 */     if ((debug2.getDeltaMovement()).y >= -0.08D) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     double debug3 = Math.abs(debug1.getX() + 0.5D - debug2.getX());
/* 118 */     double debug5 = Math.abs(debug1.getZ() + 0.5D - debug2.getZ());
/*     */     
/* 120 */     double debug7 = 0.4375D + (debug2.getBbWidth() / 2.0F);
/*     */     
/* 122 */     return (debug3 + 1.0E-7D > debug7 || debug5 + 1.0E-7D > debug7);
/*     */   }
/*     */   
/*     */   private void maybeDoSlideAchievement(Entity debug1, BlockPos debug2) {
/* 126 */     if (debug1 instanceof ServerPlayer && debug1.level.getGameTime() % 20L == 0L)
/*     */     {
/* 128 */       CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer)debug1, debug1.level.getBlockState(debug2));
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSlideMovement(Entity debug1) {
/* 133 */     Vec3 debug2 = debug1.getDeltaMovement();
/* 134 */     if (debug2.y < -0.13D) {
/*     */ 
/*     */ 
/*     */       
/* 138 */       double debug3 = -0.05D / debug2.y;
/* 139 */       debug1.setDeltaMovement(new Vec3(debug2.x * debug3, -0.05D, debug2.z * debug3));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 145 */       debug1.setDeltaMovement(new Vec3(debug2.x, -0.05D, debug2.z));
/*     */     } 
/* 147 */     debug1.fallDistance = 0.0F;
/*     */   }
/*     */   
/*     */   private void maybeDoSlideEffects(Level debug1, Entity debug2) {
/* 151 */     if (doesEntityDoHoneyBlockSlideEffects(debug2)) {
/* 152 */       if (debug1.random.nextInt(5) == 0)
/*     */       {
/* 154 */         debug2.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 157 */       if (!debug1.isClientSide && debug1.random.nextInt(5) == 0)
/*     */       {
/* 159 */         debug1.broadcastEntityEvent(debug2, (byte)53);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\HoneyBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */