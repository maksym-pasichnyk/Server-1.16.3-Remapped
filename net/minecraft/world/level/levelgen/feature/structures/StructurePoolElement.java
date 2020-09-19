/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.ProcessorLists;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public abstract class StructurePoolElement
/*    */ {
/* 28 */   public static final Codec<StructurePoolElement> CODEC = Registry.STRUCTURE_POOL_ELEMENT.dispatch("element_type", StructurePoolElement::getType, StructurePoolElementType::codec);
/*    */   
/*    */   protected static <E extends StructurePoolElement> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
/* 31 */     return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(StructurePoolElement::getProjection);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private volatile StructureTemplatePool.Projection projection;
/*    */   
/*    */   protected StructurePoolElement(StructureTemplatePool.Projection debug1) {
/* 38 */     this.projection = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleDataMarker(LevelAccessor debug1, StructureTemplate.StructureBlockInfo debug2, BlockPos debug3, Rotation debug4, Random debug5, BoundingBox debug6) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructurePoolElement setProjection(StructureTemplatePool.Projection debug1) {
/* 55 */     this.projection = debug1;
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public StructureTemplatePool.Projection getProjection() {
/* 60 */     StructureTemplatePool.Projection debug1 = this.projection;
/* 61 */     if (debug1 == null) {
/* 62 */       throw new IllegalStateException();
/*    */     }
/* 64 */     return debug1;
/*    */   }
/*    */   
/*    */   public int getGroundLevelDelta() {
/* 68 */     return 1;
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, EmptyPoolElement> empty() {
/* 72 */     return debug0 -> EmptyPoolElement.INSTANCE;
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String debug0) {
/* 76 */     return debug1 -> new LegacySinglePoolElement(Either.left(new ResourceLocation(debug0)), (), debug1);
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String debug0, StructureProcessorList debug1) {
/* 80 */     return debug2 -> new LegacySinglePoolElement(Either.left(new ResourceLocation(debug0)), (), debug2);
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String debug0) {
/* 84 */     return debug1 -> new SinglePoolElement(Either.left(new ResourceLocation(debug0)), (), debug1);
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String debug0, StructureProcessorList debug1) {
/* 88 */     return debug2 -> new SinglePoolElement(Either.left(new ResourceLocation(debug0)), (), debug2);
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, FeaturePoolElement> feature(ConfiguredFeature<?, ?> debug0) {
/* 92 */     return debug1 -> new FeaturePoolElement((), debug1);
/*    */   }
/*    */   
/*    */   public static Function<StructureTemplatePool.Projection, ListPoolElement> list(List<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> debug0) {
/* 96 */     return debug1 -> new ListPoolElement((List<StructurePoolElement>)debug0.stream().map(()).collect(Collectors.toList()), debug1);
/*    */   }
/*    */   
/*    */   public abstract List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager paramStructureManager, BlockPos paramBlockPos, Rotation paramRotation, Random paramRandom);
/*    */   
/*    */   public abstract BoundingBox getBoundingBox(StructureManager paramStructureManager, BlockPos paramBlockPos, Rotation paramRotation);
/*    */   
/*    */   public abstract boolean place(StructureManager paramStructureManager, WorldGenLevel paramWorldGenLevel, StructureFeatureManager paramStructureFeatureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, Random paramRandom, boolean paramBoolean);
/*    */   
/*    */   public abstract StructurePoolElementType<?> getType();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\StructurePoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */