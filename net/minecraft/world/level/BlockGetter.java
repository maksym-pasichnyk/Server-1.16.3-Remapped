/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public interface BlockGetter
/*     */ {
/*     */   default int getLightEmission(BlockPos debug1) {
/*  42 */     return getBlockState(debug1).getLightEmission();
/*     */   }
/*     */   
/*     */   default int getMaxLightLevel() {
/*  46 */     return 15;
/*     */   }
/*     */   
/*     */   default int getMaxBuildHeight() {
/*  50 */     return 256;
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStates(AABB debug1) {
/*  54 */     return BlockPos.betweenClosedStream(debug1).map(this::getBlockState);
/*     */   }
/*     */   
/*     */   default BlockHitResult clip(ClipContext debug1) {
/*  58 */     return traverseBlocks(debug1, (debug1, debug2) -> {
/*     */           BlockState debug3 = getBlockState(debug2);
/*     */           FluidState debug4 = getFluidState(debug2);
/*     */           Vec3 debug5 = debug1.getFrom();
/*     */           Vec3 debug6 = debug1.getTo();
/*     */           VoxelShape debug7 = debug1.getBlockShape(debug3, this, debug2);
/*     */           BlockHitResult debug8 = clipWithInteractionOverride(debug5, debug6, debug2, debug7, debug3);
/*     */           VoxelShape debug9 = debug1.getFluidShape(debug4, this, debug2);
/*     */           BlockHitResult debug10 = debug9.clip(debug5, debug6, debug2);
/*     */           double debug11 = (debug8 == null) ? Double.MAX_VALUE : debug1.getFrom().distanceToSqr(debug8.getLocation());
/*     */           double debug13 = (debug10 == null) ? Double.MAX_VALUE : debug1.getFrom().distanceToSqr(debug10.getLocation());
/*     */           return (debug11 <= debug13) ? debug8 : debug10;
/*     */         }debug0 -> {
/*     */           Vec3 debug1 = debug0.getFrom().subtract(debug0.getTo());
/*     */           return BlockHitResult.miss(debug0.getTo(), Direction.getNearest(debug1.x, debug1.y, debug1.z), new BlockPos(debug0.getTo()));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default BlockHitResult clipWithInteractionOverride(Vec3 debug1, Vec3 debug2, BlockPos debug3, VoxelShape debug4, BlockState debug5) {
/*  84 */     BlockHitResult debug6 = debug4.clip(debug1, debug2, debug3);
/*  85 */     if (debug6 != null) {
/*     */       
/*  87 */       BlockHitResult debug7 = debug5.getInteractionShape(this, debug3).clip(debug1, debug2, debug3);
/*  88 */       if (debug7 != null && debug7.getLocation().subtract(debug1).lengthSqr() < debug6.getLocation().subtract(debug1).lengthSqr()) {
/*  89 */         return debug6.withDirection(debug7.getDirection());
/*     */       }
/*     */     } 
/*  92 */     return debug6;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(VoxelShape debug1, Supplier<VoxelShape> debug2) {
/*  96 */     if (!debug1.isEmpty()) {
/*  97 */       return debug1.max(Direction.Axis.Y);
/*     */     }
/*     */ 
/*     */     
/* 101 */     double debug3 = ((VoxelShape)debug2.get()).max(Direction.Axis.Y);
/* 102 */     if (debug3 >= 1.0D) {
/* 103 */       return debug3 - 1.0D;
/*     */     }
/*     */     
/* 106 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(BlockPos debug1) {
/* 110 */     return getBlockFloorHeight(getBlockState(debug1).getCollisionShape(this, debug1), () -> {
/*     */           BlockPos debug2 = debug1.below();
/*     */           return getBlockState(debug2).getCollisionShape(this, debug2);
/*     */         });
/*     */   }
/*     */   
/*     */   static <T> T traverseBlocks(ClipContext debug0, BiFunction<ClipContext, BlockPos, T> debug1, Function<ClipContext, T> debug2) {
/* 117 */     Vec3 debug3 = debug0.getFrom();
/* 118 */     Vec3 debug4 = debug0.getTo();
/*     */     
/* 120 */     if (debug3.equals(debug4)) {
/* 121 */       return debug2.apply(debug0);
/*     */     }
/*     */ 
/*     */     
/* 125 */     double debug5 = Mth.lerp(-1.0E-7D, debug4.x, debug3.x);
/* 126 */     double debug7 = Mth.lerp(-1.0E-7D, debug4.y, debug3.y);
/* 127 */     double debug9 = Mth.lerp(-1.0E-7D, debug4.z, debug3.z);
/*     */     
/* 129 */     double debug11 = Mth.lerp(-1.0E-7D, debug3.x, debug4.x);
/* 130 */     double debug13 = Mth.lerp(-1.0E-7D, debug3.y, debug4.y);
/* 131 */     double debug15 = Mth.lerp(-1.0E-7D, debug3.z, debug4.z);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     int debug17 = Mth.floor(debug11);
/* 137 */     int debug18 = Mth.floor(debug13);
/* 138 */     int debug19 = Mth.floor(debug15);
/*     */     
/* 140 */     BlockPos.MutableBlockPos debug20 = new BlockPos.MutableBlockPos(debug17, debug18, debug19);
/* 141 */     T debug21 = debug1.apply(debug0, debug20);
/* 142 */     if (debug21 != null) {
/* 143 */       return debug21;
/*     */     }
/*     */     
/* 146 */     double debug22 = debug5 - debug11;
/* 147 */     double debug24 = debug7 - debug13;
/* 148 */     double debug26 = debug9 - debug15;
/*     */     
/* 150 */     int debug28 = Mth.sign(debug22);
/* 151 */     int debug29 = Mth.sign(debug24);
/* 152 */     int debug30 = Mth.sign(debug26);
/*     */     
/* 154 */     double debug31 = (debug28 == 0) ? Double.MAX_VALUE : (debug28 / debug22);
/* 155 */     double debug33 = (debug29 == 0) ? Double.MAX_VALUE : (debug29 / debug24);
/* 156 */     double debug35 = (debug30 == 0) ? Double.MAX_VALUE : (debug30 / debug26);
/*     */     
/* 158 */     double debug37 = debug31 * ((debug28 > 0) ? (1.0D - Mth.frac(debug11)) : Mth.frac(debug11));
/* 159 */     double debug39 = debug33 * ((debug29 > 0) ? (1.0D - Mth.frac(debug13)) : Mth.frac(debug13));
/* 160 */     double debug41 = debug35 * ((debug30 > 0) ? (1.0D - Mth.frac(debug15)) : Mth.frac(debug15));
/*     */     
/* 162 */     while (debug37 <= 1.0D || debug39 <= 1.0D || debug41 <= 1.0D) {
/* 163 */       if (debug37 < debug39) {
/* 164 */         if (debug37 < debug41) {
/* 165 */           debug17 += debug28;
/* 166 */           debug37 += debug31;
/*     */         } else {
/* 168 */           debug19 += debug30;
/* 169 */           debug41 += debug35;
/*     */         }
/*     */       
/* 172 */       } else if (debug39 < debug41) {
/* 173 */         debug18 += debug29;
/* 174 */         debug39 += debug33;
/*     */       } else {
/* 176 */         debug19 += debug30;
/* 177 */         debug41 += debug35;
/*     */       } 
/*     */ 
/*     */       
/* 181 */       T debug43 = debug1.apply(debug0, debug20.set(debug17, debug18, debug19));
/* 182 */       if (debug43 != null) {
/* 183 */         return debug43;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return debug2.apply(debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   BlockEntity getBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   BlockState getBlockState(BlockPos paramBlockPos);
/*     */   
/*     */   FluidState getFluidState(BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\BlockGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */