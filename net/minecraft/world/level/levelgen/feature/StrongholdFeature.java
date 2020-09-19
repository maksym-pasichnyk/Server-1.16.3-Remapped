/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StrongholdPieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class StrongholdFeature extends StructureFeature<NoneFeatureConfiguration> {
/*    */   public StrongholdFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 26 */     return StrongholdStart::new;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, NoneFeatureConfiguration debug10) {
/* 31 */     return debug1.hasStronghold(new ChunkPos(debug6, debug7));
/*    */   }
/*    */   
/*    */   public static class StrongholdStart extends StructureStart<NoneFeatureConfiguration> {
/*    */     private final long seed;
/*    */     
/*    */     public StrongholdStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 38 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/* 39 */       this.seed = debug6;
/*    */     }
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/*    */       StrongholdPieces.StartPiece debug9;
/* 44 */       int debug8 = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       do {
/* 50 */         this.pieces.clear();
/* 51 */         this.boundingBox = BoundingBox.getUnknownBox();
/* 52 */         this.random.setLargeFeatureSeed(this.seed + debug8++, debug4, debug5);
/* 53 */         StrongholdPieces.resetPieces();
/*    */         
/* 55 */         debug9 = new StrongholdPieces.StartPiece((Random)this.random, (debug4 << 4) + 2, (debug5 << 4) + 2);
/* 56 */         this.pieces.add(debug9);
/* 57 */         debug9.addChildren((StructurePiece)debug9, this.pieces, (Random)this.random);
/*    */         
/* 59 */         List<StructurePiece> debug10 = debug9.pendingChildren;
/* 60 */         while (!debug10.isEmpty()) {
/* 61 */           int debug11 = this.random.nextInt(debug10.size());
/* 62 */           StructurePiece debug12 = debug10.remove(debug11);
/* 63 */           debug12.addChildren((StructurePiece)debug9, this.pieces, (Random)this.random);
/*    */         } 
/*    */         
/* 66 */         calculateBoundingBox();
/* 67 */         moveBelowSeaLevel(debug2.getSeaLevel(), (Random)this.random, 10);
/*    */       }
/* 69 */       while (this.pieces.isEmpty() || debug9.portalRoomPiece == null);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\StrongholdFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */