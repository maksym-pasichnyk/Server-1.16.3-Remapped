/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ 
/*     */ public interface ChunkAccess
/*     */   extends BlockGetter, FeatureAccess
/*     */ {
/*     */   @Nullable
/*     */   BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean);
/*     */   
/*     */   void setBlockEntity(BlockPos paramBlockPos, BlockEntity paramBlockEntity);
/*     */   
/*     */   void addEntity(Entity paramEntity);
/*     */   
/*     */   @Nullable
/*     */   default LevelChunkSection getHighestSection() {
/*  38 */     LevelChunkSection[] debug1 = getSections();
/*  39 */     for (int debug2 = debug1.length - 1; debug2 >= 0; debug2--) {
/*  40 */       LevelChunkSection debug3 = debug1[debug2];
/*  41 */       if (!LevelChunkSection.isEmpty(debug3)) {
/*  42 */         return debug3;
/*     */       }
/*     */     } 
/*  45 */     return null;
/*     */   }
/*     */   
/*     */   default int getHighestSectionPosition() {
/*  49 */     LevelChunkSection debug1 = getHighestSection();
/*  50 */     return (debug1 == null) ? 0 : debug1.bottomBlockY();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<BlockPos> getBlockEntitiesPos();
/*     */ 
/*     */   
/*     */   LevelChunkSection[] getSections();
/*     */ 
/*     */   
/*     */   Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps();
/*     */ 
/*     */   
/*     */   void setHeightmap(Heightmap.Types paramTypes, long[] paramArrayOflong);
/*     */ 
/*     */   
/*     */   Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types paramTypes);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*     */   ChunkPos getPos();
/*     */   
/*     */   void setLastSaveTime(long paramLong);
/*     */   
/*     */   Map<StructureFeature<?>, StructureStart<?>> getAllStarts();
/*     */   
/*     */   void setAllStarts(Map<StructureFeature<?>, StructureStart<?>> paramMap);
/*     */   
/*     */   default boolean isYSpaceEmpty(int debug1, int debug2) {
/*  79 */     if (debug1 < 0) {
/*  80 */       debug1 = 0;
/*     */     }
/*  82 */     if (debug2 >= 256) {
/*  83 */       debug2 = 255;
/*     */     }
/*  85 */     for (int debug3 = debug1; debug3 <= debug2; debug3 += 16) {
/*  86 */       if (!LevelChunkSection.isEmpty(getSections()[debug3 >> 4])) {
/*  87 */         return false;
/*     */       }
/*     */     } 
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   ChunkBiomeContainer getBiomes();
/*     */   
/*     */   void setUnsaved(boolean paramBoolean);
/*     */   
/*     */   boolean isUnsaved();
/*     */   
/*     */   ChunkStatus getStatus();
/*     */   
/*     */   void removeBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   default void markPosForPostprocessing(BlockPos debug1) {
/* 105 */     LogManager.getLogger().warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", debug1);
/*     */   }
/*     */   
/*     */   ShortList[] getPostProcessing();
/*     */   
/*     */   default void addPackedPostProcess(short debug1, int debug2) {
/* 111 */     getOrCreateOffsetList(getPostProcessing(), debug2).add(debug1);
/*     */   }
/*     */   
/*     */   default void setBlockEntityNbt(CompoundTag debug1) {
/* 115 */     LogManager.getLogger().warn("Trying to set a BlockEntity, but this operation is not supported.");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   CompoundTag getBlockEntityNbt(BlockPos paramBlockPos);
/*     */   
/*     */   @Nullable
/*     */   CompoundTag getBlockEntityNbtForSaving(BlockPos paramBlockPos);
/*     */   
/*     */   Stream<BlockPos> getLights();
/*     */   
/*     */   TickList<Block> getBlockTicks();
/*     */   
/*     */   TickList<Fluid> getLiquidTicks();
/*     */   
/*     */   UpgradeData getUpgradeData();
/*     */   
/*     */   void setInhabitedTime(long paramLong);
/*     */   
/*     */   long getInhabitedTime();
/*     */   
/*     */   static ShortList getOrCreateOffsetList(ShortList[] debug0, int debug1) {
/* 137 */     if (debug0[debug1] == null) {
/* 138 */       debug0[debug1] = (ShortList)new ShortArrayList();
/*     */     }
/* 140 */     return debug0[debug1];
/*     */   }
/*     */   
/*     */   boolean isLightCorrect();
/*     */   
/*     */   void setLightCorrect(boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ChunkAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */