/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class EmptyPoolElement
/*    */   extends StructurePoolElement {
/* 18 */   public static final Codec<EmptyPoolElement> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 20 */   public static final EmptyPoolElement INSTANCE = new EmptyPoolElement();
/*    */   
/*    */   private EmptyPoolElement() {
/* 23 */     super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager debug1, BlockPos debug2, Rotation debug3, Random debug4) {
/* 33 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureManager debug1, BlockPos debug2, Rotation debug3) {
/* 38 */     return BoundingBox.getUnknownBox();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureManager debug1, WorldGenLevel debug2, StructureFeatureManager debug3, ChunkGenerator debug4, BlockPos debug5, BlockPos debug6, Rotation debug7, BoundingBox debug8, Random debug9, boolean debug10) {
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 48 */     return StructurePoolElementType.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return "Empty";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\EmptyPoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */