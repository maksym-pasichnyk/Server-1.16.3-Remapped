/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ public interface CommonLevelAccessor
/*    */   extends EntityGetter, LevelReader, LevelSimulatedRW
/*    */ {
/*    */   default Stream<VoxelShape> getEntityCollisions(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/* 22 */     return super.getEntityCollisions(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isUnobstructed(@Nullable Entity debug1, VoxelShape debug2) {
/* 27 */     return super.isUnobstructed(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   default BlockPos getHeightmapPos(Heightmap.Types debug1, BlockPos debug2) {
/* 32 */     return super.getHeightmapPos(debug1, debug2);
/*    */   }
/*    */   
/*    */   RegistryAccess registryAccess();
/*    */   
/*    */   default Optional<ResourceKey<Biome>> getBiomeName(BlockPos debug1) {
/* 38 */     return registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(getBiome(debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\CommonLevelAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */