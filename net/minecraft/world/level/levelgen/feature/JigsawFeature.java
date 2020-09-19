/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
/*    */ import net.minecraft.world.level.levelgen.structure.BeardedStructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class JigsawFeature extends StructureFeature<JigsawConfiguration> {
/*    */   private final int startY;
/*    */   
/*    */   public JigsawFeature(Codec<JigsawConfiguration> debug1, int debug2, boolean debug3, boolean debug4) {
/* 22 */     super(debug1);
/* 23 */     this.startY = debug2;
/* 24 */     this.doExpansionHack = debug3;
/* 25 */     this.projectStartToHeightmap = debug4;
/*    */   }
/*    */   private final boolean doExpansionHack; private final boolean projectStartToHeightmap;
/*    */   
/*    */   public StructureFeature.StructureStartFactory<JigsawConfiguration> getStartFactory() {
/* 30 */     return (debug1, debug2, debug3, debug4, debug5, debug6) -> new FeatureStart(this, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends BeardedStructureStart<JigsawConfiguration> {
/*    */     private final JigsawFeature feature;
/*    */     
/*    */     public FeatureStart(JigsawFeature debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 37 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/* 38 */       this.feature = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, JigsawConfiguration debug7) {
/* 43 */       BlockPos debug8 = new BlockPos(debug4 * 16, this.feature.startY, debug5 * 16);
/*    */       
/* 45 */       Pools.bootstrap();
/*    */       
/* 47 */       JigsawPlacement.addPieces(debug1, debug7, net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece::new, debug2, debug3, debug8, this.pieces, (Random)this.random, this.feature.doExpansionHack, this.feature.projectStartToHeightmap);
/*    */       
/* 49 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\JigsawFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */