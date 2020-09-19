/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class SwimNodeEvaluator extends NodeEvaluator {
/*     */   private final boolean allowBreaching;
/*     */   
/*     */   public SwimNodeEvaluator(boolean debug1) {
/*  18 */     this.allowBreaching = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  23 */     return super.getNode(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getGoal(double debug1, double debug3, double debug5) {
/*  28 */     return new Target(super.getNode(Mth.floor(debug1 - (this.mob.getBbWidth() / 2.0F)), Mth.floor(debug3 + 0.5D), Mth.floor(debug5 - (this.mob.getBbWidth() / 2.0F))));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] debug1, Node debug2) {
/*  33 */     int debug3 = 0;
/*     */     
/*  35 */     for (Direction debug7 : Direction.values()) {
/*  36 */       Node debug8 = getWaterNode(debug2.x + debug7.getStepX(), debug2.y + debug7.getStepY(), debug2.z + debug7.getStepZ());
/*  37 */       if (debug8 != null && !debug8.closed) {
/*  38 */         debug1[debug3++] = debug8;
/*     */       }
/*     */     } 
/*     */     
/*  42 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4, Mob debug5, int debug6, int debug7, int debug8, boolean debug9, boolean debug10) {
/*  47 */     return getBlockPathType(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4) {
/*  52 */     BlockPos debug5 = new BlockPos(debug2, debug3, debug4);
/*  53 */     FluidState debug6 = debug1.getFluidState(debug5);
/*  54 */     BlockState debug7 = debug1.getBlockState(debug5);
/*     */     
/*  56 */     if (debug6.isEmpty() && debug7.isPathfindable(debug1, debug5.below(), PathComputationType.WATER) && debug7.isAir())
/*  57 */       return BlockPathTypes.BREACH; 
/*  58 */     if (!debug6.is((Tag)FluidTags.WATER) || !debug7.isPathfindable(debug1, debug5, PathComputationType.WATER)) {
/*  59 */       return BlockPathTypes.BLOCKED;
/*     */     }
/*     */     
/*  62 */     return BlockPathTypes.WATER;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Node getWaterNode(int debug1, int debug2, int debug3) {
/*  67 */     BlockPathTypes debug4 = isFree(debug1, debug2, debug3);
/*     */     
/*  69 */     if ((this.allowBreaching && debug4 == BlockPathTypes.BREACH) || debug4 == BlockPathTypes.WATER) {
/*  70 */       return getNode(debug1, debug2, debug3);
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Node getNode(int debug1, int debug2, int debug3) {
/*  78 */     Node debug4 = null;
/*     */     
/*  80 */     BlockPathTypes debug5 = getBlockPathType((BlockGetter)this.mob.level, debug1, debug2, debug3);
/*     */     
/*  82 */     float debug6 = this.mob.getPathfindingMalus(debug5);
/*     */ 
/*     */     
/*  85 */     debug4 = super.getNode(debug1, debug2, debug3);
/*  86 */     debug4.type = debug5;
/*  87 */     debug4.costMalus = Math.max(debug4.costMalus, debug6);
/*     */     
/*  89 */     if (debug6 >= 0.0F && this.level.getFluidState(new BlockPos(debug1, debug2, debug3)).isEmpty()) {
/*  90 */       debug4.costMalus += 8.0F;
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (debug5 == BlockPathTypes.OPEN) {
/*  95 */       return debug4;
/*     */     }
/*     */     
/*  98 */     return debug4;
/*     */   }
/*     */   
/*     */   private BlockPathTypes isFree(int debug1, int debug2, int debug3) {
/* 102 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/* 103 */     for (int i = debug1; i < debug1 + this.entityWidth; i++) {
/* 104 */       for (int debug6 = debug2; debug6 < debug2 + this.entityHeight; debug6++) {
/* 105 */         for (int debug7 = debug3; debug7 < debug3 + this.entityDepth; debug7++) {
/* 106 */           FluidState debug8 = this.level.getFluidState((BlockPos)debug4.set(i, debug6, debug7));
/* 107 */           BlockState debug9 = this.level.getBlockState((BlockPos)debug4.set(i, debug6, debug7));
/*     */           
/* 109 */           if (debug8.isEmpty() && debug9.isPathfindable((BlockGetter)this.level, debug4.below(), PathComputationType.WATER) && debug9.isAir())
/* 110 */             return BlockPathTypes.BREACH; 
/* 111 */           if (!debug8.is((Tag)FluidTags.WATER)) {
/* 112 */             return BlockPathTypes.BLOCKED;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     BlockState debug5 = this.level.getBlockState((BlockPos)debug4);
/*     */ 
/*     */     
/* 121 */     if (debug5.isPathfindable((BlockGetter)this.level, (BlockPos)debug4, PathComputationType.WATER)) {
/* 122 */       return BlockPathTypes.WATER;
/*     */     }
/*     */     
/* 125 */     return BlockPathTypes.BLOCKED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\SwimNodeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */