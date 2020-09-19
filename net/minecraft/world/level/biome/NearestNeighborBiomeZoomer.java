/*   */ package net.minecraft.world.level.biome;
/*   */ 
/*   */ public enum NearestNeighborBiomeZoomer implements BiomeZoomer {
/* 4 */   INSTANCE;
/*   */ 
/*   */   
/*   */   public Biome getBiome(long debug1, int debug3, int debug4, int debug5, BiomeManager.NoiseBiomeSource debug6) {
/* 8 */     return debug6.getNoiseBiome(debug3 >> 2, debug4 >> 2, debug5 >> 2);
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\NearestNeighborBiomeZoomer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */