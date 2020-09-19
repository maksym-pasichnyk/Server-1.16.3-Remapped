/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
/*    */ 
/*    */ public class BastionFeature
/*    */   extends JigsawFeature {
/*    */   public BastionFeature(Codec<JigsawConfiguration> debug1) {
/* 15 */     super(debug1, 33, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, JigsawConfiguration debug10) {
/* 21 */     return (debug5.nextInt(5) >= 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BastionFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */