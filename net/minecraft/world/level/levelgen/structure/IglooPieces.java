/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ public class IglooPieces
/*     */ {
/*  34 */   private static final ResourceLocation STRUCTURE_LOCATION_IGLOO = new ResourceLocation("igloo/top");
/*  35 */   private static final ResourceLocation STRUCTURE_LOCATION_LADDER = new ResourceLocation("igloo/middle");
/*  36 */   private static final ResourceLocation STRUCTURE_LOCATION_LABORATORY = new ResourceLocation("igloo/bottom");
/*     */   
/*  38 */   private static final Map<ResourceLocation, BlockPos> PIVOTS = (Map<ResourceLocation, BlockPos>)ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static final Map<ResourceLocation, BlockPos> OFFSETS = (Map<ResourceLocation, BlockPos>)ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPieces(StructureManager debug0, BlockPos debug1, Rotation debug2, List<StructurePiece> debug3, Random debug4) {
/*  51 */     if (debug4.nextDouble() < 0.5D) {
/*  52 */       int debug5 = debug4.nextInt(8) + 4;
/*  53 */       debug3.add(new IglooPiece(debug0, STRUCTURE_LOCATION_LABORATORY, debug1, debug2, debug5 * 3));
/*  54 */       for (int debug6 = 0; debug6 < debug5 - 1; debug6++) {
/*  55 */         debug3.add(new IglooPiece(debug0, STRUCTURE_LOCATION_LADDER, debug1, debug2, debug6 * 3));
/*     */       }
/*     */     } 
/*     */     
/*  59 */     debug3.add(new IglooPiece(debug0, STRUCTURE_LOCATION_IGLOO, debug1, debug2, 0));
/*     */   }
/*     */   
/*     */   public static class IglooPiece extends TemplateStructurePiece {
/*     */     private final ResourceLocation templateLocation;
/*     */     private final Rotation rotation;
/*     */     
/*     */     public IglooPiece(StructureManager debug1, ResourceLocation debug2, BlockPos debug3, Rotation debug4, int debug5) {
/*  67 */       super(StructurePieceType.IGLOO, 0);
/*  68 */       this.templateLocation = debug2;
/*  69 */       BlockPos debug6 = (BlockPos)IglooPieces.OFFSETS.get(debug2);
/*  70 */       this.templatePosition = debug3.offset(debug6.getX(), debug6.getY() - debug5, debug6.getZ());
/*  71 */       this.rotation = debug4;
/*  72 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     public IglooPiece(StructureManager debug1, CompoundTag debug2) {
/*  76 */       super(StructurePieceType.IGLOO, debug2);
/*  77 */       this.templateLocation = new ResourceLocation(debug2.getString("Template"));
/*  78 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/*  79 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     private void loadTemplate(StructureManager debug1) {
/*  83 */       StructureTemplate debug2 = debug1.getOrCreate(this.templateLocation);
/*  84 */       StructurePlaceSettings debug3 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot((BlockPos)IglooPieces.PIVOTS.get(this.templateLocation)).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*  85 */       setup(debug2, this.templatePosition, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  90 */       super.addAdditionalSaveData(debug1);
/*  91 */       debug1.putString("Template", this.templateLocation.toString());
/*  92 */       debug1.putString("Rot", this.rotation.name());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {
/*  97 */       if (!"chest".equals(debug1)) {
/*     */         return;
/*     */       }
/*     */       
/* 101 */       debug3.setBlock(debug2, Blocks.AIR.defaultBlockState(), 3);
/* 102 */       BlockEntity debug6 = debug3.getBlockEntity(debug2.below());
/* 103 */       if (debug6 instanceof ChestBlockEntity) {
/* 104 */         ((ChestBlockEntity)debug6).setLootTable(BuiltInLootTables.IGLOO_CHEST, debug4.nextLong());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 110 */       StructurePlaceSettings debug8 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot((BlockPos)IglooPieces.PIVOTS.get(this.templateLocation)).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*     */       
/* 112 */       BlockPos debug9 = (BlockPos)IglooPieces.OFFSETS.get(this.templateLocation);
/* 113 */       BlockPos debug10 = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(debug8, new BlockPos(3 - debug9.getX(), 0, 0 - debug9.getZ())));
/* 114 */       int debug11 = debug1.getHeight(Heightmap.Types.WORLD_SURFACE_WG, debug10.getX(), debug10.getZ());
/* 115 */       BlockPos debug12 = this.templatePosition;
/* 116 */       this.templatePosition = this.templatePosition.offset(0, debug11 - 90 - 1, 0);
/*     */       
/* 118 */       boolean debug13 = super.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */       
/* 120 */       if (this.templateLocation.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
/* 121 */         BlockPos debug14 = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(debug8, new BlockPos(3, 0, 5)));
/* 122 */         BlockState debug15 = debug1.getBlockState(debug14.below());
/* 123 */         if (!debug15.isAir() && !debug15.is(Blocks.LADDER)) {
/* 124 */           debug1.setBlock(debug14, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
/*     */         }
/*     */       } 
/*     */       
/* 128 */       this.templatePosition = debug12;
/* 129 */       return debug13;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\IglooPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */