/*     */ package net.minecraft.world.level.chunk;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.worldgen.biome.Biomes;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class EmptyLevelChunk extends LevelChunk {
/*     */   static {
/*  26 */     BIOMES = (Biome[])Util.make(new Biome[ChunkBiomeContainer.BIOMES_SIZE], debug0 -> Arrays.fill((Object[])debug0, Biomes.PLAINS));
/*     */   } private static final Biome[] BIOMES;
/*     */   public EmptyLevelChunk(Level debug1, ChunkPos debug2) {
/*  29 */     super(debug1, debug2, new ChunkBiomeContainer((IdMap<Biome>)debug1.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), BIOMES));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos debug1) {
/*  34 */     return Blocks.VOID_AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState setBlockState(BlockPos debug1, BlockState debug2, boolean debug3) {
/*  40 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/*  45 */     return Fluids.EMPTY.defaultFluidState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelLightEngine getLightEngine() {
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightEmission(BlockPos debug1) {
/*  61 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEntity(Entity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEntity(Entity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEntity(Entity debug1, int debug2) {}
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1, LevelChunk.EntityCreationType debug2) {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBlockEntity(BlockEntity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockPos debug1, BlockEntity debug2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void markUnsaved() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void getEntities(@Nullable Entity debug1, AABB debug2, List<Entity> debug3, Predicate<? super Entity> debug4) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Entity> void getEntitiesOfClass(Class<? extends T> debug1, AABB debug2, List<T> debug3, Predicate<? super T> debug4) {}
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isYSpaceEmpty(int debug1, int debug2) {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkHolder.FullChunkStatus getFullStatus() {
/* 118 */     return ChunkHolder.FullChunkStatus.BORDER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\EmptyLevelChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */