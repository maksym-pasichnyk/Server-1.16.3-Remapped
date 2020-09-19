/*   */ package net.minecraft.world.level.biome;
/*   */ 
/*   */ public enum FuzzyOffsetConstantColumnBiomeZoomer implements BiomeZoomer {
/* 4 */   INSTANCE;
/*   */ 
/*   */   
/*   */   public Biome getBiome(long debug1, int debug3, int debug4, int debug5, BiomeManager.NoiseBiomeSource debug6) {
/* 8 */     return FuzzyOffsetBiomeZoomer.INSTANCE.getBiome(debug1, debug3, 0, debug5, debug6);
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\FuzzyOffsetConstantColumnBiomeZoomer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */