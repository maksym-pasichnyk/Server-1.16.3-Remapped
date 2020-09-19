/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class ShipwreckPieces {
/*  29 */   private static final BlockPos PIVOT = new BlockPos(4, 0, 15);
/*     */   
/*  31 */   private static final ResourceLocation[] STRUCTURE_LOCATION_BEACHED = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final ResourceLocation[] STRUCTURE_LOCATION_OCEAN = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/upsidedown_full"), new ResourceLocation("shipwreck/upsidedown_fronthalf"), new ResourceLocation("shipwreck/upsidedown_backhalf"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/upsidedown_full_degraded"), new ResourceLocation("shipwreck/upsidedown_fronthalf_degraded"), new ResourceLocation("shipwreck/upsidedown_backhalf_degraded"), new ResourceLocation("shipwreck/sideways_full_degraded"), new ResourceLocation("shipwreck/sideways_fronthalf_degraded"), new ResourceLocation("shipwreck/sideways_backhalf_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPieces(StructureManager debug0, BlockPos debug1, Rotation debug2, List<StructurePiece> debug3, Random debug4, ShipwreckConfiguration debug5) {
/*  69 */     ResourceLocation debug6 = (ResourceLocation)Util.getRandom(debug5.isBeached ? (Object[])STRUCTURE_LOCATION_BEACHED : (Object[])STRUCTURE_LOCATION_OCEAN, debug4);
/*  70 */     debug3.add(new ShipwreckPiece(debug0, debug6, debug1, debug2, debug5.isBeached));
/*     */   }
/*     */   
/*     */   public static class ShipwreckPiece extends TemplateStructurePiece {
/*     */     private final Rotation rotation;
/*     */     private final ResourceLocation templateLocation;
/*     */     private final boolean isBeached;
/*     */     
/*     */     public ShipwreckPiece(StructureManager debug1, ResourceLocation debug2, BlockPos debug3, Rotation debug4, boolean debug5) {
/*  79 */       super(StructurePieceType.SHIPWRECK_PIECE, 0);
/*     */       
/*  81 */       this.templatePosition = debug3;
/*  82 */       this.rotation = debug4;
/*  83 */       this.templateLocation = debug2;
/*  84 */       this.isBeached = debug5;
/*  85 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     public ShipwreckPiece(StructureManager debug1, CompoundTag debug2) {
/*  89 */       super(StructurePieceType.SHIPWRECK_PIECE, debug2);
/*  90 */       this.templateLocation = new ResourceLocation(debug2.getString("Template"));
/*  91 */       this.isBeached = debug2.getBoolean("isBeached");
/*  92 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/*  93 */       loadTemplate(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  98 */       super.addAdditionalSaveData(debug1);
/*  99 */       debug1.putString("Template", this.templateLocation.toString());
/* 100 */       debug1.putBoolean("isBeached", this.isBeached);
/* 101 */       debug1.putString("Rot", this.rotation.name());
/*     */     }
/*     */     
/*     */     private void loadTemplate(StructureManager debug1) {
/* 105 */       StructureTemplate debug2 = debug1.getOrCreate(this.templateLocation);
/* 106 */       StructurePlaceSettings debug3 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 107 */       setup(debug2, this.templatePosition, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {
/* 112 */       if ("map_chest".equals(debug1)) {
/* 113 */         RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug3, debug4, debug2.below(), BuiltInLootTables.SHIPWRECK_MAP);
/* 114 */       } else if ("treasure_chest".equals(debug1)) {
/* 115 */         RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug3, debug4, debug2.below(), BuiltInLootTables.SHIPWRECK_TREASURE);
/* 116 */       } else if ("supply_chest".equals(debug1)) {
/* 117 */         RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug3, debug4, debug2.below(), BuiltInLootTables.SHIPWRECK_SUPPLY);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 123 */       int debug8 = 256;
/* 124 */       int debug9 = 0;
/* 125 */       BlockPos debug10 = this.template.getSize();
/* 126 */       Heightmap.Types debug11 = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 127 */       int debug12 = debug10.getX() * debug10.getZ();
/* 128 */       if (debug12 == 0) {
/* 129 */         debug9 = debug1.getHeight(debug11, this.templatePosition.getX(), this.templatePosition.getZ());
/*     */       } else {
/* 131 */         BlockPos blockPos = this.templatePosition.offset(debug10.getX() - 1, 0, debug10.getZ() - 1);
/* 132 */         for (BlockPos debug15 : BlockPos.betweenClosed(this.templatePosition, blockPos)) {
/* 133 */           int debug16 = debug1.getHeight(debug11, debug15.getX(), debug15.getZ());
/* 134 */           debug9 += debug16;
/* 135 */           debug8 = Math.min(debug8, debug16);
/*     */         } 
/* 137 */         debug9 /= debug12;
/*     */       } 
/*     */       
/* 140 */       int debug13 = this.isBeached ? (debug8 - debug10.getY() / 2 - debug4.nextInt(3)) : debug9;
/* 141 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), debug13, this.templatePosition.getZ());
/* 142 */       return super.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\ShipwreckPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */