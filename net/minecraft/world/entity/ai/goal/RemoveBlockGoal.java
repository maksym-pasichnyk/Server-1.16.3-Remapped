/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RemoveBlockGoal
/*     */   extends MoveToBlockGoal {
/*     */   private final Block blockToRemove;
/*     */   private final Mob removerMob;
/*     */   private int ticksSinceReachedGoal;
/*     */   
/*     */   public RemoveBlockGoal(Block debug1, PathfinderMob debug2, double debug3, int debug5) {
/*  32 */     super(debug2, debug3, 24, debug5);
/*  33 */     this.blockToRemove = debug1;
/*  34 */     this.removerMob = (Mob)debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  39 */     if (!this.removerMob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     if (this.nextStartTick > 0) {
/*  44 */       this.nextStartTick--;
/*  45 */       return false;
/*     */     } 
/*     */     
/*  48 */     if (tryFindBlock()) {
/*     */       
/*  50 */       this.nextStartTick = 20;
/*  51 */       return true;
/*     */     } 
/*  53 */     this.nextStartTick = nextStartTick(this.mob);
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean tryFindBlock() {
/*  59 */     if (this.blockPos != null && isValidTarget((LevelReader)this.mob.level, this.blockPos)) {
/*  60 */       return true;
/*     */     }
/*     */     
/*  63 */     return findNearestBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  68 */     super.stop();
/*  69 */     this.removerMob.fallDistance = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  74 */     super.start();
/*  75 */     this.ticksSinceReachedGoal = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playDestroyProgressSound(LevelAccessor debug1, BlockPos debug2) {}
/*     */ 
/*     */   
/*     */   public void playBreakSound(Level debug1, BlockPos debug2) {}
/*     */ 
/*     */   
/*     */   public void tick() {
/*  86 */     super.tick();
/*  87 */     Level debug1 = this.removerMob.level;
/*  88 */     BlockPos debug2 = this.removerMob.blockPosition();
/*     */     
/*  90 */     BlockPos debug3 = getPosWithBlock(debug2, (BlockGetter)debug1);
/*     */     
/*  92 */     Random debug4 = this.removerMob.getRandom();
/*  93 */     if (isReachedTarget() && debug3 != null) {
/*  94 */       if (this.ticksSinceReachedGoal > 0) {
/*  95 */         Vec3 debug5 = this.removerMob.getDeltaMovement();
/*  96 */         this.removerMob.setDeltaMovement(debug5.x, 0.3D, debug5.z);
/*     */         
/*  98 */         if (!debug1.isClientSide) {
/*  99 */           double debug6 = 0.08D;
/* 100 */           ((ServerLevel)debug1).sendParticles((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, new ItemStack((ItemLike)Items.EGG)), debug3
/*     */               
/* 102 */               .getX() + 0.5D, debug3
/* 103 */               .getY() + 0.7D, debug3
/* 104 */               .getZ() + 0.5D, 3, (debug4
/*     */               
/* 106 */               .nextFloat() - 0.5D) * 0.08D, (debug4
/* 107 */               .nextFloat() - 0.5D) * 0.08D, (debug4
/* 108 */               .nextFloat() - 0.5D) * 0.08D, 0.15000000596046448D);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 114 */       if (this.ticksSinceReachedGoal % 2 == 0) {
/* 115 */         Vec3 debug5 = this.removerMob.getDeltaMovement();
/* 116 */         this.removerMob.setDeltaMovement(debug5.x, -0.3D, debug5.z);
/*     */         
/* 118 */         if (this.ticksSinceReachedGoal % 6 == 0) {
/* 119 */           playDestroyProgressSound((LevelAccessor)debug1, this.blockPos);
/*     */         }
/*     */       } 
/*     */       
/* 123 */       if (this.ticksSinceReachedGoal > 60) {
/* 124 */         debug1.removeBlock(debug3, false);
/* 125 */         if (!debug1.isClientSide) {
/* 126 */           for (int debug5 = 0; debug5 < 20; debug5++) {
/* 127 */             double debug6 = debug4.nextGaussian() * 0.02D;
/* 128 */             double debug8 = debug4.nextGaussian() * 0.02D;
/* 129 */             double debug10 = debug4.nextGaussian() * 0.02D;
/* 130 */             ((ServerLevel)debug1).sendParticles((ParticleOptions)ParticleTypes.POOF, debug3.getX() + 0.5D, debug3.getY(), debug3.getZ() + 0.5D, 1, debug6, debug8, debug10, 0.15000000596046448D);
/*     */           } 
/* 132 */           playBreakSound(debug1, debug3);
/*     */         } 
/*     */       } 
/* 135 */       this.ticksSinceReachedGoal++;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockPos getPosWithBlock(BlockPos debug1, BlockGetter debug2) {
/* 141 */     if (debug2.getBlockState(debug1).is(this.blockToRemove)) {
/* 142 */       return debug1;
/*     */     }
/* 144 */     BlockPos[] debug3 = { debug1.below(), debug1.west(), debug1.east(), debug1.north(), debug1.south(), debug1.below().below() };
/* 145 */     for (BlockPos debug7 : debug3) {
/* 146 */       if (debug2.getBlockState(debug7).is(this.blockToRemove)) {
/* 147 */         return debug7;
/*     */       }
/*     */     } 
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 155 */     ChunkAccess debug3 = debug1.getChunk(debug2.getX() >> 4, debug2.getZ() >> 4, ChunkStatus.FULL, false);
/* 156 */     if (debug3 != null) {
/* 157 */       return (debug3.getBlockState(debug2).is(this.blockToRemove) && debug3.getBlockState(debug2.above()).isAir() && debug3.getBlockState(debug2.above(2)).isAir());
/*     */     }
/* 159 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RemoveBlockGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */