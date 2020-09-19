/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class ImposterProtoChunk extends ProtoChunk {
/*     */   private final LevelChunk wrapped;
/*     */   
/*     */   public ImposterProtoChunk(LevelChunk debug1) {
/*  30 */     super(debug1.getPos(), UpgradeData.EMPTY);
/*     */     
/*  32 */     this.wrapped = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1) {
/*  38 */     return this.wrapped.getBlockEntity(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getBlockState(BlockPos debug1) {
/*  44 */     return this.wrapped.getBlockState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/*  49 */     return this.wrapped.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxLightLevel() {
/*  54 */     return this.wrapped.getMaxLightLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState setBlockState(BlockPos debug1, BlockState debug2, boolean debug3) {
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockPos debug1, BlockEntity debug2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEntity(Entity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(ChunkStatus debug1) {}
/*     */ 
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/*  77 */     return this.wrapped.getSections();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelLightEngine getLightEngine() {
/*  83 */     return this.wrapped.getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightmap(Heightmap.Types debug1, long[] debug2) {}
/*     */ 
/*     */   
/*     */   private Heightmap.Types fixType(Heightmap.Types debug1) {
/*  91 */     if (debug1 == Heightmap.Types.WORLD_SURFACE_WG) {
/*  92 */       return Heightmap.Types.WORLD_SURFACE;
/*     */     }
/*     */     
/*  95 */     if (debug1 == Heightmap.Types.OCEAN_FLOOR_WG) {
/*  96 */       return Heightmap.Types.OCEAN_FLOOR;
/*     */     }
/*     */     
/*  99 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/* 104 */     return this.wrapped.getHeight(fixType(debug1), debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkPos getPos() {
/* 114 */     return this.wrapped.getPos();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastSaveTime(long debug1) {}
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public StructureStart<?> getStartForFeature(StructureFeature<?> debug1) {
/* 124 */     return this.wrapped.getStartForFeature(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartForFeature(StructureFeature<?> debug1, StructureStart<?> debug2) {}
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
/* 133 */     return this.wrapped.getAllStarts();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<StructureFeature<?>, StructureStart<?>> debug1) {}
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForFeature(StructureFeature<?> debug1) {
/* 142 */     return this.wrapped.getReferencesForFeature(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReferenceForFeature(StructureFeature<?> debug1, long debug2) {}
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, LongSet> getAllReferences() {
/* 151 */     return this.wrapped.getAllReferences();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<StructureFeature<?>, LongSet> debug1) {}
/*     */ 
/*     */   
/*     */   public ChunkBiomeContainer getBiomes() {
/* 160 */     return this.wrapped.getBiomes();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnsaved(boolean debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnsaved() {
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getStatus() {
/* 175 */     return this.wrapped.getStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag debug1) {}
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getBlockEntityNbt(BlockPos debug1) {
/* 193 */     return this.wrapped.getBlockEntityNbt(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos debug1) {
/* 199 */     return this.wrapped.getBlockEntityNbtForSaving(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBiomes(ChunkBiomeContainer debug1) {}
/*     */ 
/*     */   
/*     */   public Stream<BlockPos> getLights() {
/* 208 */     return this.wrapped.getLights();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoTickList<Block> getBlockTicks() {
/* 213 */     return new ProtoTickList<>(debug0 -> debug0.defaultBlockState().isAir(), getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoTickList<Fluid> getLiquidTicks() {
/* 218 */     return new ProtoTickList<>(debug0 -> (debug0 == Fluids.EMPTY), getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public BitSet getCarvingMask(GenerationStep.Carving debug1) {
/* 223 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */ 
/*     */   
/*     */   public BitSet getOrCreateCarvingMask(GenerationStep.Carving debug1) {
/* 228 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */   
/*     */   public LevelChunk getWrapped() {
/* 232 */     return this.wrapped;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLightCorrect() {
/* 237 */     return this.wrapped.isLightCorrect();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean debug1) {
/* 242 */     this.wrapped.setLightCorrect(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ImposterProtoChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */