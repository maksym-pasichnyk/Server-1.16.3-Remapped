/*     */ package net.minecraft.world.level.levelgen.feature.structures;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.data.worldgen.ProcessorLists;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ public class SinglePoolElement extends StructurePoolElement {
/*     */   private static <T> DataResult<T> encodeTemplate(Either<ResourceLocation, StructureTemplate> debug0, DynamicOps<T> debug1, T debug2) {
/*  37 */     Optional<ResourceLocation> debug3 = debug0.left();
/*  38 */     if (!debug3.isPresent()) {
/*  39 */       return DataResult.error("Can not serialize a runtime pool element");
/*     */     }
/*  41 */     return ResourceLocation.CODEC.encode(debug3.get(), debug1, debug2);
/*     */   }
/*     */   
/*  44 */   private static final Codec<Either<ResourceLocation, StructureTemplate>> TEMPLATE_CODEC = Codec.of(SinglePoolElement::encodeTemplate, ResourceLocation.CODEC
/*     */       
/*  46 */       .map(Either::left)); public static final Codec<SinglePoolElement> CODEC; protected final Either<ResourceLocation, StructureTemplate> template; protected final Supplier<StructureProcessorList> processors;
/*     */   
/*     */   static {
/*  49 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec()).apply((Applicative)debug0, SinglePoolElement::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Supplier<StructureProcessorList>> processorsCodec() {
/*  56 */     return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(debug0 -> debug0.processors);
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Either<ResourceLocation, StructureTemplate>> templateCodec() {
/*  60 */     return TEMPLATE_CODEC.fieldOf("location").forGetter(debug0 -> debug0.template);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SinglePoolElement(Either<ResourceLocation, StructureTemplate> debug1, Supplier<StructureProcessorList> debug2, StructureTemplatePool.Projection debug3) {
/*  67 */     super(debug3);
/*  68 */     this.template = debug1;
/*  69 */     this.processors = debug2;
/*     */   }
/*     */   
/*     */   public SinglePoolElement(StructureTemplate debug1) {
/*  73 */     this(Either.right(debug1), () -> ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StructureTemplate getTemplate(StructureManager debug1) {
/*  83 */     return (StructureTemplate)this.template.map(debug1::getOrCreate, Function.identity());
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureManager debug1, BlockPos debug2, Rotation debug3, boolean debug4) {
/*  87 */     StructureTemplate debug5 = getTemplate(debug1);
/*  88 */     List<StructureTemplate.StructureBlockInfo> debug6 = debug5.filterBlocks(debug2, (new StructurePlaceSettings()).setRotation(debug3), Blocks.STRUCTURE_BLOCK, debug4);
/*  89 */     List<StructureTemplate.StructureBlockInfo> debug7 = Lists.newArrayList();
/*  90 */     for (StructureTemplate.StructureBlockInfo debug9 : debug6) {
/*  91 */       if (debug9.nbt == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  95 */       StructureMode debug10 = StructureMode.valueOf(debug9.nbt.getString("mode"));
/*  96 */       if (debug10 != StructureMode.DATA) {
/*     */         continue;
/*     */       }
/*     */       
/* 100 */       debug7.add(debug9);
/*     */     } 
/*     */     
/* 103 */     return debug7;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager debug1, BlockPos debug2, Rotation debug3, Random debug4) {
/* 108 */     StructureTemplate debug5 = getTemplate(debug1);
/* 109 */     List<StructureTemplate.StructureBlockInfo> debug6 = debug5.filterBlocks(debug2, (new StructurePlaceSettings()).setRotation(debug3), Blocks.JIGSAW, true);
/* 110 */     Collections.shuffle(debug6, debug4);
/* 111 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureManager debug1, BlockPos debug2, Rotation debug3) {
/* 116 */     StructureTemplate debug4 = getTemplate(debug1);
/* 117 */     return debug4.getBoundingBox((new StructurePlaceSettings()).setRotation(debug3), debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(StructureManager debug1, WorldGenLevel debug2, StructureFeatureManager debug3, ChunkGenerator debug4, BlockPos debug5, BlockPos debug6, Rotation debug7, BoundingBox debug8, Random debug9, boolean debug10) {
/* 122 */     StructureTemplate debug11 = getTemplate(debug1);
/* 123 */     StructurePlaceSettings debug12 = getSettings(debug7, debug8, debug10);
/*     */     
/* 125 */     if (debug11.placeInWorld((ServerLevelAccessor)debug2, debug5, debug6, debug12, debug9, 18)) {
/* 126 */       List<StructureTemplate.StructureBlockInfo> debug13 = StructureTemplate.processBlockInfos((LevelAccessor)debug2, debug5, debug6, debug12, getDataMarkers(debug1, debug5, debug7, false));
/* 127 */       for (StructureTemplate.StructureBlockInfo debug15 : debug13) {
/* 128 */         handleDataMarker((LevelAccessor)debug2, debug15, debug5, debug7, debug9, debug8);
/*     */       }
/*     */       
/* 131 */       return true;
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   protected StructurePlaceSettings getSettings(Rotation debug1, BoundingBox debug2, boolean debug3) {
/* 137 */     StructurePlaceSettings debug4 = new StructurePlaceSettings();
/* 138 */     debug4.setBoundingBox(debug2);
/* 139 */     debug4.setRotation(debug1);
/* 140 */     debug4.setKnownShape(true);
/* 141 */     debug4.setIgnoreEntities(false);
/* 142 */     debug4.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 143 */     debug4.setFinalizeEntities(true);
/* 144 */     if (!debug3) {
/* 145 */       debug4.addProcessor((StructureProcessor)JigsawReplacementProcessor.INSTANCE);
/*     */     }
/* 147 */     ((StructureProcessorList)this.processors.get()).list().forEach(debug4::addProcessor);
/* 148 */     getProjection().getProcessors().forEach(debug4::addProcessor);
/* 149 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElementType<?> getType() {
/* 154 */     return StructurePoolElementType.SINGLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     return "Single[" + this.template + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\SinglePoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */