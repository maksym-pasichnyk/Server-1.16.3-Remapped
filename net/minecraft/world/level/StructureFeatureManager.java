/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.WorldGenRegion;
/*    */ import net.minecraft.world.level.chunk.ChunkStatus;
/*    */ import net.minecraft.world.level.chunk.FeatureAccess;
/*    */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ 
/*    */ public class StructureFeatureManager
/*    */ {
/*    */   private final LevelAccessor level;
/*    */   private final WorldGenSettings worldGenSettings;
/*    */   
/*    */   public StructureFeatureManager(LevelAccessor debug1, WorldGenSettings debug2) {
/* 23 */     this.level = debug1;
/* 24 */     this.worldGenSettings = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeatureManager forWorldGenRegion(WorldGenRegion debug1) {
/* 29 */     if (debug1.getLevel() != this.level) {
/* 30 */       throw new IllegalStateException("Using invalid feature manager (source level: " + debug1.getLevel() + ", region: " + debug1);
/*    */     }
/* 32 */     return new StructureFeatureManager((LevelAccessor)debug1, this.worldGenSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<? extends StructureStart<?>> startsForFeature(SectionPos debug1, StructureFeature<?> debug2) {
/* 37 */     return this.level.getChunk(debug1.x(), debug1.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForFeature(debug2)
/* 38 */       .stream()
/* 39 */       .map(debug0 -> SectionPos.of(new ChunkPos(debug0.longValue()), 0))
/* 40 */       .map(debug2 -> getStartForFeature(debug2, debug1, (FeatureAccess)this.level.getChunk(debug2.x(), debug2.z(), ChunkStatus.STRUCTURE_STARTS)))
/* 41 */       .filter(debug0 -> (debug0 != null && debug0.isValid()));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public StructureStart<?> getStartForFeature(SectionPos debug1, StructureFeature<?> debug2, FeatureAccess debug3) {
/* 46 */     return debug3.getStartForFeature(debug2);
/*    */   }
/*    */   
/*    */   public void setStartForFeature(SectionPos debug1, StructureFeature<?> debug2, StructureStart<?> debug3, FeatureAccess debug4) {
/* 50 */     debug4.setStartForFeature(debug2, debug3);
/*    */   }
/*    */   
/*    */   public void addReferenceForFeature(SectionPos debug1, StructureFeature<?> debug2, long debug3, FeatureAccess debug5) {
/* 54 */     debug5.addReferenceForFeature(debug2, debug3);
/*    */   }
/*    */   
/*    */   public boolean shouldGenerateFeatures() {
/* 58 */     return this.worldGenSettings.generateFeatures();
/*    */   }
/*    */   
/*    */   public StructureStart<?> getStructureAt(BlockPos debug1, boolean debug2, StructureFeature<?> debug3) {
/* 62 */     return (StructureStart)DataFixUtils.orElse(startsForFeature(SectionPos.of(debug1), debug3)
/* 63 */         .filter(debug1 -> debug1.getBoundingBox().isInside((Vec3i)debug0))
/* 64 */         .filter(debug2 -> (!debug0 || debug2.getPieces().stream().anyMatch(())))
/* 65 */         .findFirst(), StructureStart.INVALID_START);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\StructureFeatureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */