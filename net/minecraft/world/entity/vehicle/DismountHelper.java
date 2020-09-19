/*     */ package net.minecraft.world.entity.vehicle;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.block.TrapDoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DismountHelper {
/*     */   public static int[][] offsetsForDirection(Direction debug0) {
/*  25 */     Direction debug1 = debug0.getClockWise();
/*  26 */     Direction debug2 = debug1.getOpposite();
/*  27 */     Direction debug3 = debug0.getOpposite();
/*     */     
/*  29 */     return new int[][] { { debug1
/*  30 */           .getStepX(), debug1.getStepZ() }, { debug2
/*  31 */           .getStepX(), debug2.getStepZ() }, { debug3
/*  32 */           .getStepX() + debug1.getStepX(), debug3.getStepZ() + debug1.getStepZ() }, { debug3
/*  33 */           .getStepX() + debug2.getStepX(), debug3.getStepZ() + debug2.getStepZ() }, { debug0
/*  34 */           .getStepX() + debug1.getStepX(), debug0.getStepZ() + debug1.getStepZ() }, { debug0
/*  35 */           .getStepX() + debug2.getStepX(), debug0.getStepZ() + debug2.getStepZ() }, { debug3
/*  36 */           .getStepX(), debug3.getStepZ() }, { debug0
/*  37 */           .getStepX(), debug0.getStepZ() } };
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBlockFloorValid(double debug0) {
/*  42 */     return (!Double.isInfinite(debug0) && debug0 < 1.0D);
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter debug0, LivingEntity debug1, AABB debug2) {
/*  46 */     return debug0.getBlockCollisions((Entity)debug1, debug2).allMatch(VoxelShape::isEmpty);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 findDismountLocation(CollisionGetter debug0, double debug1, double debug3, double debug5, LivingEntity debug7, Pose debug8) {
/*  51 */     if (isBlockFloorValid(debug3)) {
/*  52 */       Vec3 debug9 = new Vec3(debug1, debug3, debug5);
/*  53 */       if (canDismountTo(debug0, debug7, debug7.getLocalBoundsForPose(debug8).move(debug9))) {
/*  54 */         return debug9;
/*     */       }
/*     */     } 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   public static VoxelShape nonClimbableShape(BlockGetter debug0, BlockPos debug1) {
/*  61 */     BlockState debug2 = debug0.getBlockState(debug1);
/*  62 */     if (debug2.is((Tag)BlockTags.CLIMBABLE) || (debug2.getBlock() instanceof TrapDoorBlock && ((Boolean)debug2.getValue((Property)TrapDoorBlock.OPEN)).booleanValue())) {
/*  63 */       return Shapes.empty();
/*     */     }
/*  65 */     return debug2.getCollisionShape(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static double findCeilingFrom(BlockPos debug0, int debug1, Function<BlockPos, VoxelShape> debug2) {
/*  69 */     BlockPos.MutableBlockPos debug3 = debug0.mutable();
/*  70 */     int debug4 = 0;
/*  71 */     while (debug4 < debug1) {
/*  72 */       VoxelShape debug5 = debug2.apply(debug3);
/*  73 */       if (!debug5.isEmpty()) {
/*  74 */         return (debug0.getY() + debug4) + debug5.min(Direction.Axis.Y);
/*     */       }
/*  76 */       debug4++;
/*  77 */       debug3.move(Direction.UP);
/*     */     } 
/*  79 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 findSafeDismountLocation(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2, boolean debug3) {
/*  84 */     if (debug3 && debug0.isBlockDangerous(debug1.getBlockState(debug2))) {
/*  85 */       return null;
/*     */     }
/*     */     
/*  88 */     double debug4 = debug1.getBlockFloorHeight(nonClimbableShape((BlockGetter)debug1, debug2), () -> nonClimbableShape((BlockGetter)debug0, debug1.below()));
/*  89 */     if (!isBlockFloorValid(debug4)) {
/*  90 */       return null;
/*     */     }
/*     */     
/*  93 */     if (debug3 && debug4 <= 0.0D && debug0.isBlockDangerous(debug1.getBlockState(debug2.below()))) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     Vec3 debug6 = Vec3.upFromBottomCenterOf((Vec3i)debug2, debug4);
/*  98 */     if (debug1.getBlockCollisions(null, debug0.getDimensions().makeBoundingBox(debug6)).allMatch(VoxelShape::isEmpty)) {
/*  99 */       return debug6;
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\DismountHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */