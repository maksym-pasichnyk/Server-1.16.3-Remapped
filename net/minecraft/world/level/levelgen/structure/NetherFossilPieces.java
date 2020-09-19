/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class NetherFossilPieces {
/* 24 */   private static final ResourceLocation[] FOSSILS = new ResourceLocation[] { new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14") };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addPieces(StructureManager debug0, List<StructurePiece> debug1, Random debug2, BlockPos debug3) {
/* 42 */     Rotation debug4 = Rotation.getRandom(debug2);
/* 43 */     debug1.add(new NetherFossilPiece(debug0, (ResourceLocation)Util.getRandom((Object[])FOSSILS, debug2), debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class NetherFossilPiece extends TemplateStructurePiece {
/*    */     private final ResourceLocation templateLocation;
/*    */     private final Rotation rotation;
/*    */     
/*    */     public NetherFossilPiece(StructureManager debug1, ResourceLocation debug2, BlockPos debug3, Rotation debug4) {
/* 51 */       super(StructurePieceType.NETHER_FOSSIL, 0);
/* 52 */       this.templateLocation = debug2;
/* 53 */       this.templatePosition = debug3;
/* 54 */       this.rotation = debug4;
/* 55 */       loadTemplate(debug1);
/*    */     }
/*    */     
/*    */     public NetherFossilPiece(StructureManager debug1, CompoundTag debug2) {
/* 59 */       super(StructurePieceType.NETHER_FOSSIL, debug2);
/* 60 */       this.templateLocation = new ResourceLocation(debug2.getString("Template"));
/* 61 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/* 62 */       loadTemplate(debug1);
/*    */     }
/*    */     
/*    */     private void loadTemplate(StructureManager debug1) {
/* 66 */       StructureTemplate debug2 = debug1.getOrCreate(this.templateLocation);
/* 67 */       StructurePlaceSettings debug3 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 68 */       setup(debug2, this.templatePosition, debug3);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 73 */       super.addAdditionalSaveData(debug1);
/* 74 */       debug1.putString("Template", this.templateLocation.toString());
/* 75 */       debug1.putString("Rot", this.rotation.name());
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {}
/*    */ 
/*    */     
/*    */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 84 */       debug5.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
/* 85 */       return super.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\NetherFossilPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */