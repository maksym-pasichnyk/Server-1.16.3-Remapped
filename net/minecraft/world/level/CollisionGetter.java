/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CollisionGetter
/*    */   extends BlockGetter
/*    */ {
/*    */   default boolean isUnobstructed(@Nullable Entity debug1, VoxelShape debug2) {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   default boolean isUnobstructed(BlockState debug1, BlockPos debug2, CollisionContext debug3) {
/* 29 */     VoxelShape debug4 = debug1.getCollisionShape(this, debug2, debug3);
/* 30 */     return (debug4.isEmpty() || isUnobstructed(null, debug4.move(debug2.getX(), debug2.getY(), debug2.getZ())));
/*    */   }
/*    */   
/*    */   default boolean isUnobstructed(Entity debug1) {
/* 34 */     return isUnobstructed(debug1, Shapes.create(debug1.getBoundingBox()));
/*    */   }
/*    */   
/*    */   default boolean noCollision(AABB debug1) {
/* 38 */     return noCollision(null, debug1, debug0 -> true);
/*    */   }
/*    */   
/*    */   default boolean noCollision(Entity debug1) {
/* 42 */     return noCollision(debug1, debug1.getBoundingBox(), debug0 -> true);
/*    */   }
/*    */   
/*    */   default boolean noCollision(Entity debug1, AABB debug2) {
/* 46 */     return noCollision(debug1, debug2, debug0 -> true);
/*    */   }
/*    */   
/*    */   default boolean noCollision(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/* 50 */     return getCollisions(debug1, debug2, debug3).allMatch(VoxelShape::isEmpty);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default Stream<VoxelShape> getCollisions(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/* 56 */     return Stream.concat(
/* 57 */         getBlockCollisions(debug1, debug2), 
/* 58 */         getEntityCollisions(debug1, debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   default Stream<VoxelShape> getBlockCollisions(@Nullable Entity debug1, AABB debug2) {
/* 63 */     return StreamSupport.stream(new CollisionSpliterator(this, debug1, debug2), false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Stream<VoxelShape> getBlockCollisions(@Nullable Entity debug1, AABB debug2, BiPredicate<BlockState, BlockPos> debug3) {
/* 71 */     return StreamSupport.stream(new CollisionSpliterator(this, debug1, debug2, debug3), false);
/*    */   }
/*    */   
/*    */   WorldBorder getWorldBorder();
/*    */   
/*    */   @Nullable
/*    */   BlockGetter getChunkForCollisions(int paramInt1, int paramInt2);
/*    */   
/*    */   Stream<VoxelShape> getEntityCollisions(@Nullable Entity paramEntity, AABB paramAABB, Predicate<Entity> paramPredicate);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\CollisionGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */