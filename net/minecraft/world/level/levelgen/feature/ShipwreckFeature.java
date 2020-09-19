/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.ShipwreckPieces;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class ShipwreckFeature extends StructureFeature<ShipwreckConfiguration> {
/*    */   public ShipwreckFeature(Codec<ShipwreckConfiguration> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<ShipwreckConfiguration> getStartFactory() {
/* 22 */     return FeatureStart::new;
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends StructureStart<ShipwreckConfiguration> {
/*    */     public FeatureStart(StructureFeature<ShipwreckConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 27 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, ShipwreckConfiguration debug7) {
/* 32 */       Rotation debug8 = Rotation.getRandom((Random)this.random);
/* 33 */       BlockPos debug9 = new BlockPos(debug4 * 16, 90, debug5 * 16);
/*    */       
/* 35 */       ShipwreckPieces.addPieces(debug3, debug9, debug8, this.pieces, (Random)this.random, debug7);
/* 36 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ShipwreckFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */