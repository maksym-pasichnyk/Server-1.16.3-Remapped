/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class ListPoolElement extends StructurePoolElement {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter(()), (App)projectionCodec()).apply((Applicative)debug0, ListPoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ListPoolElement> CODEC;
/*    */   private final List<StructurePoolElement> elements;
/*    */   
/*    */   public ListPoolElement(List<StructurePoolElement> debug1, StructureTemplatePool.Projection debug2) {
/* 27 */     super(debug2);
/* 28 */     if (debug1.isEmpty()) {
/* 29 */       throw new IllegalArgumentException("Elements are empty");
/*    */     }
/* 31 */     this.elements = debug1;
/* 32 */     setProjectionOnEachElement(debug2);
/*    */   }
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
/*    */   
/*    */   public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager debug1, BlockPos debug2, Rotation debug3, Random debug4) {
/* 52 */     return ((StructurePoolElement)this.elements.get(0)).getShuffledJigsawBlocks(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureManager debug1, BlockPos debug2, Rotation debug3) {
/* 57 */     BoundingBox debug4 = BoundingBox.getUnknownBox();
/* 58 */     for (StructurePoolElement debug6 : this.elements) {
/* 59 */       BoundingBox debug7 = debug6.getBoundingBox(debug1, debug2, debug3);
/* 60 */       debug4.expand(debug7);
/*    */     } 
/*    */     
/* 63 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureManager debug1, WorldGenLevel debug2, StructureFeatureManager debug3, ChunkGenerator debug4, BlockPos debug5, BlockPos debug6, Rotation debug7, BoundingBox debug8, Random debug9, boolean debug10) {
/* 68 */     for (StructurePoolElement debug12 : this.elements) {
/* 69 */       if (!debug12.place(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8, debug9, debug10)) {
/* 70 */         return false;
/*    */       }
/*    */     } 
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 78 */     return StructurePoolElementType.LIST;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElement setProjection(StructureTemplatePool.Projection debug1) {
/* 83 */     super.setProjection(debug1);
/* 84 */     setProjectionOnEachElement(debug1);
/* 85 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return "List[" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
/*    */   }
/*    */   
/*    */   private void setProjectionOnEachElement(StructureTemplatePool.Projection debug1) {
/* 94 */     this.elements.forEach(debug1 -> debug1.setProjection(debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\ListPoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */